package orioni.jz.awt.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This listener is executes a method (provided on construction) whenever a specific key (provided on construction) is
 * pressed.
 *
 * @author Zachary Palmer
 */
public class ExecuteMethodKeyPressedListener extends ExecuteMethodListener implements KeyListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The keycode on which to execute this listener's method.
     */
    protected int keycode;
    /**
     * The modifier flags to use to screen execution of this listener's method.
     */
    protected int modifiers;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * This constructor creates a listener which will execute the non-static method with the given definition when it is
     * notified that a key is pressed.  It also assumes that the method will be run on the AWT listener thread.
     *
     * @param obj       The Object on which to execute the method.
     * @param method    The name of the method to execute.
     * @param param     The parameters with which to execute the method.
     * @param keycode   The keycode on which to execute the method.
     * @param modifiers The modifier flags which must appear to execute the method.
     * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[])
     */
    public ExecuteMethodKeyPressedListener(Object obj, String method, Object[] param, int keycode, int modifiers)
    {
        super(obj, method, param);
        this.keycode = keycode;
        this.modifiers = modifiers;
    }

    /**
     * This constructor creates a listener which will execute the static method with the given description when it is
     * notified that the given key is pressed.  It also assumes that the method will be run on the AWT listener thread.
     *
     * @param cl        The class whose static method will be executed.
     * @param method    The name of the method to execute.
     * @param param     The parameters with which to execute the method.
     * @param keycode   The keycode on which to execute the method.
     * @param modifiers The modifier flags which must appear to execute the method.
     * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[])
     */
    public ExecuteMethodKeyPressedListener(Class cl, String method, Object[] param, int keycode, int modifiers)
    {
        super(cl, method, param);
        this.keycode = keycode;
        this.modifiers = modifiers;
    }

    /**
     * This constructor creates a listener which will execute the non-static method with the given definition when it is
     * notified that a key is pressed.
     *
     * @param obj        The Object on which to execute the method.
     * @param method     The name of the method to execute.
     * @param param      The parameters with which to execute the method.
     * @param keycode    The keycode on which to execute the method.
     * @param modifiers  The modifier flags which must appear to execute the method.
     * @param newThread Whether or not the method should be invoked on a different thread.
     * @see ExecuteMethodListener#ExecuteMethodListener(Object, String, Object[])
     */
    public ExecuteMethodKeyPressedListener(Object obj, String method, Object[] param, int keycode, int modifiers,
                                           boolean newThread)
    {
        super(obj, method, param, newThread);
        this.keycode = keycode;
        this.modifiers = modifiers;
    }

    /**
     * This constructor creates a listener which will execute the static method with the given description when it is
     * notified that the given key is pressed.
     *
     * @param cl         The class whose static method will be executed.
     * @param method     The name of the method to execute.
     * @param param      The parameters with which to execute the method.
     * @param keycode    The keycode on which to execute the method.
     * @param modifiers  The modifier flags which must appear to execute the method.
     * @param newThread Whether or not the method should be invoked on a different thread.
     * @see ExecuteMethodListener#ExecuteMethodListener(Class, String, Object[])
     */
    public ExecuteMethodKeyPressedListener(Class cl, String method, Object[] param, int keycode, int modifiers,
                                           boolean newThread)
    {
        super(cl, method, param, newThread);
        this.keycode = keycode;
        this.modifiers = modifiers;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executes the method for this listener if the keycode of the key pressed is identical to the keycode provided on
     * construction.
     */
    public void keyPressed(KeyEvent e)
    {
        if ((e.getKeyCode() == keycode) && (e.getModifiers() == modifiers))
        {
            super.invokeMethod();
        }
    }

    /**
     * Does nothing.
     */
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * Does nothing.
     */
    public void keyReleased(KeyEvent e)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //