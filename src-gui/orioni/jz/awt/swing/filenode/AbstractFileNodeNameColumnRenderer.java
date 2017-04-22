package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * This {@link TableCellRenderer} is used to render instances of classes which implement the {@link
 * orioni.jz.io.files.abstractnode.AbstractFileNode}. If a {@link JTable} which is using a {@link
 * AbstractFileNodeTableModel} as its model has this renderer associated with the class {@link
 * orioni.jz.io.files.abstractnode.AbstractFileNode}, names of files will appear with an icon immediately to their left.
 * interface.
 */
public class AbstractFileNodeNameColumnRenderer extends JPanel implements TableCellRenderer
{
    /**
     * A {@link javax.swing.border.Border} which is used when no focus is owned.
     */
    protected static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    /**
     * The {@link JLabel} which contains the icon for this {@link AbstractFileNodeNameColumnRenderer}.
     */
    protected JLabel iconLabel;
    /**
     * The {@link JLabel} which contains the text for this {@link AbstractFileNodeNameColumnRenderer}.
     */
    protected JLabel textLabel;

    /**
     * The overriding {@link Color} for the foreground, in the event that this renderer's color has been set.
     */
    protected Color overrideForeground;
    /**
     * The overriding {@link Color} for the background, in the event that this renderer's color has been set.
     */
    protected Color overrideBackground;

    /**
     * The source from which icons are retrieved for the nodes.
     */
    protected AbstractFileNodeIconSource iconSource;

    /**
     * Skeleton constructor.  Assumes a {@link DefaultAbstractFileNodeIconSource}.
     */
    public AbstractFileNodeNameColumnRenderer()
    {
        this(DefaultAbstractFileNodeIconSource.SINGLETON);
    }

    /**
     * General constructor.
     *
     * @param iconSource The source from which icons are retrieved for the nodes.
     */
    public AbstractFileNodeNameColumnRenderer(AbstractFileNodeIconSource iconSource)
    {
        super(new BorderLayout(2, 2));
        this.iconSource = iconSource;
        iconLabel = new JLabel();
        textLabel = new JLabel();
        this.add(iconLabel, BorderLayout.WEST);
        this.add(textLabel, BorderLayout.CENTER);
        overrideBackground = null;
        overrideForeground = null;
    }

    /**
     * Overrides {@link JPanel#setForeground(Color)} to assign the foreground color to the specified color.
     *
     * @param color The new foreground color.
     */
    public void setForeground(Color color)
    {
        super.setForeground(color);
        overrideForeground = color;
    }

    /**
     * Overrides {@link JPanel#setBackground(Color)} to assign the Background color to the speciBied color.
     *
     * @param color The new Background color.
     */
    public void setBackground(Color color)
    {
        super.setBackground(color);
        overrideBackground = color;
    }

    /**
     * Notification from the {@link UIManager} that the look and feel has changed.
     */
    public void updateUI()
    {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    /**
     * Returns the component used for drawing the cell.  This method is used to configure the renderer appropriately
     * before drawing.
     *
     * @param table      The <code>JTable</code> that is asking the renderer to draw; can be <code>null</code>.
     * @param value      The value of the cell to be rendered.  It is up to the specific renderer to interpret and draw
     *                   the value.  <code>null</code> is a valid value.
     * @param isSelected <code>true</code> if the cell is to be rendered with the selection highlighted; otherwise
     *                   <code>false</code>
     * @param hasFocus   If <code>true</code>, render cell appropriately.  For example, put a special border on the
     *                   cell, if the cell can be edited, render in the color used to indicate editing.
     * @param row        The row index of the cell being drawn.  When drawing the header, the value of <code>row</code>
     *                   is <code>-1</code>.
     * @param column     The column index of the cell being drawn.
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        if (value instanceof AbstractFileNode)
        {
            AbstractFileNode dataSource = (AbstractFileNode) (value);
            iconLabel.setIcon(iconSource.getAbstractNodeIcon(dataSource));
            textLabel.setText(dataSource.getName());
        } else
        {
            iconLabel.setIcon(null);
            textLabel.setText(value.toString());
        }
        if (isSelected)
        {
            iconLabel.setForeground(table.getSelectionForeground());
            textLabel.setForeground(table.getSelectionForeground());
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
            iconLabel.setBackground(table.getSelectionBackground());
            textLabel.setBackground(table.getSelectionBackground());
        } else
        {
            if (overrideForeground == null)
            {
                iconLabel.setForeground(table.getForeground());
                textLabel.setForeground(table.getForeground());
                super.setForeground(table.getForeground());
            } else
            {
                iconLabel.setForeground(overrideForeground);
                textLabel.setForeground(overrideForeground);
                super.setForeground(overrideForeground);
            }
            if (overrideBackground == null)
            {
                iconLabel.setBackground(table.getBackground());
                textLabel.setBackground(table.getBackground());
                super.setBackground(table.getBackground());
            } else
            {
                iconLabel.setBackground(overrideBackground);
                textLabel.setBackground(overrideBackground);
                super.setBackground(overrideBackground);
            }
        }

        textLabel.setFont(table.getFont());

        // Following code borrowed from the DefaultTableCellRenderer (J2SDK v1.4.1)
        if (hasFocus)
        {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column))
            {
                super.setForeground(UIManager.getColor("Table.focusCellForeground"));
                super.setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else
        {
            this.setBorder(NO_FOCUS_BORDER);
        }
        // Borrowed code ends here

        return this;
    }
}