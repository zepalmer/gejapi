package orioni.jz.awt.swing.dialog;

import orioni.jz.awt.InformalGridLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;

/**
 * This panel is intended to be a general-purpose input panel.  Upon construction, a list of {@link InputField}s is
 * provided.  Each {@link InputField} is meant to represent a different value.  The contents of the fields are
 * accessible through {@link InputPanel#getResult(Object)}.
 *
 * @author Zachary Palmer
 */
public class InputPanel extends JPanel
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * A {@link HashMap} which is used to key the {@link InputField}s by their key values.
     */
    protected HashMap<Object, InputField> fieldMap;
    /**
     * <code>true</code> if the dialog completed its last execution; <code>false</code> if it was cancelled or if the
     * dialog has not yet been executed.
     */
    protected boolean completed;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the fields will start from the top of this panel and proceed to the bottom
     * and that there will be one column.  The default field will be the first provided.
     *
     * @param fields An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects that
     *               will be used to gather input for this dialog.
     */
    public InputPanel(InputField... fields)
    {
        this(true, 1, null, fields);
    }

    /**
     * Skeleton constructor.  Assumes that there will be a single line.  The default field will be the first provided.
     *
     * @param direction <code>true</code> if the fields are to start at the top of the panel and proceed to the bottom;
     *                  <code>false</code> if the fields are to start at the left of the panel and proceed to the
     * @param fields    An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects
     *                  that will be used to gather input for this dialog.
     */
    public InputPanel(boolean direction, InputField... fields)
    {
        this(direction, 1, null, fields);
    }

    /**
     * Skeleton constructor.  Assumes that the direction is column-wise.  The default field will be the first provided.
     *
     * @param lines  The number of lines of items to use.  One "line" is a column or row (depending on
     * @param fields An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects that
     *               will be used to gather input for this dialog.
     */
    public InputPanel(int lines, InputField... fields)
    {
        this(true, lines, null, fields);
    }

    /**
     * Skeleton constructor.  The default field will be the first provided.
     *
     * @param direction <code>true</code> if the fields are to start at the top of the panel and proceed to the bottom;
     *                  <code>false</code> if the fields are to start at the left of the panel and proceed to the
     *                  right.
     * @param lines     The number of lines of items to use.  One "line" is a column or row (depending on
     * @param fields    An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects
     *                  that will be used to gather input for this dialog.
     */
    public InputPanel(boolean direction, int lines, InputField... fields)
    {
        this(direction, lines, null, fields);
    }

    /**
     * Skeleton constructor.  Assumes that the fields will start from the top of this panel and proceed to the bottom
     * and that there will be one column.
     *
     * @param def    The default field; that is, the field which will have focus when the dialog is created.  This
     *               parameter is the key of such a field.  If this parameter is <code>null</code>, the first field is
     * @param fields An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects that
     *               will be used to gather input for this dialog.
     */
    public InputPanel(String def, InputField... fields)
    {
        this(true, 1, def, fields);
    }

    /**
     * Skeleton constructor.  Assumes that there will be a single line.
     *
     * @param direction <code>true</code> if the fields are to start at the top of the panel and proceed to the bottom;
     *                  <code>false</code> if the fields are to start at the left of the panel and proceed to the
     *                  right.
     * @param def       The default field; that is, the field which will have focus when the dialog is created.  This
     *                  parameter is the key of such a field.  If this parameter is <code>null</code>, the first field
     * @param fields    An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects
     *                  that will be used to gather input for this dialog.
     */
    public InputPanel(boolean direction, String def, InputField... fields)
    {
        this(direction, 1, def, fields);
    }

    /**
     * Skeleton constructor.  Assumes that the direction is column-wise.
     *
     * @param lines  The number of lines of items to use.  One "line" is a column or row (depending on
     *               <code>direction</code>) of fields and their corresponding prompting texts.
     * @param def    The default field; that is, the field which will have focus when the dialog is created.  This
     *               parameter is the key of such a field.  If this parameter is <code>null</code>, the first field is
     * @param fields An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects that
     *               will be used to gather input for this dialog.
     */
    public InputPanel(int lines, String def, InputField... fields)
    {
        this(true, lines, def, fields);
    }

    /**
     * General constructor.
     *
     * @param direction <code>true</code> if the fields are to start at the top of the panel and proceed to the bottom;
     *                  <code>false</code> if the fields are to start at the left of the panel and proceed to the
     *                  right.
     * @param lines     The number of lines of items to use.  One "line" is a column or row (depending on
     *                  <code>direction</code>) of fields and their corresponding prompting texts.
     * @param def       The default field; that is, the field which will have focus when the dialog is created.  This
     *                  parameter is the key of such a field.  If this parameter is <code>null</code>, the first field
     * @param fields    An {@link InputField}<code>[]</code> which contains, in order, the {@link InputField} objects
     *                  that will be used to gather input for this dialog.
     */
    public InputPanel(boolean direction, int lines, String def, InputField... fields)
    {
        fieldMap = new HashMap<Object, InputField>();
        completed = false;

        int lineLength = (fields.length + (lines - 1)) / lines;

        InformalGridLayout layout;
        if (direction)
        {
            layout = new InformalGridLayout(
                    lines, lineLength, 5, 5, false,
                    InformalGridLayout.ORIENTATION_LEFT_TO_RIGHT_VERTICAL);
        } else
        {
            layout = new InformalGridLayout(
                    lineLength, lines, 5, 5, false,
                    InformalGridLayout.ORIENTATION_LEFT_TO_RIGHT_HORIZONTAL);
        }
        this.setLayout(layout);
        for (final InputField field : fields)
        {
            fieldMap.put(field.getKey(), field);
            JPanel p = new JPanel();
            if (direction)
            {
                p.setLayout(new FlowLayout());
            } else
            {
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            }
            JLabel promptingLabel = new JLabel(field.getPromptingText());
            promptingLabel.setAlignmentX(direction ? Component.RIGHT_ALIGNMENT : Component.CENTER_ALIGNMENT);
            p.add(promptingLabel);
            JComponent c = field.getComponent();
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(c);
            this.add(p);

            field.setFocusDefault((def != null) && (field.getKey().equals(def)));
        }
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        initialize();
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Initializes this panel.
     */
    public void initialize()
    {
        for (InputField field : fieldMap.values())
        {
            field.initialize();
        }
    }

    /**
     * Retrieves the {@link InputField} with the specified key.
     *
     * @param key The input field key.
     * @return The {@link InputField} with that key, or <code>null</code> if no such field exists.
     */
    public InputField getField(Object key)
    {
        return fieldMap.get(key);
    }

    /**
     * Retrieves a {@link Set} containing all field keys.
     *
     * @return The specified {@link Set}.
     */
    public Set getFieldKeySet()
    {
        return fieldMap.keySet();
    }

    /**
     * Retrieves the result obtained from the {@link InputField} with the specified key.
     *
     * @param key The key of the {@link InputField} in question.
     * @return The result that the specified {@link InputField} garnered from the user.
     * @throws NullPointerException If the key does not map to an {@link InputField} provided upon construction.
     */
    public Object getResult(Object key)
    {
        return fieldMap.get(key).getResult();
    }

    /**
     * Retrieves the first error message returned by the {@link InputField}s in this object.  If no {@link InputField}s
     * return an error, <code>null</code> is returned.
     *
     * @return An error message, or <code>null</code> for no error.
     */
    public String getFirstError()
    {
        for (InputField field : fieldMap.values())
        {
            String errorMessage = field.getErrorMessage();
            if (errorMessage != null) return errorMessage;
        }
        return null;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class represents one field of input for the {@link InputPanel}.  It is abstract; the actual {@link
     * InputField}s are extensions of this class.
     *
     * @author Zachary Palmer
     */
    public static abstract class InputField
    {
        /**
         * The key for this {@link InputField}.
         */
        protected Object key;
        /**
         * The prompting text for this field.
         */
        protected String promptingText;
        /**
         * Whether or not this field is designated as being default for focus.  This field is only manipulated by the
         * {@link InputPanel} itself.
         */
        protected boolean focusDefault;

        /**
         * General constructor.
         *
         * @param key            The key that will be used to retrieve the value obtained by this field from the dialog
         *                       once execution is complete.
         * @param promptingText The text which will be displayed to the left of the component to prompt the user for
         *                       information.
         */
        public InputField(Object key, String promptingText)
        {
            this.key = key;
            this.promptingText = promptingText;
            focusDefault = false;
        }

        /**
         * Determines whether or not this field is marked for default focus.
         *
         * @return <code>true</code> if this field is marked for default focus; <code>false</code> otherwise.
         */
        public boolean isFocusDefault()
        {
            return focusDefault;
        }

        /**
         * Sets whether or not this field is marked for default focus.
         *
         * @param focusDefault <code>true</code> if this field is to be marked for default focus; <code>false</code> if
         *                      not.
         */
        void setFocusDefault(boolean focusDefault)
        {
            this.focusDefault = focusDefault;
        }

        /**
         * Retrieves the key that will be used to retrieve the value obtained by this field from the dialog once
         * execution is complete.
         *
         * @return The key for this {@link InputField}.
         */
        public Object getKey()
        {
            return key;
        }

        /**
         * Retrieves the text which will be displayed to the left of the component to prompt the user for information.
         *
         * @return The prompting text for this {@link InputField}.
         */
        public String getPromptingText()
        {
            return promptingText;
        }

        /**
         * Retrieves the result from the execution of the {@link InputPanel}.  If execution has not occurred, this
         * method should provide a default, usually prescribed by the constructor.
         *
         * @return The result as prescribed above.
         */
        public abstract Object getResult();

        /**
         * Initializes this {@link InputField}.  This is called whenever the dialog is executed.
         */
        public abstract void initialize();

        /**
         * Retrieves the {@link JComponent} which will represent this {@link InputField} in the dialog.
         */
        public abstract JComponent getComponent();

        /**
         * Determines whether or not the value contained within this field is permissible.  If there is something about
         * the contents of this field that indicates that the user should not be allowed to proceed without changing
         * them, this method should return a {@link String} containing the value for a label which explains the problem.
         * Otherwise, this method should return <code>null</code>.
         *
         * @return An error message, or <code>null</code> for no error.
         */
        public abstract String getErrorMessage();
    }

    /**
     * This class represents an {@link InputField} for the {@link JTextField} class.
     *
     * @author Zachary Palmer
     */
    public static class TextField extends InputField
    {
        /**
         * The {@link JTextField} for this {@link InputField}.
         */
        protected JTextField textField;
        /**
         * The default value for this {@link InputField}'s text field.
         */
        protected String defaultValue;

        /**
         * General constructor.
         *
         * @param key            The key object by which this result will be retrieved.
         * @param promptingText The prompting text for this {@link InputField}.
         * @param defaultValue  The default value for this field.
         */
        public TextField(Object key, String promptingText, String defaultValue)
        {
            this(key, promptingText, defaultValue, 0);
        }

        /**
         * General constructor.
         *
         * @param key            The key object by which this result will be retrieved.
         * @param promptingText The prompting text for this {@link InputField}.
         * @param defaultValue  The default value for this field.
         * @param columns        The number of columns that the {@link JTextField} must have, or <code>0</code> to let
         *                       the default text dictate this.
         */
        public TextField(Object key, String promptingText, String defaultValue, int columns)
        {
            super(key, promptingText);
            this.defaultValue = defaultValue;
            if (columns == 0)
            {
                textField = new JTextField(this.defaultValue);
            } else
            {
                textField = new JTextField(columns);
                textField.setText(defaultValue);
            }
        }

        /**
         * Retrieves the text from the execution of the {@link InputPanel}.  In this case, it is always a string
         * containing the entered text.
         *
         * @return The result as prescribed above.
         */
        public Object getResult()
        {
            return textField.getText();
        }

        /**
         * Initializes this {@link InputField}.  This is called whenever the dialog is executed.
         */
        public void initialize()
        {
            textField.setText(defaultValue);
        }

        /**
         * Retrieves the {@link JComponent} which will represent this {@link InputField} in the dialog.
         */
        public JComponent getComponent()
        {
            return textField;
        }

        /**
         * Retrieves the text from the execution of the {@link TextField}.
         *
         * @return The result as prescribed above.
         */
        public String getTextResult()
        {
            return textField.getText();
        }

        /**
         * Changes the default value for this dialog.
         *
         * @param defaultValue The new default value for this dialog.
         */
        public void setDefault(String defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        /**
         * This field always accepts the value stored within it.  Therefore, it always returns <code>null</code> here
         * for "no error."
         *
         * @return <code>null</code>, always.
         */
        public String getErrorMessage()
        {
            return null;
        }
    }

    /**
     * This extension to {@link TextField} permits the input provided by the user only if it is a parsable integer.
     *
     * @author Zachary Palmer
     */
    public static class IntegerField extends TextField
    {
        /**
         * General constructor.
         *
         * @param key            The key object by which this result will be retrieved.
         * @param promptingText The prompting text for this {@link InputField}.
         * @param defaultValue  The default value for this field.
         * @param columns        The number of columns that the {@link JTextField} must have, or <code>0</code> to let
         *                       the default text dictate this.
         */
        public IntegerField(Object key, String promptingText, String defaultValue, int columns)
        {
            super(key, promptingText, defaultValue, columns);
        }

        /**
         * This field returns an error message if the current value is not a parsable integer.
         *
         * @return The error message, or <code>null</code> if {@link IntegerField#getResult()} can be called safely.
         */
        public String getErrorMessage()
        {
            try
            {
                Integer.parseInt(super.getTextResult());
                return null;
            } catch (NumberFormatException nfe)
            {
                return '"' + super.getTextResult() + "\" is not an integer.";
            }
        }

        /**
         * Retrieves the text from the execution of the {@link InputPanel}.  In this case, it is always an {@link
         * Integer} containing the value of the entered text.
         *
         * @return The result as prescribed above.
         */
        public Object getResult()
        {
            return new Integer(super.getTextResult());
        }
    }

    /**
     * This {@link InputField} implementation accepts an integer within a certain range.
     *
     * @author Zachary Palmer
     */
    public static class BoundedIntegerField extends IntegerField
    {
        /**
         * The minimum acceptable value (inclusive) of this field.
         */
        protected int minimum;
        /**
         * The maximum acceptable value (inclusive) of this field.
         */
        protected int maximum;
        /**
         * The message to provide if the field value is an integer outside of that bound.
         */
        protected String boundMessage;


        /**
         * General constructor.
         *
         * @param key              The key object by which this result will be retrieved.
         * @param promptingText   The prompting text for this {@link InputField}.
         * @param defaultValue    The default value for this field.
         * @param columns          The number of columns that the {@link JTextField} must have, or <code>0</code> to let
         *                         the default text dictate this.
         * @param min              The minimum acceptable value (inclusive) of this field.
         * @param max              The maximum acceptable value (inclusive) of this field.
         * @param boundingMessage The message to provide if the field value is an integer outside of that bound.
         */
        public BoundedIntegerField(Object key, String promptingText, String defaultValue, int columns, int min,
                                   int max, String boundingMessage)
        {
            super(key, promptingText, defaultValue, columns);
            minimum = min;
            maximum = max;
            boundMessage = boundingMessage;
        }

        /**
         * This field returns an error message if the current value is not a parsable integer or is outside of the
         * specified boundary.
         *
         * @return The error message, or <code>null</code> if {@link IntegerField#getResult()} can be called safely.
         */
        public String getErrorMessage()
        {
            String error = super.getErrorMessage();
            if (error == null)
            {
                int value = ((Integer) (getResult()));
                if ((value < minimum) || (value > maximum))
                {
                    error = boundMessage;
                }
            }
            return error;
        }
    }

    /**
     * This {@link InputField} implementation contains a single checkbox.
     *
     * @author Zachary Palmer
     */
    public static class CheckBoxField extends InputField
    {
        /**
         * The default state for the checkbox.
         */
        protected boolean defaultValue;
        /**
         * The checkbox which is used for user interface.
         */
        protected JCheckBox checkBox;

        /**
         * General constructor.
         *
         * @param key    The key which is used to identify this field.
         * @param prompt The prompting text for this field.
         * @param def    The default value for the checkbox.
         */
        public CheckBoxField(String key, String prompt, boolean def)
        {
            super(key, prompt);
            defaultValue = def;
            checkBox = new JCheckBox();
            initialize();
        }

        /**
         * Retrieves the {@link JCheckBox} which will represent this {@link InputPanel.CheckBoxField} in the dialog.
         */
        public JComponent getComponent()
        {
            return checkBox;
        }

        /**
         * Unused, as it is impossible to enter an illegal value into a checkbox.
         *
         * @return <code>null</code>, always.
         */
        public String getErrorMessage()
        {
            return null;
        }

        /**
         * Retrieves the result from the execution of the {@link InputPanel}.  This will be a {@link Boolean} object
         * representing whether or not the checkbox was checked.
         *
         * @return The result as prescribed above.
         */
        public Object getResult()
        {
            return checkBox.isSelected();
        }

        /**
         * Initializes this {@link InputPanel.CheckBoxField}.  This is called whenever the dialog is executed.
         */
        public void initialize()
        {
            checkBox.setSelected(defaultValue);
        }
    }
}