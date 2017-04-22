package orioni.jz.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the general idea of an object cache.  In modern versions of Java (1.5+ at the time of this
 * writing) object allocation is very cheap; nonetheless, the cost of initializing an object after allocation can be
 * very high depending on the preparation work involved.  This object uses a mapping between a given descriptor type and
 * a given data type to act as a caching mechanism for the data.
 * <p/>
 * The size control on this cache is very simple since analysis of the cache contents is frequently not worth the
 * effort.  The cache is limited by a specific size; once that size is exceeded, the entire contents of the cache are
 * cleared, making room for new data.  This has the obvious drawback that any important data in the cache must now be
 * restored.  If the nature of the data generation is such that a smarter cache analysis is appropriate, implementing
 * classes are encouraged to override the {@link AbstractCache#ensureCacheLimit()} method.
 * <p/>
 * Users of the cache should access the data through the <code>getData(...)</code> method.  Implementers of the cache
 * are merely required to describe how the data is generated via the <code>generateData(...)</code> method.
 * <p/>
 * This cache is not capable of storing <code>null</code> values.
 *
 * @author Zachary Palmer
 */
public abstract class AbstractCache<DescriptorType, DataType> implements Cache<DescriptorType, DataType>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The size limit on this cache.
     */
    protected int sizeLimit;
    /**
     * The data storage mechanism for this cache.
     */
    protected Map<DescriptorType, DataType> data;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param sizeLimit The upper limit of the size of this cache in data elements.
     * @throws IllegalArgumentException If the specified size is less than or equal to <code>0</code>.
     */
    public AbstractCache(int sizeLimit)
            throws IllegalArgumentException
    {
        super();
        if (sizeLimit <= 0)
        {
            throw new IllegalArgumentException("Invalid cache size (" + sizeLimit + "): must be positive.");
        }
        this.sizeLimit = sizeLimit;
        data = new HashMap<DescriptorType, DataType>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is used to generate the data described by the provided descriptor.
     *
     * @param descriptor The descriptor which describes the data to be generated.
     * @return The data which is represented by the provided descriptor.
     */
    protected abstract DataType generateData(DescriptorType descriptor);

    /**
     * This method is used to prevent the cache from exceeding its maximum size.  After this method returns, the cache
     * should be of a size no larger than one less than the size limit.  Another data element will be added immediately
     * after this call.
     */
    protected void ensureCacheLimit()
    {
        if (data.size() >= sizeLimit - 1) clear();
    }

    /**
     * Explicitly clears the contents of this cache.
     */
    public void clear()
    {
        data.clear();
    }

    /**
     * This method is used to retrieve data from the cache.  If the described data element is not currently cached, it
     * will be generated.
     *
     * @param descriptor The object which describes the data to retrieve.
     * @return The described data.
     */
    public DataType getData(DescriptorType descriptor)
    {
        DataType ret = data.get(descriptor);
        if (ret == null)
        {
            ret = generateData(descriptor);
            data.put(descriptor, ret);
        }
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
