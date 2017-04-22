package orioni.jz.awt.swing.filenode;

import orioni.jz.awt.VectorImageIcon;
import orioni.jz.awt.swing.icon.ColoredStringIcon;
import orioni.jz.io.files.abstractnode.AbstractFileNode;
import orioni.jz.io.files.abstractnode.LocalFileNode;
import orioni.jz.io.files.abstractnode.ParentFileNode;

import javax.swing.*;
import java.awt.*;

/**
 * This implementation of {@link AbstractFileNodeIconSource} provides a default icon generation mechanism for abstract
 * file nodes.
 *
 * @author Zachary Palmer
 */
public class DefaultAbstractFileNodeIconSource implements AbstractFileNodeIconSource
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A singleton instance of this class for convenience.
     */
    public static final DefaultAbstractFileNodeIconSource SINGLETON = new DefaultAbstractFileNodeIconSource();

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public DefaultAbstractFileNodeIconSource()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Returns an icon for the provided {@link AbstractFileNode}.
     *
     * @param node The {@link AbstractFileNode} for which an icon should be generated.
     * @return The created {@link Icon}, or <code>null</code> if the node should not have an icon.  The latter is
     *         acceptable as well if no suitable icon could be generated.
     */
    public Icon getAbstractNodeIcon(AbstractFileNode node)
    {
        if (node instanceof ParentFileNode)
        {
            node = ((ParentFileNode) node).getWrappedNode();
        }

        if (node instanceof LocalFileNode)
        {
            if (node.isDirectory())
            {
                return new VectorImageIcon(
                        VectorImageIcon.IconType.FOLDER, 20, 16, Color.BLACK, Color.YELLOW.darker());
            } else
            {
                return new VectorImageIcon(
                        VectorImageIcon.IconType.PIECE_OF_PAPER_DOG_EARED, 10, 16, Color.BLACK, Color.YELLOW.darker());
            }
        } else
        {
            return new ColoredStringIcon().setFont(new Font("Monospaced", Font.PLAIN, 12)).
                    setBackgroundColor(Color.BLUE).
                    setForegroundColor(Color.WHITE).
                    setOutlineColor(Color.WHITE).
                    setOutlineSize(1).
                    setText("?").
                    setAbsoluteHeight(16).
                    setAbsoluteWidth(16);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE