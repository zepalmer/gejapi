package orioni.jz.io.files;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.EOFException;

/**
 * This {@link InputStream} is designed to provide streaming input for a {@link RandomAccessFile}.  The first read byte
 * is that at which the {@link RandomAccessFile}'s pointer is currently positioned; further reads increment the pointer
 * accordingly.
 *
 * @author Zachary Palmer
 */
public class RandomAccessFileInputStream extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link RandomAccessFile} providing the data.
     */
    protected RandomAccessFile raf;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param randomAccessFile The underlying {@link RandomAccessFile} from which to read data.
     */
    public RandomAccessFileInputStream(RandomAccessFile randomAccessFile)
    {
        super();
        raf = randomAccessFile;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Reads the next byte of data from the random access file.
     *
     * @return The next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        try
        {
            return (raf.readByte() & 0xFF);
        } catch (EOFException eofe)
        {
            return -1;
        }
    }

    /**
     * Reads up to <code>len</code> bytes of data from the random access file into an array of bytes.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array <code>b</code> at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
     *         end of the stream has been reached.
     * @throws IOException          if an I/O error occurs.
     * @throws NullPointerException if <code>b</code> is <code>null</code>.
     * @see InputStream#read()
     */
    public int read(byte[] b, int off, int len)
            throws IOException
    {
        return raf.read(b,off,len);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE