package orioni.jz.io.files;

import orioni.jz.io.MultiplexInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This implementation of the <code>MultiplexInputStream</code> allows the target of writing to be a series of
 * <code>FileOutputStream</code> objects.  These objects are created based upon the filenames returned by a
 * <code>FilenameFactory</code> which is passed in upon construction time.
 * <p/>
 * When a byte is read, this stream determines whether or not the file it has open has any remaining bytes left.  If it
 * does, a byte is returned from that file.  Otherwise, a filename is retrieved from the filename factory and a new file
 * is opened from which data is to be read.  Files are exhausted entirely before a new file is opened.
 * <p/>
 * The <code>FilenameFactory.getFilename(int)</code> method is passed a series of numbers from <code>0</code> to
 * <code>n</code> until the stream is closed.  A call to <code>FilenameFactory.getFilename(int)</code> by this class
 * will never have an index value of less than the last call.
 *
 * @author Zachary Palmer
 */
public class MultiplexIndexedFileInputStream extends MultiplexInputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The <code>FilenameFactory</code> from which filenames will be generated.
     */
    protected FilenameFactory filenameFactory;
    /**
     * The index for the next filename to be retrieved.
     */
    protected int index;
    /**
     * The current <code>FileInputStream</code> from which data is currently being read.
     */
    protected FileInputStream currentInputStream;
    /**
     * The number of files in the multiplex series.
     */
    protected int files;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param factory The <code>FilenameFactory</code> to use in retrieving the filenames for the files to open.
     * @param files   The number of files in the multiplex series.
     */
    public MultiplexIndexedFileInputStream(FilenameFactory factory, int files)
    {
        super();
        filenameFactory = factory;
        index = 0;
        this.files = files;
        currentInputStream = null;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method provides input streams from which further data can be read.  Any number of streams may be provided by
     * this method; the more streams provided, the more information that {@link MultiplexInputStream} will have to
     * improve the efficiency of its operations.
     * <p/>
     * {@link MultiplexInputStream} guarantees that the provided streams will be fully exhausted before this method is
     * called again.
     *
     * @return At least one {@link InputStream} from which data should be read, or <code>null</code> if no additional
     *         streams can be obtained.
     * @throws IOException If there is a problem creating or retrieving the stream.
     */
    public InputStream[] getMoreStreams()
            throws IOException
    {
        if (index < files)
        {
            return new InputStream[]{new FileInputStream(new File(filenameFactory.getFilename(index++)))};
        } else
        {
            return null;
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //