package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This component emulates the display capabilities of a normal system-dependent text console with color.  The size of
 * the screen is adjustable, but the component itself has a minimum, maximum, and preferred size based upon the number
 * of characters in the component.
 * <p/>
 * Upon construction, a <code>TextConsoleDisplayComponent</code> calls its <code>loadBitmap</code> method to generate a
 * boolean bitmap of the character set it will be using.  It uses this bitmap to generate <code>BufferedImage</code>
 * objects which represent the characters in its bitmap font.  The size of the cache is configurable to a specific
 * number of <code>BufferedImage</code> objects.  Note that the <code>BufferedImage</code> object for a given character
 * X in color Y is different from the <code>BufferedImage</code> for character X in color Z.
 * <p/>
 * Generated <code>BufferedImage</code> objects are cached in the system until they become stale or until the
 * <code>TextConsoleDisplayComponent</code> is destroyed.  An image cached in the system will become stale when more
 * space is needed for images and it has not been used in the longest period of time.
 *
 * @author Zachary Palmer
 */
public class TextConsoleDisplayComponent extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A constant representing the tab length (in cells) of one tab character.
     */
    public static final int CONSOLE_TAB_LENGTH = 8;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * This <code>BufferedImage</code> object contains the buffer for the image to be painted.  This prevents excessive
     * calculation during rendering.
     */
    protected BufferedImage paintBuffer;
    /**
     * The {@link Graphics2D} of the paint buffer.
     */
    protected Graphics2D paintBufferGraphics;

    /**
     * The width of the text console.
     */
    protected int consoleWidth;
    /**
     * The height of the text console.
     */
    protected int consoleHeight;
    /**
     * The preferred width of the text console.
     */
    protected int consolePreferredWidth;
    /**
     * The preferred height of the text console.
     */
    protected int consolePreferredHeight;
    /**
     * The cells for the character grid.
     */
    protected Cell[] cells;

    /**
     * The X coordinate of the console cursor.
     */
    protected int cursorX;
    /**
     * The Y coordinate of the console cursor.
     */
    protected int cursorY;
    /**
     * The top line of the cursor, inclusive.  This value represents the first row on which the cursor will appear.
     */
    protected int cursorTopRow;
    /**
     * The bottom line of the cursor, inclusive.  This value represents the last row on which the cursor will appear.
     */
    protected int cursorBottomRow;
    /**
     * The foreground color that the cursor is currently writing.
     */
    protected int cursorForeground;
    /**
     * The background color that the cursor is currently writing.
     */
    protected int cursorBackground;

    /**
     * The {@link TextConsoleFont} for this console.
     */
    protected TextConsoleFont font;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a cache size of 256 character images and the default extended ASCII character set
     * bitmap.
     */
    public TextConsoleDisplayComponent()
    {
        this(new BitmappedTextConsoleFont());
    }

    /**
     * General constructor.
     *
     * @param font The {@link TextConsoleFont} to use in displaying the cells of this component.
     * @throws IllegalArgumentException If the size of the <code>bitmap</code> parameter is not 3,584 bytes or if the
     *                                  <code>cache_size</code> parameter is negative.
     */
    public TextConsoleDisplayComponent(TextConsoleFont font)
            throws IllegalArgumentException
    {
        this.font = font;

        consoleWidth = 80;
        consoleHeight = 25;
        consolePreferredWidth = 80;
        consolePreferredHeight = 25;

        cursorX = 0;
        cursorY = 0;
        cursorTopRow = this.font.getTopCursorLine();
        cursorBottomRow = this.font.getBottomCursorLine();
        setConsoleSize(80, 25);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the width of this console in cells.
     */
    public synchronized int getConsoleWidth()
    {
        return consoleWidth;
    }

    /**
     * Retrieves the height of this console in cells.
     */
    public synchronized int getConsoleHeight()
    {
        return consoleHeight;
    }

    /**
     * Changes the size of this console in cells.
     *
     * @param width  The new width of this console in cells.
     * @param height The new height of this console in cells.
     * @throws IllegalArgumentException If either the provided width or height is less than one.
     */
    public synchronized void setConsoleSize(int width, int height)
    {
        if (width < 1)
        {
            throw new IllegalArgumentException("Illegal console width " + width + ": must be at least one.");
        }
        if (height < 1)
        {
            throw new IllegalArgumentException("Illegal console height " + height + ": must be at least one.");
        }
        int oldWidth = consoleWidth;
        int oldHeight = consoleHeight;
        BufferedImage oldBuffer = paintBuffer;
        Cell[] oldCells = cells;

        consoleWidth = width;
        consoleHeight = height;
        paintBuffer = new BufferedImage(
                getConsoleWidth() * getConsoleFont().getCellWidth(),
                getConsoleHeight() * getConsoleFont().getCellHeight(),
                BufferedImage.TYPE_INT_RGB);
        paintBufferGraphics = paintBuffer.createGraphics();
        cells = new Cell[consoleWidth * consoleHeight];

        if (oldBuffer != null) paintBuffer.createGraphics().drawImage(oldBuffer, 0, 0, null);
        if (oldCells != null)
        {
            for (int y = 0; y < Math.min(oldHeight, consoleHeight); y++)
            {
                System.arraycopy(
                        oldCells, y * oldWidth, cells, y * consoleWidth, Math.min(oldWidth, consoleWidth));
                for (int i = oldWidth; i < consoleWidth; i++) cells[y * consoleWidth + i] = new Cell();
            }
            for (int i = oldCells.length; i < cells.length; i++) cells[i] = new Cell();
        } else
        {
            for (int i = 0; i < cells.length; i++) cells[i] = new Cell();
        }
    }

    /**
     * Sets the preferred size of this console in cells.
     *
     * @param width  The preferred width in cells of this component.
     * @param height The preferred height in cells of this component.
     */
    public void setPreferredConsoleSize(int width, int height)
    {
        consolePreferredWidth = width;
        consolePreferredHeight = height;
    }

    /**
     * Retrieves the width in pixes of one cell.
     */
    public synchronized int getCellWidth()
    {
        return font.getCellWidth();
    }

    /**
     * Retrieves the height in pixels of one cell.
     */
    public synchronized int getCellHeight()
    {
        return font.getCellHeight();
    }

    /**
     * Retrieves the console font being used on this console.
     */
    public TextConsoleFont getConsoleFont()
    {
        return font;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves an <code>Image</code> object which represents the given character specifications (character and color).
     * If this <code>Image</code> is in cache, it is retrieved from there.  If the image is not in cache, it is
     * generated and placed in cache before it is returned.  This method will cause the generation, removal, or
     * reordering of objects in cache before it finishes execution.
     * <p/>
     * This method can be called by outside users of the <code>TextConsoleDisplayComponent</code> to push objects into
     * the cache.
     *
     * @param character  The index of the character desired, between <code>0</code> and <code>255</code>.
     * @param foreground The foreground color of the character desired, between <code>0</code> and <code>15</code>.
     * @param background The background color of the character desired, between <code>0</code> and <code>15</code>.
     * @return An <code>Image</code> object graphically representing the character requested.
     */
    public synchronized Image getCharacterImage(int character, byte foreground, byte background)
    {
        BufferedImage image =
                new BufferedImage(font.getCellWidth(), font.getCellHeight(), BufferedImage.TYPE_INT_RGB);
        font.renderCell((char) character, foreground, background, image.createGraphics(), 0, 0);
        return image;
    }

    /**
     * Sets a specific character in the console and blits the change immediately.
     *
     * @param x   The X coordinate of the character to set.
     * @param y   The Y coordinate of the character to set.
     * @param chr The index of the character to use (<code>0</code> through <code>255</code>).
     * @param fg  The foreground color to use (<code>0</code> through <code>15</code>).
     * @param bg  The background color to use (<code>0</code> through <code>15</code>).
     */
    public synchronized void setCharacter(int x, int y, int chr, int fg, int bg)
    {
        setCharacter(x, y, chr, fg, bg, true);
    }

    /**
     * Sets a specific character in the console.
     *
     * @param x       The X coordinate of the character to set.
     * @param y       The Y coordinate of the character to set.
     * @param chr     The index of the character to use (<code>0</code> through <code>255</code>).
     * @param fg      The foreground color to use (<code>0</code> through <code>15</code>).
     * @param bg      The background color to use (<code>0</code> through <code>15</code>).
     * @param repaint <code>true</code> to initiate a repaint request immediately after the change is stored;
     *                <code>false</code> if not.  Usually, <code>false</code> indicates a case in which multiple
     *                characters are being updated and a full repaint will follow afterward.
     */
    public synchronized void setCharacter(int x, int y, int chr, int fg, int bg, boolean repaint)
    {
        if ((x >= getConsoleWidth()) || (x < 0) || (y >= getConsoleHeight()) || (y < 0)) return;
        cells[x + y * getConsoleWidth()].setCell(chr, fg, bg);
        blitCell(x, y, repaint);
    }

    /**
     * Draws the specified cell onto the buffer and, optionally, requests a repaint of that cell.
     *
     * @param x       The X coordinate of the cell to blit.
     * @param y       The Y coordinate of the cell to blit.
     * @param repaint <code>true</code> if a repaint request should be made for that cell; <code>false</code> otherwise.
     *                This may be helpful in the event that a number of cells need to be blitted simultaneously.
     */
    protected void blitCell(int x, int y, boolean repaint)
    {
        int gx = x * getCellWidth();
        int gy = y * getCellHeight();
        Cell c = cells[x + y * getConsoleWidth()];
        font.renderCell(
                (char) c.getCharacter(), c.getForeground(), c.getBackground(), paintBufferGraphics, gx, gy);
        if (repaint) repaint(gx, gy, getCellWidth(), getCellHeight());
    }

    /**
     * This method retrieves the foreground color of a character on the console.
     *
     * @param x The zero-based column of the character for which foreground color is being requested.
     * @param y The zero-based row of the character for which foreground color is being requested.
     * @return The foreground color of the specified character.
     */
    public synchronized int getForegroundColor(int x, int y)
    {
        return cells[x + y * getConsoleWidth()].getForeground();
    }

    /**
     * This method retrieves the background color of a character on the console.
     *
     * @param x The zero-based column of the character for which background color is being requested.
     * @param y The zero-based row of the character for which background color is being requested.
     * @return The background color of the specified character.
     */
    public synchronized int getBackgroundColor(int x, int y)
    {
        return cells[x + y * getConsoleWidth()].getBackground();
    }

    /**
     * This method retrieves the specified character on the console.
     *
     * @param x The zero-based column of the character.
     * @param y The zero-based row of the character.
     * @return The specified character.
     */
    public synchronized int getCharacter(int x, int y)
    {
        return cells[x + y * getConsoleWidth()].getCharacter();
    }

    /**
     * Adjusts the cursor as specified by the parameters.  Assumes that the cursor's style is to remain the same.
     *
     * @param x The new X coordinate for the cursor.
     * @param y The new Y coordinate for the cursor.
     */
    public synchronized void setCursor(int x, int y)
    {
        this.setCursor(x, y, cursorTopRow, cursorBottomRow);
    }

    /**
     * Adjusts the cursor as specified by the provided parameters.  If the value for <code>cursor_top</code> is greater
     * than the value for <code>cursor_bottom</code>, the cursor will not appear.
     *
     * @param x             The new X coordinate for the cursor.
     * @param y             The new Y coordinate for the cursor.
     * @param cursorTop    The top pixel row on which the cursor should appear.  This value must be within <code>[0,
     *                      cellHeight-1)</code>.
     * @param cursorBottom The bottom pixel row on which the cursor should appear.  This value must be within <code>[0,
     *                      cellHeight-1)</code>.
     * @throws IllegalArgumentException If <code>cursor_top</code> or <code>cursor_bottom</code> is out of range.
     */
    public synchronized void setCursor(int x, int y, int cursorTop, int cursorBottom)
    {
        if ((cursorTop < 0) || (cursorTop >= getCellHeight()))
        {
            throw new IllegalArgumentException(
                    "cursor_top==" + cursorTop + " is outside of range [0," + (getCellHeight() - 1) + ")");
        }
        if ((cursorBottom < 0) || (cursorBottom >= getCellHeight()))
        {
            throw new IllegalArgumentException(
                    "cursor_top==" + cursorTop + " is outside of range [0," + (getCellHeight() - 1) + ")");
        }
        cursorX = x;
        cursorY = y;
        cursorTopRow = cursorTop;
        cursorBottomRow = cursorBottom;
    }

    /**
     * Sets the current colors for the cursor.  The new color settings will affect new characters written with methods
     * such as <code>BlitString</code>.
     *
     * @param fg The foreground color for the cursor.
     * @param bg The background color for the cursor.
     */
    public synchronized void setCursorColors(int fg, int bg)
    {
        cursorForeground = fg;
        cursorBackground = bg;
    }

    /**
     * Prints the given string onto the screen at the current cursor position in the current color.  If the cursor moves
     * beyond the last cell in the text screen, the text will be scrolled upward one line; the text at the top of the
     * screen will be lost. <P> Newlines in these strings will be interpreted appropriately.  A newly scrolled line is
     * padded with the current text color.
     *
     * @param string The string to blit to the screen at the cursor position.
     */
    public synchronized void consolePrint(String string)
    {
        this.consolePrint(string, true);
    }

    /**
     * Prints the given string onto the screen at the current cursor position in the current color.  If the
     * <code>scroll</code> parameter is true and the cursor moves beyond the last cell in the text screen, the text will
     * be scrolled upward one line; the text at the top of the screen will be lost. <P> Newlines in these strings will
     * be interpreted appropriately.  A newly scrolled line is padded with the current text color.
     *
     * @param string The string to blit to the screen at the cursor position.
     * @param scroll Whether or not to scroll if the cursor reaches the bottom of the screen.
     */
    public synchronized void consolePrint(String string, boolean scroll)
    {
        // First, blit the string.
        blitString(cursorX, cursorY, string, cursorForeground, cursorBackground, scroll);
        // Next, move the cursor.
        char[] chs = string.toCharArray();
        for (final char ch : chs)
        {
            switch (ch)
            {
                case '\n':
                    cursorX = 0;
                    cursorY++;
                    break;
                case '\t':
                    do
                    {
                        cursorX++;
                    } while (cursorX % CONSOLE_TAB_LENGTH != 0);
                    break;
                default:
                    cursorX++;
            }
            if (cursorX >= getConsoleWidth())
            {
                cursorX = 0;
                cursorY++;
            }
            if ((scroll) && (cursorY >= getConsoleHeight()))
            {
                cursorY = getConsoleHeight() - 1;
            }
        }
    }

    /**
     * Prints the given string onto the screen at the current cursor position in the current color and advances the
     * cursor to a new line.  If the cursor moves beyond the last cell in the text screen, the text will be scrolled
     * upward one line; the text at the top of the screen will be lost. <P> Newlines in these strings will be
     * interpreted appropriately.  A newly scrolled line is padded with the current text color.
     *
     * @param string The string to blit to the screen at the cursor position.
     */
    public synchronized void consolePrintln(String string)
    {
        this.consolePrintln(string, true);
    }

    /**
     * Prints the given string onto the screen at the current cursor position in the current color and advances the
     * cursor to a new line.  If the <code>scroll</code> parameter is true and the cursor moves beyond the last cell in
     * the text screen, the text will be scrolled upward one line; the text at the top of the screen will be lost. <P>
     * Newlines in these strings will be interpreted appropriately.  A newly scrolled line is padded with the current
     * text color.
     *
     * @param string The string to blit to the screen at the cursor position.
     * @param scroll Whether or not to scroll if the cursor reaches the bottom of the screen.
     */
    public synchronized void consolePrintln(String string, boolean scroll)
    {
        // First, print the string.
        consolePrint(string, scroll);
        // Next, advance the cursor.
        cursorX = 0;
        cursorY++;
        if (scroll)
        {
            while (cursorY >= getConsoleHeight())
            {
                scrollUpOneLine(cursorForeground, cursorBackground);
                cursorY--;
            }
        }
    }

    /**
     * Blits the given string onto the screen at the specified position in the current color.  If the
     * <code>scroll</code> parameter is trueand the cursor moves beyond the last cell in the text screen, the text will
     * be scrolled upward one line; the text at the top of the screen will be lost. <P> Newlines in these strings will
     * be interpreted appropriately.  A newly scrolled line is padded with the specified text color.
     *
     * @param x      The zero-based X-coordinate at which the string should be blitted.
     * @param y      The zero-based Y-coordinate at which the string should be blitten.
     * @param string The string to blit to the screen at the specified position and in the specified color.
     * @param fg     The foreground color in which to blit the string.
     * @param bg     The background color in which to blit the string.
     * @param scroll Whether or not to scroll the screen upwards in the event that the string trails off of the edge of
     *               the screen.
     */
    public synchronized void blitString(int x, int y, String string, int fg, int bg, boolean scroll)
    {
        byte[] stringData = string.getBytes();
        for (final byte data : stringData)
        {
            switch (data)
            {
                case '\n':
                    x = 0;
                    y++;
                    break;
                case '\t':
                    do
                    {
                        setCharacter(x, y, ' ', fg, bg);
                    } while (x % CONSOLE_TAB_LENGTH != 0);
                    break;
                default:
                    setCharacter(x, y, data, fg, bg);
                    x++;
            }

            // Consider scrolling one line.
            if (x >= getConsoleWidth())
            {
                x = 0;
                y++;
            }
            if (scroll)
            {
                while (y >= getConsoleHeight())
                {
                    scrollUpOneLine(fg, bg);
                    y--;
                }
            }
        }
    }

    /**
     * Scrolls the text up one line.  The top line of text is lost.  The bottom line is padded with the provided
     * foreground and background, with spaces.
     *
     * @param fg The foreground color with which the bottom line will be padded.
     * @param bg The background color with which the bottom line will be padded.
     */
    public synchronized void scrollUpOneLine(int fg, int bg)
    {
        for (int j = 0; j < (getConsoleHeight() - 1) * getConsoleWidth(); j++)
        {
            Cell source = cells[j + getConsoleWidth()];
            setCharacter(
                    j % getConsoleWidth(), j / getConsoleWidth(), source.getCharacter(),
                    source.getForeground(), source.getBackground());
        }
        for (int k = (getConsoleHeight() - 1) * getConsoleWidth();
             k < getConsoleHeight() * getConsoleWidth(); k++)
        {
            setCharacter(
                    k % getConsoleWidth(), k / getConsoleWidth(), 0,
                    fg, bg);
            cells[k].setCell(0, fg, bg);
        }
    }

    /**
     * Retrieves the cell X-coordinate of the provided {@link Point} on this component.  This method can be used to
     * partially translate pixel-based coordiantes to cell-based coordinates.
     *
     * @param point The {@link Point} object to examine.
     * @return The X coordinate of the provided {@link Point} in terms of cell-based coordinates.
     */
    public synchronized int getCellX(Point point)
    {
        return getCellX(point.getX());
    }

    /**
     * Converts the provided pixel X-coordinate to a cell X-coordinate.  This method can be used to partially translate
     * pixel-based coordiantes to cell-based coordinates.
     *
     * @param pixelx The pixel-based X-coordinate to translate.
     * @return The cell-based X-coordinate for that pixel-based X-coordinate.  Note that this may produce a value which
     *         is out of the bounds of the console itself if the coordinate is not within the buffer.
     */
    public synchronized int getCellX(double pixelx)
    {
        return (int) ((pixelx - getBufferXOffset()) / getCellWidth());
    }

    /**
     * Retrieves the cell Y-coordinate of the provided {@link Point} on this component.  This method can be used to
     * partially translate pixel-based coordiantes to cell-based coordinates.
     *
     * @param point The {@link Point} object to examine.
     * @return The Y coordinate of the provided {@link Point} in terms of cell-based coordinates.
     */
    public synchronized int getCellY(Point point)
    {
        return getCellY(point.getY());
    }

    /**
     * Converts the provided pixel Y-coordinate to a cell Y-coordinate.  This method can be used to partially translate
     * pixel-based coordiantes to cell-based coordinates.
     *
     * @param pixely The pixel-based Y-coordinate to translate.
     * @return The cell-based Y-coordinate for that pixel-based Y-coordinate.  Note that this may produce a value which
     *         is out of the bounds of the console itself if the coordinate is not within the buffer.
     */
    public synchronized int getCellY(double pixely)
    {
        return (int) ((pixely - getBufferYOffset()) / getCellHeight());
    }

    /**
     * Determines whether or not the provided {@link Point} is within the display of the console.
     *
     * @param point The {@link Point} in question.
     * @return <code>true</code> if the point is within the display of the console; <code>false</code> if it is outside
     *         of the display.
     */
    public synchronized boolean isPointWithinDisplay(Point point)
    {
        int cellx = getCellX(point);
        int celly = getCellY(point);
        return ((cellx >= 0) && (cellx < getConsoleWidth()) && (celly >= 0) && (celly < getConsoleHeight()));
    }

    /**
     * Retrieves the pixel-based X offset of the image from the upper left corner of the component.
     *
     * @return The pixel-based X offset of the image from the upper left corner of the component.
     */
    public int getBufferXOffset()
    {
        return (getWidth() - paintBuffer.getWidth()) / 2;
    }

    /**
     * Retrieves the piYel-based Y offset of the image from the upper left corner of the component.
     *
     * @return The piYel-based Y offset of the image from the upper left corner of the component.
     */
    public int getBufferYOffset()
    {
        return (getHeight() - paintBuffer.getHeight()) / 2;
    }

    /**
     * Retrieves the upper left corner of the specified cell as a {@link Point} object.
     *
     * @param x The cell's X coordinate.
     * @param y The cell's Y coordinate.
     * @return The {@link Point} on this component representing the upper left corner of that cell.
     */
    public Point getCellOrigin(int x, int y)
    {
        return new Point(getBufferXOffset() + getCellWidth() * x, getBufferYOffset() + getCellHeight() * y);
    }

    /**
     * Retrieves the center point of the specified cell as a {@link Point} object.  If there are multiple centermost
     * points, the upper-left center point is returned.
     *
     * @param x The cell's X coordinate.
     * @param y The cell's Y coordinate.
     * @return The {@link Point} on this component representing the upper left corner of that cell.
     */
    public Point getCellCenter(int x, int y)
    {
        return new Point(
                getBufferXOffset() + getCellWidth() * x + getCellWidth() / 2,
                getBufferYOffset() + getCellHeight() * y + getCellHeight() / 2);
    }

// AWT/SWING OVERRIDE FUNCTIONS //////////////////////////////////////////////////

    /**
     * Returns the necessary size of this console component.
     */
    public synchronized Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    /**
     * Returns the necessary size of this console component.
     */
    public synchronized Dimension getPreferredSize()
    {
        return new Dimension(
                consolePreferredWidth * getCellWidth(),
                consolePreferredHeight * getCellHeight());
    }

    /**
     * Returns the necessary size of this console component.
     */
    public synchronized Dimension getMaximumSize()
    {
        return getPreferredSize();
    }

    /**
     * Draws this text console in its current state.
     *
     * @param g The <code>Graphics</code> object onto which to draw this console.
     */
    public synchronized void paint(Graphics g)
    {
        int offsetX = getBufferXOffset();
        int offsetY = getBufferYOffset();
        g.drawImage(paintBuffer, offsetX, offsetY, null);
        if (cursorTopRow <= cursorBottomRow)
        {
            g.setColor(
                    new Color(
                            TextConsoleFont.CONSOLE_COLOR_MODEL.getRGB(
                                    cells[cursorX + cursorY * getConsoleWidth()].getForeground())));
            g.fillRect(
                    offsetX + cursorX * getCellWidth(), offsetY + cursorY * getCellHeight() + cursorTopRow,
                    getCellWidth(), cursorBottomRow - cursorTopRow + 1);
        }
    }

// NON-STATIC PROTECTED METHODS //////////////////////////////////////////////////

    /**
     * Retrieves an <code>Image</code> object which represents the given Cell.  If this <code>Image</code> is in cache,
     * it is retrieved from there.  If the image is not in cache, it is generated and placed in cache before it is
     * returned.  This method will cause the generation, removal, or reordering of objects in cache before it finishes
     * execution.
     *
     * @param cell The <code>Cell</code> for which an <code>Image</code> should be returned.
     * @return An <code>Image</code> object graphically representing the <code>Cell</code> provided.
     */
    protected synchronized Image getCharacterImage(Cell cell)
    {
        return getCharacterImage(cell.getCharacter(), (byte) cell.getForeground(), (byte) cell.getBackground());
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
        return TextConsoleFont.getRGBForIndex(index);
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * An instance of this class is a data structure which represents a single cell on the text screen.  This class is a
     * simple data structure class and, because it is very frequently used, is finalized for optimization purposes.
     */
    final class Cell
    {
        /**
         * The character contents of this cell.
         */
        protected int character;
        /**
         * The foreground color of this cell.
         */
        protected int foreground;
        /**
         * The background color of this cell.
         */
        protected int background;
        /**
         * The property key for this cell.
         */
        protected int key;

        /**
         * Skeleton constructor.  Initializes the cell to have a character, foreground, and background of zero.
         */
        public Cell()
        {
            this(0, 0, 0);
        }

        /**
         * General constructor.
         *
         * @param chr The index of the character to display (between <code>0</code> and <code>255</code>).
         * @param fg  The index of the foreground color to display (between <code>0</code> and <code>15</code>).
         * @param bg  The index of the background color to display (between <code>0</code> and <code>15</code>).
         */
        public Cell(int chr, int fg, int bg)
        {
            character = chr;
            foreground = fg;
            background = bg;
            recalculateKey();
        }

        /**
         * Retrieves the character contents for this cell.
         */
        public final int getCharacter()
        {
            return character;
        }

        /**
         * Sets the character contents for this cell.
         */
        public final void setCharacter(int character)
        {
            this.character = character;
            recalculateKey();
        }

        /**
         * Retrieves the foreground color for this cell.
         */
        public final int getForeground()
        {
            return foreground;
        }

        /**
         * Sets the foreground color for this cell.
         */
        public final void setForeground(int foreground)
        {
            this.foreground = foreground;
            recalculateKey();
        }

        /**
         * Retrieves the background color for this cell.
         */
        public final int getBackground()
        {
            return background;
        }

        /**
         * Sets the background color for this cell.
         */
        public final void setBackground(int background)
        {
            this.background = background;
            recalculateKey();
        }

        /**
         * Sets the properties of this cell.
         *
         * @param character  The new character contents of this cell.
         * @param foreground The new foreground color of this cell.
         * @param background The new background color of this cell.
         */
        public final void setCell(int character, int foreground, int background)
        {
            this.character = character;
            this.foreground = foreground;
            this.background = background;
            recalculateKey();
        }

        /**
         * Retrieves a key for the <code>Cell</code> which uniquely identifies its properties.
         */
        public final int cellKey()
        {
            return key;
        }

        /**
         * Causes this cell to recalculate its key.
         */
        public final void recalculateKey()
        {
            key = character + foreground * 256 + background * 4096;
        }
    }

}

// END OF FILE //