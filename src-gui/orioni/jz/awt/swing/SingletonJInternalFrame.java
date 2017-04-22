package orioni.jz.awt.swing;

import orioni.jz.util.Pair;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * This class is designed to represent a {@link JInternalFrame} which is activated by multiple sources.  Each source
 * wishes to bring focus to the frame, creating it if necessary.  The {@link  SingletonJInternalFrame} class uses a
 * method, {@link SingletonJInternalFrame#getKey()}, which returns a key object with which the frame can be stored in a
 * hash.  Upon a call to the static method {@link SingletonJInternalFrame}, the frame can be recalled from the hash.  If
 * the frame does not exist in the hash, it is created using a constructor which accepts the key and the target {@link
 * JDesktopPane}.  Note that construction information can be passed through the key if it does not affect the behavior
 * of the key's <code>.equals(Object)</code> method.
 * <p/>
 * Due to the nature of this hashing system, it is not advisable to use {@link SingletonJInternalFrame} objects in more
 * than one {@link JDesktopPane} in the same Java VM. Functionality for multiple desktops may be implemented in a later
 * revision.
 *
 * @author Zachary Palmer
 */
public abstract class SingletonJInternalFrame extends JInternalFrame
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

    /**
     * A static cache of all existing and instantiated {@link SingletonJInternalFrame} objects, keyed by the key values
     * that were passed to them during construction.
     */
    private static final HashMap<Object, Pair<JDesktopPane, SingletonJInternalFrame>> singletonCache =
            new HashMap<Object, Pair<JDesktopPane, SingletonJInternalFrame>>();

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.  Prevents the void constructor from being called.
     */
    private SingletonJInternalFrame()
    {
        super();
    }

    /**
     * General constructor.  Extenders of this constructor are encouraged to consider attaching a window listener on
     * internal frame which will call the {@link SingletonJInternalFrame#deactivate(SingletonJInternalFrame)} method
     * with it as the parameter; this will dispose of the frame when it is closed and free up the resources it is using,
     * which is generally desirable unless the frame needs to maintain some state.
     *
     * @param key     The key object which is used to uniquely identify and construct this singleton frame.
     * @param desktop The {@link JDesktopPane} on which this frame will reside.
     */
    protected SingletonJInternalFrame(Object key, JDesktopPane desktop)
    {
        this();
        synchronized (singletonCache)
        {
            singletonCache.put(key, new Pair<JDesktopPane, SingletonJInternalFrame>(this.getDesktopPane(), this));
        }
        this.setVisible(true);
        desktop.add(this);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method provides a unique key object which describes this frame.  The data in the key needs to be such that
     * the key provided by this method and the key passed to this frame upon construction are equal by the key class's
     * <code>.equals(Object)</code> method.
     *
     * @return A key object which represents this frame.
     */
    public abstract Object getKey();

    /**
     * Calls the static deactivate method with this frame.  Provided for convenience.
     */
    public void deactivate()
    {
        deactivate(this);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Activates a specific {@link SingletonJInternalFrame}, as described by the key object provided.  If the frame does
     * not exist, it will be instantiated.
     *
     * @param cl      The {@link Class} of the {@link SingletonJInternalFrame} to instantiate.
     * @param key     The key to be used when instantiating the frame.
     * @param desktop The {@link JDesktopPane} in which this {@link SingletonJInternalFrame} will reside.
     * @throws ClassCastException        If the {@link Class} provided is not an extension of {@link
     *                                   SingletonJInternalFrame}.
     * @throws NoSuchMethodException     If the {@link Class} provided does not have a constructor which accepts the
     *                                   appropriate parameters <code>(Object, JDesktopPane)</code>.
     * @throws InstantiationException    If the {@link Class} provided is abstract.
     * @throws IllegalAccessException    If the appropriate constructor does not provide public access.
     * @throws InvocationTargetException If the appropriate constructor throws an exception.
     */
    public static void activate(Class cl, Object key, JDesktopPane desktop)
            throws ClassCastException, NoSuchMethodException, InstantiationException, IllegalAccessException,
                   InvocationTargetException
    {
        SingletonJInternalFrame singletonFrame;
        synchronized (singletonCache)
        {
            singletonFrame = singletonCache.get(key).getSecond();
        }
        if (singletonFrame == null)
        {
            Constructor constructor = cl.getConstructor(
                    Object.class,
                    JDesktopPane.class);
            singletonFrame = (SingletonJInternalFrame) (constructor.newInstance(key, desktop));
            // The frame is registered with the static frame hash and made visible by its constructor.
        }
        desktop.getDesktopManager().deiconifyFrame(singletonFrame);
        singletonFrame.toFront();
        singletonFrame.requestFocus();
        try
        {
            singletonFrame.setSelected(true);
        } catch (PropertyVetoException e)
        {
            // Not much to do here...
        }
    }

    /**
     * Deactivates a {@link SingletonJInternalFrame} by the key by which it is stored in the singleton frame cache. This
     * key will be the key that would be used to generate the frame using the {@link
     * SingletonJInternalFrame#activate(java.lang.Class, java.lang.Object, javax.swing.JDesktopPane)} method.
     *
     * @param key The key which would be used to create the frame.
     * @return <code>true</code> if the frame was successfully deactivated; <code>false</code> if it did not exist to be
     *         deactivated.
     */
    public static boolean deactivateByKey(Object key)
    {
        SingletonJInternalFrame frame;
        synchronized (singletonCache)
        {
            frame = singletonCache.get(key).getSecond();
        }
        if (frame == null)
        {
            return false;
        } else
        {
            deactivate(frame);
            return true;
        }
    }

    /**
     * Deactivates a specific {@link SingletonJInternalFrame}.  This method removes the frame from the desktop and from
     * the singleton frame hash.  It is called by the window listener which is attached by the {@link
     * SingletonJInternalFrame#SingletonJInternalFrame(java.lang.Object, javax.swing.JDesktopPane) constructor when the
     * singleton frame is closed.
     *
     * @param singleton_frame The {@link SingletonJInternalFrame} to deactivate.
     */
    public static void deactivate(SingletonJInternalFrame singletonFrame)
    {
        JDesktopPane desktop = singletonFrame.getDesktopPane();
        if (desktop != null)
        {
            desktop.getDesktopManager().closeFrame(singletonFrame);
        }
        singletonCache.remove(singletonFrame.getKey());
        singletonFrame.dispose();
    }
}