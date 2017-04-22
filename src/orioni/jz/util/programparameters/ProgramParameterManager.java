package orioni.jz.util.programparameters;

import orioni.jz.common.exception.ParseException;
import orioni.jz.util.Pair;
import orioni.jz.util.strings.StringUtilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Instances of this class are designed to parse arrays of strings which represent a program's command line parameters.
 * A parsing context is created by adding {@link ProgramParameter} objects to this manager; each object represents a
 * single parameterized option.  Once the context is established, the {@link ProgramParameterManager#parse(String[])}
 * method is called using a {@link String}<code>[]</code> containing the parameters to be parsed, and a {@link
 * Pair}<code>&lt;{@link ProgramParameterInstance}[],{@link String}[]&gt;</code> is returned; this return value
 * contains the parsed program parameters and any extraneous arguments which may have appeared at the end of the list.
 * <p/>
 * For example, assume a program using two {@link ProgramParameter}s.  One {@link ProgramParameter} represents a
 * "<code>-h</code>" flag with no parameters; the other represents a "<code>-f</code>" flag with a single file path
 * parameter.  If the string array <code>String[]{"-f","/tmp/test.txt","output.txt"}</code> were passed to a {@link
 * ProgramParameterManager} with those {@link ProgramParameter}s, the result would be a {@link Pair}<code>&lt;{@link
 * ProgramParameterInstance}[],{@link String}[]&gt;</code> whose {@link ProgramParameterInstance}<code>[]</code>
 * contained a single object representing the "-f /tmp/test.txt" portion of the program's parameters.  The {@link
 * String}<code>[]</code> would contain a single string, "output.txt".
 * <p/>
 * Note that this class does not support mandatory parameters.  However, proper use of the {@link ProgramParameter}
 * class will allow this class to prevent or allow multiple instances of the same parameter.  Additionally, this class
 * will detect partially completed parameter lists and incorrect parameter types.
 *
 * @author Zachary Palmer
 */
public class ProgramParameterManager
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Set} of {@link ProgramParameter}s that this {@link ProgramParameterManager} will use.
     */
    protected Set<ProgramParameter> programParameters;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public ProgramParameterManager()
    {
        super();
        programParameters = new HashSet<ProgramParameter>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a {@link ProgramParameter} to this {@link ProgramParameterManager}'s context.
     *
     * @param parameter The {@link ProgramParameter} to add.
     */
    public void addParameter(ProgramParameter parameter)
    {
        programParameters.remove(parameter);
        programParameters.add(parameter);
    }

    /**
     * Parses the provided {@link String}<code>[]</code> using the parameters provided to this {@link
     * ProgramParameterManager}.
     *
     * @param strings The {@link String}<code>[]</code> to parse.
     * @return A {@link Pair}<code>&lt;{@link ProgramParameterInstance}[],{@link String}<code>[]&gt;</code> containing
     *         the results of the parse and any extraneous {@link String}s which were not used.
     * @throws ParseException If an error occurred parsing the {@link String} array.  This may occur if the array did
     *                        not contain enough {@link String}s provided its content or if one of the {@link String}s
     *                        could not be converted to an appropriate type (for example, the string "<code>haha</code>"
     *                        appearing where an integer was required).  The message in this exception will be a
     *                        human-readable description of the cause of failure.
     */
    public Pair<ProgramParameterInstance[], String[]> parse(String[] strings)
            throws ParseException
    {
        // The Set of ProgramParameters which have been used this parse
        Set<ProgramParameter> usedParameters = new HashSet<ProgramParameter>();

        ArrayList<String> remaining = new ArrayList<String>();
        for (String s : strings) remaining.add(s);
        ArrayList<ProgramParameterInstance> ret = new ArrayList<ProgramParameterInstance>();

        while ((remaining.size() > 0) && (remaining.get(0).startsWith("-")))
        {
            if (remaining.get(0).startsWith("--"))
            {
                String parameterStringUnmodified = remaining.remove(0);
                String s = parameterStringUnmodified.substring(2);
                String identity;
                String[] subparameters;
                if (s.indexOf('=') == -1)
                {
                    identity = s;
                    subparameters = StringUtilities.EMPTY_STRING_ARRAY;
                } else
                {
                    identity = s.substring(0, s.indexOf('='));
                    subparameters = s.substring(s.indexOf('=') + 1).split(",");
                    parameterStringUnmodified =
                    parameterStringUnmodified.substring(0, parameterStringUnmodified.indexOf('='));
                }
                ProgramParameter parameter = findParameterFor(identity);
                ret.add(createInstanceFor(parameter, parameterStringUnmodified, subparameters, usedParameters));
            } else
            {
                // Single dash
                String unmodified = remaining.remove(0);
                String identity = unmodified.substring(1);
                while (identity.length() > 1)
                {
                    String realIdentity = identity.substring(0, 1);
                    identity = identity.substring(1);
                    ProgramParameter parameter = findParameterFor(realIdentity);
                    if (parameter == null)
                    {
                        throw new ParseException("Unrecognized parameter: -" + realIdentity);
                    }
                    if (parameter.getInterpreters().length > 0)
                    {
                        throw new ParseException(
                                "Parameter -" + realIdentity + " has 0 parameter(s): should be " +
                                parameter.getInterpreters().length);
                    }
                    ret.add(
                            createInstanceFor(
                                    parameter, "-" + realIdentity, StringUtilities.EMPTY_STRING_ARRAY, usedParameters));
                }
                ProgramParameter parameter = findParameterFor(identity);
                if (parameter == null)
                {
                    throw new ParseException("Unrecognized parameter: -" + identity);
                }
                if (parameter.getInterpreters().length > remaining.size())
                {
                    throw new ParseException(
                            "Parameter -" + identity + " has " + remaining.size() + " parameter(s): should be " +
                            parameter.getInterpreters().length);
                }
                String[] subparameters = new String[parameter.getInterpreters().length];
                for (int i = 0; i < subparameters.length; i++) subparameters[i] = remaining.remove(0);
                ret.add(createInstanceFor(parameter, "-" + identity, subparameters, usedParameters));
            }
        }

        return new Pair<ProgramParameterInstance[], String[]>(
                ret.toArray(new ProgramParameterInstance[0]), remaining.toArray(StringUtilities.EMPTY_STRING_ARRAY));
    }

    /**
     * Finds the {@link ProgramParameter} which deals with the specified identity string.
     *
     * @param string The identity string for which to search.
     * @return A {@link ProgramParameter} which uses that identity string, or <code>null</code> if no such parameter
     *         type is registered.
     */
    protected ProgramParameter findParameterFor(String string)
    {
        for (ProgramParameter parameter : programParameters)
        {
            if (parameter.hasString(string)) return parameter;
        }
        return null;
    }

    /**
     * Creates a {@link ProgramParameterInstance} for the provided data.
     *
     * @param parameter         The {@link ProgramParameter} for which the instance is being created.
     * @param unmodifiedString The unmodified parameter identity string (for error messages).
     * @param subparameters     The {@link String}<code>[]</code> containing the parameter's subparameter data.
     * @param usedParameters   The {@link Set} of {@link ProgramParameter}s which have already been used.
     * @return The {@link ProgramParameterInstance} representing this data.
     * @throws ParseException If a parse error occurs while parsing the data.
     */
    protected ProgramParameterInstance createInstanceFor(ProgramParameter parameter, String unmodifiedString,
                                                         String[] subparameters, Set<ProgramParameter> usedParameters)
            throws ParseException
    {
        if (parameter == null)
        {
            throw new ParseException("Unrecognized parameter: " + unmodifiedString);
        }

        if ((!usedParameters.add(parameter)) && (parameter.isDistinct()))
        {
            throw new ParseException("Parameter " + unmodifiedString + " can only appear once.");
        }

        if (subparameters.length != parameter.getInterpreters().length)
        {
            throw new ParseException(
                    "Parameter " + unmodifiedString + " has " + subparameters.length + " parameter(s): should be " +
                    parameter.getInterpreters().length);
        }
        Object[] parsedParameters = new Object[subparameters.length];
        for (int i = 0; i < parsedParameters.length; i++)
        {
            Object parsed = parameter.getInterpreters()[i].fromString(subparameters[i]);
            if (parsed == null)
            {
                throw new ParseException(
                        "Subparameter #" + (i + 1) + " for parameter " + unmodifiedString + " (\"" + subparameters[i] +
                        "\") is invalid.");
            }
            parsedParameters[i] = parsed;
        }
        return new ProgramParameterInstance(parameter.getStrings()[0], parsedParameters);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE