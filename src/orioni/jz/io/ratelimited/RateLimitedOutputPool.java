package orioni.jz.io.ratelimited;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is designed to monitor output rate for a set of {@link OutputStream}s.  Like {@link
 * RateLimitedOutputStream}, a transfer rate specified in bytes per second is specified and the output of the streams is
 * limited to a maximum of that rate.  This class, however, produces wrapper streams to allow a group of streams to
 * consume at most the specified amount of transfer bandwidth.  For example, if four streams participated in the pool
 * with a maximum rate of 102,400 bytes (100K) per second, then each stream would get approximately 25K/sec on average.
 * If, however, three of the streams were not transmitting data, the fourth stream could consume the entire allotted
 * 100K/sec transfer rate.
 * <p/>
 * Streams are limited by this pool by calling the pool's {@link RateLimitedOutputPool#wrapStream(OutputStream)} method.
 * The returned {@link OutputStream} will then be rate-limited by this pool; the original {@link OutputStream} remains
 * unaffected.
 * <p/>
 * Prioritization of the streams is based upon the subclass's implementation of the abstract methods on this class.
 *
 * @author Zachary Palmer
 */
public abstract class RateLimitedOutputPool
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The next ID number to be assigned to a wrapper stream.
     */
    protected long nextId = 0;
    /**
     * The synchronization object for access to the {@link RateLimitedOutputPool#nextId} field.
     */
    protected final Object sync = new Object();
    /**
     * The maximum number of bytes per second to transmit through this pool's streams.  Access to this field must occur
     * in a synchronized context; therefore, this field is private.
     */
    private int rate;
    /**
     * The number of remaining bytes left for this second of transmission.
     */
    protected int remaining;
    /**
     * The last time at which the remaining byte pool was refilled.
     */
    protected long lastRefillTime;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param rate The maximum number of bytes/second which can be transferred by all streams in this pool.
     */
    public RateLimitedOutputPool(int rate)
    {
        super();
        setRate(rate);
        remaining = getRate();
        lastRefillTime = System.currentTimeMillis();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Creates a wrapper stream for the provided {@link OutputStream}.  The returned wrapper stream will be rate-limited
     * by this pool; the original stream will be unaffected.
     *
     * @param stream The {@link OutputStream} to wrap.
     */
    public OutputStream wrapStream(OutputStream stream)
    {
        synchronized (sync)
        {
            return new WrapperOutputStream(stream, nextId++);
        }
    }

    /**
     * Called to cancel a transmission that has been approved.  This allows the pool to recover the bandwidth that the
     * request claimed.  This method is called by the write methods on {@link WrapperOutputStream}.
     *
     * @param request The request which has been cancelled.
     */
    protected void cancelTransmission(TransmissionRequest request)
    {
        synchronized (this)
        {
            if (request.getAcknowledgedTime() >= lastRefillTime)
            {
                remaining += request.getSize();
            }
        }
    }

    /**
     * Determines whether or not the provided request has top priority (i.e., should be acknowledged immediately).  This
     * method is only ever called if there is at least one outstanding stream.
     *
     * @param request The request to consider.
     * @return <code>true</code> if the request should be acknowledged immediately; <code>false</code> if the calling
     *         thread should yield and then try again.
     */
    protected abstract boolean hasPriority(TransmissionRequest request);

    /**
     * Determines the number of outstanding streams.  An outstanding stream is one for which a request has been issued
     * but deferred.
     *
     * @return The number of outstanding streams.
     */
    protected abstract int getOutstandingStreamCount();

    /**
     * Marks a specific stream ID as outstanding.
     *
     * @param streamId The stream ID to mark as outstanding.
     */
    protected abstract void markOutstanding(long streamId);

    /**
     * Notifies subclasses that this pool has just acknowledged a request to a specific stream.  This should also mark
     * the stream as no longer outstanding.
     *
     * @param streamId The stream to whom the request has been fulfilled.
     * @param size      The size of the fulfilled request.
     */
    protected abstract void markAcknowledged(long streamId, int size);

    /**
     * Retrieves the amount of time that the caller should wait before checking to see if the specified request has been
     * acknowledged.  If not enough bandwidth remains to fulfill a request, it is deferred to the outstanding streams
     * list and processed later.
     *
     * @param request The request for which a wait time is desired.
     * @return The amount of time to wait.  May be zero or less than zero, indicating that no wait should occur.  If the
     *         return value is {@link Long#MIN_VALUE}, a yield should occur.
     */
    protected synchronized long getWaitTime(TransmissionRequest request)
    {
        if (lastRefillTime + 1000 <= System.currentTimeMillis())
        {
            lastRefillTime = System.currentTimeMillis();
            remaining = getRate();
        }
        int maxSize = (getOutstandingStreamCount() == 0 ?
                        getRate() :
                        (getRate() - 1) / getOutstandingStreamCount() + 1);
        if (remaining > 0)
        {
            if ((getOutstandingStreamCount() == 0) || (hasPriority(request)))
            {
                request.setSize(Math.min(Math.min(remaining, maxSize), request.getSize()));
                remaining -= request.getSize();
                request.acknowledge();
                markAcknowledged(request.getStreamId(), request.getSize());
                return 0;
            } else
            {
                markOutstanding(request.getStreamId());
                return Long.MIN_VALUE;
            }
        } else
        {
            markOutstanding(request.getStreamId());
            return (lastRefillTime + 1000 - System.currentTimeMillis());
        }
    }

    /**
     * Called to request permission to send the provided amount of data.  The calling thread will not return from this
     * method until transmission of the data is acceptable.
     *
     * @param request The {@link TransmissionRequest} representing the request being made.
     */
    void requestTransmission(TransmissionRequest request)
    {
        while (request.getAcknowledgedTime() == Long.MIN_VALUE)
        {
            long time = getWaitTime(request);
            if (time > 0)
            {
                try
                {
                    Thread.sleep(time);
                } catch (InterruptedException e)
                {
                }
            } else if (time == Long.MIN_VALUE)
            {
                Thread.yield();
            }
        }
    }

    /**
     * Retrieves the current rate of this {@link RateLimitedOutputPool}.
     *
     * @return The maximum number of bytes allowed to be transmitted through this pool during any given second.
     */
    public synchronized int getRate()
    {
        return rate;
    }

    /**
     * Sets the rate of this {@link RateLimitedOutputPool}.
     *
     * @param rate The new rate for this pool in bytes per second.
     * @throws IllegalArgumentException If the provided rate is less than <code>1</code>.
     */
    public synchronized void setRate(int rate)
    {
        if (rate > 0)
        {
            synchronized (this)
            {
                this.rate = rate;
            }
        } else
        {
            throw new IllegalArgumentException("Invalid rate: " + rate + " (must be greater than zero)");
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class is the wrapper stream class for the {@link RateLimitedOutputPool} class.  Each {@link
     * RateLimitedOutputPool} assigns a series of 64-bit unique ID numbers to their wrapper streams for purposes of
     * idenitification by the {@link RateLimitedOutputPool}s.  This stream calls the {@link RateLimitedOutputPool}'s
     * {@link RateLimitedOutputPool#requestTransmission(TransmissionRequest) requestTransmission(TransmissionRequest)}
     * method before actually performing a write.  If the write generates an {@link IOException}, then it is presumed
     * that the data was not send and {@link RateLimitedOutputPool#cancelTransmission(TransmissionRequest)} is called to
     * allow the pool to reclaim the lost bandwidth.
     *
     * @author Zachary Palmer
     */
    class WrapperOutputStream extends OutputStream
    {
        /**
         * The underlying target {@link OutputStream}.
         */
        protected OutputStream target;
        /**
         * The unique ID number for this stream.
         */
        protected long id;

        /**
         * General constructor.
         *
         * @param target The underlying target {@link OutputStream}.
         * @param id     The ID number for this stream.
         */
        WrapperOutputStream(OutputStream target, long id)
        {
            this.target = target;
            this.id = id;
        }

        /**
         * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte
         * is written to the output stream. The byte to be written is the eight low-order bits of the argument
         * <code>b</code>. The 24 high-order bits of <code>b</code> are ignored.
         * <p/>
         * This call may be delayed to allow for rate-limited performance.
         *
         * @param b the <code>byte</code>.
         * @throws IOException If an I/O error occurs in the underlying stream.
         */
        public synchronized void write(int b)
                throws IOException
        {
            boolean repeat;
            TransmissionRequest request;
            do
            {
                request = new TransmissionRequest(id, 1);
                requestTransmission(request);
                repeat = (request.getSize() == 0);
            } while (repeat);
            try
            {
                target.write(b);
            } catch (IOException ioe)
            {
                cancelTransmission(request);
            }
        }

        /**
         * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this
         * output stream. The general contract for <code>write(b, off, len)</code> is that some of the bytes in the
         * array <code>b</code> are written to the output stream in order; element <code>b[off]</code> is the first byte
         * written and <code>b[off+len-1]</code> is the last byte written by this operation.
         * <p/>
         * This method performs writes against the underlying target stream in increments of a size equal to the rate of
         * the pool to which it belongs.  It may block to allow for rate-limiting behavior.
         *
         * @param b   the data.
         * @param off the start offset in the data.
         * @param len the number of bytes to write.
         * @throws IOException if an I/O error occurs. In particular, an <code>IOException</code> is thrown if the
         *                     output stream is closed.
         */
        public void write(byte[] b, int off, int len)
                throws IOException
        {
            while (len > 0)
            {
                int toWrite = Math.max(len, getRate());
                TransmissionRequest request = new TransmissionRequest(id, toWrite);
                requestTransmission(request);
                toWrite = request.getSize();
                try
                {
                    target.write(b, off, toWrite);
                    off += toWrite;
                    len -= toWrite;
                } catch (IOException ioe)
                {
                    cancelTransmission(request);
                }
            }
        }

        /**
         * Flushes this output stream and forces any buffered output bytes to be written out.  This method does nothing
         * except by forcing the caller to wait for the monitor on this object, allowing any pending writes to
         * complete.
         */
        public synchronized void flush()
        {
        }
    }

    /**
     * This class represents transmission requests that are created by the wrapper streams which are constructed by the
     * {@link RateLimitedOutputPool#wrapStream(OutputStream)} method.  Each request demonstrates a stream's desire to
     * transmit a unit of data.
     *
     * @author Zachary Palmer
     */
    protected static class TransmissionRequest
    {
        /**
         * The ID number of the stream making the request.
         */
        protected long streamId;
        /**
         * The number of bytes being requested for transmission.
         */
        protected int size;
        /**
         * The time at which the request was acknowledged, or {@link Long#MIN_VALUE} if the request has not yet been
         * acknowledged.
         */
        protected long acknowledgementTime;

        /**
         * General constructor.
         *
         * @param id      The ID number of the stream making the request.
         * @param request The number of bytes being requested for transmission.
         */
        TransmissionRequest(long id, int request)
        {
            streamId = id;
            size = Integer.MAX_VALUE;
            setSize(request);
            acknowledgementTime = Long.MIN_VALUE;
        }

        /**
         * Retrieves the ID number of the stream making the request.
         *
         * @return The ID number of the stream making the request.
         */
        public long getStreamId()
        {
            return streamId;
        }

        /**
         * Retrieves the number of bytes being requested for transmission.
         *
         * @return The number of bytes being requested for transmission.
         */
        public int getSize()
        {
            return size;
        }

        /**
         * Changes the number of bytes being requested for transmission.  This is used by the {@link
         * RateLimitedOutputPool#requestTransmission(TransmissionRequest)} method to decrease the amount of data which
         * will be send in this request.  This method can only be used to decrease the amount of data being requested.
         *
         * @param size The new size of this request.
         * @throws IllegalArgumentException If the new request size is larger than the old request size or less than
         *                                  zero.
         */
        public void setSize(int size)
        {
            if (size > this.size)
            {
                throw new IllegalArgumentException("New size was larger than old size: " + size + ">" + this.size);
            }
            if (size < 0)
            {
                throw new IllegalArgumentException("New size is less than zero.");
            }
            this.size = size;
        }

        /**
         * Retrieves the time at which this request was acknowledged.
         *
         * @return The timeat which this request was acknowledged, or {@link Long#MIN_VALUE} if the request has not yet
         *         been acknowledged.
         */
        public long getAcknowledgedTime()
        {
            return acknowledgementTime;
        }

        /**
         * Sets the time of acknowledgement of this request to now.  If this request has already been acknowledged, this
         * call does nothing.
         */
        public void acknowledge()
        {
            if (acknowledgementTime == Long.MIN_VALUE)
            {
                acknowledgementTime = System.currentTimeMillis();
            }
        }
    }
}

// END OF FILE