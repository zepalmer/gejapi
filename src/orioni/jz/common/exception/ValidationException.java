package orioni.jz.common.exception;

/**
 * This {@link Exception} class is designed to represent an event in which a piece of data failed a validation test.
 *
 * @author Zachary Palmer
 */
public class ValidationException extends Exception
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
    public ValidationException()
    {
        super();
    }

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception(String)
     */
    public ValidationException(String s)
    {
        super(s);
    }

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception()
     */
    public ValidationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Wrapper constructor.
     *
     * @see Exception#Exception(String)
     */
    public ValidationException(String s, Throwable cause)
    {
        super(s, cause);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Performs an assertion-like operation.  If the provided condition is false, a {@link ValidationException} with the
     * provided string as its message is thrown.
     *
     * @param condition The condition to test.
     * @param message   The message to provide to any thrown exception.
     * @throws ValidationException If the provided condition is <code>false</code>.
     */
    public static void performValidationAssertion(boolean condition, String message)
            throws ValidationException
    {
        if (!condition) throw new ValidationException(message);
    }
}

// END OF FILE
