package orioni.jz.util;

/**
 * A Sustainer is a type of Object that runs Sustainable.sustain() at
 * calculated intervals.
 * @author Zachary Palmer
 */
public abstract class Sustainer implements Runnable
{
    /** The Object that is being sustained. */
    protected Sustainable sustainable;
    /** The Thread on which sustaining will run. */
    protected Thread sustainThread;
    /** Whether or not to continue sustaining. */
    protected boolean sustain;

    /**
     * Builds a Sustainer.
     * @param s The Sustainable object that this Sustainer will sustain.  :)
     */
    public Sustainer(Sustainable s)
    {
        sustainable = s;
        sustainThread = null;
        sustain = false;
    }

    /**
     * Starts sustaining the Sustainable object.
     * @return True if the Sustainable object is going to be sustained; false
     *         if the object is already being sustained.
     */
    public synchronized boolean start()
    {
        if (sustainThread != null) return false;
        sustain = true;
        sustainThread = new Thread(this);
        sustainThread.start();
        return true;
    }

    /**
     * Stops sustaining the Sustainable object.  The sustaining thread is left
     * to finish itself; it may not have terminated by the time the current
     * thread leaves the stop method.
     * @return True if sustain on the Sustainable object will be stopped; false
     *         if sustain was not being performed at the time of the call.
     */
    public synchronized boolean stop()
    {
        if (sustainThread == null) return false;
        sustain = false;
        return true;
    }

    /**
     * Runs the sustaining thread.  The thread calls the sustain method of the
     * Sustainable object and then waits the appropriate amount of time.
     */
    public void run()
    {
        while (sustain)
        {
            long timeOfSustain = System.currentTimeMillis();
            sustainable.sustain();
            long timeToWait =
                    getWait() - (System.currentTimeMillis() - timeOfSustain);
            if (timeToWait > 10)
            {
                try
                {
                    Thread.currentThread().sleep(timeToWait);
                } catch (InterruptedException ie)
                {
                }
            }
        }
    }

    /**
     * Retrieval method for the amount of time to wait between sustains.
     * @return A number of milliseconds indicating how long to wait between
     *         sustains.
     */
    public abstract long getWait();
}
