package orioni.jz.io;

import java.io.*;
import java.util.ArrayList;

/**
 * This {@link InputStream} and {@link OutputStream} implementation is designed to replace the {@link PipedInputStream}
 * and {@link PipedOutputStream} classes.  This class permits the ability to specify buffer size.
 * <p/>
 * This class's buffer is stored as an {@link ArrayList}<code>&lt;byte[]&gt;</code>, with each write adding a
 * <code>byte[]</code> to the buffer.  Therefore, large numbers of small writes cause the buffer to use memory
 * inefficiently.  Occasionally, the pipe performs a cleanup operation to prevent this problem from getting out of hand.
 * However, if this {@link StreamPipe}'s {@link OutputStream} is expecting a large number of small writes, a great deal
 * of time can be saved by wrapping the {@link OutputStream} in a large {@link BufferedOutputStream}.
 *
 * @author Zachary Palmer
 */
public class StreamPipe
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A constant representing an ulimited buffer size.
     */
    public static final int UNLIMITED_BUFFER = Integer.MAX_VALUE;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A circular reference to this object.  This allows the streams contained within this object to access this
     * object.
     */
    final protected StreamPipe thisValue;

    /**
     * The {@link InputStream} for this {@link StreamPipe}.
     */
    protected StreamPipeInputStream inputStream;
    /**
     * The {@link OutputStream} for this {@link StreamPipe}.
     */
    protected StreamPipeOutputStream outputStream;

    /**
     * The maximum size of the buffer, in bytes.
     */
    protected int maximumBufferSize;
    /**
     * The queue of <code>byte[]</code>s to use as the buffer.
     */
    protected ArrayList<byte[]> queue;
    /**
     * The index of the next byte to provide as buffer data.
     */
    protected int nextByteIndex;
    /**
     * A variable used to cache the total number of bytes in the buffer.
     */
    protected int available;

    /**
     * <code>true</code> if this pipe has been closed; <code>false</code> otherwise.
     */
    protected boolean closed;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes with an unlimited default buffer.
     */
    public StreamPipe()
    {
        this(UNLIMITED_BUFFER);
    }

    /**
     * General constructor.
     *
     * @param bufferSize The size of the buffer, or {@link StreamPipe#UNLIMITED_BUFFER} for an unlimited buffer.
     * @throws IllegalArgumentException If the buffer size is less than or equal to <code>0</code>.
     */
    public StreamPipe(int bufferSize)
    {
        super();

        if (bufferSize <= 0) throw new IllegalArgumentException("Illegal buffer size: " + bufferSize);

        thisValue = this;
        available = 0;
        nextByteIndex = 0;
        queue = new ArrayList<byte[]>();
        maximumBufferSize = bufferSize;
        closed = false;
        inputStream = new StreamPipeInputStream();
        outputStream = new StreamPipeOutputStream();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the stream which is used to read data from this pipe.
     *
     * @return The stream which is used to read data from this pipe.
     */
    public InputStream getInputStream()
    {
        return inputStream;
    }

    /**
     * Retrieves the stream which is used to write data to this pipe.
     *
     * @return The stream which is used to write data to this pipe.
     */
    public OutputStream getOutputStream()
    {
        return outputStream;
    }

    /**
     * Determines whether or not the streams for this pipe are closed.
     *
     * @return <code>true</code> if the streams are closed; <code>false</code> otherwise.
     */
    public synchronized boolean isClosed()
    {
        return closed;
    }

    /**
     * Performs cleanup on the buffer queue.
     */
    public synchronized void bufferCleanup()
    {
        byte[] buffer = new byte[available];
        System.arraycopy(
                queue.get(0),
                nextByteIndex,
                buffer,
                0,
                queue.get(0).length - nextByteIndex);
        int index = queue.get(0).length - nextByteIndex;
        for (int i = 1; i < queue.size(); i++)
        {
            System.arraycopy(queue.get(i), 0, buffer, index, queue.get(i).length);
            index += queue.get(i).length;
        }
        queue.clear();
        queue.add(buffer);
        nextByteIndex = 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * The {@link InputStream} for this {@link StreamPipe}.  Data read from this stream comes from the same pipe's
     * {@link OutputStream}.
     *
     * @author Zachary Palmer
     */
    class StreamPipeInputStream extends InputStream
    {
        /**
         * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the
         * next caller of a method for this input stream.
         *
         * @return The number of bytes that can be read from this input stream without blocking.
         */
        public int available()
        {
            synchronized (thisValue)
            {
                return available;
            }
        }

        /**
         * Closes this input stream and releases any system resources associated with the stream.
         *
         * @throws IOException if an I/O error occurs.
         */
        public void close()
                throws IOException
        {
            synchronized (thisValue)
            {
                closed = true;
                thisValue.notifyAll();
            }
        }

        /**
         * Reads the next byte of data from the input stream.
         *
         * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
         * @throws IOException if an I/O error occurs.
         */
        public int read()
                throws IOException
        {
            byte[] data = new byte[1];
            int count = read(data, 0, 1);
            if (count == -1)
            {
                return -1;
            } else
            {
                return data[0] & 0xFF;
            }
        }

        /**
         * Reads some number of bytes from the input stream and stores them into the buffer array <code>b</code>. The
         * number of bytes actually read is returned as an integer.  This method blocks until input data is available,
         * end of file is detected, or an exception is thrown.
         *
         * @param b The buffer into which the data is read.
         * @return The total number of bytes read into the buffer, or <code>-1</code> is there is no more data because
         *         the end of the stream has been reached.
         * @throws IOException If an I/O error occurs.
         * @see InputStream#read(byte[], int, int)
         */
        public int read(byte[] b)
                throws IOException
        {
            return read(b, 0, b.length);
        }

        /**
         * Reads up to <code>len</code> bytes of data from the input stream into an array of bytes.  An attempt is made
         * to read as many as <code>len</code> bytes, but a smaller number may be read. The number of bytes actually
         * read is returned as an integer.
         *
         * @param b   The buffer into which the data is read.
         * @param off The start offset in array <code>b</code> at which the data is written.
         * @param len The maximum number of bytes to read.
         * @return The total number of bytes read into the buffer, or <code>-1</code> if there is no more data because
         *         the end of the stream has been reached.
         * @throws IOException If an I/O error occurs.
         * @see InputStream#read()
         */
        public int read(byte[] b, int off, int len)
                throws IOException
        {
            if (len == 0) return 0;
            int totalRead = 0;
            synchronized (thisValue)
            {
                if ((closed) && (available() == 0))
                {
                    return -1;
                }
                // while
                //   we want data AND
                //   either
                //       there is data OR
                //       there is potential for more AND we don't have any yet
                while ((len > 0) && ((available() > 0) || ((!closed) && (totalRead == 0))))
                {
                    if (available == 0)
                    {
                        try
                        {
                            thisValue.wait();
                        } catch (InterruptedException e)
                        {
                        }
                    } else
                    {
                        int bytesToRead = Math.min(len, available);
                        while (bytesToRead > 0)
                        {
                            int bytesToReadFromForwardBuffer = Math.min(
                                    bytesToRead, queue.get(0).length - nextByteIndex);
                            System.arraycopy(
                                    queue.get(0),
                                    nextByteIndex,
                                    b,
                                    off,
                                    bytesToReadFromForwardBuffer);
                            nextByteIndex += bytesToReadFromForwardBuffer;
                            bytesToRead -= bytesToReadFromForwardBuffer;
                            len -= bytesToReadFromForwardBuffer;
                            off += bytesToReadFromForwardBuffer;
                            available -= bytesToReadFromForwardBuffer;
                            totalRead += bytesToReadFromForwardBuffer;
                            if (nextByteIndex == queue.get(0).length)
                            {
                                queue.remove(0);
                                nextByteIndex = 0;
                            }
                            thisValue.notifyAll();
                        }
                    }
                }
            }

            if (totalRead == 0)
            {
                assert(closed);
                totalRead = -1;
            }

            return totalRead;
        }

        /**
         * Skips over and discards <code>n</code> bytes of data from this input stream. The <code>skip</code> method may,
         * for a variety of reasons, end up skipping over some smaller number of bytes, possibly <code>0</code>. This may
         * result from any of a number of conditions; reaching end of file before <code>n</code> bytes have been skipped is
         * only one possibility. The actual number of bytes skipped is returned.  If <code>n</code> is negative, no bytes
         * are skipped.
         *
         * @param n The number of bytes to be skipped.
         * @return The actual number of bytes skipped.
         * @throws IOException If an I/O error occurs.
         */
        public long skip(long n)
                throws IOException
        {
            synchronized (thisValue)
            {
                if (available <= n)
                {
                    int ret = available;
                    queue.clear();
                    nextByteIndex = 0;
                    available = 0;
                    thisValue.notifyAll();
                    return ret;
                } else
                {
                    long ret = n;
                    while (n > 0)
                    {
                        if (queue.get(0).length - nextByteIndex <= n)
                        {
                            n -= (queue.get(0).length - nextByteIndex);
                            available -= (queue.get(0).length - nextByteIndex);
                            nextByteIndex = 0;
                            queue.remove(0);
                        } else
                        {
                            nextByteIndex += n;
                            available -= n;
                            n = 0;
                        }
                    }
                    thisValue.notifyAll();
                    return ret;
                }
            }
        }
    }

    /**
     * The {@link OutputStream} for this {@link StreamPipe}.  Data written to this stream can be read from the same pipe's
     * {@link InputStream}.
     *
     * @author Zachary Palmer
     */
    class StreamPipeOutputStream extends OutputStream
    {
        /**
         * Flushes this output stream and forces any buffered output bytes to be written out.  This method does nothing; all
         * writes are instantly buffered.
         */
        public void flush()
        {
        }

        /**
         * Writes the specified byte to this output stream.
         *
         * @param b the <code>byte</code>.
         * @throws IOException If an I/O error occurs. In particular, an <code>IOException</code> may be thrown if the
         *                     output stream has been closed.
         */
        public void write(int b)
                throws IOException
        {
            byte[] data = new byte[1];
            data[0] = (byte) b;
            write(data, 0, 1);
        }

        /**
         * Writes <code>b.length</code> bytes from the specified byte array to this output stream. The general contract for
         * <code>write(b)</code> is that it should have exactly the same effect as the call <code>write(b, 0,
         * b.length)</code>.
         *
         * @param b The data to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void write(byte[] b)
                throws IOException
        {
            write(b, 0, b.length);
        }

        /**
         * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this output
         * stream.
         *
         * @param b   The data.
         * @param off The start offset in the data.
         * @param len The number of bytes to write.
         * @throws IOException If an I/O error occurs. In particular, an <code>IOException</code> is thrown if the output
         *                     stream is closed.
         */
        public void write(byte[] b, int off, int len)
                throws IOException
        {
            synchronized (thisValue)
            {
                if (closed)
                {
                    throw new IOException("Cannot write: stream is closed.");
                }
                while (len > 0)
                {
                    int bytesToWrite;
                    bytesToWrite = Math.min(len, maximumBufferSize - available);
                    if (bytesToWrite == 0)
                    {
                        try
                        {
                            thisValue.wait();
                        } catch (InterruptedException e)
                        {
                        }
                    } else
                    {
                        byte[] data = new byte[bytesToWrite];
                        System.arraycopy(b, off, data, 0, bytesToWrite);
                        off += bytesToWrite;
                        len -= bytesToWrite;
                        available += bytesToWrite;
                        queue.add(data);
                        // Now test for queue compacting: queue ratio is worse than 128 bytes per element and more than 100 elements or there
                        // are more than 1000 elements in the queue
                        if (((available / queue.size() < 128) && (queue.size() > 100)) ||
                            (queue.size() > 1000))
                        {
                            bufferCleanup();
                        }
                        thisValue.notifyAll();
                    }
                }
            }
        }

        /**
         * Closes this input stream and releases any system resources associated with the stream.
         *
         * @throws IOException if an I/O error occurs.
         */
        public void close()
                throws IOException
        {
            synchronized (thisValue)
            {
                closed = true;
                thisValue.notifyAll();
            }
        }
    }
}

// END OF FILE