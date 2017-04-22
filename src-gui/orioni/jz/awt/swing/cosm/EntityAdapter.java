package orioni.jz.awt.swing.cosm;

import java.awt.image.BufferedImage;

/**
 * Patterned after the AWT adapter classes, this class implements all of the functions on {@link EntityListener} with
 * void functions.  Extending classes may then choose those classes which they wish to override rather than being forced
 * to implement all of the functions on the listener.
 *
 * @author Zachary Palmer
 */
public class EntityAdapter implements EntityListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public EntityAdapter()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Does nothing.
     *
     * @param entity Ignored.
     * @param oldLayer Ignored.
     * @param newLayer Ignored.
     */
    public void layerChanged(Entity entity, int oldLayer, int newLayer)
    {
    }

    /**
     * Does nothing.
     *
     * @param entity Ignored.
     * @param oldX  Ignored.
     * @param oldY  Ignored.
     * @param newX  Ignored.
     * @param newY  Ignored.
     */
    public void positionChanged(Entity entity, double oldX, double oldY, double newX, double newY)
    {
    }

    /**
     * Does nothing.
     *
     * @param entity    Ignored.
     * @param oldImage Ignored.
     * @param oldX     Ignored.
     * @param oldY     Ignored.
     * @param oldImage Ignored.
     * @param oldX     Ignored.
     * @param oldY     Ignored.
     */
    public void renderingChanged(Entity entity, BufferedImage oldImage, int oldX, int oldY, BufferedImage newImage,
                                 int newX, int newY)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
