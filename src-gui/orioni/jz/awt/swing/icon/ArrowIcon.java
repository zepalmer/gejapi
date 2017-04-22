package orioni.jz.awt.swing.icon;

import javax.swing.*;
import java.awt.*;

/**
 * This implementation of Icon draws a 3D arrow in the direction, of the color, and of the size specified upon
 * construction.
 * @author Zachary Palmer
 */
public class ArrowIcon implements Icon
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /** The constant indicating that the tip of the arrow should point upward. */
    public static final int DIRECTION_UP = 1050;
    /** The constant indicating that the tip of the arrow should point downward. */
    public static final int DIRECTION_DOWN = 1061;
    /** The constant indicating that the tip of the arrow should point to the left. */
    public static final int DIRECTION_LEFT = 1072;
    /** The constant indicating that the tip of the arrow should point to the right. */
    public static final int DIRECTION_RIGHT = 1083;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The direction of this ArrowIcon. */
    protected int direction;
    /** The Color of the counter-clockwise shading. */
    protected Color counterColor;
    /** The Color of the clockwise shading. */
    protected Color clockColor;
    /** The Color of the border of the arrow. */
    protected Color border;
    /** The Dimension that this arrow occupies. */
    protected Dimension dimension;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param direction One of the four <code>DIRECTION_XXXX</code> constants specified in this class.
     * @param ccw The color shading the arrow in the counter-clockwise direction of the tip.
     * @param cw The color shading the arrow n the clockwise direction of the tip.
     * @param border The color of the border surrounding the arrow.
     * @param dimension The dimension of the arrow.
     * @throws IllegalArgumentException If the dimension is less than 1 in either respect, if the direction is
     *                                  invalid, or if any color is <code>null</code>.
     */
    public ArrowIcon(int direction,Color ccw, Color cw, Color border, Dimension dimension)
    {
        this.direction = direction;
        counterColor = ccw;
        clockColor = cw;
        this.border = border;
        this.dimension = dimension;
        // Check values
        if ((this.direction !=DIRECTION_DOWN) && (this.direction !=DIRECTION_LEFT) && (this.direction !=DIRECTION_RIGHT) &&
            (this.direction !=DIRECTION_UP))
        {
            throw new IllegalArgumentException("Invalid direction value.");
        }
        if (ccw==null)
        {
            throw new IllegalArgumentException("Null counterclockwise color.");
        }
        if (cw==null)
        {
            throw new IllegalArgumentException("Null clockwise color.");
        }
        if (border==null)
        {
            throw new IllegalArgumentException("Null border color.");
        }
        if ((this.dimension.getHeight()<1) || (this.dimension.getWidth()<1))
        {
            throw new IllegalArgumentException("Dimension with one side less than 1.");
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Draw the icon at the specified location.  Icon implementations
     * may use the Component argument to get properties useful for
     * painting, e.g. the foreground or background color.
     * @param c The component on which painting will occur.
     * @param g The {@link Graphics} object on which to paint.
     * @param x The X-coordinate at which to paint.
     * @param y The Y-coordinate at which to paint.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        // Determine arrow height and width as integers
        int height = getIconHeight();
        int width = getIconWidth();
        // Declare significant points
        Point ccwHead = null;
        Point ccwHeadPoint = null;
        Point ccwHeadPit = null;
        Point ccwTail = null;
        Point ccwTailCenter = null;
        Point cwTailCenter = null;
        Point cwTail = null;
        Point cwHeadPit = null;
        Point cwHeadPoint = null;
        Point cwHead = null;
        // Determine these points based on the dimension and direction of the arrow and the coordinates in the graphics
        // plane.
        switch (direction)
        {
            case DIRECTION_DOWN:
                ccwHead = new Point(x+width/2,y+height);
                ccwHeadPoint = new Point(x+width,y+height*2/3);
                ccwHeadPit = new Point(x+width*2/3,y+height*2/3);
                ccwTail = new Point(x+width*2/3,y);
                ccwTailCenter = new Point(x+width/2,y);
                cwTailCenter = new Point(x+width/2-1,y);
                cwTail = new Point(x+width/3,y);
                cwHeadPit = new Point(x+width/3,y+height*2/3);
                cwHeadPoint = new Point(x,y+height*2/3);
                cwHead = new Point(x+width/2-1,y+height);
                break;
            case DIRECTION_LEFT:
                ccwHead = new Point(x,y+height/2);
                ccwHeadPoint = new Point(x+width/3,y+height);
                ccwHeadPit = new Point(x+width/3,y+height*2/3);
                ccwTail = new Point(x+width,y+height*2/3);
                ccwTailCenter = new Point(x+width,y+height/2);
                cwTailCenter = new Point(x+width,y+height/2);
                cwTail = new Point(x+width,y+height/3);
                cwHeadPit = new Point(x+width/3,y+height/3);
                cwHeadPoint = new Point(x+width/3,y);
                cwHead = new Point(x,y+height/2-1);
                break;
            case DIRECTION_RIGHT:
                ccwHead = new Point(x+width,y+height/2);
                ccwHeadPoint = new Point(x+width*2/3,y);
                ccwHeadPit = new Point(x+width*2/3,y+height/3);
                ccwTail = new Point(x,y+height/3);
                ccwTailCenter = new Point(x,y+height/2);
                cwTailCenter = new Point(x,y+height/2);
                cwTail = new Point(x,y+height*2/3);
                cwHeadPit = new Point(x+width*2/3,y+height*2/3);
                cwHeadPoint = new Point(x+width*2/3,y+height);
                cwHead = new Point(x+width,y+height/2-1);
                break;
            case DIRECTION_UP:
                ccwHead = new Point(x+width/2,y);
                ccwHeadPoint = new Point(x,y+height/3);
                ccwHeadPit = new Point(x+width/3,y+height/3);
                ccwTail = new Point(x+width/3,y+height);
                ccwTailCenter = new Point(x+width/2,y+height);
                cwTailCenter = new Point(x+width/2-1,y+height);
                cwTail = new Point(x+width*2/3,y+height);
                cwHeadPit = new Point(x+width*2/3,y+height/3);
                cwHeadPoint = new Point(x+width,y+height/3);
                cwHead = new Point(x+width/2-1,y);
                break;
        }
        // Set up position matrices
        int[] ccwX = new int[]{(int)(ccwHead.getX()),(int)(ccwHeadPoint.getX()),(int)(ccwHeadPit.getX()),
                          (int)(ccwTail.getX()),(int)(ccwTailCenter.getX())};
        int[] ccwY = new int[]{(int)(ccwHead.getY()),(int)(ccwHeadPoint.getY()),(int)(ccwHeadPit.getY()),
                          (int)(ccwTail.getY()),(int)(ccwTailCenter.getY())};
        int[] cwX = new int[]{(int)(cwHead.getX()),(int)(cwHeadPoint.getX()),(int)(cwHeadPit.getX()),
                          (int)(cwTail.getX()),(int)(cwTailCenter.getX())};
        int[] cwY = new int[]{(int)(cwHead.getY()),(int)(cwHeadPoint.getY()),(int)(cwHeadPit.getY()),
                          (int)(cwTail.getY()),(int)(cwTailCenter.getY())};
        // Draw the polygons of the arrow sides
        if (c.isEnabled())
        {
            g.setColor(counterColor);
        } else
        {
            g.setColor(new Color(
                    (counterColor.getRed()+c.getBackground().getRed())/2,
                    (counterColor.getGreen()+c.getBackground().getGreen())/2,
                    (counterColor.getBlue()+c.getBackground().getBlue())/2));
        }
        g.fillPolygon(ccwX,ccwY,5);
        if (c.isEnabled())
        {
            g.setColor(clockColor);
        } else
        {
            g.setColor(new Color(
                    (clockColor.getRed()+c.getBackground().getRed())/2,
                    (clockColor.getGreen()+c.getBackground().getGreen())/2,
                    (clockColor.getBlue()+c.getBackground().getBlue())/2));
        }
        g.fillPolygon(cwX,cwY,5);
        // Draw the border of the arrow
        if (c.isEnabled())
        {
            g.setColor(border);
        } else
        {
            g.setColor(new Color(
                    (border.getRed()+c.getBackground().getRed())/2,
                    (border.getGreen()+c.getBackground().getGreen())/2,
                    (border.getBlue()+c.getBackground().getBlue())/2));
        }
        g.drawLine(ccwX[0],ccwY[0],ccwX[1],ccwY[1]);
        g.drawLine(ccwX[1],ccwY[1],ccwX[2],ccwY[2]);
        g.drawLine(ccwX[2],ccwY[2],ccwX[3],ccwY[3]);
        g.drawLine(ccwX[3],ccwY[3],cwX[3],cwY[3]);
        g.drawLine(cwX[3],cwY[3],cwX[2],cwY[2]);
        g.drawLine(cwX[2],cwY[2],cwX[1],cwY[1]);
        g.drawLine(cwX[1],cwY[1],cwX[0],cwY[0]);
    }

    /**
     * Returns the icon's width.
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth()
    {
        return (int)(dimension.getWidth());
    }

    /**
     * Returns the icon's height.
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight()
    {
        return (int)(dimension.getHeight());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //