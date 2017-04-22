package orioni.jz.awt.image;

import orioni.jz.awt.color.SampleDifferenceColorComparator;
import orioni.jz.util.BitMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.math.BigInteger;
import java.util.*;

/**
 * This {@link IndexColorModel} extension provides additional functionality, such as the ability to quickly determine to
 * which index a {@link Color} maps and the ability to redraw a {@link BufferedImage} using this model.
 *
 * @author Zachary Palmer
 */
public class RestrictableIndexColorModel extends IndexColorModel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The maximum size for a find-map in a {@link RestrictableIndexColorModel}.
     */
    protected static final int MAXIMUM_FIND_MAP_SIZE = 65536;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Set} of sRGB values which are to be "restrictively mapped."  No colors in redrawn images will be
     * mapped to the contained sRGB values unless they match those values exactly.
     */
    protected Set<Integer> restrictedRgbs;
    /**
     * A bitmap containing the incides which are to be "restrictively mapped."  No colors in redrawn images will be mapped to
     * the indicated indices unless the color matches the color value of that index exactly.
     */
    protected BitMap restrictedIndices;
    /**
     * The cache used to accelerate the results of {@link RestrictableIndexColorModel#find(int)}.
     */
    protected Map<Integer, Integer> findMap;
    /**
     * Contains the most transparent index in this color model.  This is not necessarily the same as the "transparent
     * index" contained by the superclass, as this value may be partially or wholly opaque.  In the event of a tie, the
     * color with the lowest index value wins.
     */
    protected int mostTransparent;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,byte[],byte[],byte[])
     */
    public RestrictableIndexColorModel(int bits, int size,
                                       byte[] r, byte[] g, byte[] b)
    {
        super(bits, size, r, g, b);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,byte[],byte[],byte[],int)
     */
    public RestrictableIndexColorModel(int bits, int size,
                                       byte[] r, byte[] g, byte[] b, int trans)
    {
        super(bits, size, r, g, b, trans);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,byte[],byte[],byte[],byte[])
     */
    public RestrictableIndexColorModel(int bits, int size,
                                       byte[] r, byte[] g, byte[] b, byte[] a)
    {
        super(bits, size, r, g, b, a);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,byte[],int,boolean)
     */
    public RestrictableIndexColorModel(int bits, int size, byte[] cmap, int start,
                                       boolean hasalpha)
    {
        super(bits, size, cmap, start, hasalpha);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,byte[],int,boolean,int)
     */
    public RestrictableIndexColorModel(int bits, int size, byte[] cmap, int start,
                                       boolean hasalpha, int trans)
    {
        super(bits, size, cmap, start, hasalpha, trans);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,int[],int,boolean,int,int)
     */
    public RestrictableIndexColorModel(int bits, int size,
                                       int[] cmap, int start,
                                       boolean hasalpha, int trans, int transferType)
    {
        super(bits, size, cmap, start, hasalpha, trans, transferType);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see IndexColorModel#IndexColorModel(int,int,int[],int,int,BigInteger)
     */
    public RestrictableIndexColorModel(int bits, int size, int[] cmap, int start,
                                       int transferType, BigInteger validBits)
    {
        super(bits, size, cmap, start, transferType, validBits);
        initialize();
    }

    /**
     * General constructor.
     *
     * @param model An {@link IndexColorModel} on which to base this model.
     */
    public RestrictableIndexColorModel(IndexColorModel model)
    {
        super(
                model.getPixelSize(), model.getMapSize(), getRGBs(model), 0, DataBuffer.TYPE_BYTE,
                model.getValidPixels());
        initialize();
    }

    /**
     * Used by the different constructors to provide a common construction point for the {@link RestrictableIndexColorModel}.
     */
    private void initialize()
    {
        restrictedRgbs = new HashSet<Integer>();
        restrictedIndices = new BitMap(getMapSize());
        findMap = new HashMap<Integer, Integer>();
        mostTransparent = 0;

        int bestOpacity = 0xFF;
        for (int i = 0; i < getMapSize(); i++)
        {
            if (isValid(i))
            {
                int currentOpacity = getRGB(i) >>> 24;
                if (currentOpacity < bestOpacity)
                {
                    bestOpacity = currentOpacity;
                    mostTransparent = i;
                }
            }
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a {@link Color} to be restrictively mapped by this {@link RestrictableIndexColorModel}.
     *
     * @param color The {@link Color} to restrictively map.
     */
    public void addRestrictedColor(Color color)
    {
        addRestrictedColor(color.getRGB());
    }

    /**
     * Adds an RGB value to be restrictively mapped by this {@link RestrictableIndexColorModel}.
     *
     * @param rgb The sRGB value to restrictively map.
     */
    public void addRestrictedColor(int rgb)
    {
        restrictedRgbs.add(rgb);
    }

    /**
     * Removes a {@link Color} from the restrictive mapping of this {@link RestrictableIndexColorModel}.
     *
     * @param color The {@link Color} to map normally.
     */
    public void removeRestrictedColor(Color color)
    {
        removeRestrictedColor(color.getRGB());
    }

    /**
     * Removes an RGB value from the restrictive mapping of this {@link RestrictableIndexColorModel}.
     *
     * @param rgb The sRGB value to map normally.
     */
    public void removeRestrictedColor(int rgb)
    {
        restrictedRgbs.remove(rgb);
    }

    /**
     * Adds an index to the restrictive mapping of this {@link RestrictableIndexColorModel}.
     *
     * @param index The index to add.
     */
    public void addRestrictedIndex(int index)
    {
        restrictedIndices.setBit(index, true);
    }

    /**
     * Removes an index from the restrictive mapping of this {@link RestrictableIndexColorModel}.
     *
     * @param index The index to remove.
     */
    public void removeRestrictedIndex(int index)
    {
        restrictedIndices.setBit(index, false);
    }

    /**
     * Removes all restrictions from this {@link RestrictableIndexColorModel}.
     */
    public void removeRestrictions()
    {
        restrictedIndices = new BitMap(getMapSize());
        restrictedRgbs.clear();
    }

    /**
     * Redraws the provided {@link BufferedImage} using this {@link RestrictableIndexColorModel}.  Any restricted colors
     * which have been applied to this model apply.
     *
     * @param image The {@link BufferedImage} to redraw.
     * @return A {@link BufferedImage} drawn using this model.
     */
    public BufferedImage redraw(BufferedImage image)
    {
        BufferedImage ret = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, this);
        int[] samples = new int[image.getWidth()*image.getHeight()];
        image.getRGB(0,0,image.getWidth(), image.getHeight(), samples, 0, image.getWidth());
        for (int i=0;i<samples.length;i++)
        {
            samples[i] = find(samples[i]);
        }
        ret.getRaster().setPixels(0,0,image.getWidth(),image.getHeight(),samples);
        //ret.setRGB(0,0,image.getWidth(),image.getHeight(),samples,0,image.getWidth());
        return ret;
    }

    /**
     * Derives a new palette from this one.  The new palette will have the specified indices enabled.
     *
     * @param indices The indices to enable.
     * @throws IndexOutOfBoundsException If one of the indices is out of the range of this model's map size.
     */
    public RestrictableIndexColorModel deriveWithValidIndices(int... indices)
            throws IndexOutOfBoundsException
    {
        BigInteger bi = this.getValidPixels();
        for (int index : indices)
        {
            if ((index < 0) || (index >= getMapSize()))
            {
                throw new IndexOutOfBoundsException("Index " + index + " is not within [0," + getMapSize() + ")");
            }
            bi = bi.setBit(index);
        }
        RestrictableIndexColorModel ret = new RestrictableIndexColorModel(
                getPixelSize(), getMapSize(), getRGBs(this), 0, getTransferType(), bi);
        for (Integer rgb : restrictedRgbs) ret.addRestrictedColor(rgb);
        for (int i=0;i<getMapSize();i++)
        {
            if (restrictedIndices.getBit(i)) ret.addRestrictedIndex(i);
        }
        return ret;
    }

    /**
     * Derives a new palette from this one.  The new palette will have the specified indices enabled.
     *
     * @param indices The indices to enable.
     * @throws IndexOutOfBoundsException If one of the indices is out of the range of this model's map size.
     */
    public RestrictableIndexColorModel deriveWithInvalidIndices(int... indices)
            throws IndexOutOfBoundsException
    {
        BigInteger bi = this.getValidPixels();
        for (int index : indices)
        {
            if ((index < 0) || (index >= getMapSize()))
            {
                throw new IndexOutOfBoundsException("Index " + index + " is not within [0," + getMapSize() + ")");
            }
            bi = bi.clearBit(index);
        }
        RestrictableIndexColorModel ret = new RestrictableIndexColorModel(
                getPixelSize(), getMapSize(), getRGBs(this), 0, getTransferType(), bi);
        for (Integer rgb : restrictedRgbs) ret.addRestrictedColor(rgb);
        for (int i=0;i<getMapSize();i++)
        {
            if (restrictedIndices.getBit(i)) ret.addRestrictedIndex(i);
        }
        return ret;
    }

    /**
     * Derives a new palette from this one.  The new palette will have a transparent entries at the provided indices.
     * @param indices The indices at which a transparent entry should be generated.
     * @throws IndexOutOfBoundsException If the index is out of the range of this model's map size.
     */
    public RestrictableIndexColorModel deriveWithTransparentInices(int... indices)
    {
        int[] rgbs = getRGBs(this);
        for (int index : indices)
        {
            if ((index < 0) || (index >= getMapSize()))
            {
                throw new IndexOutOfBoundsException("Index " + index + " is not within [0," + getMapSize() + ")");
            }
            rgbs[index] = 0x00000000;
        }
        RestrictableIndexColorModel ret = new RestrictableIndexColorModel(
                getPixelSize(), getMapSize(), rgbs, 0, getTransferType(), getValidPixels());
        for (Integer rgb : restrictedRgbs) ret.addRestrictedColor(rgb);
        for (int i=0;i<getMapSize();i++)
        {
            if (restrictedIndices.getBit(i)) ret.addRestrictedIndex(i);
        }
        return ret;
    }

    /**
     * Retrieves the index of the color in this model which most closely matches the provided {@link Color}. Restrictive
     * mapping applies.
     *
     * @param color The {@link Color} to find.
     * @return The index which most closely matches that color.
     */
    public int find(Color color)
    {
        return find(color.getRGB());
    }

    /**
     * Retrieves the index of the color in this model which most closely matches the provided sRGB value. Restrictive
     * mapping applies.
     *
     * @param rgb The sRGB value to find.
     * @return The index which most closely matches that color.
     */
    public int find(int rgb)
    {
        Integer index = findMap.get(rgb);
        if (index == null)
        {
            double bestDistance = 1.2;
            index = 0;
            for (int i = 0; i < this.getMapSize(); i++)
            {
                SampleDifferenceColorComparator colorComparator = SampleDifferenceColorComparator.SINGLETON;
                if (isValid(i))
                {
                    double currentDistance = colorComparator.compareColors(rgb, this.getRGB(i));
                    if (((restrictedRgbs.contains(rgb)) || (restrictedIndices.getBit(i))) &&
                        ((rgb & 0xFF000000) != 0))
                    {
                        currentDistance = 1.1;
                    }
                    if (currentDistance < bestDistance)
                    {
                        bestDistance = currentDistance;
                        index = i;
                    }
                }
            }
            findMap.put(rgb, index);
            if (findMap.size() > MAXIMUM_FIND_MAP_SIZE)
            {
                findMap.clear();
                findMap.put(rgb, index);
            }
        }
        return index;
    }

    /**
     * Retrieves the most transparent index in this {@link RestrictableIndexColorModel}.  This will be the index with
     * the lowest alpha value.  In the event that multiple indices share the best alpha value, the lowest index will be
     * returned.
     *
     * @return The most transparent index in this model.
     */
    public int getMostTransparentIndex()
    {
        return mostTransparent;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves the RGB component array from the provided {@link IndexColorModel}.
     *
     * @param model The model from which to retrieve the array.
     * @return The red component array from the provided model.
     */
    private static int[] getRGBs(IndexColorModel model)
    {
        int[] rgbs = new int[model.getMapSize()];
        model.getRGBs(rgbs);
        return rgbs;
    }
}

// END OF FILE