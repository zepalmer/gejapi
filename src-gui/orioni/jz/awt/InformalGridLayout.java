package orioni.jz.awt;

import java.awt.*;

/**
 * This <code>LayoutManager</code> arranges its components in a grid, similar to <code>GridLayout</code>.  The grid,
 * however, is more flexible than the grid produced by <code>GridLayout</code>.  Instead of each cell being the same
 * size, a cell has a height equal to the maximum height in its row and a width equal to the maximum width in its
 * column.
 * <P>
 * In addition to these standards, it is also possible for the user to specify a number of border pixels for each of
 * the cells.  The border pixels surround the cell, preventing the component within it from contacting any other
 * cell directly.
 * <P>
 * This layout manager can observe component alignments when positioning objects within the containers.  Whether or not
 * it does so is specified at construction time.
 * <P>
 * This layout manager will also observe the alignment of the container it is laying out.  If the container is larger
 * than the size of the informal grid, the components within the grid will be aligned as specified by the container's
 * X and Y alignments.
 *
 * @author Zachary Palmer
 */
public class InformalGridLayout implements LayoutManager
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

	/** The constant indicating that the orientation of this layout manager is to be detected from the container it is
	 *  laying out.  This is default behavior. */
	public static final int ORIENTATION_DETECT = 0;
	/** The constant indicating that this layout manager should lay out the components of containers in a left-to-right
	 *  fashion, using horizontal rows top-to-bottom. */
	public static final int ORIENTATION_LEFT_TO_RIGHT_HORIZONTAL = 1;
	/** The constant indicating that this layout manager should lay out the components of containers in a right-to-left
	 *  fashion, using horizontal rows top-to-bottom. */
	public static final int ORIENTATION_RIGHT_TO_LEFT_HORIZONTAL = 2;
	/** The constant indicating that this layout manager should lay out the components of containers in a top-to-bottom
	 *  fashion, using vertical columns left-to-right. */
	public static final int ORIENTATION_LEFT_TO_RIGHT_VERTICAL = 3;
	/** The constant indicating that this layout manager should lay out the components of containers in a top-to-bottom
	 *  fashion, using vertical columns right-to-left. */
	public static final int ORIENTATION_RIGHT_TO_LEFT_VERTICAL = 4;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The number of pixels to leave as horizontal border for the cells. */
	protected int borderWidth;
	/** The number of pixels to leave as vertical border for the cells. */
	protected int borderHeight;
	/** The number of columns to use in this layout. */
	protected int columns;
	/** The number of rows to use in this layout. */
	protected int rows;
	/** Determines whether or not this manager ignores the alignments of its components. */
	protected boolean ignoreAlignments;

	/** Determines the orientation behavior of this layout manager. */
	protected int orientationBehavior;

	 // CONSTRUCTORS //////////////////////////////////////////////////////////////////

	 /**
	 * Skeleton constructor.  Assumes that no border pixels are desired and that this layout ignores the alignment of
	 * its components.  Also assumes that orientation of components will be detected from their containers.
	 * @param columns The number of columns in this <code>InformalGridLayout</code>.
	 * @param rows The number of rows in this <code>InformalGridLayout</code>.
	 */
	public InformalGridLayout(int columns, int rows)
	{
		this(columns, rows, 0, 0, true, ORIENTATION_DETECT);
	}

	/**
	 * Skeleton constructor.  Assumes that no border pixels are desired and that this layout ignores the alignment of
	 * its components.  Also assumes that orientation of components will be detected from their containers.
	 * @param columns The number of columns in this <code>InformalGridLayout</code>.
	 * @param rows The number of rows in this <code>InformalGridLayout</code>.
	 * @param ignoreAlignments <code>true</code> if this layout manager should ignore the alignment requests of its
	 *                          components; <code>false</code> otherwise.
	 */
	public InformalGridLayout(int columns, int rows, boolean ignoreAlignments)
	{
		this(columns, rows, 0, 0, ignoreAlignments, ORIENTATION_DETECT);
	}

	/**
	 * Skeleton constructor.  Assumes that this layout manager will ignore the alignments of its components.  Also
	 * assumes that orientation of components will be detected from their containers.
	 * @param columns The number of columns in this <code>InformalGridLayout</code>.
	 * @param rows The number of rows in this <code>InformalGridLayout</code>.
	 * @param borderWidth The number of pixels on either side of the <code>Component</code> to leave as border.
	 * @param borderHeight The number of pixels above and below the <code>Component</code> to leave as border.
	 */
	public InformalGridLayout(int columns, int rows, int borderWidth, int borderHeight)
	{
		this(columns, rows, borderWidth, borderHeight, true, ORIENTATION_DETECT);
	}

	/**
	 * Skeleton constructor.  Assumes that orientation of components will be detected from their containers.
	 * @param columns The number of columns in this <code>InformalGridLayout</code>.
	 * @param rows The number of rows in this <code>InformalGridLayout</code>.
	 * @param borderWidth The number of pixels on either side of the <code>Component</code> to leave as border.
	 * @param borderHeight The number of pixels above and below the <code>Component</code> to leave as border.
	 * @param ignoreAlignments <code>true</code> if this layout manager should ignore the alignment requests of its
	 *                          components; <code>false</code> otherwise.  If alignment is ignored, all components are
	 *                          increased to the size of their respective cells.  If alignment is observed (this value
	 *                          is <code>false</code>, cells are given the aforementioned size but components within
	 *                          them are set to their preferred size and aligned appropriately.
	 */
	public InformalGridLayout(int columns, int rows, int borderWidth, int borderHeight, boolean ignoreAlignments)
	{
		this(columns, rows, borderWidth, borderHeight, ignoreAlignments, ORIENTATION_DETECT);
	}

	/**
	 * General constructor.
	 * @param columns The number of columns in this <code>InformalGridLayout</code>.
	 * @param rows The number of rows in this <code>InformalGridLayout</code>.
	 * @param borderWidth The number of pixels on either side of the <code>Component</code> to leave as border.
	 * @param borderHeight The number of pixels above and below the <code>Component</code> to leave as border.
	 * @param ignoreAlignments <code>true</code> if this layout manager should ignore the alignment requests of its
	 *                          components; <code>false</code> otherwise.  If alignment is ignored, all components are
	 *                          increased to the size of their respective cells.  If alignment is observed (this value
	 *                          is <code>false</code>, cells are given the aforementioned size but components within
	 *                          them are set to their preferred size and aligned appropriately.
	 * @param orientation The orientation behavior of this layout manager, as one of the <code>ORIENTATION_XXXX</code>
	 *                    constants on this class.
	 */
	public InformalGridLayout(int columns, int rows, int borderWidth, int borderHeight, boolean ignoreAlignments,
	                          int orientation)
	{
		this.columns = columns;
		this.rows = rows;
		this.borderWidth = borderWidth;
		this.borderHeight = borderHeight;
		this.ignoreAlignments = ignoreAlignments;
		orientationBehavior = orientation;
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Does nothing.
     * @param name Ignored.
     * @param comp Ignored.
	 */
	public void addLayoutComponent(String name, Component comp)
	{
	}

	/**
	 * Does nothing.
     * @param comp Ignored.
	 */
	public void removeLayoutComponent(Component comp)
	{
	}

	/**
	 * Lays out the specified container.  This layout does not respect the Container's minimum, maximum, or preferred
	 * sizes.
	 * @param parent the container to be laid out
	 */
	public void layoutContainer(Container parent)
	{
		// We will repreform the size check for performance reasons
		int[][] sizes = determineNecessarySizes(parent);

		int[] columnWidths = sizes[1];
		int[] rowHeights = sizes[0];

		// Determine column and row coordinates.  The extra row and column are for the calculation of pane size.
		int[] columnX = new int[columnWidths.length + 1];
		int[] rowY = new int[rowHeights.length + 1];

		columnX[0] = borderWidth;
		rowY[0] = borderHeight;

		for (int i = 1; i < columnX.length; i++)
		{
			columnX[i] = columnX[i - 1] + columnWidths[i - 1] + borderWidth;
		}
		for (int i = 1; i < rowY.length; i++)
		{
			rowY[i] = rowY[i - 1] + rowHeights[i - 1] + borderHeight;
		}

		// Compensate for component's alignment
		int leftOffset = (int) ((parent.getWidth() - columnX[columnX.length - 1]) * parent.getAlignmentX());
		int topOffset = (int) ((parent.getHeight() - rowY[rowY.length - 1]) * parent.getAlignmentY());

		// Determine component row and height and set size and position
		Component[] components = parent.getComponents();
		ComponentOrientation orientation = parent.getComponentOrientation();
		int maxValue = components.length;
		if (maxValue > rows * columns) maxValue = rows * columns;
		for (int i = 0; i < maxValue; i++)
		{
			int columnNumber = getColumnFromOrientationIndex(i, orientation);
			int rowNumber = getRowFromOrientationIndex(i, orientation);

			if (ignoreAlignments)
			{
				components[i].setSize(columnWidths[columnNumber],
				                      rowHeights[rowNumber]);
				components[i].setLocation(columnX[columnNumber] + leftOffset,
				                          rowY[rowNumber] + topOffset);
			} else
			{
				components[i].setSize(components[i].getPreferredSize());
				// Make sure that the component's horizontal and vertical alignment is considered
				int indentWidth = (int)
				        ((columnWidths[columnNumber] - components[i].getWidth()) * components[i].getAlignmentX());
				int indentHeight = (int)
				        ((rowHeights[rowNumber] - components[i].getHeight()) * components[i].getAlignmentY());
				components[i].setLocation(
				        columnX[columnNumber] + indentWidth + leftOffset,
				        rowY[rowNumber] + indentHeight + topOffset);
			}
		}
	}

	/**
	 * Calculates the minimum size dimensions for the specified
	 * container, given the components it contains.
	 * @param parent the component to be laid out
     * @return The minimum layout size for the container.
	 * @see #preferredLayoutSize
	 */
	public Dimension minimumLayoutSize(Container parent)
	{
		return preferredLayoutSize(parent);
	}

	/**
	 * Calculates the preferred size dimensions for the specified
	 * container, given the components it contains.
	 * @param parent the container to be laid out
     * @return The preferred layout size for the container.
	 * @see #minimumLayoutSize
	 */
	public Dimension preferredLayoutSize(Container parent)
	{
		int i;

		int[][] sizes = determineNecessarySizes(parent);

		int[] columnWidths = sizes[1];
		int[] rowHeights = sizes[0];

		int width = 0;
		int height = 0;

		// Tally the widths
		for (i = 0; i < sizes[1].length; i++)
		{
			width += columnWidths[i];
		}
		// Tally the heights
		for (i = 0; i < sizes[0].length; i++)
		{
			height += rowHeights[i];
		}

		return new Dimension(
                width + (borderWidth * (columns + 1)) + parent.getInsets().left + parent.getInsets().right,
                height + (borderHeight * (rows + 1)) + parent.getInsets().top + parent.getInsets().bottom);
	}

	/**
	 * This method establishes the necessary heights and widths of the rows and columns (respectively) in this layout.
	 * The height and width borders are not applied to these values.
	 * @param container The <code>Container</code> for which appropriate sizes should be determined.
	 * @return An <code>int[2][]</code>.  The first integer array contains a series of heights, one for each row in the
	 *         layout.  The second integer array contains a series of widths, one for each column in the layout.
	 */
	protected int[][] determineNecessarySizes(Container container)
	{
        // TODO: if the sum of a dimension's sizes exceeds the parent container's size, proportionally reduce the dimension

		int i;

		Component[] components = container.getComponents();

		ComponentOrientation orientation = container.getComponentOrientation();

		// Determine size of the layout
		int[] rowHeights = new int[rows];
		int[] columnWidths = new int[columns];
		int maxValue = components.length;
		if (maxValue > rows * columns) maxValue = rows * columns;
		for (i = 0; i < maxValue; i++)
		{
			// Determine component's row and column positions
			int rowNumber = getRowFromOrientationIndex(i, orientation);
			int columnNumber = getColumnFromOrientationIndex(i, orientation);

			// Now adjust the appropriate dimension maximums
			int componentHeight = (int) (components[i].getPreferredSize().getHeight());
			int componentWidth = (int) (components[i].getPreferredSize().getWidth());
			if (componentHeight > rowHeights[rowNumber])
			{
				rowHeights[rowNumber] = componentHeight;
			}
			if (componentWidth > columnWidths[columnNumber])
			{
				columnWidths[columnNumber] = componentWidth;
			}
		}

		// Prepare return value
		int[][] ret = new int[2][];
		ret[0] = rowHeights;
		ret[1] = columnWidths;

		return ret;
	}

	/**
	 * Determines the row of a component at the given index with the given orientation.
	 * @param index The index at which the component is located.
	 * @param orientation The <code>ComponentOrientation</code> that this index obeys.
	 * @return The row of the component in a grid as limited by <code>m_columns</code>.
	 */
	public int getRowFromOrientationIndex(int index, ComponentOrientation orientation)
	{
		if (shouldUseHorizontalLayout(orientation))
		{
			return index / columns;
		} else
		{
			return index % rows;
		}
	}

	/**
	 * Determines the column of a component at the given index with the given orientation.
	 * @param index The index at which the component is located.
	 * @param orientation The <code>ComponentOrientation</code> that this index obeys.
	 * @return The column of the component in a grid as limited by <code>m_rows</code>.
	 */
	public int getColumnFromOrientationIndex(int index, ComponentOrientation orientation)
	{
		if (shouldUseHorizontalLayout(orientation))
		{
			if (shouldUseLeftToRightLayout(orientation))
			{
				return index % columns;
			} else
			{
				return columns - index % columns - 1;
			}
		} else
		{
			if (shouldUseLeftToRightLayout(orientation))
			{
				return index / rows;
			} else
			{
				return columns - index / rows - 1;
			}
		}
	}

	/**
	 * Determines if this layout manager should lay out the component in a horizontal fashion.  This is determined
	 * as a function of the orientation behavior of the layout manager and (if that behavior is currently set to
	 * {@link InformalGridLayout#ORIENTATION_DETECT}) the value of the {@link ComponentOrientation} object provided.
	 * @param componentOrientation The {@link ComponentOrientation} object in question.
     * @return <code>true</code> if a horizontal layout should be used; <code>false</code> otherwise.
	 */
	public boolean shouldUseHorizontalLayout(ComponentOrientation componentOrientation)
	{
		if (orientationBehavior ==ORIENTATION_DETECT)
		{
			return componentOrientation.isHorizontal();
		} else
		{
			return ((orientationBehavior ==ORIENTATION_LEFT_TO_RIGHT_HORIZONTAL) ||
			        (orientationBehavior ==ORIENTATION_RIGHT_TO_LEFT_HORIZONTAL));
		}
	}

	/**
	 * Determines if this layout manager should lay out the component in a left-to-right fashion.  This is determined
	 * as a function of the orientation behavior of the layout manager and (if that behavior is currently set to
	 * {@link InformalGridLayout#ORIENTATION_DETECT}) the value of the {@link ComponentOrientation} object provided.
	 * @param componentOrientation The {@link ComponentOrientation} object in question.
     * @return <code>true</code> if a left-to-right layout should be used; <code>false</code> otherwise.
	 */
	public boolean shouldUseLeftToRightLayout(ComponentOrientation componentOrientation)
	{
		if (orientationBehavior ==ORIENTATION_DETECT)
		{
			return componentOrientation.isLeftToRight();
		} else
		{
			return ((orientationBehavior ==ORIENTATION_LEFT_TO_RIGHT_HORIZONTAL) ||
			        (orientationBehavior ==ORIENTATION_LEFT_TO_RIGHT_VERTICAL));
		}
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //