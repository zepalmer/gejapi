package orioni.jz.awt.swing.cosm;

/**
 * This {@link CosmListener} implementation routes all calls to {@link CosmListener} notification methods into one
 * general method.
 *
 * @author Zachary Palmer
 */
public abstract class CosmAnyChangeListener implements CosmListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public CosmAnyChangeListener()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Routed to {@link CosmAnyChangeListener#cosmChanged(Cosm)}.
     *
     * @param cosm The {@link Cosm} which was changed.
     */
    public void backgroundManagerChanged(Cosm cosm)
    {
        cosmChanged(cosm);
    }

    /**
     * Routed to {@link CosmAnyChangeListener#cosmChanged(Cosm)}.
     *
     * @param cosm The {@link Cosm} which was changed.
     * @param entity Ignored.
     */
    public void entityAdded(Cosm cosm, Entity entity)
    {
        cosmChanged(cosm);
    }

    /**
     * Routed to {@link CosmAnyChangeListener#cosmChanged(Cosm)}.
     *
     * @param cosm The {@link Cosm} which was changed.
     * @param entity Ignored.
     */
    public void entityAltered(Cosm cosm, Entity entity)
    {
        cosmChanged(cosm);
    }

    /**
     * Routed to {@link CosmAnyChangeListener#cosmChanged(Cosm)}.
     *
     * @param cosm The {@link Cosm} which was changed.
     * @param entity Ignored.
     */
    public void entityRemoved(Cosm cosm, Entity entity)
    {
        cosmChanged(cosm);
    }

    /**
     * Called whenever a {@link Cosm} is changed.
     * @param cosm The {@link Cosm} which was affected.
     */
    public abstract void cosmChanged(Cosm cosm);

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
