package orioni.jz.awt.swing;

import orioni.jz.awt.AWTUtilities;
import orioni.jz.io.files.FileExtensionFilter;
import orioni.jz.io.files.FileUtilities;
import orioni.jz.util.strings.StringUtilities;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * This utilities class is designed to contain Swing-specific functionality.  This is distinct from {@link
 * AWTUtilities}, which is capable of working on any AWT-based GUI platform.
 *
 * @author Zachary Palmer
 */
public class JZSwingUtilities
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Private constructor.  Utilities classes are never instantiated.
     */
    private JZSwingUtilities()
    {
        super();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * For an array of {@link AbstractButton} objects, creates a parallel array of mnemonic keystrokes which will be
     * unique and will not conflict.  These keystrokes are based upon the buttons' texts.
     *
     * @param buttons The {@link AbstractButton}<code>[]</code> for which the parallel array is required.
     * @return The parallel array specified.
     * @see JZSwingUtilities#getMnemonicsFor(String[])
     */
    public static int[] getMnemonicsFor(AbstractButton[] buttons)
    {
        String[] strings = new String[buttons.length];
        for (int i = 0; i < strings.length; i++)
        {
            if (buttons[i] == null)
            {
                strings[i] = "";
            } else
            {
                strings[i] = buttons[i].getText();
            }
        }
        return getMnemonicsFor(strings);
    }

    /**
     * Sets the mnemonics for the specified {@link AbstractButton}<code>[]</code> as best as possible.  This definition
     * is specified by the methods which are mentioned in this method's "see" documentation.
     *
     * @param buttons The {@link AbstractButton}<code>[]</code> for which mnemonics should be assigned.
     * @see JZSwingUtilities#getBestMnemonicIndexFor(String, int)
     * @see JZSwingUtilities#getMnemonicsFor(javax.swing.AbstractButton[])
     * @see JZSwingUtilities#getMnemonicsFor(String[])
     */
    public static void setBestMnemonicsFor(AbstractButton[] buttons)
    {
        int[] keycodes = getMnemonicsFor(buttons);
        for (int i = 0; i < buttons.length; i++)
        {
            if (buttons[i] != null)
            {
                buttons[i].setMnemonic(keycodes[i]);
                buttons[i].setDisplayedMnemonicIndex(
                        JZSwingUtilities.getBestMnemonicIndexFor(buttons[i].getText(), keycodes[i]));
            }
        }
    }

    /**
     * Retrieves the "best" mnemonic index for the specified keycode.  Under normal circumstances, that index is the
     * first index where the keycode's character occurs.  For keycodes for which the character appears multiple times
     * and some instances are capital letters, however, the result is the first instance of a <i>capital</i> letter.
     *
     * @param string   The string for which an index is required.
     * @param mnemonic The keycode for the mnemonic index.
     * @return The best mnemonic index for that character, or <code>-1</code> if no acceptable index exists.
     */
    public static int getBestMnemonicIndexFor(String string, int mnemonic)
    {
        char[] chs = string.toCharArray();
        if (Character.isLetter((char) (mnemonic)))
        {
            mnemonic = Character.toUpperCase((char) (mnemonic));
            for (int i = 0; i < chs.length; i++)
            {
                if (chs[i] == mnemonic)
                {
                    return i;
                }
            }
            mnemonic = Character.toLowerCase((char) (mnemonic));
            for (int i = 0; i < chs.length; i++)
            {
                if (chs[i] == mnemonic)
                {
                    return i;
                }
            }
        } else
        {
            // First index
            for (int i = 0; i < chs.length; i++)
            {
                if (AWTUtilities.getKeyEventConstantForCharacter(chs[i]) == mnemonic) return i;
            }
        }
        return -1;
    }

    /**
     * From an array of {@link String}s, creates a parallel array of mnemonic keystrokes which will be unique and will
     * not conflict.  Capital letters in strings are prioritized first, followed by lower-case letters, followed by
     * numeric characters.  After this, a character has priority if it has a lower index.  For example, for the strings:
     * <ul> "<code>Test</code>", "<code>Fire</code>", "<code>Table</code>", "<code>Testing</code>", "<code>Test
     * Run</code>" </ul> the returned parallel array would be the keystrokes for '<code>T</code>', '<code>F</code>',
     * <code>'a'</code>, '<code>e</code>', and '<code>R</code>', respectively.  Note that the key codes themselves are
     * not case sensitive; the use of different cases here was simply to demonstrate from where the characters were
     * obtained.
     *
     * @param strings The array of strings to use.
     * @return The specified mnemonic key code array.
     */
    public static int[] getMnemonicsFor(String... strings)
    {
        int[] ret = new int[strings.length];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = KeyEvent.VK_UNDEFINED;
        }

        // TreeSet with index-based comparator required to ensure that first index in array gets priority and that
        // results are repeatable.
        final HashMap<String, Integer> reverseIndexMapping = new HashMap<String, Integer>();
        Set<String> stringSet = new TreeSet<String>(
                new Comparator<String>()
                {
                    public int compare(String a, String b)
                    {
                        Integer ai = reverseIndexMapping.get(a);
                        Integer bi = reverseIndexMapping.get(b);
                        if (ai == null) throw new NullPointerException("Mapping for " + a + " null.");
                        if (bi == null) throw new NullPointerException("Mapping for " + b + " null.");
                        if (ai<bi) return -1;
                        if (ai>bi) return 1;
                        return 0;
                    }
                });
        for (int i = 0; i < strings.length; i++)
        {
            if (!reverseIndexMapping.keySet().contains(strings[i]))
            {
                reverseIndexMapping.put(strings[i], i);
            }
            if (strings[i].length() > 0)
            {
                stringSet.add(strings[i]);
            }
        }
        Set<Integer> usedKeycodeSet = new HashSet<Integer>();
        Set<String> removalSet = new HashSet<String>();

        final int modeCapsOrNumber = 0;
        final int modeLower = 1;
        final int modeOther = 2;

        for (int mode = modeCapsOrNumber; (mode <= modeOther) && (stringSet.size() > 0); mode++)
        {
            int index = 0;
            boolean longerString;
            do
            {
                longerString = false;
                for (String s : stringSet)
                {
                    if (s.length() > index)
                    {
                        char ch = s.charAt(index);
                        boolean use;
                        switch (mode)
                        {
                            case modeCapsOrNumber:
                                use = ((Character.isUpperCase(ch)) || (Character.isDigit(ch)));
                                break;
                            case modeLower:
                                use = Character.isLowerCase(ch);
                                break;
                            case modeOther:
                                use = false; // no "other" characters at this time
                                break;
                            default:
                                use = false;
                        }
                        int keycode = AWTUtilities.getKeyEventConstantForCharacter(ch);
                        if ((use) && (!usedKeycodeSet.contains(keycode)))
                        {
                            ret[reverseIndexMapping.get(s)] = keycode;
                            removalSet.add(s);
                            usedKeycodeSet.add(keycode);
                        } else
                        {
                            longerString = true;
                        }
                    }
                }
                stringSet.removeAll(removalSet);
                removalSet.clear();
                index++;
            } while ((longerString) && (stringSet.size() > 0));
        }
        return ret;
    }

    /**
     * Configures a {@link JFileChooser} to use the provided list of {@link FileFilter}s.
     *
     * @param chooser    The chooser to configure.
     * @param selected   The {@link FileFilter} which should be selected, or <code>null</code> if none.
     * @param allFilter <code>true</code> if the "all files" filter should be enabled; <code>false</code> otherwise.
     * @param filters    The {@link FileFilter}s to use.
     */
    public static void setJFileChooserFilters(JFileChooser chooser, FileFilter selected, boolean allFilter,
                                              FileFilter... filters)
    {
        chooser.resetChoosableFileFilters();
        boolean foundFilter = false;
        for (FileFilter ff : filters)
        {
            chooser.addChoosableFileFilter(ff);
            if (ff.equals(selected)) foundFilter = true;
        }
        chooser.setAcceptAllFileFilterUsed(allFilter);
        if (foundFilter) chooser.setFileFilter(selected);
    }

    /**
     * Retrieves a {@link SwingFileFilterWrapper} which represents all image types that the Image I/O package can
     * read.
     *
     * @return The specified {@link SwingFileFilterWrapper}.
     */
    public static SwingFileFilterWrapper getAllImageReadersFileFilter()
    {
        Set<String> readers = new TreeSet<String>();
        Set<String> extensions = new TreeSet<String>();
        for (String format : ImageIO.getReaderFormatNames())
        {
            if (FileUtilities.FILESYSTEM_CASE_SENSITIVE)
            {
                readers.add(format);
                extensions.add("."+format);
            } else
            {
                readers.add(format.toLowerCase());
                extensions.add(("."+format).toLowerCase());
            }
        }

        return new SwingFileFilterWrapper(
                new FileExtensionFilter(extensions.toArray(StringUtilities.EMPTY_STRING_ARRAY)),
                "All Acceptable Image Formats (" + StringUtilities.createDelimitedList(
                        ", ", StringUtilities.prefixArray("*.", readers.toArray(StringUtilities.EMPTY_STRING_ARRAY))) +
                ")");
    }

    /**
     * Retrieves a {@link SwingFileFilterWrapper} which represents all image types that the Image I/O package can
     * write.
     *
     * @return The specified {@link SwingFileFilterWrapper}.
     */
    public static SwingFileFilterWrapper getAllImageWritersFileFilter()
    {
        Set<String> writers = new TreeSet<String>();
        Set<String> extensions = new TreeSet<String>();
        for (String format : ImageIO.getWriterFormatNames())
        {
            if (FileUtilities.FILESYSTEM_CASE_SENSITIVE)
            {
                writers.add(format);
                extensions.add("."+format);
            } else
            {
                writers.add(format.toLowerCase());
                extensions.add(("."+format).toLowerCase());
            }
        }

        return new SwingFileFilterWrapper(
                new FileExtensionFilter(extensions.toArray(StringUtilities.EMPTY_STRING_ARRAY)),
                "All Acceptable Image Formats (" + StringUtilities.createDelimitedList(
                        ", ", StringUtilities.prefixArray("*.", writers.toArray(StringUtilities.EMPTY_STRING_ARRAY))) +
                ")");
    }

    /**
     * Retrieves a {@link SwingFileFilterWrapper} which is designed to filter all files for a specific image type.  This
     * filter will examine all available {@link ImageReader}s and produce a filter which accepts all file extensions
     * which have identical readers as the provided extension.  For example, providing "jpg" might return a file filter
     * which supports "jpeg" and "jpg" extensions.
     *
     * @param extension The extension that this filter should support.
     * @return The filter supporting all compatible extensions.
     */
    public static SwingFileFilterWrapper getImageReaderFileFilterFor(String extension)
    {
        String[] readerFormats = ImageIO.getReaderFormatNames();
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(extension);
        // assuming each class only reads one type of image... but Sun didn't implement equals on their ImageReaders...
        Set<Class> targets = new HashSet<Class>();
        while (it.hasNext()) targets.add(it.next().getClass());

        Set<String> fileExtensions = new TreeSet<String>();
        for (String format : readerFormats)
        {
            it = ImageIO.getImageReadersByFormatName(format);
            while (it.hasNext())
            {
                if (targets.contains(it.next().getClass()))
                {
                    // this format has a reader which is in our accepted format
                    if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE) format = format.toLowerCase();
                    fileExtensions.add(format);
                    break;
                }
            }
        }

        String[] extensions = fileExtensions.toArray(StringUtilities.EMPTY_STRING_ARRAY);
        Arrays.sort(extensions);

        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE)
        {
            extension = extension.toUpperCase();
        }
        return new SwingFileFilterWrapper(
                new FileExtensionFilter(extensions),
                extension + " files (" + StringUtilities.createDelimitedList(
                        ", ", StringUtilities.prefixArray("*.", extensions)) + ")");
    }

    /**
     * Retrieves a set of image loading filters.  These {@link SwingFileFilterWrapper}s are built to provide the user
     * with a clean loading interface.  One of the loading filters is that returned by {@link
     * JZSwingUtilities#getAllImageReadersFileFilter()}. The rest are designed to select individual file types based
     * upon the class of the image reader they use: one filter per image reader class.  Thus, since JPEG and JPG use the
     * same class of reader, one filter would be created for both file extensions.  The filters are returned in an
     * alphabetically sorted manner with the All filter at the top.
     *
     * @return An array of {@link SwingFileFilterWrapper}s designed to select groups of readable images.
     */
    public static SwingFileFilterWrapper[] getImageLoadingFileFilters()
    {
        ArrayList<SwingFileFilterWrapper> ret = new ArrayList<SwingFileFilterWrapper>();
        ret.add(JZSwingUtilities.getAllImageReadersFileFilter());
        Set<SwingFileFilterWrapper> filters = new HashSet<SwingFileFilterWrapper>();
        for (String format : ImageIO.getReaderFormatNames())
        {
            SwingFileFilterWrapper filter = getImageReaderFileFilterFor(format);
            if (!filters.contains(filter)) ret.add(filter);
            filters.add(filter);
        }

        SwingFileFilterWrapper[] arr = ret.toArray(new SwingFileFilterWrapper[0]);
        Arrays.sort(arr);
        return arr;
    }

    /**
     * Retrieves a {@link SwingFileFilterWrapper} which is designed to filter all files for a specific image type.  This
     * filter will examine all available {@link ImageWriter}s and produce a filter which accepts all file extensions
     * which have identical Writers as the provided extension.  For example, providing "jpg" might return a file filter
     * which supports "jpeg" and "jpg" extensions.
     *
     * @param extension The extension that this filter should support.
     * @return The filter supporting all compatible extensions.
     */
    public static SwingFileFilterWrapper getImageWriterFileFilterFor(String extension)
    {
        String[] writerFormats = ImageIO.getWriterFormatNames();
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(extension);
        // assuming each class only reads one type of image: Sun didn't implement equals on their ImageWriters...
        Set<Class> targets = new HashSet<Class>();
        while (it.hasNext()) targets.add(it.next().getClass());

        Set<String> fileExtensions = new TreeSet<String>();
        for (String format : writerFormats)
        {
            it = ImageIO.getImageWritersByFormatName(format);
            while (it.hasNext())
            {
                if (targets.contains(it.next().getClass()))
                {
                    // this format has a Writer which is in our accepted format
                    if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE) format = format.toLowerCase();
                    fileExtensions.add(format);
                    break;
                }
            }
        }

        String[] extensions = fileExtensions.toArray(StringUtilities.EMPTY_STRING_ARRAY);
        Arrays.sort(extensions);

        if (!FileUtilities.FILESYSTEM_CASE_SENSITIVE)
        {
            extension = extension.toUpperCase();
        }
        return new SwingFileFilterWrapper(
                new FileExtensionFilter(extensions),
                extension + " files (" + StringUtilities.createDelimitedList(
                        ", ", StringUtilities.prefixArray("*.", extensions)) + ")");
    }

    /**
     * Retrieves a set of image loading filters.  These {@link SwingFileFilterWrapper}s are built to provide the user
     * with a clean loading interface.  The filters are designed to select individual file types based upon the class of
     * the image writer they use: one filter per image writer class.  Thus, since JPEG and JPG use the same class of
     * writer, one filter would be created for both file extensions.  The filters are returned in an alphabetically
     * sorted manner.
     *
     * @return An array of {@link SwingFileFilterWrapper}s designed to select groups of writable images.
     */
    public static SwingFileFilterWrapper[] getImageSavingFileFilters()
    {
        ArrayList<SwingFileFilterWrapper> ret = new ArrayList<SwingFileFilterWrapper>();
        Set<SwingFileFilterWrapper> filters = new HashSet<SwingFileFilterWrapper>();
        for (String format : ImageIO.getWriterFormatNames())
        {
            SwingFileFilterWrapper filter = getImageWriterFileFilterFor(format);
            if (!filters.contains(filter)) ret.add(filter);
            filters.add(filter);
        }

        SwingFileFilterWrapper[] arr = ret.toArray(new SwingFileFilterWrapper[0]);
        Arrays.sort(arr);
        return arr;
    }

    /**
     * Retrieves image loading filters.  These filters are the same as are produced by {@link
     * JZSwingUtilities#getImageLoadingFileFilters()}. However, they appear in a map, keyed to a string value
     * representing an informal format name for which the filter selects.  Note that more informal format names may be
     * registered with the Image I/O package than appear here; however, since the same reader answers for both formats,
     * only one of them is provided.  As an example, both "jpg" and "jpeg" point to the same image reader.  The {@link
     * JZSwingUtilities#getImageLoadingFileFilters()} method provides one file filter for both informal format names,
     * since they are read by the same reader. Likewise, this method will return the file filter mapped to either "jpg"
     * or "jpeg", ignoring the other, since it will not matter which is used.
     *
     * @return A {@link Map}<code>&lt;{@link SwingFileFilterWrapper},{@link String}&gt;</code> mapping each of the
     *         filters to its associated informal format name.
     */
    public static Map<SwingFileFilterWrapper, String> getImageSavingFileFilterMap()
    {
        Map<SwingFileFilterWrapper, String> ret = new HashMap<SwingFileFilterWrapper, String>();
        for (String format : ImageIO.getWriterFormatNames())
        {
            SwingFileFilterWrapper filter = getImageWriterFileFilterFor(format);
            if (!ret.keySet().contains(filter)) ret.put(filter, format);
        }
        return ret;
    }

    // TODO: why doesn't this work?
    /**
     * Adds an {@link Action} to the provided {@link JFrame}'s root pane in both the input map (for the event {@link
     * JComponent#WHEN_ANCESTOR_OF_FOCUSED_COMPONENT}) and the action map.  The name of the provided action is used to
     * map the {@link Action}; it should therefore be unique.
     *
     * @param jframe    The {@link javax.swing.JFrame} to which this mapping should be applied.
     * @param keystroke The {@link javax.swing.KeyStroke} to which the {@link javax.swing.Action} should be mapped.
     * @param action    The {@link javax.swing.Action} to map.
     */
    public static void setKeystrokeForAction(JFrame jframe, KeyStroke keystroke, Action action)
    {
        jframe.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keystroke, action.getValue(Action.NAME));
        jframe.getRootPane().getActionMap().put(action.getValue(Action.NAME), action);
    }
}

// END OF FILE