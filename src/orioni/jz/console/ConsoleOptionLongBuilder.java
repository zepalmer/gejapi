package orioni.jz.console;

import orioni.jz.common.exception.ParameterException;

/**
 * A class that implements ConsoleOptionParameterBuilder to allow the creation
 * of longs within a console.
 * @author Zachary Palmer
 */
public class ConsoleOptionLongBuilder
        implements ConsoleOptionParameterBuilder
{
	/**
	 * General constructor.
	 */
	public ConsoleOptionLongBuilder()
	{
	}

	/**
	 * Creates an Long from the String provided.
	 * @param string The String from which the Long will be created.
	 * @return The Long that was created.
	 * @throws ParameterException If an Long cannot be created from the
	 *                            String provided.
	 */
	public Object create(String string)
	        throws ParameterException
	{
		try
		{
			return new Long(string);
		} catch (NumberFormatException nfe)
		{
			throw new ParameterException("The value \"" + string + "\" is not an integer between " +
			                             "-9223372036854775808 and 9223372036854775807.");
		}
	}

	/**
	 * Returns the fully qualified name of the Long class.
	 * @return The name of the Long class.
	 */
	public String getClassName()
	{
		return "java.lang.Long";
	}
}
