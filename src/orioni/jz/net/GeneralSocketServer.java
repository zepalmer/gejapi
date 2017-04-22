package orioni.jz.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Instances of this class are designed to listen to a specific port and accept {@link Socket}s.  Whenever a {@link
 * Socket} is accepted, the {@link GeneralSocketServer#socketAccepted(Socket)} method is called.  Users of this class
 * override that method and use the {@link Socket} objects as desired.
 * <p/>
 * An additional method, {@link GeneralSocketServer#exceptionOccurred(IOException)}, notifies the user that an I/O
 * exception has occurred while attempting to establish a connection.  It should be used in much the same way that
 * {@link GeneralSocketServer#socketAccepted(Socket)} is used.  Notice {@link GeneralSocketServer#socketAccepted(Socket)}
 * method throws an {@link IOException} itself; if that method throws an {@link IOException}, {@link
 * GeneralSocketServer#exceptionOccurred(IOException)} <b>is</b> called.
 *
 * @author Zachary Palmer
 */
public abstract class GeneralSocketServer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The port on which this {@link GeneralSocketServer} listens.
     */
    protected int port;
    /**
     * The timeout of the {@link ServerSocket} which is used to receive the {@link Socket}.
     */
    protected int timeout;
    /**
     * <code>true</code> if the {@link GeneralSocketServer} is still trying to connect; <code>false</code> otherwise.
     */
    protected boolean trying;
    /**
     * The {@link ServerSocket} which is being used to establish connections.
     */
    protected ServerSocket serverSocket;

    /**
     * The {@link Thread} which is used to listen for new connections.
     */
    protected Thread listeningThread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param port    The port on which this {@link GeneralSocketServer} should listen.
     * @param timeout The number of milliseconds to wait before timing out on a connection accept.  This is important
     *                because it defines the granularity of stop polling: a lower timeout requires more CPU time,
     *                whereas a higher timeout increases the time required for the {@link GeneralSocketServer} to
     *                respond to stop requests.
     */
    public GeneralSocketServer(int port, int timeout)
    {
        super();
        this.port = port;
        this.timeout = timeout;
        listeningThread = null;
        serverSocket = null;
        trying = false;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Starts this server.  The {@link ServerSocket} will begin operating against the port provided at construction.
     *
     * @throws IOException If an I/O exception occurs while establishing the {@link ServerSocket}.
     */
    public synchronized void startServing()
            throws IOException
    {
        if (!trying)
        {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeout);
            trying = true;
            listeningThread = new Thread(this.toString()+" Accept Thread")
            {
                public void run()
                {
                    while (trying)
                    {
                        try
                        {
                            Socket socket = serverSocket.accept();
                            socketAccepted(socket);
                        } catch (SocketTimeoutException ste)
                        {
                            // expected
                        } catch (IOException ioe)
                        {
                            exceptionOccurred(ioe);
                        }
                    }
                    try
                    {
                        serverSocket.close();
                    } catch (IOException ioe)
                    {
                    }
                }
            };
            listeningThread.start();
        }
    }

    /**
     * Stops the execution of this {@link GeneralSocketServer}.  This method will block until the serving thread has
     * terminated.  Note that, even after this method is called, it is possible for one {@link Socket} to be accepted
     * before the {@link ServerSocket} is disabled.
     */
    public synchronized void stopServing()
    {
        if (trying)
        {
            trying = false;
            if (!Thread.currentThread().equals(listeningThread))
            {
                try
                {
                    listeningThread.join();
                } catch (InterruptedException e)
                {
                }
            }
        }
    }

    /**
     * This method is called whenever a {@link Socket} is accepted by this server.  It is called by the serving thread;
     * thus, this method should most likely not block or spend a great deal of time executing.
     *
     * @param socket The {@link Socket} that was accepted.
     * @throws IOException Whenever an I/O error occurs.  This method definition is intended to provide the overriding
     *                     method the ability to throw {@link IOException}s while manipulating the accepted {@link
     *                     Socket}.  If such an exception is thrown, it is passed to the {@link
     *                     GeneralSocketServer#exceptionOccurred(IOException)} method.
     */
    protected abstract void socketAccepted(Socket socket)
            throws IOException;

    /**
     * This method is called whenever a method in this server throws an {@link IOException}, such as the {@link
     * ServerSocket#accept()} method.  This allows for error reporting and handling to be abstracted.
     *
     * @param exception The {@link IOException} which was thrown.
     */
    protected void exceptionOccurred(IOException exception)
    {
    }

    /**
     * Produces a {@link String} which describes this message.
     * @return A {@link String} which describes this message.
     */
    public String toString()
    {
        return "General Socket Server port "+port;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}