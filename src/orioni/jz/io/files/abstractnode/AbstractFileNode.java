package orioni.jz.io.files.abstractnode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This interface is used as the source of data for abstract filesystem views.  This implementation is necessary to
 * allow a view of filesystems in a generic fashion rather than assuming that the files exist locally.
 *
 * @author Zachary Palmer
 */
public abstract class AbstractFileNode implements Serializable, Comparable<AbstractFileNode>
{
    /**
     * An empty {@link AbstractFileNode}<code>[]</code>.
     */
    public static final AbstractFileNode[] EMPTY_FILE_NODE_ARRAY = new AbstractFileNode[0];

    /**
     * An enumeration describing permissions.
     */
    public static enum Permission
    {
        GRANTED, DENIED, UNKNOWN
    }

    /**
     * Determines whether or not the abstract filesystem element represented by this {@link AbstractFileNode} object is
     * a directory.
     *
     * @return <code>true</code> if this object represents a directory; <code>false</code> otherwise.
     */
    public abstract boolean isDirectory();

    /**
     * Retrieves the name (not the full path) of the abstract filesystem element represented by this {@link
     * AbstractFileNode}.
     *
     * @return The name of this {@link AbstractFileNode}.
     */
    public abstract String getName();

    /**
     * Returns <code>true</code> if the filesystem on which this node is based is case sensitive or <code>false</code>
     * if it is not.
     *
     * @return Whether or not the filesystem on which this node is based is case sensitive.
     */
    public abstract boolean isFilesystemCaseSensitive();

    /**
     * Retrieves the parent in the abstract filesystem tree for this {@link AbstractFileNode}.
     *
     * @return The parent for this {@link AbstractFileNode}, or <code>null</code> if this {@link AbstractFileNode} does
     *         not have a parent.
     */
    public abstract AbstractFileNode getParent();

    /**
     * Retrieves the {@link AbstractFileNode} objects which represent the children of this {@link AbstractFileNode}.
     *
     * @return A {@link AbstractFileNode}<code>[]</code> containing the children of this {@link AbstractFileNode}.  If
     *         this {@link AbstractFileNode} does not represent a directory, a {@link AbstractFileNode}<code>[0]</code>
     *         is returned.
     */
    public abstract AbstractFileNode[] getChildren();

    /**
     * Retrieves the {@link AbstractFileNode} objects which represent the children of this {@link AbstractFileNode} in a
     * sorted array.
     *
     * @return A sorted {@link AbstractFileNode}<code>[]</code> containing the children of this {@link
     *         AbstractFileNode}.  If this {@link AbstractFileNode} does not represent a directory, a {@link
     *         AbstractFileNode}<code>[0]</code> is returned.
     */
    public AbstractFileNode[] getSortedChildren()
    {
        AbstractFileNode[] ret = getChildren();
        Arrays.sort(ret);
        return ret;
    }

    /**
     * Retrieves the size of this {@link AbstractFileNode} in bytes.  If this {@link AbstractFileNode} is a directory,
     * it still must return a value; this value, however, can be <code>0</code>.
     */
    public abstract long getSize();

    /**
     * Determines whether or not read operations are allowed against the abstract filesystem element represented by this
     * {@link AbstractFileNode} object.
     *
     * @return One of the {@link Permission} constants defined by this interface.
     */
    public abstract Permission getReadPermission();

    /**
     * Determines whether or not write operations are allowed against the abstract filesystem element represented by
     * this {@link AbstractFileNode} object.
     *
     * @return One of the {@link Permission} constants defined by this interface.
     */
    public abstract Permission getWritePermission();

    /**
     * Retrieves this {@link AbstractFileNode}'s pathname as a series of directory names separated by a separation
     * character and terminated in this node's name.
     *
     * @param separator The separator character to use.
     */
    public String getUnixStylePathname(char separator)
    {
        if (getParent() == null)
        {
            return String.valueOf(separator);
        } else
        {
            if (getParent().getParent() == null)
            {
                return getParent().getUnixStylePathname(separator) + getName();
            } else
            {
                return getParent().getUnixStylePathname(separator) + separator + getName();
            }
        }
    }

    /**
     * Retrieves all ancestors of this {@link AbstractFileNode}.
     *
     * @return The ancestors of this {@link AbstractFileNode}.  They are returned in the form of an ordered list.  The
     *         first element in the list is the furthest ancestor; the last element in the list is the immediate
     *         parent.
     */
    public List<AbstractFileNode> getAncestors()
    {
        LinkedList<AbstractFileNode> list = new LinkedList<AbstractFileNode>();
        AbstractFileNode parent = getParent();
        while (parent != null)
        {
            list.add(0, parent);
            parent = parent.getParent();
        }
        return list;
    }

    /**
     * Compares this abstract file node to another based upon the two nodes' pathname strings.  The comparison is
     * performed in a case-insensitive manner.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(AbstractFileNode o)
    {
        return this.getUnixStylePathname('/').toLowerCase().compareTo(o.getUnixStylePathname('/').toLowerCase());
    }
}