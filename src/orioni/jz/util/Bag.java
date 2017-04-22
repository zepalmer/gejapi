package orioni.jz.util;

import java.awt.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Bag} is an unordered data structure in which duplicate elements are allowed.  This differs from a set in
 * that sets do not allow duplicates.  For example, the sets <code>(0,1,3,2)</code>, <code>(1,3,2,0)</code>, and
 * <code>(0,0,2,1,1,3,3,3)</code> are all identical.  The bags <code>(0,1,3,2)</code> and <code>(1,3,2,0)</code> are
 * identical, but <code>(0,0,2,1,1,3,3,3)</code> is distinct from the previous two.  Ordering is still irrelevant with
 * respect to repeated elements; that is, <code>(0,0,1)</code> and <code>(0,1,0)</code> are identical bags.
 * <p/>
 * <b>Object Equality:</b>
 * <p/>
 * It should be noted that a bag's removal methods operate solely via the {@link Object#equals(Object)} implementation
 * of the object in question.  This means that objects which may contain different meta-data may seem to be confused
 * when they are managed in the bag.  For example, presume two objects, <code>a</code> and <code>b</code>.  Also presume
 * that <code>a.equals(b)</code> returns <code>true</code>.  Presume as well that <code>a</code> and <code>b</code>
 * differ significantly in some way that does not affect equality (perhaps in a significant field value, in internal
 * caching state, or some other manner).
 * <p/>
 * As <code>a</code> and <code>b</code> are considered "equal", the bags <code>(a,a)</code>, <code>(a,b)</code>, and
 * <code>(b,b)</code> are all considered equal.  Additionally, if both <code>a</code> and <code>b</code> are added to
 * the bag, the bag does not guarantee that references to both or either will be generated, depending upon the state of
 * the bag.  Finally, given a bag <code>(a,b)</code> under the above-prescribed circumstances, removing <code>a</code>
 * from the bag could produce either the bag <code>(a)</code> or the bag <code>(b)</code>.
 * <p/>
 * This form of behavior is common to {@link Collection}s.  For example, in the above-prescribed case, adding both
 * <code>a</code> and <code>b</code> to a {@link Set} would not imply a reference to both or either, depending upon the
 * state of the {@link Set}.  If the objects are equal and both exist in a {@link List}, removing <code>a</code> from
 * the list could result in the list actually releasing <code>a</code> or <code>b</code>.  This is mentioned here,
 * however, as it is slightly unintuitive in the case of the bag.
 *
 * @author Zachary Palmer
 */
public interface Bag<T> extends Collection<T>, Serializable
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * Lock serialization version.
     */
    public static final long serialVersionUID = 0x5;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds the specified element to this bag.  Note that, if it is already present, it is still added.
     *
     * @param o The object to add to this bag.
     * @return <code>true</code>, always, as specified by {@link Collection#add(Object)}.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean add(T o);

    /**
     * Adds the specified element to this bag a given number of times.  Note that, if it is already present, it is still
     * added.
     *
     * @param t      The object to add to this bag.
     * @param number The number of times to add it to this bag.
     * @return <code>true</code>, always, as specified by {@link Collection#add(Object)}.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean add(T t, int number);

    /**
     * Adds all elements in the specified {@link Collection} to this bag.  If they already exist, they are added again
     * anyway.
     *
     * @param c The {@link Collection} of objects to add to this bag.
     * @return <code>true</code> if the {@link Bag} was changed as a result of this call.  This will be the case if and
     *         only if the provided collection is not empty.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean addAll(Collection<? extends T> c);

    /**
     * Empties this {@link Bag}.
     *
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public void clear();

    /**
     * Determines whether or not this {@link Bag} contains at least one instance of the specified object.
     *
     * @param o The {@link Object} for which containment should be checked.
     * @return <code>true</code> if the provided object is contained within this {@link Bag}; <code>false</code> if it
     *         is not.
     */
    public boolean contains(Object o);

    /**
     * Determines whether or not this {@link Bag} contains at least one instance of every object in the specified {@link
     * Collection}.  Note that this does not imply anything about frequency of containment.  If this bag is
     * <code>(0,1)</code> and <code>containsAll(Collection)</code> is called with the bag <code>(0,0,0,0)</code>, then
     * <code>true</code> will be returned.
     */
    public boolean containsAll(Collection<?> c);

    /**
     * Compares this bag to another object for equality.
     *
     * @param o The {@link Object} with which this bag is to be compared.
     * @return <code>true</code> if and only if the provided object is a bag and the two bags contain the same objects
     *         with the same frequency of containment.
     */
    public boolean equals(Object o);

    /**
     * Determines the frequency with which the specified object appears in this {@link Bag}.
     *
     * @param o The object to find in this bag.
     * @return The frequency with which that object appears.
     */
    public int getFrequency(Object o);

    /**
     * Retrieves a {@link Set} containing the unique objects in this bag.  This set is unmodifiable.
     *
     * @return A {@link Set} containing all unique elements in this bag.
     */
    public Set<T> getUniqueValues();

    /**
     * Retrieves a representation of this bag in the form of a {@link Map}<code>&lt;T,Integer&gt;</code>.  The keys of
     * that map represent the unique values in this bag; the map's values represent the count of those bag values.
     *
     * @return A map representation of this bag.
     */
    public Map<T, Integer> getMapRepresentation();

    /**
     * Returns a hash code for this {@link Bag}.
     */
    public int hashCode();

    /**
     * Determines whether or not this bag is empty.
     *
     * @return <code>true</code> if this bag contains no elements; <code>false</code> otherwise.
     */
    public boolean isEmpty();

    /**
     * Returns an {@link Iterator} which will iterate over this {@link Bag}.
     *
     * @return An {@link Iterator} which will iterate over this {@link Bag}.
     */
    public Iterator<T> iterator();

    /**
     * Removes an object from this {@link Bag} if it is present at least once.  Note that, as bags allow repeated
     * elements, the object may be successfully removed and still exist in the bag.
     *
     * @return <code>true</code> if the object was removed; <code>false</code> if it was not.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean remove(Object o);

    /**
     * Removes an object from this {@link Bag} up to a given number of times.  Note that, as bags allow repeated
     * elements, the object may be successfully removed and still exist in the bag.
     *
     * @param o      The object to remove.
     * @param number The number of times to remove that object.
     * @return <code>true</code> if the object was removed the specified number of times; <code>false</code> if it was
     *         removed less than that number of times (including not at all).
     * @throws IllegalArgumentException      If <code>number</code> is less than zero.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean remove(Object o, int number);

    /**
     * Removes all objects in the provided {@link Collection} from this bag.  Each removal is performed independently
     * and thus duplicates in the provided {@link Collection} will affect the bag multiple times.  For example, given a
     * bag <code>(0,1,3,3,3,1,5)</code> and a list <code>(2,1,1,5,3,3,1)</code>, removing all items in the list from the
     * bag produces a resulting bag <code>(0,3)</code>.
     *
     * @param c The {@link Collection} containing the elements to remove.
     * @return <code>true</code> if the bag was changed by this operation; <code>false</code> if it was not.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean removeAll(Collection<?> c);

    /**
     * Removes all instances of the specified object from this bag.
     *
     * @param t The object to remove.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public void removeAllOf(T t);

    /**
     * Retains in this bag only the elements which are also found in the provided {@link Collection}.  Retentions
     * respect the number of occurrences of a given object in both the collection and the bag.  For example, given a bag
     * <code>(0,1,3,3,3,1,5)</code> and a list <code>(2,1,1,5,3,3,1)</code>, retaining only the items in the list
     * produces a resulting bag <code>(1,1,3,3,5)</code>.
     *
     * @param c The {@link Collection} containing the elements to retain.
     * @return <code>true</code> if the bag was changed by this operation; <code>false</code> if it was not.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public boolean retainAll(Collection<?> c);

    /**
     * Sets the contents of this bag to be equal to the contents of another bag.
     *
     * @param bag The bag to copy.  If <code>null</code>, this bag is emptied.
     * @throws UnsupportedOperationException If this operation is not supported by this bag.
     */
    public void setContents(Bag<T> bag);

    /**
     * Returns the number of elements in this bag.  This is not necessarily indicative of the number of <i>unique</i>
     * elements in the bag.
     *
     * @return The number of elements in this bag.
     */
    public int size();

    /**
     * Returns the contents of this bag as an array.
     *
     * @return The contents of this bag in an array.
     */
    public Object[] toArray();

    /**
     * Returns the contents of this bag in a typed array.
     *
     * @return The contents of this bag in a typed array.
     */
    public <T> T[] toArray(T[] a);

    /**
     * Returns the number of <i>unique</i> elements in this bag.  This is equivalent to the cardinality of this bag if
     * it were treated as a set (duplicates being erased).
     *
     * @return The number of unique elements in this bag.
     */
    public int uniqueSize();
}

// END OF FILE