package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.AWTUtilities;
import orioni.jz.awt.SpongyLayout;
import orioni.jz.util.Pair;
import orioni.jz.util.strings.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;

/**
 * This dialog displays a large amount of text inside of a scroll pane and presents an "OK" button.  Optionally, it can
 * also display a "Cancel" button.  This is accomplished by displaying the text in a {@link JLabel}.
 *
 * @author Zachary Palmer
 */
public class ScrollableTextDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The result of executing this dialog.  This field contains the text on the button that was pressed.
     */
    protected String result;
    /**
     * The {@link JLabel} which titles this dialog, preceeding the scroll pane.
     */
    protected JLabel titleLabel;
    /**
     * The {@link JLabel} inside of the scroll pane which contains the large amount of text.
     */
    protected JLabel content;
    /**
     * The {@link JScrollPane} in which to show the text.
     */
    protected JScrollPane scrollPane;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param masterFrame The frame that controls this dialog.
     * @param title        The title of this dialog.
     * @param modal        Whether or not this dialog is modal.
     * @param buttons      A {@link String}<code>[]</code> containing the names of the buttons to display along the
     *                     bottom.
     */
    public ScrollableTextDialog(Frame masterFrame, String title, boolean modal, String... buttons)
    {
        super(masterFrame, title, modal);
        initialize(buttons);
    }

    /**
     * Performs the initialization of this dialog.
     *
     * @param buttons A {@link String}<code>[]</code> containing the names of the buttons to display along the bottom.
     */
    private void initialize(final String... buttons)
    {
        this.getContentPane().setLayout(new SpongyLayout(SpongyLayout.Orientation.VERTICAL));
        titleLabel = new JLabel();
        content = new JLabel();
        content.setFont(new Font("SansSerif", Font.PLAIN, 12));
        content.setAlignmentY(Component.TOP_ALIGNMENT);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.getContentPane().add(titleLabel);
        scrollPane = new JScrollPane(content);
        scrollPane.setMinimumSize(new Dimension(600, 450));
        scrollPane.setPreferredSize(new Dimension(600, 450));
        scrollPane.setMaximumSize(new Dimension(600, 450));
        this.getContentPane().add(scrollPane);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        for (int i = 0; i < buttons.length; i++)
        {
            JButton button = new JButton(buttons[i]);
            buttonPanel.add(button);
            final JDialog scopedThis = this;
            final int iFinal = i;
            button.addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            result = buttons[iFinal];
                            scopedThis.setVisible(false);
                        }
                    });
        }
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.getContentPane().add(buttonPanel);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executes this dialog.
     *
     * @param titleText   The text to place above the scroll pane.
     * @param contentText The text to place within the scroll pane.
     */
    public void execute(String titleText, String contentText)
    {
        titleLabel.setText(titleText);
        Pair<String, Integer> newlined = AWTUtilities.getNewlinedString(
                contentText, content.getFont(), new int[]{100, 200, 300, 450, 600});

        content.setText("<html>" + StringUtilities.getHtmlSafeString(newlined.getFirst()).replaceAll("\n", "<p>"));
        scrollPane.setPreferredSize(
                new Dimension(
                        (int) ((Math.min(
                                600,
                                newlined.getSecond())) +
                                                       2 +
                                                       scrollPane.getVerticalScrollBar().getPreferredSize()
                                                               .getWidth()),
                        (int) ((Math.min(
                                450,
                                content.getFont().getStringBounds(
                                        newlined.getFirst(), new FontRenderContext(null, false, false))
                                        .getHeight() *
                                                     newlined.getFirst().split("\n").length)) + 2 +
                                                                                              scrollPane
                                                                                                      .getHorizontalScrollBar()
                                                                                                      .getPreferredSize()
                                                                                                      .getHeight())));
        this.pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Retrieves the text on the button that was pressed.
     *
     * @return The text on the button that was pressed, or <code>null</code> if the dialog has not yet been executed.
     */
    public String getResult()
    {
        return result;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Displays a {@link ScrollableTextDialog} using the provided {@link Frame} as a controller.  The provided dialog
     * title, message title and content are used.  The dialog will be modal with a single "OK" button, which closes the
     * window.
     *
     * @param frame         The controlling frame.
     * @param dialogTitle  The title of the dialog.
     * @param messageTitle The title displayed above the message.
     * @param messageText  The message to display.
     */
    public static void displayMessage(Frame frame, String dialogTitle, String messageTitle, String messageText)
    {
        ScrollableTextDialog dialog = new ScrollableTextDialog(frame, dialogTitle, true, "Ok");
        dialog.execute(messageTitle, messageText);
        dialog.dispose();
    }
}