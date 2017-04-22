package orioni.jz.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class represents a single complete response from an FTP server with regard to some action.  It contains at
 * least one {@link FtpResponseLine}; these are accessible through certain provided methods.
 * @author Zachary Palmer
 */
public class FtpResponse
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /** An empty {@link FtpResponse}<code>[]</code> used for array casting purposes. */
    public static final FtpResponse[] EMPTY_ARRAY = new FtpResponse[0];

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /** The list of {@link FtpResponseLine}s that comprise this {@link FtpResponse}. */
    protected ArrayList<FtpResponseLine> responseLines;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param is The input stream from which the response should be retrieved.
     * @throws IOException If an I/O error occurs while receiving this response.
     */
    public FtpResponse(InputStream is)
        throws IOException
    {
        super();
        responseLines = new ArrayList<FtpResponseLine>();
        FtpResponseLine line = getNextResponseLine(is);
        responseLines.add(line);
        while (!line.isTerminating())
        {
            line = getNextResponseLine(is);
            responseLines.add(line);
        }
    }

    /**
     * General constructor.  Creates the {@link FtpResponse} from an {@link FtpResponseLine}<code>[]</code>.
     * @param lines The {@link FtpResponseLine}<code>[]</code> from which the response will be created.
     */
    public FtpResponse(FtpResponseLine[] lines)
    {
        responseLines = new ArrayList<FtpResponseLine>();
        for (final FtpResponseLine line : lines)
        {
            responseLines.add(line);
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This private method is designed to retrieve a response line from the provided stream and assemble an
     * appropriate object to contain it in a usable fashion.
     * @param is The {@link InputStream} from which the response line is to be read.
     * @return An {@link FtpResponseLine} containing the line of data.
     * @throws IOException If an I/O exception occured in retrieving the response.
     */
    protected static FtpResponseLine getNextResponseLine(InputStream is)
        throws IOException
    {
        StringBuffer sb = new StringBuffer();
        int i = is.read();
        while ((i!=-1) && (i!=10) && (i!=13))
        {
            sb.append((char)i);
            i = is.read();
        }
        if (i==-1)
        {
            throw new IOException("Unexpected end of stream while reading response line.");
        }
        // Allow CR-LF
        if (i==13)
        {
            i = is.read();
        }
        if (i!=10)
        {
            throw new IOException("Badly formatted response from server: bailing out.");
        }
        return new FtpResponseLine(sb.toString());
    }

    /**
     * Retrieves the response line objects from this response.
     * @return An {@link FtpResponseLine}<code>[]</code> containing all of the {@link FtpResponseLine} objects which
     *         comprise this response.
     */
    public FtpResponseLine[] getResponseLines()
    {
        return responseLines.toArray(FtpResponseLine.EMPTY_ARRAY);
    }

    /**
     * Determines whether or not this response is an acceptance response.
     * @return <code>true</code> if this response is an acceptance response; <code>false</code> if this response
     *         indicates a failure.
     */
    public boolean isAccept()
    {
        return responseLines.get(responseLines.size()-1).isAccept();
    }

    /**
     * Determines whether or not this response is a failure response.
     * @return <code>true</code> if this response is a failure response; <code>false</code> if this response indicates
     *         an acceptance.
     */
    public boolean isError()
    {
        return responseLines.get(responseLines.size()-1).isError();
    }

    /**
     * Determines whether or not this response is a mark response.
     * @return <code>true</code> if this response is an acceptance response; <code>false</code> if this response
     *         indicates a failure.
     */
    public boolean isMark()
    {
        return responseLines.get(responseLines.size()-1).isMark();
    }

    /**
     * Retrieves the response code of the last line in this response chain.
     * @return The last response code of this response.
     */
    public int getLastResponseCode()
    {
        return responseLines.get(responseLines.size()-1).getResponseCode();
    }

    /**
     * Retrieves the last response line in this response chain.
     * @return The last {@link FtpResponseLine} in this response chain.
     */
    public FtpResponseLine getLastResponseLine()
    {
        return responseLines.get(responseLines.size()-1);
    }

    /**
     * Converts all of the {@link FtpResponseLine}s associated with this {@link FtpResponse} into a large string and
     * returns that string.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < responseLines.size(); i++)
        {
            sb.append(responseLines.get(i).getSourceString());
            if (i<responseLines.size()-1) sb.append('\n');
        }
        return sb.toString();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}