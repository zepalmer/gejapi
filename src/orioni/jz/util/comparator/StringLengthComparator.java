package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This {@link Comparator}<code>&lt;{@link String}&gt;</code> implementation compares two {@link String}s by their
 * lengths.  Larger strings are considered to be "greater" than smaller strings.
 *
 * @author Zachary Palmer
 */
public class StringLengthComparator implements Comparator<String>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public StringLengthComparator()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares two strings for length.  Strings with greater lengths are assumed to be "greater" than strings with
     * smaller lengths.
     *
     * @param s1 the first object to be compared.
     * @param s2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(String s1, String s2)
    {
        if (s1.length() > s2.length()) return 1;
        if (s1.length() < s2.length()) return -1;
        return 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE