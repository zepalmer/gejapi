package orioni.jz.awt.swing.filenode;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * This {@link Transferable} implementation allows GUI elements to transfer {@link
 * orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> objects between components by dragging and
 * dropping.
 *
 * @author Zachary Palmer
 */
public class TransferableAbstractFileNodes implements Transferable
{
    /**
     * A {@link DataFlavor}<code>[2]</code> containing the values {@link AbstractFileNodeTransferHandler#DATA_FLAVOR_ABSTRACT_FILE_NODES}
     * and {@link DataFlavor#stringFlavor}.
     */
    protected static final DataFlavor[] DATA_FLAVOR_ARRAY = new DataFlavor[]{
            AbstractFileNodeTransferHandler.DATA_FLAVOR_ABSTRACT_FILE_NODES,
            DataFlavor.stringFlavor
    };

    /**
     * The {@link orioni.jz.io.files.abstractnode.AbstractFileNode}<code>[]</code> object which is being transferred.
     */
    protected AbstractFileNode[] dataSources;

    /**
     * General constructor.
     *
     * @param dataSources The {@link orioni.jz.io.files.abstractnode.AbstractFileNode} object to use.
     */
    public TransferableAbstractFileNodes(AbstractFileNode[] dataSources)
    {
        this.dataSources = dataSources;
    }

    /**
     * Returns an object which represents the data to be transferred.  The class of the object returned is defined by the
     * representation class of the flavor.
     * <p/>
     * This method will not succeed unless the representation class is {@link orioni.jz.io.files.abstractnode.AbstractFileNode}.
     *
     * @param flavor the requested flavor for the data
     * @throws UnsupportedFlavorException If the requested data flavor is not supported.
     */
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException
    {
        if (flavor.isMimeTypeEqual(AbstractFileNodeTransferHandler.DATA_FLAVOR_ABSTRACT_FILE_NODES))
        {
            return dataSources;
        }
        if (flavor.isMimeTypeEqual(DataFlavor.stringFlavor))
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < dataSources.length; i++)
            {
                sb.append(dataSources[i].toString());
                if (i < dataSources.length - 1) sb.append("\n");
            }
            return sb.toString();
        }
        throw new UnsupportedFlavorException(flavor);
    }

    /**
     * Returns an array of {@link DataFlavor} objects indicating the flavors in which the data can be provided. This array
     * is organized in preferential order (from most to least descriptive).
     *
     * @return An array of data flavors in which this data can be transferred.
     */
    public DataFlavor[] getTransferDataFlavors()
    {
        return DATA_FLAVOR_ARRAY;
    }

    /**
     * Returns whether or not the specified data flavor is supported for this object.
     * @param flavor The requested flavor for the data
     * @return <code>true</code> if the data flavor is supported; <code>false</code> otherwise.
     */
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (final DataFlavor otherFlavor : DATA_FLAVOR_ARRAY)
        {
            if (flavor.isMimeTypeEqual(otherFlavor)) return true;
        }
        return false;
	}
}