package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This {@link OutputStream} implementation takes a number of {@link OutputStream}s as its targets.  The data which is
 * written to this stream is written to each of the {@link OutputStream}s provided.
 * <p/>
 * If one of the streams throws an {@link IOException} on any method call, that {@link IOException} is immediately
 * thrown.  For example, suppose a {@link ForkingOutputStream} with target streams <code>A</code>, <code>B</code>, and
 * <code>C</code>.  If data is written to this stream, all three streams receive a write call.  If <code>B</code> throws
 * an {@link IOException} on write, <code>C</code> never has data written to it but <code>A</code> has.  This makes the
 * ordering of the {@link OutputStream}s provided to the constructor significant.
 *
 * @author Zachary Palmer
 */
public class ForkingOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link OutputStream}s over which this {@link ForkingOutputStream} should fork its calls.
     */
    protected OutputStream[] streams;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param streams The target {@link OutputStream}s to use.
     */
    public ForkingOutputStream(OutputStream... streams)
    {
        super();
        this.streams = streams;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte is
     * written to the output stream. The byte to be written is the eight low-order bits of the argument <code>b</code>.
     * The 24 high-order bits of <code>b</code> are ignored.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular, an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    public void write(int b)
            throws IOException
    {
        for (OutputStream o : streams)
        {
            o.write(b);
        }
    }

    /**
     * Closes the underlying output streams in order.  If an I/O error occurs while closing a given stream, any streams
     * which were specified after it are left in an indeterminate state.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close()
            throws IOException
    {
        flush();
        for (OutputStream o : streams)
        {
            o.close();
        }
    }

    /**
     * Flushes all of the underlying output streams in order.  If an I/O error occurs while flushing a given stream, any
     * streams which were specified after it are left in an indeterminate state.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void flush()
            throws IOException
    {
        for (OutputStream o : streams)
        {
            o.flush();
        }
    }

    /**
     * Writes <code>b.length</code> bytes from the specified byte array to this output stream. The general contract for
     * <code>write(b)</code> is that it should have exactly the same effect as the call <code>write(b, 0,
     * b.length)</code>.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     */
    public void write(byte[] b)
            throws IOException
    {
        for (OutputStream o : streams)
        {
            o.write(b);
        }
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this output
     * stream. The general contract for <code>write(b, off, len)</code> is that some of the bytes in the array
     * <code>b</code> are written to the output stream in order; element <code>b[off]</code> is the first byte written
     * and <code>b[off+len-1]</code> is the last byte written by this operation.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs. In particular, an <code>IOException</code> is thrown if the output
     *                     stream is closed.
     */
    public void write(byte[] b, int off, int len)
            throws IOException
    {
        for (OutputStream o : streams)
        {
            o.write(b, off, len);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE