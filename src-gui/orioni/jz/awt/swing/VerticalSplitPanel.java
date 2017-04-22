package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * This panel is designed to contain three {@link JComponent}s.  Two of the {@link JComponent}s are to be presented
 * to the user, stacked vertically.  The third is provided to separate the first two.
 * <P>
 * The size of the two {@link JComponent}s provided will be determined by the size of this panel as well as the size of
 * the separator component.  The separator component can be dragged by the user to change the size of the two
 * {@link JComponent}s being presented; therefore, the separation component is usually fairly short.
 *
 * @author Zachary Palmer
 */
public class VerticalSplitPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The top {@link JComponent}. */
	protected JComponent topComponent;
	/** The bottom {@link JComponent}. */
	protected JComponent bottomComponent;
	/** The {@link JComponent} being used a separator. */
	protected JComponent separatorComponent;

	/** The {@link JComponent}<code>[]</code> that is used to store the components found within this
	 *  {@link VerticalSplitPanel}. */
	protected JComponent[] contentsArray;

	/** The {@link MouseMotionAdapter} which is attached to the separator component to track mouse drags. */
	protected MouseMotionAdapter separatorMotionAdapter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param topComponent The top {@link JComponent}.
	 * @param bottomComponent The bottom {@link JComponent}.
	 * @param separatorComponent The top {@link JComponent}.
	 */
	public VerticalSplitPanel(JComponent topComponent, JComponent bottomComponent, JComponent separatorComponent)
	{
		super();
		this.topComponent = topComponent;
		this.bottomComponent = bottomComponent;
		this.separatorComponent = separatorComponent;
		contentsArray = new JComponent[]{this.topComponent, this.bottomComponent, this.separatorComponent};
		this.setLayout(new PanelLayoutManager());
		this.add(this.topComponent);
		this.add(this.bottomComponent);
		this.add(this.separatorComponent);
		separatorMotionAdapter = new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				int y = e.getY() + e.getComponent().getY();
				y = Math.min(y, e.getComponent().getParent().getHeight() - e.getComponent().getHeight());
				y = Math.max(0, y);
				e.getComponent().setLocation((int) (e.getComponent().getLocation().getX()), y);
				doLayout();
			}
		};
		this.separatorComponent.addMouseMotionListener(separatorMotionAdapter);
		this.separatorComponent.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
	}

	/**
	 * Skeleton constructor.  This constructor creates a default separator: a panel with two lines using the foreground
	 * color of this container with the background color between.  The parameter specifies the height of this
	 * separator bar.
	 * @param topComponent The top {@link JComponent}.
	 * @param bottomComponent The bottom {@link JComponent}.
	 * @param height The height of the separator bar.
	 */
	public VerticalSplitPanel(JComponent topComponent, JComponent bottomComponent, int height)
	{
		this(topComponent, bottomComponent, new DefaultSeparator(height));
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Gets all the components in this container.  This array is guaranteed to contain, in order,
	 * {@link VerticalSplitPanel#topComponent}, {@link VerticalSplitPanel#bottomComponent}, and
	 * {@link VerticalSplitPanel#separatorComponent}.
	 * @return The above-described array.
	 */
	public Component[] getComponents()
	{
		return contentsArray;
	}

	/**
	 * Gets the number of components in this panel.  The answer is always <code>3</code>.
	 * @return <code>3</code>.
	 */
	public int getComponentCount()
	{
		return 3;
	}

	/**
	 * Gets the nth component in this container.
	 * @param n The index of the component to get.
	 * @return The n<sup>th</sup> component in this container.
	 * @throws ArrayIndexOutOfBoundsException If the n<sup>th</sup> value does not exist.
	 * @see VerticalSplitPanel#getComponents()
	 */
	public Component getComponent(int n)
	{
		return contentsArray[n];
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

	/**
	 * This {@link JComponent} extension is the default separator used for the {@link VerticalSplitPanel}.
	 * @author Zachary Palmer
	 */
	static class DefaultSeparator extends JComponent
	{
		/** The desired size of this {@link DefaultSeparator}. */
		protected Dimension dimension;

		/**
		 * Creates a new default separator using the specified height and color information.
		 * @param height The height to use.
		 */
		public DefaultSeparator(int height)
		{
			dimension = new Dimension(30, height);
		}

		/**
		 * Retrieves the preferred size of this {@link DefaultSeparator}.
		 * @return The preferred size of this {@link DefaultSeparator}.
		 */
		public Dimension getPreferredSize()
		{
			return dimension;
		}

		/**
		 * Draws the default separator.
		 * @param g The {@link Graphics} object on which to draw the default separator.
		 */
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Color foreground = getParent().getForeground();
			g.setColor(getParent().getForeground());
			g.drawLine(0, 0, getWidth() - 1, 0);
			g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			if (getHeight() > 2)
			{
				Color background = getParent().getBackground();
				Color color = new Color((foreground.getRed() + background.getRed()) / 2,
				                        (foreground.getGreen() + background.getGreen()) / 2,
				                        (foreground.getBlue() + background.getBlue()) / 2);
				g.setColor(color);
				g.drawLine(0, 1, getWidth() - 1, 1);
				g.drawLine(0, getHeight() - 2, getWidth() - 1, getHeight() - 2);
				if (getHeight() > 4)
				{
					g.setColor(background);
					g.fillRect(0, 2, getWidth(), getHeight() - 4);
				}
			}
		}
	}

	/**
	 * This {@link LayoutManager} is specifically designed for laying out the {@link VerticalSplitPanel}.  It places
	 * the separator component in its currently-defined position, positions the bottom component under it and the top
	 * component above it.  If there is no room for one or eiter of the presentation components, they are not shown.
	 * All three components are sized to be the exact width of the {@link VerticalSplitPanel}.
	 * @author Zachary Palmer
	 */
	static class PanelLayoutManager implements LayoutManager
	{
		/** A constant specifying the dimension (<code>0</code>,<code>0</code>). */
		protected static final Dimension ZERO_DIMENSION = new Dimension(0, 0);

		/**
		 * Does nothing.
         * @param name Ignored.
         * @param comp Ignored.
		 */
		public void addLayoutComponent(String name, Component comp)
		{
		}

		/**
		 * Lays out the specified container.
		 * @param parent the container to be laid out
		 */
		public void layoutContainer(Container parent)
		{
			VerticalSplitPanel panel = (VerticalSplitPanel) (parent);
			Component[] components = panel.getComponents();
			Component top = components[0];
			Component bottom = components[1];
			Component separator = components[2];
			Point separatorLocation = separator.getLocation();
			separator.setLocation(0, (int) (separatorLocation.getY()));
			separator.setSize(panel.getWidth(), (int) (separator.getPreferredSize().getHeight()));
			top.setLocation(0, 0);
			top.setSize(panel.getWidth(), (int) (separatorLocation.getY()));
			bottom.setLocation(0, (int) (separatorLocation.getY() + separator.getHeight()));
			bottom.setSize(panel.getWidth(), panel.getHeight() - separator.getHeight() - top.getHeight());
		}

		/**
		 * Returns the dimension (<code>0</code>,<code>0</code>).
		 * @param parent The component to be laid out
         * @return The minimum layout size for the component.
		 * @see #preferredLayoutSize
		 */
		public Dimension minimumLayoutSize(Container parent)
		{
			return ZERO_DIMENSION;
		}

		/**
		 * Calculates the preferred size dimensions for the specified container given the components it contains.
		 * @param parent The container to be laid out
         * @return The preferred layout size for the component.
		 * @see #minimumLayoutSize
		 */
		public Dimension preferredLayoutSize(Container parent)
		{
			VerticalSplitPanel panel = (VerticalSplitPanel) parent;
			Point p2 = panel.getComponent(1).getLocation();
			Dimension d2 = panel.getComponent(1).getPreferredSize();
			Dimension d3 = panel.getComponent(2).getPreferredSize();
			return new Dimension((int) (p2.getX() + d2.getWidth() + d3.getWidth()),
			                     (int) (p2.getY() + d2.getHeight() + d3.getHeight()));
		}

		/**
		 * Does nothing.
         * @param comp Ignored.
		 */
		public void removeLayoutComponent(Component comp)
		{
		}
	}
}