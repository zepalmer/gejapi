package orioni.jz.io.files;

import java.io.File;
import java.io.FileFilter;

/**
 * This class is specifically designed to filter classes for or against being directories.
 *
 * @author Zachary Palmer
 */
public class FileDirectoryFilter implements FileFilter, Comparable<FileDirectoryFilter>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An instance of the filter which accepts only directories.
     */
    public static final FileDirectoryFilter SINGLETON_DIRECTORIES_ONLY = new FileDirectoryFilter(true);
    /**
     * An instance of the filter which accepts only non-directories.
     */
    public static final FileDirectoryFilter SINGLETON_NON_DIRECTORIES_ONLY = new FileDirectoryFilter(false);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * If <code>true</code>, this filter only accepts directories.  If <code>false</code>, this filter only accepts
     * non-directories.
     */
    protected boolean acceptDirectories;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param acceptDirectories If <code>true</code>, this filter only accepts directories.  If <code>false</code>,
     *                           this filter only accepts non-directories.
     */
    public FileDirectoryFilter(boolean acceptDirectories)
    {
        super();
        this.acceptDirectories = acceptDirectories;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Tests whether or not the specified abstract pathname should be included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested.
     * @return <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept(File pathname)
    {
        return (pathname.isDirectory() == acceptDirectories);
    }

    /**
     * Determines whether or not this {@link FileDirectoryFilter} accepts directories.
     *
     * @return <code>true</code> if this {@link FileDirectoryFilter} accepts directories; <code>false</code> otherwise.
     */
    public boolean acceptsDirectories()
    {
        return acceptDirectories;
    }

    /**
     * Determines if this {@link FileDirectoryFilter} is equal to another one.
     *
     * @param o The object to compare to this one.
     * @return <code>true</code> if the objects are equal; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof FileDirectoryFilter)) return false;

        final FileDirectoryFilter fileDirectoryFilter = (FileDirectoryFilter) o;

        return acceptDirectories == fileDirectoryFilter.acceptDirectories;
    }

    /**
     * Generates a hash code for this {@link FileDirectoryFilter}.
     *
     * @return The hash code for this filter.
     */
    public int hashCode()
    {
        return (acceptDirectories ? 1 : 0);
    }

    /**
     * Compares this {@link FileDirectoryFilter} with another one.  {@link FileDirectoryFilter}s which accept
     * directories are arbitrarily assumed to be "less" than ones that don't.
     *
     * @param other The {@link FileDirectoryFilter} to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(FileDirectoryFilter other)
    {
        if (acceptsDirectories())
        {
            if (other.acceptsDirectories()) return 0; else return -1;
        } else
        {
            if (other.acceptsDirectories()) return 1; else return 0;
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}