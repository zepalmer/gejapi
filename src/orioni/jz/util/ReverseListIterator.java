package orioni.jz.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This {@link Iterator} implementation iterates <i>backwards</i> over a list.
 *
 * @author Zachary Palmer
 */
public class ReverseListIterator <T> implements Iterator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The list over which we are iterating.
     */
    protected List<T> list;
    /**
     * The current iterator index.
     */
    protected int index;
    /**
     * Determines whether or not removal is legal.
     */
    protected boolean canRemove;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param list The {@link List} over which iteration should occur backwards.
     */
    public ReverseListIterator(List<T> list)
    {
        super();
        this.list = list;
        index = this.list.size() - 1;
        canRemove = false;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns <tt>true</tt> if <tt>next</tt>
     * would return an element rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext()
    {
        return (index >= 0);
    }

    /**
     * Returns the next element in the iteration.  Calling this method repeatedly until the {@link #hasNext()} method
     * returns false will return each element in the underlying collection exactly once.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException iteration has no more elements.
     */
    public T next()
    {
        if (index <0) throw new NoSuchElementException();
        canRemove = true;
        return list.get(index--);
    }

    /**
     * Removes from the underlying collection the last element returned by the iterator (optional operation).  This
     * method can be called only once per call to <tt>next</tt>.  The behavior of an iterator is unspecified if the
     * underlying collection is modified while the iteration is in progress in any way other than by calling this
     * method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation is not supported by this Iterator.
     * @throws IllegalStateException         if the <tt>next</tt> method has not yet been called, or the <tt>remove</tt>
     *                                       method has already been called after the last call to the <tt>next</tt>
     *                                       method.
     */
    public void remove()
    {
        if (canRemove)
        {
            list.remove(index + 1);
            canRemove = false;
        } else
        {
            throw new IllegalStateException("Cannot remove; either next() has not been called since the last remove.");
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE