package orioni.jz.awt.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This listener is executes a method (provided on construction) whenever an event occurs.
 * @author Zachary Palmer
 */
public class ExecuteMethodActionListener extends ExecuteMethodListener implements ActionListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[])
	 */
	public ExecuteMethodActionListener(Object obj, String method, Object[] param)
	{
	    super(obj, method, param);
	}

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[])
	 */
	public ExecuteMethodActionListener(Class cl, String method, Object[] param)
	{
	    super(cl, method, param);
	}

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[], boolean)
	 */
	public ExecuteMethodActionListener(Object obj, String method, Object[] param, boolean newThread)
	{
	    super(obj, method, param, newThread);
	}

	/**
	 * Wrapper constructor.
	 * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[], boolean)
	 */
	public ExecuteMethodActionListener(Class cl, String method, Object[] param, boolean newThread)
	{
	    super(cl, method, param, newThread);
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invokes the associated method when an action is performed.
     * @param e The event which occurred.
     */
    public void actionPerformed(ActionEvent e)
    {
        super.invokeMethod();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //