package orioni.jz.awt.swing.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link Image} implementation accepts an {@link Image} and copies it into itself, adding a colored border evenly
 * around it to produce an image of a specified size.
 * <p/>
 * If a size is specified which is smaller than the original image, the middle section of that image will be used.
 *
 * @author Zachary Palmer
 */
public class BorderImage extends BufferedImage
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the image is of type {@link BufferedImage#TYPE_INT_ARGB}.
     *
     * @param image  The image to border.
     * @param border The color of the border to use.
     * @param width  The width of the new image.
     * @param height The height of the new image.
     */
    public BorderImage(Image image, Color border, int width, int height)
    {
        this(image, border, width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Skeleton constructor.  Assumes that the image is of type {@link BufferedImage#TYPE_INT_ARGB}.
     *
     * @param image  The image to border.
     * @param border The color of the border to use.
     * @param width  The width of the new image.
     * @param height The height of the new image.
     * @param type   The type of image to create, as one of the {@link BufferedImage}<code>.TYPE_XXXX</code> constants.
     */
    public BorderImage(Image image, Color border, int width, int height, int type)
    {
        super(width, height, type);

        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);
        final int imagePositionX = width / 2 - imageWidth / 2;
        final int imagePositionY = height / 2 - imageHeight / 2;

        Graphics g = getGraphics();
        g.setColor(border);
        g.fillRect(0, 0, width, imagePositionY);
        g.fillRect(0, 0, imagePositionX, height);
        g.fillRect(imagePositionX + imageWidth, 0, imagePositionX, height);
        g.fillRect(0, imagePositionY + imageHeight, width, imagePositionY);
        g.drawImage(image, imagePositionX, imagePositionY, null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE