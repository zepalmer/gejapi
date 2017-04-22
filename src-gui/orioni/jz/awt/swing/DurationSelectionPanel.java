package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link DurationSelectionPanel} allows a user to select a duration in terms of hours, minutes, and seconds from
 * combo boxes.  This duration can be returned to the program using this component in terms of seconds; a string
 * can also be generated to describe the duration.
 * <P>
 * On construction of this panel, the caller is required to provide the minimum and maximum available times selectable
 * in terms of seconds.  These values can be set later on as well.
 * <P>
 * The {@link ActionListener}s on this panel are called whenever the value contained within the panel changes.
 *
 * @author Zachary Palmer
 */
public class DurationSelectionPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link TimeComboBox} containing the number of hours selected. */
    protected TimeComboBox hours;
    /** The {@link TimeComboBox} containing the tens of minutes selected. */
    protected TimeComboBox minutesTens;
    /** The {@link TimeComboBox} containing the ones of minutes selected. */
    protected TimeComboBox minutesOnes;
    /** The {@link TimeComboBox} containing the tens of seconds selected. */
    protected TimeComboBox secondsTens;
    /** The {@link TimeComboBox} containing the ones of seconds selected. */
    protected TimeComboBox secondsOnes;
    /** The {@link java.util.Set} of {@link ActionListener}s to be called when the value contained within the panel
     *  changes. */
    protected Set<ActionListener> actionListeners;

    /** The last value of this panel during a call to {@link DurationSelectionPanel#adjust()}. */
    protected int lastValue;

    /** The minimum time selectable, in seconds. */
    protected int minimumTime;
    /** The maximum time selectable, in seconds. */
    protected int maximumTime;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param minimum The minimum time selectable, in seconds.
     * @param maximum The maximum time selectable, in seconds.
     */
    public DurationSelectionPanel(int minimum, int maximum)
    {
        super(new FlowLayout());
        lastValue = Integer.MIN_VALUE;
        actionListeners = new HashSet<ActionListener>();

        hours = new TimeComboBox(0, 23);
        minutesTens = new TimeComboBox(0, 5);
        minutesOnes = new TimeComboBox(0, 9);
        secondsTens = new TimeComboBox(0, 5);
        secondsOnes = new TimeComboBox(0, 9);

        this.add(hours);
        this.add(new JLabel("h:"));
        this.add(minutesTens);
        this.add(minutesOnes);
        this.add(new JLabel("m:"));
        this.add(secondsTens);
        this.add(secondsOnes);
        this.add(new JLabel("s"));

        minimumTime = minimum;
        maximumTime = maximum;

        adjust();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the time dispalyed by this panel in terms of seconds.  If this time is beyond one of the maximums or
     * minimums of this duration selection panel, that maximum or minimum is adjusted to accomodate.
     * @param value The new value (in seconds) for this selection panel.
     */
    public void setDuration(int value)
    {
        if (minimumTime > value) minimumTime = value;
        if (maximumTime < value) maximumTime = value;
        adjust();
        secondsOnes.setValue(value % 10);
        value /= 10;
        secondsTens.setValue(value % 6);
        value /= 6;
        minutesOnes.setValue(value % 10);
        value /= 10;
        minutesTens.setValue(value % 6);
        value /= 6;
        hours.setValue(value);
        adjust();
    }

    /**
     * Retrieves the time displayed by this panel in terms of seconds.
     * @return The time displayed by this panel in terms of seconds.
     */
    public int getDuration()
    {
        return hours.getValue() * 3600 + minutesTens.getValue() * 600 + minutesOnes.getValue() * 60 +
               secondsTens.getValue() * 10 + secondsOnes.getValue();
    }

    /**
     * Sets the maximum duration for this panel.  If the minimum is greater than this value, it is set to this value
     * as well.
     * @param max The maximum duration for this panel in seconds.
     */
    public void setMaximumDuration(int max)
    {
        maximumTime = max;
        if (minimumTime > max) minimumTime = max;
        adjust();
    }

    /**
     * Sets the minimum duration for this panel.  If the maximum is less than this value, it is set to this value
     * as well.
     * @param min The minimum duration for this panel in seconds.
     */
    public void setMinimumDuration(int min)
    {
        minimumTime = min;
        if (maximumTime < min) maximumTime = min;
        adjust();
    }

    /**
     * Retrieves a string describing the duration currently contained within this {@link DurationSelectionPanel}.
     * @return A string describing the duration currently contained within this {@link DurationSelectionPanel}.
     */
    public String getDurationString()
    {
        return DurationSelectionPanel.getDurationString(getDuration());
    }

    /**
     * This method adjusts the selectable values of the {@link TimeComboBox} objects based upon their current values
     * and the minimums and maximums of this {@link DurationSelectionPanel}.
     */
    protected void adjust()
    {
        int minimumHours = minimumTime;
        int minimumSeconds = minimumHours % 60;
        minimumHours /= 60;
        int minimumMinutes = minimumHours % 60;
        minimumHours /= 60;
        int maximumHours = maximumTime;
        int maximumSeconds = maximumHours % 60;
        maximumHours /= 60;
        int maximumMinutes = maximumHours % 60;
        maximumHours /= 60;

        hours.setValues(minimumHours, maximumHours);
        if (hours.getValue() == minimumHours)
        {
            if (minimumHours == maximumHours)
            {
                minutesTens.setValues(minimumMinutes / 10, maximumMinutes / 10);
                if (minimumMinutes / 10 == maximumMinutes / 10)
                {
                    minutesOnes.setValues(minimumMinutes % 10, maximumMinutes % 10);
                    if (minimumMinutes % 10 == maximumMinutes % 10)
                    {
                        secondsTens.setValues(minimumSeconds / 10, maximumSeconds / 10);
                        if (minimumSeconds / 10 == maximumSeconds / 10)
                        {
                            secondsOnes.setValues(minimumSeconds % 10, maximumSeconds % 10);
                        } else
                        {
                            secondsOnes.setValues(minimumSeconds % 10, 9);
                        }
                    } else
                    {
                        secondsTens.setValues(minimumSeconds / 10, 5);
                        secondsOnes.setValues(minimumSeconds % 10, 9);
                    }
                } else
                {
                    minutesOnes.setValues(minimumMinutes % 10, 9);
                    secondsTens.setValues(minimumSeconds / 10, 5);
                    secondsOnes.setValues(minimumSeconds % 10, 9);
                }
            } else
            {
                minutesTens.setValues(minimumMinutes / 10, 5);
                minutesOnes.setValues(minimumMinutes % 10, 9);
                secondsTens.setValues(minimumSeconds / 10, 5);
                secondsOnes.setValues(minimumSeconds % 10, 9);
            }
        } else if (hours.getValue() == maximumHours)
        {
            minutesTens.setValues(0, maximumMinutes / 10);
            if (minutesTens.getValue() == maximumMinutes / 10)
            {
                minutesOnes.setValues(0, maximumMinutes % 10);
                if (minutesOnes.getValue() == maximumMinutes % 10)
                {
                    secondsTens.setValues(0, maximumSeconds / 10);
                    if (secondsTens.getValue() == maximumSeconds / 10)
                    {
                        secondsOnes.setValues(0, maximumSeconds % 10);
                    } else
                    {
                        secondsOnes.setValues(0, 9);
                    }
                } else
                {
                    secondsTens.setValues(0, 5);
                    secondsOnes.setValues(0, 9);
                }
            } else
            {
                minutesOnes.setValues(0, 9);
                secondsTens.setValues(0, 5);
                secondsOnes.setValues(0, 9);
            }
        } else
        {
            minutesTens.setValues(0, 5);
            minutesOnes.setValues(0, 9);
            secondsTens.setValues(0, 5);
            secondsOnes.setValues(0, 9);
        }
        if (lastValue != this.getDuration())
        {
            long oldLastValue = lastValue;
            lastValue = this.getDuration();
            if (oldLastValue != Integer.MIN_VALUE)
            {
                for (Object actionListener : actionListeners)
                {
                    ActionListener listener = (ActionListener) (actionListener);
                    listener.actionPerformed(new ActionEvent(this, -1, "Update value to " + this.getDuration()));
                }
            }
        }
    }

    /**
     * Adds an action listener to this panel.  This action listener will be called whenever the value contained within
     * this panel changes.
     * @param listener The {@link ActionListener} to add.
     */
    public void addActionListener(ActionListener listener)
    {
        actionListeners.add(listener);
    }

    /**
     * Removes an action listener from this panel.  This action listener will no longer be called by this panel.
     * @param listener The {@link ActionListener} to remove.
     */
    public void removeActionListener(ActionListener listener)
    {
        actionListeners.remove(listener);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves a string representing the specified duration.  That duration should be provided in terms of seconds.
     * The string is of the format <code>...HH</code>h:<code>MM</code>m:<code>SS</code>s, where the capital letters
     * represent digits of the number of hours, minutes, and seconds repsectively.  The ellipse at the beginning of
     * that string indicates that, while at least two hour digits will be shown, as many digits as are necessary will
     * be used.
     * @param duration The duration, measured in seconds.
     * @return The string describing the duration.
     */
    public static String getDurationString(int duration)
    {
        StringBuffer sb = new StringBuffer();
        int hours = duration / 3600;
        int minutes = (duration / 60) % 60;
        int seconds = duration % 60;
        if (hours < 10) sb.append('0');
        sb.append(hours);
        sb.append("h:");
        if (minutes < 10) sb.append('0');
        sb.append(minutes);
        sb.append("m:");
        if (seconds < 10) sb.append('0');
        sb.append(seconds);
        sb.append("s");
        return sb.toString();
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class represents a {@link JComboBox} which is to contain a series of numbers between <code>0</code> and
     * some <code>n</code>.  It should also call the {@link DurationSelectionPanel#adjust()} method whenever its
     * value is changed.
     * @author Zachary Palmer
     */
    class TimeComboBox extends JComboBox
    {
        /** The current maximum value for this {@link TimeComboBox}. */
        protected int max;
        /** The current minimum value for this {@link TimeComboBox}. */
        protected int min;
        /** The {@link ActionListener} which automatically updates this and every other {@link TimeComboBox} in the
         *  event that a value is changed. */
        protected ActionListener listener;

        /**
         * General constructor.
         * @param min The initial minimum of the items to provide as a possible selection.  This value must be
         *            non-negative.
         * @param max The initial maximum of the items to provide as a possible selection.  This value must be
         *            non-negative.
         */
        public TimeComboBox(int min, int max)
        {
            super(new DefaultComboBoxModel());
            this.min = 0;
            this.max = 0;
            setValues(min, max);
            final TimeComboBox scopedThis = this;
            listener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    synchronized (scopedThis)
                    {
                        adjust();
                    }
                }
            };
            this.addActionListener(listener);
            super.getModel().setSelectedItem(super.getModel().getElementAt(0));
        }

        /**
         * Sets the number of items to provide as a possible selection.
         * @param min The minimum of the items to provide as a possible selection.  This value must be non-negative.
         * @param max The maximum of the items to provide as a possible selection.  This value must be non-negative.
         */
        public void setValues(int min, int max)
        {
            synchronized (this)
            {
                this.removeActionListener(listener);
                DefaultComboBoxModel model = (DefaultComboBoxModel) (super.getModel());
                Integer selectedObject = (Integer) (model.getSelectedItem());
                int selectedValue = (selectedObject == null) ? min : selectedObject;
                model.removeAllElements();
                for (int i = min; i <= max; i++)
                {
                    model.addElement(i);
                }
                if (selectedValue < min)
                {
                    super.getModel().setSelectedItem(min);
                } else if (selectedValue > max)
                {
                    super.getModel().setSelectedItem(max);
                } else
                {
                    super.getModel().setSelectedItem(selectedValue);
                }
                this.max = max;
                this.min = min;
                this.addActionListener(listener);
            }
        }

        /**
         * Retrieves the selected value for this combo box.
         * @return The selected value for this combo box.
         */
        public int getValue()
        {
            return ((Integer) (super.getModel().getSelectedItem()));
        }

        /**
         * Sets the selected value for this combo box.
         * @param value The value for this combo box.
         */
        void setValue(int value)
        {
            super.getModel().setSelectedItem(value);
        }
    }

}