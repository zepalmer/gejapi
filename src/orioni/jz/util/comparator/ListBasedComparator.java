package orioni.jz.util.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This comparator is designed to impose an arbitrary ordering on a class of objects specified by a provided list. Any
 * elements not within that list all tie for a position at the end of said list.  For all items which appear in the
 * list, the ordering imposed is the same as that which appears in the list.
 * <p/>
 * In order for this comparator to work properly, it is advised that the list contain only unique elements.  Otherwise,
 * the duplicates may not behave as intended.
 *
 * @author Zachary Palmer
 */
public class ListBasedComparator<T> implements Comparator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link List} by which ordering is imposed.
     */
    protected List<T> list;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.  Permits the use of a varargs parameter for convenience.
     *
     * @param ordering The ordering to impose.
     */
    public ListBasedComparator(T... ordering)
    {
        this(Arrays.asList(ordering));
    }

    /**
     * General constructor.
     *
     * @param list The list by which ordering is imposed.
     */
    public ListBasedComparator(List<T> list)
    {
        super();
        this.list = list;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(T a, T b)
    {
        int ai = list.indexOf(a);
        int bi = list.indexOf(b);
        if (ai == -1)
        {
            if (bi == -1)
            {
                return 0;
            } else
            {
                return 1;
            }
        } else
        {
            if (bi == -1)
            {
                return -1;
            } else
            {
                if (ai < bi) return -1;
                if (ai > bi) return 1;
                return 0;
            }
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
