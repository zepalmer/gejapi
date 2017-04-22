package orioni.jz.awt.swing.list;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.HashSet;
import java.util.Set;

/**
 * This {@link ListModel} contains a series of {@link Integer} objects.  The ranges are specified in the {@link
 * IntegerRangeListModel#setMinimum(int)} and {@link IntegerRangeListModel#setMaximum(int)} fields.
 *
 * @author Zachary Palmer
 */
public class IntegerRangeListModel implements ListModel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The minimum for this model, exclusive.
     */
    protected int minimum;
    /**
     * The maximum for this model, exclusive.
     */
    protected int maximum;

    /**
     * The {@link Set} of {@link ListDataListener}s currently listening to this model.
     */
    protected Set<ListDataListener> listenerSet;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param minimum The initial minimum for this model, inclusive.
     * @param maximum The initial maximum for this model, exclusive.
     */
    public IntegerRangeListModel(int minimum, int maximum)
    {
        super();
        this.minimum = minimum;
        this.maximum = maximum;
        listenerSet = new HashSet<ListDataListener>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a listener to the list that's notified each time a change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    public void addListDataListener(ListDataListener l)
    {
        listenerSet.add(l);
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    public Object getElementAt(int index)
    {
        if ((index < 0) || (index >= getSize()))
        {
            throw new IndexOutOfBoundsException(
                    "Index " + index + "out of bounds [" + minimum + "," + maximum + ")");
        }
        return (index + minimum);
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    public int getSize()
    {
        return Math.max(0, maximum - minimum);
    }

    /**
     * Removes a listener from the list that's notified each time a change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    public void removeListDataListener(ListDataListener l)
    {
        listenerSet.remove(l);
    }

    /**
     * Sets the minimum value for this {@link IntegerRangeListModel}.
     *
     * @param minimum The new minimum value (inclusive).
     */
    public void setMinimum(int minimum)
    {
        this.minimum = minimum;
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
        for (ListDataListener listener : listenerSet)
        {
            listener.contentsChanged(event);
        }
    }

    /**
     * Sets the maximum value for this {@link IntegerRangeListModel}.
     *
     * @param maximum The new maximum value (exclusive).
     */
    public void setMaximum(int maximum)
    {
        this.maximum = maximum;
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
        for (ListDataListener listener : listenerSet)
        {
            listener.contentsChanged(event);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE