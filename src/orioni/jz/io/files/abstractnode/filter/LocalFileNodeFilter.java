package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;
import orioni.jz.io.files.abstractnode.LocalFileNode;

import java.io.FileFilter;

/**
 * This implementation of {@link AbstractFileNodeFilter} is used to allow a {@link FileFilter} to be used in place of an
 * {@link AbstractFileNodeFilter}.  This filter will reject all non-local files and accept either all local files or
 * only the local files provided by the specified filter.
 *
 * @author Zachary Palmer
 */
public class LocalFileNodeFilter implements AbstractFileNodeFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link FileFilter} to use on local files.
     */
    protected FileFilter fileFilter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that all local files will be accepted.
     */
    public LocalFileNodeFilter()
    {
        this(null);
    }

    /**
     * General constructor.
     *
     * @param filter The {@link FileFilter} to use on local files, or <code>null</code> to use all local files.
     */
    public LocalFileNodeFilter(FileFilter filter)
    {
        super();
        fileFilter = filter;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests whether or not the provided {@link AbstractFileNode} should be accepted.
     *
     * @param node The {@link AbstractFileNode} to test.
     * @return <code>true</code> if the provided node should be accepted; <code>false</code> otherwise.
     */
    public boolean accept(AbstractFileNode node)
    {
        return ((node instanceof LocalFileNode) &&
                ((fileFilter == null) || (fileFilter.accept(((LocalFileNode) node).getFile()))));
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE