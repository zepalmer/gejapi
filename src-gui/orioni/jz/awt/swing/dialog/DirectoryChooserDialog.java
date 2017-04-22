package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.SpongyLayout;
import orioni.jz.awt.swing.ApprovalButtonPanel;
import orioni.jz.awt.swing.convenience.ComponentConstructorPanel;
import orioni.jz.awt.swing.convenience.SizeConstructorScrollPane;
import orioni.jz.awt.swing.filenode.AbstractFileTreeNode;
import orioni.jz.awt.swing.filenode.FileNodeTreeModel;
import orioni.jz.io.files.abstractnode.LocalFileNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;

/**
 * This dialog is designed to allow a user to select a directory.
 *
 * @author Zachary Palmer
 */
public class DirectoryChooserDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The selected file.
     */
    protected File selectedFile;
    /**
     * The {@link JTree} displaying directories in this dialog.
     */
    protected JTree fileTree;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public DirectoryChooserDialog(Frame owner, String title)
    {
        super(owner, title, true);

        selectedFile = null;
        fileTree = new JTree(new FileNodeTreeModel(LocalFileNode.listRoots(), false));

        final JDialog scopedThis = this;
        ApprovalButtonPanel buttonPanel = new ApprovalButtonPanel(true, false)
        {
            public boolean apply()
            {
                TreePath path = fileTree.getSelectionPath();
                if (path == null)
                {
                    JOptionPane.showMessageDialog(
                            scopedThis, "Please select a directory.",
                            "No Selection", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (!(path.getLastPathComponent() instanceof AbstractFileTreeNode))
                {
                    JOptionPane.showMessageDialog(
                            scopedThis, "That is not a directory.",
                            "Invalid Selection", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                AbstractFileTreeNode treeNode = (AbstractFileTreeNode) (path.getLastPathComponent());
                LocalFileNode node = (LocalFileNode) (treeNode.getAbstractFileNode());
                selectedFile = node.getFile();
                if (!selectedFile.isDirectory())
                {
                    JOptionPane.showMessageDialog(
                            scopedThis, "That is not a directory.",
                            "Invalid Selection", JOptionPane.ERROR_MESSAGE);
                    selectedFile = null;
                    return false;
                }
                scopedThis.dispose();
                return true;
            }

            public void close()
            {
                scopedThis.dispose();
            }
        };

        setContentPane(
                new ComponentConstructorPanel(
                        new SpongyLayout(SpongyLayout.Orientation.VERTICAL),
                        new SizeConstructorScrollPane(fileTree, 200, 400),
                        buttonPanel
                ));

        pack();
        setLocationRelativeTo(null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the currently-selected directory.
     *
     * @param dir The directory to select.  If this {@link File} is not a directory, the first parent which is a
     *            directory will be used.  If no parent directory exists (perhaps due to the file path being invalid),
     *            nothing will happen as a result of the method call.
     */
    public void setSelectedDirectory(File dir)
    {
        while ((dir != null) && (dir.isFile())) dir = dir.getParentFile();
        if (dir == null) return;
        FileNodeTreeModel model = (FileNodeTreeModel) (fileTree.getModel());
        fileTree.setSelectionPath(model.getPathForNode(new LocalFileNode(dir)));
    }

    /**
     * Displays the directory chooser dialog and waits for a response.  The selected file is returned.
     *
     * @return The selected file, or <code>null</code> if no file was selected.
     */
    public File execute()
    {
        selectedFile = null;
        setVisible(true);
        return getResult();
    }

    /**
     * Retrieves the selected file.
     *
     * @return The selected file, or <code>null</code> if no file was selected or if the dialog is still open.
     */
    public File getResult()
    {
        return selectedFile;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE