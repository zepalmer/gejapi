package orioni.jz.util.strings;



/**
 * This implementation of {@link ValueInterpreter} is used to translate {@link Boolean} objects.
 *
 * @author Zachary Palmer
 */
public class BooleanInterpreter implements ValueInterpreter<Boolean>
{
    /**
     * A singleton instance of this class.
     */
    public static final BooleanInterpreter SINGLETON = new BooleanInterpreter();

    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public Boolean fromString(String s)
    {
        return ("yes".equals(s));
    }

    /**
     * Translates an object of the specified type to a string.
     *
     * @param bool The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(Boolean bool)
    {
        return ((bool) ? "yes" : "");
    }
}

// END OF FILE