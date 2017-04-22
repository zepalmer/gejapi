package orioni.jz.awt.swing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

/**
 * This interface is designed for use with a {@link TextConsoleDisplayComponent}.  It defines how a cell in the console
 * is rendered.  This level of abstraction is provided since various types of fonts may be desired for performance or
 * look-and-feel reasons.
 *
 * @author Zachary Palmer
 */
public abstract class TextConsoleFont
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * This <code>IndexColorModel</code> reflects the colors that are currently being used to render the console.
     */
    public static final IndexColorModel CONSOLE_COLOR_MODEL;
    /**
     * This array contains {@link Color} objects representing the color of the console color model at a given index.  It
     * is used to prevent frequent construction of {@link Color} objects.
     */
    private static final Color[] CONSOLE_COLORS;

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

    static
    {
        byte[] reds = new byte[]{0, 0, 0, 0, -86, -86, -86, -86, 85, 85, 85, 85, -1, -1, -1, -1};
        byte[] greens = new byte[]{0, 0, -86, -86, 0, 0, -86, -86, 85, 85, -1, -1, 85, 85, -1, -1};
        byte[] blues = new byte[]{0, -86, 0, -86, 0, -86, 0, -86, 85, -1, 85, -1, 85, -1, 85, -1};
        CONSOLE_COLOR_MODEL = new IndexColorModel(8, 16, reds, greens, blues);
        Color[] c = new Color[reds.length];
        for (int i = 0; i < c.length; i++)
        {
            c[i] = new Color(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
        }
        CONSOLE_COLORS = c;
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines the width of a character cell in this font.  This value must be constant.
     *
     * @return The width of a character cell.
     */
    public abstract int getCellWidth();

    /**
     * Determines the height of a character cell in this font.  This value must be constant.
     *
     * @return The height of a character cell.
     */
    public abstract int getCellHeight();

    /**
     * Determines the recommended top line of the cursor in this font.
     *
     * @return The top line on which the cursor should be drawn in the cell, inclusive.
     */
    public abstract int getTopCursorLine();

    /**
     * Determines the recommended bottom line of the cursor in this font.
     *
     * @return The bottom line on which the cursor should be drawn in the cell, inclusive.
     */
    public abstract int getBottomCursorLine();

    /**
     * Renders a console cell at the provided position of the given {@link Graphics} object.
     *
     * @param character  The character to be rendered (usually within [0,255] but not necessarily).
     * @param foreground The index of the foreground color to use (within [0,15]).
     * @param background The index of the background color to use (within [0,15]).
     * @param g          The {@link Graphics} object on which to draw the cell.
     * @param x          The X-coordinate at which the upper-left corner of the cell appears.
     * @param y          The Y-coordinate at which the upper-left corner of the cell appears.
     */
    public abstract void renderCell(char character, int foreground, int background, Graphics g, int x, int y);

    /**
     * Creates a {@link BufferedImage} containing the image rendered in a cell.  This method is provided for convenience
     * for those that wish to display a cell as an icon or other such rendered component.  However, the
     * <code>renderCell</code> method should be used if possible.
     */
    public BufferedImage getCharacterImage(char character, int foreground, int background)
    {
        BufferedImage image = new BufferedImage(getCellWidth(), getCellHeight(), BufferedImage.TYPE_INT_RGB);
        renderCell(character, foreground, background, image.getGraphics(), 0, 0);
        return image;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves the color of the specified foreground/background index as an AWT RGB color value.
     *
     * @param index The color index for which to do a lookup on this color value.
     * @return The RGB value which describes the specific color index.
     */
    public static int getRGBForIndex(int index)
    {
        return CONSOLE_COLOR_MODEL.getRGB(index);
    }

    /**
     * Retrieves the color of the specified foreground/background index as an AWT {@link Color} object.
     *
     * @param index The color index for which to do a lookup on this color value.
     * @return The {@link Color} object which describes the specific color index.
     */
    public static Color getColorForIndex(int index)
    {
        return CONSOLE_COLORS[index];
    }
}
