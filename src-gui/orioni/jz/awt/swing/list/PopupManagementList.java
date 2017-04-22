package orioni.jz.awt.swing.list;

import orioni.jz.awt.listener.GeneralMethodCallMouseListener;
import orioni.jz.awt.swing.popup.PopupOption;
import orioni.jz.awt.swing.popup.PopupOptionMenu;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This {@link JList} extension is designed to maintain a set of {@link PopupOption} objects.  When an item in the list
 * is right-clicked, the selection of the list is changed to that item and a {@link JPopupMenu} is constructed based
 * upon the {@link PopupOption}s which have been registered thus far.
 * <p/>
 * The popup menu which is produced has separators between the contents of all registration calls.  For example, if one
 * were to register three options in a single array using {@link PopupManagementList#addPopupOptions(PopupOption...)}, no
 * separators would appear; if those same three options were added independently in three different calls to {@link
 * PopupManagementList#addPopupOption(PopupOption...)}, two separators would appear.
 *
 * @author Zachary Palmer
 */
public class PopupManagementList extends JList
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link ArrayList} of {@link PopupOption} arrays which have been registered with this management list.
     */
    protected List<PopupOption[]> popupOptions;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see JList#JList()
     */
    public PopupManagementList()
    {
        super();
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see JList#JList(Object[])
     */
    public PopupManagementList(Object[] data)
    {
        super(data);
        initialize();
    }

    /**
     * Wrapper constructor.
     *
     * @see JList#JList(ListModel)
     */
    public PopupManagementList(ListModel model)
    {
        super(model);
        initialize();
    }

    /**
     * Used to initialize this {@link PopupManagementList}.
     */
    private void initialize()
    {
        popupOptions = new ArrayList<PopupOption[]>();
        final PopupManagementList scopedThis = this;
        this.addMouseListener(
                new GeneralMethodCallMouseListener()
                {
                    public void checkMouseOperation(MouseEvent e)
                    {
                        if (e.isPopupTrigger())
                        {
                            int index = locationToIndex(e.getPoint());
                            if (index == -1) return;
                            setSelectedIndex(index);
                            createPopupMenu().show(scopedThis, e.getX(), e.getY());
                        }
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds an ordered group of popup options to the end of the option list.
     *
     * @param options The {@link PopupOption}<code>[]</code> to add.
     */
    public void addPopupOptions(PopupOption... options)
    {
        popupOptions.add(options);
    }

    /**
     * Adds an ordered group of popup options to the beginning of the option list.
     *
     * @param options The {@link PopupOption}<code>[]</code> to add.
     */
    public void addPopupOptionsToBeginning(PopupOption... options)
    {
        popupOptions.add(0, options);
    }

    /**
     * Adds a list of ordered groups of popup options to the end of the option list.
     *
     * @param options The {@link PopupOption}<code>[]</code> to add.
     */
    public void addPopupOptions(PopupOption[][] options)
    {
        for (PopupOption[] array : options)
        {
            this.addPopupOptions(array);
        }
    }

    /**
     * Adds a list of ordered groups of popup options to the beginning of the option list.
     *
     * @param options The {@link PopupOption}<code>[]</code> to add.
     */
    public void addPopupOptionsToBeginning(PopupOption[][] options)
    {
        for (PopupOption[] array : options)
        {
            this.addPopupOptionsToBeginning(array);
        }
    }

    /**
     * Creates the popup menu for this list.
     *
     * @return The popup menu to use for this list.
     */
    public JPopupMenu createPopupMenu()
    {
        return new PopupOptionMenu(getSelectedValue(), popupOptions);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE