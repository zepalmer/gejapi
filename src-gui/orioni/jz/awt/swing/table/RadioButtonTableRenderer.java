package orioni.jz.awt.swing.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * This {@link TableCellRenderer} implementation is designed to represent a boolean field as a radio button.
 *
 * @author Zachary Palmer
 */
public class RadioButtonTableRenderer extends JRadioButton implements TableCellRenderer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public RadioButtonTableRenderer()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the component used for drawing the cell.  This method is used to configure the renderer appropriately
     * before drawing.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
                                                   int row, int column)
    {
        setSelected(value.equals(Boolean.TRUE));
        setForeground(selected ? table.getSelectionForeground() : table.getForeground());
        setBackground(selected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
