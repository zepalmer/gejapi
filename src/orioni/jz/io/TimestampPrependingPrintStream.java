package orioni.jz.io;

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * This extension of {@link PrependablePrintStream} is designed to use a timestamp as the prepend.  This is a
 * significant change from the normal {@link PrependablePrintStream} as the prepend changes each time it is displayed.
 * The prepend count does not affect the behavior of this stream; there is always one timestamp per line.
 *
 * @author Zachary Palmer
 */
public class TimestampPrependingPrintStream extends PrependablePrintStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes no auto-flushing.
     * @param os The {@link OutputStream} to which this stream will write.
     */
    public TimestampPrependingPrintStream(OutputStream os)
    {
        this(os, false);
    }

    /**
     * General constructor.
     * @param os The {@link OutputStream} to which this stream will write.
     * @param autoFlush Whether or not this stream should automatically flush changed.
     */
    public TimestampPrependingPrintStream(OutputStream os, boolean autoFlush)
    {
        super(os, autoFlush, "", 0);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the string to use as a prepend.
     */
    protected synchronized String getPrepend()
    {
        return "[" + DateFormat.getTimeInstance().format(new Date()) + "]: ";
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE