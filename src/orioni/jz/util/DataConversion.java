package orioni.jz.util;

/**
 * This class was provided for the conversion of data types.  No instantiation for this class has been developed; its
 * existence is merely procedural.
 *
 * @author Zachary Palmer
 */
public class DataConversion
{
    /**
     * Private constructor.
     */
    private DataConversion()
    {
    }

    /**
     * Converts a byte[2] to a short.
     *
     * @param array The array to convert.
     * @return The short value as a result of the conversion.
     * @throws ArrayIndexOutOfBoundsException If the array provided is not of size 2.
     */
    public static short byteArrayToShort(byte[] array)
            throws ArrayIndexOutOfBoundsException
    {
        if (array.length != 2)
            throw new ArrayIndexOutOfBoundsException(
                    "The size of the array provided to DataConversion was not two elements.");
        return (short) (((array[0] & 0xff) << 8) | (array[1] & 0xff));
    }

    /**
     * Converts a short to a byte[2].
     *
     * @param value The short to convert into a byte array.
     * @return A byte array representing the short value.
     */
    public static byte[] shortToByteArray(short value)
    {
        byte[] ret = new byte[2];
        ret[0] = (byte) (value >>> 8);
        ret[1] = (byte) (value);
        return ret;
    }

    /**
     * Converts a byte[4] to an int.
     *
     * @param array The array to convert.
     * @return The integer resulting from the conversion.
     * @throws ArrayIndexOutOfBoundsException If the array is not of size 4.
     */
    public static int byteArrayToInt(byte[] array)
    {
        if (array.length != 4)
            throw new ArrayIndexOutOfBoundsException(
                    "The size of the array provided to DataConversion was not four elements.");
        return (((array[0] & 0xff) << 24) | ((array[1] & 0xff) << 16) |
                ((array[2] & 0xff) << 8) | (array[3] & 0xff));
    }

    /**
     * Converts a byte[4] to an int.
     *
     * @param array  The array to convert.
     * @param offset The starting offset of the <code>int</code>.
     * @return The integer resulting from the conversion.
     * @throws ArrayIndexOutOfBoundsException If there are not four bytes following the specified offset.
     */
    public static int byteArrayToInt(byte[] array, int offset)
    {
        if (array.length < offset + 4)
            throw new ArrayIndexOutOfBoundsException(
                    "The size of the array provided to DataConversion does not accomodate the specified offset: length=" +
                    array.length +
                    ", offset=" +
                    offset +
                    ".");
        return (((array[offset] & 0xff) << 24) | ((array[offset + 1] & 0xff) << 16) |
                ((array[offset + 2] & 0xff) << 8) | (array[offset + 3] & 0xff));
    }

    /**
     * Converts an int to a byte[4].
     *
     * @param value The integer to convert.
     * @return The array resulting from the conversion.
     */
    public static byte[] intToByteArray(int value)
    {
        byte[] ret = new byte[4];
        ret[0] = (byte) (value >>> 24);
        ret[1] = (byte) (value >>> 16);
        ret[2] = (byte) (value >>> 8);
        ret[3] = (byte) (value);
        return ret;
    }

    /**
     * Converts a byte[8] to an long.
     *
     * @param array The array to convert.
     * @return The long resulting from the conversion.
     * @throws ArrayIndexOutOfBoundsException If the array is not of size 4.
     */
    public static long byteArrayToLong(byte[] array)
    {
        if (array.length != 8)
            throw new ArrayIndexOutOfBoundsException(
                    "The size of the array provided to DataConversion was not eight elements.");
        return (((array[0] & 0xffL) << 56) | ((array[1] & 0xffL) << 48) |
                ((array[2] & 0xffL) << 40) | ((array[3] & 0xffL) << 32) |
                ((array[4] & 0xff) << 24) | ((array[5] & 0xff) << 16) |
                ((array[6] & 0xff) << 8) | (array[7] & 0xff));
    }

    /**
     * Converts a long to a byte[8].
     *
     * @param value The long to convert.
     * @return The array resulting from the conversion.
     */
    public static byte[] longToByteArray(long value)
    {
        byte[] ret = new byte[8];
        ret[0] = (byte) (value >>> 56);
        ret[1] = (byte) (value >>> 48);
        ret[2] = (byte) (value >>> 40);
        ret[3] = (byte) (value >>> 32);
        ret[4] = (byte) (value >>> 24);
        ret[5] = (byte) (value >>> 16);
        ret[6] = (byte) (value >>> 8);
        ret[7] = (byte) (value);
        return ret;
    }

    /**
     * Appends a header to a byte[] describing the length of that byte[] without the header.  This is used to signify
     * the length of a byte[] that is otherwise indeterminable.  There is no need for a method to remove this byte array
     * as the method that is decoding that byte[] will need to remove the header to retrieve the byte[] anyway.  The
     * header is a byte array of length four that represents a 32-bit integer and can be converted to its original form
     * with byteArrayToInt(byte[]).
     *
     * @param array The byte array onto which the header will be appended.
     */
    public static byte[] addHeader(byte[] array)
    {
        byte[] ret = new byte[array.length + 4];
        System.arraycopy(DataConversion.intToByteArray(array.length), 0, ret, 0, 4);
        System.arraycopy(array, 0, ret, 4, array.length);
        return ret;
    }

    /**
     * Converts a byte[][] to a byte[] by concatenating each array byte[x][] into a single array.
     *
     * @param byteArrayArray The byte[][] to convert.
     * @return The byte[] resulting from the conversion.
     */
    public static byte[] byteArrayArrayToByteArray(byte[][] byteArrayArray)
    {
        int len = 0;
        int i;
        for (i = 0; i < byteArrayArray.length; i++)
            len += byteArrayArray[i].length;
        byte[] ret = new byte[len];
        int loc = 0;
        for (i = 0; i < byteArrayArray.length; i++)
        {
            System.arraycopy(byteArrayArray[i], 0, ret, loc, byteArrayArray[i].length);
            loc += byteArrayArray[i].length;
        }
        return ret;
    }

    /**
     * Converts an int to bytes and stores the results in the specified byte array at the specified location.
     * @param value The integer to convert.
     * @param array The array into which the result should be placed.
     * @param index The index of the first byte for the newly converted integer's bytes.
     */
    public static void storeIntInByteArray(int value, byte[] array, int index)
    {
        array[index] = (byte) (value >>> 24);
        array[index+1] = (byte)(value >>> 16);
        array[index+2] = (byte) (value >>> 8);
        array[index+3] = (byte)(value);
    }

    /**
     * Converts a string to a Pascal-style byte array.  The first four bytes in the array describe the size of the
     * string; the remaining bytes describe the string itself.  This is useful for applications in which the array
     * will be transmitted without its length metadata.
     * @param string The string to convert.
     * @return The resulting byte array.
     */
    public static byte[] stringToByteArray(String string)
    {
        return addHeader(string.getBytes());
    }
}
