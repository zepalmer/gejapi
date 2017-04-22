package orioni.jz.awt.color;

/**
 * This {@link ColorComparator} implementation compares colors based upon the average of the normalized differences of
 * their individual color samples.  However, any difference in alpha value is assumed to be complete difference. That
 * is, if two colors (A and B) have alpha values A<sub>a</sub> and B<sub>a</sub>, <code>d<sub>a</sub> =
 * abs(A<sub>a</sub> - B<sub>a</sub>)</code>, and the function f(A,B) determines the difference (ignoring transparency)
 * of the two colors, the resulting difference is <code>f(A,B) * (1 - d<sub>a</sub>) + d<sub>a</sub></code>.
 *
 * @author Zachary Palmer
 */
public class TransparencyCriticizingSampleDifferenceComparator extends ColorComparator
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this class.
     */
    public static final TransparencyCriticizingSampleDifferenceComparator SINGLETON =
            new TransparencyCriticizingSampleDifferenceComparator();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public TransparencyCriticizingSampleDifferenceComparator()
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

        double aDiff = Math.abs(aAlpha - bAlpha) / 255.0f;

        return aDiff +
               (1 - aDiff) *
               ((Math.abs(aRed - bRed) + Math.abs(aGreen - bGreen) + Math.abs(aBlue - bBlue)) / 765.0f);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE