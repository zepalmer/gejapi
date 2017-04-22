package orioni.jz.awt.swing;

import orioni.jz.awt.InformalGridLayout;
import orioni.jz.awt.SpongyLayout;
import orioni.jz.awt.listener.DocumentAnyChangeListener;
import orioni.jz.awt.swing.convenience.ComponentConstructorPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This {@link ComponentConstructorPanel} is designed to permit the user to specify a {@link Font} value.
 *
 * @author Zachary Palmer
 */
public class FontSelectionPanel extends ComponentConstructorPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link JList} from which fonts are selected.
     */
    protected JList fontList;
    /**
     * The {@link JCheckBox} used to determine whether or not the font should be italic.
     */
    protected JCheckBox italicCheckBox;
    /**
     * The {@link JCheckBox} used to determine whether or not the font should be bold.
     */
    protected JCheckBox boldCheckBox;
    /**
     * The {@link JTextField} containing the point size of the font.
     */
    protected JTextField sizeField;
    /**
     * The {@link JLabel} containing the sample text.
     */
    protected JLabel label;

    /**
     * The current {@link Font} value of this panel.
     */
    protected Font font;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the panel initially contains a defualt font.
     */
    public FontSelectionPanel()
    {
        this(null);
    }

    /**
     * General constructor.
     *
     * @param font The initially selected {@link Font}, or <code>null</code> for none.
     */
    public FontSelectionPanel(Font font)
    {
        super();
        fontList = new JList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        italicCheckBox = new JCheckBox("");
        boldCheckBox = new JCheckBox("");
        italicCheckBox.setFont(italicCheckBox.getFont().deriveFont(Font.ITALIC));
        boldCheckBox.setFont(boldCheckBox.getFont().deriveFont(Font.BOLD));
        sizeField = new JTextField("12", 3);
        label = new JLabel("AaBbCcXxYyZz");

        boldCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        italicCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        sizeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        setContents(
                new SpongyLayout(SpongyLayout.Orientation.HORIZONTAL, true, false),
                new JScrollPane(fontList),
                new ComponentConstructorPanel(
                        new SpongyLayout(SpongyLayout.Orientation.VERTICAL, false, false),
                        new ComponentConstructorPanel(
                                new InformalGridLayout(2, 3, 2, 2),
                                new JLabel("Italic"),
                                italicCheckBox,
                                new JLabel("Bold"),
                                boldCheckBox,
                                new JLabel("Size"),
                                sizeField),
                        label));
        setEditingFont(font);

        ActionListener updateActionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateFont();
            }
        };
        italicCheckBox.addActionListener(updateActionListener);
        boldCheckBox.addActionListener(updateActionListener);
        sizeField.getDocument().addDocumentListener(
                new DocumentAnyChangeListener()
                {
                    public void documentChanged(DocumentEvent e)
                    {
                        updateFont();
                    }
                });
        fontList.addListSelectionListener(
                new ListSelectionListener()
                {
                    public void valueChanged(ListSelectionEvent e)
                    {
                        updateFont();
                    }
                });
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Updates the current {@link Font} value using the GUI components.
     */
    protected void updateFont()
    {
        try
        {
            int style;
            if (italicCheckBox.isSelected())
            {
                if (boldCheckBox.isSelected())
                {
                    style = Font.ITALIC + Font.BOLD;
                } else
                {
                    style = Font.ITALIC;
                }
            } else
            {
                if (boldCheckBox.isSelected())
                {
                    style = Font.BOLD;
                } else
                {
                    style = Font.PLAIN;
                }
            }
            int size = Integer.parseInt(sizeField.getText());
            this.font = new Font(fontList.getSelectedValue().toString(), style, size);
            label.setFont(this.font);
        } catch (NumberFormatException e)
        {
            this.font = null;
            label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        }
    }

    /**
     * Updates the state of the GUI components, changing them to that of the provided {@link Font}.
     *
     * @param font The {@link Font} to store in this dialog, or <code>null</code> to use a default value.
     */
    public void setEditingFont(Font font)
    {
        if (font == null) font = new Font("Monospaced", Font.PLAIN, 12);
        int style = font.getStyle();
        italicCheckBox.setSelected((style == Font.ITALIC) || (style == Font.ITALIC + Font.BOLD));
        boldCheckBox.setSelected((style == Font.BOLD) || (style == Font.ITALIC + Font.BOLD));
        sizeField.setText(String.valueOf(font.getSize()));
        fontList.setSelectedValue(font.getFamily(), true);
        this.font = font;
        label.setFont(this.font);
    }

    /**
     * Retrieves the font stored in this panel.
     *
     * @return The {@link Font} stored in this panel.
     */
    public Font getEditingFont()
    {
        updateFont();
        return this.font;
    }

    /**
     * Recursively sets whether or not this component is enabled.  In addition to the default implementation, this
     * method enables or disables the components which accept input for the {@link FontSelectionPanel}.
     *
     * @param enabled <code>true</code> if this component should be enabled; <code>false</code> otherwise
     */
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        italicCheckBox.setEnabled(enabled);
        boldCheckBox.setEnabled(enabled);
        sizeField.setEnabled(enabled);
        fontList.setEnabled(enabled);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE
