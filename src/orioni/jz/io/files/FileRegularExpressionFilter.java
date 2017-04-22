package orioni.jz.io.files;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This {@link FileFilter} is meant to filter files based upon whether or not the name of the file matches a Java
 * regular expression.  The parent's path is ignored, as well as whether or not the file is a directory.
 *
 * @author Zachary Palmer
 */
public class FileRegularExpressionFilter implements FileFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The regular expression with which the files are filtered.
     */
    protected String expression;
    /**
     * The {@link Pattern} used to filter files.
     */
    protected Pattern pattern;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param expression The regular expression with which the files should be filtered.
     * @throws PatternSyntaxException If the provided regular expression is improperly formatted.
     */
    public FileRegularExpressionFilter(String expression)
            throws PatternSyntaxException
    {
        super();
        this.expression = expression;
        pattern = Pattern.compile(this.expression);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the regular expression for this filter.
     *
     * @return The regular expression being used to filter files.
     */
    public String getExpression()
    {
        return expression;
    }

    /**
     * Tests whether or not the specified abstract pathname should be included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept(File pathname)
    {
        return pattern.matcher(pathname.getName()).matches();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE