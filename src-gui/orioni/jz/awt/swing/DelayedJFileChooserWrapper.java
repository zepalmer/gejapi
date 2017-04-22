package orioni.jz.awt.swing;

import orioni.jz.awt.swing.dialog.WaitingDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * This class was created in response to the very slow instantiation time of {@link JFileChooser} in the Sun J2SDK
 * version 1.4.1_02.  An instance of this class creates a {@link JFileChooser} on a separate thread of execution.  Once
 * the {@link JFileChooser} is constructed, it can be retrieved using the
 * {@link DelayedJFileChooserWrapper#getJFileChooser(Frame, boolean)} method.  If the file chooser is not ready, the
 * calling thread will block until the construction is complete, showing a dialog if such was specified upon
 * construction.  The readiness of the {@link JFileChooser} can be checked via the
 * {@link DelayedJFileChooserWrapper#isJFileChooserReady()} method.
 *
 * @author Zachary Palmer
 */
public class DelayedJFileChooserWrapper
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** Whether or not a dialog is shown if a call to {@link DelayedJFileChooserWrapper#getJFileChooser} has to
	 *  block. */
	protected boolean showDialog;
	/** The {@link JFileChooser} being wrapped by this object. */
	protected JFileChooser fileChooser;
	/** The {@link Thread} creating the {@link JFileChooser} for this object. */
	protected Thread thread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Skeleton constructor.  Assumes that the file chooser will be constructed with its void constructor.
	 * @param dialog <code>true</code> if a "Please wait" dialog is displayed when
	 *               {@link DelayedJFileChooserWrapper#getJFileChooser} is called before the construction thread has
	 *               completed; <code>false</code> otherwise.
	 */
	public DelayedJFileChooserWrapper(boolean dialog)
	{
		this(dialog, null);
	}

	/**
	 * General constructor.
	 * @param dialog <code>true</code> if a "Please wait" dialog is displayed when
	 *               {@link DelayedJFileChooserWrapper#getJFileChooser} is called before the construction thread has
	 *               completed; <code>false</code> otherwise.
	 * @param file The {@link File} object representing the {@link JFileChooser}'s starting directory, or
	 *             <code>null</code> to use the {@link JFileChooser}'s default constructor.
	 */
	public DelayedJFileChooserWrapper(boolean dialog, final File file)
	{
		super();
		showDialog = dialog;

		thread = new Thread(new Runnable()
		{
			public void run()
			{
				if (file == null)
				{
					fileChooser = new JFileChooser();
				} else
				{
					fileChooser = new JFileChooser(file);
				}
			}
		});

		thread.start();
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Determines whether or not the {@link JFileChooser} can be retrieved from the
	 * {@link DelayedJFileChooserWrapper#getJFileChooser(Frame, boolean)} method without blocking.
	 * @return <code>true</code> if the {@link JFileChooser} is ready now; <code>false</code> if blocking will be
	 *         required.
	 */
	public boolean isJFileChooserReady()
	{
		return ((thread == null) && (fileChooser != null));
	}

	/**
	 * Retrieves the {@link JFileChooser} wrapped by this object.  If the chooser is not yet constructed, this method
	 * will block until the {@link JFileChooser}'s construction is complete.  A dialog is also shown if such was
	 * specified upon construction.
	 * @param frame The {@link Frame} over which the dialog is to be displayed.  This can be <code>null</code> if the
	 *              dialog is not to be displayed or if it should use the shared window.
	 * @param disable <code>true</code> if the provided frame should be disabled while the wait dialog is showing;
	 *                <code>false</code> otherwise.
	 * @return The {@link JFileChooser} constructed by this object.
	 */
	public JFileChooser getJFileChooser(Frame frame, boolean disable)
	{
		if (thread != null)
		{
			JDialog dialog = null;
            boolean frameState = (frame != null && frame.isEnabled());
			if (showDialog)
			{
                dialog = new WaitingDialog(frame);
                dialog.setLocationRelativeTo(frame);
                final JDialog dialogFinal = dialog;
                Thread t = new Thread("JFileChooser Wait Dialog Showing Thread")
                {
                    public void run()
                    {
                        dialogFinal.setVisible(true);
                    }
                };
                t.start();
                Thread.yield();
				if ((frame != null) && (disable))
				{
					frame.setEnabled(false);
				}
			}
			while (thread.isAlive())
			{
				try
				{
					thread.join();
				} catch (InterruptedException e)
				{
                }
			}
			if (dialog != null)
			{
				if ((frame != null) && (disable))
				{
					frame.setEnabled(frameState);
				}
				dialog.setVisible(false);
                dialog.dispose();
			}
			thread = null;
		}
		return fileChooser;
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}