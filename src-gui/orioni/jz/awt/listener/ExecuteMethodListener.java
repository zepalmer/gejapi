package orioni.jz.awt.listener;

import orioni.jz.reflect.ReflectionUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is the base class for all <code>ExecuteMethod...Listener</code> classes.  It controls the location and
 * execution of the reflected method on an event.
 * <P>
 * Extenders of this class simply need to call the <code>invokeMethod</code> method whenever the reflected method needs
 * to be invoked.
 *
 * @author Zachary Palmer
 */
public abstract class ExecuteMethodListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The object on which the method is to be called. */
    protected Object obj;
    /** The Method to call on an action. */
    protected Method method;
    /** The parameters to pass to the method on call. */
    protected Object[] param;
    /** The types of the parameters to pass to the method on call. */
    protected Class[] paramTypes;
	/** Whether or not the call needs to be made on a new thread. */
	protected boolean newThread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Skeleton constructor for non-static methods.  If a static method needs to be called, use the constructor which
	 * accepts a class instead of an object.  This constructor assumes that the AWT listener thread will be used to
	 * make the call.
	 * @param obj The object on which the method is to be called.
	 * @param method The method to call on an action.
	 * @param param The parameters to pass to the method on call.
	 */
	public ExecuteMethodListener(Object obj, String method, Object[] param)
	{
		this(obj, method, param, false);
	}

	/**
	 * General constructor for static methods.  If a non-static method needs to be called, use the constructor which
	 * accepts an object instead of a class.  This constructor assumes that the AWT listener thread will be used to
	 * make the call.
	 * @param cl The class for which a static method is to be called.
	 * @param method The method to call on an action.
	 * @param param The parameters to pass to the method on call.
	 */
	public ExecuteMethodListener(Class cl, String method, Object[] param)
	{
		this(cl, method, param, false);
	}

	/**
	 * General constructor for non-static methods.  If a static method needs to be called, use the constructor which
	 * accepts a class instead of an object.
	 * @param obj The object on which the method is to be called.
	 * @param method The method to call on an action.
	 * @param param The parameters to pass to the method on call.
	 * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
	 *                   call is succinct enough to be called on the AWT thread.
	 */
	public ExecuteMethodListener(Object obj, String method, Object[] param, boolean newThread)
	{
	    this(obj.getClass(),method,param, newThread);
	    this.obj = obj;
	}

	/**
	 * General constructor for static methods.  If a non-static method needs to be called, use the constructor which
	 * accepts an object instead of a class.
	 * @param cl The class for which a static method is to be called.
	 * @param method The method to call on an action.
	 * @param param The parameters to pass to the method on call.
	 * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
	 *                   call is succinct enough to be called on the AWT thread.
	 */
	public ExecuteMethodListener(Class cl, String method, Object[] param, boolean newThread)
	{
	    obj = null;
		this.newThread = newThread;
	    this.param = param;
	    Method[] allMethods = ReflectionUtilities.findMethods(cl,method,param);
	    if (allMethods.length>1)
	    {
	        throw new Error(this.getClass().getName()+" construction failure: Ambiguous execution method description");
	    }
	    try
	    {
	        this.method = allMethods[0];
	    } catch (ArrayIndexOutOfBoundsException oob)
	    {
	        throw new Error(this.getClass().getName()+" construction failure: No execution match from description."+
	                "  Check to ensure that permissions are correct.");
	    }
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invokes the method in question.  This invocation will occur on a seperate thread if appropriate.
     */
    protected void invokeMethod()
    {
        if (newThread)
        {
	        new Thread(new Runnable()
	        {
		        public void run()
		        {
			        invokeMethodImpl();
		        }
	        }).start();
        } else invokeMethodImpl();
    }

	/**
	 * Actually performs the invocation on the calling thread.
	 */
	private void invokeMethodImpl()
	{
		try
		{
		    method.invoke(obj, param);
		} catch (InvocationTargetException ite)
		{
		    ite.getTargetException().printStackTrace(System.err);
		    System.exit(-1);
		} catch (Exception ex)
		{
		    throw new Error(this.getClass().getName()+" invoke suffered exception " + ex.getClass().getName() + ": " +
		            ex.getMessage());
		}
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //