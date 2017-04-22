package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This {@link InputStream} implementation reads at most a specified number of bytes from the underlying stream.  It
 * then reports itself as being out of data, even if the underlying stream has more.
 *
 * @author Zachary Palmer
 */
public class SizeLimitedInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link InputStream} from which to read.
     */
    protected InputStream inputStream;
    /**
     * The maximum number of bytes left to read.
     */
    protected int left;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param inputStream The underlying {@link InputStream} from which to read.
     * @param max          The maximum number of bytes to read from the underlying {@link InputStream}.  If the
     *                     underlying {@link InputStream} has less data than this value, that amount will be readable
     *                     instead.
     */
    public SizeLimitedInputStream(InputStream inputStream, int max)
    {
        super();
        this.inputStream = inputStream;
        left = max;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available because the end of the stream has been reached, the
     * value <code>-1</code> is returned. This method blocks until input data is available, the end of the stream is
     * detected, or an exception is thrown.
     *
     * @return The next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        if (left > 0)
        {
            left--;
            return inputStream.read();
        } else
        {
            return -1;
        }
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the
     * next caller of a method for this input stream.  The next caller might be the same thread or another thread.
     *
     * @return the number of bytes that can be read from this input stream without blocking.
     * @throws IOException if an I/O error occurs.
     */
    public int available()
            throws IOException
    {
        return Math.min(inputStream.available(), left);
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        left = 0;
        inputStream.close();
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into an array of bytes.  An attempt is made to
     * read as many as <code>len</code> bytes, but a smaller number may be read. The number of bytes actually read is
     * returned as an integer.
     * <p/>
     * This method blocks until input data is available, end of file is detected, or an exception is thrown.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array <code>b</code> at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
     *         end of the stream has been reached.
     * @throws IOException          if an I/O error occurs.
     * @throws NullPointerException if <code>b</code> is <code>null</code>.
     * @see InputStream#read()
     */
    public int read(byte[] b, int off, int len)
            throws IOException
    {
        if (left > 0)
        {
            int read = inputStream.read(b, off, Math.min(left, len));
            left -= read;
            return read;
        } else
        {
            return -1;
        }
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input stream. The <code>skip</code> method may,
     * for a variety of reasons, end up skipping over some smaller number of bytes, possibly <code>0</code>. This may
     * result from any of a number of conditions; reaching end of file before <code>n</code> bytes have been skipped is
     * only one possibility. The actual number of bytes skipped is returned.  If <code>n</code> is negative, no bytes
     * are skipped.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException if an I/O error occurs.
     */
    public long skip(long n)
            throws IOException
    {
        int amount = (int) (Math.min(n, left));
        left -= amount;
        return amount;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE