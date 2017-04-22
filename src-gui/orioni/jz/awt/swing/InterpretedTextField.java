package orioni.jz.awt.swing;

import orioni.jz.util.strings.ValueInterpreter;

import javax.swing.*;

/**
 * This {@link JTextField} extension uses a {@link ValueInterpreter} to verify the contents of this {@link JTextField}.
 * The methods {@link InterpretedTextField#getValue()} and {@link InterpretedTextField#setValue(Object)} can be used to
 * get and set the text of the field by providing typed objects.
 *
 * @author Zachary Palmer
 */
public class InterpretedTextField <T> extends JTextField
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The {@link ValueInterpreter} used to interpret values for this {@link JTextField}.
     */
    protected ValueInterpreter<T> interpreter;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Assumes that the field is initially blank.
     *
     * @param interpreter The {@link ValueInterpreter} to use to interpret values for this {@link JTextField}.
     * @param cols        The width in columns of this {@link InterpretedTextField}.
     */
    public InterpretedTextField(ValueInterpreter<T> interpreter, int cols)
    {
        this(interpreter, null, cols);
    }

    /**
     * General constructor.
     *
     * @param interpreter The {@link ValueInterpreter} to use to interpret values for this {@link JTextField}.
     * @param value       The default value for this {@link InterpretedTextField}.
     * @param cols        The width in columns of this {@link InterpretedTextField}.
     */
    public InterpretedTextField(ValueInterpreter<T> interpreter, T value, int cols)
    {
        super((interpreter.toString(value)) == null ? "" : interpreter.toString(value), cols);
        this.interpreter = interpreter;
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the text of this {@link InterpretedTextField} as a value.  If the {@link ValueInterpreter} cannot
     * create a value from the text currently contained in the {@link InterpretedTextField}, <code>null</code> is
     * returned.
     *
     * @return The value contained in this field, or <code>null</code>.
     */
    public T getValue()
    {
        return interpreter.fromString(this.getText());
    }

    /**
     * Sets the text for this {@link InterpretedTextField} using the provided value.  If the provided value cannot be
     * converted to a {@link String} using this field's {@link ValueInterpreter}, the text is instead set to an empty
     * string, <code>""</code>.
     *
     * @param value The value to use while setting the field.
     */
    public void setValue(T value)
    {
        String s = interpreter.toString(value);
        setText(s == null ? "" : s);
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}

// END OF FILE