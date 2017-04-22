package orioni.jz.common.exception;

/**
 * Thrown when a method does not have sufficient information to continue normal operation.  This may be because the
 * data source which the method was using has been exhausted.
 * @author Zachary Palmer
 */
public class InsufficientDataException extends RuntimeException
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Wrapper constructor.
	 * @see RuntimeException#RuntimeException()
	 */
	public InsufficientDataException()
	{
		super();
	}

	/**
	 * Wrapper constructor.
	 * @see RuntimeException#RuntimeException(String)
	 */
	public InsufficientDataException(String s)
	{
		super(s);
	}    

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}