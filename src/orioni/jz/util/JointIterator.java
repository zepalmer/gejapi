package orioni.jz.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@link JointIterator} class is an implementation of {@link Iterator}.  It accepts multiple {@link Iterator}
 * objects on construction and uses them as underlying data sources.  All of the elements from the first iterator are
 * provided before any of the elements on following iterators are provided.  Once all iterators passed to this
 * {@link JointIterator} on construction have been exhausted, this iterator is considered exhausted.
 * <p>
 * This implementation is <b>not</b> synchronized.
 *
 * @author Zachary Palmer
 */
public class JointIterator<T> implements Iterator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** A {@link ArrayList} containing the subsidiary {@link Iterator} objects of this {@link JointIterator}. */
	protected ArrayList<Iterator<T>> iterators;
	/** The currently active {@link Iterator}. */
	protected Iterator<T> activeIterator;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * General constructor.
	 * @param iterators An {@link Iterator}<code>[]</code> containing the iterators to be used by this
	 *                  {@link JointIterator}.
	 */
	public JointIterator(Iterator<T>... iterators)
	{
		super();
		this.iterators = new ArrayList<Iterator<T>>();
		if (iterators.length>0)
		{
			activeIterator = iterators[0];
			if (iterators.length>1)
			{
				for (int i=1;i<iterators.length;i++)
				{
					this.iterators.add(iterators[i]);
				}
			}
		}
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Determines whether or not any of the subsidiary {@link Iterator}s of this {@link JointIterator} have more
	 * elements.
	 *
	 * @return <code>true</code> if this {@link JointIterator} can provide the caller with more elements;
	 *         <code>false</code> otherwise
	 */
	public boolean hasNext()
	{
		while ((activeIterator ==null) || (!activeIterator.hasNext()))
		{
			if (iterators.size()>0)
			{
				activeIterator = iterators.remove(0);
			} else return false;
		}
		return activeIterator.hasNext();  // should be true
	}

	/**
	 * Returns the next element in the iteration.
	 *
	 * @return The next element in the iteration.
	 * @throws NoSuchElementException If this iteration has no more elements.
	 */
	public T next()
	{
		if (hasNext())
		{
			return activeIterator.next();
		} else throw new NoSuchElementException("No more elements in this iterator.");
	}

	/**
	 * This method is not supported by {@link JointIterator} at this time.
	 * @throws UnsupportedOperationException Always.
	 */
	public void remove()
	{
		throw new UnsupportedOperationException("remove() not supported by JointIterator");
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}