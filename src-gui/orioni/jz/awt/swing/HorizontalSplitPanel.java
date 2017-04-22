package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * This panel is designed to contain three {@link JComponent}s.  Two of the {@link JComponent}s are to be presented
 * to the user, stacked horizontally.  The third is provided to separate the first two.
 * <P>
 * The size of the two {@link JComponent}s provided will be determined by the size of this panel as well as the size of
 * the separator component.  The separator component can be dragged by the user to change the size of the two
 * {@link JComponent}s being presented; therefore, the separation component is usually fairly thin.
 *
 * @author Zachary Palmer
 */
public class HorizontalSplitPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The left {@link JComponent}. */
	protected JComponent leftComponent;
	/** The right {@link JComponent}. */
	protected JComponent rightComponent;
	/** The {@link JComponent} being used a separator. */
	protected JComponent separatorComponent;

	/** The {@link JComponent}<code>[]</code> that is used to store the components found within this
	 *  {@link orioni.jz.awt.swing.HorizontalSplitPanel}. */
	protected JComponent[] contentsArray;

	/** The {@link MouseMotionAdapter} which is attached to the separator component to track mouse drags. */
	protected MouseMotionAdapter separatorMotionAdapter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param leftComponent The left {@link JComponent}.
	 * @param rightComponent The right {@link JComponent}.
	 * @param separatorComponent The top {@link JComponent}.
	 */
	public HorizontalSplitPanel(JComponent leftComponent, JComponent rightComponent, JComponent separatorComponent)
	{
		super();
		this.leftComponent = leftComponent;
		this.rightComponent = rightComponent;
		this.separatorComponent = separatorComponent;
		contentsArray = new JComponent[]{this.leftComponent, this.rightComponent, this.separatorComponent};
		this.setLayout(new PanelLayoutManager());
		this.add(this.leftComponent);
		this.add(this.rightComponent);
		this.add(this.separatorComponent);
		separatorMotionAdapter = new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				int x = e.getX() + e.getComponent().getX();
				x = Math.min(x, e.getComponent().getParent().getWidth() - e.getComponent().getWidth());
				x = Math.max(0, x);
				e.getComponent().setLocation(x, (int) (e.getComponent().getLocation().getY()));
				doLayout();
			}
		};
		this.separatorComponent.addMouseMotionListener(separatorMotionAdapter);
		this.separatorComponent.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
	}

	/**
	 * Skeleton constructor.  This constructor creates a default separator: a panel with two lines using the foreground
	 * color of this container with the background color between.  The parameter specifies the width of this
	 * separator bar.
	 * @param leftComponent The top {@link JComponent}.
	 * @param rightComponent The bottom {@link JComponent}.
	 * @param width The width of the separator bar.
	 */
	public HorizontalSplitPanel(JComponent leftComponent, JComponent rightComponent, int width)
	{
		this(leftComponent, rightComponent, new DefaultSeparator(width));
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Gets all the components in this container.  This array is guaranteed to contain, in order,
	 * {@link HorizontalSplitPanel#leftComponent}, {@link HorizontalSplitPanel#rightComponent}, and
	 * {@link HorizontalSplitPanel#separatorComponent}.
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
	 * @see orioni.jz.awt.swing.HorizontalSplitPanel#getComponents()
	 */
	public Component getComponent(int n)
	{
		return contentsArray[n];
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

	/**
	 * This {@link JComponent} extension is the default separator used for the {@link HorizontalSplitPanel}.
	 * @author Zachary Palmer
	 */
	static class DefaultSeparator extends JComponent
	{
		/** The desired size of this {@link DefaultSeparator}. */
		protected Dimension dimension;

		/**
		 * Creates a new default separator using the specified width and color information.
		 * @param width The width to use.
		 */
		public DefaultSeparator(int width)
		{
			dimension = new Dimension(width, 30);
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
			g.drawLine(0, 0, 0, getHeight() - 1);
			g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);
			if (getWidth() > 2)
			{
				Color background = getParent().getBackground();
				Color color = new Color((foreground.getRed() + background.getRed()) / 2,
				                        (foreground.getGreen() + background.getGreen()) / 2,
				                        (foreground.getBlue() + background.getBlue()) / 2);
				g.setColor(color);
				g.drawLine(1, 0, 1, getHeight() - 1);
				g.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 1);
				if (getHeight() > 4)
				{
					g.setColor(background);
					g.fillRect(2, 0, getWidth() - 4, getHeight());
				}
			}
		}
	}

	/**
	 * This {@link LayoutManager} is specifically designed for laying out the {@link HorizontalSplitPanel}.  It places
	 * the separator component in its currently-defined position, positions the bottom component under it and the top
	 * component above it.  If there is no room for one or eiter of the presentation components, they are not shown.
	 * All three components are sized to be the exact width of the {@link orioni.jz.awt.swing.HorizontalSplitPanel}.
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
			HorizontalSplitPanel panel = (HorizontalSplitPanel) (parent);
			Component[] components = panel.getComponents();
			Component left = components[0];
			Component right = components[1];
			Component separator = components[2];
			Point separatorLocation = separator.getLocation();
			separator.setLocation((int) (separatorLocation.getX()), 0);
			separator.setSize((int) (separator.getPreferredSize().getWidth()), panel.getHeight());
			left.setLocation(0, 0);
			left.setSize((int) (separatorLocation.getX()), panel.getHeight());
			right.setLocation((int) (separatorLocation.getX() + separator.getWidth()), 0);
			right.setSize(panel.getWidth() - separator.getWidth() - left.getWidth(), panel.getHeight());
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
		 * @param parent The container to be laid out.
         * @return The minimum layout size for the component.
		 * @see #minimumLayoutSize
		 */
		public Dimension preferredLayoutSize(Container parent)
		{
			HorizontalSplitPanel panel = (HorizontalSplitPanel) parent;
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