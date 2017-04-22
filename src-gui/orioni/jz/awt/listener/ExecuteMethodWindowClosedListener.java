package orioni.jz.awt.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * This listener is executes a method (provided on construction) whenever the window is passed a "closed" event.
 * @author Zachary Palmer
 */
public class ExecuteMethodWindowClosedListener extends ExecuteMethodListener implements WindowListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[])
     */
    public ExecuteMethodWindowClosedListener(Object obj, String method, Object[] param)
    {
        super(obj, method, param);
    }

    /**
     * Wrapper constructor.
     * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[])
     */
    public ExecuteMethodWindowClosedListener(Class cl, String method, Object[] param)
    {
        super(cl, method, param);
    }

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[], boolean)
	 */
	public ExecuteMethodWindowClosedListener(Object obj, String method, Object[] param, boolean newThread)
	{
	    super(obj, method, param, newThread);
	}

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[], boolean)
	 */
	public ExecuteMethodWindowClosedListener(Class cl, String method, Object[] param, boolean newThread)
	{
	    super(cl, method, param, newThread);
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invokes this listener's method when the window is closed.
     */
    public void windowClosed(WindowEvent e)
    {
        invokeMethod();
    }

    /**
     * Does nothing.
     */
    public void windowOpened(WindowEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void windowIconified(WindowEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void windowDeiconified(WindowEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void windowDeactivated(WindowEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void windowClosing(WindowEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void windowActivated(WindowEvent e)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //