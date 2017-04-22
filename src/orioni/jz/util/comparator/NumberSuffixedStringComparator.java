package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This {@link Comparator} is designed to more intuitively sort strings which are suffixed with a positive decimal
 * value.  When sorting, the string is split into two segments: the numeric suffix (if applicable) and the character
 * prefix.  If the character prefixes match, the numeric suffixes are then compared <i>numerically</i> rather than as
 * strings.  For example, consider the strings: <ul><code>MyString25<br/>MyString3</code></ul> In normal string
 * comparison, the first string is sorted before the second, since the character '<code>2</code>' is considered previous
 * to the character '<code>3</code>'.  However, this comparator will sort the second before the first string, since
 * <code>3 < 25</code>.
 *
 * @author Zachary Palmer
 */
public class NumberSuffixedStringComparator implements Comparator<String>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this comparator.
     */
    public static final NumberSuffixedStringComparator SINGLETON = new NumberSuffixedStringComparator();

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public NumberSuffixedStringComparator()
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
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException If the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(String a, String b)
    {
        int index = a.length() - 1;
        while ((index >= 0) && (Character.isDigit(a.charAt(index))))
        {
            index--;
        }
        index++;
        String aa = a.substring(0, index);
        String ab = a.substring(index);
        index = b.length() - 1;
        while ((index >= 0) && (Character.isDigit(b.charAt(index))))
        {
            index--;
        }
        index++;
        String ba = b.substring(0, index);
        String bb = b.substring(index);
        int comp = aa.compareTo(ba);
        if (comp != 0) return comp;
        int ia, ib;
        ia = (ab.length() > 0) ? Integer.parseInt(ab) : Integer.MIN_VALUE;
        ib = (bb.length() > 0) ? Integer.parseInt(bb) : Integer.MIN_VALUE;
        if (ia < ib) return -1;
        if (ia > ib) return 1;
        return 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
