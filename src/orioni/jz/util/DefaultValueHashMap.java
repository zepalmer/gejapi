package orioni.jz.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This {@link HashMap} extension assumes that any key which does not currently have a mapping is instead mapped to a
 * default value.  The following facts about the operation of {@link DefaultValueHashMap} should be noted: <UL>
 * <LI>Calls to {@link DefaultValueHashMap#remove(Object)} given a specific key do not cause future calls to {@link
 * DefaultValueHashMap#get(Object)} to return <code>null</code>; they will instead return the default value.</LI>
 * <LI>Calling {@link DefaultValueHashMap#put(Object, Object) put(K,V)} is equivalent in all ways to calling {@link
 * DefaultValueHashMap#remove(Object) remove(K)} if <code>V</code> is equal to the default value.</LI> <LI>Iterating
 * through the values in the map therefore provides values which are not equal to the default value.  All of the rest of
 * the keys in the key space are assumed to be mapped to the default value.</LI> <LI>Putting <code>null</code> as a
 * value in this map is equivalent to putting the default value.</UL>
 *
 * @author Zachary Palmer
 */
public class DefaultValueHashMap<K,V> extends HashMap<K, V>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The default value to return in the event that no mapping exists for a key.
     */
    protected V defaultValue;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes the map with no contents.
     *
     * @param def The default value to return in the event that no mapping is found.
     */
    public DefaultValueHashMap(V def)
    {
        super();
        defaultValue = def;
    }

    /**
     * General constructor.
     *
     * @param initial The map containing the initial mappings for this map.
     * @param def     The default value to return in the event that no mapping is found.
     */
    public DefaultValueHashMap(Map<K, V> initial, V def)
    {
        super();
        for (Map.Entry<K, V> entry : initial.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
        defaultValue = def;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the value to which the specified key is mapped in this identity hash map, or the default value if the map
     * contains no mapping for this key.  The <tt>containsKey</tt> method may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which this map maps the specified key, or <tt>null</tt> if the map contains no mapping for
     *         this key.
     * @see #put(Object, Object)
     */
    public V get(Object key)
    {
        // We have no choice... the Map interface requires that this method call be suspicious.
        //noinspection SuspiciousMethodCalls
        V ret = super.get(key);
        if (ret == null) ret = defaultValue;
        return ret;
    }

    /**
     * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
     * this key, the old value is replaced.
     *
     * @param k key with which the specified value is to be associated.
     * @param v value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt> if there was no mapping for key.  A
     *         <tt>null</tt> return can also indicate that the HashMap previously associated <tt>null</tt> with the
     *         specified key.
     */
    public V put(K k, V v)
    {
        if ((v == null) || (v.equals(defaultValue)))
        {
            return this.remove(k);
        } else
        {
            V ret = super.put(k, v);
            if (ret == null) ret = defaultValue;
            return ret;
        }
    }

    /**
     * Copies all of the mappings from the specified map to this map These mappings will replace any mappings that this
     * map had for any of the keys currently in the specified map.
     *
     * @param map mappings to be stored in this map.
     * @throws NullPointerException if the specified map is null.
     */
    public void putAll(Map<? extends K, ? extends V> map)
    {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE