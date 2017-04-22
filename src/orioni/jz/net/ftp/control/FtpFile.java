package orioni.jz.net.ftp.control;

import orioni.jz.io.files.RandomAccessFileOutputStream;
import orioni.jz.net.ftp.FtpClientSession;
import orioni.jz.net.ftp.FtpResponse;

import java.io.*;
import java.util.ArrayList;

// TODO: handle symbolic links!

/**
 * This class represents a remote file on an FTP server.  The server can be browsed by making calls to this object. The
 * {@link FtpFile} class is meant to function in a way very similar to the {@link File} class.
 * <p/>
 * {@link FtpFile} objects cannot be instantiated directly.  Instead, they must be created by an instance of the {@link
 * FtpClient} class.
 *
 * @author Zachary Palmer
 */
public class FtpFile
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A constant indicating that permission of the queried type is allowed.
     */
    public static final int PERMISSION_GRANTED = 0;
    /**
     * A constant indicating that permission of the queried type is not allowed.
     */
    public static final int PERMISSION_DENIED = 1;
    /**
     * A constant indicating that permission of the queried type is unknown.
     */
    public static final int PERMISSION_UNKNWON = 2;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The encoded pathname representing the path of the file represented by this object on the remote system.
     */
    protected String encodedPathname;
    /**
     * The decoded pathname representing the path of the file represented by this object on the remote system.
     */
    protected String decodedPathname;
    /**
     * The {@link FtpClientSession} which connects this {@link FtpFile} to the server.
     */
    protected FtpClientSession session;
    /**
     * Determines whether or not this {@link FtpFile} represents a directory.
     */
    protected boolean isDirectory;
    /**
     * The size in bytes of the file represented by this {@link FtpFile}.
     */
    protected long size;
    /**
     * Whether or not the connection which produced this {@link FtpFile} object has permission to read the file, as a
     * <code>FtpFile.PERMISSION_XXXX constant.
     */
    protected int readPermission;
    /**
     * Whether or not the connection which produced this {@link FtpFile} object has permission to write the file, as a
     * <code>FtpFile.PERMISSION_XXXX constant.
     */
    protected int writePermission;
    /**
     * Whether or not the connection which produced this {@link FtpFile} object has permission to execute the file, as a
     * <code>FtpFile.PERMISSION_XXXX constant.
     */
    protected int executePermission;
    /**
     * The {@link FtpFile} object which is the parent of this {@link FtpFile} object.
     */
    protected FtpFile parent;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.  Marks each of the permissions as "unknown."
     *
     * @param encodedPathname The encoded pathname of the file or directory represented by this object.
     * @param session         The {@link FtpClientSession} to use to receive information about the FTP server.
     * @param isDirectory     <code>true</code> if this {@link FtpFile} object represents a directory; <code>false</code>
     *                        if it represents a file.
     * @param size            The size in bytes of the file represented by this {@link FtpFile}.
     */
    FtpFile(String encodedPathname, FtpClientSession session, boolean isDirectory, long size)
    {
        super();
        if ((encodedPathname.endsWith("/")) && (encodedPathname.length() > 1))
        {
            encodedPathname = encodedPathname.substring(0, encodedPathname.length() - 1);
        }
        this.encodedPathname = encodedPathname;
        decodedPathname = FtpControlUtilities.decodePathname(this.encodedPathname);
        this.session = session;
        this.isDirectory = isDirectory;
        this.size = size;
        readPermission = PERMISSION_UNKNWON;
        writePermission = PERMISSION_UNKNWON;
        executePermission = PERMISSION_UNKNWON;
        // Determine parent
        String parentPath = decodedPathname.substring(0, decodedPathname.lastIndexOf(this.getName()));
        if ("".equals(parentPath))
        {
            parent = this;
        } else
        {
            parent = new FtpFile(FtpControlUtilities.encodePathname(parentPath), this.session, true, 0);
        }
    }

    /**
     * General constructor.
     *
     * @param encodedPathname The encoded pathname of the file or directory represented by this object.
     * @param session         The {@link FtpClientSession} to use to receive information about the FTP server.
     * @param isDirectory     <code>true</code> if this {@link FtpFile} object represents a directory; <code>false</code>
     *                        if it represents a file.
     * @param size            The size in bytes of the file represented by this {@link FtpFile}.
     * @param canRead         <code>true</code> if this file can be read; <code>false</code> otherwise.
     * @param canWrite        <code>true</code> if this file can be written; <code>false</code> otherwise.
     * @param canExecute      <code>true</code> if this file can be executed; <code>false</code> otherwise.
     * @param parent          The {@link FtpFile} representing the parent directory of this new {@link FtpFile}.
     */
    FtpFile(String encodedPathname, FtpClientSession session, boolean isDirectory, long size, boolean canRead,
            boolean canWrite, boolean canExecute, FtpFile parent)
    {
        super();
        if ((encodedPathname.endsWith("/")) && (encodedPathname.length() > 1))
        {
            encodedPathname = encodedPathname.substring(0, encodedPathname.length() - 1);
        }
        this.encodedPathname = encodedPathname;
        decodedPathname = FtpControlUtilities.decodePathname(this.encodedPathname);
        this.session = session;
        this.isDirectory = isDirectory;
        this.size = size;
        readPermission = canRead ? PERMISSION_GRANTED : PERMISSION_DENIED;
        writePermission = canWrite ? PERMISSION_GRANTED : PERMISSION_DENIED;
        executePermission = canExecute ? PERMISSION_GRANTED : PERMISSION_DENIED;
        this.parent = parent;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the pathname for the file represented by this object.
     *
     * @return The pathname for the file represented by this object.
     */
    public String getPath()
    {
        return decodedPathname;
    }

    /**
     * Determines the permission mode for reading this file as reported by the FTP server.  The returned value will be one
     * of the <code>FtpFile.PERMISSION_XXXX</code> constants.
     *
     * @return The permission mode for reading this file as one of the <code>FtpFile.PERMISSION_XXXX</code> constants.
     */
    public int canRead()
    {
        return readPermission;
    }

    /**
     * Determines the permission mode for writing this file as reported by the FTP server.  The returned value will be one
     * of the <code>FtpFile.PERMISSION_XXXX</code> constants.
     *
     * @return The permission mode for writing this file as one of the <code>FtpFile.PERMISSION_XXXX</code> constants.
     */
    public int canWrite()
    {
        return writePermission;
    }

    /**
     * Determines the permission mode for executing this file as reported by the FTP server.  The returned value will be
     * one of the <code>FtpFile.PERMISSION_XXXX</code> constants.
     *
     * @return The permission mode for executing this file as one of the <code>FtpFile.PERMISSION_XXXX</code> constants.
     */
    public int canExecute()
    {
        return executePermission;
    }

    /**
     * Determines whether or not this {@link FtpFile} object represents a directory.
     *
     * @return <code>true</code> if this object represents a directory; <code>false</code> otherwise.
     */
    public boolean isDirectory()
    {
        return isDirectory;
    }

    /**
     * Determines whether or not this {@link FtpFile} object represents a normal file.
     *
     * @return <code>true</code> if this object represents a normal file; <code>false</code> if it represents a directory.
     */
    public boolean isFile()
    {
        return !isDirectory;
    }

    /**
     * Determines the size of this file on the server, in bytes.
     *
     * @return The size of this file on the server, in bytes.
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Retrieves the name of the file or directory represented by this {@link FtpFile}.  This will be either
     * "<code>/</code>" (if the path is simply "<code>/</code>") or a name containing no '<code>/</code>' characters
     * whatsoever.  It is assumed that the file separation character is a '<code>/</code>'.
     */
    public String getName()
    {
        if ("/".equals(decodedPathname)) return "/";
        String pathname = decodedPathname;
        if ("/".equals(pathname.substring(pathname.length() - 1)))
        {
            pathname = pathname.substring(0, pathname.length() - 1);
        }
        int index = pathname.lastIndexOf('/');
        if (index == -1)
        {
            return decodedPathname;
        } else
        {
            return decodedPathname.substring(index + 1);
        }
    }

    /**
     * Retrieves the parent directory of this {@link FtpFile} as an {@link FtpFile}.
     *
     * @return The parent directory of this {@link FtpFile}.
     */
    public FtpFile getParent()
    {
        return parent;
    }

    /**
     * Retrieves an array of {@link FtpFile} objects which represent files that are contained within the directory
     * represented by this {@link FtpFile} object.  If this {@link FtpFile} object represents a file, then this method
     * returns <code>null</code>.  There is no guarantee that any of the returned {@link FtpFile} objects are in any form
     * of order, alphabetical or otherwise.
     *
     * @return An {@link FtpFile}<code>[]</code> representing the files contained within the directory represented by this
     *         {@link FtpFile}, or <code>null</code> if this {@link FtpFile} object represents a file rather than a
     *         directory.
     * @throws IOException           If an I/O error occurs.
     * @throws IllegalStateException If the FTP server returns a directory listing format which is unreadable by this
     *                               client.
     */
    public FtpFile[] listFtpFiles()
            throws IOException
    {
        if (isDirectory)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            session.list(encodedPathname, baos);
            String s = baos.toString();
            try
            {
                return translateUnixDirectoryFormat(s, this);
            } catch (IllegalArgumentException e)
            {
                throw new IllegalStateException(e.getMessage());
            }
        } else
        {
            return null;
        }
    }

    /**
     * Retrieves the file specified by this {@link FtpFile} object from the FTP server and stores it locally within the
     * designated stream.  This operation may take quite some time, given that the method call will only terminate when an
     * error occurs or when the transfer is complete.
     * <p/>
     * As per FTP protocol, calling this method when this {@link FtpFile} represents a directory will cause an FTP
     * <code>50x</code> error code response.  However, this library will perform the operation anyway to allow the library
     * user to control which communications occur.
     *
     * @param target The {@link OutputStream} into which the contents of the specified file will be deposited.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be transferred.
     */
    public void download(OutputStream target)
            throws IOException, FtpControlException
    {
        download(target, 0);
    }

    /**
     * Retrieves the file specified by this {@link FtpFile} object from the FTP server and stores it locally within the
     * designated file.  This operation may take quite some time, given that the method call will only terminate when an
     * error occurs or when the transfer is complete.
     * <p/>
     * As per FTP protocol, calling this method when this {@link FtpFile} represents a directory will cause an FTP
     * <code>50x</code> error code response.  However, this library will perform the operation anyway to allow the library
     * user to control which communications occur.
     *
     * @param file The {@link File} into which the contents of the specified file will be deposited.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be transferred.
     */
    public void download(File file)
            throws IOException, FtpControlException
    {
        download(file, 0);
    }

    /**
     * Retrieves the file specified by this {@link FtpFile} object from the FTP server and stores it locally within the
     * file with the designated pathname.  This operation may take quite some time, given that the method call will only
     * terminate when an error occurs or when the transfer is complete.
     * <p/>
     * As per FTP protocol, calling this method when this {@link FtpFile} represents a directory will cause an FTP
     * <code>50x</code> error code response.  However, this library will perform the operation anyway to allow the library
     * user to control which communications occur.
     *
     * @param pathname The pathname of the file into which the contents of the specified file will be deposited.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be transferred.
     */
    public void download(String pathname)
            throws IOException, FtpControlException
    {
        download(new File(pathname), 0);
    }

    /**
     * Retrieves the file specified by this {@link FtpFile} object from the FTP server and stores it locally within the
     * designated stream.  This operation may take quite some time, given that the method call will only terminate when an
     * error occurs or when the transfer is complete.
     * <p/>
     * As per FTP protocol, calling this method when this {@link FtpFile} represents a directory will cause an FTP
     * <code>50x</code> error code response.  However, this library will perform the operation anyway to allow the library
     * user to control which communications occur.
     * <p/>
     * The "<code>resume</code>" parameter specifies the offset at which to resume the transfer.  The previous part of the
     * file will not be transferred, nor will any data representing the previous part of the file be stored in the stream.
     *
     * @param target The {@link OutputStream} into which the contents of the specified file will be deposited.
     * @param resume The offset at which to resume the file.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be transferred.
     */
    public void download(OutputStream target, long resume)
            throws IOException, FtpControlException
    {
        FtpResponse[] responses = session.rest(resume);
        FtpResponse response = responses[responses.length - 1];
        // If resume is 0 and a syntax error occurs, ignore it; the server does not recognize REST, but this will not
        // affect the download.
        if ((response.isError()) &&
            ((((response.getLastResponseLine().getResponseCode() % 100) / 10 != 0)) || (resume > 0)))
        {
            throw new FtpControlException(
                    FtpControlException.EXCEPTION_TYPE_TRANSFER_ERROR_OCCURRED,
                    "Error occurred while resuming " + decodedPathname + " at offset " + resume,
                    response.getLastResponseLine().getResponseCode(),
                    response.getLastResponseLine().getString());
        }
        responses = session.retr(encodedPathname, target);
        response = responses[responses.length - 1];
        if (response.isError())
        {
            throw new FtpControlException(
                    FtpControlException.EXCEPTION_TYPE_TRANSFER_ERROR_OCCURRED,
                    "Error occurred while downloading file " + decodedPathname,
                    response.getLastResponseLine().getResponseCode(),
                    response.getLastResponseLine().getString());
        }
    }

    /**
     * Retrieves the file specified by this {@link FtpFile} object from the FTP server and stores it locally within the
     * designated file.  This operation may take quite some time, given that the method call will only terminate when an
     * error occurs or when the transfer is complete.
     * <p/>
     * As per FTP protocol, calling this method when this {@link FtpFile} represents a directory will cause an FTP
     * <code>50x</code> error code response.  However, this library will perform the operation anyway to allow the library
     * user to control which communications occur.
     * <p/>
     * The "<code>resume</code>" parameter specifies the offset at which to resume the file if the file exists.
     *
     * @param file   The {@link File} into which the contents of the specified file will be deposited.
     * @param resume The offset at which to resume the file if the file exists.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be transferred.
     */
    public void download(File file, long resume)
            throws IOException, FtpControlException
    {
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(resume);
        RandomAccessFileOutputStream stream = new RandomAccessFileOutputStream(raf, 16384);
        try
        {
            this.download(stream, resume);
        } catch (IOException ioe)
        {
            throw ioe;
        } catch (FtpControlException fce)
        {
            throw fce;
        } finally
        {
            try
            {
                stream.close();
            } catch (IOException ioe2)
            {
                // Ignore.
            }
            try
            {
                raf.close();
            } catch (IOException ioe2)
            {
                // Ignore... not much else we can do.
            }
        }
    }

    /**
     * Retrieves the file specified by this {@link FtpFile} object from the FTP server and stores it locally within the
     * file with the designated pathname.  This operation may take quite some time, given that the method call will only
     * terminate when an error occurs or when the transfer is complete.
     * <p/>
     * As per FTP protocol, calling this method when this {@link FtpFile} represents a directory will cause an FTP
     * <code>50x</code> error code response.  However, this library will perform the operation anyway to allow the library
     * user to control which communications occur.
     * <p/>
     * The "<code>resume</code>" parameter specifies the offset at which to resume the file if the file exists.
     *
     * @param pathname The local pathname of the file into which the contents of the specified file will be deposited.
     * @param resume   The offset at which to resume the file if the file exists.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be transferred.
     */
    public void download(String pathname, long resume)
            throws IOException, FtpControlException
    {
        download(new File(pathname), resume);
    }

    /**
     * Creates a directory within the directory represented by this {@link FtpFile}.  If this {@link FtpFile} does not
     * represent a directory, this operation will fail; it is attempted nonetheless.
     *
     * @param name The name of the directory as it will be created under this {@link FtpFile}.
     * @return An {@link FtpFile} representing the newly-created directory.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If a protocol-level error occurs, such as requesting that a directory be made as a child
     *                             of a file.
     */
    public FtpFile makeDirectory(String name)
            throws IOException, FtpControlException
    {
        String newName = encodedPathname + "/" + FtpControlUtilities.encodePathname(name);
        FtpResponse[] responses = session.mkd(newName);
        if (responses[responses.length - 1].isError())
        {
            throw new FtpControlException(
                    FtpControlException.EXCEPTION_TYPE_UNIDENTIFIED_ERROR, "Error during makeDirectory(" + name + ")",
                    responses[responses.length - 1].getLastResponseCode(),
                    responses[responses.length - 1].getLastResponseLine().getString());
        }
        FtpFile[] files;
        try
        {
            files = this.listFtpFiles();
            for (final FtpFile file : files)
            {
                if (file.getEncodedPath().equals(newName)) return file;
            }
        } catch (IOException ioe)
        {
        } catch (IllegalArgumentException iae)
        {
        }
        return new FtpFile(newName, session, true, 0);
    }

    /**
     * Uploads a file to the directory represented by this {@link FtpFile} object.  The specified {@link InputStream} is
     * read to exhaustion to provide the data connection with upload data; this data is then stored by the server in the
     * file with the specified path under the directory represented by this {@link FtpFile} object.  The {@link FtpFile}
     * object representing the newly uploaded file is then returned.  This method call will persist until the file is
     * uploaded and the data connection is closed.
     * <p/>
     * Note that the provided {@link InputStream} <i>must</i> terminate, or this call will not terminate.
     *
     * @param source   The {@link InputStream} from which data should be read.
     * @param filename The name of the file on the server.
     * @return The {@link FtpFile} object representing the file on the server.
     * @throws IOException         If an I/O error occurs.
     * @throws FtpControlException If the file is rejected or terminated by the server.
     */
    public FtpFile upload(InputStream source, String filename)
            throws IOException, FtpControlException
    {
        String pathname = encodedPathname + "/" + FtpControlUtilities.encodePathname(filename);
        FtpResponse[] response = session.stor(source, pathname);
        if (response[response.length - 1].isError())
        {
            throw new FtpControlException(
                    FtpControlException.EXCEPTION_TYPE_TRANSFER_ERROR_OCCURRED,
                    "Transfer error occurred while uploading " + pathname,
                    response[response.length - 1].getLastResponseCode(),
                    response[response.length - 1].getLastResponseLine().getString());
        }
        FtpFile[] files;
        try
        {
            files = this.listFtpFiles();
            for (final FtpFile file : files)
            {
                if (file.getEncodedPath().equals(pathname)) return file;
            }
        } catch (IOException ioe)
        {
        } catch (IllegalArgumentException iae)
        {
        }
        return new FtpFile(pathname, session, true, 0);
    }

    /**
     * Uploads a file to the directory represented by this {@link FtpFile} object.  The specified {@link File} is read to
     * exhaustion to provide the data connection with upload data; this data is then stored by the server in the file with
     * the specified path under the directory represented by this {@link FtpFile} object.  The {@link FtpFile} object
     * representing the newly uploaded file is then returned.  This method call will persist until the file is uploaded and
     * the data connection is closed.
     *
     * @param source   The {@link File} from which data should be read.
     * @param filename The name of the file on the server.
     * @return The {@link FtpFile} object representing the file on the server.
     * @throws IOException         If an I/O error occurs.  This may include if the local file does not exist.
     * @throws FtpControlException If the file is rejected or terminated by the server.
     */
    public FtpFile upload(File source, String filename)
            throws IOException, FtpControlException
    {
        FileInputStream fis = new FileInputStream(source);
        FtpFile ret = upload(fis, filename);
        fis.close();
        return ret;
    }

    /**
     * Uploads a file to the directory represented by this {@link FtpFile} object.  The file specified by the provided
     * local pathname is read to exhaustion to provide the data connection with upload data; this data is then stored by
     * the server in the file with the specified path under the directory represented by this {@link FtpFile} object.  The
     * {@link FtpFile} object representing the newly uploaded file is then returned.  This method call will persist until
     * the file is uploaded and the data connection is closed.
     *
     * @param localPathname The local pathname of the file from which data should be read.
     * @param filename      The name of the file on the server.
     * @return The {@link FtpFile} object representing the file on the server.
     * @throws IOException         If an I/O error occurs.  This may include if the local file does not exist.
     * @throws FtpControlException If the file is rejected or terminated by the server.
     */
    public FtpFile upload(String localPathname, String filename)
            throws IOException, FtpControlException
    {
        return upload(new File(localPathname), filename);
    }

// NON-STATIC PACKAGE METHODS ////////////////////////////////////////////////////

    /**
     * Retrieves the <i>encoded</i> pathname for the file represented by this object.
     *
     * @return The <i>encoded</i> pathname for the file represented by this object.
     */
    String getEncodedPath()
    {
        return encodedPathname;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Interprets the results of a standard UNIX-like directory listing and returns an array of {@link FtpFile} objects
     * which correspond to the contents.
     * @param listing A {@link String} containing the directory listing.
     * @param parent An {@link FtpFile} object representing the directory from which the listing was obtained.
     * @return An {@link FtpFile}<code>[]</code> containing objects which represent the files in the directory.
     * @throws IllegalArgumentException If the directory format provided could not be interpreted.
     */
    static FtpFile[] translateUnixDirectoryFormat(String listing, FtpFile parent)
            throws IllegalArgumentException
    {
        if (listing.endsWith("\n")) listing = listing.substring(0, listing.length() - 1);
        ArrayList ret = new ArrayList();
        String[] files = listing.split("\n");
        String parentPathname = parent.getEncodedPath();
        if (parentPathname.endsWith("/")) parentPathname = parentPathname.substring(0, parentPathname.length() - 1);
        for (int i = 0; i < files.length; i++)
        {
            if ((i > 0) || (!files[0].startsWith("total")))
            {
                char[] ch = files[i].toCharArray();
                boolean canRead = ((ch[1] == 'r') || (ch[4] == 'r') || (ch[7] == 'r'));
                boolean canWrite = ((ch[2] == 'w') || (ch[5] == 'w') || (ch[8] == 'w'));
                boolean canExecute = ((ch[3] == 'x') || (ch[6] == 'x') || (ch[9] == 'x'));
                // TODO: Note that the following will have to change when symbolic links are considered.
                boolean isDirectory = (ch[0] == 'd');
                // Now parse the rest of the string
                ch = files[i].substring(10).toCharArray();
                StringBuffer sb = new StringBuffer();
                try
                {
                    int index = 0;
                    while (ch[index] == ' ') index++;
                    // Next is number of links - useless to us
                    while (ch[index] != ' ') index++;
                    while (ch[index] == ' ') index++;
                    // Next is username - also useless
                    while (ch[index] != ' ') index++;
                    while (ch[index] == ' ') index++;
                    // Next is password - also useless
                    while (ch[index] != ' ') index++;
                    while (ch[index] == ' ') index++;
                    // Next is size - quite useful
                    while (ch[index] != ' ')
                    {
                        sb.append(ch[index++]);
                    }
                    String sizeString = sb.toString();
                    sb = new StringBuffer();
                    while (ch[index] == ' ') index++;
                    // Next is month - useless
                    while (ch[index] != ' ') index++;
                    while (ch[index] == ' ') index++;
                    // Next is day - also useless (but may not be in the future)
                    while (ch[index] != ' ') index++;
                    while (ch[index] == ' ') index++;
                    // Next is time - also useless
                    while (ch[index] != ' ') index++;
                    while (ch[index] == ' ') index++;
                    // Finally, the filename
                    while (index < ch.length)
                    {
                        if ((ch[index] != '\r') && (ch[index] != '\n')) sb.append(ch[index]);
                        index++;
                    }
                    String encodedPathname = sb.toString();
                    long size;
                    try
                    {
                        size = Long.parseLong(sizeString);
                    } catch (NumberFormatException nfe)
                    {
                        throw new IllegalArgumentException(
                                "Number format for file size was inappropriate     : " +
                                nfe.toString());
                    }
                    ret.add(
                            new FtpFile(
                                    parentPathname + "/" + encodedPathname, parent.session, isDirectory, size,
                                    canRead, canWrite, canExecute, parent));
                } catch (IndexOutOfBoundsException oob)
                {
                    throw new IllegalArgumentException(
                            "Unexpected end of directory listing string: " +
                            oob.getMessage());
				}
			}
		}
		return (FtpFile[]) (ret.toArray(new FtpFile[0]));
	}

}