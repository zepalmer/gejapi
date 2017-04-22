package orioni.jz.net;

import orioni.jz.io.PrependablePrintStream;

import java.io.EOFException;
import java.io.ObjectOutputStream;

/**
 * This listener is designed to provide information to the provided log stream whenever an event occurs on one of the sockets.
 *
 * @author Zachary Palmer
 */
public class QueuedObjectSocketLogListener implements QueuedObjectSocketListener
{
    /**
     * The {@link PrependablePrintStream} to which this listener should write.
     */
    protected PrependablePrintStream logStream;

    /**
     * General constructor.
     *
     * @param logStream The {@link PrependablePrintStream} to which this listener should write.
     */
    public QueuedObjectSocketLogListener(PrependablePrintStream logStream)
    {
        this.logStream = logStream;
    }

    /**
     * This method is called when an exception occurs in one of the threads.  By the time it is called, {@link
     * QueuedObjectSocket#stopQueueThreads()} has already been called.
     *
     * @param qos       The {@link QueuedObjectSocket} on which the exception occurred.
     * @param exception The {@link Exception} which occurred.
     */
    public void exceptionOccurred(QueuedObjectSocket qos, Exception exception)
    {
        if (exception instanceof EOFException)
        {
            logStream.println("Socket disconnected: " + qos);
        } else
        {
            logStream.println("Exception Occurred on " + qos);
            logStream.incPrependCount();
            exception.printStackTrace(logStream);
            logStream.decPrependCount();
        }
    }

    /**
     * This method is called whenever an object is enqueued on the socket.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was enqueued.
     */
    public void objectEnqueued(QueuedObjectSocket qos, Object object)
    {
        logStream.println("Enqueued to " + qos + ": " + object);
    }

    /**
     * This method is called whenever an object is received from the socket and added to the incoming queue.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was enqueued.
     */
    public void objectReceived(QueuedObjectSocket qos, Object object)
    {
        logStream.println("Received from " + qos + ": " + object);
    }

    /**
     * This method is called whenever an object is sent on the socket.  Note that this is distinct from the enqueue
     * operation, as at this point, the object has actually been written to the stream.  Bear in mind that this does not
     * mean that, while the call to the {@link ObjectOutputStream#writeObject(Object)} method has completed, the object
     * has not necessarily been sent on the network and there is certainly no guarantee that the other side has received
     * it.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was sent.
     */
    public void objectSent(QueuedObjectSocket qos, Object object)
    {
        logStream.println("Sent to " + qos + ": " + object);
    }
}

// END OF FILE