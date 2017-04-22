package orioni.jz.io.files;

/**
 * This interface is designed solely to provide classes which work with files with a filename based on an index.  It is
 * useful in applications such as the <code>MultiplexIndexedFileInputStream</code> and <code>MultiplexFileOutputStream</code>;
 * both of these classes use the same index-based filenames.
 * <P>
 * Generated filenames can be retrieved from a list, generated based on the index, or provided from any other source.
 * The calls to the function should be repeatable; that is, <code>getFilename(x)</code> should always return the same
 * value for a given value of <code>x</code>.
 *
 * @author Zachary Palmer
 */
public interface FilenameFactory
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * This function generates filenames for various purposes.  The filename returned is based on the index provided.
     * For any given value <code>x</code> for the index, the same filename should be returned for every call to
     * <code>getFilename(x)</code>.
     * <P>
     * If a filename cannot be properly generated from the index provided, this function may throw a runtime exception.
     *
     * @param index The index for which to generate a filename.
     * @return A filename based on the index provided.
     * @throws IllegalArgumentException If no filename can be generated from the index provided.
     */
    public String getFilename(int index)
        throws IllegalArgumentException;

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

}

// END OF FILE //