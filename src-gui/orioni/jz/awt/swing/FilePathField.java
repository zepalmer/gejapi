package orioni.jz.awt.swing;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This {@link JPanel} extension is designed to display a text field containing a file path and a button to the
 * immediate right used for changing that file path through a file dialog.  Although the components are arranged on this
 * panel, they can be retrieved and used in other panels, preserving their relationship, if necessary.  A {@link
 * DelayedJFileChooserWrapper} is used to generate the {@link JFileChooser} which modifies the field; this object can be
 * optionally specified on construction.
 *
 * @author Zachary Palmer
 */
public class FilePathField extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A {@link DelayedJFileChooserWrapper} used to obtain a {@link JFileChooser} with which to modify the field's
     * contents.
     */
    protected DelayedJFileChooserWrapper wrapper;
    /**
     * The {@link JTextField} containing the file path.
     */
    protected JTextField pathField;
    /**
     * The {@link JButton} used to call up the {@link JFileChooser}.
     */
    protected JButton button;
    /**
     * The {@link FileFilter}s that should be used when the {@link JFileChooser} is displayed.
     */
    protected FileFilter[] filters;

    /**
     * The {@link ActionListener} used on the button.
     */
    protected ActionListener listener;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param wrapper          The {@link DelayedJFileChooserWrapper} to be used as a source for a {@link JFileChooser}.
     *                         If this field is <code>null</code>, this object will make its own.
     * @param path             The default path for this {@link FilePathField}.  If this is <code>null</code>, the
     *                         path defaults to the home directory.
     * @param length           The size of the {@link JTextField} which contains the file's path.
     * @param allFilesFilter <code>true</code> if the all-files filter is to be used; <code>false</code> otherwise.
     * @param filters          The {@link FileFilter}s to display when the {@link JFileChooser} is displayed.
     */
    public FilePathField(DelayedJFileChooserWrapper wrapper, String path, int length, final boolean allFilesFilter,
                         FileFilter... filters)
    {
        super(new BorderLayout());

        final FilePathField scopedThis = this;

        if (path==null) path = System.getProperty("user.home");
        this.wrapper = (wrapper != null) ?
                    wrapper :
                    new DelayedJFileChooserWrapper(true, new File((path == null) ? "" : path));
        pathField = new JTextField(length);
        this.filters = (filters == null) ? new FileFilter[0] : filters;

        pathField.setText(path);
        this.add(pathField, BorderLayout.CENTER);
        button = new JButton("...");
        this.add(button, BorderLayout.EAST);

        listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Container owner = button.getTopLevelAncestor();
                while (owner instanceof Dialog)
                {
                    Dialog dialog = (Dialog) owner;
                    owner = dialog.getOwner();
                }
                if (!(owner instanceof Frame)) owner = null;
                JFileChooser chooser = scopedThis.wrapper.getJFileChooser((Frame) owner, true);
                File file = new File(pathField.getText());
                while ((file != null) && (!file.exists()))
                {
                    file = file.getParentFile();
                }
                if (file == null) file = new File(System.getProperty("user.home"));
                if (file.isFile())
                {
                    chooser.setSelectedFile(file);
                } else
                {
                    chooser.setCurrentDirectory(file);
                }

                for (FileFilter filter : chooser.getChoosableFileFilters())
                {
                    chooser.removeChoosableFileFilter(filter);
                }

                for (FileFilter filter : scopedThis.filters)
                {
                    chooser.addChoosableFileFilter(filter);
                }

                chooser.setAcceptAllFileFilterUsed(allFilesFilter);

                if (chooser.showDialog(owner, "Select") == JFileChooser.APPROVE_OPTION)
                {
                    pathField.setText(chooser.getSelectedFile().getPath());
                }
            }
        };
        button.addActionListener(listener);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the button which, when pushed, calls the {@link JFileChooser} to edit the contents of the field.
     *
     * @return The button on this panel.
     */
    public JButton getButton()
    {
        return button;
    }

    /**
     * Retrieves the text field in which the path is stored.
     *
     * @return The text field in which the path is stored.
     */
    public JTextField getPathField()
    {
        return pathField;
    }

    /**
     * Retrieves the text of the path field.  Provided for convenience and simplicity of code appearance.
     *
     * @return The stored path.
     */
    public String getPath()
    {
        return pathField.getText();
    }

    /**
     * Retrieves the {@link DelayedJFileChooserWrapper} used to produce the {@link JFileChooser}.
     *
     * @return The {@link DelayedJFileChooserWrapper} used to produce the {@link JFileChooser}.
     */
    public DelayedJFileChooserWrapper getWrapper()
    {
        return wrapper;
    }

    /**
     * Sets the enabled state of this {@link FilePathField}.  This also sets the enabled state of its subcomponents.
     * @param enabled <code>true</code> to enable this panel and its components; <code>false</code> otherwise.
     */
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        for (Component c : getComponents()) c.setEnabled(enabled);
    }

    /**
     * Invokes the path change dialog immediately.
     *
     * @return <code>true</code> if the text was changed; <code>false</code> otherwise.
     */
    public boolean invokeChooser()
    {
        String text = pathField.getText();
        listener.actionPerformed(new ActionEvent("", ActionEvent.ACTION_FIRST, "invoke"));
        return (!(text.equals(pathField.getText())));
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE