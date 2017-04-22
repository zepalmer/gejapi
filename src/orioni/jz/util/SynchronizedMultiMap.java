package orioni.jz.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * This {@link MultiMap} extension synchronizes all calls to the underlying {@link MultiMap}.
 *
 * @author Zachary Palmer
 */
public class SynchronizedMultiMap<K,V> implements MultiMap<K, V>, Serializable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * Allow interoperability between versions by overriding the serial version UID.
     */
    public static final long serialVersionUID = 0x9192939495969798L;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The underlying {@link MultiMap} to which all calls must be synchronized.
     */
    protected MultiMap<K, V> multimap;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param multimap The {@link MultiMap} to which access should be synchronized.
     */
    public SynchronizedMultiMap(MultiMap<K, V> multimap)
    {
        super();
        this.multimap = multimap;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized void clear()
    {
        multimap.clear();
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized boolean containsKey(K key)
    {
        return multimap.containsKey(key);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized boolean containsValue(V value)
    {
        return multimap.containsValue(value);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized Set<V> getAll(K key)
    {
        return multimap.getAll(key);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized int getKeyCount()
    {
        return multimap.getKeyCount();
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized K getKeyFor(V value)
    {
        return multimap.getKeyFor(value);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized int getMappingsForKey(K key)
    {
        return multimap.getMappingsForKey(key);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized int getValuesCount()
    {
        return multimap.getValuesCount();
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized boolean isEmpty()
    {
        return multimap.isEmpty();
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized Set<K> keySet()
    {
        return multimap.keySet();
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized void put(K key, V value)
    {
        multimap.put(key, value);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized boolean remove(K key, V value)
    {
        return multimap.remove(key, value);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized Set<V> removeAll(K key)
    {
        return multimap.removeAll(key);
    }

    /**
     * Calls the underlying multimap in a synchronized manner.
     */
    public synchronized Collection<V> values()
    {
        return multimap.values();
    }

    /**
     * Duplicates the underlying multimap, returning it within another {@link SynchronizedMultiMap}.
     *
     * @return A duplicate of the underlying multimap, wrapped within another {@link SynchronizedMultiMap}.
     */
    public synchronized MultiMap<K, V> duplicate()
    {
        return new SynchronizedMultiMap<K, V>(multimap.duplicate());
    }

    /**
     * Adds a set of key-value pairs to this multimap.  Each value in the set is added to this {@link MultiMap} as a value mapped by the specified key.
     *
     * @param key    The key to add to this map.
     * @param values The {@link Set} of values to map to the specified key.
     */
    public synchronized void put(K key, Set<V> values)
    {
        multimap.put(key, values);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
