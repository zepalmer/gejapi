package orioni.jz.util;

import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class is intended to be an implementation of {@link Enumeration} which uses an array for its data contents.
 *
 * @author Zachary Palmer
 */
public class ListEnumeration<T> implements Enumeration<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link List} from which this {@link Enumeration} retrieves its data.
     */
    protected List<T> list;
    /**
     * The next element of data to provide.
     */
    protected int nextIndex;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param list The {@link List} from which this {@link Enumeration} retrieves its data.
     */
    public ListEnumeration(List<T> list)
    {
        super();
        this.list = list;
        nextIndex = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object contains at least one more element to provide;
     *         <code>false</code> otherwise.
     */
    public boolean hasMoreElements()
    {
        return (list.size() > nextIndex);
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to
     * provide.
     *
     * @return The next element of this enumeration.
     * @throws java.util.NoSuchElementException
     *          If no more elements exist.
     */
    public T nextElement()
            throws NoSuchElementException
    {
        try
        {
            return list.get(nextIndex++);
        } catch (IndexOutOfBoundsException ioobe)
        {
            throw new NoSuchElementException("No more elements remain.");
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}