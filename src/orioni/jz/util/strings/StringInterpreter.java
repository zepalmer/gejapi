package orioni.jz.util.strings;



/**
 * This implementation of {@link ValueInterpreter} is used to translate simple strings.
 *
 * @author Zachary Palmer
 */
public class StringInterpreter implements ValueInterpreter<String>
{
    /**
     * A singleton instance of this class.
     */
    public static final StringInterpreter SINGLETON = new StringInterpreter();

    /**
     * Returns the provided string.
     *
     * @param s The string to convert.
     * @return <code>s</code>, always.
     */
    public String fromString(String s)
    {
        return s;
    }

    /**
     * Returns the provided string.
     *
     * @param s The value to convert.
     * @return <code>s</code>, always.
     */
    public String toString(String s)
    {
        return s;
    }
}

// END OF FILE