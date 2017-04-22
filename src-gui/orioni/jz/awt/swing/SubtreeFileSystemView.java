package orioni.jz.awt.swing;

import orioni.jz.io.files.FileUtilities;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

/**
 * This {@link FileSystemView} echoes the behavior of the underlying filesystem excepting in that its sole root is a
 * specific subdirectory of that filesystem.  A {@link File} object is provided indicating the subdirectory which is to
 * act as the root of this filesystem view.
 *
 * @author Zachary Palmer
 */
public class SubtreeFileSystemView extends FileSystemView
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The subtree path of this {@link SubtreeFileSystemView}.
     */
    private File subtreeFile;
    /**
     * The system display name for this filesystem view's root.
     */
    private String displayName;
    /**
     * <code>true</code> if the root of this filesystem view is treated as a "drive"; <code>false</code> if it is not.
     */
    private boolean driveRoot;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param subtreeFile The {@link File} object representing the root of this {@link FileSystemView}.
     * @param displayName The display name of the filesystem root, which is used to describe the filesystem in a
     *                    human-readable context.
     * @param driveRoot   <code>true</code> if the root of this filesystem view should be treated as a "drive";
     *                    <code>false</code> if it should not.  This primarily impacts display icons.
     */
    public SubtreeFileSystemView(File subtreeFile, String displayName, boolean driveRoot)
    {
        super();

        this.subtreeFile = subtreeFile;
        this.displayName = displayName;
        this.driveRoot = driveRoot;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Creates a new folder with a default folder name.
     */
    public File createNewFolder(File containingDir)
            throws IOException
    {
        return getFileSystemView().createNewFolder(containingDir);
    }

    /**
     * Returns a File object constructed in dir from the given filename.
     */
    public File createFileObject(File dir, String filename)
    {
        return getFileSystemView().createFileObject(dir, filename);
    }

    /**
     * Returns a File object constructed from the given path string.
     */
    public File createFileObject(String path)
    {
        return getFileSystemView().createFileObject(path);
    }

    /**
     * Creates a new <code>File</code> object for <code>f</code> with correct behavior for a file system root
     * directory.
     *
     * @param f a <code>File</code> object representing a file system root directory, for example "/" on Unix or "C:\"
     *          on Windows.
     * @return a new <code>File</code> object
     */
    protected File createFileSystemRoot(File f)
    {
        return subtreeFile;
    }

    /**
     * @param parent   a <code>File</code> object repesenting a directory or special folder
     * @param fileName a name of a file or folder which exists in <code>parent</code>
     * @return a File object. This is normally constructed with <code>new File(parent, fileName)</code> except when
     *         parent and child are both special folders, in which case the <code>File</code> is a wrapper containing a
     *         <code>ShellFolder</code> object.
     */
    public File getChild(File parent, String fileName)
    {
        return getFileSystemView().getChild(parent, fileName);
    }

    /**
     * Return the user's default starting directory for the file chooser.
     *
     * @return a <code>File</code> object representing the default starting folder
     */
    public File getDefaultDirectory()
    {
        return subtreeFile;
    }

    /**
     * Gets the list of shown (i.e. not hidden) files.
     */
    public File[] getFiles(File dir, boolean useFileHiding)
    {
        return getFileSystemView().getFiles(dir, useFileHiding);
    }

    public File getHomeDirectory()
    {
        return subtreeFile;
    }

    /**
     * Returns the parent directory of <code>dir</code>.
     *
     * @param dir the <code>File</code> being queried
     * @return the parent directory of <code>dir</code>, or <code>null</code> if <code>dir</code> is <code>null</code>
     */
    public File getParentDirectory(File dir)
    {
        if (dir.equals(subtreeFile))
        {
            return subtreeFile;
        } else
        {
            return getFileSystemView().getParentDirectory(dir);
        }
    }

    /**
     * Returns all root partitions on this system. For example, on Windows, this would be the "Desktop" folder, while on
     * DOS this would be the A: through Z: drives.
     */
    public File[] getRoots()
    {
        return new File[]{subtreeFile};
    }

    /**
     * Name of a file, directory, or folder as it would be displayed in a system file browser. Example from Windows: the
     * "M:\" directory displays as "CD-ROM (M:)"
     * <p/>
     * The default implementation gets information from the ShellFolder class.
     *
     * @param f a <code>File</code> object
     * @return the file name as it would be displayed by a native file chooser
     * @see javax.swing.JFileChooser#getName
     */
    public String getSystemDisplayName(File f)
    {
        if (f.equals(subtreeFile))
        {
            return displayName;
        } else
        {
            return getFileSystemView().getSystemDisplayName(f);
        }
    }

    /**
     * Icon for a file, directory, or folder as it would be displayed in a system file browser. Example from Windows:
     * the "M:\" directory displays a CD-ROM icon.
     * <p/>
     * The default implementation gets information from the ShellFolder class.
     *
     * @param f a <code>File</code> object
     * @return an icon as it would be displayed by a native file chooser
     * @see javax.swing.JFileChooser#getIcon
     */
    public Icon getSystemIcon(File f)
    {
        return getFileSystemView().getSystemIcon(f);
    }

    /**
     * Type description for a file, directory, or folder as it would be displayed in a system file browser. Example from
     * Windows: the "Desktop" folder is desribed as "Desktop".
     * <p/>
     * Override for platforms with native ShellFolder implementations.
     *
     * @param f a <code>File</code> object
     * @return the file type description as it would be displayed by a native file chooser or null if no native
     *         information is available.
     * @see javax.swing.JFileChooser#getTypeDescription
     */
    public String getSystemTypeDescription(File f)
    {
        return getFileSystemView().getSystemTypeDescription(f);
    }

    /**
     * Used by UI classes to decide whether to display a special icon for a computer node, e.g. "My Computer" or a
     * network server.
     * <p/>
     * The default implementation has no way of knowing, so always returns false.
     *
     * @param dir a directory
     * @return <code>false</code> always
     */
    public boolean isComputerNode(File dir)
    {
        return getFileSystemView().isComputerNode(dir);
    }

    /**
     * Used by UI classes to decide whether to display a special icon for drives or partitions, e.g. a "hard disk"
     * icon.
     * <p/>
     * The default implementation has no way of knowing, so always returns false.
     *
     * @param dir a directory
     * @return <code>false</code> always
     */
    public boolean isDrive(File dir)
    {
        if ((dir.equals(subtreeFile)) && (driveRoot))
        {
            return true;
        } else
        {
            return getFileSystemView().isDrive(dir);
        }
    }

    /**
     * Checks if <code>f</code> represents a real directory or file as opposed to a special folder such as
     * <code>"Desktop"</code>. Used by UI classes to decide if a folder is selectable when doing directory choosing.
     *
     * @param f a <code>File</code> object
     * @return <code>true</code> if <code>f</code> is a real file or directory.
     */
    public boolean isFileSystem(File f)
    {
        return getFileSystemView().isFileSystem(f);
    }

    /**
     * Is dir the root of a tree in the file system, such as a drive or partition. Example: Returns true for "C:\" on
     * Windows 98.
     *
     * @param dir a <code>File</code> object representing a directory
     * @return <code>true</code> if <code>f</code> is a root of a filesystem
     * @see #isRoot
     */
    public boolean isFileSystemRoot(File dir)
    {
        return (dir.equals(subtreeFile));
    }

    /**
     * Used by UI classes to decide whether to display a special icon for a floppy disk. Implies isDrive(dir).
     * <p/>
     * The default implementation has no way of knowing, so always returns false.
     *
     * @param dir a directory
     * @return <code>false</code> always
     */
    public boolean isFloppyDrive(File dir)
    {
        return getFileSystemView().isFloppyDrive(dir);
    }

    /**
     * Returns whether a file is hidden or not.
     */
    public boolean isHiddenFile(File f)
    {
        return getFileSystemView().isHiddenFile(f);
    }

    /**
     * On Windows, a file can appear in multiple folders, other than its parent directory in the filesystem. Folder
     * could for example be the "Desktop" folder which is not the same as file.getParentFile().
     *
     * @param folder a <code>File</code> object repesenting a directory or special folder
     * @param file   a <code>File</code> object
     * @return <code>true</code> if <code>folder</code> is a directory or special folder and contains
     *         <code>file</code>.
     */
    public boolean isParent(File folder, File file)
    {
        return getFileSystemView().isParent(folder, file);
    }

    /**
     * Determines if the given file is a root in the navigatable tree(s). Examples: Windows 98 has one root, the Desktop
     * folder. DOS has one root per drive letter, <code>C:\</code>, <code>D:\</code>, etc. Unix has one root, the
     * <code>"/"</code> directory.
     * <p/>
     * The default implementation gets information from the <code>ShellFolder</code> class.
     *
     * @param f a <code>File</code> object representing a directory
     * @return <code>true</code> if <code>f</code> is a root in the navigatable tree.
     * @see #isFileSystemRoot
     */
    public boolean isRoot(File f)
    {
        return (f.equals(subtreeFile));
    }

    /**
     * Returns true if the file (directory) can be visited. Returns false if the directory cannot be traversed.
     *
     * @param f the <code>File</code>
     * @return <code>true</code> if the file/directory can be traversed, otherwise <code>false</code>
     * @see javax.swing.JFileChooser#isTraversable
     * @see javax.swing.filechooser.FileView#isTraversable
     */
    public Boolean isTraversable(File f)
    {
        return (FileUtilities.isAncestorFile(subtreeFile, f) && getFileSystemView().isTraversable(f));
    }



    /**
     * Changes the system display name for this filesystem view's root.
     * @param displayName The new display name to use.
     */
    public void setBasePathDescription(String displayName)
    {
        this.displayName = displayName;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
