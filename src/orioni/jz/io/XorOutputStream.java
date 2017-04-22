package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A XorOutputStream is designed to simply binary XOR any bytes read from the contained OutputStream.  The XOR pattern
 * is defined on construction.
 *
 * @author Zachary Palmer
 */
public class XorOutputStream extends OutputStream
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
     * The OutputStream to which bytes are to be written after XORing.
     */
    protected OutputStream stream;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param stream  The OutputStream to which bytes are to be written after XORing.
     * @param pattern The XOR pattern to use.
     * @throws IllegalArgumentException If the XOR pattern has a length of zero.
     */
    public XorOutputStream(OutputStream stream, byte[] pattern)
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
     * Writes a XORed byte to the contained OutputStream.
     *
     * @param b A single byte to be written to the contained OutputStream after it is XORed with the pattern provided
     *          upon construction.
     * @throws IOException If the contained OutputStream throws an IOException.
     */
    public void write(int b)
            throws IOException
    {
        int data = ((byte) (b)) ^ (xorPattern[xorPosition++]);
        stream.write(data);
        if (xorPosition >= xorPattern.length) xorPosition = 0;
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        flush();
        stream.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //