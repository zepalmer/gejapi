package orioni.jz.net;

import java.io.IOException;
import java.net.Socket;

/**
 * This {@link GeneralSocketServer} extension wraps generated {@link Socket}s in {@link QueuedObjectSocket} objects.
 * They are then passed to the {@link GeneralQueuedObjectSocketServer#queuedObjectSocketAccepted(QueuedObjectSocket)}
 * method, which is to be handled in the same way as the {@link GeneralSocketServer#socketAccepted(Socket)} method is
 * handled by direct extenders of that class.
 *
 * @author Zachary Palmer
 */
public abstract class GeneralQueuedObjectSocketServer extends GeneralSocketServer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * <code>true</code> if {@link QueuedObjectSocket#startQueueThreads()} should be called after the {@link
     * QueuedObjectSocket} is constructed; <code>false</code> otherwise.
     */
    protected boolean autostartSocketThreads;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that socket threads will automatically be started.
     *
     * @param port    The port on which to serve.
     * @param timeout The amount of time in milliseconds to wait before timing out when listening for a connection.  See
     *                {@link GeneralSocketServer#GeneralSocketServer(int, int)} for more information.
     */
    public GeneralQueuedObjectSocketServer(int port, int timeout)
    {
        this(port, timeout, true);
    }

    /**
     * General constructor.
     *
     * @param port                     The port on which to serve.
     * @param timeout                  The amount of time in milliseconds to wait before timing out when listening for a
     *                                 connection.  See {@link GeneralSocketServer#GeneralSocketServer(int, int)} for
     *                                 more information.
     * @param autostartSocketThreads <code>true</code> if {@link QueuedObjectSocket#startQueueThreads()} should be
     *                                 called after the {@link QueuedObjectSocket} is constructed; <code>false</code>
     *                                 otherwise.
     */
    public GeneralQueuedObjectSocketServer(int port, int timeout, boolean autostartSocketThreads)
    {
        super(port, timeout);
        this.autostartSocketThreads = autostartSocketThreads;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is called whenever a {@link Socket} is accepted by this server.
     *
     * @param socket The {@link Socket} that was accepted.
     * @throws IOException Whenever an I/O error occurs.  This method definition is intended to provide the overriding
     *                     method the ability to throw {@link IOException}s while manipulating the accepted {@link
     *                     Socket}.
     */
    protected void socketAccepted(Socket socket)
            throws IOException
    {
        QueuedObjectSocket qos = new QueuedObjectSocket(socket, autostartSocketThreads);
        queuedObjectSocketAccepted(qos);
    }

    /**
     * This method is called whenever an {@link QueuedObjectSocket} is accepted and created by this server.
     *
     * @param socket The {@link QueuedObjectSocket} that was accepted.
     * @throws IOException Whenever an I/O error occurs.  This method definition is intended to provide the overriding
     *                     method the ability to throw {@link IOException}s while manipulating the accepted {@link
     *                     QueuedObjectSocket} or the underlying {@link Socket}.
     */
    protected abstract void queuedObjectSocketAccepted(QueuedObjectSocket socket)
            throws IOException;

    /**
     * Produces a {@link String} which describes this message.
     * @return A {@link String} which describes this message.
     */
    public String toString()
    {
        return "General Queued Object Socket Server port "+port;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE