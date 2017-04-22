package orioni.jz.hashing;

import orioni.jz.util.ComparableList;

import java.io.IOException;
import java.io.InputStream;

/**
 * This {@link HashingAlgorithm} uses the SHA1 code from {@link SHA1} but returns the result as a list of integers
 * rather than an array.  This is slightly less efficient but more compatible with the Java object structure (since the
 * list properly provides hash and equals functionality).
 *
 * @author Zachary Palmer
 */
public class ComparableSHA1 extends HashingAlgorithm<ComparableList<Integer>>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The singleton instance of this class.
     */
    public static final ComparableSHA1 SINGLETON = new ComparableSHA1();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public ComparableSHA1()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Performs this {@link orioni.jz.hashing.HashingAlgorithm} on a set of input data provided through an {@link
     * java.io.InputStream}.
     *
     * @param source The {@link java.io.InputStream} which will be used as a source for the data.  This {@link
     *               java.io.InputStream} will be read until it is exhausted.
     * @return The hash of the provided data.
     * @throws java.io.IOException If an I/O error occurs while retrieving data from the provided stream.
     */
    public ComparableList<Integer> hash(InputStream source)
            throws IOException
    {
        int[] hash = SHA1.SINGLETON.hash(source);
        ComparableList<Integer> ret = new ComparableList<Integer>();
        for (int i : hash) ret.add(i);
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
