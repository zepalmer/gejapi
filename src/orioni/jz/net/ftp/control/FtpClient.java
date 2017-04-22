package orioni.jz.net.ftp.control;

import orioni.jz.net.ftp.FtpClientSession;
import orioni.jz.net.ftp.FtpResponse;
import orioni.jz.net.ftp.FtpResponseLine;

import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;

/**
 * This class is meant to operate as a programmatic FTP client.  A connection can be established with an FTP server and
 * standard FTP operations can be performed.  This class makes use of the {@link FtpClientSession} class for most of its
 * functionality, adding value to it by organizing the calls in a logical manner so as to prevent the interfacer of this
 * class from having to work directly with the file transfer protocol.
 * @author Zachary Palmer
 */
public class FtpClient
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The {@link FtpClientSession} which is currently representing the connected state of this {@link FtpClient}. */
	protected FtpClientSession clientSession;
	/** The home directory of the server. */
	protected FtpFile root;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 */
	public FtpClient()
	{
		super();
		clientSession = null;
		root = null;
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * This method opens a connection using the specified hostname, port, username, and password.  If a connection to
	 * an FTP server is already open, an exception is thrown.  If an {@link IOException} or {@link FtpControlException}
	 * is thrown, the client returns to an unconnected state.
	 *
	 * @param hostname The hostname of the FTP server to which a connection should be made.
	 * @param port The port on which the FTP server is listening for connections.
	 * @param username The username to use when logging into the FTP server.
	 * @param password The password to use when logging into the FTP server.
	 * @param logStream The {@link OutputStream} to which events should be logged.
	 * @return The {@link FtpFile} which represents the root of the FTP server.
	 * @throws IOException If an I/O error occurs while opening the connection.
	 * @throws UnknownHostException If the specified hostname could not be resolved.
	 * @throws FtpControlException If a protocol-related failure occurs.
	 * @throws IllegalArgumentException If the client is already connected.
	 */
	public FtpFile open(String hostname, int port, String username, String password, OutputStream logStream)
	        throws IOException, FtpControlException, UnknownHostException, IllegalArgumentException
	{
		// TODO: adjust this method to return an FtpFile
		// That file should represent the home directory of the FTP server.  This does not require an immediate LIST;
		// it simply requires that an object be created which can represent the directory structure.
		try
		{
			clientSession = new FtpClientSession(logStream);
			FtpResponse[] responses = clientSession.open(hostname, port);
			if (!responses[responses.length - 1].isAccept())
			{
				clientSession = null;
				throw new FtpControlException(
				        FtpControlException.EXCEPTION_TYPE_CONNECTION_CLOSED_BEFORE_AUTHORIZATION,
				        "Connection was closed prematurely: " +
				        responses[responses.length - 1].getLastResponseLine().getSourceString());
			}
			responses = clientSession.user(username);
			if (!responses[responses.length - 1].isAccept())
			{
				clientSession = null;
				throw new FtpControlException(
				        FtpControlException.EXCEPTION_TYPE_AUTHORIZATION_FAILED,
				        "Username not accepted: "+
				        responses[responses.length - 1].getLastResponseLine().getSourceString(),
				        responses[responses.length - 1].getLastResponseLine().getResponseCode(),
				        responses[responses.length - 1].getLastResponseLine().getString());
			}
			responses = clientSession.pass(password);
			if (!responses[responses.length - 1].isAccept())
			{
				clientSession = null;
				throw new FtpControlException(
				        FtpControlException.EXCEPTION_TYPE_AUTHORIZATION_FAILED,
				        "Username or password not accepted: "+
				        responses[responses.length - 1].getLastResponseLine().getSourceString(),
				        responses[responses.length - 1].getLastResponseLine().getResponseCode(),
				        responses[responses.length - 1].getLastResponseLine().getString());
			}
			responses = clientSession.pwd();
			FtpResponseLine line = responses[responses.length - 1].getLastResponseLine();
			if (line.isError())
			{
				try
				{
					clientSession.quit();
				} catch (IOException ioe2)
				{
				}
				clientSession = null;
				throw new FtpControlException(
				        FtpControlException.EXCEPTION_TYPE_STARTING_DIRECTORY_NOT_PROVIDED,
				        "PWD denied to starting directory: "+
				        responses[responses.length - 1].getLastResponseLine().getSourceString());
			}
            // Otherwise, the response contains the PWD of the client.
            char[] ch = line.getString().toCharArray();
			StringBuffer sb = new StringBuffer();
			int index = 1;
			while (index<ch.length)
			{
				if (ch[index]=='"')
				{
					if ((index<ch.length-1) && (ch[index+1]=='"'))
					{
						// This is just a quote.
						sb.append(ch[index]);
						index+=2;
					} else
					{
						// We're done!
						break;
					}
				} else
				{
					sb.append(ch[index++]);
				}
			}
			root = new FtpFile(sb.toString(), clientSession, true, 0);
		} catch (IOException ioe)
		{
			try
			{
				clientSession.quit();
			} catch (IOException ioe2)
			{
			}
			clientSession = null;
			throw ioe;
		}
		return root;
	}

	/**
	 * This method closes this {@link FtpClient}'s current connection, if one exists.  If one does not, an exception is
	 * thrown.
	 * @throws IOException If an I/O error occurs while attempting to close the FTP connection.
	 * @throws IllegalArgumentException If the {@link FtpClient} is not currently connected.
	 */
	public void close()
			throws IOException, IllegalArgumentException
	{
		if (clientSession ==null)
		{
			throw new IllegalArgumentException("No client connection is currently open.");
		}
		try
		{
			clientSession.quit();
		} catch (IOException e)
		{
			throw e;
		} finally
		{
			clientSession = null;
		}
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}