package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.swing.SpacingComponent;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * This dialog is designed to present the user with the message "<code>Please Wait...</code>" or some other simple
 * message for a period of time.  It is non-modal.  The message which is to be displayed can be configured at
 * construction time.
 *
 * @author Zachary Palmer
 */
public class WaitingDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link JProgressBar} to use in this {@link WaitingDialog}. */
    protected JProgressBar progressBar;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Uses the message "<code>Please Wait...</code>".
     *
     * @param frame The {@link Frame} that will own this dialog.
     */
    public WaitingDialog(Frame frame)
    {
        this(frame, "Please Wait...");
    }

    /**
     * Skeleton constructor.  Uses the message "<code>Please Wait...</code>".
     *
     * @param dialog The {@link Dialog} that will own this dialog.
     */
    public WaitingDialog(Dialog dialog)
    {
        this(dialog, "Please Wait...");
    }

    /**
     * General constructor.
     *
     * @param frame   The {@link Frame} that will own this dialog.
     * @param message The message to display.
     */
    public WaitingDialog(Frame frame, String message)
    {
        super(frame, true);
        initialize(message);
    }

    /**
     * General constructor.
     *
     * @param dialog  The {@link Dialog} that will own this dialog.
     * @param message The message to display.
     */
    public WaitingDialog(Dialog dialog, String message)
    {
        super(dialog, true);
        initialize(message);
    }

    /**
     * Initializes the dialog.
     *
     * @param message The message to display.
     */
    private void initialize(String message)
    {
        setUndecorated(true);
        JPanel contentPane = new JPanel(new FlowLayout());
        setContentPane(contentPane);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(message);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(new SpacingComponent(new Dimension(5, 5)));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(progressBar);
        contentPane.add(panel);
        contentPane.setBorder(
                new CompoundBorder(
                        new MatteBorder(1, 1, 1, 1, label.getForeground()),
                        new MatteBorder(4, 4, 4, 4, progressBar.getForeground())));
        pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the progress bar used by this dialog.
     * @return The progress bar used by this dialog.
     */
    public JProgressBar getProgressBar()
    {
        return progressBar;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE