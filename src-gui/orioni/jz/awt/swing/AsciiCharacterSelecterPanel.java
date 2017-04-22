package orioni.jz.awt.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the dialog box which will be used to edit terrain values.
 *
 * @author Zachary Palmer
 */
public class AsciiCharacterSelecterPanel extends JPanel
{
    /**
     * The {@link javax.swing.JLabel} displaying the current tile.
     */
    protected JLabel label;
    /**
     * The dropdown box for the color of the foreground.
     */
    protected JComboBox foregroundColorBox;
    /**
     * The dropdown box for the color of the background.
     */
    protected JComboBox backgroundColorBox;

    /**
     * The {@link JPanel} that is handling character selection for this dialog.
     */
    protected JPanel characterSelectionPanel;
    /**
     * The variable in which the presently-selected character is stored.
     */
    protected char character;

    /**
     * The tile ID of the appearance component before this dialog was executed.
     */
    protected int oldTileId;
    /**
     * The result of this dialog's last execution.
     */
    protected int result;

    /**
     * The {@link Set} containing the {@link ActionListener}s currently listening to this component.
     */
    protected Set<ActionListener> actionListenerSet;

    /**
     * The {@link TextConsoleDisplayComponent} from which this panel will retrieve its character rendering information.
     */
    protected TextConsoleDisplayComponent textConsole;

    /**
     * General constructor.
     *
     * @param label        The label containing the image describing the current tile.  This tile will be updated as
     *                     this panel's controls are used.
     * @param textConsole The text console from which this panel will retrieve its character information.
     */
    public AsciiCharacterSelecterPanel(JLabel label, TextConsoleDisplayComponent textConsole)
    {
        super();

        // Initialize and construct components

        this.label = label;
        this.textConsole = textConsole;

        oldTileId = 0;
        result = 0;

        actionListenerSet = new HashSet<ActionListener>();

        Object[] data = new Object[16];
        for (int i = 0; i < 16; i++) data[i] = new ColorOption(i);
        foregroundColorBox = new JComboBox(data);
        backgroundColorBox = new JComboBox(data);

        // Add components

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(this.label);

        mainPanel.add(new SpacingComponent(new Dimension(5, 5)));

        mainPanel.add(foregroundColorBox);

        mainPanel.add(backgroundColorBox);

        characterSelectionPanel = new JPanel();

        characterSelectionPanel.setLayout(new GridLayout(6, 16));
        for (int i = 0; i < 96; i++)
        {
            characterSelectionPanel.add(new CharacterSelectionComponent(this, i));
        }

        this.setLayout(new FlowLayout());
        this.add(mainPanel);
        this.add(characterSelectionPanel);

        // Add listeners

        foregroundColorBox.addActionListener(new ItemUpdateListener(this));
        backgroundColorBox.addActionListener(new ItemUpdateListener(this));
    }

    /**
     * Sets the tile ID for this panel.
     *
     * @param tileId The current tile ID to display.
     */
    public void setTileId(int tileId)
    {
        oldTileId = tileId;
        setSelectedTileId(oldTileId);
    }

    /**
     * Retrieves the results of this dialog's last execution.
     *
     * @return The new tile ID to display.
     */
    public int getResult()
    {
        return result;
    }

    /**
     * Accepts the current state of this dialog.
     */
    public void commit()
    {
        result = getSelectedTileId();
        updateCurrentlyDisplayedTile();
    }

    /**
     * Cancels this dialog.
     */
    public void revert()
    {
        result = oldTileId;
        backgroundColorBox.setSelectedIndex((result >>> 12) & 0x0F);
        foregroundColorBox.setSelectedIndex((result >>> 8) & 0x0F);
        character = (char) (result & 0xFF);
        updateCurrentlyDisplayedTile();
    }

    /**
     * Determines the currently-selected tile ID from the various components of this dialog.
     *
     * @return The new tile ID.
     */
    public int getSelectedTileId()
    {
        return (backgroundColorBox.getSelectedIndex() << 12) | (foregroundColorBox.getSelectedIndex() << 8) |
               (character);
    }

    /**
     * Sets the currently-selected tile ID by adjusting the various components of this dialog.
     *
     * @param tileId The new tile ID to make selected.
     */
    public void setSelectedTileId(int tileId)
    {
        foregroundColorBox.setSelectedIndex((tileId & 0xF00) >>> 8);
        backgroundColorBox.setSelectedIndex((tileId & 0xF000) >>> 12);
        setCharacter((char) (tileId & 0xFF));
    }

    /**
     * Sets the displayed character for this dialog.
     *
     * @param ch The new character for this dialog.
     */
    public void setCharacter(char ch)
    {
        character = ch;
        updateCurrentlyDisplayedTile();
    }

    /**
     * Updates the tile ID which is currently selected with the rest of the system.
     */
    public synchronized void updateCurrentlyDisplayedTile()
    {
        for (Object aMActionListenerSet : actionListenerSet)
        {
            ActionListener listener = (ActionListener) (aMActionListenerSet);
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        }
        label.repaint();
    }

    /**
     * Retrieves the index of the foreground color that is currently selected.
     *
     * @return Retrieves the index of the foreground color that is currently selected.
     */
    public int getForegroundIndex()
    {
        return foregroundColorBox.getSelectedIndex();
    }

    /**
     * Retrieves the index of the background color that is currently selected.
     *
     * @return Retrieves the index of the background color that is currently selected.
     */
    public int getBackgroundIndex()
    {
        return backgroundColorBox.getSelectedIndex();
    }

    /**
     * Update the character selection panel.  This should be called after color changes.
     */
    public void updateCharacterSelectionPanel()
    {
        characterSelectionPanel.repaint();
    }

    /**
     * Adds an action listener to this component which will be called when the character selection is changed.
     *
     * @param listener The action listener to add.
     */
    public void addActionListener(ActionListener listener)
    {
        actionListenerSet.add(listener);
    }

    /**
     * Adds an action listener to this component which will be called when the character selection is changed.
     *
     * @param listener The action listener to add.
     * @return <code>true</code> if the listener was removed; <code>false</code> if it was not attached.
     */
    public boolean removeActionListener(ActionListener listener)
    {
        return actionListenerSet.remove(listener);
    }

    ///////// CONTAINED CLASSES

    /**
     * This internal class is designed to handle the panel on which the dialog is to be drawn.
     */
    class CharacterSelectionComponent extends JLabel
    {
        /**
         * Size of this panel.
         */
        protected final Dimension size;
        /**
         * Controller of this panel.
         */
        protected final AsciiCharacterSelecterPanel master;
        /**
         * The index of this letter component.
         */
        protected final int index;
        /**
         * Whether or not this component is highlighted.
         */
        protected boolean highlight;

        /**
         * General constructor.
         *
         * @param master The {@link AsciiCharacterSelecterPanel} which controls this {@link
         *               CharacterSelectionComponent}.
         * @param index  The index of the character which this component will display.
         */
        public CharacterSelectionComponent(final AsciiCharacterSelecterPanel master, int index)
        {
            final CharacterSelectionComponent scopedThis = this;
            this.master = master;
            this.index = index;
            this.addMouseListener(
                    new MouseAdapter()
                    {
                        /**
                         * Handles selection of characters by double-clicking.
                         */
                        public void mouseClicked(MouseEvent e)
                        {
                            scopedThis.master.setCharacter((char) (scopedThis.index + 32));
                        }

                        /**
                         * Handles highlighting.
                         */
                        public void mouseEntered(MouseEvent e)
                        {
                            highlight = true;
                            repaint();
                        }

                        /**
                         * Handles highlighting.
                         */
                        public void mouseExited(MouseEvent e)
                        {
                            highlight = false;
                            repaint();
                        }
                    });
            size = new Dimension(
                    textConsole.getCellWidth() + 2,
                    textConsole.getCellHeight() + 2);
        }

        /**
         * Returns the absolute size of this panel.
         *
         * @return The absolute size of this panel.
         */
        public Dimension getMinimumSize()
        {
            return size;
        }

        /**
         * Returns the absolute size of this panel.
         *
         * @return The absolute size of this panel.
         */
        public Dimension getPreferredSize()
        {
            return size;
        }

        /**
         * Returns the absolute size of this panel.
         *
         * @return The absolute size of this panel.
         */
        public Dimension getMaximumSize()
        {
            return size;
        }

        /**
         * Draws this dialog on the provided {@link java.awt.Graphics} object.
         *
         * @param g The {@link java.awt.Graphics} object onto which to draw this dialog.
         */
        public void paint(Graphics g)
        {
            g.drawImage(
                    textConsole.getCharacterImage(
                            index + 32, (byte) (master.getForegroundIndex()),
                            (byte) (master.getBackgroundIndex())), 1, 1, null);
            int backgroundIndex = master.getBackgroundIndex();
            Color highlightColor;
            if ((backgroundIndex > 8) || (backgroundIndex == 7))
            {
                g.setColor(Color.WHITE);
                highlightColor = Color.BLACK;
            } else
            {
                g.setColor(Color.GRAY);
                highlightColor = Color.YELLOW;
            }
            if (this.highlight) g.setColor(highlightColor);
            g.drawLine(
                    0, 0,
                    0, textConsole.getCellHeight() + 1);
            g.drawLine(
                    0, 0,
                    textConsole.getCellWidth() + 1, 0);
            g.drawLine(
                    0, textConsole.getCellHeight() + 1,
                    textConsole.getCellWidth() + 1, textConsole.getCellHeight() + 1);
            g.drawLine(
                    textConsole.getCellWidth() + 1, 0,
                    textConsole.getCellWidth() + 1, textConsole.getCellHeight() + 1);
        }
    }

    /**
     * This internal class is designed to contain representations of colors as well as their text-based indices.
     *
     * @author Zachary Palmer
     */
    static class ColorOption
    {
        /**
         * The array naming the various color indices.
         */
        public static final String[] colorIndexNames = new String[]
        {
            "0: Black", "1: Blue", "2: Green", "3: Cyan", "4: Red", "5: Purple", "6: Brown", "7: Gray",
            "8: Dark Gray", "9: Light Blue", "A: Light Green", "B: Light Cyan", "C: Light Red",
            "D: Light Purple", "E: Yellow", "F: White"
        };

        /**
         * The color index of this color option.
         */
        protected int optionIndex;

        /**
         * General constructor.
         *
         * @param index The index for this color option.
         */
        public ColorOption(int index)
        {
            this.optionIndex = index;
        }

        /**
         * Retrieves the descriptive string for this color option.
         *
         * @return The descriptive string.
         */
        public String toString()
        {
            return colorIndexNames[optionIndex];
        }
    }

    /**
     * This listener updates the tile ID of this appearance component whenever a new selection is made.
     */
    class ItemUpdateListener implements ItemListener, ActionListener
    {
        /**
         * The {@link orioni.jz.awt.swing.AsciiCharacterSelecterPanel} controlling this listener.
         */
        protected AsciiCharacterSelecterPanel master;

        /**
         * General constructor.
         *
         * @param master The {@link orioni.jz.awt.swing.AsciiCharacterSelecterPanel} controlling this listener.
         */
        public ItemUpdateListener(AsciiCharacterSelecterPanel master)
        {
            this.master = master;
        }

        /**
         * Updates the tile ID of this appearance component.
         * @param e Ignored.
         */
        public void itemStateChanged(ItemEvent e)
        {
            master.updateCurrentlyDisplayedTile();
            master.updateCharacterSelectionPanel();
        }

        /**
         * Updates the tile ID of this appearance component.
         * @param e Ignored.
         */
        public void actionPerformed(ActionEvent e)
        {
            master.updateCurrentlyDisplayedTile();
            master.updateCharacterSelectionPanel();
        }
    }
}