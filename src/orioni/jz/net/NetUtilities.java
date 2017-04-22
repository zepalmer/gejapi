package orioni.jz.net;

import orioni.jz.io.IOUtilities;
import orioni.jz.io.files.FileUtilities;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

/**
 * This utilities class is designed to hold utility functions related to network operations.
 *
 * @author Zachary Palmer
 */
public class NetUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.
     */
    private NetUtilities()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Fetches a web resource with the provided URL.
     *
     * @param urlString The URL of the resource to retrieve.
     * @return A <code>byte[]</code> containing the obtained data.
     * @throws IOException If an I/O error occurs while retrieving the web resource.
     */
    public static byte[] getURL(String urlString)
            throws IOException
    {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtilities.pumpStream(is, baos);
        is.close();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * Retrieves a cachable web resource using the specified {@link File}.  If the {@link File} exists, its contents are
     * used; otherwise, the URL is obtained, stored in the {@link File}, and then returned.  If the {@link File} is a
     * directory, the URL is used to create a suitable {@link File}, replacing all '/' characters with the filesystem's
     * separator character.
     * <p/>
     * For examples, consult the following table. <table> <tr> <td><center><b>Path of File Provided</b></center></td>
     * <td><center><b>URL Provided</b></center></td> <td><center><b>Path of Cached File</b></center></td> </tr><tr>
     * <td><center>/tmp/cache/web.dat</center></td> <td><center>http://www.somesite.com/main.html</center></td>
     * <td><center>/tmp/cache/web.dat</center></td> </tr><tr> <td><center>/tmp/cache/</center></td>
     * <td><center>http://www.somesite.com/main.html</center></td> <td><center>/tmp/cache/www.somesite.com/main.html</center></td>
     * </tr><tr> <td><center>/tmp/cache/</center></td> <td><center>http://www.somesite.com/some/weird/backward/directory/in/the/sticks.html</center></td>
     * <td><center>/tmp/cache/www.somesite.com/some/weird/backward/directory/in/the/sticks.html</center></td> </tr>
     * </table>
     *
     * @param urlString The URL to use.
     * @param file       The cache file.
     * @return The contents of the URL as specified by the cache.
     * @throws IOException If an I/O exception occurs during the operaiton.
     */
    public static byte[] getCachedURL(String urlString, File file)
            throws IOException
    {
        if (file.isDirectory())
        {
            file = new File(
                    file.getPath() + File.separatorChar +
                    urlString.substring(urlString.indexOf("://") + 3).replaceAll(
                            Pattern.quote("/"), "\\" + File.separator));
        }
        if (file.isDirectory())
        {
            file = new File(file.getPath() + File.separatorChar + "__default");
        }
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getURL(urlString));
            fos.close();
        }
        return FileUtilities.getFileContents(file);
    }

    /**
     * This method is used to perform a Diffie-Hellman key exchange.  It allows two parties communicating over a network
     * socket to generate and agree upon an encryption key.  Third parties spying on the key exchange will not be
     * capable of discerning the key as a result of the difficulty of solving the discrete logarithm problem.
     * <p/>
     * For this operation to succeed, both sides must be calling this method with the same key length parameter but
     * different <code>initiator</code> parameters.
     *
     * @param socket    The {@link Socket} over which the communication is to take place.
     * @param length    The length (in bytes) of the key to generate.
     * @param initiator <code>true</code> if the caller is initiating the key exchange; <code>false</code> if the remote
     *                  side is initiating the key exchange.
     * @throws IOException              If an I/O error occurred while performing the exchange.
     * @throws IllegalArgumentException If the provided length is less than one.
     */
    public static byte[] performDiffieHellmanExchange(Socket socket, int length, boolean initiator)
            throws IOException
    {
        return IOUtilities.performDiffieHellmanExchange(
                socket.getInputStream(), socket.getOutputStream(), length, initiator);
    }
}

// END OF FILE
