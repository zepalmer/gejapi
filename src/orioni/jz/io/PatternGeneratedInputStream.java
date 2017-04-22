package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This {@link InputStream} produces the same array of bytes in repetition.  It never exhausts.
 *
 * @author Zachary Palmer
 */
public class PatternGeneratedInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The <code>byte[]</code> to repeat.
     */
    protected byte[] pattern;
    /**
     * The index in the pattern for the next read.
     */
    protected int index;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param pattern The <code>byte[]</code> to repeat.
     */
    public PatternGeneratedInputStream(byte[] pattern)
    {
        super();
        this.pattern = pattern;
        index = 0;
        if (this.pattern.length == 0)
        {
            throw new IllegalArgumentException("Patten has no contents from which to generate input.");
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>.
     *
     * @return The next byte of data.
     * @throws IOException If an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        int ret = pattern[index++];
        if (index >= pattern.length) index = 0;
        return ret;
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the
     * next caller of a method for this input stream.
     *
     * @return {@link Integer#MAX_VALUE}, always.
     */
    public int available()
    {
        return Integer.MAX_VALUE;
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into an array of bytes.  An attempt is made to
     * read as many as <code>len</code> bytes, but a smaller number may be read. The number of bytes actually read is
     * returned as an integer.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array <code>b</code> at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return The total number of bytes read into the buffer.
     */
    public int read(byte[] b, int off, int len)
    {
        int total = 0;
        while (len > 0)
        {
            int amount = Math.min(len, pattern.length - index);
            System.arraycopy(pattern, index, b, off, amount);
            off += amount;
            len -= amount;
            index += amount;
            total += amount;
            if (index >= pattern.length) index = 0;
        }
        return total;
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input stream.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     */
    public long skip(long n)
    {
        index = (int) ((n + index) % pattern.length);
        return n;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE