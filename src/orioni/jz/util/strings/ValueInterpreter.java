package orioni.jz.util.strings;

/**
 * This interface translates strings to and from the specified parameterized class.
 *
 * @author Zachary Palmer
 */
public interface ValueInterpreter <T>
{
    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public T fromString(String s);

    /**
     * Translates an object of the specified type to a string.  The general contract of this method is that
     * <ul><code>this.fromString(this.toString(myObject)).equals(myObject) == true</code></ul> as long as the return
     * value of {@link orioni.jz.util.strings.ValueInterpreter#toString(Object)} is not <code>null</code>.
     *
     * @param t The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(T t);
}

// END OF FILE