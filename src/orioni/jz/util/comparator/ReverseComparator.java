package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This {@link Comparator} implementation negates the results of any {@link Comparator} supplied to it, thus causing
 * the ordering to be reversed.
 *
 * @author Zachary Palmer
 */
public class ReverseComparator<T> implements Comparator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link Comparator} to reverse. */
    protected Comparator<T> comparator;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param comparator The {@link Comparator} to reverse.
     */
    public ReverseComparator(Comparator<T> comparator)
    {
        super();
        this.comparator = comparator;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.  This ordering is exactly the opposite of the
     * ordering imposed by the constructor's comparator.
     *
     * @param oa the first object to be compared.
     * @param ob the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(T oa, T ob)
    {
        return -comparator.compare(oa,ob);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE