package orioni.jz.io.files;

import orioni.jz.io.MultiplexOutputStream;
import orioni.jz.util.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This implementation of the <code>MultiplexOutputStream</code> allows the target of writing to be a series of files
 * specified by a <code>FilenameFactory</code>.  Each file is filled to a maximum number of bytes sequentially; that is,
 * no two files are open at any given time as writing targets.  The maximum number of bytes written to a file is
 * specified at construction time. <P> The <code>FilenameFactory.getFilename(int)</code> method is passed a series of
 * numbers from <code>0</code> to <code>n</code> until the stream is closed.  A call to
 * <code>FilenameFactory.getFilename(int)</code> by this class will never have an index value of less than the last
 * call.
 *
 * @author Zachary Palmer
 */
public class MultiplexSizeLimitedIndexedFileOutputStream extends MultiplexOutputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link FilenameFactory} from which this stream gets the filenames of the files which act as sources for this
     * stream's data.
     */
    protected FilenameFactory filenameFactory;
    /**
     * The maximum number of bytes to write to each file before moving to the next.
     */
    protected long maximumSize;

    /**
     * The current <code>FileOutputStream</code> to use as an output target.
     */
    protected FileOutputStream currentOutputStream;
    /**
     * The index with which filenames are retrieved.
     */
    protected int index;

    /**
     * The number of bytes that have been written to the current output target.
     */
    protected int writtenThisTarget;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param factory The <code>FilenameFactory</code> from which this stream should retrieve the filenames from which
     *                it gets its data.
     * @param size    The maximum size of each file in bytes.
     */
    public MultiplexSizeLimitedIndexedFileOutputStream(FilenameFactory factory, long size)
    {
        super();
        filenameFactory = factory;
        maximumSize = size;
        currentOutputStream = null;
        index = 0;
        writtenThisTarget = 0;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method retrieves the {@link OutputStream} to which the provided data should be written, combined with the
     * total amount of data which should be written to that stream.  This method is used to control the manner in which
     * data is written to the {@link OutputStream}s.
     *
     * @param data The array containing data which is to be written to some output stream.
     * @param off  The starting offset of the data in the array.
     * @param len  The length of the data to be written to an {@link OutputStream}.
     * @return A {@link Pair}<code>&lt;{@link OutputStream},<code>int&gt;</code>.  The {@link OutputStream} is the
     *         stream to which data should be written; the <code>int</code> is the amount of data to write.
     */
    public Pair<OutputStream, Integer> getNextWriteOperation(byte[] data, int off, int len)
            throws IOException
    {
        if (currentOutputStream == null)
        {
            currentOutputStream = new FileOutputStream(filenameFactory.getFilename(index++));
            writtenThisTarget = 0;
        }
        int writeSize = (int) (Math.min(len, maximumSize - writtenThisTarget));
        Pair<OutputStream, Integer> ret = new Pair<OutputStream, Integer>(currentOutputStream, writeSize);
        writtenThisTarget += writeSize;
        if (writtenThisTarget == maximumSize)
        {
            currentOutputStream = null;
        }
        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //