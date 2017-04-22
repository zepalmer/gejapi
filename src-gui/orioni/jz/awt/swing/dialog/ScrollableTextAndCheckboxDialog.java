package orioni.jz.awt.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * <B><I>This file lacks a description.</I></B>
 *
 * @author Zachary Palmer
 */
public class ScrollableTextAndCheckboxDialog extends ScrollableTextDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link JCheckBox} for this dialog.
     */
    protected JCheckBox checkbox;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param masterFrame The frame that controls this dialog.
     * @param title        The title of this dialog.
     * @param modal        Whether or not this dialog is modal.
     * @param checkbox     The text to appear by the checkbox.
     * @param buttons      A {@link String}<code>[]</code> containing the names of the buttons to display along the
     *                     bottom.
     */
    public ScrollableTextAndCheckboxDialog(Frame masterFrame, String title, boolean modal, String checkbox,
                                           String... buttons)
    {
        super(masterFrame, title, modal, buttons);
        this.checkbox = new JCheckBox(checkbox);
        this.getContentPane().add(this.checkbox);
        pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executes this dialog.
     *
     * @param titleText     The text to place above the scroll pane.
     * @param contentText   The text to place within the scroll pane.
     * @param checkboxState The state of the checkbox: <code>true</code> for checked; <code>false</code> for
     *                       unchecked.
     */
    public void execute(String titleText, String contentText, boolean checkboxState)
    {
        checkbox.setSelected(checkboxState);
        super.execute(titleText, contentText);
    }

    /**
     * Determines whether or not the checkbox is currently selected.
     *
     * @return <code>true</code> if the checkbox is selected; <code>false</code> otherwise.
     */
    public boolean isCheckboxSelected()
    {
        return checkbox.isSelected();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Displays a {@link ScrollableTextDialog} using the provided {@link Frame} as a controller.  The provided dialog
     * title, message title and content are used.  The dialog will be modal with a single "OK" button, which closes the
     * window.
     *
     * @param frame            The controlling frame.
     * @param dialogTitle     The title of the dialog.
     * @param messageTitle    The title displayed above the message.
     * @param messageText     The message to display.
     * @param checkboxMessage The message to display by the checkbox.
     * @param checkboxState   The initial state of the checkbox.
     * @return The resulting state of the checkbox.
     */
    public static boolean displayMessage(Frame frame, String dialogTitle, String messageTitle, String messageText,
                                         String checkboxMessage, boolean checkboxState)
    {
        ScrollableTextAndCheckboxDialog dialog = new ScrollableTextAndCheckboxDialog(
                frame, dialogTitle, true, checkboxMessage, "Ok");
        dialog.execute(messageTitle, messageText, checkboxState);
        boolean ret = dialog.isCheckboxSelected();
        dialog.dispose();
        return ret;
    }
}

// END OF FILE