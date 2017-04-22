package orioni.jz.console;

import orioni.jz.common.exception.ParameterException;

/**
 * Creates an instance of a class from a String.  This interface is used by the
 * ConsoleManager to provide ConsoleOption objects with the appropriate types
 * of parameters; essentially, a ConsoleOptionParameterBuilder assists in the
 * parsing of commands.
 * @author Zachary Palmer
 */
public interface ConsoleOptionParameterBuilder
{
	/**
	 * Creates the appropriate Object from the String provided.
	 * @param string A String representing the Object to create.
	 * @return The resulting Object.
	 * @throws ParameterException If the string is not correctly formatted to
	 *                            create the given Object.
	 */
	public Object create(String string) throws ParameterException;

	/**
	 * Returns the fully qualified name of the class that this
	 * ConsoleOptionParameterBuilder can create from a String.
	 * @return A String containing the name of the class that this
	 *         ConsoleOptionParameterBuilder can create.  The String returned
	 *         should be the value of created_object.getClass().getName().
	 */
	public String getClassName();
}
