package orioni.jz.net.ftp;

import java.io.IOException;

// TODO: Consider removing this class.  It doesn't necessarily contribute significant information.

/**
 * This class is designed to represent an exception which occurred during an FTP protocol communication.
 * @author Zachary Palmer
 */
public class FtpException extends IOException
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

	/** A constant indicating that a data connection could not be opened because the server responded with an
	 *  error. */
	public static final int ERROR_TYPE_DATA_CONNECTION_COULD_NOT_BE_OPENED = 1;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The error type that this {@link FtpException} represents. */
	protected int errorType;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Skeleton constructor.  Does not provide this exception with a message.
	 * @param errorType The error type of this exception.
	 */
	public FtpException(int errorType)
	{
		this("",errorType);
	}

	/**
	 * General constructor.
	 * @param s The message for this {@link FtpException} to contain.
	 * @param errorType The error type of this exception.
	 */
	public FtpException(String s, int errorType)
	{
		super(s);
		this.errorType = errorType;
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Retrieves the error type code for this {@link FtpException}, defined as one of the <code>ERROR_TYPE_XXXX</code>
	 * constants defined in the {@link FtpException} class.
	 * @return One of the <code>ERROR_TYPE_XXXX</code> constants defined in {@link FtpException}.
	 */
	public int getErrorType()
	{
		return errorType;
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}