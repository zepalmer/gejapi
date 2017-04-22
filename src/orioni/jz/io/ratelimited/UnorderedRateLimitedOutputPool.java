package orioni.jz.io.ratelimited;

import java.util.Random;

/**
 * This {@link RateLimitedOutputPool} implementation doesn't actually take the individual making the request into
 * account when making its decisions about wait time; it simply acknowledges any request until it runs out of bandwidth
 * for those requests and then instructs requests to wait until more bandwidth is available.  If incoming requests are
 * asked to wait, the first of them permitted to transmit is the first that tries again after bandwidth is available.
 * <p/>
 * Unless a specific ordering method is desired or response time is a requirement, this pool is usually sufficient.
 *
 * @author Zachary Palmer
 */
public class UnorderedRateLimitedOutputPool extends RateLimitedOutputPool
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A random number generator used to add entropy to the order in which requests are fulfilled.
     */
    protected Random random;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param rate The maximum rate, in bytes per second, of all streams in this pool.
     */
    public UnorderedRateLimitedOutputPool(int rate)
    {
        super(rate);
        random = new Random();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the amount of time that the caller should wait before checking to see if the specified request has been
     * acknowledged.  This value may be less than or equal to zero, which indicates that no wait should occur and that
     * the provided request has been acknowledged.  No write should occur, however, until the transmission request is
     * actually checked for acknowledgement.
     *
     * @param request The request for which a wait time is desired.
     * @return The amount of time to wait.  May be zero or less than zero, indicating that no wait should occur.
     */
    protected synchronized long getWaitTime(TransmissionRequest request)
    {
        long time = super.getWaitTime(request);
        return time+random.nextInt(10);
    }

    /**
     * This pool never treats streams as outstanding.
     *
     * @return <code>0</code>, always.
     */
    protected int getOutstandingStreamCount()
    {
        return 0;
    }

    /**
     * As no stream is ever outstanding, all streams are treated with equal priority.  Therefore, any stream presented
     * to this method has priority.
     *
     * @param request Ignored.
     * @return <code>true</code>, always.
     */
    protected boolean hasPriority(TransmissionRequest request)
    {
        return true;
    }

    /**
     * Ignored.
     *
     * @param streamId Ignored.
     * @param size      Ignored.
     */
    protected void markAcknowledged(long streamId, int size)
    {
    }

    /**
     * Ignored.
     *
     * @param streamId Ignored.
     */
    protected void markOutstanding(long streamId)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE