package orioni.jz.awt.swing;

import orioni.jz.awt.SpongyLayout;
import orioni.jz.reflect.ReflectionUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * This {@link JPanel} extension uses a {@link SpongyLayout} without stretching to present a set of three buttons: OK,
 * Cancel, and Apply (in that order).  These buttons activate the {@link ApprovalButtonPanel#apply()} and {@link
 * ApprovalButtonPanel#close()} methods when these buttons are pressed.  The Apply button calls the first method, the
 * Cancel button calls the second, and the OK button calls both.  Overriding these methods will allow one to use those
 * buttons freely.
 *
 * @author Zachary Palmer
 */
public abstract class ApprovalButtonPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An enumeration of the orientations which this panel can take.
     */
    public static enum Orientation
    {
        /**
         * Buttons are arranged vertically.
         */
        VERTICAL,
        /**
         * Buttons are arranged horizontally.
         */
        HORIZONTAL
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The OK button for this panel.
     */
    protected JButton okButton;
    /**
     * The cancel button for this panel.
     */
    protected JButton cancelButton;
    /**
     * The apply button for this panel.
     */
    protected JButton applyButton;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes button mnemonics will be activated, that the orientation is {@link
     * Orientation#HORIZONTAL}, and that an Apply button does appear.
     */
    public ApprovalButtonPanel()
    {
        this(true, Orientation.HORIZONTAL, true);
    }

    /**
     * Skeleton constructor.  Assumes that the orientation is {@link Orientation#HORIZONTAL} and that an Apply button
     * does appear.
     *
     * @param mnemonics <code>true</code> if the default button mnemonics will be activated; <code>false</code>
     *                  otherwise.
     */
    public ApprovalButtonPanel(boolean mnemonics)
    {
        this(mnemonics, Orientation.HORIZONTAL, true);
    }

    /**
     * Skeleton constructor.  Assumes that the orientation is {@link Orientation#HORIZONTAL}.
     *
     * @param mnemonics   <code>true</code> if the default button mnemonics will be activated; <code>false</code>
     *                    otherwise.
     * @param applyButton <code>true</code> if an Apply button appears; <code>false</code> if it does not.
     */
    public ApprovalButtonPanel(boolean mnemonics, boolean applyButton)
    {
        this(mnemonics, Orientation.HORIZONTAL, applyButton);
    }

    /**
     * Skeleton constructor.  Uses default button text.
     *
     * @param mnemonics   <code>true</code> if the default button mnemonics will be activated; <code>false</code>
     *                    otherwise.
     * @param orientation A {@link Orientation} specifying the orientation of the buttons.
     * @param applyButton <code>true</code> if an Apply button appears; <code>false</code> if it does not.
     */
    public ApprovalButtonPanel(boolean mnemonics, Orientation orientation, boolean applyButton)
    {
        this(mnemonics, orientation, applyButton, "OK", "Cancel", "Apply");
    }

    /**
     * General constructor.
     *
     * @param mnemonics        <code>true</code> if the default button mnemonics will be activated; <code>false</code>
     *                         otherwise.
     * @param orientation      A {@link Orientation} specifying the orientation of the buttons.
     * @param applyButton      <code>true</code> if an Apply button appears; <code>false</code> if it does not.
     * @param okButtonText     The text to appear on the "OK" button.
     * @param cancelButtonText The text to appear on the "Cancel" button.
     * @param applyButtonText  The text to appear on the "Apply" button.  If the apply button does not appear, this
     *                         value is ignored.
     */
    public ApprovalButtonPanel(boolean mnemonics, Orientation orientation, boolean applyButton,
                               String okButtonText, String cancelButtonText, String applyButtonText)
    {
        switch (orientation)
        {
            case VERTICAL:
                setLayout(new GridLayout(0, 1, 2, 2));
                break;
            case HORIZONTAL:
                setLayout(new GridLayout(1, 0, 2, 2));
                break;
        }
        okButton = new JButton(okButtonText);
        cancelButton = new JButton(cancelButtonText);
        if (applyButton)
        {
            this.applyButton = new JButton(applyButtonText);
        } else
        {
            this.applyButton = null;
        }

        add(okButton);
        add(cancelButton);
        if (this.applyButton != null) add(this.applyButton);

        okButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        cancelButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        if (this.applyButton != null) this.applyButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setAlignmentY(Component.CENTER_ALIGNMENT);

        okButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (apply()) close();
                    }
                });
        cancelButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        close();
                    }
                });
        if (this.applyButton != null)
        {
            this.applyButton.addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            apply();
                        }
                    });
        }

        setMnemonics(mnemonics);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the button with the specified text.
     *
     * @param text The text on the button to retrieve.
     * @return The first button which has that text, or <code>null</code> if no such button exists.
     */
    public JButton getButtonByText(String text)
    {
        Component[] components = getComponents();
        for (Component component : components)
        {
            if (ReflectionUtilities.instanceOf(component, JButton.class))
            {
                JButton button = (JButton) (component);
                if (button.getText().equals(text)) return button;
            }
        }
        return null;
    }

    /**
     * Called whenever the OK or Cancel button is pressed.
     */
    public abstract void close();

    /**
     * Called whenever the OK or Apply button is pressed.
     *
     * @return <code>true</code> if the apply was successful; <code>false</code> if not.  If <code>false</code> is
     *         returned after the OK button has been pressed, the {@link ApprovalButtonPanel#close()} method will not be
     *         called.  The default implementation returns <code>true</code>.
     */
    public abstract boolean apply();

    /**
     * Determines whether or not mnemonics should be established for this button panel.
     *
     * @param mnemonics <code>true</code> to provide all buttons with mnemonics; <code>false</code> to remove them.
     */
    public void setMnemonics(boolean mnemonics)
    {
        Component[] components = getComponents();
        if (mnemonics)
        {
            int size = 0;
            for (final Component component : components)
            {
                if (ReflectionUtilities.instanceOf(component, AbstractButton.class)) size++;
            }
            AbstractButton[] buttons = new AbstractButton[size];
            int index = 0;
            for (final Component component : components)
            {
                if (ReflectionUtilities.instanceOf(component, AbstractButton.class))
                {
                    buttons[index++] = (AbstractButton) (component);
                }
            }
            orioni.jz.awt.swing.JZSwingUtilities.setBestMnemonicsFor(buttons);
        } else
        {
            for (final Component component : components)
            {
                if (ReflectionUtilities.instanceOf(component, AbstractButton.class))
                {
                    ((AbstractButton) (component)).setMnemonic(KeyEvent.VK_UNDEFINED);
                }
            }
        }
    }

    /**
     * Retrieves the apply button for this panel.
     *
     * @return The apply button for this panel.
     */
    public JButton getApplyButton()
    {
        return applyButton;
    }

    /**
     * Retrieves the cancel button for this panel.
     *
     * @return The cancel button for this panel.
     */
    public JButton getCancelButton()
    {
        return cancelButton;
    }

    /**
     * Retrieves the OK button for this panel.
     *
     * @return The OK button for this panel.
     */
    public JButton getOkButton()
    {
        return okButton;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}