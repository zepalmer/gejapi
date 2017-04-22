package orioni.jz.util.configuration;

import orioni.jz.util.strings.ValueInterpreter;

/**
 * This class is designed to represent a single element of configuration within a configuration object.  The
 * parameter for this class is the type of data the element represents.
 *
 * @author Zachary Palmer
 */
public class ConfigurationElement <T>
{
    /**
     * The key {@link String} used in the data file.
     */
    protected String key;
    /**
     * A {@link orioni.jz.util.strings.ValueInterpreter} used to translate the value to and from a {@link String}.
     */
    protected ValueInterpreter<T> interpreter;
    /**
     * The default value for this element.
     */
    protected T defaultValue;

    /**
     * General constructor.
     *
     * @param key           The key {@link String} used in the data file.
     * @param interpreter   A {@link orioni.jz.util.strings.ValueInterpreter} used to translate the value to and from a {@link String}.
     * @param defaultValue The default value for this element.
     */
    public ConfigurationElement(String key, ValueInterpreter<T> interpreter, T defaultValue)
    {
        this.key = key;
        this.interpreter = interpreter;
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves the default value for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     *
     * @return The default value for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     */
    public T getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Retrieves the {@link orioni.jz.util.strings.ValueInterpreter} used to translate strings for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     *
     * @return This {@link orioni.jz.util.configuration.ConfigurationElement}'s {@link orioni.jz.util.strings.ValueInterpreter}.
     */
    public ValueInterpreter<T> getInterpreter()
    {
        return interpreter;
    }

    /**
     * The {@link String} used to define the key for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     *
     * @return The key {@link String} for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Determines whether or not this {@link orioni.jz.util.configuration.ConfigurationElement} is equal to another.
     *
     * @param o The other element.
     * @return <code>true</code> if and only if the key of both elements match.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ConfigurationElement)) return false;

        final ConfigurationElement element = (ConfigurationElement) o;

        return !(key != null ? !key.equals(element.key) : element.key != null);
    }

    /**
     * Generates a hash code for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     *
     * @return A hash code for this {@link orioni.jz.util.configuration.ConfigurationElement}.
     */
    public int hashCode()
    {
        return key.hashCode();
    }
}

// END OF FILE