package orioni.jz.util.strings;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation of {@link ValueInterpreter} is used to translate {@link java.util.List}<code>&lt;{@link
 * String}&gt;</code> objects.
 *
 * @author Zachary Palmer
 */
public class ListInterpreter<T> implements ValueInterpreter<List<T>>
{
    /**
     * The {@link orioni.jz.util.strings.ValueInterpreter} used for the parameterized list type.
     */
    protected ValueInterpreter<T> interpreter;

    /**
     * General constructor.
     *
     * @param interpreter The {@link ValueInterpreter} used for the parameterized list type.
     */
    public ListInterpreter(ValueInterpreter<T> interpreter)
    {
        this.interpreter = interpreter;
    }

    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public List<T> fromString(String s)
    {
        try
        {
            int index = 0;
            ArrayList<T> als = new ArrayList<T>();
            while (index < s.length())
            {
                int start = index;
                while ((index < s.length()) && (s.charAt(index) != '!'))
                {
                    index++;
                }
                String lengthSubstring = s.substring(start, index);
                index++;
                int len = Math.min(Integer.parseInt(lengthSubstring), s.length() - index);
                als.add(interpreter.fromString(s.substring(index, index + len)));
                index += len;
            }
            return als;
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    /**
     * Translates an object of the specified type to a string.  The general contract of this method is that
     * <ul><code>this.fromString(this.toString(myObject)).equals(myObject) == true</code></ul> as long as the return
     * value of {@link orioni.jz.util.strings.ValueInterpreter#toString(Object)} is not <code>null</code>.
     *
     * @param list The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(List<T> list)
    {
        StringBuffer sb = new StringBuffer();
        for (T t : list)
        {
            String s = interpreter.toString(t);
            sb.append(s.length()).append("!").append(s);
        }
        return sb.toString();
    }
}

// END OF FILE