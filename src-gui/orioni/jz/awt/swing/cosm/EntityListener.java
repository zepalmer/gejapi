package orioni.jz.awt.swing.cosm;

import java.awt.image.BufferedImage;

/**
 * This interface must be implemented by any object which wishes to receive notification when an {@link Entity} is
 * changed.
 *
 * @author Zachary Palmer
 */
public interface EntityListener
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Indicates that the position of an {@link Entity} within its {@link Cosm} has changed.
     *
     * @param entity The {@link Entity} which was changed.
     * @param oldX  The previous X coordinate for the {@link Entity}.
     * @param oldY  The previous Y coordinate for the {@link Entity}.
     * @param newX  The new X coordinate for the {@link Entity}.
     * @param newY  The new Y coordinate for the {@link Entity}.
     */
    public void positionChanged(Entity entity, double oldX, double oldY, double newX, double newY);

    /**
     * Indicates that the manner in which an {@link Entity} is displayed has been changed.  This may either be the image
     * or the offsets for that image.
     *
     * @param entity The {@link Entity} which was changed.
     * @param oldImage The previous image for the {@link Entity} in question.
     * @param oldX The previous X offset for that image.
     * @param oldY The previous Y offset for that image.
     * @param oldImage The new image for the {@link Entity} in question.
     * @param oldX The X offset for that image.
     * @param oldY The Y offset for that image.
     */
    public void renderingChanged(Entity entity, BufferedImage oldImage, int oldX, int oldY, BufferedImage newImage,
                                 int newX, int newY);

    /**
     * Indicates that the rendering layer of an {@link Entity} has been changed.
     * @param entity The {@link Entity} whose rendering layer changed.
     * @param oldLayer The previous rendering layer for that entity.
     * @param newLayer The new rendering layer for that entity.
     */
    public void layerChanged(Entity entity, int oldLayer, int newLayer);
}
