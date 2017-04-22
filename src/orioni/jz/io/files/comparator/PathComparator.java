package orioni.jz.io.files.comparator;

import orioni.jz.io.files.FileUtilities;

import java.io.File;
import java.util.Comparator;

/**
 * This class is designed to perform a string comparison on the filenames of {@link File} objects.  The path is
 * retrieved and, if appropriate, unified in case (as per the contents of the
 * {@link FileUtilities#FILESYSTEM_CASE_SENSITIVE} field).  It is then compared in the same fashion as
 * {@link String#compareTo(Object)}.
 *
 * @author Zachary Palmer
 */
public class PathComparator implements Comparator
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

	public static final PathComparator SINGLETON = new PathComparator();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 */
	public PathComparator()
	{
		super();
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Compares its two arguments for order.  Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.
	 *
	 * @param o1 The first object to be compared.
	 * @param o2 The second object to be compared.
	 * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
	 *         than the second.
	 * @throws ClassCastException If either argument is not a {@link File} or a {@link String}.
	 */
	public int compare(Object o1, Object o2)
	{
		String s1;
		String s2;
		try
		{
			if (o1 instanceof File) s1 = ((File) o1).getPath(); else s1 = (String) o1;
			if (o2 instanceof File) s2 = ((File) o2).getPath(); else s2 = (String) o2;
		} catch (ClassCastException e)
		{
			System.out.println(o1.getClass());
			System.out.println(o2.getClass());
			throw e;
		}
		if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE)
		{
			s1 = s1.toLowerCase();
			s2 = s2.toLowerCase();
		}
		return s1.compareTo(s2);
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}