package orioni.jz.io.files;

import orioni.jz.util.DefaultValueHashMap;
import orioni.jz.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * As Java has no system-inspecific manner in which to watch for directory changes (as of v1.5.0_01), this class is
 * designed to poll a specified directory for changes.  Whenever a change is noticed, the {@link
 * DirectoryWatcher#fileChanged(File)} method is called for each file which has been changed.  This includes if the file
 * in question has been deleted.
 *
 * @author Zachary Palmer
 */
public abstract class DirectoryWatcher
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link File} to be watched.
     */
    protected File file;
    /**
     * The number of milliseconds to wait between directory checks.
     */
    protected int wait;
    /**
     * Whether or not the {@link File} should be watched recursively.
     */
    protected boolean recursive;

    /**
     * The {@link Thread} which watches for changes.
     */
    protected Thread thread;
    /**
     * A mapping between the files in the directory and the last time at which they'd been changed.
     */
    protected DefaultValueHashMap<File, Long> fileChangeMap;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param file      The {@link File} object to be watched.  If this is a regular file, this file is the only one to
     *                  be watched.  If it is a directory, the <code>recursive</code> parameter defines how it should be
     *                  handled.
     * @param wait      The number of milliseconds to wait between directory checks.  The lower this number, the more
     *                  responsive the watch will be, but the more demanding the watch will be on the filesystem.
     * @param recursive <code>true</code> if the provided {@link File} object should be watched, along with every
     *                  descendent of that {@link File} object; <code>false</code> if only the {@link File}'s direct
     *                  children should be watched.
     */
    public DirectoryWatcher(File file, int wait, boolean recursive)
    {
        super();
        this.file = file;
        this.wait = wait;
        this.recursive = recursive;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Called when a {@link File} is seen to have been changed.  This method is, of course, only called for regular
     * files.
     *
     * @param file The {@link File} which was changed.
     */
    public abstract void fileChanged(File file);

    /**
     * Starts the watching thread.  If the watching thread is already running, nothing happens.
     * @throws IOException If an I/O error occurs while creating the initial file list.
     */
    public synchronized void startWatching()
            throws IOException
    {
        if (thread == null)
        {
            thread = new Thread(this.toString() + " Watch Thread")
            {
                public void run()
                {
                    try
                    {
                        while (thread != null)
                        {
                            Set<File> files = getFiles();
                            // First, check the new image of the directory against the recorded one.
                            for (File file : files)
                            {
                                if (file.lastModified() != fileChangeMap.get(file))
                                {
                                    fileChanged(file);
                                    fileChangeMap.put(file, file.lastModified());
                                }
                            }
                            // Next, see if any files are not present in the new image which are in the recorded image.
                            for (File file : fileChangeMap.keySet().toArray(FileUtilities.EMPTY_FILE_ARRAY))
                            {
                                if (!files.contains(file))
                                {
                                    fileChanged(file);
                                    fileChangeMap.remove(file);
                                }
                            }
                            // Now perform the wait
                            try
                            {
                                Thread.sleep(wait);
                            } catch (InterruptedException e)
                            {
                            }
                        }
                    } catch (IOException e)
                    {
                        stopWatching();
                    }
                }
            };
            // Create initial mapping so we don't report the entire directory as a change initially
            fileChangeMap = new DefaultValueHashMap<File, Long>(0L);
            Set<File> files = getFiles();
            for (File file : files)
            {
                fileChangeMap.put(file, file.lastModified());
            }
            thread.start();
        }
    }

    /**
     * Stops the watching thread.  If the watching thread is not running, nothing happens.
     */
    public synchronized void stopWatching()
    {
        if (thread != null)
        {
            Thread thread = this.thread;
            this.thread = null;
            Utilities.safeJoin(thread);
        }
    }

    /**
     * Retrieves the set of files within the watched {@link File}.
     *
     * @return The {@link Set} of {@link File}s being watched.
     * @throws IOException If an I/O error occurs while listing the files.
     */
    public Set<File> getFiles()
            throws IOException
    {
        if (file.isDirectory())
        {
            if (recursive)
            {
                return FileUtilities.getRecursiveFiles(file);
            } else
            {
                HashSet<File> ret = new HashSet<File>();
                File[] files = file.listFiles();
                for (File file : files)
                {
                    ret.add(file);
                }
                return ret;
            }
        } else
        {
            if (file.exists())
            {
                return Collections.singleton(file);
            } else
            {
                return Collections.emptySet();
            }
        }
    }

    /**
     * Produces a {@link String} which describes this message.
     *
     * @return A {@link String} which describes this message.
     */
    public String toString()
    {
        return "Directory Watcher watching " + file + (recursive ? " recursive" : "");
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE