package orioni.jz.util.comparator;

import java.util.Comparator;

/**
 * Compares the provided objects by their classes rather than their contents.  Another comparator is wrapped to do
 * the class comparison; this comparator just extracts the classes and passes them to that comparator.
 *
 * @author Zachary Palmer
 */
public class ClassComparator<T> implements Comparator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The comparator which is used to compare the classes.
     */
    protected Comparator<Class> classComparator;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param comparator The comparator to use in the actual comparison of classes.
     */
    public ClassComparator(Comparator<Class> comparator)
    {
        super();
        classComparator = comparator;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
     */
    public int compare(T a, T b)
    {
        return classComparator.compare(a.getClass(), b.getClass());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
