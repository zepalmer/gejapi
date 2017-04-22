package orioni.jz.util.configuration;

import orioni.jz.util.strings.BoundedIntegerInterpreter;

/**
 * This {@link ConfigurationElement} extension only accepts an {@link Integer} if it falls within a certain bound.
 * Integers outside of this range are converted to <code>null</code>.
 *
 * @author Zachary Palmer
 */
public class BoundedIntegerConfigurationElement extends ConfigurationElement<Integer>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param key           The {@link String} used to key this {@link ConfigurationElement}.
     * @param defaultValue The default value for this {@link ConfigurationElement}.
     * @param min           The lowest accepted {@link Integer}.
     * @param max           The highest accepted {@link Integer}.
     */
    public BoundedIntegerConfigurationElement(String key, int defaultValue, int min, int max)
    {
        super(key, new BoundedIntegerInterpreter(min, max), defaultValue);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the minimum bound of this {@link ConfigurationElement}.
     * @return The minimum bound.
     */
    public int getMinimumBound()
    {
        return ((BoundedIntegerInterpreter)(interpreter)).getMinimum();
    }

    /**
     * Retrieves the maximum bound of this {@link ConfigurationElement}.
     * @return The maximum bound.
     */
    public int getMaximumBound()
    {
        return ((BoundedIntegerInterpreter)(interpreter)).getMaximum();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE