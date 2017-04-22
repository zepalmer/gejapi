package orioni.jz.awt.swing;

import orioni.jz.util.AbstractCache;
import orioni.jz.util.Cache;
import orioni.jz.util.SynchronizedCache;
import orioni.jz.util.Utilities;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link TextConsoleFont} uses bitmapped images for the display of console cells.
 *
 * @author Zachary Palmer
 */
public class BitmappedTextConsoleFont extends TextConsoleFont
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * This array is the gzipped data for the default font.  After it is decompressed, it represents an 8x14 bitmap
     * font.  Each set of fourteen bytes represents a character, each byte representing a row and each bit representing
     * a single pixel in the bitmap.  The highest-order bits in the bytes are considered leftmost in the bitmap.
     */
    private static final byte[] DEFAULT_BITMAP_DATA = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0, 109, 87, 63, 107, 35,
            71, 20, 95, 28, -40, 106, -47, 57, -35, 16, 11, -69, 73, 113, -27, -110, -128, -68, -124, 69, 10, 71, -66,
            66, -102, 84, -53, 57, 76, -82, 24, -126, 42, 69, -112, 97, -17, -36, 92, -107, 47, -32, 38, 93, -102, 124,
            7, -61, 98, -121, -31, -118, -59, -91, 16, 72, 17, -82, -92, -26, 56, 12, 1, -97, 10, 49, -101, 55, 111,
            -34, -52, -50, -8, -18, -55, 66, -13, -37, -9, -10, -3, -99, -9, 102, -100, 36, 49, -43, -105, 127, 93, 94,
            94, 95, 93, -42, 22, 117, -85, -82, -69, -35, 117, 22, 37, 66, 3, -55, -30, -40, -94, -29, 66, -10, -128,
            -107, -27, 110, -73, 99, -84, 36, 84, 119, 93, -19, -112, 101, 51, 92, 116, -122, 118, -73, -96, -45, -112,
            121, 82, -14, 23, 47, 120, -23, 120, -73, 87, -41, -41, 87, -73, -106, 119, 58, -8, -22, -101, 121, -37,
            -74, -13, -60, -54, 113, 94, 50, 80, -118, 104, -14, -19, 36, -49, -13, -23, -61, 61, -94, -41, 23, -81, 47,
            46, 46, 126, -39, 109, 27, 52, -58, 86, -27, -82, 92, -111, -28, -101, -26, 126, -81, -9, -9, -51, 27, 68,
            71, -23, 96, -84, -57, -125, -12, -56, -7, -55, 24, -85, -55, 55, -114, 4, 63, 86, -25, 106, -75, -6, -3, 4,
            -56, 0, -87, -86, 66, 40, 37, -118, 76, 73, -97, 43, 72, 70, -84, -91, -18, 17, 35, -21, 118, -23, 44, 0,
            -50, 116, -58, -68, -122, -68, -46, 85, -34, 39, -65, 105, 26, -19, -63, 115, -95, -59, 115, 7, -114, -117,
            66, 74, -19, -104, 80, 4, 89, -72, -52, -57, 4, -119, 46, -63, -76, -75, 14, -47, 124, 29, -16, 4, 104, -60,
            47, -6, 37, -43, 77, 35, -45, -73, 74, -110, -89, 55, 42, 99, 57, 87, -72, 46, -124, 40, 102, -21, -74, -99,
            -95, -109, 121, 94, 5, 90, 64, -54, 16, -53, 108, 4, 44, 51, -60, 92, 20, -68, -20, 74, -34, 123, -29, -21,
            -43, 63, -15, -94, -119, 78, -98, 16, -55, 30, -91, -96, -81, -94, -126, 65, -22, -17, 54, -113, 91, 69,
            121, 103, -59, -36, 102, -108, 120, 40, -86, -76, 67, 105, -103, -90, 36, -103, 13, 75, -47, -22, 44, 59,
            -75, -74, 32, -73, 7, -49, 43, 42, 64, -54, -23, -44, -88, 5, -120, -76, -64, 115, -49, 51, -85, 58, 77,
            -77, 121, -32, -95, 15, -119, -112, 125, 15, 61, -127, 116, -92, 46, -102, 26, -1, -112, -16, -71, -31, 59,
            -99, -103, -81, -111, 65, -101, -51, 102, -35, 88, 123, -57, 102, -101, 105, 48, -118, -24, 0, 21, -108,
            -16, 61, 32, 42, -7, 13, 68, 113, 99, -101, 37, -39, 11, -36, -82, 98, 111, 99, -32, 47, 95, -51, 95, -67,
            -28, 58, 64, 85, -11, -48, -65, -73, 81, -4, 59, 68, -96, 27, 13, 88, 11, -91, -35, -96, -44, -87, -89, 88,
            76, -41, 113, 91, 46, -60, 92, 8, -66, 69, -12, 80, 25, 114, 22, -44, 7, -83, 23, 94, -117, -38, 62, -22,
            -51, -99, 67, 38, 4, 101, -102, -91, -113, -95, 114, -66, -104, 124, -86, -59, 70, 102, 3, -49, -125, 56,
            -74, -114, 87, 65, -125, 81, -26, -21, -6, -89, -64, 51, 69, 36, 3, 36, -88, 5, 80, -27, 66, 75, 65, 72, 20,
            -123, 113, 1, 17, -57, -87, -31, -76, 104, -11, 7, 20, -31, -122, -10, 75, -103, 91, -78, 60, -104, 19, -45,
            98, -24, 38, 67, -103, 89, 42, -87, 38, -55, 103, -87, -125, -26, 96, 79, 31, -50, 51, 73, -99, -109, -36,
            87, -43, -36, -108, -55, -49, 12, -87, -102, -122, 98, 24, -126, 114, -47, 58, 73, -28, 105, -57, 43, -60,
            -49, -43, -125, -49, 25, -48, 12, 4, 101, -122, 117, 1, -99, 98, -58, 93, -50, 96, 19, 21, 125, -106, -46,
            52, 25, -92, 64, 28, -9, 8, 72, 114, 40, 32, 73, 22, 81, -91, -127, -34, -21, -59, 98, -31, 3, 91, -37, -31,
            -25, 125, -15, -71, 38, -98, -12, -34, 88, 95, -88, -81, -110, -11, -116, 7, 126, 74, 53, 29, -46, 123, -57,
            121, 126, -128, -28, -114, -122, -114, -41, -74, 97, -76, -74, 48, 14, 97, 1, -123, 71, -94, 8, 82, -82,
            -80, 5, 51, -69, -49, 19, -35, -62, -108, -78, -11, 27, 64, 52, 83, -8, 14, 40, 19, -40, 83, -44, 85, -8,
            -36, -16, -83, -53, -21, 36, 34, -37, 101, 118, -115, -35, 1, 77, -107, -91, 18, -99, 12, -4, 52, 99, -83,
            -81, 10, -68, 20, -42, 22, 36, 123, 4, 13, 30, -96, 2, -74, 126, 32, 105, 108, 84, -58, 0, 109, -90, -80,
            -46, -96, -91, 71, 70, 75, -64, -125, 114, -12, -75, 101, 101, -120, -116, 100, -113, -108, 114, 99, -125,
            -116, -9, -56, -52, 28, -51, 43, 89, 113, 55, 109, -37, -39, -88, 94, 46, 127, -59, -11, 24, 54, -96, -122,
            104, -17, 18, -17, 89, -48, 99, 1, -78, -98, 57, -108, -49, -61, 44, 25, 94, 80, 91, 120, -113, 42, 54, 55,
            -64, 14, 3, 59, 11, -120, 69, 90, -64, 121, -40, 58, -90, 59, 109, -50, -20, -106, -33, 30, 40, 118, 115,
            -42, -45, -23, -79, 111, -37, -3, 63, -19, -90, 109, 113, 79, 12, 78, 24, -98, 43, -116, 45, -89, 54, -64,
            62, -41, -90, 98, 65, -50, -128, -41, 123, 109, 80, -32, 39, -20, 9, -65, -25, 97, -19, 70, 24, -106, 75,
            -120, -79, -97, -35, 120, 24, 38, -3, -87, 15, -57, 4, 28, -120, 42, -68, 7, -64, 84, -18, -9, -106, -122,
            -2, -61, 5, 116, 122, -69, -52, -85, -11, -37, -116, -115, 29, -30, 119, 127, -114, -111, -115, 123, -75,
            44, 125, 7, -116, -60, 82, -116, -68, 14, 88, -117, 37, -82, -66, -4, 33, -4, -4, -8, 119, -8, -7, -9, -73,
            -16, -61, 62, 71, -5, 104, 77, 104, 100, -23, -47, -2, 56, -73, 3, 20, 75, 62, -90, 36, 25, 19, 5, 27, -14,
            30, 83, -38, 100, -124, 9, 57, -21, 73, 72, -95, 103, -116, -99, 5, -110, -116, 117, -111, 100, 23, 75, 6,
            -105, 43, 51, -128, -93, -9, 34, -71, -77, 40, -38, -13, -64, -49, -13, 124, 18, -24, -97, -28, 33, -17, 99,
            18, 90, -17, -110, -113, -47, 123, -25, 65, -20, -99, -109, -92, -9, 72, -110, 34, 8, 121, -93, 81, -84,
            -77, -117, 99, -120, -46, 58, 9, 34, 58, 115, 121, 65, 58, 123, 18, -5, 36, 122, -81, 11, -83, -77, 56, 103,
            113, -26, 73, 75, 23, -111, -13, -59, -46, 67, 68, -49, 34, -118, 95, 64, -102, -83, -105, -53, -75, -101,
            117, 82, -63, -51, -22, -48, 52, -33, 39, -26, -68, -123, -13, -82, -23, -37, -61, -36, 63, -123, -80, 115,
            94, 43, 24, 28, -2, -18, 6, 4, 83, 105, -71, -100, 18, -32, -10, -56, 105, -100, -127, 62, -16, -102, -47,
            127, 2, -120, 104, -46, -47, 93, 3, -121, 13, 24, -8, -128, -24, 20, 110, 93, 99, -50, -71, 63, -15, -22,
            -43, -54, 117, -11, 23, 41, -128, -1, 106, 50, 48, -52, 43, 24, -111, 85, 62, 116, 33, 88, 34, -89, -35, 31,
            21, 6, -121, -110, -115, -97, -82, 117, 52, 43, -24, 2, 72, 104, 112, 114, -14, 73, 59, -6, -8, 64, 69, -35,
            -33, 35, -51, -4, 113, 71, 20, 78, -101, -104, -98, 94, -95, -23, -9, 25, 94, 80, -34, -117, 18, -67, 94,
            -10, -103, 5, -102, -62, -32, 121, 23, -106, 93, 34, 37, -97, -48, -1, 40, -65, -88, -40, 0, 14, 0, 0};

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The width of this font.
     */
    protected int width;
    /**
     * The height of this font.
     */
    protected int height;
    /**
     * The bitmap for this font.
     */
    protected boolean[] bitmap;
    /**
     * The image cache for this font.
     */
    protected Cache<CacheKey, BufferedImage> cache;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Uses a default font.
     */
    public BitmappedTextConsoleFont()
    {
        this(8, 14, getDefaultBitmap());
    }

    /**
     * General constructor.
     *
     * @param width  The width of this bitmapped font.
     * @param height The height of this bitmapped font.
     * @param bitmap A <code>boolean[]</code> which represents the font to use.  The bitmap must be exactly of a size
     *               equal to <code>width*height*K</code>, where <code>K</code> is an integer.  The font then has
     *               <code>K</code> character mappings.  The caller may be guaranteed that <code>bitmap</code> will not
     *               be changed in any way.
     * @throws IllegalArgumentException If <code>bitmap</code> is not properly sized.
     */
    public BitmappedTextConsoleFont(int width, int height, boolean[] bitmap)
            throws IllegalArgumentException
    {
        super();
        if (bitmap.length % (width * height) != 0)
        {
            throw new IllegalArgumentException("Bitmap length must be a multiple of " + (width * height));
        }
        this.width = width;
        this.height = height;
        this.bitmap = bitmap;
        cache = new SynchronizedCache<CacheKey, BufferedImage>(new ConsoleImageCache(500));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines the height of a character cell in this font.  This value must be constant.
     *
     * @return The height of a character cell.
     */
    public int getCellHeight()
    {
        return height;
    }

    /**
     * Determines the width of a character cell in this font.  This value must be constant.
     *
     * @return The width of a character cell.
     */
    public int getCellWidth()
    {
        return width;
    }

    /**
     * Determines the recommended top line of the cursor in this font.
     *
     * @return The top line on which the cursor should be drawn in the cell, inclusive.
     */
    public int getTopCursorLine()
    {
        return 10;
    }

    /**
     * Determines the recommended bottom line of the cursor in this font.
     *
     * @return The bottom line on which the cursor should be drawn in the cell, inclusive.
     */
    public int getBottomCursorLine()
    {
        return 12;
    }

    /**
     * Renders a console cell at the provided position of the given {@link java.awt.Graphics} object.
     *
     * @param character  The character to be rendered (usually within [0,255] but not necessarily).
     * @param foreground The index of the foreground color to use (within [0,15]).
     * @param background The index of the background color to use (within [0,15]).
     * @param g          The {@link java.awt.Graphics} object on which to draw the cell.
     * @param x          The X-coordinate at which the upper-left corner of the cell appears.
     * @param y          The Y-coordinate at which the upper-left corner of the cell appears.
     */
    public void renderCell(char character, int foreground, int background, Graphics g, int x, int y)
    {
        g.drawImage(cache.getData(new CacheKey(character, (byte) foreground, (byte) background)), x, y, null);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Unpacks the default bitmap data into a <code>boolean[]</code> which can be used by the general constructor of
     * this class.
     *
     * @return The default bitmap.
     */
    private static boolean[] getDefaultBitmap()
    {
        byte[] decompressed = Utilities.ungzip(DEFAULT_BITMAP_DATA);
        boolean[] bitmap = new boolean[decompressed.length * 8];
        for (int i = 0; i < decompressed.length; i++)
        {
            int offset = i * 8;
            int data = decompressed[i];
            bitmap[offset] = ((data & 0x80) != 0);
            bitmap[offset + 1] = ((data & 0x40) != 0);
            bitmap[offset + 2] = ((data & 0x20) != 0);
            bitmap[offset + 3] = ((data & 0x10) != 0);
            bitmap[offset + 4] = ((data & 0x08) != 0);
            bitmap[offset + 5] = ((data & 0x04) != 0);
            bitmap[offset + 6] = ((data & 0x02) != 0);
            bitmap[offset + 7] = ((data & 0x01) != 0);
        }
        return bitmap;
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * A data container for the data which describes a single console character.
     *
     * @author Zachary Palmer
     */
    static class CacheKey
    {
        /**
         * The character image index.
         */
        protected int character;
        /**
         * The foreground color of the image.
         */
        protected byte foreground;
        /**
         * The background color of the image.
         */
        protected byte background;

        /**
         * General constructor.
         */
        public CacheKey(int character, byte foreground, byte background)
        {
            this.background = background;
            this.character = character;
            this.foreground = foreground;
        }

        public byte getBackground()
        {
            return background;
        }

        public int getCharacter()
        {
            return character;
        }

        public byte getForeground()
        {
            return foreground;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final CacheKey cacheKey = (CacheKey) o;

            if (background != cacheKey.background) return false;
            if (character != cacheKey.character) return false;
            return foreground == cacheKey.foreground;
        }

        public int hashCode()
        {
            int result;
            result = character;
            result = 29 * result + (int) foreground;
            result = 29 * result + (int) background;
            return result;
        }
    }

    /**
     * The caching system for a {@link TextConsoleDisplayComponent}.
     *
     * @author Zachary Palmer
     */
    class ConsoleImageCache extends AbstractCache<CacheKey, BufferedImage>
    {
        /**
         * General constructor.
         *
         * @param size The size of the cache.
         */
        public ConsoleImageCache(int size)
        {
            super(size);
        }

        /**
         * This method is used to generate the data described by the provided descriptor.
         *
         * @param descriptor The descriptor which describes the data to be generated.
         * @return The data which is represented by the provided descriptor.
         */
        protected BufferedImage generateData(CacheKey descriptor)
        {
            int character = descriptor.getCharacter();
            int foreground = descriptor.getForeground();
            int background = descriptor.getBackground();

            // Create the image.
            BufferedImage image = new BufferedImage(
                    getCellWidth(), getCellHeight(), BufferedImage.TYPE_BYTE_INDEXED, CONSOLE_COLOR_MODEL);
            int bitmapBaseOffset = character * getCellWidth() * getCellHeight();
            int[] rasterSpace = new int[getCellWidth() * getCellHeight()];
            for (int i = 0; i < rasterSpace.length; i++)
            {
                rasterSpace[i] = (bitmap[bitmapBaseOffset + i]) ? foreground : background;
            }
            image.getRaster().setPixels(0, 0, getCellWidth(), getCellHeight(), rasterSpace);
            return image;
        }
    }
}

// END OF FILE
