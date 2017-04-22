package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link JComponent} is designed to display a centered image over a background of a specified color.  The
 * preferred size of this component is the maximum between the size of the image and the preferred size as it is set by
 * {@link CenteredImageComponent#setPreferredSize(java.awt.Dimension)}.
 * <p/>
 * The drawing order is a simple three-step process.  First, the entire component is painted in the background color.
 * Then, if an underlay color exists, it is drawn in the square in which the image will be drawn.  Finally, the image is
 * drawn.  The underlay color is useful if the image contains transparent pixels and these pixels need to be made
 * distinct from the rest of the background.
 *
 * @author Zachary Palmer
 */
public class CenteredImageComponent extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The image to display.
     */
    protected Image image;
    /**
     * The current preferred size.
     */
    protected Dimension preferredSize;
    /**
     * The underlay color, if any.
     */
    protected Color underlay;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param image      The initial image to display.  If <code>null</code>, the displayed image is blank.
     * @param background The initial background over which to display the image.
     */
    public CenteredImageComponent(Image image, Color background)
    {
        super();
        setImage(image);
        super.setBackground(background);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Draws the image, centered over the background.
     *
     * @param g The object on which to draw the image.
     */
    protected void paintComponent(Graphics g)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (image != null)
        {
            int imageXPos = (this.getWidth() - image.getWidth(null)) / 2;
            int imageYPos = (this.getHeight() - image.getHeight(null)) / 2;
            if (underlay != null)
            {
                g.setColor(underlay);
                g.fillRect(imageXPos, imageYPos, image.getWidth(null), image.getHeight(null));
            }
            g.drawImage(
                    image, (this.getWidth() - image.getWidth(null)) / 2,
                    (this.getHeight() - image.getHeight(null)) / 2, null);
        }
    }

    /**
     * Changes the image being displayed in this component.
     *
     * @param image The new image to display.
     */
    public void setImage(Image image)
    {
        this.image = image;
        updatePreferredSize();
        repaint();
    }

    /**
     * Retrieves the image currently being displayed in this component.
     *
     * @return The image being displayed.
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * Sets the preferred size of this component.
     *
     * @param size The preferred size of this component.
     * @see CenteredImageComponent
     */
    public void setPreferredSize(Dimension size)
    {
        super.setPreferredSize(size);
        updatePreferredSize();
    }

    /**
     * Retrieves the preferred size of this component.
     *
     * @return The preferred size of this component.
     * @see CenteredImageComponent
     */
    public Dimension getPreferredSize()
    {
        return preferredSize;
    }

    /**
     * Updates the preferred size of this component.
     */
    protected void updatePreferredSize()
    {
        if (image == null)
        {
            preferredSize = super.getPreferredSize();
        } else
        {
            preferredSize = new Dimension(
                    (int) (Math.max(super.getPreferredSize().getWidth(), image.getWidth(null))),
                    (int) (Math.max(super.getPreferredSize().getHeight(), image.getHeight(null))));
        }
    }

    /**
     * Sets the underlay color or removes the underlay.
     *
     * @param color The new underlay color, or <code>null</code> to remove the underlay.
     */
    public void setUnderlay(Color color)
    {
        underlay = color;
        this.repaint();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE