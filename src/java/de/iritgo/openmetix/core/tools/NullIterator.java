/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.tools;


import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A NullIterator is an empty iterator that returns no items.
 *
 * @version $Id: NullIterator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class NullIterator
	implements Iterator
{
	/**
	 * Create a new null iterator.
	 */
	public NullIterator ()
	{
	}

	/**
	 * A null iterator has no items.
	 *
	 * @return False.
	 */
	public boolean hasNext ()
	{
		return false;
	}

	/**
	 * The null iterator returns no values.
	 */
	public Object next ()
	{
		throw new NoSuchElementException();
	}

	/**
	 * Nothing to remove.
	 */
	public void remove ()
	{
		throw new UnsupportedOperationException();
	}
}