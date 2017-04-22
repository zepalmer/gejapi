package orioni.jz.awt.swing.convenience;

import javax.swing.*;

/**
 * This {@link JTabbedPane} extension is designed to allow the caller to create a {@link JTabbedPane} entirely through
 * the constructor rather than through calls to {@link JTabbedPane#addTab(String, javax.swing.Icon, java.awt.Component)}
 * or other calls, allowing one to avoid handling the {@link JTabbedPane} in a local variable.  When used
 * properly in some situations, this can make code more understandable.
 *
 * @author Zachary Palmer
 */
public class ContentConstructorTabbedPane extends JTabbedPane
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes default tab placement and layout policy.
     * @param tabs The tabs to add to this {@link JTabbedPane}.  These tabs are in the form of {@link TabData} objects
     * which define the attributes of the tab.
     */
    public ContentConstructorTabbedPane(TabData... tabs)
    {
        super();
        initialize(tabs);
    }

    /**
     * Skeleton constructor.  Assumes default tab layout policy.
     * @param placement The tab placement for this {@link JTabbedPane}.  See {@link JTabbedPane#JTabbedPane(int)}.
     * @param tabs The tabs to add to this {@link JTabbedPane}.  These tabs are in the form of {@link TabData} objects
     * which define the attributes of the tab.
     */
    public ContentConstructorTabbedPane(int placement, TabData... tabs)
    {
        super(placement);
        initialize(tabs);
    }

    /**
     * General constructor.
     * @param placement The tab placement for this {@link JTabbedPane}.  See {@link JTabbedPane#JTabbedPane(int)}.
     * @param layoutPolicy The tab placement for this {@link JTabbedPane}.  See {@link JTabbedPane#JTabbedPane(int)}.
     * @param tabs The tabs to add to this {@link JTabbedPane}.  These tabs are in the form of {@link TabData} objects
     * which define the attributes of the tab.
     */
    public ContentConstructorTabbedPane(int placement, int layoutPolicy, TabData... tabs)
    {
        super(placement, layoutPolicy);
        initialize(tabs);
    }

    /**
     * Initializes this pane.
     * @param tabs The tabs to add to this {@link JTabbedPane}.  These tabs are in the form of {@link TabData} objects
     * which define the attributes of the tab.
     */
    private void initialize(TabData ... tabs)
    {
        for (TabData tab : tabs)
        {
            addTab(tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getTooltip());
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * One instance of this class describes a single tab in a {@link JTabbedPane}.
     * @author Zachary Palmer
     */
    public static class TabData
    {
        /** The name of the tab. */
        protected String title;
        /** The component contained in the tab. */
        protected JComponent component;
        /** The icon for this tab. */
        protected Icon icon;
        /** The tooltip for this tab. */
        protected String tooltip;

        /**
         * Skeleton constructor.  Assumes no icon or tooltip.
         * @param name The name of the tab.
         * @param component The {@link JComponent} to act as the contents of the tab.
         */
        public TabData(String name, JComponent component)
        {
            this(name, component, null, null);
        }

        /**
         * Skeleton constructor.
         * @param name The name of the tab.
         * @param component The {@link JComponent} to act as the contents of the tab.
         * @param icon The {@link Icon} to display for the tab.
         */
        public TabData(String name, JComponent component, Icon icon)
        {
            this(name, component, icon, null);
        }

        /**
         * General constructor.
         * @param name The name of the tab.
         * @param component The {@link JComponent} to act as the contents of the tab.
         * @param icon The {@link Icon} to display for the tab.
         * @param tooltip The tooltip {@link String} for this tab.
         */
        public TabData(String name, JComponent component, Icon icon, String tooltip)
        {
            this.component = component;
            this.icon = icon;
            title = name;
            this.tooltip = tooltip;
        }

        /**
         * Retrieves the {@link JComponent} which is the content of the described tab.
         * @return The content of the described tab.
         */
        public JComponent getComponent()
        {
            return component;
        }

        /**
         * Retrieves the described tab's icon.
         * @return The described tab's {@link Icon}.
         */
        public Icon getIcon()
        {
            return icon;
        }

        /**
         * Retrieves the title of the described tab.
         * @return The title of the described tab.
         */
        public String getTitle()
        {
            return title;
        }

        /**
         * Retrieves the described tab's tooltip.
         * @return The described tab's tooltip.
         */
        public String getTooltip()
        {
            return tooltip;
        }
    }
}

// END OF FILE