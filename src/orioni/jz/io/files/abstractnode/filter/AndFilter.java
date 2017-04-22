package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

/**
 * This {@link AbstractFileNodeFilter} combines the results of a number of other {@link AbstractFileNodeFilter}s,
 * accepting the provided {@link AbstractFileNode} only if all sub-filters accept it.
 *
 * @author Zachary Palmer
 */
public class AndFilter implements AbstractFileNodeFilter
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
    public AndFilter(AbstractFileNodeFilter... filters)
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
            if (!filter.accept(node)) return false;
        }
        return true;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE