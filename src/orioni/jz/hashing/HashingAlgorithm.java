package orioni.jz.hashing;

import java.io.*;

/**
 * This interface is meant to be implemented by all hashing algorithms in the {@link orioni.jz.hashing} package.
 *
 * @author Zachary Palmer
 */
public abstract class HashingAlgorithm<T>
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Performs this hashing algorithm on a set of input data contained within a <code>byte[]</code>.
     *
     * @param data The <code>byte[]</code> from which the data should be retrieved.
     * @return The hash of the provided data.
     */
    public T hash(byte[] data)
    {
        try
        {
            return hash(new ByteArrayInputStream(data));
        } catch (IOException e)
        {
            // Okay, ByteArrayInputStream *can't* throw an IOException.  If this happens, things are really messed up.
            throw new IllegalStateException("ByteArrayInputStream threw an IOException!", e);
        }
    }

    /**
     * Performs this {@link HashingAlgorithm} on a set of input data provided through an {@link java.io.InputStream}.
     *
     * @param source The {@link InputStream} which will be used as a source for the data.  This {@link InputStream} will
     *               be read until it is exhausted.
     * @return The hash of the provided data.
     * @throws IOException If an I/O error occurs while retrieving data from the provided stream.
     */
    public abstract T hash(InputStream source)
            throws IOException;

    /**
     * Performs this {@link HashingAlgorithm} on a given file.
     *
     * @param file The {@link File} used as a source for the data.
     * @return The hash of the provided data.
     * @throws IOException If an I/O error occurs while retrieving data from the provided stream.
     */
    public T hash(File file)
            throws IOException
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
            return hash(fis);
        } finally
        {
            try
            {
                if (fis != null) fis.close();
            } catch (IOException e)
            {
            }
        }
    }
}

// END OF FILE