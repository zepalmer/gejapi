package orioni.jz.io.ratelimited;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This {@link RateLimitedOutputPool} implementation tracks the transmission history of all streams which use its
 * bandwidth.  The stream which has transmitted the least data during the life of the pool is given priority.
 *
 * @author Zachary Palmer
 */
public class LeastFirstRateLimitedOutputPool extends RateLimitedOutputPool
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link HashMap} which is used to map stream ID numbers to total consumed bandwidth during the life of this
     * pool.
     */
    protected Map<Long, Long> bandwidthMap;
    /**
     * The {@link Set} containing the ID numbers of currently-outstanding streams.
     */
    protected Set<Long> outstandingStreams;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param rate The maximum number of bytes/second which can be transferred by all streams in this pool.
     */
    public LeastFirstRateLimitedOutputPool(int rate)
    {
        super(rate);
        bandwidthMap = new HashMap<Long, Long>();
        outstandingStreams = new HashSet<Long>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Called to cancel a transmission that has been approved.  This allows the pool to recover the bandwidth that the
     * request claimed.  This method is called by the write methods on {@link WrapperOutputStream}.
     *
     * @param request The request which has been cancelled.
     */
    protected synchronized void cancelTransmission(TransmissionRequest request)
    {
        super.cancelTransmission(request);
        bandwidthMap.put(request.getStreamId(), getBandwidthUsed(request.getStreamId() - request.getSize()));
    }

    /**
     * Determines the number of outstanding streams.  An outstanding stream is one for which a request has been issued
     * but deferred.
     *
     * @return The number of outstanding streams.
     */
    protected synchronized int getOutstandingStreamCount()
    {
        return outstandingStreams.size();
    }

    /**
     * Determines whether or not the provided request has top priority (i.e., should be acknowledged immediately).  This
     * method is only ever called if there is at least one outstanding stream.
     *
     * @param request The request to consider.
     * @return <code>true</code> if the request should be acknowledged immediately; <code>false</code> if the calling
     *         thread should yield and then try again.
     */
    protected synchronized boolean hasPriority(TransmissionRequest request)
    {
        long total = getBandwidthUsed(request.getStreamId());
        for (Long stream : outstandingStreams)
        {
            if (getBandwidthUsed(stream) < total)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Notifies subclasses that this pool has just acknowledged a request to a specific stream.  This should also mark
     * the stream as no longer outstanding.
     *
     * @param streamId The stream to whom the request has been fulfilled.
     * @param size      The size of the fulfilled request.
     */
    protected synchronized void markAcknowledged(long streamId, int size)
    {
        outstandingStreams.remove(streamId);
        bandwidthMap.put(streamId, getBandwidthUsed(streamId) + size);
    }

    /**
     * Marks a specific stream ID as outstanding.
     *
     * @param streamId The stream ID to mark as outstanding.
     */
    protected synchronized void markOutstanding(long streamId)
    {
        outstandingStreams.add(streamId);
    }

    /**
     * Retrieves the total bandwidth consumption for the specified stream.  This convenience method substitutes zero if
     * no entry exists in the bandwidth map.
     *
     * @param streamId The ID number of the stream for which a total bandwidth is requested.
     * @return The total bandwidth for that stream.
     */
    protected synchronized long getBandwidthUsed(long streamId)
    {
        Long total = bandwidthMap.get(streamId);
        if (total == null) return 0; else return total;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE