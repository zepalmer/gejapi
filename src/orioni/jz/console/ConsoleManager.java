package orioni.jz.console;

import orioni.jz.common.exception.DuplicateKeyException;
import orioni.jz.common.exception.ParameterException;
import orioni.jz.util.ConditionallySustainable;
import orioni.jz.util.strings.StringUtilities;

import java.io.*;
import java.util.*;

/**
 * This console management class accepts an input and output stream through which to interact with a user.  A series of
 * ConsoleOption objects are added to the console when it is created.  Each ConsoleOption object contains a series of
 * Class objects that represent the parameters it will require.  The extension of the ConsoleOption class will allow the
 * provision of more data to the ConsoleOption object on construction; thus, the extended ConsoleOption can perform more
 * functions such as modifying other objects. <BR><BR>Each ConsoleOptionParameterBuilder object will provide the
 * appropriate actions in the case that a parameter is needed for a ConsoleOption.  A ConsoleOptionParameterBuilder is
 * always required to create the instance of the Class in question from a string. <BR><BR>The sustain method of the
 * ConsoleManager performs the input of the data from the InputStream, the parsing of that data, and the interpretation
 * of ConsoleOption calls. <BR><BR>This class is abstracted to force the user of a ConsoleManager to explicitly declare
 * various options such as the String used to represent the console prompt and the series of ConsoleOption objects it
 * will use.
 *
 * @author Zachary Palmer
 */
public abstract class ConsoleManager implements ConditionallySustainable
{
    /**
     * The InputStream from which commands are being read.
     */
    protected InputStream inputStream;
    /**
     * The BufferedReader that corresponds to the InputStream.
     */
    protected BufferedReader bufferedReader;
    /**
     * The OutputStream to which reports are being sent.
     */
    protected OutputStream outputStream;
    /**
     * The PrintStream that corresponds to the OutputStream.
     */
    protected PrintStream printStream;

    /**
     * The Hashtable containing the ConsoleOption objects that are available to the user of the console.
     */
    protected HashMap<String, ConsoleOption> consoleOptions;
    /**
     * The Hashtable containing the ConsoleOptionParameterBuilders that are available.
     */
    protected HashMap<String, ConsoleOptionParameterBuilder> parameterBuilders;

    /**
     * Whether or not the sustaining loop should stop.
     */
    protected boolean shouldStop;

    /**
     * The "are you sure" dialog for quitting.  If this is null, no confirmation for quitting is displayed.
     */
    protected String quitDialog;

    /**
     * The last successfully performed command.
     */
    protected String lastCommand;

    /**
     * The appearance of the prompt.
     */
    protected String prompt;

    /**
     * Skeleton constructor.  Assumes that the prompt is simply '&gt;'.
     *
     * @param inputStream  The stream from which commands are taken.
     * @param outputStream The stream to which reports are written.
     */
    public ConsoleManager(InputStream inputStream, OutputStream outputStream)
    {
        this(inputStream, outputStream, "> ");
    }

    /**
     * Full constructor.
     *
     * @param inputStream      The stream from which commands are taken.
     * @param outputStream     The stream to which reports are written.
     * @param promptAppearance The String that represents the prompt.
     */
    public ConsoleManager(InputStream inputStream, OutputStream outputStream,
                          String promptAppearance)
    {
        int i;

        this.inputStream = inputStream;
        bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.outputStream = outputStream;
        printStream = new PrintStream(this.outputStream);
        shouldStop = false;
        lastCommand = "help";
        prompt = promptAppearance;
        // Add console options
        prepareOptions();
        // Add parameter builders
        ConsoleOptionParameterBuilder[] copb =
                new ConsoleOptionParameterBuilder[]
                        {new ConsoleOptionByteBuilder(),
                                new ConsoleOptionShortBuilder(),
                                new ConsoleOptionIntegerBuilder(),
                                new ConsoleOptionLongBuilder(),
                                new ConsoleOptionStringBuilder()};
        parameterBuilders = new HashMap<String, ConsoleOptionParameterBuilder>();
        for (i = 0; i < copb.length; i++)
        {
            addBuilder(copb[i]);
        }
    }

    /**
     * <B><I>This method should be called at the end of each subclass's constructor.</I></B>  It calls getInitialOptions
     * and creates the option array; however, any data that is prepared in the constructor of the subclass will not be
     * available when the superclass's constructor creates its options.
     */
    public void prepareOptions()
    {
        int i;
        ConsoleOption[] option = getInitialOptions();
        consoleOptions = new HashMap<String, ConsoleOption>();
        for (i = 0; i < option.length; i++)
        {
            try
            {
                addOption(option[i]);
            } catch (DuplicateKeyException dke)
            {
                printStream.println(
                        "WARNING: multiple instances of keyword " + option[i].getKeyword() + " in initial options.");
            }
        }
    }

    /**
     * Returns the ConsoleOptions that this ConsoleManager should use.  This level of abstraction forces instantiators of
     * the ConsoleManager to explicitly provide a list of ConsoleOptions to be used in the manager.
     *
     * @return A ConsoleOption[] containing the ConsoleOption objects to be used in this ConsoleManager.
     */
    public abstract ConsoleOption[] getInitialOptions();

    /**
     * Changes the value of the quit confirmation dialog.
     *
     * @param newDialog The new dialog, or null if no confirmation is to be attained.
     */
    public void setQuitDialog(String newDialog)
    {
        quitDialog = newDialog;
    }

    /**
     * Adds a ConsoleOption to the ConsoleManager.
     *
     * @param option The ConsoleOption to add to the ConsoleManager.
     * @throws DuplicateKeyException If an option with the same keyword as this option already exists in the manager or if
     *                               the keyword chosen is a default keyword and thus not available.  The default keywords
     *                               are "help" and "quit."
     */
    public void addOption(ConsoleOption option)
            throws DuplicateKeyException
    {
        if (consoleOptions.get(option.getKeyword()) != null)
        {
            throw new DuplicateKeyException("Option with keyword " + option.getKeyword() + " already defined.");
        }
        consoleOptions.put(option.getKeyword(), option);
    }

    /**
     * Adds or changes a ConsoleOptionParameterBuilder entry.
     *
     * @param parameterBuilder The ConsoleOptionParameterBuilder to add.  If a parameter builder already exists to handle
     *                         the same object, this newer builder is used.
     */
    public void addBuilder(ConsoleOptionParameterBuilder parameterBuilder)
    {
        parameterBuilders.put(
                parameterBuilder.getClassName(),
                parameterBuilder);
    }

    /**
     * Retrieves an Object that is an instance of the given Class by feeding the given String through the appropriate
     * ConsoleOptionParameterBuidler.
     *
     * @param cl     The Class dictating the Object returned.
     * @param string The String that should be used to create the Object.
     * @return The given Object, if an appropriate builder is present, or a com.zpalmer.jz.console.NoParameterObject if no
     *         appropriate builder can be found.
     * @throws ParameterException If the string provided is not correctly formatted to create an Object of the given Class,
     *                            as reported by the builder.
     */
    public Object build(Class cl, String string)
            throws ParameterException
    {
        ConsoleOptionParameterBuilder copb = parameterBuilders.get(cl.getName());
        if (copb == null) return new NoParameterObject();
        return copb.create(string);
    }

    /**
     * The sustain method; parses and processes data received from the InputStream.
     */
    public void sustain()
    {
        printStream.println();
        printStream.print(prompt);
        // Check the input stream for data.  If data is available, get it and
        // put it in the queue.
        String input = null;
        try
        {
            input = bufferedReader.readLine();
        } catch (IOException ioe)
        {
            // We really have no provision to deal with this.  Report it to
            // standard error.
            ioe.printStackTrace(System.err);
        }
        if (input == null)
        {
            shouldStop = true;
        } else
        {
            // Now handle any commands on the input stream.
            doExecute(input);
        }
    }

    /**
     * Branching from sustain, this performs a given command.
     *
     * @param command The command to perform.
     */
    protected void doExecute(String command)
    {
        printStream.println();
        int i;
        try
        {
            StringTokenizer st = new StringTokenizer(command);
            String keyword = st.nextToken();
            if ("help".equals(keyword))
            {
                if (st.hasMoreElements())
                {
                    // Specific help on the following keyword.
                    keyword = st.nextToken();
                    if ("help".equals(keyword))
                    {
                        printStream.println("     Keyword: help");
                        printStream.println();
                        printStream.println("     Usage: help [option]");
                        printStream.println();
                        printStream.println("     option - If option is specified, specific help about the option is");
                        printStream.println("              displayed.  Otherwise, general help about all options is");
                        printStream.println("              displayed.");
                    } else if ("quit".equals(keyword))
                    {
                        printStream.println("     Keyword: quit");
                        printStream.println();
                        printStream.println("     Exits this console.");
                        printStream.println();
                        printStream.println("     Usage: quit");
                    } else
                    {
                        ConsoleOption option = consoleOptions.get(keyword);
                        if (option == null)
                        {
                            printStream.println("Invalid keyword - " + keyword);
                        } else
                        {
                            printStream.println("     Keyword: " + keyword);
                            printStream.println();
                            printStream.println("     " + option.getDescriptiveString());
                            printStream.println();
                            String[] strings = option.getHelpStrings();
                            for (i = 0; i < strings.length; i++)
                            {
                                printStream.println("     " + strings[i]);
                            }
                        }
                    }
                } else
                {
                    // General help on all keywords.
                    // Get length of longest keyword.
                    int size = 0;
                    for (ConsoleOption op : consoleOptions.values())
                    {
                        if (op.getKeyword().length() > size) size = op.getKeyword().length();
                    }
                    // Now prepare descriptions
                    List<String> descriptions = new ArrayList<String>();
                    StringBuffer sb = new StringBuffer(80);
                    for (ConsoleOption op : consoleOptions.values())
                    {
                        sb.delete(0, sb.length());
                        sb.append(op.getKeyword());
                        while (sb.length() < size) sb.append(' ');
                        sb.append(" - ");
                        sb.append(op.getDescriptiveString());
                        descriptions.add(sb.toString());
                    }
                    sb.delete(0, sb.length());
                    sb.append("help");
                    while (sb.length() < size) sb.append(' ');
                    sb.append(" - ");
                    sb.append("Use \"help help\" to display info on specific help.");
                    descriptions.add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append("quit");
                    while (sb.length() < size) sb.append(' ');
                    sb.append(" - ");
                    sb.append("Exits this console.");
                    descriptions.add(sb.toString());
                    for (i = descriptions.size(); i > 0; i--)
                    {
                        for (int j = 0; j < i - 1; j++)
                        {
                            String a = descriptions.get(j);
                            String b = descriptions.get(j + 1);
                            if (b.compareTo(a) < 0)
                            {
                                descriptions.remove(j + 1);
                                descriptions.add(j, b);
                            }
                        }
                    }
                    // Display descriptions
                    for (i = 0; i < descriptions.size(); i++)
                    {
                        printStream.println(descriptions.get(i));
                    }
                }
            } else if ("quit".equals(keyword))
            {
                if (quitDialog != null)
                {
                    printStream.println(quitDialog);
                    try
                    {
                        String quitinp = bufferedReader.readLine();
                        quitinp = quitinp.toLowerCase();
                        if (("y".equals(quitinp)) || ("yes".equals(quitinp)))
                        {
                            shouldStop = true;
                        }
                    } catch (IOException ioe)
                    {
                        // Do nothing.
                    }
                } else
                {
                    shouldStop = true;
                }
            } else
            {
                ConsoleOption option = consoleOptions.get(keyword);
                if (option == null)
                {
                    printStream.println("Invalid keyword - " + keyword);
                } else
                {
                    try
                    {
                        Class[] classes = option.getParameterClasses();
                        Object[] parameter = new Object[classes.length];
                        for (i = 0; i < parameter.length; i++)
                        {
                            parameter[i] = build(classes[i], st.nextToken());
                        }
                        List<String> extraV = new ArrayList<String>();
                        while (st.hasMoreTokens()) extraV.add(st.nextToken());
                        String[] extra = extraV.toArray(StringUtilities.EMPTY_STRING_ARRAY);
                        option.execute(parameter, extra);
                    } catch (NoSuchElementException nseex)
                    {
                        printStream.println("Not enough parameters.");
                    } catch (ParameterException pe)
                    {
                        printStream.println(pe.getMessage());
                    }
                }
            }
        } catch (NoSuchElementException nsee)
        {
            // Heh.  Blank string.  Reperform the last successful command.
            doExecute(lastCommand);
        }
    }

    /**
     * Returns whether or not the sustaining loop should stop.
     *
     * @return Whether or not the sustaining loop should stop.
     */
    public boolean shouldStop()
    {
        return shouldStop;
    }

    /**
     * Returns the PrintStream to which reports are being printed.
     * @return The PrintStream currently on the OutputStream of this manager.
     */
    public PrintStream getPrintStream()
	{
		return printStream;
	}
}
