/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * IObjectIterator.
 *
 * @version $Id: IObjectIterator.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectIterator
	implements Iterator
{
	/** The iterator. */
	Iterator iterator;

	/**
	 * Create a new IObjectIterator.
	 *
	 * @param linkedList
	 */
	public IObjectIterator (IObjectList linkedList)
	{
		iterator = linkedList.getListIterator ();
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other
	 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
	 * rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	synchronized public boolean hasNext ()
	{
		return iterator.hasNext ();
	}

	/**
	 * Returns the next element in the iteration.
	 *
	 * @return the next element in the iteration.
	 * @exception NoSuchElementException iteration has no more elements.
	 */
	synchronized public Object next ()
	{
		IObjectProxy proxy = (IObjectProxy) iterator.next ();

		return proxy.getRealObject ();
	}

	/**
	 *
	 * Removes from the underlying collection the last element returned by the
	 * iterator (optional operation).  This method can be called only once per
	 * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
	 * the underlying collection is modified while the iteration is in
	 * progress in any way other than by calling this method.
	 *
	 * @exception UnsupportedOperationException if the <tt>remove</tt>
	 *                  operation is not supported by this Iterator.

	 * @exception IllegalStateException if the <tt>next</tt> method has not
	 *                  yet been called, or the <tt>remove</tt> method has already
	 *                  been called after the last call to the <tt>next</tt>
	 *                  method.
	 */
	public void remove ()
	{
		iterator.remove ();
	}
}