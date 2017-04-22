package orioni.jz.util;

/**
 * This class is designed to track the average (both moving and running) of a set of numbers.  The AverageTracker
 * provides entry of numbers and modification of the last number entered into the set.
 *
 * @author Zachary Palmer
 */
public class AverageTracker
{
    /**
     * The moving average array for this AverageTracker.
     */
    protected double[] movingArray;
    /**
     * The rewrite point for this AverageTracker's moving average array.
     */
    protected int rewritePoint;
    /**
     * The last point that was rewritted by this AverageTracker.
     */
    protected int lastRewritePoint;
    /**
     * The moving average total for this AverageTracker.
     */
    protected double movingTotal;

    /**
     * The running average total for this AverageTracker.
     */
    protected double runningTotal;
    /**
     * The total count of numbers entered into the AverageTracker.
     */
    protected int numberCount;

    /**
     * Skeleton constructor.  Assumes the length of the moving average array to be twenty.
     */
    public AverageTracker()
    {
        this(20);
    }

    /**
     * Full constructor.
     *
     * @param movingLength The length of the moving average array in which the most recent value are kept.
     */
    public AverageTracker(int movingLength)
    {
        movingArray = new double[movingLength];
        rewritePoint = 0;
        lastRewritePoint = 0;
        movingTotal = 0.0;
        runningTotal = 0.0;
        numberCount = 0;
    }

    /**
     * Enters a new number into the AverageTracker.
     *
     * @param num The new number to register.
     */
    public void addNumber(double num)
    {
        // Add to running average
        runningTotal += num;
        if (numberCount < movingArray.length) numberCount++;
        // Add to moving average
        movingTotal -= movingArray[rewritePoint];
        movingArray[rewritePoint] = num;
        movingTotal += num;
        lastRewritePoint = rewritePoint++;
        if (rewritePoint >= movingArray.length) rewritePoint = 0;
    }

    /**
     * Modifies the last value entered into the AverageTracker.
     *
     * @param adj The adjustment to make to the last number.
     */
    public void adjustNumber(double adj)
    {
        // Adjust running average
        runningTotal += adj;
        // Adjust moving average
        movingTotal += adj;
        movingArray[lastRewritePoint] += adj;
    }

    /**
     * Retrieves the running average of the AverageTracker at present.
     *
     * @return The requested running average.
     */
    public double getRunningAverage()
    {
        if (numberCount > 0)
        {
            return runningTotal / numberCount;
        } else
        {
            return Double.NaN;
        }
    }

    /**
     * Retrieves the running total of this {@link AverageTracker} at present. This can be useful when the average
     * tracker is being used to track units of time.
     *
     * @return The requested running total.
     */
    public double getRunningTotal()
    {
        return runningTotal;
    }

    /**
     * Retrieves the moving average of the AverageTracker at present.
     *
     * @return The requested moving average or {@link Double#NaN} if no data has yet been provided.
     */
    public double getMovingAverage()
    {
        if (numberCount > 0)
        {
            return movingTotal / numberCount;
        } else
        {
            return Double.NaN;
        }
    }

    /**
     * Retrieves the moving array size of this AverageTracker.
     *
     * @return The number of values stored for the moving average of this tracker.
     */
    public int getMovingAverageSize()
    {
        return movingArray.length;
    }
}
