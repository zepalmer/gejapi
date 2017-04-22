package orioni.jz.util;

import orioni.jz.math.MathUtilities;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is designed to provide a method by which a unit of code may report progress on a task to another unit of
 * code.  In many such cases, a progress indicator of some kind is desired and it is not necessarily appropriate for the
 * code segment performing the workload to directly manipulate the progress indicator in question.  The {@link
 * ProgressTracker} class is designed to lower coupling between these two units of code.
 * <p/>
 * In a normal case, the workload code accepts a {@link ProgressTracker} as a parameter.  It then makes calls to the
 * {@link ProgressTracker} indicating its progress.  These calls are reported to any {@link ProgressTracker.Listener}s
 * which have been registered.  Presumably, the progress indication code has registered such a listener with the same
 * {@link ProgressTracker} and will act upon listener calls accordingly.
 * <p/>
 * {@link ProgressTracker}s are defined to range between two values: one representing no work accomplished and the other
 * representing all work accomplished.  Usually, the latter will be greater than the prior.  However, it may
 * occasionally be necessary to change the range of values over which a {@link ProgressTracker} operates even while it
 * is being used (such as when the workload code has discovered a previously unanticipated task to be performed). The
 * {@link ProgressTracker} class allows the starting and ending values to be changed during operation.  If rollback
 * changes (see below) are not permitted, the percentage reported by the {@link ProgressTracker#getPercentCompleted()}
 * method will return the greatest value thus far observed by the {@link ProgressTracker}.
 * <p/>
 * This class also supports the ability to suppress changes which decrease the perceived progress made.  For example,
 * consider a case in which a {@link ProgressTracker} ranges from progress values of <code>0.0</code> to
 * <code>10.0</code>.  Presume two updates are reported: one which moves the {@link ProgressTracker} to a value of
 * <code>5.0</code> and another which moves the {@link ProgressTracker} to a value of <code>3.0</code>.  The latter
 * would seem to suggest that the progress indicator display a lesser value than was previously displayed and, in some
 * user interfaces, this is undesirable.  The {@link ProgressTracker} can optionally be instructed not to change its
 * present progress value.  Nonetheless, all {@link ProgressTracker.Listener}s are notified of the event.
 * <p/>
 * A significant scalability feature of this class can be found by the {@link ProgressTracker}<code>.getSubtrackerByValue(...)</code>
 * and {@link ProgressTracker}<code>.getSubtrackerByPercentage(...)</code> methods.  These methods create a sub-tracker:
 * a {@link ProgressTracker} object which represents a subsection of this {@link ProgressTracker}'s total progress
 * range.  In significantly complex tasks, the distinction between workload code and progress reporting code can become
 * very blurred; the method called to do the work may call several other methods which are delegated significant
 * portions of the task.  In such a situation, these secondary methods would be passed subtrackers (which could, in
 * turn, produce additional subtrackers) in order to track their portion specifically.
 * <p/>
 * Once the task tracked by a {@link ProgressTracker} is finished, it is suggested that the {@link
 * ProgressTracker#setProgressCompleted()} method be called.  As <code>double</code> fields are used to track the
 * progress, any amount of rounding error may reduce the amount of total progress value accumulated by an unexpected
 * amount.  The aforementioned method is a method of convenience, setting the progress value on the {@link
 * ProgressTracker} to the ending value, ensuring that the methods on the {@link ProgressTracker} report completion.
 *
 * @author Zachary Palmer
 */
public class ProgressTracker
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The starting value for this {@link ProgressTracker}.
     */
    protected double startingValue;
    /**
     * The ending value for this {@link ProgressTracker}.
     */
    protected double endingValue;
    /**
     * The current value for this {@link ProgressTracker}.
     */
    protected double currentValue;
    /**
     * The highest percentage of completion thus far reported to this {@link ProgressTracker}.
     */
    protected double highestPercentage;
    /**
     * Whether or not this {@link ProgressTracker} will allow rollback changes.
     */
    protected boolean permitRollback;

    /**
     * The pivot time at which {@link ProgressTracker#setProgress(double)} was called.  This is used to produce an
     * estimated time to completion.
     */
    protected long startTime;

    /**
     * The {@link Set} of {@link Listener}s which are to be notified in the event that this {@link ProgressTracker} is
     * changed.
     */
    protected Set<Listener> listeners;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a starting value of <code>0.0</code> and an ending value of <code>100.0</code>.
     * Also assumes that rollback is not permitted.
     */
    public ProgressTracker()
    {
        this(0.0, 100.0, false);
    }

    /**
     * Skeleton constructor.  Assumes a starting value of <code>0.0</code> and an ending value of <code>100.0</code>.
     *
     * @param permitRollback <code>true</code> if this {@link ProgressTracker} should permit calls to it to roll the
     *                        present progress backward; <code>false</code> if such calls are not to change the state of
     *                        the {@link ProgressTracker}.
     */
    public ProgressTracker(boolean permitRollback)
    {
        this(0.0, 100.0, permitRollback);
    }

    /**
     * Skeleton constructor.  Assumes that rollback is not permitted.
     *
     * @param startingValue The starting value for this {@link ProgressTracker}.
     * @param endingValue   The ending value for this {@link ProgressTracker}; that is, the value which represents that
     *                       the task in question has been completed.
     */
    public ProgressTracker(double startingValue, double endingValue)
    {
        this(startingValue, endingValue, false);
    }

    /**
     * General constructor.
     *
     * @param startingValue  The starting value for this {@link ProgressTracker}.
     * @param endingValue    The ending value for this {@link ProgressTracker}; that is, the value which represents
     *                        that the task in question has been completed.
     * @param permitRollback <code>true</code> if this {@link ProgressTracker} should permit calls to it to roll the
     *                        present progress backward; <code>false</code> if such calls are not to change the state of
     *                        the {@link ProgressTracker}.
     */
    public ProgressTracker(double startingValue, double endingValue, boolean permitRollback)
    {
        super();
        this.startingValue = startingValue;
        this.endingValue = endingValue;
        this.permitRollback = permitRollback;
        currentValue = this.startingValue;
        listeners = new HashSet<Listener>();
        highestPercentage = 0.0;
        startTime = -1;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a {@link Listener} to this {@link ProgressTracker}.
     *
     * @param listener The {@link Listener} to add.
     */
    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes a {@link Listener} from this {@link ProgressTracker}.
     *
     * @param listener The {@link Listener} to remove.
     */
    public void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Notifies all {@link Listener}s that this {@link ProgressTracker} has been changed.
     *
     * @param amount The amount by which this {@link ProgressTracker} has been changed.
     */
    protected void notifyListeners(double amount)
    {
        for (Listener listener : listeners)
        {
            listener.changeObserved(this, amount);
        }
    }

    /**
     * Retrieves the current progress value.
     *
     * @return The current progress value.
     */
    public double getProgress()
    {
        return currentValue;
    }

    /**
     * Reports a change in the amount of work completed.  The provided value is added to the current progress value.
     *
     * @param progress The amount of progress to add to the current value.  If this value would take the progress value
     *                 beyond the bounds of the {@link ProgressTracker}, it instead takes it to the appropriate bound.
     */
    public void incrementProgress(double progress)
    {
        setProgress(currentValue + progress);
    }

    /**
     * Reports a change in the amount of work completed.  The provided value is used as the new progress value.
     *
     * @param progress The new progress value for this {@link ProgressTracker}.  If that value is less than the starting
     *                 value of this {@link ProgressTracker}, the starting value is used.  If that value is greater than
     *                 the ending value of this {@link ProgressTracker}, the ending value is used.
     */
    public void setProgress(double progress)
    {
        // Update pivot data for the estimated completion time
        if (startTime == -1)
        {
            startTime = System.currentTimeMillis();
        }

        // Set the new value
        double oldValue = currentValue;
        if ((progress >= currentValue) || (permitRollback))
        {
            if (startingValue < endingValue)
            {
                currentValue = MathUtilities.bound(progress, startingValue, endingValue);
            } else
            {
                currentValue = MathUtilities.bound(progress, endingValue, startingValue);
            }
            recalculatePercentage();
        }
        notifyListeners(currentValue - oldValue);
    }

    /**
     * Reports a change in the amount of work completed.  The provided value is interpreted as a percentage of the total
     * range of this {@link ProgressTracker}.
     *
     * @param percentage The percentage to mark as completed, as a value between <code>0.0</code> and
     *                   <code>100.0</code>.
     */
    public void incrementPercentage(double percentage)
    {
        incrementProgress((endingValue - startingValue) * percentage / 100.0);
    }

    /**
     * Sets the progress value of this {@link ProgressTracker} to its ending value.  This method is provided for
     * convenience.
     */
    public void setProgressCompleted()
    {
        setProgress(endingValue);
    }

    /**
     * Retrieves the current progress percentage.
     *
     * @return The current progress percentage, as a value between <code>0.0</code> and <code>100.0</code>, inclusive.
     */
    public double getPercentCompleted()
    {
        if (permitRollback)
        {
            return getCurrentPercentage();
        } else
        {
            return highestPercentage;
        }
    }

    /**
     * Recalculates the highest percentage thus far observed by this {@link ProgressTracker}.
     */
    protected void recalculatePercentage()
    {
        highestPercentage = Math.max(highestPercentage, getCurrentPercentage());
    }

    /**
     * Determines the percentage of completion at this moment.
     */
    protected double getCurrentPercentage()
    {
        if (startingValue == endingValue)
        {
            return 100.0;
        } else
        {
            return Math.abs((currentValue - startingValue) / (startingValue - endingValue) * 100);
        }
    }

    /**
     * Retrieves the starting value for this {@link ProgressTracker}.
     *
     * @return The starting value for this {@link ProgressTracker}.
     */
    public double getStartingValue()
    {
        return startingValue;
    }

    /**
     * Retrieves the ending value for this {@link ProgressTracker}.
     *
     * @return The ending value for this {@link ProgressTracker}.
     */
    public double getEndingValue()
    {
        return endingValue;
    }

    /**
     * Changes the starting value for this {@link ProgressTracker}.
     *
     * @param startingValue The new starting value for this {@link ProgressTracker}.
     */
    public void setStartingValue(double startingValue)
    {
        this.startingValue = startingValue;
        setProgress(currentValue);
    }

    /**
     * Changes the ending value for this {@link ProgressTracker}.
     *
     * @param endingValue The new ending value for this {@link ProgressTracker}.
     */
    public void setEndingValue(double endingValue)
    {
        this.endingValue = endingValue;
        setProgress(currentValue);
    }

    /**
     * Retrieves a sub-tracker for this {@link ProgressTracker}.  The range of that sub-tracker will be from
     * <code>0.0</code> to <code>100.0</code>.
     *
     * @param amount The amount of progress to allocate to the sub-tracker.
     * @return The {@link ProgressTracker} to use as the requested sub-tracker.
     */
    public ProgressTracker getSubtrackerByValue(double amount)
    {
        return getSubtrackerByValue(0.0, 100.0, amount);
    }

    /**
     * Retrieves a sub-tracker for this {@link ProgressTracker}.  The range of that sub-tracker will be from
     * <code>0.0</code> to <code>100.0</code>.
     *
     * @param percentage The percentage of this {@link ProgressTracker}'s range to allocate to the subtracker, expressed
     *                   as a value in the range <code>[0.0, 100.0]</code>.
     * @return The {@link ProgressTracker} to use as the requested sub-tracker.
     */
    public ProgressTracker getSubtrackerByPercentage(double percentage)
    {
        return getSubtrackerByValue(Math.abs((endingValue - startingValue) * percentage / 100.0));
    }

    /**
     * Retrieves a sub-tracker for this {@link ProgressTracker}.  The range of that sub-tracker will be as specified in
     * this method.  It will allocate the specified amount of progress to this subtracker proportionally as it is marked
     * completed.  For example, if this {@link ProgressTracker} had a range of <code>0.0</code> to <code>10.0</code>,
     * the subtracker's range were set at <code>0.0</code> to <code>5.0</code>, and the subtracker were requested to
     * represent <code>2.0</code> progress on this {@link ProgressTracker}, progress on the subtracker would be assigned
     * to this {@link ProgressTracker} at a 40% ratio.
     *
     * @param subtrackerStartingValue The starting value for the subtracker.
     * @param subtrackerEndingValue   The ending value for the subtracker.
     * @param range                     The amount of progress range allocated to the subtracker.
     * @return The {@link ProgressTracker} to use as the requested sub-tracker.
     */
    public ProgressTracker getSubtrackerByValue(double subtrackerStartingValue, double subtrackerEndingValue,
                                                double range)
    {
        return new Subtracker(
                this, subtrackerStartingValue, subtrackerEndingValue,
                Math.abs(range / (subtrackerStartingValue - subtrackerEndingValue)), permitRollback);
    }

    /**
     * Retrieves a sub-tracker for this {@link ProgressTracker}.  The range of that sub-tracker will be as specified in
     * this method.  It will allocate the specified percentage of this {@link ProgressTracker}'s total progress as it is
     * marked completed.  For example, if this {@link ProgressTracker} had a range of <code>0.0</code> to
     * <code>10.0</code>, the subtracker's range were set at <code>0.0</code> to <code>5.0</code>, and the subtracker
     * were requested to represent <code>30.0</code> percent of the progress on this {@link ProgressTracker}, progress
     * on the subtracker would be assigned to this {@link ProgressTracker} at a 60% ratio.
     *
     * @param subtrackerStartingValue The starting value for the subtracker.
     * @param subtrackerEndingValue   The ending value for the subtracker.
     * @param percentage                The percentage of total progress to assign to the subtracker.
     * @return The {@link ProgressTracker} to use as the requested sub-tracker.
     */
    public ProgressTracker getSubtrackerByPercentage(double subtrackerStartingValue, double subtrackerEndingValue,
                                                     double percentage)
    {
        return getSubtrackerByValue(
                subtrackerStartingValue, subtrackerEndingValue,
                Math.abs(endingValue - startingValue) * percentage / 100.0);
    }

    /**
     * Changes the range of values for this {@link ProgressTracker}.  This is effectively the same as calling {@link
     * ProgressTracker#setStartingValue(double)} followed by {@link ProgressTracker#setEndingValue(double)}.
     *
     * @param startingValue The new starting value for this {@link ProgressTracker}.
     * @param endingValue   The new ending value for this {@link ProgressTracker}.
     */
    public void setRange(double startingValue, double endingValue)
    {
        setStartingValue(startingValue);
        setEndingValue(endingValue);
    }

    /**
     * Retrieves the estimated amount of time (in milliseconds) it will take before the operation is complete.  This
     * information is determined from the current time, the first time at which {@link
     * ProgressTracker#setProgress(double)} was called, and the current percentage of completion.  It will, therefore,
     * not be particularly accurate when used with {@link ProgressTracker}s whose progress is not frequently updated.
     *
     * @return The estimated time in milliseconds to completion, or {@link Long#MAX_VALUE} if such a time cannot be
     *         estimated (perhaps because {@link ProgressTracker#setProgress(double)} has not yet been called).
     */
    public long getEstimatedTimeToCompletion()
    {
        if (startTime == -1) return Long.MAX_VALUE;
        if ((getProgress() - getStartingValue()) == 0) return Long.MAX_VALUE;
        double workLeftFactor = (getEndingValue() - getProgress()) / (getProgress() - getStartingValue());
        long timeElapsed = System.currentTimeMillis() - startTime;
        if (timeElapsed == 0) return Long.MAX_VALUE;
        return (long) (Math.ceil(timeElapsed * workLeftFactor));
    }

    /**
     * Retrieves a string which describes the estimated amount of time to completion in terms of seconds.
     *
     * @return A string describing the amount of time remaining to completion.
     */
    public String getEstimatedTimeToCompletionString()
    {
        long timeleft = getEstimatedTimeToCompletion();
        if (timeleft == Long.MAX_VALUE) return "ETA: ?";
        timeleft /= 1000;

        StringBuffer sb = new StringBuffer("s");
        sb.insert(0, timeleft % 60);
        if ((timeleft % 60 < 10) && (timeleft / 60 > 0)) sb.insert(0, '0');
        timeleft /= 60;

        if (timeleft > 0)
        {
            sb.insert(0, "m");
            sb.insert(0, timeleft % 60);
            if ((timeleft % 60 < 10) && (timeleft / 60 > 0)) sb.insert(0, '0');
            timeleft /= 60;

            if (timeleft > 0)
            {
                sb.insert(0, "h");
                sb.insert(0, timeleft % 24);
                if ((timeleft % 24 < 10) && (timeleft / 24 > 0)) sb.insert(0, '0');
                timeleft /= 24;

                if (timeleft > 0)
                {
                    sb.insert(0, "d");
                    sb.insert(0, timeleft);
                }
            }
        }
        sb.insert(0, "ETA: ");
        return sb.toString();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This interface is designed to be implemented when notification of changes to the state of a {@link
     * ProgressTracker} are required.
     *
     * @author Zachary Palmer
     */
    public static interface Listener
    {
        /**
         * Notifies this {@link Listener} that a {@link ProgressTracker} with which it is registered has been changed.
         *
         * @param tracker The {@link ProgressTracker} which has been changed.
         * @param amount  The amount by which that {@link ProgressTracker} has changed.
         */
        public void changeObserved(ProgressTracker tracker, double amount);
    }

    /**
     * This subtracker class is designed to provide additional features to ensure that its total allocated progress
     * value is added to the parent {@link ProgressTracker}.
     *
     * @author Zachary Palmer
     */
    static class Subtracker extends ProgressTracker
    {
        /**
         * The scale at which the parent {@link ProgressTracker} receives this {@link Subtracker}'s reports.
         */
        protected double scale;

        /**
         * General constructor.  Adds the {@link SubtrackerListener} to this {@link Subtracker}.
         *
         * @param parent The parent {@link ProgressTracker} to notify of changes.
         * @param scale  The scale at which the parent {@link ProgressTracker} receives this {@link Subtracker}'s
         *               reports. For example, if <code>scale</code> were <code>0.25</code> and this {@link Subtracker}
         *               received an increase in progress of <code>20</code>, the parent would receive an increase in
         *               progress of <code>5</code>.
         */
        public Subtracker(ProgressTracker parent, double startingValue, double endingValue, double scale,
                          boolean permitRollback)
        {
            super(startingValue, endingValue, permitRollback);
            this.scale = scale;
            this.addListener(new SubtrackerListener(parent));
        }

        /**
         * Changes the ending value for this {@link orioni.jz.util.ProgressTracker}.  This extension reconsiders the
         * ratio of this subtracker when the change is made.
         *
         * @param endingValue The new ending value for this {@link orioni.jz.util.ProgressTracker}.
         */
        public void setEndingValue(double endingValue)
        {
            scale *= (startingValue - this.endingValue) / (startingValue - endingValue);
            super.setEndingValue(endingValue);
        }

        /**
         * Changes the starting value for this {@link orioni.jz.util.ProgressTracker}.  This extension reconsiders the
         * ratio of this subtracker when the change is made.
         *
         * @param startingValue The new starting value for this {@link orioni.jz.util.ProgressTracker}.
         */
        public void setStartingValue(double startingValue)
        {
            scale *= (this.startingValue - endingValue) / (startingValue - endingValue);
            super.setStartingValue(startingValue);
        }

        /**
         * This implementation of the {@link Listener} class is designed to allow a {@link Subtracker} to inform its
         * parent tracker whenever a change is made to this {@link Subtracker}.
         *
         * @author Zachary Palmer
         */
        class SubtrackerListener implements Listener
        {
            /**
             * The parent {@link ProgressTracker}.
             */
            protected ProgressTracker parent;

            /**
             * General constructor.
             *
             * @param parent The parent {@link ProgressTracker} to notify.
             */
            public SubtrackerListener(ProgressTracker parent)
            {
                this.parent = parent;
            }

            /**
             * Notifies this {@link ProgressTracker.Listener} that a {@link ProgressTracker} with which it is registered
             * has been changed.
             *
             * @param tracker The {@link ProgressTracker} which has been changed.
             * @param amount  The amount by which it was changed.
             */
            public void changeObserved(ProgressTracker tracker, double amount)
            {
                parent.incrementProgress(amount * scale);
            }
        }
    }
}

// END OF FILE
