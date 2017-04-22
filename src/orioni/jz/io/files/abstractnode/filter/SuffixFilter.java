package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

/**
 * This {@link AbstractFileNodeFilter} filters {@link AbstractFileNode}s by the file suffix.
 *
 * @author Zachary Palmer
 */
public class SuffixFilter implements AbstractFileNodeFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The string which must appear on the end of the file's name for the file to be accepted.
     */
    protected String suffix;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param suffix The string which must appear on the end of the file's name for the file to be accepted.
     */
    public SuffixFilter(String suffix)
    {
        super();
        this.suffix = suffix;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests whether or not the provided {@link AbstractFileNode} should be accepted.
     *
     * @param node The {@link AbstractFileNode} to test.
     * @return <code>true</code> if the provided node should be accepted; <code>false</code> otherwise.
     */
    public boolean accept(AbstractFileNode node)
    {
        if (node.isFilesystemCaseSensitive())
        {
            return node.getName().endsWith(suffix);
        } else
        {
            return node.getName().toLowerCase().endsWith(suffix.toLowerCase());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE