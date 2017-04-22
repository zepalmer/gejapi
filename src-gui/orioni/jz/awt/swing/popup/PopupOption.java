package orioni.jz.awt.swing.popup;

/**
 * Instances of this class are designed to represent an option in a popup menu.  This class is subclassed to provide
 * functionality which occurs when this option is selected.
 *
 * @author Zachary Palmer
 */
public abstract class PopupOption
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The text for this {@link PopupOption}. */
    protected String text;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  A blank string is used for text.
     */
    public PopupOption()
    {
        this("");
    }

    /**
     * General constructor.
     * @param text The text to be used to label the popup menu option.
     */
    public PopupOption(String text)
    {
        super();
        this.text = text;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the text for this {@link PopupOption}.
     *
     * @return The text for this {@link PopupOption}.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Called whenever this option is selected from a popup menu.
     *
     * @param selection The selected object in the component which produced the popup.  If this does not apply or if no
     *                  object is selected, this parameter will be <code>null</code>.
     */
    public abstract void execute(Object selection);

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE