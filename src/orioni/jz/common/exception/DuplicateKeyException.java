package orioni.jz.common.exception;

/**
 * This Exception class is designed to be thrown when a database action is
 * performed that results in the addition of a duplicate unique key.
 * @author Zachary Palmer
 */
public class DuplicateKeyException extends Exception
{
    /** Wrapper constructor. */
    public DuplicateKeyException() { super(); }
    /** Wrapper constructor. */
    public DuplicateKeyException(String s) { super(); }
}
