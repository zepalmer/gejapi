package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This InputStream represents the general concept of reading a single stream of content from multiple streams.  Streams
 * are obtained by calling {@link MultiplexInputStream#getMoreStreams()} until this method call no longer produces any
 * streams, at which point this stream reports itself exhausted.
 *
 * @author Zachary Palmer
 */
public abstract class MultiplexInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * This field contains the current InputStream from which data is being read.
     */
    protected InputStream currentSource;
    /**
     * This field contains the queued streams.
     */
    protected ArrayList<InputStream> nextSources;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public MultiplexInputStream()
    {
        super();
        currentSource = null;
        nextSources = new ArrayList<InputStream>();
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
    public abstract InputStream[] getMoreStreams()
            throws IOException;

    /**
     * This method cleans up the given stream, allowing another stream to take its place in transmitting read data.  The
     * default implementation simply closes the stream.
     * <p/>
     * This method allows the implementer to control the logic which determines how underlying streams should be cleaned
     * up when the <code>MultiplexInputStream</code> changes target streams.  In most cases, the response will be to
     * close the provided stream; in some cases, however, this may not be sufficient or appropriate.
     * <p/>
     * The {@link InputStream} provided will always be the input stream that this <code>MultiplexInputStream</code> is
     * using as a reading source.
     *
     * @param stream The input stream to clean up.
     * @throws IOException If an I/O exception occurs while cleaning up the provided stream.
     */
    public void cleanUpStream(InputStream stream)
            throws IOException
    {
        stream.close();
    }

    /**
     * Reads the given byte of data from an underlying input stream.  Before this is done, the current source input
     * stream is examined for its validity.  In the event that the stream is not in such a state that it can transmit
     * data, it is cleaned up and a new stream for the data is obtained.
     *
     * @return The byte that was read.  See {@link java.io.InputStream} for more information.
     * @throws IOException If an I/O error occurs while reading the data or while establishing a new underlying input
     *                     stream.
     */
    public int read()
            throws IOException
    {
        if (!ensureSource()) return -1;
        int ret = currentSource.read();
        while (ret == -1)
        {
            cleanUpStream(currentSource);
            currentSource = null;
            if (!ensureSource()) return -1;
            ret = currentSource.read();
        }
        return ret;
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into an array of bytes.  An attempt is made to
     * read as many as <code>len</code> bytes, but a smaller number may be read. The number of bytes actually read is
     * returned as an integer.
     * <p/>
     * This method blocks until input data is available, end of file is detected, or an exception is thrown.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array <code>b</code> at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
     *         end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read(byte[] b, int off, int len)
            throws IOException
    {
        if (!ensureSource()) return -1;
        int totalRead = 0;
        while (len > 0)
        {
            int read = currentSource.read(b, off, len);
            if (read == -1)
            {
                cleanUpStream(currentSource);
                currentSource = null;
                if (!ensureSource()) return totalRead;
            } else
            {
                len -= read;
                off += read;
                totalRead += read;
            }
        }
        return totalRead;
    }

    /**
     * This method flushes any buffers associated with this stream and cleans up any associated resources.  This method
     * also calls a <code>cleanUpStream</code> on the current input source.  After this method is called, calls to
     * <code>read</code> will throw <code>IOExceptions</code>.
     *
     * @throws IOException If an I/O exception occurs while cleaning up the current input source or if an exception
     *                     occurs while closing this stream.
     */
    public void close()
            throws IOException
    {
        if (currentSource != null) cleanUpStream(currentSource);
        super.close();
    }

    /**
     * Returns whether or not there are any further bytes to read from this <code>MultiplexInputStream</code>.  This
     * value is <code>1</code> if there is an open input stream and <code>0</code> if this stream is closed.  The return
     * value is also <code>0</code> if the last attempt to open an input stream failed or if no input stream has yet
     * been opened.  Note that this function is not entirely capable of determining whether or not data is available;
     * although data is guaranteed to be available if a <code>1</code> is returned, data is not guaranteed to be
     * unavailable if a <code>0</code> is returned.
     *
     * @return <code>1</code> or <code>0</code>, as described above.
     * @throws IOException Never.  This is provided for compatibility with <code>InputStream</code> and extensions of
     *                     this stream.
     */
    public int available()
            throws IOException
    {
        if (!ensureSource()) return 0;
        int total = currentSource.available();
        for (InputStream is : nextSources) total += is.available();
        return total;
    }

    /**
     * Ensures that, if a source is available, that it is in <code>m_current_source</code>.
     *
     * @return <code>true</code> if the source is valid; <code>false</code> if there were no more sources.
     */
    protected boolean ensureSource()
            throws IOException
    {
        if (currentSource == null)
        {
            if (nextSources.size() == 0)
            {
                InputStream[] iss = getMoreStreams();
                if (iss != null)
                {
                    for (InputStream is : iss) nextSources.add(is);
                }
            }
            if (nextSources.size() > 0)
            {
                currentSource = nextSources.remove(0);
            } else
            {
                return false;
            }
        }
        return true;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //