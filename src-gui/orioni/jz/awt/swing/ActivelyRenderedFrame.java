package orioni.jz.awt.swing;

import orioni.jz.util.Utilities;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link JFrame} extension is designed to actively render itself rather than waiting for the AWT/Swing repainting
 * system to get around to redrawing it.  This consumes considerably more processing power but allows the frame to be
 * re-rendered on a reliable basis, allowing games and other video-intensive applications to display properly.
 *
 * @author Zachary Palmer
 */
public class ActivelyRenderedFrame extends JFrame
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The minimum amount of time between frame rendering operations.
     */
    protected int frameDelay;
    /**
     * The rendering thread for this {@link ActivelyRenderedFrame}.
     */
    protected Thread renderingThread;
    /**
     * The <code>boolean</code> controlling whether or not rendering should continue.  This value is used to stop the
     * rendering thread once the {@link ActivelyRenderedFrame} has been discarded via the {@link JFrame#dispose()}
     * method.
     */
    protected boolean continueRendering;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param title   The title of this {@link ActivelyRenderedFrame}.
     * @param maxFps The maximum desired framerate of this {@link ActivelyRenderedFrame}.
     */
    public ActivelyRenderedFrame(String title, int maxFps)
    {
        super(title);

        final ActivelyRenderedFrame scopedThis = this;

        setIgnoreRepaint(true);

        frameDelay = 1000 / maxFps;
        continueRendering = true;
        renderingThread = new Thread("ActivelyRenderedFrame Rendering Thread for "+this.toString())
        {
            public void run()
            {
                boolean continueRendering;
                synchronized (scopedThis)
                {
                    continueRendering = scopedThis.continueRendering;
                }
                while (continueRendering)
                {
                    long start = System.currentTimeMillis();

                    Graphics g = scopedThis.getGraphics();
                    if (g != null)
                    {
                        scopedThis.paint(g);
                        g.dispose();
                    }

                    long waitTime = frameDelay - (System.currentTimeMillis() - start);
                    if (waitTime > 0)
                    {
                        try
                        {
                            Thread.sleep(waitTime);
                        } catch (InterruptedException e)
                        {
                        }
                    }

                    synchronized (scopedThis)
                    {
                        continueRendering = scopedThis.continueRendering;
                    }
                }
            }
        };
        renderingThread.start();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Disposes of this {@link ActivelyRenderedFrame}.  Subsequent calls to {@link JFrame#pack()} will restore this
     * frame's resources, but the frame will no longer be actively rendered.
     *
     * @see JFrame#dispose()
     */
    public void dispose()
    {
        setIgnoreRepaint(false);
        continueRendering = false;
        renderingThread.interrupt();
        Utilities.safeJoin(renderingThread);
        super.dispose();
    }

    /**
     * Instructs this {@link ActivelyRenderedFrame} whether or not it should occupy the full screen exclusive context on
     * its current graphics device.  This method is provided as a convenience step since many actively rendered
     * applications will make use of the full screen exclusive mode.
     *
     * @param fullscreen <code>true</code> if this frame should be full screen on its current graphics device;
     *                   <code>false</code> if it should be a standard window.
     */
    public void setFullScreen(boolean fullscreen)
    {
        if (fullscreen)
        {
            getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
        } else
        {
            if (isFullScreen())
            {
                getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
            }
        }
    }

    /**
     * Determines whether or not this {@link ActivelyRenderedFrame} is the full screen window on its current device.
     * Note that this does not indicate whether or not it is the full screen window on any other device.
     * @return <code>true</code> if the {@link ActivelyRenderedFrame} is the full screen window on its graphics device;
     *         <code>false</code> if it is not.
     */
    public boolean isFullScreen()
    {
        return this.equals(getGraphicsConfiguration().getDevice().getFullScreenWindow());
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
