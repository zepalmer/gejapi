package orioni.jz.awt.swing.cosm;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This {@link JComponent} extension is used to render a {@link Cosm} in Swing.  A {@link CosmDisplayComponent} renders
 * the {@link Entity} objects it finds in the {@link Cosm} it is rendering.  The upper-left of the {@link
 * CosmDisplayComponent} is positioned in terms of the {@link Cosm}'s coordinates, which gives a point of reference for
 * how the {@link Entity} objects in {@link Cosm} space are rendered in terms of graphical space.
 *
 * @author Zachary Palmer
 */
public class CosmDisplayComponent extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Cosm} which this {@link CosmDisplayComponent} is displaying.
     */
    protected Cosm cosm;

    /**
     * The X position in Cosm coordinates of the center of this component's display.
     */
    protected double positionX;
    /**
     * The Y position in Cosm coordinates of the center of this component's display.
     */
    protected double positionY;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param cosm The {@link Cosm} that this {@link CosmDisplayComponent} is to display.
     */
    public CosmDisplayComponent(Cosm cosm)
    {
        super();
        this.cosm = cosm;
        positionX = 0.0;
        positionY = 0.0;
        setPreferredSize(new Dimension(400, 300));
        setFocusable(true);
        setEnabled(true);
        setOpaque(true);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Changes the relative position of this {@link CosmDisplayComponent}'s display within the model of the {@link
     * Cosm}.
     *
     * @param x The new X position for the center.
     * @param y The new Y position for the center.
     */
    public void setDisplayCenter(double x, double y)
    {
        positionX = x;
        positionY = y;
        this.repaint(40);
    }

    /**
     * Retrieves the current X coordinate of the display center.
     *
     * @return The X coordinate of the display center.
     */
    public double getDisplayCenterX()
    {
        return positionX;
    }

    /**
     * Retrieves the current Y coordinate of the display center.
     *
     * @return The Y coordinate of the display center.
     */
    public double getDisplayCenterY()
    {
        return positionY;
    }

    /**
     * Paints this component.
     *
     * @param g The graphics context to use for painting
     */
    public void paint(Graphics g)
    {
        final int upperLeftX = (int) (positionX - this.getWidth() / 2);
        final int upperLeftY = (int) (positionY - this.getHeight() / 2);

        // Draw background
        cosm.renderBackground(g, upperLeftX, upperLeftY, getWidth(), getHeight());

        // Draw entities in order
        for (Entity entity : cosm.getEntities())
        {
            BufferedImage image = entity.getImage();
            int leftX = (int) (entity.getX() + entity.getImageXOffset() - (image.getWidth() / 2) - upperLeftX);
            int topY = (int) (entity.getY() + entity.getImageYOffset() - (image.getHeight() / 2) - upperLeftY);

            if ((leftX > -image.getWidth()) && (leftX < getWidth()) && (topY > -image.getHeight()) &&
                (topY < getHeight()))
            {
                g.drawImage(
                        image,
                        (int) (entity.getX() + entity.getImageXOffset() - (image.getWidth() / 2) - upperLeftX),
                        (int) (entity.getY() + entity.getImageYOffset() - (image.getHeight() / 2) - upperLeftY),
                        null);
            }
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
