package orioni.jz.math;

import orioni.jz.util.Pair;

import java.math.BigInteger;
import java.util.Arrays;

// TODO: complete implementation

/**
 * This class is designed to perform similarly to a {@link BigInteger}.  However, as {@link BigInteger} lacks
 * functionality to interact efficiently with non-{@link BigInteger} classes, this class was constructed for that
 * purpose.
 * <p/>
 * The manipulation operations on this class are designed to not modify the contents of this {@link LargeInteger}. If
 * one wishes to use the same {@link LargeInteger} object to store multiple different values, {@link
 * MutableLargeInteger} should be used.
 * <p/>
 * As the manipulation methods on this class produce new {@link LargeInteger} objects rather than modifying the called
 * object, they are labeled as "non-destructive" and are written in terms of past-tense adjectives or mathematical
 * prepositions (such as "left-shifted" or "minus"). This is as opposed to the manipulation operations on {@link
 * MutableLargeInteger} which modify the object on which the method is called are labeled "destructive" and are written
 * in terms of present-tense verbs (such as "shift left" or "subtract").
 *
 * @author Zachary Palmer
 */
public class LargeInteger
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A mask which selects bit number 31 in a 32-bit integer.
     */
    static final byte HIGHEST_BYTE_BIT_MASK = (byte) (0x80);
    /**
     * A mask which selects bit number 31 in a 32-bit integer.
     */
    static final int HIGHEST_INT_BIT_MASK = 0x80000000;
    /**
     * A mask which selects all but the highest bit in a <code>long</code>.
     */
    static final int ALL_BUT_HIGHEST_INT_BIT_MASK = 0x7FFFFFFF;
    /**
     * A mask which contains all bits on for a 32-bit integer.
     */
    static final int ALL_INT_BIT_MASK = 0xFFFFFFFF;
    /**
     * A mask which selects all but the highest bit in a <code>long</code>.
     */
    static final long FIRST_31_BITS_LONG_BIT_MASK = 0x000000007FFFFFFFL;
    /**
     * A mask which selects all but the highest bit in a <code>long</code>.
     */
    static final long FIRST_32_BITS_LONG_BIT_MASK = 0x00000000FFFFFFFFL;
    /**
     * A mask which selects all but the highest bit in a <code>long</code>.
     */
    static final long ALL_BUT_HIGHEST_LONG_BIT_MASK = 0x7FFFFFFFFFFFFFFFL;
    /**
     * A mask which contains all bits on for a 64-bit long integer.
     */
    static final long ALL_LONG_BIT_MASK = 0xFFFFFFFFFFFFFFFFL;

    /**
     * An array containing a mask for each bit.
     */
    static final int[] SINGLE_BIT_MASK = new int[]{
            0x00000001, 0x00000002, 0x00000004, 0x00000008, 0x00000010, 0x00000020, 0x00000040, 0x00000080,
            0x00000100, 0x00000200, 0x00000400, 0x00000800, 0x00001000, 0x00002000, 0x00004000, 0x00008000,
            0x00010000, 0x00020000, 0x00040000, 0x00080000, 0x00100000, 0x00200000, 0x00400000, 0x00800000,
            0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000, 0x40000000, 0x80000000};

    /**
     * An <code>int[]</code> containing a value which is interpreted as <code>1</code>.
     */
    static final int[] ARRAY_VALUE_ONE = new int[]{1};
    /**
     * An <code>int[]</code> containing a value which is interpreted as <code>1</code>.
     */
    static final int[] ARRAY_VALUE_ZERO = new int[]{0};

    /**
     * A {@link LargeInteger} containing the value <code>0</code>.
     */
    public static final LargeInteger ZERO = new LargeInteger(0);
    /**
     * A {@link LargeInteger} containing the value <code>1</code>.
     */
    public static final LargeInteger ONE = new LargeInteger(1);
    /**
     * A {@link LargeInteger} containing the value <code>-1</code>.
     */
    public static final LargeInteger NEGATIVE_ONE = new LargeInteger(-1);

    /**
     * A {@link LargeInteger} containing the minimum value for <code>int</code> variables.
     */
    public static final LargeInteger INT_MIN_VALUE = new LargeInteger(Integer.MIN_VALUE);
    /**
     * A {@link LargeInteger} containing the maximum value for <code>int</code> variables.
     */
    public static final LargeInteger INT_MAX_VALUE = new LargeInteger(Integer.MAX_VALUE);
    /**
     * A {@link LargeInteger} containing the minimum value for <code>long</code> variables.
     */
    public static final LargeInteger LONG_MIN_VALUE = new LargeInteger(Integer.MIN_VALUE);
    /**
     * A {@link LargeInteger} containing the maximum value for <code>long</code> variables.
     */
    public static final LargeInteger LONG_MAX_VALUE = new LargeInteger(Integer.MAX_VALUE);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The <code>int[]</code> in which the data for this {@link LargeInteger} is stored.  This information is contained
     * in big-endian format.  This field is always the smallest array which can contain this information.  Values of
     * <code>0</code> have an array size of <code>1</code>.
     */
    protected int[] data;
    /**
     * The sign of this number.  <code>true</code> indicates positive; <code>false</code> indicates negative.  Values of
     * <code>0</code> must have a positive sign to ensure only one possible representation for <code>0</code>.
     */
    protected boolean sign;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes this {@link LargeInteger} to contain <code>0</code>.  The initial size of the
     * integer is 128 bits.
     */
    public LargeInteger()
    {
        this(new int[1]);
    }

    /**
     * Skeleton constructor.  Initializes this {@link LargeInteger} with an <code>int</code> value.
     *
     * @param value The <code>int</code> value to store in this {@link LargeInteger}.
     */
    public LargeInteger(int value)
    {
        this(new int[]{value});
    }

    /**
     * Skeleton constructor.  Initializes this {@link LargeInteger} with an <code>long</code> value.
     *
     * @param value The <code>long</code> value to store in this {@link LargeInteger}.
     */
    public LargeInteger(long value)
    {
        this(new int[]{(int) (value >>> 32), (int) (value)});
    }

    /**
     * General constructor.
     *
     * @param data The <code>byte[]</code> which contains the number to store in this {@link LargeInteger}.  This array
     *             will be interpreted as a <i>signed</i>, big-endian number.
     */
    public LargeInteger(byte[] data)
    {
        int[] newdata = new int[(data.length + 3) / 4];
        boolean sign = (data[0] & HIGHEST_BYTE_BIT_MASK) == 0;
        int indexByte = data.length - 1;
        while (indexByte >= 0)
        {
            int first, second, third, fourth;
            fourth = (indexByte >= 0) ? data[indexByte--] : (sign ? 0 : 0xFF);
            third = (indexByte >= 0) ? data[indexByte--] : (sign ? 0 : 0xFF);
            second = (indexByte >= 0) ? data[indexByte--] : (sign ? 0 : 0xFF);
            first = (indexByte >= 0) ? data[indexByte--] : (sign ? 0 : 0xFF);

            newdata[(indexByte == 0) ? 0 : (indexByte + 3) / 4] = ((first & 0xFF) << 24) |
                                                                  ((second & 0xFF) << 16) |
                                                                  ((third & 0xFF) << 8) |
                                                                  (fourth & 0xFF);
        }
        initialize(newdata);
    }

    /**
     * General constructor.
     *
     * @param data The <code>int[]</code> which contains the number to store in this {@link LargeInteger}.  This array
     *             will be interpreted as an <i>unsigned</i>, big-endian number.
     * @param sign <code>true</code> if this number is positive; <code>false</code> if it is negative.
     */
    public LargeInteger(byte[] data, boolean sign)
    {
        int[] newdata = new int[(data.length + 3) / 4];
        int indexByte = data.length - 1;
        while (indexByte >= 0)
        {
            int first, second, third, fourth;
            fourth = (indexByte >= 0) ? data[indexByte--] : 0;
            third = (indexByte >= 0) ? data[indexByte--] : 0;
            second = (indexByte >= 0) ? data[indexByte--] : 0;
            first = (indexByte >= 0) ? data[indexByte--] : 0;

            newdata[(indexByte + 3) / 4] = ((first & 0xFF) << 24) |
                                           ((second & 0xFF) << 16) |
                                           ((third & 0xFF) << 8) |
                                           (fourth & 0xFF);
        }
        initialize(newdata, sign);
    }

    /**
     * General constructor.
     *
     * @param data The <code>int[]</code> which contains the number to store in this {@link LargeInteger}.  This array
     *             will be interpreted as a <i>signed</i>, big-endian number.
     */
    public LargeInteger(int[] data)
    {
        initialize(data);
    }

    /**
     * General constructor.
     *
     * @param data The <code>int[]</code> which contains the number to store in this {@link LargeInteger}.  This array
     *             will be interpreted as an <i>unsigned</i>, big-endian number.
     * @param sign <code>true</code> if this number is positive; <code>false</code> if it is negative.
     */
    public LargeInteger(int[] data, boolean sign)
    {
        initialize(data, sign);
    }

    /**
     * Skeleton constructor.  Sets this {@link LargeInteger} equal in value to the provided {@link LargeInteger}.
     *
     * @param largeint The {@link LargeInteger} to equal.
     */
    public LargeInteger(LargeInteger largeint)
    {
        initialize(duplicateArray(largeint.data), largeint.getSign());
    }

    /**
     * Initializes this object with the provided byte array.
     *
     * @param data The <code>int[]</code> which contains the number to store in this {@link LargeInteger}.  This array
     *             is treated as signed data.
     */
    void initialize(int[] data)
    {
        boolean sign;
        if ((data[0] & HIGHEST_INT_BIT_MASK) == 0)
        {
            sign = true;
        } else
        {
            sign = false;
            data = twosComplement(data);
        }
        initialize(data, sign);
    }

    /**
     * Initializes this object with the provided byte array.
     *
     * @param data The <code>int[]</code> which contains the number to store in this {@link LargeInteger}.  This array
     *             is treated as unsigned data.
     * @param sign The sign of this number.
     */
    void initialize(int[] data, boolean sign)
    {
        // Trim any unnecessary bits.
        data = trimArray(data);
        if ((data.length == 1) && (data[0] == 0)) sign = true;

        this.data = data;
        this.sign = sign;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    // *** Analysis Methods ***

    /**
     * Retrieves the sign of this {@link LargeInteger}.
     *
     * @return <code>true</code> if this {@link LargeInteger} is positive; <code>false</code> if it is negative.
     *         <code>0</code> is considered positive for purposes of sign.
     */
    public boolean getSign()
    {
        return sign;
    }

    /**
     * Retrieves the "sign number" for this {@link LargeInteger}.  The sign number is <code>-1</code> for negative
     * numbers, <code>1</code> for positive numbers, and <code>0</code> for <code>0</code>.
     */
    public int getSignNumber()
    {
        if (sign)
        {
            if ((data.length == 1) && (data[0] == 0))
            {
                return 0;
            } else
            {
                return 1;
            }
        } else
        {
            return -1;
        }
    }

    /**
     * Determines whether or not the provided bit is set.
     *
     * @param index The index of the bit to check.  The last bit is assumed to have an index of <code>0</code>.
     * @return <code>true</code> if that bit is <code>1</code>; <code>false</code> if it is <code>0</code>.
     */
    public boolean getBit(int index)
    {
        return checkBit(data, index);
    }

    /**
     * Retrieves this {@link LargeInteger} as an <code>int</code>.  If this {@link LargeInteger} is currently storing a
     * value that does not fit in an <code>int</code> (less than <code>-2147483648</code> or greater than
     * <code>2147483647</code>), this {@link LargeInteger} is downcasted to an <code>int</code> in the same manner as
     * Java downcasts primitives to smaller primitives: by ignoring the extra upper bits and treating the result as
     * signed.
     *
     * @return This {@link LargeInteger} as an <code>int</code>.
     */
    public int getIntValue()
    {
        if (data[0] == HIGHEST_INT_BIT_MASK) return data[0]; // allow for Integer.MIN_VALUE
        return getSignNumber() * (data[0] & ALL_BUT_HIGHEST_INT_BIT_MASK);
    }

    /**
     * Retrieves this {@link LargeInteger} as a <code>long</code>.  If this {@link LargeInteger} is currently storing a
     * value that does not fit in a <code>long</code> (less than <code>-9223372036854775808</code> or greater than
     * <code>9223372036854775807</code>), this {@link LargeInteger} is downcasted to a <code>long</code> in the same
     * manner as Java downcasts primitives to smaller primitives: by ignoring the extra upper bits and treating the
     * result as signed.
     *
     * @return This {@link LargeInteger} as a <code>long</code>.
     */
    public long getLongValue()
    {
        if (data.length == 1)
        {
            return data[0] & ALL_LONG_BIT_MASK * getSignNumber();
        }
        if (((data[0] & ALL_BUT_HIGHEST_INT_BIT_MASK) != 0 && (data[1] == 0)) && (data.length == 2))
        {
            // This represents Long.MIN_VALUE.  It is ok to report in any case, since -Long.MIN_VALUE overflows to
            // Long.MIN_VALUE
            return Long.MIN_VALUE;
        }
        return (((data[data.length - 2] & FIRST_31_BITS_LONG_BIT_MASK) << 32) |
                (data[data.length - 1] & FIRST_32_BITS_LONG_BIT_MASK)) * getSignNumber();
    }

    /**
     * Retrieves a {@link String} representing this {@link LargeInteger} using the provided radix.
     *
     * @param radix The radix to use when constructing the string.
     */
    public String toString(int radix)
    {
        if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX))
        {
            throw new IllegalArgumentException(
                    "Radix must be between " + Character.MIN_RADIX + " and " + Character.MAX_RADIX + ": was " + radix);
        }
        if (this.equalTo(ZERO)) return new String(new char[]{Character.forDigit(0, radix)});
        StringBuffer sb = new StringBuffer();
        boolean negative = !getSign();
        LargeInteger liRadix = new LargeInteger(radix);
        LargeInteger li = getAbsoluteValue();
        while (li.greaterThan(ZERO))
        {
            Pair<LargeInteger, LargeInteger> ret = li.dividedByWithRemainder(liRadix);
            sb.insert(0, Character.forDigit(ret.getSecond().getIntValue(), radix));
            li = ret.getFirst();
        }
        if (negative) sb.insert(0, '-');
        return sb.toString();
    }

    /**
     * Returns a string representation of the object: the base ten representation of this number.
     *
     * @return A string representation of the object.
     */
    public String toString()
    {
        return toString(10);
    }

    // *** Manipulation Methods : Non-Destructive ***

    /**
     * <i>Non-Destructive:</i> Inverts the sign on this {@link LargeInteger} <i>if</i> this {@link LargeInteger} is
     * negative, returning the result as a new {@link LargeInteger}.
     *
     * @return The absolute value of this {@link LargeInteger}.
     */
    public LargeInteger getAbsoluteValue()
    {
        return new LargeInteger(duplicateArray(data), true);
    }

    /**
     * <i>Non-Destructive:</i> Inverts the sign on this {@link LargeInteger}.  The result is returned as another {@link
     * LargeInteger}, returning the result as a new {@link LargeInteger}.
     *
     * @return The {@link LargeInteger} which is the negative of this {@link LargeInteger}.
     */
    public LargeInteger negated()
    {
        return new LargeInteger(duplicateArray(data), !sign);
    }

    /**
     * <i>Non-Destructive:</i> Adds two {@link LargeInteger}s together, returning the result as a third {@link
     * LargeInteger}.
     *
     * @param other The number to add to this number.
     * @return The resulting number.
     */
    public LargeInteger plus(LargeInteger other)
    {
        return sum(other, false);
    }

    /**
     * <i>Non-Destructive:</i> Adds two {@link LargeInteger}s together, returning the result as a third {@link
     * LargeInteger}.  This implementation is designed to allow the caller to indicate subtraction as well.
     * <p/>
     * This method is used to prevent the {@link LargeInteger#minus(LargeInteger)} method from having to create an
     * instance of the provided variable with an inverted sign.  The {@link MutableLargeInteger#subtract(LargeInteger)}
     * method is not similarly afflicted because the called object is capable of destructively performing operations on
     * itself.
     *
     * @param other           The number to add to this number.
     * @param invertOtherSign Effectively inverts <code>other</code>'s sign during the addition process, thus
     *                        subtracting the value.
     * @return The resulting number.
     */
    LargeInteger sum(LargeInteger other, boolean invertOtherSign)
    {
        boolean otherSign = other.getSign() ^ invertOtherSign;
        if (getSign() == otherSign)
        {
            int[] sum = add(data, other.data, false, false);
            return new LargeInteger(sum, getSign());
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
                sign = otherSign;
                a = other.data;
                b = data;
            }
            a = add(a, twosComplement(b), true, false);
            return new LargeInteger(a, sign);
        }
    }

    /**
     * <i>Non-Destructive:</i> Subtracts the provided number from this number, returning the result as another {@link
     * LargeInteger}.
     *
     * @param other The number to subtract from this number.
     * @return The resulting number.
     */
    public LargeInteger minus(LargeInteger other)
    {
        return this.sum(other, true);
    }

    /**
     * <i>Non-Destructive:</i> Multiplies the provided number by this number, returning the result as another {@link
     * LargeInteger}.
     *
     * @param other The number to multiply with this number.
     * @return The resulting number.
     */
    public LargeInteger times(LargeInteger other)
    {
        return new LargeInteger(multiply(data, other.data), getSign() == other.getSign());
    }

    /**
     * <i>Non-Destructive:</i> Retrieves this {@link LargeInteger} divided by the provided {@link LargeInteger}, return
     * the result as another {@link LargeInteger}.
     *
     * @param other The number by which to divide this number.
     * @return The resulting number.
     */
    public LargeInteger dividedBy(LargeInteger other)
    {
        return new LargeInteger(divide(duplicateArray(data), other.data), getSign() == other.getSign());
    }

    /**
     * <i>Non-Destructive:</i> Retrieves both the quotient and the remainder which are produced when this {@link
     * LargeInteger} is divided by the provided {@link LargeInteger}.
     *
     * @param other The number by which to divide this number.
     * @return A {@link Pair}<code>&lt;{@link LargeInteger},{@link LargeInteger}&gt;</code>.  The first number is the
     *         quotient from the division.  The second number is the remainder from that division.
     */
    public Pair<LargeInteger, LargeInteger> dividedByWithRemainder(LargeInteger other)
    {
        Pair<int[], int[]> ret = divideWithRemainder(duplicateArray(data), other.data);
        return new Pair<LargeInteger, LargeInteger>(
                new LargeInteger(ret.getFirst(), getSign() == other.getSign()),
                new LargeInteger(ret.getSecond(), getSign()));
    }

    /**
     * <i>Non-Destructive:</i> Retrieves this {@link LargeInteger} modulused by the provided <code>int</code>, returning
     * the result as another {@link LargeInteger}.
     *
     * @param other The number to modulus with this number.
     * @return The resulting number.
     */
    public LargeInteger modulusedBy(int other)
    {
        LargeInteger liOther = new LargeInteger(other);
        return modulusedBy(liOther);
    }

    /**
     * <i>Non-Destructive:</i> Retrieves this {@link LargeInteger} modulused by the provided <code>long</code>,
     * returning the result as another {@link LargeInteger}.
     *
     * @param other The number to modulus with this number.
     * @return The resulting number.
     */
    public LargeInteger modulusedBy(long other)
    {
        LargeInteger liOther = new LargeInteger(other);
        return modulusedBy(liOther);
    }

    /**
     * <i>Non-Destructive:</i> Retrieves this {@link LargeInteger} modulused by the provided {@link LargeInteger},
     * returning the result as another {@link LargeInteger}.
     *
     * @param other The number to modulus with this number.
     * @return The resulting number.
     */
    public LargeInteger modulusedBy(LargeInteger other)
    {
        // the sign of the modulus base is insignificant
        if (other.equalTo(ZERO)) throw new ArithmeticException("n%0 is undefined");
        return new LargeInteger(modulo(duplicateArray(data), other.data), getSign());
    }

    /**
     * <i>Non-Destructive:</i> Raises this number to the specified power and returns the result as a third {@link
     * LargeInteger}.
     *
     * @param other The number to which to raise this number.
     * @return The resulting number.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public LargeInteger raisedTo(int other)
    {
        if (other == 0)
        {
            if ((data.length == 0) && (data[0] == 0)) throw new ArithmeticException("0^0 is undefined");
            return ONE;
        } else
        {
            int[] dataOriginal = duplicateArray(data);
            int[] dataWorking = duplicateArray(data);
            for (int i = 1; i < other; i++)
            {
                dataWorking = multiply(dataWorking, dataOriginal);
            }
            return new LargeInteger(dataWorking, getSign() || (other % 2 == 0));
        }
    }

    /**
     * <i>Non-Destructive:</i> Raises this number to the specified power and returns the result as a third {@link
     * LargeInteger}.
     *
     * @param other The number to which to raise this number.
     *              <p/>
     *              <i>Note:</i> Raising any {@link LargeInteger} other than <code>-1</code>, <code>0</code>, or
     *              <code>1</code> to a power <code>n</code> will require at least <code>n+1</code> bits of memory to
     *              store.  Therefore, raising a {@link LargeInteger} to a power greater than <code>17179869184</code>
     *              will consume 2Gb of memory.  Presently (J2SDK v1.5.0), JVMs cannot address more than 2Gb of RAM;
     *              therefore, raising a {@link LargeInteger} to this power will cause a crash.
     * @return The resulting number.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public LargeInteger raisedTo(long other)
    {
        if (other == 0)
        {
            if ((data.length == 0) && (data[0] == 0)) throw new ArithmeticException("0^0 is undefined");
            return ONE;
        } else
        {
            int[] dataOriginal = duplicateArray(data);
            int[] dataWorking = duplicateArray(data);
            for (int i = 1; i < other; i++)
            {
                dataWorking = multiply(dataWorking, dataOriginal);
            }
            return new LargeInteger(dataWorking, getSign() || (other % 2 == 0));
        }
    }

    /**
     * <i>Non-Destructive:</i> Raises this number to the specified power and returns the result as a third {@link
     * LargeInteger}.
     *
     * @param other The number to which to raise this number.
     *              <p/>
     *              <i>Note:</i> Raising any {@link LargeInteger} other than <code>-1</code>, <code>0</code>, or
     *              <code>1</code> to a power <code>n</code> will require at least <code>n+1</code> bits of memory to
     *              store.  Therefore, raising a {@link LargeInteger} to a power greater than <code>17179869184</code>
     *              will consume 2Gb of memory.  Presently (J2SDK v1.5.0), JVMs cannot address more than 2Gb of RAM;
     *              therefore, raising a {@link LargeInteger} to this power will cause a crash.
     * @return The resulting number.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public LargeInteger raisedTo(LargeInteger other)
    {
        if (other.lessThanOrEqualTo(LargeInteger.LONG_MAX_VALUE)) return raisedTo(other.getLongValue());
        if (other.equalTo(ZERO))
        {
            if ((data.length == 0) && (data[0] == 0)) throw new ArithmeticException("0^0 is undefined");
            return ONE;
        } else
        {
            int[] dataOriginal = duplicateArray(data);
            int[] dataWorking = duplicateArray(data);
            for (MutableLargeInteger i = ONE.copy(); i.lessThan(other); i.add(ONE))
            {
                dataWorking = multiply(dataWorking, dataOriginal);
            }
            return new LargeInteger(dataWorking, getSign() || (other.modulusedBy(2).equalTo(ZERO)));
        }
    }

    /**
     * <i>Non-Destructive:</i> Shifts this number left by the specified number of bits, returning the result as another
     * {@link LargeInteger}.  The sign of the {@link LargeInteger} is not affected by this operation.
     *
     * @param shift The number of bits to shift left.
     * @return The resulting {@link BigInteger}.
     */
    public LargeInteger leftShifted(int shift)
    {
        return new LargeInteger(arrayShiftLeft(data, shift), getSign());
    }

    /**
     * <i>Non-Destructive:</i> Shifts this number left by the specified number of bits, returning the result as another
     * {@link LargeInteger}.  The sign of the {@link LargeInteger} is not affected by this operation.
     *
     * @param shift The number of bits to shift left.
     * @return The resulting {@link BigInteger}.
     */
    public LargeInteger rightShifted(int shift)
    {
        return new LargeInteger(arrayShiftRight(data, shift), getSign());
    }

    // *** Comparison Methods ***

    /**
     * Determines if this {@link LargeInteger} is greater than the provided <code>int</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean greaterThan(int other)
    {
        return (compareTo(other) > 0);
    }

    /**
     * Determines if this {@link LargeInteger} is greater than or equal to the provided <code>int</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean greaterThanOrEqualTo(int other)
    {
        return (compareTo(other) >= 0);
    }

    /**
     * Determines if this {@link LargeInteger} is less than the provided <code>int</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean lessThan(int other)
    {
        return (compareTo(other) < 0);
    }

    /**
     * Determines if this {@link LargeInteger} is less than or equal to the provided <code>int</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean lessThanOrEqualTo(int other)
    {
        return (compareTo(other) <= 0);
    }

    /**
     * Determines if this {@link LargeInteger} is equal to the provided <code>int</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean equalTo(int other)
    {
        return (compareTo(other) == 0);
    }

    /**
     * Determines if this {@link LargeInteger} is not equal to the provided <code>int</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean notEqualTo(int other)
    {
        return (compareTo(other) != 0);
    }

    /**
     * Determines if this {@link LargeInteger} is greater than the provided <code>long</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean greaterThan(long other)
    {
        return (compareTo(other) > 0);
    }

    /**
     * Determines if this {@link LargeInteger} is greater than or equal to the provided <code>long</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean greaterThanOrEqualTo(long other)
    {
        return (compareTo(other) >= 0);
    }

    /**
     * Determines if this {@link LargeInteger} is less than the provided <code>long</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean lessThan(long other)
    {
        return (compareTo(other) < 0);
    }

    /**
     * Determines if this {@link LargeInteger} is less than or equal to the provided <code>long</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean lessThanOrEqualTo(long other)
    {
        return (compareTo(other) <= 0);
    }

    /**
     * Determines if this {@link LargeInteger} is equal to the provided <code>long</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean equalTo(long other)
    {
        return (compareTo(other) == 0);
    }

    /**
     * Determines if this {@link LargeInteger} is not equal to the provided <code>long</code>.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean notEqualTo(long other)
    {
        return (compareTo(other) != 0);
    }

    /**
     * Determines if this {@link LargeInteger} is greater than another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean greaterThan(LargeInteger other)
    {
        return (compareTo(other) > 0);
    }

    /**
     * Determines if this {@link LargeInteger} is greater than or equal to another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean greaterThanOrEqualTo(LargeInteger other)
    {
        return (compareTo(other) >= 0);
    }

    /**
     * Determines if this {@link LargeInteger} is less than another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean lessThan(LargeInteger other)
    {
        return (compareTo(other) < 0);
    }

    /**
     * Determines if this {@link LargeInteger} is less than or equal to another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean lessThanOrEqualTo(LargeInteger other)
    {
        return (compareTo(other) <= 0);
    }

    /**
     * Determines if this {@link LargeInteger} is equal to another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean equalTo(LargeInteger other)
    {
        return (compareTo(other) == 0);
    }

    /**
     * Determines if this {@link LargeInteger} is not equal to another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return The result of the comparison.
     */
    public boolean notEqualTo(LargeInteger other)
    {
        return (compareTo(other) != 0);
    }

    /**
     * Compares this {@link LargeInteger} to another {@link LargeInteger}.
     *
     * @param other The other <code>int</code> to compare to this one.
     * @return <code>0</code> if the numbers are equal, <code>-1</code> if this {@link LargeInteger} is less than the
     *         provided number, or <code>1</code> if this {@link LargeInteger} is greater than the provided number.
     */
    public int compareTo(int other)
    {
        if (other == Integer.MIN_VALUE)
        {
            return compareTo(INT_MIN_VALUE);
        } else
        {
            boolean otherSign = (other >= 0);
            if (other < 0) other = -other;
            if (otherSign)
            {
                if (getSign())
                {
                    if (data.length > 1) return 1;
                    if ((data[0] & HIGHEST_INT_BIT_MASK) == 0)
                    {
                        if (data[0] > other)
                        {
                            return 1;
                        } else if (data[0] < other)
                        {
                            return -1;
                        } else
                        {
                            return 0;
                        }
                    } else
                    {
                        return 1;
                    }
                } else
                {
                    return -1;
                }
            } else
            {
                if (getSign())
                {
                    return 1;
                } else
                {
                    if (data.length > 1) return -1;
                    if ((data[0] & HIGHEST_INT_BIT_MASK) == 0)
                    {
                        if (data[0] > other)
                        {
                            return -1;
                        } else if (data[0] < other)
                        {
                            return 1;
                        } else
                        {
                            return 0;
                        }
                    } else
                    {
                        return -1;
                    }
                }
            }
        }
    }

    /**
     * Compares this {@link LargeInteger} to another {@link LargeInteger}.
     *
     * @param other The other <code>long</code> to compare to this one.
     * @return <code>0</code> if the numbers are equal, <code>-1</code> if this {@link LargeInteger} is less than the
     *         provided number, or <code>1</code> if this {@link LargeInteger} is greater than the provided number.
     */
    public int compareTo(long other)
    {
        // TODO: implement a more efficient comparison
        return this.compareTo(new LargeInteger(other));
    }

    /**
     * Compares this {@link LargeInteger} to another {@link LargeInteger}.
     *
     * @param other The other {@link LargeInteger} to compare to this one.
     * @return <code>0</code> if the two {@link LargeInteger}s are equal, <code>-1</code> if this {@link LargeInteger}
     *         is less than the other one, or <code>1</code> if this {@link LargeInteger} is greater than the other
     *         one.
     */
    public int compareTo(LargeInteger other)
    {
        return compareTo(other, false);
    }

    /**
     * Compares this {@link LargeInteger} to another {@link LargeInteger}.
     *
     * @param other      The other {@link LargeInteger} to compare to this one.
     * @param ignoreSign Whether or not the numbers are to be compared as absolute values.
     * @return <code>0</code> if the two {@link LargeInteger}s are equal, <code>-1</code> if this {@link LargeInteger}
     *         is less than the other one, or <code>1</code> if this {@link LargeInteger} is greater than the other
     *         one.
     */
    int compareTo(LargeInteger other, boolean ignoreSign)
    {
        if (this.getSign() || (ignoreSign))
        {
            if ((other.getSign()) || (ignoreSign))
            {
                if (this.data.length > other.data.length)
                {
                    return 1;
                } else if (this.data.length < other.data.length)
                {
                    return -1;
                } else
                {
                    for (int i = 0; i < data.length; i++)
                    {
                        int compA = data[i] ^ HIGHEST_INT_BIT_MASK;
                        int compB = other.data[i] ^ HIGHEST_INT_BIT_MASK;
                        if (compA > compB)
                        {
                            return 1;
                        } else if (compB > compA)
                        {
                            return -1;
                        }
                    }
                    return 0;
                }
            } else
            {
                return 1;
            }
        } else
        {
            if (other.getSign())
            {
                return -1;
            } else
            {
                if (this.data.length > other.data.length)
                {
                    return -1;
                } else if (this.data.length < other.data.length)
                {
                    return 1;
                } else
                {
                    for (int i = 0; i < data.length; i++)
                    {
                        int compA = data[i] ^ HIGHEST_INT_BIT_MASK;
                        int compB = other.data[i] ^ HIGHEST_INT_BIT_MASK;
                        if (compA > compB)
                        {
                            return -1;
                        } else if (compB > compA)
                        {
                            return 1;
                        }
                    }
                    return 0;
                }
            }
        }
    }

    // *** Other Methods ***

    /**
     * Creates a mutable, deep copy of this {@link LargeInteger}.
     *
     * @return A mutable, deep copy of this {@link LargeInteger}.
     */
    public MutableLargeInteger copy()
    {
        return new MutableLargeInteger(duplicateArray(data), getSign());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves a {@link LargeInteger} which represents the factorial of the provided number.
     *
     * @param factor The value whose factorial is to be determined.
     * @return The factorial of that number.  For <code>0</code>, <code>1</code> is returned.
     * @throws ArithmeticException If a negative factor is supplied.
     */
    public static LargeInteger factorial(int factor)
    {
        MutableLargeInteger ret = ONE.copy();
        for (int i = 2; i <= factor; i++)
        {
            ret.multiply(new LargeInteger(i));
        }
        return ret;
    }

// INTERNAL STATIC METHODS ///////////////////////////////////////////////////////

    /**
     * "Trims" the provided array.  All values starting at the beginning of the array which equal <code>0</code> are
     * removed.  If no such values exist, the provided array is returned.
     *
     * @param array The array to trim.
     * @return The trimmed array.
     */
    static int[] trimArray(int[] array)
    {
        if ((array.length == 1) && (array[0] == 0)) return array;
        int index = 0;
        while ((index < array.length) && (array[index] == 0)) index++;
        if (index > 0)
        {
            if (index >= array.length)
            {
                return new int[]{0};
            } else
            {
                int[] newarray = new int[array.length - index];
                System.arraycopy(array, index, newarray, 0, newarray.length);
                array = newarray;
            }
        }
        return array;
    }

    /**
     * Translates the provided array into its two's-complement.
     *
     * @param array The array to transform.  This array is destroyed.
     * @return The resulting transformation.
     */
    static int[] twosComplement(int[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            array[i] = ~array[i];
        }
        return add(array, ARRAY_VALUE_ONE, true, true);
    }

    /**
     * Determines whether or not the provided bit is set.
     *
     * @param array The array for which to check the bit.
     * @param index The index of the bit to check.  The last bit is assumed to have an index of <code>0</code>.
     * @return <code>true</code> if that bit is <code>1</code>; <code>false</code> if it is <code>0</code>.
     */
    public static boolean checkBit(int[] array, int index)
    {
        if (array.length * 32 < index) return false;
        return ((array[(array.length * 32 - index - 1) / 32] & SINGLE_BIT_MASK[index % 32]) != 0);
    }

    /**
     * Shifts the provided array left by the specified number of bits.
     *
     * @param array The array to shift.
     * @param shift The number of bits to shift.
     * @return The resulting array.
     */
    static int[] arrayShiftLeft(int[] array, int shift)
    {
        if (shift < 0) return arrayShiftRight(array, -shift);
        int sigbits = MathUtilities.countSignificantBits(array[0]);
        int[] newArray = new int[(shift + sigbits - 1) / 32 + array.length];
        int bitsLeft = shift % 32;

        int addedIndex;
        if (bitsLeft > (32 - sigbits))
        {
            addedIndex = 1;
            newArray[0] = array[0] >>> (32 - bitsLeft);
        } else
        {
            addedIndex = 0;
        }

        if (bitsLeft == 0)
        {
            System.arraycopy(array, 0, newArray, addedIndex, array.length);
        } else
        {
            for (int i = 0; i < array.length; i++)
            {
                if (i + 1 < array.length)
                {
                    newArray[i + addedIndex] = (array[i] << bitsLeft) | (array[i + 1] >>> (32 - bitsLeft));
                } else
                {
                    newArray[i + addedIndex] = (array[i] << bitsLeft);
                }
            }
        }

        return newArray;
    }

    /**
     * Shifts the provided array right by the specified number of bits.
     *
     * @param array The array to shift.
     * @param shift The number of bits to shift.
     * @return The resulting array.  This array will always be at least one <code>int</code> in size, even if all of the
     *         significant bits are shifted out.
     */
    static int[] arrayShiftRight(int[] array, int shift)
    {
        if (shift < 0) return arrayShiftLeft(array, -shift);
        int sigbits = MathUtilities.countSignificantBits(array[0]);
        if (shift >= sigbits + array.length * 32 - 32) return new int[1];

        int[] newArray = new int[array.length + (sigbits - shift - 32) / 32];
        int bitsRight = shift % 32;

        int addedIndex = 1;
        int index = 0;
        if (bitsRight < sigbits)
        {
            newArray[index++] = array[0] >>> bitsRight;
            addedIndex = 0;
        }

        if (bitsRight == 0)
        {
            for (; index < newArray.length; index++)
            {
                newArray[index] = (array[index + addedIndex] >>> bitsRight);
            }
        } else
        {
            for (; index < newArray.length; index++)
            {
                newArray[index] = (array[index - 1 + addedIndex] << (32 - bitsRight)) |
                                  (array[index + addedIndex] >>> bitsRight);
            }
        }

        return newArray;
    }

    /**
     * Retrieves the number of significant bits in the provided array.
     *
     * @param array The array to check.  This array must not contain leading zeroes.
     * @return The number of significant bits in the provided array.
     * @see MathUtilities#countSignificantBits(int)
     */
    static int countSignificantBits(int[] array)
    {
        return MathUtilities.countSignificantBits(array[0]) + 32 * (array.length - 1);
    }

    /**
     * Performs an addition of two bytes arrays, returning the resulting byte array.  These arrays are assumed to be in
     * unsigned, big-endian format.
     *
     * @param a               The first array to add.
     * @param b               The second array to add.
     * @param damagePermitted <code>true</code> if this method is permitted to use <code>a</code> as a vessel for
     *                        providing the result; <code>false</code> if <code>a</code> must be left in tact.  The
     *                        array <code>b</code> is always left in tact.
     */
    static int[] add(int[] a, int[] b, boolean damagePermitted)
    {
        return add(a, b, false, damagePermitted);
    }

    /**
     * Performs an addition of two byte arrays, returning the resulting byte array.  These arrays are assumed to be in
     * unsigned, big-endian format.
     *
     * @param a               The first array to add.
     * @param b               The second array to add.
     * @param ignoreCarry     Determines whether or not carry bits are ignored.  <code>true</code> indicates that
     *                        carried bits have no effect on the return value (used for subtraction). <code>false</code>
     *                        indicates that carried bits do have an effect by increasing the array size (used for
     *                        addition).
     * @param damagePermitted <code>true</code> if this method is permitted to use <code>a</code> as a vessel for
     *                        providing the result; <code>false</code> if <code>a</code> must be left in tact.  The
     *                        array <code>b</code> is always left in tact.
     */
    static int[] add(int[] a, int[] b, boolean ignoreCarry, boolean damagePermitted)
    {
        // Ensure that a is the larger array
        int[] temp;
        boolean copy = !damagePermitted;
        if (a.length < b.length)
        {
            temp = a;
            a = b;
            b = temp;
            copy = true;
        }

        if (copy)
        {
            // Make a copy of a to prevent damaging the original array
            temp = new int[a.length];
            System.arraycopy(a, 0, temp, 0, a.length);
            a = temp;
        }

        // Perform add
        boolean carry = false;
        for (int i = 1; i <= a.length; i++)
        {
            if (carry)
            {
                Pair<Boolean, Integer> pbi = add(a[a.length - i], 1);
                carry = pbi.getFirst();
                a[a.length - i] = pbi.getSecond();
            }

            if (b.length < i)
            {
                if (!carry) break;
            } else
            {
                Pair<Boolean, Integer> pbi = add(a[a.length - i], b[b.length - i]);
                carry |= (pbi.getFirst());
                a[a.length - i] = pbi.getSecond();
            }
        }

        // Consider increasing the array size
        if ((carry) && (!ignoreCarry))
        {
            temp = new int[a.length + 1];
            System.arraycopy(a, 0, temp, 1, a.length);
            temp[0] = 0x00000001;
            a = temp;
        }

        return a;
    }

    /**
     * Adds two integers together.  This method treats the integers as unsigned values.  The resulting 32-bit unsigned
     * value is returned, along with a <code>boolean</code> indicating whether or not a 33rd bit was overflowed.
     *
     * @param a The first <code>int</code>.
     * @param b The second <code>int</code>.
     * @return A {@link Pair}<code>&lt;boolean,int&gt;</code> containing the specified sum.
     */
    static Pair<Boolean, Integer> add(int a, int b)
    {
        return add(a, b, false);
    }

    /**
     * Adds two integers together.  This method treats the integers as unsigned values.  The resulting 32-bit unsigned
     * value is returned, along with a <code>boolean</code> indicating whether or not a 33rd bit was overflowed.
     *
     * @param a The first <code>int</code>.
     * @param b The second <code>int</code>.
     * @param o Whether or not the overflow bit is set.  This value is used internally.
     * @return A {@link Pair}<code>&lt;boolean,int&gt;</code> containing the specified sum.
     */
    static Pair<Boolean, Integer> add(int a, int b, boolean o)
    {
        while (true)
        {
            if (a == 0) return new Pair<Boolean, Integer>(o, b);
            if (b == 0) return new Pair<Boolean, Integer>(o, a);
            int xor = a ^ b;
            int and = a & b;
            boolean overflow = (and & HIGHEST_INT_BIT_MASK) != 0;
            and <<= 1;
            a = and;
            b = xor;
            o = overflow || o;
        }
    }

    /**
     * Performs a multiplication of the two byte-arrays.  The contents are assumed to be unsigned and in big-endian
     * format.  This method always returns a new array.
     *
     * @param a The first array to multiply.  This array must not have leading zeroes.
     * @param b The second array to multiply.  This array must not have leading zeroes.
     */
    static int[] multiply(int[] a, int[] b)
    {
        // Ensure that the first array is the largest
        if (b.length > a.length)
        {
            int[] temp = a;
            a = b;
            b = temp;
        }

        // SPECIAL CASES

        // Special case: x * 1
        if ((a.length == 1) && (a[0] == 1)) return duplicateArray(b);
        if ((b.length == 1) && (b[0] == 1)) return duplicateArray(a);

        // Spacial case: x * 0
        if ((a.length == 1) && (a[0] == 0)) return duplicateArray(ARRAY_VALUE_ZERO);
        if ((b.length == 1) && (b[0] == 0)) return duplicateArray(ARRAY_VALUE_ZERO);

        // Special case: x * power of 2
        int sigbits = 2;
        int index = 0;
        while ((sigbits > 0) && (index < b.length))
        {
            int var = b[index++];
            if (var != 0) // we've found a value with at least one sig bit
            {
                sigbits--;
                // see if there is another sigbit in the value
                while (var % 2 != 1) var >>>= 1;
                var >>>= 1;
                if (var != 0) sigbits--;
            }
        }
        if (sigbits > 0)
        {
            return arrayShiftLeft(a, MathUtilities.countSignificantBits(b[0]) + 32 * (b.length - 1) - 1);
        }
        sigbits = 2;
        index = 0;
        while ((sigbits > 0) && (index < a.length))
        {
            int var = a[index++];
            if (var != 0) // we've found a value with at least one sig bit
            {
                sigbits--;
                // see if there is another sigbit in the value
                while (var % 2 != 1) var >>>= 1;
                var >>>= 1;
                if (var != 0) sigbits--;
            }
        }
        if (sigbits > 0)
        {
            return arrayShiftLeft(b, MathUtilities.countSignificantBits(a[0]) + 32 * (a.length - 1) - 1);
        }

        // END SPECIAL CASES

        // The size in bits of the array necessary to represent a non-negative value X is max(log_2(X)+1,1)
        // The value being represented is a*b.  The space necessary to represent X is
        //         log_2(a*b) = log_2(a)+log_2(b) = length(a) + length(b)
        // The size of the array in a LargeInteger representing X is ceil(max(log_2(X),1)/32)*32 (natural division)

        int sigBitsInB = countSignificantBits(b);
        int[] ret = new int[(countSignificantBits(a) + sigBitsInB + 33) / 32];
        if (checkBit(b, 0)) ret = add(ret, a, false, true);
        int bitIndex = 1;

        while (bitIndex < sigBitsInB)
        {
            a = arrayShiftLeft(a, 1);
            if (checkBit(b, bitIndex)) ret = add(ret, a, true);
            bitIndex++;
        }
        return trimArray(ret);
    }

    /**
     * Compares the two arrays as unsigned big-endian values.
     *
     * @param a The first array to compare.  This array must not have leading zeroes.
     * @param b The second array to compare.  This array must not have leading zeroes.
     * @return <code>-1</code>, <code>0</code>, or <code>1</code> as the first array is less than, equal to, or greater
     *         than the second.
     */
    static int compare(int[] a, int[] b)
    {
        if (a.length > b.length) return 1;
        if (b.length > a.length) return -1;
        for (int i = 0; i < a.length; i++)
        {
            int ai = a[i];
            int bi = b[i];
            if (ai < 0)
            {
                // Negative value indicates bit 31 is set
                if (bi < 0)
                {
                    if (ai > bi) return 1;
                    if (bi > ai) return -1;
                } else
                {
                    return 1;
                }
            } else
            {
                if (bi < 0)
                {
                    return -1;
                } else
                {
                    if (ai > bi) return 1;
                    if (bi > ai) return -1;
                }
            }
        }
        return 0;
    }

    /**
     * "Extends" the size of the provided array by padding its left side with a specified value.  If the array is
     * already large enough, it is returned unmodified.
     *
     * @param array The array to extend.
     * @param size  The size of the resulting array.
     * @param value The value with which to extend the array.
     * @return The extended array.
     */
    static final int[] extendArray(int[] array, int size, int value)
    {
        if (size <= array.length) return array;
        int[] ret = new int[size];
        System.arraycopy(array, 0, ret, size - array.length, array.length);
        Arrays.fill(ret, 0, size - array.length, value);
        return ret;
    }

    /**
     * Retrieves the right-most bits of the provided array.  Theoretically, this would have the same effect as
     * <code>array & 2<sup>bits</sup>-1</code> (treating <code>array</code> as an arbitrarily-sized, unsigned big-endian
     * value).
     *
     * @param array The array to mask.
     * @param bits  The number of bits, starting with the rightmost, to allow.
     * @return The resulting array.  Leading zeroes will automatically be trimmed from the right.  If <code>bits</code>
     *         is zero, an array containing a single <code>int</code> <code>0</code> will be returned.
     */
    static final int[] getOnlyRightmostBits(int[] array, int bits)
    {
        if (bits == 0) return new int[]{0};
        int[] ret = new int[(bits + 31) / 32];
        int mask = ALL_INT_BIT_MASK >>> (31 - (bits - 1) % 32);
        ret[0] = array[array.length - ret.length] & mask;
        System.arraycopy(array, array.length - ret.length + 1, ret, 1, ret.length - 1);
        return ret;
    }

    /**
     * Divides the first array by the second, producing the quotient as a new array.  This is a simple shift-and-divide
     * algorithm and is probably not very efficient.
     *
     * @param a The dividend array.  This array must not have leading zeroes.  The contents of this array will be
     *          destroyed.
     * @param b The divisor array.  This array must not have leading zeroes.
     * @return A {@link Pair}<code>&lt;int[],int[]&gt;</code>.  The first <code>int[]</code> contains the quotient of
     *         the division.  The second <code>int[]</code> contains the remainder of the division.
     */
    static Pair<int[], int[]> divideWithRemainder(int[] a, int[] b)
    {
        // See the method multiply(int[],int[]) for commentary on the size of an array necessary to store a value
        int[] ret = new int[(countSignificantBits(a) - countSignificantBits(b) + 31) / 32 + 1];

        // Special case: divide by zero
        if ((b.length == 1) && (b[0] == 0)) throw new ArithmeticException("Divide by zero");

        // Special case: divide by one
        if ((b.length == 1) && (b[0] == 1))
        {
            return new Pair<int[], int[]>(duplicateArray(a), duplicateArray(ARRAY_VALUE_ZERO));
        }

        // Special case: divide by power of two
        int sigbits = 2;
        int index = 0;
        while ((sigbits > 0) && (index < b.length))
        {
            int var = b[index++];
            if (var != 0) // we've found a value with at least one sig bit
            {
                sigbits--;
                // see if there is another sigbit in the value
                while (var % 2 != 1) var >>>= 1;
                var >>>= 1;
                if (var != 0) sigbits--;
            }
        }
        if (sigbits > 0)
        {
            return new Pair<int[], int[]>(
                    arrayShiftRight(a, countSignificantBits(b) - 1),
                    getOnlyRightmostBits(a, countSignificantBits(b) - 1));
        }

        // Special case: divisor greater than divided
        if (compare(a, b) == -1)
        {
            return new Pair<int[], int[]>(duplicateArray(ARRAY_VALUE_ZERO), a);
        }

        int retIndex = 0;
        int retMask = 0x80000000;

        // Align divisor
        int divisorShiftBits = countSignificantBits(a) - countSignificantBits(b);
        b = arrayShiftLeft(b, divisorShiftBits);
        if (compare(a, b) >= 0)
        {
            a =
                    trimArray(
                            add(
                                    a, extendArray(twosComplement(duplicateArray(b)), a.length, ALL_INT_BIT_MASK), true,
                                    true));
            ret[retIndex] |= retMask;
        }
        for (int i = 0; i < divisorShiftBits; i++)
        {
            retMask >>>= 1;
            if (retMask == 0)
            {
                retIndex++;
                retMask = 0x80000000;
            }
            b = arrayShiftRight(b, 1);
            if (compare(a, b) >= 0)
            {
                a =
                        trimArray(
                                add(
                                        a, extendArray(twosComplement(duplicateArray(b)), a.length, ALL_INT_BIT_MASK),
                                        true, true));
                ret[retIndex] |= retMask;
            }
        }

        // Note that the result was written starting with the leftmost bit.  For big-endian format, all of the
        // unwritten bits need to be shifted right.
        ret = arrayShiftRight(ret, ret.length * 32 - (divisorShiftBits + 1));
        //ret = arrayShiftRight(ret, original_a_size - (divisor_shift_bits + 2));
        return new Pair<int[], int[]>(trimArray(ret), trimArray(a));
    }

    /**
     * Divides the first array by the second, producing the quotient as a new array.  This is a simple shift-and-divide
     * algorithm and is probably not very efficient.
     *
     * @param a The dividend array.  This array must not have leading zeroes.  The contents of this array will be
     *          destroyed.
     * @param b The divisor array.  This array must not have leading zeroes.  The contents of this array will be
     *          destroyed.
     * @return The quotient of the division.  This is always a new array.
     */
    static int[] divide(int[] a, int[] b)
    {
        return divideWithRemainder(a, b).getFirst();
    }

    /**
     * Divides the first array by the second, producing the quotient as a new array.  This is a simple shift-and-divide
     * algorithm and is probably not very efficient.
     *
     * @param a The dividend array.  This array must not have leading zeroes.  The contents of this array will be
     *          destroyed.
     * @param b The divisor array.
     * @return The remainder of the division.  This is always a new array.
     */
    static int[] modulo(int[] a, int[] b)
    {
        return divideWithRemainder(a, b).getSecond();
    }

    /**
     * Produces a copy of an array.
     *
     * @param array The array to copy.
     * @return A copy of that array.
     */
    static int[] duplicateArray(int[] array)
    {
        int[] ret = new int[array.length];
        System.arraycopy(array, 0, ret, 0, array.length);
        return ret;
    }
}

// END OF FILE