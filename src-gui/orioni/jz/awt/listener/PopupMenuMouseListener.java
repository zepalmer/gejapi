package orioni.jz.awt.listener;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * This listener pops up the menu passed to it on construction when a popup event occurs.
 * @author Zachary Palmer
 */
public abstract class PopupMenuMouseListener extends MouseInputAdapter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The last MouseEvent received by this listener which invoked the popup menu, or <code>null</code> if no such
     *  event has yet occurred. */
    protected MouseEvent lastEvent;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public PopupMenuMouseListener()
    {
        super();
        lastEvent = null;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not the popup menu has been called.  Also determines if a Component has earned focus.
     */
    public void mousePressed(MouseEvent e)
    {
        // Check for popup
        testPopup(e);
    }

    /**
     * Determines whether or not the popup menu has been called.
     */
    public void mouseReleased(MouseEvent e)
    {
        // Check for popup
        testPopup(e);
    }

    /**
     * Retrieves the last MouseEvent that invoked the popup menu.
     * @return The last MouseEvent that invoked the popup menu, or <code>null</code> if no such event has yet
     *         occurred.
     */
    public MouseEvent getLastMousePopupEvent()
    {
        return lastEvent;
    }

    /**
     * Retrieves the popup menu which will be displayed when a popup event occurs.
     * @param e The {@link MouseEvent} which prompted the request for the popup menu.
     * @return The popup menu to display.  If no popup menu should be displayed, <code>null</code> is returned.
     */
    public abstract JPopupMenu getPopupMenu(MouseEvent e);

    /**
     * Generates the popup menu, if appropriate.
     */
    protected void testPopup(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            lastEvent = e;
            JPopupMenu popupMenu = getPopupMenu(e);
            if (popupMenu !=null) popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //