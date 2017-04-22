package orioni.jz.awt.swing.commonconfig.optionframework;



/**
 * This {@link ConfigurationOption} implementation allows for basic generation of key and group data in a {@link
 * ConfigurationOption}, as well as
 *
 * @author Zachary Palmer
 */
public abstract class AbstractConfigurationOption implements ConfigurationOption
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////


    /**
     * The key of the property that this {@link ConfigurationOption} represents.
     */
    protected String key;
    /**
     * The group to which this {@link ConfigurationOption} belongs.
     */
    protected String group;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param key   The key of the property that this {@link ConfigurationOption} represents.
     * @param group The group to which this {@link ConfigurationOption} belongs.
     */
    public AbstractConfigurationOption(String key, String group)
    {
        super();
        this.key = key;
        this.group = group;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * The group to which this option belongs.  Examples of "groups" include appearance, filesystem, networking, and
     * other such categories.
     *
     * @return The group to which this option belongs.
     */
    public String getGroup()
    {
        return group;
    }

    /**
     * Retrieves the key of the property that tihs {@link AbstractConfigurationOption} represents.
     *
     * @return The aforementioned key.
     */
    public String getKey()
    {
        return key;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE