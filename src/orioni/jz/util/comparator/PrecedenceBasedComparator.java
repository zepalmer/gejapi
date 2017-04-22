package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * This {@link Comparator} implementation accepts a list of comparators on construction.  When two objects are compared,
 * it checks the first comparator.  If that comparator returns a non-zero value, that value is returned; otherwise, the
 * next comparator is checked.  This process continues until a comparator returns a non-zero value or this comparator
 * runs out of sub-comparators to use (in which case, <code>0</code> is returned).
 *
 * @author Zachary Palmer
 */
public class PrecedenceBasedComparator <T> implements Comparator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Comparator}s to use, in order of their precedence.
     */
    protected Comparator<? super T>[] comparators;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param subcomparators The {@link Comparator}s to use, in order of their precedence.
     */
    public PrecedenceBasedComparator(Comparator<? super T>... subcomparators)
    {
        super();
        comparators = subcomparators;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares its two arguments for order using the comparator list specified upon construction.
     *
     * @param oa the first object to be compared.
     * @param ob the second object to be compared.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(T oa, T ob)
    {
        for (Comparator<? super T> c : comparators)
        {
            int ret = c.compare(oa, ob);
            if (ret != 0) return ret;
        }
        return 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE