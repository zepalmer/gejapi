package orioni.jz.util.configuration;

import orioni.jz.util.strings.IntegerInterpreter;

/**
 * This {@link ConfigurationElement} implementation is designed for integer elements.  This is a convenience class,
 * as integers in configurations are common.
 *
 * @author Zachary Palmer
 */
public class IntegerConfigurationElement extends ConfigurationElement<Integer>
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
    public IntegerConfigurationElement(String key, Integer defaultValue)
    {
        super(key, IntegerInterpreter.SINGLETON, defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE