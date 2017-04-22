package orioni.jz.awt.listener;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

/**
 * This adapter is designed to receive input from the mouse regularly and monitor the mouse position.  Whenever the
 * mouse's position changes, the {@link MousePositionListener#positionChanged(MouseEvent e)} method is called.  The
 * position of the mouse can also be obtained by called {@link MousePositionListener#getPosition()}, which will return
 * a {@link Point} or, if the mouse is not in the desired component, <code>null</code>.
 * <p>
 * Please note that the {@link MousePositionListener#getPosition()} method will always return <code>null</code> until
 * the first time the mouse moves.  There is no AWT utility for polling the position of the mouse; indeed, this listener
 * is designed to accomodate for that deficiency.  As a result, adding this listener to a component when the component
 * already has the mouse may produce unexpected results until the mouse is moved.
 * <p>
 * This listener should <i>not</i> be added to multiple components.  To function properly, this listener must be added
 * to a component as both a {@link MouseListener} and a {@link MouseMotionListener}.
 *
 * @author Zachary Palmer
 */
public abstract class MousePositionListener extends MouseInputAdapter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The current position of the mouse.
     */
    protected Point mousePosition;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public MousePositionListener()
    {
        super();
        mousePosition = null;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the last known position of the mouse, or <code>null</code> if the mouse is not in the component.
     * @return The position of the mouse, or <code>null</code>.
     */
    public Point getPosition()
    {
        return mousePosition;
    }

    /**
     * Called whenever the mouse is moved with a button down.  This method simply pushes its parameters to
     * {@link MousePositionListener#mouseMoved(MouseEvent)}.
     * @param e The {@link MouseEvent} describing the movement.
     */
    public void mouseDragged(MouseEvent e)
    {
        mouseMoved(e);
    }

    /**
     * Tracks movement of the mouse.  This method, as a result of the implementation of
     * {@link MousePositionListener#mouseDragged(MouseEvent)}, is called whenever the mouse is moved, regardless of
     * button status.  It sets the mouse position and notifies the {@link MousePositionListener#positionChanged(MouseEvent)} method.
     * @param e The {@link MouseEvent} describing the movement.
     */
    public void mouseMoved(MouseEvent e)
    {
        mousePosition = e.getPoint();
        positionChanged(e);
    }

    /**
     * Indicates that the mouse has been removed from the component.  This method can be overridden by subclasses, but
     * <code>super.mouseExited(MouseEvent)</code> must be called to ensure proper behavior of {@link MousePositionListener}.
     * @param e The {@link MouseEvent} describing the movement.
     */
    public void mouseExited(MouseEvent e)
    {
        mousePosition = null;
    }

    /**
     * This method is called whenever the position of the mouse changes <i>inside</i> of the listener.  This method will
     * not be called when the mouse is moved out of the component.
     * @param e The {@link MouseEvent} describing the movement.
     */
    public abstract void positionChanged(MouseEvent e);

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE