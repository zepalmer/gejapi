package orioni.jz.awt;

import orioni.jz.util.HashMultiMap;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@link SpongyLayout} implementation of {@link LayoutManager} is designed to place components in their parent
 * container in an orderly row or column.  Unlike {@link javax.swing.BoxLayout}, however, the components will be resized
 * to fit in the container; {@link javax.swing.BoxLayout}'s behavior when the container has become too large is to
 * simply exclude the trailing components.
 * <p/>
 * When a {@link SpongyLayout} is constructed, a dimension (either horizontal or vertical) must be specified.  Every
 * pixel in the targeted dimension will be used; that is, if the components within a container do not fill that
 * dimension of the container, they will be stretched to fill it.  If the components within a container require more
 * space than is available in the container, they will be compacted to fit in the container.  These resizings are
 * proportional to the original component sizes.  Therefore, if a horizontally-oriented {@link SpongyLayout} contained
 * two components of sizes <code>500</code>x<code>200</code> and <code>300</code>x<code>100</code> with no gap fill, the
 * components together would require 800 horizontal pixels.  If the container in which they were to be placed was 500
 * pixels wide, they would be resized to five eighths their original widths: <code>312</code>x<code>200</code> and
 * <code>187</code>x<code>100</code>.
 * <p/>
 * Note that, in the example above, integer rounding has discarded a pixel.  That pixel will be redistributed to make
 * the components consume the entire 500 pixels of the container.
 * <p/>
 * In the untargeted dimension, components are not resized in this manner.  If, in a horizontally-oriented {@link
 * SpongyLayout} as described above, a component were too tall to fit in the container, it would be resized to fit the
 * height of its container.  In all other cases, the height will be set to the preferred height of the component.
 * <p/>
 * When components are added to a {@link SpongyLayout}, they can be prioritized.  This is accomplished by providing an
 * {@link Integer} constraint object when the component is added to its container.  Components without constraint
 * objects are treated as if they have a priority of <code>0</code>.
 * <p/>
 * When resizing occurs, components with <b>lower</b> priorities will be affected first.  Components with higher
 * priorities will be unaffected until all components with lower priorities have been reduced in the targeted dimension
 * to a size of zero pixels.  Note that this does <i>not</i> affect cases when the components need to be increased in
 * size to fill the container.
 * <p/>
 * For example, consider a horizontally-oriented {@link SpongyLayout} with components of the following sizes contained
 * within: <UL> <code>200</code>x<code>100</code>, Priority 0<br> <code>150</code>x<code>150</code>, Priority 0<br>
 * <code>300</code>x<code>200</code>, Priority 1<br> <code>250</code>x<code>50</code>, Priority 0<br> </UL> The
 * following table shows how the components would be resized to different widths: <table cols="3"> <tr><td><b>Container
 * Width</b></td><td><b>Component Sizes</b></td><td><b>Notes</b></td></tr> <tr><td>900</td><td>
 * <code>200</code>x<code>100</code><br> <code>150</code>x<code>150</code><br> <code>300</code>x<code>200</code><br>
 * <code>250</code>x<code>50</code> </td><td>Exactly the size necessary.</td></tr> <tr><td>840</td><td>
 * <code>180</code>x<code>100</code><br> <code>135</code>x<code>150</code><br> <code>300</code>x<code>200</code><br>
 * <code>225</code>x<code>50</code> </td><td>The container, due to prioritization, is divided into a 300 pixel space
 * (for the Priority 1 component) and a 600 pixel space (for the Priority 0 components).  Note that this space is not
 * necessary contiguous.  The 60 pixels which have been removed from the container are removed from the collective space
 * of the Priority 0 components first, reducing the size of that space by ten percent.  Therefore, all Priority 0
 * components are reduced in width by ten percent to compensate, leaving the Priority 1 component untouched.</td></tr>
 * <tr><td>360</td><td> <code>20</code>x<code>100</code><br> <code>15</code>x<code>150</code><br>
 * <code>300</code>x<code>200</code><br> <code>25</code>x<code>50</code> </td><td>An example of the condition listed
 * above, if more extreme.  90% of the Priority 0 space has been removed; the Priority 1 space is still
 * untouched.</td></tr> <tr><td>280</td><td> <code>0</code>x<code>100</code><br> <code>0</code>x<code>150</code><br>
 * <code>280</code>x<code>200</code><br> <code>0</code>x<code>50</code> </td><td>Now, the Priority 0 space has been
 * exhausted.  The 20 pixels it couldn't provide are removed from the Priority 1 space.</td></tr> <tr><td>1080</td><td>
 * <code>240</code>x<code>100</code><br> <code>180</code>x<code>150</code><br> <code>360</code>x<code>200</code><br>
 * <code>300</code>x<code>50</code> </td><td>The components must be <i>increased</i> in size to fill the container.  In
 * this case, priority is ignored. </td></tr></table>
 * <p/>
 * Upon construction, it is possible to specify that stretching to fill the container does not occur.  If this is done,
 * the components will be set to their preferred sizes in the targeted dimension and centered.
 * <p/>
 * This layout provides a series of constant {@link Integer} objects for convenience in specifying priorities.  It is
 * hoped that, for most cases, the use of these {@link Integer} objects will be sufficient for prioritization of added
 * components.  The priorities are, from the highest to lowest, <ul> {@link SpongyLayout#PRIORITY_TOP} {@link
 * SpongyLayout#PRIORITY_HIGH} {@link SpongyLayout#PRIORITY_PREFERRED} {@link SpongyLayout#PRIORITY_NORMAL} {@link
 * SpongyLayout#PRIORITY_DEFERRED} {@link SpongyLayout#PRIORITY_LOW} {@link SpongyLayout#PRIORITY_BOTTOM}
 *
 * @author Zachary Palmer
 */
public class SpongyLayout implements LayoutManager2
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * An enumeration which indicates the orientation for the spongy layout.  This indicates the direction in which the
     * components will be arranged.
     */
    public enum Orientation
    {
        HORIZONTAL, VERTICAL
    }

    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_TOP = Integer.MAX_VALUE;
    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_HIGH = 65536;
    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_PREFERRED = 256;
    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_NORMAL = 0;
    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_DEFERRED = -PRIORITY_PREFERRED;
    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_LOW = -PRIORITY_HIGH;
    /**
     * An {@link Integer} provided for convenience in specifying priority of added components to this layout.  See this
     * class's documentation.
     */
    public static final Integer PRIORITY_BOTTOM = Integer.MIN_VALUE;

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The horizontal fill for this layout.  This indicates the number of spaces between components, other components,
     * and the edge of their container on the X-axis.
     */
    protected int horizontalFill;
    /**
     * The vertical fill for this layout.  This indicates the number of spaces between components, other components, and
     * the edge of their container on the Y-axis.
     */
    protected int verticalFill;
    /**
     * The orientation for this layout, as one of the orientation constants specified in this class.
     */
    protected Orientation orientation;

    /**
     * Determines whether or not components will be stretched to fill the targeted dimension of the container if they
     * are not large enough to consume it.  If not, they will be centered within the container.
     */
    protected boolean targetedStretch;
    /**
     * Determines whether or not components will be stretched to fill the untargeted dimension of the container if they
     * are not large enough to consume it.  If not, they will be centered within the container.
     */
    protected boolean untargetedStretch;

    /**
     * The {@link HashMap} associating components with their constraint objects.
     */
    protected HashMap<Component, Integer> constraintMap;

    /**
     * The layout alignment for the X-axis of the component being laid out.
     */
    protected float layoutAlignmentX;
    /**
     * The layout alignment for the Y-axis of the component being laid out.
     */
    protected float layoutAlignmentY;

    /**
     * Whether or not high priority objects absorb additional space.  If this value is <code>false</code>, higher
     * priority objects will not be set to a size larger than their preferred sizes if the additional space can be
     * delegated to a lower priority object instead.
     */
    protected boolean highPriorityAbsorbs;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes a two pixel fill in both directions and stretching.
     *
     * @param orientation The orientation for this layout manager, as one of the <code>XXXX</code> constants on this
     *                    class.
     */
    public SpongyLayout(Orientation orientation)
    {
        this(orientation, 2, 2, true, true);
    }

    /**
     * Skeleton constructor.  Assumes a two pixel fill in both directions.  Also assumes untargeted stretching.
     *
     * @param orientation      The orientation for this layout manager, as one of the {@link Orientation} enums on this
     *                         class.
     * @param targetedStretch <code>true</code> if components are to be stretched to fill the container's targeted
     *                         dimension; <code>false</code> otherwise.
     */
    public SpongyLayout(Orientation orientation, boolean targetedStretch)
    {
        this(orientation, 2, 2, targetedStretch, true);
    }

    /**
     * Skeleton constructor.  Assumes a two pixel fill in both directions.  Also assumes untargeted stretching.
     *
     * @param orientation        The orientation for this layout manager, as one of the {@link Orientation} enums on
     *                           this class.
     * @param targetedStretch   <code>true</code> if components are to be stretched to fill the container's targeted
     *                           dimension; <code>false</code> otherwise.
     * @param untargetedStretch <code>true</code> if components are to be stretched to fill the container's untargeted
     *                           dimension; <code>false</code> otherwise.
     */
    public SpongyLayout(Orientation orientation, boolean targetedStretch, boolean untargetedStretch)
    {
        this(orientation, 2, 2, targetedStretch, untargetedStretch);
    }

    /**
     * Skeleton constructor.  Assumes stretch.
     *
     * @param orientation     The orientation for this layout manager, as one of the {@link Orientation} enums on this
     *                        class.
     * @param horizontalFill The horizontal fill for this layout manager.
     * @param verticalFill   The vertical fill for this layout manager.
     */
    public SpongyLayout(Orientation orientation, int horizontalFill, int verticalFill)
    {
        this(orientation, horizontalFill, verticalFill, true, true);
    }

    /**
     * Skeleton constructor.  Assumes untargeted stretching.
     *
     * @param orientation      The orientation for this layout manager, as one of the {@link Orientation} enums on this
     *                         class.
     * @param horizontalFill  The horizontal fill for this layout manager.
     * @param verticalFill    The vertical fill for this layout manager.
     * @param targetedStretch <code>true</code> if components are to be stretched to fill the container's targeted
     *                         dimension; <code>false</code> otherwise.
     */
    public SpongyLayout(Orientation orientation, int horizontalFill, int verticalFill, boolean targetedStretch)
    {
        this(orientation, horizontalFill, verticalFill, targetedStretch, true);
    }

    /**
     * General constructor.
     *
     * @param orientation        The orientation for this layout manager, as one of the {@link Orientation} enums on
     *                           this class.
     * @param horizontalFill    The horizontal fill for this layout manager.
     * @param verticalFill      The vertical fill for this layout manager.
     * @param targetedStretch   <code>true</code> if components are to be stretched to fill the container's targeted
     *                           dimension; <code>false</code> otherwise.
     * @param untargetedStretch <code>true</code> if components are to be stretched to fill the container's untargeted
     *                           dimension; <code>false</code> otherwise.
     */
    public SpongyLayout(Orientation orientation, int horizontalFill, int verticalFill, boolean targetedStretch,
                        boolean untargetedStretch)
    {
        super();
        this.orientation = orientation;
        this.horizontalFill = horizontalFill;
        this.verticalFill = verticalFill;
        constraintMap = new HashMap<Component, Integer>();
        this.targetedStretch = targetedStretch;
        this.untargetedStretch = untargetedStretch;
        layoutAlignmentX = Component.CENTER_ALIGNMENT;
        layoutAlignmentY = Component.CENTER_ALIGNMENT;
        highPriorityAbsorbs = false;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Sets whether or not high priority objects can absorb additional space.
     *
     * @param absorb <code>true</code> if high priority objects can absorb more space than is specified by their
     *               preferred sizes; <code>false</code> if this should be routed to their lower priority compatriots.
     */
    public void setHighPriorityAbsorb(boolean absorb)
    {
        highPriorityAbsorbs = absorb;
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
        Component[] components = parent.getComponents();
        // Build priority mapping
        HashMultiMap<Integer, Component> multiMap = new HashMultiMap<Integer, Component>();
        for (final Component component : components)
        {
            Integer priority = constraintMap.get(component);
            if (priority == null) priority = PRIORITY_NORMAL;
            multiMap.put(priority, component);
        }
        // Sort priority processing set
        Integer[] priorities = multiMap.keySet().toArray(new Integer[0]);
        Arrays.sort(priorities);

        // The following field is used for position components later.  It may be adjusted by the sizing algorithm if
        // centering is to occur.
        int targetedPos = getTargetedFill() + getTargetedFirstInset(parent);

        // The following if-then-else performs the resizing operation
        if ((getTargetedSize(parent.getSize()) > getTargetedSize(preferredLayoutSize(parent))) &&
            (highPriorityAbsorbs))
        {
            // The components aren't even big enough to demand the whole container.
            if (targetedStretch)
            {
                double factor = (getTargetedSize(parent.getSize()) - (getTargetedFill() * (components.length + 1))) /
                                ((double) (getTargetedSize(preferredLayoutSize(parent))) -
                                 (getTargetedFill() * (components.length + 1)));
                int targetedLeft = getTargetedSize(parent) - getTargetedFirstInset(parent) -
                                    getTargetedSecondInset(parent) - getTargetedFill() * (components.length + 1);
                for (int i = 0; i < components.length; i++)
                {
                    Component component = components[i];
                    if (i < components.length - 1)
                    {
                        setTargetedSize(component, (int) (factor * getPreferredTargetedSize(component)));
                        targetedLeft -= getTargetedSize(component);
                    } else
                    {
                        setTargetedSize(component, targetedLeft);
                    }
                }
            } else
            {
                int total = 0;
                for (final Component component : components)
                {
                    setTargetedSize(component, getPreferredTargetedSize(component));
                    total += getTargetedSize(component);
                }
                total += getTargetedFill() * (components.length - 1);
                targetedPos = (int) ((getTargetedSize(parent) - total) * getTargetedLayoutAlignment(parent));
            }
        } else
        {
            // Give targeted space to highest priority components first
            int targetedLeft = getTargetedSize(parent) - getTargetedFirstInset(parent) -
                                getTargetedSecondInset(parent) - getTargetedFill() * (components.length + 1);
            for (int i = priorities.length - 1; i >= 0; i--)
            {
                if (targetedLeft == 0)
                {
                    Set current = multiMap.getAll(priorities[i]);
                    for (Object obj : current)
                    {
                        setTargetedSize((Component) (obj), 0);
                    }
                } else
                {
                    Set<Component> current = new HashSet<Component>(multiMap.getAll(priorities[i]));
                    int request = 0;
                    for (Object aCurrent : current)
                    {
                        Component component = (Component) (aCurrent);
                        request += getPreferredTargetedSize(component);
                    }
                    if ((request > targetedLeft) || ((i == 0) && (targetedStretch))) // Distribute remaining space
                    {
                        // We need to divide what remains among the current set as evenly as possible.
                        // To do this, consider each case individually.
                        // Additionally, the fill space from any undisplayed components must be recovered.
                        for (int j = i - 1; j >= 0; j--)
                        {
                            targetedLeft += getTargetedFill() * multiMap.getAll(priorities[j]).size();
                        }
                        while (current.size() > 1)
                        {
                            Component component = current.iterator().next();
                            current.remove(component);
                            if (request == 0)
                            {
                                setTargetedSize(component, 0);
                            } else
                            {
                                setTargetedSize(
                                        component, getPreferredTargetedSize(component) * targetedLeft / request);
                            }
                            request -= getPreferredTargetedSize(component);
                            targetedLeft -= getTargetedSize(component);
                            if (getTargetedSize(component) == 0) targetedLeft += getTargetedFill();
                        }
                        Component component = current.iterator().next();
                        setTargetedSize(component, targetedLeft);
                        targetedLeft = 0;
                    } else
                    {
                        // We have enough for this level of priority
                        targetedLeft -= request;
                        for (Object obj : current)
                        {
                            Component component = (Component) (obj);
                            setTargetedSize(component, getPreferredTargetedSize(component));
                        }
                    }
                }
            }
            targetedPos += (int) (targetedLeft * getTargetedLayoutAlignment(parent));
        }

        // Enforce limit on untargeted dimension
        for (Component component : components)
        {
            if (untargetedStretch)
            {
                setUntargetedSize(
                        component, getUntargetedSize(parent) - getUntargetedFirstInset(parent) -
                                   getUntargetedSecondInset(parent));
            } else
            {
                setUntargetedSize(
                        component, Math.min(
                        getPreferredUntargetedSize(component),
                        getUntargetedSize(parent) - getUntargetedFirstInset(parent) -
                        getUntargetedSecondInset(parent)));
            }
        }

        // Position components
        for (Component component : components)
        {
            component.setLocation(
                    createPoint(
                            targetedPos,
                            (int) (
                                    ((getUntargetedSize(parent) - getUntargetedFirstInset(parent) -
                                      getUntargetedSecondInset(parent) - getUntargetedSize(component))
                                     * getUntargetedAlignment(component))
                                    + getUntargetedFirstInset(parent))));
            if (getTargetedSize(component) > 0)
            {
                targetedPos += getTargetedSize(component);
                targetedPos += getTargetedFill();
            }
        }
    }

    /**
     * Returns a {@link Dimension} object of no size.  {@link SpongyLayout} can accomodate sizes starting from zero.
     *
     * @param parent The component to be laid out.
     * @return The minimum layout size for the container.
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        return new Dimension(0, 0);
    }

    /**
     * Calculates the preferred size dimensions for the specified container, given the components it contains.
     *
     * @param parent The container to be laid out.
     * @return The preferred layout size for the container.
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        Component[] components = parent.getComponents();
        int targeted = getTargetedFill();
        int untargeted = 0;
        for (final Component component : components)
        {
            targeted += getPreferredTargetedSize(component);
            targeted += getTargetedFill();
            untargeted = Math.max(untargeted, getPreferredUntargetedSize(component));
        }
        return createDimension(targeted, untargeted + getUntargetedFill() * 2);
    }

    /**
     * Removes this component from the constraint map.
     *
     * @param comp Ignored.
     */
    public void removeLayoutComponent(Component comp)
    {
        constraintMap.remove(comp);
    }

    /**
     * Adds the specified component to the layout using the specified constraint object.
     *
     * @param comp        The component to be added
     * @param constraints The {@link Integer} which represents the priority of this component.
     */
    public void addLayoutComponent(Component comp, Integer constraints)
    {
        if (constraints == null) constraints = 0;
        constraintMap.put(comp, constraints);
    }

    /**
     * Adds the specified component to the layout using the specified constraint object.
     *
     * @param comp        The component to be added
     * @param constraints The {@link Integer} which represents the priority of this component.
     * @throws ClassCastException If the <code>constraints</code> parameter is not an instance of {@link Integer}.
     */
    public void addLayoutComponent(Component comp, Object constraints)
    {
        if (constraints == null)
        {
            addLayoutComponent(comp, PRIORITY_NORMAL);
        } else
        {
            if (constraints instanceof Integer)
            {
                addLayoutComponent(comp, (Integer) constraints);
            } else
            {
                throw new ClassCastException("Constraint must be an Integer representing priority.");
            }
        }
    }

    /**
     * Returns the alignment along the x axis.  This specifies how the component would like to be aligned relative to
     * other components.  The value should be a number between 0 and 1 where 0 represents alignment along the origin, 1
     * is aligned the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target The container in question.
     * @return The appropriate layout alignment.
     */
    public float getLayoutAlignmentX(Container target)
    {
        return layoutAlignmentX;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how the component would like to be aligned relative to
     * other components.  The value should be a number between 0 and 1 where 0 represents alignment along the origin, 1
     * is aligned the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target The container in question.
     * @return The appropriate layout alignment.
     */
    public float getLayoutAlignmentY(Container target)
    {
        return layoutAlignmentY;
    }

    /**
     * Retrieves the layout alignment along the targeted axis.  This specifies how the component would like to be
     * aligned relative to other components.  The value should be a number between 0 and 1 where 0 represents alignment
     * along the origin, 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
     */
    public float getTargetedLayoutAlignment(Container target)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return getLayoutAlignmentX(target);
            case VERTICAL:
                return getLayoutAlignmentY(target);
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Sets the layout alignment for the X-axis of the laid-out component.  This applies to all containers which are
     * laid out by this component.
     *
     * @param alignment The new alignment for the X-axis.
     * @return This object.
     */
    public SpongyLayout setLayoutAlignmentX(float alignment)
    {
        layoutAlignmentX = alignment;
        return this;
    }

    /**
     * Sets the layout alignment for the Y-axis of the laid-out component.  This applies to all containers which are
     * laid out by this component.
     *
     * @param alignment The new alignment for the Y-axis.
     * @return This object.
     */
    public SpongyLayout setLayoutAlignmentY(float alignment)
    {
        layoutAlignmentY = alignment;
        return this;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager has cached information it should be discarded.
     *
     * @param target Ignored.
     */
    public void invalidateLayout(Container target)
    {
    }

    /**
     * Calculates the maximum size dimensions for the specified container, given the components it contains.
     *
     * @param target The container for which a maximum size is requested.
     * @return The maximum size for that container.
     */
    public Dimension maximumLayoutSize(Container target)
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Retrieves the size of the first inset in the targeted direction.
     *
     * @param container The component in question.
     * @return The size of the first insert in the targeted direction.
     */
    protected int getTargetedFirstInset(Container container)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return container.getInsets().left;
            case VERTICAL:
                return container.getInsets().top;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the size of the second inset in the targeted direction.
     *
     * @param container The component in question.
     * @return The size of the second insert in the targeted direction.
     */
    protected int getTargetedSecondInset(Container container)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return container.getInsets().right;
            case VERTICAL:
                return container.getInsets().bottom;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the size of the first inset in the untargeted direction.
     *
     * @param container The component in question.
     * @return The size of the first insert in the untargeted direction.
     */
    protected int getUntargetedFirstInset(Container container)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return container.getInsets().top;
            case VERTICAL:
                return container.getInsets().left;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the size of the second inset in the untargeted direction.
     *
     * @param container The component in question.
     * @return The size of the second insert in the untargeted direction.
     */
    protected int getUntargetedSecondInset(Container container)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return container.getInsets().bottom;
            case VERTICAL:
                return container.getInsets().right;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the preferred size of the specified component in the targeted dimension.
     *
     * @param component The component in question.
     * @return The preferred size of the component in the targeted dimension.
     */
    protected int getPreferredTargetedSize(Component component)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return (int) Math.max(component.getMinimumSize().getWidth(), component.getPreferredSize().getWidth());
            case VERTICAL:
                return (int) Math.max(component.getMinimumSize().getHeight(), component.getPreferredSize().getHeight());
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the preferred size of the specified component in the untargeted dimension.
     *
     * @param component The component in question.
     * @return The preferred size of the component in the untargeted dimension.
     */
    protected int getPreferredUntargetedSize(Component component)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return (int) Math.max(component.getMinimumSize().getHeight(), component.getPreferredSize().getHeight());
            case VERTICAL:
                return (int) Math.max(component.getMinimumSize().getWidth(), component.getPreferredSize().getWidth());
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the size of the specified component in the targeted dimension.
     *
     * @param component The component in question.
     * @return The size of the component in the targeted dimension.
     */
    protected int getTargetedSize(Component component)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return (int) (component.getSize().getWidth());
            case VERTICAL:
                return (int) (component.getSize().getHeight());
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the size of the specified component in the untargeted dimension.
     *
     * @param component The component in question.
     * @return The size of the component in the untargeted dimension.
     */
    protected int getUntargetedSize(Component component)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return (int) (component.getSize().getHeight());
            case VERTICAL:
                return (int) (component.getSize().getWidth());
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Sets the size of the specified component in the targeted dimension.
     *
     * @param component The component in question.
     * @param size      The new size for the component in the targeted dimension.
     */
    protected void setTargetedSize(Component component, int size)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                component.setSize(size, component.getHeight());
                break;
            case VERTICAL:
                component.setSize(component.getWidth(), size);
                break;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Sets the size of the specified component in the untargeted dimension.
     *
     * @param component The component in question.
     * @param size      The new size of the component in the untargeted dimension.
     */
    protected void setUntargetedSize(Component component, int size)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                component.setSize(component.getWidth(), size);
                break;
            case VERTICAL:
                component.setSize(size, component.getHeight());
                break;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the fill value for the targeted dimension.
     *
     * @return The fill value for the targeted dimension.
     */
    protected int getTargetedFill()
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return horizontalFill;
            case VERTICAL:
                return verticalFill;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the fill value for the untargeted dimension.
     *
     * @return The fill value for the untargeted dimension.
     */
    protected int getUntargetedFill()
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return verticalFill;
            case VERTICAL:
                return horizontalFill;
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Creates a {@link Dimension} object based upon the orientation of this layout manager.
     *
     * @param targeted   The size of this manager's targeted dimension.
     * @param untargeted The size of this manager's untargeted dimension.
     * @return The created object.
     */
    protected Dimension createDimension(int targeted, int untargeted)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return new Dimension(targeted, untargeted);
            case VERTICAL:
                return new Dimension(untargeted, targeted);
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Creates a {@link Point} object based upon the orientation of this layout manager.
     *
     * @param targeted   The position in this manager's targeted dimension.
     * @param untargeted The position in this manager's untargeted dimension.
     * @return The created object.
     */
    protected Point createPoint(int targeted, int untargeted)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return new Point(targeted, untargeted);
            case VERTICAL:
                return new Point(untargeted, targeted);
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the targeted size from the specified {@link Dimension} object.
     *
     * @param dim The {@link Dimension} object in question.
     * @return The targeted size of that object.
     */
    protected int getTargetedSize(Dimension dim)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return (int) (dim.getWidth());
            case VERTICAL:
                return (int) (dim.getHeight());
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the untargeted size from the specified {@link Dimension} object.
     *
     * @param dim The {@link Dimension} object in question.
     * @return The untargeted size of that object.
     */
    protected int getUntargetedSize(Dimension dim)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return (int) (dim.getHeight());
            case VERTICAL:
                return (int) (dim.getWidth());
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the alignment of the specified component in the targeted dimension.
     *
     * @param component The component in question.
     * @return The alignment of the component in the targeted dimension.
     */
    protected double getTargetedAlignment(Component component)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return component.getAlignmentX();
            case VERTICAL:
                return component.getAlignmentY();
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

    /**
     * Retrieves the alignment of the specified component in the untargeted dimension.
     *
     * @param component The component in question.
     * @return The alignment of the component in the untargeted dimension.
     */
    protected double getUntargetedAlignment(Component component)
    {
        switch (orientation)
        {
            case HORIZONTAL:
                return component.getAlignmentY();
            case VERTICAL:
                return component.getAlignmentX();
            default:
                throw new IllegalStateException("Invalid orientation: " + orientation);
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}