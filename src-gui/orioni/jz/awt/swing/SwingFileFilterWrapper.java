package orioni.jz.awt.swing;

import javax.swing.filechooser.FileView;
import java.io.File;
import java.io.FileFilter;

/**
 * This {@link javax.swing.filechooser.FileFilter} implementation allows the use of a {@link java.io.FileFilter}
 * implementation in a Swing context.
 *
 * @author Zachary Palmer
 */
public class SwingFileFilterWrapper extends javax.swing.filechooser.FileFilter implements Comparable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The description of the filter to appear in Swing windows.
     */
    protected String fileDescription;
    /**
     * The {@link java.io.FileFilter} to use to filter files.
     */
    protected java.io.FileFilter fileFilter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param filter      The {@link java.io.FileFilter} to use when filtering.
     * @param description The description of this file type to appear in Swing windows.
     */
    public SwingFileFilterWrapper(java.io.FileFilter filter, String description)
    {
        super();
        fileFilter = filter;
        fileDescription = description;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f)
    {
        return fileFilter.accept(f);
    }

    /**
     * Retrieves the underlying filter for this {@link SwingFileFilterWrapper}.
     *
     * @return The underlying filter.
     */
    public FileFilter getFilter()
    {
        return fileFilter;
    }

    /**
     * Retrieves the description of this filter.
     *
     * @see FileView#getName
     */
    public String getDescription()
    {
        return fileDescription;
    }

    /**
     * Determines whether or not this {@link SwingFileFilterWrapper} is equal to another.  This is the case only if the
     * other object is a {@link SwingFileFilterWrapper} and both have equal {@link java.io.FileFilter}s.  Description is
     * ignored.
     *
     * @param o The object to compare to this one.
     * @return <code>true</code> if that object is equal to this one; <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof SwingFileFilterWrapper)) return false;

        final SwingFileFilterWrapper swingFileFilterWrapper = (SwingFileFilterWrapper) o;

        if (!fileFilter.equals(swingFileFilterWrapper.fileFilter)) return false;

        return true;
    }

    /**
     * Generates a hash code for this {@link SwingFileFilterWrapper}.
     *
     * @return The hash code for this {@link SwingFileFilterWrapper}.
     */
    public int hashCode()
    {
        return fileFilter.hashCode();
    }

    /**
     * Compares this {@link SwingFileFilterWrapper} with another.  The first condition for comparison is the underlying
     * filter of each wrapping filter (if they are comparable and can be compared); the second is the description.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(Object o)
    {
        SwingFileFilterWrapper other = (SwingFileFilterWrapper) o;
        return ((Comparable) (getFilter())).compareTo(((Comparable) (other.getFilter())));
    }

    /**
     * Obtains a string describing this {@link SwingFileFilterWrapper}.  This call returns the exact same value as
     * does {@link SwingFileFilterWrapper#getDescription()}.
     * @return A string describing this {@link SwingFileFilterWrapper}.
     */
    public String toString()
    {
        return getDescription();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE