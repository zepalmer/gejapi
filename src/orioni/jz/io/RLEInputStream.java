package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class decodes a stream of input data from RLE format to uncompressed bytes.  The compressed RLE format is
 * discussed in the counterpart to this class, {@link RLEOutputStream}.  This stream may buffer up to 8K of compressed
 * data from the underlying stream.
 *
 * @author Zachary Palmer
 */
public class RLEInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The current data for this stream.
     */
    protected byte data;
    /**
     * The number of times this stream's data can be repeated before more underlying reads must be performed.
     */
    protected int count;
    /**
     * The signal code for this stream.
     */
    protected byte signal;
    /**
     * The underlying InputStream providing this stream with RLE data.
     */
    protected InputStream source;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a signal code of <code>0xE2</code>.
     *
     * @param source The source stream from which RLE data will be read.
     * @see RLEOutputStream#RLEOutputStream(java.io.OutputStream)
     */
    public RLEInputStream(InputStream source)
    {
        this(source, (byte) 0xE2);
    }

    /**
     * Full constructor.
     *
     * @param source The source stream from which RLE data will be read.
     * @param signal The signal code for this stream.
     */
    public RLEInputStream(InputStream source, byte signal)
    {
        this.source = source;
        this.signal = signal;
        data = 0;
        count = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the next byte of uncompressed data.  This may require underlying reads.
     *
     * @return The next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException If an I/O error occurs in the underlying stream or if the RLE format is bad.
     */
    public int read()
            throws IOException
    {
        // If there is compressed data waiting, send out a byte of it.
        if (count > 0)
        {
            count--;
            return data;
        }
        // Read the next byte and react appropriately.
        int readData = source.read();
        if (readData == -1) return -1;
        if (readData != (signal & 0xFF)) return readData;
        // A signal occurred.  Here we go...
        int compressedBlockIndicator = source.read();
        if (compressedBlockIndicator == -1)
        {
            throw new IOException("RLE format error: unexpected end of stream reading compressed block indicator.");
        }
        if (compressedBlockIndicator == 0xFF) return signal;
        // This is a compressed block.  Calculate the size.
        int trailingBytes = compressedBlockIndicator >>> 6;
        count = compressedBlockIndicator & 0x3F;
        while (trailingBytes > 0)
        {
            readData = source.read();
            if (readData == -1)
            {
                throw new IOException(
                        "RLE format error: unexpected end of stream reading compressed block size variable.");
            }
            count *= 256;
            count += readData;
            trailingBytes--;
        }
        // Now get the return data
        readData = source.read();
        if (readData == -1)
        {
            throw new IOException("RLE format error: unexpected end of stream reading data value.");
        }
        data = (byte) readData;

        // Return a byte to the caller.
        count--;
        return data;
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

// END OF FILE //