package orioni.jz.util.configuration;

import orioni.jz.util.strings.ListInterpreter;
import orioni.jz.util.strings.ValueInterpreter;

import java.util.List;

/**
 * This {@link ConfigurationElement} is designed to abstractly represent a list of interpreted strings.
 *
 * @author Zachary Palmer
 */
public class ListConfigurationElement<T> extends ConfigurationElement<List<T>>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param key The key for this element.
     * @param interpreter The {@link ValueInterpreter} for individual elements in the list.
     * @param defaultValue The default value for this {@link ConfigurationElement}.
     */
    public ListConfigurationElement(String key, ValueInterpreter<T> interpreter, List<T> defaultValue)
    {
        super(key, new ListInterpreter<T>(interpreter), defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
