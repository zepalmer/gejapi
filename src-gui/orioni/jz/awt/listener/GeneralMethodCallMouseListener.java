package orioni.jz.awt.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This {@link MouseListener} implementation simply calls the abstract method {@link
 * GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)} whenever <i>any</i> listener method is called.
 */
public abstract class GeneralMethodCallMouseListener implements MouseListener
{
    /**
     * General constructor;
     */
    public GeneralMethodCallMouseListener()
    {
    }

    /**
     * Calls {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     * @param e The parameter for {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     */
    public void mouseClicked(MouseEvent e)
    {
        checkMouseOperation(e);
    }

    /**
     * Calls {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     * @param e The parameter for {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     */
    public void mouseEntered(MouseEvent e)
    {
        checkMouseOperation(e);
    }

    /**
     * Calls {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     * @param e The parameter for {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     */
    public void mouseExited(MouseEvent e)
    {
        checkMouseOperation(e);
    }

    /**
     * Calls {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     * @param e The parameter for {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     */
    public void mousePressed(MouseEvent e)
    {
        checkMouseOperation(e);
    }

    /**
     * Calls {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     * @param e The parameter for {@link GeneralMethodCallMouseListener#checkMouseOperation(MouseEvent)}.
     */
    public void mouseReleased(MouseEvent e)
    {
        checkMouseOperation(e);
    }

    /**
     * The method containing the instructions which should be executed when any kind of mouse event occurs.
     * @param e The {@link MouseEvent} describing what happened.
     */
    public abstract void checkMouseOperation(MouseEvent e);
}

// END OF FILE