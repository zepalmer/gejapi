package orioni.jz.awt.swing.convenience;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This class is provided for convenience; it allows a caller to call certain methods on the {@link SelfReturningJLabel}
 * and receive the {@link JLabel} itself as a result.  This can lead to code segments which look like this:
 * <ul><code>SelfReturningJLabel label = ...;<br> label.setText("Yaay!  I don't know what you just said!")
 * <ul>.setFont(new Font("Serif",Font.PLAIN,12)) <br>.setForeground(Color.BLUE); </ul></ul> This has potential to make
 * code either much cleaner or much uglier.  Abused, it could get confusing, but its intended use is in constructor
 * calls, thus preventing an additional local field from being created and centralizing all usage of the label in one
 * place.
 *
 * @author Zachary Palmer
 */
public class SelfReturningJLabel extends JLabel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see JLabel#JLabel()
     */
    public SelfReturningJLabel()
    {
        super();
    }

    /**
     * Wrapper constructor.
     *
     * @see JLabel#JLabel(Icon)
     */
    public SelfReturningJLabel(Icon image)
    {
        super(image);
    }

    /**
     * Wrapper constructor.
     *
     * @see JLabel#JLabel(Icon,int)
     */
    public SelfReturningJLabel(Icon image, int horizontalAlignment)
    {
        super(image, horizontalAlignment);
    }

    /**
     * Wrapper constructor.
     *
     * @see JLabel#JLabel(String)
     */
    public SelfReturningJLabel(String text)
    {
        super(text);
    }

    /**
     * Wrapper constructor.
     *
     * @see JLabel#JLabel(String,int)
     */
    public SelfReturningJLabel(String text, int horizontalAlignment)
    {
        super(text, horizontalAlignment);
    }

    /**
     * Wrapper constructor.
     *
     * @see JLabel#JLabel(String,Icon,int)
     */
    public SelfReturningJLabel(String text, Icon icon, int horizontalAlignment)
    {
        super(text, icon, horizontalAlignment);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setText(String)
     */
    public SelfReturningJLabel setTextAndReturn(String text)
    {
        setText(text);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setHorizontalTextPosition(int)
     */
    public SelfReturningJLabel setHorizontalTextPositionAndReturn(int textPosition)
    {
        super.setHorizontalTextPosition(textPosition);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setHorizontalAlignment(int)
     */
    public SelfReturningJLabel setHorizontalAlignmentAndReturn(int alignment)
    {
        super.setHorizontalAlignment(alignment);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setVerticalTextPosition(int)
     */
    public SelfReturningJLabel setVerticalTextPositionAndReturn(int textPosition)
    {
        super.setVerticalTextPosition(textPosition);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setVerticalAlignment(int)
     */
    public SelfReturningJLabel setVerticalAlignmentAndReturn(int alignment)
    {
        super.setVerticalAlignment(alignment);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setIcon(Icon)
     */
    public SelfReturningJLabel setIconAndReturn(Icon icon)
    {
        super.setIcon(icon);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setFont(Font)
     */
    public SelfReturningJLabel setFontAndReturn(Font font)
    {
        super.setFont(font);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setForeground(Color)
     */
    public SelfReturningJLabel setForegroundAndReturn(Color fg)
    {
        super.setForeground(fg);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setBackground(Color)
     */
    public SelfReturningJLabel setBackgroundAndReturn(Color bg)
    {
        super.setBackground(bg);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setBorder(Border)
     */
    public SelfReturningJLabel setBorderAndReturn(Border border)
    {
        super.setBorder(border);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setAlignmentX(float)
     */
    public SelfReturningJLabel setAlignmentXAndReturn(float alignmentX)
    {
        super.setAlignmentX(alignmentX);
        return this;
    }

    /**
     * This is a wrapper method designed to perform a function and return this {@link SelfReturningJLabel}.
     *
     * @see JLabel#setAlignmentY(float)
     */
    public SelfReturningJLabel setAlignmentYAndReturn(float alignmentY)
    {
        super.setAlignmentY(alignmentY);
        return this;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE