package orioni.jz.io.sound;

import orioni.jz.io.PrimitiveInputStream;
import orioni.jz.io.PrimitiveOutputStream;
import orioni.jz.io.bit.BitInputStream;
import orioni.jz.io.bit.BitOrder;
import orioni.jz.io.bit.BitOutputStream;
import orioni.jz.io.bit.EndianFormat;
import orioni.jz.math.MathUtilities;

import javax.sound.sampled.AudioFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed to contain a "difference compressed" sampled sound.  It does not extend the {@link
 * SampledSound} class because the data contained within must be decompressed before any operations can be performed
 * upon it.  The compression method used is described below.
 * <p/>
 * First, the audio data is stored as differences rather than samples.  The "first" sample (which would appear at index
 * -1) is assumed to be <code>0</code>.  Thus, if the sample data proceeded <code>[3,5,7,8,7,2,-3,-5,-2,0]</code>, the
 * difference array for that sample would be <code>[3,2,2,1,-1,-5,-5,-2,3,2]</code>.  Note that the difference array and
 * the sample array are the same size.
 * <p/>
 * Next, the difference array is encoded bitwise in regions smaller than the sample size.  The difference array is then
 * divided into segments; difference values in each segment are represented in terms of the same bit size, but different
 * segments may have different bit sizes.  In the example above, the first five values (<code>[3,2,2,1,-1]</code>) can
 * be represented in terms of a signed 3-bit value; the next two values (<code>[-5,-5]</code>) would require a signed
 * 4-bit value; the final three values could be represented by a signed 3-bit value.
 * <p/>
 * Thus, the encoded data will appear in the following manner: <ul><code>Header<sub>1</sub> Data<sub>1</sub>
 * Header<sub>2</sub> Data<sub>2</sub> ... Header<sub><b>n</b></sub> Data<sub><b>n</b></sub></ul> The header consists of
 * a single value which indicates the size of the difference values in the next data segment. The actual value stored is
 * the bitwise inverse of the actual value; the resoning for this is described below.  This value is written as a four
 * bit value with a zero-point of two; that is, a value of <code>1111</code> represents the number two, a value of
 * <code>1110</code> represents the number three, and so on.  In this way, a data segment can represent difference
 * values of up to 17 bits (see below).  Since all difference values are signed, this is sufficient to span any
 * difference value for sample sizes up to 16 bits in length.
 * <p/>
 * The data segment consists of an arbitrary number of difference values (of a size specified by the most recent header)
 * followed by a terminator; the definition of a terminator is best described by example.  In the case of a header
 * specifying 4-bit difference values, the range of possible values is <code>1000</code> through <code>0111</code>
 * (<code>-8</code> to <code>7</code> decimal).  A terminator starts by signaling the lowest possible value of a range
 * (in this case, <code>1000</code>).  If the next bit is <code>0</code>, the value <code>-8</code> is intended; if that
 * bit is <code>1</code>, this is a terminator, which indicates the end of this data segment.  The bit determining the
 * terminator state of the value is then discarded.
 * <p/>
 * In the case of a multi-channel (ex. stereo) stream, the encoding is interleaved.  For example, if two channels exist
 * (such as in a stereo wave), the encoding process uses the first sample of the first channel, followed by the first
 * sample of the second channel, followed by the second sample of the first channel.  This is done because it simplifies
 * encoding and is likely to improve performance (as the two channels are likely to have significant similarities).
 * <p/>
 * The purpose of encoding the frame bitsize in its inverse is to protect against confusion when decoding the bitstream.
 * Since up to seven padded zero bits may be stored at the end of the stream, it is important for the decoder to be able
 * to distinguish between the padding and actual data.
 * <p/>
 * For example, consider the case in which a decoder has reached the end of the actual data and has seven zero bits
 * remaining.  Assume that the bitwise inversion is not performed.  In this case, the first four zeroes will be read as
 * a header (incidating a two-bit sample size) and the next two bits will be read as a sample.  This sample was
 * generated out of padding and, therefore, is erroneous.  However, if the bitwise inversion is performed, the decoder
 * will interpret the four zero "header" as indicating a segment with a bit size of 17.  The decoder can then attempt to
 * read 17 bits and will instead reach then end of the stream; no erroneous samples have been added.
 * <p/>
 * The storage of the bits is defined by the {@link AudioFormat} of the sample data.  The endianness of the {@link
 * AudioFormat} affects how data is stored.  Data is always stored lowest bit first (see {@link BitOrder}).
 */
public class DifferenceCompressedSampledSound implements Serializable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The number of bits used to describe the header.
     */
    protected static final int HEADER_SIZE_IN_BITS = 4;
    /**
     * The zero point for the header.
     */
    protected static final int HEADER_ZERO_POINT = 2;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The data for this object.
     */
    protected byte[] data;
    /**
     * The {@link AudioFormat} of the uncompressed sample data.
     */
    protected AudioFormat format;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param sound The {@link SampledSound} on which to base the data for this object.
     */
    public DifferenceCompressedSampledSound(SampledSound sound)
    {
        super();
        format = sound.getAudioFormat();
        data = performDifferenceCompression(sound.getSampleData(), format);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Decompresses this {@link DifferenceCompressedSampledSound}'s contents, producing a {@link SampledSound} which
     * represents the same {@link SampledSound} which was used to create this object.
     *
     * @return A {@link SampledSound} which contains the data which is compressed in this object.
     */
    public SampledSound decompress()
    {
        BitInputStream bis = new BitInputStream(
                new ByteArrayInputStream(data), BitOrder.LOWEST_BIT_FIRST,
                format.isBigEndian() ? EndianFormat.BIG_ENDIAN : EndianFormat.LITTLE_ENDIAN);
        ByteArrayOutputStream sampleBuffer = new ByteArrayOutputStream(data.length * 2);
        PrimitiveOutputStream pos = new PrimitiveOutputStream(sampleBuffer, format.isBigEndian());

        int lastValue = 0;
        try
        {
            while (true)
            {
                int segmentBitsize =
                        MathUtilities.setRelevantBitCount(~bis.readBits(HEADER_SIZE_IN_BITS), HEADER_SIZE_IN_BITS) +
                        HEADER_ZERO_POINT;
                boolean terminatorFound = false;
                final int terminatorSignal = ~MathUtilities.setRelevantBitCount(~0, segmentBitsize - 1);
                while (!terminatorFound)
                {
                    int sampleData = bis.readBitsSigned(segmentBitsize);
                    if ((sampleData == terminatorSignal) && (bis.readBoolean()))
                    {
                        terminatorFound = true;
                    } else
                    {
                        lastValue += sampleData;
                        if (format.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED)
                        {
                            switch (format.getSampleSizeInBits())
                            {
                                case 8:
                                    pos.writeUnsignedByte(lastValue);
                                    break;
                                case 16:
                                    pos.writeUnsignedShort(lastValue);
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Unsupported sample data size in bits: " + format.getSampleSizeInBits());
                            }
                        } else if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED)
                        {
                            switch (format.getSampleSizeInBits())
                            {
                                case 8:
                                    pos.writeByte((byte) lastValue);
                                    break;
                                case 16:
                                    pos.writeShort((short) lastValue);
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Unsupported sample data size in bits: " + format.getSampleSizeInBits());
                            }
                            sampleBuffer.write(lastValue);
                        }
                    }
                }
            }
        } catch (EOFException e)
        {
            // This is how we tell that we're finished.  Sloppy, yes, but there doesn't appear to be another way
            // to handle the padding at the end of the array.
        } catch (IOException e)
        {
            e.printStackTrace();  // TODO: Consider exception handling.
        }

        return new SampledSound(sampleBuffer.toByteArray(), format);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    // TODO: test this method more extensively... it does not seem to work under some cases (16-bit, I think)

    /**
     * Compresses the provided <code>byte[]</code> (which is assumed to be stored in the provided {@link AudioFormat})
     * using the algorithm described in the documentation of this class.  The resulting compressed data is returned as a
     * <code>byte[]</code>.
     * <p/>
     * At the present time, only PCM audio data is supported.
     *
     * @param data   The data to compress.
     * @param format The {@link AudioFormat} in which the data is stored.
     * @throws IllegalArgumentException If the sample size in bits of the provided format is not divisible by eight.
     */
    protected static byte[] performDifferenceCompression(byte[] data, AudioFormat format)
    {
        PrimitiveInputStream pis = new PrimitiveInputStream(new ByteArrayInputStream(data), format.isBigEndian());
        if (format.getSampleSizeInBits() % 8 != 0)
        {
            throw new IllegalArgumentException(
                    "Uncompressed sample data must have a sample size in bits divisible by 8 (was " +
                    format.getSampleSizeInBits() + ").");
        }

        int[] sampleBuffer;
        int index;

        // Translate the sample data into a buffer.
        try
        {
            if (format.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED)
            {
                switch (format.getSampleSizeInBits())
                {
                    case 8:
                        sampleBuffer = new int[data.length];
                        index = 0;
                        try
                        {
                            while (true)
                            {
                                sampleBuffer[index++] = pis.readUnsignedByte();
                            }
                        } catch (EOFException eofe)
                        {
                        }
                        break;
                    case 16:
                        sampleBuffer = new int[data.length / 2];
                        index = 0;
                        try
                        {
                            while (true)
                            {
                                sampleBuffer[index++] = pis.readUnsignedShort();
                            }
                        } catch (EOFException eofe)
                        {
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unsupported sample data size in bits: " + format.getSampleSizeInBits());
                }
            } else if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED)
            {
                switch (format.getSampleSizeInBits())
                {
                    case 8:
                        sampleBuffer = new int[data.length];
                        index = 0;
                        try
                        {
                            while (true)
                            {
                                sampleBuffer[index++] = pis.readByte();
                            }
                        } catch (EOFException eofe)
                        {
                        }
                        break;
                    case 16:
                        sampleBuffer = new int[data.length / 2];
                        index = 0;
                        try
                        {
                            while (true)
                            {
                                sampleBuffer[index++] = pis.readShort();
                            }
                        } catch (EOFException eofe)
                        {
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unsupported sample data size in bits: " + format.getSampleSizeInBits());
                }
            } else
            {
                throw new IllegalArgumentException(
                        "This compression method can only accept PCM audio data (format was " + format + ").");
            }
        } catch (IOException ioe)
        {
            // This shouldn't happen... we're reading from and writing to buffers only.
            throw new IllegalStateException("Buffer stream threw an exception!", ioe);
        }

        // Rewrite the sample data in terms of difference values.  Simultaneously, analyze these values and determine
        // the most efficient segmenting pattern for storage.
        int lastValue = 0;
        ArrayList<Segment> segmentList = new ArrayList<Segment>();
        Segment latestSegment = null;
        for (int i = 0; i < sampleBuffer.length; i++)
        {
            int temp = lastValue;
            lastValue = sampleBuffer[i];
            sampleBuffer[i] -= temp;

            int bitsRequired = (byte) (
                    Math.max(
                            HEADER_ZERO_POINT,
                            MathUtilities.countSignificantBits(
                                    (sampleBuffer[i] >= 0) ? sampleBuffer[i] : -sampleBuffer[i] - 1) + 1));
            if ((latestSegment == null) || (latestSegment.getBitSize() != bitsRequired))
            {
                latestSegment = new Segment(bitsRequired, 1);
                segmentList.add(latestSegment);
                // As each segment is created, consider compacting it with the adjacent segments.
                if (segmentList.size() > 2)
                {
                    testSegmentCompact(segmentList, -3);
                }
            } else
            {
                latestSegment.setSampleCount(latestSegment.getSampleCount() + 1);
            }
        }
        if (segmentList.size() > 1)
        {
            testSegmentCompact(segmentList, -2);
        }

        // * Write the encoded data.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(
                buffer, BitOrder.LOWEST_BIT_FIRST,
                format.isBigEndian() ? EndianFormat.BIG_ENDIAN : EndianFormat.LITTLE_ENDIAN);
        index = 0;
        try
        {
            for (Segment s : segmentList)
            {
                // Write header.
                int bitSize = s.getBitSize();
                bos.writeBits(~(bitSize - HEADER_ZERO_POINT), HEADER_SIZE_IN_BITS);
                // Write data.
                int remaining = s.getSampleCount();
                final int terminatorSignal = ~MathUtilities.setRelevantBitCount(~0, bitSize - 1);
                while (remaining > 0)
                {
                    remaining--;
                    int sample = sampleBuffer[index++];
                    bos.writeBits(sample, bitSize);
                    if (sample == terminatorSignal) bos.writeBit(false);
                }
                // Write terminator.
                bos.writeBits(terminatorSignal, bitSize);
                bos.writeBit(true);
            }

            // Close the buffer streams.
            bos.close();
            buffer.close();
        } catch (IOException e)
        {
            // This can only happen if an IOException is thrown by the ByteArrayOutputStream or if something goes
            // severely wrong with BitOutputStream.
            throw new IllegalStateException("Unexpected I/O problem using buffer streams.", e);
        }

        // Return the encoded data.
        return buffer.toByteArray();
    }

    /**
     * Used to determine whether or not two {@link Segment}s in the provided segment list should be compacted.
     *
     * @param list  The list of {@link Segment}s.
     * @param index The index from the <i>end</i> of the list of the first segment to consider.
     */
    private static void testSegmentCompact(List<Segment> list, int index)
    {
        index += list.size();
        Segment a = list.get(index);
        Segment b = list.get(index + 1);
        // We need to compact the segments since changing segments creates an overhead.
        if (a.getBitSize() == b.getBitSize())
        {
            // These two segments must be joined; they're the same size.
            list.remove(index);
            b.setSampleCount(a.getSampleCount() + b.getSampleCount());
        } else if (a.getBitSize() < b.getBitSize())
        {
            // Segment A has a smaller bitsize than Segment B.  The question, then, is whether or not Segment A should
            // be discarded and Segment B fill its space.  This should be done if the amount of space lost by this
            // operation is less than the number of bits required to generate Segment B's header.
            int spaceLostByTossingInBits =
                    a.getSampleCount() * (b.getBitSize() - a.getBitSize());
            int spaceRequiredByBHeader = a.getBitSize() + 1 + HEADER_SIZE_IN_BITS;
            if (spaceLostByTossingInBits < spaceRequiredByBHeader)
            {
                list.remove(index);
                b.setSampleCount(a.getSampleCount() + b.getSampleCount());
            }
        } else
        {
            // Segment A has a larger bitsize than Segment B.  The question, then, is whether or not to absorb Segment B
            // into Segment A.  This should be done if the number of bits required to write Segment B's is larger than
            // the number of bits that would be wasted by incorporating it into Segment A.
            int spaceLostByAbsorbingInBits =
                    b.getSampleCount() * (a.getBitSize() - b.getBitSize());
            int spaceRequiredByBHeader = a.getBitSize() + 1 + HEADER_SIZE_IN_BITS;
            if (spaceRequiredByBHeader >= spaceLostByAbsorbingInBits)
            {
                list.remove(index + 1);
                a.setSampleCount(a.getSampleCount() + b.getSampleCount());
            }
        }
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class is designed to contain information about a segment in an encoded sample.  It is solely a mutable data
     * container for two integers.
     *
     * @author Zachary Palmer
     */
    static class Segment
    {
        /**
         * The size of this segment in samples.
         */
        protected int sampleCount;
        /**
         * The bit size of samples in this segment.
         */
        protected int bitSize;

        /**
         * General constructor.
         *
         * @param bitSize     The bit size of samples in this segment.
         * @param sampleCount The size of this segment in samples.
         */
        public Segment(int bitSize, int sampleCount)
        {
            this.sampleCount = sampleCount;
            this.bitSize = bitSize;
        }

        public int getBitSize()
        {
            return bitSize;
        }

        public void setBitSize(int bitSize)
        {
            this.bitSize = bitSize;
        }

        public int getSampleCount()
        {
            return sampleCount;
        }

        public void setSampleCount(int sampleCount)
        {
            this.sampleCount = sampleCount;
        }

        public String toString()
        {
            return bitSize + "b*" + sampleCount;
        }
    }
}

// END OF FILE
