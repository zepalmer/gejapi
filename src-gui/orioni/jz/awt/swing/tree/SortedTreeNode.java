package orioni.jz.awt.swing.tree;

import orioni.jz.util.ArrayEnumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * This {@link TreeNode} implementation is designed to represent a tree node with a pre-indicated method for sorting its
 * children.  It is also mutable, allowing the contents to be resorted after construction.  Note that the fact that a
 * node is a {@link SortedTreeNode} has no effect on how it itself appears in the tree's model; the parent node must
 * also be a {@link SortedTreeNode} for the given node to appear in a sorted order.
 * <p/>
 * {@link SortedTreeNode}s may only have {@link DataObjectTreeNode}s as their children.  This is because the ordering of
 * the children is dependent upon the tree's data object.
 * <p/>
 * The {@link SortedTreeNode} class is capable of accepting a {@link Comparator} for sorting purposes.  If one is not
 * provided, the data objects of its children must be {@link Comparable}.
 *
 * @author Zachary Palmer
 */
public class SortedTreeNode <DataType, ChildDataType> implements DataObjectTreeNode<DataType>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An empty {@link TreeNode}<code>[]</code>.
     */
    protected static final TreeNode[] EMPTY_TREE_NODE_ARRAY = new TreeNode[0];

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The data object for this node.
     */
    protected DataType data;
    /**
     * The parent for this node.
     */
    protected TreeNode parent;
    /**
     * The {@link SortedSet} of children for this node.
     */
    protected SortedSet<DataObjectTreeNode<ChildDataType>> children;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes this node with a <code>null</code> data object.  Also, assumes that children
     * will be sorted based upon their "natural ordering" (i.e., {@link Comparable#compareTo(Object)} methods).  For
     * this constructor to function properly, all child nodes' data types must implement {@link Comparable}.
     */
    public SortedTreeNode()
    {
        this((DataType) (null));
    }

    /**
     * Skeleton constructor.  Assumes that children will be sorted based upon their "natural ordering" (i.e., {@link
     * Comparable#compareTo(Object)} methods).  For this constructor to function properly, all child nodes' data types
     * must implement {@link Comparable}.
     *
     * @param data The data object for this node.
     */
    public SortedTreeNode(DataType data)
    {
        super();
        parent = null;
        this.data = data;
        children = new TreeSet<DataObjectTreeNode<ChildDataType>>();
    }

    /**
     * Skeleton constructor.  Initializes this node with a <code>null</code> data object.
     *
     * @param comparator The {@link Comparator} which will be used to sort the children of this node.
     */
    public SortedTreeNode(Comparator<ChildDataType> comparator)
    {
        this(null, comparator);
    }

    /**
     * General constructor.
     *
     * @param data       The data object for this node.
     * @param comparator The {@link Comparator} which will be used to sort the children of this node.
     */
    public SortedTreeNode(DataType data, Comparator<ChildDataType> comparator)
    {
        super();
        parent = null;
        this.data = data;
        children = new TreeSet<DataObjectTreeNode<ChildDataType>>(
                new DataObjectTreeNodeComparator<ChildDataType>(comparator));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a child to this node.
     *
     * @param child The child to add.
     * @return The index of the new child.
     */
    public int addChild(DataObjectTreeNode<ChildDataType> child)
    {
        children.add(child);
        child.setParent(this);
        return getIndex(child);
    }

    /**
     * Removes all children from this node.
     */
    public void clearChildren()
    {
        for (DataObjectTreeNode<ChildDataType> child : children)
        {
            child.setParent(null);
        }
        children.clear();
    }

    /**
     * Obtains the child with the specified data object.  If multiple such children exist, the first child found is
     * returned.
     *
     * @param data The data object in question.
     * @return The first child found with the specified data object, or <code>null</code> if no such child exists.
     */
    public DataObjectTreeNode<ChildDataType> getChild(ChildDataType data)
    {
        for (DataObjectTreeNode<ChildDataType> node : children)
        {
            if (node.getDataObject().equals(data)) return node;
        }
        return null;
    }

    /**
     * Retrieves all children of this {@link SortedTreeNode}.
     *
     * @return The children of this {@link SortedTreeNode}.
     */
    public Set<DataObjectTreeNode<ChildDataType>> getChildren()
    {
        return Collections.unmodifiableSet(children);
    }

    /**
     * Removes a child from this node.
     *
     * @param child The child to remove.
     * @return <code>true</code> if the child was removed; <code>false</code> if the given node was not a child of this
     *         node.
     */
    public boolean removeChild(DataObjectTreeNode<ChildDataType> child)
    {
        if (child==null)
        {
            return false;
        }
        if (!children.remove(child)) return false;
        child.setParent(null);
        return true;
    }

    /**
     * Provides the data object object for this {@link DataObjectTreeNode}.
     *
     * @return This {@link DataObjectTreeNode}'s data object object.
     */
    public DataType getDataObject()
    {
        return data;
    }

    /**
     * Changes the data object object for this {@link DataObjectTreeNode}.
     *
     * @param data The new data object object for this {@link DataObjectTreeNode}.
     */
    public void setDataObject(DataType data)
    {
        this.data = data;
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    public Enumeration<TreeNode> children()
    {
        return new ArrayEnumeration<TreeNode>(children.toArray(EMPTY_TREE_NODE_ARRAY));
    }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren()
    {
        return true;
    }

    /**
     * Returns the child <code>TreeNode</code> at index <code>index</code>.
     *
     * @param index The index of the child to obtain.
     */
    public TreeNode getChildAt(int index)
    {
        return children.toArray(EMPTY_TREE_NODE_ARRAY)[index];
    }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver contains.
     */
    public int getChildCount()
    {
        return children.size();
    }

    /**
     * Returns the index of <code>node</code> in the receivers children. If the receiver does not contain
     * <code>node</code>, -1 will be returned.
     */
    public int getIndex(TreeNode node)
    {
        if (children.contains(node))
        {
            int i = 0;
            for (TreeNode child : children)
            {
                if (node.equals(child)) return i;
                i++;
            }
        }
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
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf()
    {
        return false;
    }

    /**
     * Returns the {@link Object#toString()} method of the data object for this node.
     *
     * @return A string representation of this node's data object.
     */
    public String toString()
    {
        return String.valueOf(data);
    }

    /**
     * Changes the data object for this {@link DataObjectTreeNode}.  If the provided object is of an incompatible type,
     * a {@link ClassCastException} may be thrown.
     *
     * @param o The new data object for this {@link DataObjectTreeNode}.
     * @throws ClassCastException If the provided method is not of type {@link DataType}.
     */
    public void setUserObject(Object o)
    {
        setDataObject((DataType) o);
    }

    /**
     * Adds <code>child</code> to the receiver at <code>index</code>. <code>child</code> will be messaged with
     * <code>setParent</code>.  Please note that, as the order of the nodes is implied by a function other than
     * arbitrary indexing, the index parameter for this method will be ignored.  The provided {@link MutableTreeNode}
     * must be a {@link DataObjectTreeNode} or a {@link ClassCastException} will be thrown.
     */
    public void insert(MutableTreeNode child, int index)
    {
        DataObjectTreeNode<ChildDataType> castedChild = (DataObjectTreeNode<ChildDataType>) child;
        castedChild.setParent(this);
        children.add(castedChild);
    }

    /**
     * Removes the child at <code>index</code> from the receiver.
     */
    public void remove(int index)
    {
        MutableTreeNode node = (MutableTreeNode) (getChildAt(index)); // all children must be MutableTreeNodes
        remove(node);
    }

    /**
     * Removes <code>node</code> from the receiver. <code>setParent</code> will be messaged on <code>node</code>.
     */
    public void remove(MutableTreeNode node)
    {
        if (node == null) return;
        node.setParent(null);
        children.remove(node);
    }

    /**
     * Removes the receiver from its parent.
     */
    public void removeFromParent()
    {
        if (getParent() != null)
        {
            ((MutableTreeNode) (getParent())).remove(this);
        }
    }

    /**
     * Sets the parent of the receiver to <code>parent</code>.
     */
    public void setParent(MutableTreeNode parent)
    {
        this.parent = parent;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * A comparator for {@link DataObjectTreeNode}s which sorts those nodes based upon the sorting of their data
     * objects.  This {@link Comparator} is dependent upon another comparator for its actual sorting operations and
     * simply provides as a means by which the contents of the {@link DataObjectTreeNode}s can be transparently
     * extracted.
     *
     * @author Zachary Palmer
     */
    static class DataObjectTreeNodeComparator <T> implements Comparator<DataObjectTreeNode<T>>
    {
        /**
         * The {@link Comparator} to use when comparing the nodes' data objects.
         */
        protected Comparator<T> comparator;

        /**
         * General constructor.
         *
         * @param comparator The {@link Comparator} to use when comparing the nodes' data objects.
         */
        public DataObjectTreeNodeComparator(Comparator<T> comparator)
        {
            this.comparator = comparator;
        }

        /**
         * Compares two {@link DataObjectTreeNode}s based upon their data objects.
         *
         * @param nodeA The first node to be compared.
         * @param nodeB The second node to be compared.
         * @return A negative integer, zero, or a positive integer as the first node's data object is less than, equal
         *         to, or greater than the second's.
         */
        public int compare(DataObjectTreeNode<T> nodeA, DataObjectTreeNode<T> nodeB)
        {
            return comparator.compare(nodeA.getDataObject(), nodeB.getDataObject());
        }
    }
}

// END OF FILE