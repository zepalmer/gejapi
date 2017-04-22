package orioni.jz.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * This {@link Thread} extension class is designed to abstract away the nuances of creating a single connection on the
 * side of the serving machine.  This class allows for obtaining a single connection and provides for cancellation of
 * operation at a specified granularity.  If the creator would like to pause a thread until the operation has completed,
 * joining this {@link Thread} extension class is sufficient.
 *
 * @author Zachary Palmer
 */
public class SingleSocketServer extends Thread
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The port on which this {@link SingleSocketServer} listens.
     */
    protected int port;
    /**
     * The timeout of the {@link ServerSocket} which is used to receive the {@link Socket}.
     */
    protected int timeout;
    /**
     * The {@link Socket} which was procured from the operation.
     */
    protected Socket result;
    /**
     * The {@link IOException} which occurred during the operation, if any.
     */
    protected IOException exception;
    /**
     * <code>true</code> if the {@link SingleSocketServer} is still trying to connect; <code>false</code> otherwise.
     */
    protected boolean trying;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param port    The port on which this {@link SingleSocketServer} listens.
     * @param timeout The timeout of the {@link ServerSocket} which is used to receive the {@link Socket}.
     */
    public SingleSocketServer(int port, int timeout)
    {
        super();
        this.port = port;
        this.timeout = timeout;
        result = null;
        exception = null;
        trying = false;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Accepts a single {@link Socket} on the specified port.  This method will persist in listening for a {@link
     * Socket} until one is found or until {@link SingleSocketServer#stopServing()} is called.
     */
    public void run()
    {
        try
        {
            ServerSocket ss = new ServerSocket(port);
            ss.setSoTimeout(timeout);
            while (trying)
            {
                try
                {
                    Socket s = ss.accept();
                    synchronized (this)
                    {
                        result = s;
                    }
                    trying = false;
                    socketServed();
                } catch (SocketTimeoutException ste)
                {
                }
            }
            ss.close();
        } catch (IOException ioe)
        {
            synchronized (this)
            {
                exception = ioe;
            }
        }
    }

    /**
     * Activates this {@link SingleSocketServer}.
     */
    public void serve()
    {
        if (!this.isAlive())
        {
            trying = true;
            start();
        }
    }

    /**
     * Ceases operation of this {@link SingleSocketServer}.  Note that this method may not be called in time to prevent
     * a connection from being established.  Callers should still call {@link SingleSocketServer#getResult()} to verify
     * the results of running the {@link SingleSocketServer}.
     */
    public void stopServing()
    {
        if (this.isAlive())
        {
            trying = false;
            if (!Thread.currentThread().equals(this))
            {
                try
                {
                    this.join();
                } catch (InterruptedException ie)
                {
                }
            }
        }
    }

    /**
     * Retrieves the results of running this {@link SingleSocketServer}.  The result will be <code>null</code> if the
     * operation was cancelled (or if it was never run), a {@link Socket} if the operation successfully established a
     * connection, or an {@link IOException} (which will be thrown) if the operation failed.
     *
     * @return The {@link Socket} which was established, or <code>null</code> if the operation was cancelled.
     * @throws IOException If an I/O error occurred in attempting to establish the {@link Socket}.
     */
    public synchronized Socket getResult()
            throws IOException
    {
        if (exception != null) throw exception;
        return result;
    }

    /**
     * Called immediately after the {@link SingleSocketServer} establishes a {@link Socket}.  This is provided to allow
     * a notification-based monitoring of the behavior of the {@link SingleSocketServer}.  The default implementation
     * does nothing, but subclasses could use this method for cleanup, user prompting, or to trigger the progression of
     * the program using the data obtained.
     */
    public void socketServed()
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}