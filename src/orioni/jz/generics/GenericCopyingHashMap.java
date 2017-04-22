package orioni.jz.generics;

import java.util.HashMap;
import java.util.Map;

/**
 * This {@link HashMap} implementation is designed to accept a {@link Map}<code>&lt;?, ?&gt;</code> at runtime and copy
 * it, element by element, into this map.  This map can then be used in type-safe operations and to pass type checking.
 * This effectively trades CPU time for the right to prevent the Java compiler from producing warnings.  It also has the
 * benefit of checking the types at construction time, rather than later during iteration.
 *
 * @author Zachary Palmer
 */
public class GenericCopyingHashMap <K,V> extends HashMap<K, V>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param map The {@link Map} from which data should be copied.
     * @throws ClassCastException If one of the keys in the map is not of type <code>K</code> or one of the values in
     *                            the map is not of type <code>V</code>.
     */
    public GenericCopyingHashMap(Map map)
    {
        super();
        for (Object key : map.keySet())
        {
            this.put((K) key, (V) (map.get(key)));
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE