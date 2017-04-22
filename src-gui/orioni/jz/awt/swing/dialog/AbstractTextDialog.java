package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.listener.ExecuteMethodActionListener;
import orioni.jz.awt.listener.ExecuteMethodKeyPressedListener;
import orioni.jz.awt.listener.ExecuteMethodWindowClosingListener;
import orioni.jz.awt.swing.SpacingComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This is the base class for a simple text dialog; it lacks only the element of validation when the "OK" button is
 * pressed.  The container is laid out in a grid-like fashion: <BR>
 * <BR><code>+------------------------------+<BR>
 *           | &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;------&nbsp; |<BR>
 *           | Prompt text here: &nbsp; |&nbsp; OK &nbsp;| |<BR>
 *           | &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;------&nbsp; |<BR>
 *           | &nbsp;-----------------&nbsp; &nbsp;------&nbsp; |<BR>
 *           | | Text typed here | |Cancel| |<BR>
 *           | &nbsp;-----------------&nbsp; &nbsp;------&nbsp; |<BR>
 *           | &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
 * &nbsp;|<BR>
 *           +------------------------------+</code><BR>
 * @author Zachary Palmer
 */
public abstract class AbstractTextDialog extends JDialog
{
    /** The new text, or <code>null</code> if the dialog was cancelled. */
    protected String text;
    /** The text field into which the new label text will be typed. */
    protected JTextField field;
    /** The OK button for this dialog. */
    protected JButton okButton;
    /** The Cancel button for this dialog. */
    protected JButton cancelButton;
    /** The Frame that controls this dialog. */
    protected Frame master;

    /** The text and field column. */
    protected JPanel textColumn;
    /** The button column. */
    protected JPanel buttonColumn;

// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * A simple text dialog for Shifter.
     * @param master The Frame that controls the dialog.  If this is <code>null</code>, AWT's shared internal frame
     *               will be used (see JDialog constructor).
     * @param title The title of the dialog window.
     * @param labelText The text prompt instructing what should be put in the text field.
     */
    public AbstractTextDialog(Frame master, String title, String labelText)
    {
        super(master, title, true);
        this.master = master;

        text = null;

        JLabel textLabel = new JLabel(labelText);
        SpacingComponent sc;
        field = new JTextField(25);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        // Establish text column and contents

        textColumn = new JPanel();
        textColumn.setLayout(new BoxLayout(textColumn,BoxLayout.Y_AXIS));
        // add text label
        textLabel.setAlignmentX((float) 0.5);
        textLabel.setMaximumSize(textLabel.getPreferredSize());
        textColumn.add(textLabel);
        // add spacing
        sc = new SpacingComponent(new Dimension(10,5));
        sc.setAlignmentX((float) 0.5);
        sc.setMaximumSize(sc.getPreferredSize());
        textColumn.add(sc);
        // add text field
        field.setAlignmentX((float) 0.5);
        field.setMaximumSize(field.getPreferredSize());
        textColumn.add(field);

        // Establish button column and contents

        buttonColumn = new JPanel();
        buttonColumn.setLayout(new BoxLayout(buttonColumn,BoxLayout.Y_AXIS));
        // add ok button
        okButton.setAlignmentX((float) 0.5);
        okButton.setMaximumSize(okButton.getPreferredSize());
        buttonColumn.add(okButton);
        // add spacing
        sc = new SpacingComponent(new Dimension(10,5));
        sc.setAlignmentX((float) 0.5);
        sc.setMaximumSize(sc.getPreferredSize());
        buttonColumn.add(sc);
        // add cancel button
        cancelButton.setAlignmentX((float) 0.5);
        cancelButton.setMaximumSize(cancelButton.getPreferredSize());
        buttonColumn.add(cancelButton);

        // Add columns to content pane
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(textColumn);
        getContentPane().add(buttonColumn);

        // Attach component listeners

        okButton.addActionListener(new ExecuteMethodActionListener(this,"okAction",new Object[0]));
        cancelButton.addActionListener(new ExecuteMethodActionListener(this,"cancelAction",new Object[0]));
        field.addKeyListener(new ExecuteMethodKeyPressedListener(this,"okAction",new Object[0],
                KeyEvent.VK_ENTER, 0));
        field.addKeyListener(new ExecuteMethodKeyPressedListener(this,"cancelAction",new Object[0],
                KeyEvent.VK_ESCAPE, 0));
        addWindowListener(new ExecuteMethodWindowClosingListener(this,"cancelAction",new Object[0]));

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Executes this dialog.
     * @param defaultText The defualt text for the text field.  A value of <code>null</code> specifies no default text.
     */
    public void execute(String defaultText)
    {
        field.setText(defaultText);
        setVisible(true);
    }

    /**
     * Sets the frame visibility, but also clears the text field.
     * @param visible Whether or not the frame should be visible.
     */
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        field.requestFocus();
    }

    /**
     * Determines whether or not the text is acceptable.  If this is the case, this method should set the
     * <code>m_text</code> value equal to the results of <code>m_field.getText()</code> and should also call
     * <code>setVisible(false)</code>.  Otherwise, a dialog (preferrably from <code>JOptionPane</code>) should
     * be displayed with information as to how to correct the syntax error.
     */
    public abstract void okAction();

    /**
     * Sets the result to <code>null</code> and closes the dialog.
     */
    public void cancelAction()
    {
        text = null;
        setVisible(false);
    }

    /**
     * Retrieves the result of the dialog query.
     * @return The value entered into the text field, or <code>null</code> if the dialog was cancelled.
     */
    public String getResult()
    {
        return text;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //