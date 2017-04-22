package orioni.jz.awt;

import orioni.jz.math.MathUtilities;
import orioni.jz.util.Pair;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

/**
 * This class contains several static utility methods related to AWT.
 *
 * @author Zachary Palmer
 */
public class AWTUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An empty {@link Color}<code>[]</code>.
     */
    public static final Color[] EMPTY_COLOR_ARRAY = new Color[0];

    /**
     * This {@link Color} object represents a transparent "color."
     */
    public static final Color COLOR_TRANSPARENT = new Color(0, true);
    /**
     * This {@link Dimension} object represents a dimension of <code>(0,0)</code>.
     */
    public static final Dimension DIMENSION_SIZE_ZERO = new Dimension(0, 0);

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Prevent the instantiation of the <code>AWTUtilities</code> class.
     */
    private AWTUtilities()
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves the {@link KeyEvent} key constant for a specified character.  The provided constant is not
     * case-sensitive.
     *
     * @param ch The character for which a {@link KeyEvent} <code>VK</code> constant is needed.
     * @return One of the <code>VK_XXXX</code> constants on the {@link KeyEvent} class.  If no such constant suits (such
     *         as is the case for unprintable characters), <code>VK_UNDEFINED</code> is returned.
     */
    public static int getKeyEventConstantForCharacter(char ch)
    {
        if (Character.isLetterOrDigit(ch))
        {
            if (Character.isLowerCase(ch)) ch = Character.toUpperCase(ch);
            return ch;
        } else
        {
            switch (ch)
            {
                case '*':
                    return KeyEvent.VK_ASTERISK;
                case '@':
                    return KeyEvent.VK_AT;
                case '`':
                    return KeyEvent.VK_BACK_QUOTE;
                case '\\':
                    return KeyEvent.VK_BACK_SLASH;
                case '\b':
                    return KeyEvent.VK_BACK_SPACE;
                case '{':
                    return KeyEvent.VK_BRACELEFT;
                case '}':
                    return KeyEvent.VK_BRACERIGHT;
                case '^':
                    return KeyEvent.VK_CIRCUMFLEX;
                case ']':
                    return KeyEvent.VK_CLOSE_BRACKET;
                case ':':
                    return KeyEvent.VK_COLON;
                case ',':
                    return KeyEvent.VK_COMMA;
                case '$':
                    return KeyEvent.VK_DOLLAR;
                case '\n':
                    return KeyEvent.VK_ENTER;
                case '!':
                    return KeyEvent.VK_EXCLAMATION_MARK;
                case '>':
                    return KeyEvent.VK_GREATER;
                case '(':
                    return KeyEvent.VK_LEFT_PARENTHESIS;
                case '<':
                    return KeyEvent.VK_LESS;
                case '-':
                    return KeyEvent.VK_MINUS;
                case '#':
                    return KeyEvent.VK_NUMBER_SIGN;
                case '[':
                    return KeyEvent.VK_OPEN_BRACKET;
                case '.':
                    return KeyEvent.VK_PERIOD;
                case '+':
                    return KeyEvent.VK_PLUS;
                case '\'':
                    return KeyEvent.VK_QUOTE;
                case '"':
                    return KeyEvent.VK_QUOTEDBL;
                case ')':
                    return KeyEvent.VK_RIGHT_PARENTHESIS;
                case ';':
                    return KeyEvent.VK_SEMICOLON;
                case '/':
                    return KeyEvent.VK_SLASH;
                case ' ':
                    return KeyEvent.VK_SPACE;
                case '\t':
                    return KeyEvent.VK_TAB;
                case '_':
                    return KeyEvent.VK_UNDEFINED;
            }
        }
        return KeyEvent.VK_UNDEFINED;
    }

    /**
     * This method will convert the provided Cartesian <code>Point</code> object to polar coordinates, rotate it around
     * the origin the specific number of radians, and convert it back into the Cartesian coordinate space. The
     * <code>Point</code> object that is passed in is altered by this conversion and returned by the function.
     *
     * @param point The starting point that will be rotated around the origin.
     * @param angle The angle (in radians) to rotate the point around the origin.
     * @return The <code>Point</code> object that was passed in, modified to reflect the rotation performed.
     */
    public static Point rotatePoint(Point point, double angle)
    {
        double theta = Math.atan(point.getY() / point.getX());
        double r = Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY());
        theta += angle;
        point.setLocation(r * Math.cos(theta), r * Math.sin(theta));
        return point;
    }

    /**
     * This method accepts a number of {@link Color} objects and blends them evenly.
     *
     * @param colors The colors to blend.
     * @return The resulting average {@link Color}.
     */
    public static Color blendColors(Color... colors)
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        for (Color c : colors)
        {
            red += c.getRed();
            green += c.getGreen();
            blue += c.getBlue();
            alpha += c.getAlpha();
        }
        return new Color(red / colors.length, green / colors.length, blue / colors.length, alpha / colors.length);
    }

    /**
     * This method accepts two {@link Color} objects and a weight.  The purpose of this method is to blend the two
     * colors dependent upon the weight.  A value of <code>0.00</code> indicates that 0% of the second color should be
     * used, while 100% of the first color should be used (i.e., return the first color).  A value of <code>1.00</code>
     * indicates the opposite and results in a return of the second color.  A value of <code>0.25</code>, for example,
     * would return a color which is comprised of 75% of the first color and 25% of the second color.
     * <p/>
     * If the weight provided is less than zero, it will be treated as if it were zero.  If it is greater than one, it
     * will be treated as if it were one.
     *
     * @param first  The first {@link Color} object to blend.
     * @param second The second {@link Color} object to blend.
     * @param weight The weight with which to blend the colors, as described above.
     * @return The blended {@link Color}.
     */
    public static Color blendColors(Color first, Color second, double weight)
    {
        return new Color(blendColors(first.getRGB(), second.getRGB(), weight));
    }

    /**
     * This method accepts two sRGB values and a weight.  The purpose of this method is to blend the two colors
     * dependent upon the weight.  A value of <code>0.00</code> indicates that 0% of the second color should be used,
     * while 100% of the first color should be used (i.e., return the first color).  A value of <code>1.00</code>
     * indicates the opposite and results in a return of the second color.  A value of <code>0.25</code>, for example,
     * would return a color which is comprised of 75% of the first color and 25% of the second color. <P> If the weight
     * provided is less than zero, it will be treated as if it were zero.  If it is greater than one, it will be treated
     * as if it were one.
     *
     * @param first  The first sRGB value to blend.
     * @param second The second sRGB value to blend.
     * @param weight The weight with which to blend the colors, as described above.
     * @return The blended sRGB value.
     */
    public static int blendColors(int first, int second, double weight)
    {
        if (weight <= 0) return first;
        if (weight >= 1) return second;
        double otherWeight = 1.0 - weight;
        return ((int) (((first >>> 24) & 0xFF) * otherWeight + ((second >>> 24) & 0xFF) * weight) << 24) |
               ((int) (((first >>> 16) & 0xFF) * otherWeight + ((second >>> 16) & 0xFF) * weight) << 16) |
               ((int) (((first >>> 8) & 0xFF) * otherWeight + ((second >>> 8) & 0xFF) * weight) << 8) |
               ((int) ((first & 0xFF) * otherWeight + (second & 0xFF) * weight));
    }

    /**
     * This method accepts two sRGB values and blends them.  This method is slightly less accurate than {@link
     * AWTUtilities#blendColors(int, int, double)} and only performs blending on a 50% ratio (as if calling {@link
     * AWTUtilities#blendColors(int, int, double) AWTUtilities.blendColors(int, int, 0.5)}), but is significantly faster
     * in that it performs one simultaneous integer operation rather than multiple floating point operation.
     *
     * @param first  The first sRGB value to blend.
     * @param second The second sRGB value to blend.
     * @return The evenly-blended sRGB value.  Up to one bit of error for each sample may occur (approx. 0.4%).
     */
    public static int blendColorsQuickly(int first, int second)
    {
        return ((first & 0xFEFEFEFE) >>> 1) + ((second & 0xFEFEFEFE) >>> 1);
    }

    /**
     * Creates a {@link String} containing newlines to format the text into a block rather than a straight line.  The
     * width of the block is determined by the provided <code>max_width</code> parameter (in pixels) and the {@link
     * Font} provided.
     *
     * @param string   The string to newline.
     * @param font     The {@link Font} in which to assume the string will be rendered.
     * @param maxWidth The max_width of the block.  If this is smaller than the max_width of a character in the string,
     *                 the characters will exceed the block size.
     * @return The modified string, containing newlines to create a text block.
     */
    public static String getNewlinedString(String string, Font font, int maxWidth)
    {
        char[] ch = string.toCharArray();
        StringBuffer sb = new StringBuffer();
        StringBuffer word = new StringBuffer();
        int i = 0;
        double currentWidth = 0;
        while (i < ch.length)
        {
            while ((i < ch.length) && ((!("\n\t ".contains(new String(new char[]{ch[i]})))) || (word.length() == 0)))
            {
                word.append(ch[i++]);
            }
            double widthThisString = font.getStringBounds(
                    word.toString(), new FontRenderContext(null, false, false))
                    .getWidth();
            if (currentWidth + widthThisString > (maxWidth - 1)) // -1 for safety
            {
                sb.append("\n");
                currentWidth = 0;
            }

            sb.append(word.toString());
            currentWidth += widthThisString;
            word.delete(0, word.length());
        }
        return sb.toString();
    }

    /**
     * Creates a {@link String} containing newlines to format the text into a block rather than a straight line.  The
     * possible widths of the block are determined by the provided <code>widths</code> parameter (in pixels) and the
     * {@link Font} provided.  The returned string is formatted for the width which best matches the default graphics
     * device's ratio.
     *
     * @param string The {@link String} to newline.
     * @param font   The {@link Font} in which to assume the string will be rendered.
     * @param widths The widths to try.
     * @return A {@link Pair}ing containing the newlined string and the width for which it was prepared.
     */
    public static Pair<String, Integer> getNewlinedString(String string, Font font, int[] widths)
    {
        // establish target ratio... most people are more comfortable with slightly wider text panes than screen ratio
        Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();
        double targetRatio = screenBounds.getWidth() / screenBounds.getHeight();

        double bestRatio = Double.POSITIVE_INFINITY;
        String bestString = null;
        int bestWidth = 100;
        for (int i = 0; i < widths.length; i++)
        {
            String newlined = AWTUtilities.getNewlinedString(string, font, widths[i]);
            int height = (int) (Math.ceil(
                    font.getStringBounds(newlined, new FontRenderContext(null, false, false)).
                            getHeight()) * newlined.split("\n").length);
            double thisRatio = widths[i] / (double) (height);
            double thisComparison = thisRatio / targetRatio;
            double bestComparison = bestRatio / targetRatio;
            if (Math.abs(1 - (targetRatio / Math.abs(1 - Math.abs(thisComparison)))) <
                Math.abs(1 - (targetRatio / Math.abs(1 - Math.abs(bestComparison)))))
            {
                bestRatio = thisRatio;
                bestString = newlined;
                bestWidth = widths[i];
            }
        }
        return new Pair<String, Integer>(bestString, bestWidth);
    }

    /**
     * Switches the palette for a {@link BufferedImage} using an {@link IndexColorModel}.  This changes the rendering
     * behavior (the {@link Color} to which each index maps) but not the content of the image.  The resulting image is
     * returned.
     *
     * @param image   The {@link BufferedImage} to use.
     * @param palette The new palette.
     * @return A {@link BufferedImage} based upon the old {@link BufferedImage} which uses the new palette.
     * @throws IllegalArgumentException If the provided {@link BufferedImage} is not using an {@link IndexColorModel}.
     */
    public static BufferedImage changePalette(BufferedImage image, IndexColorModel palette)
    {
        if (!(image.getColorModel() instanceof IndexColorModel))
        {
            throw new IllegalArgumentException("Provided image " + image + " is not using an IndexColorModel.");
        }
        BufferedImage ret = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED,
                palette);
        ret.setData(image.getData());
        return ret;
    }

    /**
     * Adjusts the specified sRGB value using the provided HSB modifiers.  Each modifier should be within the range
     * <code>[-1.0,1.0]</code>.
     *
     * @param rgb        The sRGB value to modify.
     * @param hue        The amount by which the hue should be changed.  As hue is a circular value, providing
     *                   <code>-1.0</code> will have the same effect as providing <code>1.0</code>.
     * @param saturation The amount by which the saturation should be changed.  A value of <code>-1.0</code> completely
     *                   removes all saturation (effectively grayscaling the image).
     * @param brightness The amount by which the brightness should be changed.
     * @return The resulting sRGB value.
     */
    public static int adjustHSB(int rgb, double hue, double saturation, double brightness)
    {
        return adjustHSB(rgb, hue, saturation, brightness, null);
    }

    /**
     * Adjusts the specified sRGB value using the provided HSB modifiers.  Each modifier should be within the range
     * <code>[-1.0,1.0]</code>.
     *
     * @param rgb            The sRGB value to modify.
     * @param hue            The amount by which the hue should be changed.  As hue is a circular value, providing
     *                       <code>-1.0</code> will have the same effect as providing <code>1.0</code>.
     * @param saturation     The amount by which the saturation should be changed.  A value of <code>-1.0</code>
     *                       completely removes all saturation (effectively grayscaling the image).
     * @param brightness     The amount by which the brightness should be changed.
     * @param hsbPixelFilter An {@link HsbPixelFilter} which will determine whether or not the operation is actually
     *                       performed.  If the filter rejects the HSB form of the provided RGB value, the returned
     *                       value is identical to the RGB value provided.  This parameter is used primarily for batch
     *                       operations; if <code>null</code>, the pixel is always affected.
     * @return The resulting sRGB value.
     */
    public static int adjustHSB(int rgb, double hue, double saturation, double brightness,
                                HsbPixelFilter hsbPixelFilter)
    {
        int alpha = rgb & 0xFF000000;
        hue = MathUtilities.bound(hue, -1, 1);
        saturation = MathUtilities.bound(saturation, -1, 1);
        brightness = MathUtilities.bound(brightness, -1, 1);

        float[] hsbArray = Color.RGBtoHSB((rgb >>> 16) & 0xFF, (rgb >>> 8) & 0xFF, rgb & 0xFF, null);

        if ((hsbPixelFilter != null) && (!hsbPixelFilter.filter(hsbArray[0], hsbArray[1], hsbArray[2]))) return rgb;

        hsbArray[0] += MathUtilities.bound(hue, -1.0, 1.0);
        if (saturation < 0)
        {
            hsbArray[1] *= (1 + saturation);
        } else
        {
            hsbArray[1] += (1 - hsbArray[1]) * saturation;
        }
        if (brightness < 0)
        {
            hsbArray[2] *= (1 + brightness);
        } else
        {
            hsbArray[2] += (1 - hsbArray[2]) * brightness;
        }
        return ((Color.HSBtoRGB(hsbArray[0], hsbArray[1], hsbArray[2])) & 0x00FFFFFF) | (alpha);
    }

    /**
     * Performs a minimum operation on a number of {@link Dimension} objects.  The returned value is a {@link Dimension}
     * object which is the smallest in both coordinates.
     *
     * @param dimensions The {@link Dimension}s to compare.
     * @return The largest possible {@link Dimension} which is smaller than or equal to all of the provided {@link
     *         Dimension}s.
     */
    public static Dimension dimensionMin(Dimension... dimensions)
    {
        int w = Integer.MAX_VALUE;
        int h = Integer.MAX_VALUE;
        for (Dimension d : dimensions)
        {
            w = Math.min(w, (int) (d.getWidth()));
            h = Math.min(h, (int) (d.getHeight()));
        }
        return new Dimension(w, h);
    }

    /**
     * Performs a maximum operation on a number of {@link Dimension} objects.  The returned value is a {@link Dimension}
     * object which is the largest in both coordinates.
     *
     * @param dimensions The {@link Dimension}s to compare.
     * @return The smallest possible {@link Dimension} which is larger than or equal to all of the provided {@link
     *         Dimension}s.
     */
    public static Dimension dimensionMax(Dimension... dimensions)
    {
        int w = 0;
        int h = 0;
        for (Dimension d : dimensions)
        {
            w = Math.max(w, (int) (d.getWidth()));
            h = Math.max(h, (int) (d.getHeight()));
        }
        return new Dimension(w, h);
    }
}

// END OF FILE //