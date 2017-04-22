package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This {@link MultiplexInputStream} implementation reads data from each stream in order.  Once one stream is completely
 * exhausted, it starts on the next stream; it continues this until all streams are exhausted, at which point it reports
 * an end of stream.
 *
 * @author Zachary Palmer
 */
public class MultiplexSequentialInputStream extends MultiplexInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link InputStream}s which are to be multiplexed.
     */
    protected InputStream[] streams;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param streams The {@link InputStream}s to sequentialize.
     */
    public MultiplexSequentialInputStream(InputStream... streams)
    {
        super();
        this.streams = streams;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

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
        InputStream[] ret = streams;
        streams = null;
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE