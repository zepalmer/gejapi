package orioni.jz.awt.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * This extension of <code>AbstractTextDialog</code> does not accept a text value unless it properly converts into a
 * <code>Double</code>.
 * @author Zachary Palmer
 */
public class DoubleValueDialog extends AbstractTextDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     */
    public DoubleValueDialog(Frame master,String title, String labelText)
    {
        super(master,title,labelText);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not the text is acceptable.  If this is the case, this method should set the
     * <code>m_text</code> value equal to the results of <code>m_field.getText()</code> and should also call
     * <code>setVisible(false)</code>.  Otherwise, a dialog (preferrably from <code>JOptionPane</code>) should
     * be displayed with information as to how to correct the syntax error.
     */
    public void okAction()
    {
        String textString = field.getText();
        try
        {
            Double.parseDouble(textString);
            super.text = textString;
            setVisible(false);
        } catch (NumberFormatException nfe)
        {
            JOptionPane.showMessageDialog(this,"That is not a number.","Not A Number",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //