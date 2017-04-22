package orioni.jz.io.image;

import orioni.jz.util.DefaultValueHashMap;
import orioni.jz.util.Pair;
import orioni.jz.util.comparator.ReverseComparator;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <B><I>This file lacks a description.</I></B>
 *
 * @author Zachary Palmer
 */
public class PCXWriter extends ImageWriter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param spi The service provider instance which spawned this {@link PCXWriter}.
     */
    public PCXWriter(ImageWriterSpi spi)
    {
        super(spi);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    // comment inherited
    public IIOMetadata convertImageMetadata(IIOMetadata inData, ImageTypeSpecifier imageType, ImageWriteParam param)
    {
        return null;
    }

    // comment inherited
    public IIOMetadata convertStreamMetadata(IIOMetadata inData, ImageWriteParam param)
    {
        return null;
    }

    /**
     * Returns an <code>IIOMetadata</code> object containing default values for encoding an image of the given type. The
     * contents of the object may be manipulated using either the XML tree structure returned by the
     * <code>IIOMetadata.getAsTree</code> method, an <code>IIOMetadataController</code> object, or via plug-in specific
     * interfaces, and the resulting data supplied to one of the <code>write</code> methods that take a stream metadata
     * parameter.
     * <p/>
     * <p> An optional <code>ImageWriteParam</code> may be supplied for cases where it may affect the structure of the
     * image metadata.
     * <p/>
     * <p> If the supplied <code>ImageWriteParam</code> contains optional setting values not supported by this writer
     * (<i>e.g.</i> progressive encoding or any format-specific settings), they will be ignored.
     *
     * @param imageType an <code>ImageTypeSpecifier</code> indicating the format of the image to be written later.
     * @param param     an <code>ImageWriteParam</code> that will be used to encode the image, or <code>null</code>.
     * @return an <code>IIOMetadata</code> object.
     */
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType, ImageWriteParam param)
    {
        return null;
    }

    /**
     * Returns an <code>IIOMetadata</code> object containing default values for encoding a stream of images.  The
     * contents of the object may be manipulated using either the XML tree structure returned by the
     * <code>IIOMetadata.getAsTree</code> method, an <code>IIOMetadataController</code> object, or via plug-in specific
     * interfaces, and the resulting data supplied to one of the <code>write</code> methods that take a stream metadata
     * parameter.
     * <p/>
     * <p> An optional <code>ImageWriteParam</code> may be supplied for cases where it may affect the structure of the
     * stream metadata.
     * <p/>
     * <p> If the supplied <code>ImageWriteParam</code> contains optional setting values not supported by this writer
     * (<i>e.g.</i> progressive encoding or any format-specific settings), they will be ignored.
     * <p/>
     * <p> Writers that do not make use of stream metadata (<i>e.g.</i>, writers for single-image formats) should return
     * <code>null</code>.
     *
     * @param param an <code>ImageWriteParam</code> that will be used to encode the image, or <code>null</code>.
     * @return an <code>IIOMetadata</code> object.
     */
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param)
    {
        return null;
    }

    /**
     * Appends a complete image stream containing a single image and associated stream and image metadata and thumbnails
     * to the output.  Any necessary header information is included.  If the output is an
     * <code>ImageOutputStream</code>, its existing contents prior to the current seek position are not affected, and
     * need not be readable or writable.
     * <p/>
     * <p> The output must have been set beforehand using the <code>setOutput</code> method.
     * <p/>
     * <p> Stream metadata may optionally be supplied; if it is <code>null</code>, default stream metadata will be
     * used.
     * <p/>
     * <p> If <code>canWriteRasters</code> returns <code>true</code>, the <code>IIOImage</code> may contain a
     * <code>Raster</code> source.  Otherwise, it must contain a <code>RenderedImage</code> source.
     * <p/>
     * <p> The supplied thumbnails will be resized if needed, and any thumbnails in excess of the supported number will
     * be ignored. If the format requires additional thumbnails that are not provided, the writer should generate them
     * internally.
     * <p/>
     * <p>  An <code>ImageWriteParam</code> may optionally be supplied to control the writing process.  If
     * <code>param</code> is <code>null</code>, a default write param will be used.
     * <p/>
     * <p> If the supplied <code>ImageWriteParam</code> contains optional setting values not supported by this writer
     * (<i>e.g.</i> progressive encoding or any format-specific settings), they will be ignored.
     *
     * @param streamMetadata an <code>IIOMetadata</code> object representing stream metadata, or <code>null</code> to
     *                       use default values.
     * @param image          an <code>IIOImage</code> object containing an image, thumbnails, and metadata to be
     *                       written.
     * @param param          an <code>ImageWriteParam</code>, or <code>null</code> to use a default
     *                       <code>ImageWriteParam</code>.
     * @throws IllegalStateException         if the output has not been set.
     * @throws UnsupportedOperationException if <code>image</code> contains a <code>Raster</code> and
     *                                       <code>canWriteRasters</code> returns <code>false</code>.
     * @throws IllegalArgumentException      if <code>image</code> is <code>null</code>.
     * @throws java.io.IOException           if an error occurs during writing.
     */
    public void write(IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param)
            throws IOException
    {
        Raster raster = image.getRaster();
        RenderedImage ri = image.getRenderedImage();
        ImageOutputStream target = (ImageOutputStream) (this.output);

        BufferedImage bi;
        if (raster == null)
        {
            WritableRaster wr = ri.getData().createCompatibleWritableRaster();
            wr.setDataElements(0, 0, ri.getData());
            bi = new BufferedImage(ri.getColorModel(), wr, false, new Properties());
        } else
        {
            bi = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_INT_ARGB);
            bi.setData(raster);
        }

        // **************************************************
        // * We now have an image to use.  Prepare the PCX. *
        // **************************************************

        // First, determine how many colors we will need to optimize file size.
        // We will be selecting from three of the PCX formats:
        // EGA with modifiable palette, VGA palette, or 24-bit RGB.
        // We will use these if the image has 16 or less, between 17 and 256, or more than 256 colors, respectively
        DefaultValueHashMap<Integer, Integer> colorCountMap = new DefaultValueHashMap<Integer, Integer>(0);
        for (int y = 0; y < bi.getHeight(); y++)
        {
            for (int x = 0; x < bi.getWidth(); x++)
            {
                int rgb = bi.getRGB(x, y);
                colorCountMap.put(rgb, colorCountMap.get(rgb) + 1);
            }
        }

        // Create data fields which represent portions of the PCX file
        int version;
        int bitsPerPixelPerPlane;
        byte[] egaPalette = new byte[48];
        for (int i = 0; i < egaPalette.length; i++) egaPalette[i] = (byte) ((47 - i) / 3); // default EGA palette
        byte[] vgaPalette = null;
        int bitplanes;
        int bytesPerPlane;
        byte[][] scanlines = new byte[bi.getHeight()][];

        // Fill data fields
        if (colorCountMap.size() <= 16)
        {
            // EGA paletted based
            Map<Integer, Byte> palette = new HashMap<Integer, Byte>(); // RGB-to-palette-index
            byte index = 0;
            for (int rgb : colorCountMap.keySet())
            {
                egaPalette[index * 3] = (byte) ((rgb & 0xFF0000) >>> 16);
                egaPalette[index * 3 + 1] = (byte) ((rgb & 0xFF00) >>> 8);
                egaPalette[index * 3 + 2] = (byte) (rgb & 0xFF);
                palette.put(rgb, index++);
            }

            // Generate data
            for (int y = 0; y < bi.getHeight(); y++)
            {
                byte[] scanline = new byte[(bi.getWidth() + 1) / 2];
                for (int x = 0; x < bi.getWidth(); x++)
                {
                    scanline[x] |= palette.get(bi.getRGB(x, y)) << ((x / 2 == 0) ? 4 : 0);
                }
                scanlines[y] = scanline;
            }

            // Set header data
            version = PCXHeader.VERSION_MODIFIABLE_EGA_PALETTE;
            bitsPerPixelPerPlane = 4;
            bitplanes = 1;
            bytesPerPlane = (bi.getWidth() + 1) / 2;
        } else if (colorCountMap.size() > 256)
        {
            // RGB based
            int w = bi.getWidth();
            for (int y = 0; y < bi.getHeight(); y++)
            {
                byte[] scanline = new byte[w * 3];
                for (int x = 0; x < w; x++)
                {
                    scanline[x] = (byte) ((bi.getRGB(x, y) & 0xFF0000) >>> 16);
                }
                for (int x = 0; x < w; x++)
                {
                    scanline[x + w] = (byte) ((bi.getRGB(x, y) & 0xFF00) >>> 8);
                }
                for (int x = 0; x < w; x++)
                {
                    scanline[x + 2 * w] = (byte) (bi.getRGB(x, y) & 0xFF);
                }
                scanlines[y] = scanline;
            }

            // Set header data
            version = PCXHeader.VERSION_WINDOWS;
            bitsPerPixelPerPlane = 8;
            bitplanes = 3;
            bytesPerPlane = bi.getWidth();
        } else
        {
            // VGA palette based

            // Sort the colors in order of frequency; that way, we can push the more common colors toward the top of the
            // palette.
            //noinspection unchecked
            Pair<Integer, Integer>[] arr = new Pair[colorCountMap.keySet().size()];
            int index = 0;
            for (Integer rgb : colorCountMap.keySet())
            {
                arr[index++] = new Pair<Integer, Integer>(rgb, colorCountMap.get(rgb));
            }
            Arrays.sort(
                    arr, new ReverseComparator<Pair<Integer, Integer>>(
                    new Pair.SecondElementComparator<Integer, Integer>()));

            Map<Integer, Byte> palette = new HashMap<Integer, Byte>(); // RGB-to-palette-index
            vgaPalette = new byte[768];
            for (int i = 0; i < arr.length; i++)
            {
                vgaPalette[i * 3] = (byte) ((arr[i].getFirst() & 0xFF0000) >>> 16);
                vgaPalette[i * 3 + 1] = (byte) ((arr[i].getFirst() & 0xFF00) >>> 8);
                vgaPalette[i * 3 + 2] = (byte) (arr[i].getFirst() & 0xFF);
                palette.put(arr[i].getFirst(), (byte) (i));
            }

            // Generate data
            for (int y = 0; y < bi.getHeight(); y++)
            {
                byte[] scanline = new byte[bi.getWidth()];
                for (int x = 0; x < bi.getWidth(); x++)
                {
                    scanline[x] = palette.get(bi.getRGB(x, y));
                }
                scanlines[y] = scanline;
            }

            // Set header data
            version = PCXHeader.VERSION_WINDOWS;
            bitsPerPixelPerPlane = 8;
            bitplanes = 1;
            bytesPerPlane = bi.getWidth();
        }

        // RLE the unencoded data
        byte[][] encodedScanlines = new byte[bi.getHeight()][];
        for (int y = 0; y < scanlines.length; y++)
        {
            byte[] scanline = scanlines[y];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int index = 0;
            while (index < scanline.length)
            {
                int current = scanline[index++];
                int count = 1;
                while ((index < scanline.length) && (scanline[index] == current) && (count < 63))
                {
                    index++;
                    count++;
                }
                if ((count == 1) && ((current & 0xC0) != 0xC0))
                {
                    baos.write(current);
                } else
                {
                    baos.write(0xC0 | count);
                    baos.write(current);
                }
            }
            encodedScanlines[y] = baos.toByteArray();
        }

        // **********************
        // * Write the PCX file *
        // **********************

        target.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        // Header
        target.writeByte(0x0A);                     // PCX magic number
        target.writeByte(version);                  // PCX version number
        target.writeByte(1);                        // PCX encoding: RLE (only one choice)
        target.writeByte(bitsPerPixelPerPlane); // bits per pixel per plane
        target.writeShort(0);                       // Leftmost X coordiante
        target.writeShort(0);                       // Topmost Y coordinate
        target.writeShort(bi.getWidth() - 1);         // Rightmost X coordinate
        target.writeShort(bi.getHeight() - 1);        // Bottommost Y coordinate
        target.writeShort(72);                     // Horizontal resolution (DPI)
        target.writeShort(72);                     // Vertical resolution (DPI)
        target.write(egaPalette);                  // PCX EGA palette info
        target.writeByte(0);                        // Reserved byte (always zero)
        target.writeByte(bitplanes);                // Number of bitplanes per scanline
        target.writeShort(bytesPerPlane);         // Number of bytes in an uncompressed scanline bitplane
        target.writeShort(1);                       // "Palette type" - 1 for color, 2 for monochrome... unused
        target.writeShort(0);                     // Horizontal screen size.  Unusued by almost any viewer
        target.writeShort(0);                     // Vertical screen size.  Unusued by almost any viewer
        target.write(new byte[54]);                 // Reserved section.  Pads header out to 128 bytes.

        // Scanlines
        for (byte[] encodedScanline : encodedScanlines)
        {
            target.write(encodedScanline);
        }

        // Write VGA palette footer if necessary
        if (vgaPalette != null)
        {
            target.write(0x0C); // VGA palette indicator
            target.write(vgaPalette);
        }

        // ***************************
        // * PCX file write complete *
        // ***************************
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE