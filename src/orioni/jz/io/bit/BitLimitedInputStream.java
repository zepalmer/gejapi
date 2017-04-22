package orioni.jz.io.bit;

import java.io.InputStream;
import java.io.IOException;

/**
 * This {@link BitInputStream} extension permits the reading of a specified number of bits.  After that number of bits
 * has been read, the stream behaves as if exhausted even if the underlying stream has more data.  If the number of bits
 * is not divisible by <code>8</code>, the extraneous bits of the last byte read from the underlying stream will be
 * discarded.
 *
 * @author Zachary Palmer
 */
public class BitLimitedInputStream extends BitInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The number of bits which can be read from this stream before it is exhausted.  This does not count data which
     *  is already present in the buffer. */
    protected long bitsLeftToRead;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * @param source The {@link java.io.InputStream} from which this {@link BitInputStream} gets its data.
     * @param order  The order in which the bits are read.
     * @param format The {@link EndianFormat} specifying how multi-byte values are read.
     * @param bits The number of bits which can be read from this stream before it behaves as if exhausted.
     */
    public BitLimitedInputStream(InputStream source, BitOrder order, EndianFormat format, long bits)
    {
        super(source, order, format);
        bitsLeftToRead = bits;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Attempts to ensure that there is at least one bit in the buffer.  If this method returns <code>false</code>, the
     * end of the stream has been reached.
     *
     * @return <code>true</code> if the buffer has at least one bit in it; <code>false</code> if it does not because the
     *         end of the stream has been reached.
     * @throws java.io.IOException If an I/O error occurs while refilling the buffer.
     */
    protected boolean ensureBuffer()
            throws IOException
    {
        if (bitsLeft >0) return true;
        if (bitsLeftToRead ==0) return false;
        if (bitsLeftToRead <8)
        {
            if (!super.ensureBuffer()) return false;
            bitsLeft = (byte)(bitsLeftToRead);
            switch (order)
            {
                case HIGHEST_BIT_FIRST:
                    buffer >>>= 8 - bitsLeft;
                    break;
                case LOWEST_BIT_FIRST:
                    buffer <<= 8 - bitsLeft;
                    break;
                default:
                    throw new IllegalStateException("Unrecognized bit order: "+order);
            }
            bitsLeftToRead = 0;
        } else
        {
            bitsLeftToRead -=8;
            return super.ensureBuffer();
        }
        return true;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE