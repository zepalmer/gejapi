package orioni.jz.awt.listener;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/**
 * This {@link DocumentListener} implementation is designed to route all calls to any {@link DocumentListener} methods
 * to a single method to simplify anonymous simple change listener construction.
 *
 * @author Zachary Palmer
 */
public abstract class DocumentAnyChangeListener implements DocumentListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public DocumentAnyChangeListener()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    public void changedUpdate(DocumentEvent e)
    {
        documentChanged(e);
    }

    /**
     * Gives notification that there was an insert into the document.  The range given by the DocumentEvent bounds the
     * freshly inserted region.
     *
     * @param e the document event
     */
    public void insertUpdate(DocumentEvent e)
    {
        documentChanged(e);
    }

    /**
     * Gives notification that a portion of the document has been removed.  The range is given in terms of what the view
     * last saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    public void removeUpdate(DocumentEvent e)
    {
        documentChanged(e);
    }

    /**
     * Gives notification that the document has been changed in some way.  The underlying, presumably anonymous class
     * must implement this method.
     * @param e The document event which inspired the call.
     */
    public abstract void documentChanged(DocumentEvent e);

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
