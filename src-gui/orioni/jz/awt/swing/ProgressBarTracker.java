package orioni.jz.awt.swing;

import orioni.jz.util.ProgressTracker;

import javax.swing.*;

/**
 * This {@link ProgressTracker} extension is designed to manipulate a {@link JProgressBar} as the {@link
 * ProgressTracker} is updated.
 *
 * @author Zachary Palmer
 */
public class ProgressBarTracker extends ProgressTracker
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The progress bar that this tracker is updating.
     */
    private JProgressBar progressBar;
    /**
     * Whether or not a percentage string is displayed by this tracker.  If a percentage string is displayed, this
     * variable is also used to control the update thread which ensures that the timer is frequently updated.
     */
    private boolean usePercentageString;
    /**
     * Whether or not a time remaining string is displayed by this tracker.
     */
    private boolean useTimeLeftString;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the percentage string is displayed but that the time remaining string is
     * not.
     *
     * @param bar The {@link JProgressBar} to update as the tracker is updated.
     */
    public ProgressBarTracker(final JProgressBar bar)
    {
        this(bar, true);
    }

    /**
     * Skeleton constructor.  Assumes that the time remaining string will not be displayed.
     *
     * @param bar               The {@link JProgressBar} to update as the tracker is updated.
     * @param percentageString <code>true</code> if it is desired that this tracker set the progress bar's string to be
     *                          a percentage of completion; <code>false</code> otherwise.
     */
    public ProgressBarTracker(final JProgressBar bar, final boolean percentageString)
    {
        this(bar, percentageString, false);
    }

    /**
     * General constructor.
     *
     * @param bar               The {@link JProgressBar} to update as the tracker is updated.
     * @param percentageString <code>true</code> if it is desired that this tracker set the progress bar's string to be
     *                          a percentage of completion; <code>false</code> otherwise.
     * @param timeLeftString  <code>true</code> if it is desired that this tracker set the progress bar's string to be
     *                          an estimate of time remaining; <code>false</code> otherwise.
     */
    public ProgressBarTracker(final JProgressBar bar, final boolean percentageString, final boolean timeLeftString)
    {
        super();
        progressBar = bar;
        usePercentageString = percentageString;
        useTimeLeftString = timeLeftString;

        progressBar.setIndeterminate(false);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        if ((usePercentageString) || (useTimeLeftString)) progressBar.setStringPainted(true);
        this.addListener(
                new ProgressTracker.Listener()
                {
                    public void changeObserved(ProgressTracker tracker, double amount)
                    {
                        int completed = (int) (tracker.getPercentCompleted());
                        progressBar.setValue(completed);

                        // update status bar text
                        if (Double.compare(tracker.getEndingValue(), tracker.getProgress()) == 0)
                        {
                            useTimeLeftString = false;
                        }
                        if (usePercentageString)
                        {
                            if (useTimeLeftString)
                            {
                                progressBar.setString(
                                        (int) (getPercentCompleted()) + "%, " + getEstimatedTimeToCompletionString());
                            } else
                            {
                                progressBar.setString((int) (getPercentCompleted()) + "%");
                            }
                        } else if (useTimeLeftString)
                        {
                            progressBar.setString(getEstimatedTimeToCompletionString());
                        }
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
