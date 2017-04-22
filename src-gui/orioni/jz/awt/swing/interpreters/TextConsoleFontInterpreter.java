package orioni.jz.awt.swing.interpreters;

import orioni.jz.awt.swing.BitmappedTextConsoleFont;
import orioni.jz.awt.swing.TextConsoleFont;
import orioni.jz.awt.swing.WrappingTextConsoleFont;
import orioni.jz.util.strings.ValueInterpreter;

import java.awt.*;

/**
 * This class is designed to text-serialize {@link TextConsoleFont} objects.
 *
 * @author Zachary Palmer
 */
public class TextConsoleFontInterpreter implements ValueInterpreter<TextConsoleFont>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this interpreter.
     */
    public static final TextConsoleFontInterpreter SINGLETON = new TextConsoleFontInterpreter();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public TextConsoleFontInterpreter()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public TextConsoleFont fromString(String s)
    {
        if (s == null) return null;
        if (s.startsWith("w/"))
        {
            Font f = FontInterpreter.SINGLETON.fromString(s.substring(2));
            if (f == null)
            {
                return null;
            } else
            {
                return new WrappingTextConsoleFont(f);
            }
        } else if (s.startsWith("b/"))
        {
            return new BitmappedTextConsoleFont();
        } else
        {
            return null;
        }
    }

    /**
     * Translates an object of the specified type to a string.  The general contract of this method is that
     * <ul><code>this.fromString(this.toString(myObject)).equals(myObject) == true</code></ul> as long as the return
     * value of {@link orioni.jz.util.strings.ValueInterpreter#toString(Object)} is not <code>null</code>.
     *
     * @param t The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(TextConsoleFont t)
    {
        if (t instanceof WrappingTextConsoleFont)
        {
            WrappingTextConsoleFont wrapper = (WrappingTextConsoleFont) t;
            Font f = wrapper.getFont();
            return "w/" + FontInterpreter.SINGLETON.toString(f);
        } else if (t instanceof BitmappedTextConsoleFont)
        {
            return "b/";
        } else
        {
            return null;
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
