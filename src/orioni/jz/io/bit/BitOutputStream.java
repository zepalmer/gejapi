package orioni.jz.io.bit;

import orioni.jz.math.MathUtilities;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This wrapper {@link OutputStream} implementation allows the writing of whole bytes or individual bits to the target
 * {@link OutputStream}.  When an individual bit is written, successive bytes are written as the next eight bits.  The
 * performance of Java's binary arithmetic is poor; therefore, when bytes are written outside of the standard eight-bit
 * boundaries, this class will perform notably worse.
 * <p/>
 * When this class is flushed or closed, it must flush in terms of whole bytes.  If bits are partially written at this
 * time, the buffer will be filled out with zero bits.
 *
 * @author Zachary Palmer
 */
public class BitOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The target {@link OutputStream} for the values in this {@link OutputStream}.
     */
    protected OutputStream target;
    /**
     * The buffer for this output stream.
     */
    protected byte buffer;
    /**
     * The number of bits which have yet to be written to the buffer.
     */
    protected int remaining;
    /**
     * The {@link BitOrder} for this {@link BitOutputStream}.
     */
    protected BitOrder order;
    /**
     * The {@link EndianFormat} in which to write values.
     */
    protected EndianFormat format;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param target The target {@link java.io.OutputStream} for the values in this {@link java.io.OutputStream}.
     * @param order  The {@link BitOrder} in which bits are written.
     */
    public BitOutputStream(OutputStream target, BitOrder order, EndianFormat format)
    {
        super();
        this.target = target;
        remaining = 8;
        this.order = order;
        this.format = format;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Writes the specified number of bits of the given value to the buffer using the {@link BitOrder} of this stream.
     * If there is less space available in the buffer than the number of bits requested to be written, an exception is
     * thrown.
     *
     * @param value The value to write.
     * @param bits  The number of bits in that value to write.
     * @throws IndexOutOfBoundsException If the number of bits is greater than the space remaining in the buffer.
     */
    protected void writeBitsToBuffer(int value, int bits)
    {
        if (bits == 0) return;
        if (bits > remaining)
        {
            throw new IndexOutOfBoundsException(
                    "Internal buffer write error: tried to write " + bits + " bits, only had space for " + remaining);
        }
        value = MathUtilities.setRelevantBitCount(value, bits);
        switch (order)
        {
            case HIGHEST_BIT_FIRST:
                buffer |= value << (remaining - bits);
                break;
            case LOWEST_BIT_FIRST:
                buffer |= value << (8 - remaining);
                break;
            default:
                throw new IllegalStateException("Unrecognized bit order: " + order);
        }
        remaining -= bits;
    }

    /**
     * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte is
     * written to the output stream. The byte to be written is the eight low-order bits of the argument <code>b</code>.
     * The 24 high-order bits of <code>b</code> are ignored.
     *
     * @param b The <code>byte</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void write(int b)
            throws IOException
    {
        int tempRemaining = remaining;
        writeBitsToBuffer(b, tempRemaining);
        flush();
        writeBitsToBuffer(b >>> tempRemaining, 8 - tempRemaining);
    }

    /**
     * Writes the specified boolean value to the output stream.
     *
     * @param value The <code>boolean</code> to write to the stream: <code>true</code> indicates an on bit while
     *              <code>false</code> indicates an off bit.
     * @throws IOException If an I/O error occurs.
     */
    public void writeBit(boolean value)
            throws IOException
    {
        writeBitsToBuffer(value ? 1 : 0, 1);
        if (remaining == 0) flush();
    }

    /**
     * Writes a specified number of bits to the stream using the provided <code>int</code>.  The value is treated as an
     * unsigned <code>n</code>-bit big endian variable.  Note that if 32 bits are written, the <code>int</code> will be
     * treated as unsigned: the highest order bit will be assigned the value <code>2<sup>31</sup></code> and no
     * complementation will be performed on the other 31 bits.
     * <p/>
     * A write of <code>0</code> bits writes nothing.  Any bits above the specified number which exist in the
     * <code>int</code> are ignored.
     *
     * @param value The value to write.
     * @param bits  The number of bits to write.
     * @throws IOException              If an I/O error occurs while writing the data.
     * @throws IllegalArgumentException If the write is larger than <code>32</code> bits or less than <code>0</code>
     *                                  bits.
     */
    public void writeBits(int value, int bits)
            throws IOException
    {
        if (bits == 0) return;
        if ((bits < 0) || (bits > 32)) throw new IllegalArgumentException("Bit count out of range [0,32]");
        if (remaining < 1) flush();

        if (bits > remaining)
        {
            switch (format)
            {
                case BIG_ENDIAN:
                    while (bits > remaining)
                    {
                        bits -= remaining;
                        writeBitsToBuffer(value >>> bits, remaining);
                        flush();
                    }
                    break;
                case LITTLE_ENDIAN:
                    while (bits > remaining)
                    {
                        int tempBits = remaining;
                        bits -= tempBits;
                        writeBitsToBuffer(value, tempBits);
                        value >>>= tempBits;
                        flush();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unrecognized endian format: " + format);
            }
        }
        writeBitsToBuffer(value, bits);
        if (remaining == 0) flush();
    }

    /**
     * Writes a specified number bits to the stream using the provided <code>int</code>.  The value is treated as a
     * signed <code>n</code>-bit big endian variable.  A write of <code>0</code> bits writes nothing.  If the bit order
     * of this stream is {@link BitOrder#HIGHEST_BIT_FIRST}, the sign bit will be written before the rest of the data;
     * if the bit order of this stream is {@link BitOrder#LOWEST_BIT_FIRST}, the sign bit will be written after the rest
     * of the data.
     *
     * @param value The value to write.
     * @param bits  The number of bits to write.
     * @throws IOException              If an I/O error occurs while writing the data.
     * @throws IllegalArgumentException If the number of bits is outside of the range [0,32].
     */
    public void writeBitsSigned(int value, int bits)
            throws IOException, IllegalArgumentException
    {
        if (bits == 0) return;
        if ((bits < 0) || (bits > 32)) throw new IllegalArgumentException("Bit count out of range [0,32]");
        if (bits == 1)
        {
            writeBit((value & 0x1) != 0);
        } else
        {
            boolean signed = MathUtilities.getBit(value, bits - 1);
            switch (order)
            {
                case HIGHEST_BIT_FIRST:
                    writeBit(signed);
                    writeBits(value, bits - 1);
                    break;
                case LOWEST_BIT_FIRST:
                    writeBits(value, bits - 1);
                    writeBit(signed);
                    break;
                default:
                    throw new IllegalStateException("Unrecognized bit order: " + order);
            }
        }
    }

    /**
     * Writes the specified number of bits from the provided array.  The bits are encoded using the data provided in
     * this {@link BitOutputStream}'s constructor.  If the array contains additional bits, they will be ignored.  The
     * write starts at the beginning of the array.  If the array is not as large as the specified number of bits, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param data The <code>byte[]</code> containing the data.
     * @param bits The number of bits to write.
     * @throws IOException              If an I/O error occurs while writing to the stream.
     * @throws IllegalArgumentException If the <code>byte[]</code> is not large enough to hold <code>bits</code> bits.
     */
    public void writeBitsFromArray(byte[] data, int bits)
            throws IOException, IllegalArgumentException
    {
        this.write(data, 0, bits / 8);
        if (bits % 8 != 0)
        {
            byte last = data[data.length - 1];
            switch (order)
            {
                case HIGHEST_BIT_FIRST:
                    last >>>= (8 - bits % 8);
                    break;
                case LOWEST_BIT_FIRST:
                    break;
                default:
                    throw new IllegalStateException("Unrecognized bit order: " + order);
            }
            this.writeBits(last, bits % 8);
        }
    }

    /**
     * Retrieves the bit order of this {@link BitOutputStream}.
     *
     * @return The bit order of this {@link BitOutputStream}.
     */
    public BitOrder getBitOrder()
    {
        return order;
    }

    /**
     * Retrieves the endian format of this {@link BitOutputStream}.
     *
     * @return The endian format of this {@link BitOutputStream}.
     */
    public EndianFormat getEndianFormat()
    {
        return format;
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out.  If a byte has been partially
     * written, the bits which were written are considered high order and the low order bits are padded with zeroes.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void flush()
            throws IOException
    {
        if (remaining < 8)
        {
            target.write(buffer);
            remaining = 8;
            buffer = 0;
        }
    }

    /**
     * Flushes and closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        this.flush();
        target.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}