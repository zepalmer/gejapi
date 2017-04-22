package orioni.jz.io.sound;

import orioni.jz.io.PrimitiveInputStream;
import orioni.jz.io.PrimitiveOutputStream;
import orioni.jz.io.SizeLimitedInputStream;

import javax.sound.sampled.*;
import java.io.*;

/**
 * This class represents a unit of sampled sound in memory.  It is loaded from an {@link AudioInputStream}; the
 * pertinent data as well as the sample set are stored in this class.
 * <p/>
 * Because this class buffers the audio file in question, audio files larger than the indexing capacity of an array (2
 * Gb) cannot be loaded.
 *
 * @author Zachary Palmer
 */
public class SampledSound
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The normalization method to use in a mixing operation.
     */
    public static enum NormalizationMethod
    {
        /**
         * This normalization method does nothing to the samples after they are mixed.
         */
        NONE,
        /**
         * This normalization method rescales the samples so that the most extreme sample is no more extreme than the
         * maximum allowed by the audio format.  All other samples are scaled down by the same linear factor.
         */
        LINEAR
    }

    /**
     * The resizing method to use in a length adjustment operation.
     */
    public static enum ResizingMethod
    {
        /**
         * This resizing method matches any unknown samples to their nearest known neighbor.
         */
        NEAREST_NEIGHBOR,
        /**
         * This resizing method performs a linear weighted average on any unknown samples with the two known adjacent
         * samples.
         */
        LINEAR_WEIGHTED_AVERAGE
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The sample data for this sound.
     */
    protected byte[] sampleData;
    /**
     * The {@link AudioFormat} in which this sample sound is formatted.
     */
    protected AudioFormat format;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Creates the {@link AudioInputStream} from the specified file.
     *
     * @param file The {@link File} from which to load the audio data.
     * @throws IOException                   If an I/O error occurs while reading the audio data.
     * @throws UnsupportedAudioFileException If the file in question is not recognized by the system.
     */
    public SampledSound(File file)
            throws UnsupportedAudioFileException, IOException
    {
        AudioInputStream ais = null;
        try
        {
            ais = AudioSystem.getAudioInputStream(file);
            initialize(ais);
        } finally
        {
            try
            {
                if (ais != null) ais.close();
            } catch (IOException e)
            {
                // We tried.
            }
        }
    }

    /**
     * General constructor.
     *
     * @param stream The {@link AudioInputStream} from which to obtain the sampled audio data.
     * @throws IOException If an I/O error occurs while reading the sampled data.
     */
    public SampledSound(AudioInputStream stream)
            throws IOException
    {
        initialize(stream);
    }

    /**
     * General constructor.
     *
     * @param sampleData The sample data for this {@link SampledSound}.
     * @param format     The {@link AudioFormat} of the provided sample data.
     */
    public SampledSound(byte[] sampleData, AudioFormat format)
    {
        this.sampleData = sampleData;
        this.format = format;
    }

    /**
     * Initializes this class.
     *
     * @param stream The {@link AudioInputStream} from which to obtain the sampled audio data.
     * @throws IOException If an I/O error occurs while reading the sampled data.
     */
    private void initialize(AudioInputStream stream)
            throws IOException
    {
        format = stream.getFormat();
        sampleData = new byte[(int) (stream.getFrameLength() * format.getFrameSize())];
        //noinspection ResultOfMethodCallIgnored
        stream.read(sampleData);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the sample data for this {@link SampledSound}.  This method should be used with care as altering the
     * contents will alter the content of this {@link SampledSound}.
     *
     * @return The <code>byte[]</code> used to store this {@link SampledSound}'s data.
     */
    protected byte[] getSampleData()
    {
        return sampleData;
    }

    /**
     * Plays this sampled audio using the Java audio framework.  This call blocks until the audio playback is complete.
     *
     * @throws LineUnavailableException If no line could be found on which to play the audio.
     */
    public void play()
            throws LineUnavailableException
    {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sdl = (SourceDataLine) (AudioSystem.getLine(info));
        sdl.open(format);
        sdl.start();
        sdl.write(sampleData, 0, sampleData.length);
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    /**
     * Plays this sampled audio using the Java audio framework.  This call does not block, and any errors which may
     * occur during playing will be ignored.
     */
    public void playWithoutBlocking()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    play();
                } catch (LineUnavailableException e)
                {
                    // We have no way to report the error.
                }
            }
        }.start();
    }

    /**
     * Retrieves the format of this {@link SampledSound}.
     *
     * @return The {@link AudioFormat} of this {@link SampledSound}.
     */
    public AudioFormat getAudioFormat()
    {
        return format;
    }

    /**
     * Retrieves the number of frames in this {@link SampledSound}.
     *
     * @return The number of frames in this {@link SampledSound}.
     */
    public int getFrameCount()
    {
        return sampleData.length / format.getFrameSize();
    }

    /**
     * Retrieves an {@link InputStream} which supplies the sample's buffered data.
     *
     * @return An {@link InputStream} containing the sample's buffered data.
     */
    public InputStream getInputStream()
    {
        return new ByteArrayInputStream(sampleData);
    }

    /**
     * Retrieves an {@link AudioInputStream} which draws its data from this sample's buffered data.
     *
     * @return An {@link AudioInputStream} which draws its data from this sample's buffered data.
     */
    public AudioInputStream getAudioInputStream()
    {
        return new AudioInputStream(
                new ByteArrayInputStream(sampleData), format, sampleData.length / format.getFrameSize());
    }

    /**
     * Creates a shallow copy of this {@link SampledSound} object.
     *
     * @return A shallow copy of this {@link SampledSound} object.
     * @throws IOException If an I/O error occurs while creating the copy.
     */
    public SampledSound copy()
            throws IOException
    {
        return new SampledSound(getAudioInputStream());
    }

    /**
     * Creates a {@link SampledSound} containing a subset of the samples contained within this {@link SampledSound}.
     *
     * @param start The starting location for the new sample.  This is the first frame in the new {@link SampledSound}
     *              (inclusive).
     * @param stop  The stopping location for the new sample.  This is the last frame in the new {@link SampledSound}
     *              (exclusive).
     * @return The {@link SampledSound} which was created.
     * @throws IOException If an IOException occurs while creating the subsample.
     */
    public SampledSound getSubSampledSound(int start, int stop)
            throws IOException
    {
        if (stop < start) throw new IllegalArgumentException("stop cannot be less than start");
        int length = stop - start;
        InputStream is = getInputStream();
        //noinspection ResultOfMethodCallIgnored
        is.skip(start * format.getFrameSize());
        is = new SizeLimitedInputStream(is, length * format.getFrameSize());
        return new SampledSound(new AudioInputStream(is, format, length));
    }

    /**
     * Retrieves an {@link AudioInputStream} which provides the data which appears in this {@link SampledSound} after a
     * delay of the specified number of milliseconds.  The precision of this method will be limited to the precision of
     * the audio stream.
     *
     * @param milliseconds The number of milliseconds of blank space to add to the beginning of the audio stream.
     * @return The modified stream.
     */
    public AudioInputStream getDelayedAudioInputStream(int milliseconds)
    {
        return new DelayedAudioInputStream(milliseconds, getInputStream(), format, getFrameCount());
    }

    /**
     * Creates a copy of this {@link SampledSound} object which has a period of blank time at the beginning.
     *
     * @param milliseconds The number of milliseconds of blank space to add to the beginning of the sound sample.
     * @return The new {@link SampledSound} object.
     * @throws IOException If an I/O error occurs while creating the copy.
     */
    public SampledSound getDelayedCopy(int milliseconds)
            throws IOException
    {
        return new SampledSound(getDelayedAudioInputStream(milliseconds));
    }

    /**
     * Retrieves an {@link AudioInputStream} which provides the data which appears in this {@link SampledSound} followed
     * by a padding of the specified number of milliseconds.  The precision of this method will be limited to the
     * precision of the audio stream.
     *
     * @param milliseconds The number of milliseconds of blank space to add to the end of the audio stream.
     * @return The modified stream.
     */
    public AudioInputStream getPaddedAudioInputStream(int milliseconds)
    {
        return new PaddedAudioInputStream(milliseconds, getInputStream(), format, getFrameCount());
    }

    /**
     * Creates a copy of this {@link SampledSound} object which has a period of blank time at the end.
     *
     * @param milliseconds The number of milliseconds of blank space to add to the end of the sound sample.
     * @return The new {@link SampledSound} object.
     * @throws IOException If an I/O error occurs while creating the copy.
     */
    public SampledSound getPaddedCopy(int milliseconds)
            throws IOException
    {
        return new SampledSound(getPaddedAudioInputStream(milliseconds));
    }

    /**
     * Generates an {@link AudioInputStream} which is scaled by the provided amount.
     *
     * @param scale The scale to apply to the sampling information.
     * @throws IOException If an I/O error occurs when scaling the data.
     */
    public AudioInputStream getScaledAudioStream(double scale)
            throws IOException
    {
        return new ScaledAudioInputStream(getInputStream(), format, getFrameCount(), scale);
    }

    /**
     * Generates a {@link SampledSound} which matches this {@link SampledSound}'s sampling information.  These samples
     * are scaled by the specified degree.
     *
     * @param scale The scale to apply to the sampling information.
     * @throws IOException If an I/O error occurs when scaling the data.
     */
    public SampledSound getScaledCopy(double scale)
            throws IOException
    {
        return new SampledSound(getScaledAudioStream(scale));
    }

    /**
     * Mixes this {@link SampledSound} object with another {@link SampledSound} object using the specified normalization
     * method.
     *
     * @param otherSound          The other {@link SampledSound} with which to mix this sound.
     * @param normalizationMethod The normalization method for this mixing operation.
     * @throws IOException              If an I/O error occurs during the mixing process.
     * @throws IllegalArgumentException If the two samples are incompatible.
     */
    public SampledSound mix(SampledSound otherSound, NormalizationMethod normalizationMethod)
            throws IOException, IllegalArgumentException
    {
        // Ensure that the two samples are of the same format.
        otherSound = otherSound.convert(format);

        // Mix and normalize stream
        Normalizer normalizer = null;

        switch (normalizationMethod)
        {
            case NONE:
                normalizer = NullNormalizer.SINGLETON;
                break;
            case LINEAR:
                normalizer = new LinearNormalizer(getInputStream(), otherSound.getInputStream());
                break;
        }

        // Construct the sample
        byte[] mixedData = mixAndNormalize(getInputStream(), otherSound.getInputStream(), normalizer);
        return new SampledSound(
                new AudioInputStream(
                        new ByteArrayInputStream(mixedData), format, mixedData.length / format.getFrameSize()));
    }

    /**
     * Converts this sample to the specified format.
     *
     * @param format The new format for this sample.
     * @throws IOException              If an I/O error occurs during the conversion.
     * @throws IllegalArgumentException If the specified conversion is not possible.
     */
    public SampledSound convert(AudioFormat format)
            throws IOException
    {
        if (format.equals(this.format)) return this;
        return new SampledSound(AudioSystem.getAudioInputStream(format, getAudioInputStream()));
    }

    /**
     * Resizes this {@link SampledSound} to the specified ratio of its original size.  The precision of this method is
     * limited to the precision of the audio format this {@link SampledSound} contains.
     *
     * @param resizingMethod The {@link ResizingMethod} to use when resizing the {@link SampledSound}.
     * @param resizeFactor   The factor by which to resize this {@link SampledSound}.
     * @return The resized {@link SampledSound}.
     * @throws IOException If an I/O error occurs during the resizing.
     */
    public SampledSound resize(ResizingMethod resizingMethod, double resizeFactor)
            throws IOException
    {
        return resize(resizingMethod, (int) (getFrameCount() * resizeFactor));
    }

    /**
     * Resizes this {@link SampledSound} to the specified number of frames.
     *
     * @param resizingMethod The {@link ResizingMethod} to use when resizing the {@link SampledSound}.
     * @param frames         The number of frames for the new {@link SampledSound}.
     * @return The resized {@link SampledSound}.
     * @throws IOException If an I/O error occurs during the resizing.
     */
    public SampledSound resize(ResizingMethod resizingMethod, long frames)
            throws IOException
    {
        switch (resizingMethod)
        {
            case NEAREST_NEIGHBOR:
                return new SampledSound(
                        new ResizingNearestNeighborAudioInputStream(
                                frames, getInputStream(), getAudioFormat(), getFrameCount()));
            case LINEAR_WEIGHTED_AVERAGE:
                return new SampledSound(
                        new ResizingLinearWeightedAverageAudioInputStream(
                                frames, getInputStream(), getAudioFormat(), getFrameCount()));
            default:
                throw new IllegalArgumentException("Unrecognized resizing method: " + resizingMethod);
        }
    }

    /**
     * Splits this {@link SampledSound} into a number of {@link SampledSound}s.  The provided indices indicate the
     * frames at which splits should occur.  For example, provided an index array of <code>{44100, 88200, 89200}</code>,
     * this method would produce four {@link SampledSound}s.  The first would start at frame <code>0</code> and proceed
     * to frame <code>44099</code>.  The second would start at frame <code>44100</code> and continue until frame
     * <code>88199</code>.  The third would be from frame <code>88200</code> to frame <code>89199</code>.  The last
     * would start at frame <code>89200</code> and continue until the end of this {@link SampledSound}.
     *
     * @param indices The array of incides over which this {@link SampledSound} should be split.
     * @return The {@link SampledSound}<code>[]</code> containing the split sounds.
     * @throws IOException               If an I/O error occurs while performing this operation.
     * @throws IllegalArgumentException  If the provided indices are not in ascending order.
     * @throws IndexOutOfBoundsException If one or more of the provided indices are larger than the number of frames in
     *                                   this {@link SampledSound}.
     */
    public SampledSound[] split(int... indices)
            throws IOException
    {
        SampledSound[] ret = new SampledSound[indices.length + 1];
        int lastIndex = 0;
        for (int i = 0; i < indices.length; i++)
        {
            int index = indices[i];
            if (lastIndex > index)
            {
                throw new IllegalArgumentException("Indices out of order: " + lastIndex + " before " + index);
            }

            ret[i] = getSubSampledSound(lastIndex, index);

            lastIndex = indices[i];
        }
        ret[ret.length - 1] = getSubSampledSound(lastIndex, getFrameCount());
        return ret;
    }

// INTERNAL METHODS //////////////////////////////////////////////////////////////

    /**
     * Determines the linear normalizer ratio necessary to normalize the provided streams.  This operation is heavily
     * optimized over analyzation of the streams over {@link PrimitiveInputStream}s, as {@link PrimitiveInputStream}
     * operations can require the continuous creation of byte arrays and <code>short</code>s for 16-bit samples, as well
     * as frequent end-of-stream checks.  This method reads the bytes individually and compares them seperately, but
     * performs maximum analysis treating the two fields as one 16-bit field and merges the results at the end.  This
     * approach is quite ghastly in code but is enormously more efficient.
     *
     * @param isA The first input stream of sample data.
     * @param isB The second input stream of sample data.
     * @return The normalizer ratio for the streams.
     * @throws IOException If one of the underlying streams throws an {@link IOException}.
     */
    private double getLinearNormalizerRatio(InputStream isA, InputStream isB)
            throws IOException
    {
        byte[] bufferA = new byte[16384];
        byte[] bufferB = new byte[16384];
        int readA;
        int readB;
        int maximumSignal = 0;
        int maximumSignalUpper = 0;
        int maximumSignalLower = 0;

        final boolean signed = SoundUtilities.isSignedEncoding(format.getEncoding());
        final boolean bigEndianForm = format.isBigEndian();
        final int maximumPossibleSignal = SoundUtilities.getMaximumSampleValue(format);

        switch (format.getSampleSizeInBits())
        {
            case 8:
                if (signed)
                {
                    readA = isA.read(bufferA);
                    readB = isB.read(bufferB);
                    while ((readA > 0) && (readB > 0))
                    {
                        int maxloop = Math.min(readA, readB);
                        /* This for-loop differs between sample sizes */
                        for (int i = 0; i < maxloop; i++)
                        {
                            /* This line differs between signed states */
                            int combinedSample = bufferA[i] + bufferB[i];
                            if (combinedSample > 0)
                            {
                                maximumSignal = Math.max(maximumSignal, Math.abs(combinedSample));
                            } else
                            {
                                maximumSignal = Math.max(maximumSignal, Math.abs(combinedSample) - 1);
                            }
                        }

                        readA = isA.read(bufferA);
                        readB = isB.read(bufferB);
                    }
                } else
                {
                    readA = isA.read(bufferA);
                    readB = isB.read(bufferB);
                    while ((readA > 0) && (readB > 0))
                    {
                        int maxloop = Math.min(readA, readB);
                        /* This for-loop differs between sample sizes */
                        for (int i = 0; i < maxloop; i++)
                        {
                            /* This line differs between signed states */
                            int combinedSample = (bufferA[i] & 0xFF) + (bufferB[i] & 0xFF);
                            if (combinedSample > 0)
                            {
                                maximumSignal = Math.max(maximumSignal, Math.abs(combinedSample));
                            } else
                            {
                                maximumSignal = Math.max(maximumSignal, Math.abs(combinedSample) - 1);
                            }
                        }

                        readA = isA.read(bufferA);
                        readB = isB.read(bufferB);
                    }
                }
                break;
            case 16:
                if (signed)
                {
                    if (bigEndianForm)
                    {
                        readA = isA.read(bufferA);
                        readB = isB.read(bufferB);
                        while ((readA > 0) && (readB > 0))
                        {
                            int maxloop = Math.min(readA, readB);
                            /* This for-loop differs between sample sizes */
                            for (int i = 0; i < maxloop; i += 2)
                            {
                                /* These two lines differ between big endian forms. */
                                int upper = bufferA[i] + bufferB[i];
                                int lower = bufferA[i + 1] + bufferB[i + 1];

                                // invert the sign on the 16-bit value represented by upper:lower and subtract
                                // one (to compensate for the added one point of negative range
                                // Note that subtraction is easy; we just don't perform the add-one from the
                                // 2's complement operation
                                if (upper < 0)
                                {
                                    upper ^= 0xFF;
                                    lower ^= 0xFF;
                                }

                                if (upper > maximumSignalUpper)
                                {
                                    maximumSignalUpper = upper;
                                    maximumSignalLower = lower;
                                } else if ((upper == maximumSignalUpper) && (lower > maximumSignalLower))
                                {
                                    maximumSignalLower = lower;
                                }
                            }

                            readA = isA.read(bufferA);
                            readB = isB.read(bufferB);
                        }
                    } else
                    {
                        readA = isA.read(bufferA);
                        readB = isB.read(bufferB);
                        while ((readA > 0) && (readB > 0))
                        {
                            int maxloop = Math.min(readA, readB);
                            /* This for-loop differs between sample sizes */
                            for (int i = 0; i < maxloop; i += 2)
                            {
                                /* These two lines differ between big endian forms. */
                                int lower = bufferA[i] + bufferB[i];
                                int upper = bufferA[i + 1] + bufferB[i + 1];

                                // invert the sign on the 16-bit value represented by upper:lower and subtract
                                // one (to compensate for the added one point of negative range
                                // Note that subtraction is easy; we just don't perform the add-one from the
                                // 2's complement operation
                                if (upper < 0)
                                {
                                    upper ^= 0xFF;
                                    lower ^= 0xFF;
                                }

                                if (upper > maximumSignalUpper)
                                {
                                    maximumSignalUpper = upper;
                                    maximumSignalLower = lower;
                                } else if ((upper == maximumSignalUpper) && (lower > maximumSignalLower))
                                {
                                    maximumSignalLower = lower;
                                }
                            }

                            readA = isA.read(bufferA);
                            readB = isB.read(bufferB);
                        }
                    }
                } else
                {
                    if (bigEndianForm)
                    {
                        readA = isA.read(bufferA);
                        readB = isB.read(bufferB);
                        while ((readA > 0) && (readB > 0))
                        {
                            int maxloop = Math.min(readA, readB);
                            /* This for-loop differs between sample sizes */
                            for (int i = 0; i < maxloop; i += 2)
                            {
                                /* These two lines differ between big endian forms. */
                                int upper = (bufferA[i] & 0xFF) + (bufferB[i] & 0xFF);
                                int lower = (bufferA[i + 1] & 0xFF) + (bufferB[i + 1] & 0xFF);

                                if (upper > maximumSignalUpper)
                                {
                                    maximumSignalUpper = upper;
                                    maximumSignalLower = lower;
                                } else if ((upper == maximumSignalUpper) && (lower > maximumSignalLower))
                                {
                                    maximumSignalLower = lower;
                                }
                            }

                            readA = isA.read(bufferA);
                            readB = isB.read(bufferB);
                        }
                    } else
                    {
                        readA = isA.read(bufferA);
                        readB = isB.read(bufferB);
                        while ((readA > 0) && (readB > 0))
                        {
                            int maxloop = Math.min(readA, readB);
                            /* This for-loop differs between sample sizes */
                            for (int i = 0; i < maxloop; i += 2)
                            {
                                /* These two lines differ between big endian forms. */
                                int lower = (bufferA[i] & 0xFF) + (bufferB[i] & 0xFF);
                                int upper = (bufferA[i + 1] & 0xFF) + (bufferB[i + 1] & 0xFF);

                                if (upper > maximumSignalUpper)
                                {
                                    maximumSignalUpper = upper;
                                    maximumSignalLower = lower;
                                } else if ((upper == maximumSignalUpper) && (lower > maximumSignalLower))
                                {
                                    maximumSignalLower = lower;
                                }
                            }

                            readA = isA.read(bufferA);
                            readB = isB.read(bufferB);
                        }
                    }
                }
                maximumSignal = ((maximumSignalUpper & 0xFF) << 8) | (maximumSignalLower & 0xFF);
                break;
            default:
                throw new IllegalStateException(
                        "This class cannot handle samples of " + format.getSampleSizeInBits() +
                        " bits.");
        }
        isA.close();
        isB.close();

        return maximumPossibleSignal / Math.max((double) maximumSignal, maximumPossibleSignal);
    }

    /**
     * This method is used to mix and normalize two streams using the provided {@link Normalizer}.  It is heavily
     * optimized for performance at the cost of containing redundant code.
     *
     * @param isA        The first input stream with audio samples.
     * @param isB        The second input stream with audio samples.
     * @param normalizer The normalizer to use.
     * @return The normalized audio samples.
     * @throws IOException If an I/O error occurs while performing the normalization.
     */
    private byte[] mixAndNormalize(InputStream isA, InputStream isB, Normalizer normalizer)
            throws IOException
    {
        ByteArrayOutputStream mixedStream = new ByteArrayOutputStream();

        PrimitiveInputStream pisA = new PrimitiveInputStream(isA, format.isBigEndian());
        PrimitiveInputStream pisB = new PrimitiveInputStream(isB, format.isBigEndian());
        PrimitiveOutputStream mixedPrimitiveStream = new PrimitiveOutputStream(mixedStream, format.isBigEndian());

        final int maximumPossibleSignal = SoundUtilities.getMaximumSampleValue(format);
        boolean cont = true;
        int sampleA;
        int sampleB;

        switch (format.getSampleSizeInBits())
        {
            case 8:
                if (SoundUtilities.isSignedEncoding(format.getEncoding()))
                {
                    while (cont)
                    {
                        cont = false;
                        sampleA = 0;
                        sampleB = 0;
                        try
                        {
                            sampleA = pisA.readByte();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        try
                        {
                            sampleB = pisB.readByte();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        int combinedSample = sampleA + sampleB;
                        combinedSample = normalizer.normalize(combinedSample);
                        if (combinedSample > maximumPossibleSignal) combinedSample = maximumPossibleSignal;
                        if (combinedSample < -maximumPossibleSignal - 1)
                        {
                            combinedSample = -maximumPossibleSignal - 1;
                        }
                        mixedPrimitiveStream.writeByte((byte) combinedSample);
                    }
                } else
                {
                    while (cont)
                    {
                        cont = false;
                        sampleA = 0;
                        sampleB = 0;
                        try
                        {
                            sampleA = pisA.readUnsignedByte();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        try
                        {
                            sampleB = pisB.readUnsignedByte();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        int combinedSample = sampleA + sampleB;
                        combinedSample = normalizer.normalize(combinedSample);
                        if (combinedSample > maximumPossibleSignal) combinedSample = maximumPossibleSignal;
                        if (combinedSample < -maximumPossibleSignal - 1)
                        {
                            combinedSample = -maximumPossibleSignal -
                                             1;
                        }
                        mixedPrimitiveStream.writeUnsignedByte(combinedSample);
                    }
                }
                break;
            case 16:
                if (SoundUtilities.isSignedEncoding(format.getEncoding()))
                {
                    while (cont)
                    {
                        cont = false;
                        sampleA = 0;
                        sampleB = 0;
                        try
                        {
                            sampleA = pisA.readShort();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        try
                        {
                            sampleB = pisB.readShort();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        int combinedSample = sampleA + sampleB;
                        combinedSample = normalizer.normalize(combinedSample);
                        if (combinedSample > maximumPossibleSignal) combinedSample = maximumPossibleSignal;
                        if (combinedSample < -maximumPossibleSignal - 1)
                        {
                            combinedSample = -maximumPossibleSignal - 1;
                        }
                        mixedPrimitiveStream.writeShort((short) combinedSample);
                    }
                } else
                {
                    while (cont)
                    {
                        cont = false;
                        sampleA = 0;
                        sampleB = 0;
                        try
                        {
                            sampleA = pisA.readUnsignedShort();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        try
                        {
                            sampleB = pisB.readUnsignedShort();
                            cont = true;
                        } catch (IOException e)
                        {
                            // Not much we can do about this.
                        }
                        int combinedSample = sampleA + sampleB;
                        combinedSample = normalizer.normalize(combinedSample);
                        if (combinedSample > maximumPossibleSignal) combinedSample = maximumPossibleSignal;
                        if (combinedSample < -maximumPossibleSignal - 1)
                        {
                            combinedSample = -maximumPossibleSignal - 1;
                        }
                        mixedPrimitiveStream.writeUnsignedShort(combinedSample);
                    }
                }
                break;
            default:
                throw new IllegalStateException(
                        "This class cannot handle samples of " + format.getSampleSizeInBits() +
                        " bits.");
        }

        mixedPrimitiveStream.close();
        return mixedStream.toByteArray();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This interface is implemented by all normalizers used by this class.
     *
     * @author Zachary Palmer
     */
    interface Normalizer
    {
        /**
         * Performs the normalization operation on the provided sample.
         *
         * @param sample The sample on which normalization will be performed.
         * @return The normalized sample.
         */
        public int normalize(int sample);
    }

    /**
     * This {@link Normalizer} does nothing.
     *
     * @author Zachary Palmer
     */
    static class NullNormalizer implements Normalizer
    {
        /**
         * A singleton instance of this normalizer.
         */
        public static final NullNormalizer SINGLETON = new NullNormalizer();

        /**
         * General constructor.
         */
        public NullNormalizer()
        {
        }

        /**
         * Returns the provided sample.
         *
         * @param sample The sample on which normalization will be performed.
         * @return The <code>sample</code> variable.
         */
        public int normalize(int sample)
        {
            return sample;
        }
    }

    /**
     * This {@link Normalizer} normalizes the samples on a linear scale.  The mixed samples are scanned for their
     * maximum value.  All samples in the mixed stream are then scaled in such a way that the mixed sample with the
     * highest distance from the X-axis is equal to or less than the maximum possible value of the sample.
     *
     * @author Zachary Palmer
     */
    class LinearNormalizer implements Normalizer
    {
        /**
         * The normalizer ratio this normalizer is using.
         */
        protected double normalizerRatio;

        /**
         * General constructor.
         *
         * @param isA The {@link InputStream} for the samples of the first stream being mixed.
         * @param isB The {@link InputStream} for the samples of the first stream being mixed.
         * @throws IOException If an I/O error occurs while establishing the normalizer ratio.
         */
        public LinearNormalizer(InputStream isA, InputStream isB)
                throws IOException
        {
            normalizerRatio = getLinearNormalizerRatio(isA, isB);
        }

        /**
         * Performs the normalization operation on the provided sample.
         *
         * @param sample The sample on which normalization will be performed.
         * @return The normalized sample.
         */
        public int normalize(int sample)
        {
            return (int) (sample * normalizerRatio);
        }
    }
}

// END OF FILE