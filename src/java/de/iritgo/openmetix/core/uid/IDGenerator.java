/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.uid;


import de.iritgo.openmetix.core.iobject.IObject;


/**
 * Interface for unique id generators. An id generator provides methods for
 * creating new unique ids and storing and loading of its internal state.
 *
 * @version $Id: IDGenerator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public interface IDGenerator
	extends IObject
{
	/**
	 * Create a new unique id.
	 *
	 * @return The new unique id.
	 */
	public long createId ();

	/**
	 * Get the value of the next id that createId() will return.
	 *
	 * @return The next id value.
	 */
	public long peekNextId ();

	/**
	 * Load the last generator state.
	 */
	public void load ();

	/**
	 * Store the generator state.
	 */
	public void save ();
}