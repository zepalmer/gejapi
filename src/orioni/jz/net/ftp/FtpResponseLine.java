package orioni.jz.net.ftp;

/**
 * This class represents a single response line received from an FTP server.  Response lines are divided into the
 * response code and the text provided.
 * @author Zachary Palmer
 */
public class FtpResponseLine
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

	/** An <code>FtpResponseLine[0]</code> used for <code>toArray()</code> casting. */
	public static final FtpResponseLine[] EMPTY_ARRAY = new FtpResponseLine[0];

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The FTP server response code. */
	protected int responseCode;
	/** The string that followed the response code. */
	protected String string;
	/** The source string which generated this response line. */
	protected String sourceString;
    /** Determines whether or not this response line was a terminating response line. */
	protected boolean terminating;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param string The string that was received from the FTP server.
	 * @throws IllegalArgumentException If the string provided is not a valid FTP server response.
	 */
	public FtpResponseLine(String string)
	{
		super();
		sourceString = string;
		if (string.length()<3)
		{
			throw new IllegalArgumentException("String too short.");
		}
		terminating = true;
		while (string.startsWith(" "))
		{
			string = string.substring(1);
			terminating = false;
		}
		try
		{
			responseCode = Integer.parseInt(string.substring(0,3));
		} catch (NumberFormatException nfe)
		{
			throw new IllegalArgumentException("String's first three characters must be response code.");
		}
		string = string.substring(3);
		if (string.length()==0)
		{
			this.string = "";
		} else
		{
			if (string.startsWith("-"))
			{
				terminating = false;
			}
			this.string = string.substring(1);
		}
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Retrieves the response code received from the string.
	 * @return This response line's response code.
	 */
	public int getResponseCode()
	{
		return responseCode;
	}

	/**
	 * Retrieves the server's comment string which appeared after the response code.
	 * @return The server's comment string which appeared after the response code.
	 */
	public String getString()
	{
		return string;
	}

	/**
	 * Retrieves the string from which this object was created.
	 * @return The string from which this object was created.
	 */
	public String getSourceString()
	{
		return sourceString;
	}

	/**
	 * Determines whether or not this FTP response line is a terminating response line.
	 * @return <code>true</code> if this line is the last line of a response; <code>false</code> otherwise.
	 */
	public boolean isTerminating()
	{
		return terminating;
	}

	/**
	 * Determines whether or not this FTP response line is a mark.
	 * @return <code>true</code> if this FTP response line is a mark (100<=response code<=199); <code>false</code>
	 *         otherwise.
	 */
	public boolean isMark()
	{
		return ((responseCode >=100) && (responseCode <=199));
	}

	/**
	 * Determines whether or not this FTP response line is an acceptance response.
	 * @return <code>true</code> if this FTP response line is an acceptance (200<=response code<=399); <code>false</code>
	 *         otherwise.
	 */
	public boolean isAccept()
	{
		return ((responseCode >=200) && (responseCode <=399));
	}

	/**
	 * Determines whether or not this FTP response line is an error response.
	 * @return <code>true</code> if this FTP response line is an error (400<=response code<=599); <code>false</code>
	 *         otherwise.
	 */
	public boolean isError()
	{
		return ((responseCode >=400) && (responseCode <=599));
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}