package orioni.jz.io.files;

import orioni.jz.util.ByteConstructable;
import orioni.jz.util.DataPacker;
import orioni.jz.util.DataUnpacker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class maintains a list of Files and uses them to create a comprehensive file list.  The Files submitted to this
 * class may be actual files or directory entries; directory entries may be marked recursive or non- recursive.  When a
 * rescan() is called, the directories that have been submitted are appropriately scanned and a list of files that they
 * contain is created.  This list can then be retrieved by getAllFiles(). <BR><BR>The toBytes() method of this object
 * does not reserve the results of the last scan; it merely reserves the contents of the vectors that indicate which
 * directories and files to scan.  After calling fromBytes(), the recreated FileList will not contain any Files
 * (rescan() must be called).
 *
 * @author Zachary Palmer
 */
public class FileList implements ByteConstructable
{
    /**
     * The Vector containing the recursive directory entries.
     */
    protected ArrayList<File> recursiveDirectories;
    /**
     * The ArrayList containing the non-recursive directory entries.
     */
    protected ArrayList<File> nonRecursiveDirectories;
    /**
     * The ArrayList containing the static file entries.
     */
    protected ArrayList<File> staticFiles;
    /**
     * The ArrayList containing the scanned list of Files.
     */
    protected ArrayList<File> scannedFiles;

    /**
     * General constructor.
     */
    public FileList()
    {
        recursiveDirectories = new ArrayList<File>();
        nonRecursiveDirectories = new ArrayList<File>();
        staticFiles = new ArrayList<File>();
        scannedFiles = new ArrayList<File>();
    }

    /**
     * Adds a file or directory to the FileList.  If a directory is added, it is assumed to be added recursively.  If a
     * file or directory is added more than once before it is deleted, subsequent additions are ignored.
     *
     * @param file The file or directory to submit.
     * @return True if the file is already in the FileList; false otherwise.
     */
    public boolean add(File file)
    {
        return add(file, true);
    }

    /**
     * Adds a file or directory to the FileList.  If a file or directory is added more than once before it is deleted,
     * subsequent additions are ignored.
     *
     * @param file      The file or directory to submit.
     * @param recursive If the File object submitted represents a directory, this boolean determines whether or not the
     *                  directory is recursive.
     * @return True if the file is already in the FileList; false otherwise.
     */
    public boolean add(File file, boolean recursive)
    {
        if (contains(file)) return true;
        if (file.isDirectory())
        {
            if (recursive)
            {
                recursiveDirectories.add(file);
            } else
            {
                nonRecursiveDirectories.add(file);
            }
        } else
        {
            staticFiles.add(file);
        }
        return false;
    }

    /**
     * Tests whether or not a particular file or directory is contained within the FileList.
     *
     * @param file The file to locate.
     * @return True if the file is contained within the file list, false otherwise.
     */
    public boolean contains(File file)
    {
        return ((staticFiles.contains(file)) || (recursiveDirectories.contains(file)) ||
                (nonRecursiveDirectories.contains(file)));
    }

    /**
     * Tests whether or not a particular file or directory is contained within the FileList as a recursive directory.
     *
     * @param file The file or directory to test.
     * @return True if the file is contained within the list as a recursive directory, false otherwise.
     */
    public boolean containsAsRecursive(File file)
    {
        if (!file.isDirectory()) return false;
        return (recursiveDirectories.contains(file));
    }

    /**
     * Removes a File from the FileList.
     *
     * @return True if the file was in the FileList, false if it was not.
     */
    public boolean remove(File file)
    {
        if (staticFiles.contains(file))
        {
            staticFiles.remove(file);
            return true;
        }
        if (recursiveDirectories.contains(file))
        {
            recursiveDirectories.remove(file);
            return true;
        }
        if (nonRecursiveDirectories.contains(file))
        {
            nonRecursiveDirectories.remove(file);
            return true;
        }
        return false;
    }

    /**
     * Scans for the appropriate directories and files and creates a list of the files in those locations.
     *
     * @throws IOException If an I/O error occurs while scanning.
     */
    public void rescan()
            throws IOException
    {
        scannedFiles.clear();
        for (File file : staticFiles)
        {
            scan(file, true);
        }
        for (File file : recursiveDirectories)
        {
            scan(file, true);
        }
        for (File file : nonRecursiveDirectories)
        {
            scan(file, false);
        }
    }

    /**
     * Scans for the given File.  If that file is a directory, the contents of the directory are scanned.  If the file
     * is a directory and recursion is true, subdirectories are also scanned.
     *
     * @param file      The File to scan.
     * @param recursion Whether or not to recurse subdirectories if the given File is a directory.
     * @throws IOException If an I/O error occurs when trying to scan the file.
     */
    protected void scan(File file, boolean recursion)
            throws IOException
    {
        file = file.getCanonicalFile();
        if (!file.exists()) return;
        if (file.isDirectory())
        {
            File[] dirContents = file.listFiles();
            if (dirContents != null)
            {
                for (final File dirFile : dirContents)
                {
                    if ((dirFile.isFile()) || (recursion))
                    {
                        scan(dirFile, recursion);
                    }
                }
            }
        } else
        {
            scannedFiles.add(file);
        }
    }

    /**
     * Retrieves a list of File objects representing the files that were found in the objects listed to be scanned.
     *
     * @return A File[] containing the aforementioned File objects.
     */
    public File[] getAllFiles()
    {
        return scannedFiles.toArray(new File[0]);
    }

    /**
     * Tests whether or not a particular file is contained within the FileList's scanned results.
     *
     * @param file The File for which to search.
     * @return True if the File is contained within the scanned results, false otherwise.
     */
    public boolean listContains(File file)
    {
        File[] test = getAllFiles();
        for (final File testFile : test) if (testFile.equals(file)) return true;
        return false;
    }

    /**
     * Tests whether or not a particular file is contained within the FileList's scanned results that has the given name
     * and any path.
     *
     * @param name The name of the file for which to search.  This name should contain the extension of the file, if
     *             applicable.
     * @return An array of File objects if the list contains at least one file that matches the name given with any
     *         path, null otherwise.
     */
    public File[] listContainsNamed(String name)
    {
        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE) name = name.toLowerCase();
        File[] test = getAllFiles();
        List<File> temp = new ArrayList<File>();
        for (final File file : test)
        {
            try
            {
                String s = file.getCanonicalPath();
                if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE) s = s.toLowerCase();
                System.out.println(s + "  ->  " + s.substring(s.lastIndexOf(File.separatorChar) + 1));
                s = s.substring(s.lastIndexOf(File.separatorChar) + 1);
                if (s.equals(name))
                {
                    temp.add(file);
                }
            } catch (IOException ioe)
            {
                ioe.printStackTrace(System.err);
            }
        }
        if (temp.size() == 0)
        {
            return null;
        } else
        {
            return temp.toArray(new File[0]);
        }
    }

    /**
     * Clears all elements in the FileList.
     */
    public void clear()
    {
        staticFiles.clear();
        recursiveDirectories.clear();
        nonRecursiveDirectories.clear();
        scannedFiles.clear();
    }

    /**
     * Returns a File[] containing all static files added to this FileList. The presence of a File object in this File[]
     * does not imply that the file exists or that it will be returned by a rescan().
     */
    public File[] getAllStaticFiles()
    {
        return staticFiles.toArray(new File[0]);
    }

    /**
     * Returns a File[] containing all non-recursive directories added to the FileList.  The presence of a File object
     * in this File[] does not imply that the directory exists or that it will be returned by a rescan().
     */
    public File[] getAllNonrecursiveDirectories()
    {
        return nonRecursiveDirectories.toArray(new File[0]);
    }

    /**
     * Returns a File[] containing all recursive directories added to the FileList.  The presence of a File object in
     * this File[] does not imply that the directory exists or that it will be returned by a rescan().
     */
    public File[] getAllRecursiveDirectories()
    {
        return recursiveDirectories.toArray(new File[0]);
    }

    /**
     * Converts this FileList to an array of bytes.
     *
     * @return A byte[] that represents the FileList.
     */
    public byte[] toBytes()
    {
        DataPacker dp = new DataPacker();
        dp.addInt(recursiveDirectories.size());
        for (File file : recursiveDirectories)
        {
            dp.addString(file.getPath());
        }
        dp.addInt(nonRecursiveDirectories.size());
        for (File file : recursiveDirectories)
        {
            dp.addString(file.getPath());
        }
        dp.addInt(staticFiles.size());
        for (File file : recursiveDirectories)
        {
            dp.addString(file.getPath());
        }
        return dp.pack();
    }

    /**
     * Builds a FileList from an array of bytes.
     *
     * @param data The array of bytes to use.
     * @throws ArrayIndexOutOfBoundsException If the array of bytes is not of the right size.
     */
    public void fromBytes(byte[] data)
    {
        DataUnpacker du = new DataUnpacker();
        du.set(data);
        int i;
        int maxloop;

        maxloop = du.getInt();
        recursiveDirectories.clear();
        for (i = 0; i < maxloop; i++)
        {
            recursiveDirectories.add(new File(du.getString()));
        }

        maxloop = du.getInt();
        nonRecursiveDirectories.clear();
        for (i = 0; i < maxloop; i++)
        {
            nonRecursiveDirectories.add(new File(du.getString()));
        }

        maxloop = du.getInt();
        staticFiles.clear();
        for (i = 0; i < maxloop; i++)
        {
            staticFiles.add(new File(du.getString()));
        }
    }
}
