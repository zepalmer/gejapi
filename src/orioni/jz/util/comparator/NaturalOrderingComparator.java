package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This {@link Comparator} compares {@link Comparable} objects by their natural ordering.  It is only useful in that it
 * provides a means by which a comparator may be generically replaced with natural ordering.
 *
 * @author Zachary Palmer
 */
public class NaturalOrderingComparator<T extends Comparable<T>> implements Comparator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public NaturalOrderingComparator()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.<p>
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(T a, T b)
    {
        return a.compareTo(b);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
