package orioni.jz.util.programparameters;

import orioni.jz.util.strings.ValueInterpreter;

import java.util.Arrays;

/**
 * This class is designed to represent a single type of parameter which can appear on a program's command line.  For
 * example, a single {@link ProgramParameter} could, among others, represent any of the following: <ul> <li>The
 * appearance of the parameter <code>-i</code>. <li>The appearance of a parameter which can be represented
 * <code>-h</code> or <code>--help</code>. <li>The appearance of a parameter <code>-x</code> which requires a file path
 * as its parameter (for example, <code>-x /tmp/myfile</code>). <li>The appearance of a parameter <code>-c</code> or
 * <code>--count</code> which requires a positive integer as its parameter (for example, <code>-c 5</code> or
 * <code>--count=5</code>). </ul> Instances of this class are constructed with an array of {@link String}s which
 * represent the parameter, an array of {@link ValueInterpreter}s indicating the types of parameters which are required,
 * and a <code>boolean</code> indicating whether or not the parameter can appear multiple times in the command line.
 * <p/>
 * The prefixes <code>-</code> and <code>--</code> are an assumed part of this package.  The purpose is to allow
 * single-character, zero-parameter program parameters to appear in a group.  For example, the command line
 * <ul><code>java -jar MyJar.jar -c 0 -x -v -r -f myfile.txt</code></ul> could also appear as <ul><code>java -jar
 * MyJar.jar -c 0 -xvrf myfile.txt</code></ul> In this example, it <i>is</i> legal for the <code>-f</code> parameter to
 * require the parameter "myfile.txt", since it appears last in the list.
 * <p/>
 * This class has a parameterized type to allow {@link ProgramParameterInstance}s to have parameterized types defining
 * the types of the variables in their variable containers.  If the parameterized type on this class is used, <i>all</i>
 * subparameters defined by this {@link ProgramParameter} must be instances of the parameterized type.  For example, a
 * {@link ProgramParameter}<code>&lt;{@link Integer}&gt;</code> must have only {@link Integer}s as its parameters.
 *
 * @author Zachary Palmer
 */
public class ProgramParameter<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link String}s which can represent this {@link ProgramParameter}.
     */
    protected String[] strings;
    /**
     * <code>true</code> if this parameter must appear only once in a parameter list; <code>false</code> otherwise.
     */
    protected boolean distinct;
    /**
     * The {@link ValueInterpreter}s which can parse this {@link ProgramParameter}s subparameters.
     */
    protected ValueInterpreter<T>[] interpreters;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes one string and no subparameters.
     *
     * @param string           The {@link String} which can represent this {@link ProgramParameter}.  This string should
     *                         not have a leading flag; single-character strings are prefixed with a single
     *                         "<code>-</code>" while multiple-character strings are prefixed with two.
     * @param multipleAllowed <code>true</code> if more than one of these parameters can appear in a single parameter
     *                         list; <code>false</code> if this parameter is distinct.
     */
    public ProgramParameter(String string, boolean multipleAllowed)
    {
        //noinspection unchecked
        this(new String[]{string}, multipleAllowed, new ValueInterpreter[0]);
    }

    /**
     * Skeleton constructor.  Assumes no subparameters.
     *
     * @param stringA         The first {@link String} which can represent this {@link ProgramParameter}.  This string
     *                         should not have a leading flag; single-character strings are prefixed with a single
     *                         "<code>-</code>" while multiple-character strings are prefixed with two.
     * @param stringB         The first {@link String} which can represent this {@link ProgramParameter}.  This string
     *                         should not have a leading flag.
     * @param multipleAllowed <code>true</code> if more than one of these parameters can appear in a single parameter
     *                         list; <code>false</code> if this parameter is distinct.
     */
    public ProgramParameter(String stringA, String stringB, boolean multipleAllowed)
    {
        //noinspection unchecked
        this(new String[]{stringA, stringB}, multipleAllowed, new ValueInterpreter[0]);
    }

    /**
     * Skeleton constructor.  Assumes one string and no subparameters.
     *
     * @param strings          The {@link String}s which can represent this {@link ProgramParameter}.  Strings in this
     *                         array should not have a leading flag; single-character strings are prefixed with a single
     *                         "<code>-</code>" while multiple-character strings are prefixed with two.
     * @param multipleAllowed <code>true</code> if more than one of these parameters can appear in a single parameter
     *                         list; <code>false</code> if this parameter is distinct.
     */
    public ProgramParameter(String[] strings, boolean multipleAllowed)
    {
        //noinspection unchecked
        this(strings, multipleAllowed, new ValueInterpreter[0]);
    }

    /**
     * Skeleton constructor.  Assumes one string and one subparameter.
     *
     * @param string           The {@link String} which can represent this {@link ProgramParameter}.  This string should
     *                         not have a leading flag; single-character strings are prefixed with a single
     *                         "<code>-</code>" while multiple-character strings are prefixed with two.
     * @param multipleAllowed <code>true</code> if more than one of these parameters can appear in a single parameter
     *                         list; <code>false</code> if this parameter is distinct.
     * @param interpreter      The {@link ValueInterpreter} for this parameter's subparameter.
     */
    public ProgramParameter(String string, boolean multipleAllowed, ValueInterpreter<T> interpreter)
    {
        //noinspection unchecked
        this(new String[]{string}, multipleAllowed, new ValueInterpreter[]{interpreter});
    }

    /**
     * Skeleton constructor.  Assumes two strings and one subparameter.
     *
     * @param stringA         The first {@link String} which can represent this {@link ProgramParameter}.  This string
     *                         should not have a leading flag; single-character strings are prefixed with a single
     *                         "<code>-</code>" while multiple-character strings are prefixed with two.
     * @param stringB         The first {@link String} which can represent this {@link ProgramParameter}.  This string
     *                         should not have a leading flag.
     * @param multipleAllowed <code>true</code> if more than one of these parameters can appear in a single parameter
     *                         list; <code>false</code> if this parameter is distinct.
     */
    public ProgramParameter(String stringA, String stringB, boolean multipleAllowed, ValueInterpreter<T> interpreter)
    {
        //noinspection unchecked
        this(new String[]{stringA, stringB}, multipleAllowed, new ValueInterpreter[]{interpreter});
    }

    /**
     * General constructor.
     *
     * @param strings          The {@link String}s which can represent this {@link ProgramParameter}.  Strings in this
     *                         array should not have a leading flag; single-character strings are prefixed with a single
     *                         "<code>-</code>" while multiple-character strings are prefixed with two.
     * @param multipleAllowed <code>true</code> if more than one of these parameters can appear in a single parameter
     *                         list; <code>false</code> if this parameter is distinct.
     * @param interpreters     The {@link ValueInterpreter}s used to parse the subparameters of this parameter.
     */
    public ProgramParameter(String[] strings, boolean multipleAllowed, ValueInterpreter<T>... interpreters)
    {
        super();
        this.strings = strings;
        distinct = !multipleAllowed;
        this.interpreters = interpreters;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not this {@link ProgramParameter} is distinct.
     *
     * @return <code>true</code> if a maximum of one instance of this {@link ProgramParameter} is allowed;
     *         <code>false</code> if any number can appear.
     */
    public boolean isDistinct()
    {
        return distinct;
    }

    /**
     * Retrieves the interpreters used to interpret this parameter's subparameters.
     *
     * @return The interpreters used to interpret this parameter's subparameters.  This is a shallow copy and should not
     *         be modified.
     */
    public ValueInterpreter<T>[] getInterpreters()
    {
        return interpreters;
    }

    /**
     * Retrieves the {@link String}s which can be used to identify this {@link ProgramParameter}.
     *
     * @return The {@link String}s which can be used to identify this {@link ProgramParameter}.  This is a shallow copy
     *         and should not be modified.
     */
    public String[] getStrings()
    {
        return strings;
    }

    /**
     * Determines if the identity {@link String} array contains a provided {@link String}.
     *
     * @param string The string for which to check.
     * @return <code>true</code> if the {@link String} appears in the identity list; <code>false</code> otherwise.
     */
    public boolean hasString(String string)
    {
        for (String s : strings) if (s.equals(string)) return true;
        return false;
    }

    /**
     * Determines whether or not this {@link ProgramParameter} and another are equal.
     *
     * @param o The {@link ProgramParameter} with which to compare.
     * @return <code>true</code> if they are equal; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ProgramParameter)) return false;

        final ProgramParameter programParameter = (ProgramParameter) o;

        if (distinct != programParameter.distinct) return false;
        if (!Arrays.equals(interpreters, programParameter.interpreters)) return false;
        return Arrays.equals(strings, programParameter.strings);
    }

    public int hashCode()
    {
        int ret = (distinct ? 0xFF00FF00 : 0x00FF00FF);
        for (String s : strings) ret ^= s.hashCode();
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE