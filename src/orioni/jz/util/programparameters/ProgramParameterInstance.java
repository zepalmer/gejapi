package orioni.jz.util.programparameters;

/**
 * This class represents specific instances of parameters derived from the general {@link ProgramParameter} case during
 * parsing.  For example, given a {@link ProgramParameter} constructed in the following manner: <ul><code>new
 * ProgramParameter("-p", "--port", false, {@link orioni.jz.util.strings.IntegerInterpreter#SINGLETON})</code></ul> and
 * the parameter list <ul><code>--port=65</code></ul> this {@link ProgramParameterInstance} would contain as its
 * identifier string "<code>-p</code>" (because that is the first string which appears in the {@link ProgramParameter}
 * list) and as its only subparameter an {@link Integer} containing the value <code>65</code>.
 *
 * @author Zachary Palmer
 */
public class ProgramParameterInstance <T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link String} representing the {@link ProgramParameter} which created this object.
     */
    protected String string;
    /**
     * The subparameters for this {@link ProgramParameterInstance}.
     */
    protected T[] subparameters;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param string     The {@link String} representing the {@link ProgramParameter} which created this object.
     * @param parameters The subparameters for this {@link ProgramParameterInstance}.
     */
    public ProgramParameterInstance(String string, T... parameters)
    {
        super();
        this.string = string;
        subparameters = parameters;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the {@link String} which represents the {@link ProgramParameter} which was used to create this object.
     *
     * @return The {@link String} defining the type of this {@link ProgramParameterInstance}.
     */
    public String getString()
    {
        return string;
    }

    /**
     * The subparameters for this specific instance of a {@link ProgramParameter}.
     *
     * @return The subparameters for this {@link ProgramParameterInstance}; this is a shallow copy and should not be
     *         modified.
     */
    public T[] getSubparameters()
    {
        return subparameters;
    }

    /**
     * Generates a string for this {@link ProgramParameterInstance}.
     *
     * @return A string describing this {@link ProgramParameterInstance}.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(string);
        for (T t : subparameters)
        {
            sb.append(' ');
            sb.append(t.toString());
        }
        return sb.toString();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE