package orioni.jz.math;

/**
 * This class is intended to represent fractions.  Fractions are, of course, represented by a numerator and a
 * denomenator, both integers.  This class uses <code>long</code>s to represent its fractions and thus can support
 * numerators and denomenators of up to 2<sup>63</sup>-1.  The denomenator of a {@link Fraction} is never negative; if a
 * negative denomenator is passed on construction, both values have their signs inverted.
 *
 * @author Zachary Palmer
 */
public class Fraction extends Number
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A {@link Fraction} representing the value <code>0</code>.
     */
    public static final Fraction ZERO = new Fraction(0, 1);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The numerator of this fraction.
     */
    protected final long numerator;
    /**
     * The denomenator of this fraction.
     */
    protected final long denomenator;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a denomenator of <code>1</code>.
     *
     * @param numerator The numerator of this fraction.
     */
    public Fraction(long numerator)
    {
        this(numerator, 1);
    }

    /**
     * General constructor.
     *
     * @param numerator   The numerator of this fraction.
     * @param denomenator The denomenator of this fraction.
     */
    public Fraction(long numerator, long denomenator)
    {
        super();
        if (denomenator < 0)
        {
            denomenator = -denomenator;
            numerator = -numerator;
        } else if (denomenator == 0)
        {
            throw new ArithmeticException("Fraction has zero denomenator.");
        }
        this.denomenator = denomenator;
        this.numerator = numerator;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the value of the specified number as a <code>double</code>. This may involve rounding.
     *
     * @return the numeric value represented by this object after conversion to type <code>double</code>.
     */
    public double doubleValue()
    {
        return ((double) numerator) / denomenator;
    }

    /**
     * Returns the value of the specified number as a <code>float</code>. This may involve rounding.
     *
     * @return the numeric value represented by this object after conversion to type <code>float</code>.
     */
    public float floatValue()
    {
        return ((float) numerator) / denomenator;
    }

    /**
     * Returns the value of the specified number as an <code>int</code>. This may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion to type <code>int</code>.
     */
    public int intValue()
    {
        return (int) (numerator / denomenator);
    }

    /**
     * Returns the value of the specified number as a <code>long</code>. This may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion to type <code>long</code>.
     */
    public long longValue()
    {
        return numerator / denomenator;
    }

    /**
     * Retrieves the denomenator of this fraction.
     *
     * @return The denomenator of this fraction.
     */
    public long getDenomenator()
    {
        return denomenator;
    }

    /**
     * Retrieves the numerator of this fraction.
     *
     * @return The numerator of this fraction.
     */
    public long getNumerator()
    {
        return numerator;
    }

    /**
     * Returns a string representation of this fraction.
     *
     * @return A string representation of this fraction.
     */
    public String toString()
    {
        return numerator + "/" + denomenator;
    }

    /**
     * Reduces this fraction.  The numerator and denomenator are divided by their greatest common factor and the result
     * is returned as a new fraction.
     *
     * @return The reduced fraction.
     */
    public Fraction reduce()
    {
        long div = MathUtilities.gcd(numerator, denomenator);
        return new Fraction(numerator / div, denomenator / div);
    }

    /**
     * Adds this fraction to another fraction.  If the least common multiple of the two fractions' denomenators is
     * larger than the value which fits in the denomenator field, nothing happens.
     * <p/>
     * TODO: arrange for an ArithmeticException to be thrown.
     *
     * @param other The fraction to add to this one.
     * @return The sum of those two fractions.
     */
    public Fraction plus(Fraction other)
    {
        long thisD = this.getDenomenator();
        long otherD = other.getDenomenator();
        long newD = MathUtilities.lcm(thisD, otherD);
        long newN = this.getNumerator() * (newD / thisD) + other.getNumerator() * (newD / otherD);
        return new Fraction(newN, newD);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE