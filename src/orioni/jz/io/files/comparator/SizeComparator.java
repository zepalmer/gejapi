package orioni.jz.io.files.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * This {@link Comparator} implementation sorts {@link File} objects by the size of the specified file.  It also
 * accepts {@link String} objects, creating the corresponding {@link File} objects with the string as a path, alongside
 * the {@link File} objects.  {@link File} objects which do not represent existing files are considered to have a size
 * of <code>-1</code> bytes; they are therefore sorted as being "less than" existing {@link File} objects.
 * Non-existent files are considered to be equal.
 * @author Zachary Palmer
 */
public class SizeComparator implements Comparator
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

	/** A singleton instance of the {@link SizeComparator} class. */
	public static final SizeComparator SINGLETON = new SizeComparator();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 */
	public SizeComparator()
	{
		super();
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
	 * argument is less than, equal to, or greater than the second.
	 *
	 * @param o1 The first object to be compared.
	 * @param o2 The second object to be compared.
	 * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
	 *         than the second.
	 * @throws ClassCastException If either argument is not a {@link File} or a {@link String}.
	 */
	public int compare(Object o1, Object o2)
	{
		File f1;
		File f2;
		if (o1 instanceof String) f1 = new File((String) o1); else f1 = (File) o1;
		if (o2 instanceof String) f2 = new File((String) o2); else f2 = (File) o2;
		if (f1.exists())
		{
			if (f2.exists())
			{
				if (f1.length() > f2.length())
				{
					return -1;
				} else if (f2.length() > f1.length())
				{
					return 1;
				} else
				{
					return 0;
				}
			} else
			{
				return 1;
			}
		} else
		{
			if (f2.exists())
			{
				return -1;
			} else
			{
				return 0;
			}
		}
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}