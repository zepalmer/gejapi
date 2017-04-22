package orioni.jz.awt;

/**
 * This interface is designed to permit the creation of HSB pixel filters.  The filter either accepts or rejects pixels
 * based upon their HSB color values.  The particular contextual meaning of this acceptance or rejection is dependent
 * upon the context in which they are used.
 *
 * @author Zachary Palmer
 */
public interface HsbPixelFilter
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Accepts or rejects a described HSB pixel.
     *
     * @param hue        The hue of the pixel, expressed in terms of a number within <code>[0,1]</code>.
     * @param saturation The saturation of the pixel, expressed in terms of a number within <code>[0,1]</code>.
     * @param brightness The brightness of the pixel, expressed in terms of a number within <code>[0,1]</code>.
     * @return <code>true</code> if this filter accepts the pixel; <code>false</code> if it does not.
     */
    public boolean filter(double hue, double saturation, double brightness);

}
