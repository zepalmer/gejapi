package orioni.jz.io.files;

/**
 * This implementation of <code>FilenameFactory</code> generates a filename based upon a string passed at
 * construction time.  That string may contain any number of instances of the substring <code>"***"</code>.  On call
 * to <code>getFilename(x)</code>, each instance of <code>"***"</code> is replaced with the index <code>x</code>.  The
 * string for <code>x</code> is padded on the left with a number of zeroes, also specified at construction time.
 * <P>
 * This factory is particularly useful in archiving applications, especially those in which the size of the final
 * output file isn't determinable.
 *
 * @author Zachary Palmer
 */
public class FilenamePatternFactory implements FilenameFactory
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The pattern from which filenames should be generated. */
    protected String pattern;
    /** The minimum number of digits in the number that replaces the pattern <code>"***"</code> in the pattern from
     *  which filenames are generated. */
    protected int digits;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param pattern The filename pattern string for which this factory will generate filenames.  The pattern string
     *                should contain at least one instance of the substring <code>"***"</code>.  On calls to
     *                <code>getFilename(x)</code>, each instance of the substring <code>"***"</code> will be replaced
     *                with a string representing the index <code>x</code>.
     * @param digits The minimum number of digits in the number inserted into each filename returned.  If the number
     *               of digits in the index <code>x</code> in the call to <code>getFilename(x)</code> is less than this
     *               number, the string representing <code>x</code> will be padded on the left with zeroes until it is
     *               at least this number of characters in length.
     */
    public FilenamePatternFactory(String pattern, int digits)
    {
        this.pattern = pattern;
        this.digits = digits;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This function generates a filename based upon the parameters provided at construction time.  For any given value
     * <code>x</code> for a call to <code>getFilename(x)</code>, all instances of the substring <code>"***"</code> in
     * the pattern string are replaced with a string representing <code>x</code>.  If the string does not contain at
     * least the number of digits specified at construction time, it is padded on the left with zeroes.
     * <P>
     * For example, given the construction pattern <code>"archive***.dat"</code> and the digit count <code>4</code>, a
     * call to <code>getFilename(15)</code> would return the string <code>"archive0015.dat"</code>.  A call to
     * <code>getFilename(90024)</code> would return the string <code>"archive90024.dat"</code>.
     * <P>
     * Calling this method with a negative index value (such as <code>getFilename(-15)</code>) throws a runtime
     * exception.
     *
     * @param index The index for which to generate a filename.
     * @return A filename based on the index provided.
     * @throws IllegalArgumentException If the provided index is a negative number.
     */
    public String getFilename(int index)
        throws IllegalArgumentException
    {
        // Establish the string representing the index.
        StringBuffer sb = new StringBuffer();
        sb.append(index);
        while (sb.length()<digits) sb.insert(0,'0');
        String indexString = sb.toString();

        // Now replace all pattern instances with it.
        String ret = new String(pattern);
        int patternIndex;
        while ((patternIndex = ret.indexOf("***"))!=-1)
        {
            sb.delete(0,sb.length());
            sb.append(ret.substring(0,patternIndex));
            sb.append(indexString);
            if (ret.length()>patternIndex +3)
            {
                sb.append(ret.substring(patternIndex +3));
            }
            ret = sb.toString();
        }

        return ret;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //