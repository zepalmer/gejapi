package orioni.jz.awt.swing.dialog;

import java.awt.*;

/**
 * This extension of AbstractChoiceDialog accepts any value on the combo box provided by the user.
 *
 * @author Zachary Palmer
 */
public class SimpleChoiceDialog extends AbstractChoiceDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see AbstractChoiceDialog#AbstractChoiceDialog(Frame,String,String)
     */
    public SimpleChoiceDialog(Frame master, String title, String labelText)
    {
        super(master, title, labelText);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Accepts the given value in the combo box.  This operation performs no checks, since this dialog is designed to
     * accept whatever choice it is given.
     */
    public void okAction()
    {
        super.result = super.comboBox.getSelectedItem();
        setVisible(false);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //