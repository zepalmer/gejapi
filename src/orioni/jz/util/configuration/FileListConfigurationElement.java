package orioni.jz.util.configuration;

import orioni.jz.util.strings.FileInterpreter;

import java.io.File;
import java.util.List;

/**
 * This {@link ConfigurationElement} implementation is provided as a convenience method by which a list of files may be
 * stored in a {@link Configuration}.
 */
public class FileListConfigurationElement extends ListConfigurationElement<File>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param key           The key in the configuration file for this element.
     * @param defaultValue The default value for this element.
     */
    public FileListConfigurationElement(String key, List<File> defaultValue)
    {
        super(key, FileInterpreter.SINGLETON, defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
