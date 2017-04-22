package orioni.jz.io;

import orioni.jz.math.MathUtilities;
import orioni.jz.util.strings.StringUtilities;
import orioni.jz.io.files.FileUtilities;

import java.util.Arrays;
import java.util.HashSet;

/**
 * This class is designed to act as a container for information specifying a file type.  Given that certain types of
 * files can have multiple extensions (ex., "jpg" and "jpeg" or "htm" and "html"), this class contains a single
 * description string and multiple extensions for the file type.
 *
 * @author Zachary Palmer
 */
public class FileType
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A user-readable description of this {@link FileType}.
     */
    protected String description;
    /**
     * A list of extensions which can be used by this {@link FileType}.  These extensions should not contain the leading
     * '<code>.</code>' character.
     */
    protected String[] extensions;
    public static final FileType[] EMPTY_FILE_TYPE_ARRAY = new FileType[0];

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param description A user-readable description of this {@link FileType}.
     * @param extensions  A list of extensions which can be used by this {@link FileType}.  These extensions should not
     *                    contain the leading '<code>.</code>' character.
     */
    public FileType(String description, String... extensions)
    {
        super();
        this.description = description;
        this.extensions = extensions;
        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE)
        {
            for (int i=0;i<this.extensions.length;i++)
            {
                this.extensions[i] = this.extensions[i].toLowerCase();
            }
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves a user-readable description of this {@link FileType}.
     *
     * @return A user-readable description of this {@link FileType}.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Retrieves a list of extensions which can be used by this {@link FileType}.
     * @return A list of extensions which can be used by this {@link FileType}.
     */
    public String[] getExtensions()
    {
        return getExtensions(false);
    }

    /**
     * Retrieves a list of extensions which can be used by this {@link FileType}.
     *
     * @param withLeadingSeparator <code>true</code> if the values in the returned array should contain leading '<code>.</code>' characters;
     *        <code>false</code> otherwise.
     * @return A list of extensions which can be used by this {@link FileType}.
     */
    public String[] getExtensions(boolean withLeadingSeparator)
    {
        String[] ret = new String[extensions.length];
        System.arraycopy(extensions, 0, ret, 0, ret.length);
        if (withLeadingSeparator)
        {
            for (int i=0;i<ret.length;i++)
            {
                ret[i] = '.' + ret[i];
            }
        }
        return ret;
    }

    /**
     * Determines whether or not this {@link FileType} contains the provided extension.
     * @param extension The extension to find.  This value should not contain the leading '<code>.</code>' character.
     * @return <code>true</code> if the extension is found in this {@link FileType}'s extension list; <code>false</code>
     *         otherwise.  Note that this method will perform comparisons based upon the case sensitivity of the
     *         underlying file system.
     */
    public boolean usesExtension(String extension)
    {
        HashSet<String> set = new HashSet<String>(Arrays.asList(getExtensions()));
        if (FileUtilities.FILESYSTEM_CASE_SENSITIVE)
        {
            return set.contains(extension);
        } else
        {
            for (String s : set)
            {
                if (s.equalsIgnoreCase(extension)) return true;
            }
            return false;
        }
    }

    /**
     * Determines if this {@link FileType} and another are equal.
     *
     * @param o The other object to which this {@link FileType} will be compared.
     * @return <code>true</code> if and only if the other object is a {@link FileType} and both file types have the same
     *         set of extensions.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof FileType)) return false;

        final FileType other = (FileType) o;

        return new HashSet<String>(Arrays.asList(other.getExtensions())).equals(
                new HashSet<String>(Arrays.asList(this.getExtensions())));
    }

    /**
     * Generates a hash code for this {@link FileType}.
     *
     * @return A hash code for this object.
     */
    public int hashCode()
    {
        String[] strings = new HashSet<String>(Arrays.asList(this.getExtensions())).toArray(
                StringUtilities.EMPTY_STRING_ARRAY);
        Arrays.sort(strings);
        int hashCode = 0;
        for (String s : strings)
        {
            hashCode = MathUtilities.rotateLeft(hashCode ^ s.hashCode(), 11);
        }
        return hashCode;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE