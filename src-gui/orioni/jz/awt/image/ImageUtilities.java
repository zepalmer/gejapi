package orioni.jz.awt.image;

import orioni.jz.awt.AWTUtilities;
import orioni.jz.awt.HsbPixelFilter;
import orioni.jz.awt.color.ColorComparator;
import orioni.jz.math.MathUtilities;
import orioni.jz.math.MutableInteger;
import orioni.jz.util.DefaultValueHashMap;
import orioni.jz.util.Pair;
import orioni.jz.util.comparator.ReverseComparator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This utilities class is designed to contain common functionality for images.
 *
 * @author Zachary Palmer
 */
public class ImageUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * This {@link java.awt.image.IndexColorModel} represents the "Netscape 216" or "Web" color palette.  This palette
     * uses 8-bit values.  The first 216 (6<sup>3</sup>) values are scaled, evenly distributed colors: each increment
     * increases the blue sample by a normalized value of <code>0.2</code>, each six increments increases the green
     * sample by a normalized <code>0.2</code>, and each thirty-six increments increases the red sample by a normalized
     * <code>0.2</code>. The remaining forty values are evenly distributed representations of grey, increasing by a
     * normalized <code>1/39</code> each index.
     */
    public static final IndexColorModel NETSCAPE_216_MODEL;

    /**
     * The X-offset table for the dithering method.
     */
    private static final int[] DITHERING_X_OFFSETS = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
    /**
     * The Y-offset table for the dithering method.
     */
    private static final int[] DITHERING_Y_OFFSETS = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

    static
    {
        byte[] model_data = new byte[768];
        int index = 0;
        for (int r = 0; r < 6; r++)
        {
            for (int g = 0; g < 6; g++)
            {
                for (int b = 0; b < 6; b++)
                {
                    model_data[index++] = (byte) (255 * r / 6);
                    model_data[index++] = (byte) (255 * g / 6);
                    model_data[index++] = (byte) (255 * b / 6);
                }
            }
        }
        int greys = (model_data.length - index) / 3;
        for (int i = 0; i < greys; i++)
        {
            model_data[index++] = (byte) (255 * i / greys);
            model_data[index++] = (byte) (255 * i / greys);
            model_data[index++] = (byte) (255 * i / greys);
        }

        NETSCAPE_216_MODEL = ImageUtilities.interpretIndexColorModel(model_data, PaletteDataInterpretation.RGB);
    }

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.  Utilities classes are never instantiated.
     */
    private ImageUtilities()
    {
        super();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Loads the provided byte array as an indexed color palette.  The interpretation of this data is determined by the
     * {@link PaletteDataInterpretation} value provided.
     *
     * @param data           The <code>byte[]</code> to interpret.
     * @param interpretation The {@link PaletteDataInterpretation} to use.
     * @return The interpreted {@link java.awt.image.IndexColorModel}.
     * @throws IllegalArgumentException If the length of the provided data array is not divisible by <code>3</code>.
     */
    public static IndexColorModel interpretIndexColorModel(byte[] data, PaletteDataInterpretation interpretation)
            throws IllegalArgumentException
    {
        if (data.length % 3 != 0)
        {
            throw new IllegalArgumentException("Data array is of length " + data.length + ": not divisible by 3.");
        }
        byte[] c1 = new byte[data.length / 3];
        byte[] c2 = new byte[data.length / 3];
        byte[] c3 = new byte[data.length / 3];
        switch (interpretation)
        {
            case RGB:
            case BGR:
                for (int i = 0; i < data.length / 3; i++)
                {
                    c1[i] = data[i * 3];
                    c2[i] = data[i * 3 + 1];
                    c3[i] = data[i * 3 + 2];
                }
                break;
            case RGB_BURST:
            case BGR_BURST:
                for (int i = 0; i < data.length / 3; i++)
                {
                    c1[i] = data[i];
                    c2[i] = data[i + data.length / 3];
                    c3[i] = data[i + data.length * 2 / 3];
                }
                break;
            default:
                throw new IllegalArgumentException("Unrecognized interpretation mode: " + interpretation);
        }

        switch (interpretation)
        {
            case BGR_BURST:
            case BGR:
                byte[] t = c1;
                c1 = c3;
                c3 = t;
        }

        return new IndexColorModel(
                (int) (Math.max(Math.ceil(MathUtilities.log(c1.length, 2)), 1)),
                c1.length,
                c1,
                c2,
                c3);
    }

    /**
     * Buffers the provided {@link java.awt.Image}.  This method has the same effect as {@link
     * ImageUtilities#copyImage(java.awt.Image)} unless the provided image is a {@link java.awt.image.BufferedImage}
     * already; in that case, it is simply returned.
     *
     * @param image The {@link java.awt.Image} to buffer.
     * @return The {@link java.awt.image.BufferedImage}.
     */
    public static BufferedImage bufferImage(Image image)
    {
        if (image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        } else
        {
            return copyImage(image);
        }
    }

    /**
     * Copies the provided {@link java.awt.Image} into a {@link java.awt.image.BufferedImage} of the same size.  This
     * can be used to prevent the image from sharing a raster with another.  Assumes that the returned image should be
     * of the type {@link BufferedImage#TYPE_INT_ARGB}.
     *
     * @param image The image to copy.
     * @return The copied image.
     */
    public static BufferedImage copyImage(Image image)
    {
        return copyImage(image, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Copies the provided {@link java.awt.Image} into a {@link java.awt.image.BufferedImage} of the same size.  This
     * can be used to prevent the image from sharing a raster with another.
     *
     * @param image The image to copy.
     * @param type  The image type to be returned, as one of the {@link BufferedImage}<code>.TYPE_XXX</code> constants.
     * @return The copied image.
     */
    public static BufferedImage copyImage(Image image, int type)
    {
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        bi.getGraphics().drawImage(image, 0, 0, null);
        return bi;
    }

    /**
     * Creates a {@link java.awt.image.BufferedImage} object with the specified {@link java.awt.Image}.  The new image
     * is padded by a number of pixels on each side as specified in the parameters with the specified color.
     *
     * @param image    The base image.
     * @param left     The number of pixels to pad on the left.
     * @param top      The number of pixels to pad on the top.
     * @param right    The number of pixels to pad on the right.
     * @param bottom   The number of pixels to pad on the bottom.
     * @param padColor The {@link java.awt.Color} in which to pad the pixels.
     * @return The padded image.
     */
    public static BufferedImage padImage(Image image, int left, int top, int right, int bottom, Color padColor)
    {
        left = Math.max(0, left);
        top = Math.max(0, top);
        right = Math.max(0, right);
        bottom = Math.max(0, bottom);
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null) + left + right,
                image.getHeight(null) + top + bottom,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.setColor(padColor);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.drawImage(image, left, top, null);
        return bufferedImage;
    }

    /**
     * Retrieves the {@link java.awt.Color} from the provided {@link java.awt.image.IndexColorModel} with the specified
     * index.
     *
     * @param model The {@link java.awt.image.IndexColorModel} in question.
     * @param index The index to retrieve.
     * @return The {@link java.awt.Color} at that index, or <code>null</code> if that index is invalid.
     */
    public static Color getColorInIndexColorModel(IndexColorModel model, int index)
    {
        if (model.isValid(index))
        {
            return new Color(model.getRed(index), model.getGreen(index), model.getBlue(index), model.getAlpha(index));
        } else
        {
            return null;
        }
    }

    /**
     * Dithers the provided {@link java.awt.image.BufferedImage}, reducing the number of unique colors which appear in
     * it.  This operation will select a palette of the colors considered most critical to the appearance of the image
     * and translate and other colors to one of those in the palette.  The size of the palette is specified by the
     * <code>color</code> parameter.  The priority of a color is determined by its frequency of appearance in the image
     * as well as how different it is from the colors immediately surrounding it.
     * <p/>
     * This method creates a mapping entry for each color and sorts the set of entries based upon the aggregated
     * importance value of each color in the image.  Therefore, the amount of work required to accomplish this function
     * is heavily influenced by the number of colors in the image as well as the size of the image itself.
     *
     * @param image            The image to edit.
     * @param colors           The number of colors which will be in the image when the edit is complete.
     * @param restrictivelyMap <code>true</code> if colors should not be mapped to any of the critical colors unless
     *                         absolutely necessary; <code>false</code> for no special treatment.
     * @param colorComparator  The {@link ColorComparator} which should be used to compare colors for contrast.
     * @param criticalColors   Any colors which, if they appear in the image, must be preserved exactly as they are. If
     *                         there are more colors in this list than the count specified in the <code>colors</code>
     *                         variable, an {@link IllegalArgumentException} is thrown.
     * @return The {@link java.util.Set} of {@link java.awt.Color}s that was used as the palette when dithering this
     *         image.
     * @throws IllegalArgumentException If the <code>colors</code> value is less than <code>1</code> or if the number
     *                                  critical colors is greater than <code>colors</code>.
     */
    public static Set<Color> ditherImage(BufferedImage image, int colors, boolean restrictivelyMap,
                                         ColorComparator colorComparator, Color... criticalColors)
    {
        return ditherImage(
                image, colors, 0, 0, image.getWidth(), image.getHeight(), restrictivelyMap, colorComparator,
                criticalColors);
    }

    /**
     * Dithers the provided {@link java.awt.image.BufferedImage}, reducing the number of unique colors which appear in
     * it.  This operation will select a palette of the colors considered most critical to the appearance of the image
     * and translate and other colors to one of those in the palette.  The size of the palette is specified by the
     * <code>color</code> parameter.  The priority of a color is determined by its frequency of appearance in the image
     * as well as how different it is from the colors immediately surrounding it.
     * <p/>
     * The dithering operation performed by this method occurs over the specified rectangle; the rest of the image is
     * not affected or considered, except in that it is used for the purpose of determining contrast.
     * <p/>
     * This method creates a mapping entry for each color and sorts the set of entries based upon the aggregated
     * importance value of each color in the image.  Therefore, the amount of work required to accomplish this function
     * is heavily influenced by the number of colors in the image as well as the size of the image itself.
     *
     * @param image            The image to edit.
     * @param colors           The number of colors which will be in the image when the edit is complete.
     * @param x                The X-offset of the first pixel to dither.
     * @param y                The Y-offset of the first pixel to dither.
     * @param width            The width of the rectangle to dither, beginning at the specified location.
     * @param height           The height of the rectangle to dither, beginning at the specified location.
     * @param restrictivelyMap <code>true</code> if colors should not be mapped to any of the critical colors unless
     *                         absolutely necessary; <code>false</code> for no special treatment.
     * @param colorComparator  The {@link ColorComparator} which should be used to compare colors for contrast.
     * @param criticalColors   Any colors which, if they appear in the image, must be preserved exactly as they are. If
     *                         there are more colors in this list than the count specified in the <code>colors</code>
     *                         variable, an {@link IllegalArgumentException} is thrown.
     * @return The {@link java.util.Set} of {@link java.awt.Color}s that was used as the palette when dithering this
     *         image.
     * @throws IllegalArgumentException  If the <code>colors</code> value is less than <code>1</code> or if the number
     *                                   critical colors is greater than <code>colors</code>.
     * @throws IndexOutOfBoundsException If the specified rectangle is outside of the bounds of the image.
     */
    public static Set<Color> ditherImage(BufferedImage image, int colors, int x, int y, int width, int height,
                                         boolean restrictivelyMap, ColorComparator colorComparator,
                                         Color... criticalColors)
    {
        if (colors < 1) throw new IllegalArgumentException("colors parameter must be at least 1; was " + colors);
        if (criticalColors.length > colors)
        {
            throw new IllegalArgumentException(
                    "More critical colors (" + criticalColors.length +
                    ")were specified than can appear in the image (" + colors + ").");
        }

        // the following map stores the number of instances of a color (first) and the maximum contrast of that color
        // against its surrounding pixels (second)
        HashMap<Integer, MutableInteger> colorInstanceMap = new HashMap<Integer, MutableInteger>();
        DefaultValueHashMap<Integer, Double> colorMaxContrastMap = new DefaultValueHashMap<Integer, Double>(0.0);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // cache the RGB data for the image locally, since it is used so often
        int[][] imageRgbData = new int[imageHeight][imageWidth];
        for (int row = 0; row < imageRgbData.length; row++)
        {
            imageRgbData[row] = image.getRGB(0, row, imageWidth, 1, null, 0, imageWidth);
        }

        if ((width > 1) || (height > 1))
        {
            for (int yOff = y; yOff < y + height; yOff++)
            {
                for (int xOff = x; xOff < x + width; xOff++)
                {
                    int rgbPix = imageRgbData[yOff][xOff];
                    double contrast = 0.0;
                    int counted = 0;
                    for (int i = 0; i < DITHERING_X_OFFSETS.length; i++)
                    {
                        if ((MathUtilities.isBoundedBy(xOff + DITHERING_X_OFFSETS[i], 0, imageWidth - 1)) &&
                            (MathUtilities.isBoundedBy(yOff + DITHERING_Y_OFFSETS[i], 0, imageHeight - 1)))
                        {
                            counted++;
                            contrast += colorComparator.compareColors(
                                    rgbPix,
                                    imageRgbData[yOff + DITHERING_Y_OFFSETS[i]][xOff + DITHERING_X_OFFSETS[i]]);
                        }
                    }
                    contrast /= counted;

                    MutableInteger mi = colorInstanceMap.get(rgbPix);
                    if (mi == null)
                    {
                        mi = new MutableInteger(0);
                        colorInstanceMap.put(rgbPix, mi);
                    }
                    mi.setValue(mi.intValue() + 1);
                    colorMaxContrastMap.put(rgbPix, Math.max(contrast, colorMaxContrastMap.get(rgbPix)));
                }
            }
        } else
        {
            if ((imageWidth > 0) && (imageHeight > 0))
            {
                return new HashSet<Color>(Collections.singleton(new Color(image.getRGB(0, 0))));
            } else
            {
                Set<Color> ret = Collections.emptySet();
                return new HashSet<Color>(ret);
            }
        }

        if (colorInstanceMap.size() < colors)
        {
            // no work needs to be done
            Set<Color> ret = new HashSet<Color>();
            for (int rgb : colorInstanceMap.keySet())
            {
                ret.add(new Color(rgb, true));
            }
            return ret;
        }

        // now is the part where we cheat on behalf of the critical colors
        for (Color c : criticalColors)
        {
            colorMaxContrastMap.put(c.getRGB(), 1.1);
        }

        // Now sort the entries by order of aggregate value
        //noinspection unchecked
        Pair<Double, Integer>[] colorSortingArray = new Pair[colorInstanceMap.size()];
        int index = 0;
        for (Integer rgbPix : colorInstanceMap.keySet())
        {
            colorSortingArray[index++] = new Pair<Double, Integer>(
                    colorMaxContrastMap.get(rgbPix) * Math.sqrt(colorInstanceMap.get(rgbPix).intValue()),
                    rgbPix);
        }
        Arrays.sort(
                colorSortingArray,
                new ReverseComparator<Pair<Double, Integer>>(new Pair.FirstElementComparator<Double, Integer>()));
        Set<Color> palette = new HashSet<Color>();
        Set<Integer> paletteRgbs = new HashSet<Integer>();
        for (int i = 0; i < colors; i++)
        {
            paletteRgbs.add(colorSortingArray[i].getSecond());
            palette.add(new Color(colorSortingArray[i].getSecond(), true));
        }
        Set<Integer> restrictiveRgbs;
        if (restrictivelyMap)
        {
            restrictiveRgbs = new HashSet<Integer>();
            for (Color c : criticalColors)
            {
                restrictiveRgbs.add(c.getRGB());
            }
        } else
        {
            restrictiveRgbs = new HashSet<Integer>();
        }

        // Now replace other colors
        for (int yOff = y; yOff < y + height; yOff++)
        {
            for (int xOff = x; xOff < x + width; xOff++)
            {
                int rgbPix = imageRgbData[yOff][xOff];
                if (!paletteRgbs.contains(rgbPix))
                {
                    int bestMatch = 0;
                    double distance = 2.2;
                    for (int candidate : paletteRgbs)
                    {
                        double candidateDistance;
                        if (restrictiveRgbs.contains(candidate))
                        {
                            candidateDistance = 1.1 + colorComparator.compareColors(candidate, rgbPix);
                        } else
                        {
                            candidateDistance = colorComparator.compareColors(candidate, rgbPix);
                        }
                        if (candidateDistance < distance)
                        {
                            bestMatch = candidate;
                            distance = candidateDistance;
                        }
                    }
                    image.setRGB(xOff, yOff, bestMatch);
                }
            }
        }

        return palette;
    }

    /**
     * Replaces all instances of a specific color in the provided image with another color.  This method makes use of
     * the {@link java.awt.image.BufferedImage#getRGB(int, int, int, int, int[], int, int)} method and directly compares
     * against the source color's RGB value.
     * <p/>
     * As a special case, if the source's alpha value is <code>0</code>, any completely transparent pixel is replaced.
     * This operation is performed under the reasoning that all colors with a <code>0</code> alpha are equivalent.
     *
     * @param image  The {@link java.awt.image.BufferedImage} to adjust.
     * @param source The color to replace.
     * @param target The color with which to replace it.
     */
    public static void replaceAll(BufferedImage image, Color source, Color target)
    {
        int[] rgbdata = new int[image.getWidth() * image.getHeight()];
        int sourceRgb = source.getRGB();
        int targetRgb = target.getRGB();
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbdata, 0, image.getWidth());
        if ((sourceRgb & 0xFF000000) == 0)
        {
            for (int i = 0; i < rgbdata.length; i++)
            {
                if ((rgbdata[i] & 0xFF000000) == 0) rgbdata[i] = targetRgb;
            }
        } else
        {
            for (int i = 0; i < rgbdata.length; i++)
            {
                if (rgbdata[i] == sourceRgb) rgbdata[i] = targetRgb;
            }
        }
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgbdata, 0, image.getWidth());
    }

    /**
     * Uses the {@link ImageIO} framework to save the provided file.  This method is distinct from the {@link
     * ImageIO#write(java.awt.image.RenderedImage, String, java.io.File)} in that, if the first attempt fails, the image
     * is copied to an image of type {@link BufferedImage#TYPE_INT_RGB} to eliminate transparency.  Some image file
     * formats (such as .bmp) do not contain alpha values and will therefore report to {@link ImageIO} that they cannot
     * write the provided image rather than transforming the image so that it can be written.
     * <p/>
     * This method also deletes the image if the write fails.
     *
     * @param image      The {@link RenderedImage} to save.
     * @param formatName The informal format name in which to save the image.
     * @param file       The file to which to save the image.
     * @return <code>true</code> if the save was successful; <code>false</code> otherwise.
     */
    public static boolean writeImage(BufferedImage image, String formatName, File file)
            throws IOException
    {
        if (!ImageIO.write(image, formatName, file))
        {
            BufferedImage copy = ImageUtilities.copyImage(image, BufferedImage.TYPE_INT_RGB);
            if (!ImageIO.write(copy, formatName, file))
            {
                file.delete();
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two images to determine if they are identical.
     *
     * @param a The first image.
     * @param b The second image.
     * @return <code>true</code> if and only if the images are the same size and every pixel in each image is the same.
     */
    public static boolean imagesAreEqual(BufferedImage a, BufferedImage b)
    {
        if ((a.getWidth() != b.getWidth()) || (a.getHeight() != b.getHeight()))
        {
            return false;
        }
        for (int y = 0; y < a.getHeight(); y++)
        {
            for (int x = 0; x < a.getWidth(); x++)
            {
                if (a.getRGB(x, y) != b.getRGB(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * Determines the "average" color in an image.  Each pixel in the image will be blended with the other pixels,
     * weighted by its alpha value.  The resulting color is returned.  Note that this value will always be opaque, even
     * if the colors in the image aren't.  This function is useful for determining the general color of a simple or
     * small image.
     *
     * @param image The image to analyze.
     * @return The average color contained in the image.
     */
    public static Color getAverageColorIn(BufferedImage image)
    {
        final int height = image.getHeight();
        final int width = image.getWidth();
        double redTotal = 0;
        double greenTotal = 0;
        double blueTotal = 0;
        double alphaTotal = 0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int rgb = image.getRGB(x, y);
                int alpha = rgb >>> 24;
                redTotal += ((rgb >>> 16) & 0xFF) * alpha;
                greenTotal += ((rgb >>> 8) & 0xFF) * alpha;
                blueTotal += (rgb & 0xFF) * alpha;
                alphaTotal += alpha;
            }
        }
        return new Color(
                (int) (redTotal / alphaTotal),
                (int) (greenTotal / alphaTotal),
                (int) (blueTotal / alphaTotal));
    }

    /**
     * Adjusts each pixel in the provided image using the provided HSB modifiers.  Each modifier should be within the
     * range <code>[-1.0, 1.0]</code>.  Assumes that all pixels in the image will be affected.
     *
     * @param image      The image to use.
     * @param hue        The amount by which the hue should be changed.  As hue is a circular value, providing
     *                   <code>-1.0</code> will have the same effect as providing <code>1.0</code>.
     * @param saturation The amount by which the saturation should be changed.  A value of <code>-1.0</code> completely
     *                   removes all saturation (effectively grayscaling the image).
     * @param brightness The amount by which the brightness should be changed.
     * @return The modified version of the image.
     */
    public static BufferedImage adjustHSB(BufferedImage image, double hue, double saturation, double brightness)
    {
        return adjustHSB(image, hue, saturation, brightness, null);
    }

    /**
     * Adjusts each pixel in the provided image using the provided HSB modifiers.  Each modifier should be within the
     * range <code>[-1.0, 1.0]</code>.
     *
     * @param image          The image to use.
     * @param hue            The amount by which the hue should be changed.  As hue is a circular value, providing
     *                       <code>-1.0</code> will have the same effect as providing <code>1.0</code>.
     * @param saturation     The amount by which the saturation should be changed.  A value of <code>-1.0</code>
     *                       completely removes all saturation (effectively grayscaling the image).
     * @param brightness     The amount by which the brightness should be changed.
     * @param hsbPixelFilter The HSB pixel filter which is used to determine whether or not a given pixel is affected in
     *                       this manner.  If <code>null</code>, all pixels are modified.
     * @return The modified version of the image.
     */
    public static BufferedImage adjustHSB(BufferedImage image, double hue, double saturation, double brightness,
                                          HsbPixelFilter hsbPixelFilter)
    {
        BufferedImage ret = copyImage(image);

        hue = MathUtilities.bound(hue, -1, 1);
        saturation = MathUtilities.bound(saturation, -1, 1);
        brightness = MathUtilities.bound(brightness, -1, 1);

        for (int y = 0; y < ret.getHeight(); y++)
        {
            for (int x = 0; x < ret.getWidth(); x++)
            {
                ret.setRGB(x, y, AWTUtilities.adjustHSB(ret.getRGB(x, y), hue, saturation, brightness, hsbPixelFilter));
            }
        }

        return ret;
    }

    /**
     * Redraws the given indexed color image using the provided {@link IndexColorModel}.  This is distinct from the
     * effect of {@link RestrictableIndexColorModel#redraw(java.awt.image.BufferedImage)} in that it does not perform
     * any color-matching operations; this method simply interprets the same raster using a different model.
     * <p/>
     * If <code>image</code> is not of type {@link BufferedImage#TYPE_BYTE_INDEXED}, the behavior of this method is
     * unspecified.
     *
     * @param image The {@link BufferedImage} to redraw.
     * @param model The {@link IndexColorModel} to use.
     * @return A new {@link BufferedImage} which is redrawn using the provided {@link IndexColorModel}.
     */
    public static BufferedImage redrawIndexedImage(BufferedImage image, IndexColorModel model)
    {
        int[] samples = image.getRaster().getPixels(0, 0, image.getWidth(), image.getHeight(), (int[]) null);
        BufferedImage ret =
                new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, model);
        ret.getRaster().setPixels(0, 0, ret.getWidth(), ret.getHeight(), samples);
        return ret;
    }

    /**
     * Changes the alpha values for all pixels in a given image, increasing or decreasing their transparency by a
     * specified percentage.  The provided image is then returned.
     *
     * @param image        The image to manipulate.
     * @param transparency The percentage of transparency to impose, as a value in the range <code>[-1.0, 1.0]</code>. A
     *                     negative value indicates that transparency should be decreased (the image made more opaque)
     *                     whereas a positive value indicates that transparency should be increased.
     * @return The manipulated image.
     * @throws IllegalArgumentException If <code>transparency</code> is not within bounds.
     */
    public static BufferedImage adjustTransparency(BufferedImage image, double transparency)
    {
        if ((transparency < -1.0) || (transparency > 1.0))
        {
            throw new IllegalArgumentException("Transparency value " + transparency + " not within range [-1.0, 1.0]");
        }
        if (transparency > 0)
        {
            double opacity = 1 - transparency;
            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    int rgb = image.getRGB(x, y);
                    image.setRGB(x, y, (rgb & 0x00FFFFFF) | ((int) ((rgb >>> 24) * opacity) << 24));
                }
            }
        } else
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    int rgb = image.getRGB(x, y);
                    image.setRGB(
                            x, y,
                            (rgb & 0x00FFFFFF) | (Math.min((int) (255 - ((rgb >>> 24) * transparency)), 255) << 24));
                }
            }
        }
        return image;
    }

    /**
     * Draws the provided image over a background of the specified color.  This is useful for filling the transparent
     * pixels of an image.
     *
     * @param image The image to redraw.
     * @param color The {@link Color} to use as the background.
     * @return The redrawn image.
     */
    public static BufferedImage drawOver(Image image, Color color)
    {
        BufferedImage ret = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = ret.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
        g.drawImage(image, 0, 0, null);
        return ret;
    }

    /**
     * Retrieves a {@link BufferedImage} containing the appearance of the provided {@link Icon}.
     *
     * @param icon The {@link Icon} to retrieve in {@link BufferedImage} form.
     * @return The rendered {@link BufferedImage}.
     */
    public static BufferedImage getIconImage(Icon icon)
    {
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, image.getGraphics(), 0, 0);
        return image;
    }

    /**
     * Blends every pixel in the provided {@link BufferedImage} with the provided color at the provided proportion.
     *
     * @param image  The image to change.
     * @param color  The {@link Color} to use in blending.
     * @param weight The weight of the blend.  A value of <code>0.00</code> will leave the image unchanged whereas a
     *               value of <code>1.00</code> will completely discard the old image data.  A value of
     *               <code>0.50</code> will blend evenly.
     * @return <code>image</code>, after it is changed.
     */
    public static BufferedImage maskImage(BufferedImage image, Color color, double weight)
    {
        int rgb = color.getRGB();
        weight = MathUtilities.bound(weight, 0.00, 1.00);
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                image.setRGB(x, y, AWTUtilities.blendColors(image.getRGB(x, y), rgb, weight));
            }
        }
        return image;
    }

    /**
     * Creates a snapshot of the provided component as a {@link BufferedImage}.
     *
     * @param component The {@link Component} for which a snapshot is required.
     * @return A snapshot of that component, or <code>null</code> if the component is either of zero height or width.
     */
    public static BufferedImage createComponentSnapshot(Component component)
    {
        if ((component.getHeight() < 1) || (component.getWidth() < 1)) return null;
        BufferedImage image =
                new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        component.paint(image.getGraphics());
        return image;
    }
}

// END OF FILE