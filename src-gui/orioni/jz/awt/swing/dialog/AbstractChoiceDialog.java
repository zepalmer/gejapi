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
 * pressed.  The container is laid out in a grid-like fashion: <BR> <BR><code>+------------------------------+<BR> |
 * &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;------&nbsp; |<BR> | Prompt text here:
 * &nbsp; |&nbsp; OK &nbsp;| |<BR> | &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
 * &nbsp;------&nbsp; |<BR> | &nbsp;-----------------&nbsp; &nbsp;------&nbsp; |<BR> | |Choice shown here| |Cancel|
 * |<BR> | &nbsp;-----------------&nbsp; &nbsp;------&nbsp; |<BR> | &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
 * &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;|<BR> +------------------------------+</code><BR>
 *
 * @author Zachary Palmer
 */
public abstract class AbstractChoiceDialog extends JDialog
{
    /**
     * The selected value, or <code>null</code> if the dialog was cancelled.
     */
    protected Object result;
    /**
     * The text field into which the new label text will be typed.
     */
    protected JComboBox comboBox;
    /**
     * The OK button for this dialog.
     */
    protected JButton okButton;
    /**
     * The Cancel button for this dialog.
     */
    protected JButton cancelButton;
    /**
     * The Frame that controls this dialog.
     */
    protected Frame master;

    /**
     * The text and field column.
     */
    protected JPanel textColumn;
    /**
     * The button column.
     */
    protected JPanel buttonColumn;
    /**
     * The instructional text label.
     */
    protected JLabel text;

// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * A simple text dialog for Shifter.
     *
     * @param master     The ShifterFrame that controls the dialog.
     * @param title      The title of the dialog window.
     * @param labelText The text prompt instructing on what criteria the user should make a choice about which element
     *                   to select.
     */
    public AbstractChoiceDialog(Frame master, String title, String labelText)
    {
        super(master, title, true);
        this.master = master;

        text = new JLabel(labelText);
        SpacingComponent sc;
        comboBox = new JComboBox(new DefaultComboBoxModel());
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        // Establish text column and contents

        textColumn = new JPanel();
        textColumn.setLayout(new BoxLayout(textColumn, BoxLayout.Y_AXIS));
        // add text label
        text.setAlignmentX((float) 0.5);
        textColumn.add(text);
        // add spacing
        sc = new SpacingComponent(new Dimension(10, 5));
        sc.setAlignmentX((float) 0.5);
        sc.setMaximumSize(sc.getPreferredSize());
        textColumn.add(sc);
        // add text field
        comboBox.setAlignmentX((float) 0.5);
        textColumn.add(comboBox);

        // Establish button column and contents

        buttonColumn = new JPanel();
        buttonColumn.setLayout(new BoxLayout(buttonColumn, BoxLayout.Y_AXIS));
        // add ok button
        okButton.setAlignmentX((float) 0.5);
        buttonColumn.add(okButton);
        // add spacing
        sc = new SpacingComponent(new Dimension(10, 5));
        sc.setAlignmentX((float) 0.5);
        sc.setMaximumSize(sc.getPreferredSize());
        buttonColumn.add(sc);
        // add cancel button
        cancelButton.setAlignmentX((float) 0.5);
        buttonColumn.add(cancelButton);

        // Add columns to content pane
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(textColumn);
        getContentPane().add(buttonColumn);

        // Attach component listeners

        okButton.addActionListener(new ExecuteMethodActionListener(this, "okAction", new Object[0]));
        cancelButton.addActionListener(new ExecuteMethodActionListener(this, "cancelAction", new Object[0]));
        comboBox.addKeyListener(
                new ExecuteMethodKeyPressedListener(
                        this, "okAction", new Object[0],
                        KeyEvent.VK_ENTER, 0));
        comboBox.addKeyListener(
                new ExecuteMethodKeyPressedListener(
                        this, "cancelAction", new Object[0],
                        KeyEvent.VK_ESCAPE, 0));
        addWindowListener(new ExecuteMethodWindowClosingListener(this, "cancelAction", new Object[0]));

        this.getRootPane().setDefaultButton(okButton);

        pack();
    }

    /**
     * Executes this dialog.
     *
     * @param data The list of choices to provide.
     */
    public void execute(Object... data)
    {
        execute(-1, data);
    }

    /**
     * Executes this dialog.  The specified index is used as the default selection.
     *
     * @param data          The list of choices to provide.
     * @param defaultIndex The index of the default selection, or <code>-1</code> if no default is specified.
     */
    public void execute(int defaultIndex, Object... data)
    {
        DefaultComboBoxModel model = (DefaultComboBoxModel) (comboBox.getModel());
        model.removeAllElements();
        for (final Object dataElement : data)
        {
            model.addElement(dataElement);
        }
        if ((defaultIndex != -1) && (defaultIndex < data.length))
        {
            model.setSelectedItem(data[defaultIndex]);
        }
        pack();
        setVisible(true);
    }

    /**
     * Sets the frame visibility, but also clears the text field.
     *
     * @param visible Whether or not the frame should be visible.
     */
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        comboBox.requestFocus();
    }

    /**
     * Determines whether or not the choice is acceptable.  If this is the case, this method should set the
     * <code>m_result</code> value equal to the results of <code>m_combo_box.getSelectedItem</code> and should also call
     * <code>setVisible(false)</code>.  Otherwise, a dialog (preferrably from <code>JOptionPane</code> should be
     * displayed with information as to how to correct the error.
     */
    public abstract void okAction();

    /**
     * Sets the result to <code>null</code> and closes the dialog.
     */
    public void cancelAction()
    {
        result = null;
        setVisible(false);
    }

    /**
     * Retrieves the result of the dialog query.
     *
     * @return The Object that was selected, or <code>null</code> if the dialog was cancelled.
     */
    public Object getResult()
    {
        return result;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //