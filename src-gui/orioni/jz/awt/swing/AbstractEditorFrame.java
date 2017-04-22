package orioni.jz.awt.swing;

import orioni.jz.awt.swing.action.ExecuteMethodAction;
import orioni.jz.awt.swing.dialog.LookAndFeelDialog;
import orioni.jz.io.FileType;
import orioni.jz.io.files.FileExtensionFilter;
import orioni.jz.io.files.FileUtilities;
import orioni.jz.util.configuration.*;
import orioni.jz.util.strings.StringUtilities;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.io.File;
import java.util.*;

/**
 * This abstract extension of the {@link JFrame} class contains functionality for creating a new file, opening and
 * saving files, and disposing the editor frame on exit.  The content pane is unaffected and can be manipulated freely
 * by the extending class.  Configuration on construction inclues specifying whether or not "new" is an available choice
 * in the file menu.  By default, this frame will be disposed when it is closed.
 * <p/>
 * This frame can maintain a set of configuration settings designed to make use of the software easier.  Among these
 * options is a history of edited files which appears in the File menu so as to avoid the user having to locate commonly
 * edited files frequently.  If this functionality is not desired, the constructor call can neglect to provide a
 * configuration file.
 * <p/>
 * Please note that, for exit confirmation to work properly, the {@link AbstractEditorFrame#contentsUpdated()} method
 * must be called whenever the application-specific data in the editing frame is modified.  Also note that the
 * accelerator keystrokes Ctrl-O, Ctrl-S, and Ctrl-A (as well as Ctrl-N if "New" is displayed) are used by this abstract
 * class.
 *
 * @author Zachary Palmer
 */
public abstract class AbstractEditorFrame extends JFrame
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The {@link ConfigurationElement} used to determine the size of the file history on startup.
     */
    protected static final ConfigurationElement<Integer> CONFIG_ELEMENT_FILE_HISTORY_SIZE =
            new BoundedIntegerConfigurationElement("file.history.size", 4, 1, 9);
    /**
     * The {@link ConfigurationElement} used to contain the file history.
     */
    protected static final ConfigurationElement<List<String>> CONFIG_ELEMENT_FILE_HISTORY =
            new StringListConfigurationElement("file.history", new ArrayList<String>());

    /**
     * The {@link ConfigurationElement} used to determine which of the loading filters was most recently selected.
     */
    protected static final IntegerConfigurationElement CONFIG_ELEMENT_LOADING_FILTER_INDEX =
            new IntegerConfigurationElement("load.filter", 0);
    /**
     * The {@link ConfigurationElement} used to determine which of the saving filters was most recently selected.
     */
    protected static final IntegerConfigurationElement CONFIG_ELEMENT_SAVING_FILTER_INDEX =
            new IntegerConfigurationElement("save.filter", 0);

    /**
     * The {@link ConfigurationElement} for the Look & Feel classnames in the configuration object.
     */
    protected static final StringConfigurationElement CONFIG_ELEMENT_UI_LOOK_AND_FEEL_CLASSNAME =
            new StringConfigurationElement("ui.lnf.classname", UIManager.getLookAndFeel().getClass().getName());

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The base title used in this {@link AbstractEditorFrame}.
     */
    protected String baseTitle;

    /**
     * Determines whether or not the file has been saved since its last modification.
     */
    protected boolean fileSaved;
    /**
     * The configuration file in which the {@link Properties} are stored which represent this application's
     * configuration.
     */
    protected File configurationFile;
    /**
     * The currently active file.
     */
    protected File activeFile;

    /**
     * The file history-related menu items.
     */
    protected JMenuItem[] fileHistoryItems;

    /**
     * The {@link ExtendedJMenuBar} which is displayed in this frame.
     */
    protected ExtendedJMenuBar extendedMenuBar;

    /**
     * The {@link JFileChooser} to use in selecting files.
     */
    protected JFileChooser fileChooser;

    /**
     * The current configuration for this {@link AbstractEditorFrame}.
     */
    protected Configuration configuration;
    /**
     * The {@link LookAndFeelDialog} used to manipulate Look & Feel.
     */
    protected LookAndFeelDialog lookAndFeelDialog;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the "new" option will be displayed.
     *
     * @param configurationFile The file which contains the {@link Properties} which represent the configuration of this
     *                          applcation.  If this functionality is not desired, <code>null</code> can be provided.
     * @param title             The title of the frame.
     */
    public AbstractEditorFrame(File configurationFile, String title)
    {
        this(configurationFile, title, true);
    }

    /**
     * General constructor.
     *
     * @param configurationFile The file which contains the {@link Properties} which represent the configuration of this
     *                          applcation.  If this functionality is not desired, <code>null</code> can be provided.
     * @param title             The title of the frame.
     * @param newDisplayed      Whether or not "new" is displayed as an option in the menu bar.
     */
    public AbstractEditorFrame(File configurationFile, String title, boolean newDisplayed)
    {
        super(title);
        baseTitle = title;

        fileSaved = true;
        this.configurationFile = configurationFile;
        configuration = new Configuration(
                CONFIG_ELEMENT_FILE_HISTORY, CONFIG_ELEMENT_FILE_HISTORY_SIZE, CONFIG_ELEMENT_UI_LOOK_AND_FEEL_CLASSNAME
        );
        tryLoadConfiguration();

        extendedMenuBar = new ExtendedJMenuBar();
        setJMenuBar(extendedMenuBar);
        if (newDisplayed)
        {
            extendedMenuBar.add(
                    "File", "New", new ExecuteMethodAction(this, "performMenuNew", true),
                    KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        }
        extendedMenuBar.add(
                "File", "Open", new ExecuteMethodAction(this, "performMenuOpen", true),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        extendedMenuBar.add(
                "File", "Save", new ExecuteMethodAction(this, "performMenuSave", true),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        extendedMenuBar.add(
                "File", "Save As...", new ExecuteMethodAction(this, "performMenuSaveAs", true),
                KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        extendedMenuBar.addSeparator("File");
        extendedMenuBar.add("File", "Exit", new ExecuteMethodAction(this, "performMenuExit", false));

        extendedMenuBar.add("Options", "Look & Feel", new ExecuteMethodAction(this, "performMenuLookAndFeel"));

        fileHistoryItems = new JMenuItem[configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY_SIZE)];
        extendedMenuBar.addSeparator("File");
        for (int i = 0; i < fileHistoryItems.length; i++)
        {
            fileHistoryItems[i] = extendedMenuBar.add("File", (i + 1) + ".");
            final int iFinal = i;
            fileHistoryItems[i].addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            List<String> history = configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY);
                            if ((history.size() > iFinal) && (history.get(iFinal) != null))
                            {
                                File historicalFile = new File(
                                        configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY).get(iFinal));
                                if (performLoad(historicalFile))
                                {
                                    setActiveFile(historicalFile);
                                    fileSaved = true;
                                } else
                                {
                                    setActiveFile(null);
                                    performClear();
                                }
                            }
                        }
                    });
        }
        updateFileHistoryDisplay();
        if ((configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY).size() > 0) &&
            (configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY).get(0) != null))
        {
            fileChooser = new JFileChooser(
                    new File(configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY).get(0)).getParentFile());
        } else
        {
            fileChooser = new JFileChooser();
        }
        extendedMenuBar.autoAssignAllMnemonics();

        lookAndFeelDialog = new LookAndFeelDialog(this, this, fileChooser);
        lookAndFeelDialog.setSelection(configuration.getValue(CONFIG_ELEMENT_UI_LOOK_AND_FEEL_CLASSNAME));
        lookAndFeelDialog.apply();
        lookAndFeelDialog.apply();

        setSaveEnabled(false);

        this.addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        performMenuExit();
                    }

                    public void windowClosed(WindowEvent e)
                    {
                        lookAndFeelDialog.dispose();
                    }
                });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Allows the user to manipulate this program's Look and Feel.
     */
    public void performMenuLookAndFeel()
    {
        lookAndFeelDialog.execute();
        configuration
                .setValue(CONFIG_ELEMENT_UI_LOOK_AND_FEEL_CLASSNAME, lookAndFeelDialog.getLastAppliedLookAndFeel());
    }

    /**
     * Sets whether or not the save options are enabled in the file menu.
     *
     * @param enabled <code>true</code> if the save menu items should be enabled; <code>false</code> otherwise.
     */
    protected void setSaveEnabled(boolean enabled)
    {
        ((ExtendedJMenu) (extendedMenuBar.getMenu("File"))).getItem("Save").setEnabled(enabled);
        ((ExtendedJMenu) (extendedMenuBar.getMenu("File"))).getItem("Save As...").setEnabled(enabled);
    }

    /**
     * Updates the file history display in the menu.
     */
    protected void updateFileHistoryDisplay()
    {
        List<String> historyList = configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY);
        for (int i = 0; i < fileHistoryItems.length; i++)
        {
            String suffix;
            if ((historyList.size() <= i) || (historyList.get(i) == null))
            {
                suffix = "(no file)";
            } else
            {
                suffix = historyList.get(i);
            }
            fileHistoryItems[i].setText((i + 1) + ". " + suffix);
        }
    }

    /**
     * Attempts to load the configuration file from the disk.  If this fails, the configuration will simply be an empty
     * {@link Properties} object.
     */
    protected void tryLoadConfiguration()
    {
        if (configuration.load(configurationFile) != null) configuration.revertToDefaults();
    }

    /**
     * Attempts to save the current configuration to the disk.  If this fails, it will do so silently.
     */
    protected void trySaveConfiguration()
    {
        configuration.save(configurationFile);
    }

    /**
     * Performs an operation which allows the user to create a new file.
     */
    public void performMenuNew()
    {
        fileSaved = true;
        setActiveFile(null);
        performNew();
        setSaveEnabled(true);
    }

    /**
     * Performs an operation which allows the user to select a file to open for editing.
     */
    public void performMenuOpen()
    {
        FileFilter[] filters = getLoadingFilters();
        int loadingFilterIndex = configuration.getValue(CONFIG_ELEMENT_LOADING_FILTER_INDEX);
        if ((loadingFilterIndex < 0) || (loadingFilterIndex >= filters.length)) loadingFilterIndex = 0;
        JZSwingUtilities.setJFileChooserFilters(fileChooser, filters[loadingFilterIndex], false, filters);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File f = fileChooser.getSelectedFile();
            if (fileChooser.getFileFilter() instanceof FileTypeFilter)
            {
                f =
                        FileUtilities.coerceFileExtension(
                                f, ((FileTypeFilter) (fileChooser.getFileFilter())).getFileType().getExtensions(true));
            } else
            {
                Set<String> set = new HashSet<String>();
                for (FileType type : getFileTypes())
                {
                    set.addAll(Arrays.asList(type.getExtensions(true)));
                }
                f = FileUtilities.coerceFileExtension(f, set.toArray(StringUtilities.EMPTY_STRING_ARRAY));
            }

            performLoadingOperation(f);
        }
    }

    /**
     * Performs the file loading operation.  This method is called by {@link AbstractEditorFrame#performMenuOpen()} once
     * a file is selected; it performs the various steps necessary to load the file, including calling {@link
     * AbstractEditorFrame#performLoad(File)}.  It is abstracted from the {@link AbstractEditorFrame#performMenuOpen()}
     * method in order to allow extensions of this class to programmatically load files while preventing them from
     * needing to directly manipulate the member fields of the {@link AbstractEditorFrame}.
     *
     * @param f The {@link File} to load.
     */
    protected void performLoadingOperation(File f)
    {
        if (performLoad(f))
        {
            setActiveFile(f);
            fileSaved = true;
        } else
        {
            performClear();
            setActiveFile(null);
        }
    }

    /**
     * Sets the active file for this {@link AbstractEditorFrame}.  This method should be called rather than setting the
     * field directly as it updates the frame's title, sets the save enabled state, and performs other such operations.
     *
     * @param activeFile The new active file.
     */
    protected void setActiveFile(File activeFile)
    {
        this.activeFile = activeFile;
        if (this.activeFile == null)
        {
            setTitle(baseTitle);
        } else
        {
            setTitle(baseTitle + " - " + this.activeFile);
            addToFileHistory(activeFile);
        }
        setSaveEnabled(this.activeFile != null);
        fileSaved = true;
    }

    /**
     * Performs an operation which allows the user to save the current file.
     *
     * @return <code>true</code> if the file was saved; <code>false</code> otherwise.
     */
    public boolean performMenuSave()
    {
        return performMenuSave(activeFile);
    }

    /**
     * Performs an operation which allows the user to save the current file.
     *
     * @param file The {@link File} in which to save, or <code>null</code> if one has not been selected.
     * @return <code>true</code> if the file was saved; <code>false</code> otherwise.
     */
    public boolean performMenuSave(File file)
    {
        if (file == null)
        {
            fileSaved = performMenuSaveAs();
        } else
        {
            fileSaved = performSave(file);
        }
        if (fileSaved) setActiveFile(file);
        return fileSaved;
    }

    /**
     * Performs an operation which allows the user to save the current file.  This method specifically displays a dialog
     * asking for the filename.
     *
     * @return <code>true</code> if the file was saved; <code>false</code> otherwise.
     */
    public boolean performMenuSaveAs()
    {
        FileFilter[] filters = getSavingFilters();
        int savingFilterIndex = configuration.getValue(CONFIG_ELEMENT_SAVING_FILTER_INDEX);
        if ((savingFilterIndex < 0) || (savingFilterIndex >= filters.length)) savingFilterIndex = 0;
        JZSwingUtilities.setJFileChooserFilters(fileChooser, filters[savingFilterIndex], false, filters);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            boolean ret = performMenuSave(
                    FileUtilities.forceFileExtension(
                            fileChooser.getSelectedFile(),
                            ((FileTypeFilter) (fileChooser.getFileFilter())).getFileType().getExtensions(true)));
            if (ret)
            {
                for (int i = 0; i < filters.length; i++)
                {
                    if (fileChooser.getFileFilter().equals(filters[i]))
                    {
                        configuration.setValue(CONFIG_ELEMENT_SAVING_FILTER_INDEX, i);
                        break;
                    }
                }
            }
            return ret;
        }
        return false;
    }

    /**
     * Performs an operation which allows the user to confirm exiting before doing so.  The confirmation will only occur
     * if the editor has unsaved contents.
     */
    public void performMenuExit()
    {
        if ((fileSaved) ||
            (JOptionPane.showConfirmDialog(
                    this,
                    "You haven't saved your work.  Are you sure you wish to quit?",
                    "Quit Without Saving?", JOptionPane.YES_NO_OPTION) ==
                                                                       JOptionPane.YES_OPTION))
        {
            trySaveConfiguration();
            this.dispose();
        }
    }

    /**
     * Informs this editor frame that its contents have been updated, allowing it to maintain an accurate confirm-exit
     * behavior.
     */
    public void contentsUpdated()
    {
        fileSaved = false;
    }

    /**
     * Clears the editor of any data.  Used whenever the load method fails.  By default, this method simply calls {@link
     * AbstractEditorFrame#performNew()}.
     */
    public void performClear()
    {
        performNew();
    }

    /**
     * Adds the provided {@link File} to the file history.
     *
     * @param f The file to add.
     */
    protected void addToFileHistory(File f)
    {
        removeFromFileHistory(f);
        List<String> history = configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY);
        history.add(0, f.getPath());
        while (history.size() > configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY_SIZE))
        {
            history.remove(history.size() - 1);
        }
        configuration.setValue(CONFIG_ELEMENT_FILE_HISTORY, history);
        updateFileHistoryDisplay();
    }

    /**
     * Removes the first instance of the provided {@link File} from the file history, if it exists.
     *
     * @param f The file to remove.
     */
    protected void removeFromFileHistory(File f)
    {
        List<String> history = configuration.getValue(CONFIG_ELEMENT_FILE_HISTORY);
        for (int i = history.size() - 1; i >= 0; i--)
        {
            if (new File(history.get(i)).equals(f)) history.remove(i);
        }
        configuration.setValue(CONFIG_ELEMENT_FILE_HISTORY, history);
        updateFileHistoryDisplay();
    }

    /**
     * Retrieves an array of filters for the types of files this frame can open.  This method will return at least one
     * filter.
     *
     * @return The appropriate file filters.
     */
    protected FileFilter[] getLoadingFilters()
    {
        FileFilter[] savingFilters = getSavingFilters();
        FileFilter[] ret = new FileFilter[savingFilters.length + 1];
        System.arraycopy(savingFilters, 0, ret, 1, savingFilters.length);
        Set<String> set = new HashSet<String>();
        for (FileType type : getFileTypes())
        {
            set.addAll(Arrays.asList(type.getExtensions(true)));
        }
        String[] exts = set.toArray(StringUtilities.EMPTY_STRING_ARRAY);
        Arrays.sort(exts);
        ret[0] = new SwingFileFilterWrapper(
                new FileExtensionFilter(set.toArray(StringUtilities.EMPTY_STRING_ARRAY)),
                "Supported File Types (*" + StringUtilities.createDelimitedList(", *", exts) + ")");
        return ret;
    }

    /**
     * Retrieves an array of filters for the types of files this frame can save.  This method will return at least one
     * filter.
     *
     * @return The appropriate file filters.
     */
    protected FileFilter[] getSavingFilters()
    {
        ArrayList<FileFilter> ret = new ArrayList<FileFilter>();
        for (FileType type : getFileTypes())
        {
            ret.add(new FileTypeFilter(type));
        }
        return ret.toArray(new FileFilter[0]);
    }

    /**
     * Creates a new file within this editing frame.
     */
    public abstract void performNew();

    /**
     * Loads a file into the editor.
     *
     * @param file The file to be loaded.
     * @return <code>true</code> if the load was successful; <code>false</code> otherwise.
     */
    public abstract boolean performLoad(File file);

    /**
     * Saves the current editor's file.
     *
     * @param file The file into which the current data is to be saved.
     * @return <code>true</code> if the save was successful; <code>false</code> otherwise.
     */
    public abstract boolean performSave(File file);

    /**
     * Retrieves the file types which are supported by this editor.
     *
     * @return The {@link FileType}s for this editor.
     */
    public abstract FileType[] getFileTypes();

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE