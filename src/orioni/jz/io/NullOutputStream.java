package orioni.jz.io;

import java.io.OutputStream;

/**
 * This {@link OutputStream} implementation is designed to act as a void into which data can be thrown.  Nothing will
 * ever be done with data provided to this stream; all writes will be treated as successful.
 *
 * @author Zachary Palmer
 */
public class NullOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance, for convenience.
     */
    public static final NullOutputStream SINGLETON = new NullOutputStream();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public NullOutputStream()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Does nothing.
     *
     * @param b Ignored.
     */
    public void write(int b)
    {
    }

    /**
     * Does nothing.
     */
    public void close()
    {
    }

    /**
     * Does nothing.
     */
    public void flush()
    {
    }

    /**
     * Does nothing.
     *
     * @param b Ignored.
     */
    public void write(byte[] b)
    {
    }

    /**
     * Does nothing.
     *
     * @param b   Ignored.
     * @param off Ignored.
     * @param len Ignored.
     */
    public void write(byte[] b, int off, int len)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}