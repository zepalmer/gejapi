package orioni.jz.awt.swing.interpreters;

import orioni.jz.util.strings.IntegerInterpreter;
import orioni.jz.util.strings.ListInterpreter;
import orioni.jz.util.strings.StringInterpreter;
import orioni.jz.util.strings.ValueInterpreter;

import java.awt.*;
import java.util.ArrayList;

/**
 * This {@link ValueInterpreter} interprets the values of {@link Font} objects.
 *
 * @author Zachary Palmer
 */
public class FontInterpreter implements ValueInterpreter<Font>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this class.
     */
    public static final FontInterpreter SINGLETON = new FontInterpreter();
    /**
     * A {@link ListInterpreter}<code>&lt;{@link String}&gt;</code> which is used as an intermediary in the
     * interpretation process.
     */
    protected static final ListInterpreter<String> STRING_LIST_INTERPRETER =
            new ListInterpreter<String>(StringInterpreter.SINGLETON);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public FontInterpreter()
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
    public Font fromString(String s)
    {
        java.util.List<String> list = STRING_LIST_INTERPRETER.fromString(s);
        if (list == null) return null;
        if (list.size() != 3) return null;
        String name = list.get(0);
        String styleString = list.get(1);
        int style;
        if ("".equals(styleString))
        {
            style = Font.PLAIN;
        } else if ("i".equals(styleString))
        {
            style = Font.ITALIC;
        } else if ("b".equals(styleString))
        {
            style = Font.BOLD;
        } else if ("bi".equals(styleString))
        {
            style = Font.ITALIC + Font.BOLD;
        } else
        {
            return null;
        }
        Integer size = IntegerInterpreter.SINGLETON.fromString(list.get(2));
        if (size == null) return null;
        return new Font(name, style, size);
    }

    /**
     * Translates an object of the specified type to a string.  The general contract of this method is that
     * <ul><code>this.fromString(this.toString(myObject)).equals(myObject) == true</code></ul> as long as the return
     * value of {@link orioni.jz.util.strings.ValueInterpreter#toString(Object)} is not <code>null</code>.
     *
     * @param t The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(Font t)
    {
        java.util.List<String> list = new ArrayList<String>();
        list.add(t.getName());
        switch (t.getStyle())
        {
            case Font.PLAIN:
                list.add("");
                break;
            case Font.ITALIC:
                list.add("i");
                break;
            case Font.BOLD:
                list.add("b");
                break;
            case Font.ITALIC + Font.BOLD:
                list.add("bi");
                break;
            default:
                return null;
        }
        list.add(IntegerInterpreter.SINGLETON.toString(t.getSize()));
        return STRING_LIST_INTERPRETER.toString(list);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
