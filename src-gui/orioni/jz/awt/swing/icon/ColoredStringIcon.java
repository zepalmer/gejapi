package orioni.jz.awt.swing.icon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This implementation of {@link Icon} displays a colored text string on a colored, opaque background.  The rendering of
 * this colored string is determined by a {@link JLabel} which is maintained within the {@link Icon} object.
 * <p/>
 * Rather than providing an exponentially increasing number of constructors for each feature that this class provides, a
 * single, void constructor is provided.  Attributes are then set through a series of method calls.  To maintain the
 * convenience which is usually provided by being able to construct a new object and pass it immediately to a method,
 * the attribute-setting methods always return the object from which they were called.  Therefore, where one would
 * normally place a call such as: <UL><code>JLabel label = new JLabel(new ColoredStringIcon("H", DEFAULT_FONT,
 * Color.WHITE, Color.BLUE))</code></UL> one would create a similar icon by doing the following: <UL><code>JLabel label
 * = new JLabel(new ColoredStringIcon().setString("H").setFont(DEFAULT_FONT).<br> &nbsp; &nbsp; &nbsp;
 * setForeground(Color.WHITE).setBackground(Color.BLUE))</code></UL> Not only does this prevent the creation of a
 * multitude of constructors, it also clarifies the meaning of each attribute to the reader as it is passed to the
 * icon.
 * <p/>
 * A distinct difference exists between the <code>setXXXX(...)</code> methods and the <code>deriveXXXX(...)</code>
 * methods.  The <code>set</code> calls modify the state of the {@link ColoredStringIcon} on which they are called.  The
 * <code>derive</code> calls create a new {@link ColoredStringIcon} with identical properties with the old icon,
 * excepting the property just derived.
 *
 * @author Zachary Palmer
 */
public class ColoredStringIcon implements Icon, Cloneable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The default font used for {@link ColoredStringIcon}s.
     */
    public static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 14);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The absolute height of this icon, in pixels.  If this value is <code>0</code>, the icon's height will be
     * determined by the preferred height of the {@link JLabel} which is rendering this {@link Icon}.
     */
    protected int absoluteHeight;
    /**
     * The absolute width of this icon, in pixels.  If this value is <code>0</code>, the icon's width will be determined
     * by the preferred width of the {@link JLabel} which is rendering this {@link Icon}.
     */
    protected int absoluteWidth;
    /**
     * The {@link JLabel} being used to represent this {@link Icon}.
     */
    protected JLabel label;
    /**
     * The background color for this {@link Icon}.
     */
    protected Color backgroundColor;
    /**
     * The number of pixels to border this {@link Icon} on the top and bottom.  Note that this will be applied
     * <i>after</i> resizing to accomodate the absolute height of the icon.  Therefore, if the absolute height of the
     * icon is 12 and the horizontal border size is 2, the image rendered by the {@link JLabel} will be resized to
     * <i>8</i> pixels before the border is applied.
     */
    protected int horizontalBorder;
    /**
     * The number of pixels to border this {@link Icon} on the left and right.  Note that this will be applied
     * <i>after</i> resizing to accomodate the absolute width of the icon.  Therefore, if the absolute width of the icon
     * is 12 and the vertical border size is 2, the image rendered by the {@link JLabel} will be resized to <i>8</i>
     * pixels before the border is applied.
     */
    protected int verticalBorder;
    /**
     * Whether or not the image rendered by the {@link JLabel} should be <i>stretched</i> to match the absolute width of
     * this icon.  If <code>true</code>, the image will always fill the size of the icon excepting the horizontal and
     * vertical borders; if <code>false</code>, the image will be compacted to force it to fit within those boundaries,
     * but it will not be made larger to touch them.
     * <p/>
     * If this icon has no absolute size requirements, the value of this field is unimportant.
     */
    protected boolean stretch;
    /**
     * The outline color of this {@link ColoredStringIcon}.
     */
    protected Color outlineColor;
    /**
     * The size of this {@link ColoredStringIcon}'s outline.
     */
    protected int outlineSize;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.  The following defaults will be applied: <table cols="2"> <tr><td>Vertical
     * Border</td><td><center>2</center></td> <tr><td>Horizontal Border</td><td><center>2</center></td> <tr><td>Absolute
     * Width</td><td><center>None <i>(0)</i></center></td> <tr><td>Absolute Height</td><td><center>None
     * <i>(0)</i></center></td> <tr><td>Background Color</td><td><center>{@link Color#WHITE}</center></td>
     * <tr><td>Foreground Color</td><td><center>{@link Color#BLACK}</center></td> <tr><td>Font</td><td><center>Bold
     * Serif, 14 point</center></td> <tr><td>Text</td><td><center>"<code>?</code>"</center></td>
     * <tr><td>Stretch</td><td><center>No <i>(false)</i></center></td> <tr><td>Outline Color</td><td><center>{@link
     * Color#WHITE}</center></td> <tr><td>Outline Size</td><td><center>0</center></td> </table>
     */
    public ColoredStringIcon()
    {
        super();
        verticalBorder = 2;
        horizontalBorder = 2;
        absoluteWidth = 0;
        absoluteHeight = 0;
        backgroundColor = Color.WHITE;
        label = new JLabel("?");
        label.setOpaque(false);
        label.setForeground(Color.BLACK);
        label.setFont(DEFAULT_FONT);
        label.setSize(label.getPreferredSize());
        stretch = false;
        outlineColor = Color.WHITE;
        outlineSize = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the icon's height.
     *
     * @return An <code>int</code> specifying the fixed height of the icon.
     */
    public int getIconHeight()
    {
        return (absoluteHeight == 0 ? label.getHeight() + outlineSize : absoluteHeight);
    }

    /**
     * Returns the icon's width.
     *
     * @return An <code>int</code> specifying the fixed width of the icon.
     */
    public int getIconWidth()
    {
        return (absoluteWidth == 0 ? label.getWidth() + outlineSize : absoluteWidth);
    }

    /**
     * Draw the icon at the specified location.  Icon implementations may use the Component argument to get properties
     * useful for painting, e.g. the foreground or background color.
     *
     * @param c Ignored.
     * @param g The {@link Graphics} object on which to paint.
     * @param x The X-coordinate at which to paint.
     * @param y The Y-coordinate at which to paint.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g = g.create();
        g.translate(x, y);
        g.setColor(outlineColor);
        g.fillRect(0, 0, getIconWidth(), outlineSize);
        g.fillRect(0, 0, outlineSize, getIconHeight());
        g.fillRect(getIconWidth() - outlineSize, 0, outlineSize, getIconHeight());
        g.fillRect(0, getIconHeight() - outlineSize, getIconWidth(), outlineSize);
        g.setColor(backgroundColor);
        g.fillRect(
                outlineSize,
                outlineSize,
                getIconWidth() - outlineSize * 2,
                getIconHeight() - outlineSize * 2);

        BufferedImage image = new BufferedImage(label.getWidth(), label.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setColor(backgroundColor);
        imageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        label.paint(imageGraphics);

        int width = label.getWidth();
        if (absoluteWidth != 0)
        {
            if ((stretch) || (width > absoluteWidth - verticalBorder * 2 - outlineSize * 2))
            {
                width = absoluteWidth - verticalBorder * 2 - outlineSize * 2;
            }
        }
        int height = label.getHeight();
        if (absoluteHeight != 0)
        {
            if ((stretch) || (height > absoluteHeight - horizontalBorder * 2 - outlineSize * 2))
            {
                height = absoluteHeight - horizontalBorder * 2 - outlineSize * 2;
            }
        }
        g.drawImage(image, getIconWidth() / 2 - width / 2, getIconHeight() / 2 - height / 2, width, height, null);
    }

    /**
     * Sets the font of this {@link ColoredStringIcon}.
     *
     * @param font The new font for this {@link ColoredStringIcon}.
     * @return This object.
     */
    public ColoredStringIcon setFont(Font font)
    {
        label.setFont(font);
        label.setSize(label.getPreferredSize());
        return this;
    }

    /**
     * Sets the foreground color for this {@link ColoredStringIcon} (the color in which the text is written).
     *
     * @param foreground The new foreground color for this {@link ColoredStringIcon}.
     * @return This object.
     */
    public ColoredStringIcon setForegroundColor(Color foreground)
    {
        label.setForeground(foreground);
        return this;
    }

    /**
     * Sets the background color for this {@link ColoredStringIcon}.
     *
     * @param background The new background color for this {@link ColoredStringIcon}.
     * @return This object.
     */
    public ColoredStringIcon setBackgroundColor(Color background)
    {
        backgroundColor = background;
        return this;
    }

    /**
     * Sets the size of the horizontal border (the top and bottom borders) in pixels.
     *
     * @param horizontalBorder The new size for the horizontal border.
     * @return This object.
     */
    public ColoredStringIcon setHorizontalBorder(int horizontalBorder)
    {
        this.horizontalBorder = horizontalBorder;
        return this;
    }

    /**
     * Sets the size of the vertical border (the left and right borders) in pixels.
     *
     * @param verticalBorder The new size for the vertical border.
     * @return This object.
     */
    public ColoredStringIcon setVerticalBorder(int verticalBorder)
    {
        this.verticalBorder = verticalBorder;
        return this;
    }

    /**
     * Sets the absolute width of this icon.
     *
     * @param absoluteWidth The new absolute width of this icon, or <code>0</code> to allow this icon to determine its
     *                       own width.
     * @return This object.
     */
    public ColoredStringIcon setAbsoluteWidth(int absoluteWidth)
    {
        this.absoluteWidth = absoluteWidth;
        return this;
    }

    /**
     * Sets the absolute height of this icon.
     *
     * @param absoluteHeight The new absolute height of this icon, or <code>0</code> to allow this icon to determine
     *                        its own height.
     * @return This object.
     */
    public ColoredStringIcon setAbsoluteHeight(int absoluteHeight)
    {
        this.absoluteHeight = absoluteHeight;
        return this;
    }

    /**
     * Sets the text contained within this icon.
     *
     * @param text The new text for this icon.
     * @return This object.
     */
    public ColoredStringIcon setText(String text)
    {
        label.setText(text);
        label.setSize(label.getPreferredSize());
        return this;
    }

    /**
     * Sets the outline color for this icon.
     *
     * @param outlineColor The new outline color for this icon.
     * @return This object.
     */
    public ColoredStringIcon setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
        return this;
    }

    /**
     * Sets the outline size for this icon.
     *
     * @param outlineSize The new outline size for this icon.
     * @return This object.
     */
    public ColoredStringIcon setOutlineSize(int outlineSize)
    {
        this.outlineSize = outlineSize;
        return this;
    }

    /**
     * Retrieves the font of this {@link ColoredStringIcon}.
     *
     * @return The font for this {@link ColoredStringIcon}.
     */
    public Font getFont()
    {
        return label.getFont();
    }

    /**
     * Retrieves the foreground color for this {@link ColoredStringIcon} (the color in which the text is written).
     *
     * @return The foreground color for this {@link ColoredStringIcon}.
     */
    public Color getForegroundColor()
    {
        return label.getForeground();
    }

    /**
     * Retrieves the background color for this {@link ColoredStringIcon}.
     *
     * @return The background color for this {@link ColoredStringIcon}.
     */
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * Retrieves the size of the horizontal border (the top and bottom borders) in pixels.
     *
     * @return The size of the horizontal border.
     */
    public int getHorizontalBorder()
    {
        return horizontalBorder;
    }

    /**
     * Retrieves the size of the vertical border (the left and right borders) in pixels.
     *
     * @return The size for the vertical border.
     */
    public int getVerticalBorder()
    {
        return verticalBorder;
    }

    /**
     * Retrieves the absolute width of this icon.
     *
     * @return The absolute width of this icon, or <code>0</code> to indicate that this icon determines its own width.
     */
    public int getAbsoluteWidth()
    {
        return absoluteWidth;
    }

    /**
     * Retrieves the absolute height of this icon.
     *
     * @return The absolute height of this icon, or <code>0</code> to indicate that this icon determines its own
     *         height.
     */
    public int getAbsoluteHeight()
    {
        return absoluteHeight;
    }

    /**
     * Retrieves the text contained within this icon.
     *
     * @return The text for this icon.
     */
    public String getText()
    {
        return label.getText();
    }

    /**
     * Retrieves the outline color for this icon.
     *
     * @return The outline color for this icon.
     */
    public Color getOutlineColor()
    {
        return outlineColor;
    }

    /**
     * Retrieves the outline size for this icon.
     *
     * @return The outline size for this icon.
     */
    public int getOutlineSize()
    {
        return outlineSize;
    }

    /**
     * Duplicates this object.
     *
     * @return The duplicate {@link Object}.
     */
    public Object clone()
    {
        return new ColoredStringIcon().
                setAbsoluteHeight(getAbsoluteHeight()).
                setAbsoluteWidth(getAbsoluteWidth()).
                setBackgroundColor(getBackgroundColor()).
                setFont(getFont()).
                setForegroundColor(getForegroundColor()).
                setHorizontalBorder(getHorizontalBorder()).
                setText(getText()).
                setVerticalBorder(getVerticalBorder()).
                setOutlineColor(getOutlineColor()).
                setOutlineSize(getOutlineSize());
    }

    /**
     * Duplicates this object.  This method is provided for convenience to allow the object to be returned as a {@link
     * ColoredStringIcon}.
     *
     * @return The duplicate {@link ColoredStringIcon}.
     */
    public ColoredStringIcon duplicate()
    {
        return (ColoredStringIcon) clone();
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the font provided.  All other attributes of the new {@link
     * ColoredStringIcon} are identical to this one.
     *
     * @param font The new font for the new {@link ColoredStringIcon}.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveFont(Font font)
    {
        return duplicate().setFont(font);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the foreground color (the color in which the text is written)
     * provided.  All other attributes of the new {@link ColoredStringIcon} are identical to this one.
     *
     * @param foreground The new foreground color for the new {@link ColoredStringIcon}.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveForegroundColor(Color foreground)
    {
        return duplicate().setForegroundColor(foreground);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the background color provided.  All other attributes of the new
     * {@link ColoredStringIcon} are identical to this one.
     *
     * @param background The new background color for the new {@link ColoredStringIcon}.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveBackgroundColor(Color background)
    {
        return duplicate().setBackgroundColor(background);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the horizontal border (the top and bottom borders) provided.  All
     * other attributes of the new {@link ColoredStringIcon} are identical to this one.
     *
     * @param horizontalBorder The new size for the horizontal border.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveHorizontalBorder(int horizontalBorder)
    {
        return duplicate().setHorizontalBorder(horizontalBorder);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the vertical border (the left and right borders) provided.  All
     * other attributes of the new {@link ColoredStringIcon} are identical to this one.
     *
     * @param verticalBorder The new size for the vertical border.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveVerticalBorder(int verticalBorder)
    {
        return duplicate().setVerticalBorder(verticalBorder);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the absolute width provided.  All other attributes of the new {@link
     * ColoredStringIcon} are identical to this one.
     *
     * @param absoluteWidth The new absolute width of this icon, or <code>0</code> to allow this icon to determine its
     *                       own width.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveAbsoluteWidth(int absoluteWidth)
    {
        return duplicate().setAbsoluteWidth(absoluteWidth);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the absolute height provided.  All other attributes of the new
     * {@link ColoredStringIcon} are identical to this one.
     *
     * @param absoluteHeight The new absolute height of this icon, or <code>0</code> to allow this icon to determine
     *                        its own height.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveAbsoluteHeight(int absoluteHeight)
    {
        return duplicate().setAbsoluteHeight(absoluteHeight);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the text provided.  All other attributes of the new {@link
     * ColoredStringIcon} are identical to this one.
     *
     * @param text The new text for this icon.
     * @return The new {@link ColoredStringIcon}.
     */
    public ColoredStringIcon deriveText(String text)
    {
        return duplicate().setText(text);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the outline color provided.  All other attributes of the new {@link
     * ColoredStringIcon} are identical to this one.
     *
     * @param outlineColor The new outline color for this icon.
     * @return This object.
     */
    public ColoredStringIcon deriveOutlineColor(Color outlineColor)
    {
        return duplicate().setOutlineColor(outlineColor);
    }

    /**
     * Creates a new {@link ColoredStringIcon} with the outline size provided.  All other attributes of the new {@link
     * ColoredStringIcon} are identical to this one.
     *
     * @param outlineSize The new outline size for this icon.
     * @return This object.
     */
    public ColoredStringIcon deriveOutlineSize(int outlineSize)
    {
        return duplicate().setOutlineSize(outlineSize);
    }

    /**
     * Retrieves an {@link Image} containing the likeness of this icon.
     *
     * @return An {@link Image} on which this icon has been drawn.
     */
    public Image getImage()
    {
        BufferedImage image = new BufferedImage(this.getIconWidth(), this.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        paintIcon(null, image.createGraphics(), 0, 0);
        return image;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}