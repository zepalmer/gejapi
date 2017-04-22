package orioni.jz.util.strings;

import orioni.jz.util.strings.ValueInterpreter;

/**
 * This implementation of {@link orioni.jz.util.strings.ValueInterpreter} is used to translate base ten {@link Integer}s.
 *
 * @author Zachary Palmer
 */
public class IntegerInterpreter implements ValueInterpreter<Integer>
{
    /**
     * A singleton instance of this class.
     */
    public static final IntegerInterpreter SINGLETON = new IntegerInterpreter();

    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public Integer fromString(String s)
    {
        try
        {
            return Integer.parseInt(s);
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    /**
     * Translates an object of the specified type to a string.
     *
     * @param integer The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(Integer integer)
    {
        return String.valueOf(integer);
    }
}

// END OF FILE