package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * This class is designed to allow the writing of primitive values to an output stream.  This is accomplished by
 * converting the Java primitives to a byte array and writing that array to the underlying stream.  The endian form of
 * the data is specified on construction but can be overridden on each method call.
 * <p/>
 * This class is similar to {@link java.io.DataOutputStream} except in that it allows for streams to be written in
 * either endian form.
 *
 * @author Zachary Palmer
 */
public class PrimitiveOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The constant representing big-endian format.  This constant is guaranteed to be equal to {@link
     * PrimitiveInputStream#BIG_ENDIAN}.
     */
    public static final boolean BIG_ENDIAN = true;
    /**
     * The constant representing little-endian format.  This constant is guaranteed to be equal to {@link
     * PrimitiveInputStream#LITTLE_ENDIAN}.
     */
    public static final boolean LITTLE_ENDIAN = false;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link OutputStream}.
     */
    protected OutputStream outputStream;
    /**
     * The default endian format.
     */
    protected boolean bigEndian;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param outputStream The underlying output stream from which data should be read.
     * @param bigEndian    Whether or not the underlying output stream should be read in big endian form by default.
     *                      You may also use one of the <code>XXX_ENDIAN</code> constants on this class.
     */
    public PrimitiveOutputStream(OutputStream outputStream, boolean bigEndian)
    {
        super();
        this.outputStream = outputStream;
        this.bigEndian = bigEndian;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        flush(); // in case extensions use it for some reason
        outputStream.close();
    }

    /**
     * Does nothing.  Writes to this stream are automatically flushed.
     */
    public void flush()
    {
    }

    /**
     * Writes <code>b.length</code> bytes from the specified byte array to this output stream.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     * @see OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b)
            throws IOException
    {
        write(b, 0, b.length);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this output
     * stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs. In particular, an <code>IOException</code> is thrown if the output
     *                     stream is closed.
     */
    public void write(byte[] b, int off, int len)
            throws IOException
    {
        outputStream.write(b, off, len);
    }

    /**
     * Writes the specified byte to this output stream.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular, an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    public void write(int b)
            throws IOException
    {
        outputStream.write(b);
    }

    /**
     * Writes a <code>short</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>short</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeShort(short data)
            throws IOException
    {
        writeShort(data, bigEndian);
    }

    /**
     * Writes a short to the underlying stream in the specified endian format.
     *
     * @param data       The <code>short</code> to write.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeShort(short data, boolean bigEndian)
            throws IOException
    {
        byte[] out = new byte[2];
        if (bigEndian)
        {
            out[0] = (byte) (data >>> 8);
            out[1] = (byte) (data);
        } else
        {
            out[0] = (byte) (data);
            out[1] = (byte) (data >>> 8);
        }
        outputStream.write(out);
    }

    /**
     * Writes a <code>char</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>char</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeChar(char data)
            throws IOException
    {
        writeChar(data, bigEndian);
    }

    /**
     * Writes a <code>char<code> to the underlying stream in the specified endian format.
     *
     * @param data       The <code>char</code> to write.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeChar(char data, boolean bigEndian)
            throws IOException
    {
        writeShort((short) data, bigEndian);
    }

    /**
     * Writes a <code>int</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>int</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeInt(int data)
            throws IOException
    {
        writeInt(data, bigEndian);
    }

    /**
     * Writes a <code>int<code> to the underlying stream in the specified endian format.
     *
     * @param data       The <code>int</code> to write.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeInt(int data, boolean bigEndian)
            throws IOException
    {
        byte[] out = new byte[4];
        int shift = 0;
        if (bigEndian)
        {
            for (int i = out.length - 1; i >= 0; i--)
            {
                out[i] = (byte) (data >>> shift);
                shift += 8;
            }
        } else
        {
            for (int i = 0; i < out.length; i++)
            {
                out[i] = (byte) (data >>> shift);
                shift += 8;
            }
        }
        outputStream.write(out);
    }

    /**
     * Writes a <code>long</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>long</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeLong(long data)
            throws IOException
    {
        writeLong(data, bigEndian);
    }

    /**
     * Writes a <code>long<code> to the underlying stream in the specified endian format.
     *
     * @param data       The <code>long</code> to write.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeLong(long data, boolean bigEndian)
            throws IOException
    {
        byte[] out = new byte[8];
        int shift = 0;
        if (bigEndian)
        {
            for (int i = out.length - 1; i >= 0; i--)
            {
                out[i] = (byte) (data >>> shift);
                shift += 8;
            }
        } else
        {
            for (int i = 0; i < out.length; i++)
            {
                out[i] = (byte) (data >>> shift);
                shift += 8;
            }
        }
        outputStream.write(out);
    }

    /**
     * Writes a <code>boolean</code> to the underlying stream.
     *
     * @param data The <code>boolean</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeBoolean(boolean data)
            throws IOException
    {
        write(data ? 1 : 0);
    }

    /**
     * Writes a <code>double</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>double</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeDouble(double data)
            throws IOException
    {
        writeDouble(data, bigEndian);
    }

    /**
     * Writes a <code>double<code> to the underlying stream in the specified endian format.
     *
     * @param data       The <code>double</code> to write.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeDouble(double data, boolean bigEndian)
            throws IOException
    {
        long x = Double.doubleToLongBits(data);
        writeLong(x, bigEndian);
    }

    /**
     * Writes a <code>float</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>float</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeFloat(float data)
            throws IOException
    {
        writeFloat(data, bigEndian);
    }

    /**
     * Writes a <code>float<code> to the underlying stream in the specified endian format.
     *
     * @param data       The <code>float</code> to write.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeFloat(float data, boolean bigEndian)
            throws IOException
    {
        int x = Float.floatToIntBits(data);
        writeInt(x, bigEndian);
    }

    /**
     * Writes a <code>byte</code> to the underlying stream.
     *
     * @param data The <code>byte</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeUnsignedByte(int data)
            throws IOException
    {
        write(data & 0xFF);
    }

    /**
     * Writes a <code>byte</code> to the underlying stream.
     *
     * @param data The <code>byte</code> to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeByte(byte data)
            throws IOException
    {
        write(data);
    }

    /**
     * Writes a <code>short</code> to the underlying stream in the default endian format for this stream.
     *
     * @param data The <code>short</code> to write.  The lowest sixteen bits of this <code>int</code> are used.
     * @throws IOException If an I/O error occurs.
     */
    public void writeUnsignedShort(int data)
            throws IOException
    {
        writeUnsignedShort(data, bigEndian);
    }

    /**
     * Writes a <code>short</code> to the underlying stream in the specified endian format.
     *
     * @param data       The <code>short</code> to write.  The lowest sixteen bits of this <code>int</code> are used.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeUnsignedShort(int data, boolean bigEndian)
            throws IOException
    {
        byte[] out = new byte[2];
        if (bigEndian)
        {
            out[0] = (byte) (data >>> 8);
            out[1] = (byte) (data);
        } else
        {
            out[0] = (byte) (data);
            out[1] = (byte) (data >>> 8);
        }
        outputStream.write(out);
    }

    /**
     * Writes an <code>int</code> as a variable of the specified number of bytes.  Sign is not preserved.
     *
     * @param data  The value to write.
     * @param bytes The number of bytes in which to write it.  If this value exceeds the size of the variable, zeroes
     *              are used as padding.  If this value is less than the size of the variable, the higher order bytes
     *              are ignored.
     * @throws IOException If an I/O error occurs.
     */
    public void writeUnsignedInteger(int data, int bytes)
            throws IOException
    {
        writeUnsignedInteger(data, bytes, bigEndian);
    }

    /**
     * Writes an <code>int</code> as a variable of the specified number of bytes.  Sign is not preserved.
     *
     * @param data       The value to write.
     * @param bytes      The number of bytes in which to write it.  If this value exceeds the size of the variable,
     *                   zeroes are used as padding.  If this value is less than the size of the variable, the higher
     *                   order bytes are ignored.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeUnsignedInteger(int data, int bytes, boolean bigEndian)
            throws IOException
    {
        byte[] out = new byte[bytes];
        if (bigEndian)
        {
            for (int i = 0; i < bytes; i++)
            {
                out[i] = (byte) (data >>> (bytes - i - 1) * 8);
            }
        } else
        {
            for (int i = 0; i < bytes; i++)
            {
                out[i] = (byte) (data >>> i * 8);
            }
        }
        outputStream.write(out);
    }

    /**
     * Writes an <code>int</code> as a variable of the specified number of bytes.  Sign is preserved.
     *
     * @param data  The value to write.
     * @param bytes The number of bytes in which to write it.  If this value exceeds the size of the variable, zeroes
     *              are used as padding.  If this value is less than the size of the variable, the higher order bytes
     *              are ignored.
     * @throws IOException If an I/O error occurs.
     */
    public void writeSignedInteger(int data, int bytes)
            throws IOException
    {
        writeSignedInteger(data, bytes, bigEndian);
    }

    /**
     * Writes an <code>int</code> as a variable of the specified number of bytes.  Sign is preserved.
     *
     * @param data       The value to write.
     * @param bytes      The number of bytes in which to write it.  If this value exceeds the size of the variable,
     *                   zeroes are used as padding.  If this value is less than the size of the variable, the higher
     *                   order bytes are ignored.
     * @param bigEndian The endian format in which to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeSignedInteger(int data, int bytes, boolean bigEndian)
            throws IOException
    {
        byte[] out = new byte[bytes];
        if (data < 0)
        {
            for (int i = 0; i < out.length; i++)
            {
                out[i] = (byte) (0xFF);
            }
        }
        if (bigEndian)
        {
            for (int i = 0; i < bytes; i++)
            {
                out[i] = (byte) (data >>> (bytes - i - 1) * 8);
            }
        } else
        {
            for (int i = 0; i < bytes; i++)
            {
                out[i] = (byte) (data >>> i * 8);
            }
        }
        outputStream.write(out);
    }

    /**
     * Writes the provided {@link BigInteger} in the smallest possible bytewise representation to this stream.  The
     * {@link BigInteger}'s byte array is prefixed with an <code>int</code> which describes its length.
     * <p/>
     * Note that the <code>byte[]</code> written to the stream will be in big endian format regardless of the endianness
     * of this stream as a result of the specification of the {@link BigInteger#toByteArray()} specification.
     *
     * @param bi The {@link BigInteger} to write.
     * @throws IOException If an I/O error occurs while writing the {@link BigInteger} to this stream.
     */
    public void writeBigInteger(BigInteger bi)
            throws IOException
    {
        byte[] biBytes = bi.toByteArray();
        writeInt(biBytes.length);
        write(biBytes);
    }

    /**
     * Writes the provided {@link String} to this stream.  The {@link String#getBytes()} method is used; thus, the
     * platform's default is used.
     *
     * @param string The {@link String} to write.
     * @throws IOException If an I/O error occurs while writing the {@link String}.
     */
    public void writeString(String string)
            throws IOException
    {
        writeInt(string.length());
        write(string.getBytes());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE