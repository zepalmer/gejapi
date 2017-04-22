package orioni.jz.util.strings;

import java.awt.*;

/**
 * This implementation of {@link ValueInterpreter} is used to translate {@link java.awt.Color} objects.
 *
 * @author Zachary Palmer
 */
public class ColorInterpreter implements ValueInterpreter<Color>
{
    /**
     * A singleton instance of this class.
     */
    public static final ColorInterpreter SINGLETON = new ColorInterpreter();

    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public Color fromString(String s)
    {
        if (s == null) return null;
        try
        {
            if (s.toLowerCase().startsWith("0x"))
            {
                return new Color(Integer.parseInt(s.substring(2), 16), true);
            } else
            {
                return new Color(Integer.parseInt(s), true);
            }
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    /**
     * Translates a {@link java.awt.Color} to a {@link String}.
     *
     * @param color The value to convert.
     * @return A string representing the {@link java.awt.Color}.
     */
    public String toString(Color color)
    {
        return Integer.toString(color.getRGB());
    }
}

// END OF FILE