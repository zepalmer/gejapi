package orioni.jz.awt.swing;

import orioni.jz.awt.SingleComponentPositioningLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This extension of {@link JPanel} contains a {@link JButton} which has as its icon a single, colored rectangle.  The
 * size of the colored square is determined upon the construction of this {@link ColorSelectorButton}.  When pressed,
 * this button opens a {@link JColorChooser} which allows the user to select a new color for the button.  If the dialog
 * completes execution without cancelling, the {@link ColorSelectorButton}'s color is changed to that of a new color and
 * a {@link ChangeEvent} is fired to all of the {@link ColorSelectorButton}'s color change listeners.
 *
 * @author Zachary Palmer
 */
public class ColorSelectorButton extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The currently displayed color of this selector button.
     */
    protected Color color;
    /**
     * The size of the colored rectangle for this selector button.
     */
    protected Dimension rectangleSize;
    /**
     * The {@link JButton} that this panel contains.
     */
    protected JButton button;
    /**
     * The {@link Set} of the {@link ChangeListener} objects which will be notified when this selector's color is
     * changed.
     */
    protected Set<ChangeListener> listenerSet;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param color The starting color for this selector button.
     * @param size  The size, as a {@link Dimension} object, of the colored rectangle.
     */
    public ColorSelectorButton(Color color, Dimension size)
    {
        super();
        this.color = color;
        rectangleSize = size;
        button = new JButton();
        listenerSet = new HashSet<ChangeListener>();

        this.setLayout(new SingleComponentPositioningLayout());
        this.add(button);
        resetColoredRectangle();

        // Displays the color chooser to select a new color for this selector button
        final ColorSelectorButton scopedThis = this;
        button.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        Color selection = JColorChooser.showDialog(scopedThis, "Please Select Color", scopedThis.color);
                        if (selection != null)
                        {
                            scopedThis.color = selection;
                            resetColoredRectangle();
                            fireColorChangeEvent();
                        }
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method resets the colored rectangle on the displayed button to represent the currently selected color.
     */
    public void resetColoredRectangle()
    {
        BufferedImage rectangle = new BufferedImage(
                (int) (rectangleSize.getWidth()),
                (int) (rectangleSize.getHeight()),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = rectangle.getGraphics();
        // Draw black border around colored rectangle
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, rectangle.getHeight() - 1);
        g.drawLine(0, rectangle.getHeight() - 1, rectangle.getWidth() - 1, rectangle.getHeight() - 1);
        g.drawLine(0, 0, rectangle.getWidth() - 1, 0);
        g.drawLine(rectangle.getWidth() - 1, 0, rectangle.getWidth() - 1, rectangle.getHeight() - 1);

        if (this.isEnabled())
        {
            // Draw colored rectangle
            g.setColor(color);
            g.fillRect(1, 1, rectangle.getWidth() - 2, rectangle.getHeight() - 2);
        } else
        {
            // Draw black X in rectangle
            g.drawLine(0,0,rectangle.getWidth()-1,rectangle.getHeight()-1);
            g.drawLine(0,rectangle.getHeight()-1,rectangle.getWidth()-1,0);
        }
        // Set the button's icon
        button.setIcon(new ImageIcon(rectangle));
        button.repaint();
    }

    /**
     * Retrieves the currently-selected color.
     *
     * @return The currently-selected {@link Color} object.
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Sets the currently-selected color.  This invalidates this object.
     *
     * @param color The new color.
     */
    public void setColor(Color color)
    {
        this.color = color;
        button.repaint();
        fireColorChangeEvent();
    }

    /**
     * Sets whether or not this component is enabled.  This extension is used to change the button's icon as well.
     *
     * @param enabled <code>true</code> if this component should be enabled, <code>false</code> otherwise.
     * @see JComponent#setEnabled(boolean)
     * @see java.awt.Component#isEnabled
     * @see java.awt.Component#isLightweight
     */
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        resetColoredRectangle();
    }

    /**
     * Adds an Change listener to this selector.  The listener will be notified when the color of this selector is
     * changed.
     *
     * @param listener The listener to add to the listener set.
     */
    public synchronized void addColorChangeListener(ChangeListener listener)
    {
        listenerSet.add(listener);
    }

    /**
     * Removes an Change listener from this selector, preventing it from being notified when the color of this selector
     * is changed.
     *
     * @param listener The listener to remove from the listener set.
     */
    public synchronized void removeColorChangeListener(ChangeListener listener)
    {
        listenerSet.remove(listener);
    }

    /**
     * Fires an Change event notifying all listeners that this selector's color has changed.
     */
    protected synchronized void fireColorChangeEvent()
    {
        Iterator it = listenerSet.iterator();
        ChangeEvent event = new ChangeEvent(this);
        while (it.hasNext())
        {
            ChangeListener listener = (ChangeListener) (it.next());
            listener.stateChanged(event);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}