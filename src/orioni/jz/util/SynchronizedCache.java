package orioni.jz.util;

/**
 * This {@link Cache} implementation acts as a wrapper for other {@link Cache} objects.  It is designed to provide
 * access to those objects in a synchronized manner.  For this synchronization to work properly, all access to the
 * underlying cache must be completed through use of its wrapper.
 *
 * @author Zachary Palmer
 */
public class SynchronizedCache<DescriptorType,DataType> implements Cache<DescriptorType, DataType>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The cache which is wrapped.
     */
    protected Cache<DescriptorType, DataType> underlyingCache;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param cache The {@link Cache} to wrap.
     */
    public SynchronizedCache(Cache<DescriptorType, DataType> cache)
    {
        super();
        underlyingCache = cache;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Explicitly clears the contents of this cache.
     */
    public synchronized void clear()
    {
        underlyingCache.clear();
    }

    /**
     * This method is used to retrieve data from the cache.  If the described data element is not currently cached, it
     * will be generated.
     *
     * @param descriptor The object which describes the data to retrieve.
     * @return The described data.
     */
    public synchronized DataType getData(DescriptorType descriptor)
    {
        return underlyingCache.getData(descriptor);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
