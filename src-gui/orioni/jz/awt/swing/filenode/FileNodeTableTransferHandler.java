package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * This implementation of {@link FileNodeTableTransferHandler} functions specifically with {@link JTable}s
 * which use the {@link AbstractFileNodeTableModel}.
 * @author Zachary Palmer
 */
public class FileNodeTableTransferHandler extends AbstractFileNodeTransferHandler
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Wrapper constructor.
	 * @see AbstractFileNodeTransferHandler#AbstractFileNodeTransferHandler(boolean, boolean)
	 */
	public FileNodeTableTransferHandler(boolean awayOkay, boolean towardsOkay)
	{
		super(awayOkay, towardsOkay);
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Retrieves the {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects from a {@link JTable}.  If the specified {@link JComponent} is not
	 * a {@link JTable} using the {@link AbstractFileNodeTableModel}, this method returns <code>null</code>.
	 *
	 * @param source The {@link JComponent} from which {@link orioni.jz.io.files.abstractnode.AbstractFileNode}s should be retrieved.
	 * @return An {@link orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> or <code>null</code>, as prescribed above.
	 */
	public AbstractFileNode[] getAbstractFileNodes(JComponent source)
	{
		if (!(source instanceof JTable)) return null;
		JTable table = (JTable) source;
		TableModel model = table.getModel();
		if (!(model instanceof AbstractFileNodeTableModel)) return null;
		AbstractFileNodeTableModel infoModel = (AbstractFileNodeTableModel) model;
		int[] selection = table.getSelectedRows();
		AbstractFileNode[] sources = new AbstractFileNode[selection.length];
		for (int i = 0; i < selection.length; i++)
		{
			sources[i] = infoModel.getDataSourceAtRow(selection[i]);
		}
		return sources;
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}