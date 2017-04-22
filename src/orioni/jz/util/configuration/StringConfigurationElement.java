package orioni.jz.util.configuration;

import orioni.jz.util.strings.StringInterpreter;

/**
 * This {@link ConfigurationElement} implementation is designed for string elements.  This is a convenience class,
 * as strings in configurations are common.
 *
 * @author Zachary Palmer
 */
public class StringConfigurationElement extends ConfigurationElement<String>
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
    public StringConfigurationElement(String key, String defaultValue)
    {
        super(key, StringInterpreter.SINGLETON, defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE