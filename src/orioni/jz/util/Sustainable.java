package orioni.jz.util;

/**
 * Any sustainable Object can be submitted to a orioni.jz.util.Sustainer.
 * A Sustainer runs a method on a Sustainable Object with a set frequency.  See
 * orioni.jz.util.Sustainer for more information.
 * @author Zachary Palmer
 */
public interface Sustainable
{
    /**
     * This method is the method to be performed by the Sustainer.
     */
    public void sustain();
}
