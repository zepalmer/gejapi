package orioni.jz.math;

import orioni.jz.util.DefaultValueHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is designed to represent a polynomial.  The coefficients can be {@link Fraction}s, <code>long</code>s,
 * <code>int</code>s, or <code>double</code>s.  This class provides basic operations which may be necessary in handling
 * polynomials such as factoring, division, and, of course, evaluation.
 * <p/>
 * The order of polynomials created by this class is limited to <code>2<sup>31</sup>-1</code>.
 *
 * @author Zachary Palmer
 */
public class UnivariateRationalPolynomial
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link HashMap} used to represent the coefficients of this polynomial.
     */
    protected HashMap<Integer, Fraction> coefficientMap;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Converts the provided array into a mapping as described in {@link
     * UnivariateRationalPolynomial(Map)} with the assumption that the item with index <code>n</code> in the array is
     * the coefficient for the <code>x<sup>n</sup></code> term.  For all terms <code>x<sup>n</sup></code> where
     * <code>n</code> is greater than the length of the array, the coefficient is assumed to be zero.
     *
     * @param array The array of <code>int</code>s to use as the coefficients for the polynomial with the first element
     *              being the constant coefficient.
     */
    public UnivariateRationalPolynomial(int[] array)
    {
        super();
        Map<Integer, Fraction> map = new HashMap<Integer, Fraction>();
        for (int i = 0; i < array.length; i++)
        {
            map.put(i, new Fraction(array[i], 1));
        }
        initialize(map);
    }

    /**
     * Skeleton constructor.  Converts the provided array into a mapping as described in {@link
     * UnivariateRationalPolynomial(Map)} with the assumption that the item with index <code>n</code> in the array is
     * the coefficient for the <code>x<sup>n</sup></code> term.  For all terms <code>x<sup>n</sup></code> where
     * <code>n</code> is greater than the length of the array, the coefficient is assumed to be zero.
     *
     * @param array The array of <code>long</code>s to use as the coefficients for the polynomial with the first element
     *              being the constant coefficient.
     */
    public UnivariateRationalPolynomial(long[] array)
    {
        super();
        Map<Integer, Fraction> map = new HashMap<Integer, Fraction>();
        for (int i = 0; i < array.length; i++)
        {
            map.put(i, new Fraction(array[i], 1));
        }
        initialize(map);
    }

    /**
     * Skeleton constructor.  Converts the provided array into a mapping as described in {@link
     * UnivariateRationalPolynomial(Map)} with the assumption that the item with index <code>n</code> in the array is
     * the coefficient for the <code>x<sup>n</sup></code> term.  For all terms <code>x<sup>n</sup></code> where
     * <code>n</code> is greater than the length of the array, the coefficient is assumed to be zero.
     *
     * @param array The array of {@link Fraction}s to use as the coefficients for the polynomial with the first element
     *              being the constant coefficient.
     */
    public UnivariateRationalPolynomial(Fraction[] array)
    {
        super();
        Map<Integer, Fraction> map = new HashMap<Integer, Fraction>();
        for (int i = 0; i < array.length; i++)
        {
            map.put(i, array[i]);
        }
        initialize(map);
    }

    /**
     * General constructor.
     *
     * @param coefficientMap The mapping between the indices of coefficients and the coefficients themselves.  All
     *                        coefficients without mappings in this map will be assumed to have a value of zero.  That
     *                        is, for a mapping with the following entries <UL> <code>0 -> 9<br> 1 -> 3<br> 3 -> -2<br>
     *                        150 -> 1</code> </UL> the polynomial <code>x<sup>150</sup>-2x<sup>3</sup>+3x+9</code> will
     *                        be described.
     * @throws IllegalArgumentException If the provided mapping has a negative key, as polynomials are not permitted to
     *                                  contain terms where the exponent of <code>x</code> is negative.
     */
    public UnivariateRationalPolynomial(Map<Integer, Fraction> coefficientMap)
    {
        for (int i : coefficientMap.keySet())
        {
            if (i < 0) throw new IllegalArgumentException("Illegally negative key in mapping: " + i);
        }
        initialize(coefficientMap);
    }

    /**
     * Actually initializes the {@link UnivariateRationalPolynomial} class.
     *
     * @param map The mapping between coefficient index and the coefficient itself.
     */
    private void initialize(Map<Integer, Fraction> map)
    {
        coefficientMap = new DefaultValueHashMap<Integer, Fraction>(map, Fraction.ZERO);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a {@link UnivariateRationalPolynomial} to this one.  This {@link UnivariateRationalPolynomial} is modified.
     *
     * @param other The {@link UnivariateRationalPolynomial} to add to this one.
     */
    public void add(UnivariateRationalPolynomial other)
    {
        for (int exponent : other.getNonzeroTermSet())
        {
            coefficientMap.put(exponent, coefficientMap.get(exponent).plus(other.getCoefficient(exponent)));
        }
    }

    /**
     * Retrieves the {@link Set} of exponents for which the coefficients in this polynomial are not zero.
     *
     * @return The exponents for this polynomial with non-zero coefficients.
     */
    public Set<Integer> getNonzeroTermSet()
    {
        return new HashSet<Integer>(coefficientMap.keySet());
    }

    /**
     * Retrieves the coefficient of this polynomial for the term with the specified exponent.
     *
     * @param exponent The exponent of the term for which a coefficient is desired.
     * @return The coefficient of that term.
     */
    public Fraction getCoefficient(int exponent)
    {
        return coefficientMap.get(exponent);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE