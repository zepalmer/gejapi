package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This {@link OutputStream} extension accepts an underlying output stream and passes all of its writes verbatim to the
 * underlying stream.  It also calls the {@link NotifyingOutputStream#writeOccurred()} method whenever a write occurs,
 * allowing extending classes to perform actions when a write occurs.
 *
 * @author Zachary Palmer
 */
public abstract class NotifyingOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link OutputStream} to which to pass writes.
     */
    protected OutputStream target;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param target The underlying {@link OutputStream}.
     */
    public NotifyingOutputStream(OutputStream target)
    {
        super();
        this.target = target;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Writes the provided data.  Also calls {@link NotifyingOutputStream#writeOccurred()}.
     *
     * @param b The data to write.
     * @throws IOException If an I/O error occurs.
     * @see OutputStream#write(int)
     */
    public void write(int b)
            throws IOException
    {
        target.write(b);
        writeOccurred();
    }

    /**
     * Writes the provided data.  Also calls {@link NotifyingOutputStream#writeOccurred()}.
     *
     * @param b The data.
     * @throws IOException If an I/O error occurs.
     * @see OutputStream#write(byte[])
     */
    public void write(byte[] b)
            throws IOException
    {
        target.write(b);
        writeOccurred();
    }

    /**
     * Writes the provided data.  Also calls {@link NotifyingOutputStream#writeOccurred()}.
     *
     * @param b   The data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException If an I/O error occurs.
     * @see OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len)
            throws IOException
    {
        target.write(b, off, len);
        writeOccurred();
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close()
            throws IOException
    {
        flush(); // in case extensions use it
        target.close();
    }

    /**
     * This method is called whenever a write occurs on this stream.
     */
    public abstract void writeOccurred();

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE