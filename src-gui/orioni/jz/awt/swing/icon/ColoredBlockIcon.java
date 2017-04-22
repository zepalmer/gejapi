package orioni.jz.awt.swing.icon;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link Icon} implementation displays itself as a colored square of a specified size.
 *
 * @author Zachary Palmer
 */
public class ColoredBlockIcon implements Icon
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The width of this icon.
     */
    protected int width;
    /**
     * The height of this icon.
     */
    protected int height;
    /**
     * The color of this icon.
     */
    protected Color color;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.
     *
     * @param size  The size of this icon.
     * @param color The initial color of this icon.
     */
    public ColoredBlockIcon(Dimension size, Color color)
    {
        this((int) (size.getWidth()), (int) (size.getHeight()), color);
    }

    /**
     * General constructor.
     *
     * @param width  The width of this icon.
     * @param height The height of this icon.
     * @param color  The initial color of this icon.
     */
    public ColoredBlockIcon(int width, int height, Color color)
    {
        super();
        this.width = width;
        this.height = height;
        this.color = color;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight()
    {
        return height;
    }

    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth()
    {
        return width;
    }

    /**
     * Draw the icon at the specified location.  Icon implementations may use the Component argument to get properties
     * useful for painting, e.g. the foreground or background color.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * Changes the color of this icon.  Note that, as the icon is not bound to any parent component, any component
     * using this icon will likely have to be informed that this change has been made.
     * @param color The new color for this icon.
     */
    public void setColor(Color color)
    {
        this.color = color;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE