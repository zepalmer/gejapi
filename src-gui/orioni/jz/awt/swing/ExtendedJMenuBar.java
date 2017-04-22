package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;

/**
 * This class is designed to function as a JMenuBar but allow the organization of the JMenu objects contained within.
 * Any JMenu object added to a ExtendedJMenuBar can be accessed by passing its text to the <code>getMenu(String)</code>
 * method.  Likewise, ExtendedJMenu objects allow access to JMenuItems by their texts.
 * <p/>
 * The {@link ExtendedJMenuBar} also allows {@link JMenu} items to be added in a more direct fashion.  The {@link
 * ExtendedJMenuBar#add(String,String)} method directly creates a {@link JMenuItem} and, if necessary, creates the
 * {@link JMenu} in which it will reside.
 *
 * @author Zachary Palmer
 * @see ExtendedJMenu
 */
public class ExtendedJMenuBar extends JMenuBar
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     */
    public ExtendedJMenuBar()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Automatically generates mnemonic characters for any menus which do not have them.  This is performed by searching
     * the title first for capital letters, then for lower case letters, and using the first character which is not in
     * use as a mnemonic for a different menu.
     */
    public void autoAssignMenuMnemonics()
    {
        AbstractButton[] buttons = new AbstractButton[getMenuCount()];
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = getMenu(i);
        }
        JZSwingUtilities.setBestMnemonicsFor(buttons);
    }

    /**
     * Automatically generates mnemonic characters for <i>all</i> menus and menu items within this {@link
     * ExtendedJMenuBar}.  For menu items to be automatically assigned mnemonics, their containing {@link JMenu} must be
     * an {@link ExtendedJMenu}.
     *
     * @see ExtendedJMenuBar#autoAssignMenuMnemonics()
     * @see ExtendedJMenu#autoAssignMenuItemMnemonics()
     */
    public void autoAssignAllMnemonics()
    {
        this.autoAssignMenuMnemonics();
        for (int i = 0; i < this.getMenuCount(); i++)
        {
            if (this.getMenu(i) instanceof ExtendedJMenu)
            {
                ((ExtendedJMenu) (this.getMenu(i))).autoAssignMenuItemMnemonics();
            }
        }
    }

    /**
     * Adds a {@link JMenuItem} under an implied series of {@link JMenu}s.  If one of the {@link JMenu}s does not exist,
     * it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to the first {@link JMenu} tree
     * which matches the prescribed set of names.  The created {@link JMenuItem} is then returned.
     * <p/>
     * If the last value of the list is <code>null</code> rather than a menu item name, a separator is added instead and
     * <code>null</code> is returned.  Only the last name may be <code>null</code>.
     * <p/>
     * If only one name is supplied, a separator is added to the menu.
     *
     * @param names The names of the menus and menu item.
     * @return The created {@link JMenuItem}.
     */
    public JMenuItem add(String... names)
    {
        if (names.length == 0)
        {
            throw new IllegalArgumentException("Names array of length zero is meaningless.");
        } else if (names.length == 1)
        {
            getMenuForced(names[0]).addSeparator();
            return null;
        } else
        {
            JMenu parent = getMenuForced(names[0]);
            for (int i = 1; i < names.length - 1; i++)
            {
                JMenu child = null;
                for (Component c : parent.getMenuComponents())
                {
                    if ((c instanceof JMenu) && (((JMenu) c).getText().equals(names[i])))
                    {
                        child = (JMenu) c;
                        break;
                    }
                }
                if (child == null)
                {
                    child = new JMenu(names[i]);
                    parent.add(child);
                }
                parent = child;
            }
            if (names[names.length - 1] != null)
            {
                JMenuItem item = new JMenuItem(names[names.length - 1]);
                parent.add(item);
                return item;
            } else
            {
                parent.addSeparator();
                return null;
            }
        }
    }

    /**
     * Adds a {@link JMenuItem} under an implied {@link JMenu}.  If an {@link JMenu} does not exist with the specified
     * name, it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to the first {@link JMenu}
     * with that name.  The created {@link JMenuItem} is returned.
     * <p/>
     * If <code>itemName</code> is <code>null</code> rather than an actual {@link String}, a separator is added instead
     * and <code>null</code> is returned.  <code>menuName</code> must not be <code>null</code>.
     *
     * @param menuName The name of the {@link JMenu}.
     * @param itemName The name of the {@link JMenuItem}.
     * @return The created {@link JMenuItem}.
     */
    public JMenuItem add(String menuName, String itemName)
    {
        return add(menuName, itemName, getMenuForced(menuName).getMenuComponentCount());
    }

    /**
     * Adds a {@link JMenuItem} under an implied {@link JMenu}.  If an {@link JMenu} does not exist with the specified
     * name, it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to the first {@link JMenu}
     * with that name.  The provided {@link Action} is then added to the item as an {@link
     * java.awt.event.ActionListener}. The provided {@link Action} is then returned.
     *
     * @param menuName The name of the {@link JMenu}.
     * @param itemName The name of the {@link JMenuItem}.
     * @param action    The {@link Action} to attach to the {@link JMenuItem}.
     * @return The created {@link JMenuItem}.
     */
    public JMenuItem add(String menuName, String itemName, Action action)
    {
        JMenuItem item = add(menuName, itemName);
        item.addActionListener(action);
        return item;
    }

    /**
     * Adds a {@link JMenuItem} under an implied {@link JMenu}.  If an {@link JMenu} does not exist with the specified
     * name, it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to the first {@link JMenu}
     * with that name.  The provided {@link Action} is then added to the item as an {@link
     * java.awt.event.ActionListener}.  Additionally, the provided {@link KeyStroke} is set as the accelerator for the
     * {@link JMenuItem}.  The provided {@link Action} is then returned.
     *
     * @param menuName The name of the {@link JMenu}.
     * @param itemName The name of the {@link JMenuItem}.
     * @param action    The {@link Action} to attach to the {@link JMenuItem}.
     * @param keystroke The {@link KeyStroke} to use to set the accelerator.
     * @return The created {@link JMenuItem}.
     */
    public JMenuItem add(String menuName, String itemName, Action action, KeyStroke keystroke)
    {
        JMenuItem item = add(menuName, itemName, action);
        item.setAccelerator(keystroke);
        return item;
    }

    /**
     * Adds a {@link JMenuItem} under an implied {@link JMenu} at the specified index.  If an {@link JMenu} does not
     * exist with the specified name, it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to
     * the first {@link JMenu} with that name.  The created {@link JMenuItem} is returned.
     * <p/>
     * If <code>itemName</code> is <code>null</code> rather than an actual {@link String}, a separator is added instead
     * and <code>null</code> is returned.  <code>menuName</code> must not be <code>null</code>.
     *
     * @param menuName The name of the {@link JMenu}.
     * @param itemName The name of the {@link JMenuItem}.
     * @param index     The index in the menu at which to add the item.
     * @return The created {@link JMenuItem}.
     */
    public JMenuItem add(String menuName, String itemName, int index)
    {
        JMenu menu = this.getMenuForced(menuName);
        if (itemName != null)
        {
            JMenuItem item = new JMenuItem(itemName);
            menu.insert(item, index);
            return item;
        } else
        {
            menu.insertSeparator(index);
            return null;
        }
    }

    /**
     * Adds a {@link JMenuItem} under an implied {@link JMenu} at the specified index.  If an {@link JMenu} does not
     * exist with the specified name, it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to
     * the first {@link JMenu} with that name.  The provided {@link Action} is then added to the item as an {@link
     * java.awt.event.ActionListener}. The created {@link JMenuItem} is then returned.
     *
     * @param menuName The name of the {@link JMenu}.
     * @param itemName The name of the {@link JMenuItem}.
     * @param index     The index in the menu at which to add the item.
     * @param action    The {@link Action} to attach to the {@link JMenuItem}.
     * @return The provided {@link Action} is then returned.
     */
    public JMenuItem add(String menuName, String itemName, int index, Action action)
    {
        JMenuItem item = add(menuName, itemName, index);
        item.addActionListener(action);
        return item;
    }

    /**
     * Adds a {@link JMenuItem} under an implied {@link JMenu} at the specified index.  If an {@link JMenu} does not
     * exist with the specified name, it is created as an {@link ExtendedJMenu}.  The {@link JMenuItem} is then added to
     * the first {@link JMenu} with that name.  The provided {@link Action} is then added to the item as an {@link
     * java.awt.event.ActionListener}. Additionally, the provided {@link KeyStroke} is set as the accelerator for the
     * {@link JMenuItem}.  The created {@link JMenuItem} is then returned.
     *
     * @param menuName The name of the {@link JMenu}.
     * @param itemName The name of the {@link JMenuItem}.
     * @param index     The index in the menu at which to add the item.
     * @param action    The {@link Action} to attach to the {@link JMenuItem}.
     * @param keystroke The {@link KeyStroke} to use to set the accelerator.
     * @return The provided {@link Action} is then returned.
     */
    public JMenuItem add(String menuName, String itemName, int index, Action action, KeyStroke keystroke)
    {
        JMenuItem item = add(menuName, itemName, index, action);
        item.setAccelerator(keystroke);
        return item;
    }

    /**
     * Adds a separator to the {@link JMenu} of the specified name.
     *
     * @param menuName The name of the {@link JMenu} to which the separator should be added.
     */
    public void addSeparator(String menuName)
    {
        JMenu menu = this.getMenu(menuName);
        if (menu != null) menu.addSeparator();
    }

    /**
     * Inserts a separator into the {@link JMenu} of the specified name.
     *
     * @param menuName The name of the {@link JMenu} to which the separator should be added.
     * @param index     The index at which to insert the seperator.
     */
    public void insertSeparator(String menuName, int index)
    {
        JMenu menu = this.getMenu(menuName);
        if (menu != null) menu.insertSeparator(index);
    }

    /**
     * Allows a user to retrieve a JMenu based on its text.
     *
     * @param s The text of the JMenu.
     * @return The first JMenu with the given String as its text, or <code>null</code> if no such JMenu is contained
     *         within this JMenuBar.
     */
    public JMenu getMenu(String s)
    {
        int menuCount = getMenuCount();
        for (int i = 0; i < menuCount; i++)
        {
            if (getMenu(i).getText().equals(s)) return getMenu(i);
        }
        return null;
    }

    /**
     * Allows a user to retrieve a JMenu based on its text.  If the menu does not exist, it is created.
     *
     * @param s The text of the JMenu.
     * @return The first JMenu with the given String as its text, or <code>null</code> if no such JMenu is contained
     *         within this JMenuBar.
     */
    public JMenu getMenuForced(String s)
    {
        int menuCount = getMenuCount();
        for (int i = 0; i < menuCount; i++)
        {
            if (getMenu(i).getText().equals(s)) return getMenu(i);
        }
        ExtendedJMenu menu = new ExtendedJMenu(s);
        this.add(menu);
        return menu;
    }

    /**
     * Allows a user to retrieve a {@link JMenuItem} based on its text.
     *
     * @param menuText The text on the {@link JMenu}.
     * @param itemText The text on the {@link JMenuItem}.
     * @return The first {@link JMenuItem} with matching text which appears in the first {@link JMenu} with matching
     *         text, or <code>null</code> if no such text can be found.
     */
    public JMenuItem getMenuItem(String menuText, String itemText)
    {
        int menuCount = getMenuCount();
        for (int i = 0; i < menuCount; i++)
        {
            if (getMenu(i).getText().equals(menuText))
            {
                JMenu menu = getMenu(i);
                if (menu instanceof ExtendedJMenu)
                {
                    ExtendedJMenu exMenu = (ExtendedJMenu) menu;
                    return exMenu.getItem(itemText);
                } else
                {
                    for (int j = 0; j < menu.getItemCount(); j++)
                    {
                        if (menu.getItem(j).getText().equals(itemText))
                        {
                            return menu.getItem(j);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Allows a user to retrieve a ExtendedJMenu based on its text.  If a JMenu is found matching the text provided but
     * no ExtendedJMenu is found matching that text, <code>null</code> is returned.
     *
     * @param s The text of the JMenu.
     * @return The first JMenu with the given String as its text, or <code>null</code> if no such ExtendedJMenu is
     *         contained within this JMenuBar.
     */
    public ExtendedJMenu getExtendedMenu(String s)
    {
        int menuCount = getMenuCount();
        for (int i = 0; i < menuCount; i++)
        {
            JMenu j = getMenu(i);
            if ((j.getText().equals(s)) && (j instanceof ExtendedJMenu)) return (ExtendedJMenu) (getMenu(i));
        }
        return null;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //