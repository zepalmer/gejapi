package orioni.jz.awt.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This class is designed to report an exception using the Swing user environment rather than the command prompt.
 *
 * @author Zachary Palmer
 */
public class ExceptionDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

    /**
     * The title which is used for this dialog if one is not provided.
     */
    protected static final String DEFAULT_TITLE = "Exception Reported";

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Uses the common frame as its owner and a generic title.
     *
     * @param e The {@link Throwable} which is being reported.
     */
    public ExceptionDialog(Throwable e)
    {
        this((Frame) null, DEFAULT_TITLE, e);
    }

    /**
     * Skeleton constructor.  Uses the common frame as its owner.
     *
     * @param title The title of the dialog.
     * @param e     The {@link Exception} which is being reported.
     */
    public ExceptionDialog(String title, Throwable e)
    {
        this((Frame) null, title, e);
    }

    /**
     * Skeleton constructor.  Uses a generic title.
     *
     * @param owner The {@link Frame} which owns this dialog.
     * @param e     The {@link Exception} which is being reported.
     */
    public ExceptionDialog(Frame owner, Throwable e)
    {
        this(owner, DEFAULT_TITLE, e);
    }

    /**
     * Skeleton constructor.  Uses a generic title.
     *
     * @param owner The {@link Dialog} which owns this dialog.
     * @param e     The {@link Exception} which is being reported.
     */
    public ExceptionDialog(Dialog owner, Throwable e)
    {
        this(owner, DEFAULT_TITLE, e);
    }

    /**
     * General constructor.  Uses a {@link Frame} owner.
     *
     * @param owner The {@link Frame} which owns this dialog.
     * @param title The title of the dialog.
     * @param e     The {@link Exception} which is being reported.
     */
    public ExceptionDialog(Frame owner, String title, Throwable e)
    {
        super(owner, title, true);
        initialize(e);
    }

    /**
     * General constructor.  Uses a {@link Dialog} owner.
     *
     * @param owner The {@link Dialog} which owns this dialog.
     * @param title The title of the dialog.
     * @param e     The {@link Exception} which is being reported.
     */
    public ExceptionDialog(Dialog owner, String title, Throwable e)
    {
        super(owner, title, true);
        initialize(e);
    }

    /**
     * Initializes this dialog.  Used eventually by all constructors.
     *
     * @param e The {@link Exception} which is being reported.
     */
    private void initialize(Throwable e)
    {
        Container pane = this.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(new JLabel("The following exception has been reported:"));
        JScrollPane scrollPane = new JScrollPane();
        pane.add(scrollPane);
        JButton okButton = new JButton("OK");
        pane.add(okButton);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        JTextArea stackTrace = new JTextArea(baos.toString());
        stackTrace.setEditable(false);
        stackTrace.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane.getViewport().setView(stackTrace);

        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        setVisible(false);
                    }
                });

        this.pack();
        this.setLocationRelativeTo(null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Displays this dialog.  Simply calls <code>setVisible(true)</code>.
     */
    public void execute()
    {
        this.setVisible(true);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Creates an exception dialog and displays it based upon the provided exception.
     *
     * @param e The exception to be reported.
     */
    public static void reportException(Throwable e)
    {
        ExceptionDialog dialog = new ExceptionDialog(e);
        dialog.execute();
        dialog.dispose();
    }

    /**
     * Creates an exception dialog and displays it based upon the provided exception.  This dialog is created over the
     * provided {@link Window}.
     *
     * @param window The {@link Window} which should control the exception.  This must either be a {@link Frame} or a
     *               {@link Dialog}.  If it is not, the {@link ExceptionDialog#reportException(Throwable)} method is
     *               called instead.
     * @param e      The exception to be reported.
     */
    public static void reportException(Window window, Throwable e)
    {
        ExceptionDialog dialog = null;
        if (window instanceof Frame)
        {
            dialog = new ExceptionDialog((Frame) window, e);
        } else if (window instanceof Dialog)
        {
            dialog = new ExceptionDialog((Dialog) window, e);
        }
        if (dialog == null)
        {
            reportException(e);
        } else
        {
            dialog.execute();
            dialog.dispose();
        }
    }
}