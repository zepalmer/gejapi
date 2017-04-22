package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.SpongyLayout;
import orioni.jz.awt.swing.ProgressBarTracker;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link JDialog} extension contains a single {@link JProgressBar} and, optionally, a text message and a button. A
 * {@link ProgressBarTracker} is automatically attached and made available.  The purpose of this class is to provide a
 * common means by which a progress dialog may be shown.
 *
 * @author Zachary Palmer
 */
public class ProgressDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link ProgressBarTracker} which tracks progress and updates the progress bar accordingly.
     */
    protected ProgressBarTracker tracker;
    /**
     * The {@link JButton} for this dialog or <code>null</code> if a button has not been created.
     */
    protected JButton button;
    /**
     * The {@link JLabel} which is used to display the message to the user.
     */
    protected JLabel messageLabel;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param textMessage A text message to appear above the progress bar or <code>null</code> for no message.
     * @param buttonText  A text to appear on the button or <code>null</code> for no button.
     */
    public ProgressDialog(String textMessage, String buttonText)
    {
        super();
        JProgressBar bar = new JProgressBar();
        tracker = new ProgressBarTracker(bar);

        Container c = this.getContentPane();
        c.setLayout(new SpongyLayout(SpongyLayout.Orientation.VERTICAL, 2, 2, false, false));
        if (textMessage != null)
        {
            messageLabel = new JLabel(textMessage);
        }
        c.add(bar);
        if (buttonText != null)
        {
            button = new JButton(buttonText);
            c.add(button);
        }

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        this.pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the progress bar tracker for this dialog.
     *
     * @return The {@link ProgressBarTracker} which updates the bar on this dialog.
     */
    public ProgressBarTracker getTracker()
    {
        return tracker;
    }

    /**
     * Retrieves the button for this dialog or <code>null</code> if no button was created.
     *
     * @return The button for this dialog or <code>null</code>.
     */
    public JButton getButton()
    {
        return button;
    }

    /**
     * Changes the text message displayed by this dialog's label <i>if</i> the dialog was originally constructed with a
     * message.  If not, nothing happens.
     *
     * @param message The new message to display.
     */
    public void setMessage(String message)
    {
        if (messageLabel != null) messageLabel.setText(message);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
