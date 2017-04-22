package orioni.jz.generics;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This {@link ArrayList} extension is designed to force runtime type-checking on a collection and add that collection
 * to a list.
 *
 * @author Zachary Palmer
 */
public class GenericCopyingArrayList<T> extends ArrayList<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public GenericCopyingArrayList(Class c, Collection collection)
    {
        super();
        for (Object o : collection)
        {
            if (!c.isInstance(o)) throw new ClassCastException("Found " + o.getClass() + ", expected " + c);
            //noinspection unchecked
            this.add((T) (o));
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
