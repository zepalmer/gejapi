package orioni.jz.util;

import orioni.jz.math.MathUtilities;

import java.util.*;

/**
 * Implements the {@link Bag} interface using a hashing mechanism.  This class is backed by a {@link HashMap} instance
 * which maps objects contained in the {@link Bag} to the number of times they exist in the {@link Bag}.
 *
 * @author Zachary Palmer
 */
public class HashBag<T> implements Bag<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The hash code modifier which is used for <code>null</code>.
     */
    private static final int NULL_HASH_CODE = 0x40123756;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Used to represent the contents of the bag.  The mapping is between objects in the bag and the number of times
     * they occur.
     */
    protected HashMap<T, Integer> contentsMap;
    /**
     * The number of elements in the bag.  This is used to expediate the operation of the {@link Bag#size()} method.
     */
    protected int size;
    /**
     * The number of <code>null</code>s which exist in this bag.
     */
    protected int nullCount;

    /**
     * The identification number of the last modification to this bag.  This is used to provie fast-fail behavior of the
     * bag's iterators.
     */
    protected int modificationId;

    /**
     * The hash code for this bag.  This code is maintained throughout the life of the bag so as to prevent it from
     * needing to be frequently recalculated.
     */
    protected int hashCode; // TODO: recalculate hashCode on deserialization

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Creates an empty bag.
     */
    public HashBag()
    {
        initialize();
        setContents(null);
    }

    /**
     * General constructor.  Creates the bag based upon the values stored in the provided map.
     *
     * @param map The map on which to base this bag.  Values of less than zero are ignored.
     */
    public HashBag(Map<T, Integer> map)
    {
        initialize();
        for (Map.Entry<T, Integer> entry : map.entrySet())
        {
            if (entry.getValue() > 0) add(entry.getKey(), entry.getValue());
        }
    }

    /**
     * General constructor.
     *
     * @param bag The {@link Bag} to copy.  If <code>null</code>, the newly created bag is empty.
     */
    public HashBag(Bag<T> bag)
    {
        initialize();
        setContents(bag);
    }

    /**
     * Common initialization method for constructors.
     */
    private void initialize()
    {
        contentsMap = new DefaultValueHashMap<T, Integer>(0);
        size = 0;
        nullCount = 0;
        hashCode = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the contents of this bag to be equal to the contents of another bag.
     *
     * @param bag The bag to copy.  If <code>null</code>, this bag is emptied.
     */
    public void setContents(Bag<T> bag)
    {
        clear();
        if (bag != null)
        {
            for (T t : bag.getUniqueValues())
            {
                add(t, bag.getFrequency(t));
            }
        }
    }

    /**
     * Adds the specified element to this bag.  Note that, if it is already present, it is still added.
     *
     * @param t The object to add to this bag.
     * @return <code>true</code>, always, as specified by {@link Collection#add(Object)}.
     */
    public boolean add(T t)
    {
        return add(t, 1);
    }

    /**
     * Adds the specified element to this bag a given number of times.  Note that, if it is already present, it is still
     * added.
     *
     * @param t      The object to add to this bag.
     * @param number The number of times to add it to this bag.
     * @return <code>true</code>, always, as specified by {@link Collection#add(Object)}.
     */
    public boolean add(T t, int number)
    {
        if (t == null)
        {
            if (nullCount > 0)
            {
                hashCode ^= MathUtilities.rotateLeft(NULL_HASH_CODE, nullCount);
            }
            nullCount += number;
            hashCode ^= MathUtilities.rotateLeft(NULL_HASH_CODE, nullCount);
        } else
        {
            int count = contentsMap.get(t);
            hashCode ^= MathUtilities.rotateLeft(t.hashCode(), count);
            contentsMap.put(t, count + number);
            hashCode ^= MathUtilities.rotateLeft(t.hashCode(), count + 1);
        }
        size += number;
        modificationId++;
        return true;
    }

    /**
     * Adds all elements in the specified {@link Collection} to this bag.  If they already exist, they are added again
     * anyway.
     *
     * @param c The {@link Collection} of objects to add to this bag.
     * @return <code>true</code> if the {@link Bag} was changed as a result of this call.  This will be the case if and
     *         only if the provided collection is not empty.
     */
    public boolean addAll(Collection<? extends T> c)
    {
        for (T t : c)
        {
            add(t);
        }
        return (!c.isEmpty());
    }

    /**
     * Empties this {@link Bag}.
     */
    public void clear()
    {
        contentsMap.clear();
        size = 0;
        nullCount = 0;
        hashCode = 0;
        modificationId++;
    }

    /**
     * Determines whether or not this {@link Bag} contains at least one instance of the specified object.
     *
     * @param o The {@link Object} for which containment should be checked.
     * @return <code>true</code> if the provided object is contained within this {@link Bag}; <code>false</code> if it
     *         is not.
     */
    public boolean contains(Object o)
    {
        if (o == null)
        {
            return (nullCount > 0);
        } else
        {
            //noinspection SuspiciousMethodCalls
            return (contentsMap.keySet().contains(o));
        }
    }

    /**
     * Determines whether or not this {@link Bag} contains at least one instance of every object in the specified {@link
     * Collection}.  Note that this does not imply anything about frequency of containment.  If this bag is
     * <code>(0,1)</code> and <code>containsAll(Collection)</code> is called with the bag <code>(0,0,0,0)</code>, then
     * <code>true</code> will be returned.
     */
    public boolean containsAll(Collection<?> c)
    {
        for (Object o : c)
        {
            //noinspection SuspiciousMethodCalls
            if (!contains(o)) return false;
        }
        return true;
    }

    /**
     * Determines whether or not this {@link HashBag} is equal to another.
     *
     * @param o The other bag.
     * @return <code>true</code> if and only if the other object is a bag and contains the same objects as this bag with
     *         the same number of repetitions.
     */
    public boolean equals(Object o)
    {
        if (o instanceof Bag)
        {
            return equals((Bag) o);
        } else
        {
            return false;
        }
    }

    /**
     * Determines whether or not this {@link HashBag} is equal to another.
     *
     * @param bag The other bag.
     * @return <code>true</code> if and only if the other bag contains the same objects as this bag with the same number
     *         of repetitions.
     */
    public boolean equals(Bag bag)
    {
        try
        {
            Set values = getUniqueValues();
            if (!values.equals(bag.getUniqueValues())) return false;
            for (Object value : values)
            {
                if (getFrequency(value) != bag.getFrequency(value))
                {
                    return false;
                }
            }
            return (getFrequency(null) == bag.getFrequency(null));
        } catch (ClassCastException cce)
        {
            return false;
        }
    }

    /**
     * Determines the frequency with which the specified object appears in this {@link Bag}.
     *
     * @param o The object to find in this bag.
     * @return The frequency with which that object appears.
     */
    public int getFrequency(Object o)
    {
        if (o == null) return nullCount;
        try
        {
            //noinspection SuspiciousMethodCalls
            return contentsMap.get(o);
        } catch (ClassCastException cce)
        {
            return 0;
        }
    }

    /**
     * Retrieves a representation of this bag in the form of a {@link Map}<code>&lt;T,Integer&gt;</code>.  The keys of
     * that map represent the unique values in this bag; the map's values represent the count of those bag values.
     *
     * @return A map representation of this bag.
     */
    public Map<T, Integer> getMapRepresentation()
    {
        return new HashMap<T, Integer>(contentsMap);
    }

    /**
     * Retrieves a {@link Set} containing the unique objects in this bag.  This set is unmodifiable.
     *
     * @return A {@link Set} containing all unique elements in this bag.
     */
    public Set<T> getUniqueValues()
    {
        return Collections.unmodifiableSet(contentsMap.keySet());
    }

    /**
     * Returns a hash code for this {@link HashBag}.
     *
     * @return The hash code for this hash bag.
     */
    public int hashCode()
    {
        return hashCode;
    }

    /**
     * Determines whether or not this bag is empty.
     *
     * @return <code>true</code> if this bag contains no elements; <code>false</code> otherwise.
     */
    public boolean isEmpty()
    {
        return (size == 0);
    }

    /**
     * Returns an {@link Iterator} which will iterate over this {@link Bag}.
     *
     * @return An {@link Iterator} which will iterate over this {@link Bag}.
     */
    public Iterator<T> iterator()
    {
        return new HashBagIterator<T>(this);
    }

    /**
     * Removes an object from this {@link Bag} if it is present at least once.  Note that, as bags allow repeated
     * elements, the object may be successfully removed and still exist in the bag.
     *
     * @param o The object to remove.
     * @return <code>true</code> if the object was removed; <code>false</code> if it was not.
     */
    public boolean remove(Object o)
    {
        return remove(o, 1);
    }

    /**
     * Removes an object from this {@link Bag} up to a given number of times.  Note that, as bags allow repeated
     * elements, the object may be successfully removed and still exist in the bag.
     *
     * @param o      The object to remove.
     * @param number The number of times to remove that object.
     * @return <code>true</code> if the object was removed the specified number of times; <code>false</code> if it was
     *         removed less than that number of times (including not at all).
     * @throws IllegalArgumentException If <code>number</code> is less than zero.
     */
    public boolean remove(Object o, int number)
    {
        if (number == 0) return true;
        if (number < 0)
        {
            throw new IllegalArgumentException("Number must be greater than or equal to zero (was " + number + ").");
        }
        if (getFrequency(o) < number)
        {
            remove(o, getFrequency(o));
            return false;
        } else
        {
            if (o == null)
            {
                hashCode ^= MathUtilities.rotateLeft(NULL_HASH_CODE, nullCount);
                nullCount -= number;
                if (nullCount > 0)
                {
                    hashCode ^= MathUtilities.rotateLeft(NULL_HASH_CODE, nullCount);
                }
            } else
            {
                T t;
                try
                {
                    //noinspection unchecked
                    t = (T) o;
                } catch (ClassCastException cce)
                {
                    return false;   // the object isn't even of this bag's type
                }
                Integer count = contentsMap.get(t);
                hashCode ^= MathUtilities.rotateLeft(t.hashCode(), count);
                count -= number;
                if (count == 0)
                {
                    contentsMap.remove(t);
                } else
                {
                    contentsMap.put(t, count);
                    hashCode ^= MathUtilities.rotateLeft(t.hashCode(), count);
                }
            }
            size -= number;
            modificationId++;
            return true;
        }
    }

    /**
     * Removes all objects in the provided {@link Collection} from this bag.  Each removal is performed independently
     * and thus duplicates in the provided {@link Collection} will affect the bag multiple times.  For example, given a
     * bag <code>(0,1,3,3,3,1,5)</code> and a list <code>(2,1,1,5,3,3,1)</code>, removing all items in the list from the
     * bag produces a resulting bag <code>(0,3)</code>.
     *
     * @param c The {@link Collection} containing the elements to remove.
     * @return <code>true</code> if the bag was changed by this operation; <code>false</code> if it was not.
     */
    public boolean removeAll(Collection<?> c)
    {
        boolean change = false;
        for (Object o : c)
        {
            //noinspection SuspiciousMethodCalls
            change |= remove(o);
        }
        if (change) modificationId++;
        return change;
    }

    /**
     * Removes all instances of the specified object from this bag.
     *
     * @param t The object to remove.
     */
    public void removeAllOf(T t)
    {
        remove(t, getFrequency(t));
    }

    /**
     * Retains in this bag only the elements which are also found in the provided {@link Collection}.  Retentions
     * respect the number of occurrences of a given object in both the collection and the bag.  For example, given a bag
     * <code>(0,1,3,3,3,1,5)</code> and a list <code>(2,1,1,5,3,3,1)</code>, retaining only the items in the list
     * produces a resulting bag <code>(1,1,3,3,5)</code>.
     *
     * @param c The {@link Collection} containing the elements to retain.
     * @return <code>true</code> if the bag was changed by this operation; <code>false</code> if it was not.
     */
    public boolean retainAll(Collection<?> c)
    {
        // Create a pseudo-bag of the provided collection.
        int nulls = 0;
        HashMap<T, Integer> map = new DefaultValueHashMap<T, Integer>(0);
        for (Object o : c)
        {
            if (o == null)
            {
                nulls++;
            } else
            {
                try
                {
                    //noinspection unchecked
                    T t = (T) o;
                    Integer count = map.get(t);
                    map.put(t, count + 1);
                } catch (ClassCastException cce)
                {
                    // Do nothing.  The object isn't of the right type, so we don't have any; this means it won't be
                    // retained anyway.
                }
            }
        }
        // Now we've bagged the provided collection.  Do a minimum comparison on the two maps.
        int startingSize = size;
        HashSet<T> keys = new HashSet<T>(contentsMap.keySet());
        keys.addAll(map.keySet());
        for (T key : keys)
        {
            Integer ourCount = contentsMap.get(key);
            Integer colCount = map.get(key);
            if (ourCount != 0) // otherwise, we don't care what of this key the collection has in it.
            {
                if (colCount == 0)
                {
                    size -= contentsMap.remove(key);
                } else
                {
                    if (ourCount > colCount)
                    {
                        hashCode ^= MathUtilities.rotateLeft(key.hashCode(), ourCount);
                        contentsMap.put(key, colCount);
                        size -= (ourCount - colCount);
                        hashCode ^= MathUtilities.rotateLeft(key.hashCode(), colCount);
                    }
                }
            }
        }
        // Finally, deal with nulls.
        if (nullCount > nulls)
        {
            hashCode ^= MathUtilities.rotateLeft(NULL_HASH_CODE, nullCount);
            size -= (nullCount - nulls);
            nullCount = nulls;
            hashCode ^= MathUtilities.rotateLeft(NULL_HASH_CODE, nullCount);
        }
        // If a change has occurred, it was in removing at least one object from this bag.
        if (size != startingSize) modificationId++;
        return (size != startingSize);
    }

    /**
     * Returns the number of elements in this bag.  This is not necessarily indicative of the number of <i>unique</i>
     * elements in the bag.
     *
     * @return The number of elements in this bag.
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns the contents of this bag as an array.
     *
     * @return The contents of this bag in an array.
     */
    public Object[] toArray()
    {
        int index = 0;
        Object[] ret = new Object[size];
        for (T t : this)
        {
            ret[index++] = t;
        }
        return ret;
    }

    /**
     * Returns the contents of this bag in a typed array.
     *
     * @return The contents of this bag in a typed array.
     * @throws ArrayStoreException If the runtime type of the provided array is not assignable from every object in this
     *                             collection.
     */
    public <E> E[] toArray(E[] ts)
    {
        try
        {
            if (ts.length < size)
            {
                ArrayList<E> list = new ArrayList<E>();
                for (Object t : this)
                {
                    //noinspection unchecked
                    list.add((E) t);
                }
                return list.toArray(ts);
            } else
            {
                int index = 0;
                for (Object t : this)
                {
                    //noinspection unchecked
                    ts[index++] = (E) t;
                }
                if (index < ts.length) ts[index] = null;
                return ts;
            }
        } catch (ClassCastException cce)
        {
            throw new ArrayStoreException(cce.getMessage());
        }
    }

    /**
     * Describes the contents of this bag.  The string is created by creating a parenthesis-surrounded, comma-separated
     * list.  Each element in the list is the {@link String#valueOf(Object)} result of the objects in the bag.
     *
     * @return A string describing the contents of this bag.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        Iterator it = iterator();
        while (it.hasNext())
        {
            sb.append(String.valueOf(it.next()));
            if (it.hasNext()) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Returns the number of <i>unique</i> elements in this bag.  This is equivalent to the cardinality of this bag if
     * it were treated as a set (duplicates being erased).
     *
     * @return The number of unique elements in this bag.
     */
    public int uniqueSize()
    {
        return contentsMap.keySet().size();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////


    /**
     * This class is designed to iterate over this {@link Bag}.
     *
     * @author Zachary Palmer
     */
    static class HashBagIterator<T> implements Iterator<T>
    {
        /**
         * The {@link Bag} over which we are iterating.
         */
        protected HashBag<T> bag;
        /**
         * Determines if {@link Iterator#remove()} is legal to call in this context.
         */
        protected boolean removeLegal;
        /**
         * The last object returned by this iterator.
         */
        protected T lastReturn;
        /**
         * The number of times the last return will be repeated before fetching a new value.
         */
        protected int lastReturnEndurance;
        /**
         * The {@link Iterator} from which we are obtaining return values.
         */
        protected Iterator<T> iterator;
        /**
         * The last checked modification count of the {@link Bag} over which we are iterating.
         */
        protected int lastCheckedModification;

        /**
         * General constructor.
         *
         * @param bag The bag over which we are iterating.
         */
        public HashBagIterator(HashBag<T> bag)
        {
            this.bag = bag;
            removeLegal = false;
            lastCheckedModification = this.bag.modificationId;
            lastReturn = null;
            lastReturnEndurance = this.bag.nullCount;
            iterator = this.bag.contentsMap.keySet().iterator();
        }

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns <tt>true</tt> if
         * <tt>next</tt> would return an element rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext()
        {
            if (lastCheckedModification != bag.modificationId) throw new ConcurrentModificationException();
            return ((iterator.hasNext()) || (lastReturnEndurance > 0));
        }

        /**
         * Returns the next element in the iteration.  Calling this method repeatedly until the {@link #hasNext()}
         * method returns false will return each element in the underlying collection exactly once.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException iteration has no more elements.
         */
        public T next()
        {
            if (lastCheckedModification != bag.modificationId) throw new ConcurrentModificationException();
            removeLegal = true;
            if (lastReturnEndurance == 0)
            {
                lastReturn = iterator.next();
                lastReturnEndurance = bag.contentsMap.get(lastReturn);
            }
            lastReturnEndurance--;
            return lastReturn;
        }

        /**
         * Removes from the underlying collection the last element returned by the iterator.  This method can be called
         * only once per call to <tt>next</tt>.  The behavior of an iterator is unspecified if the underlying collection
         * is modified while the iteration is in progress in any way other than by calling this method.
         *
         * @throws IllegalStateException if the <tt>next</tt> method has not yet been called, or the <tt>remove</tt>
         *                               method has already been called after the last call to the <tt>next</tt>
         *                               method.
         */
        public void remove()
        {
            if (lastCheckedModification != bag.modificationId) throw new ConcurrentModificationException();
            removeLegal = false;
            bag.remove(lastReturn);
            lastCheckedModification = bag.modificationId;
        }
    }
}

// END OF FILE