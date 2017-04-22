package orioni.jz.util;

/**
 * A ConditionalFixedSustainer behaves exactly like a FixedSustainer excepting
 * that it will sustain ConditionallySustainable objects and check their stop
 * flags.
 * @author Zachary Palmer
 */
public class ConditionalFixedSustainer extends FixedSustainer
{
    /**
     * Builds a Sustainer.
     * @param s The ConditionallySustainable object to sustain.
     * @param period The period of time between the start of each sustain, in
     *               milliseconds.
     */
    public ConditionalFixedSustainer(ConditionallySustainable s,int period)
    {
        super(s,period);
    }

    /**
     * Sustains the object as normal, but checks at the end of each sustaining
     * loop for whether or not the ConditionallySustainable object has requested
     * the cessation of the sustain.  Note that, even if the
     * ConditionallySustainable object's stop flag is set at the very beginning
     * of the call to run, it will be sustained once before the flag is checked
     * and the sustain is stopped.
     */
    public void run()
    {
        while (sustain)
	{
	    long timeOfSustain = System.currentTimeMillis();
	    sustainable.sustain();
	    long timeToWait =
	                getWait()-(System.currentTimeMillis()-timeOfSustain);
	    if (timeToWait >10)
	    {
	        try
	        {
	            Thread.currentThread().sleep(timeToWait);
                } catch (InterruptedException ie) {}
            }
	    if (((ConditionallySustainable)(sustainable)).shouldStop()) stop();
	}
    }
}
