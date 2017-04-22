package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;

/**
 * This {@link JPanel} extension allows the user to set it as "painted" or "unpainted."  If this panel is "painted", it
 * behaves normally, painting its contents when the {@link OptionallyPaintedPanel#paint(Graphics)} method is called.  If
 * this panel is "unpainted", the {@link OptionallyPaintedPanel#paint(Graphics)} method does nothing.  Note that this is
 * distinct from the panel being invisible in that this panel still retains its original size, but simply does not
 * display its contents.  By default, this panel is painted.
 * <p/>
 * Note that the panel being unpainted has nothing to do with whether or not it can respond to interface events.  The
 * contents of this panel usually should be disabled when this panel is unpainted.
 *
 * @author Zachary Palmer
 */
public class OptionallyPaintedPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Whether or not this panel will be painted.
     */
    protected boolean painted;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see JPanel#JPanel()
     */
    public OptionallyPaintedPanel()
    {
        super();
        painted = true;
    }

    /**
     * Wrapper constructor.
     *
     * @see JPanel#JPanel(LayoutManager)
     */
    public OptionallyPaintedPanel(LayoutManager layout)
    {
        super(layout);
        painted = true;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not this panel will be painted.
     *
     * @return <code>true</code> if this panel will be painted; <code>false</code> if it will not.
     */
    public boolean isPainted()
    {
        return painted;
    }

    /**
     * Changes the painted status of this panel.
     *
     * @param painted <code>true</code> if this panel should display normally; <code>false</code> if it should retain
     *                its size but not draw its contents.
     */
    public void setPainted(boolean painted)
    {
        this.painted = painted;
        repaint(100);
    }

    /**
     * Calls {@link JPanel#paint(Graphics)} if and only if this panel is "painted."  See {@link
     * OptionallyPaintedPanel#setPainted(boolean)} as well as this class's description.
     *
     * @param g The <code>Graphics</code> context in which to paint.
     */
    public void paint(Graphics g)
    {
        if (painted)
        {
            super.paint(g);
        } else
        {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE