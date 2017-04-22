package orioni.jz.console;

/**
 * A dummy class that implements ConsoleOptionParameterBuilder and returns the
 * String it is passed; allows for flat strings to be passed to a ConsoleOption.
 * @author Zachary Palmer.
 */
public class ConsoleOptionStringBuilder
        implements ConsoleOptionParameterBuilder
{
	/**
	 * General constructor.
	 */
	public ConsoleOptionStringBuilder()
	{
	}

	/**
	 * "Creates" the String.
	 * @param string The String that is to be used in the creation process.
	 * @return The very same String object that was passed in.
	 */
	public Object create(String string)
	{
		return string;
	}

	/**
	 * Provides the fully qualified name of the class creatable.
	 * @return The name of the class String.
	 */
	public String getClassName()
	{
		return "java.lang.String";
	}
}
