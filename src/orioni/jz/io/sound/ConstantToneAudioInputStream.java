package orioni.jz.io.sound;

import orioni.jz.io.PatternGeneratedInputStream;
import orioni.jz.io.PreexistingByteArrayOutputStream;
import orioni.jz.io.PrimitiveOutputStream;
import orioni.jz.io.SizeLimitedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

/**
 * This {@link AudioInputStream} provides a constant tone of a specified frequency and duration.
 *
 * @author Zachary Palmer
 */
public class ConstantToneAudioInputStream extends AudioInputStream
{

// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The audio format used in this class.
     */
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param frequency The frequency of the tone, in hertz.
     * @param duration  The duration of the tone, in milliseconds.
     */
    public ConstantToneAudioInputStream(double frequency, int duration)
    {
        super(
                new SizeLimitedInputStream(
                        new PatternGeneratedInputStream(getTonePattern(frequency)),
                        (int) (duration / 1000.0 * AUDIO_FORMAT.getFrameRate() * AUDIO_FORMAT.getFrameSize())),
                AUDIO_FORMAT,
                (int) (duration / 1000.0 * AUDIO_FORMAT.getFrameRate()));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * This method is used to produce the sine wave pattern to create the constant tone.
     *
     * @param frequency The frequency of the sine wave.
     * @return The pattern which represents the tone.
     */
    private static byte[] getTonePattern(double frequency)
    {
        int frames = (int) (AUDIO_FORMAT.getFrameRate() / frequency);
        byte[] ret = new byte[frames * AUDIO_FORMAT.getFrameSize()];
        // If the AUDIO_FORMAT field is changed, the following generation code will need to be changed.
        PreexistingByteArrayOutputStream baos = new PreexistingByteArrayOutputStream(ret);
        PrimitiveOutputStream pos = new PrimitiveOutputStream(baos, AUDIO_FORMAT.isBigEndian());
        for (int i = 0; i < frames; i++)
        {
            try
            {
                pos.writeShort((short) (10000 * Math.sin(i * 2 * Math.PI / frames)));
                pos.writeShort((short) (10000 * Math.sin(i * 2 * Math.PI / frames)));
            } catch (IOException e)
            {
                // this won't happen - ByteArrayOutputStream doesn't throw IOExceptions
            }
        }
        return ret;
    }
}

// END OF FILE