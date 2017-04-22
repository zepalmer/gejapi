package orioni.jz.math;

import orioni.jz.util.ByteConstructable;
import orioni.jz.util.DataConversion;

/**
 * This extension of the <code>Number</code> object provides the same functionality as the <code>Integer</code> object.
 * In many cases, the static functionality of the <code>Integer</code> class is used to perform the tasks associated
 * with it.  This class, however, provides the additional ability to adjust the value it is representing after it has
 * been constructed.  The static functionality of the <code>Integer</code> class is not replicated here.
 * @see java.lang.Integer
 * @author Zachary Palmer
 */
public class MutableInteger extends Number implements Comparable, ByteConstructable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The value of this MutableInteger. */
    protected int value;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes this <code>MutableInteger</code> with a value of zero.
     */
    public MutableInteger()
    {
        this(0);
    }

    /**
     * Skeleton constructor.  Converts the provided String into an int value using the <code>Integer.parseInt</code>
     * method and a radix of 10.
     * @param string The String representing the number to use as the <code>MutableInteger</code> object's starting
     *               value.
     * @throws NumberFormatException If the content of the String does not represent a base 10 number.
     * @see Integer#parseInt(String,int)
     */
    public MutableInteger(String string)
        throws NumberFormatException
    {
        this(Integer.parseInt(string,10));
    }

    /**
     * General constructor.
     * @param value The value of this <code>MutableInteger</code>.
     */
    public MutableInteger(int value)
    {
        this.value = value;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the value of this MutableInteger as a byte.
     * @return A byte representing this MutableInteger's value.
     */
    public byte byteValue()
    {
        return (byte)(value);
    }

    /**
     * Returns the value of this MutableInteger as a short.
     * @return A short representing this MutableInteger's value.
     */
    public short shortValue()
    {
        return (short)(value);
    }

    /**
     * Returns the value of this MutableInteger as an int.
     * @return An int representing this MutableInteger's value.
     */
    public int intValue()
    {
        return value;
    }

    /**
     * Returns the value of this MutableInteger as a long.
     * @return A long representing this MutableInteger's value.
     */
    public long longValue()
    {
        return value;
    }

    /**
     * Returns the value of this MutableInteger as a float.
     * @return A float representing this MutableInteger's value.
     */
    public float floatValue()
    {
        return value;
    }

    /**
     * Returns the value of this MutableInteger as a double.
     * @return A double representing this MutableInteger's value.
     */
    public double doubleValue()
    {
        return value;
    }

    /**
     * Returns a String object representing this MutableInteger's value. The value is converted to signed decimal
     * representation (radix 10) and returned as a string, exactly as if the integer value were given as an
     * argument to the {@link java.lang.Integer#toString(int)} method.
     * @return A String representation of the current value of this MutableInteger in base 10.
     * @see Integer#toString(int)
     */
    public String toString()
    {
        return Integer.toString(value);
    }

    /**
     * Returns a hashcode for this MutableInteger.  This hashcode is equal to the value of this MutableInteger.
     * @return A hash code value for this object, equal to the primitive <code>int</code> value represented by this
     *          <code>MutableInteger</code> object.
     */
    public int hashCode()
    {
        return value;
    }

    /**
     * Compares this object to the specified object.  The result is <code>true</code> if and only if the argument is
     * not <code>null</code> and is an <code>Integer</code> or <code>MutableInteger</code> object that contains the
     * same <code>int</code> value as this object.  If the specified object is a <code>MutableInteger</code> object,
     * its current value is used.
     * @param   obj   the object to compare with.
     * @return  <code>true</code> if the objects are the same;
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object obj)
    {
        // Note that instanceof will return false if obj is null
        return (((obj instanceof Integer) && (obj.equals(value))) ||
                ((obj instanceof MutableInteger) && (((MutableInteger)(obj)).intValue()==value)));
    }

    /**
     * Compares this MutableInteger to another Object.  If the Object is not a <code>MutableInteger</code>, this
     * method throws a <code>ClassCastException</code>; this is because, when the abstract class <code>Number</code>
     * was implemented in the Java API, each of its extensions were only comparable to Objects of the same type. <BR>
     * <BR>
     * If the other Object is a <code>MutableInteger</code>, this method returns a negative value if this MutableInteger
     * contains a value less than the parameter's, zero if this MutableInteger's value is equal to the parameter's, or
     * a positive value if this MutableInteger's value is greater than the parameter's.
     *
     * @param o The <code>Object</code> to be compared.
     * @return A negative, zero, or positive value, as defined by {@link java.lang.Comparable#compareTo(Object)}.
     * @throws ClassCastException if the argument is not a <code>MutableInteger</code>.
     * @see java.lang.Comparable
     */
    public int compareTo(Object o)
        throws ClassCastException
    {
        if (o instanceof MutableInteger)
        {
            MutableInteger other = (MutableInteger)o;
            int otherValue = other.intValue();
            if (otherValue >value) return -1;
            if (otherValue ==value) return 0;
            return 1;
        } else
        {
            throw new ClassCastException(o.getClass()+" is not a subclass of MutableInteger");
        }
    }

    /**
     * Sets a new value for this <code>MutableInteger</code> object to represent.
     * @param value The new value for this <code>MutableInteger</code> in primitive form.
     */
    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * Sets a new value for this <code>MutableInteger</code> object to represent.
     * @param value The new value for this <code>MutableInteger</code> in <code>Integer</code> form.
     */
    public void setValue(Integer value)
    {
        this.value = value;
    }

    /**
     * Sets a new value for this <code>MutableInteger</code> object to represent.
     * @param value The new value for this <code>MutableInteger</code> in <code>MutableInteger</code> form.
     */
    public void setValue(MutableInteger value)
    {
        this.value = value.intValue();
    }

// BYTE CONSTRUCTABLE IMPLEMENTATION /////////////////////////////////////////////

	/**
	 * Converts this object into a byte array.
	 * @return An array of bytes representing the object.
	 */
	public byte[] toBytes()
	{
		return DataConversion.intToByteArray(value);
	}

	/**
	 * Converts an array of bytes into a value to be stored in this {@link MutableInteger}.
	 * @param array The array of bytes to be used to create the object.
	 */
	public void fromBytes(byte[] array)
	{
		value = DataConversion.byteArrayToInt(array);
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //