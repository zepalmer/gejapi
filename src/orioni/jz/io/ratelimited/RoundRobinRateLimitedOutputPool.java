package orioni.jz.io.ratelimited;

import java.util.Set;
import java.util.ArrayList;


/**
 * This {@link RateLimitedOutputPool} uses a simple round robin approach in dealing with requests.  The backlogged
 * request of the stream whose last acknowledged request is oldest is given priority.  In an attempt to provide
 * additional fairness, the maximum amount of data allocated to any request is equal to the rate of this pool divided by
 * the number of present streams and rounded up.
 *
 * @author Zachary Palmer
 */
public class RoundRobinRateLimitedOutputPool extends RateLimitedOutputPool
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Set} of streams known to have requests outstanding.
     */
    protected ArrayList<Long> outstandingStreams;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param rate The maximum rate at which data may pass through this pool.
     */
    public RoundRobinRateLimitedOutputPool(int rate)
    {
        super(rate);
        outstandingStreams = new ArrayList<Long>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines the number of outstanding streams.  An outstanding stream is one for which a request has been issued
     * but deferred.
     *
     * @return The number of outstanding streams.
     */
    protected int getOutstandingStreamCount()
    {
        return outstandingStreams.size();
    }

    /**
     * Notifies subclasses that this pool has just acknowledged a request to a specific stream.  This should also mark
     * the stream as no longer outstanding.
     *
     * @param streamId The stream to whom the request has been fulfilled.
     * @param size      The size of the fulfilled request.
     */
    protected void markAcknowledged(long streamId, int size)
    {
        outstandingStreams.remove(streamId);
    }

    /**
     * Marks a specific stream ID as outstanding.
     *
     * @param streamId The stream ID to mark as outstanding.
     */
    protected void markOutstanding(long streamId)
    {
        outstandingStreams.remove(streamId);
        outstandingStreams.add(streamId);
    }

    /**
     * Determines whether or not the provided request has top priority (i.e., should be acknowledged immediately).  This
     * method is only ever called if there is at least one outstanding stream.
     *
     * @param request The request to consider.
     * @return <code>true</code> if the request should be acknowledged immediately; <code>false</code> if the calling
     *         thread should yield and then try again.
     */
    protected boolean hasPriority(TransmissionRequest request)
    {
        return (request.getStreamId() == outstandingStreams.get(0));
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE