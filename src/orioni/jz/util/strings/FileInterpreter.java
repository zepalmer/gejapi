package orioni.jz.util.strings;

import java.io.File;

/**
 * This implementation of {@link ValueInterpreter} is designed to translate {@link String}s to {@link File}s and vice
 * versa.
 */
public class FileInterpreter implements ValueInterpreter<File>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /** A singleton instance of this class. */
    public static final FileInterpreter SINGLETON = new FileInterpreter();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public FileInterpreter()
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
    public File fromString(String s)
    {
        if (s==null) return null; else return new File(s);
    }

    /**
     * Translates an object of the specified type to a string.  The general contract of this method is that
     * <ul><code>this.fromString(this.toString(myObject)).equals(myObject) == true</code></ul> as long as the return
     * value of {@link orioni.jz.util.strings.ValueInterpreter#toString(Object)} is not <code>null</code>.
     *
     * @param t The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(File t)
    {
        if (t==null) return null; else return t.getPath();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
