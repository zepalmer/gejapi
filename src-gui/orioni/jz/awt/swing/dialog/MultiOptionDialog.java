package orioni.jz.awt.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This {@link JDialog} extension operates as a normal dialog with a single {@link JLabel} as its contents and a list of
 * option buttons determined on construction.  Instances of this dialog simply return the appropriate value from the
 * constants list when executed.
 *
 * @author Zachary Palmer
 */
public class MultiOptionDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A return value indicating that the user closed the dialog without use of the control buttons.
     */
    public static final int OPTION_CANCEL = -2;
    /**
     * A return value indicating that no response has yet been ascertained.
     */
    public static final int OPTION_UNKNOWN = -1;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link JLabel} which defines the content of this {@link MultiOptionDialog}.
     */
    protected JLabel label;
    /**
     * The field in which the result is kept after selection.
     */
    protected int result;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a modal dialog without a specified owner.
     *
     * @param title   The title of this dialog.
     * @param message The message for this dialog.
     * @param options The list of strings which will appear on the option buttons.
     */
    public MultiOptionDialog(String title, String message, String... options)
    {
        this((Frame) null, title, true, message, options);
    }

    /**
     * Skeleton constructor.  Assumes a modal dialog without a specified owner.
     *
     * @param frame   The frame which owns this dialog.
     * @param title   The title of this dialog.
     * @param message The message for this dialog.
     * @param options The list of strings which will appear on the option buttons.
     */
    public MultiOptionDialog(Frame frame, String title, String message, String... options)
    {
        this(frame, title, true, message, options);
    }

    /**
     * Skeleton constructor.  Assumes a modal dialog without a specified owner.
     *
     * @param dialog  The dialog which owns this dialog.
     * @param title   The title of this dialog.
     * @param message The message for this dialog.
     * @param options The list of strings which will appear on the option buttons.
     */
    public MultiOptionDialog(Dialog dialog, String title, String message, String... options)
    {
        this(dialog, title, true, message, options);
    }

    /**
     * General constructor.
     *
     * @param frame   The frame which owns this dialog.
     * @param title   The title of this dialog.
     * @param modal   <code>true</code> if this is to be a modal dialog; <code>false</code> if it is non-modal.
     * @param message The message for this dialog.
     * @param options The list of strings which will appear on the option buttons.
     */
    public MultiOptionDialog(Frame frame, String title, boolean modal, String message, String... options)
    {
        super(frame, title, modal);
        init(message, options);
    }

    /**
     * General constructor.
     *
     * @param dialog  The dialog which owns this dialog.
     * @param title   The title of this dialog.
     * @param modal   <code>true</code> if this is to be a modal dialog; <code>false</code> if it is non-modal.
     * @param message The message for this dialog.
     * @param options The list of strings which will appear on the option buttons.
     */
    public MultiOptionDialog(Dialog dialog, String title, boolean modal, String message, String... options)
    {
        super(dialog, title, modal);
        init(message, options);
    }

    /**
     * Initializer.  Called by all constructors eventually.
     *
     * @param message The message for this dialog.
     * @param options The list of strings which will appear on the option buttons.
     */
    private void init(String message, String... options)
    {
        label = new JLabel(message);
        this.getContentPane().setLayout(new BorderLayout(3+3*options.length,3+3*options.length));
        this.getContentPane().add(label, BorderLayout.CENTER);
        JPanel panel = new JPanel(new GridLayout(1, 4,3,3));
        this.getContentPane().add(panel, BorderLayout.SOUTH);
        for (int i = 0; i < options.length; i++)
        {
            JButton optionButton = new JButton(options[i]);
            panel.add(optionButton);
            final int iFinal = i;
            optionButton.addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            result = iFinal;
                            setVisible(false);
                        }
                    });
        }
        this.addComponentListener(new ComponentAdapter()
        {
            public void componentHidden(ComponentEvent e)
            {
                if (result ==OPTION_UNKNOWN) result = OPTION_CANCEL;
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executes this dialog.  The dialog is displayed and, once the dialog is no longer visible, the result of this
     * dialog's execution can be retrieved through the {@link MultiOptionDialog#getResult()} method or through the
     * return value of this method call.
     */
    public int execute()
    {
        result = OPTION_UNKNOWN;
        this.setVisible(true);
        return result;
    }

    /**
     * Retrieves the result of this dialog's execution.  This will be one of the special <code>OPTION_XXXX</code>
     * constants on this class or the index of the chosen option.
     * @return The index of the chosen option (if an option was chosen), {@link MultiOptionDialog#OPTION_UNKNOWN} if
     * the user has not yet made a decision, or {@link MultiOptionDialog#OPTION_CANCEL} if the user closed the dialog
     * without making a choice.
     */
    public int getResult()
    {
        return result;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}