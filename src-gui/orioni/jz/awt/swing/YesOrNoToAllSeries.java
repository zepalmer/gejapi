package orioni.jz.awt.swing;

import orioni.jz.awt.swing.dialog.MultiOptionDialog;
import orioni.jz.reflect.ReflectionUtilities;

import java.awt.*;

/**
 * This class represents the execution of a series of dialogs.  A list of items is provided upon construction which will
 * be processed.  For each item, the user is asked to provide a selection of "Yes" or "No."  Each time the user makes a
 * selection, the corresponding method is called on this class.
 * <p/>
 * The options "Yes To All" and "No To All" also appear on the dialog.  The first causes the current item and all
 * remaining items in the series to be treated as if "Yes" were answered.  The second causes such items to be answered
 * with "No."  These options are provided as a convenience mechanism.
 * <p/>
 * The {@link MultiOptionDialog} class is used for choice presentation.  If the dialog is closed without an option being
 * chosen, this is interpreted as the answer "No."
 *
 * @author Zachary Palmer
 */
public abstract class YesOrNoToAllSeries<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The list of items to present one at a time.
     */
    protected T[] items;
    /**
     * The master of all generated dialogs.
     */
    protected Window masterWindow;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Uses the common frame as a master for all dialogs.
     *
     * @param items The list of items to present one at a time.
     */
    public YesOrNoToAllSeries(T... items)
    {
        this((Frame) null, items);
    }

    /**
     * General constructor.
     *
     * @param frame The frame to use as the master of all dialogs.
     * @param items The list of items to present one at a time.
     */
    public YesOrNoToAllSeries(Frame frame, T... items)
    {
        super();
        masterWindow = frame;
        this.items = items;
    }

    /**
     * General constructor.
     *
     * @param dialog The dialog to use as the master of all dialogs.
     * @param items  The list of items to present one at a time.
     */
    public YesOrNoToAllSeries(Dialog dialog, T... items)
    {
        super();
        masterWindow = dialog;
        this.items = items;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the title of the dialog displayed for a given item.
     *
     * @param item The item for which a dialog title is desired.
     * @return The title for the specified item.
     */
    public abstract String getDialogTitleFor(T item);

    /**
     * Retrieves the dialog message displayed for a given item.
     *
     * @param item The item for which a dialog message is desired.
     * @return The message for the specified item.
     */
    public abstract String getDialogMessageFor(T item);

    /**
     * This method is called whenever an item is displayed and "Yes" is selected.  For convenience, a default
     * implementation is provided which does nothing.
     *
     * @param item The item for which the choice was "yes."
     */
    public void choiceIsYes(T item)
    {
    }

    /**
     * This method is called whenever an item is displayed and "No" is selected.  For convenience, a default
     * implementation is provided which does nothing.
     *
     * @param item The item for which the choice was "no."
     */
    public void choiceIsNo(T item)
    {
    }

    /**
     * Executes the dialog box series.  The appropriate dialogs will be displayed in sequence.
     */
    public synchronized void execute()
    {
        final int yesOption = 2;
        final int noOption = 0;
        final int maybeOption = 1;
        int standing = maybeOption;
        boolean masterIsDialog = ReflectionUtilities.instanceOf(masterWindow, Dialog.class);
        String[] options = new String[]{"Yes", "No", "Yes To All", "No To All"};
        String[] lastOptions = new String[]{"Yes", "No"};

        for (int i = 0; i < items.length; i++)
        {
            T item = items[i];
            int choice = maybeOption;
            if (standing == maybeOption)
            {
                MultiOptionDialog dialog;
                if (masterIsDialog)
                {
                    dialog = new MultiOptionDialog(
                            (Dialog) masterWindow, getDialogTitleFor(item), true, getDialogMessageFor(item),
                            (i == items.length - 1 ? lastOptions : options));
                } else
                {
                    dialog = new MultiOptionDialog(
                            (Frame) masterWindow, getDialogTitleFor(item), true, getDialogMessageFor(item),
                            (i == items.length - 1 ? lastOptions : options));
                }
                switch (dialog.execute())
                {
                    case 0: // Yes
                        choice = yesOption;
                        break;
                    case MultiOptionDialog.OPTION_UNKNOWN:
                    case MultiOptionDialog.OPTION_CANCEL:
                    case 1: // No
                        choice = noOption;
                        break;
                    case 2: // Yes To All
                        choice = yesOption;
                        standing = yesOption;
                        break;
                    case 3: // No To All
                        choice = noOption;
                        standing = noOption;
                        break;
                }
                dialog.dispose();
            } else
            {
                choice = standing;
            }
            if (choice == yesOption)
            {
                choiceIsYes(item);
            } else
            {
                choiceIsNo(item);
            }
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}