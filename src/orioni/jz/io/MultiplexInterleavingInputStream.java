package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * This extension of <code>MultiplexInputStream</code> allows a user to draw data from several interleaved input sources
 * at one time.  Several <code>InputStream</code> objects are passed to this object on construction and will be used for
 * input throughout this object's operation.  A "block size" is specified on construction as well.
 * <p/>
 * The multiplexing initializes on stream zero.  A number of bytes equal to the block size will be read from stream zero
 * as this stream needs to provide data to its callers.  Once the block size is read from that stream, it goes to the
 * end of the stream queue and the next stream (stream one) provides the next set of bytes equal to the block size. In
 * the event that reading from one of the streams produces an exception, this stream will continue to throw that
 * exception until the problem is resolved.
 * <p/>
 * Note that, on specification of a stream during construction, a single stream can be specified multiple times to
 * change the pattern with which the streams are used.
 *
 * @author Zachary Palmer
 */
public class MultiplexInterleavingInputStream extends MultiplexInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The array in which the source <code>InputStream</code objects are stored.
     */
    protected InputStream[] sources;
    /**
     * An array containing reference to unique <code>InputStream</code> data sources.
     */
    protected InputStream[] uniqueSources;
    /**
     * The block size of the reads for this stream.
     */
    protected int blockSize;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param sources    The <code>InputStream</code>s from which data will be read in an interleaving fashion.
     * @param blockSize The number of bytes to read from a given stream before moving to the next one.
     */
    public MultiplexInterleavingInputStream(InputStream[] sources, int blockSize)
    {
        super();
        this.blockSize = blockSize;
        this.sources = sources;
        HashSet<InputStream> set = new HashSet<InputStream>();
        for (final InputStream source : this.sources)
        {
            set.add(source);
        }

        uniqueSources = set.toArray(new InputStream[0]);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method closes all of the data streams that were provided to this stream upon construction.
     *
     * @throws IOException If an I/O error occurs while closing one of the streams.
     */
    public void close()
            throws IOException
    {
        for (final InputStream source : uniqueSources) source.close();
    }

    /**
     * Returns an array of unique references to the data sources that were provided on construction.  Note that, if a
     * single source was provided twice upon construction, it will only appear once here.
     *
     * @return An array of <code>InputStream</code>s that this stream is using.
     */
    public InputStream[] getSources()
    {
        return uniqueSources;
    }

    /**
     * This method provides input streams from which further data can be read.  Any number of streams may be provided by
     * this method; the more streams provided, the more information that {@link MultiplexInputStream} will have to
     * improve the efficiency of its operations.
     * <p/>
     * {@link MultiplexInputStream} guarantees that the provided streams will be fully exhausted before this method is
     * called again.
     *
     * @return At least one {@link InputStream} from which data should be read, or <code>null</code> if no additional
     *         streams can be obtained.
     * @throws IOException If there is a problem creating or retrieving the stream.
     */
    public InputStream[] getMoreStreams()
            throws IOException
    {
        ArrayList<InputStream> ret = new ArrayList<InputStream>();
        for (InputStream is : sources)
        {
            ret.add(new SizeLimitedInputStream(is, blockSize));
        }
        return ret.toArray(new InputStream[0]);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //