package orioni.jz.io;

import orioni.jz.util.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

/**
 * This extension of the <code>MultiplexOutputStream</code> allows the user to interleave output to multiple output
 * targets.  On construction, an <code>OutputStream[]</code> is passed in along with a block size.  The interleaving
 * output stream starts using element zero in the target array and writes a number of bytes to it up to the block size.
 * Once a full block has been written to element zero, the interleaving stream moves to element one, and so forth.  Once
 * all targets have receieved one block, the process starts anew. <P> Note that, on construction, the same
 * <code>OutputStream</code> can be specified multiple times as a target within the target array.  The array is still
 * processed in order and the target in question receives proportionally more data.
 *
 * @author Zachary Palmer
 */
public class MultiplexInterleavingOutputStream extends MultiplexOutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The array in which the source <code>OutputStream</code objects are stored.
     */
    protected OutputStream[] targets;
    /**
     * An array containing reference to unique <code>OutputStream</code> data sources.
     */
    protected OutputStream[] uniqueTargets;
    /**
     * The block size of the reads for this stream.
     */
    protected int blockSize;

    /**
     * The index of the current data source.
     */
    protected int targetIndex;
    /**
     * How many bytes have been read from the current block.
     */
    protected int blockIndex;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param targets    The <code>OutputStream[]</code> containing all targets for writing by this stream.
     * @param blockSize The size of a block of data to write to a target before moving to the next target.
     */
    public MultiplexInterleavingOutputStream(OutputStream[] targets, int blockSize)
    {
        this.targets = targets;
        this.blockSize = blockSize;
        blockIndex = 0;
        targetIndex = 0;

        HashSet<OutputStream> set = new HashSet<OutputStream>();
        for (final OutputStream target : this.targets)
        {
            set.add(target);
        }
        uniqueTargets = set.toArray(new OutputStream[0]);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method retrieves the {@link OutputStream} to which the provided data should be written, combined with the
     * total amount of data which should be written to that stream.  This method is used to control the manner in which
     * data is written to the {@link OutputStream}s.
     *
     * @param data The array containing data which is to be written to some output stream.
     * @param off  The starting offset of the data in the array.
     * @param len  The length of the data to be written to an {@link OutputStream}.
     * @return A {@link Pair}<code>&lt;{@link OutputStream},<code>int&gt;</code>.  The {@link OutputStream} is the
     *         stream to which data should be written; the <code>int</code> is the amount of data to write.
     */
    public Pair<OutputStream, Integer> getNextWriteOperation(byte[] data, int off, int len)
    {
        int writeSize = Math.min(len, blockSize - blockIndex);
        Pair<OutputStream, Integer> ret = new Pair<OutputStream, Integer>(targets[targetIndex], writeSize);

        blockIndex += writeSize;
        if (blockIndex == blockSize)
        {
            blockIndex = 0;
            targetIndex++;
            if (targetIndex >= targets.length) targetIndex = 0;
        }

        return ret;
    }

    /**
     * This method closes all of the underlying target streams that this stream is using.
     *
     * @throws IOException If closing one of the output targets throws an I/O exception.
     */
    public void close()
            throws IOException
    {
        flush();
        for (final OutputStream target : uniqueTargets) target.close();
    }

    /**
     * Retrieves all of the unique underlying target streams that this stream is using.
     *
     * @return An <code>OutputStream[]</code> containing the unique output targets for this stream.  Note that this
     *         array will be smaller than the one passed in at construction time if any <code>OutputStream</code> target
     *         provided to the constructor is in that array more than once.
     */
    public OutputStream[] getAllTargets()
    {
        return uniqueTargets;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //