package orioni.jz.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * This implementation of {@link MultiMap} creates a read-only view of a {@link MultiMap} specified upon construction.
 * This allows code modules with a {@link MultiMap} of information which needs to be communicated to but not modified by
 * another code module to do so without risking corruption or duplicating the data.
 *
 * @author Zachary Palmer
 */
public class UnmodifiableMultiMap<K,V> implements MultiMap<K, V>, Serializable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * Allow interoperability by overriding the serial version UID.
     */
    public static final long serialVersionUID = 0x192837465FECDBAL;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link MultiMap} which backs this one.
     */
    protected MultiMap<K, V> multimap;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param multimap The {@link MultiMap} for which a view should be provided.
     */
    public UnmodifiableMultiMap(MultiMap<K, V> multimap)
    {
        super();
        this.multimap = multimap;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Throws an {@link UnsupportedOperationException}.  This is a write-mode operation and is not permitted by an
     * {@link UnmodifiableMultiMap}.
     *
     * @throws UnsupportedOperationException Always.
     */
    public void clear()
    {
        throw new UnsupportedOperationException("This Multimap is unmodifiable");
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @param key The key for which mappings should be checked.
     * @return The return value of the same call to the underlying multimap.
     */
    public boolean containsKey(K key)
    {
        return multimap.containsKey(key);
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @param value The value to be found in the multimap.
     * @return The return value of the same call to the underlying multimap.
     */
    public boolean containsValue(V value)
    {
        return multimap.containsValue(value);
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @param key The key for which the number of values should be retrieved.
     * @return The return value of the same call to the underlying multimap.
     */
    public Set<V> getAll(K key)
    {
        return multimap.getAll(key);
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @return The return value of the same call to the underlying multimap.
     */
    public int getKeyCount()
    {
        return multimap.getKeyCount();
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @param value The value for which to find a key.
     * @return The return value of the same call to the underlying multimap.
     */
    public K getKeyFor(V value)
    {
        return multimap.getKeyFor(value);
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @param key The key for which mappings need to be determined.
     * @return The return value of the same call to the underlying multimap.
     */
    public int getMappingsForKey(K key)
    {
        return multimap.getMappingsForKey(key);
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @return The return value of the same call to the underlying multimap.
     */
    public int getValuesCount()
    {
        return multimap.getValuesCount();
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @return The return value of the same call to the underlying multimap.
     */
    public boolean isEmpty()
    {
        return multimap.isEmpty();
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @return The return value of the same call to the underlying multimap.
     */
    public Set<K> keySet()
    {
        return multimap.keySet();
    }

    /**
     * Throws an {@link UnsupportedOperationException}.  This is a write-mode operation and is not permitted by an
     * {@link UnmodifiableMultiMap}.
     *
     * @throws UnsupportedOperationException Always.
     */
    public void put(K key, V value)
    {
        throw new UnsupportedOperationException("This Multimap is unmodifiable");
    }

    /**
     * Throws an {@link UnsupportedOperationException}.  This is a write-mode operation and is not permitted by an
     * {@link UnmodifiableMultiMap}.
     *
     * @throws UnsupportedOperationException Always.
     */
    public boolean remove(K key, V value)
    {
        throw new UnsupportedOperationException("This Multimap is unmodifiable");
    }

    /**
     * Throws an {@link UnsupportedOperationException}.  This is a write-mode operation and is not permitted by an
     * {@link UnmodifiableMultiMap}.
     *
     * @throws UnsupportedOperationException Always.
     */
    public Set<V> removeAll(K key)
    {
        throw new UnsupportedOperationException("This Multimap is unmodifiable");
    }

    /**
     * Passes the call to the underlying multimap, returning what it returned.
     *
     * @return The return value of the same call to the underlying multimap.
     */
    public Collection<V> values()
    {
        return multimap.values();
    }

    /**
     * Duplicates the underlying multimap, returning it as a modifiable {@link MultiMap}.
     *
     * @return A duplicate, modifiable version of the underlying {@link MultiMap}.
     */
    public MultiMap<K, V> duplicate()
    {
        return multimap.duplicate();
    }

    /**
     * Throws an {@link UnsupportedOperationException}.  This is a write-mode operation and is not permitted by an
     * {@link UnmodifiableMultiMap}.
     *
     * @throws UnsupportedOperationException Always.
     */
    public void put(K key, Set<V> values)
    {
        throw new UnsupportedOperationException("This Multimap is unmodifiable");
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
