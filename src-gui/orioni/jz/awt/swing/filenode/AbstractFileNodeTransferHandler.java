package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;
import orioni.jz.util.HashMultiMap;
import orioni.jz.util.JointIterator;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This {@link TransferHandler} extension is designed to allow the transfer of {@link
 * orioni.jz.io.files.abstractnode.AbstractFileNode} objects to and from {@link JTable} objects using the {@link
 * AbstractFileNodeTableModel}.  On construction, it should be specified whether or not drags away from or to the table
 * to which this {@link AbstractFileNodeTransferHandler} is attached are allowed.
 * <p/>
 * This implementation is abstract.  Extensions which implement the abstract interface provided by this implementation
 * are designed for specific {@link JComponent}s.
 *
 * @author Zachary Palmer
 */
public abstract class AbstractFileNodeTransferHandler extends TransferHandler
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The {@link DataFlavor} object used to represent {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects
     * as {@link Transferable}.  This {@link DataFlavor} object represents instances of the {@link
     * orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> class.
     */
    public static final DataFlavor DATA_FLAVOR_ABSTRACT_FILE_NODES =
            new DataFlavor(AbstractFileNode[].class, "AbstractFileNode[]");

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * <code>true</code> if dragging {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects <i>from</i> the
     * target table is legal; <code>false</code> otherwise.
     */
    protected boolean awayOkay;
    /**
     * <code>true</code> if dragging {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects <i>towards</i> the
     * target table is legal; <code>false</code> otherwise.
     */
    protected boolean towardsOkay;

    /**
     * The {@link HashSet} containing the {@link AbstractFileNodeReceiptListener}s which should be called whenever any
     * component using this {@link AbstractFileNodeTransferHandler} receives {@link
     * orioni.jz.io.files.abstractnode.AbstractFileNode}s.
     */
    protected Set<AbstractFileNodeReceiptListener> universalListeners;
    /**
     * The {@link HashMultiMap} containing the {@link AbstractFileNodeReceiptListener}s which should be called only when
     * a specific component using this {@link AbstractFileNodeTransferHandler} receives {@link
     * orioni.jz.io.files.abstractnode.AbstractFileNode}s, keyed by the {@link JComponent}s which trigger them.
     */
    protected HashMultiMap<JComponent, AbstractFileNodeReceiptListener> conditionalListeners;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param awayOkay    <code>true</code> if dragging {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects
     *                    <i>from</i> the target table is legal; <code>false</code> otherwise.
     * @param towardsOkay <code>true</code> if dragging {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects
     *                    <i>towards</i> the target table is legal; <code>false</code> otherwise.
     */
    public AbstractFileNodeTransferHandler(boolean awayOkay, boolean towardsOkay)
    {
        super();
        this.awayOkay = awayOkay;
        this.towardsOkay = towardsOkay;
        universalListeners = new HashSet<AbstractFileNodeReceiptListener>();
        conditionalListeners = new HashMultiMap<JComponent, AbstractFileNodeReceiptListener>();
    }

// NON-STATIC ABSTRACT METHODS ///////////////////////////////////////////////////

    /**
     * Retrieves the {@link orioni.jz.io.files.abstractnode.AbstractFileNode} objects from a specific {@link
     * JComponent}.  If the specified {@link JComponent} is not compatible with this implementation of {@link
     * AbstractFileNodeTransferHandler}, this method returns <code>null</code>.
     * <p/>
     * This method is used by the {@link AbstractFileNodeTransferHandler#createTransferable(JComponent)} method.
     *
     * @param source The {@link JComponent} from which {@link orioni.jz.io.files.abstractnode.AbstractFileNode}s should
     *               be retrieved.
     * @return An {@link orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> or <code>null</code>, as
     *         prescribed above.
     */
    public abstract AbstractFileNode[] getAbstractFileNodes(JComponent source);

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds an {@link AbstractFileNodeReceiptListener} to this {@link AbstractFileNodeTransferHandler} unconditionally.
     * That listener will now be notified whenever any {@link JComponent} using this {@link
     * AbstractFileNodeTransferHandler} receives {@link orioni.jz.io.files.abstractnode.AbstractFileNode}s.
     *
     * @param listener The {@link AbstractFileNodeReceiptListener} to add.
     */
    public void addReceiptListener(AbstractFileNodeReceiptListener listener)
    {
        universalListeners.add(listener);
        JComponent key = conditionalListeners.getKeyFor(listener);
        while (key != null)
        {
            conditionalListeners.remove(key, listener);
            key = conditionalListeners.getKeyFor(listener);
        }
    }

    /**
     * Adds an {@link AbstractFileNodeReceiptListener} to this {@link AbstractFileNodeTransferHandler} such that it only
     * be called when the specified {@link JComponent} receives data.
     *
     * @param listener  The {@link AbstractFileNodeReceiptListener} to add.
     * @param component The {@link JComponent} which triggers the listener specified.
     */
    public void addReceiptListener(AbstractFileNodeReceiptListener listener, JComponent component)
    {
        conditionalListeners.put(component, listener);
        universalListeners.remove(listener);
    }

    /**
     * Removes the specified {@link AbstractFileNodeReceiptListener} from this {@link AbstractFileNodeTransferHandler}.
     *
     * @param listener The {@link AbstractFileNodeReceiptListener} to remove.
     */
    public void removeReceiptListener(AbstractFileNodeReceiptListener listener)
    {
        universalListeners.remove(listener);
        JComponent key = conditionalListeners.getKeyFor(listener);
        while (key != null)
        {
            conditionalListeners.remove(key, listener);
            key = conditionalListeners.getKeyFor(listener);
        }
    }

    /**
     * This method does nothing since <code>MOVE</code> is not a supported action of this implementation.
     *
     * @param source Ignored.
     * @param data   Ignored.
     * @param action Ignored.
     */
    protected void exportDone(JComponent source, Transferable data, int action)
    {
    }

    /**
     * Creates a {@link Transferable} to use as the source for a data transfer.  Returns the representation of the data
     * to be transferred, or <code>null</code> if the component's property is <code>null</code>
     *
     * @param source The component holding the data to be transferred.
     * @return The representation of the data to be transferred.
     */
    protected Transferable createTransferable(JComponent source)
    {
        AbstractFileNode[] sources = getAbstractFileNodes(source);
        if (sources == null)
        {
            return null;
        } else
        {
            return new TransferableAbstractFileNodes(sources);
        }
    }

    /**
     * Returns the type of transfer actions supported by the source.  This model only allows <code>COPY</code>
     * operations.
     *
     * @param source Ignored.
     * @return {@link TransferHandler#COPY}.
     */
    public int getSourceActions(JComponent source)
    {
        return TransferHandler.COPY;
    }

    /**
     * Indicates whether a component would accept an import of the given set of data flavors prior to actually
     * attempting to import it.
     *
     * @param target  The component to receive the transfer.
     * @param flavors The data formats available.
     * @return <code>true</code> if the data can be inserted into the component; <code>false</code> otherwise.
     */
    public boolean canImport(JComponent target, DataFlavor[] flavors)
    {
        if (!towardsOkay) return false;
        for (final DataFlavor flavor : flavors)
        {
            if (flavor.isMimeTypeEqual(DATA_FLAVOR_ABSTRACT_FILE_NODES)) return true;
        }
        return false;
    }

    /**
     * Receives a value from a DND operation.
     *
     * @param target       The component to receive the transfer.
     * @param transferable The data to import.
     * @return <code>true</code> if the data was inserted into the component; <code>false</code> otherwise.
     */
    public boolean importData(JComponent target, Transferable transferable)
    {
        if (!(transferable instanceof TransferableAbstractFileNodes)) return false;
        TransferableAbstractFileNodes transferableNodes = (TransferableAbstractFileNodes) transferable;
        AbstractFileNode[] nodes = null;
        try
        {
            nodes = (AbstractFileNode[]) (transferableNodes.getTransferData(DATA_FLAVOR_ABSTRACT_FILE_NODES));
        } catch (UnsupportedFlavorException e)
        {
            // Can't happen.
        }
        Iterator<AbstractFileNodeReceiptListener> it = new JointIterator<AbstractFileNodeReceiptListener>(
                universalListeners.iterator(), conditionalListeners.getAll(target).iterator());
        while (it.hasNext())
        {
            AbstractFileNodeReceiptListener listener = it.next();
            listener.abstractFileNodesReceived(target, nodes);
        }
        return true;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}