package orioni.jz.awt.swing;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * This {@link JComponent} is designed to display a series of {@link Shape} objects.  It acts as a container for the
 * shapes, requesting enough size from its layout to display them all.  The shapes are displayed in black on a white
 * background.
 *
 * @author Zachary Palmer
 */
public class ShapeDisplayComponent extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The width of the border on each side.
     */
    protected int borderWidth;
    /**
     * The height of the border on each side.
     */
    protected int borderHeight;
    /**
     * The {@link Shape}s to render.
     */
    protected Shape[] shapes;

    /**
     * The preferred size of this component.
     */
    protected Dimension preferredSize;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param transform     The {@link AffineTransform} to use to transform the shapes, or <code>null</code> if no
     *                      transform is desired.
     * @param borderWidth  The width of the border on each side of this component.
     * @param borderHeight The height of the border on each side of this component.
     * @param shapes        The {@link Shape}s to display.
     */
    public ShapeDisplayComponent(AffineTransform transform, int borderWidth, int borderHeight, Shape... shapes)
    {
        super();
        this.borderWidth = borderWidth;
        this.borderHeight = borderHeight;
        this.shapes = shapes;

        if (transform != null)
        {
            for (int i = 0; i < this.shapes.length; i++) this.shapes[i] = transform.createTransformedShape(this.shapes[i]);
        }

        double upperLeftX = 0;
        double upperLeftY = 0;
        double lowerRightX = 0;
        double lowerRightY = 0;

        for (Shape s : this.shapes)
        {
            Rectangle2D rect = s.getBounds2D();
            upperLeftX = Math.min(upperLeftX, rect.getMinX());
            upperLeftY = Math.min(upperLeftY, rect.getMinY());
            lowerRightX = Math.max(lowerRightX, rect.getMaxX());
            lowerRightY = Math.max(lowerRightY, rect.getMaxY());
        }

        preferredSize = new Dimension(
                (int) (Math.abs(lowerRightX - upperLeftX))+1,
                (int) (Math.abs(lowerRightY - upperLeftY))+1);

        // Now we need to move all of the shapes so that the upper left corner is (0,0).
        AffineTransform moveTransform = AffineTransform.getTranslateInstance(-upperLeftX, -upperLeftY);
        for (int i = 0; i < this.shapes.length; i++) this.shapes[i] = moveTransform.createTransformedShape(this.shapes[i]);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Provides the preferred size of this component.
     *
     * @return the value of the <code>preferredSize</code> property
     * @see #setPreferredSize
     * @see ComponentUI
     */
    public Dimension getPreferredSize()
    {
        return preferredSize;
    }

    /**
     * Renders the shapes provided in the constructor.  This method will fail to render the shapes if the provided
     * {@link Graphics} object is not an instance of {@link Graphics2D}; it will render the text <code>Render
     * failed</code> instead.
     *
     * @param g The {@link Graphics} object on which to render the shapes.
     */
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if (g instanceof Graphics2D)
        {
            Graphics2D gg = (Graphics2D) g;
            for (Shape s : shapes)
            {
                gg.draw(s);
            }
        } else
        {
            g.drawString("Render failed.", 0, 0);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE