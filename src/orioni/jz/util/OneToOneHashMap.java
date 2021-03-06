package orioni.jz.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * This {@link AbstractMap} implementation adds to the {@link Map} interface the guarantee that the contents of this map
 * will be one-to-one; that is, if a key-value pair is added and the value is already present in the map, the old
 * key-value pair is <i>replaced</i> with the new one.  This is similar to how a {@link Map} already behaves if the
 * <i>key</i> is already present.
 *
 * @author Zachary Palmer
 */
public class OneToOneHashMap<K,V> extends AbstractMap<K, V>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link Map}.
     */
    protected Map<K,V> map;
    /**
     * The underlying reverse {@link Map}.
     */
    protected Map<V,K> reverseMap;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public OneToOneHashMap()
    {
        super();
        map = new HashMap<K,V>();
        reverseMap = new HashMap<V,K>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns a set view of the mappings contained in this map.  Each element in this set is a Map.Entry.  The set is
     * backed by the map, so changes to the map are reflected in the set, and vice-versa.  (If the map is modified while
     * an iteration over the set is in progress, the results of the iteration are undefined.)  The set supports element
     * removal, which removes the corresponding entry from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map.
     */
    public Set<Entry<K, V>> entrySet()
    {
        return map.entrySet();
    }

    /**
     * Associates the specified value with the specified key in this map (optional operation).  If the map previously
     * contained a mapping for this key, the old value is replaced.<p>
     *
     * @param key   key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt> if there was no mapping for key.  (A
     *         <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt> with the
     *         specified key, if the implementation supports <tt>null</tt> values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation is not supported by this map.
     * @throws ClassCastException            if the class of the specified key or value prevents it from being stored in
     *                                       this map.
     * @throws IllegalArgumentException      if some aspect of this key or value * prevents it from being stored in this
     *                                       map.
     * @throws NullPointerException          if this map does not permit <tt>null</tt> keys or values, and the specified
     *                                       key or value is <tt>null</tt>.
     */
    public V put(K key, V value)
    {
        V oldValue = map.remove(key);
        K oldKey = reverseMap.remove(value);

        map.remove(oldKey);
        reverseMap.remove(oldValue);

        map.put(key, value);
        reverseMap.put(value, key);

        return oldValue;
    }

    /**
     * Retrieves a key in this map based on the provided value.
     * @param value The value for which a result should be retrieved.
     * @return The key for that value.
     */
    public K getKeyByValue(V value)
    {
        return reverseMap.get(value);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves an instance of this class which is generated by adding each of the provided key-value pairs in turn
     * to a new mapping.
     * @param pairs The key-value {@link Pair}s to use.
     * @return The resulting mapping.
     */
    public static <K,V> OneToOneHashMap<K,V> createFromPairs(Pair<K,V>... pairs)
    {
        OneToOneHashMap<K,V> ret = new OneToOneHashMap<K,V>();
        for (Pair<K,V> pair : pairs)
        {
            ret.put(pair.getFirst(), pair.getSecond());
        }
        return ret;
    }
}

// END OF FILE
