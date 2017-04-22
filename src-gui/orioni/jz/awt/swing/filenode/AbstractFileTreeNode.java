package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;
import orioni.jz.io.files.abstractnode.filter.AbstractFileNodeFilter;
import orioni.jz.io.files.abstractnode.filter.DirectoriesOnlyFilter;
import orioni.jz.io.files.abstractnode.filter.UnconditionalAcceptFilter;
import orioni.jz.util.ArrayEnumeration;
import orioni.jz.util.ListEnumeration;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This implementation of {@link TreeNode} accepts an {@link AbstractFileNode} in its constructor and renders the
 * associated tree appropriately.
 *
 * @author Zachary Palmer
 */
public class AbstractFileTreeNode implements TreeNode, Comparable<AbstractFileTreeNode>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The file node on which this {@link AbstractFileTreeNode} is based.
     */
    protected AbstractFileNode node;
    /**
     * The {@link AbstractFileNodeFilter} to use to determine if a file should be counted as a child.
     */
    protected AbstractFileNodeFilter filter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that files are counted.
     *
     * @param node The file node on which this {@link AbstractFileTreeNode} is based.
     */
    public AbstractFileTreeNode(AbstractFileNode node)
    {
        this(node, true);
    }

    /**
     * Skeleton constructor.  Either assumes that all regular files or only directories are counted.
     *
     * @param node        The file node on which this {@link AbstractFileTreeNode} is based.
     * @param countFiles Whether or not this node treats regular files as children.
     */
    public AbstractFileTreeNode(AbstractFileNode node, boolean countFiles)
    {
        this(
                node,
                countFiles ?
                (AbstractFileNodeFilter) UnconditionalAcceptFilter.SINGLETON :
                (AbstractFileNodeFilter) DirectoriesOnlyFilter.SINGLETON);
    }

    /**
     * General constructor.
     *
     * @param node             The file node on which this {@link AbstractFileTreeNode} is based.
     * @param fileNodeFilter The {@link AbstractFileNodeFilter} to use on the contents of the tree.
     */
    public AbstractFileTreeNode(AbstractFileNode node, AbstractFileNodeFilter fileNodeFilter)
    {
        super();
        this.node = node;
        filter = fileNodeFilter;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    public Enumeration<AbstractFileNode> children()
    {
        if (filter.equals(UnconditionalAcceptFilter.SINGLETON))
        {
            return new ArrayEnumeration<AbstractFileNode>(node.getSortedChildren());
        } else
        {
            AbstractFileNode[] children = node.getSortedChildren();
            List<AbstractFileNode> list = new ArrayList<AbstractFileNode>();
            for (final AbstractFileNode child : children)
            {
                if (filter.accept(child))
                {
                    list.add(child);
                }
            }
            return new ListEnumeration<AbstractFileNode>(list);
        }
    }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren()
    {
        return true;
    }

    /**
     * Returns the child <code>TreeNode</code> at index <code>childIndex</code>.
     */
    public TreeNode getChildAt(int childIndex)
    {
        if (filter.equals(UnconditionalAcceptFilter.SINGLETON))
        {
            return new AbstractFileTreeNode(node.getSortedChildren()[childIndex], filter);
        } else
        {
            int index = -1;
            AbstractFileNode[] children = node.getSortedChildren();
            while (childIndex >= 0)
            {
                index++;
                if (filter.accept(children[index])) childIndex--;
            }
            return new AbstractFileTreeNode(children[index], filter);
        }
    }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver contains.
     */
    public int getChildCount()
    {
        if (filter.equals(UnconditionalAcceptFilter.SINGLETON))
        {
            return node.getSortedChildren().length;
        } else
        {
            int count = 0;
            AbstractFileNode[] children = node.getSortedChildren();
            for (final AbstractFileNode child : children)
            {
                if (filter.accept(child)) count++;
            }
            return count;
        }
    }

    /**
     * Returns the index of <code>node</code> in the receivers children. If the receiver does not contain
     * <code>node</code>, -1 will be returned.
     */
    public int getIndex(TreeNode node)
    {
        if (!(node instanceof AbstractFileTreeNode)) return -1;
        AbstractFileTreeNode other = (AbstractFileTreeNode) node;
        AbstractFileNode[] children = this.node.getSortedChildren();

        if (!filter.equals(UnconditionalAcceptFilter.SINGLETON))
        {
            List<AbstractFileNode> list = new ArrayList<AbstractFileNode>();
            for (final AbstractFileNode child : children)
            {
                if (filter.accept(child)) list.add(child);
            }
            children = list.toArray(new AbstractFileNode[0]);
        }

        for (int i = 0; i < children.length; i++)
        {
            if (children[i].equals(other.getAbstractFileNode())) return i;
        }
        return -1;
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public TreeNode getParent()
    {
        if (node.getParent() == null)
        {
            return null;
        } else
        {
            return new AbstractFileTreeNode(node.getParent(), filter);
        }
    }

    /**
     * Retrieves the {@link AbstractFileNode} backing this object.
     *
     * @return The {@link AbstractFileNode} backing this object.
     */
    public AbstractFileNode getAbstractFileNode()
    {
        return node;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf()
    {
        return (!(node.isDirectory()));
    }

    /**
     * Creates a display for this {@link AbstractFileTreeNode}.
     *
     * @return A {@link String} describing this {@link AbstractFileTreeNode}.
     */
    public String toString()
    {
        return node.toString();
    }

    /**
     * Determines whether or not this {@link AbstractFileTreeNode} and another are equal.  This is only true if they
     * have equal filters and equal nodes.
     *
     * @param o The other object to which this should be compared.
     * @return <code>true</code> if they are equal; <code>false</code> if they are not.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AbstractFileTreeNode)) return false;

        final AbstractFileTreeNode abstractFileTreeNode = (AbstractFileTreeNode) o;

        if (filter != null ? !filter.equals(abstractFileTreeNode.filter) : abstractFileTreeNode.filter != null)
        {
            return false;
        }
        return !(node != null ? !node.equals(abstractFileTreeNode.node) : abstractFileTreeNode.node != null);

    }

    /**
     * Generates a hash code for this {@link AbstractFileTreeNode}.
     *
     * @return This object's hash code.
     */
    public int hashCode()
    {
        int result;
        result = (node != null ? node.hashCode() : 0);
        result = 29 * result + (filter != null ? filter.hashCode() : 0);
        return result;
    }

    /**
     * Compares this {@link AbstractFileTreeNode} with another, sorting in terms of the names of the paths contained
     * within.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(AbstractFileTreeNode o)
    {
        return this.getAbstractFileNode().getUnixStylePathname(File.separatorChar).toLowerCase().compareTo(
                o.getAbstractFileNode().getUnixStylePathname(File.separatorChar).toLowerCase());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}