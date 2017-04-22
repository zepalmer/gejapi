package orioni.jz.io.files;

import orioni.jz.math.MathUtilities;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * This {@link FileFilter} is designed to accept files if any of its subfilters accept them.
 *
 * @author Zachary Palmer
 */
public class OrFileFilter implements FileFilter
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link FileFilter}s this {@link OrFileFilter} uses as subordinates.
     */
    protected FileFilter[] filters;
    /**
     * The {@link FileFilter}s this {@link OrFileFilter} uses as subordinates, in the form of a set.
     */
    protected Set<FileFilter> filtersInASet;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param filters The {@link FileFilter}s this {@link OrFileFilter} is to use as subordinates.
     */
    public OrFileFilter(FileFilter... filters)
    {
        super();
        this.filters = filters;
        filtersInASet = new HashSet<FileFilter>();
        for (FileFilter filter : this.filters) filtersInASet.add(filter);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the filters being used by this {@link OrFileFilter}.
     *
     * @return The filters being used by this {@link OrFileFilter}.
     */
    public Set<FileFilter> getFilters()
    {
        return Collections.unmodifiableSet(filtersInASet);
    }

    /**
     * Tests whether or not the specified abstract pathname should be included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code> should be included
     */
    public boolean accept(File pathname)
    {
        for (FileFilter filter : filters)
        {
            if (filter.accept(pathname)) return true;
        }
        return false;
    }

    /**
     * Determines whether or not this {@link OrFileFilter} is equal to another.  This is true if and only if they
     * contain the same filters.
     *
     * @param o The other object.
     * @return <code>true</code> if the filters are equal; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof OrFileFilter)) return false;

        final OrFileFilter other = (OrFileFilter) o;

        HashSet<FileFilter> otherFilters = new HashSet<FileFilter>();
        for (FileFilter filter : other.getFilters()) otherFilters.add(filter);

        return (otherFilters.equals(filtersInASet));
    }

    /**
     * Generates a hashcode for this {@link OrFileFilter}.
     *
     * @return A hashcode for this {@link OrFileFilter}.
     */
    public int hashCode()
    {
        int hashcode = 0;
        for (FileFilter filter : filters)
        {
            hashcode ^= filter.hashCode();
            hashcode = MathUtilities.rotateLeft(hashcode, 3);
        }
        return hashcode;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE