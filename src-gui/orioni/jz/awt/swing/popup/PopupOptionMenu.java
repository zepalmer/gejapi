package orioni.jz.awt.swing.popup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This {@link JPopupMenu} accepts a list of {@link PopupOption} objects and presents them.  When one of the options is
 * chosen, the {@link Object} provided to this menu on construction will be passed to its {@link
 * PopupOption#execute(Object)} method.
 *
 * @author Zachary Palmer
 */
public class PopupOptionMenu extends JPopupMenu
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param object  The {@link Object} to provide to {@link PopupOption#execute(Object)} for the selected option.
     * @param options The popup options to use.
     */
    public PopupOptionMenu(Object object, PopupOption[]... options)
    {
        super();
        List<PopupOption[]> list = new ArrayList<PopupOption[]>();
        for (PopupOption[] option : options)
        {
            list.add(option);
        }
        initialize(list, object);
    }

    /**
     * General constructor.
     *
     * @param object  The {@link Object} to provide to {@link PopupOption#execute(Object)} for the selected option.
     * @param options The {@link List} containing the options with which to initialize.
     */
    public PopupOptionMenu(Object object, List<PopupOption[]> options)
    {
        super();
        initialize(options, object);
    }

    /**
     * Initializes this menu.
     *
     * @param options The {@link List} containing the options with which to initialize.
     */
    private void initialize(List<PopupOption[]> options, final Object object)
    {
        for (int i = 0; i < options.size(); i++)
        {
            if (i != 0) addSeparator();
            PopupOption[] optionArray = options.get(i);
            for (final PopupOption option : optionArray)
            {
                this.add(option.getText()).addActionListener(
                        new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                option.execute(object);
                            }
                        });
            }
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE