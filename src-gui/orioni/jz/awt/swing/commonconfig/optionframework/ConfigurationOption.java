package orioni.jz.awt.swing.commonconfig.optionframework;

import orioni.jz.awt.swing.commonconfig.ApplicationConfiguration;


/**
 * This interface is implemented by any class which wishes to represent a configuration option in an {@link
 * ApplicationConfiguration} or related tool.  It provides functionality which can be used to describe a configuration
 * option, such as a grouping and the key under which the option's property setting is stored.
 *
 * @author Zachary Palmer
 */
public interface ConfigurationOption
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the key of the property that tihs {@link AbstractConfigurationOption} represents.
     *
     * @return The aforementioned key.
     */
    public String getKey();

    /**
     * The group to which this option belongs.  Examples of "groups" include appearance, filesystem, networking, and
     * other such categories.
     *
     * @return The group to which this option belongs.
     */
    public String getGroup();

    /**
     * Enforces this {@link ConfigurationOption}.  When this method is called, the option which is represented by this
     * object is effected.  For example, if this option were to represent the background color of a panel, calling this
     * method would actually perform the setting of that panel's background color.
     * <p/>
     * This method will usually be assisted by data provided through other method calls.  In the above example, for
     * instance, the panel whose background is to be set would have been provided to a different method prior to this
     * call.
     *
     * @param configuration The {@link ApplicationConfiguration} on which this option should be enforced.  The {@link
     *                      ApplicationConfiguration} is used to determine what parts of the application are to be
     *                      affected and in what way.
     * @return A string containing error messages separated by newlines, or <code>null</code> if no error occurred.
     */
    public String enforce(ApplicationConfiguration configuration);

    /**
     * Determines whether or not this {@link ConfigurationOption} is defaultable.
     *
     * @return <code>true</code> if this method can have an application-independent default; <code>false</code> if the
     *         value of this option applies only to one specific application.
     */
    public boolean isDefaultable();
}

// END OF FILE