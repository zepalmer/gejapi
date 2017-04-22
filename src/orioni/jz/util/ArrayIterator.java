package orioni.jz.util;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * This class is designed to allow iterator operations over an object array in Java.
 *
 * @author Zachary Palmer
 */
public class ArrayIterator<T> implements ListIterator<T>
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

	/** The <code>Object[]</code> on which this iterator will iterate. */
	protected T[] objectArray;
	/** The current index of this iterator. */
	protected int index;
	/** The index of the last return. */
	protected int lastReturnIndex;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

	/**
	 * Full constructor.  Creates an array iterator on the provided array.
	 * @param ar The <code>T[]</code> for which an iterator should be constructed.
	 */
	public ArrayIterator(T[] ar)
	{
		objectArray = ar;
		index = 0;
		lastReturnIndex = -1;
	}

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

	/**
	 * Returns <code>true</code> if this list iterator has more elements when
	 * traversing the list in the forward direction. (In other words, returns
	 * <code>true</code> if <code>next</code> would return an element rather than
	 * throwing an exception.)
	 *
	 * @return <code>true</code> if the list iterator has more elements when
	 *		traversing the list in the forward direction.
	 */
	public boolean hasNext()
	{
		return (index <objectArray.length);
	}

	/**
	 * Returns the next element in the list.  This method may be called
	 * repeatedly to iterate through the list, or intermixed with calls to
	 * <code>previous</code> to go back and forth.  (Note that alternating calls
	 * to <code>next</code> and <code>previous</code> will return the same element
	 * repeatedly.)
	 *
	 * @return the next element in the list.
	 * @throws NoSuchElementException if the iteration has no next element.
	 */
	public T next()
	{
		if (index ==objectArray.length)
		{
			lastReturnIndex = -1;
			throw new NoSuchElementException("No further elements exist");
		}
		lastReturnIndex = index;
		return objectArray[index++];
	}

	/**
	 * Returns <code>true</code> if this list iterator has more elements when
	 * traversing the list in the reverse direction.  (In other words, returns
	 * <code>true</code> if <code>previous</code> would return an element rather than
	 * throwing an exception.)
	 *
	 * @return <code>true</code> if the list iterator has more elements when
	 *	       traversing the list in the reverse direction.
	 */
	public boolean hasPrevious()
	{
		return (index >0);
	}

	/**
	 * Returns the previous element in the list.  This method may be called
	 * repeatedly to iterate through the list backwards, or intermixed with
	 * calls to <code>next</code> to go back and forth.  (Note that alternating
	 * calls to <code>next</code> and <code>previous</code> will return the same
	 * element repeatedly.)
	 *
	 * @return the previous element in the list.
	 *
	 * @throws NoSuchElementException if the iteration has no previous
	 *            element.
	 */
	public T previous()
	{
		lastReturnIndex = index - 1;
		if (index ==0)
			throw new NoSuchElementException("No previous elements exist");
		return objectArray[--index];
	}

	/**
	 * Returns the index of the element that would be returned by a subsequent
	 * call to <code>next</code>. (Returns list size if the list iterator is at the
	 * end of the list.)
	 *
	 * @return the index of the element that would be returned by a subsequent
	 * 	       call to <code>next</code>, or list size if list iterator is at end
	 *	       of list.
	 */
	public int nextIndex()
	{
		return index;
	}

	/**
	 * Returns the index of the element that would be returned by a subsequent
	 * call to <code>previous</code>. (Returns -1 if the list iterator is at the
	 * beginning of the list.)
	 *
	 * @return the index of the element that would be returned by a subsequent
	 * 	       call to <code>previous</code>, or -1 if list iterator is at
	 *	       beginning of list.
	 */
	public int previousIndex()
	{
		return index -1;
	}

	/**
	 * Throws an {@link UnsupportedOperationException}; elements cannot be removed from arrays.
	 * @throws UnsupportedOperationException Always; this class does not support {@link ListIterator#remove()}.
	 */
	public void remove()
	{
		throw new UnsupportedOperationException("ArrayIterator objects do not support remove()");
	}

	/**
	 * Replaces the last element returned by <code>next</code> or
	 * <code>previous</code> with the specified element (optional operation).
	 * This call can be made only if neither <code>ListIterator.remove</code> nor
	 * <code>ListIterator.add</code> have been called after the last call to
	 * <code>next</code> or <code>previous</code>.
	 *
	 * @param o the element with which to replace the last element returned by
	 *          <code>next</code> or <code>previous</code>.
	 * @throws IllegalStateException if neither <code>next</code> nor
	 *	          <code>previous</code> have been called, or <code>remove</code> or
	 *		  <code>add</code> have been called after the last call to
	 * 		  <code>next</code> or <code>previous</code>.
	 */
	public void set(T o)
	{
		if (lastReturnIndex == -1)
			throw new IllegalStateException("Last call to next() or previous() was not successful.");
		objectArray[lastReturnIndex] = o;
	}

	/**
	 * Throws an {@link UnsupportedOperationException}; elements cannot be added to arrays.
	 * @throws UnsupportedOperationException Always; this class does not support {@link ListIterator#add(Object)}.
	 */
	public void add(T o)
	{
		throw new UnsupportedOperationException("ArrayIterator objects do not support add(Object)");
	}

// STATIC METHODS ////////////////////////////////////////////////////////////////

}