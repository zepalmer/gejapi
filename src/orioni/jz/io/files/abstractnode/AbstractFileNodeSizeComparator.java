package orioni.jz.io.files.abstractnode;

import java.util.Comparator;

/**
 * This {@link java.util.Comparator} implementation sorts {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects by their respective sizes in bytes.
 * @author Zachary Palmer
 */
public class AbstractFileNodeSizeComparator implements Comparator<AbstractFileNode>
{
	/** A singleton instance of the {@link orioni.jz.io.files.abstractnode.AbstractFileNodeSizeComparator}, for convenience. */
	public static final AbstractFileNodeSizeComparator SINGLETON = new AbstractFileNodeSizeComparator();

	/**
	 * General constructor.
	 */
	public AbstractFileNodeSizeComparator()
	{
	}

	/**
	 * Sorts the two {@link orioni.jz.io.files.abstractnode.AbstractFileNode} parameters by the sizes in bytes each reports.
	 * @param ds1 The first {@link orioni.jz.io.files.abstractnode.AbstractFileNode} to be compared.
	 * @param ds2 The second {@link orioni.jz.io.files.abstractnode.AbstractFileNode} to be compared.
	 * @return A negative integer, zero, or a positive integer as the first {@link orioni.jz.io.files.abstractnode.AbstractFileNode} is smaller than,
	 *         equal in size to, or larger than the second.
	 * @throws java.lang.ClassCastException If one or both of the arguments are not {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects.
	 */
	public int compare(AbstractFileNode ds1, AbstractFileNode ds2)
	{
		if (ds1.getSize() < ds2.getSize())
			return -1;
		else if (ds1.getSize() > ds2.getSize())
			return 1;
		else
			return 0;
	}
}