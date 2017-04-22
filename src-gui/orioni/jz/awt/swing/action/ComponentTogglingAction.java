package orioni.jz.awt.swing.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This {@link Action} will invert a {@link JComponent}'s enabled state whenever it is invoked.
 *
 * @author Zachary Palmer
 */
public class ComponentTogglingAction extends AbstractAction
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link JComponent} to toggle. */
    protected JComponent component;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param component The {@link JComponent} to toggle.
     */
    public ComponentTogglingAction(JComponent component)
    {
        super();
        this.component = component;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        component.setEnabled(!component.isEnabled());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE