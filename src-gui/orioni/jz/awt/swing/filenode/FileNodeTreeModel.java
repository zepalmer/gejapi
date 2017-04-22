package orioni.jz.awt.swing.filenode;

import orioni.jz.awt.swing.DefaultTreeNode;
import orioni.jz.io.files.abstractnode.AbstractFileNode;
import orioni.jz.io.files.abstractnode.filter.AbstractFileNodeFilter;
import orioni.jz.io.files.abstractnode.filter.DirectoriesOnlyFilter;
import orioni.jz.io.files.abstractnode.filter.UnconditionalAcceptFilter;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.*;

// TODO: consider implementing an update mode which is not so aggressive to allow higher speeds

/**
 * This implementation of {@link TreeModel} bases its information on a set of {@link AbstractFileNode} objects.  Update
 * events are fired as soon as the model notices that the information contained within the {@link AbstractFileNode} is
 * inconsistent with the model's representation of it.  The display for this model varies depending upon the constructor
 * used.
 * <p/>
 * This model is also capable of showing either all children of a node or only the directory children of a node.  This
 * should be configured upon construction of the model.
 *
 * @author Zachary Palmer
 */
public class FileNodeTreeModel implements TreeModel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Set} of {@link TreeModelListener} objects which will be notified when a model event occurs.
     */
    protected Set<TreeModelListener> listeners;
    /**
     * The root {@link TreeNode} for this model.
     */
    protected TreeNode rootTreeNode;
    /**
     * The {@link AbstractFileNodeFilter} to use.
     */
    protected AbstractFileNodeFilter filter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that general files will not be shown.
     *
     * @param node The single {@link AbstractFileNode} to use as a root.
     */
    public FileNodeTreeModel(AbstractFileNode node)
    {
        this(node, false);
    }

    /**
     * Skeleton constructor.  Assumes that either general files will not be shown or all files will be shown.
     *
     * @param node       The single {@link orioni.jz.io.files.abstractnode.AbstractFileNode} to use as a root.
     * @param showFiles <code>true</code> if files should be shown in the trees; <code>false</code> otherwise.
     */
    public FileNodeTreeModel(AbstractFileNode node, boolean showFiles)
    {
        this(Collections.singleton(node).toArray(new AbstractFileNode[1]), showFiles);
    }

    /**
     * General constructor.
     *
     * @param node   The {@link AbstractFileNode} to use as the root.
     * @param filter The filter to use to determine which nodes to include in the model.
     */
    public FileNodeTreeModel(AbstractFileNode node, AbstractFileNodeFilter filter)
    {
        this(Collections.singleton(node).toArray(new AbstractFileNode[1]), filter);
    }

    /**
     * Skeleton constructor.  Assumes that general files will not be shown.
     *
     * @param nodes The set of {@link AbstractFileNode}s to use as roots.
     */
    public FileNodeTreeModel(AbstractFileNode[] nodes)
    {
        this(nodes, false);
    }

    /**
     * Skeleton constructor.  Assumes that either general files will not be shown or all files will be shown.
     *
     * @param nodes      The set of {@link AbstractFileNode}s to use as roots.
     * @param showFiles <code>true</code> if files should be shown in the trees; <code>false</code> otherwise.
     */
    public FileNodeTreeModel(AbstractFileNode[] nodes, boolean showFiles)
    {
        this(
                nodes,
                showFiles ?
                (AbstractFileNodeFilter) UnconditionalAcceptFilter.SINGLETON :
                (AbstractFileNodeFilter) DirectoriesOnlyFilter.SINGLETON);
    }

    /**
     * General constructor.
     *
     * @param nodes  The set of {@link AbstractFileNode}s to use as roots.
     * @param filter The filter to use to determine which nodes to include in the model.
     */
    public FileNodeTreeModel(AbstractFileNode[] nodes, AbstractFileNodeFilter filter)
    {
        super();
        listeners = new HashSet<TreeModelListener>();

        if (nodes.length > 1)
        {
            AbstractFileTreeNode[] treeNodes = new AbstractFileTreeNode[nodes.length];
            for (int i = 0; i < treeNodes.length; i++)
            {
                treeNodes[i] = new AbstractFileTreeNode(nodes[i], filter);
            }
            Arrays.sort(treeNodes);
            rootTreeNode = new DefaultTreeNode(null, treeNodes, "Root");
        } else
        {
            rootTreeNode = new AbstractFileTreeNode(nodes[0], filter);
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a listener for events posted on this model.
     *
     * @param listener The listener to add.
     */
    public void addTreeModelListener(TreeModelListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Returns the child of <code>parent</code> at index <code>index</code> in the parent's child array.
     * <code>parent</code> must be a node previously obtained from this data source. This should not return
     * <code>null</code> if <code>index</code> is a valid index for <code>parent</code> (that is <code>index >= 0 &&
     * index < getChildCount(parent</code>)).
     *
     * @param parent a node in the tree, obtained from this data source
     * @return the child of <code>parent</code> at index <code>index</code>
     */
    public Object getChild(Object parent, int index)
    {
        TreeNode node = (TreeNode) parent;
        return node.getChildAt(index);
    }

    /**
     * Returns the number of children of <code>parent</code>.  Returns 0 if the node is a leaf or if it has no children.
     * <code>parent</code> must be a node previously obtained from this data source.
     *
     * @param parent A node in the tree, obtained from this data source
     * @return The number of children of the node <code>parent</code>
     */
    public int getChildCount(Object parent)
    {
        TreeNode node = (TreeNode) parent;
        return node.getChildCount();
    }

    /**
     * Returns the index of child in parent.  If <code>parent</code> is <code>null</code> or <code>child</code> is
     * <code>null</code>, returns -1.
     *
     * @param parent A node in the tree, obtained from this data source
     * @param child  The node in which the caller is interested.
     * @return The index of the child in the parent, or -1 if either <code>child</code> or <code>parent</code> are
     *         <code>null</code>
     */
    public int getIndexOfChild(Object parent, Object child)
    {
        if ((parent == null) || (child == null)) return -1;
        return ((TreeNode) parent).getIndex((TreeNode) child);
    }

    /**
     * Returns the root of the tree.  Returns <code>null</code> only if the tree has no nodes.
     *
     * @return The root of the tree
     */
    public Object getRoot()
    {
        return rootTreeNode;
    }

    /**
     * Returns <code>true</code> if the specified node is a leaf.
     *
     * @param node A node in the tree, obtained from this data source
     * @return <code>true</code> if the specified node is a leaf; <code>false</code> otherwise.
     */
    public boolean isLeaf(Object node)
    {
        if ((node instanceof AbstractFileTreeNode) &&
            (!(((TreeNode) (node)).getParent() instanceof AbstractFileTreeNode)))
        {
            // In this case, the node is one of the roots of the model.
            // This condition is primarily inserted to ensure that the filesystem doesn't make stupid active checks
            // of removable devices (i.e., stalling for four seconds while it tries to find out if B:\ is a directory
            return false;
        }
        return ((TreeNode) node).isLeaf();
    }

    /**
     * Removes a listener for events posted on this model.
     *
     * @param listener The listener to remove.
     */
    public void removeTreeModelListener(TreeModelListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Messaged when the user has altered the value for the item identified by <code>path</code> to
     * <code>newValue</code>. If <code>newValue</code> signifies a truly new value the model should post a
     * <code>treeNodesChanged</code> event.
     *
     * @param path     path to the node that the user has altered
     * @param newValue the new value from the TreeCellEditor
     */
    public void valueForPathChanged(TreePath path, Object newValue)
    {
    }

    /**
     * Retrieves a {@link TreePath} for the provided {@link AbstractFileNode} within this {@link FileNodeTreeModel}, if
     * it exists.  Otherwise, <code>null</code> is returned.
     *
     * @param node The {@link AbstractFileNode} to locate within the tree.
     * @return The {@link TreePath} to the provided node, or <code>null</code> if no such path can be found.  If part of
     *         the path could be found but not all of it (such as is the case if part of the path represented by the
     *         node exists but the specific file or a set of subdirectories does not), the portion of the path which
     *         could be found is returned.
     *         <p/>
     *         To distinguish the difference between these cases, check the number of elements in the path.  For a
     *         single-root model, the number of elements should be equal to the number of ancestors for the node; for a
     *         multi-root model (with the "fake" root as the actual root of the tree), the number of elements should be
     *         one more than the number of ancestors for the node.
     */
    public TreePath getPathForNode(AbstractFileNode node)
    {
        java.util.List<AbstractFileNode> ancestors = node.getAncestors();
        ancestors.add(node);

        if (ancestors.size() > 0)
        {
            AbstractFileTreeNode rootNode = null;
            LinkedList<TreeNode> nodesInPath = new LinkedList<TreeNode>();

            if (this.getRoot() instanceof AbstractFileTreeNode)
            {
                if (((AbstractFileTreeNode) (this.getRoot())).getAbstractFileNode().equals(ancestors.get(0)))
                {
                    rootNode = (AbstractFileTreeNode) (this.getRoot());
                }
            } else
            {
                TreeNode fakeRoot = (TreeNode) (this.getRoot());
                nodesInPath.add(fakeRoot);
                for (int i = 0; i < fakeRoot.getChildCount(); i++)
                {
                    TreeNode treeNode = fakeRoot.getChildAt(i);
                    if (treeNode instanceof AbstractFileTreeNode)
                    {
                        AbstractFileTreeNode aftn = (AbstractFileTreeNode) treeNode;
                        if (aftn.getAbstractFileNode().equals(ancestors.get(0)))
                        {
                            rootNode = aftn;
                            break;
                        }
                    }
                }
            }
            if (rootNode != null)
            {
                nodesInPath.add(rootNode);
                int index = 1;
                AbstractFileTreeNode treeNode = rootNode;
                while (index < ancestors.size())
                {
                    AbstractFileNode ancestor = ancestors.get(index);
                    for (int i = 0; i < treeNode.getChildCount(); i++)
                    {
                        TreeNode child = treeNode.getChildAt(i);
                        if (child instanceof AbstractFileTreeNode)
                        {
                            AbstractFileTreeNode aftn = (AbstractFileTreeNode) child;
                            if (aftn.getAbstractFileNode().equals(ancestor))
                            {
                                nodesInPath.add(aftn);
                                treeNode = aftn;
                                break;
                            }
                        }
                    }
                    index++;
                }
                return new TreePath(nodesInPath.toArray());
            }
        }
        return null;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}