package orioni.jz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import orioni.jz.common.exception.ParseException;
import orioni.jz.io.IOUtilities;
import orioni.jz.io.bit.BitOrder;
import orioni.jz.io.bit.BitOutputStream;
import orioni.jz.io.bit.EndianFormat;

/**
 * This class contains a series of generic methods that may be useful in various situations.
 * 
 * @author Zachary Palmer
 */
public final class Utilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An Object array with no elements (length==0).
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    /**
     * An Object array array with no elements (length==0).
     */
    public static final Object[][] EMPTY_OBJECT_OBJECT_ARRAY = new Object[0][];
    /**
     * A Class array with no elements (length==0).
     */
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    /**
     * An <code>int</code> array with no elements (length==0).
     */
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    /**
     * An array of <code>int</code> arrays with no elements (length==0).
     */
    public static final int[][] EMPTY_INT_INT_ARRAY = new int[0][];
    /**
     * A <code>byte</code> array with no elements (length==0).
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * An {@link Integer} array with no elements (length==0).
     */
    public static final Integer[] EMPTY_INTEGER_ARRAY = new Integer[0];

    /**
     * An Object array with no elements (length==0) (for reflection parameter lists).
     */
    public static final Object[] PARAMS_NONE = EMPTY_OBJECT_ARRAY;
    /**
     * An Object array containing a single Boolean representing <code>true</code>.
     */
    public static final Object[] PARAMS_TRUE = new Object[] { Boolean.TRUE };
    /**
     * An Object array containing a single Boolean representing <code>false</code>.
     */
    public static final Object[] PARAMS_FALSE = new Object[] { Boolean.FALSE };
    /**
     * An Object array containing a single <code>null</code> value.
     */
    public static final Object[] PARAMS_NULL = new Object[] { null };

    /**
     * An empty, unmodifiable bag.
     */
    @SuppressWarnings( { "unchecked" })
    public static final Bag EMPTY_BAG = new UnmodifiableBag(new HashBag());

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Prevents <code>Utilities</code> from being instantiated.
     */
    private Utilities()
    {
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Converts a {@link Serializable} object into a <code>byte[]</code> by creating an {@link ObjectOutputStream},
     * writing the object to it, and piping the data to an input stream. This process requires the generation of a new
     * {@link Thread}, which will have terminated by the end of this method's execution.
     * 
     * @param s The {@link Serializable} object to convert to a <code>byte[]</code>.
     * @return The <code>byte[]</code> produced by an {@link ObjectOutputStream}.
     * @throws IOException If an I/O error occurs while serializing the object.
     */
    public static byte[] serialize(Serializable s) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(s);
        oos.close();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * Rebuilds a {@link Serializable} object from a byte array constructed by {@link
     * Utilities#serialize(Serializable)}.
     * 
     * @param data The <code>byte[]</code> created by {@link Utilities#serialize(Serializable)}.
     */
    public static Serializable deserialize(final byte[] data) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Serializable s = (Serializable) (ois.readObject());
        ois.close();
        bais.close();
        return s;
    }

    /**
     * Compresses an array of data using a {@link GZIPOutputStream} and returns it.
     * 
     * @param uncompressed The uncompressed data.
     * @return The compressed data.
     */
    public static byte[] gzip(byte[] uncompressed)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(uncompressed);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            IOUtilities.pumpStream(bais, gzos);
            bais.close();
            gzos.close();
            baos.close();
            return baos.toByteArray();
        } catch (IOException ioe)
        {
            // Impossible. ByteArrayOutputStream never throws these, and GZIPOutputStream is just a wrapper.
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Decompresses an array of data using a {@link GZIPInputStream} and returns it.
     * 
     * @param compressed The compressed data.
     * @return The uncompressed data.
     */
    public static byte[] ungzip(byte[] compressed)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            GZIPInputStream gzis = new GZIPInputStream(bais);
            IOUtilities.pumpStream(gzis, baos);
            gzis.close();
            bais.close();
            baos.close();
            return baos.toByteArray();
        } catch (IOException ioe)
        {
            // Impossible. ByteArrayInputStream never throws these, and GZIPInputStream is just a wrapper.
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Inflates an array of zlib-compressed data.
     * 
     * @param compressed The compressed data.
     * @return The data in an uncompressed form.
     * @throws DataFormatException If the compressed data is improperly formatted.
     * @throws ParseException If the data is incomplete or a dictionary is required.
     */
    public static byte[] inflate(byte[] compressed) throws DataFormatException, ParseException
    {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);
        byte[] buffer = new byte[16384];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (inflater.getRemaining() > 0)
        {
            int read = inflater.inflate(buffer);
            if (read == 0)
                throw new ParseException("Data incomplete.");
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    /**
     * Deflates an array of data using zlib compression.
     * 
     * @param uncompressed The data to compress.
     * @return The zlib-compressed data.
     */
    public static byte[] deflate(byte[] uncompressed)
    {
        Deflater deflater = new Deflater();
        deflater.setInput(uncompressed);
        byte[] buffer = new byte[16384];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (!deflater.finished())
        {
            int read = deflater.deflate(buffer);
            if (read == 0)
                break;
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    /**
     * Uses the specified bit pattern to create a byte array containing a given number of bytes. The pattern can be any
     * length greater than zero and will be repeated when it is exhausted. For example, for the bit pattern
     * <UL>
     * <code>11110011 00011001 1100</code> (or <code>F319C</code> hex)
     * </UL>
     * and the requested size of six bytes, the result would be an array containing the elements
     * <UL>
     * <code>11110011 00011001 11001111 00110001 10011100
     * 11110011</code> (or <code>F319CF319CF3</code> hex)
     * </UL>. <p/> Note that patterns can be required to repeat up to eight times before the resulting byte pattern
     * repeats. For example, the bit pattern <code>1010111</code> results in the hexadecimal pattern
     * <code>AF5EBD7AF5EBD7...</code>.
     * 
     * @param pattern The <code>byte[]</code> containing the bit pattern to use.
     * @param bitOffset The <i>bit offset</i> in that array at which the pattern begins. Note that this is distinct
     *            from the byte offset.
     * @param patternLength The number of bits in that array to use as a pattern.
     * @param arrayLength The length in bytes of the array to create.
     * @return The created array which follows the given bit pattern.
     */
    public static byte[] createBitPatternArray(byte[] pattern, int bitOffset, int patternLength, int arrayLength)
    {
        int bits = patternLength;
        while (bits % 8 != 0)
            bits *= 2;
        if (arrayLength > bits / 8)
        {
            // We can create a sub-pattern to save time. This pattern will be repeated over the byte boundary, which
            // allows the rest of the work to be done by System.arraycopy
            byte[] subpattern = createBitPatternArray(pattern, bitOffset, patternLength, bits / 8);
            byte[] ret = new byte[arrayLength];
            int index = 0;
            while (index < ret.length)
            {
                int toWrite = Math.min(ret.length - index, subpattern.length);
                System.arraycopy(subpattern, 0, ret, index, toWrite);
                index += toWrite;
            }
            return ret;
        } else
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitOutputStream bos = new BitOutputStream(baos, BitOrder.HIGHEST_BIT_FIRST, EndianFormat.BIG_ENDIAN);
            long bitIndex = 0;
            long targetIndex = arrayLength * (long) 8;
            while (bitIndex < targetIndex)
            {
                int patternByteIndex = (int) ((bitOffset + (bitIndex % patternLength)) / 8);
                int patternBitIndex = (int) ((bitOffset + (bitIndex % patternLength)) % 8);
                try
                {
                    bos.writeBit((pattern[patternByteIndex] & (0x01 << (7 - patternBitIndex))) != 0);
                } catch (IOException e)
                {
                    // Can't happen.
                }
                bitIndex++;
            }
            return baos.toByteArray();
        }
    }

    /**
     * Called to join a thread "safely." This method is designed to prevent the deadlock which is experienced when a
     * thread tries to join itself. Calls to <code>Thread.currentThread().join()</code> always result in a deadlock.
     * This method calls {@link Thread#join()} on the provided thread if and only if the provided thread is not the
     * current thread. This method also ignores {@link InterruptedException}s which are thrown by the {@link
     * Thread#join()} call.
     * 
     * @param thread The {@link Thread} on which to join.
     */
    public static void safeJoin(Thread thread)
    {
        try
        {
            if (!Thread.currentThread().equals(thread))
                thread.join();
        } catch (InterruptedException ie)
        {
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static <T> void reverseArray(T[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            T temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(byte[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            byte temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(short[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            short temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(int[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(long[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            long temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(float[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            float temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(double[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            double temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Reverses the contents of the provided array.
     * 
     * @param arr The array to reverse.
     */
    public static void reverseArray(boolean[] arr)
    {
        for (int i = 0; i < arr.length / 2; i++)
        {
            boolean temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(byte[] a, byte[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(short[] a, short[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(int[] a, int[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(long[] a, long[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(float[] a, float[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(double[] a, double[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Performs a deep comparison of each of the elements in the provided array. This method is similar in behavior to
     * {@link Arrays#deepEquals(Object[], Object[])} but functions for an array containing primitives.
     * 
     * @param a The first array.
     * @param b The second array.
     * @return <code>true</code> if the arrays are the same length and each element in one array is equal to the
     *         corresponding element in the other.
     */
    public static boolean deepEquals(boolean[] a, boolean[] b)
    {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static byte[] copyArray(byte[] array)
    {
        byte[] ret = new byte[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static short[] copyArray(short[] array)
    {
        short[] ret = new short[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static char[] copyArray(char[] array)
    {
        char[] ret = new char[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static int[] copyArray(int[] array)
    {
        int[] ret = new int[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static long[] copyArray(long[] array)
    {
        long[] ret = new long[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static float[] copyArray(float[] array)
    {
        float[] ret = new float[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static double[] copyArray(double[] array)
    {
        double[] ret = new double[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Creates a deep copy of the provided array.
     * 
     * @param array The array to copy.
     * @return The deep copy of that array.
     */
    public static boolean[] copyArray(boolean[] array)
    {
        boolean[] ret = new boolean[array.length];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static byte[] mergeArrays(byte[]... arrays)
    {
        int size = 0;
        for (byte[] o : arrays)
            size += o.length;
        byte[] ret = new byte[size];
        int index = 0;
        for (byte[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static short[] mergeArrays(short[]... arrays)
    {
        int size = 0;
        for (short[] o : arrays)
            size += o.length;
        short[] ret = new short[size];
        int index = 0;
        for (short[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static int[] mergeArrays(int[]... arrays)
    {
        int size = 0;
        for (int[] o : arrays)
            size += o.length;
        int[] ret = new int[size];
        int index = 0;
        for (int[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static long[] mergeArrays(long[]... arrays)
    {
        int size = 0;
        for (long[] o : arrays)
            size += o.length;
        long[] ret = new long[size];
        int index = 0;
        for (long[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static float[] mergeArrays(float[]... arrays)
    {
        int size = 0;
        for (float[] o : arrays)
            size += o.length;
        float[] ret = new float[size];
        int index = 0;
        for (float[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static double[] mergeArrays(double[]... arrays)
    {
        int size = 0;
        for (double[] o : arrays)
            size += o.length;
        double[] ret = new double[size];
        int index = 0;
        for (double[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static boolean[] mergeArrays(boolean[]... arrays)
    {
        int size = 0;
        for (boolean[] o : arrays)
            size += o.length;
        boolean[] ret = new boolean[size];
        int index = 0;
        for (boolean[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Merges the provided arrays into one array.
     * 
     * @param arrays The arrays to merge.
     * @return The resulting array.
     */
    public static Object[] mergeArrays(Object[]... arrays)
    {
        int size = 0;
        for (Object[] o : arrays)
            size += o.length;
        Object[] ret = new Object[size];
        int index = 0;
        for (Object[] o : arrays)
        {
            System.arraycopy(o, 0, ret, index, o.length);
            index += o.length;
        }
        return ret;
    }

    /**
     * Creates a copy of the given array.
     * 
     * @param array The array whose contents are to be copied.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static <T> T[] copyArray(T[] array)
    {
        return copyArray(array, 0, array.length);
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyArray(T[] array, int offset, int length)
    {
        T[] ret = (T[]) (Array.newInstance(array.getClass().getComponentType(), length));
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static byte[] copyArray(byte[] array, int offset, int length)
    {
        byte[] ret = new byte[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static short[] copyArray(short[] array, int offset, int length)
    {
        short[] ret = new short[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static int[] copyArray(int[] array, int offset, int length)
    {
        int[] ret = new int[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static long[] copyArray(long[] array, int offset, int length)
    {
        long[] ret = new long[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static float[] copyArray(float[] array, int offset, int length)
    {
        float[] ret = new float[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static double[] copyArray(double[] array, int offset, int length)
    {
        double[] ret = new double[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * Creates a copy of the given subsection of an array.
     * 
     * @param array The array whose contents are to be copied.
     * @param offset The starting index of the copy.
     * @param length The size of the resulting array.
     * @return The copied subsection of the array.
     * @throws IndexOutOfBoundsException If the specified subsection exceeds the bounds of the source array.
     */
    public static boolean[] copyArray(boolean[] array, int offset, int length)
    {
        boolean[] ret = new boolean[length];
        System.arraycopy(array, offset, ret, 0, length);
        return ret;
    }

    /**
     * This method creates a wrapper for the provided {@link Bag} which is unmodifiable.
     * 
     * @param bag The bag in question.
     * @return An unmodifiable bag backed by the provided bag.
     */
    public static <T> Bag<T> unmodifiableBag(Bag<T> bag)
    {
        return new UnmodifiableBag<T>(bag);
    }

    /**
     * This method creates a typesafe, empty, unmodifiable bag. It is similar to methods such as {@link
     * java.util.Collections#emptySet()}.
     * 
     * @return A typesafe, empty, unmodifiable bag.
     */
    @SuppressWarnings("unchecked")
    public static <T> Bag<T> emptyBag()
    {
        return (Bag<T>) (EMPTY_BAG);
    }

    /**
     * This convenience method creates a {@link Map} object from the provided sequence of key-value pairs.
     * 
     * @param pairs The key-value pairs to use.
     * @return The resulting {@link Map}.
     */
    public static <K, V> Map<K, V> inlineMap(Pair<K, V>... pairs)
    {
        HashMap<K, V> map = new HashMap<K, V>();
        for (Pair<K, V> pair : pairs)
        {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
    }

    /**
     * Creates a {@link String} representing the full stack trace of the provided {@link Throwable}.
     * 
     * @param t The {@link Throwable} in question.
     * @return The {@link Throwable}'s stack trace.
     */
    public static String getStackTrace(Throwable t)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        t.printStackTrace(ps);
        ps.close();
        return new String(baos.toByteArray());
    }

    /**
     * Determines whether or not two object references are equal. This is true if:
     * <ul>
     * <li>Both references are <code>null</code>, or</li>
     * <li>The first reference is equal to the second reference by use of the <code>equals</code> method.
     * </ul>
     * This method provides a convenience mechanism for avoiding explicit null checks.
     * 
     * @param a The first object.
     * @param b The second object.
     */
    public static boolean objectsAreEqual(Object a, Object b)
    {
        if (a == null)
        {
            return b == null;
        } else
        {
            return a.equals(b);
        }
    }
}

// END OF FILE //
