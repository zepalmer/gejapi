package orioni.jz.awt.swing;

import javax.swing.*;

/**
 * This class is designed to function as a JMenu but allow the organization of the JMenuItem objects contained within.
 * Any JMenuItem object added to a ExtendedJMenu can be accessed by passing its text to the <code>getItem(String)</code>
 * method.
 *
 * @author Zachary Palmer
 * @see ExtendedJMenuBar
 */
public class ExtendedJMenu extends JMenu
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Wrapper constructor.
     *
     * @see JMenu#JMenu
     */
    public ExtendedJMenu()
    {
        super();
    }

    /**
     * Wrapper constructor.
     *
     * @see JMenu#JMenu(Action)
     */
    public ExtendedJMenu(Action a)
    {
        super(a);
    }

    /**
     * Wrapper constructor.
     *
     * @see JMenu#JMenu(String)
     */
    public ExtendedJMenu(String s)
    {
        super(s);
    }

    /**
     * Wrapper constructor.
     *
     * @see JMenu#JMenu(String,boolean)
     */
    public ExtendedJMenu(String s, boolean b)
    {
        super(s, b);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves a JMenuItem based on its text.
     *
     * @param s The text of the JMenuItem to retrieve.
     * @return The first JMenuItem matching the provided text, or <code>null</code> if no such JMenuItem is part of this
     *         JMenu.
     */
    public JMenuItem getItem(String s)
    {
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++)
        {
            if ((getItem(i) != null) && (getItem(i).getText().equals(s)))
            {
                return getItem(i);
            }
        }
        return null;
    }

    /**
     * A convenience for adding items.  This method will add items with the names specified to this ExtendedJMenu.
     *
     * @param names An array of Strings that specify the names of the items to add.  A <code>null</code> adds a
     *              separator.
     */
    public void add(String[] names)
    {
        int i;
        // Add the names
        for (i = 0; i < names.length; i++)
        {
            if (names[i] == null)
            {
                addSeparator();
            } else
            {
                add(names[i]);
            }
        }
    }

    /**
     * A convenience for adding items.  This method will add items with the names specified to this ExtendedJMenu. This
     * method will also set the mnemonics for those items.
     *
     * @param names     An array of Strings that specify the names of the items to add.  A <code>null</code> adds a
     *                  separator.
     * @param mnemonics An array of integers that specify the mnemonics to add to the items.  Any value associated with
     *                  a <code>null</code> in the <code>names</code> parameter is ignored.
     * @throws ArrayIndexOutOfBoundsException If the <code>mnemonics</code> parameter is smaller in length than the
     *                                        <code>names</code> parameter.
     */
    public void add(String[] names, int[] mnemonics)
            throws ArrayIndexOutOfBoundsException
    {
        int i;
        // Add the names
        for (i = 0; i < names.length; i++)
        {
            if (names[i] == null)
            {
                addSeparator();
            } else
            {
                add(names[i]);
                getItem(names[i]).setMnemonic(mnemonics[i]);
            }
        }
    }

    /**
     * Automatically generates mnemonic characters for any menu items which do not have them.  This is performed by
     * searching the title first for capital letters, then for lower case letters, and using the first character which
     * is not in use as a mnemonic for a different menu.
     */
    public void autoAssignMenuItemMnemonics()
    {
        AbstractButton[] buttons = new AbstractButton[getItemCount()];
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = getItem(i);
        }
        JZSwingUtilities.setBestMnemonicsFor(buttons);
    }

    /**
     * Retrieves the index of the seperator with the specified seperator index.  That is, the index of the
     * (<code>n+1</code>)th seperator will be returned (0 for 1st, 1 for 2nd, etc).
     * @param seperatorIndex The seperator to find.
     * @return The component index of that seperator, or <code>-1</code> if there aren't that many seperators in the
     * menu.
     */
    public int getIndexOfSeperator(int seperatorIndex)
    {
        seperatorIndex++;
        int index = 0;
        while (seperatorIndex >0)
        {
            if (getMenuComponentCount()<=index)
            {
                return -1;
            }
            if (getMenuComponent(index) instanceof JPopupMenu.Separator)
            {
                seperatorIndex--;
            }
            index++;
        }
        index--;
        return index;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE //