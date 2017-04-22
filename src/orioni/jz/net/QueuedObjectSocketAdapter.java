package orioni.jz.net;

import java.io.ObjectOutputStream;

/**
 * This class is an implementation of {@link QueuedObjectSocketListener} with each method implemented to do nothing.
 *
 * @author Zachary Palmer
 */
public class QueuedObjectSocketAdapter implements QueuedObjectSocketListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public QueuedObjectSocketAdapter()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is called when an exception occurs in one of the threads.  By the time it is called, {@link
     * QueuedObjectSocket#stopQueueThreads()} has already been called.
     *
     * @param qos       The {@link QueuedObjectSocket} on which the exception occurred.
     * @param exception The {@link Exception} which occurred.
     */
    public void exceptionOccurred(QueuedObjectSocket qos, Exception exception)
    {
    }

    /**
     * This method is called whenever an object is enqueued on the socket.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was enqueued.
     */
    public void objectEnqueued(QueuedObjectSocket qos, Object object)
    {
    }

    /**
     * This method is called whenever an object is received from the socket and added to the incoming queue.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was enqueued.
     */
    public void objectReceived(QueuedObjectSocket qos, Object object)
    {
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
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE