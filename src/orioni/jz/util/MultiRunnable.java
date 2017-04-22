package orioni.jz.util;

/**
 * This {@link Runnable} object accepts a number of other {@link Runnable} objects and runs them in serial.  This class
 * can be used to effectively "merge" two or more {@link Runnable} objects, ensuring that each only runs after the
 * previous has finished.
 *
 * @author Zachary Palmer
 */
public class MultiRunnable implements Runnable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link Runnable} objects, in their sequence of execution. */
    protected Runnable[] runnables;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param runnables The {@link Runnable} objects to use.
     */
    public MultiRunnable(Runnable... runnables)
    {
        super();
        this.runnables = runnables;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Executes the {@link Runnable} objects which were provided on construction, in sequence.
     *
     * @see Thread#run()
     */
    public void run()
    {
        for (Runnable r : runnables)
        {
            r.run();
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
