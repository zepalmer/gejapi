package orioni.jz.util;

/**
 * This interface is designed to represent the public interface for a caching system.  Use of this interface rather than
 * {@link AbstractCache} or any of its implementers allows wrappers (such as {@link SynchronizedCache} to be used.
 *
 * @author Zachary Palmer
 */
public interface Cache<DescriptorType,DataType>
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Explicitly clears the contents of this cache.
     */
    public void clear();

    /**
     * This method is used to retrieve data from the cache.  If the described data element is not currently cached, it
     * will be generated.
     *
     * @param descriptor The object which describes the data to retrieve.
     * @return The described data.
     */
    public DataType getData(DescriptorType descriptor);
}
