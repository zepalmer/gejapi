package orioni.jz.util.configuration;

import orioni.jz.util.strings.BooleanInterpreter;

/**
 * This {@link ConfigurationElement} implementation is designed for boolean elements.  This is a convenience class,
 * as booleans in configurations are common.
 *
 * @author Zachary Palmer
 */
public class BooleanConfigurationElement extends ConfigurationElement<Boolean>
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
    public BooleanConfigurationElement(String key, Boolean defaultValue)
    {
        super(key, BooleanInterpreter.SINGLETON, defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE