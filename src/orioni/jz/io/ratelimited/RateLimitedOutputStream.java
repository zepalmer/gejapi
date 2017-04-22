package orioni.jz.io.ratelimited;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This {@link OutputStream} wraps another {@link OutputStream} implementation and limits the rate at which data can be
 * sent to it.  The rate is specified on construction but can be manipuated during the operation of the stream.  If a
 * caller attempts to write more data at once than can be written to the stream, the stream will delay the caller's
 * write for the appropriate amount of time, performing writes incrementally to allow the stream to continue to operate
 * at this rated maximum.
 *
 * @author Zachary Palmer
 */
public class RateLimitedOutputStream extends OutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link OutputStream} to which data is written.
     */
    protected OutputStream target;
    /**
     * The maximum rate of this stream in bytes per second.
     */
    protected int rate;
    /**
     * The amount of data sent in the last second.
     */
    protected int dataSent;
    /**
     * The last time at wihch the sent data field was cleared.
     */
    protected long lastClearTime;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param outputStream The {@link OutputStream} to which data will be written.
     * @param rate          The maximum number of bytes per second to write.
     * @throws IllegalArgumentException If the provided rate limit is less than or equal to zero.
     */
    public RateLimitedOutputStream(OutputStream outputStream, int rate)
    {
        super();
        target = outputStream;
        this.rate = rate;
        dataSent = 0;
        lastClearTime = System.currentTimeMillis();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Writes the specified byte to the underlying output stream.  A delay may be incurred to allow rate limiting.
     *
     * @param b the <code>byte</code>.
     * @throws IOException If an I/O error occurs in the underlying stream.
     */
    public void write(int b)
            throws IOException
    {
        boolean written = false;
        do
        {
            boolean sleep;
            synchronized (this)
            {
                if (lastClearTime + 1000 <= System.currentTimeMillis())
                {
                    lastClearTime = System.currentTimeMillis();
                    dataSent = 0;
                }
                sleep = (dataSent >= rate);
            }
            if (sleep)
            {
                long time;
                synchronized (this)
                {
                    time = 1000 - (System.currentTimeMillis() - lastClearTime);
                }
                try
                {
                    Thread.sleep(time);
                } catch (InterruptedException e)
                {
                }
            } else
            {
                target.write(b);
                written = true;
                synchronized (this)
                {
                    dataSent++;
                }
            }
        } while (!written);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this output
     * stream.  This is done in a series of writes to the underlying stream no larger than the number of bytes in the
     * limited rate of this stream.  A delay may be incurred to allow rate limiting.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException If an I/O error occurs in the underlying stream.
     */
    public void write(byte[] b, int off, int len)
            throws IOException
    {
        while (len > 0)
        {
            int toWrite = 0;
            synchronized (this)
            {
                if (lastClearTime + 1000 <= System.currentTimeMillis())
                {
                    lastClearTime = System.currentTimeMillis();
                    dataSent = 0;
                }
                if (dataSent < rate)
                {
                    toWrite = Math.min(rate - dataSent, len);
                }
            }
            if (toWrite > 0)
            {
                target.write(b, off, toWrite);
                len -= toWrite;
                off += toWrite;
                synchronized (this)
                {
                    dataSent += toWrite;
                }
            } else
            {
                long time;
                synchronized (this)
                {
                    time = 1000 - (System.currentTimeMillis() - lastClearTime);
                }
                if (time > 0)
                {
                    try
                    {
                        Thread.sleep(time);
                    } catch (InterruptedException e)
                    {
                    }
                }
            }
        }
    }

    /**
     * Changes the rate limit of this stream.
     *
     * @param rate The new rate limit of this stream.
     * @throws IllegalArgumentException If the provided rate limit is less than or equal to zero.
     */
    public synchronized void setRateLimit(int rate)
    {
        if (rate > 0)
        {
            this.rate = rate;
        } else
        {
            throw new IllegalArgumentException("Rate must be at least 1 byte per second.  Provided: " + rate);
        }
    }

    /**
     * Retrieves the rate limit of this stream.
     *
     * @return The rate limit of this stream.
     */
    public synchronized int getRateLimit()
    {
        return rate;
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        target.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE