package orioni.jz.io.sound;

import orioni.jz.io.PrimitiveInputStream;
import orioni.jz.io.PrimitiveOutputStream;
import orioni.jz.math.MathUtilities;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <B><I>This file lacks a description.</I></B>
 *
 * @author Zachary Palmer
 */
public class ScaledAudioInputStream extends AudioInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The scaling factor to apply to the sample data.
     */
    protected double scale;
    /**
     * Determines if the encoding used on this stream is signed.
     */
    protected final boolean encodingSigned;
    /**
     * The minimum sample value for this stream's format.
     */
    protected final int minimumSampleValue;
    /**
     * The maximum sample value for this stream's format.
     */
    protected final int maximumSampleValue;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param is     The {@link InputStream} containing the sample data.
     * @param format The {@link AudioFormat} of the sample data.
     * @param length The number of frames to read from the underlying {@link InputStream}.
     * @param scale  The scaling factor to apply to the sample data.
     */
    public ScaledAudioInputStream(InputStream is, AudioFormat format, long length, double scale)
    {
        super(is, format, length);
        this.scale = scale;
        encodingSigned = SoundUtilities.isSignedEncoding(format.getEncoding());
        minimumSampleValue = SoundUtilities.getMinimumSampleValue(format);
        maximumSampleValue = SoundUtilities.getMaximumSampleValue(format);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the audio input stream.  The audio input stream's frame size must be one byte,
     * or an <code>IOException</code> will be thrown.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached
     * @throws IOException if an input or output error occurs
     */
    public int read()
            throws IOException
    {
        int ret = super.read();
        if (ret != -1)
        {
            return MathUtilities.bound(ret, minimumSampleValue, maximumSampleValue);
        } else
        {
            return ret;
        }
    }

    /**
     * Reads some number of bytes from the audio input stream and stores them into the buffer array <code>b</code>. The
     * number of bytes actually read is returned as an integer. This method blocks until input data is available, the
     * end of the stream is detected, or an exception is thrown. <p>This method will always read an integral number of
     * frames. If the length of the array is not an integral number of frames, a maximum of <code>b.length - (b.length %
     * frameSize) </code> bytes will be read.
     * <p/>
     * The samples which are read will be modified by the factor of this {@link ScaledAudioInputStream}.
     *
     * @param b the buffer into which the data is read
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the
     *         stream has been reached
     * @throws IOException if an input or output error occurs
     * @see #read(byte[], int, int)
     * @see #read()
     * @see #available
     */
    public int read(byte[] b)
            throws IOException
    {
        return read(b, 0, b.length);
    }

    /**
     * Reads up to a specified maximum number of bytes of data from the audio stream, putting them into the given byte
     * array. <p>This method will always read an integral number of frames. If <code>len</code> does not specify an
     * integral number of frames, a maximum of <code>len - (len % frameSize) </code> bytes will be read.
     * <p/>
     * The samples which are read will be modified by the factor of this {@link ScaledAudioInputStream}.
     *
     * @param b   the buffer into which the data is read
     * @param off the offset, from the beginning of array <code>b</code>, at which the data will be written
     * @param len the maximum number of bytes to read
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the
     *         stream has been reached
     * @throws IOException if an input or output error occurs
     * @see #read(byte[])
     * @see #read()
     * @see #skip
     * @see #available
     */
    public int read(byte[] b, int off, int len)
            throws IOException
    {
        int read = super.read(b, off, len);
        if (read == -1) return read;
        ByteArrayInputStream bais = new ByteArrayInputStream(b, off, read);
        PrimitiveInputStream pis = new PrimitiveInputStream(bais, getFormat().isBigEndian());
        ByteArrayOutputStream baos = new ByteArrayOutputStream(read);
        PrimitiveOutputStream pos = new PrimitiveOutputStream(baos, getFormat().isBigEndian());
        while (pis.available() > 0)
        {
            SoundUtilities.writeSample(
                    pos, getFormat(),
                    MathUtilities.bound(
                            (int)(SoundUtilities.readSample(pis, getFormat())*scale),
                            minimumSampleValue,
                            maximumSampleValue));
        }
        pos.close();
        System.arraycopy(baos.toByteArray(), 0, b, off, read);
        return read;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE