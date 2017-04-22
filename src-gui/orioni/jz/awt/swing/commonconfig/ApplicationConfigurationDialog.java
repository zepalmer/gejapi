package orioni.jz.awt.swing.commonconfig;

import orioni.jz.awt.swing.commonconfig.optionframework.ConfigurationOption;

import javax.swing.*;
import java.awt.*;

/**
 * This modal {@link JDialog} extension is designed to allow a user to change the settings for a set of {@link
 * ConfigurationOption}s. If the "OK" button on the dialog is pressed, the options are changed in their respective
 * {@link ApplicationConfiguration}s and those {@link ApplicationConfiguration}s are saved.  If the "Cancel" button is
 * pressed, the dialog is set invisible with no other consequences.  How each option is presented is dependent upon what
 * subinterface of {@link ConfigurationOption} the option itself implements.
 *
 * @author Zachary Palmer
 */
public class ApplicationConfigurationDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     * @param owner The {@link Frame} that owns this dialog.
     * @param title The title to display on the dialog.
     */
    public ApplicationConfigurationDialog(Frame owner, String title) // TODO
    {
        super(owner, title, true);
    }

    /**
     * General constructor.
     * @param owner The {@link Dialog} that owns this dialog.
     * @param title The title to display on the dialog.
     */
    public ApplicationConfigurationDialog(Dialog owner, String title)
    {
        super(owner, title, true);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE