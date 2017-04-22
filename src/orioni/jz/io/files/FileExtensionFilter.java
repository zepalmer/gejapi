package orioni.jz.io.files;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Set;

/**
 * This implementation of <code>FileFilter</code> filters files based on the extensions provided at construction time.
 * All directories are accepted as valid; all files that match any one of the extensions provided is also accepted.
 *
 * @author Zachary Palmer
 */
public class FileExtensionFilter implements FileFilter, Comparable<FileExtensionFilter>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The extensions that are considered legal for this filter.
     */
    protected String[] extensions;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param extensions The extensions to accept.
     */
    public FileExtensionFilter(String... extensions)
    {
        super();
        this.extensions = extensions;
        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE)
        {
            for (int i = 0; i < this.extensions.length; i++)
            {
                this.extensions[i] = this.extensions[i].toLowerCase();
            }
        }
        Arrays.sort(this.extensions);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not the given <code>File</code> object represents some file that can be accepted by this
     * filter.  If the <code>File</code> object represents a directory, it is considered an automatic success.
     * Otherwise, the <code>File</code> is accepted if the results of <code>File.getPath</code> end in one of the
     * extensions provided at construction time.
     *
     * @param f The <code>File</code> object to judge for filtering.
     * @return <code>true</code> if the <code>File</code> object is acceptable; <code>false</code> otherwise
     */
    public boolean accept(File f)
    {
        if (f.isDirectory()) return true;
        String path = f.getPath();
        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE) path = path.toLowerCase();
        for (final String extension : extensions)
        {
            if (path.endsWith(extension)) return true;
        }
        return false;
    }

    /**
     * Retrieves the extensions for this file filter.
     *
     * @return A {@link Set}<code>&lt{@link String}&gt;</code> containing the file extensions accepted by this filter.
     */
    public String[] getExtensions()
    {
        String[] ret = new String[extensions.length];
        System.arraycopy(extensions, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Determines whether or not this {@link FileExtensionFilter} is equal to another object.  This is only the case if
     * the other object is also a {@link FileExtensionFilter} and meets the terms of {@link
     * FileExtensionFilter#equals(FileExtensionFilter)}.
     *
     * @param o The object to compare.
     * @return <code>true</code> if the objects are equal; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (o instanceof FileExtensionFilter)
        {
            return equals((FileExtensionFilter) o);
        } else
        {
            return false;
        }
    }

    /**
     * Determines whether or not this {@link FileExtensionFilter} is equal to another {@link FileExtensionFilter}. This
     * is the case if they use the same set of file extensions, regardless of ordering.
     *
     * @param other The other {@link FileExtensionFilter}.
     * @return <code>true</code> if they are equal; <code>false</code> otherwise.
     */
    public boolean equals(FileExtensionFilter other)
    {
        return (compareTo(other) == 0);
    }

    /**
     * Returns a hash code for this {@link FileExtensionFilter}.
     */
    public int hashCode()
    {
        int code = 0;
        for (String s : extensions) code ^= s.hashCode();
        return code;
    }

    /**
     * Compares this {@link FileExtensionFilter} to another one.  A {@link FileExtensionFilter} is compared to another
     * by the extensions it accepts.  The first precedence is the alphabetically lowest extension that a filter accepts,
     * then the next lowest, and so on.  The "lesser" filter is the one that has the first alphabetically lower
     * extension in comparison to its counterpart on the other filter.
     * <p/>
     * If the two filters have the exact same list of extensions, they are equal.  If one filter has more extensions
     * than the other and both filters match in extensions up to the point that the other filter runs out, the filter
     * with <b>fewer</b> extensions is "greater."  This is intended to allow filters which accept a greater set of
     * extensions to appear first.
     *
     * @param other The Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(FileExtensionFilter other)
    {
        int index = 0;
        while ((index < other.getExtensions().length) && (index < this.getExtensions().length))
        {
            int ret = (getExtensions()[index].compareTo(other.getExtensions()[index]));
            if (ret != 0) return ret;
            index++;
        }
        if (this.getExtensions().length > other.getExtensions().length) return -1;
        if (this.getExtensions().length < other.getExtensions().length) return 1;
        return 0;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //