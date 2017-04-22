package orioni.jz.awt;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class represents an icon which is drawn by a series of calls to the {@link Graphics} object on which it is
 * painted.  This eliminates the need for raster images for certain simple icons.
 *
 * @author Zachary Palmer
 */
public class VectorImageIcon implements Icon
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The enumeration describing the different types of icons which this vector image icon can produce.  These types
     * are as follows: <ul> <li>{@link IconType#PIECE_OF_PAPER_DOG_EARED}: Displays a dog-eared piece of paper.
     * Recommended aspect ratio: 2/3.</li> <li>{@link IconType#PIECE_OF_PAPER_DOG_EARED_WITH_WRITING}: Displays a
     * dog-eared piece of paper with horizontal lines to represent writing.  Recommended aspect ratio: 2/3.</li>
     * <li>{@link IconType#PIECE_OF_PAPER_WITH_WRITING}: Displays a piece of paper with horizontal lines to represent
     * writing.  Recommended aspect ratio: 2/3.</li> <li>{@link IconType#FOLDER}: Displays a file folder.   Recommended
     * aspect ratio: 5/4.</li> </ul>
     */
    public static enum IconType
    {
        PIECE_OF_PAPER_DOG_EARED, PIECE_OF_PAPER_DOG_EARED_WITH_WRITING,
        PIECE_OF_PAPER_WITH_WRITING,
        FOLDER
    }

    /**
     * The default outline {@link Color}s for the various types of {@link VectorImageIcon}s.  This array is indexed by
     * the ordinal value of the {@link IconType}.
     */
    public static final Color[] DEFAULT_COLOR_OUTLINE = new Color[]{
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
    /**
     * The default fill {@link Color}s for the various types of {@link VectorImageIcon}s.  This array is indexed by the
     * ordinal value of the {@link IconType}.
     */
    public static final Color[] DEFAULT_COLOR_FILL = new Color[]{
            Color.WHITE, Color.WHITE, Color.WHITE, new Color(64, 64, 255)};

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link BufferedImage} object which contains the rendering information for this icon.
     */
    protected BufferedImage image;
    /**
     * The {@link Color} to be used for the outline of this {@link VectorImageIcon}.
     */
    protected Color outline;
    /**
     * The {@link Color} to be used for the fill of this {@link VectorImageIcon}.
     */
    protected Color fill;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Uses default colors for the icon.
     *
     * @param type   The type of icon to create.
     * @param width  The width of the icon
     * @param height The height of the icon.
     */
    public VectorImageIcon(IconType type, int width, int height)
    {
        this(type, width, height, DEFAULT_COLOR_OUTLINE[type.ordinal()], DEFAULT_COLOR_FILL[type.ordinal()]);
    }

    /**
     * General constructor.
     *
     * @param type    The type of icon to create.
     * @param width   The width of the icon
     * @param height  The height of the icon.
     * @param outline The {@link Color} to be used for the outline of this {@link VectorImageIcon}.
     * @param fill    The {@link Color} to be used for the fill of this {@link VectorImageIcon}.
     */
    public VectorImageIcon(IconType type, int width, int height, Color outline, Color fill)
    {
        super();

        final int w11 = width - 1;
        final int w12 = width / 2 - 1;
        final int w13 = width / 3 - 1;
        final int w23 = width * 2 / 3 - 1;
        final int w14 = width / 4 - 1;
        final int w34 = width * 3 / 4 - 1;
        final int w15 = width / 5 - 1;
        final int w25 = width * 2 / 5 - 1;
        final int w35 = width * 3 / 5 - 1;
        final int w45 = width * 4 / 5 - 1;

        final int h11 = height - 1;
        final int h12 = height / 2 - 1;
        final int h13 = height / 3 - 1;
        final int h23 = height * 2 / 3 - 1;
        final int h14 = height / 4 - 1;
        final int h34 = height * 3 / 4 - 1;
        final int h15 = height / 5 - 1;
        final int h25 = height * 2 / 5 - 1;
        final int h35 = height * 3 / 5 - 1;
        final int h45 = height * 4 / 5 - 1;

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = this.image.getGraphics();

        switch (type)
        {
            case PIECE_OF_PAPER_DOG_EARED:
                g.setColor(fill);
                g.fillPolygon(
                        new int[]{w34, 0, 0, w11, w11},
                        new int[]{0, 0, h11, h11, h14},
                        5);
                g.setColor(outline);
                g.drawPolygon(
                        new int[]{w34, w34, w11, w34, 0, 0, w11, w11},
                        new int[]{0, h14, h14, 0, 0, h11, h11, h14},
                        8);
                break;
            case PIECE_OF_PAPER_DOG_EARED_WITH_WRITING:
                // Draw paper
                g.setColor(fill);
                g.fillPolygon(
                        new int[]{w34, 0, 0, w11, w11},
                        new int[]{0, 0, h11, h11, h14},
                        5);
                g.setColor(outline);
                g.drawPolygon(
                        new int[]{w34, w34, w11, w34, 0, 0, w11, w11},
                        new int[]{0, h14, h14, 0, 0, h11, h11, h14},
                        8);
                // Draw lines
                for (int i = 3; i < height - 3; i += 2)
                {
                    if (i <= height / 4)
                    {
                        g.drawLine(2, i, width * 3 / 4 - 3, i);
                    } else
                    {
                        g.drawLine(2, i, width - 3, i);
                    }
                }
                break;
            case PIECE_OF_PAPER_WITH_WRITING:
                // Draw paper
                g.setColor(fill);
                g.fillPolygon(
                        new int[]{0, 0, w11, w11},
                        new int[]{0, h11, h11, 0},
                        4);
                g.setColor(outline);
                g.drawPolygon(
                        new int[]{0, 0, w11, w11},
                        new int[]{0, h11, h11, 0},
                        4);
                // Draw lines
                for (int i = 3; i < height - 3; i += 2)
                {
                    if ((i % 10 == 3) && (i < height - 5))
                    {
                        g.drawLine(w14 + 2, i, width - 3, i);
                    } else if ((i % 10 == 1) && (i < height - 7))
                    {
                        g.drawLine(2, i, w34 - 3, i);
                    } else
                    {
                        g.drawLine(2, i, width - 3, i);
                    }
                }
                break;
            case FOLDER:
                int[] xDarker = new int[]{0, w13, w12, w34, w34, w13, 0, 0};
                int[] yDarker = new int[]{0, 0, h14, h14, h25, h25, h11, 0};
                int[] xLighter = new int[]{w34, w11, w23, 0, w13};
                int[] yLighter = new int[]{h25, h25, h11, h11, h25};
                g.setColor(fill.darker());
                g.fillPolygon(xDarker, yDarker, xDarker.length);
                g.setColor(fill);
                g.fillPolygon(xLighter, yLighter, xLighter.length);
                g.setColor(outline);
                g.drawPolygon(xDarker, yDarker, xDarker.length);
                g.drawPolygon(xLighter, yLighter, xLighter.length);
                break;
            default:
                throw new IllegalArgumentException("Invalid Icon Type: " + type);
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight()
    {
        return this.image.getHeight();
    }

    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth()
    {
        return this.image.getWidth();
    }

    /**
     * Draw the icon at the specified location.
     *
     * @param c Ignored.
     * @param g The {@link Graphics} object on which to draw the image.
     * @param x The X-coordinate at which to draw.
     * @param y The Y-coordinate at which to draw.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.drawImage(this.image, x, y, null);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}