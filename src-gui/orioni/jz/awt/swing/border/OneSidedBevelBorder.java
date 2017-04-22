package orioni.jz.awt.swing.border;

import orioni.jz.awt.AWTUtilities;

import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * This class uses the functionality from the {@link BevelBorder} class, but only borders the specified component on one
 * side.  This is usually used in combination with a {@link java.awt.BorderLayout} with a component on one of the sides
 * so as to separate that component from the center component.
 *
 * @author Zachary Palmer
 */
public class OneSidedBevelBorder extends BevelBorder
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The constant indicating beveling for the top side.
     */
    public static final int SIDE_TOP = 0;
    /**
     * The constant indicating beveling for the left side.
     */
    public static final int SIDE_LEFT = 1;
    /**
     * The constant indicating beveling for the right side.
     */
    public static final int SIDE_RIGHT = 2;
    /**
     * The constant indicating beveling for the bottom side.
     */
    public static final int SIDE_BOTTOM = 3;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The side of the border which will be beveled.  This will be one of the <code>SIDE_XXXX</code> constants on this
     * class.
     */
    protected int side;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Constructs a {@link OneSidedBevelBorder} using the background colors of the target component for color
     * information.
     *
     * @param bevelType The type of beveling to perform.  See {@link BevelBorder} for more information.
     * @param side       The side on which beveling will occur.  This should be one of the <code>SIDE_XXXX</code>
     *                   constants on this class.
     */
    public OneSidedBevelBorder(int bevelType, int side)
    {
        super(bevelType);
        initialize(side);
    }

    /**
     * Constructs a {@link OneSidedBevelBorder} using the background color information provided in this constructor.
     *
     * @param bevelType      The type of beveling to perform.  See {@link BevelBorder} for more information.
     * @param highlightColor The highlighting color for beveling.
     * @param shadowColor    The shadowing color for beveling.
     * @param side            The side on which beveling will occur.  This should be one of the <code>SIDE_XXXX</code>
     *                        constants on this class.
     */
    public OneSidedBevelBorder(int bevelType, Color highlightColor, Color shadowColor, int side)
    {
        super(bevelType, highlightColor, shadowColor);
        initialize(side);
    }

    /**
     * Constructs a {@link OneSidedBevelBorder} using the background color information provided in this constructor.
     *
     * @param bevelType            The type of beveling to perform.  See {@link BevelBorder} for more information.
     * @param highlightColorOuter The outer highlighting color for beveling.
     * @param shadowColorOuter    The outer shadowing color for beveling.
     * @param highlightColorInner The inner highlighting color for beveling.
     * @param shadowColorInner    The inner shadowing color for beveling.
     * @param side                  The side on which beveling will occur.  This should be one of the
     *                              <code>SIDE_XXXX</code> constants on this class.
     */
    public OneSidedBevelBorder(int bevelType, Color highlightColorOuter, Color shadowColorOuter,
                               Color highlightColorInner, Color shadowColorInner, int side)
    {
        super(bevelType, highlightColorOuter, highlightColorInner, shadowColorOuter, shadowColorInner);
        initialize(side);
    }

    /**
     * The private initializer for this class, called by all constructors.
     *
     * @param side The side on which beveling will occur.  This should be one of the <code>SIDE_XXXX</code> constants on
     *             this class.
     */
    private void initialize(int side)
    {
        this.side = side;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the side on which beveling will occur.
     *
     * @return The side on which beveling will occur, as a <code>SIDE_XXXX</code> constant on this class.
     */
    public int getSide()
    {
        return side;
    }

    /**
     * Sets the side on which beveling will occur.
     *
     * @param side The new side on which beveling will occur, as a <code>SIDE_XXXX</code> constant on this class.
     */
    public void setSide(int side)
    {
        this.side = side;
    }

    /**
     * Returns the insets of the border.
     *
     * @param c The component for which this border insets value applies.
     * @return The requested insets.
     */
    public Insets getBorderInsets(Component c)
    {
        return new Insets(
                side == SIDE_TOP ? 2 : 0,
                side == SIDE_LEFT ? 2 : 0,
                side == SIDE_BOTTOM ? 2 : 0,
                side == SIDE_RIGHT ? 2 : 0);
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     *
     * @param c      the component for which this border insets value applies
     * @param insets the object to be reinitialized
     * @return The requested insets.
     */
    public Insets getBorderInsets(Component c, Insets insets)
    {
        insets.top = (side == SIDE_TOP ? 2 : 0);
        insets.left = (side == SIDE_LEFT ? 2 : 0);
        insets.right = (side == SIDE_RIGHT ? 2 : 0);
        insets.bottom = (side == SIDE_BOTTOM ? 2 : 0);
        return insets;
    }

    /**
     * Returns the inner highlight color of the bevel border when rendered on the specified component.  If no highlight
     * color was specified at instantiation, the highlight color is derived from the specified component's background
     * color.
     *
     * @param c the component for which the highlight may be derived
     * @return The inner highlight color.
     */
    public Color getHighlightInnerColor(Component c)
    {
        if (getHighlightInnerColor()!=null) return getHighlightInnerColor();
        Color normalHighlightInner = c.getBackground().brighter();
        Color normalHighlightOuter = normalHighlightInner.brighter();
        if (normalHighlightInner.equals(normalHighlightOuter))
        {
            return AWTUtilities.blendColors(c.getBackground(), normalHighlightOuter, 0.5);
        } else
        {
            return normalHighlightInner;
        }
    }

    /**
     * Returns the outer highlight color of the bevel border when rendered on the specified component.  If no highlight
     * color was specified at instantiation, the highlight color is derived from the specified component's background
     * color.
     *
     * @param c the component for which the highlight may be derived
     * @return The outer highlight color.
     */
    public Color getHighlightOuterColor(Component c)
    {
        if (getHighlightOuterColor()!=null) return getHighlightOuterColor();
        return c.getBackground().brighter().brighter();
    }

    /**
     * Returns the inner shadow color of the bevel border when rendered on the specified component.  If no shadow color
     * was specified at instantiation, the shadow color is derived from the specified component's background color.
     *
     * @param c the component for which the shadow may be derived
     * @return The inner shadow color.
     */
    public Color getShadowInnerColor(Component c)
    {
        if (getShadowInnerColor()!=null) return getShadowInnerColor();
        Color normalShadowInner = c.getBackground().darker();
        Color normalShadowOuter = normalShadowInner.darker();
        if (normalShadowInner.equals(normalShadowOuter))
        {
            return AWTUtilities.blendColors(c.getBackground(), normalShadowOuter, 0.5);
        } else
        {
            return normalShadowInner;
        }
    }

    /**
     * Returns the outer shadow color of the bevel border when rendered on the specified component.  If no shadow color
     * was specified at instantiation, the shadow color is derived from the specified component's background color.
     *
     * @param c the component for which the shadow may be derived
     * @return The outer shadow color.
     */
    public Color getShadowOuterColor(Component c)
    {
        if (getShadowOuterColor()!=null) return getShadowOuterColor();
        return c.getBackground().darker().darker();
    }

    /**
     * Overrides the base {@link BevelBorder#paintRaisedBevel(java.awt.Component, java.awt.Graphics, int, int, int,
     * int)} method, only painting the part of the raised bevel which has been specified.
     *
     * @param c      The {@link Component} on which the border is to be drawn.
     * @param g      The {@link Graphics} object on which to draw.
     * @param x      The x-position for the border.
     * @param y      The y-position for the border.
     * @param width  The width of the {@link Graphics} object.
     * @param height The height of the {@link Graphics} object.
     */
    protected void paintRaisedBevel(Component c, Graphics g, int x, int y,
                                    int width, int height)
    {
        Color oldColor = g.getColor();

        g.translate(x, y);

        if (side ==SIDE_TOP)
        {
            g.setColor(getHighlightOuterColor(c));
            g.drawLine(0, 0, width - 1, 0);
            g.setColor(getHighlightInnerColor(c));
            g.drawLine(0, 1, width - 1, 1);
        } else if (side ==SIDE_LEFT)
        {
            g.setColor(getHighlightOuterColor(c));
            g.drawLine(0, 0, 0, height - 1);
            g.setColor(getHighlightInnerColor(c));
            g.drawLine(1, 0, 1, height - 1);
        } else if (side ==SIDE_RIGHT)
        {
            g.setColor(getShadowOuterColor(c));
            g.drawLine(width - 1, 0, width - 1, height - 1);
            g.setColor(getShadowInnerColor(c));
            g.drawLine(width - 2, 0, width - 2, height - 1);
        } else if (side ==SIDE_BOTTOM)
        {
            g.setColor(getShadowOuterColor(c));
            g.drawLine(0, height - 1, width - 1, height - 1);
            g.setColor(getShadowInnerColor(c));
            g.drawLine(0, height - 2, width - 1, height - 2);
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }

    /**
     * Overrides the base {@link BevelBorder#paintLoweredBevel(java.awt.Component, java.awt.Graphics, int, int, int,
     * int)} method, only painting the part of the lowered bevel which has been specified.
     *
     * @param c      The {@link Component} on which the border is to be drawn.
     * @param g      The {@link Graphics} object on which to draw.
     * @param x      The x-position for the border.
     * @param y      The y-position for the border.
     * @param width  The width of the {@link Graphics} object.
     * @param height The height of the {@link Graphics} object.
     */
    protected void paintLoweredBevel(Component c, Graphics g, int x, int y,
                                     int width, int height)
    {
        Color oldColor = g.getColor();

        g.translate(x, y);

        if (side ==SIDE_TOP)
        {
            g.setColor(getShadowOuterColor(c));
            g.drawLine(0, 0, width - 1, 0);
            g.setColor(getShadowInnerColor(c));
            g.drawLine(0, 1, width - 1, 1);
        } else if (side ==SIDE_LEFT)
        {
            g.setColor(getShadowOuterColor(c));
            g.drawLine(0, 0, 0, height - 1);
            g.setColor(getShadowInnerColor(c));
            g.drawLine(1, 0, 1, height - 1);
        } else if (side ==SIDE_RIGHT)
        {
            g.setColor(getHighlightOuterColor(c));
            g.drawLine(width - 1, 0, width - 1, height - 1);
            g.setColor(getHighlightInnerColor(c));
            g.drawLine(width - 2, 0, width - 2, height - 1);
        } else if (side ==SIDE_BOTTOM)
        {
            g.setColor(getHighlightOuterColor(c));
            g.drawLine(0, height - 1, width - 1, height - 1);
            g.setColor(getHighlightInnerColor(c));
            g.drawLine(0, height - 2, width - 1, height - 2);
        }

        g.translate(-x, -y);
        g.setColor(oldColor);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}