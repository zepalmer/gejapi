package orioni.jz.awt.swing.commonconfig;

import orioni.jz.awt.swing.commonconfig.optionframework.AbstractDistinctConfigurationOption;
import orioni.jz.awt.swing.commonconfig.optionframework.ConfigurationOption;
import orioni.jz.awt.swing.lnf.MetalAluminumTheme;
import orioni.jz.awt.swing.lnf.MetalCobaltTheme;
import orioni.jz.awt.swing.lnf.MetalDinariiTheme;
import orioni.jz.generics.GenericCopyingHashSet;
import orioni.jz.util.Pair;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * This {@link ConfigurationOption} is intended to represent the look and feel of a Swing application.
 *
 * @author Zachary Palmer
 */
public class LookAndFeelOption extends AbstractDistinctConfigurationOption
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The string describing the setting for the Metal L&F with the Steel theme.
     */
    public static final String VALUE_METAL_STEEL = "metal/steel";
    /**
     * The string describing the setting for the Metal L&F with the Cobalt theme.
     */
    public static final String VALUE_METAL_COBALT = "metal/cobalt";
    /**
     * The string describing the setting for the Metal L&F with the Ocean theme.
     */
    public static final String VALUE_METAL_OCEAN = "metal/ocean";
    /**
     * The string describing the setting for the Metal L&F with the Aluminum theme.
     */
    public static final String VALUE_METAL_ALUMINUM = "metal/aluminum";
    /**
     * The string describing the setting for the Metal L&F with the Dinarii theme.
     */
    public static final String VALUE_METAL_DINARII = "metal/dinarii";
    /**
     * The string describing the setting for the Metal L&F with the Motif theme.
     */
    public static final String VALUE_MOTIF = "motif";
    /**
     * The string describing the setting for the Metal L&F with the Windows theme.
     */
    public static final String VALUE_WINDOWS = "windows";

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public LookAndFeelOption()
    {
        super(
                "appearance.lnf", ApplicationConfiguration.APPEARANCE_GROUP,
                new GenericCopyingHashSet<Pair<String, String>>(
                        Pair.class,
                        new Pair<String, String>(VALUE_METAL_STEEL, "Metal - Steel"),
                        new Pair<String, String>(VALUE_METAL_COBALT, "Metal - Cobalt"),
                        new Pair<String, String>(VALUE_METAL_OCEAN, "Metal - Ocean"),
                        new Pair<String, String>(VALUE_METAL_ALUMINUM, "Metal - Aluminum"),
                        new Pair<String, String>(VALUE_METAL_DINARII, "Metal - Dinarii"),
                        new Pair<String, String>(VALUE_MOTIF, "Motif"),
                        new Pair<String, String>(VALUE_WINDOWS, "Windows")));
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Enforces this {@link ConfigurationOption}.  When this method is called, the option which is represented by this
     * object is effected.  For example, if this option were to represent the background color of a panel, calling this
     * method would actually perform the setting of that panel's background color.
     * <p/>
     * This method will usually be assisted by data provided through other method calls.  In the above example, for
     * instance, the panel whose background is to be set would have been provided to a different method prior to this
     * call.
     *
     * @param configuration The {@link ApplicationConfiguration} on which this option should be enforced.  The {@link
     *                      ApplicationConfiguration} is used to determine what parts of the application are to be
     *                      affected and in what way.
     * @return A string containing error messages separated by newlines, or <code>null</code> if no error occurred.
     */
    public String enforce(ApplicationConfiguration configuration)
    {
        Set<Component> components = configuration.getComponents();
        java.util.List<String> errors = new ArrayList<String>();
        String value = configuration.getProperty(this.getKey());
        boolean error = false;
        String lookAndFeelClassName = null;
        if (value == null)
        {
            lookAndFeelClassName = System.getProperty("swing.defaultlaf");
            if (lookAndFeelClassName == null)
            {
                lookAndFeelClassName = UIManager.getCrossPlatformLookAndFeelClassName();
            }
        } else if (VALUE_METAL_STEEL.equals(value))
        {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            lookAndFeelClassName = MetalLookAndFeel.class.getName();
        } else if (VALUE_METAL_COBALT.equals(value))
        {
            MetalLookAndFeel.setCurrentTheme(new MetalCobaltTheme());
            lookAndFeelClassName = MetalLookAndFeel.class.getName();
        } else if (VALUE_METAL_ALUMINUM.equals(value))
        {
            MetalLookAndFeel.setCurrentTheme(new MetalAluminumTheme());
            lookAndFeelClassName = MetalLookAndFeel.class.getName();
        } else if (VALUE_METAL_DINARII.equals(value))
        {
            MetalLookAndFeel.setCurrentTheme(new MetalDinariiTheme());
            lookAndFeelClassName = MetalLookAndFeel.class.getName();
        } else if (VALUE_METAL_OCEAN.equals(value))
        {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            lookAndFeelClassName = MetalLookAndFeel.class.getName();
        } else if (VALUE_MOTIF.equals(value))
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if (info.getName().toLowerCase().contains("Motif".toLowerCase()))
                {
                    lookAndFeelClassName = info.getClassName();
                    break;
                }
            }
        } else if (VALUE_WINDOWS.equals(value))
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if (info.getName().toLowerCase().contains("Windows".toLowerCase()))
                {
                    lookAndFeelClassName = info.getClassName();
                    break;
                }
            }
        } else
        {
            errors.add("Configuration file corrupt.  " + value + " is not a legal Look and Feel.");
            error = true;
        }
        if (!error)
        {
            try
            {
                UIManager.setLookAndFeel(lookAndFeelClassName);
                for (Component c : components)
                {
                    SwingUtilities.updateComponentTreeUI(c);
                    SwingUtilities.updateComponentTreeUI(c); // needed 'cause some L&Fs (Windows) behave oddly otherwise
                    c.invalidate();
                    c.validate();
                }
            } catch (Exception e)
            {
                errors.add(e.getMessage());
            }
        }

        if (errors.size() > 0)
        {
            StringBuffer sb = new StringBuffer();
            for (String s : errors)
            {
                if (sb.length() > 0) sb.append('\n');
                sb.append(s);
            }
            return sb.toString();
        } else
        {
            return null;
        }
    }

    /**
     * Determines whether or not this {@link ConfigurationOption} is defaultable.  This option is.
     *
     * @return <code>true</code>, always.
     */
    public boolean isDefaultable()
    {
        return true;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE