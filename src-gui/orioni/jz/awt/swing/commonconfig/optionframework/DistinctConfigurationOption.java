package orioni.jz.awt.swing.commonconfig.optionframework;


/**
 * This {@link ConfigurationOption} extension is intended for those {@link ConfigurationOption}s which are designed
 * to provide a small set of distinct options, usually as would be represented by a combo box in a GUI.  For example,
 * an option which requires the entry of a primary or secondary color would be represented by this interface.
 *
 * @author Zachary Palmer
 */
public interface DistinctConfigurationOption extends ConfigurationOption
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Translates a stored value to a display value for this option.
     *
     * @param stored The stored value.
     * @return The corresponding display value, or <code>null</code> if the provided parameter was not a stored
     *         value.
     */
    String toDisplayValue(String stored);

    /**
     * Translates a display value to a display value for this option.
     *
     * @param display The display value.
     * @return The corresponding stored value, or <code>null</code> if the provided parameter was not a display
     *         value.
     */
    String toStoredValue(String display);
}

// END OF FILE