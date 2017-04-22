package orioni.jz.util;

/**
 * This class is designed to perform the reverse function of a Packer.  The byte
 * array submitted to the Unpacker is reassembled, piece by piece, into usable
 * data.
 * @author Zachary Palmer
 */
public class Unpacker
{
	/** Currently assigned data array. */
	protected byte[] data;
	/** The offset of the data reading pointer. */
	protected int offset;

	/**
	 * Skeleton constructor.  Assumes <code>null</code> for the initial byte array.  This requires that
	 * {@link Unpacker#set(byte[])} be called before any retrieval methods.
	 */
	public Unpacker()
	{
		this(null);
	}

	/**
	 * General constructor.
	 * @param array The initial byte array to use for unpacking.
	 */
	public Unpacker(byte[] array)
	{
		data = array;
		offset = 0;
	}

	/**
	 * Sets the array from which data will be extracted.
	 * @param array The array of bytes to use.
	 */
	public void set(byte[] array)
	{
		data = array;
		offset = 0;
	}

	/**
	 * Gets a data element from the array.
	 * @param size The size of the byte array to retrieve.
	 * @return The requested byte array.
	 * @throws NullPointerException If no array has been assigned to this
	 *                              Unpacker.
	 * @throws ArrayIndexOutOfBoundsException If more data is requested than is
	 *                                        available.
	 */
	public byte[] getByteArray(int size) throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		if (data == null) throw new NullPointerException("No data assigned to this Unpacker.");
		if (size + offset > data.length) throw new ArrayIndexOutOfBoundsException("More data requested from array than was available.");
		byte[] ret = new byte[size];
		System.arraycopy(data, offset, ret, 0, size);
		offset += size;
		return ret;
	}

	/**
	 * Gets the size of the data block on which this Unpacker is currently
	 * working.
	 * @return The total amount of data in the array.
	 */
	public int getTotalDataSize()
	{
		return data.length;
	}

	/**
	 * Gets the size of the data block that the Unpacker has not currently
	 * read.
	 * @return The amount of data remaining to be read.
	 */
	public int getRemainingDataSize()
	{
		return data.length - offset;
	}

	/**
	 * Retrieves the rest of the data contained within this unpacker.
	 * @return The rest of the data contained within this unpacker.
	 */
	public byte[] getRemainingData()
	{
		return getByteArray(getRemainingDataSize());
	}
}
