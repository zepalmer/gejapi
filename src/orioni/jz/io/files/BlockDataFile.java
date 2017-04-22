package orioni.jz.io.files;

import orioni.jz.util.BlockDataStore;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This extension of {@link BlockDataStore} allows generic data storage of data through a
 * {@link orioni.jz.util.BlockDataMap} in a {@link RandomAccessFile}.
 *
 * @author Zachary Palmer
 */
public class BlockDataFile extends BlockDataStore
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link RandomAccessFile} being used to store the data. */
    protected RandomAccessFile raf;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param raf The {@link RandomAccessFile} that will be used to store the data.
     */
    public BlockDataFile(RandomAccessFile raf)
    {
        super();
        this.raf = raf;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method adjusts the location of the store's pointer, indicating that the next read or write should occur at
     * this location.  This is an absolute seek; the value of the store's pointer will be equal to the parameter
     * provided.  If the data store is not large enough to accomodate this pointer movement, it will grow to this size
     * when a write occurs beyond its end.
     * @param offset The new offset of the store's pointer.
     * @throws IOException If an I/O error occurs in attempting to set the store's pointer.  This can include situations
     *                     in which the store is unable to grow to the necessary size or when the <code>offset</code>
     *                     parameter is negative.
     */
    protected void seek(long offset) throws IOException
    {
        raf.seek(offset);
    }

    /**
     * This method adjusts the location of the store's pointer by the specified number of bytes, indicating that the
     * next read or write should occur at the new location.  This is a relative seek; the value of the store's pointer
     * will be equal to its old value plus the parameter provided.  If the data store is not large enough to accomodate
     * this pointer movement, it will grow to this size when a write occurs beyond its end.
     * @param offsetAdjust The amount by which the store's pointer should be adjusted.
     * @throws IOException If an I/O error occurs in attempting to set the store's pointer.  This can include
     *                     situations in which the store is unable to grow to the necessary size or when the value of
     *                     the <code>offset_adjust</code> parameter sets the store's pointer to an offset less than
     *                     zero.
     */
    protected void seekRelative(long offsetAdjust) throws IOException
    {
        raf.seek(raf.getFilePointer()+offsetAdjust);
    }

    /**
     * Retrieves the current position of the pointer in the data store.
     * @return The pointer's offset in the data store.
     * @throws IOException If an I/O exception occurs while retrieving the offset of the block data store's pointer.
     */
    protected long getPointerOffset() throws IOException
    {
        return raf.getFilePointer();
    }

    /**
     * Retrieves the size of the data store, in bytes.
     * @return The current size of the data store.
     * @throws IOException If an I/O error occurs while determining the length of the data store.
     */
    public long getSize() throws IOException
    {
        return raf.length();
    }

    /**
     * Sets the length of the data store.  This allows the data store to change in size.  If the data store grows, the
     * content of the bytes after growth is dependent upon the extension's data storage mechanism.
     * @param length The new length of the data store.
     * @throws IOException If an I/O exception occurred when changing the size of the data store.
     */
    protected void setLength(long length) throws IOException
    {
        raf.setLength(length);
    }

    /**
     * Closes this data store; no further reading or writing can occur after this call.
     * @throws IOException If an I/O exception occurred when closing the data store.
     */
    public void close() throws IOException
    {
        raf.close();
    }

    /**
     * This method writes the specified array of data at the current location of the data store's pointer.
     * @param data The data to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the data.
     */
    protected void writeByteArray(byte[] data) throws IOException
    {
        raf.write(data);
    }

    /**
     * This method writes the specified byte of data at the current location of the data store's pointer.
     * @param data The byte of data to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the data.
     */
    protected void writeByte(byte data) throws IOException
    {
        raf.writeByte(data);
    }

    /**
     * This method writes the specified short at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The short to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the short.
     */
    protected void writeShort(short data) throws IOException
    {
        raf.writeShort(data);
    }

    /**
     * This method writes the specified char at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The char to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the char.
     */
    protected void writeChar(char data) throws IOException
    {
        raf.writeChar(data);
    }

    /**
     * This method writes the specified int at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The int to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the int.
     */
    protected void writeInt(int data) throws IOException
    {
        raf.writeInt(data);
    }

    /**
     * This method writes the specified long at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The long to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the long.
     */
    protected void writeLong(long data) throws IOException
    {
        raf.writeLong(data);
    }

    /**
     * This method writes the specified float at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The float to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the float.
     */
    protected void writeFloat(float data) throws IOException
    {
        raf.writeFloat(data);
    }

    /**
     * This method writes the specified double at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The double to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the double .
     */
    protected void writeDouble(double data) throws IOException
    {
        raf.writeDouble(data);
    }

    /**
     * This method reads the specified array of data at the current location of the data store's pointer.
     * @param data The array into which data should be read.
     * @return The number of bytes actually read.  This will be no larger than <code>data.length</code>.
     * @throws IOException If an I/O error occurs while attempting to read the data.
     */
    protected int readByteArray(byte[] data) throws IOException
    {
        return raf.read(data);
    }

    /**
     * This method reads the specified byte of data at the current location of the data store's pointer.
     * @return The byte of data to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the data.
     */
    protected byte readByte() throws IOException
    {
        return raf.readByte();
    }

    /**
     * This method reads the specified short at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The short to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the short.
     */
    protected short readShort() throws IOException
    {
        return raf.readShort();
    }

    /**
     * This method reads the specified char at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The char to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the char.
     */
    protected char readChar() throws IOException
    {
        return raf.readChar();
    }

    /**
     * This method reads the specified int at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The int to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the int.
     */
    protected int readInt() throws IOException
    {
        return raf.readInt();
    }

    /**
     * This method reads the specified long at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The long to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the long.
     */
    protected long readLong() throws IOException
    {
        return raf.readLong();
    }

    /**
     * This method reads the specified float at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The float to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the float.
     */
    protected float readFloat() throws IOException
    {
        return raf.readFloat();
    }

    /**
     * This method reads the specified double at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The double to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the double .
     */
    protected double readDouble() throws IOException
    {
        return raf.readDouble();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}