package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.swing.ApprovalButtonPanel;
import orioni.jz.util.Pair;
import orioni.jz.util.Utilities;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This {@link JDialog} extension displays a dialog with all registered Looks & Feels in a radio button list.  When one
 * of the items in the list is selected and the "Apply" button is pressed, the {@link Component}s which are passed to
 * this dialog's constructor have their component tree UIs updated to reflect the newly selected Look & Feel. <P> The
 * "Cancel" button closes the window without applying the currently selected Look & Feel.  The "OK" button closes the
 * window after applying the currently selected Look & Feel.  This {@link JDialog} is automatically included in the list
 * of {@link Component}s to be updated.
 *
 * @author Zachary Palmer
 */
public class LookAndFeelDialog extends JDialog
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

    /**
     * The default metal theme.
     */
    public static final MetalTheme DEFAULT_METAL_THEME;

// STATIC INITIALIZER ////////////////////////////////////////////////////////////

    static
    {
        DEFAULT_METAL_THEME = MetalLookAndFeel.getCurrentTheme();
    }

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link Set} of {@link Component}s which will be updated.
     */
    protected Set<Component> componentSet;
    /**
     * The class name of the currently selected Look & Feel.
     */
    protected String currentlySelectedClassName;
    /**
     * The class name of the last Look & Feel this dialog applied.
     */
    protected String lastAppliedLnf;
    /**
     * A mapping between Look & Feel classnames and the {@link JRadioButton}s they use.
     */
    protected Map<String, JRadioButton> buttonMap;
    /**
     * Contains the Apply, OK, and Cancel buttons for this dialog.
     */
    protected ApprovalButtonPanel buttonPanel;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     *
     * @param master
     * @param components The {@link Component}s to be updated.
     */
    public LookAndFeelDialog(Frame master, Component... components)
    {
        super(master, "Change Look & Feel", true);

        componentSet = new HashSet<Component>();
        buttonMap = new HashMap<String, JRadioButton>();

        for (final Component component : components)
        {
            componentSet.add(component);
        }
        componentSet.add(this);

        JPanel inputPanel = new JPanel();
        JPanel radioButtons = new JPanel();

        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        ButtonGroup group = new ButtonGroup();
        radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.Y_AXIS));
        String currentLafName = (UIManager.getLookAndFeel() == null ? "" : UIManager.getLookAndFeel().getName());
        boolean selectionMade = false;

        ArrayList<Pair<String, String>> lnfNames = new ArrayList<Pair<String, String>>();
        for (final UIManager.LookAndFeelInfo lnf : info)
        {
            lnfNames.add(new Pair<String, String>(lnf.getClassName(), lnf.getName()));
        }
        for (String s : new String[]{
                orioni.jz.awt.swing.lnf.MetalAluminumTheme.class.getName(),
                orioni.jz.awt.swing.lnf.MetalCobaltTheme.class.getName(),
                orioni.jz.awt.swing.lnf.MetalDinariiTheme.class.getName()})
        {
            lnfNames.add(
                    new Pair<String, String>(
                            "javax.swing.plaf.metal.MetalLookAndFeel&" + s, "Metal/" + s.split("Metal|Theme")[1]));
        }

        final LookAndFeelDialog scopedThis = this;
        buttonPanel = new ApprovalButtonPanel()
        {
            public boolean apply()
            {
                scopedThis.apply();
                return true;
            }

            public void close()
            {
                scopedThis.close();
            }
        };

        for (final Pair<String, String> pss : lnfNames)
        {
            JRadioButton button = new JRadioButton(pss.getSecond());
            group.add(button);
            buttonMap.put(pss.getFirst(), button);
            if (pss.getFirst().equals(currentLafName))
            {
                button.setSelected(true);
                currentlySelectedClassName = pss.getFirst();
                selectionMade = true;
            }
            button.addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            currentlySelectedClassName = pss.getFirst();
                            buttonPanel.getApplyButton().setEnabled(true);
                        }
                    });
            radioButtons.add(button);
        }

        if (System.getProperty("swing.defaultlaf") != null)
        {
            JRadioButton button = new JRadioButton("Default");
            group.add(button);
            if (!selectionMade)
            {
                button.setSelected(true);
                currentlySelectedClassName = null;
            }
            button.addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            currentlySelectedClassName = null;
                            buttonPanel.getApplyButton().setEnabled(true);
                        }
                    });
            radioButtons.add(button);
        }

        this.getContentPane().setLayout(new BorderLayout());
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(new JLabel("Please select a Look & Feel:"));
        inputPanel.add(radioButtons);
        this.getContentPane().add(inputPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Displays this dialog by setting it visible.
     */
    public void execute()
    {
        this.setVisible(true);
    }

    /**
     * Applies the selected Look & Feel.
     */
    public void apply()
    {
        buttonPanel.getApplyButton().setEnabled(false);

        Set<Pair<JFrame, Integer>> extendedStates = new HashSet<Pair<JFrame, Integer>>();
        for (Component c : componentSet)
        {
            if (c instanceof JFrame)
            {
                JFrame f = (JFrame) c;
                extendedStates.add(new Pair<JFrame, Integer>(f, f.getExtendedState()));
            }
        }

        String[] lnfData;
        if (currentlySelectedClassName == null)
        {
            lnfData = new String[]{null, null};
        } else
        {
            lnfData = currentlySelectedClassName.split("&");
        }

        for (int i = 0; i < 2; i++) // gotta do this twice for it to take
        {
            try
            {
                if (lnfData[0] == null)
                {
                    String defaultLaf = System.getProperty("swing.defaultlaf");
                    if (defaultLaf != null)
                    {
                        lastAppliedLnf = defaultLaf;
                        UIManager.setLookAndFeel(defaultLaf);
                    }
                } else
                {
                    UIManager.setLookAndFeel(lnfData[0]);
                    lastAppliedLnf = currentlySelectedClassName;
                    if ("javax.swing.plaf.metal.MetalLookAndFeel".equals(lnfData[0]))
                    {
                        if (lnfData.length > 1)
                        {
                            try
                            {
                                Constructor c = Class.forName(lnfData[1]).getConstructor(Utilities.EMPTY_CLASS_ARRAY);
                                MetalLookAndFeel.setCurrentTheme(
                                        (MetalTheme) (c.newInstance(Utilities.EMPTY_OBJECT_ARRAY)));
                            } catch (NoSuchMethodException e)
                            {
                            } catch (InvocationTargetException e)
                            {
                            } catch (ClassCastException cce)
                            {
                            }
                        } else
                        {
                            MetalLookAndFeel.setCurrentTheme(DEFAULT_METAL_THEME);
                        }
                    }
                }
            } catch (ClassNotFoundException e)
            {
                ExceptionDialog.reportException(this, e);
            } catch (InstantiationException e)
            {
                ExceptionDialog.reportException(this, e);
            } catch (IllegalAccessException e)
            {
                ExceptionDialog.reportException(this, e);
            } catch (UnsupportedLookAndFeelException e)
            {
                ExceptionDialog.reportException(this, e);
            }

            for (Component component : componentSet)
            {
                SwingUtilities.updateComponentTreeUI(component);
                SwingUtilities.updateComponentTreeUI(component);
                if (component instanceof JFrame)
                {
                    ((JFrame) component).pack();
                } else if (component instanceof JDialog)
                {
                    ((JDialog) component).pack();
                }
            }
        }

        for (Pair<JFrame, Integer> p : extendedStates)
        {
            p.getFirst().setExtendedState(p.getSecond());
        }
    }

    /**
     * Closes this dialog by setting it invisible.
     */
    protected void close()
    {
        this.setVisible(false);
    }

    /**
     * Retrieves the selection which was made.
     *
     * @return The name of a {@link LookAndFeel} class, or <code>null</code> if "Default" was selected.
     */
    public String getSelection()
    {
        return currentlySelectedClassName;
    }

    /**
     * Sets the current selection in this dialog.
     *
     * @param selection The name of a {@link LookAndFeel} class, or <code>null</code> if "Default" should be selected.
     */
    public void setSelection(String selection)
    {
        currentlySelectedClassName = selection;
        JRadioButton button = buttonMap.get(selection);
        if (button != null) button.setSelected(true);
    }

    /**
     * Retrieves the class name of the last Look & Feel this class applied.
     */
    public String getLastAppliedLookAndFeel()
    {
        return lastAppliedLnf;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}