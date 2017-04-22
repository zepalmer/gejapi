package orioni.jz.awt.color;

import java.awt.*;

/**
 * This class is designed to allow two sRGB values (or {@link Color} objects) to be compared to each other; it is
 * <i>not</i> similar in function to the {@link java.util.Comparator} interface.  Depending upon intention, there may
 * be many different ways to describe how colors differ from each other.  This interface is designed to provide an
 * abstraction point around which methods which require color comparisons can build their functionality.
 *
 * @author Zachary Palmer
 */
public abstract class ColorComparator
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public ColorComparator()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is designed to compare two sRGB values to each other.  An sRGB value is a packed <code>int</code>
     * containing four 8-bit values: alpha, red, green, and blue (from highest bit to lowest bit).  This method
     * determines how different two sRGBs are in terms of a value in the range [<code>0.0</code>, <code>1.0</code>].
     * @param a The first sRGB value.
     * @param b The second sRGB value.
     * @return A value describing how different the values are from each other.
     */
    public abstract double compareColors(int a, int b);

    /**
     * This method is designed to compare two {@link Color}s to each other, describing how different they are from
     * each other in terms of a value in the range [<code>0.0</code>, <code>1.0</code>].
     * @param a The first {@link Color}.
     * @param b The second {@link Color}.
     * @return A value describing how different the values are from each other.
     */
    public double compareColors(Color a, Color b)
    {
        return compareColors(a.getRGB(),b.getRGB());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE