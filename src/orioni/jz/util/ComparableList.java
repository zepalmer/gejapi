package orioni.jz.util;

import java.util.ArrayList;

/**
 * This {@link java.util.List} implementation only contains {@link Comparable} objects.  As such, it itself has a
 * natural ordering.
 *
 * @author Zachary Palmer
 */
public class ComparableList<T extends Comparable<T>> extends ArrayList<T> implements Comparable<ComparableList<T>>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public ComparableList()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares this object with the specified object for order.  Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.  The natural ordering of a
     * {@link ComparableList} is the natural ordering of all of its elements in succession; that is, it is based on
     * the natural ordering of the first elements unless they are equal, in which case it is based on the natural
     * ordering of the second elements, and so on.  If the lists are equal in all of the applicable contents, the
     * shorter list is less than the longer list.  If the lists' lengths are equal, the lists are equal.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(ComparableList<T> o)
    {
        int loopmax = Math.min(this.size(), o.size());
        for (int i=0;i<loopmax;i++)
        {
            int comparison = this.get(i).compareTo(o.get(i));
            if (comparison!=0) return comparison;
        }
        if (this.size()<o.size()) return -1;
        if (this.size()>o.size()) return 1;
        return 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
