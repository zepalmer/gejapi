package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.LocalFileNode;

import java.io.File;
import java.io.FileFilter;

/**
 * This class allows an {@link AbstractFileNodeFilter} to be used as a {@link FileFilter}; essentially, this class is
 * the opposite of the {@link LocalFileNodeFilter}.
 *
 * @author Zachary Palmer
 */
public class AbstractFileFilterConverter implements FileFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link AbstractFileNodeFilter} to use to filter files.
     */
    protected AbstractFileNodeFilter filter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param filter The {@link AbstractFileNodeFilter} to use to filter files.
     */
    public AbstractFileFilterConverter(AbstractFileNodeFilter filter)
    {
        super();
        this.filter = filter;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests whether or not the specified abstract pathname should be included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept(File pathname)
    {
        return filter.accept(new LocalFileNode(pathname));
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE