package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.listener.ExecuteMethodActionListener;
import orioni.jz.awt.listener.ExecuteMethodWindowClosingListener;

import javax.swing.*;
import java.awt.*;

/**
 * This dialog allows the user to select a color.
 * @author Zachary Palmer
 */
public class ColorDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The color chooser that runs this dialog. */
    protected JColorChooser chooser;
    /** The panel in which the buttons are kept. */
    protected JPanel panel;
    /** The OK button that completes this dialog. */
    protected JButton okButton;
    /** The cancel button that cancels this dialog. */
    protected JButton cancelButton;
    /** Whether or not this dialog was cancelled. */
    protected boolean cancelled;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param owner The controlling frame of this dialog.
     * @param color The default color for this dialog.
     */
    public ColorDialog(Frame owner,Color color)
    {
        super(owner,"Please select a color",true);
        initialize(color);
    }

    /**
     * General constructor.
     * @param owner The controlling dialog of this dialog.
     * @param color The default color for this dialog.
     */
    public ColorDialog(Dialog owner,Color color)
    {
        super(owner,"Please select a color",true);
        initialize(color);
    }

    /**
     * Initializes the dialog with the specified default color.
     * @param color The initial default color for this dialog.
     */
    public void initialize(Color color)
    {
        chooser = new JColorChooser(color);

        okButton = new JButton("OK");
        okButton.setAlignmentX((float)0.5);
        okButton.setMaximumSize(okButton.getPreferredSize());
        cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX((float)0.5);
        cancelButton.setMaximumSize(cancelButton.getPreferredSize());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(okButton);
        panel.add(cancelButton);

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(chooser);
        getContentPane().add(panel);

        // Add listeners

        okButton.addActionListener(new ExecuteMethodActionListener(this,"okAction",new Object[0]));
        cancelButton.addActionListener(new ExecuteMethodActionListener(this,"cancelAction",new Object[0]));
        addWindowListener(new ExecuteMethodWindowClosingListener(this,"cancelAction",new Object[0]));

        // Complete

        pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executes this dialog.
     */
    public void execute()
    {
        setVisible(true);
    }

    /**
     * Closes this dialog on a selection.
     */
    public void okAction()
    {
        cancelled = false;
        setVisible(false);
    }

    /**
     * Closes this dialog on a cancel.
     */
    public void cancelAction()
    {
        cancelled = true;
        setVisible(false);
    }

    /**
     * Gets the results of the last execution of this dialog.  Results are unpredictable if the dialog has not yet
     * been executed.
     * @return The Color currently reflected by the color chooser in the dialog, or <code>null</code> if the dialog was
     *         cancelled on its last execution.
     */
    public Color getResult()
    {
        if (cancelled)
        {
            return null;
        } else
        {
            return chooser.getColor();
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //