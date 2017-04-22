package orioni.jz.awt.listener;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.*;

/**
 * This {@link ComponentListener} is designed to dispose of any dialog of frame on which it is attached if that dialog
 * or frame is set invisible.
 *
 * @author Zachary Palmer
 */
public class DisposeOnHideListener extends ComponentAdapter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public DisposeOnHideListener()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invoked when the component has been made invisible.
     */
    public void componentHidden(ComponentEvent e)
    {
        Component c = e.getComponent();
        if (c instanceof Dialog)
        {
            ((Dialog)c).dispose();
        } else if (c instanceof Frame)
        {
            ((Frame)c).dispose();
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE