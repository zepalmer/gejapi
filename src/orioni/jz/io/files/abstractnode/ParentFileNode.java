package orioni.jz.io.files.abstractnode;



/**
 * This implementation of the {@link AbstractFileNode} interface is meant to act simply as a "parent directory"
 * instance.  It wraps another {@link AbstractFileNode}, providing most of the functionality of the
 * {@link AbstractFileNode} interface from the instance provided on construction.  The primary difference is the result
 * of the {@link ParentFileNode#getName()} method, which returns the string "<code>..</code>" to indicate a relative
 * parent directory.
 *
 * @author Zachary Palmer
 */
public class ParentFileNode extends AbstractFileNode
{
	/** The actual parent {@link AbstractFileNode}. */
	protected AbstractFileNode dataSource;

	/**
	 * General constructor.
	 * @param dataSource The actual parent {@link AbstractFileNode}.  Most of the information will be drawn from this
	 *                    object.
	 */
	public ParentFileNode(AbstractFileNode dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * Returns the {@link AbstractFileNode#getReadPermission()} results provided by the contained
	 * {@link AbstractFileNode}.
	 * @return One of the <code>PERMISSION_XXXX</code> constants defined by this interface.
	 */
	public Permission getReadPermission()
	{
		return dataSource.getReadPermission();
	}

	/**
	 * Returns the {@link AbstractFileNode#getWritePermission()} results provided by the contained
	 * {@link AbstractFileNode}.
	 * @return One of the <code>PERMISSION_XXXX</code> constants defined by this interface.
	 */
	public Permission getWritePermission()
	{
		return dataSource.getWritePermission();
	}

	/**
	 * Returns the {@link AbstractFileNode#getChildren()} results provided by the contained {@link AbstractFileNode}.
	 * @return A {@link AbstractFileNode}<code>[]</code> containing the children of this {@link AbstractFileNode}.  If
	 *         this {@link AbstractFileNode} does not represent a directory, a {@link AbstractFileNode}<code>[0]</code>
	 *         is returned.
	 */
	public AbstractFileNode[] getChildren()
	{
		return dataSource.getChildren();
	}

	/**
	 * Returns "<code>..</code>": the name of the parent from the point of view of the child.
	 * {@link AbstractFileNode}.
	 * @return The name of this {@link AbstractFileNode}.
	 */
	public String getName()
	{
		return "..";
	}

	/**
	 * Returns the {@link AbstractFileNode#isFilesystemCaseSensitive()} results provided by the contained
	 * {@link AbstractFileNode}.
	 * @return Whether or not the filesystem on which this node is based is case sensitive.
	 */
	public boolean isFilesystemCaseSensitive()
	{
		return dataSource.isFilesystemCaseSensitive();
	}

	/**
	 * Returns the {@link AbstractFileNode#getParent()} results provided by the contained {@link AbstractFileNode}.
	 * @return The parent for this {@link AbstractFileNode}, or <code>null</code> if this {@link AbstractFileNode} does
	 *         not have a parent.
	 */
	public AbstractFileNode getParent()
	{
		return dataSource.getParent();
	}

	/**
	 * Returns the {@link AbstractFileNode#getSize()} results provided by the contained {@link AbstractFileNode}.
	 * must return a value; this value, however, can be <code>0</code>.
	 */
	public long getSize()
	{
		return dataSource.getSize();
	}

	/**
	 * Returns the {@link AbstractFileNode#isDirectory()} results provided by the contained {@link AbstractFileNode}.
	 * This will hopefully return <code>true</code>.
	 * @return <code>true</code> if this object represents a directory; <code>false</code> otherwise.
	 */
	public boolean isDirectory()
	{
		return dataSource.isDirectory();
	}

    /**
     * Retrieves the actual wrapped file node.
     * @return The data source provided upon construction.
     */
    public AbstractFileNode getWrappedNode()
    {
        return dataSource;
    }
}