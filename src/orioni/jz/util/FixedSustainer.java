package orioni.jz.util;

/**
 * This implementation of Sustainer calls the sustain method at a fixed
 * interval.
 */
public class FixedSustainer extends Sustainer
{
    /**
     * The period of time to wait between sustain starts.
     */
    protected long period;

    /**
     * General constructor.
     * @param s The Sustainable object to sustain.
     * @param period The period of time (in ms) between the beginning of each
     *               sustain.
     */
    public FixedSustainer(Sustainable s,long period)
    {
        super(s);
        this.period = period;
    }

    /**
     * Returns the amount of time to wait for a sustain.
     */
    public long getWait()
    {
        return period;
    }
}
