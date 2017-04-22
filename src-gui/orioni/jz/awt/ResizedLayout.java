package orioni.jz.awt;

import java.awt.*;

/**
 * This LayoutManager is designed to resize the Components in the Container without changing their positions.  Thus, the
 * "minimum" and "preferred" sizes are the same.  The Components are queried for their preferred sizes and the size is
 * adjusted to accommodate all Components in the Container.
 * @author Zachary Palmer
 */
public class ResizedLayout implements LayoutManager
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The border allowance for this ResizedLayout. */
    protected int border;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a border allowance of five pixels.
     */
    public ResizedLayout()
    {
        this(5);
    }

    /**
     * General constructor.
     * @param border The number of pixels by which to increase the preferred size past the requirement.
     */
    public ResizedLayout(int border)
    {
        this.border = border;
    }

// NON-STATIC METHODS : LAYOUT MANAGER IMPLEMENTATION ////////////////////////////

    /**
     * Adds the specified component with the specified name to the layout.
     * @param name the component name
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp)
    {
    }

    /**
     * Removes the specified component from the layout.
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp)
    {
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @return The preferred layout size for the container.
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        Component[] components = parent.getComponents();
        Insets insets = parent.getInsets();
        int prefX = 0;
        int prefY = 0;
        for (final Component component : components)
        {
            Dimension compPrefSize = component.getPreferredSize();
            int compPrefWidth = (int) (compPrefSize.getWidth());
            int compPrefHeight = (int) (compPrefSize.getHeight());
            if (component.getX() + compPrefWidth > prefX)
            {
                prefX = component.getX() + compPrefWidth;
            }
            if (component.getY() + compPrefHeight > prefY)
            {
                prefY = component.getY() + compPrefHeight;
            }
        }
        prefX += insets.left + insets.right + border;
        prefY += insets.top + insets.bottom + border;
        return new Dimension(prefX,prefY);
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @return The minimum layout size for the container.
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        return preferredLayoutSize(parent);
    }

    /**
     * Lays out the container in the specified panel.
     * @param parent the component which needs to be laid out
     */
    public void layoutContainer(Container parent)
    {
        Component[] components = parent.getComponents();
        for (final Component component : components)
        {
            component.setSize(component.getPreferredSize());
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //