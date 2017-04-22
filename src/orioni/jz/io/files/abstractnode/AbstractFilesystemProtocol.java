package orioni.jz.io.files.abstractnode;

import java.io.IOException;

/**
 * This interface defines the controls which must be provided by an object for it to represent a protocol within the
 * abstract file node framework.
 *
 * @author Zachary Palmer
 */
public interface AbstractFilesystemProtocol
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the name of this protocol.
     *
     * @return The name of this prtocol.
     */
    public String getName();

    /**
     * Determines whether or not this {@link AbstractFilesystemProtocol} implementation is capable of parsing the
     * specified connection string.
     * <p/>
     * Note that this method must be written to predict the behavior of {@link AbstractFilesystemProtocol#createConnection(String)}
     * properly.  If {@link AbstractFilesystemProtocol#createConnection(String)} will return an {@link
     * AbstractFilesystemConnection} properly when the specified connection string is provided, this method <i>must</i>
     * return <code>true</code>.  If {@link AbstractFilesystemProtocol#createConnection(String)} will throw an {@link
     * IllegalArgumentException} if provided the specified connection string, this method <i>must</i> return
     * <code>false</code>.  It is acceptable for this method to return <code>true</code> and for the {@link
     * AbstractFilesystemProtocol#createConnection(String)} method to then throw an {@link IOException}, as {@link
     * IOException}s may be unpredictable at the time this method is called.
     * <p/>
     * Please note that, when this method is called, no guarantee is made that {@link
     * AbstractFilesystemProtocol#createConnection(String)} will be called at any time.
     *
     * @param connectionString The connection string in question.
     * @return <code>true</code> if the connection string can be parsed; <code>false</code> otherwise.
     */
    public boolean canParse(String connectionString);

    /**
     * Creates a connection using this {@link AbstractFilesystemProtocol} object and the provided connection string. If
     * this {@link AbstractFilesystemProtocol} object cannot parse the specified connection string, an {@link
     * IllegalArgumentException} is thrown.
     *
     * @param connectionString The connection string which should be used to create a new connection.
     * @return The {@link AbstractFilesystemConnection} which represents the connection which was created.
     * @throws IllegalArgumentException If the connection string which provided to this {@link
     *                                  AbstractFilesystemProtocol} object was improperly formatted.
     * @throws IOException              If an I/O error occurs while attempting to establish the connection.
     */
    public AbstractFilesystemConnection createConnection(String connectionString)
            throws IllegalArgumentException, IOException;

}