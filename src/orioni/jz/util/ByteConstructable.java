package orioni.jz.util;

/**
 * This interface is designed to indicate that the object in question is capable
 * of being turned into an array of bytes.  The object is reconstructable from
 * the byte array.
 * @author Zachary Palmer
 */
public interface ByteConstructable
{
    /**
     * Converts the object in question to the byte array.
     * @return An array of bytes representing the object.
     */
    public byte[] toBytes();

    /**
     * Converts an array of bytes into a representation of this object type.
     * @param array The array of bytes to be used to create the object.
     */
    public void fromBytes(byte[] array);
}
