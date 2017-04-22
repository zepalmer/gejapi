package orioni.jz.awt.swing.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * This {@link TableCellRenderer} displays a progress bar based upon the data provided.  The data should be a {@link
 * Double} between <code>0.0</code> and <code>1.0</code>, inclusive.  Values outside of this range result in an empty or
 * full progress bar, respectively.  The value represents the percentage of the progress bar that is filled.
 *
 * @author Zachary Palmer
 */
public class ProgressBarTableCellRenderer extends JProgressBar implements TableCellRenderer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The precision for this renderer.
     */
    protected static final int PRECISION = 10000;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public ProgressBarTableCellRenderer()
    {
        super(JProgressBar.HORIZONTAL, 0, PRECISION);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the component used for drawing the cell.  This method is used to configure the renderer appropriately
     * before drawing.
     *
     * @param	table		the <code>JTable</code> that is asking the renderer to draw; can be <code>null</code>
     * @param	value		the value of the cell to be rendered.  It is up to the specific renderer to interpret and draw the
     * value.  For example, if <code>value</code> is the string "true", it could be rendered as a string or it could be
     * rendered as a check box that is checked.  <code>null</code> is a valid value
     * @param	selected	true if the cell is to be rendered with the selection highlighted; otherwise false
     * @param	hasFocus	if true, render cell appropriately.  For example, put a special border on the cell, if the cell
     * can be edited, render in the color used to indicate editing
     * @param	row	 the row index of the cell being drawn.  When drawing the header, the value of <code>row</code> is -1
     * @param	column	 the column index of the cell being drawn
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
                                                   int row, int column)
    {
        this.setForeground(selected ? table.getSelectionForeground() : table.getForeground());
        this.setBackground(selected ? table.getSelectionBackground() : table.getBackground());
        if (value instanceof Double)
        {
            double ratio = (Double) value;
            if (ratio < 0)
            {
                setValue(0);
            } else if (ratio > 1)
            {
                setValue(PRECISION);
            } else
            {
                setValue((int) (ratio * PRECISION));
            }
        } else
        {
            setValue(0);
        }
        return this;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE