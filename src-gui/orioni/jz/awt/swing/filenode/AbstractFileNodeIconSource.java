package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

import javax.swing.*;

/**
 * This interface is applied to any class which is capable of providing an icon for {@link AbstractFileNode}s.  An
 * {@link AbstractFileNode} is provided and it is this object's duty to provide an icon which can represent that object
 * in a tree, table, or other such GUI component.
 *
 * @author Zachary Palmer
 */
public interface AbstractFileNodeIconSource
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns an icon for the provided {@link AbstractFileNode}.
     * @param node The {@link AbstractFileNode} for which an icon should be generated.
     * @return The created {@link Icon}, or <code>null</code> if the node should not have an icon.  The latter is
     * acceptable as well if no suitable icon could be generated.
     */
    public Icon getAbstractNodeIcon(AbstractFileNode node);

}

// END OF FILE