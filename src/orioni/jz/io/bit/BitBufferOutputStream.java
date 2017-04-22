package orioni.jz.io.bit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * This {@link BitOutputStream} is designed to allow a caller to write to a buffer as well as track the number of bits
 * which have been written.  This is distinct from using a {@link ByteArrayOutputStream} as the underlying stream as
 * this extension is capable of tracking the exact number of bits which have been written to the stream.  Note that
 * closing the {@link BitBufferOutputStream} flushes the stream, thus causing the last bit to be padded.
 *
 * @author Zachary Palmer
 */
public class BitBufferOutputStream extends BitOutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param order  The {@link BitOrder} used to write the data.
     * @param format The {@link EndianFormat} in which the data is written.
     */
    public BitBufferOutputStream(BitOrder order, EndianFormat format)
    {
        super(new ByteArrayOutputStream(), order, format);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves a <code>byte[]</code> containing all of the data which has been written to this stream.  If the number
     * of bits written to this stream is not divisible by <code>8</code>, the extraneous bits in the last byte will be
     * padded with zeroes according to the {@link BitOrder} of this stream.
     *
     * @return The data which has been written to this stream.
     */
    public byte[] toByteArray()
    {
        if (remaining == 8)
        {
            return ((ByteArrayOutputStream) target).toByteArray();
        } else
        {
            byte[] wholeBytes = ((ByteArrayOutputStream) target).toByteArray();
            byte[] ret = new byte[wholeBytes.length + 1];
            System.arraycopy(wholeBytes, 0, ret, 0, wholeBytes.length);
            ret[ret.length - 1] = buffer;
            return ret;
        }
    }

    /**
     * Retrieves the number of bits which have been written to this stream.  If this value is greater than {@link
     * Integer#MAX_VALUE}, {@link Integer#MAX_VALUE} will be returned.
     *
     * @return The number of bits which have been written to this stream.
     */
    public int bitsWritten()
    {
        if (((ByteArrayOutputStream) target).size() > Integer.MAX_VALUE / 8)
        {
            return Integer.MAX_VALUE;
        } else
        {
            return ((ByteArrayOutputStream) target).size() * 8 + (8 - remaining);
        }
    }

    /**
     * Retrieves the number of bits which have been written to this stream as a <code>long</code>.
     *
     * @return The number of bits which have been written to this stream.
     */
    public long bitsWrittenLong()
    {
        return ((ByteArrayOutputStream) target).size() * 8 + (8 - remaining);
    }

    /**
     * Retrieves a {@link BitInputStream} which contains the data which has been stored in this stream so far.  A copy
     * of the buffer is made, allowing this stream to be used for further writes without affecting the returned stream.
     *
     * @return A {@link BitInputStream} which contains the bits written to this stream thus far.  If more than {@link
     *         Integer#MAX_VALUE} bits have been written to this stream, only the first {@link Integer#MAX_VALUE} bits
     *         will appear.
     */
    public BitInputStream getBufferStream()
    {
        return new BitLimitedInputStream(
                new ByteArrayInputStream(this.toByteArray()), this.getBitOrder(), this.getEndianFormat(),
                bitsWritten());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE