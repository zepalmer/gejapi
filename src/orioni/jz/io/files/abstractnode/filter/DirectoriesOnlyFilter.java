package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

/**
 * This {@link AbstractFileNodeFilter} accepts all {@link AbstractFileNode}s representing directories.
 *
 * @author Zachary Palmer
 */
public class DirectoriesOnlyFilter implements AbstractFileNodeFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /** A singleton instance of this filter. */
    public static final DirectoriesOnlyFilter SINGLETON = new DirectoriesOnlyFilter();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public DirectoriesOnlyFilter()
    {
        super();
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
        return (node.isDirectory());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE