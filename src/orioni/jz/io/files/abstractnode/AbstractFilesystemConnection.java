package orioni.jz.io.files.abstractnode;

import java.io.IOException;

/**
 * This interface is implemented by any class which can represent a connection to a filesystem.  This includes the
 * local filesystem, which is (presumably) never closed.
 * @author Zachary Palmer
 */
public interface AbstractFilesystemConnection
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Closes this connection.  If this connection is already closed, the return value indicates as such and nothing
     * else happens.
     * @return <code>true</code> if the connection has been closed; <code>false</code> if it was already closed.
     * @throws IOException If an I/O error occurs while closing the connection.  The connection may or may not be
     *                     closed if this exception is thrown.
     */
	public boolean close()
			throws IOException;

	/**
	 * Determines whether or not the connection is closed.
	 * @return <code>true</code> if this connection is closed; <code>false</code> otherwise.
	 */
	public boolean isClosed();

	/**
	 * Retrieves the connection string which was used to create this connection.  This information must be provided
	 * regardless of the open/closed state of the connection.
	 * @return The connection string which was used to create this connection.
	 */
	public String getConnectionString();

	/**
	 * Retrieves an instance of the class which implements {@link AbstractFilesystemProtocol} which was used to create
	 * this {@link AbstractFilesystemConnection} object.  This is not guaranteed to be the same instance on every call,
	 * but for all {@link AbstractFilesystemConnection} objects, this method will always return a class which can parse
	 * this connection's connection string.
	 */
	public AbstractFilesystemProtocol getProtocol();

	/**
	 * Retrieves all root file nodes for this connection.
	 * <P>
	 * If this connection has already been closed, this method may provide different content, including an
	 * {@link AbstractFileNode}<code>[0]</code>.  In general, however, this method should never return
	 * <code>null</code>.
	 *
	 * @return All {@link AbstractFileNode} objects which represent the root files within the filesystem to which this
	 *         connection is connected.
	 * @throws IOException If an I/O error occurs.
	 */
	public AbstractFileNode[] getRootNodes()
			throws IOException;
}