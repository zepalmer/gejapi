package orioni.jz.io.parsing;

import orioni.jz.common.exception.ParseException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is designed to tokenize the provided input stream.  The tokenization of the stream is dependent upon the
 * token patterns which are provided on construction.  The type parameter of this class determines the type of object
 * used to identify the different token types.  For example, if the type parameter of this class is {@link String}, then
 * strings will be used to name the different types of tokens provided upon construction.
 * <p/>
 * This stream may buffer data.  Thus, reading from the underlying stream may provide out-of-order data.  It is
 * recommended that, if data other than tokens is contained within this stream, the data be read through this {@link
 * InputStream} object rather than the underlying stream.
 *
 * @author Zachary Palmer
 */
public class PatternTokenInputStream<T> extends InputStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link InputStream} from which the data targeted for tokenization will be obtained.
     */
    protected InputStream inputStream;
    /**
     * The token types to be used in tokenizing this stream.
     */
    protected List<TokenType<T>> tokenTypes;
    /**
     * The buffered data in this stream.
     */
    protected StringBuffer buffer;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public PatternTokenInputStream(InputStream is, TokenType<T>... tokenTypes)
    {
        this(is, Arrays.asList(tokenTypes));
    }

    /**
     * General constructor.
     */
    public PatternTokenInputStream(InputStream is, List<TokenType<T>> tokenTypes)
    {
        super();
        inputStream = is;
        this.tokenTypes = tokenTypes;
        buffer = new StringBuffer();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the next token from the input stream.
     *
     * @return The stream's next token.  If the end of the stream has been reached when this method is called, this
     *         method returns <code>null</code>.
     * @throws IOException    If an I/O error occurs while tokenizing the stream.
     * @throws EOFException   If the stream is exhausted during the middle of a token read.
     * @throws ParseException If no valid token is available.
     */
    public Token<T> readToken()
            throws IOException, ParseException
    {
        boolean retry;
        ArrayList<TokenType<T>> tokenTypes = new ArrayList<TokenType<T>>();
        for (TokenType<T> tt : this.tokenTypes)
        {
            tokenTypes.add(tt);
        }
        int desiredSize = 1;
        boolean firstPass = true;
        do
        {
            retry = false;
            int inp = 0;
            if ((buffer.length() < desiredSize) || (!firstPass))
            {
                inp = inputStream.read();
                if (inp == -1)
                {
                    if (buffer.length() == 0)
                    {
                        return null;
                    } else
                    {
                        throw new EOFException("End of stream reached before token was completed");
                    }
                }
                buffer.append((char) inp);
            }
            String s = buffer.toString().substring(0, desiredSize);
            ArrayList<TokenType<T>> tokenTypesTemp = new ArrayList<TokenType<T>>(tokenTypes);
            for (TokenType<T> tt : tokenTypesTemp)
            {
                switch (tt.attemptMatch(s))
                {
                    case SUCCESS:
                        Token<T> token;
                        TokenType.MatchResult matchResult;
                        int tries = tt.getReadAhead();
                        do
                        {
                            token = tt.getToken();
                            desiredSize++;
                            if (buffer.length() < desiredSize)
                            {
                                inp = inputStream.read();
                                if (inp != -1)
                                {
                                    buffer.append((char) inp);
                                }
                            }
                            if (desiredSize <= buffer.length())
                            {
                                s = buffer.toString().substring(0, desiredSize);
                            } else
                            {
                                s = null;
                            }
                            matchResult = tt.attemptMatch(s);
                            tries--;
                            if (matchResult == TokenType.MatchResult.SUCCESS)
                            {
                                tries = tt.getReadAhead();
                            }
                        } while (((matchResult == TokenType.MatchResult.SUCCESS) || (tries > 0)) && (inp != -1));
                        buffer.delete(0, token.getTokenText().length());
                        return token;
                    case FAILURE:
                        tokenTypes.remove(tt);
                        break;
                    case INCONCLUSIVE:
                        retry = true;
                        break;
                }
            }
            desiredSize++;
            firstPass = false;
        } while (retry);
        throw new ParseException("No token type matches the existing character sequence.");
    }

    /**
     * Reads the next byte of data from the input stream.
     *
     * @return The next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    public int read()
            throws IOException
    {
        if (buffer.length() > 0)
        {
            byte b = (byte) (buffer.charAt(0));
            buffer.delete(0, 1);
            return (0xFF & b);
        } else
        {
            return inputStream.read();
        }
    }

    /**
     * Reads tokens from the stream until the stream is exhausted or an error occurs.  The tokens are added to the
     * provided {@link Collection}.  If an error occurs, this method behaves exactly as the {@link
     * PatternTokenInputStream#readToken()} method: the data which caused the error is buffered.  In this method,
     * however, any successfully read tokens will be placed in the provided collection and not buffered when the
     * exception is thrown.
     *
     * @param collection The collection into which to deposit the tokens.
     * @throws IOException    If an I/O error occurs while tokenizing the stream.
     * @throws EOFException   If the stream is exhausted during the middle of a token read.
     * @throws ParseException If no valid token is available.
     */
    public void readAllTokens(Collection<Token<T>> collection)
            throws IOException, EOFException, ParseException
    {
        Token<T> token = readToken();
        while (token != null)
        {
            collection.add(token);
            token = readToken();
        }
    }

    /**
     * Closes the underlying stream.
     *
     * @throws IOException If an I/O error occurs while closing the underlying stream.
     */
    public void close()
            throws IOException
    {
        inputStream.close();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class represents token types that are provided to the {@link PatternTokenInputStream} on construction.
     *
     * @author Zachary Palmer
     */
    public static class TokenType<T>
    {
        /**
         * Used to distinguish between the different types of results from attempted matches.
         */
        public static enum MatchResult
        {
            /**
             * Indicates that a token was successfully parsed and that it should be returned immediately.
             */
            SUCCESS,
            /**
             * Indicates that a token was not successfully parsed and cannot be constructed based upon the provided
             * input data.
             */
            FAILURE,
            /**
             * Indicates that a token was not successfully parsed but that this result may change if more data is
             * added.
             */
            INCONCLUSIVE
        }

        /**
         * The identification of this token type.
         */
        protected T identity;
        /**
         * The regular expression pattern defining the token type.
         */
        protected Pattern pattern;
        /**
         * The most recently matched token.
         */
        protected Token<T> token;
        /**
         * The distance which must be read ahead to establish a token's identity.
         */
        protected int readAhead;

        /**
         * Skeleton constructor.  Assumes that a read-ahead of one character is sufficient to determine the token's
         * type.
         *
         * @param identity The identification of this token type.
         * @param pattern  The regular expression pattern defining the token type.
         */
        public TokenType(T identity, String pattern)
        {
            this(identity, pattern, 1);
        }

        /**
         * General constructor.
         *
         * @param identity  The identification of this token type.
         * @param pattern   The regular expression pattern defining the token type.
         * @param readAhead The distance which must be read ahead of any success or inconclusive result to establish the
         *                  token's type with certaintly.  Usually, a read ahead of <code>1</code> is sufficient; if
         *                  "<code>a</code>" is valid but "<code>ab</code>" is not, a read ahead of <code>1</code> is
         *                  appropriate. If, however, both "<code>a</code>" and "<code>abc</code>" are appropriate, a
         *                  read-ahead of two is necessary.
         */
        public TokenType(T identity, String pattern, int readAhead)
        {
            this.identity = identity;
            this.pattern = Pattern.compile(pattern);
            this.readAhead = readAhead;
        }

        /**
         * Retrieves the identification of this token type.
         *
         * @return The identification of this token type.
         */
        public T getIdentity()
        {
            return identity;
        }

        /**
         * Retrieves the pattern which defines this token type.
         *
         * @return The pattern which defines this token type.
         */
        public String getPattern()
        {
            return pattern.pattern();
        }

        /**
         * Attempts to match the provided text against the pattern contained within this token type.
         *
         * @param text The text to check.  If <code>null</code>, the result is always failure.
         * @return One of the {@link MatchResult} values.
         */
        public MatchResult attemptMatch(String text)
        {
            if (text == null) return MatchResult.FAILURE;
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches())
            {
                token = new Token<T>(identity, text);
                return MatchResult.SUCCESS;
            } else
            {
                if (matcher.hitEnd())
                {
                    return MatchResult.INCONCLUSIVE;
                } else
                {
                    return MatchResult.FAILURE;
                }
            }
        }

        /**
         * Obtains the token from this token type which is a result of the most recent match.  The results of this
         * method have no meaning unless a successful match has been made.
         *
         * @return The {@link Token} which is the result of the most recent successful match.
         */
        public Token<T> getToken()
        {
            return token;
        }

        /**
         * Retrieves the read ahead length for this token type.
         */
        public int getReadAhead()
        {
            return readAhead;
        }
    }

    /**
     * Instances of this class represents a token produced by a {@link PatternTokenInputStream}.
     *
     * @author Zachary Palmer
     */
    public static class Token<T>
    {
        /**
         * The text which produced this token.
         */
        protected String tokenText;
        /**
         * The identification of this token's type.
         */
        protected T identification;

        /**
         * General constructor.
         *
         * @param identification The identification of this token's type.
         * @param text           The text which produced this token.
         */
        public Token(T identification, String text)
        {
            this.identification = identification;
            tokenText = text;
        }

        /**
         * Retrieves the identification of this token's type.
         *
         * @return The identification of this token's type.
         */
        public T getIdentification()
        {
            return identification;
        }

        /**
         * Retrieves the text which produced this token.
         *
         * @return The text which produced this token.
         */
        public String getTokenText()
        {
            return tokenText;
        }

        /**
         * Retrieves a string describing this token.
         *
         * @return A string describing this token.
         */
        public String toString()
        {
            return "(" + identification + ": \"" + tokenText + "\")";
        }
    }
}

// END OF FILE