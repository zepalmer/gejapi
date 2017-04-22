package orioni.jz.math;

import java.math.BigInteger;

// TODO: complete implementation

/**
 * This {@link LargeInteger} implementation behaves identically to a {@link LargeInteger} except that its stored value
 * cannot be modified.  Attempts to do so cause an {@link IllegalAccessException}.
 *
 * @author Zachary Palmer
 */
public class MutableLargeInteger extends LargeInteger
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(int[],boolean)
     */
    public MutableLargeInteger(int[] data, boolean sign)
    {
        super(data, sign);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(int[])
     */
    public MutableLargeInteger(int[] data)
    {
        super(data);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(byte[],boolean)
     */
    public MutableLargeInteger(byte[] data, boolean sign)
    {
        super(data, sign);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(byte[])
     */
    public MutableLargeInteger(byte[] data)
    {
        super(data);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(long)
     */
    public MutableLargeInteger(long value)
    {
        super(value);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(int)
     */
    public MutableLargeInteger(int value)
    {
        super(value);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger(LargeInteger)
     */
    public MutableLargeInteger(LargeInteger largeint)
    {
        super(largeint);
    }

    /**
     * Wrapper constructor.
     *
     * @see LargeInteger#LargeInteger()
     */
    public MutableLargeInteger()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    // *** Manipulation Methods : Destructive ***

    /**
     * <b>Destructive:</b> Inverts the sign on this {@link LargeInteger} <i>if</i> this {@link LargeInteger} is
     * negative.
     *
     * @return This {@link LargeInteger}, after the changes are applied.
     */
    public MutableLargeInteger makeAbsoluteValue()
    {
        sign = true;
        return this;
    }

    /**
     * <b>Destructive:</b> Inverts the sign on this {@link LargeInteger}.
     *
     * @return This {@link LargeInteger}, after the changes are applied.
     */
    public MutableLargeInteger negate()
    {
        if ((data.length > 1) || (data[0] != 0))
        {
            sign = !sign;
        }
        return this;
    }

    /**
     * <b>Destructive:</b> Adds the provided {@link LargeInteger} to this {@link LargeInteger}.
     *
     * @param other The number to add to this number.
     * @return This {@link LargeInteger}, after the changes are applied.
     */
    public MutableLargeInteger add(LargeInteger other)
    {
        if (getSign() == other.getSign())
        {
            data = add(data, other.data, false, true);
        } else
        {
            int[] a;
            int[] b;
            boolean sign;
            if (compareTo(other, true) >= 0)
            {
                sign = getSign();
                a = data;
                b = other.data;
            } else
            {
                sign = other.getSign();
                a = other.data;
                b = data;
            }
            b = twosComplement(b);
            data = add(a, b, true, true);
            this.sign = sign;
        }
        return this;
    }

    /**
     * <b>Destructive:</b> Subtracts the provided number from this number.
     *
     * @param other The number to subtract from this number.
     * @return This {@link LargeInteger}, after the changes are applied.
     */
    public MutableLargeInteger subtract(LargeInteger other)
    {
        return negate().add(other).negate();
    }

    /**
     * <b>Destructive:</b> Multiplies this number by the provided number.
     *
     * @param other The number with which to multiply this number.
     * @return This {@link LargeInteger}, after the changes are applied.
     */
    public MutableLargeInteger multiply(LargeInteger other)
    {
        sign = getSign() == other.getSign();
        data = multiply(data, other.data);
        return this;
    }

    /**
     * <b>Destructive:</b> Divides this number by the provided number.
     *
     * @param other The number by which to divide this number.
     * @return This {@link LargeInteger}, after the changes are applied.
     */
    public MutableLargeInteger divide(LargeInteger other)
    {
        data = divide(data, other.data);
        sign = getSign() == other.getSign();
        return this;
    }

    /**
     * <b>Destructive:</b> Moduluses this {@link LargeInteger} by the provided <code>int</code>.
     *
     * @param other The number to modulus with this number.
     * @return The resulting number.
     */
    public MutableLargeInteger modulo(int other)
    {
        MutableLargeInteger liOther = new MutableLargeInteger(other);
        return modulo(liOther);
    }

    /**
     * <b>Destructive:</b> Moduluses this {@link LargeInteger} by the provided <code>long</code>.
     *
     * @param other The number to modulus with this number.
     * @return The resulting number.
     */
    public MutableLargeInteger modulo(long other)
    {
        MutableLargeInteger liOther = new MutableLargeInteger(other);
        return modulo(liOther);
    }

    /**
     * <b>Destructive:</b> Moduluses this {@link LargeInteger} by the provided {@link LargeInteger}.
     *
     * @param other The number to modulus with this number.
     * @return The resulting number.
     */
    public MutableLargeInteger modulo(LargeInteger other)
    {
        // the sign of the modulus base is insignificant
        if (other.equalTo(ZERO)) throw new ArithmeticException("n%0 is undefined");
        data = modulo(data, other.data);
        return this;
    }

    /**
     * <b>Destructive:</b> Raises this number to the specified power.
     *
     * @param other The number to which to raise this number.
     * @return This {@link LargeInteger}, after the changes are applied.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public MutableLargeInteger power(int other)
    {
        if (other == 0)
        {
            if ((data.length == 0) && (data[0] == 0)) throw new ArithmeticException("0^0 is undefined");
            data = duplicateArray(ARRAY_VALUE_ONE);
            sign = true;
        } else
        {
            int[] data = this.data;
            for (int i = 1; i < other; i++)
            {
                this.data = multiply(this.data, data);
            }
            sign = getSign() || (other % 2 == 0);
        }
        return this;
    }

    /**
     * <b>Destructive:</b> Raises this number to the specified power.
     *
     * @param other The number to which to raise this number.
     *              <p/>
     *              <i>Note:</i> Raising any {@link LargeInteger} other than <code>-1</code>, <code>0</code>, or
     *              <code>1</code> to a power <code>n</code> will require at least <code>n+1</code> bits of memory to
     *              store.  Therefore, raising a {@link LargeInteger} to a power greater than <code>17179869184</code>
     *              will consume 2Gb of memory.  Presently (J2SDK v1.5.0), JVMs cannot address more than 2Gb of RAM;
     *              therefore, raising a {@link LargeInteger} to this power will cause a crash.
     * @return This {@link LargeInteger}, after the changes are applied.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public MutableLargeInteger power(long other)
    {
        if (other == 0)
        {
            if ((data.length == 0) && (data[0] == 0)) throw new ArithmeticException("0^0 is undefined");
            data = duplicateArray(ARRAY_VALUE_ONE);
            sign = true;
        } else
        {
            int[] data = this.data;
            for (int i = 1; i < other; i++)
            {
                this.data = multiply(this.data, data);
            }
            sign = getSign() || (other % 2 == 0);
        }
        return this;
    }

    /**
     * <b>Destructive:</b> Raises this number to the specified power.
     *
     * @param other The number to which to raise this number.
     *              <p/>
     *              <i>Note:</i> Raising any {@link LargeInteger} other than <code>-1</code>, <code>0</code>, or
     *              <code>1</code> to a power <code>n</code> will require at least <code>n+1</code> bits of memory to
     *              store.  Therefore, raising a {@link LargeInteger} to a power greater than <code>17179869184</code>
     *              will consume 2Gb of memory.  Presently (J2SDK v1.5.0), JVMs cannot address more than 2Gb of RAM;
     *              therefore, raising a {@link LargeInteger} to this power will cause a crash.
     * @return This {@link LargeInteger}, after the changes are applied.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public MutableLargeInteger power(LargeInteger other)
    {
        if (other.lessThanOrEqualTo(LargeInteger.LONG_MAX_VALUE)) return power(other.getLongValue());
        if ((other.data.length == 1) && (other.data[0] == 0))
        {
            if ((data.length == 0) && (data[0] == 0)) throw new ArithmeticException("0^0 is undefined");
            data = duplicateArray(ARRAY_VALUE_ONE);
            sign = true;
        } else
        {
            int[] data = this.data;
            MutableLargeInteger i = ONE.copy();
            while (i.lessThan(other))
            {
                this.data = multiply(this.data, data);
                i.add(ONE);
            }
            sign = getSign() || (other.modulusedBy(2).equalTo(ZERO));
        }
        return this;
    }

    /**
     * <b>Destructive:</b> Shifts this number left by the specified number of bits.  The sign of the {@link
     * LargeInteger} is not affected by this operation.
     *
     * @param shift The number of bits to shift left.
     * @return The resulting {@link BigInteger}.
     */
    public MutableLargeInteger shiftLeft(int shift)
    {
        data = arrayShiftLeft(data, shift);
        return this;
    }

    /**
     * <b>Destructive:</b> Shifts this number left by the specified number of bits.  The sign of the {@link
     * LargeInteger} is not affected by this operation.
     *
     * @param shift The number of bits to shift left.
     * @return The resulting {@link BigInteger}.
     */
    public MutableLargeInteger shiftRight(int shift)
    {
        data = arrayShiftRight(data, shift);
        return this;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE