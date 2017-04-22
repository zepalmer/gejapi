package orioni.jz.awt.swing.table;

import orioni.jz.awt.listener.ExecuteMethodActionListener;
import orioni.jz.util.Utilities;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This cell editor is designed to allow the user to edit the filename stored in a given cell.  This is accomplished
 * either by entering text into the text field provided or pressing the button to the right of the text field.
 * Pressing the button allows the user to select a file from a JFileChooser instead of typing the filename manually.
 * <P>
 * This class also acts as a <code>TableCellRenderer</code> for the same content.
 * @author Zachary Palmer
 */
public class TableFilenameCellEditor implements TableCellEditor, TableCellRenderer
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The component used to edit table filenames. */
    protected FilenameEditingComponent editingComponent;
    /** The component used for display purposes.  This component is used so as not to disrupt an edit in action. */
    protected FilenameEditingComponent displayComponent;
    /** The value being represented by this editor. */
    protected Object value;

    /** The Set containing all CellEditorListener objects. */
    protected Set<CellEditorListener> cellEditorListeners;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param fileChooser The JFileChooser to use when selecting a file.
     */
    public TableFilenameCellEditor(JFileChooser fileChooser)
    {
        super();
        editingComponent = new FilenameEditingComponent(this,fileChooser);
        displayComponent = new FilenameEditingComponent(this,fileChooser);
        value = null;

        cellEditorListeners = new HashSet<CellEditorListener>();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Starts the editing of a table cell.  This involves setting the initial value of this cell editor
     * from the contents of the table's model (which are provided by the parameter <code>value</code>) and
     * preparing the editing component for rendering.  The editing component is distinct from the component
     * used to render unfocused cells to prevent damage to data during editing.
     * @param table The JTable for which this editing is being performed.
     * @param value The initial value of the editor at the beginning of the edit.
     * @param isSelected Whether or not the editing component should be highlighted.
     * @param row The row of the cell being edited.
     * @param column The column of the cell being edited.
     * @return The Component to use while editing the cell.
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column)
    {
        this.value = (value==null)?"":value;
        editingComponent.setFieldText(this.value.toString());
        if (isSelected)
        {
            editingComponent.setForeground(table.getSelectionForeground());
            editingComponent.setBackground(table.getSelectionBackground());
        } else
        {
            editingComponent.setForeground(table.getForeground());
            editingComponent.setBackground(table.getBackground());
        }
        editingComponent.setEnabled(table.isEnabled());
        return editingComponent;
    }

    /**
     * Returns the value contained in the editor.
     * @return The current value of the editor's text field.
     */
    public Object getCellEditorValue()
    {
        return value;
    }

    /**
     * Determines whether or not a cell's editing can be started using the given event.
     * @param e The <code>EventObject</code> representing the event being verified for authority to begin editing on
     *          the current cell using this editor.
     * @return <code>true</code> if editing should begin on the cell using that event; <code>false</code> otherwise.
     * @see #shouldSelectCell
     */
    public boolean isCellEditable(EventObject e)
    {
        return ((e instanceof MouseEvent) &&
                (((MouseEvent)(e)).getClickCount()>=1));
    }

    /**
     * Determines whether or not a cell will be selected when editing starts.  For this cell editor,
     * the editing cell should always be selected to provide the text field with focus.
     * @param e The <code>EventObject</code> that will be used to start the editing.
     * @return <code>true</code> if this cell should be selected when editing starts; </code>false</code>
     *        otherwise.  This method always returns <code>true</code>.
     * @see #isCellEditable
     */
    public boolean shouldSelectCell(EventObject e)
    {
        return true;
    }

    /**
     * Instructs the cell editor to stop editing the current cell and to accept
     * any partially edited value.  This instruction may or may not be obeyed
     * by the cell editor; this is determined after the fact by the return value
     * of the function.  This method effectively sets the value of
     * <code>getCellEditorValue</code> to the value currently in the editor.
     * <P>
     * This method must notify all currently registered CellEditorListeners of this
     * event.
     * @return <code>true</code> if editing was stopped; <code>false</code> if
     *         some element of the editor cannot stop editing due to
     *         invalid content.  This method will always return <code>true</code>.
     */
    public boolean stopCellEditing()
    {
        value = editingComponent.getFieldText();
        // Notify all listeners
        Iterator iterator = cellEditorListeners.iterator();
        ChangeEvent e = null;
        while (iterator.hasNext())
        {
            if (e==null) e = new ChangeEvent(this);
            ((CellEditorListener)(iterator.next())).editingStopped(e);
        }
        // Return success
        return true;
    }

    /**
     * Tell the editor to cancel editing and not accept any partially
     * edited value.  This method discards the current state of the
     * editor and does not affect the return value of
     * <code>getCellEditorValue</code>.
     * <P>
     * This method must notify all currently registered CellEditorListeners of this
     * event.
     */
    public void cancelCellEditing()
    {
        // Notify all listeners
        Iterator iterator = cellEditorListeners.iterator();
        ChangeEvent e = null;
        while (iterator.hasNext())
        {
            if (e==null) e = new ChangeEvent(this);
            ((CellEditorListener)(iterator.next())).editingCanceled(e);
        }
    }

    /**
     * Add a listener to the list that's notified when the editor starts,
     * stops, or cancels editing.
     * @param l The <code>CellEditorListener</code> to add to this
     *          <code>CellEditor</code>'s notification list.
     */
    public void addCellEditorListener(CellEditorListener l)
    {
        cellEditorListeners.add(l);
    }

    /**
     * Removes a listener from the notification list.
     * @param l The <code>CellEditorListener</code> to remove from this
     *          <code>CellEditor</code>'s notification list.
     */
    public void removeCellEditorListener(CellEditorListener l)
    {
        cellEditorListeners.remove(l);
    }

    /**
     * Returns a Component used to draw a cell in a JTable using the given value.  This
     * Component is kept distinct from the editing component to prevent damage to data
     * during editing.
     * @param table The JTable in which the cell-to-render resides.
     * @param value The value to be rendered in this rendering component.
     * @param isSelected Whether or not the rendering component should be rendered as highlighted.
     * @param hasFocus Whether or not the rendering component is considered to have focus.
     * @param row The row of the cell in the JTable which is being rendered by this component.
     * @param column The column of the cell in the JTable which is being rendered by this
     *               component.
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        displayComponent.setFieldText((value==null)?"":value.toString());
        if (isSelected)
        {
            displayComponent.setForeground(table.getSelectionForeground());
            displayComponent.setBackground(table.getSelectionBackground());
        } else
        {
            displayComponent.setForeground(table.getForeground());
            displayComponent.setBackground(table.getBackground());
        }
        displayComponent.setEnabled(table.isEnabled());
        return displayComponent;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class represents a single JPanel.  This panel contains a JTextField and a JButton, in that order,
     * displayed in BorderLayout.  The JTextField is a CENTER oriented value; the JButton is an EAST oriented value.
     * <P>
     * When the <code>JButton</code> is pressed, a JFileChooser dialog is opened.  If the selection from this
     * JFileChooser is not cancelled, it becomes the new text of the JTextField.
     */
    public class FilenameEditingComponent extends JPanel
    {
        /** The JTextField containing editable text. */
        protected JTextField field;
        /** The JButton which opens the JFileChooser. */
        protected JButton button;
        /** The JFileChooser which allows the selection of a new file. */
        protected JFileChooser fileChooser;
        /** The CellEditor that needs input from this component. */
        protected CellEditor master;

        /**
         * General constructor.
         * @param master The CellEditor that needs input from this component.
         * @param fileChooser The JFileChooser used to select files when the JButton is pressed.
         */
        public FilenameEditingComponent(CellEditor master, JFileChooser fileChooser)
        {
            this.master = master;
            field = new JTextField();
            button = new JButton("...");
            this.fileChooser = fileChooser;

            // Arrange component

            setLayout(new BorderLayout(0,0));
            add(field, BorderLayout.CENTER);
            add(button, BorderLayout.EAST);

            // Add listener

            button.addActionListener(new ExecuteMethodActionListener(this,"processFileChooser",
                    Utilities.PARAMS_NONE));
            field.addActionListener(new ExecuteMethodActionListener(this.master,"stopCellEditing",
                    Utilities.PARAMS_NONE));
        }

        /**
         * Prompts the user to select a file.  If a file is selected, its pathname (abstract or absolute) becomes
         * the new text of the JTextField contained within this component.
         */
        public void processFileChooser()
        {
            switch (fileChooser.showOpenDialog(this))
            {
                case JFileChooser.CANCEL_OPTION:
                case JFileChooser.ERROR_OPTION:
                    master.cancelCellEditing();
                    return;
                case JFileChooser.APPROVE_OPTION:
                    break;
                default:
                    return;
            }

            // Use the provided result

            field.setText(fileChooser.getSelectedFile().getPath());
            master.stopCellEditing();
        }

        /**
         * Retrieves the text value contained in the text field of this component.
         * @return The text value contained in the text field of this component.
         */
        public String getFieldText()
        {
            return field.getText();
        }

        /**
         * Sets the text value contained in the text field of this component.
         * @param text The new value for this component's text field to contain.
         */
        public void setFieldText(String text)
        {
            field.setText(text);
        }
    }

}

// END OF FILE //