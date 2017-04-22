package orioni.jz.io.files;

import orioni.jz.common.exception.ParseException;
import orioni.jz.math.MathUtilities;

import java.io.File;
import java.io.FileFilter;

/**
 * This {@link FileFilter} implementation filters files based upon their sizes.  It can filter for files less than,
 * greater than, or equal to a specific size.  The size must be specified in bytes but may also be a string containing a
 * multiplicative suffix.  The multiplicative suffix must be one or two characters, wherein the second character (if
 * present) is always 'i'.  The first character indicates an exponent according to the following table:
 * <table><tr><td><center><b>Character</b></center></td><td><center><b>Exponent</b></center></td></tr>
 * <tr><td><center>b</center></td><td><center>0</center></td></tr><tr><td><center>k</center></td><td><center>1</center></td></tr>
 * <tr><td><center>m</center></td><td><center>2</center></td></tr> <tr><td><center>g</center></td><td><center>3</center></td></tr>
 * <tr><td><center>t</center></td><td><center>4</center></td></tr> <tr><td><center>p</center></td><td><center>5</center></td></tr>
 * <tr><td><center>e</center></td><td><center>6</center></td></tr> <tr><td><center>z</center></td><td><center>7</center></td></tr>
 * <tr><td><center>y</center></td><td><center>8</center></td></tr></table> The base of the multiplier is dependent upon
 * the second character.  If the 'i' is present, the base of 1000 is used. If it is not, the base of 1024 is used.
 * Therefore, the multiplicative suffix <code>ki</code> represents 1000, <code>m</code> represents 1048576, and
 * <code>ti</code> represents 1,000,000,000.
 *
 * @author Zachary Palmer
 */
public class FileSizeFilter implements FileFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The size against which to compare.
     */
    protected long size;
    /**
     * Whether or not files less than the provided size should be included.
     */
    protected boolean lessThan;
    /**
     * Whether or not files equal to the provided size should be included.
     */
    protected boolean equalTo;
    /**
     * Whether or not files greater than the provided size should be included.
     */
    protected boolean greaterThan;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.
     *
     * @param size        The size string against which to compare.
     * @param lessThan    <code>true<code> if files less than the specified size should be included in the filter;
     *                    <code>false</code> otherwise.
     * @param equalTo     <code>true<code> if files equal to the specified size should be included in the filter;
     *                    <code>false</code> otherwise.
     * @param greaterThan <code>true<code> if files greater than the specified size should be included in the filter;
     *                    <code>false</code> otherwise.
     * @throws ParseException If the provided size string is not properly formatted.
     */
    public FileSizeFilter(String size, boolean lessThan, boolean equalTo, boolean greaterThan)
            throws ParseException
    {
        this(parseSizeString(size), lessThan, equalTo, greaterThan);
    }

    /**
     * General constructor.
     *
     * @param size        The size against which to compare.
     * @param lessThan    <code>true<code> if files less than the specified size should be included in the filter;
     *                    <code>false</code> otherwise.
     * @param equalTo     <code>true<code> if files equal to the specified size should be included in the filter;
     *                    <code>false</code> otherwise.
     * @param greaterThan <code>true<code> if files greater than the specified size should be included in the filter;
     *                    <code>false</code> otherwise.
     */
    public FileSizeFilter(long size, boolean lessThan, boolean equalTo, boolean greaterThan)
    {
        super();

        this.size = size;
        this.lessThan = lessThan;
        this.equalTo = equalTo;
        this.greaterThan = greaterThan;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests whether or not the specified abstract pathname should be included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept(File pathname)
    {
        long fileSize = pathname.length();
        return ((fileSize < this.size) && this.lessThan) || ((fileSize == this.size) && this.equalTo) ||
               ((fileSize > this.size) && this.greaterThan);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Parses the provided string as a size, possibly containing a multiplicative suffix as described in the
     * documentation of this class.
     *
     * @param size The string containing a size.
     * @return A <code>long</code> containing the equivalent size.
     * @throws ParseException If the provided string is not properly formatted.
     */
    protected static long parseSizeString(String size)
            throws ParseException
    {
        int index = 0;
        while ((index < size.length()) && (Character.isDigit(size.charAt(index)))) index++;
        String number = size.substring(0, index);
        String suffix = size.substring(index);
        if (suffix.length() == 0)
        {
            try
            {
                return Long.parseLong(number);
            } catch (NumberFormatException e)
            {
                throw new ParseException(e);
            }
        }
        if (suffix.length() > 2) throw new ParseException("Invalid suffix (too long): " + suffix);
        if ((suffix.length() == 2) && (suffix.charAt(1) != 'i'))
        {
            throw new ParseException("Invalid suffix (invalid second character): " + suffix);
        }
        long base = (suffix.length() == 2) ? 1000 : 1024;
        long exponent;
        switch (suffix.charAt(0))
        {
            case 'b':
                exponent = 0;
                break;
            case 'k':
                exponent = 1;
                break;
            case 'm':
                exponent = 2;
                break;
            case 'g':
                exponent = 3;
                break;
            case 't':
                exponent = 4;
                break;
            case 'p':
                exponent = 5;
                break;
            case 'e':
                exponent = 6;
                break;
            case 'z':
                exponent = 7;
                break;
            case 'y':
                exponent = 8;
                break;
            default:
                throw new ParseException("Invalid suffix (unrecognized size character): " + suffix);
        }
        long k;
        try
        {
            k = Long.parseLong(number);
        } catch (NumberFormatException e)
        {
            throw new ParseException(e.getMessage());
        }
        return k * MathUtilities.exponent(base, exponent);
    }
}

// END OF FILE
