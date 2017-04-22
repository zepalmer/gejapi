package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * This {@link JTextArea} is designed to render the contents of an {@link InputStream}.  Upon its construction, a thread
 * is created which reads the stream to exhaustion, displaying the contents here.
 *
 * @author Zachary Palmer
 */
public class StreamDisplayArea extends JTextArea
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link InputStream} from which data is retrieved.
     */
    protected InputStream inputStream;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param input   The {@link InputStream} containing the data to display.
     * @param rows    The number of rows for this {@link JTextArea}.
     * @param columns The number of columns for this {@link JTextArea}.
     */
    public StreamDisplayArea(InputStream input, int rows, int columns)
    {
        super(rows, columns);
        setFont(new Font("Monospaced", Font.PLAIN, getFont().getSize()));
        setEditable(false);
        inputStream = input;
        new Thread()
        {
            public void run()
            {
                try
                {
                    byte[] buffer = new byte[2048];
                    int read = inputStream.read(buffer);
                    while (read != -1)
                    {
                        setText(getText() + new String(buffer, 0, read));
                        read = inputStream.read(buffer);
                    }
                } catch (IOException e)
                {
                    setText(e.getMessage());
                }
            }
        }.start();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
