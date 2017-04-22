package orioni.jz.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * This class is designed to allow the reading of primitive values from an input stream.  This is accomplished by
 * reading units of data from the input stream and converting them to Java primitives, which are then returned.  The
 * endian form of the data is specified on construction but can be overridden on each method call.
 * <p/>
 * This class is similar to {@link java.io.DataInputStream} except in that it allows for streams to be read in either
 * endian form.
 *
 * @author Zachary Palmer
 */
public class PrimitiveInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The constant representing big-endian format.  This constant is guaranteed to be equal to {@link
     * PrimitiveOutputStream#BIG_ENDIAN}.
     */
    public static final boolean BIG_ENDIAN = true;
    /**
     * The constant representing little-endian format.  This constant is guaranteed to be equal to {@link
     * PrimitiveOutputStream#LITTLE_ENDIAN}.
     */
    public static final boolean LITTLE_ENDIAN = false;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link InputStream}.
     */
    protected InputStream inputStream;
    /**
     * The default endian format.
     */
    protected boolean bigEndian;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param inputStream The underlying input stream from which data should be read.
     * @param bigEndian   Whether or not the underlying input stream should be read in big endian form by default.  You
     *                    may also use one of the <code>XXX_ENDIAN</code> constants on this class.
     */
    public PrimitiveInputStream(InputStream inputStream, boolean bigEndian)
    {
        super();
        this.inputStream = inputStream;
        this.bigEndian = bigEndian;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available because the end of the stream has been reached, the
     * value <code>-1</code> is returned. This method blocks until input data is available, the end of the stream is
     * detected, or an exception is thrown.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        return inputStream.read();
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into an array of bytes.  An attempt is made to
     * read as many as <code>len</code> bytes, but a smaller number may be read. The number of bytes actually read is
     * returned as an integer. This method blocks until input data is available, end of file is detected, or an
     * exception is thrown.
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
        return inputStream.read(b, off, len);
    }

    /**
     * Reads the specified array "fully."  This method will continue to perform reads until either the entire array is
     * filled or the stream is exhausted.
     *
     * @param buf The buffer into which to read data.
     * @throws EOFException If the stream is exhausted before the read is complete.
     * @throws IOException  If an I/O error occurs.
     */
    public void readFully(byte[] buf)
            throws EOFException, IOException
    {
        readFully(buf, 0, buf.length);
    }

    /**
     * Reads the specified part of the array "fully."  This method will continue to perform reads until either the
     * entire array segment is filled or the stream is exhausted.
     *
     * @param buf The buffer into which to read data.
     * @param off The first byte in the array to fill.
     * @param len The number of bytes in the array to fill.
     * @throws EOFException              If the stream is exhausted before the read is complete.
     * @throws IOException               If an I/O error occurs.
     * @throws IndexOutOfBoundsException If <code>off+len&gt;buf.length</code>.
     */
    public void readFully(byte[] buf, int off, int len)
            throws EOFException, IOException
    {
        while (len > 0)
        {
            int read = read(buf, off, len);
            if (read == -1) throw new EOFException("Unexpected end of stream.");
            len -= read;
            off += read;
        }
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input stream. The <code>skip</code> method may,
     * for a variety of reasons, end up skipping over some smaller number of bytes, possibly <code>0</code>.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException if an I/O error occurs.
     */
    public long skip(long n)
            throws IOException
    {
        return inputStream.skip(n);
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from this input stream in total.  The <code>skipFully</code>
     * method will <b>not</b> skip over a smaller number of bytes.
     *
     * @param n The number of bytes to be skipped.
     * @throws IOException if an I/O error occurs.
     */
    public void skipFully(long n)
            throws IOException
    {
        while (n > 0)
        {
            n -= skip(n);
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
        inputStream.close();
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
        return inputStream.available();
    }

    /**
     * Marks the current position in this input stream. A subsequent call to the <code>reset</code> method repositions
     * this stream at the last marked position so that subsequent reads re-read the same bytes.
     *
     * @param readlimit the maximum limit of bytes that can be read before the mark position becomes invalid.
     * @see InputStream#reset()
     */
    public synchronized void mark(int readlimit)
    {
        inputStream.mark(readlimit);
    }

    /**
     * Tests if this input stream supports the <code>mark</code> and <code>reset</code> methods. Whether or not
     * <code>mark</code> and <code>reset</code> are supported is an invariant property of a particular input stream
     * instance.
     *
     * @return <code>true</code> if this stream instance supports the mark and reset methods; <code>false</code>
     *         otherwise.
     * @see InputStream#mark(int)
     * @see InputStream#reset()
     */
    public boolean markSupported()
    {
        return inputStream.markSupported();
    }

    /**
     * Repositions this stream to the position at the time the <code>mark</code> method was last called on this input
     * stream.
     *
     * @throws IOException if this stream has not been marked or if the mark has been invalidated.
     * @see InputStream#mark(int)
     * @see IOException
     */
    public synchronized void reset()
            throws IOException
    {
        inputStream.reset();
    }

    /**
     * Reads a signed <code>byte</code> from the underlying stream.
     *
     * @return The <code>byte</code> to return.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public byte readByte()
            throws IOException
    {
        int n = read();
        if (n == -1) throw new EOFException("Not enough data in the stream to perform read.");
        return (byte) n;
    }

    /**
     * Reads an unsigned <code>byte</code> from the underlying stream.  The distinct difference between this method and
     * the {@link PrimitiveInputStream#read()} method is that this method throws an {@link EOFException} if the stream
     * is exhausted instead of returning <code>-1</code>.
     *
     * @return The <code>byte</code> to return.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public int readUnsignedByte()
            throws IOException
    {
        int n = read();
        if (n == -1) throw new EOFException("Not enough data in the stream to perform read.");
        return n;
    }

    /**
     * Reads a <code>short</code> from the underlying stream in the default endian format for this stream.
     *
     * @return The <code>short</code> which was read.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public short readShort()
            throws IOException
    {
        return readShort(bigEndian);
    }

    /**
     * Reads a short from the underlying stream in the specified endian format.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>short</code> which was read.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public short readShort(boolean bigEndian)
            throws IOException
    {
        // avoid the use of arrays
        int first = read();
        int second = read();
        if ((first == -1) || (second == -1)) throw new EOFException("Not enough data in the stream to perform read.");
        if (bigEndian)
        {
            return (short) (((first & 0xFF) << 8) | (second & 0xFF));
        } else
        {
            return (short) (((second & 0xFF) << 8) | (first & 0xFF));
        }
    }

    /**
     * Reads a <code>char</code> from the underlying stream in the default endian format for this stream.
     *
     * @return The <code>char</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public char readChar()
            throws IOException
    {
        return readChar(bigEndian);
    }

    /**
     * Reads a <code>char<code> from the underlying stream in the specified endian format.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>char</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public char readChar(boolean bigEndian)
            throws IOException
    {
        return (char) readShort(bigEndian);
    }

    /**
     * Reads a <code>int</code> from the underlying stream in the default endian format for this stream.
     *
     * @return The <code>int</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public int readInt()
            throws IOException
    {
        return readInt(bigEndian);
    }

    /**
     * Reads a <code>int<code> from the underlying stream in the specified endian format.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>int</code> which was read.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public int readInt(boolean bigEndian)
            throws IOException
    {
        int first = read();
        int second = read();
        int third = read();
        int fourth = read();
        if ((first == -1) || (second == -1) || (third == -1) || (fourth == -1))
        {
            throw new EOFException("Not enough data in the stream to perform read.");
        }
        if (bigEndian)
        {
            return ((first & 0xFF) << 24) | ((second & 0xFF) << 16) | ((third & 0xFF) << 8) | (fourth & 0xFF);
        } else
        {
            return ((fourth & 0xFF) << 24) | ((third & 0xFF) << 16) | ((second & 0xFF) << 8) | (first & 0xFF);
        }
    }

    /**
     * Reads a <code>long</code> from the underlying stream in the default endian format for this stream.
     *
     * @return The <code>long</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public long readLong()
            throws IOException
    {
        return readLong(bigEndian);
    }

    /**
     * Reads a <code>long<code> from the underlying stream in the specified endian format.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>long</code> which was read.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public long readLong(boolean bigEndian)
            throws IOException
    {
        int first = read();
        int second = read();
        int third = read();
        int fourth = read();
        int fifth = read();
        int sixth = read();
        int seventh = read();
        int eighth = read();
        if ((first == -1) || (second == -1) || (third == -1) || (fourth == -1) || (fifth == -1) || (sixth == -1) ||
            (seventh == -1) || (eighth == -1))
        {
            throw new EOFException("Not enough data in the stream to perform read.");
        }
        if (bigEndian)
        {
            return ((first & 0xFFL) << 56) | ((second & 0xFFL) << 48) | ((third & 0xFFL) << 40) |
                   ((fourth & 0xFFL) << 32) | ((fifth & 0xFFL) << 24) | ((sixth & 0xFF) << 16) |
                   ((seventh & 0xFF) << 8) | (eighth & 0xFF);
        } else
        {
            return ((eighth & 0xFFL) << 56) | ((seventh & 0xFFL) << 48) | ((sixth & 0xFFL) << 40) |
                   ((fifth & 0xFFL) << 32) | ((fourth & 0xFF) << 24) | ((third & 0xFF) << 16) | ((second & 0xFF) << 8) |
                   (first & 0xFF);
        }
    }

    /**
     * Reads a <code>boolean</code> from the underlying stream.
     *
     * @return The <code>boolean</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public boolean readBoolean()
            throws IOException
    {
        return (read() != 0);
    }

    /**
     * Reads a <code>double</code> from the underlying stream in the default endian format for this stream.
     *
     * @return The <code>double</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public double readDouble()
            throws IOException
    {
        return readDouble(bigEndian);
    }

    /**
     * Reads a <code>double<code> from the underlying stream in the specified endian format.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>double</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public double readDouble(boolean bigEndian)
            throws IOException
    {
        long x = readLong(bigEndian);
        return Double.longBitsToDouble(x);
    }

    /**
     * Reads a <code>float</code> from the underlying stream in the default endian format for this stream.
     *
     * @return The <code>float</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public float readFloat()
            throws IOException
    {
        return readFloat(bigEndian);
    }

    /**
     * Reads a <code>float<code> from the underlying stream in the specified endian format.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>float</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public float readFloat(boolean bigEndian)
            throws IOException
    {
        int x = readInt(bigEndian);
        return Float.intBitsToFloat(x);
    }

    /**
     * Reads a <code>short</code> from the underlying stream in the default endian format for this stream.  This byte is
     * then placed in an <code>int</code> variable as an unsigned value.
     *
     * @return The <code>byte</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public int readUnsignedShort()
            throws IOException
    {
        return readUnsignedShort(bigEndian);
    }

    /**
     * Reads a <code>short</code> from the underlying stream in the specified endian format.  This byte is then placed
     * in an <code>int</code> variable as an unsigned value.
     *
     * @param bigEndian The endian format in which to read.
     * @return The <code>byte</code> which was read.
     * @throws IOException If an I/O error occurs.
     */
    public int readUnsignedShort(boolean bigEndian)
            throws IOException
    {
        short s = readShort(bigEndian);
        return (s & 0xFFFF);
    }

    /**
     * Reads the specified number of bytes as an <code>int</code> using the default endian format for this stream.  The
     * higher order bytes which are not read are treated as zeroes.
     *
     * @param bytes The number of bytes to read.
     * @return The <code>int</code> contianing the read data.
     * @throws IOException If an I/O error occurs.
     */
    public int readUnsignedInteger(int bytes)
            throws IOException
    {
        return readUnsignedInteger(bytes, bigEndian);
    }

    /**
     * Reads the specified number of bytes as an <code>int</code> using the specified endian format.  The higher order
     * bytes which are not read are treated as zeroes.
     *
     * @param bytes     The number of bytes to read.
     * @param bigEndian The endian format in which to read.
     * @return The <code>int</code> contianing the read data.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public int readUnsignedInteger(int bytes, boolean bigEndian)
            throws IOException
    {
        byte[] buffer = new byte[bytes];
        int bufferRead = inputStream.read(buffer);
        if (bufferRead < buffer.length) throw new EOFException("Not enough data in the stream to perform read.");
        if (bigEndian)
        {
            switch (bytes)
            {
                case 0:
                    return 0;
                case 1:
                    return buffer[0] & 0xFF;
                case 2:
                    return (buffer[0] & 0xFF) << 8 | (buffer[1] & 0xFF);
                case 3:
                    return (buffer[0] & 0xFF) << 16 | (buffer[1] & 0xFF) << 8 | (buffer[2] & 0xFF);
                default:
                    return (buffer[buffer.length - 4] & 0xFF) << 24 | (buffer[buffer.length - 3] & 0xFF) << 16 |
                           (buffer[buffer.length - 2] & 0xFF) << 8 |
                           (buffer[buffer.length - 1] & 0xFF);
            }
        } else
        {
            switch (bytes)
            {
                case 0:
                    return 0;
                case 1:
                    return buffer[0] & 0xFF;
                case 2:
                    return (buffer[1] & 0xFF) << 8 | (buffer[0] & 0xFF);
                case 3:
                    return (buffer[2] & 0xFF) << 16 | (buffer[1] & 0xFF) << 8 | buffer[0] & 0xFF;
                default:
                    return (buffer[3] & 0xFF) << 24 | (buffer[2] & 0xFF) << 16 | (buffer[1] & 0xFF) << 8 |
                           (buffer[0] & 0xFF);
            }
        }
    }

    /**
     * Reads the specified number of bytes as an <code>long</code> using the default endian format for this stream.  The
     * higher order bytes which are not read are treated as zeroes.
     *
     * @param bytes The number of bytes to read.
     * @return The <code>long</code> contianing the read data.
     * @throws IOException If an I/O error occurs.
     */
    public long readUnsignedLongInteger(int bytes)
            throws IOException
    {
        return readUnsignedLongInteger(bytes, bigEndian);
    }

    /**
     * Reads the specified number of bytes as an <code>long</code> using the specified endian format.  The higher order
     * bytes which are not read are treated as zeroes.
     *
     * @param bytes     The number of bytes to read.
     * @param bigEndian The endian format in which to read.
     * @return The <code>long</code> contianing the read data.
     * @throws EOFException If the stream is exhausted before the read is completed.
     * @throws IOException  If an I/O error occurs.
     */
    public long readUnsignedLongInteger(int bytes, boolean bigEndian)
            throws IOException
    {
        long ret = 0;
        byte[] buffer = new byte[bytes];
        int bufferRead = inputStream.read(buffer);
        if (bufferRead < buffer.length) throw new EOFException("Not enough data in the stream to perform read.");
        int bufferIndex = 0;
        if (bigEndian)
        {
            while (bytes > 0)
            {
                ret <<= 8;
                ret |= (buffer[bufferIndex++] & 0xFF);
                bytes--;
            }
        } else
        {
            int bits = 0;
            while ((bytes > 0) && (bits < 64))
            {
                ret |= ((buffer[bufferIndex++] & 0xFF) << bits);
                bits += 8;
                bytes--;
            }
        }
        return ret;
    }

    /**
     * Reads the specified number of bytes as an <code>int</code> using the default endian format for this stream.  The
     * higher order bytes which are not read are treated as zeroes.
     *
     * @param bytes The number of bytes to read.
     * @return The <code>int</code> contianing the read data.
     * @throws IOException If an I/O error occurs.
     */
    public int readSignedInteger(int bytes)
            throws IOException
    {
        return readSignedInteger(bytes, bigEndian);
    }

    /**
     * Reads the specified number of bytes as an <code>int</code> using the specified endian format.  The higher order
     * bytes which are not read are filled with the highest bit of the highest read byte.
     *
     * @param bytes     The number of bytes to read.
     * @param bigEndian The endian format in which to read.
     * @return The <code>int</code> contianing the read data.
     * @throws IOException If an I/O error occurs.
     */
    public int readSignedInteger(int bytes, boolean bigEndian)
            throws IOException
    {
        int ret = readUnsignedInteger(bytes, bigEndian);
        if (ret >= Math.pow(2, bytes * 8 - 1)) ret = -(int) (Math.pow(2, bytes * 8) - ret);
        return ret;
    }

    /**
     * Reads the specified number of bytes as an <code>long</code> using the default endian format for this stream.  The
     * higher order bytes which are not read are filled with the highest bit of the highest read byte.
     *
     * @param bytes The number of bytes to read.
     * @return The <code>long</code> contianing the read data.
     * @throws IOException If an I/O error occurs.
     */
    public long readSignedLongInteger(int bytes)
            throws IOException
    {
        return readUnsignedLongInteger(bytes, bigEndian);
    }

    /**
     * Reads the specified number of bytes as an <code>long</code> using the specified endian format.  The higher order
     * bytes which are not read are treated as zeroes.
     *
     * @param bytes     The number of bytes to read.
     * @param bigEndian The endian format in which to read.
     * @return The <code>long</code> contianing the read data.
     * @throws IOException If an I/O error occurs.
     */
    public long readSignedLongInteger(int bytes, boolean bigEndian)
            throws IOException
    {
        long ret = readUnsignedLongInteger(bytes, bigEndian);
        if (ret >= Math.pow(2, bytes * 8 - 1)) ret = -(long) (Math.pow(2, bytes * 8) - ret);
        return ret;
    }

    /**
     * Reads a {@link BigInteger} from the underlying stream.  The {@link BigInteger} is assumed to be prefixed with an
     * <code>int</code> describing the length of its smallest possible <code>byte[]</code> representation; that
     * representation is then assumed to be found in the stream.
     * <p/>
     * Note that the <code>byte[]</code> read from the stream must be in big endian format regardless of the endianness
     * of this stream as a result of the specification of the {@link BigInteger#toByteArray()} specification.
     *
     * @return The {@link BigInteger} which was read.
     * @throws IOException If an I/O error occurs.
     */
    public BigInteger readBigInteger()
            throws IOException
    {
        int length = readInt();
        byte[] bytes = new byte[length];
        readFully(bytes);
        return new BigInteger(bytes);
    }

    /**
     * Reads a {@link String} from the underlying stream.  This method uses the {@link String#String(byte[])}
     * constructor, meaning that the platform's default encoding is used.
     *
     * @return The {@link String} which was read.
     * @throws IOException If an I/O error occurs.
     */
    public String readString()
            throws IOException
    {
        int length = readInt();
        try
        {
            byte[] data = new byte[length];
            readFully(data);
            return new String(data);
        } catch (OutOfMemoryError oome)
        {
            throw new IOException("String length indicator was " + length + "; probable corruption?");
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE