package orioni.jz.awt.swing.cosm;

/**
 * This interface should be implemented by any class which wishes to be notified of changes to a {@link Cosm}.
 *
 * @author Zachary Palmer
 */
public interface CosmListener
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is called whenever a {@link Cosm} which this listener is observing changes its {@link BackgroundRenderer}.
     * @param cosm The {@link Cosm} which was changed.
     */
    public void backgroundManagerChanged(Cosm cosm);

    /**
     * This method is called whenever a {@link Cosm} which this listener is observing adds another {@link Entity} to its
     * group.
     * @param cosm The {@link Cosm} which was changed.
     * @param entity The {@link Entity} which was added.
     */
    public void entityAdded(Cosm cosm, Entity entity);

    /**
     * This method is called whenever a {@link Cosm} which this listener is observing remvoed an {@link Entity} from its
     * group.
     * @param cosm The {@link Cosm} which was changed.
     * @param entity The {@link Entity} which was removed.
     */
    public void entityRemoved(Cosm cosm, Entity entity);

    /**
     * This method is called whenever a {@link Cosm} which this listener is observing notices a change in one of its
     * entities.  This change can be anything: a change in layer, in rendering behavior, in position, etc.
     * @param cosm The {@link Cosm} which was changed.
     * @param entity The {@link Entity} which was changed.
     */
    public void entityAltered(Cosm cosm, Entity entity);
}
