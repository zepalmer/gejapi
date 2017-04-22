package orioni.jz.util.convenience;

import java.util.Collection;
import java.util.HashSet;

/**
 * This {@link HashSet} extension provides convenience methods which modify this set and return it.  This allows a chain
 * of simple modifications to be performed without the set consuming a local variable, potentially allowing for more
 * compact or more organized code.
 *
 * @author Zachary Palmer
 */
public class SelfReturningHashSet<T> extends HashSet<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     */
    public SelfReturningHashSet()
    {
        super();
    }

    /**
     * Wrapper constructor.
     */
    public SelfReturningHashSet(Collection<? extends T> c)
    {
        super(c);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds the provided elements to this set.
     *
     * @param c The {@link Collection} of elements to add.
     * @return This set.
     */
    public SelfReturningHashSet<T> plus(Collection<? extends T> c)
    {
        this.addAll(c);
        return this;
    }

    /**
     * Removes the provided elements from this set.
     *
     * @param c The {@link Collection} of elements to remove.
     * @return This set.
     */
    public SelfReturningHashSet<T> minus(Collection<? extends T> c)
    {
        this.removeAll(c);
        return this;
    }

    /**
     * Retains only the elements in the provided collection.
     *
     * @param c The {@link Collection} of elements to retain.
     * @return This set.
     */
    public SelfReturningHashSet<T> intersect(Collection<? extends T> c)
    {
        this.retainAll(c);
        return this;
    }

    /**
     * Retains the elements in this set as well as those in the provided collection.  Note that this is equivalent to
     * addition.
     *
     * @param c The {@link Collection} of elements to include in this set.
     * @return This set.
     */
    public SelfReturningHashSet<T> union(Collection<? extends T> c)
    {
        this.addAll(c);
        return this;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
