package orioni.jz.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed to construct a byte array from a series of pieces of data by coverting the data to a
 * representative byte[] and, on call to pack(), concatenating the arrays.
 *
 * @author Zachary Palmer
 */
public class Packer
{
    /**
     * The byte arrays that have been added to this packer.
     */
    protected List<byte[]> arrays;

    /**
     * General constructor.
     */
    public Packer()
    {
        arrays = new ArrayList<byte[]>();
    }

    /**
     * Discards the data currently submitted to the Packer.
     */
    public void clear()
    {
        arrays.clear();
    }

    /**
     * Adds an array of bytes to the Packer.
     *
     * @param array The array to add to the Packer.
     */
    public void addByteArray(byte[] array)
    {
        if (array == null)
        {
            throw new NullPointerException("Attempted to add a null array to a Packer.");
        }
        arrays.add(array);
    }

    /**
     * Creates a packed byte[] containing all of the data that has been added to this Packer.
     *
     * @return A byte[] containing the concatenated data.
     */
    public byte[] pack()
    {
        byte[][] temp = arrays.toArray(new byte[0][]);
        return DataConversion.byteArrayArrayToByteArray(temp);
    }
}
