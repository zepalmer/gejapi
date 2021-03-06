package orioni.jz.io.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This {@link AudioInputStream} extension modifies the samples so that the stream length will be altered.  The
 * algorithm used is "nearest neighbor", a proess by which the new sample is equal to the proportionally closest old
 * sample.
 *
 * @author Zachary Palmer
 */
public class ResizingNearestNeighborAudioInputStream extends AudioInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The number of old frames per new frame.
     */
    protected double oldFramesPerNewFrame;
    /**
     * The current frame index.
     */
    protected double oldFrameIndex;
    /**
     * The most recently read frame.
     */
    protected byte[] recentFrame;
    /**
     * Determines whether or not this stream has exhausted the underlying stream.
     */
    protected boolean eos;
    /**
     * The new frame length for the modified stream.
     */
    protected long newFrameLength;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param newFrameCount The new number of frames for this {@link ResizingNearestNeighborAudioInputStream}.
     * @param is              The {@link InputStream} containing the sample data to be translated.
     * @param format          The {@link AudioFormat} of the underlying {@link InputStream}.
     * @param oldFrameCount The number of frames in the underlying {@link InputStream}.
     */
    public ResizingNearestNeighborAudioInputStream(long newFrameCount, InputStream is, AudioFormat format,
                                                   long oldFrameCount)
    {
        super(is, format, oldFrameCount);
        oldFramesPerNewFrame = (double) oldFrameCount / newFrameCount;
        eos = false;
        recentFrame = new byte[format.getFrameSize()];
        newFrameLength = newFrameCount;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the audio input stream.  The audio input stream's frame size must be one byte,
     * or an <code>IOException</code> will be thrown.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached
     * @throws IOException if an input or output error occurs
     * @see #read(byte[], int, int)
     * @see #read(byte[])
     * @see #available
     *      <p/>
     */
    public int read()
            throws IOException
    {
        int ret = read(recentFrame, 0, recentFrame.length);
        if (ret == -1) return ret; else return recentFrame[0];
    }

    /**
     * Reads some number of bytes from the audio input stream and stores them into the buffer array <code>b</code>. The
     * number of bytes actually read is returned as an integer. This method blocks until input data is available, the
     * end of the stream is detected, or an exception is thrown. <p>This method will always read an integral number of
     * frames. If the length of the array is not an integral number of frames, a maximum of <code>b.length - (b.length %
     * frameSize) </code> bytes will be read.
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
        if (eos) return -1;
        len = len - (len % format.getFrameSize());
        int totalRead = 0;
        while (len > 0)
        {
            int framesForward =
                    (int) ((oldFrameIndex < 0) ?
                           1 :
                           Math.round(oldFrameIndex + oldFramesPerNewFrame) - Math.round(oldFrameIndex));
            oldFrameIndex += oldFramesPerNewFrame;
            for (int i = 0; (i < framesForward) && (this.available() > 0); i++)
            {
                if (super.read(recentFrame, 0, recentFrame.length) == -1)
                {
                    eos = true;
                    if (totalRead == 0) return -1; else return totalRead;
                }
            }
            System.arraycopy(recentFrame, 0, b, off, recentFrame.length);
            len -= recentFrame.length;
            off += recentFrame.length;
            totalRead += recentFrame.length;
        }
        return totalRead;
    }

    /**
     * Obtains the length of the stream, expressed in sample frames rather than bytes.
     *
     * @return the length in sample frames
     */
    public long getFrameLength()
    {
        return newFrameLength;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE