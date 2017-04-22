package orioni.jz.awt.swing;

import orioni.jz.util.ArrayEnumeration;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

/**
 * This implementation of {@link TreeNode} accepts an initial, immutable set of {@link TreeNode} children and reports
 * on them as per the {@link TreeNode} interface.  Notice that this cannot be used to create an entire tree, since the
 * node needs both its parent and children in the constructor.
 * @author Zachary Palmer
 */
public class DefaultTreeNode implements TreeNode
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The parent of this {@link TreeNode}. */
    protected TreeNode parent;
    /** The children of this {@link TreeNode}. */
    protected TreeNode[] children;
    /** The {@link String} for this {@link TreeNode}. */
    protected String string;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param parent The parent of this {@link TreeNode}.
     * @param children The children of this {@link TreeNode}.  If this node should not allow children, this value should
     *                 be <code>null</code>.
     * @param string The string to return on calls to {@link #toString} on this node.
     */
    public DefaultTreeNode(TreeNode parent, TreeNode[] children, String string)
    {
        super();
        this.parent = parent;
        this.children = children;
        this.string = string;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// NON-STATIC METHODS : TREE NODE IMPLEMENTATION /////////////////////////////////

    /**
     * Returns the children of this node as an {@link Enumeration}.
     * @return The specified {@link Enumeration}.
     */
    public Enumeration children()
    {
        if (children == null)
        {
            return new ArrayEnumeration<TreeNode>(new TreeNode[0]);
        } else
        {
            return new ArrayEnumeration<TreeNode>(children);
        }
    }

    /**
     * Determines if this node allows children.
     * @return <code>true</code> if this node allows children.
     */
    public boolean getAllowsChildren()
    {
        return (children != null);
    }

    /**
     * Returns the child {@link TreeNode} at the specified index.
     * @param index The index of the child to return.
     * @return The specified child.
     */
    public TreeNode getChildAt(int index)
    {
        return children[index];
    }

    /**
     * Returns the number of children that this node contains.
     * @return The number of children that this node contains.
     */
    public int getChildCount()
    {
        return children.length;
    }

    /**
     * Returns the index of the specified node among the children of this node.  If the specified node is not a child of
     * this node, <code>-1</code> is returned.
     * @param node The node for which to search.
     * @return The index of the specified node, or <code>-1</code> if that node was not found.
     */
    public int getIndex(TreeNode node)
    {
        for (int i = 0; i < children.length; i++)
        {
            if (node.equals(children[i])) return i;
        }
        return -1;
    }

    /**
     * Returns the parent of this node.
     * @return The parent node.
     */
    public TreeNode getParent()
    {
        return parent;
    }

    /**
     * Determines if this node is a leaf.
     * @return <code>true</code> if this node is a leaf.
     */
    public boolean isLeaf()
    {
        return (children == null);
    }

    /**
     * Returns the string provided on construction.
     * @return The string provided on construction.
     */
    public String toString()
    {
        return string;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}