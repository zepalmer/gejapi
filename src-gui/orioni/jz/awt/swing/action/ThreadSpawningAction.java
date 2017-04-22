package orioni.jz.awt.swing.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This {@link AbstractAction} extension can start a new thread whenever the action takes place.  The actual operational
 * behavior of the action is defined in the {@link ThreadSpawningAction#performAction(ActionEvent e)} method, which may
 * be called from the new thread.
 *
 * @author Zachary Palmer
 */
public abstract class ThreadSpawningAction extends AbstractAction
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Whether or not to spawn a new thread.
     */
    protected boolean newThread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a new thread will be spawned.
     */
    public ThreadSpawningAction()
    {
        super();
    }

    /**
     * General constructor.
     *
     * @param newThread Whether or not to spawn a new thread.
     */
    public ThreadSpawningAction(boolean newThread)
    {
        super();
        this.newThread = newThread;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Invoked when an action occurs.
     *
     * @param e The {@link ActionEvent} associated with the action.
     */
    public void actionPerformed(final ActionEvent e)
    {
        if (newThread)
        {
            new Thread()
            {
                public void run()
                {
                    performAction(e);
                }
            }.start();
        } else
        {
            performAction(e);
        }
    }

    /**
     * Retrieves this {@link Action}'s name.
     */
    public String getName()
    {
        return (String)(getValue(Action.NAME));
    }

    /**
     * Invoked by a new thread when an action occurs.
     *
     * @param e The {@link ActionEvent} associated with the action.
     */
    public abstract void performAction(ActionEvent e);

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE