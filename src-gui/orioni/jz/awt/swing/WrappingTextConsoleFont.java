package orioni.jz.awt.swing;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.HashMap;
import java.util.Map;

/**
 * This {@link TextConsoleFont} implementation uses a {@link Font} object to create and render the character in
 * question.  This process is usually a great deal faster than blitting rendered images of bitmap console fonts.  It
 * does not, however, produce the same appearance as a classic bitmap font.
 *
 * @author Zachary Palmer
 */
public class WrappingTextConsoleFont extends TextConsoleFont
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The {@link FontRenderContext} used to get information about the fonts.
     */
    private static final FontRenderContext RENDER_CONTEXT = new FontRenderContext(null, false, false);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The font which is wrapped by this console font.
     */
    private Font font;
    /**
     * The width of this font, cached for performance purposes.
     */
    private int width;
    /**
     * The height of this font, cached for performance purposes.
     */
    private int height;

    /**
     * A mapping between characters and strings.  This mapping is provided as a cache to prevent frequent calls to
     * {@link String#valueOf(char)}.
     */
    private Map<Character, String> stringCache;
    /**
     * A mapping between characters and their respective ascents.  This mapping is provided as a cache to prevent
     * frequent calls to {@link Font#getLineMetrics(String, FontRenderContext)}.
     */
    private Map<Character, Integer> ascentCache;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public WrappingTextConsoleFont(Font font)
    {
        super();
        this.font = font;
        stringCache = new HashMap<Character, String>();
        ascentCache = new HashMap<Character, Integer>();
        width = (int) (Math.ceil(this.font.getMaxCharBounds(RENDER_CONTEXT).getWidth()));
        height = (int) (Math.ceil(this.font.getMaxCharBounds(RENDER_CONTEXT).getHeight()));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines the height of a character cell in this font.  This value must be constant.
     *
     * @return The height of a character cell.
     */
    public int getCellHeight()
    {
        return height;
    }

    /**
     * Determines the width of a character cell in this font.  This value must be constant.
     *
     * @return The width of a character cell.
     */
    public int getCellWidth()
    {
        return width;
    }

    /**
     * Determines the recommended top line of the cursor in this font.
     *
     * @return The top line on which the cursor should be drawn in the cell, inclusive.
     */
    public int getTopCursorLine()
    {
        return Math.max(0, getCellHeight() - 4);
    }

    /**
     * Determines the recommended bottom line of the cursor in this font.
     *
     * @return The bottom line on which the cursor should be drawn in the cell, inclusive.
     */
    public int getBottomCursorLine()
    {
        return Math.max(0, getCellHeight() - 2);
    }

    /**
     * Retrieves the {@link Font} being wrapped by this object.
     *
     * @return The wrapped {@link Font}.
     */
    public Font getFont()
    {
        return font;
    }

    /**
     * Renders a console cell at the provided position of the given {@link java.awt.Graphics} object.
     *
     * @param character  The character to be rendered (usually within [0,255] but not necessarily).
     * @param foreground The index of the foreground color to use (within [0,15]).
     * @param background The index of the background color to use (within [0,15]).
     * @param g          The {@link java.awt.Graphics} object on which to draw the cell.
     * @param x          The X-coordinate at which the upper-left corner of the cell appears.
     * @param y          The Y-coordinate at which the upper-left corner of the cell appears.
     */
    public void renderCell(char character, int foreground, int background, Graphics g, int x, int y)
    {
        g.setColor(getColorForIndex(background));
        g.fillRect(x, y, getCellWidth(), getCellHeight());
        g.setColor(getColorForIndex(foreground));
        g.setFont(font);

        String s = stringCache.get(character);
        if (s == null)
        {
            s = String.valueOf(character);
            stringCache.put(character, s);
        }

        Integer ascent = ascentCache.get(character);
        if (ascent == null)
        {
            ascent = (int) (font.getLineMetrics(s, RENDER_CONTEXT).getAscent());
            ascentCache.put(character, ascent);
        }

        g.drawString(s, x, y + ascent);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
