package orioni.jz.awt;

import java.awt.*;

/**
 * This {@link LayoutManager} is designed to position a single component in the parent container.  The component in
 * question is the first child of the parent container; all other children are ignored.  The child component will be set
 * to its preferred size regardless of parent size.  Its position will be dependent upon the alignment parameters
 * specified upon construction.
 *
 * @author Zachary Palmer
 */
public class SingleComponentPositioningLayout implements LayoutManager
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The X alignment for the child component.
     */
    protected double alignmentX;
    /**
     * The Y alignment for the child component .
     */
    protected double alignmentY;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes both alignments are centered: <code>0.5</code>.
     */
    public SingleComponentPositioningLayout()
    {
        this(0.5, 0.5);
    }

    /**
     * General constructor.
     *
     * @param alignmentX The X alignment for the child component.  This is expressed in terms of a value between
     *                    <code>0.0</code> and <code>1.0</code> and specifies the horizontal position of the component
     *                    within its parent container.
     * @param alignmentY The Y alignment for the child component.  This variable is treated in the same manner as
     *                    <code>x_alignment</code> but is used to establish vertical position.
     */
    public SingleComponentPositioningLayout(double alignmentX, double alignmentY)
    {
        super();
        this.alignmentX = alignmentX;
        this.alignmentY = alignmentY;
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
     * Does nothing.
     *
     * @param comp Ignored.
     */
    public void removeLayoutComponent(Component comp)
    {
    }

    /**
     * Returns the preferred size of the component at index zero.  This will be the layout size of the container.
     *
     * @param parent The target layout container.
     * @return The preferred layout size for the container.
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        if (parent.getComponentCount() > 0)
        {
            return new Dimension(
                    (int) (parent.getComponent(0).getPreferredSize().getWidth() + parent.getInsets().left +
                           parent.getInsets().right),
                    (int) (parent.getComponent(0).getPreferredSize().getHeight() + parent.getInsets().top +
                           parent.getInsets().bottom));
        } else
        {
            return new Dimension(0, 0);
        }
    }

    /**
     * Returns the preferred size of the component at index zero.  This will be the layout size of the container.
     *
     * @param parent The target layout container.
     * @return The minimum layout size for the container.
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        return (parent.getComponentCount() > 0 ? parent.getComponent(0).getPreferredSize() : new Dimension(0, 0));
    }

    /**
     * Lays out the container in the specified panel.
     *
     * @param parent the component which needs to be laid out
     */
    public void layoutContainer(Container parent)
    {
        if (parent.getComponentCount() == 0) return;
        Component component = parent.getComponent(0);
        component.setSize(component.getPreferredSize());
        component.setLocation(
                (int) ((parent.getSize().getWidth() - parent.getInsets().left - parent.getInsets().right -
                        component.getSize().getWidth()) * alignmentX) + parent.getInsets().left,
                (int) ((parent.getSize().getHeight() - parent.getInsets().top - parent.getInsets().bottom -
                        component.getSize().getHeight()) * alignmentY) + parent.getInsets().top);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //