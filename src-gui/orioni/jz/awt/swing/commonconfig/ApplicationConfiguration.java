package orioni.jz.awt.swing.commonconfig;

import orioni.jz.awt.swing.commonconfig.optionframework.ConfigurationOption;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * This {@link Properties} extension is designed for use in the Common Configuration Framework for sharing common
 * properties among multiple applications.  It allows access to the default {@link Properties} object which is specified
 * upon construction.  Additionally, it is capable of enforcing those properties which are defined by it.
 * <p/>
 * A typical use of the {@link ApplicationConfiguration} class by an application is to extend it and provide
 * application-specific functionality.  The {@link ApplicationConfiguration#getAllOptions()} method is overridden to
 * include the application-specific options.  The {@link ApplicationConfiguration#getAllOptions()}-overriding method
 * should always include the set returned by <code>super.getAllOptions()</code> in the set it returns.
 *
 * @author Zachary Palmer
 */
public class ApplicationConfiguration extends Properties
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The name of the "appearance" option group.
     */
    public static final String APPEARANCE_GROUP = "appearance";

    /**
     * The default filename for the default configuration.
     */
    public static final File DEFAULT_DEFAULT_CONFIGURATION_FILE = new File(
            System.getProperty("user.home") + File.separatorChar + ".cff.cfg");

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link File} from which the default configuration was loaded.
     */
    protected File defaultConfigurationFile;
    /**
     * The {@link File} from which the application-specific configuration was loaded.
     */
    protected File applicationConfigurationFile;

    /**
     * The {@link Component}s which are to be affected by this {@link ApplicationConfiguration}.
     */
    protected Set<Component> components;
    /**
     * The {@link Set} of {@link ConfigurationOption}s enforced by this class.  This set is used as a caching mechanism
     * and is lazily constructed.
     */
    protected Set<ConfigurationOption> allOptions;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.  Loads the contents of this object and its defaults object from the two specified files.
     * Errors such as {@link IOException}s are suppressed; in the event that they occur, the affected {@link Properties}
     * object is merely generated empty.
     *
     * @param defaultFile The {@link File} containing the default properties.
     * @param configFile  The {@link File} containing the application-specific properties.
     */
    public ApplicationConfiguration(File defaultFile, File configFile)
    {
        super(createProperties(defaultFile));
        defaultConfigurationFile = defaultFile;
        applicationConfigurationFile = configFile;
        Properties p = createProperties(configFile);
        for (Object key : p.keySet())
        {
            this.put(key, p.get(key));
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the {@link Component}s which are to be affected by this {@link ApplicationConfiguration}.
     *
     * @return The {@link Component}s which are to be affected by this {@link ApplicationConfiguration}.
     */
    public Set<Component> getComponents()
    {
        return components;
    }

    /**
     * Sets the {@link Component}s which are to be affected by this {@link ApplicationConfiguration}.  For each {@link
     * Component}, if it is a {@link Container}, its children are recursively affected as well.
     *
     * @param components The {@link Component}s to affect.
     */
    public void setComponents(Set<Component> components)
    {
        this.components = components;
    }

    /**
     * Enforces this configuration.
     *
     * @return A set of error messages, separated by newline characters, or <code>null</code> if no errors occurred.
     */
    public String enforceConfiguration()
    {
        StringBuffer errors = new StringBuffer();
        for (ConfigurationOption option : getAllOptions())
        {
            String error = option.enforce(this);
            if (error != null)
            {
                if (errors.length() > 0) errors.append('\n');
                errors.append(error);
            }
        }
        if (errors.length() > 0) return errors.toString(); else return null;
    }

    /**
     * Retrieves all of the {@link ConfigurationOption}s enforced by this {@link ApplicationConfiguration} class.
     *
     * @return A {@link Set} containing all {@link ConfigurationOption}s enforced by this {@link
     *         ApplicationConfiguration}.
     */
    public Set<ConfigurationOption> getAllOptions()
    {
        if (allOptions == null)
        {
            allOptions = new HashSet<ConfigurationOption>();
            // Add all options.
            allOptions.add(new LookAndFeelOption());
            // Secure the set.
            allOptions = Collections.unmodifiableSet(allOptions);
        }
        return allOptions;
    }

    /**
     * Retrieves only those {@link ConfigurationOption}s related to the specified option group.
     *
     * @param group The group of the options to retrieve.
     * @return All {@link ConfigurationOption}s enforced by this {@link ApplicationConfiguration} which belong to the
     *         specified group.
     */
    public Set<ConfigurationOption> getOptionsOfGroup(String group)
    {
        HashSet<ConfigurationOption> ret = new HashSet<ConfigurationOption>();
        for (ConfigurationOption option : getAllOptions())
        {
            if (option.getGroup().equals(group)) ret.add(option);
        }
        return ret;
    }

    /**
     * Retrieves the {@link Properties} object which is being used for default settings.
     */
    public Properties getDefaults()
    {
        return super.defaults;
    }

    /**
     * Saves the configuration as it is stored within this object.
     *
     * @throws IOException If an I/O error occurs while saving.
     */
    public void save()
            throws IOException
    {
        FileOutputStream fos = new FileOutputStream(defaultConfigurationFile);
        getDefaults().store(fos, null);
        fos.close();
        fos = new FileOutputStream(applicationConfigurationFile);
        store(fos, null);
        fos.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Creates a {@link Properties} object from the specified file.  Suppresses exceptions.
     *
     * @param file The file from which to create a {@link Properties} object.
     */
    private static Properties createProperties(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
            Properties ret = new Properties();
            try
            {
                ret.load(fis);
            } catch (IllegalArgumentException iae)
            {
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                }
                file.delete();
                return new Properties();
            }
            fis.close();
            return ret;
        } catch (IOException ioe)
        {
            return new Properties();
        } finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                }
            }
        }
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

}

// END OF FILE