package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class encodes the data written to it in an RLE format.  The format of the stream is structured to contain a
 * series of uncompressed bytes interspersed with compression blocks.  Compression blocks are signalled by a signal code
 * such as <code>0xFF</code>, which is specified on construction.  Following a signal code is either the value
 * <code>0xFF</code>, indicating that the signal code should be treated as a normal byte of data, or a compressed block
 * indicator (not <code>0xFF</code>), indicating that the following section of data represents RLE data.
 * <p/>
 * The first two bits of the compressed block indicator indicate the length of the compressed block size variable.  If
 * the value is <code>00</code>, the length of the compressed block size variable is 6 bits (which are stored in the
 * rest of the compressed block indicator).  If the value is <code>01</code> or <code>10</code>, the next one or two
 * bytes of data (respectively) are considered part of the compressed block size variable.  For example, the data
 * <code>01000001b 1000000b</code> indicates that the compressed block size variable is 14 bits long (and equal to
 * <code>00000110000000b</code>).  Note that this allows for compressed blocks of up to four megabytes.  Larger RLE
 * blocks will be split into RLE blocks of four megabytes or smaller.
 * <p/>
 * Once the compressed block size variable has been established, the next byte in the stream is the RLE character. In
 * uncompressed form, the RLE character appears a number of times equal to the compressed block size variable.
 * <p/>
 * Note that the signal code can appear as an RLE character.  This is legal, since the reading of the result of this
 * process should be in a different state when reading a compressed block.
 *
 * @author Zachary Palmer
 */
public class RLEOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The byte that was last written to this stream.
     */
    protected byte last;
    /**
     * The number of times that this byte has been written.
     */
    protected int count;
    /**
     * The underlying <code>OutputStream</code> to which RLE-compressed data will be written.
     */
    protected OutputStream target;
    /**
     * The signal code for this stream.
     */
    protected byte signal;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a signal code of <code>0xE2</code>.  This is obscure enough to be useful for most
     * purposes.
     *
     * @param target The <code>OutputStream</code> to which encoded data will be written.
     */
    public RLEOutputStream(OutputStream target)
    {
        this(target, (byte) 0xE2);
    }

    /**
     * Full constructor.
     *
     * @param target The <code>OutputStream</code> to which encoded data will be written.
     * @param signal The signal code to use for compressed blocks.  The value of this byte should be one that will not
     *               frequently be written to the output stream for best performance.
     */
    public RLEOutputStream(OutputStream target, byte signal)
    {
        super();
        last = 0;
        count = 0;
        this.target = target;
        this.signal = signal;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Encodes the lower 8 bits of the specified integer into RLE for this stream.  This method may or may not write to
     * the underlying output stream, depending upon what results have become available.
     *
     * @param b The <code>byte</code> of data to encode.
     * @throws IOException If an I/O error occurs. In particular, an <code>IOException</code> may be thrown if this
     *                     output stream or the underlying output stream has been closed.
     */
    public void write(int b)
            throws IOException
    {
        byte data = (byte) b;
        if (data == last)
        {
            count++;
            // As long as we don't have to force an RLE write, wait for the next byte.
            if (count < 4194303) return;
        }

        performRLEWrite();
        last = data;
        count = 1;
    }

    /**
     * This method writes the contents of the buffer to disk in the most efficient fashion.  Calling this method before
     * the last write may impair compression quality.
     *
     * @throws IOException If an I/O error occurs on the underlying stream.
     * @see OutputStream#flush()
     */
    public void flush()
            throws IOException
    {
        performRLEWrite();
        last = 0;
        count = 0;
    }

    /**
     * This method is used to write RLE-formatted data to the underlying stream.
     *
     * @throws IOException If the underlying stream encounters an I/O error.
     */
    protected void performRLEWrite()
            throws IOException
    {
        if (count == 1)
        {
            if (last == signal)
            {
                // If it's the signal, we have to write an extra byte
                target.write(signal);
                target.write(0xFF);
            } else
            {
                target.write(last);
            }
        } else if ((count == 2) && (last != signal))
        {
            // It would not be efficient to write the signal byte as data twice in this manner.
            target.write(last);
            target.write(last);
        } else if (count >= 3)
        {
            // Write a compressed block.
            target.write(signal);
            // Write length
            if (count < 64) // 6-bit length code
            {
                target.write(count);
            } else if (count < 8192) // 14-bit length code
            {
                target.write(0x40 | count >>> 8);
                target.write(count);
            } else // 22-bit length code
            {
                target.write(0x80 | count >>> 16);
                target.write(count >>> 8);
                target.write(count);
            }
            // Write data
            target.write(last);
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
        flush();
        target.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //