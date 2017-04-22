package orioni.jz.math;

/**
 * This class mimicks the <code>java.lang.Boolean</code> class in functionality.  It provides the additional ability to
 * change the boolean value as necessary.  The static methods belonging to the <code>Boolean</code> class are not
 * replicated.
 * @author Zachary Palmer
 * @see java.lang.Boolean
 */
public class MutableBoolean
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The value of this MutableBoolean object. */
    protected boolean value;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param bool The starting value for this MutableBoolean object.
     */
    public MutableBoolean(boolean bool)
    {
        value = bool;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the boolean value represented by this <code>MutableBoolean</code> object.
     * @return The boolean value represented by this <code>MutableBoolean</code> object.
     */
    public boolean booleanValue()
    {
        return value;
    }

    /**
     * Returns <code>true</code> if the other object is a <code>MutableBoolean</code> and represents the same value as
     * this <code>MutableBoolean</code>.  This method returns false if a <code>Boolean</code> is passed to this method
     * because the method <code>Boolean.equals(Object)</code> returns false if a <code>MutableBoolean</code> object is
     * passed to it; this is required behavior to meet the specifications of <code>Object.equals(Object)</code>.
     * @param o The Object to compare to this <code>MutableBoolean</code>.
     */
    public boolean equals(Object o)
    {
        return ((o instanceof MutableBoolean) && (((MutableBoolean)o).booleanValue()==value));
    }

    /**
     * Returns a hashcode for this <code>MutableBoolean</code> object.
     * @return A hashcode for this <code>MutableBoolean</code> object.
     */
    public int hashCode()
    {
        if (value)
        {
            return 19820227;
        } else
        {
            return 19821030;
        }
    }

    /**
     * Returns a String representing this <code>MutableBoolean</code>'s value.  This value is identical to
     * <code>Boolean.TRUE.toString()</code> or <code>Boolean.FALSE.toString()</code>, whichever is appropriate.
     * @return A String representing this <code>MutableBoolean</code>'s value.
     */
    public String toString()
    {
        if (value)
        {
            return Boolean.TRUE.toString();
        } else
        {
            return Boolean.FALSE.toString();
        }
    }

    /**
     * Sets a new value for this <code>MutableBoolean</code> object to represent.
     * @param value The new value for this <code>MutableBoolean</code> in primitive form.
     */
    public void setValue(boolean value)
    {
        this.value = value;
    }

    /**
     * Sets a new value for this <code>MutableBoolean</code> object to represent.
     * @param value The new value for this <code>MutableBoolean</code> in <code>Boolean</code> form.
     */
    public void setValue(Boolean value)
    {
        this.value = value;
    }

    /**
     * Sets a new value for this <code>MutableBoolean</code> object to represent.
     * @param value The new value for this <code>MutableBoolean</code> in <code>MutableBoolean</code> form.
     */
    public void setValue(MutableBoolean value)
    {
        this.value = value.booleanValue();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //