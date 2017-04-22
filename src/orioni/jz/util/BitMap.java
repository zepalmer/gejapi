package orioni.jz.util;

/**
 * This class represents a standard {@link BitMap}: an array of bits which can be accessed one at a time to store a
 * number of boolean values.
 *
 * @author Zachary Palmer
 */
public class BitMap
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An array containing masks for the indexed bit number.  For example, the element in the array at index
     * <code>3</code> contains a mask for bit <code>3</code>.
     */
    protected static final int[] BITMASKS = new int[]{0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};
    /**
     * An array containing inverse bitmasks for the indexed bit number.
     *
     * @see BitMap#BITMASKS
     */
    protected static final int[] INVERSE_BITMASKS = new int[]{0xFE, 0xFD, 0xFB, 0xF7, 0xEF, 0xDF, 0xBF, 0x7F};

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The <code>byte[]</code> used to store the bitmap.
     */
    protected byte[] bitmap;
    /**
     * The number of bits in the bitmap.
     */
    protected int bits;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  The bitmap is initialized with all values being false.
     *
     * @param bits The number of bits in the bitmap.
     */
    public BitMap(int bits)
    {
        this(new byte[(bits + 7) / 8], bits);
    }

    /**
     * General constructor.
     *
     * @param data The array containing the initial value of the bitmap.
     * @param bits The number of bits in the array which are valid.
     * @throws IndexOutOfBoundsException If the number of bits specified is larger than the number of bits in the
     *                                   array.
     */
    public BitMap(byte[] data, int bits)
    {
        if (bits > data.length * 8)
        {
            throw new IndexOutOfBoundsException(
                    "Not enough data provided: " + bits + " bits specified, " + data.length * 8 + " provided");
        }
        if (bits < 0) throw new IndexOutOfBoundsException("Bits value must be >=0.");

        bitmap = new byte[(bits + 7) / 8];
        System.arraycopy(data, 0, bitmap, 0, bitmap.length);
        this.bits = bits;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the number of bits in this bitmap.
     *
     * @return The number of bits in this bitmap.
     */
    public int getSize()
    {
        return bits;
    }

    /**
     * Retrieves the contents of this bitmap as an array.  The first bit is the highest-order bit in the first byte of
     * the array.
     */
    public byte[] getBitmap()
    {
        byte[] ret = new byte[bitmap.length];
        System.arraycopy(bitmap, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Retrieves the value of a bit in this bitmap.
     *
     * @param index The index of the bit.
     * @return <code>true</code> if the bit is set; <code>false</code> if it is clear.
     * @throws IndexOutOfBoundsException If the value of <code>index</code> is out of range.
     */
    public boolean getBit(int index)
    {
        if ((index < 0) || (index >= bits))
        {
            throw new IndexOutOfBoundsException("Index " + index + " out of range [0," + bits + ")");
        }
        return (bitmap[index / 8] & BITMASKS[index % 8]) != 0;
    }

    /**
     * Sets the value of a bit in the bitmap.
     *
     * @param index The index of the bit.
     * @param value The value of the bit: <code>true</code> for set, <code>false</code> for clear.
     * @throws IndexOutOfBoundsException If the value of <code>index</code> is out of range.
     */
    public void setBit(int index, boolean value)
    {
        if ((index < 0) || (index >= bits))
        {
            throw new IndexOutOfBoundsException("Index " + index + " out of range [0," + bits + ")");
        }
        if (value)
        {
            bitmap[index / 8] |= BITMASKS[index % 8];
        } else
        {
            bitmap[index / 8] &= INVERSE_BITMASKS[index % 8];
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE