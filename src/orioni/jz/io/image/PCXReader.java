package orioni.jz.io.image;

import orioni.jz.common.exception.ParseException;
import orioni.jz.io.bit.BitInputStream;
import orioni.jz.io.bit.BitOrder;
import orioni.jz.io.bit.EndianFormat;
import orioni.jz.math.MathUtilities;
import orioni.jz.util.ArrayIterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <B><I>This file lacks a description.</I></B>
 *
 * @author Zachary Palmer
 */
public class PCXReader extends ImageReader
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The PCX header for the provided input source.
     */
    protected PCXHeader header;
    /**
     * The {@link BufferedImage} read from the current input source.
     */
    protected BufferedImage image;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param provider The originating provider of this {@link ImageReader}.
     */
    public PCXReader(ImageReaderSpi provider)
    {
        super(provider);
        header = null;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Performs the normal {@link ImageReader#setInput(Object, boolean, boolean)} operation.  Also clears the PCX
     * header.
     *
     * @param input           the <code>ImageInputStream</code> or other <code>Object</code> to use for future
     *                        decoding.
     * @param seekForwardOnly if <code>true</code>, images and metadata may only be read in ascending order from this
     *                        input source.
     * @param ignoreMetadata  if <code>true</code>, metadata may be ignored during reads.
     * @throws IllegalArgumentException if <code>input</code> is not an instance of one of the classes returned by the
     *                                  originating service provider's <code>getInputTypes</code> method, or is not an
     *                                  <code>ImageInputStream</code>.
     * @see javax.imageio.stream.ImageInputStream
     * @see #getInput
     * @see javax.imageio.spi.ImageReaderSpi#getInputTypes
     */
    public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata)
    {
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        header = null;
        image = null;
    }

    /**
     * Ensures that the PCX header has been read from the input source.
     *
     * @return <code>true</code> if the PCX header has been read correctly; <code>false</code> if the input source does
     *         not appear to contain a valid PCX header.
     * @throws IOException If an I/O error occurs.
     */
    protected boolean ensureHeader()
            throws IOException
    {
        if (header == null)
        {
            try
            {
                header = new PCXHeader((ImageInputStream) this.input);
            } catch (ParseException e)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Ensures that the PCX image has been loaded.
     *
     * @return <code>true</code> if the PCX header has been read correctly; <code>false</code> if the input source does
     *         not appear to contain a valid PCX header.
     * @throws IOException If an I/O error occurs.
     */
    protected boolean ensureImage()
            throws IOException
    {
        if (image == null)
        {
            ImageInputStream source = (ImageInputStream) (this.input);
            if (!ensureHeader()) return false;

            // read PCX image
            int scanlineLength = header.getBitPlanes() * header.getBytesPerScanline();
            int[][] imageData = new int[getHeight(0)][getWidth(0)];

            for (int y = 0; y < header.getLastY() - header.getFirstY() + 1; y++)
            {
                // for each scanline:
                // decode the scanline
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); // used to buffer decoded scanline
                int written = 0;
                while (written < scanlineLength)
                {
                    int signal = source.readByte() & 0xFF;
                    int count;
                    int pixelData;
                    if ((signal & 0xC0) == 0xC0)
                    {
                        // compressed run-code
                        count = (signal & 0x3F);
                        pixelData = source.readByte() & 0xFF;
                    } else
                    {
                        count = 1;
                        pixelData = signal;
                    }
                    for (int i = 0; i < count; i++)
                    {
                        baos.write(pixelData);
                    }
                    written += count;
                }

                // process the scanline
                byte[] decoded = baos.toByteArray();
                BitInputStream bitInputStream = new BitInputStream(
                        new ByteArrayInputStream(decoded),
                        BitOrder.HIGHEST_BIT_FIRST, EndianFormat.BIG_ENDIAN);
                for (int i = 0; i < header.getBitPlanes(); i++)
                {
                    for (int x = 0;
                         x < (decoded.length / header.getBitPlanes() * (8 / header.getBitsPerPixel())); x++)
                    {
                        int data = bitInputStream.readBits(header.getBitsPerPixel());
                        if (x < imageData[y].length)
                        {
                            if (i < 3)
                            {
                                imageData[y][x] <<= header.getBitsPerPixel();
                                imageData[y][x] |= data;
                            } else
                            {
                                imageData[y][x] |= data << (i * header.getBitsPerPixel());
                            }
                        }
                    }
                }
            }

            // now that we have the image data, we need to try to make some sense of it
            // first... can we get a VGA palette from the file footer?
            byte[] vgaPalette = new byte[768];
            if (source.length() != -1)
            {
                source.skipBytes(source.length() - source.getStreamPosition() - 769);
            }
            try
            {
                source.readByte();
                source.readFully(vgaPalette);
            } catch (IOException ioe)
            {
                vgaPalette = null;
            }

            // Here's the general idea: if any pixel values are outside of the range of our palette, we have to
            // interpret them in terms of the number of bits per pixel (not per pixel per plane) over a generalized
            // palette.  Otherwise, they are indices into the palette that is available.  First, lets establish our
            // palette; then we'll get the maximum pixel data value.
            // As for CGA palettes... screw 'em.  The amount of effort required to support a CGA-style PCX palette is
            // far more than it's worth.
            java.util.List<Color> palette = new ArrayList<Color>();
            if (vgaPalette != null)
            {
                for (int i = 0; i < 256; i++)
                {
                    palette.add(
                            new Color(
                                    vgaPalette[i * 3] & 0xFF, vgaPalette[i * 3 + 1] & 0xFF,
                                    vgaPalette[i * 3 + 2] &
                                    0xFF));
                }
            } else
            {
                byte[] paletteData = header.getEgaPalette();
                for (int i = 0; i < 16; i++)
                {
                    palette.add(
                            new Color(
                                    paletteData[i * 3] & 0xFF, paletteData[i * 3 + 1] & 0xFF,
                                    paletteData[i * 3 + 2] & 0xFF));
                }
            }

            int maxPixelValue = 0;
            for (int[] imageDataRow : imageData)
            {
                for (int imageDataElement : imageDataRow)
                {
                    maxPixelValue = Math.max(maxPixelValue, imageDataElement);
                }
            }

            image = new BufferedImage(getWidth(0), getHeight(0), BufferedImage.TYPE_INT_ARGB);
            // Do we use the palette or not?
            if (maxPixelValue < palette.size())
            {
                // Yes, we do.
                for (int y = 0; y < getHeight(0); y++)
                {
                    for (int x = 0; x < getWidth(0); x++)
                    {
                        image.setRGB(x, y, palette.get(imageData[y][x]).getRGB());
                    }
                }
            } else
            {
                // No, we don't.
                int dataBits = header.getBitsPerPixel() * header.getBitPlanes();
                double dataRange = MathUtilities.exponent(2, dataBits);
                for (int y = 0; y < getHeight(0); y++)
                {
                    for (int x = 0; x < getWidth(0); x++)
                    {
                        int data = imageData[y][x];
                        if (dataBits < 24)
                        {
                            // Try to force the data into a 24-bit range
                            double value = data / dataRange;
                            data = (int) (Math.round(value * 0xFFFFFF));
                        }
                        image.setRGB(x, y, data | 0xFF000000); // Set alpha to 255
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the height in pixels of the given image within the input source.
     * <p/>
     * <p> If the image can be rendered to a user-specified size, then this method returns the default height.
     *
     * @param imageIndex the index of the image to be queried.
     * @return the height of the image, as an <code>int</code>.
     * @throws IllegalStateException     if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException               if an error occurs reading the height information from the input source.
     */
    public int getHeight(int imageIndex)
            throws IOException
    {
        if (imageIndex != 0) throw new IndexOutOfBoundsException("PCX files contain only one image (index 0).");
        if (ensureHeader())
        {
            return header.getLastY() - header.getFirstY() + 1;
        } else
        {
            throw new IOException("Could not obtain PCX header.");
        }
    }

    /**
     * Returns <code>null</code>; this reader does not support reading meta-data.
     *
     * @param imageIndex The index of the image whose metadata is to be retrieved.
     * @return An <code>IIOMetadata</code> object, or <code>null</code>.
     * @throws IllegalStateException     If the input source has not been set.
     * @throws IndexOutOfBoundsException If the supplied index is out of bounds.
     * @throws IOException               If an error occurs during reading.
     */
    public IIOMetadata getImageMetadata(int imageIndex)
            throws IOException
    {
        return null;
    }

    /**
     * Returns an <code>Iterator</code> containing possible image types to which the given image may be decoded, in the
     * form of <code>ImageTypeSpecifiers</code>s.  At least one legal image type will be returned.
     * <p/>
     * This specific implementation of {@link ImageReader} will obtain the PCX image and then return as the only type an
     * sRGB {@link BufferedImage}'s color model and sample model.
     *
     * @param imageIndex the index of the image to be <code>retrieved</code>.
     * @return An <code>Iterator</code> containing at least one <code>ImageTypeSpecifier</code> representing suggested
     *         image types for decoding the current given image.
     * @throws IllegalStateException     If the input source has not been set.
     * @throws IndexOutOfBoundsException If the supplied index is out of bounds.
     * @throws IOException               If an error occurs reading the format information from the input source.
     */
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
            throws IOException
    {
        if (input == null) throw new IllegalStateException("No input source.");
        if (imageIndex != 0) throw new IndexOutOfBoundsException("PCX files contain only one image (index 0).");
        if (!ensureImage())
        {
            throw new IOException("Could not obtain image.");
        }
        return new ArrayIterator<ImageTypeSpecifier>(
                new ImageTypeSpecifier[]{
                        new ImageTypeSpecifier(image.getColorModel(), image.getSampleModel())});
    }

    /**
     * Returns the number of images, not including thumbnails, available from the current input source.  If the input
     * source contains a PCX document, the return value will be <code>1</code>; otherwise, it will be
     * <code>false</code>.
     *
     * @param allowSearch Ignored.
     * @return <code>0</code> or <code>1</code>, as specified above.
     * @throws IllegalStateException If the input source has not been set, or if the input has been specified with
     *                               <code>seekForwardOnly</code> set to <code>true</code>.
     * @throws IOException           If an error occurs reading the information from the input source.
     * @see #setInput
     */
    public int getNumImages(boolean allowSearch)
            throws IOException
    {
        if (seekForwardOnly)
        {
            throw new IllegalStateException("Seek forward only is set.");
        }
        if (this.input == null)
        {
            throw new IllegalStateException("No input source.");
        }
        if (ensureHeader())
        {
            return 1;
        } else
        {
            return 0;
        }
    }

    /**
     * Returns <code>null</code>; this reader does not support reading meta-data.
     *
     * @return <code>null</code>, always.
     * @throws IOException if an error occurs during reading.
     */
    public IIOMetadata getStreamMetadata()
            throws IOException
    {
        return null;
    }

    /**
     * Returns the width in pixels of the given image within the input source.
     * <p/>
     * <p> If the image can be rendered to a user-specified size, then this method returns the default width.
     *
     * @param imageIndex the index of the image to be queried.
     * @return the width of the image, as an <code>int</code>.
     * @throws IllegalStateException     if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException               if an error occurs reading the width information from the input source.
     */
    public int getWidth(int imageIndex)
            throws IOException
    {
        if (imageIndex != 0) throw new IndexOutOfBoundsException("PCX files contain only one image (index 0).");
        if (ensureHeader())
        {
            return header.getLastX() - header.getFirstX() + 1;
        } else
        {
            throw new IOException("Could not obtain PCX header.");
        }
    }

    /**
     * Reads the image indexed by <code>imageIndex</code> and returns it as a complete <code>BufferedImage</code>, using
     * a supplied <code>ImageReadParam</code>.
     * <p/>
     * <p> The actual <code>BufferedImage</code> returned will be chosen using the algorithm defined by the
     * <code>getDestination</code> method.
     * <p/>
     * <p> Any registered <code>IIOReadProgressListener</code> objects will be notified by calling their
     * <code>imageStarted</code> method, followed by calls to their <code>imageProgress</code> method as the read
     * progresses.  Finally their <code>imageComplete</code> method will be called. <code>IIOReadUpdateListener</code>
     * objects may be updated at other times during the read as pixels are decoded.  Finally,
     * <code>IIOReadWarningListener</code> objects will receive notification of any non-fatal warnings that occur during
     * decoding.
     * <p/>
     * <p> The set of source bands to be read and destination bands to be written is determined by calling
     * <code>getSourceBands</code> and <code>getDestinationBands</code> on the supplied <code>ImageReadParam</code>.  If
     * the lengths of the arrays returned by these methods differ, the set of source bands contains an index larger that
     * the largest available source index, or the set of destination bands contains an index larger than the largest
     * legal destination index, an <code>IllegalArgumentException</code> is thrown.
     * <p/>
     * <p> If the supplied <code>ImageReadParam</code> contains optional setting values not supported by this reader
     * (<i>e.g.</i> source render size or any format-specific settings), they will be ignored.
     *
     * @param imageIndex the index of the image to be retrieved.
     * @param param      an <code>ImageReadParam</code> used to control the reading process, or <code>null</code>.
     * @return the desired portion of the image as a <code>BufferedImage</code>.
     * @throws IllegalStateException     if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IllegalArgumentException  if the set of source and destination bands specified by
     *                                   <code>param.getSourceBands</code> and <code>param.getDestinationBands</code>
     *                                   differ in length or include indices that are out of bounds.
     * @throws IllegalArgumentException  if the resulting image would have a width or height less than 1.
     * @throws IOException               if an error occurs during reading.
     */
    public BufferedImage read(int imageIndex, ImageReadParam param)
            throws IOException
    {
        if (imageIndex != 0) throw new IndexOutOfBoundsException("PCX files contain only one image (index 0).");
        ensureImage();
        BufferedImage target = param.getDestination();
        if (target == null)
        {
            return image;
        } else
        {
            Graphics g = target.getGraphics();
            int x = 0;
            int y = 0;
            if (param.getDestinationOffset() != null)
            {
                x = (int) (param.getDestinationOffset().getX());
                y = (int) (param.getDestinationOffset().getY());
            }
            g.drawImage(image, x, y, null);
            return target;
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE