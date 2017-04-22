package orioni.jz.util;

/**
 * This extension allows the unpacking of native data types.
 * @author Zachary Palmer
 */
public class DataUnpacker extends Unpacker
{
	/**
	 * Wrapper constructor.
	 * @see Unpacker#Unpacker()
	 */
	public DataUnpacker()
	{
		super();
	}

	/**
	 * Wrapper constructor.
	 * @see Unpacker#Unpacker(byte[])
	 */
	public DataUnpacker(byte[] array)
	{
		super(array);
	}

	/**
	 * Retrieves a boolean from the arry.
	 * @return The boolean retrieved.
	 */
	public boolean getBoolean()
	{
		return (getByteArray(1)[0] != 0);
	}

	/**
	 * Retrieves a byte from the array.
	 * @return The byte retrieved.
	 */
	public byte getByte()
	{
		return getByteArray(1)[0];
	}

	/**
	 * Retrieves a short from the array.
	 * @return The short received.
	 */
	public short getShort()
	{
		return DataConversion.byteArrayToShort(getByteArray(2));
	}

	/**
	 * Retrieves an int from the array.
	 * @return The int received.
	 */
	public int getInt()
	{
		return DataConversion.byteArrayToInt(getByteArray(4));
	}

	/**
	 * Retrieves a long from the array.
	 * @return The long received.
	 */
	public long getLong()
	{
		return DataConversion.byteArrayToLong(getByteArray(8));
	}

	/**
	 * Retrieves a String from the array.  This string will be retrieved using
	 * this system's default encoding scheme.  If one machine is passing a
	 * String to another using DataPacker.addString and DataUnpacker.getString,
	 * both machines must be using the same default encoding scheme.
	 * @return The String received.
	 */
	public String getString()
	{
		int len = DataConversion.byteArrayToInt(getByteArray(4));
		return new String(getByteArray(len));
	}

	/**
	 * Retrieves a byte array from this {@link DataUnpacker} by reading its header to determine its length.  The
	 * byte array must have been written to this data pack by the {@link DataPacker#addByteArrayWithHeader(byte[])}
	 * method.
	 * @return A <code>byte[]</code> of a length dependent upon its header.
	 */
	public byte[] getByteArrayWithHeader()
	{
		int len = DataConversion.byteArrayToInt(getByteArray(4));
		return getByteArray(len);
	}

	/**
	 * Retrieves a ByteConstructable from the array.  As long as an Object
	 * implements ByteConstructable and follows the contract of <BR><BR>&nbsp;
	 * &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 *      ByteConstructable b.fromBytes(b.toBytes()).equals(b)==true,<BR><BR>
	 * then the Object can be packed and unpacked without error.
	 * @param target The target of the retrieval.  This method cannot return
	 *               an abstract ByteConstructable object because the interface
	 *               does not guarantee that a default constructor is available.
	 * @return The target that was passed in.
	 */
	public ByteConstructable getByteConstructable(ByteConstructable target)
	{
		int len = DataConversion.byteArrayToInt(getByteArray(4));
		target.fromBytes(getByteArray(len));
		return target;
	}
}
