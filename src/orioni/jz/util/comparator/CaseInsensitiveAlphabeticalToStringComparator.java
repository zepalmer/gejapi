package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This class is designed to perform comparisons of two objects based upon the alphabetical sorting order of their
 * respective {@link Object#toString()} method calls without regard to case.  Alphabetically prior objects are
 * considered "lesser" than alphabetically latter objects.
 */
public class CaseInsensitiveAlphabeticalToStringComparator<T> implements Comparator<T>
{
    /**
     * A constant singleton instance of this {@link Comparator}.
     */
    public static final CaseInsensitiveAlphabeticalToStringComparator SINGLETON =
            new CaseInsensitiveAlphabeticalToStringComparator();

    /**
     * Compares the provided objects for alphabetical sorting order based upon the results of calling their
     * respective {@link Object#toString()} methods without regard to case.  Returns a negative integer, zero, or a
     * positive integer as the first object is alphabetically before, equal to, or after the second.
     * <P>
     * Clearly, this {@link Comparator} violates the presumtion that <code>(compare(a,b)==0)==(a.equals(b)) unless
     * the <code>equals(Object)</code> method on <code>a</code> determines whether or not <code>a.toString()</code>
     * and <code>b.toString()</code> return equal strings without regard to case.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     *	       second.
     * @throws ClassCastException if the arguments' types prevent them from
     * 	       being compared by this Comparator.
     */
    public int compare(T a, T b)
    {
        return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
    }
}