package orioni.jz.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This {@link Bag} implementation is designed to prevent any form of modification to itself by throwing an {@link
 * UnsupportedOperationException} on write operations.
 *
 * @author Zachary Palmer
 */
public class UnmodifiableBag<T> implements Bag<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The bag which is backing this unmodifiable bag.
     */
    private Bag<T> bag;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param bag The bag which is backing this unmodifiable bag.
     */
    public UnmodifiableBag(Bag<T> bag)
    {
        super();
        this.bag = bag;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param o Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean add(T o)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param t      Ignored.
     * @param number Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean add(T t, int number)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param c Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean addAll(Collection<? extends T> c)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @throws UnsupportedOperationException Always.
     */
    public void clear()
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Determines whether or not this {@link orioni.jz.util.Bag} contains at least one instance of the specified
     * object.
     *
     * @param o The {@link Object} for which containment should be checked.
     * @return <code>true</code> if the provided object is contained within this {@link orioni.jz.util.Bag};
     *         <code>false</code> if it is not.
     */
    public boolean contains(Object o)
    {
        //noinspection SuspiciousMethodCalls
        return bag.contains(o);
    }

    /**
     * Determines whether or not this {@link Bag} contains at least one instance of every object in the specified {@link
     * java.util.Collection}.  Note that this does not imply anything about frequency of containment. If this bag is
     * <code>(0,1)</code> and <code>containsAll(Collection)</code> is called with the bag <code>(0,0,0,0)</code>, then
     * <code>true</code> will be returned.
     */
    public boolean containsAll(Collection<?> c)
    {
        return bag.containsAll(c);
    }

    /**
     * Determines the frequency with which the specified object appears in this {@link orioni.jz.util.Bag}.
     *
     * @param o The object to find in this bag.
     * @return The frequency with which that object appears.
     */
    public int getFrequency(Object o)
    {
        return bag.getFrequency(o);
    }

    /**
     * Retrieves a representation of this bag in the form of a {@link java.util.Map}<code>&lt;T,Integer&gt;</code>.  The
     * keys of that map represent the unique values in this bag; the map's values represent the count of those bag
     * values.
     *
     * @return A map representation of this bag.
     */
    public Map<T, Integer> getMapRepresentation()
    {
        return bag.getMapRepresentation();
    }

    /**
     * Retrieves a {@link java.util.Set} containing the unique objects in this bag.  This set is unmodifiable.
     *
     * @return A {@link java.util.Set} containing all unique elements in this bag.
     */
    public Set<T> getUniqueValues()
    {
        return bag.getUniqueValues();
    }

    /**
     * Determines whether or not this bag is empty.
     *
     * @return <code>true</code> if this bag contains no elements; <code>false</code> otherwise.
     */
    public boolean isEmpty()
    {
        return bag.isEmpty();
    }

    /**
     * Returns an {@link java.util.Iterator} which will iterate over this {@link orioni.jz.util.Bag}.
     *
     * @return An {@link java.util.Iterator} which will iterate over this {@link orioni.jz.util.Bag}.
     */
    public Iterator<T> iterator()
    {
        return bag.iterator();
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param o Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param o      Ignored.
     * @param number Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean remove(Object o, int number)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param c Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param t Ignored.
     * @throws UnsupportedOperationException Always.
     */
    public void removeAllOf(T t)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param c Ignored.
     * @return Never.
     * @throws UnsupportedOperationException Always.
     */
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Throws an exception; this bag does not support write operations.
     *
     * @param bag Ignored.
     * @throws UnsupportedOperationException Always.
     */
    public void setContents(Bag<T> bag)
    {
        throw new UnsupportedOperationException("Write operations are not supported by this bag.");
    }

    /**
     * Returns the number of elements in this bag.  This is not necessarily indicative of the number of <i>unique</i>
     * elements in the bag.
     *
     * @return The number of elements in this bag.
     */
    public int size()
    {
        return bag.size();
    }

    /**
     * Returns the contents of this bag as an array.
     *
     * @return The contents of this bag in an array.
     */
    public Object[] toArray()
    {
        return bag.toArray();
    }

    /**
     * Returns the contents of this bag in a typed array.
     *
     * @return The contents of this bag in a typed array.
     */
    public <T>T[] toArray(T[] a)
    {
        return bag.toArray(a);
    }

    /**
     * Returns the number of <i>unique</i> elements in this bag.  This is equivalent to the cardinality of this bag if
     * it were treated as a set (duplicates being erased).
     *
     * @return The number of unique elements in this bag.
     */
    public int uniqueSize()
    {
        return bag.uniqueSize();
    }

    /**
     * Passes the equality check call to the backing bag.
     *
     * @param o The object to which the backing bag should be compared.
     * @return <code>true</code> if the objects are equal; <code>false</code> if they are not.
     */
    public boolean equals(Object o)
    {
        return bag.equals(o);
    }

    /**
     * Retrieves the hashcode of the backing bag.
     *
     * @return The hashcode of the backing bag.
     */
    public int hashCode()
    {
        return bag.hashCode();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
