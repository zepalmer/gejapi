//package orioni.jz.awt.swing;
//
//import orioni.jz.util.Pair;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Map;
//import java.util.HashMap;
//
//// TODO: this class doesn't seem to work?
//
///**
// * This {@link JPanel} extension is designed to provide an easier-to-use interface than that provided by {@link
// * CardLayout}.  The variable type is to allow specification of the key used to display components within the {@link
// * CardLayout}.
// *
// * @author Zachary Palmer
// */
//public class CardPanel<T> extends JPanel
//{
//// STATIC FIELDS /////////////////////////////////////////////////////////////////
//
//// CONSTANTS /////////////////////////////////////////////////////////////////////
//
//// NON-STATIC FIELDS /////////////////////////////////////////////////////////////
//
//    /**
//     * The layout being used for this {@link CardPanel}.
//     */
//    protected CardLayout layout;
//    /**
//     * A mapping between the type of identifier being used by {@link CardPanel} and the string identifier being used
//     * by {@link CardLayout}.
//     */
//    protected Map<T, String> identifierMapping;
//    /**
//     * The last index used to generate a {@link CardLayout} identifier.
//     */
//    protected int cardLayoutIndex;
//
//// CONSTRUCTORS //////////////////////////////////////////////////////////////////
//
//    /**
//     * General constructor.
//     *
//     * @param pairings Pairings between the keys and the {@link Component}s to be displayed.
//     */
//    public CardPanel(Pair<T, Component>... pairings)
//    {
//        super();
//        identifierMapping = new HashMap<T, String>();
//        cardLayoutIndex = 0;
//
//        layout = new CardLayout();
//        this.setLayout(layout);
//
//        for (Pair<T, Component> pair : pairings) add(pair);
//    }
//
//// NON-STATIC METHODS ////////////////////////////////////////////////////////////
//
//    /**
//     * Adds a pairing to this {@link CardPanel}.
//     *
//     * @param key       The key for the component.
//     * @param component The{@link Component} to be displayed with that key.
//     */
//    public void add(T key, Component component)
//    {
//        String cardLayoutString = String.valueOf(cardLayoutIndex++);
//        identifierMapping.put(key, cardLayoutString);
//        this.add(component, cardLayoutString);
//        layout.addLayoutComponent(component, cardLayoutString);
//    }
//
//    /**
//     * Adds a pairing to this {@link CardPanel}.
//     *
//     * @param pairing A pairing between the keys and the {@link Component}s to be displayed.
//     */
//    public void add(Pair<T, Component> pairing)
//    {
//        add(pairing.getFirst(), pairing.getSecond());
//    }
//
//    /**
//     * Shows the component associated with the specified key.
//     *
//     * @param key The key whose {@link Component} should be displayed.
//     */
//    public void show(T key)
//    {
//        layout.show(this, identifierMapping.get(key));
//        this.repaint();
//    }
//
//// STATIC METHODS ////////////////////////////////////////////////////////////////
//
//}
//
//// END OF FILE
package orioni.jz.awt.swing;

import orioni.jz.awt.AWTUtilities;
import orioni.jz.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This {@link JPanel} extension is designed to provide an easier-to-use interface than that provided by {@link
 * CardLayout}.  The variable type is to allow specification of the key used to display components within the {@link
 * CardLayout}.
 *
 * @author Zachary Palmer
 */
public class CardPanel<T> extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A mapping between the keys and the components to be displayed in the layout.
     */
    protected Map<T, Component> mapping;
    /**
     * The active component.
     */
    protected Component activeComponent;
    /**
     * The preferred and minimum dimension of this container.
     */
    protected Dimension dimension;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param pairings Pairings between the keys and the {@link Component}s to be displayed.
     */
    public CardPanel(Pair<T, Component>... pairings)
    {
        super();
        setLayout(new CardPanelLayout());
        activeComponent = null;
        mapping = new HashMap<T, Component>();
        dimension = new Dimension(0, 0);
        for (Pair<T, Component> pair : pairings)
        {
            add(pair);
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Adds a pairing to this {@link CardPanel}.
     *
     * @param key       The key for the component.
     * @param component The{@link Component} to be displayed with that key.
     */
    public void add(T key, Component component)
    {
        component.setVisible(false);
        if (component instanceof Container)
        {
            Container container = (Container) component;
            container.doLayout();
        }
        mapping.put(key, component);

        java.util.List<Dimension> dimensions = new ArrayList<Dimension>();
        for (Component c : getComponents()) dimensions.add(c.getPreferredSize());
        dimension = AWTUtilities.dimensionMax(dimensions.toArray(new Dimension[0]));

        this.add(component);
    }

    /**
     * Adds a pairing to this {@link CardPanel}.
     *
     * @param pairing A pairing between the keys and the {@link Component}s to be displayed.
     */
    public void add(Pair<T, Component> pairing)
    {
        add(pairing.getFirst(), pairing.getSecond());
    }

    /**
     * Shows the component associated with the specified key.
     *
     * @param key The key whose {@link Component} should be displayed.  If <code>null</code>, no component is shown.
     */
    public void show(T key)
    {
        for (Component c : getComponents()) c.setVisible(false);
        if (key != null)
        {
            activeComponent = mapping.get(key);
            if (activeComponent != null)
            {
                activeComponent.setVisible(true);
                activeComponent.setSize(this.getSize());
                activeComponent.setLocation(0, 0);
            }
        } else
        {
            activeComponent = null;
        }
        this.repaint();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * The {@link LayoutManager} used with the {@link CardPanel}.
     *
     * @author Zachary Palmer
     */
    class CardPanelLayout implements LayoutManager
    {
        /**
         * General constructor.
         */
        public CardPanelLayout()
        {
        }

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
         * @param parent the container to be laid out
         */
        public void layoutContainer(Container parent)
        {
            parent.setSize(dimension);
            if (activeComponent != null)
            {
                activeComponent.setSize(parent.getSize());
                activeComponent.setLocation(0, 0);
            }
        }

        /**
         * Calculates the minimum size dimensions for the specified container, given the components it contains.
         *
         * @param parent the component to be laid out
         * @see #preferredLayoutSize
         */
        public Dimension minimumLayoutSize(Container parent)
        {
            return dimension;
        }

        /**
         * Calculates the preferred size dimensions for the specified container, given the components it contains.
         *
         * @param parent the container to be laid out
         * @see #minimumLayoutSize
         */
        public Dimension preferredLayoutSize(Container parent)
        {
            return dimension;
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
}

// END OF FILE