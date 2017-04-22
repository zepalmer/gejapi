package orioni.jz.util;

import java.io.Serializable;
import java.util.*;

/**
 * This class is designed to represent a single node in a tree.  A single node has the following properties: <ol> <li>An
 * element of data of type <code>DataType</code>.</li> <li>A set of children, linked to this node by data of type
 * <code>LinkType</code>.</li> <li>A guarantee that all descendents have the same parameterized types as this node.</li>
 * </ol>
 *
 * @author Zachary Palmer
 */
public class TreeNode<LinkType, DataType> implements Serializable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * Force serialization version.
     */
    public static final long serialVersionUID = 0x1;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Contains the mappings to children of this node.
     */
    protected Map<LinkType, TreeNode<LinkType, DataType>> childrenMap;
    /**
     * Contains the data at this node.
     */
    protected DataType data;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes <code>null</code> data and no children initially.
     */
    public TreeNode()
    {
        this(null);
    }

    /**
     * General constructor.  Assumes no children initially.
     *
     * @param data The data at this node.
     */
    public TreeNode(DataType data)
    {
        this.data = data;
        childrenMap = new HashMap<LinkType, TreeNode<LinkType, DataType>>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the data stored at this node.
     *
     * @return The data stored at this node.
     */
    public DataType getData()
    {
        return data;
    }

    /**
     * Changes the data stored at this node.
     *
     * @param data The new data for this node.
     * @return The old data from this node.
     */
    public DataType setData(DataType data)
    {
        DataType ret = this.data;
        this.data = data;
        return ret;
    }

    /**
     * Adds a child node to this node.
     *
     * @param link The data used to link the child to this node.
     * @param node The child node.
     * @return The old child node at that position, or <code>null</code> if no such child exists.
     */
    public TreeNode<LinkType, DataType> addChild(LinkType link, TreeNode<LinkType, DataType> node)
    {
        return childrenMap.put(link, node);
    }

    /**
     * Adds a child node to this node using the provided data element.
     *
     * @param link The data used to link the child to this node.
     * @param data The data to store in the new child node.
     * @return The old child node at that position, or <code>null</code> if no such child exists.
     */
    public TreeNode<LinkType, DataType> addChild(LinkType link, DataType data)
    {
        return addChild(link, new TreeNode<LinkType, DataType>(data));
    }

    /**
     * Retrieves the child linked to this node by the specified data.
     *
     * @param link The link data identifying the child.
     * @return The child node, or <code>null</code> if no such child exists.
     */
    public TreeNode<LinkType, DataType> getChild(LinkType link)
    {
        return childrenMap.get(link);
    }

    /**
     * Removes the child linked by the provided data.
     *
     * @param link The link data identifying the child.
     * @return The child node which was removed, or <code>null</code> if no such child exists.
     */
    public TreeNode<LinkType, DataType> removeChild(LinkType link)
    {
        return childrenMap.remove(link);
    }

    /**
     * Retrieves all children of this node.
     *
     * @return All of the children of this node.
     */
    public Collection<TreeNode<LinkType, DataType>> getChildren()
    {
        return Collections.unmodifiableCollection(childrenMap.values());
    }

    /**
     * Removes all children from this node.
     */
    public void clear()
    {
        childrenMap.clear();
    }

    /**
     * Retrieves the number of children of this node.
     *
     * @return The number of children of this node.
     */
    public int getChildCount()
    {
        return childrenMap.size();
    }

    /**
     * Retrieves the number of nodes in the tree represented by this node, excluding this node itself.
     *
     * @return The number of descendents for this node.
     */
    public int getDescendentCount()
    {
        int count = getChildCount();
        for (TreeNode<LinkType, DataType> child : getChildren())
        {
            count += child.getDescendentCount();
        }
        return count;
    }

    /**
     * Retrieves the data which links the provided child to this parent node.
     *
     * @param child The child in question.
     * @return The link data, or <code>null</code> if the provided node is not a child of this node.
     */
    public LinkType getLink(TreeNode<LinkType, DataType> child)
    {
        for (Map.Entry<LinkType, TreeNode<LinkType, DataType>> entry : childrenMap.entrySet())
        {
            if (entry.getValue().equals(child)) return entry.getKey();
        }
        return null;
    }

    /**
     * Retrieves a {@link Set} containing all <code>LinkType</code> used to link children to this node.
     *
     * @return The {@link Set} of link data linking children to this node.
     */
    public Set<LinkType> getLinkData()
    {
        return Collections.unmodifiableSet(childrenMap.keySet());
    }

    /**
     * Determines whether or not this {@link TreeNode} is equal to another object.  This is the case if the following
     * conditions are met: <ol> <li>The other object is also a {@link TreeNode}.</li> <li>The data objects for both
     * nodes are equal.</li> <li>Both nodes have equal children mapped using equal link data.</li> </ol> Essentially, it
     * is required that both nodes represent the same tree.
     *
     * @param o The other object.
     * @return <code>true</code> if the two nodes are equal; <code>false</code> if they are not.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TreeNode treeNode = (TreeNode) o;

        if (!childrenMap.equals(treeNode.childrenMap)) return false;
        return !(data != null ? !data.equals(treeNode.data) : treeNode.data != null);
    }

    /**
     * Generates a hash code for this node.
     *
     * @return A hash code for this node.
     */
    public int hashCode()
    {
        int result;
        result = childrenMap.hashCode();
        result = 29 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
