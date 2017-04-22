package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

/**
 * This {@link AbstractFileNodeFilter} combines the results of a number of other {@link AbstractFileNodeFilter}s,
 * accepting the provided {@link AbstractFileNode} if any sub-filter accepts it.
 *
 * @author Zachary Palmer
 */
public class OrFilter implements AbstractFileNodeFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The subfilters used in this filter. */
    protected AbstractFileNodeFilter[] filters;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param filters The {@link AbstractFileNodeFilter}s to use in this filter.
     */
    public OrFilter(AbstractFileNodeFilter... filters)
    {
        super();
        this.filters = filters;
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
        for (AbstractFileNodeFilter filter : filters)
        {
            if (filter.accept(node)) return true;
        }
        return false;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE