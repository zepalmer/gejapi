package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

import javax.swing.*;

/**
 * This listener interface should be implemented by those who wish to receive notice whenever a component using an
 * {@link AbstractFileNodeTransferHandler} receives {@link orioni.jz.io.files.abstractnode.AbstractFileNode}s.  The
 * {@link AbstractFileNodeReceiptListener#abstractFileNodesReceived(JComponent,orioni.jz.io.files.abstractnode.AbstractFileNode[])} method is
 * implemented to handle this event.
 *
 * @author Zachary Palmer
 */
public interface AbstractFileNodeReceiptListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/**
	 * This method is called whenever a {@link JComponent} using the {@link AbstractFileNodeTransferHandler} with which
	 * this listener is registered receives a group of {@link orioni.jz.io.files.abstractnode.AbstractFileNode}s.
	 * @param target The {@link JComponent} receiving the nodes.
	 * @param nodes The {@link orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> containing the received nodes.
	 */
	public void abstractFileNodesReceived(JComponent target, AbstractFileNode[] nodes);

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

}