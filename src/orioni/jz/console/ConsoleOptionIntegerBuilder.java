package orioni.jz.console;

import orioni.jz.common.exception.ParameterException;

/**
 * A class that implements ConsoleOptionParameterBuilder to allow the creation
 * of integers within a console.
 * @author Zachary Palmer
 */
public class ConsoleOptionIntegerBuilder
        implements ConsoleOptionParameterBuilder
{
	/**
	 * General constructor.
	 */
	public ConsoleOptionIntegerBuilder()
	{
	}

	/**
	 * Creates an Integer from the String provided.
	 * @param string The String from which the Integer will be created.
	 * @return The Integer that was created.
	 * @throws ParameterException If an Integer cannot be created from the
	 *                            String provided.
	 */
	public Object create(String string)
	        throws ParameterException
	{
		try
		{
			return new Integer(string);
		} catch (NumberFormatException nfe)
		{
			throw new ParameterException("The value \"" + string + "\" is not an integer between " +
			                             "-2147483648 and 2147483647.");
		}
	}

	/**
	 * Returns the fully qualified name of the Integer class.
	 * @return The name of the Integer class.
	 */
	public String getClassName()
	{
		return "java.lang.Integer";
	}
}
