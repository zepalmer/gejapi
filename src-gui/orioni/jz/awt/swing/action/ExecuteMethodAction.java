package orioni.jz.awt.swing.action;

import orioni.jz.reflect.ReflectionUtilities;
import orioni.jz.util.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This {@link AbstractAction} implementation calls a method whenever the action occurs.  The following defaults are
 * used if they are not specified in the constructor: <ul> <li><code>name</code>: defaults to an empty string.
 * <li><code>icon</code>: defaults to <code>null</code>. <li><code>param</code>: defaults to {@link
 * Utilities#EMPTY_OBJECT_ARRAY}. <li><code>new_thread</code>: defaults to <code>false</code>.  The AWT event thread is
 * used. </ul>
 *
 * @author Zachary Palmer
 */
public class ExecuteMethodAction extends AbstractAction
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The object on which the method is to be called.
     */
    protected Object obj;
    /**
     * The Method to call on an action.
     */
    protected Method method;
    /**
     * The parameters to pass to the method on call.
     */
    protected Object[] param;
    /**
     * The types of the parameters to pass to the method on call.
     */
    protected Class[] paramTypes;
    /**
     * Whether or not the call needs to be made on a new thread.
     */
    protected boolean newThread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(Object obj, String method)
    {
        this("", null, obj, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(Class cl, String method)
    {
        this("", null, cl, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(Icon icon, Object obj, String method)
    {
        this("", icon, obj, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(Icon icon, Class cl, String method)
    {
        this("", icon, cl, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(String name, Object obj, String method)
    {
        this(name, null, obj, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(String name, Class cl, String method)
    {
        this(name, null, cl, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(String name, Icon icon, Object obj, String method)
    {
        this(name, icon, obj, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     */
    public ExecuteMethodAction(String name, Icon icon, Class cl, String method)
    {
        this(name, icon, cl, method, Utilities.EMPTY_OBJECT_ARRAY, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(Object obj, String method, Object[] param)
    {
        this("", null, obj, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(Class cl, String method, Object[] param)
    {
        this("", null, cl, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(Icon icon, Object obj, String method, Object[] param)
    {
        this("", icon, obj, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(Icon icon, Class cl, String method, Object[] param)
    {
        this("", icon, cl, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(String name, Object obj, String method, Object[] param)
    {
        this(name, null, obj, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(String name, Class cl, String method, Object[] param)
    {
        this(name, null, cl, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param obj    The object on which the method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(String name, Icon icon, Object obj, String method, Object[] param)
    {
        this(name, icon, obj, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name   The name for this {@link Action}.
     * @param icon   The {@link Icon} representing this {@link Action}.
     * @param cl     The class for which a static method is to be called.
     * @param method The method to call on an action.
     * @param param  The parameters to pass to the method on call.
     */
    public ExecuteMethodAction(String name, Icon icon, Class cl, String method, Object[] param)
    {
        this(name, icon, cl, method, param, false);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Object obj, String method, boolean newThread)
    {
        this("", null, obj, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Class cl, String method, boolean newThread)
    {
        this("", null, cl, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Icon icon, Object obj, String method, boolean newThread)
    {
        this("", icon, obj, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Icon icon, Class cl, String method, boolean newThread)
    {
        this("", icon, cl, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name       The name for this {@link Action}.
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Object obj, String method, boolean newThread)
    {
        this(name, null, obj, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name       The name for this {@link Action}.
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Class cl, String method, boolean newThread)
    {
        this(name, null, cl, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name       The name for this {@link Action}.
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Icon icon, Object obj, String method, boolean newThread)
    {
        this(name, icon, obj, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name       The name for this {@link Action}.
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Icon icon, Class cl, String method, boolean newThread)
    {
        this(name, icon, cl, method, Utilities.EMPTY_OBJECT_ARRAY, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Object obj, String method, Object[] param, boolean newThread)
    {
        this("", null, obj, method, param, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Class cl, String method, Object[] param, boolean newThread)
    {
        this("", null, cl, method, param, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Icon icon, Object obj, String method, Object[] param, boolean newThread)
    {
        this("", icon, obj, method, param, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(Icon icon, Class cl, String method, Object[] param, boolean newThread)
    {
        this("", icon, cl, method, param, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name       The name for this {@link Action}.
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Object obj, String method, Object[] param, boolean newThread)
    {
        this(name, null, obj, method, param, newThread);
    }

    /**
     * Skeleton constructor.  See this class's documentation or {@link ExecuteMethodAction#ExecuteMethodAction(String,
            * javax.swing.Icon, Class, String, Object[], boolean)}.
     *
     * @param name       The name for this {@link Action}.
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Class cl, String method, Object[] param, boolean newThread)
    {
        this(name, null, cl, method, param, newThread);
    }

    /**
     * General constructor for non-static methods.  If a static method needs to be called, use the constructor which
     * accepts a class instead of an object.
     *
     * @param name       The name for this {@link Action}.
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param obj        The object on which the method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Icon icon, Object obj, String method, Object[] param, boolean newThread)
    {
        this(name, icon, obj.getClass(), method, param, newThread);
        this.obj = obj;
    }

    /**
     * General constructor for static methods.  If a non-static method needs to be called, use the constructor which
     * accepts an object instead of a class.
     *
     * @param name       The name for this {@link Action}.
     * @param icon       The {@link Icon} representing this {@link Action}.
     * @param cl         The class for which a static method is to be called.
     * @param method     The method to call on an action.
     * @param param      The parameters to pass to the method on call.
     * @param newThread <code>true</code> if the method call is to be made on a new thread; <code>false</code> if the
     *                   call is succinct enough to be called on the AWT thread.
     */
    public ExecuteMethodAction(String name, Icon icon, Class cl, String method, Object[] param, boolean newThread)
    {
        super(name, icon);
        obj = null;
        this.newThread = newThread;
        this.param = param;
        Method[] allMethods = ReflectionUtilities.findMethods(cl, method, param);
        if (allMethods.length > 1)
        {
            throw new Error(
                    this.getClass().getName() +
                    " construction failure: Ambiguous execution method description");
        }
        try
        {
            this.method = allMethods[0];
        } catch (ArrayIndexOutOfBoundsException oob)
        {
            throw new Error(
                    this.getClass().getName() + " construction failure: No execution match from description." +
                    "  Check to ensure that permissions are correct.");
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invokes the method in question.  This invocation will occur on a seperate thread if appropriate.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (newThread)
        {
            new Thread(
                    new Runnable()
                    {
                        public void run()
                        {
                            invokeMethodImpl();
                        }
                    }).start();
        } else
        {
            invokeMethodImpl();
        }
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
            throw new Error(
                    this.getClass().getName() + " invoke suffered exception " + ex.getClass().getName() + ": " +
                    ex.getMessage());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE