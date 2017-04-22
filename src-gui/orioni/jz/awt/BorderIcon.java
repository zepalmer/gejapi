package orioni.jz.awt;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link Icon} accepts another {@link Icon} as a parameter and presents that {@link Icon} with a colored border
 * around it.
 *
 * @author Zachary Palmer
 */
public class BorderIcon implements Icon
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Icon} to border.
     */
    protected Icon icon;
    /**
     * The {@link Color} of the border.
     */
    protected Color color;
    /**
     * The width of the border on <i>each side</i>.
     */
    protected int width;
    /**
     * The height of the border on <i>each side</i>.
     */
    protected int height;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  The same border size is used on all sides.
     *
     * @param icon  The {@link Icon} to border.
     * @param color The color of the border.
     * @param size  The width of the border on <i>each side</i>.
     */
    public BorderIcon(Icon icon, Color color, int size)
    {
        this(icon, color, size, size);
    }

    /**
     * General constructor.
     *
     * @param icon   The {@link Icon} to border.
     * @param color  The color of the border.
     * @param width  The width of the border on <i>each side</i>.
     * @param height The height of the border on <i>each side</i>.
     */
    public BorderIcon(Icon icon, Color color, int width, int height)
    {
        super();
        this.icon = icon;
        this.color = color;
        this.width = width;
        this.height = height;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight()
    {
        return icon.getIconHeight() + height * 2;
    }

    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth()
    {
        return icon.getIconWidth() + width * 2;
    }

    /**
     * Draw the icon at the specified location.  Icon implementations may use the Component argument to get properties
     * useful for painting, e.g. the foreground or background color.
     * @param c The component on which to paint.
     * @param g The {@link Graphics} object on which to paint.
     * @param x The X-coordinate at which to paint.
     * @param y The Y-coordinate at which to paint.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        Color color = g.getColor();
        g.setColor(this.color);
        g.fillRect(x, y, getIconWidth(), getIconHeight());
        g.setColor(color);
        icon.paintIcon(c, g, x + width, y + height);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}