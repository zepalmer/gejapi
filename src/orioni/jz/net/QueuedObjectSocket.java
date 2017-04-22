package orioni.jz.net;

import orioni.jz.common.exception.InsufficientDataException;
import orioni.jz.util.SynchronizedQueue;
import orioni.jz.util.Utilities;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

/**
 * This extension of {@link ObjectSocket} is designed to allow the queuing of incoming and outgoing objects.  Two
 * threads maintain this operation, which are controlled by method calls against the {@link QueuedObjectSocket}.
 * <p/>
 * <b>NOTE:</b> it is imperative that, while the queue management threads are operating, no access to the object streams
 * occurs except by those threads.  The object streams are not thread safe; thus, while the threads are running,
 * interaction with the socket should occur through the queues, as they are synchronized.
 * <p/>
 * The output thread also performs the task of sending a heartbeat object: a unique subclass which appears in this
 * class.  This is done to ensure that the connection is still live.
 *
 * @author Zachary Palmer
 */
public class QueuedObjectSocket extends ObjectSocket
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The input management thread.
     */
    protected Thread inputThread;
    /**
     * The output management thread.
     */
    protected Thread outputThread;

    /**
     * The {@link SynchronizedQueue} for incoming objects.
     */
    protected SynchronizedQueue<Serializable> incomingQueue;
    /**
     * The {@link SynchronizedQueue} for outgoing objects.
     */
    protected SynchronizedQueue<Serializable> outgoingQueue;

    /**
     * The {@link Set} of {@link QueuedObjectSocketListener}s listening to this {@link QueuedObjectSocket}.
     */
    protected Set<QueuedObjectSocketListener> listeners;

    /**
     * Whether or not the threads should continue operating.
     */
    protected boolean continueValue;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the queuing threads should be started automatically.
     *
     * @param socket The {@link Socket} around which to wrap this {@link QueuedObjectSocket}.
     * @throws IOException If an I/O error occurs while wrapping the {@link Socket}'s streams.
     */
    public QueuedObjectSocket(Socket socket)
            throws IOException
    {
        this(socket, true);
    }

    /**
     * General constrcutor.
     *
     * @param socket            The {@link Socket} around which to wrap this {@link QueuedObjectSocket}.
     * @param startQueueThreads <code>true</code> if this constructor should start the queuing threads;
     *                          <code>false</code> if not.
     * @throws IOException If an I/O error occurs while wrapping the {@link Socket}'s streams.
     */
    public QueuedObjectSocket(Socket socket, boolean startQueueThreads)
            throws IOException
    {
        super(socket);
        incomingQueue = new SynchronizedQueue<Serializable>();
        outgoingQueue = new SynchronizedQueue<Serializable>();
        continueValue = false;
        listeners = new HashSet<QueuedObjectSocketListener>();
        if (startQueueThreads) startQueueThreads();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Transmits an object on the socket.
     *
     * @param object The object to transmit.
     */
    public void send(Serializable object)
    {
        outgoingQueue.enqueue(object);
        synchronized (listeners)
        {
            for (QueuedObjectSocketListener listener : listeners)
            {
                listener.objectEnqueued(this, object);
            }
        }
    }

    /**
     * Receives an object from the socket.  This method does not block; if no object exists on the socket,
     * <code>null</code> is returned.
     *
     * @return The next object on the socket, or <code>null</code> if no object exists on the socket.
     */
    public Serializable receive()
    {
        return receive(false);
    }

    /**
     * Receives an object from the socket.
     *
     * @param block <code>true</code> if this method should block until either an object is read or the queue threads
     *              are stopped; <code>false</code> if the method should return immediately.
     * @return The next object on the socket, or <code>null</code> if no object exists on the socket.
     */
    public Serializable receive(boolean block)
    {
        try
        {
            return incomingQueue.dequeue(block);
        } catch (InsufficientDataException e)
        {
            return null;
        }
    }

    /**
     * Adds a listener to this {@link QueuedObjectSocket}.
     *
     * @param listener The listener to add to the {@link QueuedObjectSocket}.
     */
    public void addListener(QueuedObjectSocketListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from this {@link QueuedObjectSocket}.
     *
     * @param listener The listener to remove from the {@link QueuedObjectSocket}.
     */
    public void removeListener(QueuedObjectSocketListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    /**
     * Starts the queue processing threads.  These threads will read from and write to the object streams, using the
     * queues as their buffers.  If the processing threads are already operating, this method will do nothing.
     */
    public synchronized void startQueueThreads()
    {
        final QueuedObjectSocket scopedThis = this;
        if ((inputThread == null) && (outputThread == null))
        {
            incomingQueue.setBlockingAllowed(true);
            continueValue = true;
            inputThread = new Thread("Input Thread for " + socket)
            {
                public void run()
                {
                    try
                    {
                        socket.setSoTimeout(1000);
                    } catch (SocketException e)
                    {
                        stopQueueThreads();
                        synchronized (listeners)
                        {
                            for (QueuedObjectSocketListener listener : listeners)
                            {
                                listener.exceptionOccurred(scopedThis, e);
                            }
                        }
                    }
                    while (continueValue)
                    {
                        try
                        {
                            Serializable o = (Serializable) (objectInputStream.readObject());
                            if (!(o instanceof Heartbeat))
                            {
                                incomingQueue.enqueue(o);
                                synchronized (listeners)
                                {
                                    for (QueuedObjectSocketListener listener : listeners)
                                    {
                                        listener.objectReceived(scopedThis, o);
                                    }
                                }
                            }
                        } catch (SocketTimeoutException ste)
                        {
                            // yeah, that's normal
                        } catch (IOException e)
                        {
                            stopQueueThreads();
                            synchronized (listeners)
                            {
                                for (QueuedObjectSocketListener listener : listeners)
                                {
                                    listener.exceptionOccurred(scopedThis, e);
                                }
                            }
                        } catch (ClassNotFoundException e)
                        {
                            stopQueueThreads();
                            synchronized (listeners)
                            {
                                for (QueuedObjectSocketListener listener : listeners)
                                {
                                    listener.exceptionOccurred(scopedThis, e);
                                }
                            }
                        }
                    }
                }
            };
            outputThread = new Thread("Output Thread for " + socket)
            {
                public void run()
                {
                    while (continueValue)
                    {
                        if (outgoingQueue.size() > 0)
                        {
                            while (outgoingQueue.size() > 0)
                            {
                                Serializable o = outgoingQueue.front();
                                try
                                {
                                    objectOutputStream.writeObject(o);
                                    outgoingQueue.dequeue(false);
                                    synchronized (listeners)
                                    {
                                        for (QueuedObjectSocketListener listener : listeners)
                                        {
                                            listener.objectSent(scopedThis, o);
                                        }
                                    }
                                    synchronized (outgoingQueue)
                                    {
                                        outgoingQueue.notifyAll();
                                    }
                                } catch (IOException e)
                                {
                                    stopQueueThreads();
                                    synchronized (listeners)
                                    {
                                        for (QueuedObjectSocketListener listener : listeners)
                                        {
                                            listener.exceptionOccurred(scopedThis, e);
                                        }
                                    }
                                }
                            }
                        } else
                        {
                            try
                            {
                                objectOutputStream.writeObject(Heartbeat.SINGLETON);
                            } catch (IOException e)
                            {
                                stopQueueThreads();
                                synchronized (listeners)
                                {
                                    for (QueuedObjectSocketListener listener : listeners)
                                    {
                                        listener.exceptionOccurred(scopedThis, e);
                                    }
                                }
                            }
                        }
                        try
                        {
                            Thread.sleep(1000);
                        } catch (InterruptedException e)
                        {
                        }
                    }
                }
            };
            inputThread.start();
            outputThread.start();
        }
    }

    /**
     * Stops the queue processing threads.  If they are not active, this method will do nothing.
     */
    public synchronized void stopQueueThreads()
    {
        if ((inputThread != null) || (outputThread != null))
        {
            continueValue = false;
            Utilities.safeJoin(inputThread);
            inputThread = null;
            Utilities.safeJoin(outputThread);
            outputThread = null;
            incomingQueue.setBlockingAllowed(false);
        }
    }

    /**
     * Closes this {@link ObjectSocket}.  The object streams are flushed and closed.  Whether or not the underlying
     * socket is closed is determined by the <code>close_socket</code> parameter.  This extended method also calls
     * {@link QueuedObjectSocket#stopQueueThreads()}.
     *
     * @param closeSocket <code>true</code> if the underlying socket should also be closed; <code>false</code>
     *                    otherwise.
     * @throws IOException If an I/O error occurred while attempting the specified close operations.  Note: if an I/O
     *                     exception is thrown, the underlying socket close may not have been attempted.
     */
    public void close(boolean closeSocket)
            throws IOException
    {
        stopQueueThreads();
        super.close();
    }

    /**
     * Closes this {@link ObjectSocket}.  The object streams are flushed and closed and the underlying socket is closed.
     * All exceptions are completely ignored.  Each task is performed without regard to previous exceptions; that is, if
     * an exception occurs while closing an object stream, a closing call to the underlying socket will still be made.
     * This extended method also calls {@link QueuedObjectSocket#stopQueueThreads()}.
     *
     * @return A {@link String} containing stack traces from any exceptions which may have occurred.
     */
    public String closeCold()
    {
        stopQueueThreads();
        return super.closeCold();
    }

    /**
     * Closes this {@link ObjectSocket} after the outgoing queue is empty.  The object streams are flushed and closed
     * and the underlying socket is closed.  All exceptions are completely ignored.  Each task is performed without
     * regard to previous exceptions; that is, if an exception occurs while closing an object stream, a closing call to
     * the underlying socket will still be made. This extended method also calls {@link
     * QueuedObjectSocket#stopQueueThreads()}.
     * <p/>
     * If the queuing threads are not running, this method may block indefinitely.
     *
     * @return A {@link String} containing stack traces from any exceptions which may have occurred.
     */
    public String closeColdAfterSending()
    {
        synchronized (outgoingQueue)
        {
            while (outgoingQueue.size() > 0)
            {
                try
                {
                    outgoingQueue.wait();
                } catch (InterruptedException e)
                {
                }
            }
        }
        return this.closeCold();
    }

    /**
     * Produces a {@link String} describing this socket.
     *
     * @return A {@link String} describing this socket.
     */
    public String toString()
    {
        return "Queued Object Socket " + socket.getInetAddress().toString() + ":" + socket.getPort();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This object acts as a heartbeat message to keep the socket alive.
     *
     * @author Zachary Palmer
     */
    static class Heartbeat implements Serializable
    {
        /**
         * The singleton instance of this class.
         */
        public static final Heartbeat SINGLETON = new Heartbeat();

        /**
         * General constructor.
         */
        public Heartbeat()
        {
        }

        /**
         * Generates a hash code for the heartbeat message.  This hash code is always zero.
         */
        public int hashCode()
        {
            return 0;
        }

        /**
         * Tests for equality against another object.  This is <code>true</code> if the other object is of class {@link
         * Heartbeat}.
         *
         * @param o The other object.
         */
        public boolean equals(Object o)
        {
            return (o instanceof Heartbeat);
        }
    }
}

// END OF FILE