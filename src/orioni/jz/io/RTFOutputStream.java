package orioni.jz.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This class is designed to allow the user to, through a series of calls, generate a valid RTF stream.
 *
 * @author Zachary Palmer
 */
public class RTFOutputStream extends PrintStream
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * A constant representing the number of twips in an inch.
     */
    public static final int TWIPS_PER_INCH = 1440;
    /**
     * A constant representing "single spacing".
     */
    public static final int SINGLE_SPACING = 240;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The target {@link OutputStream} of the RTF-formatted data.
     */
    protected OutputStream outputStream;
    /**
     * The {@link String}<code>[]</code> containing the names of the fonts that this stream is using.
     */
    protected String[] fontNames;

    /**
     * Determines whether or not the displayed plaintext is currently bold.
     */
    protected boolean bold;
    /**
     * Determines whether or not the displayed plaintext is currently italic.
     */
    protected boolean italic;
    /**
     * Determines whether or not the displayed plaintext is currently underlined.
     */
    protected boolean underline;

    /**
     * Determines whether or not the next written plaintext will follow a control signal.  If this is the case, a space
     * must be written to delimit the control signal first if the first plaintext character is an alphanumeric character.
     */
    protected boolean signalJustWritten;

    /**
     * Determines whether or not paragraph settings are currently the default.
     */
    protected boolean paragraphSettingsDefault;
    /**
     * The current first-line left indent of the document (in twips).
     */
    protected int firstLineLeftIndent;
    /**
     * The current other-line left indent of the document (in twips).
     */
    protected int otherLineLeftIndent;
    /**
     * The current line spacing of the document.
     */
    protected int lineSpacing;

    /**
     * The index of the current font being used.
     */
    protected int fontIndex;
    /**
     * The current font point size.
     */
    protected int fontPointSize;

    /**
     * <code>true</code> if the {@link RTFOutputStream#close()} method has been called; <code>false</code> otherwise.
     */
    protected boolean closed;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Does not define any fonts for use by this RTF writer.
     *
     * @param os The target {@link OutputStream} of the RTF-formatted data.
     */
    public RTFOutputStream(OutputStream os)
    {
        this(os, new String[]{"Courier"});
    }

    /**
     * General constructor.
     *
     * @param os    The target {@link OutputStream} of the RTF-formatted data.
     * @param fonts A {@link String}<code>[]</code> containing the names of all of the fonts that this file will use. These
     *              fonts will be referenced by their indices in this array during stream writing.
     */
    public RTFOutputStream(OutputStream os, String[] fonts)
    {
        super(os);
        outputStream = os;
        fontNames = fonts;

        bold = false;
        italic = false;
        underline = false;
        signalJustWritten = false;
        paragraphSettingsDefault = true;
        firstLineLeftIndent = -1;
        otherLineLeftIndent = -1;
        lineSpacing = -1;
        fontIndex = -1;
        fontPointSize = -1;
        closed = false;

        absoluteWrite('{');
        absoluteWrite("\\rtf1");      // RTF version 1.x
        absoluteWrite("\\ansi");      // ANSI character set
        // Font table
        absoluteWrite("{\\fonttbl");
        for (int i = 0; i < fonts.length; i++)
        {
            absoluteWrite("{\\f");
            absoluteWrite(String.valueOf(i));
            absoluteWrite("\\fnil");  // TODO: give the user the ability to provide font families
            absoluteWrite(' ');
            absoluteWrite(fonts[i]);
            absoluteWrite(';');
        }
        absoluteWrite('}');
        absoluteWrite('}'); // end of header
        absoluteWrite('\n');
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets the bold state of this stream.
     *
     * @param bold Whether or not the text should now be bold.
     */
    public void setBold(boolean bold)
    {
        if (this.bold != bold)
        {
            if (bold)
            {
                absoluteWrite("\\b");
                signalJustWritten = true;
            } else
            {
                absoluteWrite("\\b0");
                signalJustWritten = true;
            }
            this.bold = bold;
        }
    }

    /**
     * Sets the italic state of this stream.
     *
     * @param italic Whether or not the text should now be italic.
     */
    public void setItalic(boolean italic)
    {
        if (this.italic != italic)
        {
            if (italic)
            {
                absoluteWrite("\\i");
                signalJustWritten = true;
            } else
            {
                absoluteWrite("\\i0");
                signalJustWritten = true;
            }
            this.italic = italic;
        }
    }

    /**
     * Sets the underlining state of this stream.
     *
     * @param underline Whether or not the text should now be underlined.
     */
    public void setUnderline(boolean underline)
    {
        if (this.underline != underline)
        {
            if (underline)
            {
                absoluteWrite("\\ul");
                signalJustWritten = true;
            } else
            {
                absoluteWrite("\\ul0");
                signalJustWritten = true;
            }
            this.underline = underline;
        }
    }

    /**
     * Resets the paragraph settings to default.
     */
    public void resetParagraphSettings()
    {
        if (!paragraphSettingsDefault)
        {
            paragraphSettingsDefault = true;
            absoluteWrite("\\pard");
            signalJustWritten = true;
        }
    }

    /**
     * Sets the left indent of the first line of each paragraph.  This indent will remain until {@link
     * RTFOutputStream#resetParagraphSettings()} is called or until the indent is changed again.
     *
     * @param firstLineInches  The number of inches to indent the first line.
     * @param otherLinesInches The number of inches to indent the other lines.
     */
    public void setLeftIndent(double firstLineInches, double otherLinesInches)
    {
        int firstLineTwips = (int) (firstLineInches * TWIPS_PER_INCH);
        int otherLinesTwips = (int) (otherLinesInches * TWIPS_PER_INCH);
        if ((firstLineTwips != firstLineLeftIndent) || (otherLinesTwips != otherLineLeftIndent))
        {
            paragraphSettingsDefault = false;
            absoluteWrite("\\fi" + (firstLineTwips - otherLinesTwips));
            absoluteWrite("\\li" + otherLinesTwips);
            firstLineLeftIndent = firstLineTwips;
            otherLineLeftIndent = otherLinesTwips;
            signalJustWritten = true;
        }
    }

    /**
     * Sets the line spacing for the document.
     *
     * @param lines The number of lines between each line plus one.  For example, single spacing is <code>1.0</code>,
     *              double spacing is <code>2.0</code>, etc.
     */
    public void setLineSpacing(double lines)
    {
        int spacing = (int) (lines * SINGLE_SPACING);
        if (lineSpacing != spacing)
        {
            lineSpacing = spacing;
            absoluteWrite("\\sl" + spacing);
            absoluteWrite("\\slmult1");
            signalJustWritten = true;
        }
    }

    /**
     * Sets the index of the font to use.
     *
     * @param index The index of the new font.
     * @throws IndexOutOfBoundsException If the index is less than zero or equal to or greater than the number of fonts
     *                                   provided on construction.
     */
    public void setFontIndex(int index)
            throws IndexOutOfBoundsException
    {
        if (fontIndex != index)
        {
            absoluteWrite("\\f" + index);
            fontIndex = index;
            signalJustWritten = true;
        }
    }

    /**
     * Sets the size of font to use.
     *
     * @param points The point size of the font.
     */
    public void setFontSize(int points)
    {
        if (fontPointSize != points)
        {
            absoluteWrite("\\fs" + (points * 2));
            fontPointSize = points;
            signalJustWritten = true;
        }
    }

    /**
     * Writes the specified string directly to this stream.  This method is used for writing RTF signals and similar
     * strings which should <i>not</i> be delimited as plaintext.
     *
     * @param s The string to write.
     */
    protected void absoluteWrite(String s)
    {
        byte[] data = s.getBytes();
        super.write(data, 0, data.length);
    }

    /**
     * Writes the specified character directly to this stream.  This method is used for writing RTF signals and similar
     * strings which should <i>not</i> be delimited as plaintext.
     *
     * @param ch The character to write.
     */
    protected void absoluteWrite(char ch)
    {
        byte[] data = String.valueOf(ch).getBytes();
        super.write(data, 0, data.length);
    }

// NON-STATIC METHODS : PRINT STREAM OVERRIDES ///////////////////////////////////

    /**
     * Writes the specified byte to this output stream.  The byte will be treated as an ASCII character.  If the specified
     * byte is a '<code>\</code>', '<code>{</code>', or '<code>}</code>', a signal backslash will be written before it.
     *
     * @param b The <code>byte</code>.
     */
    public void write(int b)
    {
        if (b == '\n')
        {
            absoluteWrite("\\par");
            super.write('\n');
            signalJustWritten = true;
        } else
        {
            if ((b == '\\') || (b == '{') || (b == '}'))
            {
                absoluteWrite('\\');
                super.write(b);
            } else
            {
                if ((signalJustWritten) &&
                    (((b >= '0') && (b <= '9')) ||
                     ((b >= 'A') && (b <= 'Z')) ||
                     ((b >= 'a') && (b <= 'z'))))
                {
                    absoluteWrite(' ');
                }
                super.write(b);
            }
            signalJustWritten = false;
        }
    }

    /**
     * Writes <code>len</code> bytes from the array <code>buf</code>, starting at offset <code>off</code>.  This method
     * calls the {@link RTFOutputStream#write(int)} method with each byte to ensure proper RTF formatting.
     *
     * @param buf A byte array
     * @param off Offset from which to start taking bytes
     * @param len Number of bytes to write
     */
    public void write(byte[] buf, int off, int len)
    {
        for (int i = off; i < off + len; i++)
        {
            write(buf[i]);
        }
    }

    /**
     * Closes this stream.  The end of the document must be indicated by a right brace.
     */
    public void close()
    {
        if (!closed)
        {
            super.write('}');
        }
        closed = true;
        super.close();
    }

    /**
     * Print a boolean value.  The string produced by <code>{@link String#valueOf(boolean)}</code> is translated
     * into bytes according to the platform's default character encoding, and these bytes are written in exactly the manner
     * of the <code>{@link #write(int)}</code> method.
     *
     * @param b The <code>boolean</code> to be printed
     */
    public void print(boolean b)
    {
        try
        {
            write(String.valueOf(b).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print a character.  The character is translated into one or more bytes according to the platform's default character
     * encoding, and these bytes are written in exactly the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param c The <code>char</code> to be printed
     */
    public void print(char c)
    {
        try
        {
            write(String.valueOf(c).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print a double-precision floating-point number.  The string produced by <code>{@link String#valueOf(double)}</code>
     * is translated into bytes according to the platform's default character encoding, and these bytes are written in
     * exactly the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param d The <code>double</code> to be printed
     * @see Double#toString(double)
     */
    public void print(double d)
    {
        try
        {
            write(String.valueOf(d).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print a floating-point number.  The string produced by <code>{@link String#valueOf(float)}</code> is
     * translated into bytes according to the platform's default character encoding, and these bytes are written in exactly
     * the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param f The <code>float</code> to be printed
     * @see Float#toString(float)
     */
    public void print(float f)
    {
        try
        {
            write(String.valueOf(f).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print an integer.  The string produced by <code>{@link String#valueOf(int)}</code> is translated into
     * bytes according to the platform's default character encoding, and these bytes are written in exactly the manner of
     * the <code>{@link #write(int)}</code> method.
     *
     * @param i The <code>int</code> to be printed
     * @see Integer#toString(int)
     */
    public void print(int i)
    {
        try
        {
            write(String.valueOf(i).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print a long integer.  The string produced by <code>{@link String#valueOf(long)}</code> is translated into
     * bytes according to the platform's default character encoding, and these bytes are written in exactly the manner of
     * the <code>{@link #write(int)}</code> method.
     *
     * @param l The <code>long</code> to be printed
     * @see Long#toString(long)
     */
    public void print(long l)
    {
        try
        {
            write(String.valueOf(l).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print an object.  The string produced by the <code>{@link String#valueOf(Object)}</code> method is
     * translated into bytes according to the platform's default character encoding, and these bytes are written in exactly
     * the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param obj The <code>Object</code> to be printed
     * @see Object#toString()
     */
    public void print(Object obj)
    {
        try
        {
            write(String.valueOf(obj).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print an array of characters.  The characters are converted into bytes according to the platform's default character
     * encoding, and these bytes are written in exactly the manner of the <code>{@link #write(int)}</code> method.
     *
     * @param s The array of chars to be printed
     * @throws NullPointerException If <code>s</code> is <code>null</code>
     */
    public void print(char[] s)
    {
        try
        {
            write(String.valueOf(s).getBytes());
        } catch (IOException ioe)
        {
            super.setError();
        }
    }

    /**
     * Print a string.  If the argument is <code>null</code> then the string
     * <code>"null"</code> is printed.  Otherwise, the string's characters are
     * converted into bytes according to the platform's default character
     * encoding, and these bytes are written in exactly the manner of the
     * <code>{@link #write(int)}</code> method.
     *
     * @param      s   The <code>String</code> to be printed
     */
    public void print(String s)
    {
        try
        {
            write(s.getBytes());
        } catch (IOException ioe)
        {
            super.setError();
		}
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}