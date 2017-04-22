package orioni.jz.awt.swing.cosm;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * This {@link Entity} implementation is designed to provide basic storage for the data to which the {@link Entity}
 * interface provides access.  The {@link Entity} interface is abstracted from this class in order to permit systems
 * which have a previously implemented or otherwise incompatible object structure to still include their objects in the
 * Cosm engine.  However, software which is designed with the intent of using the Cosm system and which does not need to
 * extend another class to represent its entities may extend this class in order to use its implementations of the
 * storage and retrieval mechanisms.
 *
 * @author Zachary Palmer
 */
public class BaseEntity implements Entity
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link EntityListener}s which should be notified upon a change to this {@link Entity}.
     */
    protected Set<EntityListener> listeners;
    /**
     * The {@link BufferedImage} being used to display this {@link Entity}.
     */
    protected BufferedImage image;
    /**
     * The X offset at which this {@link Entity}'s image should be placed.
     */
    protected int offsetX;
    /**
     * The Y offset at which this {@link Entity}'s image should be placed.
     */
    protected int offsetY;
    /**
     * The X coordinate for this {@link Entity} in Cosm space.
     */
    protected double positionX;
    /**
     * The Y coordinate for this {@link Entity} in Cosm space.
     */
    protected double positionY;
    /**
     * The rendering layer of this {@link Entity}.
     */
    protected int layer;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes this {@link BaseEntity} with a set of defaults.
     */
    public BaseEntity()
    {
        this(0.0, 0.0, null);
    }

    /**
     * General constructor.
     *
     * @param x     The X position of this entity within the {@link Cosm}.
     * @param y     The Y position of this entity within the {@link Cosm}.
     * @param image The {@link BufferedImage} to use to display the entity.
     */
    public BaseEntity(double x, double y, BufferedImage image)
    {
        listeners = new HashSet<EntityListener>();
        this.image = image;
        offsetX = 0;
        offsetY = 0;
        positionX = x;
        positionY = y;
        layer = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds an {@link EntityListener} to this {@link Entity}.  That {@link EntityListener} will be notified of events
     * until it is removed.
     *
     * @param listener The {@link EntityListener} in question.
     */
    public void addListener(EntityListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Retrieves the {@link java.awt.image.BufferedImage} to be used to represent this {@link Entity}.  When this {@link
     * Entity} is displayed, the {@link BufferedImage} returned by this method will be centered over this {@link
     * Entity}'s coordinates.  If this is undesirable, the position of the {@link BufferedImage} can be adjusted by the
     * results of the {@link Entity#getImageXOffset()} and {@link Entity#getImageYOffset()} methods.
     *
     * @return The {@link Entity}'s current appearance.
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Retrieves the X offset of this {@link Entity}'s display.  When this {@link Entity} is rendered, the X coordinate
     * at which its {@link BufferedImage} is displayed will be adjusted by the amount returned here.
     *
     * @return The X offset for the {@link Entity}'s image.
     */
    public int getImageXOffset()
    {
        return offsetX;
    }

    /**
     * Retrieves the Y offset of this {@link Entity}'s display.  When this {@link Entity} is rendered, the Y coordinate
     * at which its {@link BufferedImage} is displayed will be adjusted by the amount returned here.
     *
     * @return The Y offset for the {@link Entity}'s image.
     */
    public int getImageYOffset()
    {
        return offsetY;
    }

    /**
     * Retrieves the X coordinate of this {@link Entity}.
     *
     * @return The X coordinate of this {@link Entity}.
     */
    public double getX()
    {
        return positionX;
    }

    /**
     * Retrieves the Y coordinate of this {@link Entity}.
     *
     * @return The Y coordinate of this {@link Entity}.
     */
    public double getY()
    {
        return positionY;
    }

    /**
     * Retrieves the rendering layer on which this {@link orioni.jz.awt.swing.cosm.Entity} exists.
     *
     * @return The rendering layer on which this {@link orioni.jz.awt.swing.cosm.Entity} exists.
     */
    public int getLayer()
    {
        return layer;
    }

    /**
     * Removes an {@link EntityListener} from this {@link Entity}.  That {@link EntityListener} will no longer receive
     * event notification from this {@link Entity}.
     *
     * @param listener The {@link EntityListener} in question.
     */
    public void removeListener(EntityListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Changes the X coordinate of this {@link Entity}.
     *
     * @param x The new X coordinate for this {@link Entity}.
     */
    public void setX(double x)
    {
        double oldX = positionX;
        positionX = x;
        for (EntityListener listener : listeners)
        {
            listener.positionChanged(this, oldX, positionY, positionX, positionY);
        }
    }

    /**
     * Changes the Y coordinate of this {@link Entity}.
     *
     * @param y The new Y coordinate for this {@link Entity}.
     */
    public void setY(double y)
    {
        double oldY = positionY;
        positionY = y;
        for (EntityListener listener : listeners)
        {
            listener.positionChanged(this, positionX, oldY, positionX, positionY);
        }
    }

    /**
     * Changes the rendering layer of this {@link Entity}.
     *
     * @param layer The new rendering layer for this {@link Entity}.
     */
    public void setLayer(int layer)
    {
        int oldLayer = this.layer;
        this.layer = layer;
        for (EntityListener listener : listeners)
        {
            listener.layerChanged(this, oldLayer, this.layer);
        }
    }

    /**
     * Changes the X and Y coordinates of this {@link Entity}.  This is distinct from the {@link BaseEntity#getX()} and
     * {@link BaseEntity#getY()} methods in that it only notifies the listeners of this change once.
     *
     * @param x The new X coordinate for this {@link Entity}.
     * @param y The new Y coordinate for this {@link Entity}.
     */
    public void setPosition(double x, double y)
    {
        double oldX = positionX;
        double oldY = positionY;
        positionX = x;
        positionY = y;
        for (EntityListener listener : listeners)
        {
            listener.positionChanged(this, oldX, oldY, positionX, positionY);
        }
    }

    /**
     * Changes the image X offset for this {@link Entity}.
     *
     * @param offsetX The X offset for the image which represents this {@link Entity}.
     */
    public void setImageOffsetX(int offsetX)
    {
        int oldX = this.offsetX;
        this.offsetX = offsetX;
        for (EntityListener listener : listeners)
        {
            listener.renderingChanged(this, image, oldX, offsetY, image, this.offsetX, offsetY);
        }
    }

    /**
     * Changes the image Y offset for this {@link Entity}.
     *
     * @param offsetY The Y offset for the image which represents this {@link Entity}.
     */
    public void setImageOffsetY(int offsetY)
    {
        int oldY = this.offsetY;
        this.offsetY = offsetY;
        for (EntityListener listener : listeners)
        {
            listener.renderingChanged(this, image, offsetX, oldY, image, offsetX, this.offsetY);
        }
    }

    /**
     * Changes the image used to display this {@link Entity}.
     *
     * @param image The new image to be used.
     */
    public void setImage(BufferedImage image)
    {
        BufferedImage oldImage = this.image;
        this.image = image;
        for (EntityListener listener : listeners)
        {
            listener.renderingChanged(this, oldImage, offsetX, offsetY, this.image, offsetX, offsetY);
        }
    }

    /**
     * Changes the offsets of the image used to display this {@link Entity}.  This method is essentially the same as a
     * call to {@link BaseEntity#setImageOffsetX(int)} followed by a call to {@link BaseEntity#setImageOffsetY(int)}
     * except in that it only notifies the listeners once.
     *
     * @param offsetX The X offset for the image which represents this {@link Entity}.
     * @param offsetY The Y offset for the image which represents this {@link Entity}.
     */
    public void setImageOffsets(int offsetX, int offsetY)
    {
        int oldX = this.offsetX;
        int oldY = this.offsetY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        for (EntityListener listener : listeners)
        {
            listener.renderingChanged(this, image, oldX, oldY, image, this.offsetX, this.offsetY);
        }
    }

    /**
     * Changes the manner in which this {@link Entity} is displayed.  This method is essentially the same as a call to
     * {@link BaseEntity#setImageOffsetX(int)} followed by a call to {@link BaseEntity#setImageOffsetY(int)} and then a
     * call to {@link BaseEntity#setImage(BufferedImage)} except in that it only notifies the listeners once.
     *
     * @param offsetX The X offset for the image which represents this {@link Entity}.
     * @param offsetY The Y offset for the image which represents this {@link Entity}.
     * @param image    The new image to be used.
     */
    public void setDisplay(int offsetX, int offsetY, BufferedImage image)
    {
        int oldX = this.offsetX;
        int oldY = this.offsetY;
        BufferedImage oldImage = this.image;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.image = image;
        for (EntityListener listener : listeners)
        {
            listener.renderingChanged(this, oldImage, oldX, oldY, this.image, this.offsetX, this.offsetY);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
