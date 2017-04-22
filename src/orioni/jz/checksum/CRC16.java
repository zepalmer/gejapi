package orioni.jz.checksum;

/**
 * This class is designed to produce CRC16 values for blocks of data.  The CRC16 lookup table is constructed lazily.
 *
 * @author Zachary Palmer
 */
public class CRC16
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

    /**
     * The CRC16 lookup table.
     */
    private static short[] crcLookupTable = null;

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    private CRC16()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Creates a CRC16 checksum for the provided data.
     *
     * @param data The <code>byte[]</code> for which a checksum is desired.
     */
    public static short checksum(byte[] data)
    {
        if (crcLookupTable == null)
        {
            crcLookupTable = new short[256];
            for (int b = 0; b < 256; b++)
            {
                int tmp = ~b;
                int crc = 0;
                for (int i = 0; i < 8; i++)
                {
                    if (((tmp ^ crc) & 0x1) == 0)
                    {
                        crc = (crc >>> 1) ^ 0xA001;
                    } else
                    {
                        crc >>>= 1;
                    }
                    tmp >>>= 1;
                }
                crcLookupTable[b] = (short) crc;
            }
        }
        short ret = 0;
        for (byte b : data)
        {
            ret = (short) (((ret >>> 8) & 0xFF) ^ crcLookupTable[(ret ^ b) & 0xFF]);
        }
        return ret;
    }
}

// END OF FILE
