package orioni.jz.common.exception;

/**
 * <B><I>This file lacks a description.</I></B>
 *
 * @author Zachary Palmer
 */
public class ParseException extends Exception
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception()
     */
    public ParseException()
    {
        super();
    }

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception(String)
     */
    public ParseException(String s)
    {
        super(s);
    }

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception()
     */
    public ParseException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception(String)
     */
    public ParseException(String s, Throwable cause)
    {
        super(s, cause);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Performs an assertion-like operation.  If the provided condition is false, a {@link ParseException} with the
     * provided string as its message is thrown.
     *
     * @param condition The condition to test.
     * @param message   The message to provide to any thrown exception.
     * @throws ParseException If the provided condition is <code>false</code>.
     */
    public static void performParseAssertion(boolean condition, String message)
            throws ParseException
    {
        if (!condition) throw new ParseException(message);
    }
}