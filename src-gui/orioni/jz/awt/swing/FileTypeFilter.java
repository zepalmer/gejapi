package orioni.jz.awt.swing;

import orioni.jz.io.FileType;
import orioni.jz.io.files.FileExtensionFilter;

/**
 * This {@link javax.swing.filechooser.FileFilter} implementation accepts a {@link orioni.jz.io.FileType} and filters files based
 * upon it.
 *
 * @author Zachary Palmer
 */
public class FileTypeFilter extends SwingFileFilterWrapper
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The {@link FileType} to use in filtering files. */
    protected FileType type;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param type The {@link FileType} to use.
     */
    public FileTypeFilter(FileType type)
    {
        super(new FileExtensionFilter(type.getExtensions()), type.getDescription());
        this.type = type;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the {@link FileType} on which filtering is based.
     * @return The {@link FileType} on which filtering is based.
     */
    public FileType getFileType()
    {
        return type;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE