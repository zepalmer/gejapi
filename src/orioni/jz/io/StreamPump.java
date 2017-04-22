package orioni.jz.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO: Make this pump operate regardless of available() implementation

/**
 * This class is a utility that automatically feeds any data in the {@link InputStream} provided upon construction into
 * the {@link OutputStream} provided upon construction.  This object starts a new thread when the {@link
 * StreamPump#start()} method is called which performs the processing of data.  This thread will pause when the {@link
 * InputStream} is blocking for more data or when the {@link StreamPump#stop()} method is called.  This thread will
 * terminate when an end-of-stream marker is read from the provided {@link InputStream} or when the provided {@link
 * InputStream} or {@link OutputStream} throws an exception.
 * <p/>
 * The pump is capable of performing either blocking or non-blocking reads.  The non-blocking reads use the method
 * {@link InputStream#available()} to determine if a read will succeed without blocking.  The advantages and
 * disadvantages are thus: <ul> <li>If blocking reads are used, the pump thread will not stop until after the first read
 * after the call to {@link StreamPump#stop()}.  If non-blocking reads are used, the pump will stop immediately after
 * the call to {@link StreamPump#stop()}.</li> <li>Non-blocking reads cannot detect the end of the stream.  Thus, if the
 * stream is closed, a non-blocking read pump must be stopped manually.</li> <li>Non-blocking reads will not function if
 * the {@link InputStream} does not properly implement its {@link InputStream#available() available()} method.</li>
 * </ul>
 *
 * @author Zachary Palmer
 */
public class StreamPump implements Runnable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Thread} that is pumping the stream data.
     */
    protected Thread thread;
    /**
     * The {@link InputStream} from which the pump is drawing information.
     */
    protected InputStream inputStream;
    /**
     * The {@link OutputStream} to which the pump is sending read information.
     */
    protected OutputStream outputStream;
    /**
     * Whether or not the thread should be operating.
     */
    protected boolean pumpOn;
    /**
     * Whether or not the pump works using blocking reads.
     */
    protected boolean blocking;
    /**
     * Whether or not the participating streams are closed when pumping is complete.
     */
    protected boolean closeOnFinish;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes blocking reads and that streams will not be closed upon completion.
     *
     * @param is The {@link InputStream} from which the pump will draw information.
     * @param os The {@link OutputStream} to which the pump will send information.
     */
    public StreamPump(InputStream is, OutputStream os)
    {
        this(is, os, true, false);
    }

    /**
     * General constructor.
     *
     * @param is            The {@link InputStream} from which the pump will draw information.
     * @param os            The {@link OutputStream} to which the pump will send information.
     * @param blocking      <code>true</code> if a blocking read should be performed; <code>false</code> if the read
     *                      should only be performed if {@link InputStream#available()} indicates that it will not
     *                      block.
     * @param closeOnFinish <code>true</code> if this pump should close the {@link InputStream} and {@link OutputStream}
     *                      on completion; <code>false</code> if it should not.
     */
    public StreamPump(InputStream is, OutputStream os, boolean blocking, boolean closeOnFinish)
    {
        super();
        inputStream = is;
        outputStream = os;
        pumpOn = false;
        this.blocking = blocking;
        this.closeOnFinish = closeOnFinish;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Activates the pump, starting the thread which reads information from the {@link InputStream} and sends it to the
     * {@link OutputStream}.  If the pump is already active, this method does nothing.
     */
    public synchronized void start()
    {
        if (!pumpOn)
        {
            pumpOn = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Shuts down the pump, sending a signal to the pump thread to halt execution.  If the pump is not active, this
     * method does nothing.  This method will block until the pump has halted.
     */
    public synchronized void stop()
    {
        if (pumpOn)
        {
            pumpOn = false;
            thread.interrupt();
            try
            {
                thread.join();
            } catch (InterruptedException e)
            {
            }
        }
    }

    /**
     * Waits for the pump to complete execution.  If the pump is stopped, this method immediately returns.  If the pump
     * is active, this method waits for the pump to stop either by a call to {@link StreamPump#stop()}, by the pump's
     * thread becoming interrupted, by one of the pump's streams failing, or by an EOS on the input stream. If a stream
     * read causes the pump's thread to block, this thread will continue to wait until the thread expires.
     */
    public synchronized void join()
    {
        if (pumpOn)
        {
            try
            {
                thread.join();
            } catch (InterruptedException e)
            {
            }
        }
    }

// NON-STATIC METHODS : RUNNABLE IMPLEMENTATION //////////////////////////////////

    /**
     * Performs the data pumping.
     */
    public void run()
    {
        byte[] buffer = new byte[16384];
        try
        {
            while (pumpOn)
            {
                if (blocking || (inputStream.available() > 0))
                {
                    int readSize = inputStream.available();
                    if (readSize == 0)
                    {
                        readSize = buffer.length;
                    } else
                    {
                        readSize = Math.min(readSize, buffer.length);
                    }
                    readSize = inputStream.read(buffer, 0, readSize);
                    if (readSize == -1) break;
                    outputStream.write(buffer);
                } else
                {
                    try
                    {
                        Thread.sleep(20);
                    } catch (InterruptedException e)
                    {
                    }
                }
            }
        } catch (IOException ioe)
        {
        }
        if (this.closeOnFinish)
        {
            try
            {
                this.inputStream.close();
            } catch (IOException e)
            {
            }
            try
            {
                this.outputStream.close();
            } catch (IOException e)
            {
            }
        }
        pumpOn = false;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}