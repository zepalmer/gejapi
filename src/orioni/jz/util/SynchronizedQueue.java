package orioni.jz.util;

import orioni.jz.common.exception.InsufficientDataException;

import java.util.LinkedList;

/**
 * This class represents a basic queue data structure.  This queue is threadsafe.  Above and beyond {@link
 * java.util.Vector}, it provides a means by which dequeuing threads may (1) atomically dequeue data and (2) block until
 * data is available, if desired. <P> If threads are blocking for data, they may be freed by calling {@link
 * SynchronizedQueue#setBlockingAllowed(boolean)} with a parameter of <code>false</code>.  If this is done, threads will
 * throw {@link InsufficientDataException}s regardless of whether or not the user requests block-for-data behavior when
 * there is no data available.  This can be reversed by calling {@link SynchronizedQueue#setBlockingAllowed(boolean)
 * with a parameter of <code>true</code>.
 *
 * @author Zachary Palmer
 */
public class SynchronizedQueue <T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link LinkedList} which is used to back this queue.  This object is also used to supply the {@link
     * SynchronizedQueue#freeDequeuingThreads()} method with a synchronization object.
     */
    protected LinkedList<T> list;
    /**
     * <code>true</code> if threads are permitted to block while waiting for data; <code>false</code> otherwise.
     */
    protected boolean blockingAllowed;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public SynchronizedQueue()
    {
        super();
        list = new LinkedList<T>();
        blockingAllowed = true;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not this queue is empty.
     *
     * @return <code>true</code> if this queue is empty; <code>false</code> otherwise.
     */
    public synchronized boolean isEmpty()
    {
        return list.isEmpty();
    }

    /**
     * Retrieves the object from the front of the queue.  If there is no data in the queue, the calling thread will
     * block until data becomes available.
     *
     * @return The {@link Object} at the front of the queue.
     * @throws InsufficientDataException If blocking is not allowed and there is no data in the queue.
     */
    public synchronized T front()
            throws InsufficientDataException
    {
        return front(true);
    }

    /**
     * Enqueues an {@link Object}.
     *
     * @param o The {@link Object} to enqueue.
     */
    public synchronized void enqueue(T o)
    {
        list.add(o);
        this.notify();
    }

    /**
     * Dequeues an {@link Object}.  If there is no data in this queue, the calling thread will block until data becomes
     * available.
     *
     * @return An {@link Object} retrieved from the queue.
     * @throws InsufficientDataException If blocking is not allowed and there is no data in the queue.
     */
    public synchronized T dequeue()
            throws InsufficientDataException
    {
        return dequeue(true);
    }

    /**
     * Dequeues an {@link Object}.  In the event that there is no data in the queue, the behavior of this method is
     * determined by the <code>block</code> variable.  If <code>block</code> is <code>true</code>, then the calling
     * thread will block until data becomes available.  If <code>block</code> is <code>false</code> (or if blocking is
     * disallowed), an {@link InsufficientDataException} is thrown.
     *
     * @param block Whether or not this method should block waiting for data if no data is available.  Note that this
     *              parameter is ineffective when blocking is not allowed.
     * @return An {@link Object} retrieved from the queue.
     * @throws InsufficientDataException If either (A) <code>block</code> is <code>false</code> and the queue contains
     *                                   no data or (B) <code>block</code> is <code>true</code>, the queue contains no
     *                                   data, and blocking is not allowed.
     */
    public synchronized T dequeue(boolean block)
            throws InsufficientDataException
    {
        T ret = front(block);
        list.remove(0);
        return ret;
    }

    /**
     * Retrieves an {@link Object} from the front of the queue.  In the event that there is no data in the queue, the
     * behavior of this method is determined by the <code>block</code> variable.  If <code>block</code> is
     * <code>true</code>, then the calling thread will block until data becomes available.  If <code>block</code> is
     * <code>false</code> (or if blocking is disallowed), an {@link InsufficientDataException} is thrown.
     *
     * @param block Whether or not this method should block waiting for data if no data is available.  Note that this
     *              parameter is ineffective when blocking is not allowed.
     * @return The {@link Object} at the front of the queue.
     * @throws InsufficientDataException If either (A) <code>block</code> is <code>false</code> and the queue contains
     *                                   no data or (B) <code>block</code> is <code>true</code>, the queue contains no
     *                                   data, and blocking is not allowed.
     */
    public synchronized T front(boolean block)
            throws InsufficientDataException
    {
        if ((block) && (blockingAllowed))
        {
            synchronized (list)
            {
                while (list.size() == 0)
                {
                    if (!blockingAllowed)
                    {
                        throw new InsufficientDataException("No data remains in the queue.");
                    }
                    try
                    {
                        this.wait();
                    } catch (InterruptedException e)
                    {
                    }
                }
            }
        } else
        {
            if (list.size() == 0)
            {
                throw new InsufficientDataException("No data remains in the queue.");
            }
        }
        return list.get(0);
    }

    /**
     * Reports the current number of elements contained within the queue.
     *
     * @return The current number of elements contained within the queue.
     */
    public synchronized int size()
    {
        return list.size();
    }

    /**
     * Empties the queue.
     */
    public synchronized void clear()
    {
        list.clear();
    }

    /**
     * Sets whether or not blocking for data when none is available is allowed.
     *
     * @param blockingAllowed <code>true</code> if threads should be permitted to wait for data if none is available;
     *                         <code>false</code> otherwise.
     */
    public synchronized void setBlockingAllowed(boolean blockingAllowed)
    {
        boolean old = this.blockingAllowed;
        this.blockingAllowed = blockingAllowed;
        if (old)
        {
            this.notifyAll();
        }
    }

    /**
     * Causes the calling thread to free all dequeuing threads.  This is accomplished by first setting blocking to
     * <code>false</code> and then waiting for the dequeuing threads to escape.  Once this has been accomplished, the
     * blocking state of this queue will be returned to normal.
     */
    public void freeDequeuingThreads()
    {
        boolean blocking;
        synchronized (this)
        {
            blocking = blockingAllowed;
            setBlockingAllowed(false);
            this.notifyAll();
        }
        synchronized (list)
        {
        }
        setBlockingAllowed(blocking);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}