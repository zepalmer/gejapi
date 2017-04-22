package orioni.jz.awt;

import java.awt.*;
import java.util.ArrayList;

/**
 * This grid-like variant is designed to produce a grid which respects one of the container's two boundaries.  The
 * default respected boundary is horizontal.  This means that the components are positioned in such a way (if possible)
 * as to fill the container horizontally but not exceed the horizontal bounds.  Thus, the number of columns in a row is
 * determined by the components' preferred widths and the width of the container.  Once the width of a row is
 * established (in terms of columns), the components are used to fill out the rows.  In the event that the vertical
 * boundary is respected, columns are established in terms of a number of rows and columns are used as the major
 * ordering.
 * <p/>
 * The use of the preferred width or height of the components to determine the size of a row or column is identical to
 * the manner in which {@link InformalGridLayout} handles its components.
 * <p/>
 * The {@link ContainedInformalGridLayout} also supports adjusting the number of bands (rows or columns, as approriate)
 * in terms of a given increment.  For example, a {@link ContainedInformalGridLayout} with an increment of two would
 * always have an even number of bands.  This is useful in the event that a given container's components are logically
 * connected somehow.  Consider the case of a container in which text fields and text labels are stored.  Assume that
 * each text label corresponds to a single text field; it would be desirable for the layout manager to keep them
 * associated. By setting an increment of two, each label will have a field immediately adjacent to it (as opposed to
 * appearing in the next band).
 *
 * @author Zachary Palmer
 */
public class ContainedInformalGridLayout implements LayoutManager
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An enumeration of the orientations of a {@link ContainedInformalGridLayout}.
     */
    public static enum RespectedBoundary
    {
        HORIZONTAL,
        VERTICAL
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The padding which appears between each row.
     */
    protected int rowPadding;
    /**
     * The padding which appears between each column.
     */
    protected int columnPadding;
    /**
     * Whether or not component alignment is respected.
     */
    protected boolean respectAlignments;
    /**
     * The respected boundary.
     */
    protected RespectedBoundary boundary;
    /**
     * The number of major bands to increment at a time.
     */
    protected int bandIncrement;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a horizontal respected boundary, a band increment of one, two pixel padding in
     * both directions, and that alignments are not respected.
     */
    public ContainedInformalGridLayout()
    {
        this(2, 2, false, RespectedBoundary.HORIZONTAL, 1);
    }

    /**
     * Skeleton constructor.  Assumes a horizontal respected boundary and a band increment of one.
     *
     * @param rowPadding        The number of pixels used to pad the space between rows.
     * @param columnPadding     The number of pixels used to pad the space between columns.
     * @param respectAlignments <code>true</code> if component alignments are respected; <code>false</code> otherwise.
     */
    public ContainedInformalGridLayout(int rowPadding, int columnPadding, boolean respectAlignments)
    {
        this(rowPadding, columnPadding, respectAlignments, RespectedBoundary.HORIZONTAL, 1);
    }

    /**
     * Skeleton constructor.  Assumes a horizontal respected boundary.
     *
     * @param rowPadding        The number of pixels used to pad the space between rows.
     * @param columnPadding     The number of pixels used to pad the space between columns.
     * @param respectAlignments <code>true</code> if component alignments are respected; <code>false</code> otherwise.
     * @param bandIncrement     The number of major bands to increment at a time.
     */
    public ContainedInformalGridLayout(int rowPadding, int columnPadding, boolean respectAlignments,
                                       int bandIncrement)
    {
        this(rowPadding, columnPadding, respectAlignments, RespectedBoundary.HORIZONTAL, bandIncrement);
    }

    /**
     * Skeleton constructor.  Assumes a band increment of one.
     *
     * @param rowPadding        The number of pixels used to pad the space between rows.
     * @param columnPadding     The number of pixels used to pad the space between columns.
     * @param respectAlignments <code>true</code> if component alignments are respected; <code>false</code> otherwise.
     * @param boundary          The boundary which is respected.
     */
    public ContainedInformalGridLayout(int rowPadding, int columnPadding, boolean respectAlignments,
                                       RespectedBoundary boundary)
    {
        this(rowPadding, columnPadding, respectAlignments, boundary, 1);
    }

    /**
     * General constructor.
     *
     * @param rowPadding        The number of pixels used to pad the space between rows.
     * @param columnPadding     The number of pixels used to pad the space between columns.
     * @param respectAlignments <code>true</code> if component alignments are respected; <code>false</code> otherwise.
     * @param boundary          The boundary which is respected.
     * @param bandIncrement     The number of major bands to increment at a time.
     */
    public ContainedInformalGridLayout(int rowPadding, int columnPadding, boolean respectAlignments,
                                       RespectedBoundary boundary, int bandIncrement)
    {
        super();
        this.rowPadding = rowPadding;
        this.columnPadding = columnPadding;
        this.respectAlignments = respectAlignments;
        this.boundary = boundary;
        this.bandIncrement = bandIncrement;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Ignored.
     *
     * @param name Ignored.
     * @param comp Ignored.
     */
    public void addLayoutComponent(String name, Component comp)
    {
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    public void layoutContainer(Container parent)
    {
        int[] columnWidths;
        int[] rowHeights;
        int[] columnLocations;
        int[] rowLocations;
        int bandCount;
        if (boundary.equals(RespectedBoundary.HORIZONTAL))
        {
            bandCount = getContainerOptimalColumnCount(parent);
            // Determine widths of columns and heights of rows.
            columnWidths = new int[bandCount];
            java.util.List<Integer> rowHeightsList = new ArrayList<Integer>();
            int columnIndex = 0;
            int currentRowHeight = 0;
            for (Component c : parent.getComponents())
            {
                columnWidths[columnIndex] =
                        (int) Math.max(columnWidths[columnIndex], c.getPreferredSize().getWidth());
                currentRowHeight = (int) (Math.max(currentRowHeight, c.getPreferredSize().getHeight()));
                columnIndex++;
                if (columnIndex >= bandCount)
                {
                    columnIndex = 0;
                    rowHeightsList.add(currentRowHeight);
                }
            }
            rowHeightsList.add(currentRowHeight); // an extra one won't hurt
            rowHeights = new int[rowHeightsList.size()];
            for (int i = 0; i < rowHeightsList.size(); i++) rowHeights[i] = rowHeightsList.get(i);

            // Adjust column width factor
            double widthFactor = parent.getWidth() / getContainerSizeFromColumns(parent, bandCount).getWidth();
            for (int i = 0; i < columnWidths.length; i++) columnWidths[i] = (int) (columnWidths[i] * widthFactor);

            // Create location arrays
            columnLocations = new int[bandCount];
            if (columnLocations.length > 0)
            {
                columnLocations[0] = 0;
                for (int i = 1; i < columnLocations.length; i++)
                {
                    columnLocations[i] = columnLocations[i - 1] + columnWidths[i - 1];
                }
            }
            rowLocations = new int[rowHeights.length];
            if (rowLocations.length > 0)
            {
                rowLocations[0] = 0;
                for (int i = 1; i < rowLocations.length; i++)
                {
                    rowLocations[i] = rowLocations[i - 1] + rowHeights[i - 1];
                }
            }
        } else if (boundary.equals(RespectedBoundary.VERTICAL))
        {
            bandCount = getContainerOptimalRowCount(parent);
            // Determine widths of columns and heights of rows.
            rowHeights = new int[bandCount];
            java.util.List<Integer> columnWidthsList = new ArrayList<Integer>();
            int rowIndex = 0;
            int currentColumnWidth = 0;
            for (Component c : parent.getComponents())
            {
                rowHeights[rowIndex] =
                        (int) Math.max(rowHeights[rowIndex], c.getPreferredSize().getHeight());
                currentColumnWidth = (int) (Math.max(currentColumnWidth, c.getPreferredSize().getWidth()));
                rowIndex++;
                if (rowIndex >= bandCount)
                {
                    rowIndex = 0;
                    columnWidthsList.add(currentColumnWidth);
                }
            }
            columnWidthsList.add(currentColumnWidth); // an extra one won't hurt
            columnWidths = new int[columnWidthsList.size()];
            for (int i = 0; i < columnWidthsList.size(); i++) columnWidths[i] = columnWidthsList.get(i);

            // Adjust column width factor
            double heightFactor = parent.getHeight() / getContainerSizeFromRows(parent, bandCount).getHeight();
            for (int i = 0; i < rowHeights.length; i++) rowHeights[i] = (int) (rowHeights[i] * heightFactor);

            // Create location arrays
            rowLocations = new int[bandCount];
            if (rowLocations.length > 0)
            {
                rowLocations[0] = 0;
                for (int i = 1; i < rowLocations.length; i++)
                {
                    rowLocations[i] = rowLocations[i - 1] + rowHeights[i - 1];
                }
            }
            columnLocations = new int[columnWidths.length];
            if (columnLocations.length > 0)
            {
                columnLocations[0] = 0;
                for (int i = 1; i < columnLocations.length; i++)
                {
                    columnLocations[i] = columnLocations[i - 1] + columnWidths[i - 1];
                }
            }
        } else
        {
            throw new IllegalStateException("Unrecognized boundary: " + boundary);
        }

        // Arrange components
        int columnIndex = 0;
        int rowIndex = 0;
        for (Component c : parent.getComponents())
        {
            if (respectAlignments)
            {
                c.setSize(
                        (int) (Math.min(columnWidths[columnIndex], c.getPreferredSize().getWidth())),
                        (int) (Math.min(rowHeights[rowIndex], c.getPreferredSize().getHeight())));
                c.setLocation(
                        (int) (columnLocations[columnIndex] +
                               (columnWidths[columnIndex] - c.getWidth()) * c.getAlignmentX()),
                        (int) (rowLocations[rowIndex] +
                               (rowHeights[rowIndex] - c.getHeight()) * c.getAlignmentY()));
            } else
            {
                c.setSize(columnWidths[columnIndex], rowHeights[rowIndex]);
                c.setLocation(columnLocations[columnIndex], rowLocations[rowIndex]);
            }
            switch (boundary)
            {
                case HORIZONTAL:
                    columnIndex++;
                    if (columnIndex >= bandCount)
                    {
                        columnIndex = 0;
                        rowIndex++;
                    }
                    break;
                case VERTICAL:
                    rowIndex++;
                    if (rowIndex >= bandCount)
                    {
                        rowIndex = 0;
                        columnIndex++;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unrecognized boundary: " + boundary);
            }
        }
    }

    /**
     * Retrieves the width of the provided {@link Container} if the given number of columns is used.
     *
     * @param container The {@link Container} in question.
     * @param columns   The number of columns which would be used.
     * @return The width of that container.
     */
    protected Dimension getContainerSizeFromColumns(Container container, int columns)
    {
        int[] columnWidths = new int[columns];
        int height = 0;
        int currentRowHeight = 0;
        int columnIndex = 0;
        for (Component c : container.getComponents())
        {
            columnWidths[columnIndex] =
                    (int) (Math.max(columnWidths[columnIndex], c.getPreferredSize().getWidth()));
            columnIndex++;
            currentRowHeight = (int) (Math.max(currentRowHeight, c.getPreferredSize().getHeight()));
            if (columnIndex >= columns)
            {
                height += currentRowHeight;
                columnIndex = 0;
                currentRowHeight = 0;
            }
        }
        height += currentRowHeight;
        int width = 0;
        for (int w : columnWidths) width += w;
        width += columnPadding * columns + columnPadding;
        return new Dimension(width, height);
    }

    /**
     * Retrieves the width of the provided {@link Container} if the given number of rows is used.
     *
     * @param container The {@link Container} in question.
     * @param rows      The number of rows which would be used.
     * @return The width of that container.
     */
    protected Dimension getContainerSizeFromRows(Container container, int rows)
    {
        int[] rowHeights = new int[rows];
        int width = 0;
        int currentColumnWidth = 0;
        int rowIndex = 0;
        for (Component c : container.getComponents())
        {
            rowHeights[rowIndex] =
                    (int) (Math.max(rowHeights[rowIndex], c.getPreferredSize().getHeight()));
            rowIndex++;
            currentColumnWidth = (int) (Math.max(currentColumnWidth, c.getPreferredSize().getWidth()));
            if (rowIndex >= rows)
            {
                width += currentColumnWidth;
                rowIndex = 0;
                currentColumnWidth = 0;
            }
        }
        width += currentColumnWidth;
        int height = 0;
        for (int h : rowHeights) height += h;
        height += columnPadding * rows + columnPadding;
        return new Dimension(width, height);
    }

    /**
     * Finds the optimal column count for the provided container.
     *
     * @param container The {@link Container} in question.
     * @return The column count to use for that {@link Container}.
     */
    protected int getContainerOptimalColumnCount(Container container)
    {
        int columns = bandIncrement;
        while ((getContainerSizeFromColumns(container, columns).getWidth() < container.getWidth()) &&
               (columns < container.getComponentCount()))
        {
            columns += bandIncrement;
        }
        return Math.max(bandIncrement, columns - bandIncrement);
    }

    /**
     * Finds the optimal row count for the provided container.
     *
     * @param container The {@link Container} in question.
     * @return The row count to use for that {@link Container}.
     */
    protected int getContainerOptimalRowCount(Container container)
    {
        int rows = bandIncrement;
        while ((getContainerSizeFromRows(container, rows).getHeight() < container.getHeight()) &&
               (rows < container.getComponentCount()))
        {
            rows += bandIncrement;
        }
        return Math.max(bandIncrement, rows - bandIncrement);
    }

    /**
     * Calculates the minimum size dimensions for the specified container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        return preferredLayoutSize(parent);
    }

    /**
     * Calculates the preferred size dimensions for the specified container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        switch (boundary)
        {
            case HORIZONTAL:
                return getContainerSizeFromColumns(parent, getContainerOptimalColumnCount(parent));
            case VERTICAL:
                return getContainerSizeFromRows(parent, getContainerOptimalRowCount(parent));
            default:
                throw new IllegalStateException("Unrecognized boundary: " + boundary);
        }
    }

    /**
     * Ignored.
     *
     * @param comp Ignored.
     */
    public void removeLayoutComponent(Component comp)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
