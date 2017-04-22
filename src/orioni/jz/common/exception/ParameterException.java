package orioni.jz.common.exception;

/**
 * This exception class is designed to be thrown whenever a parameter does not
 * meet a general requirement.
 * @author Zachary Palmer
 */
public class ParameterException extends Exception
{
    /** Wrapper constructor. */
    public ParameterException() { super(); }
    /** Wrapper constructor. */
    public ParameterException(String s) { super(s); }
}
