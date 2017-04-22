package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;

/**
 * This component is solely designed to provide blank space in the design of a Container.
 * @author Zachary Palmer
 */
public class SpacingComponent extends JComponent
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The Dimension of the spacing component. */
	protected Dimension dimension;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
     * @param d The dimension of the spacing component.
	 */
	public SpacingComponent(Dimension d)
	{
		super();
		dimension = d;
	}

	/**
	 * Skeleton constructor.
     * @param width The width of the component.
     * @param height The height of the component.
	 */
	public SpacingComponent(int width, int height)
	{
		this(new Dimension(width, height));
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Fetches the spacing component dimension.
	 * @return The spacing component dimension.
	 */
	public Dimension getMinimumSize()
	{
		return dimension;
	}

	/**
	 * Fetches the spacing component dimension.
	 * @return The spacing component dimension.
	 */
	public Dimension getMaximumSize()
	{
		return dimension;
	}

	/**
	 * Fetches the spacing component dimension.
	 * @return The spacing component dimension.
	 */
	public Dimension getPreferredSize()
	{
		return dimension;
	}

	/**
	 * Paints this {@link SpacingComponent} as a rectangle colored by the result of {@link Component#getBackground()}.
	 * @param g The {@link Graphics} object on which to paint this component.
	 */
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(getBackground());
		g.fillRect(0,0, getWidth(), getHeight());
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //