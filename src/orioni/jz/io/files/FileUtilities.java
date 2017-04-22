package orioni.jz.io.files;

import orioni.jz.io.IOUtilities;

import java.io.*;
import java.util.*;

/**
 * This class contains a series of generic methods that may be useful in various file-based I/O situations.
 *
 * @author Zachary Palmer
 */
public class FileUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A {@link File} array with no elements (length==0).
     */
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    /**
     * A value stating whether or not the filesystem is case sensitive.
     */
    public static final boolean FILESYSTEM_CASE_SENSITIVE;

// STATIC INITIALIZATION /////////////////////////////////////////////////////////

    /** Sets the case sensitivity trigger. */
    static
    {
        // Sets case sensitivity trigger.
        FILESYSTEM_CASE_SENSITIVE = !(new File("A").equals(new File("a")));
    }

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Prevents <code>FileUtilities</code> from being instantiated.
     */
    private FileUtilities()
    {
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * "Coerces" the provided file into having one of the provided extensions.  This method is more passive than
     * <code>forceFileExtension</code>.  This method attempts to return a file with the provided extension.
     * <p/>
     * The return value will have the first given extension <b>if</b>: <UL><LI>The original file had that extension
     * <b>or</b> <LI>The original file contains no instances of the first character in the extension (in which case the
     * extension will be appended to the original file's path).</UL> The return value will be the unmodified parameter
     * if it has any of the listed extensions.  If the extensions list is empty, the unmodified parameter will be
     * returned.
     * <p/>
     * This method is designed to affect a file in much the same way as a standard Windows file prompt when an extension
     * is selected.  If the user clearly specifies an extension, the original is unchanged; if the user does not specify
     * an extension, one is added.
     *
     * @param original   The file to be forced into having a given extension.
     * @param extensions The extensions to ensure a file has.  This extensions should include a period or other
     *                   extension separator as its first character.
     * @return A file with the given extension.  This may be the original file, if that file already has that
     *         extension.
     */
    public static File coerceFileExtension(File original, String... extensions)
    {
        if (extensions.length == 0) return original;
        int indexOfSeparator = original.getName().lastIndexOf(extensions[0].charAt(0));
        String currentFileSuffix;
        if (indexOfSeparator == -1)
        {
            currentFileSuffix = "";
        } else
        {
            currentFileSuffix = original.getName().substring(indexOfSeparator);
        }
        if (FILESYSTEM_CASE_SENSITIVE)
        {
            for (String extension : extensions)
            {
                if (currentFileSuffix.equals(extension)) return original;
            }
        } else
        {
            for (String extension : extensions)
            {
                if (currentFileSuffix.equalsIgnoreCase(extension)) return original;
            }
        }
        // Coerce the suffix if appropriate
        if (indexOfSeparator == -1)
        {
            // If the first character of the extension is not found anywhere in the file...
            return new File(original.getPath() + extensions[0]);
        } else
        {
            // The file already has some extension
            return original;
        }
    }

    /**
     * Forces the provided file to have one of the provided extensions.  If the file already has one of these extensions
     * as a suffix, the file is returned as-is.  If the file does not have any of the extensions as a suffix, the first
     * extensions is added.
     *
     * @param original   The file to be forced into having a given extensions.
     * @param extensions The extensions to ensure a file has.  This extensions should include a period, if appropriate.
     * @return A file with the given extensions.  This may be the original file, if that file already has that
     *         extensions.
     */
    public static File forceFileExtension(File original, String... extensions)
    {
        if (extensions.length == 0) return original;
        for (String extension : extensions)
        {
            String currentFileSuffix = original.getName().substring(
                    Math.max(original.getName().length() - extension.length(), 0));
            if (FILESYSTEM_CASE_SENSITIVE)
            {
                if (currentFileSuffix.equals(extension)) return original;
            } else
            {
                if (currentFileSuffix.equalsIgnoreCase(extension)) return original;
            }
        }
        // Force the suffix
        return new File(original.getPath() + extensions[0]);
    }

    /**
     * Retrieves the "extension" of the provided file.  The extension is the set of characters that appear after the
     * last instance of '<code>.</code>' in the name.  If '<code>.</code>' does not appear in the name of the file, an
     * empty string is returned.
     *
     * @param file The {@link File} whose extension is to be retrieved.
     * @return The extension.
     */
    public static String getFileExtension(File file)
    {
        String name = file.getName();
        int index = name.lastIndexOf('.');
        if (index == -1) return "";
        return name.substring(index + 1);
    }

    /**
     * Returns the provided file without an extension.  That is, the file is returned after removing the last character
     * '<code>.</code>' and any characters which followed it from the file's name.  Note that this will not affect any
     * path information up to and including the file's parent.  If the file's name does not contain the character
     * '<code>.</code>', it will be returned unchanged.
     */
    public static File removeFileExtension(File original)
    {
        String name = original.getName();
        String path = original.getParent();
        if (name.indexOf('.') == -1) return original;
        name = name.substring(0, name.lastIndexOf('.'));
        return new File(path + File.separatorChar + name);
    }

    /**
     * Returns the provided file, replacing its extension with the provided one.  The provided extension <i>should</i>
     * contain the leading '<code>.</code>' character.  If the file did not have an extension, this method will simply
     * add the extension to the file.
     *
     * @param file      The file to change.
     * @param extension The extension to use.
     * @return The modified file.
     */
    public static File replaceFileExtension(File file, String extension)
    {
        file = removeFileExtension(file);
        return new File(file.getPath() + extension);
    }

    /**
     * Returns a String which represents a "relative" filename.  This filename is built by taking the given starting
     * directory and attempting to construct a String which can be appended to that directory's path String to acheive
     * the stated target file or directory.
     *
     * @param starting A File representing the directory in which to start.  If this value is not a directory, the
     *                 directory in which it is contained is considered the starting point.
     * @param target   The File to locate.  This file needn't necessarily exist.
     * @return A "relative" path string that, when appended to the end of the starting directory's path string, gives
     *         the target file.
     * @throws IOException If calling <code>starting.getCanonicalFile</code> or <code>target.getCanonicalFile</code>
     *                     results in an IOException.
     * @see File#getCanonicalFile
     */
    public static String getRelativeString(File starting, File target)
            throws IOException,
                   IllegalArgumentException
    {
        int i;

        starting = starting.getCanonicalFile();
        target = target.getCanonicalFile();

        if (!starting.isDirectory()) starting = starting.getParentFile();

        // Construct ArrayLists of the directory structure used to reach the File objects
        List<File> startingList = new ArrayList<File>();
        while (starting != null)
        {
            startingList.add(0, starting);
            starting = starting.getParentFile();
        }
        List<File> targetList = new ArrayList<File>();
        while (target != null)
        {
            targetList.add(0, target);
            target = target.getParentFile();
        }

        // Eliminate common roots
        while ((startingList.size() > 0) && (targetList.size() > 0) &&
               (startingList.get(0).equals(targetList.get(0))))
        {
            startingList.remove(0);
            targetList.remove(0);
        }

        // Create the abstract String that leads from the specified starting directory to the target file
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < startingList.size(); i++)
        {
            sb.append("..");
            sb.append(File.separatorChar);
        }
        for (i = 0; i < targetList.size(); i++)
        {
            File targetPathElement = targetList.get(i);
            sb.append(targetPathElement.getName());
            if (i < targetList.size() - 1)
            {
                sb.append(File.separatorChar);
            }
        }
        return sb.toString();
    }

    /**
     * Copies the source file into the target file.  If the target file exists, it will be deleted.  If the source file
     * does not exist, an {@link IOException} will occur.  If an {@link IOException} of any kind occurs during the
     * actual copying process, this method will make an attempt to delete the target file (so as not to leave a partial
     * copy).
     *
     * @param source The file to copy.
     * @param target The copy.
     * @throws IOException If an I/O error occurs while copying.
     */
    public static void copyFile(File source, File target)
            throws IOException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            try
            {
                byte[] data = new byte[16384];
                int read = fis.read(data);
                while (read != -1)
                {
                    fos.write(data, 0, read);
                    read = fis.read(data);
                }
            } catch (IOException ioe)
            {
                target.delete();
                throw ioe;
            }
        } finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException ioe)
                {
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                } catch (IOException ioe)
                {
                }
            }
        }
    }

    /**
     * Retrieves all files from the specified directory and all of its subdirectories.  The directories themselves will
     * not be returned.
     *
     * @param file The file from which to retrieve all files.
     * @return All files within that directory and all subdirectories, or the provided file if it was not a directory.
     * @throws IOException If an I/O error occurs while listing the directory contents.
     */
    public static Set<File> getRecursiveFiles(File file)
            throws IOException
    {
        return getRecursiveFiles(file, false);
    }

    /**
     * Retrieves all files from the specified directory and all of its subdirectories.  If the <code>directories</code>
     * parameter is <code>true</code>, then the directories will also be returned, including the root directory (the
     * parameter <code>file</code>).
     *
     * @param file        The file from which to retrieve all files.
     * @param directories <code>true</code> if the directories should be returned as well.
     * @return All files within that directory and all subdirectories, or the provided file if it was not a directory.
     * @throws IOException If an I/O error occurs while listing the directory contents.
     */
    public static Set<File> getRecursiveFiles(File file, boolean directories)
            throws IOException
    {
        HashSet<File> ret = new HashSet<File>();
        HashSet<File> directoryHistory = new HashSet<File>(); // so we don't follow circular symlinks
        if (file.isDirectory())
        {
            ArrayList<File> queue = new ArrayList<File>();
            queue.add(file);
            while (queue.size() > 0)
            {
                File[] ff = queue.remove(0).listFiles();
                if (ff != null) // This may happen if listing permission to the directory is denied
                {
                    for (File f : ff)
                    {
                        f = f.getCanonicalFile(); // This allows us to resolve things like Linux symlinks
                        if (f.isDirectory())
                        {
                            if (directoryHistory.add(f))
                            {
                                queue.add(f);
                                if (directories) ret.add(f);
                            }
                        } else
                        {
                            ret.add(f);
                        }
                    }
                }
            }
        } else
        {
            ret.add(file);
        }
        return ret;
    }

    /**
     * Deletes all files from the specified directory, as well as the directory itself.
     *
     * @param dir The directory to delete.  If this file is not a directory, the file itself is simply deleted.
     * @return <code>true</code> if the recursive delete was successful; <code>false</code> if any error occurred.
     * @throws IOException If an I/O error occurs while listing the files.
     */
    public static boolean deleteRecursiveFiles(File dir)
            throws IOException
    {
        boolean ret = true;
        Set<File> files = getRecursiveFiles(dir, true);
        ArrayList<File> dirs = new ArrayList<File>();
        for (File f : files)
        {
            if (f.isDirectory())
            {
                dirs.add(f);
            } else
            {
                ret &= f.delete();
            }
        }
        File[] dirarray = dirs.toArray(EMPTY_FILE_ARRAY);
        Arrays.sort(dirarray);
        for (int i = dirarray.length - 1; i >= 0; i--)
        {
            ret &= dirarray[i].delete();
        }
        return ret;
    }

    /**
     * Retrieves the entire contents of a file as a <code>byte[]</code>.  This method is provided for convenience, as it
     * opens and closes the input stream.
     *
     * @param file The {@link File} whose contents should be retrieved.
     * @return The contents of the file.
     * @throws IOException              If an I/O error occurs.
     * @throws IllegalArgumentException If the file is larger than the maximum allocatable array size.
     */
    public static byte[] getFileContents(File file)
            throws IOException, IllegalArgumentException
    {
        if (file.length() > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException("File size is larger than maximum allocatable array size.");
        }
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream((int) file.length());
            IOUtilities.pumpStream(fis, buffer);
            return buffer.toByteArray();
        } finally
        {
            try
            {
                // Ensure we don't leave a file handle hanging open regardless of exceptions.
                if (fis != null) fis.close();
            } catch (IOException e)
            {
            }
        }
    }

    /**
     * Sets the contents of a file using a <code>byte[]</code>.  This method is provided for convenience, as it opens
     * and closes the output stream.  If an error occurs during the write, the file will be deleted.
     *
     * @param file The file to which to write.
     * @param data The <code>byte[]</code> to write to the file.
     * @throws IOException If an I/O error occurs.
     */
    public static void setFileContents(File file, byte[] data)
            throws IOException
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
            fos.write(data);
        } catch (IOException ioe)
        {
            file.delete();
            throw ioe;
        } finally
        {
            if (fos != null) fos.close();
        }
    }

    /**
     * Determines whether or not the first provided {@link File} is an ancestor of the second provided {@link File}.
     *
     * @param ancestor   The candidate ancestor file.
     * @param descendent The candidate descendent file.
     * @return <code>true</code> if this descendent-ancestor relationship exists or if the files are equal;
     *         <code>false</code> otherwise.
     */
    public static boolean isAncestorFile(File ancestor, File descendent)
    {
        while (descendent != null)
        {
            if (ancestor.equals(descendent)) return true;
            descendent = descendent.getParentFile();
        }
        return false;
    }

    /**
     * Compares the contents of two files to determine if they are identical.
     *
     * @param a The first file to compare.
     * @param b The second file to compare.
     * @return <code>true</code> if both files are the same size and have exactly the same contents; <code>false</code>
     *         otherwise.
     * @throws IOException If an I/O error occurs while reading the contents of the files.
     */
    public static boolean compareFiles(File a, File b)
            throws IOException
    {
        if (a.length() != b.length()) return false;
        FileInputStream ais = null;
        FileInputStream bis = null;
        try
        {
            ais = new FileInputStream(a);
            bis = new FileInputStream(b);
            return IOUtilities.compareStreams(ais, bis);
        } finally
        {
            try
            {
                if (ais != null) ais.close();
            } catch (IOException e)
            {
            }
            try
            {
                if (bis != null) bis.close();
            } catch (IOException e)
            {
            }
        }
    }
}

// END OF FILE //