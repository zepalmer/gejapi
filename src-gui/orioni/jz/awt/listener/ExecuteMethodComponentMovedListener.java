package orioni.jz.awt.listener;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * This listener is executes a method (provided on construction) whenever the commanding component is moved.
 * @author Zachary Palmer
 */
public class ExecuteMethodComponentMovedListener extends ExecuteMethodListener implements ComponentListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[])
     */
    public ExecuteMethodComponentMovedListener(Object obj, String method, Object[] param)
    {
        super(obj, method, param);
    }

    /**
     * Wrapper constructor.
     * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[])
     */
    public ExecuteMethodComponentMovedListener(Class cl, String method, Object[] param)
    {
        super(cl, method, param);
    }

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[], boolean)
	 */
	public ExecuteMethodComponentMovedListener(Object obj, String method, Object[] param, boolean newThread)
	{
	    super(obj, method, param, newThread);
	}

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[], boolean)
	 */
	public ExecuteMethodComponentMovedListener(Class cl, String method, Object[] param, boolean newThread)
	{
	    super(cl, method, param, newThread);
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executed when the given component moves.
     */
    public void componentMoved(ComponentEvent e)
    {
        super.invokeMethod();
    }

    /**
     * Does nothing.
     */
    public void componentHidden(ComponentEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void componentResized(ComponentEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void componentShown(ComponentEvent e)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //