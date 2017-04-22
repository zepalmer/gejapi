package orioni.jz.util;

import java.io.Serializable;
import java.util.*;

/**
 * This data structure implementation is similar to a standard linked list (such as the {@link java.util.LinkedList}
 * class). However, this implementation stores the last accessed node as well as the front and back nodes under the
 * assumption that callers which access or manipulate the contents will want to handle an element with an index similar
 * to the last accessed element.
 * <p/>
 * For example, consider a case of a list with ten elements (termed <code>E00</code> through <code>E99</code>). Consider
 * a case in which one is performing a binary search and presuming the list to be sorted.  Also assume that the element
 * to be found is <code>E55</code>.  In that case, the pattern of access may be as follows:
 * <ul><code>E50<br>E75<br>E67<br>E58<br>E54<br>E56<br>E55</code></ul> In the case of a normal doubly-linked list, each
 * element must be accessed from the first or last position.  Thus, the traversal cost to perform the above operation
 * becomes: <table cols="2" border="2"> <tr><td><code>E50</code></td><td>49 steps (from end of list)</td></tr>
 * <tr><td><code>E75</code></td><td>24 steps (from end of list)</td></tr> <tr><td><code>E67</code></td><td>32 steps
 * (from end of list)</td></tr> <tr><td><code>E58</code></td><td>41 steps (from end of list)</td></tr>
 * <tr><td><code>E54</code></td><td>45 steps (from end of list)</td></tr> <tr><td><code>E56</code></td><td>43 steps
 * (from end of list)</td></tr> <tr><td><code>E55</code></td><td>44 steps (from end of list)</td></tr> <tr><td><b>Total
 * Cost:</b></td><td><b>278 steps</b></td></tr> </table> In contrast, this doubly-linked list implementation remembers
 * the last position accessed and traverses from the beginning, end, or marked list position.  Determination as to which
 * of these is most advantageous as well as storage of the marked position's location incurrs a small constant overhead.
 * However, the example above is drastically changed: <table cols="2" border="2"> <tr><td><code>E50</code></td><td>49
 * steps (from end of list)</td></tr> <tr><td><code>E75</code></td><td>24 steps (from end of list)</td></tr>
 * <tr><td><code>E67</code></td><td> 8 steps (from position 75)</td></tr> <tr><td><code>E58</code></td><td> 9 steps
 * (from position 67)</td></tr> <tr><td><code>E54</code></td><td> 4 steps (from position 58)</td></tr>
 * <tr><td><code>E56</code></td><td> 2 steps (from position 54)</td></tr> <tr><td><code>E55</code></td><td> 1 step (from
 * position 56)</td></tr> <tr><td><b>Total Cost:</b></td><td><b>97 steps</b></td></tr> </table> The quality of this
 * list's performance over a normal doubly-linked list increases with frequent access to the same region of the list,
 * especially when that region is close to the center of the list.
 * <p/>
 * Performance of this list is <i>worse</i> than that of a standard doubly-linked list for access which is essentially
 * random.  This is due to the overhead incurred by storing the last accessed position.  Additionally, some operations
 * are not affected by the behavior of this list in comparison to a normal doubly-linked list.  For example, the speed
 * of the <code>clone()</code> operation will remain the same and the speed of operations which remove items by identity
 * rather than index (such as <code>remove(Object)</code>) cannot make use of the most recently accessed index.
 * <p/>
 * <b>Note that access to this list is not synchronized.</b>  If multi-threaded access to this list is desired, one is
 * advised to use the {@link java.util.Collections#synchronizedList(java.util.List<T>)} method.
 */
public class PositionCachedLinkedList<T>
        implements Serializable, Cloneable, Iterable<T>, List<T>, Collection<T>, Queue<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The first node in the list, or <code>null</code> if the list is empty.
     */
    protected Node<T> first;
    /**
     * The last node in the list, or <code>null</code> if the list is empty.
     */
    protected Node<T> last;
    /**
     * The size of this list, stored seperately to ensure its constant speed availability.
     */
    protected int size;
    /**
     * The node in this list which was most recently accessed, or <code>null</code> if this list has no content which
     * has been accessed.
     */
    protected Node<T> recent;
    /**
     * The index of the most recently accessed node.
     */
    protected int recentIndex;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public PositionCachedLinkedList()
    {
        super();
        first = null;
        last = null;
        size = 0;
        recent = null;
        recentIndex = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an Iterator.
     */
    public Iterator<T> iterator()
    {
        return listIterator();
    }

    /**
     * Sets the most recently accessed element to the specified index, traversing as necessary.
     *
     * @param index The index to obtain.
     * @throws IndexOutOfBoundsException If the provided index is not within the bounds of this list.
     */
    private void setRecent(int index)
            throws IndexOutOfBoundsException
    {
        if ((index < 0) || (index >= size))
        {
            throw new IndexOutOfBoundsException(
                    "Provided index must be within the boundaries of this list: [0, " + size + ")");
        }
        if (size == 0) return;
        if (index < size >> 1)
        {
            if (index < recentIndex >> 1)
            {
                // The beginning is closer than the current.
                recent = first;
                recentIndex = 0;
            }
        } else
        {
            if (size - index > (index - recentIndex))
            {
                // The end is closer than the current.
                recent = last;
                recentIndex = size - 1;
            }
        }
        if (recentIndex > index)
        {
            for (int i = 0; i < recentIndex - index; i++)
            {
                recent = recent.getPrev();
            }
        } else
        {
            for (int i = 0; i < index - recentIndex; i++)
            {
                recent = recent.getNext();
            }
        }
        recentIndex = index;
    }

    /**
     * Inserts the specified element at the specified position in this list.  Shifts the element currently at that
     * position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param index   Index at which the specified element is to be inserted.
     * @param element Element to be inserted.
     * @throws ClassCastException        If the class of the specified element prevents it from being added to this
     *                                   list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    public void add(int index, T element)
    {
        if (index == 0)
        {
            if (size > 0)
            {
                setRecent(0);
                Node<T> node = new Node<T>(element, recent, null);
                recent.setPrev(node);
                first = node;
                recent = node;
            } else
            {
                first = new Node<T>(element, null, null);
                last = first;
                recent = first;
                recentIndex = 0;
            }
        } else if (index == size)
        {
            setRecent(size - 1);
            Node<T> node = new Node<T>(element, null, recent);
            recent.setNext(node);
            last = node;
            recent = node;
            recentIndex = size;
        } else
        {
            setRecent(index);
            Node<T> node = new Node<T>(element, recent, recent.getPrev());
            recent.getPrev().setNext(node);
            recent.setPrev(node);
            recent = node;
        }
        size++;
        if (size == 2)
        {
            first.setNext(last);
            last.setPrev(first);
        }
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param o element to be appended to this list.
     * @return <code>true</code> (as per the general contract of the <code>Collection.add</code> method).
     * @throws ClassCastException If the class of the specified element prevents it from being added to this list.
     */
    public boolean add(T o)
    {
        add(size, o);
        return true;
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list, in the order that they are
     * returned by the specified collection's iterator.  The behavior of this operation is unspecified if the specified
     * collection is modified while the operation is in progress.
     *
     * @param c collection whose elements are to be added to this list.
     * @return <code>true</code> if this list changed as a result of the call.
     * @throws ClassCastException if the class of an element in the specified collection prevents it from being added to
     *                            this list.
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends T> c)
    {
        boolean changed = false;
        for (T t : c)
        {
            changed = true;
            add(t);
        }
        return changed;
    }

    /**
     * Inserts all of the elements in the specified collection into this list at the specified position.  Shifts the
     * element currently at that position (if any) and any subsequent elements to the right (increases their indices).
     * The new elements will appear in this list in the order that they are returned by the specified collection's
     * iterator.  The behavior of this operation is unspecified if the specified collection is modified while the
     * operation is in progress.  (Note that this will occur if the specified collection is this list, and it's
     * nonempty.)
     *
     * @param index Index at which to insert first element from the specified collection.
     * @param c     Elements to be inserted into this list.
     * @return <code>true</code> if this list changed as a result of the call.
     * @throws UnsupportedOperationException if the <code>addAll</code> method is not supported by this list.
     * @throws ClassCastException            if the class of one of elements of the specified collection prevents it
     *                                       from being added to this list.
     * @throws IndexOutOfBoundsException     if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    public boolean addAll(int index, Collection<? extends T> c)
    {
        boolean changed = false;
        for (T t : c)
        {
            changed = true;
            add(index++, t);
        }
        return changed;
    }

    /**
     * Removes all of the elements from this list (optional operation).  This list will be empty after this call
     * returns.
     */
    public void clear()
    {
        size = 0;
        first = null;
        last = null;
        recent = null;
        recentIndex = 0;
    }

    /**
     * Returns <code>true</code> if this list contains the specified element. More formally, returns <code>true</code>
     * if and only if this list contains at least one element <code>e</code> such that
     * <code>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</code>.
     *
     * @param o element whose presence in this list is to be tested.
     * @return <code>true</code> if this list contains the specified element.
     * @throws ClassCastException if the type of the specified element is incompatible with this list.
     */
    public boolean contains(Object o)
    {
        if (o == null)
        {
            for (T t : this)
            {
                if (t == null) return true;
            }
        } else
        {
            for (T t : this)
            {
                if (o.equals(t)) return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if this list contains all of the elements of the specified collection.
     *
     * @param c collection to be checked for containment in this list.
     * @return <code>true</code> if this list contains all of the elements of the specified collection.
     * @throws ClassCastException   If the types of one or more elements in the specified collection are incompatible
     *                              with this list.
     * @throws NullPointerException If the specified collection is <code>null</code>.
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c)
    {
        boolean contains = true;
        for (Object o : c)
        {
            //noinspection SuspiciousMethodCalls
            if (!this.contains(o))
            {
                contains = false;
                break;
            }
        }
        return contains;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of element to return.
     * @return the element at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public T get(int index)
    {
        setRecent(index);
        return recent.getContent();
    }

    /**
     * Returns the index in this list of the first occurrence of the specified element, or -1 if this list does not
     * contain this element. More formally, returns the lowest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the first occurrence of the specified element, or -1 if this list does not
     *         contain this element.
     * @throws ClassCastException If the type of the specified element is incompatible with this list.
     */
    public int indexOf(Object o)
    {
        for (int i = 0; i < size; i++)
        {
            if (o.equals(get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if this list contains no elements.
     *
     * @return <code>true</code> if this list contains no elements.
     */
    public boolean isEmpty()
    {
        return (size == 0);
    }

    /**
     * Returns the index in this list of the last occurrence of the specified element, or -1 if this list does not
     * contain this element. More formally, returns the highest index <code>i</code> such that <code>(o==null ?
     * get(i)==null : o.equals(get(i)))</code>, or -1 if there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the last occurrence of the specified element, or -1 if this list does not
     *         contain this element.
     * @throws ClassCastException   if the type of the specified element is incompatible with this list (optional).
     * @throws NullPointerException if the specified element is null and this list does not support null elements
     *                              (optional).
     */
    public int lastIndexOf(Object o)
    {
        for (int i = size - 1; i >= 0; i--)
        {
            if (o.equals(get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns a list iterator of the elements in this list (in proper sequence).
     *
     * @return a list iterator of the elements in this list (in proper sequence).
     */
    public ListIterator<T> listIterator()
    {
        return new IteratorImpl<T>(this);
    }

    /**
     * Returns a list iterator of the elements in this list (in proper sequence), starting at the specified position in
     * this list.  The specified index indicates the first element that would be returned by an initial call to the
     * <code>next</code> method.  An initial call to the <code>previous</code> method would return the element with the
     * specified index minus one.
     *
     * @param index index of first element to be returned from the list iterator (by a call to the <code>next</code>
     *              method).
     * @return a list iterator of the elements in this list (in proper sequence), starting at the specified position in
     *         this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    public ListIterator<T> listIterator(int index)
    {
        return new IteratorImpl<T>(this, index);
    }

    /**
     * Removes the element at the specified position in this list (optional operation).  Shifts any subsequent elements
     * to the left (subtracts one from their indices).  Returns the element that was removed from the list.
     *
     * @param index the index of the element to removed.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public T remove(int index)
    {
        setRecent(index);
        T ret = recent.getContent();
        if (index == 0)
        {
            recent = recent.getNext();
            first = recent;
            if (size == 0) last = null;
        } else if (index == size - 1)
        {
            recent = recent.getPrev();
            last = recent;
            recentIndex--;
        } else
        {
            recent.getNext().setPrev(recent.getPrev());
            recent.getPrev().setNext(recent.next);
//            Node<T> next = m_recent.next;
//            Node<T> prev = m_recent.prev;
//            next.prev = prev);
//            prev.next = next);
//            m_recent = next;
            recent = recent.getNext();
        }
        size--;
        return ret;
    }

    /**
     * Removes the first occurrence in this list of the specified element (optional operation).  If this list does not
     * contain the element, it is unchanged.  More formally, removes the element with the lowest index i such that
     * <code>(o==null ? get(i)==null : o.equals(get(i)))</code> (if such an element exists).
     *
     * @param o element to be removed from this list, if present.
     * @return <code>true</code> if this list contained the specified element.
     * @throws ClassCastException If the type of the specified element is incompatible with this list.
     */
    public boolean remove(Object o)
    {
        for (int i = 0; i < size; i++)
        {
            if (o.equals(get(i)))
            {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes from this list all the elements that are contained in the specified collection.  Each element in the
     * provided collection will be removed exactly once for each time it appears.  For example, if element
     * <code>E</code> appears twice in the provided collection and three times in this list, one instance of
     * <code>E</code> will be in this list upon the completion of this operation.
     *
     * @param c collection that defines which elements will be removed from this list.
     * @return <code>true</code> if this list changed as a result of the call.
     * @throws ClassCastException   If the types of one or more elements in this list are incompatible with the
     *                              specified collection.
     * @throws NullPointerException If the specified collection is <code>null</code>.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> c)
    {
        boolean change = false;
        for (Object o : c)
        {
            //noinspection SuspiciousMethodCalls
            change |= remove(o);
        }
        return change;
    }

    /**
     * Retains only the elements in this list that are contained in the specified collection (optional operation).  In
     * other words, removes from this list all the elements that are not contained in the specified collection.
     *
     * @param c collection that defines which elements this set will retain.
     * @return <code>true</code> if this list changed as a result of the call.
     * @throws ClassCastException   if the types of one or more elements in this list are incompatible with the
     *                              specified collection (optional).
     * @throws NullPointerException if the specified collection is <code>null</code>.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> c)
    {
        int index = 0;
        boolean change = false;
        while (index < size)
        {
            if (c.contains(get(index)))
            {
                index++;
            } else
            {
                remove(index);
                change = true;
            }
        }
        return change;
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index   Index of element to replace.
     * @param element Element to be stored at the specified position.
     * @return The element previously at the specified position.
     * @throws ClassCastException        if the class of the specified element prevents it from being added to this
     *                                   list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public T set(int index, T element)
    {
        setRecent(index);
        T ret = recent.getContent();
        recent.setContent(element);
        return ret;
    }

    /**
     * Returns the number of elements in this list.  If this list contains more than <code>Integer.MAX_VALUE</code>
     * elements, returns <code>Integer.MAX_VALUE</code>.
     *
     * @return The number of elements in this list.
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns a view of the portion of this list between the specified <code>from</code>, inclusive, and
     * <code>to</code>, exclusive.  (If <code>from</code> and <code>to</code> are equal, the returned list is empty.)
     * The returned list is backed by this list, so non-structural changes in the returned list are reflected in this
     * list, and vice-versa. The returned list supports all of the optional list operations supported by this list.<p>
     * <p/>
     * This method eliminates the need for explicit range operations (of the sort that commonly exist for arrays).   Any
     * operation that expects a list can be used as a range operation by passing a subList view instead of a whole list.
     * For example, the following idiom removes a range of elements from a list:
     * <pre>
     * 	    list.subList(from, to).clear();
     * </pre>
     * Similar idioms may be constructed for <code>indexOf</code> and <code>lastIndexOf</code>, and all of the
     * algorithms in the <code>Collections</code> class can be applied to a subList.<p>
     * <p/>
     * The semantics of the list returned by this method become undefined if the backing list (i.e., this list) is
     * <i>structurally modified</i> in any way other than via the returned list.  (Structural modifications are those
     * that change the size of this list, or otherwise perturb it in such a fashion that iterations in progress may
     * yield incorrect results.)
     *
     * @param from low endpoint (inclusive) of the subList.
     * @param to   high endpoint (exclusive) of the subList.
     * @return a view of the specified range within this list.
     * @throws IndexOutOfBoundsException for an illegal endpoint index value (from &lt; 0 || to &gt; size || from &gt;
     *                                   to).
     */
    public List<T> subList(int from, int to)
    {
        return null;  // TODO: implement sublist
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence.  Obeys the general contract of
     * the <code>Collection.toArray</code> method.
     *
     * @return An array containing all of the elements in this list in proper sequence.
     */
    public Object[] toArray()
    {
        Object[] ret = new Object[size];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = get(i);
        }
        return ret;
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence; the runtime type of the returned
     * array is that of the specified array.  Obeys the general contract of the <code>Collection.toArray(Object[])</code>
     * method.
     *
     * @param a The array into which the elements of this list are to be stored, if it is big enough; otherwise, a new
     *          array of the same runtime type is allocated for this purpose.
     * @return An array containing the elements of this list.
     * @throws ArrayStoreException  If the runtime type of the specified array is not a supertype of the runtime type of
     *                              every element in this list.
     * @throws NullPointerException If the specified array is <code>null</code>.
     */
    public <T>T[] toArray(T[] a)
    {
        if (a.length < size)
        {
            //noinspection unchecked
            a = (T[]) (new Object[size]);
        }
        Object[] o = toArray();
        System.arraycopy(o, 0, a, 0, size);
        return a;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method differs from the <code>peek</code> method
     * only in that it throws an exception if this queue is empty.
     *
     * @return the head of this queue.
     * @throws java.util.NoSuchElementException
     *          if this queue is empty.
     */
    public T element()
    {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        return get(0);
    }

    /**
     * Inserts the specified element into this queue, if possible.  When using queues that may impose insertion
     * restrictions (for example capacity bounds), method <code>offer</code> is generally preferable to method {@link
     * java.util.Collection#add}, which can fail to insert an element only by throwing an exception.
     *
     * @param o the element to insert.
     * @return <code>true</code> if it was possible to add the element to this queue, else <code>false</code>
     */
    public boolean offer(T o)
    {
        add(o);
        return true;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, returning <code>null</code> if this queue is empty.
     *
     * @return the head of this queue, or <code>null</code> if this queue is empty.
     */
    public T peek()
    {
        if (isEmpty())
        {
            return null;
        } else
        {
            return get(0);
        }
    }

    /**
     * Retrieves and removes the head of this queue, or <code>null</code> if this queue is empty.
     *
     * @return the head of this queue, or <code>null</code> if this queue is empty.
     */
    public T poll()
    {
        if (isEmpty())
        {
            return null;
        } else
        {
            return remove(0);
        }
    }

    /**
     * Retrieves and removes the head of this queue.  This method differs from the <code>poll</code> method in that it
     * throws an exception if this queue is empty.
     *
     * @return the head of this queue.
     * @throws java.util.NoSuchElementException
     *          if this queue is empty.
     */
    public T remove()
    {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        return remove(0);
    }

    /**
     * Generates a string which can describe the contents of this list.  The string is formatted "<code>[<i>string</i>,
     * <i>string</i>, ..., <i>string</i>]</code>", where each substring is the <code>toString()</code> value of the
     * objects in this list.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++)
        {
            sb.append(get(i).toString());
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class is designed to act as a doubly-linked node for the {@link PositionCachedLinkedList}.
     *
     * @author Zachary Palmer
     */
    static class Node<E>
    {
        /**
         * The link to the next node in the list.
         */
        protected Node<E> next;
        /**
         * The link to the previous node in the list.
         */
        protected Node<E> prev;
        /**
         * The content of this node.
         */
        protected E content;

        /**
         * General constructor.
         *
         * @param content The node's data content.
         * @param next    The next node in the list.
         * @param prev    The previous node in the list.
         */
        public Node(E content, Node<E> next, Node<E> prev)
        {
            this.setContent(content);
            this.setNext(next);
            this.setPrev(prev);
        }

        public Node<E> getNext()
        {
            return next;
        }

        public void setNext(Node<E> next)
        {
            this.next = next;
        }

        public Node<E> getPrev()
        {
            return prev;
        }

        public void setPrev(Node<E> prev)
        {
            this.prev = prev;
        }

        public E getContent()
        {
            return content;
        }

        public void setContent(E content)
        {
            this.content = content;
        }
    }

    /**
     * The {@link Iterator} for this type of list.  This {@link Iterator} operates by storing a reference to the next
     * {@link Node} over which to iterate.
     *
     * @author Zachary Palmer
     */
    static class IteratorImpl<T> implements ListIterator<T>
    {
        /**
         * The index on which this iterator believes that it is positioned.
         */
        protected int index;
        /**
         * The index which will be removed by a call to remove().  This exists separate from the <code>m_index</code>
         * field as the item to be remove differs for calls to <code>next()</code> and <code>previous()</code>.
         */
        protected int nextOperationIndex;
        /**
         * Whether or not a removal can be performed on the element currently indicated by the iterator.
         */
        protected boolean canRemove;
        /**
         * The {@link PositionCachedLinkedList} which created this {@link ListIterator}.
         */
        protected PositionCachedLinkedList<T> controller;

        /**
         * Private constructor.
         *
         * @param controller The list which created this object.
         */
        public IteratorImpl(PositionCachedLinkedList<T> controller)
        {
            this(controller, 0);
        }

        /**
         * General constructor.
         *
         * @param controller The list which created this object.
         * @param index      The starting index of this iterator.
         */
        public IteratorImpl(PositionCachedLinkedList<T> controller, int index)
        {
            this.controller = controller;
            this.index = index;
            nextOperationIndex = -1;
        }

        /**
         * Inserts the specified element into the list.  The element is inserted immediately before the next element
         * that would be returned by <code>next</code>, if any, and after the next element that would be returned by
         * <code>previous</code>, if any.  (If the list contains no elements, the new element becomes the sole element
         * on the list.) The new element is inserted before the implicit cursor: a subsequent call to <code>next</code>
         * would be unaffected, and a subsequent call to <code>previous</code> would return the new element.  (This call
         * increases by one the value that would be returned by a call to <code>nextIndex</code> or
         * <code>previousIndex</code>.)
         *
         * @param o the element to insert.
         * @throws UnsupportedOperationException if the <code>add</code> method is not supported by this list iterator.
         * @throws ClassCastException            if the class of the specified element prevents it from being added to
         *                                       this list.
         * @throws IllegalArgumentException      if some aspect of this element prevents it from being added to this
         *                                       list.
         */
        public void add(T o)
        {
            nextOperationIndex = -1;
            controller.add(index++, o);
        }

        /**
         * Returns <code>true</code> if this list iterator has more elements when traversing the list in the forward
         * direction. (In other words, returns <code>true</code> if <code>next</code> would return an element rather
         * than throwing an exception.)
         *
         * @return <code>true</code> if the list iterator has more elements when traversing the list in the forward
         *         direction.
         */
        public boolean hasNext()
        {
            return (index < controller.size() - 1);
        }

        /**
         * Returns <code>true</code> if this list iterator has more elements when traversing the list in the reverse
         * direction. (In other words, returns <code>true</code> if <code>previous</code> would return an element rather
         * than throwing an exception.)
         *
         * @return <code>true</code> if the list iterator has more elements when traversing the list in the reverse
         *         direction.
         */
        public boolean hasPrevious()
        {
            return (index > 0);
        }

        /**
         * Returns the next element in the list.  This method may be called repeatedly to iterate through the list, or
         * intermixed with calls to <code>previous</code> to go back and forth.  (Note that alternating calls to
         * <code>next</code> and <code>previous</code> will return the same element repeatedly.)
         *
         * @return the next element in the list.
         * @throws java.util.NoSuchElementException
         *          if the iteration has no next element.
         */
        public T next()
        {
            if (hasNext())
            {
                nextOperationIndex = index;
                return controller.get(index++);
            } else
            {
                throw new NoSuchElementException("No further elements.");
            }
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call to <code>next</code>. (Returns
         * list size if the list iterator is at the end of the list.)
         *
         * @return The index of the element that would be returned by a subsequent call to <code>next</code>, or list
         *         size if list iterator is at end of list.
         */
        public int nextIndex()
        {
            return index;
        }

        /**
         * Returns the previous element in the list.  This method may be called repeatedly to iterate through the list
         * backwards, or intermixed with calls to <code>next</code> to go back and forth.  (Note that alternating calls
         * to <code>next</code> and <code>previous</code> will return the same element repeatedly.)
         *
         * @return the previous element in the list.
         * @throws java.util.NoSuchElementException
         *          if the iteration has no previous element.
         */
        public T previous()
        {
            if (hasPrevious())
            {
                nextOperationIndex = --index;
                return controller.get(index);
            } else
            {
                throw new NoSuchElementException("No previous elements.");
            }
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call to <code>previous</code>.
         * (Returns -1 if the list iterator is at the beginning of the list.)
         *
         * @return the index of the element that would be returned by a subsequent call to <code>previous</code>, or -1
         *         if list iterator is at beginning of list.
         */
        public int previousIndex()
        {
            return index - 1;
        }

        /**
         * Removes from the list the last element that was returned by <code>next</code> or <code>previous</code>.  This
         * call can only be made once per call to <code>next</code> or <code>previous</code>.  It can be made only if
         * <code>ListIterator.add</code> has not been called after the last call to <code>next</code> or
         * <code>previous</code>.
         *
         * @throws IllegalStateException neither <code>next</code> nor <code>previous</code> have been called, or
         *                               <code>remove</code> or <code>add</code> have been called after the last call to
         *                               * <code>next</code> or <code>previous</code>.
         */
        public void remove()
        {
            if (nextOperationIndex == -1)
            {
                throw new IllegalStateException("No item to remove.");
            }
            controller.remove(nextOperationIndex);
            nextOperationIndex = -1;
        }

        /**
         * Replaces the last element returned by <code>next</code> or <code>previous</code> with the specified element
         * (optional operation). This call can be made only if neither <code>ListIterator.remove</code> nor
         * <code>ListIterator.add</code> have been called after the last call to <code>next</code> or
         * <code>previous</code>.
         *
         * @param o the element with which to replace the last element returned by <code>next</code> or
         *          <code>previous</code>.
         * @throws ClassCastException    if the class of the specified element prevents it from being added to this
         *                               list.
         * @throws IllegalStateException if neither <code>next</code> nor <code>previous</code> have been called, or
         *                               <code>remove</code> or <code>add</code> have been called after the last call to
         *                               <code>next</code> or <code>previous</code>.
         */
        public void set(T o)
        {
            if (nextOperationIndex == -1)
            {
                throw new IllegalStateException("No item to replace.");
            }
            controller.set(nextOperationIndex, o);
        }
    }
}

// END OF FILE
