package orioni.jz.io;

import orioni.jz.util.Pair;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This {@link OutputStream} represents the general concept of writing a single stream of content to multiple streams.
 * This class is not a broadcaster; any byte of data written to this stream is only written to one of the underlying
 * streams.  This class is instead designed to delegate the location of the data that is written to it.
 *
 * @author Zachary Palmer
 */
public abstract class MultiplexOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public MultiplexOutputStream()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method retrieves the {@link OutputStream} to which the provided data should be written, combined with the
     * total amount of data which should be written to that stream.  This method is used to control the manner in which
     * data is written to the {@link OutputStream}s.
     *
     * @param data The array containing data which is to be written to some output stream.
     * @param off  The starting offset of the data in the array.
     * @param len  The length of the data to be written to an {@link OutputStream}.
     * @return A {@link Pair}<code>&lt;{@link OutputStream},<code>int&gt;</code>.  The {@link OutputStream} is the
     *         stream to which data should be written; the <code>int</code> is the amount of data to write.
     * @throws IOException If an I/O error occurs while establishing an output resource.
     */
    public abstract Pair<OutputStream, Integer> getNextWriteOperation(byte[] data, int off, int len)
        throws IOException;

    /**
     * Writes the given byte of data to an underlying output stream.
     *
     * @param b The byte to write.  See {@link java.io.OutputStream} for more information.
     * @throws IOException If an I/O error occurs while writing the data or while establishing a new underlying output
     *                     stream.
     */
    public void write(int b)
            throws IOException
    {
        write(new byte[]{(byte) b}, 0, 1);
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
        while (len > 0)
        {
            Pair<OutputStream, Integer> writeOperation = getNextWriteOperation(b, off, len);
            writeOperation.getFirst().write(b, off, writeOperation.getSecond());
            off += writeOperation.getSecond();
            len -= writeOperation.getSecond();
        }
    }

    /**
     * This method flushes any buffers associated with this stream and cleans up any associated resources.
     *
     * @throws IOException If an I/O exception occurs while cleaning up the current output target or if an exception
     *                     occurs while closing this stream.
     */
    public void close()
            throws IOException
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //