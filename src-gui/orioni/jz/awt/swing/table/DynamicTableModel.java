package orioni.jz.awt.swing.table;

import orioni.jz.reflect.ReflectionUtilities;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * This class extends the <code>AbstractTableModel</code> class to allow for the addition and removal of columns from
 * the model.
 * <p/>
 * <b>This model is not threadsafe!</b>  If multiple threads access this model at one time, it may be corrupted.
 * Synchronization must be performed by an outside source.
 * @author Zachary Palmer
 * @see TableModel
 */
public class DynamicTableModel extends AbstractTableModel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The number of rows in this model. */
    protected int rowCount;
    /** The number of columns in this model. */
    protected int columnCount;
    /** The ArrayList object containing the table model data.  Each object in this ArrayList is an ArrayList of itself;
     *  each element in that ArrayList contains an Object representing the contents of a cell. */
    protected ArrayList data;
    /** The ArrayList containing the names of the columns. */
    protected ArrayList columnNames;
    /** The ArrayList containing Boolean objects which represent whether or not a given column is editable. */
    protected ArrayList columnEditable;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public DynamicTableModel()
    {
        rowCount = 0;
        columnCount = 0;
        data = new ArrayList();
        columnNames = new ArrayList();
        columnEditable = new ArrayList();
    }

// NON-STATIC METHODS : NEW FUNCTIONALITY ////////////////////////////////////////

    /**
     * Adds a column to this model with the given name.  The new column is assigned the first available positive index.
     * The caller is guaranteed that no column currently in the model has this index; other columns, however, may have
     * had this index at some point if they have already been removed.  The column is filled with a series of blank
     * strings (<code>""</code>).  It is assumed to be uneditable.
     * @param name The name of the column.
     * @return The model's index of the newly created column.
     */
    public int addColumn(String name)
    {
        Object[] data = new Object[rowCount];
        for (int i=0;i<data.length;i++)
        {
            data[i] = "";
        }
        return addColumn(name,data,false);
    }

    /**
     * Adds a column to this model with the given name.  The new column is assigned the first available positive index.
     * The caller is guaranteed that no column currently in the model has this index; other columns, however, may have
     * had this index at some point if they have already been removed.  The column is filled with a series of blank
     * strings (<code>""</code>).
     * @param name The name of the column.
     * @param editable Whether or not the new column is to be editable.
     * @return The model's index of the newly created column.
     */
    public int addColumn(String name,boolean editable)
    {
        Object[] data = new Object[rowCount];
        for (int i=0;i<data.length;i++)
        {
            data[i] = "";
        }
        return addColumn(name,data,editable);
    }

    /**
     * Adds a column to this model with the given name.  The new column is assigned the first available positive index.
     * The caller is guaranteed that no column currently in the model has this index; other columns, however, may have
     * had this index at some point if they have already been removed.  The column is filled with the data provided.
     * It is assumed to be uneditable.
     * @param name The name of the column.
     * @param data An array of Objects representing the data contained within this new column.
     * @return The model's index of the newly created column.
     * @throws IllegalArgumentException If the length of the data array is not equal to the number of rows in this
     *                                  table.
     */
    public int addColumn(String name,Object[] data)
    {
        return addColumn(name,data,false);
    }

    /**
     * Adds a column to this model with the given name.  The new column is assigned the first available positive index.
     * The caller is guaranteed that no column currently in the model has this index; other columns, however, may have
     * had this index at some point if they have already been removed.  The column is filled with the data provided.
     * @param name The name of the column.
     * @param data An array of Objects representing the data contained within this new column.
     * @param editable Whether or not the new column is to be editable.
     * @return The model's index of the newly created column.
     * @throws IllegalArgumentException If the length of the data array is not equal to the number of rows in this
     *                                  table.
     */
    public int addColumn(String name,Object[] data,boolean editable)
    {
        // Note the column count change.
        columnCount++;
        // Determine target index of new column
        int targetIndex = 0;
        while ((targetIndex <this.data.size()) && (this.data.get(targetIndex)!=null))
        {
            targetIndex++;
        }
        // Add the column
        if (data.length!=rowCount)
            throw new IllegalArgumentException(
                    "Data array size ("+data.length+") must match row count ("+rowCount +").");
        ArrayList column = new ArrayList();
        if (targetIndex >=this.data.size())
        {
            columnNames.add(name);
            columnEditable.add(editable);
            this.data.add(column);
        } else
        {
            columnNames.set(targetIndex,name);
            columnEditable.set(targetIndex,editable);
            this.data.set(targetIndex,column);
        }
        for (final Object element : data)
        {
            column.add(element);
        }
        return targetIndex;
    }

    /**
     * "Removes" the column with the given index from this table model entirely.  This method effectively removes the
     * data from the model entirely.  The column numbers of other columns, however, are unaffected; this column number
     * is reserved for later use if another column is added.
     * @param index The index of the column to remove.
     * @throws IndexOutOfBoundsException If the column index is less than <code>0</code> or greater than the number of
     *                                   columns in the table minus 1.  This exception puts the model into an unknown
     *                                   state.
     */
    public void removeColumn(int index)
    {
        // Note the column count change
        columnCount--;
        // Now remove the column
        try
        {
            columnEditable.set(index,null);
            columnNames.set(index,null);
            data.set(index,null);
        } catch (IndexOutOfBoundsException oob)
        {
            throw new IndexOutOfBoundsException("Column index "+index+" out of range");
        }
        while ((data.size()>0) && (data.get(data.size()-1)==null))
        {
            columnEditable.remove(data.size()-1);
            columnNames.remove(data.size()-1);
            data.remove(data.size()-1);
        }
    }

    /**
     * Adds a row with the given index to this table model.  The new row is assigned the stated index and the index of
     * all rows previously of that index or higher is increased by one.  The new row is created with new, blank Strings.
     * <code>JTable</code> objects which are viewing this model will need to have <code>revalidate</code> called to
     * ensure that changes are reflected in their display.
     * @param index The index of the new row.
     */
    public void addRow(int index)
    {
        // Add the row
        Object[] rowData = new Object[columnCount];
        for (int i=0;i<rowData.length;i++)
        {
            rowData[i] = "";
        }
        addRow(index,rowData);
    }

    /**
     * Adds a row with the given index and contents to this table model.  The new row is assigned the stated index and
     * the index of all rows previously of that index or higher is increased by one.  <code>JTable</code> objects which
     * are viewing this model will need to have <code>revalidate</code> called to ensure that changes are reflected in
     * their display.
     * @param index The index of the new row.
     * @param data An array of Objects representing the data for the new row.
     * @throws IndexOutOfBoundsException If the row index is less than <code>0</code> or greater than the number of rows
     *                                   in the table minus 1.
     * @throws IllegalArgumentException If the length of the data array is not equal to the number of columns in this
     *                                  table model.
     */
    public void addRow(int index,Object[] data)
            throws IndexOutOfBoundsException,
            IllegalArgumentException
    {
        if (data.length!=columnCount)
        {
            throw new IllegalArgumentException("Length of data array ("+data.length+") not equal to column "+
                    "count ("+columnCount);
        }
        // Note the row count change
        rowCount++;
        // Add the row
        int i=0;
        int dataIndex = 0;
        while (i<columnCount)
        {
            ArrayList column;
            do
            {
                column = ((ArrayList)(this.data.get(i++)));
            } while (column==null);
            column.add(index,data[dataIndex++]);
        }
        this.fireTableRowsInserted(index,index);
    }

    /**
     * Removes a row with the given index from this table model entirely.  <code>JTable</code> objects which are
     * viewing this model will need to have <code>revalidate</code> called to ensure that changes are reflected in
     * their display.
     * @param index The index of the row to remove.
     * @throws IndexOutOfBoundsException If the row index is less than <code>0</code> or greater than the number of rows
     *                                   in the table minus 1.
     */
    public void removeRow(int index)
            throws IndexOutOfBoundsException
    {
        // Note the row count change
        rowCount--;
        // Remove the row
        for (int i=0;i<data.size();i++)
        {
            ArrayList column = ((ArrayList)(data.get(i)));
            if (column!=null)
            {
                column.remove(index);
            }
        }
        this.fireTableRowsDeleted(index,index);
    }

    /**
     * Retrieves the indices of all valid columns.  Note that this is not an array between <code>0</code> and
     * <code>m_column_count-1</code>.  Depending on adds and deletes, some of the column numbers may be unused.  Using
     * one of these numbers generates a <code>NullPointerException</code>.  Only columns with the given numbers can
     * be used with this model.
     * @return An int[] containing valid column indices for this model.
     */
    public int[] getValidIndices()
    {
        int[] ret = new int[columnCount];
        int index = 0;
        for (int i=0;i<data.size();i++)
        {
            if (data.get(i)!=null) ret[index++]=i;
        }
        return ret;
    }

    /**
     * Changes the name of a column.
     * @param index The index of a column.
     * @param name The new name for the column.
     * @throws NullPointerException If the value of <code>index</code> is not a valid index as stated by
     *                              <code>getValidIndices</code>.
     * @throws ArrayIndexOutOfBoundsException If the value of <code>index</code> is out of range.
     */
    public void setColumnName(int index,String name)
        throws NullPointerException,
        ArrayIndexOutOfBoundsException
    {
        if (data.get(index)==null) throw new NullPointerException("Invalid index "+index+"in setColumnName");
        columnNames.set(index,name);
    }

// NON-STATIC METHODS : OVERRIDES ////////////////////////////////////////////////

    /**
     * Returns the number of rows in the model. A <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount()
    {
        return rowCount;
    }

    /**
     * Returns the number of columns in the model. A <code>JTable</code> uses this method to determine how many columns
     * it should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount()
    {
        return columnCount;
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used to initialize the table's column
     * header name.  Note: this name does not need to be unique; two columns in a table can have the same name.
     *
     * @param	columnIndex	the index of the column
     * @return  the name of the column
     */
    public String getColumnName(int columnIndex)
    {
        return (String)(columnNames.get(columnIndex));
    }

    /**
     * Returns the most specific superclass for all the cell values in the column.  This is used by the
     * <code>JTable</code> to set up a default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass(int columnIndex)
    {
        int[] indices = getValidIndices();

        ArrayList column = (ArrayList)(data.get(columnIndex));
        if ((column==null) || (column.size()==0)) return Object.class;

        Class goingClass = data.get(indices[0]).getClass();
        for (int i=1;i<indices.length;i++)
        {
            while (!ReflectionUtilities.instanceOf(data.get(indices[i]),goingClass))
            {
                goingClass = goingClass.getSuperclass();
            }
            if (goingClass ==Object.class) return goingClass;
        }
        return goingClass;
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and <code>columnIndex</code> is editable.  Otherwise,
     * <code>setValueAt</code> on the cell will not change the value of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return ((Boolean) (columnEditable.get(columnIndex)));
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return ((ArrayList)(data.get(columnIndex))).get(rowIndex);
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex 	 the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        ArrayList column = (ArrayList)(data.get(columnIndex));
        column.set(rowIndex,aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //