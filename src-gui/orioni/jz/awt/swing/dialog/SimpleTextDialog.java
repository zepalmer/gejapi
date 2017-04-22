package orioni.jz.awt.swing.dialog;

import java.awt.*;

/**
 * This extension of the <code>AbstractTextDialog</code> accepts whatever text is entered.
 * @author Zachary Palmer
 */
public class SimpleTextDialog extends AbstractTextDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param master The Frame that controls the dialog.  If this is <code>null</code>, AWT's shared internal frame
     *               will be used (see JDialog constructor).
     * @param title The title of the dialog window.
     * @param labelText The text prompt instructing what should be put in the text field.
     */
    public SimpleTextDialog(Frame master, String title, String labelText)
    {
        super(master, title, labelText);
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
        text = field.getText();
        setVisible(false);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //