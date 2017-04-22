package orioni.jz.io.sound;

import orioni.jz.io.PrimitiveInputStream;
import orioni.jz.io.PrimitiveOutputStream;
import orioni.jz.math.MathUtilities;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

/**
 * This class contains static utility methods and constants relating to the {@link orioni.jz.io.sound} package.
 *
 * @author Zachary Palmer
 */
public class SoundUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.  Utilities classes are never instantiated.
     */
    private SoundUtilities()
    {
        super();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Determines if an audio encoding is signed.
     *
     * @param encoding The encoding to examine.
     * @return <code>true</code> if this is a signed encoding; <code>false</code> if it is not.
     */
    public static boolean isSignedEncoding(AudioFormat.Encoding encoding)
    {
        return AudioFormat.Encoding.PCM_SIGNED.equals(encoding);
    }

    /**
     * Determines the maximum sample value for the specified format.
     *
     * @param format The format in question.
     * @return The maximum sample value for that format.
     */
    public static int getMaximumSampleValue(AudioFormat format)
    {
        return MathUtilities.exponent(
                2, format.getSampleSizeInBits() - (isSignedEncoding(format.getEncoding()) ? 1 : 0)) - 1;
    }

    /**
     * Determines the minimum sample value for the specified format.
     *
     * @param format The format in question.
     * @return The minimum sample value for that format.
     */
    public static int getMinimumSampleValue(AudioFormat format)
    {
        return isSignedEncoding(format.getEncoding()) ?
               -MathUtilities.exponent(2, format.getSampleSizeInBits() - 1) : 0;
    }

    /**
     * Reads a sample from the provided {@link PrimitiveInputStream} based upon the provided {@link AudioFormat}.
     *
     * @param pis    The {@link PrimitiveInputStream} from which to read the sample.
     * @param format The {@link AudioFormat} in which to read.
     * @return The sample which was read.
     * @throws IOException If an I/O error occurs while reading from the stream.
     */
    public static int readSample(PrimitiveInputStream pis, AudioFormat format)
            throws IOException
    {
        switch (format.getSampleSizeInBits())
        {
            case 8:
                if (isSignedEncoding(format.getEncoding()))
                {
                    return pis.readByte();
                } else
                {
                    return pis.readUnsignedByte();
                }
            case 16:
                if (isSignedEncoding(format.getEncoding()))
                {
                    return pis.readShort();
                } else
                {
                    return pis.readUnsignedShort();
                }
            default:
                throw new IllegalStateException(
                        "This class cannot handle samples of " + format.getSampleSizeInBits() + " bits.");
        }
    }

    /**
     * Writes the provided sample to the target {@link PrimitiveOutputStream} based upon the provided {@link
     * AudioFormat}.
     *
     * @param pos    The {@link PrimitiveOutputStream} to which to write the sample.
     * @param format The {@link AudioFormat} in which to write.
     * @param sample The sample to write.
     * @throws IOException If an I/O error occurs while writing to the stream.
     */
    public static void writeSample(PrimitiveOutputStream pos, AudioFormat format, int sample)
            throws IOException
    {
        switch (format.getSampleSizeInBits())
        {
            case 8:
                if (isSignedEncoding(format.getEncoding()))
                {
                    pos.writeByte((byte) sample);
                } else
                {
                    pos.writeUnsignedByte(sample);
                }
                break;
            case 16:
                if (isSignedEncoding(format.getEncoding()))
                {
                    pos.writeShort((short) sample);
                } else
                {
                    pos.writeUnsignedShort(sample);
                }
                break;
            default:
                throw new IllegalStateException(
                        "This class cannot handle samples of " + format.getSampleSizeInBits() + " bits.");
        }
    }
}

// END OF FILE