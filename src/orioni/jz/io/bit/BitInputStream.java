package orioni.jz.io.bit;

import orioni.jz.math.MathUtilities;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This wrapper {@link InputStream} implementation allows the reading of whole bytes or individual bits from the stream.
 * If individual bits are read from the stream, the next byte read from the stream will be constructed of the next eight
 * bits; no bits are skipped or filled.
 * <p/>
 * An exception to this occurs when the {@link BitInputStream} reaches the end of its source stream.  In this case, the
 * right side of the last byte read is padded with zeroes.  This padding can be avoided if {@link
 * BitInputStream#readBit()} is called a number of times evenly divisible by eight.
 * <p/>
 * Note that, because Java has inefficient binary arithmetic operations, the performance of this class will drop
 * severely if bytes are read from a nonstandard byte boundary.
 *
 * @author Zachary Palmer
 */
public class BitInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link InputStream} from which this {@link BitInputStream} gets its data.
     */
    protected InputStream source;
    /**
     * The current byte in buffer, if any.
     */
    protected byte buffer;
    /**
     * The number of bits currently available in the buffer.
     */
    protected byte bitsLeft;
    /**
     * The bit order for this {@link BitInputStream}.
     */
    protected BitOrder order;
    /**
     * The {@link EndianFormat} in which the bits should be read.
     */
    protected EndianFormat format;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param source The {@link java.io.InputStream} from which this {@link BitInputStream} gets its data.
     * @param order  The order in which the bits are read.
     * @param format The {@link EndianFormat} specifying how multi-byte values are read.
     */
    public BitInputStream(InputStream source, BitOrder order, EndianFormat format)
    {
        super();
        this.source = source;
        bitsLeft = 0;
        this.order = order;
        this.format = format;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Attempts to ensure that there is at least one bit in the buffer.  If this method returns <code>false</code>, the
     * end of the stream has been reached.
     *
     * @return <code>true</code> if the buffer has at least one bit in it; <code>false</code> if it does not because the
     *         end of the stream has been reached.
     * @throws IOException If an I/O error occurs while refilling the buffer.
     */
    protected boolean ensureBuffer()
            throws IOException
    {
        if (bitsLeft > 0) return true;
        int data = source.read();
        if (data == -1) return false;

        bitsLeft = 8;
        buffer = (byte) (data);
        return true;
    }

    /**
     * Attempts to retrieve the provided number of bits from the buffer.  If more bits are requested than are available,
     * an {@link IndexOutOfBoundsException} is thrown.
     *
     * @param bits The number of bits to retrieve.
     * @return The bits from the buffer, as a big endian value.
     */
    protected int getBitsFromBuffer(int bits)
    {
        if (bits > bitsLeft)
        {
            throw new IndexOutOfBoundsException(
                    "Internal buffer error: requested " + bits + " bits, only had " +
                    bitsLeft);
        }
        int ret;
        switch (order)
        {
            case HIGHEST_BIT_FIRST:
                ret = buffer >>> (bitsLeft - bits);
                break;
            case LOWEST_BIT_FIRST:
                ret = buffer >>> (8 - bitsLeft);
                break;
            default:
                throw new IllegalStateException("Unrecognized bit order: " + order);
        }
        bitsLeft -= bits;
        return MathUtilities.setRelevantBitCount(ret, bits);
    }

    /**
     * Finds the byte boundary.  This method will discard any partially read byte currently held in this {@link
     * BitInputStream}'s buffer.  If the stream is already on a byte boundary, this method does nothing.
     */
    public void findByteBoundary()
    {
        if (bitsLeft < 8)
        {
            bitsLeft = 0;
        }
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available because the end of the stream has been reached, the
     * value <code>-1</code> is returned. This method blocks until input data is available, the end of the stream is
     * detected, or an exception is thrown.
     *
     * @return The next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException If an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        if (bitsLeft == 0)
        {
            if (!ensureBuffer()) return -1;
        }
        if (bitsLeft == 8)
        {
            bitsLeft = 0;
            return buffer & 0xFF;
        }
        int tempBits = bitsLeft;
        int ret = getBitsFromBuffer(tempBits);
        if (!ensureBuffer()) return -1;
        ret |= getBitsFromBuffer(8 - tempBits) << tempBits;
        return ret;
    }

    /**
     * Reads the next bit in the stream.
     *
     * @return The next bit of data: <code>1</code> for an on bit, <code>0</code> for an off bit, and <code>-1</code>
     *         for end of stream.
     * @throws IOException If an I/O error occurs.
     */
    public byte readBit()
            throws IOException
    {
        if (!ensureBuffer()) return -1;
        return (byte) (getBitsFromBuffer(1));
    }

    /**
     * Reads a specified number of bits in the stream and returns them as an <code>int</code>.  The value is treated as
     * an unsigned <code>n</code>-bit big endian variable.  Note that if 32 bits are read and the value is larger than
     * {@link Integer#MAX_VALUE}, rollover will occur.  A read of <code>0</code> bits returns the integer value
     * <code>0</code>.
     *
     * @param bits The number of bits to read.
     * @return An <code>int</code> containing those bits, padded with zeroes.
     * @throws IOException              If an I/O error occurs while reading the data.
     * @throws EOFException             If the end of stream is reached before obtaining that many bits.
     * @throws IllegalArgumentException If the read is larger than <code>32</code> bits or less than <code>0</code>
     *                                  bits.
     */
    public int readBits(int bits)
            throws IOException
    {
        if (bits == 0) return 0;
        if ((bits < 0) || (bits > 32)) throw new IllegalArgumentException("Bit count out of range [0,32]");
        int ret = 0;
        if (!ensureBuffer()) throw new EOFException("Unexpected end of stream.");
        int shiftBits = 0;
        if (bits > bitsLeft)
        {
            switch (format)
            {
                case BIG_ENDIAN:
                    while (bits > bitsLeft)
                    {
                        bits -= bitsLeft;
                        ret <<= bitsLeft;
                        ret |= getBitsFromBuffer(bitsLeft);
                        if (bits > 0)
                        {
                            if (!ensureBuffer()) throw new EOFException("Unexpected end of stream.");
                        }
                    }
                    ret <<= bits;
                    break;
                case LITTLE_ENDIAN:
                    while (bits > bitsLeft)
                    {
                        bits -= bitsLeft;
                        int tempBits = bitsLeft;
                        ret |= getBitsFromBuffer(bitsLeft) << shiftBits;
                        shiftBits += tempBits;
                        if (bits > 0)
                        {
                            if (!ensureBuffer()) throw new EOFException("Unexpected end of stream.");
                        }
                    }
                    break;
                default:
                    throw new IllegalStateException("Unrecognized endian format: " + format);
            }
        }
        ret |= getBitsFromBuffer(bits) << shiftBits;
        return ret;
    }

    /**
     * Reads a specified number of bits in the stream and returns them as an <code>int</code>.  The value is treated as
     * a signed <code>n</code>-bit big endian variable.  A read of <code>0</code> or <code>1</code> bits returns the
     * integer value <code>0</code>.  (<code>1</code> bit reads return zero because they indicate either positive or
     * negative <code>0</code>.)  If the current byte order is {@link BitOrder#HIGHEST_BIT_FIRST}, the sign bit of the
     * number is assumed to come before the value itself; if the current byte order is {@link
     * BitOrder#LOWEST_BIT_FIRST}, the sign bit of the number is assumed to come after the value.
     *
     * @param bits The number of bits to read.
     * @return An <code>int</code> containing those bits, padded with the first read bit.
     * @throws IOException              If an I/O error occurs while reading the data.
     * @throws EOFException             If the end of stream is reached before obtaining that many bits.
     * @throws IllegalArgumentException If the read is larger than <code>32</code> bits or less than <code>0</code>
     *                                  bits.
     */
    public int readBitsSigned(int bits)
            throws IOException
    {
        if ((bits < 0) || (bits > 32)) throw new IllegalArgumentException("Bit count out of range [0,32]");
        if (bits == 0) return 0;
        if (bits == 1)
        {
            if (readBoolean())
            {
                return -1;
            } else
            {
                return 0;
            }
        }
        int ret;
        boolean sign;
        switch (order)
        {
            case HIGHEST_BIT_FIRST:
                sign = readBoolean();
                bits--;
                ret = readBits(bits);
                break;
            case LOWEST_BIT_FIRST:
                bits--;
                ret = readBits(bits);
                sign = readBoolean();
                break;
            default:
                throw new IllegalStateException("Unrecognized bit order: " + order);
        }
        if (sign)
        {
            return ret | (~MathUtilities.setRelevantBitCount(0xFFFFFFFF, bits));
        } else
        {
            return ret;
        }
    }

    /**
     * Reads the next <code>bits</code> bits and stores them in an array, which is then returned.  The bits are stored
     * using the encoding specified in this {@link BitInputStream}'s constructor.  If the number of bits requested is
     * not divisible by <code>8</code>, the extraneous bits will be padded with zeroes.
     *
     * @param bits The number of bits to read.
     * @return An array of size <code>(bits+7)/8</code> containing the requested bits.
     * @throws EOFException             If the stream is exhausted before the requested number of bits can be read.
     * @throws IOException              If an I/O error occurs while reading the data.
     * @throws IllegalArgumentException If <code>bits</code> is less than zero.
     */
    public byte[] readBitsAsArray(int bits)
            throws EOFException, IOException, IllegalArgumentException
    {
        if (bits < 0) throw new IllegalArgumentException("Bits must be at least 0 (was " + bits + ")");
        byte[] ret = new byte[(bits + 7) / 8];
        if (bits % 8 == 0)
        {
            for (int i = 0; i < ret.length; i++) ret[i] = (byte) (readBits(8));
        } else
        {
            for (int i = 0; i < ret.length - 1; i++) ret[i] = (byte) (readBits(8));
            ret[ret.length - 1] = (byte) (readBits(bits % 8));
            switch (order)
            {
                case HIGHEST_BIT_FIRST:
                    ret[ret.length - 1] <<= 8 - bits;
                    break;
                case LOWEST_BIT_FIRST:
                    break;
                default:
                    throw new IllegalStateException("Unrecognized bit order: " + order);
            }
        }
        return ret;
    }

    /**
     * Obtains a {@link BitInputStream} which contains the next <code>bits</code> bits from this {@link BitInputStream}.
     * This method returns the same result as the expression <ul><code>new BitLimitedInputStream(new
     * ByteArrayInputStream(bis.readBitsAsArray(bits)), bis.getBitOrder(), bis.getEndianFormat(), bits)</code></ul>
     * (assuming that this stream is <code>bis</code>) and is provided solely for convenience.
     *
     * @param bits The number of bits to buffer.
     * @return The {@link BitInputStream} which buffers that data.
     * @throws EOFException             If this stream is exhausted before reading <code>bits</code> bits.
     * @throws IOException              If an I/O error occurs while buffering the data.
     * @throws IllegalArgumentException If <code>bits</code> is less than zero.
     */
    public BitInputStream getBufferedStream(int bits)
            throws EOFException, IOException, IllegalArgumentException
    {
        return new BitLimitedInputStream(
                new ByteArrayInputStream(this.readBitsAsArray(bits)), this.getBitOrder(), this.getEndianFormat(), bits);
    }

    /**
     * Reads the next bit in the stream as a <code>boolean</code>.  If the end of the stream is reached, an {@link
     * IllegalStateException} is thrown.
     *
     * @return The next bit in the stream, as a <code>boolean</code>.
     * @throws IOException  If an I/O error occurs.
     * @throws EOFException If the end of the stream is reached.
     */
    public boolean readBoolean()
            throws IOException, EOFException
    {
        switch (readBit())
        {
            case 1:
                return true;
            case 0:
                return false;
            case -1:
                throw new EOFException("End of stream.");
        }
        throw new IllegalStateException("Invalid response from readBit()");
    }

    /**
     * Retrieves the bit order of this {@link BitInputStream}.
     *
     * @return The bit order of this {@link BitInputStream}.
     */
    public BitOrder getBitOrder()
    {
        return order;
    }

    /**
     * Retrieves the endian format of this {@link BitInputStream}.
     *
     * @return The endian format of this {@link BitInputStream}.
     */
    public EndianFormat getEndianFormat()
    {
        return format;
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the
     * next caller of a method for this input stream.
     *
     * @return The number of bytes that can be read from this input stream without blocking.  Note that an additional
     *         number of bits less than eight may also be available.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int available()
            throws IOException
    {
        return (source.available() + bitsLeft / 8);
    }

    /**
     * Returns the number of available bits that can be read (or skipped over) from this input stream without blocking
     * by the next caller of a method for this input stream.  If the actual number of bits which can be read is greater
     * than {@link Integer#MAX_VALUE}, {@link Integer#MAX_VALUE} is returned.
     *
     * @return The number of bits that can be read from this input stream without blocking.
     * @throws IOException If an I/O error occurs.
     */
    public int availableBits()
            throws IOException
    {
        int bytesAvailable = source.available() + bitsLeft / 8;
        if (bytesAvailable > Integer.MAX_VALUE / 8)
        {
            return Integer.MAX_VALUE;
        } else
        {
            return bytesAvailable * 8 + bitsLeft % 8;
        }
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