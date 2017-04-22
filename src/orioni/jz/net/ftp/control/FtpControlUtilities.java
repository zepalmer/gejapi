package orioni.jz.net.ftp.control;

/**
 * This class exists to contain common functionality for FTP control objects.
 * @author Zachary Palmer
 */
public class FtpControlUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Private constructor.  Prevents the instantiation of this class.
	 */
	private FtpControlUtilities()
	{
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

	/**
	 * Converts a standard pathname into an encoded pathname for the purposes of the FTP server.  File separators are
	 * still expected to be '<code>/</code>' in the input.  This method replaces all instances of the character \012
	 * with the character \000 and all instances of the character '<code>"</code>' with two of the same.
	 * @param pathname The unencoded pathname.
	 * @return The encoded equivalent.
	 */
	public static String encodePathname(String pathname)
	{
		StringBuffer sb = new StringBuffer();
		char[] chs = pathname.toCharArray();
        for (final char ch : chs)
        {
            switch (ch)
            {
                case '"':
                    sb.append("\"\"");
                    break;
                case 10:
                    sb.append((char) (0));
                    break;
                default:
                    sb.append(ch);
            }
        }
		return sb.toString();
	}

	/**
	 * Converts an FTP encoded pathname into an unencoded pathname.  File separators will not be affected.  This method
	 * replaces all instances of "<code>""</code>" with '<code>"</code>' and all instances of \000 with \012.
	 * @param encodedPathname The encoded pathname.
	 * @return The unencoded pathname.
	 */
	public static String decodePathname(String encodedPathname)
	{
		StringBuffer sb = new StringBuffer();
		char[] ch = encodedPathname.toCharArray();
		for (int i = 0; i < ch.length; i++)
		{
			switch (ch[0])
			{
				case '"':
					sb.append('"');
					i++;
					break;
				case 0:
					sb.append((char)(12));
					break;
				default:
					sb.append(ch[i]);
			}
		}
		return sb.toString();
	}

}