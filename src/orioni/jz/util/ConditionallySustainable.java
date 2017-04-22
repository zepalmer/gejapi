package orioni.jz.util;

/**
 * This extension to the standard Sustainable interface allows the object
 * implementing it to stop the sustaining thread by setting a flag.  This flag
 * is declared within the implementation.
 * @author Zachary Palmer
 */
public interface ConditionallySustainable extends Sustainable
{
    /** Whether or not the sustaining loop should be stopped. */
    public boolean shouldStop();
}
