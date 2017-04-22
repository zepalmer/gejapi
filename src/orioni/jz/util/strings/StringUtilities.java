package orioni.jz.util.strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is designed to contain utilities for manipulating {@link String} objects.
 *
 * @author Zachary Palmer
 */
public class StringUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

    /**
     * A hex string conversion array.  For context about its use, please see {@link
     * Utilities#stringToHexString(String)}.  This array is lazily constructed because of memory concerns.
     */
    private static String[] hexStringConversions = null;
    /**
     * A hex string mapping containing double-digit hex strings as keys and their decimal values as {@link Integer}
     * objects.  This mapping is lazily constructed because of memory concerns.
     */
    private static HashMap<String, Integer> hexStringMapping = null;
    /**
     * An array mapping between characters and the strings which represent them in SQL.  This array can be indexed using
     * the character which identifies the original character in a SQL string.  If replaced with the string from this
     * array, that character can then be inlined in a string within a SQL statement.
     */
    public static final String[] SQL_STRING_MAPPING;
    /**
     * An array mapping between characters and the strings which represent them in HTML.  This array can be indexed
     * using the character which identifies the original character in an HTML string.  If replaced with the string from
     * this array, that character can then be inlined in a string within an HTML document body.
     */
    public static final String[] HTML_STRING_MAPPING;
    /**
     * A String array with no elements (length==0).
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

// CONSTANTS /////////////////////////////////////////////////////////////////////

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

    static
    {
        // *** Create mapping for SQL escape patterns
        SQL_STRING_MAPPING = new String[256];
        for (int i = 0; i < 256; i++)
        {
            if (i < 32)
            {
                StringUtilities.SQL_STRING_MAPPING[i] = "\\" + (char) (i);
            } else if (i == '\'')
            {
                StringUtilities.SQL_STRING_MAPPING[i] = "''";
            } else if (i == '\\')
            {
                StringUtilities.SQL_STRING_MAPPING[i] = "\\\\";
            } else
            {
                StringUtilities.SQL_STRING_MAPPING[i] = String.valueOf((char) i);
            }
        }
        // *** Create mapping for HTML escape patterns
        HTML_STRING_MAPPING = new String[256];
        for (int i = 0; i < 256; i++)
        {
            if (((i < 32) ||
                 (i == '<') ||
                 (i == '>') ||
                 (i == '#') ||
                 (i == '&') ||
                 (i == ';') ||
                 (i == '"') ||
                 (i > 126)) &&
                            (i != '\n'))   // exceptions... these are definitely safe
            {
                StringUtilities.HTML_STRING_MAPPING[i] = "&#" + i + ";";
            } else
            {
                StringUtilities.HTML_STRING_MAPPING[i] = String.valueOf((char) i);
            }
        }

    }

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.  Utilities classes are never instantiated.
     */
    private StringUtilities()
    {
        super();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Pads the left side of a string with the specified character until it is the specified length.  If the string is
     * already at least that length, it is returned without modification.
     *
     * @param string The string to pad.
     * @param ch     The character to use when padding.
     * @param size   The requested size of the returned string.
     * @return The padded stirng.
     */
    public static String padLeft(String string, char ch, int size)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(string);
        while (sb.length() < size)
        {
            sb.insert(0, ch);
        }
        return sb.toString();
    }

    /**
     * Pads the right side of a string with the specified character until it is the specified length.  If the string is
     * already at least that length, it is returned without modification.
     *
     * @param string The string to pad.
     * @param ch     The character to use when padding.
     * @param size   The requested size of the returned string.
     * @return The padded stirng.
     */
    public static String padRight(String string, char ch, int size)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(string);
        while (sb.length() < size)
        {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Creates a string which represents a hexidecimal dump of the provided byte array.  This method assumes that no
     * space skipping is needed.
     *
     * @param data The array of bytes from which to create a dump string.
     * @return The dump string created from the provided data.
     * @see StringUtilities#createHexDumpString(byte[],int)
     */
    public static String createHexDumpString(byte[] data)
    {
        return createHexDumpString(data, 0);
    }

    /**
     * Creates a string which represents a hexidecimal dump of the provided byte array.  This string is formatted with a
     * pair of hexadecimal digits for each byte followed by a character for each byte (using <code>.</code> for
     * characters which do not display).  Thus, a sample string could be:<BR> <UL><code>41 43 45 00  ACE.</code> </UL>
     * for a four-byte array.
     *
     * @param data   The array of bytes from which to create a dump string.
     * @param blanks The number of spaces for bytes to leave blank.  This is useful when one needs to create a hex dump
     *               string which contains less information than a larger string but needs to line up with the larger
     *               string's content.
     * @return The dump string created from the provided data.
     */
    public static String createHexDumpString(byte[] data, int blanks)
    {
        StringBuffer hex = new StringBuffer();
        StringBuffer dump = new StringBuffer();
        for (int i = 0; i < data.length - blanks; i++)
        {
            int now = data[i];
            if (now < 0) now += 256;
            String hexstring = Integer.toHexString(now);
            if (hexstring.length() < 2) hex.append('0');
            hex.append(hexstring.toUpperCase());
            hex.append(' ');
            if (now >= 32)
            {
                dump.append((char) now);
            } else
            {
                dump.append('.');
            }
        }
        for (int j = 0; j < blanks; j++)
        {
            hex.append("   ");
            dump.append(' ');
        }

        dump.insert(0, ' ');
        dump.insert(0, hex.toString());
        return dump.toString();
    }

    /**
     * Creates a formatted hex dump string.  This method uses the {@link StringUtilities#createHexDumpString(byte[])}
     * and {@link StringUtilities#createHexDumpString(byte[], int)} methods to create a formatted string describing the
     * contents of the provided stream.
     *
     * @param is The {@link InputStream} containing the data for which a hex dump string should be created.
     * @return The hex dump {@link String} which was created.
     * @throws IOException If reading from the provided stream causes an {@link IOException}.
     */
    public static String createFormattedHexDumpString(InputStream is)
            throws IOException
    {
        StringBuffer sb = new StringBuffer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        int count = 0;
        int offset = 0;
        byte[] data = new byte[16];
        ps.println("     00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F  0123456789ABCDEF");
        while (is.available() > 0)
        {
            int incoming = is.read();
            data[count++] = (byte) incoming;
            if (count == 16)
            {
                sb.delete(0, sb.length());
                sb.append(Integer.toHexString(offset));
                while (sb.length() < 4)
                {
                    sb.insert(0, '0');
                }
                ps.print(sb.toString().toUpperCase());
                ps.print(' ');

                ps.println(StringUtilities.createHexDumpString(data));
                count = 0;
                offset++;
            }
        }
        if (count > 0)
        {
            sb.delete(0, sb.length());
            sb.append(Integer.toHexString(offset));
            while (sb.length() < 4)
            {
                sb.insert(0, '0');
            }
            ps.print(sb.toString().toUpperCase());
            ps.print(' ');

            ps.println(StringUtilities.createHexDumpString(data, 16 - count));
        }
        ps.close();
        return baos.toString();
    }

    /**
     * Converts the provided string to a "hex" string.  The string will be twice as large as the original and contain
     * only the characters '0'-'9' and 'A'-'F'.  This method uses the {@link String#getBytes()} method, which may cause
     * cross-platform problems with systems with differing locales.  It is not recommended to use this method to produce
     * long-term storage strings.
     *
     * @param string The string to encode.
     * @return The encoded string, consisting only of '0'-'9' and 'A'-'F'.
     */
    public static String stringToHexString(String string)
    {
        verifyHexStringConversions();
        char[] chars = string.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] > 255) System.out.println("chars[i]==" + (int) (chars[i]));
            sb.append(hexStringConversions[chars[i]]);
        }
        return sb.toString();
    }

    /**
     * Converts a string encoded by the {@link StringUtilities#stringToHexString(String)} method back into its original
     * form.
     *
     * @param string The encoded string.
     * @return The original string.
     */
    public static String hexStringToString(String string)
    {
        verifyHexStringConversions();
        StringBuffer sb = new StringBuffer();
        while (string.length() > 1)
        {
            String key = string.substring(0, 2);
            string = string.substring(2);
            int app = hexStringMapping.get(key);
            sb.append((char) app);
        }
        return sb.toString();
    }

    /**
     * Ensures the existence of the values in the {@link StringUtilities#hexStringConversions} field.
     */
    private synchronized static void verifyHexStringConversions()
    {
        if (hexStringConversions == null)
        {
            char[] ch = new char[2];
            hexStringConversions = new String[256];
            hexStringMapping = new HashMap<String, Integer>();
            for (int i = 0; i < hexStringConversions.length; i++)
            {
                if (i % 16 == 0)
                {
                    int left = i / 16;
                    if (left > 9)
                    {
                        ch[0] = (char) ('A' - 10 + left);
                    } else
                    {
                        ch[0] = (char) ('0' + left);
                    }
                }
                int right = i % 16;
                if (right > 9)
                {
                    ch[1] = (char) ('A' - 10 + right);
                } else
                {
                    ch[1] = (char) ('0' + right);
                }
                hexStringConversions[i] = new String(ch);
                hexStringMapping.put(hexStringConversions[i], i);
            }
        }
    }

    /**
     * Determines whether or not the provided string contains a parsable {@link Integer}.  This method merely catches
     * the exception thrown by {@link Integer#parseInt(String)} and returns a <code>boolean</code> instead.
     *
     * @param string The {@link String} to test.
     */
    public static boolean isIntegerParsable(String string)
    {
        try
        {
            Integer.parseInt(string);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Creates a {@link String} containing a representation of the stack trace of the specified {@link Throwable}
     * object.
     *
     * @param throwable The {@link Throwable} object.
     * @return A stack trace of that object.
     */
    public static String getStackTraceFrom(Throwable throwable)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        throwable.printStackTrace(ps);
        ps.close();
        // ByteArrayOutputStream does not need to be closed.
        return baos.toString();
    }

    /**
     * Converts a standard string to a string which can be inlined in an HTML document body.
     *
     * @param source The original string in question.
     * @return The string which can be inlined in an HTML document body.
     */
    public static String getHtmlSafeString(String source)
    {
        StringBuffer sb = new StringBuffer();
        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++)
        {
            sb.append(HTML_STRING_MAPPING[ch[i]]);
        }
        return sb.toString();
    }

    /**
     * Converts a standard string to a string which can be used as a parameter name or value or as a URL pathname in a
     * URL string.
     *
     * @param source The original string in question.
     * @return The string which has been escaped.
     */
    public static String getUrlEscapedString(String source)
    {
        StringBuffer sb = new StringBuffer();
        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++)
        {
            if (((ch[i] >= 'A') && (ch[i] <= 'Z')) ||
                ((ch[i] >= 'a') && (ch[i] <= 'z')) ||
                ((ch[i] >= '0') && (ch[i] <= '9')) || (ch[i] == '_'))
            {
                sb.append(ch[i]);
            } else
            {
                // escape
                sb.append("%").append(stringToHexString(String.valueOf(ch[i])));
            }
        }
        return sb.toString();
    }

    /**
     * Retrieves a parameter string for a URL based upon the parameters provided.  This string will be escaped properly
     * before it is returned.  The parameter string separates parameters by '<code>&</code>' characters, but does not
     * include the prepending '<code>?</code>' character.
     *
     * @param map The {@link Map} object which contains a mapping between parameters and values.
     * @return The URL parameter string.
     */
    public static String getUrlParameterString(Map map)
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = map.keySet().iterator();
        boolean first = true;
        while (it.hasNext())
        {
            Object key = it.next();
            Object value = map.get(key);
            while (value.getClass().getComponentType() != null)
            {
                Object[] valueArray = (Object[]) (value);
                value = valueArray[0];
            }
            if (first)
            {
                first = false;
            } else
            {
                sb.append("&");
            }
            sb.append(getUrlEscapedString(key.toString())).append("=").append(getUrlEscapedString(value.toString()));
        }
        return sb.toString();
    }

    /**
     * Converts a standard string to a string which can be inlined in SQL syntax.
     *
     * @param source The original string in question.
     * @return The string which can be inlined in SQL syntax.
     */
    public static String getSqlSafeString(String source)
    {
        StringBuffer sb = new StringBuffer();
        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++)
        {
            sb.append(SQL_STRING_MAPPING[ch[i]]);
        }
        return sb.toString();
    }

    /**
     * Retrieves a string in YYYYMMDD format representing today's date.
     *
     * @return The specified string.
     */
    public static String getEightCharacterTodayString()
    {
        Calendar calendar = Calendar.getInstance();
        StringBuffer dateSb = new StringBuffer();
        dateSb.append(calendar.get(Calendar.YEAR));
        dateSb.append(calendar.get(Calendar.MONTH) + 1);
        if (dateSb.length() < 6) dateSb.insert(4, '0');
        dateSb.append(calendar.get(Calendar.DAY_OF_MONTH));
        if (dateSb.length() < 8) dateSb.insert(6, '0');
        return dateSb.toString();
    }

    /**
     * Creates a seperated list.  The list is delimited by a provided string.
     *
     * @param delimiter The string with which to delimit the string.
     * @param strings   The strings to use to create the list.
     * @return The delimited list.
     */
    public static String createDelimitedList(String delimiter, String... strings)
    {
        StringBuffer sb = new StringBuffer();
        for (String s : strings)
        {
            sb.append(s);
            sb.append(delimiter);
        }
        if (sb.length() >= delimiter.length()) sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }

    /**
     * Prefixes all provided strings with another string.  The result is returned as an array.
     *
     * @param prefix  The prefix to use.
     * @param strings The strings to prefix.
     * @return The prefixed strings.
     */
    public static String[] prefixArray(String prefix, String... strings)
    {
        String[] ret = new String[strings.length];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = prefix + strings[i];
        }
        return ret;
    }

    /**
     * Removes all whitespace from the beginning and end of the provided string.  If <code>null</code> is provided,
     * <code>null</code> is returned.
     *
     * @param s The string from which to trim whitespace.
     * @return The resulting string.
     */
    public static String trimWhitespace(String s)
    {
        if (s == null) return null;
        return s.replaceAll("^\\s+","").replaceAll("\\s+$","");
    }

    /**
     * Reverses the contents of the provided string.
     *
     * @param comp The string to reverse.
     * @return The reversed string.
     */
    public static String reverseString(String comp)
    {
        StringBuilder sb = new StringBuilder();
        for (char c : comp.toCharArray()) sb.insert(0, c);
        return sb.toString();
    }

    /**
     * Creates an array of strings based upon the provided array.  Each element in the returned array is the result of
     * calling {@link Object#toString()} on the corresponding object in the source array.
     *
     * @param array The source array.
     * @return The array of {@link Object#toString() toString()} results.
     */
    public static String[] createToStringArray(Object[] array)
    {
        String[] ret = new String[array.length];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = array[i].toString();
        }
        return ret;
    }

    /**
     * Generates a string of hexadecimal characters, treating the provided byte array as if it represented an unsigned
     * big-endian integer.
     *
     * @param data The data to use.
     * @return The resulting hex string.
     */
    public static String createHexString(byte[] data)
    {
        verifyHexStringConversions();
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data)
        {
            sb.append(hexStringConversions[b & 0xFF]);
        }
        return sb.toString();
    }
}

// END OF FILE