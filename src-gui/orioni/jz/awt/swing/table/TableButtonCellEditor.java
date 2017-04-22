package orioni.jz.awt.swing.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This implementation of {@link TableCellEditor} and {@link TableCellRenderer} is designed to display an {@link AbstractButton}
 * in a table and cause presses within the table to activate that button's
 *
 * @author Zachary Palmer
 */
public class TableButtonCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The currently-used {@link AbstractButton} for the editor (not the renderer. */
    protected AbstractButton editorButton;
    /** The {@link ActionListener} which is used to generate edit-stopping events. */
    protected ActionListener actionListener;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public TableButtonCellEditor()
    {
        super();
        actionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fireEditingStopped();
            }
        };
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns the value contained in the editor.  This is the {@link AbstractButton} currently being used.
     *
     * @return the value contained in the editor
     */
    public Object getCellEditorValue()
    {
        return editorButton;
    }

    /**
     * Sets the current editor button.
     *
     * @param	table		the <code>JTable</code> that is asking the editor to edit; can be <code>null</code>
     * @param	value		The value of the cell to be edited.  This must be an {@link AbstractButton}.
     * @param	isSelected	true if the cell is to be rendered with highlighting
     * @param	row the row of the cell being edited
     * @param	column the column of the cell being edited
     * @return	the component for editing
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        if (editorButton !=null)
        {
            editorButton.removeActionListener(actionListener);
        }
        editorButton = (AbstractButton)value;
        editorButton.addActionListener(actionListener);
        return editorButton;
    }

    /**
     * Returns the component used for drawing the cell.  This method is used to configure the renderer appropriately
     * before drawing.
     *
     * @param	table		the <code>JTable</code> that is asking the renderer to draw; can be <code>null</code>
     * @param	value		The value of the cell to be rendered.  This must be an {@link AbstractButton}.
     * @param	isSelected	true if the cell is to be rendered with the selection highlighted; otherwise false
     * @param	hasFocus	if true, render cell appropriately.  For example, put a special border on the cell, if the cell
     * can be edited, render in the color used to indicate editing
     * @param	row	 the row index of the cell being drawn.  When drawing the header, the value of <code>row</code> is -1
     * @param	column	 the column index of the cell being drawn
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        return (AbstractButton)value;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE