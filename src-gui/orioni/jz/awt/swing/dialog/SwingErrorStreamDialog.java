package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.SpongyLayout;
import orioni.jz.awt.swing.convenience.ComponentConstructorPanel;
import orioni.jz.io.ForkingOutputStream;
import orioni.jz.io.NotifyingOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This class is designed to redirect the standard error stream of a Swing application and instead present that stream
 * in a {@link JDialog}.  Whenever new information is written to the standard error stream, this {@link JDialog} will
 * present itself, informing the user of all writes to {@link System#err} thus far.
 * <p/>
 * The error data is displayed in a non-editable text frame with a scroll bar, allowing the text to be copied from the
 * application if necessary.  This {@link JDialog} also provides a checkbox to allow the user to specify that it should
 * not be shown for the remainder of the application's execution.  This is a non-modal dialog.
 * <p/>
 * In order to preserve the behavior of the program in which this dialog operates, any information which is written to
 * the {@link System#err} stream will be handled twice: it will be buffered by this dialog for display purposes as well
 * as written to the original {@link System#err} stream.
 *
 * @author Zachary Palmer
 */
public class SwingErrorStreamDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link ByteArrayOutputStream} used to buffer the error data.
     */
    protected ByteArrayOutputStream bufferStream;
    /**
     * The last time at which this dialog was set visible.
     */
    protected long lastSetVisibleTime;
    /**
     * The thread on which a deferred visibility change is placed.
     */
    protected Thread setVisibleThread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param owner The owner of this dialog.
     * @param title The title of this dialog.
     * @throws SecurityException If the installed security manager will not allow this dialog to replace the standard
     *                           error stream.
     */
    public SwingErrorStreamDialog(Frame owner, String title)
            throws SecurityException
    {
        super(owner, title);

        final JTextArea text = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        final JCheckBox hideBox = new JCheckBox("Do not display this dialog again.");
        hideBox.setSelected(false);
        JButton closeButton = new JButton("Close");
        closeButton.setMnemonic(KeyEvent.VK_C);

        this.setContentPane(
                new ComponentConstructorPanel(
                        new SpongyLayout(SpongyLayout.Orientation.VERTICAL, true, true),
                        scrollPane,
                        new ComponentConstructorPanel(
                                new SpongyLayout(SpongyLayout.Orientation.HORIZONTAL, false, false), hideBox),
                        new ComponentConstructorPanel(
                                new SpongyLayout(SpongyLayout.Orientation.HORIZONTAL, false, false), closeButton)));

        closeButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        setVisible(false);
                    }
                });

        bufferStream = new ByteArrayOutputStream();
        System.setErr(
                new PrintStream(
                        new ForkingOutputStream(
                                System.err,
                                new NotifyingOutputStream(bufferStream)
                                {
                                    public void writeOccurred()
                                    {
                                        text.setText(bufferStream.toString());
                                        if (!hideBox.isSelected())
                                        {
                                            scrollPane.getVerticalScrollBar().setValue(
                                                    scrollPane.getVerticalScrollBar().getMaximum());
                                            setVisible(true);
                                        }
                                    }
                                })));

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        this.pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the visibility state of this dialog.  If the dialog is set visible more than once a second, the actual
     * change is deferred for at least one second.  This is provided to allow the user time to make decisions using the
     * dialog.
     *
     * @param visible <code>true</code> if the dialog should be visible; <code>false</code> if it should not.
     */
    public void setVisible(boolean visible)
    {
        if (visible)
        {
            synchronized (this)
            {
                if (System.currentTimeMillis() - lastSetVisibleTime > 1000)
                {
                    lastSetVisibleTime = System.currentTimeMillis();
                    super.setVisible(true);
                } else
                {
                    if (setVisibleThread != null)
                    {
                        final SwingErrorStreamDialog scopedThis = this;
                        setVisibleThread = new Thread()
                        {
                            public void run()
                            {
                                long setVisibleTime;
                                synchronized (scopedThis)
                                {
                                    setVisibleTime = lastSetVisibleTime + 1000;
                                }
                                while (System.currentTimeMillis() < setVisibleTime)
                                {
                                    try
                                    {
                                        this.wait(Math.max(10, setVisibleTime - System.currentTimeMillis()));
                                    } catch (InterruptedException e)
                                    {
                                        // Just try again.
                                    }
                                }
                                synchronized (scopedThis)
                                {
                                    setVisibleThread = null;
                                    scopedThis.setVisible(true);
                                }
                            }
                        };
                        setVisibleThread.start();
                    }
                }
            }
        } else
        {
            super.setVisible(false);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE