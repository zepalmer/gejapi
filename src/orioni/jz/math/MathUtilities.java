package orioni.jz.math;


/**
 * This utilities class is designed to allow the storage of various mathematical procedures.
 *
 * @author Zachary Palmer
 */
public class MathUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.
     */
    private MathUtilities()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Evaluates the expression <code>base<sup>exp</sup></code> mod <code>modulus</code>.  This method uses addition
     * chaining to produce the result in linear time over the length of the exponent.
     *
     * @param base    The base of the expression.
     * @param exp     The exponent to which the base will be taken.  Must be positive or zero.
     * @param modulus The modulus of the result.
     * @return The value produced by evaluating the expression.
     * @throws ArithmeticException If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public static int exponentModulus(int base, int exp, int modulus)
    {
        if ((base == 0) && (exp == 0)) throw new ArithmeticException("0^0 is undefined");
        long current = 1;
        while (exp > 0)
        {
            if (exp % 2 == 1) current = (current * base) % modulus;
            exp /= 2;
            base = (base * base) % modulus;
        }
        return (int) current;
    }

    /**
     * Evaluates the expression <code>base<sup>exp</sup></code>.  If the result is outside of the bounds of
     * <code>int</code>, an exception is thrown.
     *
     * @param base The base of the operation.
     * @param exp  The exponent of the operation.  Must be positive or zero.
     * @return The base to the power of the exponent.
     * @throws IllegalArgumentException If the result is greater than {@link Integer#MAX_VALUE} or less than {@link
     *                                  Integer#MIN_VALUE}, or if <code>exp</code> is negative.
     * @throws ArithmeticException      If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public static int exponent(int base, int exp)
    {
        if (exp < 0) throw new IllegalArgumentException("This method cannot use negative exponents.");
        if ((base == 0) && (exp == 0)) throw new ArithmeticException("0^0 is undefined");
        long current = 1;
        while (exp > 0)
        {
            exp--;
            current *= base;
            if ((current > Integer.MAX_VALUE) || (current < Integer.MIN_VALUE))
            {
                throw new IllegalArgumentException("Result out of integer bounds.");
            }
        }
        return (int) current;
    }

    /**
     * Evaluates the expression <code>base<sup>exp</sup></code>.
     *
     * @param base The base of the operation.
     * @param exp  The exponent of the operation.  Must be positive or zero.
     * @return The base to the power of the exponent.  {@link Double.NaN} is returned if <code>a)</code> the base was
     *         {@link Double.NaN} or <code>b)</code> both <code>base</code> and <code>exp</code> are <code>0</code>.
     * @throws IllegalArgumentException If <code>exp</code> is negative.
     * @throws ArithmeticException      If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public static double exponent(double base, int exp)
    {
        if (exp < 0) throw new IllegalArgumentException("This method cannot use negative exponents.");
        if ((base == 0) && (exp == 0)) return Double.NaN;
        double ret = 1;
        while (exp > 0)
        {
            exp--;
            ret *= base;
        }
        return ret;
    }

    /**
     * Evaluates the expression <code>base<sup>exp</sup></code>.  If the result is outside of the bounds of a
     * <code>long</code>, the return value is unspecified.
     *
     * @param base The base of the operation.
     * @param exp  The exponent of the operation.  Must be positive or zero.
     * @return The base to the power of the exponent.
     * @throws IllegalArgumentException If <code>exp</code> is negative.
     * @throws ArithmeticException      If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public static long exponent(long base, long exp)
    {
        if (exp < 0) throw new IllegalArgumentException("This method cannot use negative exponents.");
        if ((base == 0) && (exp == 0)) throw new ArithmeticException("0^0 is undefined");
        long current = 1;
        while (exp > 0)
        {
            exp--;
            current *= base;
        }
        return current;
    }

    /**
     * Evaluates the expression <code>base<sup>exp</sup></code>.
     *
     * @param base The base of the operation.
     * @param exp  The exponent of the operation.  Must be positive or zero.
     * @return The base to the power of the exponent.  {@link Double.NaN} is returned if <code>a)</code> the base was
     *         {@link Double.NaN} or <code>b)</code> both <code>base</code> and <code>exp</code> are <code>0</code>.
     * @throws IllegalArgumentException If <code>exp</code> is negative.
     * @throws ArithmeticException      If <code>base</code> and <code>exp</code> are both <code>0</code>.
     */
    public static double exponent(double base, long exp)
    {
        if (exp < 0) throw new IllegalArgumentException("This method cannot use negative exponents.");
        if ((base == 0) && (exp == 0)) return Double.NaN;
        double ret = 1;
        while (exp > 0)
        {
            exp--;
            ret *= base;
        }
        return ret;
    }

    /**
     * Evaluates the expression <code>n!</code>.  If the result is outside of the bounds of an <code>int</code>, an
     * exception is thrown.
     *
     * @param n The number whose factorial is to be determined.
     * @return The factorial of that number.  Any number less than <code>1</code> is said to have a factorial of
     *         <code>1</code>.
     * @throws IllegalArgumentException If the result is greater than {@link Integer#MAX_VALUE}.
     */
    public static int factorial(int n)
    {
        long ret = 1;
        for (int i = 2; i < n; i++)
        {
            ret *= i;
            if (ret > Integer.MAX_VALUE) throw new IllegalArgumentException("Result out of integer bounds.");
        }
        return (int) ret;
    }

    /**
     * Evaluates the expression <code>n!</code>.  If the result is outside of the bounds of a <code>long</code>, an
     * exception is thrown.
     *
     * @param n The number whose factorial is to be determined.
     * @return The factorial of that number.  Any number less than <code>1</code> is said to have a factorial of
     *         <code>1</code>.
     * @throws IllegalArgumentException If the result is greater than {@link Long#MAX_VALUE}.
     */
    public static long factorial(long n)
    {
        long ret = 1;
        for (int i = 2; i < n; i++)
        {
            if (Long.MAX_VALUE / ret < i) throw new IllegalArgumentException("Result out of long bounds.");
            ret *= i;
        }
        return ret;
    }

    /**
     * Evaluates the logarithm of a specified value to a specified base.
     *
     * @param value The value whose logarithm is to be computed.
     * @param base  The base of the logarithm.
     * @return The result of the function.
     */
    public static double log(double value, double base)
    {
        return Math.log(value) / Math.log(base);
    }

    /**
     * Returns a value which is contained within the specified bounds.  Calling this method as <code>bound(a,b,c)</code>
     * has the same effect as calling <code>Math.max(Math.min(a,c),b)</code>; the return value is no less than
     * <code>b</code> and no greater than <code>c</code>.
     *
     * @param value The value being bounded
     * @param lower The lower bound
     * @param upper The upper bound.
     */
    public static int bound(int value, int lower, int upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    /**
     * Returns a value which is contained within the specified bounds.  Calling this method as <code>bound(a,b,c)</code>
     * has the same effect as calling <code>Math.max(Math.min(a,c),b)</code>; the return value is no less than
     * <code>b</code> and no greater than <code>c</code>.
     *
     * @param value The value being bounded
     * @param lower The lower bound
     * @param upper The upper bound.
     */
    public static long bound(long value, long lower, long upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    /**
     * Returns a value which is contained within the specified bounds.  Calling this method as <code>bound(a,b,c)</code>
     * has the same effect as calling <code>Math.max(Math.min(a,c),b)</code>; the return value is no less than
     * <code>b</code> and no greater than <code>c</code>.
     *
     * @param value The value being bounded
     * @param lower The lower bound
     * @param upper The upper bound.
     */
    public static float bound(float value, float lower, float upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    /**
     * Returns a value which is contained within the specified bounds.  Calling this method as <code>bound(a,b,c)</code>
     * has the same effect as calling <code>Math.max(Math.min(a,c),b)</code>; the return value is no less than
     * <code>b</code> and no greater than <code>c</code>.
     *
     * @param value The value being bounded
     * @param lower The lower bound
     * @param upper The upper bound.
     */
    public static double bound(double value, double lower, double upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    /**
     * Determines if the provided value is within the specified bounds.  Calling this method as
     * <ul><code>isBoundedBy(a,b,c)</code></ul> has the same effect as the expression <ul><code>((a>=b) &&
     * (a<=c))</code></ul>.  In the event that the expressions defining the values of <code>a</code>, <code>b</code>,
     * and <code>c</code> are complex, this method can help simplify the appearance of the code.
     *
     * @param value The value to check.
     * @param lower The lower bound.
     * @param upper The upper bound.
     */
    public static boolean isBoundedBy(int value, int lower, int upper)
    {
        return ((value >= lower) && (value <= upper));
    }

    /**
     * Determines if the provided value is within the specified bounds.  Calling this method as
     * <ul><code>isBoundedBy(a,b,c)</code></ul> has the same effect as the expression <ul><code>((a>=b) &&
     * (a<=c))</code></ul>.  In the event that the expressions defining the values of <code>a</code>, <code>b</code>,
     * and <code>c</code> are complex, this method can help simplify the appearance of the code.
     *
     * @param value The value to check.
     * @param lower The lower bound.
     * @param upper The upper bound.
     */
    public static boolean isBoundedBy(long value, long lower, long upper)
    {
        return ((value >= lower) && (value <= upper));
    }

    /**
     * Determines if the provided value is within the specified bounds.  Calling this method as
     * <ul><code>isBoundedBy(a,b,c)</code></ul> has the same effect as the expression <ul><code>((a>=b) &&
     * (a<=c))</code></ul>.  In the event that the expressions defining the values of <code>a</code>, <code>b</code>,
     * and <code>c</code> are complex, this method can help simplify the appearance of the code.
     *
     * @param value The value to check.
     * @param lower The lower bound.
     * @param upper The upper bound.
     */
    public static boolean isBoundedBy(float value, float lower, float upper)
    {
        return ((value >= lower) && (value <= upper));
    }

    /**
     * Determines if the provided value is within the specified bounds.  Calling this method as
     * <ul><code>isBoundedBy(a,b,c)</code></ul> has the same effect as the expression <ul><code>((a>=b) &&
     * (a<=c))</code></ul>.  In the event that the expressions defining the values of <code>a</code>, <code>b</code>,
     * and <code>c</code> are complex, this method can help simplify the appearance of the code.
     *
     * @param value The value to check.
     * @param lower The lower bound.
     * @param upper The upper bound.
     */
    public static boolean isBoundedBy(double value, double lower, double upper)
    {
        return ((value >= lower) && (value <= upper));
    }

    /**
     * Determines the greatest common factor between two <code>int</code>s.
     *
     * @param a The first number.
     * @param b The second number.
     * @return The numbers' greatest common factor.
     */
    public static int gcd(int a, int b)
    {
        if ((a == 0) || (b == 0)) return 0;
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        while (a > 0)
        {
            int c = b % a;
            b = a;
            a = c;
        }
        return b;
    }

    /**
     * Determines the greatest common factor between two <code>long</code>s.
     *
     * @param a The first number.
     * @param b The second number.
     * @return The numbers' greatest common factor.
     */
    public static long gcd(long a, long b)
    {
        if ((a == 0) || (b == 0)) return 0;
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        while (a > 0)
        {
            long c = b % a;
            b = a;
            a = c;
        }
        return b;
    }

    /**
     * Determines the least common multiple of two <code>int</code>s.
     *
     * @param a The first number.
     * @param b The second number.
     * @return The least common multiple of those two numbers.
     */
    public static int lcm(int a, int b)
    {
        return (a / gcd(a, b)) * b;
    }

    /**
     * Determines the least common multiple of two <code>int</code>s.
     *
     * @param a The first number.
     * @param b The second number.
     * @return The least common multiple of those two numbers.
     */
    public static long lcm(long a, long b)
    {
        return (a / gcd(a, b)) * b;
    }

    /**
     * Determines the number of significant bits in the provided <code>int</code>.  This is the index of the first
     * non-zero bit plus 1, assuming the right-most bit is index <code>0</code>.  Therefore, for the value
     * <code>0</code>, this method returns <code>0</code>.
     *
     * @param value The value whose bits should be examined.
     * @return The number of significant bits in the value.  Values of zero are considered to have no significant bits.
     */
    public static int countSignificantBits(int value)
    {
        if (value == 0) return value;
        if ((value >>> 16) == 0)
        {
            if ((value >>> 8) == 0)
            {
                if ((value >>> 4) == 0)
                {
                    if ((value >>> 2) == 0)
                    {
                        if ((value >>> 1) == 0)
                        {
                            return 1;
                        } else
                        {
                            return 2;
                        }
                    } else
                    {
                        if ((value >>> 3) == 0)
                        {
                            return 3;
                        } else
                        {
                            return 4;
                        }
                    }
                } else
                {
                    if ((value >>> 6) == 0)
                    {
                        if ((value >>> 5) == 0)
                        {
                            return 5;
                        } else
                        {
                            return 6;
                        }
                    } else
                    {
                        if ((value >>> 7) == 0)
                        {
                            return 7;
                        } else
                        {
                            return 8;
                        }
                    }
                }
            } else
            {
                if ((value >>> 12) == 0)
                {
                    if ((value >>> 10) == 0)
                    {
                        if ((value >>> 9) == 0)
                        {
                            return 9;
                        } else
                        {
                            return 10;
                        }
                    } else
                    {
                        if ((value >>> 11) == 0)
                        {
                            return 11;
                        } else
                        {
                            return 12;
                        }
                    }
                } else
                {
                    if ((value >>> 14) == 0)
                    {
                        if ((value >>> 13) == 0)
                        {
                            return 13;
                        } else
                        {
                            return 14;
                        }
                    } else
                    {
                        if ((value >>> 15) == 0)
                        {
                            return 15;
                        } else
                        {
                            return 16;
                        }
                    }
                }
            }
        } else
        {
            if ((value >>> 24) == 0)
            {
                if ((value >>> 20) == 0)
                {
                    if ((value >>> 18) == 0)
                    {
                        if ((value >>> 17) == 0)
                        {
                            return 17;
                        } else
                        {
                            return 18;
                        }
                    } else
                    {
                        if ((value >>> 19) == 0)
                        {
                            return 19;
                        } else
                        {
                            return 20;
                        }
                    }
                } else
                {
                    if ((value >>> 22) == 0)
                    {
                        if ((value >>> 21) == 0)
                        {
                            return 21;
                        } else
                        {
                            return 22;
                        }
                    } else
                    {
                        if ((value >>> 23) == 0)
                        {
                            return 23;
                        } else
                        {
                            return 24;
                        }
                    }
                }
            } else
            {
                if ((value >>> 28) == 0)
                {
                    if ((value >>> 26) == 0)
                    {
                        if ((value >>> 25) == 0)
                        {
                            return 25;
                        } else
                        {
                            return 26;
                        }
                    } else
                    {
                        if ((value >>> 27) == 0)
                        {
                            return 27;
                        } else
                        {
                            return 28;
                        }
                    }
                } else
                {
                    if ((value >>> 30) == 0)
                    {
                        if ((value >>> 29) == 0)
                        {
                            return 29;
                        } else
                        {
                            return 30;
                        }
                    } else
                    {
                        if ((value >>> 31) == 0)
                        {
                            return 31;
                        } else
                        {
                            return 32;
                        }
                    }
                }
            }
        }
    }

    /**
     * Rotates the bits in the provided integer left by the given number of positions.
     *
     * @param data   The data to shift.
     * @param places The number of places to rotate.
     * @return The shifted value.
     */
    public static int rotateLeft(int data, int places)
    {
        places %= 32;
        if (places == 0) return data;
        return ((data << places) | (data >>> (32 - places)));
    }

    /**
     * Rotates the bits in the provided integer left by the given number of positions.
     *
     * @param data   The data to shift.
     * @param places The number of places to rotate.
     * @return The shifted value.
     */
    public static int rotateRight(int data, int places)
    {
        places %= 32;
        if (places == 0) return data;
        return ((data << places) | (data >>> (32 - places)));
    }

    /**
     * Determines whether or not the provided {@link String} represents an integer.
     *
     * @param string The string to test.
     * @param radix  The radix in which to test this number.  See {@link Integer#parseInt(String, int)}.
     * @return <code>true</code> if the string is an integer; <code>false</code> otherwise.
     */
    public static boolean isNumber(String string, int radix)
    {
        try
        {
            Integer.parseInt(string, radix);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Sets the number of relevant bits in the provided <code>int</code>.  All higher bits are set to zero.
     *
     * @param value The value to mask.
     * @param bits  The number of bits to keep.  Numbering the least significant bit <code>0</code> and the most
     *              significant bit <code>31</code>, all bits with an index greater than or equal to this parameter will
     *              be <code>0</code> after this operation completes.
     */
    public static int setRelevantBitCount(int value, int bits)
    {
        if ((bits < 0) || (bits > 31))
        {
            throw new IllegalArgumentException("Bit value " + bits + " out of range [0,31]");
        }
        return (value & (~(0xFFFFFFFF << bits)));
    }

    /**
     * Gets the bit in the provided value to a certain state.  This isn't particularly useful in a static case (as a
     * mask can be used instead).  This method is useful in a case when the bit to be retrieved is not constant,
     * however.
     *
     * @param value The original value.
     * @param bit   The index of the bit to set.
     * @return The state for the bit: <code>true</code> for <code>1</code>, <code>false</code> for <code>0</code>.
     */
    public static boolean getBit(int value, int bit)
    {
        if ((bit < 0) || (bit > 31)) throw new IllegalArgumentException("Bit value " + bit + " out of range [0,31]");
        return (value & (0x1 << bit)) != 0;
    }

    /**
     * Sets the bit in the provided value to a certain state.  This isn't particularly useful in a static case (as a
     * mask can be used instead).  This method is useful in a case when the bit to be set is not constant, however.
     *
     * @param value The original value.
     * @param bit   The index of the bit to set.
     * @param state The state for the bit: <code>true</code> for <code>1</code>, <code>false</code> for <code>0</code>.
     * @return The new value.
     */
    public static int setBit(int value, int bit, boolean state)
    {
        if ((bit < 0) || (bit > 31)) throw new IllegalArgumentException("Bit value " + bit + " out of range [0,31]");
        if (state)
        {
            return (value | (0x1 << bit));
        } else
        {
            return (value & (~(0x1 << bit)));
        }
    }

    /**
     * Reverses the order of the bits in the provided <code>int</code>.  Only the bits in the range specified are
     * reversed.  For example, if provided with an <code>int</code> with the bits <code>abcdefgh</code>, the start index
     * <code>5</code> and the stop index <code>2</code>, the returned value will be an <code>int</code> with the bits
     * <code>abfedcgh</code>.
     *
     * @param source The source <code>int</code> containing the bits.
     * @param start  The higher index bound of the bits to swap (inclusive).
     * @param stop   The lower index bound of the bits to swap (inclusive).
     */
    public static int reverseBits(int source, int start, int stop)
    {
        while (start > stop)
        {
            boolean tmp = getBit(source, start);
            source = setBit(source, start, getBit(source, stop));
            source = setBit(source, stop, tmp);
            start--;
            stop++;
        }
        return source;
    }

    /**
     * Counts the number of bits which are set in the provided <code>int</code>.  For example, if provided with the
     * value <code>0xFE2</code>, this method would return <code>8</code> (four for <code>F</code>, three for
     * <code>E</code>, and one for <code>2</code>).
     *
     * @param value The value to examine.
     * @return The number of set bits in the provided value.
     */
    public static int countSetBits(int value)
    {
        int mask = 0x1;
        int count = 0;
        for (int i = 0; i < 32; i++)
        {
            if ((value & mask) != 0) count++;
            mask <<= 1;
        }
        return count;
    }
}

// END OF FILE