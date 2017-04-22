package orioni.jz.awt.swing;

import orioni.jz.util.ListEnumeration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * This class extends {@link ButtonGroup} to completely override its functionality.  This {@link ButtonGroup} extension
 * permits a state of "no button selected"; this is implemented by returning the selection as the constant {@link
 * ExclusiveButtonGroup#NO_SELECTION}.
 * <p/>
 * In addition to this behavior, the {@link ExclusiveButtonGroup} adds a listener to each button within its group.  When
 * the selection state of the buttons change, this group is notified and reacts appropriately.  This group will also
 * notify its listeners of when the selection in the group has been changed. This information is provided in terms of an
 * index; therefore, the ordering of the buttons within the group is important.  To this end, extra add and remove
 * methods have been provided to allow list manipulation of the group.
 *
 * @author Zachary Palmer
 */
public class ExclusiveButtonGroup extends ButtonGroup
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The constant {@link ButtonModel} which represents no selection in this {@link ButtonGroup}.
     */
    public static final ButtonModel NO_SELECTION = null;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link ArrayList} in which the buttons will be stored.
     */
    protected List<AbstractButton> buttonList;
    /**
     * The {@link HashMap} mapping the buttons which are added to this group to the {@link ActionListener} objects which
     * were attached to them.  This mapping is retained to allow such listeners to be removed when the buttons are
     * removed from this group.
     */
    protected HashMap<ButtonModel, ActionListener> listenerMap;
    /**
     * The {@link ButtonModel} which is currently selected.
     */
    protected ButtonModel selectedButtonModel;
    /**
     * The {@link Set} containing each of the {@link ActionListener} objects to notify when the selection is changed.
     */
    protected Set<ActionListener> listenerSet;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public ExclusiveButtonGroup()
    {
        super();
        super.buttons = null; // discard old data structure

        buttonList = new ArrayList<AbstractButton>();
        listenerMap = new HashMap<ButtonModel, ActionListener>();
        selectedButtonModel = NO_SELECTION;
        listenerSet = new HashSet<ActionListener>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds the button to the end of the group.  If this button is already in the group, it remains at its current
     * index.
     *
     * @param button The button to be added.
     */
    public void add(final AbstractButton button)
    {
        add(button, buttonList.size());
    }

    /**
     * Adds the button to the group at the specified index.  If this button is already in the group, it retains its
     * current index; otherwise, all buttons after and at the specified index have their indices increased by one to
     * make room for this button.
     *
     * @param button The button to be added.
     * @param index  The index at which to add the button.
     * @throws IndexOutOfBoundsException If the specified index is greater than the number of elements in the list or
     *                                   equal to zero.
     */
    public void add(final AbstractButton button, int index)
    {
        if (!buttonList.contains(button))
        {
            buttonList.add(index, button);
            // This listener informs this ExclusiveButtonGroup if the button's state is changed.
            ActionListener listener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    setSelected(button.getModel(), button.getModel().isSelected());
                }
            };
            button.addActionListener(listener);
            listenerMap.put(button.getModel(), listener);
        }
    }

    /**
     * Adds a listener to the listener set.  After this method call, that listener will be notified whenever the
     * selected button is changed.
     * <p/>
     * The {@link ActionEvent#getSource()} method will return this {@link ExclusiveButtonGroup} when called.
     *
     * @param listener The listener to add.
     */
    public void addActionListener(ActionListener listener)
    {
        listenerSet.add(listener);
    }

    /**
     * Fires an action event.  This event will be received by all listeners currently in the listener set.
     *
     * @param event The {@link ActionEvent} to fire.
     */
    protected void fireActionEvent(ActionEvent event)
    {
        for (ActionListener listener : listenerSet)
        {
            listener.actionPerformed(event);
        }
    }

    /**
     * Returns the number of buttons in the group.
     *
     * @return The button count
     */
    public int getButtonCount()
    {
        return buttonList.size();
    }

    /**
     * Returns all the buttons that are participating in this group.
     *
     * @return An <code>Enumeration</code> of the buttons in this group
     */
    public Enumeration<AbstractButton> getElements()
    {
        return new ListEnumeration<AbstractButton>(buttonList);
    }

    /**
     * Returns the model of the selected button.
     *
     * @return the selected button model
     */
    public ButtonModel getSelection()
    {
        return selectedButtonModel;
    }

    /**
     * Returns whether a {@link ButtonModel} is selected.
     *
     * @param m The model to test for selection.
     * @return <code>true</code> if the button is selected, otherwise returns <code>false</code>
     */
    public boolean isSelected(ButtonModel m)
    {
        if (m != null)
        {
            return (m.equals(selectedButtonModel));
        } else
        {
            return (selectedButtonModel == null);
        }
    }

    /**
     * Removes the specified button from the group.  All buttons following the removed buttons will have their indicies
     * reduced by one to fill the gap.
     *
     * @param b The button to be removed.
     */
    public void remove(AbstractButton b)
    {
        buttonList.remove(b);
        listenerMap.remove(b.getModel());
    }

    /**
     * Removes the button with the specified index from the group.  All buttons following the removed buttons will have
     * their indicies reduced by one to fill the gap.
     *
     * @param index The index of the button to be removed.
     * @throws IndexOutOfBoundsException If the specified index is less than zero or greater than or equal to the number
     *                                   of buttons in the group.
     */
    public void remove(int index)
    {
        listenerMap.remove(buttonList.remove(index).getModel());
    }

    /**
     * Removes an action listener from this group.  After this method call, that listener will not be notified of
     * changes to the select button in this group.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(ActionListener listener)
    {
        listenerSet.remove(listener);
    }

    /**
     * Sets the specified {@link ButtonModel} as the selected model.  If the provided {@link ButtonModel} is the {@link
     * ExclusiveButtonGroup#NO_SELECTION} constant, no buttons will be selected.
     *
     * @param model The {@link ButtonModel} of the button to select.
     */
    public void setSelected(ButtonModel model)
    {
        if (!isSelected(model))
        {
            if (selectedButtonModel != null) selectedButtonModel.setSelected(false);
            selectedButtonModel = model;
            if (selectedButtonModel != null) selectedButtonModel.setSelected(true);
            fireActionEvent(new ActionEvent(this, 0, "setSelected"));
        }
    }

    /**
     * Sets the selected value for the {@link ButtonModel}. Only one button in the group may be selected at a time.
     *
     * @param model  The {@link ButtonModel} in question.
     * @param select <code>true</code> if this button is to be selected, otherwise <code>false</code>
     */
    public void setSelected(ButtonModel model, boolean select)
    {
        if (select)
        {
            setSelected(model);
        } else
        {
            if (isSelected(model))
            {
                setSelected(NO_SELECTION);
            }
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}