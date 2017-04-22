package orioni.jz.awt.swing.cosm;

import java.awt.*;

/**
 * This {@link BackgroundRenderer} implementation draws the background of the {@link Cosm} in a single color.
 *
 * @author Zachary Palmer
 */
public class SingleColorBackgroundRenderer implements BackgroundRenderer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Color} in which the background of the {@link Cosm} should be drawn.
     */
    protected Color color;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param color The {@link Color} in which the background of the {@link Cosm} should be drawn.
     */
    public SingleColorBackgroundRenderer(Color color)
    {
        super();
        this.color = color;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Renders a background on the provided {@link java.awt.Graphics} object.
     *
     * @param g      The {@link java.awt.Graphics} object on which to render the background.
     * @param x      The X offset of the upper left corner of the {@link Graphics} object.  The background will always
     *               be rendered at position <code>(0,0)</code> of the {@link Graphics} object; this value determines
     *               what position in the {@link Cosm} is rendered at that point.
     * @param y      The Y offset of the upper left corner of the {@link Graphics} object.  The background will always
     *               be rendered at position <code>(0,0)</code> of the {@link Graphics} object; this value determines
     *               what position in the {@link Cosm} is rendered at that point.
     * @param width  The width of the background to render.
     * @param height The height of the background to render.
     */
    public void renderBackground(Graphics g, int x, int y, int width, int height)
    {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
