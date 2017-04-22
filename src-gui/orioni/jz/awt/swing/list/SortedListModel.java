package orioni.jz.awt.swing.list;

import orioni.jz.util.Utilities;
import orioni.jz.util.comparator.CaseInsensitiveAlphabeticalToStringComparator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.List;

/**
 * This extension of {@link DefaultListModel} provides two additional methods for list operation: {@link
 * SortedListModel#addSorted(Object o)} and {@link SortedListModel#sort()}.  The first method is designed to add an
 * object to the list in such a way that it is sorted by the result of the call <code>o.toString()</code> in comparison
 * to the other elements of the list.  The second call sorts the entire list.
 * <p/>
 * This class also implements {@link ComboBoxModel} to allow sorted combo boxes to use it.
 *
 * @author Zachary Palmer
 */
public class SortedListModel extends DefaultListModel implements ComboBoxModel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The currently selected item.
     */
    protected Object selectedItem;
    /**
     * The {@link Comparator} that is being used to sort this list.
     */
    protected Comparator comparator;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes no initial data to be stored in the model and that the comparator to be used
     * initially is the {@link CaseInsensitiveAlphabeticalToStringComparator}.
     */
    public SortedListModel()
    {
        this(Utilities.EMPTY_OBJECT_ARRAY, false, CaseInsensitiveAlphabeticalToStringComparator.SINGLETON);
    }

    /**
     * Skeleton constructor.  Assumes no initial data to be stored in the model.
     * @param comparator The {@link Comparator} to use when sorting this list.
     */
    public SortedListModel(Comparator comparator)
    {
        this(Utilities.EMPTY_OBJECT_ARRAY, false, comparator);
    }

    /**
     * Skeleton constructor.  Assumes that the comparator that will be used is the {@link
     * AlphabeticalToStringComparator}.
     *
     * @param initial The initial objects to be stored in this model.
     * @param sorted  Whether or not the initial objects should be sorted.  If this value is <code>true</code>, the
     *                {@link SortedListModel#sort()} method will be called after the initial items are added.  If this
     *                value is <code>false</code>, they will be added in the order in which they appear in the array.
     */
    public SortedListModel(Object[] initial, boolean sorted)
    {
        this(initial, sorted, new CaseInsensitiveAlphabeticalToStringComparator());
    }

    /**
     * General constructor.
     *
     * @param initial    The initial objects to be stored in this model.
     * @param sorted     Whether or not the initial objects should be sorted.  If this value is <code>true</code>, the
     *                   {@link SortedListModel#sort()} method will be called after the initial items are added.  If
     *                   this value is <code>false</code>, they will be added in the order in which they appear in the
     *                   array.
     * @param comparator The {@link Comparator} to use when sorting this list.
     */
    public SortedListModel(Object[] initial, boolean sorted, Comparator comparator)
    {
        this.comparator = comparator;
        for (final Object item : initial)
        {
            this.addElement(item);
        }
        if ((sorted) && (initial.length > 0)) sort();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds an element to the list in a sorted manner.  The sort is dependent upon the {@link Comparator} provided on
     * construction.  If the list is not currently sorted (that is, if any add operation has been called other than
     * {@link SortedListModel#addSorted(Object o)} since the last call to {@link SortedListModel#sort()} or since
     * construction), the behavior of this method is unspecified.
     * <p/>
     * This method makes no promises regarding its behavior if the object being added to the list contains the same
     * value of <code>toString()</code> as an object already within the list other than that the object will be added to
     * the list in a sorted fashion relative to objects with non-matching <code>toString()</code> calls.
     *
     * @param o The object which is being added to the list in a sorted fashion.
     * @return The index at which the object was inserted into the model.
     */
    public int addSorted(Object o)
    {
        // The following will perform a binary insertion of the new contents
        int rangeLeft = 0;
        int rangeRight = super.size() - 1;
        Object left;
        Object right;
        int ret = -1;
        while (ret == -1)
        {
            int index = rangeLeft + (rangeRight - rangeLeft) / 2;
            if (index > 0) left = (get(index - 1)); else left = null;
            if (index < super.size()) right = (get(index)); else right = null;
            // Determine if this position is too far right
            if ((left != null) && (comparator.compare(o, left) < 0))
            {
                rangeRight = rangeLeft + (rangeRight - rangeLeft) / 2;
            } else
            {
                // Determine if this position is too far left
                if ((right != null) && (comparator.compare(o, right) >= 0) && (rangeRight > rangeLeft))
                {
                    rangeLeft = rangeRight - (rangeRight - rangeLeft) / 2;
                } else
                {
                    if ((right != null) && (comparator.compare(o, right) >= 0)) index++;
                    // This position is apparently appropriate.  The call to super.add(int,Object) will take care
                    // of the call to fireIntervalAdded(int,int) for us.
                    super.add(index, o);
                    ret = index; // complete!
                }
            }
        }
        return ret;
    }

    /**
     * Retrieves all elements in this {@link SortedListModel} in an {@link ArrayList}.
     *
     * @return An {@link ArrayList} containing all elements within this {@link SortedListModel}.
     */
    public List getAll()
    {
        List ret = new ArrayList();
        for (int i = 0; i < getSize(); i++)
        {
            ret.add(get(i));
        }
        return ret;
    }

    /**
     * Sorts the list by the value of the {@link Object#toString()} call of each contained object.  This method will
     * dump the contents of the list to a data structure, sort the data structure, remove the contents of the list, and
     * re-add those contents.  This method is not efficient in comparison to the {@link
     * SortedListModel#addSorted(Object)} method and is expected to exhibit <code>n*log(n)</code> behavior.
     */
    public void sort()
    {
        int oldSize = getSize();
        TreeSet set = new TreeSet(comparator);
        for (int i = 0; i < oldSize; i++)
        {
            set.add(get(i));
        }
        super.clear();
        for (final Object element : set)
        {
            super.addElement(element);
        }
        super.fireContentsChanged(this, 0, Math.max(oldSize, getSize()));
    }

    /**
     * Changes the comparator being used by this {@link SortedListModel}.  After this occurs, all elements in the list
     * are resorted.
     *
     * @param comparator The new {@link Comparator} to use.
     */
    public void setComparator(Comparator comparator)
    {
        this.comparator = comparator;
        sort();
    }

    /**
     * Locates an object in this list by index.  This operation takes linear time, as one cannot guarantee that this
     * list model is sorted at all times.
     * @param object The object to find in the list model.
     * @return The index of the item, or <code>-1</code> if the item is not in the model.
     */
    public int getIndexOf(Object object)
    {
        for (int i=0;i<getSize();i++)
        {
            if (object.equals(getElementAt(i))) return i;
        }
        return -1;
    }

// NON-STATIC METHODS : COMBO BOX MODEL IMPLEMENTATION ///////////////////////////

    /**
     * Set the selected item.  This implementation notifies all registered {@link javax.swing.event.ListDataListener}
     * objects that the lists's contents have changed.
     *
     * @param selection the list object to select or <code>null</code> to clear the selection
     */
    public void setSelectedItem(Object selection)
    {
        // If the selection actually changes, fire the update.
        if (((selection == null) ^ (selectedItem == null)) ||
            (!((selection != null) && (selection.equals(selectedItem)))))
        {
            selectedItem = selection;
            super.fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * Returns the currently selected item.
     *
     * @return The selected item, or <code>null</code> if there is no selection.
     */
    public Object getSelectedItem()
    {
        return selectedItem;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

}