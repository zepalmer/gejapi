package orioni.jz.awt.swing.lnf;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * This theme is an extension of the {@link DefaultMetalTheme} which is designed to change the color scheme for a Swing
 * UI.
 *
 * @author Zachary Palmer
 */
public class MetalAluminumTheme extends DefaultMetalTheme
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    protected static final ColorUIResource COLOR_ACCELERATOR_FOREGROUND = new ColorUIResource(102, 102, 153);
    protected static final ColorUIResource COLOR_ACCELERATOR_SELECTED_FOREGROUND = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_CONTROL = new ColorUIResource(204, 204, 204);
    protected static final ColorUIResource COLOR_CONTROL_DARK_SHADOW = new ColorUIResource(102, 102, 102);
    protected static final ColorUIResource COLOR_CONTROL_DISABLED = new ColorUIResource(153, 153, 153);
    protected static final ColorUIResource COLOR_CONTROL_HIGHLIGHT = new ColorUIResource(255, 255, 255);
    protected static final ColorUIResource COLOR_CONTROL_INFO = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_CONTROL_SHADOW = new ColorUIResource(153, 153, 153);
    protected static final ColorUIResource COLOR_CONTROL_TEXT_COLOR = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_DESKTOP_COLOR = new ColorUIResource(123, 143, 186);
    protected static final ColorUIResource COLOR_FOCUS_COLOR = new ColorUIResource(153, 153, 204);
    protected static final ColorUIResource COLOR_HIGHLIGHTED_TEXT_COLOR = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_INACTIVE_CONTROL_TEXT_COLOR = new ColorUIResource(153, 153, 153);
    protected static final ColorUIResource COLOR_INACTIVE_SYSTEM_TEXT_COLOR = new ColorUIResource(153, 153, 153);
    protected static final ColorUIResource COLOR_MENU_BACKGROUND = new ColorUIResource(204, 204, 204);
    protected static final ColorUIResource COLOR_MENU_DISABLED_FOREGROUND = new ColorUIResource(153, 153, 153);
    protected static final ColorUIResource COLOR_MENU_FOREGROUND = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_MENU_SELECTED_BACKGROUND = new ColorUIResource(147, 168, 213);
    protected static final ColorUIResource COLOR_MENU_SELECTED_FOREGROUND = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_PRIMARY_CONTROL = new ColorUIResource(187, 204, 240);
    protected static final ColorUIResource COLOR_PRIMARY_CONTROL_DARK_SHADOW = new ColorUIResource(67, 89, 136);
    protected static final ColorUIResource COLOR_PRIMARY_CONTROL_HIGHLIGHT = new ColorUIResource(255, 255, 255);
    protected static final ColorUIResource COLOR_PRIMARY_CONTROL_INFO = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_PRIMARY_CONTROL_SHADOW = new ColorUIResource(139, 158, 198);
    protected static final ColorUIResource COLOR_SEPARATOR_BACKGROUND = new ColorUIResource(255, 255, 255);
    protected static final ColorUIResource COLOR_SEPARATOR_FOREGROUND = new ColorUIResource(102, 102, 153);
    protected static final ColorUIResource COLOR_SYSTEM_TEXT_COLOR = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_TEXT_HIGHLIGHT_COLOR = new ColorUIResource(187, 204, 240);
    protected static final ColorUIResource COLOR_USER_TEXT_COLOR = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_WINDOW_BACKGROUND = new ColorUIResource(255, 255, 255);
    protected static final ColorUIResource COLOR_WINDOW_TITLE_BACKGROUND = new ColorUIResource(188, 205, 240);
    protected static final ColorUIResource COLOR_WINDOW_TITLE_FOREGROUND = new ColorUIResource(0, 0, 0);
    protected static final ColorUIResource COLOR_WINDOW_TITLE_INACTIVE_BACKGROUND = new ColorUIResource(204, 204, 204);
    protected static final ColorUIResource COLOR_WINDOW_TITLE_INACTIVE_FOREGROUND = new ColorUIResource(0, 0, 0);

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public MetalAluminumTheme()
    {
        super();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    public ColorUIResource getAcceleratorForeground()
    {
        return COLOR_ACCELERATOR_FOREGROUND;
    }

    public ColorUIResource getAcceleratorSelectedForeground()
    {
        return COLOR_ACCELERATOR_SELECTED_FOREGROUND;
    }

    public ColorUIResource getControl()
    {
        return COLOR_CONTROL;
    }

    public ColorUIResource getControlDarkShadow()
    {
        return COLOR_CONTROL_DARK_SHADOW;
    }

    public ColorUIResource getControlDisabled()
    {
        return COLOR_CONTROL_DISABLED;
    }

    public ColorUIResource getControlHighlight()
    {
        return COLOR_CONTROL_HIGHLIGHT;
    }

    public ColorUIResource getControlInfo()
    {
        return COLOR_CONTROL_INFO;
    }

    public ColorUIResource getControlShadow()
    {
        return COLOR_CONTROL_SHADOW;
    }

    public ColorUIResource getControlTextColor()
    {
        return COLOR_CONTROL_TEXT_COLOR;
    }

    public ColorUIResource getDesktopColor()
    {
        return COLOR_DESKTOP_COLOR;
    }

    public ColorUIResource getFocusColor()
    {
        return COLOR_FOCUS_COLOR;
    }

    public ColorUIResource getHighlightedTextColor()
    {
        return COLOR_HIGHLIGHTED_TEXT_COLOR;
    }

    public ColorUIResource getInactiveControlTextColor()
    {
        return COLOR_INACTIVE_CONTROL_TEXT_COLOR;
    }

    public ColorUIResource getInactiveSystemTextColor()
    {
        return COLOR_INACTIVE_SYSTEM_TEXT_COLOR;
    }

    public ColorUIResource getMenuBackground()
    {
        return COLOR_MENU_BACKGROUND;
    }

    public ColorUIResource getMenuDisabledForeground()
    {
        return COLOR_MENU_DISABLED_FOREGROUND;
    }

    public ColorUIResource getMenuForeground()
    {
        return COLOR_MENU_FOREGROUND;
    }

    public ColorUIResource getMenuSelectedBackground()
    {
        return COLOR_MENU_SELECTED_BACKGROUND;
    }

    public ColorUIResource getMenuSelectedForeground()
    {
        return COLOR_MENU_SELECTED_FOREGROUND;
    }

    public ColorUIResource getPrimaryControl()
    {
        return COLOR_PRIMARY_CONTROL;
    }

    public ColorUIResource getPrimaryControlDarkShadow()
    {
        return COLOR_PRIMARY_CONTROL_DARK_SHADOW;
    }

    public ColorUIResource getPrimaryControlHighlight()
    {
        return COLOR_PRIMARY_CONTROL_HIGHLIGHT;
    }

    public ColorUIResource getPrimaryControlInfo()
    {
        return COLOR_PRIMARY_CONTROL_INFO;
    }

    public ColorUIResource getPrimaryControlShadow()
    {
        return COLOR_PRIMARY_CONTROL_SHADOW;
    }

    public ColorUIResource getSeparatorBackground()
    {
        return COLOR_SEPARATOR_BACKGROUND;
    }

    public ColorUIResource getSeparatorForeground()
    {
        return COLOR_SEPARATOR_FOREGROUND;
    }

    public ColorUIResource getSystemTextColor()
    {
        return COLOR_SYSTEM_TEXT_COLOR;
    }

    public ColorUIResource getTextHighlightColor()
    {
        return COLOR_TEXT_HIGHLIGHT_COLOR;
    }

    public ColorUIResource getUserTextColor()
    {
        return COLOR_USER_TEXT_COLOR;
    }

    public ColorUIResource getWindowBackground()
    {
        return COLOR_WINDOW_BACKGROUND;
    }

    public ColorUIResource getWindowTitleBackground()
    {
        return COLOR_WINDOW_TITLE_BACKGROUND;
    }

    public ColorUIResource getWindowTitleForeground()
    {
        return COLOR_WINDOW_TITLE_FOREGROUND;
    }

    public ColorUIResource getWindowTitleInactiveBackground()
    {
        return COLOR_WINDOW_TITLE_INACTIVE_BACKGROUND;
    }

    public ColorUIResource getWindowTitleInactiveForeground()
    {
        return COLOR_WINDOW_TITLE_INACTIVE_FOREGROUND;
    }
// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE