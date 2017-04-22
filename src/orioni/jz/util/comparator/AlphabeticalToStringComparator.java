package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This class is designed to perform comparisons of two objects based upon the alphabetical sorting order of their
 * respective {@link java.lang.Object#toString()} method calls.  Alphabetically prior objects are considered "lesser" than
 * alphabetically latter objects.
 */
public class AlphabeticalToStringComparator<T> implements Comparator<T>
{
	/** A constant singleton instance of this {@link Comparator}. */
	public static final AlphabeticalToStringComparator SINGLETON = new AlphabeticalToStringComparator();

	/**
	 * Compares the provided objects for alphabetical sorting order based upon the results of calling their
	 * respective {@link java.lang.Object#toString()} methods.  Returns a negative integer, zero, or a positive integer as
	 * the first object is alphabetically before, equal to, or after the second.
	 * <P>
	 * Clearly, this {@link java.util.Comparator} violates the presumtion that <code>(compare(a,b)==0)==(a.equals(b)) unless
	 * the <code>equals(Object)</code> method on <code>a</code> determines whether or not <code>a.toString()</code>
	 * and <code>b.toString()</code> return equal strings.
	 *
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the
	 * 	       first argument is less than, equal to, or greater than the
	 *	       second.
	 * @throws java.lang.ClassCastException if the arguments' types prevent them from
	 * 	       being compared by this Comparator.
	 */
	public int compare(T o1, T o2)
	{
		return o1.toString().compareTo(o2.toString());
	}
}