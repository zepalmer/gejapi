package orioni.jz.io.files;

import orioni.jz.math.MathUtilities;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * This extension of the <code>FileFilter</code> class allows a single array of strings to be specified upon
 * construction time.  Files designated as accepted by any one of the strings in this array will be accepted by this
 * filter.  This filter always accepts directories.
 * <p/>
 * These strings represent file search patterns.  They may contain any characters valid in filenames to the filesystem
 * as well as the '?' and '*' characters.  The '?' character indicates that any single character may appear in its place
 * in the actual filename; the '*' character indicates that any number of characters may appear in its place. These
 * search strings also recognize the escape character '\' and the quotation delimiter '"'.
 *
 * @author Zachary Palmer
 */
public class FilePatternFilter implements FileFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The search patterns to use in this filter.
     */
    protected String[] patterns;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param patterns The search patterns to use in this filter, consisting of legal filesystem characters, '?', and
     *                 '*'.
     */
    public FilePatternFilter(String... patterns)
    {
        this.patterns = patterns;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines if a given pattern matches the given String.
     *
     * @param pattern The pattern to apply to a String.
     * @param string  The String to which the pattern should be applied.
     * @return <code>true</code> if the string matches the pattern; <code>false</code> otherwise.
     */
    public synchronized boolean patternMatches(String pattern, String string)
    {
        // Tailor the parameters to case-insensitive operating systems.
        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE)
        {
            pattern = pattern.toLowerCase();
            string = string.toLowerCase();
        }
        // Determine if the pattern matches
        int patternIndex = 0;
        int stringIndex = 0;
        while (patternIndex < pattern.length())
        {
            char patternChar = pattern.charAt(patternIndex++);
            switch (patternChar)
            {
                case '*':
                    // This is going to be expensive.  We need to check each possibility and determine if the remainder
                    // of the string can match the remainder of the pattern.
                    for (int i = 0; i < string.length() - stringIndex; i++)
                    {
                        if (patternMatches(
                                pattern.substring(patternIndex),
                                string.substring(stringIndex + i)))
                        {
                            return true;
                        }
                    }
                    return false;
                case '?':
                    if (string.length() <= stringIndex++) return false;
                    break;
                case '\\':
                    if (string.length() <= stringIndex) return false;
                    if (pattern.length() <= patternIndex) return false;
                    if (pattern.charAt(patternIndex++) != string.charAt(stringIndex++)) return false;
                    break;
                case '"':
                    // Must enter a loop of identical match checking until another quote is reached.
                    if (pattern.length() <= patternIndex) return false;
                    patternChar = pattern.charAt(patternIndex++);
                    while (patternChar != '"')
                    {
                        if (string.length() <= stringIndex) return false;
                        if (patternChar != string.charAt(stringIndex++)) return false;
                        if (pattern.length() <= patternIndex) return false;
                        patternChar = pattern.charAt(patternIndex++);
                    }
                    break;
                default:
                    if (string.length() <= stringIndex) return false;
                    if (patternChar != string.charAt(stringIndex++)) return false;
                    break;
            }
        }
        return true;
    }

    /**
     * Whether the given file is accepted by this filter.  This is true if it matches any one of the patterns provided
     * on construction.
     *
     * @param f The file for which acceptance will be tested.
     * @return <code>true</code> if the file matches a pattern and is therefore accepted; <code>false</code> otherwise.
     */
    public boolean accept(File f)
    {
        // If it's a directory, pass now
        if (f.isDirectory()) return true;
        // Test for pattern match
        for (final String pattern : patterns)
        {
            if (patternMatches(pattern, f.getName())) return true;
        }
        return false;
    }

    /**
     * Compares this object and another for equality.
     *
     * @param o The object to compare.
     * @return <code>true</code> if the objects are equal; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof FilePatternFilter)) return false;

        final FilePatternFilter filePatternFilter = (FilePatternFilter) o;

        if (!Arrays.equals(patterns, filePatternFilter.patterns)) return false;

        return true;
    }

    /**
     * Generates a hash code for this object.
     *
     * @return A hash code of this object.
     */
    public int hashCode()
    {
        int hashcode = 0;
        for (int i = 0; i < patterns.length; i++)
        {
            hashcode ^= MathUtilities.rotateLeft(patterns[i].hashCode(), i);
        }
        return hashcode;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //