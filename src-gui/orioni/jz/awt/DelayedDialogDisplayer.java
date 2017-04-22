package orioni.jz.awt;

import orioni.jz.math.MathUtilities;

import java.awt.*;

/**
 * This class is designed to allow a caller to display a dialog on a delayed timer.  Rather than calling the dialog's
 * {@link Dialog#setVisible(boolean) setVisible(boolean)} method, one calls the {@link
 * DelayedDialogDisplayer#setVisible(boolean)} method.  If the parameter to the method is <code>true</code>, a new
 * thread is created which calls that method after a period of time (as established by the constructor).  If the
 * parameter to the method is false, {@link Dialog#setVisible(boolean) Dialog.setVisible(false)} is called and the
 * {@link Thread} created by the instance of this class will be terminated.
 * <p/>
 * This class only maintains one {@link Thread} at a time.  Calling the {@link DelayedDialogDisplayer} before the thread
 * has stopped (either by being terminated or by displaying the dialog) will have no effect.
 * <p/>
 * Optionally, the displayer can disable the dialog's owner.  When the {@link DelayedDialogDisplayer#setVisible(boolean)
 * DelayedDialogDisplayer.setVisible(true)} method is initially called, the owner of the dialog is disabled via {@link
 * Window#setEnabled(boolean) Window.setEnabled(false)}. The {@link Window#setEnabled(boolean) Window.setEnabled(true)}
 * method is called when the dialog display thread terminates.  By default, this behavior is enabled if the dialog in
 * question is modal.
 *
 * @author Zachary Palmer
 */
public class DelayedDialogDisplayer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Dialog} to control.
     */
    protected Dialog dialog;
    /**
     * The amount of time to delay the display of the dialog.
     */
    protected int delayTime;
    /**
     * Whether or not the dialog's owner is disabled when the delayed display thread is running.
     */
    protected boolean disableOwner;

    /**
     * The delayed visibility {@link Thread} for this object.
     */
    protected DisplayerThread thread;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the owner is disabled if the dialog is modal.  Also assumes a 100 millisecond
     * delay time.
     *
     * @param dialog The {@link Dialog} whose display should be controlled.
     */
    public DelayedDialogDisplayer(Dialog dialog)
    {
        this(dialog, 100, dialog.isModal());
    }

    /**
     * Skeleton constructor.  Assumes that the owner is disabled if the dialog is modal.
     *
     * @param dialog     The {@link Dialog} whose display should be controlled.
     * @param delayTime The amount of time to delay the display of the dialog, in milliseconds.
     */
    public DelayedDialogDisplayer(Dialog dialog, int delayTime)
    {
        this(dialog, delayTime, dialog.isModal());
    }

    /**
     * General constructor.
     *
     * @param dialog        The {@link Dialog} whose display should be controlled.
     * @param delayTime    The amount of time to delay the display of the dialog, in milliseconds.
     * @param disableOwner <code>true</code> if the dialog's owner should be disabled while the delayed display thread
     *                      is running
     */
    public DelayedDialogDisplayer(Dialog dialog, int delayTime, boolean disableOwner)
    {
        super();
        this.dialog = dialog;
        this.delayTime = delayTime;
        this.disableOwner = disableOwner;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the delayed visibility state of the dialog.
     *
     * @param visible <code>true</code> if the dialog should be made visible in a delayed fashion; <code>false</code> if
     *                the dialog should be made invisible now.
     */
    public synchronized void setVisible(boolean visible)
    {
        if (visible)
        {
            if (!((thread != null) && (thread.isAlive())))
            {
                thread = new DisplayerThread(dialog, delayTime);
                thread.start();
            }
        } else
        {
            if (thread != null)
            {
                thread.cancel();
                thread = null;
            }
            dialog.setVisible(false);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This {@link Thread} extension is designed to perform the {@link Dialog#setVisible(boolean)} operation after a
     * preset period of time.  This will only occur if a call to {@link DisplayerThread#cancel()} has not been made.
     * After that check is performed, this thread terminates.
     *
     * @author Zachary Palmer
     */
    static class DisplayerThread extends Thread
    {
        /**
         * Determines whether or not the display call should still be made.
         */
        protected boolean display;
        /**
         * The dialog to display.
         */
        protected Dialog dialog;
        /**
         * The time to wait.
         */
        protected int time;

        /**
         * General constructor.
         *
         * @param dialog The dialog to display.
         * @param time   The time, in milliseoncds, to wait.
         */
        public DisplayerThread(Dialog dialog, int time)
        {
            super("Display Timing Thread");
            this.dialog = dialog;
            this.time = time;
            display = true;
        }

        /**
         * Runs this thread.
         */
        public void run()
        {
            long stop = System.currentTimeMillis() + time;
            while (System.currentTimeMillis() < stop)
            {
                try
                {
                    Thread.sleep(MathUtilities.bound(stop - System.currentTimeMillis(), 1, 500));
                } catch (InterruptedException e)
                {
                    // We'll catch the missed time on the next pass.
                }
                synchronized (this)
                {
                    if (!display) stop = 0;
                }
            }
            synchronized (this)
            {
                if (dialog.isModal())
                {
                    new Thread("Displayer Thread")
                    {
                        public void run()
                        {
                            if (display) dialog.setVisible(true);
                        }
                    }.start();
                } else
                {
                    if (display) dialog.setVisible(true);
                }
            }
        }

        /**
         * Cancels the display of the dialog on this thread.
         */
        public synchronized void cancel()
        {
            display = false;
        }
    }
}

// END OF FILE