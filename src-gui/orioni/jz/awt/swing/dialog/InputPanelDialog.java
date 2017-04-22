package orioni.jz.awt.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This dialog is intended to be a general-purpose input dialog.  Upon construction, an {@link InputPanel} is provided
 * to this dialog.  This dialog will consist of two buttons (OK and Cancel) and the provided panel.  Upon the exit of
 * the {@link InputPanelDialog}, the results will be available accessible through
 * {@link InputPanelDialog#getResult(Object)}, which acts as a transparent call to the provided panel's
 * {@link InputPanel#getResult(Object)} method.
 * @author Zachary Palmer
 */
public class InputPanelDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The {@link InputPanel} that serves to input values for this {@link JDialog}. */
	protected InputPanel panel;
	/** Determines whether or not this dialog completed successfully. */
	protected boolean completed;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Skeleton constructor.  Assumes that this dialog is non-modal and gives it a blank title.  The owner of this
	 * dialog is a shared, invisible frame.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 */
	public InputPanelDialog(InputPanel inputPanel)
	{
		this((Frame)null, inputPanel, "", false);
	}

	/**
	 * Skeleton constructor.  Assumes that this dialog is non-modal and gives it a blank title.
	 * @param owner The {@link Frame} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 */
	public InputPanelDialog(Frame owner, InputPanel inputPanel)
	{
		this(owner, inputPanel, "", false);
	}

	/**
	 * Skeleton constructor.  Gives this dialog a blank title.
	 * @param owner The {@link Frame} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 * @param modal <code>true</code> if this dialog is modal; <code>false</code> otherwise.
	 */
	public InputPanelDialog(Frame owner, InputPanel inputPanel, boolean modal)
	{
		this(owner, inputPanel, "", modal);
	}

	/**
	 * Skeleton constructor.  Assumes that this dialog is non-modal.
	 * @param owner The {@link Frame} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 * @param title The title of this {@link InputPanelDialog}.
	 */
	public InputPanelDialog(Frame owner, InputPanel inputPanel, String title)
	{
		this(owner, inputPanel, title, false);
	}

	/**
	 * General constructor.
	 * @param owner The {@link Frame} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 * @param title The title of this {@link InputPanelDialog}.
	 * @param modal <code>true</code> if this dialog is modal; <code>false</code> otherwise.
	 */
	public InputPanelDialog(Frame owner, InputPanel inputPanel, String title, boolean modal)
	{
		super(owner, title, modal);
		initialize(inputPanel);
	}

	/**
	 * Skeleton constructor.  Assumes that this dialog is non-modal and gives it a blank title.
	 * @param owner The {@link Dialog} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 */
	public InputPanelDialog(Dialog owner, InputPanel inputPanel)
	{
		this(owner, inputPanel, "", false);
	}

	/**
	 * Skeleton constructor.  Gives this dialog a blank title.
	 * @param owner The {@link Dialog} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 * @param modal <code>true</code> if this dialog is modal; <code>false</code> otherwise.
	 */
	public InputPanelDialog(Dialog owner, InputPanel inputPanel, boolean modal)
	{
		this(owner, inputPanel, "", modal);
	}

	/**
	 * Skeleton constructor.  Assumes that this dialog is non-modal.
	 * @param owner The {@link Dialog} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 * @param title The title of this {@link InputPanelDialog}.
	 */
	public InputPanelDialog(Dialog owner, InputPanel inputPanel, String title)
	{
		this(owner, inputPanel, title, false);
	}

	/**
	 * General constructor.
	 * @param owner The {@link Dialog} to which this dialog belongs.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 * @param title The title of this {@link InputPanelDialog}.
	 * @param modal <code>true</code> if this dialog is modal; <code>false</code> otherwise.
	 */
	public InputPanelDialog(Dialog owner, InputPanel inputPanel, String title, boolean modal)
	{
		super(owner, title, modal);
		initialize(inputPanel);
	}

	/**
	 * Initializes this dialog.
	 * @param inputPanel The {@link InputPanel} that will serve to input values for this {@link JDialog}.
	 */
	private void initialize(InputPanel inputPanel)
	{
		panel = inputPanel;
		JPanel buttonPanel = new JPanel();
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
        buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(panel);
		this.getContentPane().add(buttonPanel);

		final InputPanelDialog scopedThis = this;

		ok.addActionListener(
		        new ActionListener()
		        {
			        public void actionPerformed(ActionEvent e)
			        {
				        String error = panel.getFirstError();
				        if (error==null)
				        {
					        completed = true;
					        scopedThis.setVisible(false);
				        } else
				        {
					        JOptionPane.showMessageDialog(scopedThis, error, "Error", JOptionPane.ERROR_MESSAGE);
				        }
			        }
		        }
		);
		cancel.addActionListener(
		        new ActionListener()
		        {
			        public void actionPerformed(ActionEvent e)
			        {
				        completed = false;
				        scopedThis.setVisible(false);
			        }
		        }
		);

        ok.setMnemonic('O');
        cancel.setMnemonic('C');
        getRootPane().setDefaultButton(ok);

        for (Object o : panel.getFieldKeySet())
        {
            final InputPanel.InputField field = panel.getField(o);
            if (field.isFocusDefault())
            {
                this.addComponentListener(
                        new ComponentAdapter()
                        {
                            public void componentShown(ComponentEvent e)
                            {
                                field.getComponent().requestFocus();
                            }
                        });
                break;
            }
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
	 * Executes this dialog.
	 * @param initialize <code>true</code> if the {@link InputPanel} should be initialized before proceeding,
	 *                   <code>false</code> if it should be left in its present state.
	 */
	public void execute(boolean initialize)
	{
        if (initialize) panel.initialize();
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Determines whether or not the user completed the dialog.
	 * @return <code>true</code> if the dialog completed successfully, <code>false</code> if the dialog was cancelled
	 *         by the user.
	 */
	public boolean completed()
	{
		return completed;
	}

	/**
	 * Retrieves the results of the execution of this dialog.  This method should only be called if the result of the
	 * {@link InputPanelDialog#completed()} method is <code>true</code>; otherwise, this method is not guaranteed to
	 * behave in a predictable manner.
	 * @param key The key by which the result should be retrieved, as specified by the {@link InputPanel}'s
	 *            {@link InputPanel.InputField}s.
	 * @return The value obtained by the {@link InputPanel.InputField}.
	 * @throws NullPointerException If the key does not map to an {@link InputPanel.InputField} in this dialog's
	 *                              {@link InputPanel}.
	 */
	public Object getResult(Object key)
			throws NullPointerException
	{
		return panel.getResult(key);
	}

    /**
     * Retrieves the {@link InputPanel} being displayed by this dialog.
     * @return The {@link InputPanel} being displayed by this dialog.
     */
    public InputPanel getInputPanel()
    {
        return panel;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}