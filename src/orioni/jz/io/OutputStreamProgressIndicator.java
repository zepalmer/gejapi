package orioni.jz.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This class represents a status indicator which is shown on a Java {@link OutputStream}.  Upon construction, the size
 * of the status indicator (in columns) is specified along with the range of the progress counter.  The progress counter
 * is incapable of moving backwards.  The user makes repeated, increasing calls to {@link
 * OutputStreamProgressIndicator#setProgress(int)}; the progress indicator is updated accordingly. <P> The display of
 * the progress indicator shows a pair of pipe characters ('<code>|</code>') separated by a number of dash characters
 * ('<code>-</code>').  A newline is then written and a number of asterisk characters ('<code>*</code>') equal to the
 * width of the progress indicator is written as the indicator progresses.  <B>Note:</B> a second newline is not
 * written.  It will generally be the desire of users of this class to write a newline to a stream after an instance of
 * this class is finished with it. <P> While the indicator is being used, the specified {@link OutputStream} should not
 * have other data written to it.  This will cause the display and further use of the progress indicator to become
 * unreliable.
 *
 * @author Zachary Palmer
 */
public class OutputStreamProgressIndicator
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The target {@link PrintStream} of the progress indicator.
     */
    protected PrintStream stream;
    /**
     * The width of the indicator.
     */
    protected int width;
    /**
     * The theoretical size of the progress indicator: the maximum parameter passed to {@link
     * OutputStreamProgressIndicator#setProgress(int)}.
     */
    protected int size;

    /**
     * The current absolute number of asterisks written to the target stream.
     */
    protected int visualProgress;
    /**
     * The last number which was provided to the {@link OutputStreamProgressIndicator#setProgress(int)} method.
     */
    protected int lastProgress;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param os    The {@link OutputStream} to which the indicator will be written.  This stream must be ready, as this
     *              constructor will write to it.
     * @param width The width of the indicator, in characters.
     * @param size  The theoretical size of the indicator, in undefined units.
     */
    public OutputStreamProgressIndicator(OutputStream os, int width, int size)
    {
        super();
        if (os instanceof PrintStream)
        {
            stream = (PrintStream) os;
        } else
        {
            stream = new PrintStream(os);
        }
        this.width = width;
        this.size = size;
        visualProgress = 0;

        stream.print('|');
        for (int i = 0; i < width - 2; i++)
        {
            stream.print('-');
        }
        if (width > 1) stream.print('|');
        stream.println();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the progress value for this indicator.
     *
     * @return The current progress value for this indicator.
     */
    public int getProgress()
    {
        return lastProgress;
    }

    /**
     * Increments this progress indicator by one.
     */
    public void incProgress()
    {
        setProgress(getProgress() + 1);
    }

    /**
     * Updates the progress of the process that is using this indicator.  The specified value should be provided in the
     * same unit type in which the <code>size</code> parameter was specified at construction time.  If the parameter of
     * this method is less than any previous call to this method or less than <code>0</code>, the behavior of this
     * method and of the indicator is undefined.
     *
     * @param progress The new value for the progress counter in this indicator.
     */
    public void setProgress(int progress)
    {
        lastProgress = progress;
        while (progress * width / size > visualProgress)
        {
            visualProgress++;
            stream.print('*');
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}