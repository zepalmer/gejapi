package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * This {@link JComponent} implementation accepts a {@link String} and renders it in paragraph format, breaking at the
 * end of each line based upon its own width.  When the text is set, this component will perform a series of tests
 * designed to determine the optimum size of the label; this data is then used for the implementation of {@link
 * JComponent#getPreferredSize()}.  The intention of the default implementation is to shape the label using the same
 * proportions as the current graphics device's width-to-height ratio.
 *
 * @author Zachary Palmer
 */
public class MultiLineTextLabel extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link String} to render.
     */
    protected String string;
    /**
     * The desired width-to-height ratio.
     */
    protected double desiredRatio;
    /**
     * The {@link LineBreakMeasurer} which wraps text for this component.
     */
    protected LineBreakMeasurer lineBreakMeasurer;
    /**
     * The preferred size for this {@link MultiLineTextLabel}.
     */
    protected Dimension preferredSize;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes the desired width-to-height ratio is that of the current graphics device.  Also
     * assumes that the label starts with an empty string as its text.
     */
    public MultiLineTextLabel()
    {
        this("");
    }

    /**
     * Skeleton constructor.  Assumes the desired width-to-height ratio is that of the current graphics device.
     *
     * @param string The {@link String} in question.
     */
    public MultiLineTextLabel(String string)
    {
        this(
                string, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().getBounds().getWidth() / GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration().getBounds().getHeight());
    }

    /**
     * General constructor.
     *
     * @param string The {@link String} in question.
     * @param ratio  The ratio to attempt to achieve when establishing the preferred size of this object.
     */
    public MultiLineTextLabel(String string, double ratio)
    {
        super();
        desiredRatio = ratio;
        preferredSize = null;
        setText(string);
        if (this.getComponentOrientation().isLeftToRight())
        {
            this.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else
        {
            this.setAlignmentX(Component.RIGHT_ALIGNMENT);
        }
        addComponentListener(
                new ComponentAdapter()
                {
                    public void componentHidden(ComponentEvent e)
                    {
                        preferredSize = null;
                    }

                    public void componentShown(ComponentEvent e)
                    {
                        preferredSize = null;
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the text of this label.
     *
     * @param string The new text for this label.
     */
    public void setText(String string)
    {
        this.string = string;
        invalidate();
        preferredSize = null;
    }

    /**
     * Sets the font for this component.
     *
     * @param font the desired <code>Font</code> for this component
     * @see java.awt.Component#getFont
     */
    public void setFont(Font font)
    {
        super.setFont(font);
        invalidate();
        preferredSize = null;
    }

    /**
     * Retrieves the preferred size for this {@link JComponent}.
     *
     * @return the value of the <code>preferredSize</code> property
     * @see #setPreferredSize
     * @see javax.swing.plaf.ComponentUI
     */
    public Dimension getPreferredSize()
    {
        if (preferredSize == null) preferredSize = determinePreferredSize();
        if (preferredSize == null)
        {
            return new Dimension(0, 0);
        } else
        {
            return preferredSize;
        }
    }

    /**
     * Sets the preferred size of this component.
     *
     * @param preferredSize The preferred size for this component.
     */
    public void setPreferredSize(Dimension preferredSize)
    {
        super.setPreferredSize(preferredSize);
        this.preferredSize = preferredSize;
    }

    /**
     * Adjusts the preferred size of this component to approximate the desired ratio.  This method will do nothing if
     * certain pieces of information (such as the font of the label) are not available.
     *
     * @return The preferred size for this {@link MultiLineTextLabel}.
     */
    private Dimension determinePreferredSize()
    {
        FontRenderContext frc = new FontRenderContext(null, false, false);
        lineBreakMeasurer = null;
        // Determine the initial width of this component as a single line.
        if (string.length() < 1) return null;
        lineBreakMeasurer = new LineBreakMeasurer(new AttributedString(string).getIterator(), frc);
        if (getFont() == null) return null;
        int initialWidth = (int) (Math.ceil(new TextLayout(string, getFont(), frc).getBounds().getWidth()));
        int minRange = 1;
        int maxRange = initialWidth;

        int testHeight = 0;
        while (minRange < maxRange)
        {
            int testWidth = minRange + (maxRange - minRange) / 2;
            // Determine the ratio for this test width.
            testHeight = 0;
            lineBreakMeasurer.setPosition(0);
            while (lineBreakMeasurer.getPosition() < string.length())
            {
                TextLayout layout = lineBreakMeasurer.nextLayout(testWidth);
                if (testHeight > 0) testHeight += layout.getLeading();
                testHeight += layout.getAscent();
                testHeight += layout.getDescent();
            }
            double ratio = ((double) testWidth) / testHeight;
            // Determine what to do about this.
            if (ratio > desiredRatio)
            {
                // It was too wide.
                maxRange = testWidth - 1;
            } else if (ratio < desiredRatio)
            {
                // It was too narrow.
                minRange = testWidth + 1;
            } else
            {
                // Use this one; it's perfect!
                minRange = testWidth;
                maxRange = testWidth;
            }
        }
        // We've determined the width and height of the chosen test result.
        Insets insets = getInsets();
        return new Dimension(
                Math.max(minRange, maxRange) + insets.left + insets.right, testHeight + insets.top + insets.bottom);
    }

    /**
     * Paints this component.
     *
     * @param g The {@link Graphics} object onto which this component will be painted.
     */
    public void paint(Graphics g)
    {
        if (this.isOpaque())
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        if (lineBreakMeasurer != null)
        {
            int textSpaceWidth = this.getWidth() - this.getInsets().top - this.getInsets().bottom;
            if (textSpaceWidth > 0)
            {
                lineBreakMeasurer.setPosition(0);
                BufferedImage buffer = new BufferedImage(
                        textSpaceWidth, this.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = buffer.createGraphics();
                g2d.setColor(getForeground());
                int yPos = this.getInsets().top;
                int lastLeading = 0;
                while (lineBreakMeasurer.getPosition() < string.length())
                {
                    TextLayout layout = lineBreakMeasurer.nextLayout(textSpaceWidth);
                    yPos += (int) (layout.getAscent());
                    layout.draw(
                            g2d, this.getInsets().left +
                                 (int) (textSpaceWidth - layout.getBounds().getWidth() - 1) * this.getAlignmentX(),
                            yPos);
                    lastLeading = (int) (layout.getLeading());
                    yPos += (int) (layout.getDescent()) + lastLeading;
                }
                yPos -= lastLeading;
                g.drawImage(buffer, 0, (int) ((this.getHeight() - yPos) * this.getAlignmentY()), null);
            }
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
