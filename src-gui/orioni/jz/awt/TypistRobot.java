package orioni.jz.awt;

import orioni.jz.util.DefaultValueHashMap;
import orioni.jz.util.Utilities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * This extension to the {@link Robot} class provides some more complex input operations meant to simulate common user
 * input behavior.
 *
 * @author Zachary Palmer
 */
public class TypistRobot extends Robot
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

    /**
     * The {@link Map}ping used to determine what keycodes are required to generate a specific character.
     */
    private static final Map<Character, int[]> KEYCODES_MAP;

// CONSTANTS /////////////////////////////////////////////////////////////////////

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

    static
    {
        KEYCODES_MAP = new DefaultValueHashMap<Character, int[]>(new int[]{KeyEvent.VK_UNDEFINED});

        for (char c = 'a'; c <= 'z'; c++)
        {
            KEYCODES_MAP.put(c, new int[]{c-32});
        }
        for (char c = 'A'; c <= 'Z'; c++)
        {
            KEYCODES_MAP.put(c, new int[]{KeyEvent.VK_SHIFT, c});
        }
        for (char c = '0'; c <= '9'; c++)
        {
            KEYCODES_MAP.put(c, new int[]{c});
        }
        KEYCODES_MAP.put('\n', new int[]{KeyEvent.VK_ENTER});
        KEYCODES_MAP.put(' ', new int[]{KeyEvent.VK_SPACE});
        KEYCODES_MAP.put('"', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE});
        KEYCODES_MAP.put('!', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_1});
        KEYCODES_MAP.put('@', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_2});
        KEYCODES_MAP.put('#', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_3});
        KEYCODES_MAP.put('$', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_4});
        KEYCODES_MAP.put('%', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_5});
        KEYCODES_MAP.put('^', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_6});
        KEYCODES_MAP.put('&', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_7});
        KEYCODES_MAP.put('*', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_8});
        KEYCODES_MAP.put('(', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_9});
        KEYCODES_MAP.put(')', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_0});
        KEYCODES_MAP.put('\'', new int[]{KeyEvent.VK_QUOTE});
        KEYCODES_MAP.put('+', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_EQUALS});
        KEYCODES_MAP.put(',', new int[]{KeyEvent.VK_COMMA});
        KEYCODES_MAP.put('-', new int[]{KeyEvent.VK_MINUS});
        KEYCODES_MAP.put('.', new int[]{KeyEvent.VK_PERIOD});
        KEYCODES_MAP.put('/', new int[]{KeyEvent.VK_SLASH});
        KEYCODES_MAP.put(':', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON});
        KEYCODES_MAP.put(';', new int[]{KeyEvent.VK_SEMICOLON});
        KEYCODES_MAP.put('<', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA});
        KEYCODES_MAP.put('=', new int[]{KeyEvent.VK_EQUALS});
        KEYCODES_MAP.put('>', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD});
        KEYCODES_MAP.put('?', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH});
        KEYCODES_MAP.put('[', new int[]{KeyEvent.VK_OPEN_BRACKET});
        KEYCODES_MAP.put('\\', new int[]{KeyEvent.VK_BACK_SLASH});
        KEYCODES_MAP.put(']', new int[]{KeyEvent.VK_CLOSE_BRACKET});
        KEYCODES_MAP.put('_', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS});
        KEYCODES_MAP.put('`', new int[]{KeyEvent.VK_BACK_QUOTE});
        KEYCODES_MAP.put('{', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET});
        KEYCODES_MAP.put('|', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH});
        KEYCODES_MAP.put('}', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET});
        KEYCODES_MAP.put('~', new int[]{KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE});
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see {@link java.awt.Robot#Robot()}.
     */
    public TypistRobot()
            throws AWTException
    {
        super();
    }

    /**
     * Wrapper constructor.
     *
     * @see {@link java.awt.Robot#Robot()}.
     */
    public TypistRobot(GraphicsDevice screen)
            throws AWTException
    {
        super(screen);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Performs an alternating press-and-release operation.  This operation presses all listed keys in order and then
     * releases them in reverse order.  This is common in control keystrokes, such as Alt+S or Ctrl+0.  An example of
     * usage of this method is <code>alternatePressAndRelease(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT,
     * KeyEvent.VK_0)</code> to generate the keystroke Ctrl+Shift+0.
     *
     * @param keys The keys to press and release.
     * @throws IllegalArgumentException If one of the keycodes is invalid.
     */
    public void type(int... keys)
    {
        for (int key : keys)
        {
            this.keyPress(key);
        }
        for (int i = keys.length - 1; i >= 0; i--)
        {
            this.keyRelease(keys[i]);
        }
    }

    /**
     * Types the provided string.  This operation analyzes each character in the string and translates that character to
     * a keystroke or combination of keystrokes.  At the termination of this method, the Alt, Control, and Shift keys
     * will be released.
     *
     * @param string The string to type.
     * @throws IllegalArgumentException If one of the characters in the string is not recognized or cannot be typed.
     */
    public void type(String string)
    {
        keyRelease(KeyEvent.VK_SHIFT);
        keyRelease(KeyEvent.VK_ALT);
        keyRelease(KeyEvent.VK_CONTROL);
        char[] chs = string.toCharArray();
        char next = 0;
        try
        {
            for (char ch : chs)
            {
                next = ch;
                type(KEYCODES_MAP.get(ch));
            }
        } catch (IllegalArgumentException iae)
        {
            throw new IllegalArgumentException("Cannot type character '" +next+"' ("+((int)(next))+")", iae);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Retrieves a series of keycodes necessary to produce the provided character output.  For example, the {@link
     * KeyEvent#VK_CIRCUMFLEX} code will not type properly as there is no single key which will produce the
     * '<code>^</code>' character (the shift key must also be used).  Any return from this method will produce the
     * associated character when passed to {@link TypistRobot#type(int...)}.
     *
     * @param ch The character to type.
     * @return An <code>int[]</code> containing the necessary key codes.
     * @throws IllegalArgumentException If no such keystroke is known.
     */
    public static int[] getKeyCodesFor(char ch)
    {
        int[] ret = KEYCODES_MAP.get(ch);
        return Utilities.copyArray(ret);
    }
}

// END OF FILE