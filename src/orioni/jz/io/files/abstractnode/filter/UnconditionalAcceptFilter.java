package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

/**
 * This class implements the {@link AbstractFileNodeFilter} interface, but simply accepts every {@link AbstractFileNode}
 * which is provided to it.
 *
 * @author Zachary Palmer
 */
public class UnconditionalAcceptFilter implements AbstractFileNodeFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of the {@link UnconditionalAcceptFilter}.
     */
    public static final UnconditionalAcceptFilter SINGLETON = new UnconditionalAcceptFilter();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public UnconditionalAcceptFilter()
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
        return true;
    }

    /**
     * Generates a hash code for the {@link UnconditionalAcceptFilter}.  Because all {@link UnconditionalAcceptFilter}s
     * are identical, the code returned is <code>0</code>.
     *
     * @return <code>0</code>, always.
     */
    public int hashCode()
    {
        return 0;
    }

    /**
     * Determines whether or not this {@link UnconditionalAcceptFilter} is equal to another one.  The answer is always
     * yes.
     *
     * @param o The object to which this {@link UnconditionalAcceptFilter} is to be compared.
     * @return <code>true</code> if the other object is an {@link UnconditionalAcceptFilter}; <code>false</code>
     *         otherwise.
     */
    public boolean equals(Object o)
    {
        return (o instanceof UnconditionalAcceptFilter);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE