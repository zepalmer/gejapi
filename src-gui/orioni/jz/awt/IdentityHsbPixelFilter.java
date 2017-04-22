package orioni.jz.awt;

/**
 * This {@link HsbPixelFilter} implementation simply accepts all pixels provided to it.
 *
 * @author Zachary Palmer
 */
public class IdentityHsbPixelFilter implements HsbPixelFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this class.
     */
    public static final IdentityHsbPixelFilter SINGLETON = new IdentityHsbPixelFilter();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public IdentityHsbPixelFilter()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Accepts a described HSB pixel.
     *
     * @param hue        Ignored.
     * @param saturation Ignored.
     * @param brightness Ignored.
     * @return <code>true</code>, always.
     */
    public boolean filter(double hue, double saturation, double brightness)
    {
        return true;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
