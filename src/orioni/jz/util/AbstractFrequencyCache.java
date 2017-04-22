package orioni.jz.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@link AbstractCache} extension is intended for cache implementations whose generation of data is extremely
 * costly and therefore requires a more intelligent cache limiting algorithm than that provided by the {@link
 * AbstractCache#ensureCacheLimit()} method.
 * <p/>
 * This class ensures the cache limit by maintaining a list sorted by the last access time of each
 * <code>DescriptorType</code> used to access the cache.  Since updating the list requires locating the
 * <code>DescriptorType</code> in the list and changing its position, that operation alone is O(<b>n</b>) for an
 * <b>n</b>-element cache; thus, the intelligent discarding method used by this cache class is only suitable for those
 * caches with very few elements and very expensive element generation methods.
 *
 * @author Zachary Palmer
 */
public abstract class AbstractFrequencyCache<DescriptorType, DataType> extends AbstractCache<DescriptorType, DataType>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * This list contains the most recently accessed {@link DescriptorType}s, in order of access time starting with the
     * least reccent.
     */
    protected List<DescriptorType> accessList;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param size The maximum size of this cache.
     */
    public AbstractFrequencyCache(int size)
    {
        super(size);
        accessList = new ArrayList<DescriptorType>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method is used to prevent the cache from exceeding its maximum size.  After this method returns, the cache
     * should be of a size no larger than one less than the size limit.  Another data element will be added immediately
     * after this call.
     */
    protected void ensureCacheLimit()
    {
        while (accessList.size() >= sizeLimit)
        {
            data.remove(accessList.remove(0));
        }
    }

    /**
     * Explicitly clears the contents of this cache.
     */
    public void clear()
    {
        accessList.clear();
        super.clear();
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
        accessList.remove(descriptor);
        accessList.add(descriptor);
        return super.getData(descriptor);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
