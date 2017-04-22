package orioni.jz.io.sound;

import orioni.jz.io.MultiplexSequentialInputStream;
import orioni.jz.io.PatternGeneratedInputStream;
import orioni.jz.io.SizeLimitedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.InputStream;

/**
 * This {@link AudioInputStream} extension adds blank space to the beginning of the sample data.
 *
 * @author Zachary Palmer
 */
public class DelayedAudioInputStream extends AudioInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param milliseconds The number of milliseconds of blank samples to add to the beginning of the stream.
     * @param is           The {@link InputStream} containing the sample data.
     * @param format       The format of the sample data.
     * @param length       The length of this stream in frames.
     */
    public DelayedAudioInputStream(int milliseconds, InputStream is, AudioFormat format, long length)
    {
        super(
                new MultiplexSequentialInputStream(
                        new SizeLimitedInputStream(
                                new PatternGeneratedInputStream(new byte[512]),
                                (int) (format.getFrameRate() / 1000 * milliseconds + 0.5) * format.getFrameSize()),
                        is),
                format,
                length + (int) (format.getFrameRate() / 1000 * milliseconds + 0.5));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE