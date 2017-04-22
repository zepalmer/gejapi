package orioni.jz.awt;

import java.awt.*;

/**
 * This {@link LayoutManager} is designed to present a parent's first child as if the parent were that child; that child
 * is set to the size of the parent and positioned in the parent's space at (0,0).
 *
 * @author Zachary Palmer
 */
public class SingleComponentEmulationLayout implements LayoutManager
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public SingleComponentEmulationLayout()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Does nothing.
     *
     * @param name Ignored.
     * @param comp Ignored.
     */
    public void addLayoutComponent(String name, Component comp)
    {
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    public void layoutContainer(Container parent)
    {
        if (parent.getComponentCount() > 0)
        {
            Component c = parent.getComponent(0);
            c.setSize(
                    (int) (parent.getSize().getWidth() - parent.getInsets().left - parent.getInsets().right),
                    (int) (parent.getSize().getHeight() - parent.getInsets().top - parent.getInsets().bottom));
            c.setLocation(parent.getInsets().left, parent.getInsets().top);
        }
    }

    /**
     * Calculates the minimum size dimensions for the specified container, given the components it contains.
     *
     * @param parent The component to be laid out
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        Insets insets = parent.getInsets();
        if (parent.getComponentCount() == 0)
        {
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        } else
        {
            Dimension d = parent.getComponent(0).getMinimumSize();
            return new Dimension(
                    (int) (d.getWidth() + insets.left + insets.right),
                    (int) (d.getHeight() + insets.top + insets.bottom));
        }
    }

    /**
     * Calculates the preferred size dimensions for the specified container, given the components it contains.
     *
     * @param parent The container to be laid out
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        Insets insets = parent.getInsets();
        if (parent.getComponentCount() == 0)
        {
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        } else
        {
            Dimension d = parent.getComponent(0).getPreferredSize();
            return new Dimension(
                    (int) (d.getWidth() + insets.left + insets.right),
                    (int) (d.getHeight() + insets.top + insets.bottom));
        }
    }

    /**
     * Does nothing.
     *
     * @param comp Ignored.
     */
    public void removeLayoutComponent(Component comp)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
