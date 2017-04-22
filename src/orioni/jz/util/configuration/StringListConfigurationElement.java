package orioni.jz.util.configuration;

import orioni.jz.util.strings.StringInterpreter;

import java.util.List;

/**
 * This {@link ConfigurationElement} implementation is designed for List<String> elements.  This is a convenience class,
 * as List<String>s in configurations are common.
 *
 * @author Zachary Palmer
 */
public class StringListConfigurationElement extends ListConfigurationElement<String>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param key The key in the configuration file for this element.
     * @param defaultValue The default value for this element.
     */
    public StringListConfigurationElement(String key, List<String> defaultValue)
    {
        super(key, StringInterpreter.SINGLETON, defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE