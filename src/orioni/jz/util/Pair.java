package orioni.jz.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class is designed to represent a pairing of objects.  It is similar to the C++ struct <code>Pair</code>, as it
 * accepts two objects and merely holds them until they are requested.
 * <p/>
 * A {@link Pair} is equal to another {@link Pair} if each object the first {@link Pair} contains is equal to the
 * corresponding object in the other {@link Pair}.
 *
 * @author Zachary Palmer
 */
public class Pair<A, B> implements Serializable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * Force serialization version.
     */
    public static final long serialVersionUID = 0x8;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The first object in the pairing.
     */
    protected A first;
    /**
     * The second object in the pairing.
     */
    protected B second;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param first  The first object in the pairing.
     * @param second The second object in the pairing.
     */
    public Pair(A first, B second)
    {
        super();
        this.first = first;
        this.second = second;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the first object in the pairing.
     *
     * @return The first object in the pairing.
     */
    public A getFirst()
    {
        return first;
    }

    /**
     * Retrieves the second object in the pairing.
     *
     * @return The second object in the pairing.
     */
    public B getSecond()
    {
        return second;
    }

    /**
     * Determines whether or not this {@link Pair} is equal to another object.  This is only the case if the other
     * object is also a {@link Pair} and meets the terms of {@link Pair#equals(Pair)}.
     *
     * @param o The object to compare.
     * @return <code>true</code> if the objects are equal; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (o instanceof Pair)
        {
            try
            {
                return equals((Pair<A, B>) o);
            } catch (ClassCastException cce)
            {
                return false;
            }
        } else
        {
            return false;
        }
    }

    /**
     * Determines whether or not this {@link Pair} is equal to another {@link Pair}.  This is true if each object this
     * pair represents is equal to the corresponding object on the other pair.
     *
     * @param other The other {@link Pair}.
     * @return <code>true</code> if they are equal; <code>false</code> otherwise.
     */
    public boolean equals(Pair<A, B> other)
    {
        return ((first.equals(other.getFirst())) && (second.equals(other.getSecond())));
    }

    /**
     * Returns a hash code for this {@link Pair}.
     */
    public int hashCode()
    {
        return first.hashCode() ^ second.hashCode();
    }

    /**
     * Retrieves a string which describes this pair using the {@link Object#toString()} results from the two objects
     * contained within.
     *
     * @return A string describing this object.
     */
    public String toString()
    {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This {@link Comparator} compares two {@link Pair} objects by the contents of their first objects.
     *
     * @author Zachary Palmer
     */
    public static class FirstElementComparator<T extends Comparable<T>,U> implements Comparator<Pair<T, U>>
    {
        /**
         * Compares two pairs for order.  Returns a negative integer, zero, or a positive integer as the first argument
         * is less than, equal to, or greater than the second.
         *
         * @param pairA The first pair to be compared.
         * @param pairB The second pair to be compared.
         * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or
         *         greater than the second.
         * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
         */
        public int compare(Pair<T, U> pairA, Pair<T, U> pairB)
                throws ClassCastException
        {
            return pairA.getFirst().compareTo(pairB.getFirst());
        }
    }

    /**
     * This {@link Comparator} compares two {@link Pair} objects by the contents of their second objects.
     *
     * @author Zachary Palmer
     */
    public static class SecondElementComparator<T,U extends Comparable<U>> implements Comparator<Pair<T, U>>
    {
        /**
         * Compares two pairs for order.  Returns a negative integer, zero, or a positive integer as the first argument
         * is less than, equal to, or greater than the second.
         *
         * @param pairA The first pair to be compared.
         * @param pairB The second pair to be compared.
         * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or
         *         greater than the second.
         * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
         */
        public int compare(Pair<T, U> pairA, Pair<T, U> pairB)
                throws ClassCastException
        {
            return pairA.getSecond().compareTo(pairB.getSecond());
        }
    }

    /**
     * This {@link Comparator} compares two {@link Pair} objects by the contents of their first objects.  The comparator
     * specified on construction is used to compare them.
     *
     * @author Zachary Palmer
     */
    public static class FirstElementComparatorWrapper<T,U> implements Comparator<Pair<T, U>>
    {
        /**
         * The comparator which will be used to compare the elements.
         */
        protected Comparator<T> comparator;

        /**
         * General constructor.
         *
         * @param comparator The {@link Comparator} that will be used to compare the elements in the pairs.
         */
        public FirstElementComparatorWrapper(Comparator<T> comparator)
        {
            this.comparator = comparator;
        }

        /**
         * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
         * element in the first argument is less than, equal to, or greater than the first element in the second.
         *
         * @param objA the first object to be compared.
         * @param objB the second object to be compared.
         * @return A negative integer, zero, or a positive integer as the first element in the first argument is less
         *         than, equal to, or greater than the first element in the second.
         * @throws ClassCastException if the arguments' types prevent them from being compared by this Comparator.
         */
        public int compare(Pair<T, U> objA, Pair<T, U> objB)
        {
            return comparator.compare(objA.getFirst(), objB.getFirst());
        }
    }

    /**
     * This {@link Comparator} compares two {@link Pair} objects by the contents of their second objects.  The
     * comparator specified on construction is used to compare them.
     *
     * @author Zachary Palmer
     */
    public static class SecondElementComparatorWrapper<T,U> implements Comparator<Pair<T, U>>
    {
        /**
         * The comparator which will be used to compare the elements.
         */
        protected Comparator<U> comparator;

        /**
         * General constructor.
         *
         * @param comparator The {@link Comparator} that will be used to compare the elements in the pairs.
         */
        public SecondElementComparatorWrapper(Comparator<U> comparator)
        {
            this.comparator = comparator;
        }

        /**
         * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the second
         * element in the first argument is less than, equal to, or greater than the second element in the second.
         *
         * @param objA the first object to be compared.
         * @param objB the second object to be compared.
         * @return A negative integer, zero, or a positive integer as the second element in the first argument is less
         *         than, equal to, or greater than the second element in the second.
         * @throws ClassCastException If the arguments' types prevent them from being compared by this Comparator.
         */
        public int compare(Pair<T, U> objA, Pair<T, U> objB)
        {
            return comparator.compare(objA.getSecond(), objB.getSecond());
        }
    }
}

// END OF FILE