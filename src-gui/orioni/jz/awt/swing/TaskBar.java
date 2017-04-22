package orioni.jz.awt.swing;

import orioni.jz.awt.swing.dialog.ExceptionDialog;
import orioni.jz.awt.swing.icon.ColoredStringIcon;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Stack;

/**
 * This {@link JPanel} extension is designed to function as a task bar for a {@link JDesktopPane}.  It contains one
 * button for each {@link JInternalFrame} that is registered with it.  Usually, the frames should be registered with the
 * task bar via the {@link TaskBar#addFrame(JInternalFrame)} method immediately after they are added to the desktop.
 * <p/>
 * The buttons for the frames control their focus and visibility.  Only one button may be depressed at any given time.
 * When a button is pressed, it stays pressed (as toggle button) and unpresses any other button on the task bar.  It
 * also brings its associated frame to the front on the desktop and makes it visible.  When a button which is already
 * pressed is pressed again, it makes its associated frame invisible by iconifying it, leaving no button on the taskbar
 * pressed.
 * <p/>
 * Whenever a frame is closed, its associated button is removed, pressed or not.  This has no other effects on the
 * desktop or on the button bar.
 * <p/>
 * {@link JInternalFrame}s may wish to implement the interface {@link TaskBar.ButtonQueriable}, which allows the task
 * bar to ask for specific information to be presented on the task bar (icon, text, etc.).  Without this interface, the
 * task bar simply uses the title of the frame and a colored box as the icon.
 *
 * @author Zachary Palmer
 */
public class TaskBar extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The exclusive button group controlling the task bar's buttons.
     */
    protected ExclusiveButtonGroup buttonGroup;
    /**
     * The mapping between {@link JInternalFrame} objects and their associated {@link JToggleButton}s.
     */
    protected HashMap<JInternalFrame, JToggleButton> buttonMap;
    /**
     * A mapping between {@link ButtonQueriable} objects and the {@link ButtonQueriable.Listener}s attached to them by
     * this task bar.
     */
    protected HashMap<ButtonQueriable, ButtonQueriable.Listener> queriableListenerMap;
    /**
     * The {@link JDesktopPane} that the commanding editor is using.
     */
    protected JDesktopPane desktop;
    /**
     * The stack of {@link JInternalFrame}s for presentation.
     */
    protected Stack<JInternalFrame> frameStack;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param desktop The {@link JDesktopPane} that the commanding editor is using.
     */
    public TaskBar(final JDesktopPane desktop)
    {
        super(new TaskBarLayoutManager());
        buttonGroup = new ExclusiveButtonGroup();
        buttonMap = new HashMap<JInternalFrame, JToggleButton>();
        queriableListenerMap = new HashMap<ButtonQueriable, ButtonQueriable.Listener>();
        frameStack = new Stack<JInternalFrame>();
        this.desktop = desktop;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Presents the provided {@link JInternalFrame}, bringing it to the front and giving it focus.
     *
     * @param frame The frame to present.
     */
    protected void presentFrame(JInternalFrame frame)
    {
        frame.setVisible(true);
        desktop.getDesktopManager().deiconifyFrame(frame);
        frame.toFront();
        frame.requestFocus();
        try
        {
            frame.setSelected(true);
        } catch (PropertyVetoException e)
        {
            // Not much we can do here...
        }
        setActive(frame, true);
    }

    /**
     * Called to add a {@link JInternalFrame} to this taskbar.
     *
     * @param frame The frame to add.
     */
    public void addFrame(final JInternalFrame frame)
    {
        if (buttonMap.get(frame) != null) return;
        final ButtonQueriable queriable;
        if (frame instanceof ButtonQueriable)
        {
            queriable = (ButtonQueriable) frame;
        } else
        {
            queriable = null;
        }

        frameStack.remove(frame);
        frameStack.push(frame);

        final JToggleButton button;
        if (queriable != null)
        {
            button = new JToggleButton(queriable.getTaskButtonText(), queriable.getTaskButtonIcon());
        } else
        {
            button =
            new JToggleButton(
                    frame.getTitle(),
                    new ColoredStringIcon().setText("").setAbsoluteHeight(16).setAbsoluteWidth(16).setForegroundColor(
                            Color.BLUE)
                    .setBackgroundColor(Color.BLUE)
                    .setOutlineColor(Color.WHITE)
                    .setOutlineSize(1));
        }
        frame.addInternalFrameListener(
                new InternalFrameAdapter()
                {
                    public void internalFrameIconified(InternalFrameEvent e)
                    {
                        try
                        {
                            e.getInternalFrame().setIcon(false);
                        } catch (PropertyVetoException e1)
                        {
                            desktop.getDesktopManager().deiconifyFrame(e.getInternalFrame());
                        }
                        e.getInternalFrame().setVisible(false);
                    }

                    public void internalFrameClosed(InternalFrameEvent e)
                    {
                        removeFrame(frame);
                        frameStack.remove(frame);
                        if (frameStack.size() > 0)
                        {
                            presentFrame(frameStack.peek());
                        }
                    }
                });
        if (queriable != null)
        {
            ButtonQueriable.Listener listener = new ButtonQueriable.Listener()
            {
                public void taskButtonIconChanged(ButtonQueriable.Event event)
                {
                    button.setIcon(queriable.getTaskButtonIcon());
                }

                public void taskButtonTextChanged(ButtonQueriable.Event event)
                {
                    button.setText(queriable.getTaskButtonText());
                }
            };
            queriable.addTaskBarListener(listener);
            queriableListenerMap.put(queriable, listener);
        }
        button.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (button.isSelected())
                        {
                            presentFrame(frame);
                        } else
                        {
                            frame.setVisible(false);
                        }
                    }
                });
        frame.addComponentListener(
                new ComponentAdapter()
                {
                    public void componentHidden(ComponentEvent e)
                    {
                        frameStack.remove(frame);
                        if (((desktop.getSelectedFrame() == null) || (!desktop.getSelectedFrame().isVisible())) &&
                            (frameStack.size() > 0))
                        {
                            presentFrame(frameStack.peek());
                        } else
                        {
                            setActive(frame, false);
                        }
                    }

                    public void componentShown(ComponentEvent e)
                    {
                        frameStack.remove(frame);
                        frameStack.push(frame);
                    }
                });
        frame.addFocusListener(
                new FocusAdapter()
                {
                    public void focusGained(FocusEvent e)
                    {
                        frameStack.remove(frame);
                        frameStack.push(frame);
                        setActive(frame, true);
                    }
                });

        button.addMouseListener(
                new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent e)
                    {
                        if ((e.getButton() == MouseEvent.BUTTON2) || (e.getButton() == MouseEvent.BUTTON3))
                        {
                            JPopupMenu popupMenu = new JPopupMenu();
                            JMenuItem item = new JMenuItem("Move Left");
                            popupMenu.add(item).addActionListener(
                                    new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent e)
                                        {
                                            int index = getComponentIndex(button);
                                            remove(index);
                                            add(button, index - 1);
                                            invalidate();
                                            repaint();
                                            validate();
                                        }
                                    });
                            item.setEnabled(getComponentIndex(button) > 0);
                            item = new JMenuItem("Move Right");
                            popupMenu.add(item).addActionListener(
                                    new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent e)
                                        {
                                            int index = getComponentIndex(button);
                                            remove(index);
                                            add(button, index + 1);
                                            invalidate();
                                            repaint();
                                            validate();
                                        }
                                    });
                            item.setEnabled(getComponentIndex(button) < getComponentCount() - 1);
                            popupMenu.addSeparator();
                            item = new JMenuItem("Close");
                            popupMenu.add(item).addActionListener(
                                    new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent e)
                                        {
                                            try
                                            {
                                                frame.setClosed(true);
                                                removeFrame(frame);
                                            } catch (PropertyVetoException e1)
                                            {
                                                // Fine with us.
                                            }
                                        }
                                    });
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                });

        buttonGroup.add(button);
        buttonGroup.setSelected(button.getModel());

        add(button);
        buttonMap.put(frame, button);

        SwingUtilities.getWindowAncestor(this).validate();
        this.repaint();
    }

    /**
     * Called to remove a {@link JInternalFrame} from this taskbar.
     *
     * @param frame The frame to remove.
     */
    public void removeFrame(final JInternalFrame frame)
    {
        if (buttonMap.get(frame) == null) return;
        JToggleButton button = buttonMap.remove(frame);
        buttonGroup.remove(button);
        this.remove(button);

        if (frame instanceof ButtonQueriable)
        {
            ButtonQueriable queriable = (ButtonQueriable) frame;
            queriable.removeTaskBarListener(queriableListenerMap.remove(queriable));
        }

        SwingUtilities.getWindowAncestor(this).validate();
        this.repaint();
    }

    /**
     * Used to programatically control the task buttons.  Changes the active task button.
     *
     * @param frame  The frame whose button should be changed.
     * @param active <code>true</code> if the button should be active; <code>false</code> otherwise.
     */
    public void setActive(JInternalFrame frame, boolean active)
    {
        JToggleButton toggleButton = buttonMap.get(frame);
        if (toggleButton == null)
        {
            if (!active) return;
        }
        // The following line is necessary to prevent NullPointerException, even when closing a window.
        // 's a bit of a mystery.  Might solve later.
        if (toggleButton != null)
        {
            buttonGroup.setSelected(toggleButton.getModel(), active);
        }
        if (active)
        {
            // this frame gets focus
            frame.requestFocus();
            try
            {
                frame.setSelected(true);
            } catch (PropertyVetoException e)
            {
                ExceptionDialog.reportException(SwingUtilities.getWindowAncestor(this), e);
            }
        }
    }

    /**
     * Retrieves the index of a specified component.
     *
     * @param component The component in question.
     * @return The index of the component, or <code>-1</code> if that component is not contained within this {@link
     *         TaskBar}.
     */
    public int getComponentIndex(Component component)
    {
        Component[] components = this.getComponents();
        for (int i = 0; i < components.length; i++)
        {
            Component c = components[i];
            if (c.equals(component)) return i;
        }
        return -1;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * The layout manager for a {@link TaskBar}.  This layout manager class is written to handle the unusual sizing
     * requirements of buttons on a task bar.
     *
     * @author Zachary Palmer
     */
    static class TaskBarLayoutManager implements LayoutManager
    {
        /**
         * A constant representing the width of the borders between buttons.
         */
        public static final int BORDER_WIDTH = 2;
        /**
         * A constant representing the height of the borders between buttons.
         */
        public static final int BORDER_HEIGHT = 2;

        /**
         * Does nothing.
         *
         * @param name Ignored.
         * @param comp Ignored.
         */
        public void addLayoutComponent(String name, Component comp)
        {
        }

        /**
         * Lays out the specified container.
         *
         * @param parent The container to be laid out.
         */
        public void layoutContainer(Container parent)
        {
            Component[] components = parent.getComponents();
            int maxX = 0, maxY = 0;
            for (final Component component : components)
            {
                JToggleButton button = (JToggleButton) component;
                maxX = Math.max(maxX, (int) (button.getPreferredSize().getWidth()));
                maxY = Math.max(maxY, (int) (button.getPreferredSize().getHeight()));
            }
            if ((maxX + BORDER_WIDTH) * components.length + BORDER_WIDTH + parent.getInsets().left +
                parent.getInsets().right > parent.getWidth())
            {
                maxX = (parent.getWidth() - parent.getInsets().right - parent.getInsets().left - BORDER_WIDTH) /
                        components.length - BORDER_WIDTH;
            }
            if (maxY + parent.getInsets().top + parent.getInsets().bottom + 2 * BORDER_HEIGHT > parent.getHeight())
            {
                maxY = parent.getHeight() - parent.getInsets().top - parent.getInsets().bottom - 2 * BORDER_HEIGHT;
            }
            Dimension size = new Dimension(maxX, maxY);

            int offsetX = parent.getInsets().left + BORDER_WIDTH;
            int offsetY = parent.getInsets().top + BORDER_HEIGHT;
            for (Component c : components)
            {
                c.setSize(size);
                c.setLocation(offsetX, offsetY);
                offsetX += c.getWidth() + BORDER_WIDTH;
            }
        }

        /**
         * Calculates the minimum size dimensions for the specified container, given the components it contains.
         *
         * @param parent The container to be laid out.
         * @return The minimum {@link Dimension} for the container.
         */
        public Dimension minimumLayoutSize(Container parent)
        {
            Component[] components = parent.getComponents();
            int minX = 0, minY = 0;
            for (final Component component : components)
            {
                JToggleButton button = (JToggleButton) component;
                minY = Math.max(minY, (int) (button.getPreferredSize().getHeight()));
                minX =
                Math.max(minX, button.getIcon().getIconWidth() + button.getInsets().right + button.getInsets().left);
            }
            return new Dimension((minX + BORDER_WIDTH) * components.length + BORDER_WIDTH, minY + 2 * BORDER_HEIGHT);
        }

        /**
         * Calculates the preferred size dimensions for the specified container, given the components it contains.
         *
         * @param parent The container to be laid out.
         * @return The preferred {@link Dimension} for the container.
         */
        public Dimension preferredLayoutSize(Container parent)
        {
            Component[] components = parent.getComponents();
            int prefX = 0, prefY = 0;
            for (final Component component : components)
            {
                JToggleButton button = (JToggleButton) component;
                prefY = Math.max(prefY, (int) (button.getPreferredSize().getHeight()));
                prefX = Math.max(prefX, (int) (button.getPreferredSize().getWidth()));
            }
            return new Dimension(
                    (prefX + BORDER_WIDTH) * components.length + BORDER_WIDTH, prefY + 2 * BORDER_HEIGHT);
        }

        /**
         * Does nothing.
         *
         * @param comp Ignored.
         */
        public void removeLayoutComponent(Component comp)
        {
        }
    }

    /**
     * This interface is implemented by all {@link JInternalFrame}s which can be queried by a taskbar for information.
     *
     * @author Zachary Palmer
     */
    public static interface ButtonQueriable
    {
        /**
         * Retrieves the icon which will be displayed on the {@link TaskBar} button for this item.
         *
         * @return The {@link Icon} which will be displayed on this {@link ButtonQueriable}'s task button.
         */
        public Icon getTaskButtonIcon();

        /**
         * Retrieves the text which will be displayed on the {@link TaskBar} button for this item.
         *
         * @return The {@link String} which will be displayed on this {@link ButtonQueriable}'s task button.
         */
        public String getTaskButtonText();

        /**
         * Accepts a {@link Listener}.  This listener should be notified whenever the button icon or text changes.
         *
         * @param listener The listener to add.
         */
        public void addTaskBarListener(Listener listener);

        /**
         * Removes a {@link Listener}.  This listener will no longer be notified whenever the button icon or text
         * changes.
         *
         * @param listener The listener to add.
         */
        public void removeTaskBarListener(Listener listener);

        /**
         * This interface is implemented by all classes which wish to be notified about a {@link ButtonQueriable}'s
         * events.
         *
         * @author Zachary Palmer
         */
        public static interface Listener
        {
            /**
             * This method is called when the {@link Event}'s task button icon changes.
             *
             * @param event The {@link Event} representing the event.
             */
            public void taskButtonIconChanged(Event event);

            /**
             * This method is called when the {@link Event}'s task button text changes.
             *
             * @param event The {@link Event} representing the event.
             */
            public void taskButtonTextChanged(Event event);
        }

        /**
         * Instances of this class are created to signify a specific event occurring with regards to a {@link
         * ButtonQueriable}.
         *
         * @author Zachary Palmer
         */
        public static class Event
        {
            /**
             * The {@link ButtonQueriable} on which the event occurred.
             */
            protected ButtonQueriable queriable;

            /**
             * General constructor.
             *
             * @param queriable The {@link ButtonQueriable} on which the event occurred.
             */
            public Event(ButtonQueriable queriable)
            {
                super();
                this.queriable = queriable;
            }

            /**
             * Retrieves the {@link ButtonQueriable} on which the event occurred.
             *
             * @return The {@link ButtonQueriable} on which the event occurred.
             */
            public ButtonQueriable getQueriable()
            {
                return queriable;
            }
        }
    }
}