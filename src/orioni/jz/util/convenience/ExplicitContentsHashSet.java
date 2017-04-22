package orioni.jz.util.convenience;

import java.util.HashSet;

/**
 * This {@link HashSet} extension provides a constructor which permits the caller to explicitly specify the contents of
 * the {@link ExplicitContentsHashSet}.
 *
 * @author Zachary Palmer
 */
public class ExplicitContentsHashSet<T> extends HashSet<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param contents The contents for this set.
     */
    public ExplicitContentsHashSet(T... contents)
    {
        super();
        for (T t : contents) add(t);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
