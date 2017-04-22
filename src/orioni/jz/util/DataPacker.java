package orioni.jz.util;

/**
 * This extension allows the packing of native data types other than byte[].
 * @author Zachary Palmer
 */
public class DataPacker extends Packer
{
    /** Wrapper constructor. */
    public DataPacker() { super(); }

    /**
     * Adds a boolean to this DataPacker.
     * @param bool The boolean value to add to this DataPacker.  A boolean value
     *             added to a DataPacker takes one byte of space.
     */
    public void addBoolean(boolean bool)
    {
        if (bool) addByteArray(new byte[]{1});
	     else addByteArray(new byte[]{0});
    }

    /**
     * Adds a byte to this DataPacker.
     * @param value The byte to add to this DataPacker.
     */
    public void addByte(byte value)
    {
        addByteArray(new byte[]{value});
    }

    /**
     * Adds a short to this DataPacker.
     * @param value The short to add to this DataPacker.
     */
    public void addShort(short value)
    {
        addByteArray(DataConversion.shortToByteArray(value));
    }

    /**
     * Adds an int to this DataPacker.
     * @param value The int to add to this DataPacker.
     */
    public void addInt(int value)
    {
        addByteArray(DataConversion.intToByteArray(value));
    }

    /**
     * Adds a long to this DataPacker.
     * @param value The long to add to this DataPacker.
     */
    public void addLong(long value)
    {
        addByteArray(DataConversion.longToByteArray(value));
    }

    /**
     * Adds a String to this DataPacker.  The String will be prefixed by a 4
     * byte header stating its length.  Default encoding for the String will be
     * used.
     * @param string The String to add to this DataPacker.
     */
    public void addString(String string)
    {
        addByteArray(DataConversion.addHeader(string.getBytes()));
    }

	/**
	 * Adds a byte array to this {@link DataPacker} with a header attached.  The header describes the length of the
	 * byte array, allowing it to be retrieved without this knowledge later by the
	 * {@link DataUnpacker#getByteArrayWithHeader()} method.
	 * @param array The byte array to add, along with a header describing its size.
	 */
	public void addByteArrayWithHeader(byte[] array)
	{
		addByteArray(DataConversion.intToByteArray(array.length));
		addByteArray(array);
	}

    /**
     * Adds a ByteConstructable to the String.  As long as an Object implements
     * ByteConstructable and follows the contract of<BR><BR>&nbsp; &nbsp; &nbsp;
     * &nbsp; &nbsp; &nbsp;
     *      ByteConstructable b.fromBytes(b.toBytes()).equals(b)==true,<BR><BR>
     * then the Object can be packed and unpacked without error.
     */
    public void addByteConstructable(ByteConstructable b)
    {
        addByteArray(DataConversion.addHeader(b.toBytes()));
    }
}
