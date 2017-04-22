package orioni.jz.io.files.abstractnode;

import java.util.Comparator;

/**
 * This {@link java.util.Comparator} implementation sorts {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects by their repsective permissions states.
 * Read permission takes priority over write permission, a granted permission takes priority over an unknown
 * permission, and an unknown permission takes priority over a denied permission.
 *
 * @author Zachary Palmer
 */
public class AbstractFileNodePermissionComparator implements Comparator<AbstractFileNode>
{
	/** A singleton instance of the {@link orioni.jz.io.files.abstractnode.AbstractFileNodePermissionComparator}, for convenience. */
	public static final AbstractFileNodePermissionComparator SINGLETON = new AbstractFileNodePermissionComparator();

	/**
	 * General constructor.
	 */
	public AbstractFileNodePermissionComparator()
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
		// Check read permission first.
		switch (ds1.getReadPermission())
		{
			case GRANTED:
				if (ds2.getReadPermission() == AbstractFileNode.Permission.GRANTED)
				{
					return checkWritePermissions(ds1, ds2);
				} else
					return 1;
			case UNKNOWN:
				switch (ds2.getReadPermission())
				{
					case GRANTED:
						return -1;
					case UNKNOWN:
						return checkWritePermissions(ds1, ds2);
					case DENIED:
						return 1;
				}
				break;
			case DENIED:
				if (ds2.getReadPermission() == AbstractFileNode.Permission.GRANTED)
				{
					return checkWritePermissions(ds1, ds2);
				} else
					return 1;
		}
		return 0;
	}

	private static int checkWritePermissions(AbstractFileNode ds1, AbstractFileNode ds2)
	{
		switch (ds1.getWritePermission())
		{
			case GRANTED:
				if (ds2.getWritePermission() == AbstractFileNode.Permission.GRANTED)
					return 0;
				else
					return 1;
			case UNKNOWN:
				switch (ds2.getWritePermission())
				{
					case GRANTED:
						return -1;
					case UNKNOWN:
						return 0;
					case DENIED:
						return 1;
				}
				break;
			case DENIED:
				if (ds2.getWritePermission() == AbstractFileNode.Permission.DENIED)
					return 0;
				else
					return -1;
		}
		return 0;
	}
}