package orioni.jz.generics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This {@link HashSet} implementation is designed to take any {@link Set} and cast its contents to fit a generic set.
 * The purpose of this class is to allow the runtime-dependent construction of generic {@link HashSet} objects based
 * upon the contents of a class variable.
 * <p/>
 * Note that this requires a full shallow copy of the source set.
 *
 * @author Zachary Palmer
 */
public class GenericCopyingHashSet<T> extends HashSet<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param c          The class to which we are casting.
     * @param collection The {@link Collection} containing the data which this {@link GenericCopyingHashSet} is to
     *                   contain.
     * @throws ClassCastException If any item in the set is not of type <code>T</code>.
     */
    public GenericCopyingHashSet(Class c, Collection collection)
    {
        super();
        for (Object o : collection)
        {
            if (!c.isInstance(o)) throw new ClassCastException();
            //noinspection unchecked
            this.add((T) (o));
        }
    }

    /**
     * General constructor.
     *
     * @param c       The class to which we are casting.
     * @param objects The {@link Object}s to add to this set.
     * @throws ClassCastException If any item in the set is not of type <code>T</code>.
     */
    public GenericCopyingHashSet(Class c, Object... objects)
    {
        super();
        for (Object o : objects)
        {
            if (!c.isInstance(o)) throw new ClassCastException();
            //noinspection unchecked
            this.add((T) (o));
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE