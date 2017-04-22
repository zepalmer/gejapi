package orioni.jz.console;

import orioni.jz.common.exception.ParameterException;

/**
 * A class that implements ConsoleOptionParameterBuilder to allow the creation
 * of shorts within a console.
 * @author Zachary Palmer
 */
public class ConsoleOptionShortBuilder
        implements ConsoleOptionParameterBuilder
{
	/**
	 * General constructor.
	 */
	public ConsoleOptionShortBuilder()
	{
	}

	/**
	 * Creates an Short from the String provided.
	 * @param string The String from which the Short will be created.
	 * @return The Short that was created.
	 * @throws ParameterException If an Short cannot be created from the
	 *                            String provided.
	 */
	public Object create(String string)
	        throws ParameterException
	{
		try
		{
			return new Short(string);
		} catch (NumberFormatException nfe)
		{
			throw new ParameterException("The value \"" + string + "\" is not an integer between -32768 and 32767.");
		}
	}

	/**
	 * Returns the fully qualified name of the Short class.
	 * @return The name of the Short class.
	 */
	public String getClassName()
	{
		return "java.lang.Short";
	}
}
