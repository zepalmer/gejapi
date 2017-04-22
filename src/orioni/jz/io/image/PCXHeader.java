package orioni.jz.io.image;

import orioni.jz.common.exception.ParseException;

import javax.imageio.stream.ImageInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * A class representing the header for the PCX format.
 *
 * @author Zachary Palmer
 */
public class PCXHeader
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The version number for PC Paintbrush v2.5 (fixed EGA palette information).
     */
    public static final int VERSION_FIXED_EGA_PALETTE = 2;
    /**
     * The version number for PC Paintbrush v2.8 (modifiable EGA palette information).
     */
    public static final int VERSION_MODIFIABLE_EGA_PALETTE = 2;
    /**
     * The version number for PC Paintbrush v2.8 (no palette information).
     */
    public static final int VERSION_NO_PALETTE = 4;
    /**
     * The version number for PC Paintbrush for Windows.
     */
    public static final int VERSION_WINDOWS = 5;

    /**
     * The encoding method number for RLE.
     */
    public static final int ENCODING_RLE = 1;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The PCX version number.
     */
    protected int version;
    /**
     * The encoding format for this PCX file.
     */
    protected int encoding;
    /**
     * The number of bits per pixel in this file.
     */
    protected int bitsPerPixel;
    /**
     * The first X-coordinate pixel in the image.
     */
    protected int firstX;
    /**
     * The first Y-coordinate pixel in the image.
     */
    protected int firstY;
    /**
     * The last X-coordinate pixel in the image.
     */
    protected int lastX;
    /**
     * The last Y-coordinate pixel in the image.
     */
    protected int lastY;
    /**
     * The horizontal resolution of this PCX.
     */
    protected int horizontalResolution;
    /**
     * The vertical resolution of this PCX.
     */
    protected int verticalResolution;
    /**
     * The 16-color EGA palette.
     */
    protected byte[] egaPalette;
    /**
     * The number of bit planes in this image.
     */
    protected int bitPlanes;
    /**
     * The number of bytes per image scanline.
     */
    protected int bytesPerScanline;
    /**
     * The type of palette in this image.
     */
    protected int paletteType;
    /**
     * The horizontal screen size for this PCX.
     */
    protected int horizontalScreenSize;
    /**
     * The vertical screen size for this PCX.
     */
    protected int verticalScreenSize;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param input The {@link ImageInputStream} from which to obtain the data.
     * @throws ParseException If the provided input source does not appear to contain a PCX header.
     * @throws EOFException   If an end of file is unexpectedly reached.
     * @throws IOException    If an I/O error occurs while reading the data.
     */
    public PCXHeader(ImageInputStream input)
            throws ParseException, IOException, EOFException
    {
        super();
        ByteOrder byteOrder = input.getByteOrder();
        try
        {
            input.setByteOrder(ByteOrder.LITTLE_ENDIAN);

            input.mark();
            if (input.readByte() != 0x0A)
            {
                // Failed to have proper magic number
                throw new ParseException("This input source does not appear to be a PCX file.");
            }

            version = input.readByte() & 0xFF;
            encoding = input.readByte() & 0xFF;
            if (encoding != ENCODING_RLE)
            {
                throw new ParseException("PCX encoding format unrecognized (" + encoding + " instead of 1)");
            }

            bitsPerPixel = input.readByte() & 0xFF;
            firstX = input.readShort() & 0xFFFF;
            firstY = input.readShort() & 0xFFFF;
            lastX = input.readShort() & 0xFFFF;
            lastY = input.readShort() & 0xFFFF;
            horizontalResolution = input.readShort() & 0xFFFF;
            verticalResolution = input.readShort() & 0xFFFF;
            egaPalette = new byte[48];
            input.readFully(egaPalette);
            input.readByte(); // always 0 - reserved
            bitPlanes = input.readByte() & 0xFF;
            bytesPerScanline = input.readShort() & 0xFFFF;
            paletteType = input.readShort() & 0xFFFF;
            horizontalScreenSize = input.readShort() & 0xFFFF;
            verticalScreenSize = input.readShort() & 0xFFFF;
            byte[] reserved = new byte[54];
            input.readFully(reserved);
        } catch (ParseException pe)
        {
            input.reset();
            input.setByteOrder(byteOrder);
            throw pe;
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    public int getBitPlanes()
    {
        return bitPlanes;
    }

    public int getBitsPerPixel()
    {
        return bitsPerPixel;
    }

    public int getBytesPerScanline()
    {
        return bytesPerScanline;
    }

    public byte[] getEgaPalette()
    {
        return egaPalette;
    }

    public int getEncoding()
    {
        return encoding;
    }

    public int getFirstX()
    {
        return firstX;
    }

    public int getFirstY()
    {
        return firstY;
    }

    public int getHorizontalResolution()
    {
        return horizontalResolution;
    }

    public int getHorizontalScreenSize()
    {
        return horizontalScreenSize;
    }

    public int getLastX()
    {
        return lastX;
    }

    public int getLastY()
    {
        return lastY;
    }

    public int getPaletteType()
    {
        return paletteType;
    }

    public int getVersion()
    {
        return version;
    }

    public int getVerticalResolution()
    {
        return verticalResolution;
    }

    public int getVerticalScreenSize()
    {
        return verticalScreenSize;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE