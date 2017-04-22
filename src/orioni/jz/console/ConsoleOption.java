package orioni.jz.console;

import orioni.jz.common.exception.ParameterException;

/**
 * This abstract class provides a base for the options that can be provided
 * within a ConsoleManager.  A ConsoleOption is provided the ConsoleManager for
 * which it is working and the array of parameters it requires when it is called
 * to execute.
 * @author Zachary Palmer
 */
public abstract class ConsoleOption
{
	/** The list of Class objects that are required as parameters before this
	 *  ConsoleOption can execute. */
	protected Class[] parameterClasses;

	/** The ConsoleManager for which this ConsoleOption is working. */
	protected ConsoleManager master;

	/** The help Strings for this ConsoleOption which should contain all
	 *  information about parameters and effect to use the option correctly. */
	protected String[] helpStrings;

	/** The descriptive String for this ConsoleOption which should summarize the
	 *  use of it. */
	protected String descriptiveString;

	/** The keyword for this ConsoleOption. */
	protected String keyword;

	/**
	 * Full constructor.
	 * @param master The ConsoleManager for which this ConsoleOption is working.
	 */
	public ConsoleOption(ConsoleManager master)
	{
		parameterClasses = new Class[0];
		this.master = master;
		helpStrings = new String[]{"No help available."};
		descriptiveString = "No description available.";
		keyword = null;
	}

	/**
	 * The main execution method.  This method will be passed a reference to the
	 * ConsoleManager for which it is working and the array of Objects that it
	 * requires as parameters.  Any other information that a ConsoleOption needs
	 * to perform its execution method should be passed to it through other
	 * methods or through the constructor.  The execute method is guaranteed
	 * that the parameters are correct in number and type.
	 * @param parameter The array of parameters required by the ConsoleOption.
	 * @param extra The remaining strings left on the execution line.
	 * @throws ParameterException If one or more of the parameters are incorrect
	 *                            in context.
	 */
	public abstract void execute(Object[] parameter, String[] extra)
	        throws ParameterException;

	/**
	 * Retrieves the array of Class objects that define the Objects that this
	 * ConsoleOption needs as parameters on call to execute.
	 * @return A Class[] containing the classes of the parameters required to
	 *         execute this option.
	 */
	public Class[] getParameterClasses()
	{
		return parameterClasses;
	}

	/**
	 * Retrieves the keyword used to call this option.
	 * @return The keyword that identifies this option.
	 */
	public String getKeyword()
	{
		return keyword;
	}

	/**
	 * Retrieves the descriptive String that explains this option briefly.
	 * @return This option's descriptive String.
	 */
	public String getDescriptiveString()
	{
		return descriptiveString;
	}

	/**
	 * Retrieves the help Strings on this option.
	 * @return This option's help Strings.
	 */
	public String[] getHelpStrings()
	{
		return helpStrings;
	}
}
