package orioni.jz.awt.swing;

import orioni.jz.awt.InformalGridLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is designed to manage a radio-button-based menu inside of a {@link JPanel}.  The menu features a
 * control panel containing a series of radio buttons and a select button.  It also contains a descriptive panel
 * in which descriptions of each option are displayed when the corresponding radio button is selected.  The select
 * button allows the selection to perform some action, such as opening another window.  There is also optionally a back
 * button, allowing a menu to return to a previous menu.
 * <P>
 * Selections are provided to this panel in the form of {@link RadioSelection} objects; this class is abstract, so
 * any options must be implemented through the abstract {@link RadioSelection#select()} method.  The
 * {@link RadioSelectionPanel#back()} method on this class is <I>not</I> abstract; this is to prevent needless extension
 * of this class when a back button is not being used.  If, however, the back button is used, the current implementation
 * of {@link RadioSelectionPanel#back()} does nothing and will need to be overridden.
 *
 * @author Zachary Palmer
 */
public class RadioSelectionPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param title The title of the menu, to be displayed above the selection buttons.
	 * @param infoOnLeft <code>true</code> if the information pane is to appear on the left side of the
	 *                     panel; <code>false</code> if it is to appear on the right side.
	 * @param showBackButton <code>true</code> if the back button is displayed and usable; <code>false</code>
	 *                         otherwise.
	 * @param selections A {@link RadioSelectionPanel.RadioSelection}<code>[]</code> containing the possible
	 *                   selections for this panel.
	 */
	public RadioSelectionPanel(String title, boolean infoOnLeft, boolean showBackButton,
	                           final RadioSelection[] selections)
	{
		super();
		JPanel leftRightBreathingRoom = new JPanel();
		leftRightBreathingRoom.setLayout(new FlowLayout());
		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new InformalGridLayout(2, 1, 5, 5));
		leftRightBreathingRoom.add(new SpacingComponent(new Dimension(5, 5)));
		leftRightBreathingRoom.add(mainPanel);
		leftRightBreathingRoom.add(new SpacingComponent(new Dimension(5, 5)));
		this.add(leftRightBreathingRoom);

		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new BorderLayout(5, 5));
		JPanel titlePanel = new JPanel();
		informationPanel.add(titlePanel, BorderLayout.NORTH);

		JLabel titleLabel = new JLabel();
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titlePanel.setLayout(new FlowLayout());
		titlePanel.add(titleLabel);

		JLabel descriptionLabel = new JLabel();
		JPanel descriptionPanel = new JPanel(); // just for border
		descriptionPanel.setLayout(new BorderLayout(0, 0));
		descriptionPanel.setBackground(Color.WHITE);
		descriptionPanel.add(descriptionLabel, BorderLayout.CENTER);
		descriptionPanel.add(new SpacingComponent(new Dimension(5, 5)), BorderLayout.WEST);
		descriptionPanel.add(new SpacingComponent(new Dimension(5, 5)), BorderLayout.EAST);
		descriptionPanel.add(new SpacingComponent(new Dimension(5, 5)), BorderLayout.NORTH);
		descriptionPanel.add(new SpacingComponent(new Dimension(5, 5)), BorderLayout.SOUTH);
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionPanel);
		descriptionScrollPane.setMinimumSize(new Dimension(300, 300));
		descriptionScrollPane.setPreferredSize(new Dimension(300, 300));
		descriptionPanel.setPreferredSize(new Dimension(280, 280));

		informationPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());

		JLabel controlPanelLabel = new JLabel(title);
		controlPanelLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		controlPanelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.add(controlPanelLabel, BorderLayout.NORTH);

		controlPanel.add(new SpacingComponent(new Dimension(10, 10)));

		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		radioButtonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		JScrollPane radioButtonScrollPane = new JScrollPane(radioButtonPanel);
		controlPanel.add(radioButtonScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton back = new JButton("<< Back");
		if (showBackButton)
		{
			back.setAlignmentX(Component.LEFT_ALIGNMENT);
			buttonPanel.add(back);
		}
		JButton select = new JButton("Select");
		select.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonPanel.add(select);
		controlPanel.add(buttonPanel, BorderLayout.SOUTH);

		if (infoOnLeft)
		{
			mainPanel.add(informationPanel);
			mainPanel.add(controlPanel);
		} else
		{
			mainPanel.add(controlPanel);
			mainPanel.add(informationPanel);
		}

		final ButtonGroup group = new ButtonGroup();

		for (int i=0;i<selections.length;i++)
		{
			RadioSelection selection = selections[i];
			SelectorRadioButton button = new SelectorRadioButton(
			        selection.getTitle(), titleLabel, selection.getDescriptionTitle(), descriptionLabel,
			        selection.getDescription());
			selection.setButton(button);
			radioButtonPanel.add(button);
			group.add(button);
			if (i==0)
			{
				group.setSelected(button.getModel(), true);
				button.changeComponents();
			}
		}

		back.addActionListener(
		        new ActionListener()
		        {
			        public void actionPerformed(ActionEvent e)
			        {
				        back();
			        }
		        }
		);
		select.addActionListener(
		        new ActionListener()
		        {
			        public void actionPerformed(ActionEvent e)
			        {
				        ButtonModel selected = group.getSelection();
                        for (final RadioSelection selection : selections)
                        {
                            if (selection.getButton().getModel() == selected)
                            {
                                selection.select();
                                break;
                            }
                        }
			        }
		        }
		);

	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * This method is invoked whenever the back button is pressed.
	 */
	protected void back()
	{
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

	/**
	 * This class represents a single selectable option on a {@link RadioSelectionPanel}.  An array of them is passed
	 * to a {@link RadioSelectionPanel} upon construction.
	 */
	public abstract static class RadioSelection
	{
		/** The title of this selection. */
		protected String title;
		/** The title of this selection's description. */
		protected String descriptionTitle;
		/** The description of this selection. */
		protected String description;
		/** The {@link SelectorRadioButton} being used to select this data. */
		protected SelectorRadioButton button;

		/**
		 * General constructor.
		 * @param description The description of this selection.
		 * @param descriptionTitle The title of this selection's description.
		 * @param title The title of this selection.
		 */
		public RadioSelection(String title, String descriptionTitle, String description)
		{
			this.description = description;
			this.descriptionTitle = descriptionTitle;
			this.title = title;
		}

		/**
		 * Retrieves the description of this selection.
		 * @return The description of this selection.
		 */
		protected String getDescription()
		{
			return description;
		}

		/**
		 * Retrieves the title of this selection's description.
		 * @return The title of this selection's description.
		 */
		protected String getDescriptionTitle()
		{
			return descriptionTitle;
		}

		/**
		 * Retrieves the title of this selection.
		 * @return The title of this selection.
		 */
		protected String getTitle()
		{
			return title;
		}

		/**
		 * This method is executed whenever this selection is active and the "Select" button is pressed.
		 */
		protected abstract void select();

		/**
		 * Sets the {@link SelectorRadioButton} which is being used to select this data.
		 * @param button The {@link SelectorRadioButton} which is being used to select this data.
		 */
		protected void setButton(SelectorRadioButton button)
		{
			this.button = button;
		}

		/**
		 * Retrieves the {@link SelectorRadioButton} which is being used to select this data.
		 * @return The {@link SelectorRadioButton} which is being used to select this data.
		 */
		protected SelectorRadioButton getButton()
		{
			return button;
		}
	}

	/**
	 * This class represents a JRadioButton which changes the values of the provided components.  It is used for the
	 * menu of this program.
	 *
	 * @author Zachary Palmer
	 */
	class SelectorRadioButton extends JRadioButton
	{
		/** The title component which will be set by the selection of this button. */
		protected JLabel titleComponent;
		/** The new text for the title upon the selection of this button. */
		protected String titleText;
		/** The description component which will be set by the selection of this button. */
		protected JLabel descriptionComponent;
		/** The new text for the description upon the selection of this button. */
		protected String descriptionText;

		/**
		 * Creates an unselected radio button with the specified text.  Whenever the radio button is selected, the
		 * specified components are set to the given values.
		 * @param text The string displayed on the radio button
		 * @param titleComponent The title component which will be set by the selection of this button.
		 * @param titleText The new text for the title upon the selection of this button.
		 * @param descriptionComponent The description component which will be set by the selection of this button.
		 * @param descriptionText The new text for the description upon the selection of this button.
		 */
		public SelectorRadioButton(String text, final JLabel titleComponent, final String titleText,
		                           final JLabel descriptionComponent, final String descriptionText)
		{
			super(text);

			this.titleComponent = titleComponent;
			this.titleText = titleText;
			this.descriptionComponent = descriptionComponent;
			this.descriptionText = descriptionText;

			this.addActionListener(
			        new ActionListener()
			        {
				        public void actionPerformed(ActionEvent e)
				        {
					        changeComponents();
				        }
			        }
			);
		}

		/**
		 * Activates the effect of this button as if it had just been selected.
		 */
		public void changeComponents()
		{
			titleComponent.setText(titleText);
			descriptionComponent.setText(descriptionText);
			titleComponent.repaint();
			descriptionComponent.repaint();
		}
	}
}