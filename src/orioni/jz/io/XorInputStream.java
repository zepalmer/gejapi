package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * A XorInputStream is designed to simply binary XOR any bytes read from the contained InputStream.  The XOR pattern is
 * defined on construction.
 *
 * @author Zachary Palmer
 */
public class XorInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The XOR pattern for this stream.
     */
    protected byte[] xorPattern;
    /**
     * The position for this stream.
     */
    protected int xorPosition;
    /**
     * The InputStream from which bytes are to be read for XORing.
     */
    protected InputStream stream;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param stream  The InputStream from which bytes are to be read for XORing.
     * @param pattern The XOR pattern to use.
     * @throws IllegalArgumentException If the XOR pattern has a length of zero.
     */
    public XorInputStream(InputStream stream, byte[] pattern)
            throws IllegalArgumentException
    {
        super();
        if (pattern.length < 1) throw new IllegalArgumentException("XOR pattern must contain at least one byte.");
        this.stream = stream;
        xorPattern = pattern;
        xorPosition = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads a XORed byte from the contained InputStream.
     *
     * @return A single byte, read from the contained InputStream and XORed with the pattern provided to this stream on
     *         construction.
     * @throws IOException If the contained InputStream throws an IOException.
     */
    public int read()
            throws IOException
    {
        int data = stream.read();
        if (data == -1) return data;
        byte ret = (byte) (((byte) (data)) ^ xorPattern[xorPosition++]);
        if (xorPosition >= xorPattern.length) xorPosition = 0;
        return ret;
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        stream.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //