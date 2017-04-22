package orioni.jz.util.strings;

/**
 * This {@link IntegerInterpreter} extension only accepts an {@link Integer} if it falls within a certain bound.
 * Integers outside of this range are converted to <code>null</code>.
 *
 * @author Zachary Palmer
 */
public class BoundedIntegerInterpreter extends IntegerInterpreter
{
    /**
     * The lowest accepted {@link Integer} in this {@link ValueInterpreter}.
     */
    protected int minimum;
    /**
     * The highest accepted {@link Integer} in this {@link ValueInterpreter}.
     */
    protected int maximum;

    /**
     * General constructor.
     * @param minimum The lowest accepted {@link Integer} in this {@link ValueInterpreter}.
     * @param maximum The highest accepted {@link Integer} in this {@link ValueInterpreter}.
     */
    public BoundedIntegerInterpreter(int minimum, int maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Translates a {@link String} to the value of the specified type.
     *
     * @param s The string to convert.
     * @return The specified value, or <code>null</code> if the string cannot be converted.
     */
    public Integer fromString(String s)
    {
        Integer i = super.fromString(s);
        if ((i != null) && ((i < minimum) || (i > maximum))) i = null;
        return i;
    }

    /**
     * Translates an object of the specified type to a string.
     *
     * @param integer The value to convert.
     * @return A string representing that value, or <code>null</code> if the string cannot be produced.
     */
    public String toString(Integer integer)
    {
        if ((integer != null) && ((integer < minimum) || (integer > maximum))) return null;
        return super.toString(integer);
    }

    /**
     * Retrieves this interpreter's maximum bound.
     * @return This interpreter's maximum bound.
     */
    public int getMaximum()
    {
        return maximum;
    }

    /**
     * Retrieves this interpreter's minimum bound.
     * @return This interpreter's minimum bound.
     */
    public int getMinimum()
    {
        return minimum;
    }
}

// END OF FILE