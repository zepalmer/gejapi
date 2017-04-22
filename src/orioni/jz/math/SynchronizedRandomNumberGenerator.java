package orioni.jz.math;

import java.util.Random;

/**
 * This random number generator uses {@link Random} for its operations but serializes access to all methods.
 *
 * @author Zachary Palmer
 */
public class SynchronizedRandomNumberGenerator extends Random
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     * @see Random#Random()
     */
    public SynchronizedRandomNumberGenerator()
    {
        super();
    }

    /**
     * Wrapper constructor.
     * @see Random#Random(long)
     */
    public SynchronizedRandomNumberGenerator(long seed)
    {
        super(seed);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Synchronized wrapper method for the {@link Random} class.
     * @see Random#nextDouble()
     */
    public synchronized double nextDouble()
    {
        return super.nextDouble();
    }

    /**
     * Synchronized wrapper method for the {@link Random} class.
     * @see Random#nextInt(int)
     */
    public synchronized int nextInt(int n)
    {
        return super.nextInt(n);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE