package orioni.jz.io.files.abstractnode;

import java.util.Comparator;

/**
 * This {@link java.util.Comparator} implementation sorts {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects by their names.
 * @author Zachary Palmer
 */
public class AbstractFileNodeNameComparator implements Comparator<AbstractFileNode>
{
	/** A singleton instance of the {@link orioni.jz.io.files.abstractnode.AbstractFileNodeNameComparator} using case insensitivity, for convenience. */
	public static final AbstractFileNodeNameComparator SINGLETON_INSENSITIVE = new AbstractFileNodeNameComparator(false);
	/** A singleton instance of the {@link orioni.jz.io.files.abstractnode.AbstractFileNodeNameComparator} using case sensitivity, for convenience. */
	public static final AbstractFileNodeNameComparator SINGLETON_SENSITIVE = new AbstractFileNodeNameComparator(true);

	/** <code>true</code> if names should be compared in a case sensitive manner; <code>false</code> otherwise. */
	protected boolean caseSensitive;

	/**
	 * General constructor.
	 * @param caseSensitive <code>true</code> if names should be compared in a case sensitive manner;
	 *                       <code>false</code> otherwise.
	 */
	public AbstractFileNodeNameComparator(boolean caseSensitive)
	{
		this.caseSensitive = caseSensitive;
	}

	/**
	 * Sorts the two {@link orioni.jz.io.files.abstractnode.AbstractFileNode} parameters by the filenames they claim to have.
	 * @param ds1 The first {@link orioni.jz.io.files.abstractnode.AbstractFileNode} to be compared.
	 * @param ds2 The second {@link orioni.jz.io.files.abstractnode.AbstractFileNode} to be compared.
	 * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or
	 *         greater than the second.
	 * @throws java.lang.ClassCastException If one or both of the arguments are not {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects.
	 */
	public int compare(AbstractFileNode ds1, AbstractFileNode ds2)
	{
		String s1 = ds1.getName();
		String s2 = ds2.getName();
		if (!caseSensitive)
		{
			s1 = s1.toLowerCase();
			s2 = s2.toLowerCase();
		}
		return s1.compareTo(s2);
	}
}