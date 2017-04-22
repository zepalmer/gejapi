package orioni.jz.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This implementation of {@link Iterator} and {@link Enumeration} can take either of those two objects and allow it to
 * be treated as if it were the other.  The {@link IteratorEnumeration#hasNext()} method, for example, performs the same
 * function as the {@link IteratorEnumeration#hasMoreElements()} method.  Iterator and Enumeration methods may be called
 * interchangably, but contexts are not maintained seperately; that is, if {@link IteratorEnumeration#next()} and {@link
 * IteratorEnumeration#nextElement()} are each called once, two elements will have been removed from the underlying data
 * structure, regardless of what type it is.
 *
 * @author Zachary Palmer
 */
public class IteratorEnumeration <T> implements Iterator<T>, Enumeration<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Iterator} field for this object.  If the {@link Enumeration} field is being used instead, this field
     * will be <code>null</code>.
     */
    protected Iterator<T> iterator;
    /**
     * The {@link Enumeration} field for this object.  If the {@link Iterator} field is being used instead, this field
     * will be <code>null</code>.
     */
    protected Enumeration<T> enumeration;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param iterator The {@link Iterator} to use.
     */
    public IteratorEnumeration(Iterator<T> iterator)
    {
        super();
        this.iterator = iterator;
        enumeration = null;
    }

    /**
     * General constructor.
     *
     * @param enumeration The {@link Enumeration} to use.
     */
    public IteratorEnumeration(Enumeration<T> enumeration)
    {
        super();
        iterator = null;
        this.enumeration = enumeration;
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
        if (iterator == null)
        {
            return enumeration.hasMoreElements();
        } else
        {
            return iterator.hasNext();
        }
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
        if (iterator == null)
        {
            return enumeration.nextElement();
        } else
        {
            return iterator.next();
        }
    }

    /**
     * If the underlying data structure is an {@link Iterator}, this method is called as normal.  If it is an {@link
     * Enumeration}, an {@link UnsupportedOperationException} will be thrown.
     *
     * @throws UnsupportedOperationException If the <tt>remove</tt> operation is not supported by this Iterator.
     * @throws IllegalStateException         If the <tt>next</tt> method has not yet been called, or the <tt>remove</tt>
     *                                       method has already been called after the last call to the <tt>next</tt>
     *                                       method.
     */
    public void remove()
    {
        if (iterator == null)
        {
            throw new UnsupportedOperationException("Cannot perform remove() on an Enumeration");
        } else
        {
            iterator.remove();
        }
    }

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object contains at least one more element to provide;
     *         <code>false</code> otherwise.
     */
    public boolean hasMoreElements()
    {
        return hasNext();
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to
     * provide.
     *
     * @return The next element of this enumeration.
     * @throws NoSuchElementException if no more elements exist.
     */
    public T nextElement()
    {
        return next();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE