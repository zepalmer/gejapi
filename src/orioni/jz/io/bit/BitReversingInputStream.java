package orioni.jz.io.bit;

import orioni.jz.math.MathUtilities;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reverses the order of all of the bits for every byte that is read from the stream.  For example, if the bytes
 * <code>01000110b 00010111b 00101111b 11101100b</code> are read from the underlying stream, this stream produces
 * <code>01100010b 11101000b 11110100b 00110111b</code>.  Note that the byte order is not changed: only the order of the
 * bits within the bytes.
 *
 * @author Zachary Palmer
 */
public class BitReversingInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link InputStream}.
     */
    protected InputStream source;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param source The underlying {@link InputStream} from which to read.
     */
    public BitReversingInputStream(InputStream source)
    {
        super();
        this.source = source;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the input stream.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        int n = source.read();
        if (n == -1) return n;
        return MathUtilities.reverseBits(n, 7, 0);
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the
     * next caller of a method for this input stream.  This method simply calls {@link java.io.InputStream#available()}
     * on the underlying stream.
     *
     * @return the number of bytes that can be read from this input stream without blocking.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public int available()
            throws IOException
    {
        return source.available();
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input stream. This method simply calls {@link
     * InputStream#skip(long)} on the underlying stream.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public long skip(long n)
            throws IOException
    {
        return source.skip(n);
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        source.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE