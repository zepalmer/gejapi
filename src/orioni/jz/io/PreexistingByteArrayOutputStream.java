package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This {@link OutputStream} is implemented to write the data written to it to a <code>byte[]</code>.  Unlike {@link
 * ByteArrayOutputStream}, this stream writes its data to a pre-existing <code>byte[]</code> starting from an index
 * specified upon construction.  If more data is written than fits in the array starting at that index, an {@link
 * IOException} is thrown.
 *
 * @author Zachary Palmer
 */
public class PreexistingByteArrayOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The <code>byte[]</code> to which the data will be written.
     */
    protected byte[] target;
    /**
     * The index at which the next write is to take place.
     */
    protected int index;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that writes to the array start at index zero.
     *
     * @param target The <code>byte[]</code> to which data should be written.
     */
    public PreexistingByteArrayOutputStream(byte[] target)
    {
        this(target, 0);
    }

    /**
     * General constructor.
     *
     * @param target The <code>byte[]</code> to which data should be written.
     * @param index  The starting index for writes to the arrya.
     */
    public PreexistingByteArrayOutputStream(byte[] target, int index)
    {
        this.target = target;
        this.index = index;
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
        if (index >= target.length) throw new IOException("No more space into which to write data.");
        target[index++] = (byte) b;
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
        if (index + len > target.length)
        {
            throw new IOException("No more space into which to write data.");
        }
        System.arraycopy(b, off, target, index, len);
        index += len;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE