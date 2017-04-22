package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

/**
 * This interface is implemented by all classes which are capable of filtering {@link AbstractFileNode}s for some
 * purpose.  This interface is modeled after the {@link java.io.FileFilter} interface.
 *
 * @author Zachary Palmer
 */
public interface AbstractFileNodeFilter
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests whether or not the provided {@link AbstractFileNode} should be accepted.
     * @param node The {@link AbstractFileNode} to test.
     * @return <code>true</code> if the provided node should be accepted; <code>false</code> otherwise.
     */
    public boolean accept(AbstractFileNode node);

}

// END OF FILE