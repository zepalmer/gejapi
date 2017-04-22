package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link JPanel} extension is designed to render itself as a unit of text contained within a box with a shaded
 * background.  Objects of this class have a shading policy describing how they are shaded and use two colors in the
 * shaded background.
 * <p/>
 * This class also doubles as a {@link ListCellRenderer}, rendering itself in list cells by altering its properties to
 * match the {@link ShadedBackgroundTextLabel} that is provided to it.  As a {@link ListCellRenderer}, this class can
 * only render {@link ShadedBackgroundTextLabel} objects; any other objects passed to this class as a {@link
 * ListCellRenderer} will cause a {@link ClassCastException}.
 * <p/>
 * Using an instance of this class as a {@link ListCellRenderer} will destroy its appearance in other panes.  Thus, no
 * instance of {@link ShadedBackgroundTextLabel} should be used both as a {@link JComponent} and a {@link
 * ListCellRenderer}.
 *
 * @author Zachary Palmer
 */
public class ShadedBackgroundTextLabel extends JLabel implements ListCellRenderer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An enumeration containing the shading policies for the {@link ShadedBackgroundTextLabel}.
     */
    public static enum ShadingPolicy
    {
        /**
         * A shading policy which specifies that the primary color appears at the top, the secondary color appears at
         * the bottom, and a gradient of the two is display in between.
         */
        TOP_TO_BOTTOM,
        /**
         * A shading policy which specifies that the primary color appears in the center, the secondary color appears at
         * the top and bottom, and a gradient of the two is displayed in between each.
         */
        MIDDLE_TO_TOP_AND_BOTTOM,
        /**
         * A shading policy which specifies that the primary color appears in the middle, the secondary color appears at
         * the corners, and a radial gradient appears between them.
         */
        MIDDLE_TO_CORNERS,
        /**
         * A shading policy which specifies that only the primary color appears.
         */
        PRIMARY_ONLY,
        /**
         * A shading policy which specifies that only the secondary color appears.
         */
        SECONDARY_ONLY
    }

    /**
     * The global image caching system used by default by {@link ShadedBackgroundTextLabel}s.
     */
    private static final Cache BACKGROUND_CACHE = new Cache(100);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The primary background {@link Color} for this label.
     */
    protected Color primaryBackground;
    /**
     * The secondary background {@link Color} for this label.
     */
    protected Color secondaryBackground;
    /**
     * The current shading policy for this label.
     */
    protected ShadingPolicy shadingPolicy;

    /**
     * The horizontal inflation for this label.  The requested width for this label will be increased by the specified
     * amount over the request made by the underlying implementation of {@link JLabel}.
     */
    protected int horizontalInflation;
    /**
     * The vertical inflation for this label.  The requested height for this label will be increased by the specified
     * amount over the request made by the underlying implementation of {@link JLabel}.
     */
    protected int verticalInflation;

    /**
     * The color of the outline.
     */
    protected Color outlineColor;
    /**
     * The thickness of the outline.
     */
    protected int outlineThickness;
    /**
     * The selection mask color.  This color is masked 50/50 over the pixels of the display if it is not
     * <code>null</code>.
     */
    protected Color selectionMask;

    /**
     * The {@link Cache} used to manage the shaded backgrounds.
     */
    protected Cache backgroundCache;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes no text, a white-on-white background, black text, and a black outline.
     */
    public ShadedBackgroundTextLabel()
    {
        this("", Color.WHITE, Color.WHITE, ShadedBackgroundTextLabel.ShadingPolicy.TOP_TO_BOTTOM);
    }

    /**
     * Skeleton constructor.  Assumes a white-on-white background, black text, and a black outline.
     *
     * @param text The string that will be displayed.
     */
    public ShadedBackgroundTextLabel(String text)
    {
        this(text, Color.WHITE, Color.WHITE, ShadingPolicy.TOP_TO_BOTTOM);
    }

    /**
     * Skeleton constructor.  Assumes that the default global cache is being used for image backgrounds.
     *
     * @param text                 The string that will be displayed.
     * @param primaryBackground   The primary background color for this label.
     * @param secondaryBackground The secondary background color for this label.
     * @param policy               The shading policy for this label.
     */
    public ShadedBackgroundTextLabel(String text, Color primaryBackground, Color secondaryBackground,
                                     ShadingPolicy policy)
    {
        this(text, primaryBackground, secondaryBackground, policy, BACKGROUND_CACHE);
    }

    /**
     * General constructor.  Assumes black text and a black outline.
     *
     * @param text                 The string that will be displayed.
     * @param primaryBackground   The primary background color for this label.
     * @param secondaryBackground The secondary background color for this label.
     * @param policy               The shading policy for this label.
     * @param cache                The {@link Cache} to use to cache background images.
     */
    public ShadedBackgroundTextLabel(String text, Color primaryBackground, Color secondaryBackground,
                                     ShadingPolicy policy, Cache cache)
    {
        super();
        this.setText(text);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.primaryBackground = primaryBackground;
        this.secondaryBackground = secondaryBackground;
        shadingPolicy = policy;
        outlineColor = Color.BLACK;
        outlineThickness = 1;
        selectionMask = null;
        horizontalInflation = 0;
        verticalInflation = 0;
        backgroundCache = cache;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the selection mask for this label.  This mask will appear over the normal background but not over the
     * text.  <code>null</code> indicates no mask.
     *
     * @return The selection mask for this label.
     */
    public Color getSelectionMask()
    {
        return selectionMask;
    }

    /**
     * Sets the selection mask for this label.  This mask will appear over the normal background but not over the text.
     * <code>null</code> indicates no mask.
     *
     * @param selectionMask The selection mask to use.
     */
    public void setSelectionMask(Color selectionMask)
    {
        this.selectionMask = selectionMask;
    }

    /**
     * Retrieves the primary background for this label.
     *
     * @return The primary background for this label.
     */
    public Color getPrimaryBackground()
    {
        return primaryBackground;
    }

    /**
     * Sets the primary background for this label.
     *
     * @param primaryBackground The new primary background for this label.
     */
    public void setPrimaryBackground(Color primaryBackground)
    {
        this.primaryBackground = primaryBackground;
    }

    /**
     * Retrieves the secondary background for this label.
     *
     * @return The secondary background for this label.
     */
    public Color getSecondaryBackground()
    {
        return secondaryBackground;
    }

    /**
     * Sets the secondary background for this label.
     *
     * @param secondaryBackground The new secondary background for this label.
     */
    public void setSecondaryBackground(Color secondaryBackground)
    {
        this.secondaryBackground = secondaryBackground;
    }

    /**
     * Retrieves the outline color for this label.
     *
     * @return The outline color for this label.
     */
    public Color getOutlineColor()
    {
        return outlineColor;
    }

    /**
     * Sets the outline color for this label.
     *
     * @param outlineColor The new outline color for this label.
     */
    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    /**
     * Retrieves the current outline thickness for this label.
     *
     * @return The current outline thickness, in pixels.
     */
    public int getOutlineThickness()
    {
        return outlineThickness;
    }

    /**
     * Changes the outline thickness for this label.
     *
     * @param outlineThickness The new outline thickness, in pixels.
     */
    public void setOutlineThickness(int outlineThickness)
    {
        this.outlineThickness = outlineThickness;
    }

    /**
     * Retrieves the shading policy for this label.
     *
     * @return The shading policy for this label.
     */
    public ShadingPolicy getShadingPolicy()
    {
        return shadingPolicy;
    }

    /**
     * Sets the shading policy for this label.
     *
     * @param shadingPolicy The new shading policy for this label.
     */
    public void setShadingPolicy(ShadingPolicy shadingPolicy)
    {
        this.shadingPolicy = shadingPolicy;
    }

    /**
     * Retrieves the horizontal inflation for this {@link ShadedBackgroundTextLabel}.
     *
     * @return The current horizontal inflation.
     * @see {@link ShadedBackgroundTextLabel#setHorizontalInflation(int)}
     */
    public int getHorizontalInflation()
    {
        return horizontalInflation;
    }

    /**
     * Retrieves the vertical inflation for this {@link ShadedBackgroundTextLabel}.
     *
     * @return The current vertical inflation.
     * @see {@link ShadedBackgroundTextLabel#setVerticalInflation(int)}
     */
    public int getVerticalInflation()
    {
        return verticalInflation;
    }

    /**
     * Sets the horizontal inflation which should be used by this label.  This will increase the stated preferred size
     * of the label the specified number of pixels in width over the stated preference of the extended {@link JLabel}.
     *
     * @param horizontalInflation The number of pixels by which the width of the label should be increased.
     */
    public void setHorizontalInflation(int horizontalInflation)
    {
        this.horizontalInflation = horizontalInflation;
    }

    /**
     * Sets the vertical inflation which should be used by this label.  This will increase the stated preferred size of
     * the label the specified number of pixels in height over the stated preference of the extended {@link JLabel}.
     *
     * @param verticalInflation The number of pixels by which the height of the label should be increased.
     */
    public void setVerticalInflation(int verticalInflation)
    {
        this.verticalInflation = verticalInflation;
    }

    /**
     * Adjusts the stated size preference by the inflation as specified by calls to the inflation setters.
     *
     * @return The new preferred size.
     * @see {@link ShadedBackgroundTextLabel#setHorizontalInflation(int)}
     * @see {@link ShadedBackgroundTextLabel#setVerticalInflation(int)}
     */
    public Dimension getPreferredSize()
    {
        Dimension dim = super.getPreferredSize();
        return (new Dimension(
                (int) (dim.getWidth() + horizontalInflation),
                (int) (dim.getHeight() + verticalInflation)));
    }

    /**
     * Sets the preferred size of this component. If <code>pref</code> is <code>null</code>, the UI will be asked for
     * the preferred size.
     * <p/>
     * This override is designed to compensate for the inflation produced by the {@link
     * ShadedBackgroundTextLabel#getPreferredSize()} method.
     */
    public void setPreferredSize(Dimension pref)
    {
        super.setPreferredSize(
                new Dimension(
                        (int) (pref.getWidth() - horizontalInflation),
                        (int) (pref.getHeight() - verticalInflation)));
    }

    /**
     * Draws this component.
     *
     * @param g The {@link Graphics} object on which this component is to be drawn.
     */
    public void paint(Graphics g)
    {
        if ((getWidth() == 0) || (getHeight() == 0)) return;
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics imageGraphics = image.getGraphics();
        // Draw shading.
        backgroundCache
                .render(imageGraphics, this.getSize(), shadingPolicy, primaryBackground, secondaryBackground);
        // Draw outline
        if (outlineColor != null)
        {
            imageGraphics.setColor(outlineColor);
            for (int i = 0; i < outlineThickness; i++)
            {
                imageGraphics.drawRect(i, i, image.getWidth() - 2 * i - 1, image.getHeight() - 2 * i - 1);
            }
        }
        // Now apply selection mask
        if (selectionMask != null)
        {
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            int maskRed = selectionMask.getRed();
            int maskGreen = selectionMask.getGreen();
            int maskBlue = selectionMask.getBlue();
            for (int i = 0; i < pixels.length; i++)
            {
                // Get primary components
                int x = (i % image.getWidth());
                int y = (i / image.getWidth());
                int pixel = pixels[i];
                int alpha = (pixel >>> 24) & 0xFF;
                int red = (pixel >>> 16) & 0xFF;
                int green = (pixel >>> 8) & 0xFF;
                int blue = (pixel) & 0xFF;
                // Perform initial combination
                if ((x % 2 == 1) ^ (y % 2 == 1))
                {
                    red = (red + maskRed) / 2;
                    green = (green + maskGreen) / 2;
                    blue = (blue + maskBlue) / 2;
                } else
                {
                    red = (red + 255) / 2;
                    green = (green + 255) / 2;
                    blue = (blue + 255) / 2;
                }
                // Thicken outline
                if ((x == 1) || (x == image.getWidth() - 2) || (y == 1) || (y == image.getHeight() - 2))
                {
                    red = (red * 2 + maskRed) / 3;
                    green = (green * 2 + maskGreen) / 3;
                    blue = (blue * 2 + maskBlue) / 3;
                }
                // Store result
                pixels[i] = (alpha << 24) | (red << 16) | (green << 8) | (blue);
            }
            image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
            // Selection outline
            Color color = new Color(maskRed * 2 / 3, maskGreen * 2 / 3, maskBlue * 2 / 3);
            imageGraphics.setColor(color);
            imageGraphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
        }
        g.drawImage(image, 0, 0, null);

        this.setBackground(new Color(0, 0, 0, 0));
        super.paint(g);
    }

// NON-STATIC METHODS : LIST CELL RENDERER ///////////////////////////////////////

    /**
     * Return a component that has been configured to display the specified value. That component's <code>paint</code>
     * method is then called to "render" the cell.  If it is necessary to compute the dimensions of a list because the
     * list cells do not have a fixed size, this method is called to generate a component on which
     * <code>getPreferredSize</code> can be invoked.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(index).
     * @param index        The cells index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
        ShadedBackgroundTextLabel label = (ShadedBackgroundTextLabel) value;
        outlineColor = label.outlineColor;
        primaryBackground = label.primaryBackground;
        secondaryBackground = label.secondaryBackground;
        shadingPolicy = label.shadingPolicy;
        setText(label.getText());
        if (isSelected)
        {
            setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
        } else
        {
            setForeground(list.getForeground());
            setBackground(list.getBackground());
        }
        return this;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class is designed to act as a background image caching and generation system.  It produces the shaded
     * backgrounds for a {@link ShadedBackgroundTextLabel} and stores those backgrounds for later use.  It is capable of
     * rendering a background of the specified size and attributes on a {@link Graphics} object, allowing it to be used
     * by the {@link ShadedBackgroundTextLabel#paint(Graphics)} method.
     *
     * @author Zachary Palmer
     */
    public static class Cache extends orioni.jz.util.AbstractCache<CacheKey, BufferedImage>
    {
        /**
         * General constructor.
         *
         * @param size The maximum number of images which may be cached before the cache is flushed.
         */
        public Cache(int size)
        {
            super(size);
        }

        /**
         * Requests that this cache render the specified image on the provided {@link Graphics} object.  The upper left
         * corner of the image will be at coordinate <code>(0,0)</code>.
         *
         * @param graphics  The {@link Graphics} on which to render the image.
         * @param size      The size of the image to render.
         * @param policy    The {@link ShadingPolicy} to use.
         * @param primary   The primary {@link Color} for the image.
         * @param secondary The secondary {@link Color} for the image.
         */
        synchronized protected void render(Graphics graphics, Dimension size, ShadingPolicy policy, Color primary,
                                           Color secondary)
        {
            graphics.drawImage(getData(new CacheKey(size, policy, primary, secondary)), 0, 0, null);
        }

        /**
         * This method is used to generate the data described by the provided descriptor.
         *
         * @param descriptor The descriptor which describes the data to be generated.
         * @return The data which is represented by the provided descriptor.
         */
        protected BufferedImage generateData(CacheKey descriptor)
        {
            Dimension size = descriptor.getImageSize();
            ShadingPolicy policy = descriptor.getShadingPolicy();
            Color primary = descriptor.getPrimaryColor();
            Color secondary = descriptor.getSecondaryColor();

            final int imageWidth = (int) (size.getWidth());
            final int imageHeight = (int) (size.getHeight());
            // Generate the image.
            BufferedImage backgroundImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics backgroundImageGraphics = backgroundImage.getGraphics();
            if (policy == ShadingPolicy.PRIMARY_ONLY)
            {
                backgroundImageGraphics.setColor(primary);
                backgroundImageGraphics.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
            } else if (policy == ShadingPolicy.SECONDARY_ONLY)
            {
                backgroundImageGraphics.setColor(secondary);
                backgroundImageGraphics.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
            } else
            {
                int primaryAlpha = primary.getAlpha();
                int primaryRed = primary.getRed();
                int primaryGreen = primary.getGreen();
                int primaryBlue = primary.getBlue();
                int secondaryAlpha = secondary.getAlpha();
                int secondaryRed = secondary.getRed();
                int secondaryGreen = secondary.getGreen();
                int secondaryBlue = secondary.getBlue();
                for (int y = 1; y < imageHeight - 1; y++)
                {
                    for (int x = 1; x < imageWidth - 1; x++)
                    {
                        // This is a number between 0 and 100 expressing the distance from the secondary color as a
                        // percentage.
                        int distance;
                        switch (policy)
                        {
                            case TOP_TO_BOTTOM:
                                distance = 100 - (y * 100) / imageHeight;
                                break;
                            case MIDDLE_TO_TOP_AND_BOTTOM:
                                distance = 100 - (Math.abs(imageHeight / 2 - y) * 100) / (imageHeight / 2);
                                break;
                            case MIDDLE_TO_CORNERS:
                                int xDistancePixels = Math.abs(imageWidth / 2 - x);
                                int yDistancePixels = Math.abs(imageHeight / 2 - y);
                                // The following step converts the Y pixel count into terms of the X line
                                double yDistanceRatioedPixels =
                                        (double) yDistancePixels * imageWidth / imageHeight;
                                // Now compute the relative distance
                                double pixelDistance = Math.sqrt(
                                        xDistancePixels * xDistancePixels +
                                        yDistanceRatioedPixels * yDistanceRatioedPixels);
                                double maxPixelDistance = imageWidth / Math.sqrt(2);
                                // Scale relative distance
                                distance = (int) ((1 - (pixelDistance) / (maxPixelDistance)) * 100);
                                break;
                            default:
                                throw new RuntimeException("Invalid shading policy: " + policy);
                        }
                        if (distance > 100) distance = 100;
                        if (distance < 0) distance = 0;
                        int alpha =
                                (((primaryAlpha * distance) + secondaryAlpha * (100 - distance)) / 100) & 0xFF;
                        int red = (((primaryRed * distance) + secondaryRed * (100 - distance)) / 100) & 0xFF;
                        int green =
                                (((primaryGreen * distance) + secondaryGreen * (100 - distance)) / 100) & 0xFF;
                        int blue = (((primaryBlue * distance) + secondaryBlue * (100 - distance)) / 100) & 0xFF;
                        backgroundImage.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
                    }
                }
            }
            return backgroundImage;
        }
    }

    /**
     * This class is designed to contain key data for a cache entry.
     *
     * @author Zachary Palmer
     */
    static class CacheKey
    {
        /**
         * The size of the image.
         */
        protected Dimension imageSize;
        /**
         * The shading policy of the image.
         */
        protected ShadingPolicy policy;
        /**
         * The primary color.
         */
        protected Color primaryColor;
        /**
         * The secondary color.
         */
        protected Color secondaryColor;

        /**
         * General constructor.
         */
        public CacheKey(Dimension size, ShadingPolicy policy, Color primaryColor, Color secondaryColor)
        {
            this.policy = policy;
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            imageSize = size;
        }

        public Dimension getImageSize()
        {
            return imageSize;
        }

        public ShadingPolicy getShadingPolicy()
        {
            return policy;
        }

        public Color getPrimaryColor()
        {
            return primaryColor;
        }

        public Color getSecondaryColor()
        {
            return secondaryColor;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final CacheKey key = (CacheKey) o;

            if (policy != key.policy) return false;
            if (!primaryColor.equals(key.primaryColor)) return false;
            if (!secondaryColor.equals(key.secondaryColor)) return false;
            return imageSize.equals(key.imageSize);
        }

        public int hashCode()
        {
            int result;
            result = imageSize.hashCode();
            result = 29 * result + policy.hashCode();
            result = 29 * result + primaryColor.hashCode();
            result = 29 * result + secondaryColor.hashCode();
            return result;
        }
    }
}