package orioni.jz.awt.swing;

import orioni.jz.awt.swing.dialog.DirectoryChooserDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This {@link JPanel} extension is designed to display a text field containing a directory path and a button to the
 * immediate right used for changing that directory path through a directory dialog.  Although the components are\
 * arranged on this panel, they can be retrieved and used in other panels, preserving their relationship, if necessary.
 * A {@link DirectoryChooserDialog} is used to modify the field; this object can be optionally specified on
 * construction.
 *
 * @author Zachary Palmer
 */
public class DirectoryPathField extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A {@link DirectoryChooserDialog} used to modify the field's contents.
     */
    protected DirectoryChooserDialog dialog;
    /**
     * The {@link JTextField} containing the file path.
     */
    protected JTextField pathField;
    /**
     * The {@link JButton} used to call up the {@link DirectoryChooserDialog}.
     */
    protected JButton button;

    /**
     * The {@link ActionListener} used on the button.
     */
    protected ActionListener listener;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Constructs a {@link DirectoryChooserDialog} using the specified frame.
     *
     * @param owner  The ownder of the dialog which will appear.
     * @param path   The default path for this {@link FilePathField}.
     * @param length The size of the {@link JTextField} which contains the file's path.
     */
    public DirectoryPathField(Frame owner, String path, int length)
    {
        this(new DirectoryChooserDialog(owner, "Please choose a directory..."), path, length);
    }

    /**
     * General constructor.
     *
     * @param dialog The {@link DirectoryChooserDialog} to be used to modify the field's contents. If this field is
     *               <code>null</code>, this object will make its own.
     * @param path   The default path for this {@link FilePathField}.
     * @param length The size of the {@link JTextField} which contains the file's path.
     */
    public DirectoryPathField(DirectoryChooserDialog dialog, String path, int length)
    {
        super(new BorderLayout());

        final DirectoryPathField scopedThis = this;

        this.dialog = dialog;
        if (this.dialog == null)
        {
            this.dialog = new DirectoryChooserDialog(null, "Please choose a directory...");
        }
        pathField = new JTextField(length);

        pathField.setText(path);
        this.add(pathField, BorderLayout.CENTER);
        button = new JButton("...");
        this.add(button, BorderLayout.EAST);

        listener =
        new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                File file = new File(pathField.getText());
                while ((file != null) && (!file.exists()))
                {
                    file = file.getParentFile();
                }
                if (file == null) file = new File(System.getProperty("user.home"));
                while ((file != null) && (file.isFile()))
                {
                    file = file.getParentFile();
                }
                scopedThis.dialog.setSelectedDirectory(file);

                File selected = scopedThis.dialog.execute();
                if (selected != null)
                {
                    pathField.setText(selected.getPath());
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