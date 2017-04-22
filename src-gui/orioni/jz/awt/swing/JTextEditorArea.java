package orioni.jz.awt.swing;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This {@link JTextArea} extension provides a set of additional functionality for text editing purposes.  There are
 * methods provided on this object which allow the control of this additional functionality.  By default, all additional
 * features are disabled.
 *
 * @author Zachary Palmer
 */
public class JTextEditorArea extends JTextArea
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Whether or not auto-indent is in effect.
     */
    protected boolean autoIndent;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Constructs a new TextEditorArea.  A default model is set, the initial string is null, and rows/columns are set to
     * 0.
     */
    public JTextEditorArea()
    {
        this(null, null, 0, 0);
    }

    /**
     * Constructs a new TextEditorArea with the specified text displayed. A default model is created and rows/columns
     * are set to 0.
     *
     * @param text the text to be displayed, or null
     */
    public JTextEditorArea(String text)
    {
        this(null, text, 0, 0);
    }

    /**
     * Constructs a new empty TextEditorArea with the specified number of rows and columns.  A default model is created,
     * and the initial string is null.
     *
     * @param rows    the number of rows >= 0
     * @param columns the number of columns >= 0
     * @throws IllegalArgumentException if the rows or columns arguments are negative.
     */
    public JTextEditorArea(int rows, int columns)
    {
        this(null, null, rows, columns);
    }

    /**
     * Constructs a new TextEditorArea with the specified text and number of rows and columns.  A default model is
     * created.
     *
     * @param text    the text to be displayed, or null
     * @param rows    the number of rows >= 0
     * @param columns the number of columns >= 0
     * @throws IllegalArgumentException if the rows or columns arguments are negative.
     */
    public JTextEditorArea(String text, int rows, int columns)
    {
        this(null, text, rows, columns);
    }

    /**
     * Constructs a new JTextEditorArea with the given document model, and defaults for all of the other arguments
     * (null, 0, 0).
     *
     * @param doc the model to use
     */
    public JTextEditorArea(Document doc)
    {
        this(doc, null, 0, 0);
    }

    /**
     * Constructs a new JTextEditorArea with the specified number of rows and columns, and the given model.  All of the
     * constructors feed through this constructor.
     *
     * @param doc     the model to use, or create a default one if null
     * @param text    the text to be displayed, null if none
     * @param rows    the number of rows >= 0
     * @param columns the number of columns >= 0
     * @throws IllegalArgumentException if the rows or columns arguments are negative.
     */
    public JTextEditorArea(Document doc, String text, int rows, int columns)
    {
        super(doc, text, rows, columns);
        autoIndent = false;

        this.addKeyListener(
                new KeyAdapter()
                {
                    /**
                     * Invoked when a key has been typed. See the class description for {@link KeyEvent} for a
                     * definition of a key typed event.
                     */
                    public void keyTyped(KeyEvent e)
                    {
                        switch (e.getKeyChar())
                        {
                            case '\n':
                                int offset = getCaretPosition() - 2;
                                char[] ch = getText().toCharArray();
                                while ((offset >= 0) && (ch[offset] != '\n'))
                                {
                                    offset--;
                                }
                                offset++;
                                StringBuffer sb = new StringBuffer();
                                while ((offset < ch.length) &&
                                       ((ch[offset] == ' ') || (ch[offset] == '\t')))
                                {
                                    sb.append(ch[offset]);
                                    offset++;
                                }
                                insert(sb.toString(), getCaretPosition());
                                break;
                        }
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Determines whether or not auto-indent is enabled.  Auto-indenting inserts a number of spaces after a carraige
     * return equal to the number of spaces which appear after the last carraige return.
     *
     * @return <code>true</code> if auto-indenting is enabled; <code>false</code> otherwise.
     */
    public boolean isAutoIndenting()
    {
        return autoIndent;
    }

    /**
     * Sets auto-indenting.
     *
     * @param autoIndent <code>true</code> to enable auto-indenting; <code>false</code> otherwise.
     */
    public void setAutoIndent(boolean autoIndent)
    {
        this.autoIndent = autoIndent;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE