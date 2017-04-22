package orioni.jz.awt.swing.dialog;

import orioni.jz.io.files.FileUtilities;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

/**
 * This extension of the <code>JFileChooser</code> class is identical to the original except in that the results of
 * the <code>getSelectedFile</code> and <code>getSelectedFiles</code> methods have been altered.  An additional
 * method, <code>setRelativePath</code>, accepts a <code>File</code> object that represents a directory.  All
 * <code>File</code> objects returned by <code>getSelectedFile</code> and <code>getSelectedFiles</code> represent
 * relative paths based on the last value passed to the <code>setRelativePath</code> method.  If the last value
 * passed to the <code>setRelativePath</code> method was <code>null</code> or if <code>setRelativePath</code> has not
 * yet been called, the results of the <code>getSelectFile</code> and <code>getSelectedFiles</code> methods are not
 * affected.
 * @author Zachary Palmer
 */
public class RelativeFileChooser extends JFileChooser
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The directory from which all returns of <code>getSelectedFile</code> and <code>getSelectedFiles</code> will
     *  be based as long as this value is not <code>null</code>.  If the value is <code>null</code>, the calls to the
     * aforementioned methods are unaffected. */
    protected File relativeDirectory;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     * @see JFileChooser#JFileChooser()
     */
    public RelativeFileChooser()
    {
        super();
    }

    /**
     * Wrapper constructor.
     * @see JFileChooser#JFileChooser(String)
     */
    public RelativeFileChooser(String s)
    {
        super(s);
    }

    /**
     * Wrapper constructor.
     * @see JFileChooser#JFileChooser(File)
     */
    public RelativeFileChooser(File f)
    {
        super(f);
    }

    /**
     * Wrapper constructor.
     * @see JFileChooser#JFileChooser(FileSystemView)
     */
    public RelativeFileChooser(FileSystemView fsv)
    {
        super(fsv);
    }

    /**
     * Wrapper constructor.
     * @see JFileChooser#JFileChooser(String,FileSystemView)
     */
    public RelativeFileChooser(String s,FileSystemView fsv)
    {
        super(s,fsv);
    }

    /**
     * Wrapper constructor.
     * @see JFileChooser#JFileChooser(File,FileSystemView)
     */
    public RelativeFileChooser(File f,FileSystemView fsv)
    {
        super(f,fsv);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the new value of the relative file field.  If this value is <code>null</code>, the
     * <code>getSelectedFile</code> and <code>getSelectedFiles</code> methods return results as specified by the
     * <code>JFileChooser</code> class.  Otherwise, they return results that represent files relative to the
     * <code>File</code> object provided.
     * @param relative The new value for the relative file field.
     */
    public void setRelativePath(File relative)
    {
        relativeDirectory = relative;
    }

    /**
     * Retrieves the value of the relative file field.
     * @return The directory to which files are to be held relative, or <code>null</code>, if files are always to be
     *         absolute.
     * @see RelativeFileChooser#setRelativePath(File)
     */
    public File getRelativePath()
    {
        return relativeDirectory;
    }

    /**
     * Returns a file relative to the directory provided by the most recent <code>setRelativePath</code> call.  This
     * return value represents the selected file as it can be found from the relative directory.
     * <P>
     * <B>Note:</B> If the call to <code>Utilities.getRelativeString</code> fails, the return value of this method will
     * be the same as the return value for the super method.
     *
     * @return The selected file, as a relative <code>File</code> object.
     */
    public File getSelectedFile()
    {
        File ret = super.getSelectedFile();
        if ((relativeDirectory !=null) && (ret!=null))
        {
            try
            {
                ret = new File(FileUtilities.getRelativeString(relativeDirectory,ret));
            } catch (IOException ioe)
            {
                // The value of ret goes unchanged.
            }
        }
        return ret;
    }

    /**
     * Returns an array of files relative to the directory provided by the most recent <code>setRelativePath</code>
     * call.  This return value represents the selected files as they can be found from the relative directory.
     * <P>
     * <B>Note:</B> If the call to <code>Utilities.getRelativeString</code> fails, the value for which it failed will
     * be returned as it was retrieved from the super method.
     *
     * @return An array of <code>File</code> objects representing the files selected in the dialog.
     */
    public File[] getSelectedFiles()
    {
        File[] ret = super.getSelectedFiles();
        for (int i=0;i<ret.length;i++)
        {
            try
            {
                ret[i] = new File(FileUtilities.getRelativeString(relativeDirectory,ret[i]));
            } catch (IOException ioe)
            {
                // The value of ret[i] goes unchanged.
            }
        }
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //