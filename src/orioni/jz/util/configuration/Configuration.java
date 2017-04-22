package orioni.jz.util.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * The {@link Configuration} class is similar to the {@link Properties} class in that it allows the user to specify a
 * key-value pairing which can later be recalled for various configuration purposes.  For example, both the {@link
 * Configuration} and {@link Properties} classes could be used to store the last username which was entered in a
 * username field.  However, while the {@link Properties} object is effectively a dressed-up mapping between string keys
 * and string values, the {@link Configuration} object is capable of performing conversion operations on those strings
 * and retrieving object data rather than simple strings.
 * <p/>
 * For example, a {@link Properties} object may be used to store a UI preference for a background color. In this case,
 * the {@link Properties} object would map a key string, possibly something like "<code>ui.background</code>", to a
 * value representing the color, possibly a hex representation of the {@link java.awt.Color} object's RGB value.  Then,
 * when the program using the {@link Properties} object wanted to retrieve the color, it would have to retrieve the
 * string, convert it to an integer, and construct the {@link java.awt.Color} object itself.  The {@link Configuration}
 * class acts as an intermediary, performing that operation so as not to clutter the main program's code.
 * <p/>
 * The {@link Configuration} class is constructed using {@link ConfigurationElement} objects which describe the fields
 * which appear in the configuration.  The {@link ConfigurationElement} contains the key string used to describe the
 * value, a {@link orioni.jz.util.strings.ValueInterpreter} implementation used to convert the data object to and from a
 * string, and a default value for that element.  A number of {@link orioni.jz.util.strings.ValueInterpreter}
 * implementations exist on this class, but additional implementations can be provided by user libraries.
 * <p/>
 * When a configuration value needs to be retrieved or stored, the {@link Configuration#getValue(ConfigurationElement)}
 * and {@link Configuration#setValue(ConfigurationElement, Object)} methods are used.
 *
 * @author Zachary Palmer
 */
public class Configuration
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link ConfigurationElement}s being used in this {@link Configuration}.
     */
    protected Set<ConfigurationElement> elements;
    /**
     * The {@link java.util.Map}<code>&lt;{@link String},{@link String}&gt;</code> that backs the data for this {@link
     * Configuration}.
     */
    protected Map<String, String> data;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param elements The {@link ConfigurationElement}s to use in this {@link Configuration}.
     */
    public Configuration(ConfigurationElement... elements)
    {
        super();
        this.elements = new HashSet<ConfigurationElement>();
        for (ConfigurationElement e : elements) this.elements.add(e);
        data = new HashMap<String, String>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a configuration element to this {@link Configuration}.
     *
     * @param element The {@link ConfigurationElement} to add.
     */
    public void addElement(ConfigurationElement element)
    {
        elements.add(element);
    }

    /**
     * Adds all of the provided elements to this {@link Configuration}.
     *
     * @param elements The {@link ConfigurationElement}s to add.
     */
    public void addElements(ConfigurationElement... elements)
    {
        for (ConfigurationElement element : elements) addElement(element);
    }

    /**
     * Removes a configuration element from this {@link Configuration}.
     *
     * @param element The {@link ConfigurationElement} to remove.
     */
    public void removeElement(ConfigurationElement element)
    {
        elements.remove(element);
    }

    /**
     * Removes all of the provided elements from this {@link Configuration}.
     *
     * @param elements The {@link ConfigurationElement}s to remove.
     */
    public void removeElements(ConfigurationElement... elements)
    {
        for (ConfigurationElement element : elements) removeElement(element);
    }

    /**
     * Reverts this {@link Configuration} object to its defaults.
     */
    public void revertToDefaults()
    {
        data.clear();
    }

    /**
     * Reverts the provided {@link ConfigurationElement} to its default.
     *
     * @param element The element which should be reverted to its default.
     */
    public void revertToDefault(ConfigurationElement element)
    {
        if (elements.contains(element))
        {
            data.remove(element.getKey());
        }
    }

    /**
     * Retrieves the value of the specified element in this {@link Configuration}.  The element in question is
     * implicitly added to this {@link Configuration}.
     *
     * @param element The element whose value is to be retrieved.
     * @return The value of that element.
     */
    public <T> T getValue(ConfigurationElement<T> element)
    {
        addElement(element);
        String data = this.data.get(element.getKey());
        T ret = null;
        if (data != null) ret = element.getInterpreter().fromString(data);  // translate data to usable object
        if (ret == null) ret = element.getDefaultValue();                   // fall back to default
        return ret;
    }

    /**
     * Sets the value of the specified element in this {@link Configuration}.  The element in question is implicitly
     * added to this {@link Configuration}.
     *
     * @param element The element whose value should be set.
     * @param value   The value to which the element should be set.
     * @return <code>true</code> if the new value is different from the old value; <code>false</code> if they are the
     *         same.
     */
    public <T> boolean setValue(ConfigurationElement<T> element, T value)
    {
        addElement(element);
        String data = element.getInterpreter().toString(value);
        if (data == null) data = element.getInterpreter().toString(element.getDefaultValue());
        if (data == null)
        {
            return (this.data.remove(element.getKey()) != null);
        } else
        {
            return (!data.equals(this.data.put(element.getKey(), data)));
        }
    }

    /**
     * Loads this {@link Configuration}'s data map from the provided {@link Map}<code>&lt;{@link String},{@link
     * String}&gt;</code>.  Any existing data in this {@link Configuration} is erased.
     *
     * @param map The map to use as the source of this {@link Configuration}'s data.
     */
    public void setData(Map<String, String> map)
    {
        data.clear();
        for (ConfigurationElement element : elements)
        {
            String data = map.get(element.getKey());
            if (data != null) this.data.put(element.getKey(), data);
        }
    }

    /**
     * Loads this {@link Configuration}'s data map from the provided {@link Properties} object.  Any existing data in
     * this {@link Configuration} is erased.
     *
     * @param properties The {@link Properties} object from which data will be loaded.
     */
    public void setData(Properties properties)
    {
        data.clear();
        for (Object key : properties.keySet())
        {
            if (key instanceof String)
            {
                String data = properties.getProperty((String) key);
                if (data != null) this.data.put((String) key, data);
            }
        }
    }

    /**
     * Loads this {@link Configuration} from the provided {@link File}.  The file should be in the same format used by
     * the Java {@link Properties} object.  If any error occurs while loading the data, this method returns an error
     * string.
     *
     * @param file The {@link File} from which to load.
     * @return A {@link String} describing any errors which occurred, or <code>null</code> if the operation was
     *         successful.  On failure, the contents of this {@link Configuration} object are unchanged.
     */
    public String load(File file)
    {
        Properties properties = new Properties();
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
            properties.load(fis);
            fis.close();
        } catch (IOException e)
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException e1)
                {
                }
            }
            return e.getMessage();
        }
        setData(properties);
        return null;
    }

    /**
     * Saves this {@link Configuration} to the provided {@link File}.  The file will be in the same format used by the
     * Java {@link Properties} object.  If any error occurs while loading the data, this method returns an error
     * string.
     * <p/>
     * This method only saves data for elements which have been added to this {@link Configuration} either by calls to
     * {@link Configuration#addElement(ConfigurationElement)} or implicitly through {@link
     * Configuration#getValue(ConfigurationElement)} and {@link Configuration#setValue(ConfigurationElement, Object)}.
     * Other data in the map is not saved.
     *
     * @param file The {@link File} to which to save this {@link Configuration}.
     * @return A {@link String} describing any errors which occurred, or <code>null</code> if the operation was
     *         successful.
     */
    public String save(File file)
    {
        Properties properties = new Properties();
        for (ConfigurationElement e : elements)
        {
            String key = e.getKey();
            String value = data.get(key);
            if (value == null) value = e.getInterpreter().toString(e.getDefaultValue());
            if (value != null)
            {
                properties.setProperty(key, value);
            }
        }
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
            properties.store(fos, "");
            fos.close();
        } catch (IOException e)
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                } catch (IOException e1)
                {
                }
            }
            return e.getMessage();
        }
        return null;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

}

// END OF FILE