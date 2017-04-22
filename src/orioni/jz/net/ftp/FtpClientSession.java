package orioni.jz.net.ftp;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class represents an FTP session within this JVM.  This session is capable of directly representing the FTP
 * protocol; the methods that are available represent the FTP protocol operations and verbs.
 * <p/>
 * This object allows its creator to specify an {@link OutputStream} on construction to which FTP communications will be
 * logged.  The data socket is not logged.  Writes to the logging stream will be in the form of: <UL><UL>[<i>t</i>]:
 * <i>m</i></UL></UL> where <i>t</i> indicates the transmission type ("c" if sent by the client, "s" if sent by the
 * server) and <i>m</i> indicates the message that was sent.  Note that a space follows the colon in the string.
 *
 * @author Zachary Palmer
 */
public class FtpClientSession
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A string containing CRLF (Octal \015\012) for end-of-statement in FTP communication.
     */
    protected static final String CRLF_STRING = new String(new char[]{13, 10});

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The session's communication socket.
     */
    protected Socket communicationSocket;
    /**
     * The session's current data socket.  <code>null</code> if no data socket is open.
     */
    protected Socket dataSocket;

    /**
     * The {@link PrintStream} to which logging data is written.
     */
    protected PrintStream loggingStream;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param log The {@link OutputStream} to which all communication socket transmissions will be written for logging
     *            purposes.  This value may be <code>null</code> if this service is not desired.
     */
    public FtpClientSession(OutputStream log)
    {
        super();
        loggingStream = new PrintStream(log);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Opens a connection to an FTP server.  This method may only be called successfully once per {@link
     * FtpClientSession} object.
     *
     * @param hostname The hostname of the FTP server.
     * @param port     The port number on which to connect to the FTP server.
     * @return The server's provided {@link FtpResponse}<code>[]</code> in response to the established communication
     *         socket.
     * @throws IOException           If an I/O error occurs while attempting to connect to the server.
     * @throws UnknownHostException  If the specified host cannot be resolved.
     * @throws IllegalStateException If this method has already been called successfully on this {@link
     *                               FtpClientSession}.
     */
    public FtpResponse[] open(String hostname, int port)
            throws IOException, UnknownHostException, IllegalStateException
    {
        if (communicationSocket == null)
        {
            communicationSocket = new Socket(hostname, port);
        } else
        {
            throw new IllegalStateException("Session has previously been opened.");
        }
        return readResponseChain();
    }

// NON-STATIC METHODS : FTP PROTOCOL /////////////////////////////////////////////

    /**
     * This method implements the USER verb of the FTP protocol.  The specified username is submitted to the server for
     * this session.
     *
     * @param username The username to use for this session on the FTP server.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after this call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] user(String username)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("USER " + username);
    }

    /**
     * This method implements the PASS verb of the FTP protocol.  The specified password is submitted to the server for
     * this session.  This method should only be called immediately after {@link FtpClientSession#user(String)}.
     *
     * @param password The password to use in conjunction with the previously supplied username.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after this call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] pass(String password)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("PASS " + password);
    }

    /**
     * This method implements the QUIT verb of the FTP protocol.  This informs the FTP server that this client will be
     * breaking off communications.
     *
     * @return The {@link FtpResponse}<code>[]</code> received from the server after this call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] quit()
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("QUIT");
    }

    /**
     * This method implements the LIST verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  A listing of the specified pathname is
     * retrieved and written to the provided {@link OutputStream}.  The server's {@link FtpResponse} indicates whether
     * or not this listing is complete.
     *
     * @param pathname The encoded pathname for which a directory listing is required.  If this value is
     *                 <code>null</code>, a listing of the present working directory is provided.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after this call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] list(String pathname, OutputStream os)
            throws IOException
    {
        openDataConnection();
        if (pathname == null)
        {
            sendCommunication("LIST");
        } else
        {
            sendCommunication("LIST " + pathname);
        }
        ArrayList ret = new ArrayList();
        // NOTE: to ensure no deadlock, the client must read the next response line.  If that response line is an
        // error, the transfer cannot be performed and the client must abort.
        FtpResponseLine responseLine = FtpResponse.getNextResponseLine(communicationSocket.getInputStream());
        boolean isMark = responseLine.isMark();
        boolean tryForMore = true;
        if (responseLine.isTerminating())
        {
            tryForMore = !responseLine.isError();
            FtpResponse response = new FtpResponse(
                    new ByteArrayInputStream((responseLine.getSourceString() + "\n").getBytes()));
            ret.add(response);
            logResponse(response);
            responseLine = null;
        }
        if (isMark)
        {
            try
            {
                consumeDataConnection(os);
            } catch (IOException ioe)
            {
                dataSocket = null;
                // This error will be relayed to us in the form of server-side error messages.
                // TODO: report this error message in the logging system somehow
            }
        } else
        {
            try
            {
                dataSocket.close();
            } catch (IOException ioe)
            {
            }
            dataSocket = null;
        }
        if (responseLine != null)
        {
            FtpResponse partial = new FtpResponse(communicationSocket.getInputStream());
            FtpResponseLine[] lines = new FtpResponseLine[partial.getResponseLines().length + 1];
            lines[0] = responseLine;
            System.arraycopy(partial.getResponseLines(), 0, lines, 1, partial.getResponseLines().length);
            FtpResponse response = new FtpResponse(lines);
            ret.add(response);
            logResponse(response);
        }
        if (tryForMore)
        {
            FtpResponse[] chain = readResponseChain();
            for (final FtpResponse response : chain)
            {
                ret.add(response);
            }
        }
        return (FtpResponse[]) (ret.toArray(new FtpResponse[0]));
    }

    /**
     * This method implements the CWD verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  The current working directory is either
     * changed to the specified directory or an error message is returned.
     *
     * @param pathname The pathname of the directory into which the client will enter.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] cwd(String pathname)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("CWD " + pathname);
    }

    /**
     * This method implements the PWD verb of the FTP protocol.  The server will respond with the present working
     * directory for the connection in an encoded pathname (a pathname containing octal \000 for all octal \012).
     *
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] pwd()
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("PWD");
    }

    /**
     * This method implements the CDUP verb of the FTP protocol.  The server will move the current working directory up
     * one level of the directory hierarchy.
     *
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] cdup()
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("CDUP");
    }

    /**
     * This method implements the RETR verb of the FTP protocol.  This retrieves the file with the specified encoded
     * pathname (a pathname containing octal \000 for all octal \012) and stores it locally using the given {@link
     * OutputStream}.  <B>Note:</B> Some FTP servers require a "REST 0" after failed RETR verbs.  It is therefore safest
     * to execute a REST before every RETR.
     *
     * @param pathname The encoded pathname of the file to retrieve.
     * @param target   The {@link OutputStream} to which the file will be written.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurs.
     */
    public FtpResponse[] retr(String pathname, OutputStream target)
            throws IOException
    {
        openDataConnection();
        sendCommunication("RETR " + pathname);
        ArrayList ret = new ArrayList();
        // NOTE: to ensure no deadlock, the client must read the next response line.  If that response line is an
        // error, the transfer cannot be performed and the client must abort.
        FtpResponseLine responseLine = FtpResponse.getNextResponseLine(communicationSocket.getInputStream());
        boolean isMark = responseLine.isMark();
        boolean tryForMore = true;
        if (responseLine.isTerminating())
        {
            tryForMore = !responseLine.isError();
            FtpResponse response = new FtpResponse(
                    new ByteArrayInputStream((responseLine.getSourceString() + "\n").getBytes()));
            ret.add(response);
            logResponse(response);
            responseLine = null;
        }
        if (isMark)
        {
            try
            {
                consumeDataConnection(target);
            } catch (IOException ioe)
            {
                dataSocket = null;
                // This error will be relayed to us in the form of server-side error messages.
                // TODO: report this error message in the logging system somehow
            }
        } else
        {
            try
            {
                dataSocket.close();
            } catch (IOException ioe)
            {
            }
            dataSocket = null;
        }
        if (responseLine != null)
        {
            FtpResponse partial = new FtpResponse(communicationSocket.getInputStream());
            FtpResponseLine[] lines = new FtpResponseLine[partial.getResponseLines().length + 1];
            lines[0] = responseLine;
            System.arraycopy(partial.getResponseLines(), 0, lines, 1, partial.getResponseLines().length);
            FtpResponse response = new FtpResponse(lines);
            ret.add(response);
            logResponse(response);
        }
        if (tryForMore)
        {
            FtpResponse[] chain = readResponseChain();
            for (final FtpResponse response : chain)
            {
                ret.add(response);
            }
        }
        return (FtpResponse[]) (ret.toArray(new FtpResponse[0]));
    }

    /**
     * This method implements the REST verb of the FTP protocol.  The next file transfer which occurs will start at the
     * specified offset in the file.
     *
     * @param offset The offset parameter of the REST command.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException              If an I/O error occurs.
     * @throws IllegalArgumentException If <code>offset</code> is negative.
     */
    public FtpResponse[] rest(long offset)
            throws IOException, IllegalArgumentException
    {
        if (offset < 0) throw new IllegalArgumentException("REST offset cannot be less than zero.");
        return sendCommunicationAndAwaitResponse("REST " + String.valueOf(offset));
    }

    /**
     * This method implements the NOOP verb of the FTP protocol.  As expected, a NOOP is sent to the server which, in
     * response, will do nothing but acknowledge the NOOP.
     *
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurs.
     */
    public FtpResponse[] noop()
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("NOOP");
    }

    /**
     * This method implements the STOR verb of the FTP protocol.  The provided {@link InputStream} is exhausted by
     * sending its entire contents to the servre over a data connection into a file with the specified encoded pathname
     * (a pathname containing octal \000 for all octal \012).
     *
     * @param source   The source of the data to send.
     * @param pathname The encoded pathname to be used when storing the file on the server.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurs.
     */
    public FtpResponse[] stor(InputStream source, String pathname)
            throws IOException
    {
        openDataConnection();
        sendCommunication("STOR " + pathname);
        ArrayList ret = new ArrayList();
        // NOTE: to ensure no deadlock, the client must read the next response line.  If that response line is an
        // error, the transfer cannot be performed and the client must abort.
        FtpResponseLine responseLine = FtpResponse.getNextResponseLine(communicationSocket.getInputStream());
        boolean isMark = responseLine.isMark();
        boolean tryForMore = true;
        if (responseLine.isTerminating())
        {
            tryForMore = !responseLine.isError();
            FtpResponse response = new FtpResponse(
                    new ByteArrayInputStream((responseLine.getSourceString() + "\n").getBytes()));
            ret.add(response);
            logResponse(response);
            responseLine = null;
        }
        if (isMark)
        {
            try
            {
                fuelDataConnection(source);
            } catch (IOException ioe)
            {
                dataSocket = null;
                // This error will be relayed to us in the form of server-side error messages.
                // TODO: report this error message in the logging system somehow
            }
        } else
        {
            try
            {
                dataSocket.close();
            } catch (IOException ioe)
            {
            }
            dataSocket = null;
        }
        if (responseLine != null)
        {
            FtpResponse partial = new FtpResponse(communicationSocket.getInputStream());
            FtpResponseLine[] lines = new FtpResponseLine[partial.getResponseLines().length + 1];
            lines[0] = responseLine;
            System.arraycopy(partial.getResponseLines(), 0, lines, 1, partial.getResponseLines().length);
            FtpResponse response = new FtpResponse(lines);
            ret.add(response);
            logResponse(response);
        }
        if (tryForMore)
        {
            FtpResponse[] chain = readResponseChain();
            for (final FtpResponse response : chain)
            {
                ret.add(response);
            }
        }
        return (FtpResponse[]) (ret.toArray(new FtpResponse[0]));
    }

    /**
     * This method implements the APPE verb of the FTP protocol.  The provided {@link InputStream} is exhausted by
     * sending its entire contents to the servre over a data connection into a file with the specified encoded pathname
     * (a pathname containing octal \000 for all octal \012).
     * <p/>
     * This method functions exactly like {@link FtpClientSession#stor(InputStream, String)}, except that, as per FTP,
     * the specified file is appended with the contents of the {@link InputStream} in the event that it already exists.
     *
     * @param source   The source of the data to send.
     * @param pathname The encoded pathname to be used when storing the file on the server.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurs.
     */
    public FtpResponse[] appe(InputStream source, String pathname)
            throws IOException
    {
        openDataConnection();
        sendCommunication("APPE " + pathname);
        ArrayList ret = new ArrayList();
        // NOTE: to ensure no deadlock, the client must read the next response line.  If that response line is an
        // error, the transfer cannot be performed and the client must abort.
        FtpResponseLine responseLine = FtpResponse.getNextResponseLine(communicationSocket.getInputStream());
        boolean isMark = responseLine.isMark();
        boolean tryForMore = true;
        if (responseLine.isTerminating())
        {
            tryForMore = !responseLine.isError();
            FtpResponse response = new FtpResponse(
                    new ByteArrayInputStream((responseLine.getSourceString() + "\n").getBytes()));
            ret.add(response);
            logResponse(response);
            responseLine = null;
        }
        if (isMark)
        {
            try
            {
                fuelDataConnection(source);
            } catch (IOException ioe)
            {
                dataSocket = null;
                // This error will be relayed to us in the form of server-side error messages.
                // TODO: report this error message in the logging system somehow
            }
        } else
        {
            try
            {
                dataSocket.close();
            } catch (IOException ioe)
            {
            }
            dataSocket = null;
        }
        if (responseLine != null)
        {
            FtpResponse partial = new FtpResponse(communicationSocket.getInputStream());
            FtpResponseLine[] lines = new FtpResponseLine[partial.getResponseLines().length + 1];
            lines[0] = responseLine;
            System.arraycopy(partial.getResponseLines(), 0, lines, 1, partial.getResponseLines().length);
            FtpResponse response = new FtpResponse(lines);
            ret.add(response);
            logResponse(response);
        }
        if (tryForMore)
        {
            FtpResponse[] chain = readResponseChain();
            for (final FtpResponse response : chain)
            {
                ret.add(response);
            }
        }
        return (FtpResponse[]) (ret.toArray(new FtpResponse[0]));
    }

    /**
     * This method implements the MKD verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  The directory specified by the encoded
     * pathname is created.
     *
     * @param pathname The pathname of the directory into which the client will enter.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] mkd(String pathname)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("MKD " + pathname);
    }

    /**
     * This method implements the RMD verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  The directory specified by the encoded
     * pathname is removed.  For this command to work, the directory must be empty.
     *
     * @param pathname The pathname of the directory into which the client will enter.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] rmd(String pathname)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("RMD " + pathname);
    }

    /**
     * This method implements the DELE verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  The file specified by the encoded
     * pathname is removed.
     *
     * @param pathname The pathname of the directory into which the client will enter.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] dele(String pathname)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("DELE " + pathname);
    }

    /**
     * This method implements the RNFR verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  The specified file is made the target of
     * a rename and will be renamed to the encoded pathname provided by the next RNTO verb.
     *
     * @param pathname The pathname of the directory into which the client will enter.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] rnfr(String pathname)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("RNFR " + pathname);
    }

    /**
     * This method implements the RNTO verb of the FTP protocol.  The provided parameter is sent to the server as an
     * encoded pathname (a pathname containing octal \000 for all octal \012).  The file specified by the last verb
     * (which is required to be an RNFR verb) is renamed to the encoded pathname provided.
     *
     * @param pathname The pathname of the directory into which the client will enter.
     * @return The {@link FtpResponse}<code>[]</code> received from the server after the call.
     * @throws IOException If an I/O error occurred.
     */
    public FtpResponse[] rnto(String pathname)
            throws IOException
    {
        return sendCommunicationAndAwaitResponse("RNTO " + pathname);
    }

// NON-STATIC METHODS : GENERAL FTP FUNCTIONALITY ////////////////////////////////

    /**
     * Opens a passive data connection to the FTP server.
     *
     * @throws FtpException If an FTP-specific error occurred.
     * @throws IOException  If an I/O exception occurs while opening the passive data connection.
     */
    protected void openDataConnection()
            throws FtpException, IOException
    {
        FtpResponse[] response = sendCommunicationAndAwaitResponse("PASV");
        if (response[response.length - 1].isAccept())
        {
            // Now we must determine the port info.  It is in the format:
            //      ###[.*]a1,a2,a3,a4,p1,p2[.*]
            // Where # is a digit, [.*] is any number of characters, and a1, a2, a3, a4, p1, and p2 are integer
            // values.  The characters following the first three digits are guaranteed to be non-digit characters.
            // Therefore, scan for the first digit after the response code.
            String responseString = response[response.length - 1].getLastResponseLine().getString();
            char[] responseStringChars = responseString.toCharArray();
            // Skip to the first digit, cutting off the part before it
            int index = 0;
            try
            {
                while ((responseStringChars[index] < '0') || (responseStringChars[index] > '9'))
                {
                    index++;
                }
                responseString = new String(responseStringChars, index, responseStringChars.length - index);
            } catch (IndexOutOfBoundsException ioobe)
            {
                throw new IOException(
                        "FTP server returned malformed PASV response: " +
                        response[response.length - 1].getLastResponseLine().getSourceString());
            }
            // Begin parsing data
            int a1, a2, a3, a4, p1, p2;
            try
            {
                // Parse next digit
                index = responseString.indexOf(',');
                a1 = Integer.parseInt(responseString.substring(0, index));
                responseString = responseString.substring(index + 1);
                index = responseString.indexOf(',');
                a2 = Integer.parseInt(responseString.substring(0, index));
                responseString = responseString.substring(index + 1);
                index = responseString.indexOf(',');
                a3 = Integer.parseInt(responseString.substring(0, index));
                responseString = responseString.substring(index + 1);
                index = responseString.indexOf(',');
                a4 = Integer.parseInt(responseString.substring(0, index));
                responseString = responseString.substring(index + 1);
                index = responseString.indexOf(',');
                p1 = Integer.parseInt(responseString.substring(0, index));
                responseString = responseString.substring(index + 1);
                // Now we don't have a comma to work with; we have to trail to the end of the numeric characters
                responseStringChars = responseString.toCharArray();
                index = 0;
                while ((responseStringChars[index] >= '0') && (responseStringChars[index] <= '9'))
                {
                    index++;
                }
                p2 = Integer.parseInt(responseString.substring(0, index));
            } catch (NumberFormatException nfe)
            {
                throw new IOException(
                        "FTP server returned PASV response with bad integer: " +
                        response[response.length - 1].getLastResponseLine().getSourceString());
            }
            // Port data parsed.  The data port is at a1.a2.a3.a4:((p1*256)+p2)
            dataSocket = new Socket(a1 + "." + a2 + "." + a3 + "." + a4, p1 * 256 + p2);
        } else
        {
            throw new FtpException(
                    "FTP server could not complete opening of data connection.",
                    FtpException.ERROR_TYPE_DATA_CONNECTION_COULD_NOT_BE_OPENED);
        }
    }

    /**
     * Retrieves all of the data from the data socket.  This method will continue to read from the data socket until end
     * of stream or until an exception occurs.  The read data is written to the provided {@link OutputStream}. After
     * this method has finished, it closes the data connection and <code>null</code>s the variable to indicate that the
     * data connection has expired.
     * <p/>
     * In the event that this method throws an exception, the provided output stream may have received no data, some
     * data, or all of the data retrieved by the data connection.  This data should probably be considered incomplete in
     * any case.
     * <p/>
     * If an exception is thrown by this method, the user is promised that the data connection will be closed in any
     * case.
     *
     * @param os The target {@link OutputStream} to which data will be written.
     * @throws IOException           If an I/O error occurred while reading the data.
     * @throws IllegalStateException If there is no open data socket.
     */
    protected void consumeDataConnection(OutputStream os)
            throws IOException, IllegalStateException
    {
        try
        {
            if (dataSocket == null)
            {
                throw new IllegalStateException("No data connection established.");
            }
            byte[] dataBuffer = new byte[8192];  // 8K data buffer
            InputStream source = dataSocket.getInputStream();

            int read = source.read(dataBuffer);
            while (read != -1)
            {
                os.write(dataBuffer, 0, read);
                read = source.read(dataBuffer);
            }
        } catch (IOException ioe)
        {
            throw ioe;
        } finally
        {
            try
            {
                if (dataSocket != null)
                {
                    dataSocket.close();
                }
            } catch (IOException ioe2)
            {
                // No one cares if the data connection didn't close.  It'll time out.
            }
            dataSocket = null;
        }
    }

    /**
     * Transmits all data found on the specified {@link InputStream} down the data socket.  This method will continue to
     * write to the data socket until an end of stream is detected from the {@link InputStream} or until an exception
     * occurs.  After this method is finished, it closes the data connection and <code>null</code>s the variable to
     * indicate that the data connection has expired.
     * <p/>
     * In the event that this method throws an exception, the server may have received no data, some data, or all of the
     * data intended to be sent.  This data should probably be considered incomplete in any case.
     * <p/>
     * If an exception is thrown by this method, the user is promised that the data connection will be closed in any
     * case.
     *
     * @param is The target {@link InputStream} to which data will be written.
     * @throws IOException           If an I/O error occurred while reading the data.
     * @throws IllegalStateException If there is no open data socket.
     */
    protected void fuelDataConnection(InputStream is)
            throws IOException, IllegalStateException
    {
        try
        {
            if (dataSocket == null)
            {
                throw new IllegalStateException("No data connection established.");
            }
            byte[] dataBuffer = new byte[8192];  // 8K data buffer
            OutputStream target = dataSocket.getOutputStream();

            int read = is.read(dataBuffer);
            while (read != -1)
            {
                target.write(dataBuffer, 0, read);
                read = is.read(dataBuffer);
            }
        } catch (IOException ioe)
        {
            throw ioe;
        } finally
        {
            try
            {
                if (dataSocket != null)
                {
                    dataSocket.close();
                }
            } catch (IOException ioe2)
            {
                // No one cares if the data connection didn't close.  It'll time out.
            }
            dataSocket = null;
        }
    }

    /**
     * Sends the specified string over the communication socket.
     *
     * @param string The string to send over the communication socket.
     * @throws IOException If an I/O error occurs while writing to the socket.
     */
    protected void sendCommunication(String string)
            throws IOException
    {
        if (communicationSocket == null)
        {
            throw new IOException("Connection not open.");
        }
        if (loggingStream != null)
        {
            loggingStream.println("[c]: " + string);
        }
        if (!string.endsWith(CRLF_STRING))
        {
            string = string + CRLF_STRING;
        }
        communicationSocket.getOutputStream().write(string.getBytes());
    }

    /**
     * Sends the specified string over the communication socket and awaits for a standard, non-mark FTP response.
     *
     * @param string The string to send over the communication socket.
     * @return An array of {@link FtpResponse} objects representing the received responses.  This array will contain at
     *         least one element and the last element of that array will never be a mark.
     * @throws IOException If an I/O error occurs while writing to the socket or while receiving the responses.
     */
    protected FtpResponse[] sendCommunicationAndAwaitResponse(String string)
            throws IOException
    {
        sendCommunication(string);
        return readResponseChain();
    }

    /**
     * Waits for the FTP server to send a standard, non-mark FTP response.  The mark responses, if any, and the non-mark
     * response are gathered into an {@link FtpResponse}<code>[]</code>, logged if appropriate, and returned to the
     * caller.
     *
     * @return An array of {@link FtpResponse} objects representing the received responses.  This array will contain at
     *         least one element and the last element of that array will never be a mark.
     * @throws IOException If an I/O exception occurs while receiving the responses.
     */
    protected FtpResponse[] readResponseChain()
            throws IOException
    {
        ArrayList al = new ArrayList();
        FtpResponse response = new FtpResponse(communicationSocket.getInputStream());
        while (response.isMark())
        {
            al.add(response);
            response = new FtpResponse(communicationSocket.getInputStream());
        }
        al.add(response);
        FtpResponse[] ret = (FtpResponse[]) (al.toArray(FtpResponse.EMPTY_ARRAY));
        logResponses(ret);
        return ret;
    }

    /**
     * Writes an array of responses to the logging stream.
     *
     * @param responses An {@link FtpResponse}<code>[]</code> to write to the logging stream.
     */
    protected void logResponses(FtpResponse[] responses)
    {
        for (final FtpResponse response : responses)
        {
            logResponse(response);
        }
    }

    /**
     * Writes a response to the logging stream.
     *
     * @param response The {@link FtpResponse} to write to the logging stream.
     */
    protected void logResponse(FtpResponse response)
    {
        FtpResponseLine[] lines = response.getResponseLines();
        for (final FtpResponseLine line : lines)
        {
            loggingStream.println("[s]: " + line.getSourceString());
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}