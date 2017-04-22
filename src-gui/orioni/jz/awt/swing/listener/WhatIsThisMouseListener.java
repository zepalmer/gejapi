package orioni.jz.awt.swing.listener;

import orioni.jz.awt.listener.GeneralMethodCallMouseListener;
import orioni.jz.awt.swing.dialog.ScrollableTextDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This {@link MouseListener} will listen for popup triggers.  If one is encountered, it will create a popup menu
 * containing one option: "What is this?".  If the user selects that option, a modal dialog will be displayed containing
 * text provided at construction.
 *
 * @author Zachary Palmer
 */
public class WhatIsThisMouseListener extends GeneralMethodCallMouseListener
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The title for the popup dialog.
     */
    protected String title;
    /**
     * The description to provide in the popup dialog.
     */
    protected String description;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param title       The title for the text in the popup dialog, placed above the scroll pane.
     * @param description The description to provide in the popup dialog, within the scroll pane.
     */
    public WhatIsThisMouseListener(String title, String description)
    {
        super();
        this.title = title;
        this.description = description;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * The method which provides the "What is this?" popup mentioned in this class's documentation.
     *
     * @param e The {@link MouseEvent} describing what happened.
     */
    public void checkMouseOperation(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem item = new JMenuItem("What is this?");
            menu.add(item);
            item.addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            Component source = null;
                            if (e.getSource() instanceof Component)
                            {
                                source = (Component) (e.getSource());
                                while (!((source instanceof Frame) || (source == null))) source = source.getParent();
                            }
                            final Component sourceFinal = source;
                            new Thread()
                            {
                                public void run()
                                {
                                    ScrollableTextDialog dialog = new ScrollableTextDialog(
                                            (Frame) (sourceFinal), "What is this?", true, "Ok");
                                    dialog.execute(title, description);
                                    dialog.dispose();
                                }
                            }.start();
                        }
                    });
            if (e.getSource() instanceof Component)
            {
                menu.show((Component) (e.getSource()), e.getX(), e.getY());
            }
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Creates a {@link WhatIsThisMouseListener} with the specified title and description and adds it to the provided
     * list of {@link JComponent}s.  This can be used when a component and associated labels or inidcators need to use
     * the same {@link WhatIsThisMouseListener}; such usage can make code more simplistic and easier to read.
     *
     * @param title       The title to use.
     * @param description The description to use.
     * @param components  The {@link JComponent}s to which the listener will be applied.
     */
    public static void batchAdd(String title, String description, JComponent... components)
    {
        WhatIsThisMouseListener witml = new WhatIsThisMouseListener(title, description);
        for (JComponent c : components) c.addMouseListener(witml);
    }
}

// END OF FILE