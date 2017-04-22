package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * This implementation of {@link TableModel} bases itself on the contents of a specific directory.  It maintains a
 * specific, static set of columns of data about the contents of a directory and bases its information on the
 * {@link orioni.jz.io.files.abstractnode.AbstractFileNode} object provided.  An implementation of the {@link orioni.jz.io.files.abstractnode.AbstractFileNode} interface exists for the
 * Java {@link java.io.File} class; other implementations are necessary to display filesystems represented by other
 * container objects (such as {@link orioni.jz.net.ftp.control.FtpFile}).
 * <P>
 * This model also supports sorting by allowing the user to click on the header of a column for sorting requests.
 * Clicking on the same column a second time reverses the order of sorting; new columns to be sorted are always
 * sorted in ascending order first.  If a client wishes to support this feature, it must add the {@link MouseListener}
 * provided by the {@link AbstractFileNodeTableModel#getSortingListener} method to its
 * <b>{@link javax.swing.table.JTableHeader}</b> (and not its {@link JTable}.
 *
 * @author Zachary Palmer
 */
public class AbstractFileNodeTableModel implements TableModel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /** The constant list of column names for this model. */
    protected static final String[] COLUMN_NAMES = new String[]{"Name", "Size", "Permissions"};

    /** The constant indicating the column containing the name of the file. */
    protected static final int COLUMN_INDEX_NAME = 0;
    /** The constant indicating the column containing the size of the file. */
    protected static final int COLUMN_INDEX_SIZE = 1;
    /** The constant indicating the column containing the permissions of the file. */
    protected static final int COLUMN_INDEX_PERMISSIONS = 2;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link orioni.jz.io.files.abstractnode.AbstractFileNode} object representing the directory on which the contents of this table are based. */
    protected AbstractFileNode directory;
    /** The set of {@link TableModelListener} objects to be notified when this model is changed. */
    protected Set<TableModelListener> listeners;
    /** The array of {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects from which this table gets its information. */
    protected AbstractFileNode[] data;

    /** The column by which the table is currently being sorted. */
    protected int sortingColumn;
    /** <code>true</code> if the sort is ascending; <code>false</code> if it is descending. */
    protected boolean sortingAscending;
    /** <code>true</code> if the sort should assume that directories are of less importance than files;
     *  <code>false</code> otherwise. */
    protected boolean directoriesEvaluateLess;
    /** <code>true</code> if the files are to be sorted in a case-sensitive manner; <code>false</code> otherwise. */
    protected boolean namesCaseSensitive;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param directory The initial directory on which the contents of this table are based.
     * @param directoriesEvaluateLess <code>true</code> if the sort should assume that directories are of less
     *                                  importance than files; <code>false</code> otherwise.
     * @param namesCaseSensitive <code>true</code> if the files are to be sorted in a case-sensitive manner;
     *                             <code>false</code> otherwise.
     */
    public AbstractFileNodeTableModel(AbstractFileNode directory, boolean directoriesEvaluateLess,
                                      boolean namesCaseSensitive)
    {
        listeners = new HashSet<TableModelListener>();
        data = new AbstractFileNode[0];
        sortingColumn = COLUMN_INDEX_NAME;
        sortingAscending = true;
        this.directoriesEvaluateLess = directoriesEvaluateLess;
        this.namesCaseSensitive = namesCaseSensitive;
        setDirectory(directory);
    }

// NON-STATIC METHODS : OWNING JTABLE SUPPORT ////////////////////////////////////

    /**
     * Retrieves the {@link MouseListener} which allows a program to provide the user with sort-by-column functionality.
     * @param table The {@link JTable} to which the listener will be added.  This parameter must be correct (and
     *              non-<code>null</code>) to properly create and operate the {@link MouseListener}.
     * @return A {@link MouseListener} to be added to the {@link JTable} for which this object is a model.
     */
    public MouseListener getSortingListener(JTable table)
    {
        final AbstractFileNodeTableModel finalModel = this;
        final JTable finalTable = table;
        return new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    int column = finalTable.convertColumnIndexToModel(
                            finalTable.getColumnModel().getColumnIndexAtX(e.getX()));
                    if (column != -1)
                    {
                        finalModel.setSortingScheme(column);
                        finalModel.refresh();
                    }
                }
            }
        };
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets this {@link AbstractFileNodeTableModel} to view the specified directory.  If the specified {@link java.io.File}
     * object does not represent an existing directory, no files are shown.
     * @param directory The {@link java.io.File} object representing the directory to be shown.
     */
    public void setDirectory(AbstractFileNode directory)
    {
        AbstractFileNode[] children = directory.getChildren();
        if (directory.getParent() != null)
        {
            AbstractFileNode[] oldChildren = children;
            children = new AbstractFileNode[oldChildren.length + 1];
            System.arraycopy(oldChildren, 0, children, 0, oldChildren.length);
            children[children.length - 1] = new ParentFileNode(directory.getParent());
        }
        // Sort by current sorting scheme
        if (directoriesEvaluateLess)
        {
            List<AbstractFileNode> directoriesList = new ArrayList<AbstractFileNode>();
            List<AbstractFileNode> filesList = new ArrayList<AbstractFileNode>();
            for (final AbstractFileNode child : children)
            {
                if (child.isDirectory()) directoriesList.add(child); else filesList.add(child);
            }
            AbstractFileNode[] directories = directoriesList.toArray(new AbstractFileNode[0]);
            AbstractFileNode[] files = filesList.toArray(new AbstractFileNode[0]);
            doSort(directories);
            doSort(files);
            int index = 0;
            for (final AbstractFileNode child : directories)
            {
                children[index++] = child;
            }
            for (final AbstractFileNode child : files)
            {
                children[index++] = child;
            }
        } else
        {
            doSort(children);
        }
        // Establish table data and notify listeners.
        data = children;
        fireTableModelEvent(new TableModelEvent(this));
        this.directory = directory;
    }

    /**
     * Performs the sorting operation for the {@link AbstractFileNodeTableModel#setDirectory(orioni.jz.io.files.abstractnode.AbstractFileNode)} method.
     * @param array The {@link orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> to sort.
     */
    private void doSort(AbstractFileNode[] array)
    {
        switch (sortingColumn)
        {
            case COLUMN_INDEX_NAME:
                Arrays.sort(array, AbstractFileNodeNameComparator.SINGLETON_INSENSITIVE);
                break;
            case COLUMN_INDEX_SIZE:
                Arrays.sort(array, AbstractFileNodeSizeComparator.SINGLETON);
                break;
            case COLUMN_INDEX_PERMISSIONS:
                Arrays.sort(array, AbstractFileNodePermissionComparator.SINGLETON);
                break;
        }
        // Invert sorted order if necessary
        if (!sortingAscending)
        {
            for (int i = 0; i < array.length / 2; i++)
            {
                AbstractFileNode temp = array[i];
                array[i] = array[array.length - i - 1];
                array[array.length - i - 1] = temp;
            }
        }
    }

    /**
     * Sets the current sorting scheme to sort by a specific column.  This column should be represented as one of
     * the <code>COLUMN_INDEX_XXXX</code> constants defined in this class to ensure proper sorting behavior.
     * <P>
     * Calling this method twice in a row with the same index causes its sorting to be reversed over that index.
     * <P>
     * Please note that the sort will not actually occur until the next time either
     * {@link AbstractFileNodeTableModel#setDirectory(orioni.jz.io.files.abstractnode.AbstractFileNode)} or
     * {@link AbstractFileNodeTableModel#refresh()} is called.
     *
     * @param column The <code>COLUMN_INDEX_XXXX</code> indicating the column by which to sort.
     */
    public void setSortingScheme(int column)
    {
        if (column == sortingColumn)
        {
            setSortingScheme(column, !sortingAscending);
        } else
        {
            setSortingScheme(column, true);
        }
    }

    /**
     * Changes whether or not directories take less priority than files or not.  The newly set value is not applied to
     * the order of the data in the model until either {@link AbstractFileNodeTableModel#setDirectory(orioni.jz.io.files.abstractnode.AbstractFileNode)} or
     * {@link AbstractFileNodeTableModel#refresh()} is called.
     *
     * @param directoriesEvaluateLess <code>true</code> if the sort should assume that directories are of less
     *                                  importance than files; <code>false</code> otherwise.
     */
    public void setDirectoriesEvaluateLess(boolean directoriesEvaluateLess)
    {
        this.directoriesEvaluateLess = directoriesEvaluateLess;
    }

    /**
     * Sets the current sorting scheme to sort by a specific column and direction.  The column should be represented
     * as one of the <code>COLUMN_INDEX_XXXX</code> constants defined in this class to ensure proper sorting behavior.
     * <P>
     * Please note that the sort will not actually occur until the next time either
     * {@link AbstractFileNodeTableModel#setDirectory(orioni.jz.io.files.abstractnode.AbstractFileNode)} or
     * {@link AbstractFileNodeTableModel#refresh()} is called.
     *
     * @param column The <code>COLUMN_INDEX_XXXX</code> indicating the column by which to sort.
     * @param ascending <code>true</code> if sorting is to occur in ascending order; <code>false</code> if it is to
     *                  occur in descending order.
     */
    public void setSortingScheme(int column, boolean ascending)
    {
        sortingColumn = column;
        sortingAscending = ascending;
    }

    /**
     * Retrieves the {@link orioni.jz.io.files.abstractnode.AbstractFileNode} at a specified row.
     * @param row The index of the row for which a {@link orioni.jz.io.files.abstractnode.AbstractFileNode} should be retrieved.
     * @return The specified {@link orioni.jz.io.files.abstractnode.AbstractFileNode}.
     */
    public AbstractFileNode getDataSourceAtRow(int row)
    {
        return data[row];
    }

    /**
     * Refreshes the contents of this table.
     */
    public void refresh()
    {
        setDirectory(directory);
    }

    /**
     * Launches a {@link TableModelEvent}, distributing it to all registered {@link TableModelListener}s.
     * @param event The {@link TableModelEvent} to launch.
     */
    protected void fireTableModelEvent(TableModelEvent event)
    {
        for (Object listener : listeners)
        {
            ((TableModelListener) (listener)).tableChanged(event);
        }
    }

    /**
     * Adds a listener to the list that is notified each time a change to the data model occurs.
     * @param listener The {@link TableModelListener} to be added.
     */
    public void addTableModelListener(TableModelListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case COLUMN_INDEX_NAME:
                return AbstractFileNode.class;
            default:
                return String.class;
        }
    }

    /**
     * Returns the number of columns in the model. A {@link JTable} uses this method to determine how many columns
     * it should create and display by default.
     *
     * @return The number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount()
    {
        return COLUMN_NAMES.length;
    }

    /**
     * Returns the name of the column at <code>index</code>.  This is used to initialize the table's column header
     * name.  Note: this name does not need to be unique; two columns in a table can have the same name.
     *
     * @param index	The index of the column.
     * @return The name of the column.
     */
    public String getColumnName(int index)
    {
        return COLUMN_NAMES[index];
    }

    /**
     * Returns the number of rows in the model. A {@link JTable} uses this method to determine how many rows it
     * should display.  This method should be quick, as it is called frequently during rendering.
     *
     * @return The number of rows in the model.
     * @see #getColumnCount
     */
    public int getRowCount()
    {
        return data.length;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and <code>rowIndex</code>.
     * @param rowIndex The row whose value is to be queried.
     * @param columnIndex The column whose value is to be queried.
     * @return The value at the specified cell.
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case COLUMN_INDEX_NAME:
                return data[rowIndex];//.getName();
            case COLUMN_INDEX_SIZE:
                long size = data[rowIndex].getSize();
                if (size < 1024) return size + " bytes";
                if (size < 1048576) return (((double) (size * 100 / 1024) + 99) / 100) + " Kb";
                if (size < 1073741824) return (((double) (size * 100 / 1048576) + 99) / 100) + " Mb";
                return (((double) (size * 100 / 1073741824) + 99) / 100) + " Gb";
            case COLUMN_INDEX_PERMISSIONS:
                switch (data[rowIndex].getReadPermission())
                {
                    case GRANTED:
                        switch (data[rowIndex].getWritePermission())
                        {
                            case GRANTED:
                                return "Read + Write";
                            case DENIED:
                                return "Read Only";
                            case UNKNOWN:
                                return "Read; Write Unknown";
                        }
                        break;
                    case DENIED:
                        switch (data[rowIndex].getWritePermission())
                        {
                            case GRANTED:
                                return "Write Only";
                            case DENIED:
                                return "No Permissions";
                            case UNKNOWN:
                                return "Write Unknown";
                        }
                        break;
                    case UNKNOWN:
                        switch (data[rowIndex].getWritePermission())
                        {
                            case GRANTED:
                                return "Write; Read Unknown";
                            case DENIED:
                                return "Read Unknown";
                            case UNKNOWN:
                                return "Permissions Unknown";
                        }
                        break;
                }
        }
        throw new ArrayIndexOutOfBoundsException("Invalid column index: " + columnIndex);
    }

    /**
     * Always returns <code>false</code>.
     *
     * @param rowIndex The row whose value to be queried.
     * @param columnIndex The column whose value to be queried.
     * @return <code>false</code>, always.
     * @see #setValueAt
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /**
     * Removes a listener from the list that is notified each time a change to the data model occurs.
     *
     * @param listener The {@link TableModelListener} to remove.
     */
    public void removeTableModelListener(TableModelListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to <code>aValue</code>.
     * This implementation does nothing, since this model is immutable except through changes to the filesystem.
     *
     * @param aValue The new value.
     * @param rowIndex The row whose value is to be changed.
     * @param columnIndex The column whose value is to be changed.
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}