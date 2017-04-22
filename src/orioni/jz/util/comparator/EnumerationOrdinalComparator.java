package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This comparator class orders enumeration objects by their ordinal numbers.
 *
 * @author Zachary Palmer
 */
public class EnumerationOrdinalComparator<T extends Enum<T>> implements Comparator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public EnumerationOrdinalComparator()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(T a, T b)
    {
        if (a.ordinal() < b.ordinal()) return -1;
        if (b.ordinal() < a.ordinal()) return 1;
        return 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
