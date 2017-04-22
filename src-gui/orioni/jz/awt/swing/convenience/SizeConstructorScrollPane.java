package orioni.jz.awt.swing.convenience;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link JScrollPane} implementation provides a means by which the preferred size can be set in the constructor.
 *
 * @author Zachary Palmer
 */
public class SizeConstructorScrollPane extends JScrollPane
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param component      The {@link Component} around which to provide scrolling functions.
     * @param preferredSize The preferred size of this {@link JScrollPane}.
     */
    public SizeConstructorScrollPane(Component component, Dimension preferredSize)
    {
        super(component);
        setPreferredSize(preferredSize);
    }

    /**
     * General constructor.
     *
     * @param component        The {@link Component} around which to provide scrolling functions.
     * @param preferredWidth  The preferred width for this pane.
     * @param preferredHeight The preferred height for this pane.
     */
    public SizeConstructorScrollPane(Component component, int preferredWidth, int preferredHeight)
    {
        super(component);
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE