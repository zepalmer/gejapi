package orioni.jz.awt.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * This extension of {@link AbstractTextDialog} does not accept any text value unless it properly converts into an
 * integer.
 * @author Zachary Palmer
 */
public class IntegerValueDialog extends AbstractTextDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The minimum legal integer value for this dialog. */
	protected int minimumValue;
	/** The maximum legal integer value for this dialog. */
	protected int maximumValue;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Wrapper constructor.
	 * @see AbstractTextDialog#AbstractTextDialog(java.awt.Frame, java.lang.String, java.lang.String)
	 */
	public IntegerValueDialog(Frame master, String title, String labelText)
	{
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, master, title, labelText);
	}

	/**
	 * General constructor.  In the event that the entered value is outside of the specified range when the value is
	 * converted to an integer, an error dialog is shown and the user is returned to this dialog to correct the mistake.
	 * @param minimumValue The minimum value accepted by this dialog.
	 * @param maximumValue The maximum value accepted by this dialog.
	 * @param master The frame controlling this dialog.
	 * @param title The title of this dialog.
	 * @param labelText The text to be used to prompt for an answer.
	 */
	public IntegerValueDialog(int minimumValue, int maximumValue, Frame master, String title, String labelText)
	{
		super(master, title, labelText);
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
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
		try
		{
			int value = Integer.parseInt(super.field.getText());
			if (value<minimumValue)
			{
				JOptionPane.showMessageDialog(
				        this, "The value "+value+" is less than the minimum legal value "+minimumValue +".",
				        "Value Too Low", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (value>maximumValue)
			{
				JOptionPane.showMessageDialog(
				        this, "The value "+value+" is greater than the maximum legal value "+maximumValue +".",
				        "Value Too High", JOptionPane.ERROR_MESSAGE);
				return;
			}
			super.text = super.field.getText();
			super.setVisible(false);
		} catch (NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(
			        this, "The value \""+super.field.getText()+"\" is not an integer value.",
			        "Not an Integer", JOptionPane.ERROR_MESSAGE);
		}
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}