package orioni.jz.io;

import java.io.ByteArrayInputStream;

/**
 * This {@link ByteArrayInputStream} extension is designed to provide random-access-like behavior.  The additional
 * method, {@link RandomAccessByteArrayInputStream#seek(int)}, allows the read pointer to be set.  This allows files
 * which must be read in a random-access fashion to be buffered regardless of that requirement.
 *
 * @author Zachary Palmer
 */
public class RandomAccessByteArrayInputStream extends ByteArrayInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The initial offset of the first read.
     */
    protected int initialOffset;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.
     *
     * @param buf The buffer from which to read.
     */
    public RandomAccessByteArrayInputStream(byte[] buf)
    {
        this(buf, 0, buf.length);
    }

    /**
     * General constructor.
     *
     * @param buf The buffer from which to read.
     * @param off The offset of the first read.
     * @param len The length of the first read.
     */
    public RandomAccessByteArrayInputStream(byte[] buf, int off, int len)
    {
        super(buf, off, len);
        initialOffset = off;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Seeks the read pointer to the specified location in the source array.  This method cannot be used to force the
     * {@link RandomAccessByteArrayInputStream} to read from a segment of the array declared off-limits by the {@link
     * RandomAccessByteArrayInputStream#RandomAccessByteArrayInputStream(byte[], int, int)} constructor.  The offset is
     * relative to the original offset set by that constructor (or <code>0</code> if the skeleton constructor is used).
     * Thus, if this stream were constructed with a call such as <code>new RandomAccessByteArrayInputStream(data, 5,
     * 20)</code>, the legal values for <code>offset</code> are between <code>0</code> and <code>20</code> and calling
     * <code>seek(0)</code> causes <code>data[5]</code> to be the next byte read.
     *
     * @param offset The new offset for the read pointer.
     * @throws IndexOutOfBoundsException If the specified location is greater than the legal
     */
    public void seek(int offset)
            throws IndexOutOfBoundsException
    {
        if (offset < 0) throw new IndexOutOfBoundsException("Offset cannot be less than 0 (was " + offset + ")");
        if (initialOffset + offset > super.count)
        {
            throw new IndexOutOfBoundsException(
                    "Offset cannot be greater than " + (super.count - initialOffset) +
                    ", the number of bytes originally available in the stream (was " +
                    offset +
                    ")");
        }
        super.pos = initialOffset + offset;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE