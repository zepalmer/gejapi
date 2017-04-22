package orioni.jz.net;

/**
 * This listener interface is designed to allow an implementer to be notified of events that occur on a {@link
 * QueuedObjectSocket}.
 *
 * @author Zachary Palmer
 */
public interface QueuedObjectSocketListener
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is called when an exception occurs in one of the threads.  By the time it is called, {@link
     * QueuedObjectSocket#stopQueueThreads()} has already been called.
     *
     * @param qos       The {@link QueuedObjectSocket} on which the exception occurred.
     * @param exception The {@link Exception} which occurred.
     */
    public void exceptionOccurred(QueuedObjectSocket qos, Exception exception);

    /**
     * This method is called whenever an object is enqueued on the socket.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was enqueued.
     */
    public void objectEnqueued(QueuedObjectSocket qos, Object object);

    /**
     * This method is called whenever an object is sent on the socket.  Note that this is distinct from the enqueue
     * operation, as at this point, the object has actually been written to the stream.  Bear in mind that this does not
     * mean that, while the call to the {@link java.io.ObjectOutputStream#writeObject(Object)} method has completed, the
     * object has not necessarily been sent on the network and there is certainly no guarantee that the other side has
     * received it.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was sent.
     */
    public void objectSent(QueuedObjectSocket qos, Object object);

    /**
     * This method is called whenever an object is received from the socket and added to the incoming queue.
     *
     * @param qos    The {@link QueuedObjectSocket} on which the event occurred.
     * @param object The object which was enqueued.
     */
    public void objectReceived(QueuedObjectSocket qos, Object object);
}

// END OF FILE