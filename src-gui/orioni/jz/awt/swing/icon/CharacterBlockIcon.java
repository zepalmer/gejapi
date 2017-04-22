package orioni.jz.awt.swing.icon;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link Icon} implementation uses a {@link JLabel} to draw a single character.  The background color of the icon
 * is decided on construction as well as the size of the text (and, therefore, the size of the icon).
 * <p/>
 * The icon is always square.  Its size on any given side is equal to the largest side of the {@link JLabel} being used
 * to draw the icon plus twice the border size.
 *
 * @author Zachary Palmer
 */
public class CharacterBlockIcon implements Icon
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The default font for a {@link CharacterBlockIcon}: Bold Serif at 14 point.
     */
    public static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 14);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link JLabel} used to render the icon.
     */
    protected JLabel label;
    /**
     * The background color for this {@link Icon}.
     */
    protected Color background;
    /**
     * The number of pixels to use as a border for this icon.
     */
    protected int border;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes the default font.  (See {@link CharacterBlockIcon#DEFAULT_FONT}.)  Also assumes black
     * text on a white background and a two pixel border.
     *
     * @param ch The character to render.
     */
    public CharacterBlockIcon(char ch)
    {
        this(ch, DEFAULT_FONT, Color.BLACK, Color.WHITE, 2);
    }

    /**
     * Skeleton constructor.  Assumes the default font.  (See {@link CharacterBlockIcon#DEFAULT_FONT}.)  Also assumes black
     * text on a white background.
     *
     * @param ch     The character to render.
     * @param border The number of pixels around the text itself to paint in each direction.
     */
    public CharacterBlockIcon(char ch, int border)
    {
        this(ch, DEFAULT_FONT, Color.BLACK, Color.WHITE, border);
    }

    /**
     * Skeleton constructor.  Assumes the default font.  (See {@link CharacterBlockIcon#DEFAULT_FONT}.)  Also assumes a two
     * pixel border.
     *
     * @param ch         The character to render.
     * @param foreground The foreground color of the icon.
     * @param background The background color of the icon.
     */
    public CharacterBlockIcon(char ch, Color foreground, Color background)
    {
        this(ch, DEFAULT_FONT, foreground, background, 2);
    }

    /**
     * Skeleton constructor.  Assumes the default font.  (See {@link CharacterBlockIcon#DEFAULT_FONT}.)
     *
     * @param ch         The character to render.
     * @param foreground The foreground color of the icon.
     * @param background The background color of the icon.
     * @param border     The number of pixels around the text itself to paint in each direction.
     */
    public CharacterBlockIcon(char ch, Color foreground, Color background, int border)
    {
        this(ch, DEFAULT_FONT, foreground, background, border);
    }

    /**
     * Skeleton constructor.  Assumes black text on a white background and a two pixel border.
     *
     * @param ch   The character to render.
     * @param font The font in which to render the character.
     */
    public CharacterBlockIcon(char ch, Font font)
    {
        this(ch, font, Color.BLACK, Color.WHITE, 2);
    }

    /**
     * Skeleton constructor.  Assumes black text on a white background.
     *
     * @param ch     The character to render.
     * @param font   The font in which to render the character.
     * @param border The number of pixels around the text itself to paint in each direction.
     */
    public CharacterBlockIcon(char ch, Font font, int border)
    {
        this(ch, font, Color.BLACK, Color.WHITE, border);
    }

    /**
     * Skeleton constructor.  Assumes a two pixel border.
     *
     * @param ch         The character to render.
     * @param font       The font in which to render the character.
     * @param foreground The foreground color of the icon.
     * @param background The background color of the icon.
     */
    public CharacterBlockIcon(char ch, Font font, Color foreground, Color background)
    {
        this(ch, font, foreground, background, 2);
    }

    /**
     * General constructor.
     *
     * @param ch         The character to render.
     * @param font       The font in which to render the character.
     * @param foreground The foreground color of the icon.
     * @param background The background color of the icon.
     * @param border     The number of pixels around the text itself to paint in each direction.
     */
    public CharacterBlockIcon(char ch, Font font, Color foreground, Color background, int border)
    {
        super();
        label = new JLabel(String.valueOf(ch));
        label.setFont(font);
        label.setForeground(foreground);
        label.setOpaque(false);
        label.setSize(label.getPreferredSize());
        this.background = background;
        this.border = border;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the icon's height.
     *
     * @return An <code>int</code> specifying the fixed height of the icon.
     */
    public int getIconHeight()
    {
        return (Math.max(label.getHeight(), label.getWidth()) + 2 * border);
    }

    /**
     * Returns the icon's width.
     *
     * @return An <code>int</code> specifying the fixed width of the icon.
     */
    public int getIconWidth()
    {
        return (Math.max(label.getHeight(), label.getWidth()) + 2 * border);
    }

    /**
     * Draw the icon at the specified location.  Icon implementations may use the Component argument to get properties
     * useful for painting, e.g. the foreground or background color.
     * @param c Ignored.
     * @param g The {@link Graphics} object on which to paint.
     * @param x The X-coordinate at which to paint.
     * @param y The Y-coordinate at which to paint.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.translate(x,y);

        g.setColor(background);
        g.fillRect(0, 0, getIconWidth(), getIconHeight());
        label.setSize(label.getPreferredSize());
        label.setLocation(
                getIconWidth() / 2 - label.getWidth() / 2, getIconHeight() / 2 - label.getHeight() / 2);
        g.translate(label.getX(), label.getY());
        Font temp = g.getFont();
        label.paint(g);
        g.setFont(temp);
        g.translate(-label.getX(), -label.getY());

        g.translate(-x, -y);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}