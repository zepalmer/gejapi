package orioni.jz.hashing;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * This {@link HashingAlgorithm} is used to produce a hash based upon the size of the file, array, or stream.
 *
 * @author Zachary Palmer
 */
public class LengthHashingAlgorithm extends HashingAlgorithm<Long>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /** A singleton instance. */
    public static final LengthHashingAlgorithm SINGLETON = new LengthHashingAlgorithm();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public LengthHashingAlgorithm()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Produces a hash for the specified {@link java.io.File}.
     *
     * @param file The {@link java.io.File} to hash.
     * @return A hash of the file.
     */
    public Long hash(File file)
    {
        return file.length();
    }

    /**
     * Performs this {@link HashingAlgorithm} on a set of input data provided through an {@link InputStream}.
     *
     * @param source The {@link java.io.InputStream} which will be used as a source for the data.  This {@link
     *               java.io.InputStream} will be read until it is exhausted.
     * @return The hash of the provided data.
     * @throws java.io.IOException If an I/O error occurs while retrieving data from the provided stream.
     */
    public Long hash(InputStream source)
            throws IOException
    {
        return source.skip(Long.MAX_VALUE);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
