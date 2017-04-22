package orioni.jz.io.files.abstractnode.filter;

import orioni.jz.io.files.abstractnode.AbstractFileNode;

import java.util.regex.Pattern;

/**
 * This class filters {@link AbstractFileNode}s based upon a regular expression.
 *
 * @author Zachary Palmer
 */
public class RegularExpressionFilter implements AbstractFileNodeFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link Pattern} against which to match. */
    protected Pattern pattern;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param pattern The regular expression pattern against which to match.
     */
    public RegularExpressionFilter(String pattern)
    {
        super();
        this.pattern = Pattern.compile(pattern);
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
        return pattern.matcher(node.getName()).matches();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE