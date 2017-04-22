package orioni.jz.awt.swing.convenience;

import javax.swing.*;

/**
 * This {@link ButtonGroup} implementation adds buttons from its constructor.
 *
 * @author Zachary Palmer
 */
public class ConstructorButtonGroup extends ButtonGroup
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param buttons The {@link AbstractButton}s to add.
     */
    public ConstructorButtonGroup(AbstractButton... buttons)
    {
        super();
        for (AbstractButton button : buttons) this.add(button);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE