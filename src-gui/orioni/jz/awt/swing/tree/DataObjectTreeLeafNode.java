package orioni.jz.awt.swing.tree;

import orioni.jz.util.ArrayEnumeration;

import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.Enumeration;

/**
 * An implementation of {@link DataObjectTreeNode} which has no children.
 *
 * @author Zachary Palmer
 */
public class DataObjectTreeLeafNode <T> implements DataObjectTreeNode<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The data object for this node.
     */
    protected T data;
    /**
     * The parent {@link TreeNode} for this node.
     */
    protected TreeNode parent;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes with <code>null</code> as its data object.
     */
    public DataObjectTreeLeafNode()
    {
        this(null);
    }

    /**
     * General constructor.
     *
     * @param data The data object for this node.
     */
    public DataObjectTreeLeafNode(T data)
    {
        super();
        this.data = data;
        parent = null;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Provides the data object object for this {@link DataObjectTreeNode}.
     *
     * @return This {@link DataObjectTreeNode}'s data object object.
     */
    public T getDataObject()
    {
        return data;
    }

    /**
     * Changes the data object object for this {@link DataObjectTreeNode}.
     *
     * @param t The new data object object for this {@link DataObjectTreeNode}.
     */
    public void setDataObject(T t)
    {
        data = t;
    }

    /**
     * Returns an empty enumeration.
     */
    public Enumeration<TreeNode> children()
    {
        return new ArrayEnumeration<TreeNode>(new TreeNode[0]);
    }

    /**
     * Returns <code>false</code>, indicating that no children are allowed.
     */
    public boolean getAllowsChildren()
    {
        return false;
    }

    /**
     * Throws an {@link IndexOutOfBoundsException}, as there are never any children to this class of node.
     */
    public TreeNode getChildAt(int i)
    {
        throw new IndexOutOfBoundsException(
                "No children in a " + getClass().toString() + ": index " + i + " invalid.");
    }

    /**
     * Returns <code>0</code>: the number of children <code>TreeNode</code>s the receiver contains.
     */
    public int getChildCount()
    {
        return 0;
    }

    /**
     * Returns <code>-1</code>, indicating that the provided node is not a child of this node.
     */
    public int getIndex(TreeNode node)
    {
        return -1;
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public TreeNode getParent()
    {
        return parent;
    }

    /**
     * Returns <code>true</code>.
     */
    public boolean isLeaf()
    {
        return true;
    }

    /**
     * Returns the {@link Object#toString()} method of the data object for this node.
     *
     * @return A string representation of this node's data object.
     */
    public String toString()
    {
        return data.toString();
    }

    /**
     * Changes the data object for this {@link DataObjectTreeNode}.  If the provided object is of an incompatible type,
     * a {@link ClassCastException} may be thrown.
     *
     * @param o The new data object for this {@link DataObjectTreeNode}.
     * @throws ClassCastException If the provided method is not of type {@link T}.
     */
    public void setUserObject(Object o)
    {
        setDataObject((T)o);
    }

    /**
     * Ignored.  This node may not have children.
     */
    public void insert(MutableTreeNode child, int index)
    {
    }

    /**
     * Ignored.  This node never has children.
     */
    public void remove(int index)
    {
    }

    /**
     * Ignored.  This node never has children.
     */
    public void remove(MutableTreeNode node)
    {
    }

    /**
     * Removes the receiver from its parent.
     */
    public void removeFromParent()
    {
        if (getParent() != null)
        {
            ((MutableTreeNode)(getParent())).remove(this);
        }
    }

    /**
     * Sets the parent of the receiver to <code>parent</code>.
     * @param parent The new {@link MutableTreeNode} to use as parent to this node.
     */
    public void setParent(MutableTreeNode parent)
    {
        this.parent = parent;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE