package orioni.jz.util.configuration;

import orioni.jz.util.strings.FileInterpreter;

import java.io.File;

/**
 * This {@link ConfigurationElement} extension is provided as a measure of convenience for storing {@link File}s in a
 * {@link Configuration}.
 */
public class FileConfigurationElement extends ConfigurationElement<File>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param key The key string for this configuration element.
     * @param defaultValue The {@link File} to use as a default value for this {@link ConfigurationElement}.
     */
    public FileConfigurationElement(String key, File defaultValue)
    {
        super(key, FileInterpreter.SINGLETON, defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
