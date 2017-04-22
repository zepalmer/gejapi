package orioni.jz.io.files.abstractnode;

import orioni.jz.io.files.FileUtilities;

import java.io.File;

/**
 * This implementation of the {@link AbstractFileNode} interface is designed to function as a wrapper for {@link File}
 * objects.
 *
 * @author Zachary Palmer
 */
public class LocalFileNode extends AbstractFileNode
{
    /**
     * The {@link java.io.File} from which this {@link AbstractFileNode} instance gets its data.
     */
    protected File file;

    /**
     * General constructor.
     *
     * @param file The {@link java.io.File} from which this {@link AbstractFileNode} instance gets its data.
     */
    public LocalFileNode(File file)
    {
        this.file = file;
    }

    /**
     * Retrieves the {@link java.io.File} object backing this node.
     *
     * @return The {@link java.io.File} object backing this node.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Determines whether or not read operations are allowed against the abstract filesystem element represented by this
     * {@link AbstractFileNode} object.
     *
     * @return <code>true</code> if reading is allowed; <code>false</code> if it is not.
     */
    public Permission getReadPermission()
    {
        return (file.canRead() ? Permission.GRANTED : Permission.DENIED);
    }

    /**
     * Determines whether or not write operations are allowed against the abstract filesystem element represented by
     * this {@link AbstractFileNode} object.
     *
     * @return <code>true</code> if writing is allowed; <code>false</code> if it is not.
     */
    public Permission getWritePermission()
    {
        return (file.canWrite() ? Permission.GRANTED : Permission.DENIED);
    }

    /**
     * Retrieves the {@link AbstractFileNode} objects which represent the children of this {@link AbstractFileNode}.
     *
     * @return A {@link AbstractFileNode}<code>[]</code> containing the children of this {@link AbstractFileNode}.  If
     *         this {@link AbstractFileNode} does not represent a directory, a {@link AbstractFileNode}<code>[0]</code>
     *         is returned.
     */
    public AbstractFileNode[] getChildren()
    {
        File[] files = file.listFiles();
        if (files == null) return new AbstractFileNode[0];
        AbstractFileNode[] ret = new AbstractFileNode[files.length];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = new LocalFileNode(files[i]);
        }
        return ret;
    }

    /**
     * Retrieves the name (not the full path) of the abstract filesystem element represented by this {@link
     * AbstractFileNode}.
     *
     * @return The name of this {@link AbstractFileNode}.
     */
    public String getName()
    {
        return (file.getName().length() == 0) ? file.getPath() : file.getName();
    }

    /**
     * Returns <code>true</code> if the filesystem on which this node is based is case sensitive or <code>false</code>
     * if it is not.
     *
     * @return Whether or not the filesystem on which this node is based is case sensitive.
     */
    public boolean isFilesystemCaseSensitive()
    {
        return FileUtilities.FILESYSTEM_CASE_SENSITIVE;
    }

    /**
     * Retrieves the parent in the abstract filesystem tree for this {@link AbstractFileNode}.
     *
     * @return The parent for this {@link AbstractFileNode}, or <code>null</code> if this {@link AbstractFileNode} does
     *         not have a parent.
     */
    public AbstractFileNode getParent()
    {
        File parent = file.getParentFile();
        if (parent == null) return null; else return new LocalFileNode(parent);
    }

    /**
     * Retrieves the size of this {@link AbstractFileNode} in bytes.  If this {@link AbstractFileNode} is a directory,
     * it still must return a value; this value, however, can be <code>0</code>.
     */
    public long getSize()
    {
        return file.length();
    }

    /**
     * Determines whether or not the abstract filesystem element represented by this {@link AbstractFileNode} object is
     * a directory.
     *
     * @return <code>true</code> if this object represents a directory; <code>false</code> otherwise.
     */
    public boolean isDirectory()
    {
        return file.isDirectory();
    }

    /**
     * Determines whether or not two {@link LocalFileNode} objects are identical.
     *
     * @param o The {@link Object} to compare to this one.
     * @return <code>true</code> if and only if the other object is also a {@link LocalFileNode} and both objects refer
     *         to the same {@link File} object.
     */
    public boolean equals(Object o)
    {
        if (o instanceof LocalFileNode)
        {
            return (((LocalFileNode) o).getFile().equals(this.getFile()));
        } else
        {
            return false;
        }
    }

    /**
     * Returns a hash code for this object.
     * @return This object's hash code.
     */
    public int hashCode()
    {
        return getFile().hashCode();
    }

    /**
     * Creates a display for this {@link LocalFileNode}.
     *
     * @return A {@link String} describing this {@link LocalFileNode}.
     */
    public String toString()
    {
        return getName();
    }

    /**
     * This method mirrors the {@link File#listRoots()} method, listing the filesystem roots for this system as {@link
     * LocalFileNode}s.
     *
     * @return An array of {@link LocalFileNode}s, each representing a filesystem root.
     */
    public static LocalFileNode[] listRoots()
    {
        File[] roots = File.listRoots();
        LocalFileNode[] nodes = new LocalFileNode[roots.length];
        for (int i = 0; i < roots.length; i++)
        {
            nodes[i] = new LocalFileNode(roots[i]);
        }
        return nodes;
    }
}