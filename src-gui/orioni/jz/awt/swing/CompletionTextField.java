package orioni.jz.awt.swing;

import orioni.jz.awt.swing.convenience.ComponentConstructorPanel;
import orioni.jz.awt.swing.list.SortedListModel;
import orioni.jz.math.MathUtilities;
import orioni.jz.util.HashMultiMap;
import orioni.jz.util.MultiMap;
import orioni.jz.util.Pair;
import orioni.jz.util.Utilities;
import orioni.jz.util.comparator.CaseInsensitiveAlphabeticalToStringComparator;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * This {@link JTextField} extension is designed to permit the use of completion keystrokes in text editing.  Completion
 * is managed through a series of completion contexts.  A completion context consists of three items: a list of regular
 * expressions which matches all strings to be completed by the context as well as any partially completed string in the
 * context, a list of all legal strings in the context, and a keystroke to activate completion.
 * <p/>
 * The text completer works as follows: <ul><li>A completion keystroke is received and consumed.</li> <li>All completion
 * contexts registered with that keystroke are checked for plausibility.</li> <ul><li>If the string between the
 * beginning of the text field and the cursor cannot be matched to one of the regular expression for the context, this
 * context is not used in this completion.</li> <li>If a possible match is found, all strings for that context are
 * matched against the text from the beginning of the possible match.  Those which are identical to the possible match
 * are collected.</li> </ul> <li>All collected strings from all contexts are sorted according to a general sorting
 * mechanism and placed in a popup menu.</li> <li>The popup menu is displayed.  If the user selects an item from the
 * popup menu, the completion text is inserted excluding that portion which had already existed before this process
 * began.</li> </ul> This class also supports several behavioral options.  Firstly, if no items are found for
 * completion, a popup can be displayed stating as much.  Secondly, if only one item is found, it is possible for the
 * text completer to use that item immediately without displaying a popup menu.  Finally, it is possible for the text
 * completer to recognize a fully typed item and offer to replace it beyond the cursor position.
 * <p/>
 * As an example of the last feature, consider the text "<code>a charge</code>".  Presume the cursor is positioned after
 * the fifth character, '<code>a</code>', and that the possible completions in this context are "<code>charge</code>"
 * and "<code>challenge</code>".  If the text completer were set to replace beyond the cursor, the text after completion
 * and after selecting the "<code>challenge</code>" option would be "<code>a challenge</code>"; both the portion of the
 * word before the cursor ("<code>cha</code>") and the portion after ("<code>rge</code>") have been replaced.  If the
 * completer is set to avoid such completion, the text would read "<code>a challengerge</code>"; the "<code>rge</code>"
 * has been left intact.  The latter case may be desirable in some scripting or programming environments.
 *
 * @author Zachary Palmer
 */
public class CompletionTextField extends JTextField
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The completion context map for this {@link CompletionTextField}.  The key is a pairing in which the first item is
     * the key code and the second is the modifier set for that keystroke.
     */
    protected MultiMap<Pair<Integer, Integer>, CompletionContext> contextMap;

    /**
     * The popup string displayed by the completer if no choices are found, or <code>null</code> for no popup.
     */
    protected String popupString;
    /**
     * Whether or not completions with a single possibility are automatically filled out.
     */
    protected boolean autoChooseOnly;
    /**
     * Whether or not completions are required to preserve text after the cursor.
     */
    protected boolean preserveAfterCursor;

    /**
     * The {@link Comparator} used to sort the list of possible options.  This comparator sorts the display objects, not
     * necessarily the strings themselves.
     */
    protected Comparator<Object> comparator;
    /**
     * The {@link ListCellRenderer} used to display the menu.
     */
    protected ListCellRenderer renderer;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Initializes with a blank string, a size of <code>10</code> characters, and no completion
     * contexts.
     */
    public CompletionTextField()
    {
        this("", 10);
    }

    /**
     * General constructor.
     */
    public CompletionTextField(String text, int columns, CompletionContext... contexts)
    {
        super(text, columns);
        contextMap = new HashMultiMap<Pair<Integer, Integer>, CompletionContext>();
        for (CompletionContext context : contexts)
        {
            addContext(context);
        }
        popupString = null;
        autoChooseOnly = true;
        preserveAfterCursor = false;
        // TODO: permit this comparator to be changed
        comparator = new CaseInsensitiveAlphabeticalToStringComparator<Object>();
        renderer = new DefaultListCellRenderer();

        this.addKeyListener(
                new KeyAdapter()
                {
                    public void keyPressed(KeyEvent e)
                    {
                        Pair<Integer, Integer> key = new Pair<Integer, Integer>(e.getKeyCode(), e.getModifiers());
                        if (contextMap.getAll(key).size() > 0)
                        {
                            presentMenuFor(contextMap.getAll(key));
                        }
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a {@link CompletionContext} to this {@link CompletionTextField}.
     *
     * @param context The {@link CompletionContext} to add.
     */
    public void addContext(CompletionContext context)
    {
        contextMap.put(new Pair<Integer, Integer>(context.getKeycode(), context.getModifiers()), context);
    }

    /**
     * Retrieves the popup string which will be displayed if completion fails.
     *
     * @return This {@link CompletionTextField}'s popup string, or <code>null</code> if this behavior is not being
     *         used.
     */
    public String getPopupString()
    {
        return popupString;
    }

    /**
     * Changes the popup string which will be displayed if completion fails.
     *
     * @param popup The new popup string, or <code>null</code> to disable this behavior.
     */
    public void setpopupString(String popup)
    {
        popupString = popup;
    }

    /**
     * Determines whether or not completions with exactly one possibility will automatically be selected.
     *
     * @return <code>true</code> if single possibilities are automatically selected; <code>false</code> if they appear
     *         in a menu as normal.
     */
    public boolean isAutoSelectingSingles()
    {
        return autoChooseOnly;
    }

    /**
     * Changes whether or not completions with exactly one possibility will automatically be selected.
     *
     * @param autoSelect <code>true</code> if single possibilities are to be automatically selected; <code>false</code>
     *                    if they should appear in a menu as normal.
     */
    public void setAutoSeletingSingles(boolean autoSelect)
    {
        autoChooseOnly = autoSelect;
    }

    /**
     * Determines whether or not text after the cursor will be preserved.
     *
     * @return <code>true</code> if text after the cursor will be preserved; <code>false</code> if it may be replaced.
     */
    public boolean isPreservingAfterCursor()
    {
        return preserveAfterCursor;
    }

    /**
     * Changes whether or not text after the cursor will be preserved.
     *
     * @param preserve <code>true</code> if text after the cursor must be preserved; <code>false</code> to allow the
     *                 completer to replace text after the cursor as well as before.
     */
    public void setPreserveAfterCursor(boolean preserve)
    {
        preserveAfterCursor = preserve;
    }

    /**
     * Creates a popup menu for the provided set of contexts.  If there are no matches and a popup is set, it will be
     * displayed.  If there is exactly one match and auto choose is enabled, that item will be selected.  In all other
     * cases, the user will be presented with a popup menu containing all of the items which match the text in
     * question.
     *
     * @param contexts The {@link CompletionContext}s for which a popup is desired.
     */
    protected void presentMenuFor(Set<CompletionContext> contexts)
    {
        Set<CompletionOption> options = new HashSet<CompletionOption>();
        for (CompletionContext context : contexts)
        {
            int startIndex = -1;
            int endIndex = this.getCaretPosition();
            if (this.getCaretPosition() > 0)
            {
                int cursor = this.getCaretPosition() - 1;
                while ((cursor >= 0) && (startIndex == -1))
                {
                    if (context.canComplete(this.getText().substring(cursor, endIndex)))
                    {
                        startIndex = cursor;
                    }
                    cursor--;
                }
            }

            if (startIndex != -1)
            {
                // Looks like we've found a match for this context.
                if (!preserveAfterCursor)
                {
                    while ((endIndex < this.getText().length()) &&
                           (context.canComplete(this.getText().substring(startIndex, endIndex + 1))))
                    {
                        endIndex++;
                    }
                }

                // Now we've established the indices of the text to be replaced.  Add any options that match to
                // the set of strings.  We're using a pattern matcher rather than String.startsWith to ensure that
                // any Pattern flags are still used (such as case switches).
                for (Pair<String, Object> possibility :
                        context.getPossibilitiesFor(this.getText().substring(startIndex, this.getCaretPosition())))
                {
                    options.add(
                            new CompletionOption(
                                    possibility.getFirst(), possibility.getSecond(), startIndex, endIndex));
                }
            }
        }
        if (options.size() == 0)
        {
            // Display no suggestions popup
            if (popupString != null)
            {
                final JWindow popup = new JWindow();
                popup.setContentPane(
                        new ComponentConstructorPanel(
                                new FlowLayout(),
                                new JLabel(popupString)));
                popup.pack();
                Point point = this.getLocationOnScreen();
                popup.setLocation((int) (point.getX()), (int) (point.getY() + this.getHeight()));
                popup.setVisible(true);
                new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(2000);
                        } catch (InterruptedException e)
                        {
                            popup.dispose();
                        }
                    }
                }.start();
            }
        } else if ((options.size() == 1) && (autoChooseOnly))
        {
            // Auto perform replacement
            performReplacement(options.iterator().next());
        } else
        {
            // Present popup menu
            CompletionPopupMenu menu = new CompletionPopupMenu(options, this);
            menu.show(this, 0, this.getHeight());
        }
    }

    /**
     * Performs the replacement on this {@link CompletionTextField} as specified by the provided option.
     *
     * @param option The {@link CompletionOption} which describes the replacement which should occur.
     */
    protected void performReplacement(CompletionOption option)
    {
        this.setText(
                this.getText().substring(0, option.getStartIndex()) + option.getString() +
                this.getText().substring(option.getEndIndex()));
    }

    /**
     * Changes the {@link ListCellRenderer} used to render this {@link CompletionTextField}.
     *
     * @param renderer The {@link ListCellRenderer} to use to render the display objects in the popup menu.
     */
    public void setPopupRenderer(ListCellRenderer renderer)
    {
        this.renderer = renderer;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This {@link BasicComboPopup} extension is designed to operate on its parent {@link CompletionTextField} by
     * allowing the user to select a single option to replace the completion text.
     */
    class CompletionPopupMenu extends BasicComboPopup
    {
        /**
         * The {@link SortedListModel} which backs the display.
         */
        protected SortedListModel model;

        /**
         * General constructor.
         *
         * @param options A set of {@link CompletionOption}s which should be used in this menu.
         * @param parent  The {@link JComponent} to which the listeners should be attached.
         */
        public CompletionPopupMenu(Set<CompletionOption> options, final JComponent parent)
        {
            super(
                    new JComboBox(
                            new SortedListModel(
                                    Utilities.EMPTY_OBJECT_ARRAY, true, new CompletionOptionDisplayComparator())));
            getComboBox().setRenderer(new CompletionOptionRenderer());
            getList().addMouseListener(
                    new MouseAdapter()
                    {
                        public void mouseClicked(MouseEvent e)
                        {
                            if (e.getButton() == MouseEvent.BUTTON1)
                            {
                                getList().setSelectedIndex(getList().locationToIndex(e.getPoint()));
                                complete((CompletionOption) (getList().getSelectedValue()));
                            }
                        }
                    });

            // Attach listeners to parent.
            final KeyAdapter keyControl = new KeyAdapter() // steal keystrokes from parent
            {
                public void keyPressed(KeyEvent e)
                {
                    switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_ENTER:
                            complete(getSelectedOption());
                            break;
                        case KeyEvent.VK_DOWN:
                            adjustSelectedIndex(1);
                            break;
                        case KeyEvent.VK_UP:
                            adjustSelectedIndex(-1);
                            break;
                        case KeyEvent.VK_PAGE_UP:
                            adjustSelectedIndex(-6);
                            break;
                        case KeyEvent.VK_PAGE_DOWN:
                            adjustSelectedIndex(6);
                            break;
                        case KeyEvent.VK_HOME:
                            adjustSelectedIndex(Integer.MIN_VALUE / 4);
                            break;
                        case KeyEvent.VK_END:
                            adjustSelectedIndex(Integer.MAX_VALUE / 4);
                            break;
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_RIGHT:
                            setVisible(false);
                            return;
                        default:
                            setVisible(false);
                            return;
                    }
                    e.consume();
                }
            };
            parent.addKeyListener(keyControl);
            this.addPopupMenuListener(
                    new PopupMenuListener()
                    {
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                        {
                        }

                        public void popupMenuCanceled(PopupMenuEvent e)
                        {
                            parent.removeKeyListener(keyControl);
                        }

                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                        {
                            parent.removeKeyListener(keyControl);
                        }
                    });

            // Construct the menu
            buildMenu(options);
        }

        /**
         * Performs completion using the specified {@link CompletionOption}.
         *
         * @param option The {@link CompletionOption} with which to complete.
         */
        protected void complete(CompletionOption option)
        {
            performReplacement(option);
            setVisible(false);
        }

        /**
         * Constructs the contents of this menu.  This method is abstracted away from the constructor as it may need to
         * be called to reconstruct the menu at a later time.
         *
         * @param options The {@link CompletionOption} to use.
         */
        protected void buildMenu(Set<CompletionOption> options)
        {
            getModel().clear();

            for (CompletionOption option : options)
            {
                getModel().addSorted(option);
            }
        }

        /**
         * Sets the visibility of the popup menu.  If the visibility is set to <code>true</code>, the combo box is given
         * focus.
         *
         * @param b true to make the popup visible, or false to hide it
         */
        public void setVisible(boolean b)
        {
            getComboBox().setSelectedIndex(0);
            super.setVisible(b);
            if (b) getComboBox().requestFocus();
        }

        /**
         * Retrieves this menu's combo box.
         */
        private JComboBox getComboBox()
        {
            return comboBox;
        }

        /**
         * Retrieves this menu's combo box model.
         */
        private SortedListModel getModel()
        {
            return (SortedListModel) (getComboBox().getModel());
        }

        /**
         * Retrieves the currently selected {@link CompletionOption}.
         *
         * @return The currently selected {@link CompletionOption}.
         */
        protected CompletionOption getSelectedOption()
        {
            return (CompletionOption) (getComboBox().getSelectedItem());
        }

        /**
         * Adjusts the selected index, bounding it as necessary.
         *
         * @param offset The amount by which to adjust the selected index.
         */
        protected void adjustSelectedIndex(int offset)
        {
            getList().setSelectedIndex(
                    MathUtilities.bound(getList().getSelectedIndex() + offset, 0, getModel().getSize() - 1));
            getComboBox().setSelectedIndex(
                    MathUtilities.bound(getComboBox().getSelectedIndex() + offset, 0, getModel().getSize() - 1));
        }
    }

    /**
     * This class represents a selection which is available in the popup completion menu.
     *
     * @author Zachary Palmer
     */
    static class CompletionOption
    {
        /**
         * The starting index of completion.
         */
        protected int startIndex;
        /**
         * The ending index of completion (exclusive).
         */
        protected int endIndex;
        /**
         * The string to use to complete.
         */
        protected String string;
        /**
         * The display object for this option.
         */
        protected Object display;

        /**
         * General constructor.
         *
         * @param string      The string to use to complete.
         * @param display     The {@link Object} to use to display this option in the menu.
         * @param startIndex The starting index of completion.
         * @param endIndex   The ending index of completion (exclusive).
         */
        public CompletionOption(String string, Object display, int startIndex, int endIndex)
        {
            this.endIndex = endIndex;
            this.startIndex = startIndex;
            this.string = string;
            this.display = display;
        }

        public String getString()
        {
            return string;
        }

        public Object getDisplay()
        {
            return display;
        }

        public int getStartIndex()
        {
            return startIndex;
        }

        public int getEndIndex()
        {
            return endIndex;
        }

        /**
         * Returns the {@link String} stored in this {@link CompletionOption}.  Used for sorting.
         *
         * @return The {@link String} stored in this {@link CompletionOption}.
         */
        public String toString()
        {
            return display.toString();
        }
    }

    /**
     * This {@link Comparator} is designed to sort the {@link CompletionOption}s by their display objects, using the
     * {@link CompletionTextField}'s comparator.
     *
     * @author Zachary Palmer
     */
    class CompletionOptionDisplayComparator implements Comparator<CompletionOption>
    {
        /**
         * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer as the first
         * argument is less than, equal to, or greater than the second.
         */
        public int compare(CompletionOption a, CompletionOption b)
        {
            return comparator.compare(a.getDisplay(), b.getDisplay());
        }
    }

    /**
     * This {@link ListCellRenderer} is designed to render {@link CompletionOption}s by rendering their display objects,
     * using the {@link ListCellRenderer} on this {@link CompletionTextField}.
     *
     * @author Zachary Palmer
     */
    class CompletionOptionRenderer implements ListCellRenderer
    {
        /**
         * Return a component that has been configured to display the specified value. That component's
         * <code>paint</code> method is then called to "render" the cell.  If it is necessary to compute the dimensions
         * of a list because the list cells do not have a fixed size, this method is called to generate a component on
         * which <code>getPreferredSize</code> can be invoked.
         *
         * @param list         The JList we're painting.
         * @param value        The value returned by list.getModel().getElementAt(index).
         * @param index        The cells index.
         * @param isSelected   True if the specified cell was selected.
         * @param cellHasFocus True if the specified cell has the focus.
         * @return A component whose paint() method will render the specified value.
         * @see javax.swing.JList
         * @see javax.swing.ListSelectionModel
         * @see javax.swing.ListModel
         */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus)
        {
            return renderer.getListCellRendererComponent(
                    list, ((CompletionOption) (value)).getDisplay(), index, isSelected, cellHasFocus);
        }
    }
}

// END OF FILE
