package orioni.jz.util;

import orioni.jz.util.strings.StringUtilities;

import java.io.PrintStream;

/**
 * This {@link ProgressTracker.Listener} is designed to monitor changes on a {@link ProgressTracker} and report
 * percentages of completion to a {@link PrintStream}.  It performs this task by writing units of four characters to the
 * {@link PrintStream}.  Any previous values which have been written are erased by writing backspaces.  When work is
 * complete, this {@link ProgressTracker.Listener} will erase the last entry it made, leaving the stream in the same
 * state it was before the writes assuming that backspaces are interpreted to erase characters.
 * <p/>
 * It is not advised to have a single {@link ConsoleProgressListener} registered with more than one {@link
 * ProgressTracker} at a time.
 *
 * @author Zachary Palmer
 */
public class ConsoleProgressListener implements ProgressTracker.Listener
{
    /**
     * The {@link PrintStream} to which this {@link ConsoleProgressListener} is writing.
     */
    protected PrintStream printStream;
    /**
     * The number of characters this {@link ConsoleProgressListener} has produced on the stream.
     */
    protected int charactersWrittenNet;
    /**
     * The last percentage that was written to the {@link PrintStream}.
     */
    protected int lastPercentage;

    /**
     * General constructor.
     *
     * @param ps The {@link java.io.PrintStream} to use.
     */
    public ConsoleProgressListener(PrintStream ps)
    {
        printStream = ps;
        charactersWrittenNet = 0;
        lastPercentage = -1;
    }

    /**
     * Notifies this {@link ConsoleProgressListener} that a {@link ProgressTracker} with which it is registered has been
     * changed.
     *
     * @param tracker The {@link ProgressTracker} which has been changed.
     * @param amount  The amount by which that {@link ProgressTracker} has changed.
     */
    public void changeObserved(ProgressTracker tracker, double amount)
    {
        int percent = (int) tracker.getPercentCompleted();
        if (percent != lastPercentage)
        {
            while (charactersWrittenNet > 0)
            {
                printStream.print('\b');
                charactersWrittenNet--;
            }
            if (tracker.getPercentCompleted() < 100)
            {
                String s = StringUtilities.padLeft(Integer.toString(percent), ' ', 3) + "%";
                charactersWrittenNet += s.length();
                printStream.print(s);
            }
            lastPercentage = percent;
        }
    }
}

// END OF FILE
