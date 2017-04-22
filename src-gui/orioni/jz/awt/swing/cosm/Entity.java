package orioni.jz.awt.swing.cosm;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * All objects which exist within a {@link Cosm} must implement this interface.  The purpose of this interface is to
 * allow the {@link Cosm} to query the object for various types of information, including position and appearance.
 *
 * @author Zachary Palmer
 */
public interface Entity
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the {@link BufferedImage} to be used to represent this {@link Entity}.  When this {@link Entity} is
     * displayed, the {@link BufferedImage} returned by this method will be centered over this {@link Entity}'s
     * coordinates.  If this is undesirable, the position of the {@link BufferedImage} can be adjusted by the results of
     * the {@link Entity#getImageXOffset()} and {@link Entity#getImageYOffset()} methods.
     *
     * @return The {@link Entity}'s current appearance.  A return value of <code>null</code> indicates that the
     *         {@link Entity} should not be displayed.
     */
    public BufferedImage getImage();

    /**
     * Retrieves the X offset of this {@link Entity}'s display.  When this {@link Entity} is rendered, the X coordinate
     * at which its {@link Image} is displayed will be adjusted by the amount returned here.
     *
     * @return The X offset for the {@link Entity}'s image.
     */
    public int getImageXOffset();

    /**
     * Retrieves the Y offset of this {@link Entity}'s display.  When this {@link Entity} is rendered, the Y coordinate
     * at which its {@link Image} is displayed will be adjusted by the amount returned here.
     *
     * @return The Y offset for the {@link Entity}'s image.
     */
    public int getImageYOffset();

    /**
     * Retrieves the X coordinate of this {@link Entity}.
     *
     * @return The X coordinate of this {@link Entity}.
     */
    public double getX();

    /**
     * Retrieves the Y coordinate of this {@link Entity}.
     *
     * @return The Y coordinate of this {@link Entity}.
     */
    public double getY();

    /**
     * Retrieves the rendering layer on which this {@link Entity} exists.
     * @return The rendering layer on which this {@link Entity} exists.
     */
    public int getLayer();

    /**
     * Adds an {@link EntityListener} to this {@link Entity}.  That {@link EntityListener} will be notified of events
     * until it is removed.
     *
     * @param listener The {@link EntityListener} in question.
     */
    public void addListener(EntityListener listener);

    /**
     * Removes an {@link EntityListener} from this {@link Entity}.  That {@link EntityListener} will no longer receive
     * event notification from this {@link Entity}.
     *
     * @param listener The {@link EntityListener} in question.
     */
    public void removeListener(EntityListener listener);
}
