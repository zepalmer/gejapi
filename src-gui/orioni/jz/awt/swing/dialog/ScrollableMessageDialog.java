package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.SpongyLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This dialog is used to display long messages to the user.  It displays the message in a {@link JTextArea} which is
 * contained within a {@link JScrollPane}, thus allowing the message to be scrolled and copied to the clipboard.
 *
 * @author Zachary Palmer
 */
public class ScrollableMessageDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param owner The owner of this dialog.
     * @param title The title for the dialog.
     * @param message The message to display in the dialog.
     */
    public ScrollableMessageDialog(Frame owner, String title, String message)
    {
        super(owner, title, true);
        initialize(message);
    }

    /**
     * General constructor.
     * @param owner The owner of this dialog.
     * @param title The title for the dialog.
     * @param message The message to display in the dialog.
     */
    public ScrollableMessageDialog(Dialog owner, String title, String message)
    {
        super(owner, title, true);
        initialize(message);
    }

    /**
     * Initializes this dialog.
     * @param message The message to display in the dialog.
     */
    private void initialize(String message)
    {
        JTextArea area = new JTextArea(12, 40);
        area.setText(message);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(new JScrollPane(area), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new SpongyLayout(SpongyLayout.Orientation.HORIZONTAL, 2, 2, false));
        c.add(buttonPanel, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        buttonPanel.add(okButton);
        okButton.setMnemonic(KeyEvent.VK_O);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });

        pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Displays this dialog.
     */
    public void execute()
    {
        setVisible(true);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Creates a dialog and displays it around the provided component.
     * @param target The {@link Component} around which to display the dialog, or <code>null</code> to center the
     *               dialog on the screen.
     * @param title The title for the dialog.
     * @param message The message to display in the dialog.
     */
    public static void reportMessage(Component target, String title, String message)
    {
        Component ancestor = target;
        while ((ancestor!=null) && (!(ancestor instanceof Frame)) && (!(ancestor instanceof Dialog)))
        {
            ancestor = SwingUtilities.getWindowAncestor(ancestor);
        }
        ScrollableMessageDialog dialog;
        if (ancestor==null)
        {
            dialog = new ScrollableMessageDialog((Frame)null, title, message);
            dialog.setLocationRelativeTo(null);
            dialog.execute();
        } else
        {
            if (ancestor instanceof Frame)
            {
                dialog = new ScrollableMessageDialog((Frame)ancestor, title, message);
            } else
            {
                dialog = new ScrollableMessageDialog((Dialog)ancestor, title, message);
            }
            dialog.setLocationRelativeTo(target);
            dialog.execute();
        }
        dialog.dispose();
    }
}

// END OF FILE