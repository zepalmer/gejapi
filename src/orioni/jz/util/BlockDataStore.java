package orioni.jz.util;

import java.io.IOException;

/**
 * This class represents a storage area for a {@link BlockDataMap}.  It acts as an interface between the abstract data
 * map and its storage medium, allowing many forms of storage to contain {@link BlockDataMap} objects.
 * <P>
 * Extensions of this class should provide that, upon construction, a <code>BlockDataStore</code> is ready for read and
 * write operations and has a pointer offset of zero.  If this is not the case, that class should clearly specify as
 * such in its documentation.
 *
 * @author Zachary Palmer
 */
public abstract class BlockDataStore
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public BlockDataStore()
    {
        super();
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
    protected abstract void seek(long offset) throws IOException;

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
    protected abstract void seekRelative(long offsetAdjust) throws IOException;

    /**
     * Retrieves the current position of the pointer in the data store.
     * @return The pointer's offset in the data store.
     * @throws IOException If an I/O exception occurs while retrieving the offset of the block data store's pointer.
     */
    protected abstract long getPointerOffset() throws IOException;

    /**
     * Retrieves the size of the data store, in bytes.
     * @return The current size of the data store.
     * @throws IOException If an I/O error occurs while determining the length of the data store.
     */
    public abstract long getSize() throws IOException;

    /**
     * Sets the length of the data store.  This allows the data store to change in size.  If the data store grows, the
     * content of the bytes after growth is dependent upon the extension's data storage mechanism.
     * @param length The new length of the data store.
     * @throws IOException If an I/O exception occurred when changing the size of the data store.
     */
    protected abstract void setLength(long length) throws IOException;

    /**
     * Closes this data store; no further reading or writing can occur after this call.
     * @throws IOException If an I/O exception occurred when closing the data store.
     */
    public abstract void close() throws IOException;

    /**
     * This method writes the specified array of data at the current location of the data store's pointer.
     * @param data The data to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the data.
     */
    protected abstract void writeByteArray(byte[] data) throws IOException;

    /**
     * This method writes the specified byte of data at the current location of the data store's pointer.
     * @param data The byte of data to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the data.
     */
    protected abstract void writeByte(byte data) throws IOException;

    /**
     * This method writes the specified short at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The short to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the short.
     */
    protected abstract void writeShort(short data) throws IOException;

    /**
     * This method writes the specified char at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The char to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the char.
     */
    protected abstract void writeChar(char data) throws IOException;

    /**
     * This method writes the specified int at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The int to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the int.
     */
    protected abstract void writeInt(int data) throws IOException;

    /**
     * This method writes the specified long at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The long to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the long.
     */
    protected abstract void writeLong(long data) throws IOException;

    /**
     * This method writes the specified float at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The float to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the float.
     */
    protected abstract void writeFloat(float data) throws IOException;

    /**
     * This method writes the specified double at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @param data The double to write to the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to write the double .
     */
    protected abstract void writeDouble(double data) throws IOException;

    /**
     * This method reads the specified array of data at the current location of the data store's pointer.
     * @param data The array into which data should be read.
     * @return The number of bytes actually read.  This will be no larger than <code>data.length</code>.
     * @throws IOException If an I/O error occurs while attempting to read the data.
     */
    protected abstract int readByteArray(byte[] data) throws IOException;

    /**
     * This method reads the specified byte of data at the current location of the data store's pointer.
     * @return The byte of data to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the data.
     */
    protected abstract byte readByte() throws IOException;

    /**
     * This method reads the specified short at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The short to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the short.
     */
    protected abstract short readShort() throws IOException;

    /**
     * This method reads the specified char at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The char to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the char.
     */
    protected abstract char readChar() throws IOException;

    /**
     * This method reads the specified int at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The int to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the int.
     */
    protected abstract int readInt() throws IOException;

    /**
     * This method reads the specified long at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The long to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the long.
     */
    protected abstract long readLong() throws IOException;

    /**
     * This method reads the specified float at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The float to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the float.
     */
    protected abstract float readFloat() throws IOException;

    /**
     * This method reads the specified double at the current location of the data store's pointer.  Byte order may vary
     * depending upon the extension's data storage mechanism.
     * @return The double to read at the current location of the data store's pointer.
     * @throws IOException If an I/O error occurs while attempting to read the double .
     */
    protected abstract double readDouble() throws IOException;

// STATIC METHODS ////////////////////////////////////////////////////////////////

}