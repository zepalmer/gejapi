package orioni.jz.awt.swing.convenience;

import orioni.jz.util.Pair;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This {@link JPanel} extension accepts a default {@link LayoutManager} in its constructor <i>as well as</i> a list of
 * {@link JComponent}s.  When the constructor is called, the layout manager is applied and the components are added.
 * This is intended to eliminate the need for intermediary panel references in constructors of more complicated panels.
 *
 * @author Zachary Palmer
 */
public class ComponentConstructorPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a default layout and no components.
     */
    public ComponentConstructorPanel()
    {
        super();
    }

    /**
     * General constructor.
     *
     * @param manager    The {@link LayoutManager} to use.
     * @param components The {@link JComponent}s to use.
     */
    public ComponentConstructorPanel(LayoutManager manager, JComponent... components)
    {
        super();
        setContents(manager, components);
    }

    /**
     * General constructor.
     *
     * @param manager    The {@link LayoutManager} to use.
     * @param components A group of {@link Pair}<code>&lt;{@link JComponent},{@link Object}&gt;</code> objects
     *                   containing panels to add as well as the constraint objects to apply when they are added.
     */
    public ComponentConstructorPanel(LayoutManager manager, Pair<JComponent, Object>... components)
    {
        super();
        setContents(manager, components);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the border of this {@link ComponentConstructorPanel}.  This {@link ComponentConstructorPanel} is then
     * returned.
     *
     * @param border The border of this panel.
     * @return <code>this</code>, always.
     */
    public ComponentConstructorPanel setBorderAndReturn(Border border)
    {
        this.setBorder(border);
        return this;
    }

    /**
     * Sets the contents of this {@link ComponentConstructorPanel} according to the provided data.
     *
     * @param manager    The {@link LayoutManager} to use.
     * @param components The {@link JComponent}s to use.
     */
    public void setContents(LayoutManager manager, JComponent... components)
    {
        setLayout(manager);
        for (JComponent component : components) this.add(component);
    }

    /**
     * Sets the contents of this {@link ComponentConstructorPanel} according to the provided data.
     *
     * @param manager    The {@link LayoutManager} to use.
     * @param components A group of {@link Pair}<code>&lt;{@link JComponent},{@link Object}&gt;</code> objects
     *                   containing panels to add as well as the constraint objects to apply when they are added.
     */
    public void setContents(LayoutManager manager, Pair<JComponent, Object>... components)
    {
        removeAll();
        setLayout(manager);
        for (Pair<JComponent, Object> component : components)
        {
            this.add(component.getFirst(), component.getSecond());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE