package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;

/**
 * This simple component renders a vertical line of the specified height and color.
 * @author Zachary Palmer
 */
public class VerticalLineComponent extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The height of this line. */
	protected int height;
	/** The color of this line. */
	protected Color color;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param height The height of the line.
	 * @param color The {@link Color} of the line.
	 */
	public VerticalLineComponent(int height, Color color)
	{
		super();
		this.height = height;
		this.color = color;
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Retrieves the color of this line.
	 * @return The color of this line.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Retrieves the height of this line.
	 * @return The height of this line.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Paints this component as a vertical line.
     * @param g The {@link Graphics} on which to paint.
	 */
	protected void paintComponent(Graphics g)
	{
		g.setColor(color);
		g.drawLine(0, 0, 0, height - 1);
	}

	/**
	 * Retrieves the <i>only</i> size for this object: 1 by its height.
	 * @return A {@link Dimension} of the specified size.
	 */
	public Dimension getMaximumSize()
	{
		return new Dimension(1, height);
	}

	/**
	 * Retrieves the <i>only</i> size for this object: 1 by its height.
	 * @return A {@link Dimension} of the specified size.
	 */
	public Dimension getMinimumSize()
	{
		return new Dimension(1, height);
	}

	/**
	 * Retrieves the <i>only</i> size for this object: 1 by its height.
	 * @return A {@link Dimension} of the specified size.
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension(1, height);
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}