package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.swing.SpacingComponent;

import javax.swing.*;
import java.awt.*;

/**
 * This dialog is designed to inform a user that a loading process is taking place.  The default settings for the dialog
 * are to simply generate a window with a blue border, white background, and the text "Loading..." in fairly large font.
 * These defaults can be altered by various constructors to display in different colors, to display different text, or
 * to display a progress indicator with text describing the current loading phase. <P> The progress indicator is created
 * by providing an array of strings to the dialog.  The dialog will maintain a reference to these strings and, through
 * calls to the {@link LoadingDialog#advance()} method, display different strings and increase the value of the status
 * bar.  The status bar is divided into <code>n-1</code> segments, where <code>n</code> is the length of the string
 * array.  The number of filled segments is equal to the index of the string.  Thus, when the string with index
 * <code>0</code> is showing, no parts of the bar are filled.  If four strings were provided (<code>n=4</code>) and the
 * string with index <code>2</code> were showing (third of four strings), two-thirds of the bar would be filled.
 *
 * @author Zachary Palmer
 */
public class LoadingDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The default border color.
     */
    protected static final Color DEFAULT_BORDER_COLOR = Color.BLUE.darker();
    /**
     * The default background color.
     */
    protected static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    /**
     * The default text.
     */
    protected static final String DEFAULT_TEXT = "Loading...";
    /**
     * The default font for the text.
     */
    protected static final Font DEFAULT_FONT = new Font("Serif", Font.ITALIC | Font.BOLD, 24);
    /**
     * The default state text array for the progress bar.
     */
    protected static final String[] DEFAULT_STATE_TEXT = null;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The strings representing the loading states.
     */
    protected String[] loadingStateStrings;
    /**
     * The {@link JLabel} which displays the state strings.
     */
    protected JLabel loadingStateLabel;
    /**
     * The {@link JProgressBar} which shows loading progress.
     */
    protected JProgressBar progressBar;
    /**
     * The current index of the loading state.
     */
    protected int currentIndex;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes all defaults.
     */
    public LoadingDialog()
    {
        this(DEFAULT_BORDER_COLOR, DEFAULT_BACKGROUND_COLOR, DEFAULT_TEXT, DEFAULT_FONT, DEFAULT_STATE_TEXT);
    }

    /**
     * Skeleton constructor.  Assumes all defaults except displayed text.
     * @param text             The text to show in the dialog.
     */
    public LoadingDialog(String text)
    {
        this(DEFAULT_BORDER_COLOR, DEFAULT_BACKGROUND_COLOR, text, DEFAULT_FONT, DEFAULT_STATE_TEXT);
    }

    /**
     * Skeleton constructor.  Accepts default text and state strings while assuming default colors and font.
     * @param text             The text to show in the dialog.
     * @param states           Texts which describe various loading states, cycled by use of the {@link
     *                         LoadingDialog#advance()} method.  If this value is <code>null</code>, no progress
     *                         indicator is displayed.
     */
    public LoadingDialog(String text, String[] states)
    {
        this(DEFAULT_BORDER_COLOR, DEFAULT_BACKGROUND_COLOR, text, DEFAULT_FONT, states);
    }

    /**
     * General constructor.
     *
     * @param borderColor     The color in which to draw the border.
     * @param backgroundColor The color in which to draw the background of the dialog.
     * @param text             The text to show in the dialog.
     * @param font             The font in which to show the dialog's text.
     * @param states           Texts which describe various loading states, cycled by use of the {@link
     *                         LoadingDialog#advance()} method.  If this value is <code>null</code>, no progress
     *                         indicator is displayed.
     */
    public LoadingDialog(Color borderColor, Color backgroundColor, String text, Font font, String[] states)
    {
        super((Frame) null, false);

        if (states.length<1) states = null;
        loadingStateStrings = states;

        this.setUndecorated(true);
        this.getContentPane().setLayout(new FlowLayout());
        JPanel loadingPanel = new JPanel();
        loadingPanel.setBackground(backgroundColor);
        loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.Y_AXIS));
        JPanel spacingPanel = new JPanel(new FlowLayout());
        spacingPanel.add(loadingPanel);
        spacingPanel.setBackground(backgroundColor);
        this.getContentPane().add(spacingPanel);
        this.getContentPane().setBackground(borderColor);

        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(font);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingPanel.add(new SpacingComponent(10, 10));
        loadingPanel.add(titleLabel);
        loadingPanel.add(new SpacingComponent(10, 10));

        if (loadingStateStrings != null)
        {
            loadingStateLabel = new JLabel();
            loadingStateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            progressBar = new JProgressBar(0, loadingStateStrings.length - 1);
            progressBar.setPreferredSize(
                    new Dimension(
                            Math.max(250, titleLabel.getWidth()),
                            (int) (progressBar.getPreferredSize().getHeight())));
            loadingPanel.add(new SpacingComponent(10, 10));
            loadingPanel.add(progressBar);
            loadingPanel.add(new SpacingComponent(5, 5));
            loadingPanel.add(loadingStateLabel);
            loadingPanel.add(new SpacingComponent(10, 10));
            currentIndex = -1;
            advance();
        }

        this.pack();
        this.setLocationRelativeTo(null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method advances the current loading state.  It will do nothing if the loading state is already at the last
     * specified state or if no states were specified on construction.
     */
    public void advance()
    {
        if ((loadingStateStrings != null) && (currentIndex + 1 < loadingStateStrings.length))
        {
            currentIndex++;
            loadingStateLabel.setText(loadingStateStrings[currentIndex]);
            progressBar.setValue(currentIndex);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}