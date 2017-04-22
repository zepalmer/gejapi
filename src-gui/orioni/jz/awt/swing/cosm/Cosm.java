package orioni.jz.awt.swing.cosm;

import orioni.jz.util.HashMultiMap;
import orioni.jz.util.MultiMap;
import orioni.jz.util.Utilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is the core of the Cosm engine.  An instance of {@link Cosm} represents a single two-dimensional
 * renderable simulation.  A Cosm contains a number of {@link Entity} objects which represent its contents.  The purpose
 * of a {@link Cosm} is to act as a collection point for the {@link Entity} objects and to provide other general,
 * non-{@link Entity} rendering features such as backgrounds or tilesets.
 *
 * @author Zachary Palmer
 */
public class Cosm
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * This {@link MultiMap} contains all of the {@link Entity} objects which exist in this {@link Cosm}.  They are
     * grouped in terms of the layer on which they are drawn.  This allows the {@link Entity} objects to be retrieved in
     * order of layer.
     */
    protected MultiMap<Integer, Entity> entityMap;
    /**
     * A cached array, sorted in ascending order, of all of the keys currently being used by the entity map.  If this
     * value is <code>null</code>, the array needs to be calculated.
     */
    protected Integer[] entityKeySortedArray;

    /**
     * The {@link EntityListener} used to observe changes in {@link Entity} layers.  When a change is observed, the
     * {@link Entity} must be moved to a different key in the multimap.
     */
    protected EntityListener layerListener;
    /**
     * The {@link EntityListener} used to observe any change in registered {@link Entity} objects and report them to all
     * registered {@link CosmListener}s.
     */
    protected EntityListener cosmRelayListener;

    /**
     * The {@link CosmListener}s which this {@link Cosm} must notify.
     */
    protected Set<CosmListener> listeners;

    /**
     * The {@link BackgroundRenderer} for this {@link Cosm}.
     */
    protected BackgroundRenderer backgroundRenderer;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public Cosm()
    {
        super();
        final Cosm scopedThis = this;
        entityMap = new HashMultiMap<Integer, Entity>();
        listeners = new HashSet<CosmListener>();
        setBackgroundRenderer(new SingleColorBackgroundRenderer(Color.BLACK));

        layerListener = new EntityAdapter()
        {
            public void layerChanged(Entity entity, int oldLayer, int newLayer)
            {
                synchronized (scopedThis)
                {
                    entityMap.remove(oldLayer, entity);
                    entityMap.put(newLayer, entity);
                }
            }
        };

        cosmRelayListener = new EntityListener()
        {
            public void layerChanged(Entity entity, int oldLayer, int newLayer)
            {
                notifyCosmListeners(entity);
            }

            public void positionChanged(Entity entity, double oldX, double oldY, double newX, double newY)
            {
                notifyCosmListeners(entity);
            }

            public void renderingChanged(Entity entity, BufferedImage oldImage, int oldX, int oldY,
                                         BufferedImage newImage,
                                         int newX, int newY)
            {
                notifyCosmListeners(entity);
            }

            private void notifyCosmListeners(Entity entity)
            {
                for (CosmListener listener : listeners)
                {
                    listener.entityAltered(scopedThis, entity);
                }
            }
        };
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds an {@link Entity} to this {@link Cosm}.
     *
     * @param entity The {@link Entity} to add.
     */
    public synchronized void addEntity(Entity entity)
    {
        if (!entityMap.containsKey(entity.getLayer())) entityKeySortedArray = null;

        entityMap.put(entity.getLayer(), entity);
        entity.addListener(layerListener);
        entity.addListener(cosmRelayListener);
        for (CosmListener listener : listeners)
        {
            listener.entityAdded(this, entity);
        }
    }

    /**
     * Removes an {@link Entity} from this {@link Cosm}.
     *
     * @param entity The {@link Entity} to remove.
     */
    public synchronized void removeEntity(Entity entity)
    {
        if (!entityMap.remove(entity.getLayer(), entity)) return;
        entity.removeListener(layerListener);
        entity.removeListener(cosmRelayListener);
        for (CosmListener listener : listeners)
        {
            listener.entityRemoved(this, entity);
        }

        if (!entityMap.containsKey(entity.getLayer())) entityKeySortedArray = null;
    }

    /**
     * Adds a {@link CosmListener} to this {@link Cosm}'s listener set.
     *
     * @param listener The {@link CosmListener} to add.
     */
    public synchronized void addListener(CosmListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes a {@link CosmListener} from this {@link Cosm}'s listener set.
     *
     * @param listener The {@link CosmListener} to remove.
     */
    public synchronized void removeListener(CosmListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Sets the background manager for this {@link Cosm}.
     *
     * @param backgroundRenderer The new {@link BackgroundRenderer} to use for the background of this {@link Cosm}.
     */
    public synchronized void setBackgroundRenderer(BackgroundRenderer backgroundRenderer)
    {
        this.backgroundRenderer = backgroundRenderer;
        for (CosmListener listener : listeners)
        {
            listener.backgroundManagerChanged(this);
        }
    }

    /**
     * Renders a background on a provided {@link Graphics} object.  This method is intended to provide access to the
     * {@link BackgroundRenderer#renderBackground(java.awt.Graphics, int, int, int, int)} method without allowing the
     * caller to access other methods on the {@link BackgroundRenderer}.
     *
     * @param g      The {@link Graphics} object on which to render the background.
     * @param x      The X offset of the upper left corner of the {@link Graphics} object.
     * @param y      The Y offset of the upper left corner of the {@link Graphics} object.
     * @param width  The width of the background to render.
     * @param height The height of the background to render.
     */
    public synchronized void renderBackground(Graphics g, int x, int y, int width, int height)
    {
        backgroundRenderer.renderBackground(g, x, y, width, height);
    }

    /**
     * Retrieves a list of {@link Entity} objects for a display component to render in the order in which they should
     * be rendered.
     * @return The {@link Entity} objects to be rendered.
     */
    public synchronized java.util.List<Entity> getEntities()
    {
        java.util.List<Entity> ret = new ArrayList<Entity>();
        if (entityKeySortedArray ==null)
        {
            entityKeySortedArray = entityMap.keySet().toArray(Utilities.EMPTY_INTEGER_ARRAY);
            Arrays.sort(entityKeySortedArray);
        }
        for (Integer key : entityKeySortedArray)
        {
            ret.addAll(entityMap.getAll(key));
        }
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
