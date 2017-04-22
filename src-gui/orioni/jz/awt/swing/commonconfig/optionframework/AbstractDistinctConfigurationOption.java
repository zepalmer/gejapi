package orioni.jz.awt.swing.commonconfig.optionframework;

import orioni.jz.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Instances of this class represent configuration options which are selected over a small set of distinct,
 * noncontinuous options.  The usual editing method for this option type is the combo box.  The value as stored in the
 * file is different from that which is displayed in an editing dialog.  To translate between the display and stored
 * value, use the {@link AbstractDistinctConfigurationOption#toDisplayValue(String)} and {@link
 * AbstractDistinctConfigurationOption#toStoredValue(String)} methods.
 * <p/>
 * <b>Note:</b> Both the stored and displayed values must be <i>unique</i> within their sets.  If a duplicate stored
 * value <i>or</i> a duplicate display value appear, the behavior of this class is unspecified.
 *
 * @author Zachary Palmer
 */
public abstract class AbstractDistinctConfigurationOption extends AbstractConfigurationOption
        implements DistinctConfigurationOption
{
    /**
     * The {@link Map}ping between the {@link String}s which represent stored values and the {@link String}s which
     * represent display values.
     */
    protected Map<String, String> storedToDisplayMap;
    /**
     * The {@link Map}ping between the {@link String}s which represent display values and the {@link String}s which
     * represent stored values.
     */
    protected Map<String, String> displayToStoredMap;

    /**
     * General constructor.  Uses the {@link Pair} class.
     *
     * @param key    The key of the property that this {@link ConfigurationOption} represents.
     * @param group  The group to which this option belongs.
     * @param values A {@link Collection} of {@link Pair}<code>&lt;{@link String},{@link String}&gt;</code> objects.
     *               The first component of the pair is the <i>stored</i> value of the selection; the second is how it
     *               is <i>displayed</i>.
     */
    public AbstractDistinctConfigurationOption(String key, String group, Collection<Pair<String, String>> values)
    {
        super(key, group);
        storedToDisplayMap = new HashMap<String, String>();
        displayToStoredMap = new HashMap<String, String>();
        for (Pair<String, String> pair : values)
        {
            storedToDisplayMap.put(pair.getFirst(), pair.getSecond());
            displayToStoredMap.put(pair.getSecond(), pair.getFirst());
        }
    }

    /**
     * General constructor.  Uses parallel arrays.
     *
     * @param key     The key of the property that this {@link ConfigurationOption} represents.
     * @param group   The group to which this option belongs.
     * @param stored  An array of {@link String}s representing the stored values of this option.
     * @param display An array of {@link String}s representing the displayed values of this option.
     */
    public AbstractDistinctConfigurationOption(String key, String group, String[] stored, String[] display)
    {
        super(key, group);
        if (stored.length != display.length)
        {
            throw new IllegalArgumentException(
                    "stored length (" + stored.length + ") and display length (" + display.length +
                    ") do not match");
        }
        storedToDisplayMap = new HashMap<String, String>();
        displayToStoredMap = new HashMap<String, String>();
        for (int i = 0; i < stored.length; i++)
        {
            storedToDisplayMap.put(stored[i], display[i]);
            displayToStoredMap.put(display[i], stored[i]);
        }
    }

    /**
     * Translates a stored value to a display value for this option.
     *
     * @param stored The stored value.
     * @return The corresponding display value, or <code>null</code> if the provided parameter was not a stored value.
     */
    public String toDisplayValue(String stored)
    {
        return storedToDisplayMap.get(stored);
    }

    /**
     * Translates a display value to a display value for this option.
     *
     * @param display The display value.
     * @return The corresponding stored value, or <code>null</code> if the provided parameter was not a display value.
     */
    public String toStoredValue(String display)
    {
        return displayToStoredMap.get(display);
    }
}

// END OF FILE