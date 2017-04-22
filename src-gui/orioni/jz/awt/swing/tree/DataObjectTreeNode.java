package orioni.jz.awt.swing.tree;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * This interface is used to represent any {@link TreeNode} which represents itself based upon an arbitrary user-based
 * object.  The said node must be able to provide this object on request and change it at any time.
 * <p/>
 * Data object tree nodes must also be capable of accepting modifications to their parent references.
 *
 * @author Zachary Palmer
 */
public interface DataObjectTreeNode <T> extends TreeNode, MutableTreeNode
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Provides the data object for this {@link DataObjectTreeNode}.
     *
     * @return This {@link DataObjectTreeNode}'s data object.
     */
    public T getDataObject();

    /**
     * Changes the data object for this {@link DataObjectTreeNode}.
     *
     * @param t The new data object for this {@link DataObjectTreeNode}.
     */
    public void setDataObject(T t);

    /**
     * Changes the data object for this {@link DataObjectTreeNode}.  If the provided object is of an incompatible type,
     * a {@link ClassCastException} may be thrown.
     *
     * @param o The new data object for this {@link DataObjectTreeNode}.
     * @throws ClassCastException If the provided method is not of type {@link T}.
     */
    public void setUserObject(Object o);
}

// END OF FILE