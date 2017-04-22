package orioni.jz.console;

import orioni.jz.common.exception.ParameterException;

/**
 * A class that implements ConsoleOptionParameterBuilder to allow the creation
 * of bytes within a console.
 * @author Zachary Palmer
 */
public class ConsoleOptionByteBuilder
        implements ConsoleOptionParameterBuilder
{
	/**
	 * General constructor.
	 */
	public ConsoleOptionByteBuilder()
	{
	}

	/**
	 * Creates an Byte from the String provided.
	 * @param string The String from which the Byte will be created.
	 * @return The Byte that was created.
	 * @throws ParameterException If an Byte cannot be created from the
	 *                            String provided.
	 */
	public Object create(String string)
	        throws ParameterException
	{
		try
		{
			return new Byte(string);
		} catch (NumberFormatException nfe)
		{
			throw new ParameterException("The value \"" + string + "\" is not an integer between -128 and 127.");
		}
	}

	/**
	 * Returns the fully qualified name of the Byte class.
	 * @return The name of the Byte class.
	 */
	public String getClassName()
	{
		return "java.lang.Byte";
	}
}
