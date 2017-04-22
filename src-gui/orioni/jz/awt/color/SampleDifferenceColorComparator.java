package orioni.jz.awt.color;

/**
 * This {@link ColorComparator} implementation compares colors by averaging the difference between their normalized
 * color samples (red, green, and blue).  The average of their normalized alpha values is then multiplied into the
 * difference (reflecting that half-opaque black is less different from half-opaque white than fully-opaque black is
 * different from fully-opaque white).
 *
 * @author Zachary Palmer
 */
public class SampleDifferenceColorComparator extends ColorComparator
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this class.
     */
    public static final SampleDifferenceColorComparator SINGLETON = new SampleDifferenceColorComparator();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public SampleDifferenceColorComparator()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is designed to compare two sRGB values to each other.  An sRGB value is a packed <code>int</code>
     * containing four 8-bit values: alpha, red, green, and blue (from highest bit to lowest bit).  This method
     * determines how different two sRGBs are in terms of a value in the range [<code>0.0</code>, <code>1.0</code>].
     *
     * @param a The first sRGB value.
     * @param b The second sRGB value.
     * @return A value describing how different the values are from each other.
     */
    public double compareColors(int a, int b)
    {
        int aAlpha = (a >>> 24) & 0xFF;
        int aRed = (a >>> 16) & 0xFF;
        int aGreen = (a >>> 8) & 0xFF;
        int aBlue = a & 0xFF;
        int bAlpha = (b >>> 24) & 0xFF;
        int bRed = (b >>> 16) & 0xFF;
        int bGreen = (b >>> 8) & 0xFF;
        int bBlue = b & 0xFF;

        return (Math.abs(aRed - bRed) + Math.abs(aGreen - bGreen) + Math.abs(aBlue - bBlue)) *
                (aAlpha + bAlpha) / 1170450.0f;
        // The above code embodies the below commented code:
        //sample_distance += Math.abs(aRed - bRed) / 255.0f;
        //sample_distance += Math.abs(aGreen - bGreen) / 255.0f;
        //sample_distance += Math.abs(aBlue - bBlue) / 255.0f;
        //sample_distance /= 3;
        //sample_distance *= (aAlpha + bAlpha) / 510.0f;
        //return sample_distance;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE