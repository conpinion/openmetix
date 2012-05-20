/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.base;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * All instances of BaseObjects are stored in this registry.
 *
 * This is the central point to retrieve objects by it's unique id.
 *
 * @version $Id: BaseRegistry.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class BaseRegistry
{
	/** Mapping from unique ids to BaseObjects. */
	private Map baseObjects;

	/**
	 * Create a new BaseRegistry.
	 *
	 */
	public BaseRegistry ()
	{
		baseObjects = new HashMap();
	}

	/**
	 * Add a BaseObject to the registry. The object is
	 * stored in the registry under it's unique id.
	 *
	 * @param object The object to add.
	 */
	synchronized public void add (BaseObject object)
	{
		baseObjects.put (new Long(object.getUniqueId ()), object);
	}

	/**
	 * Retrieve a BaseObject by it's unique id.
	 *
	 * @param uniqueId The unique of the object to retrieve.
	 * @return The object or null if it wasn't found.
	 */
	synchronized public BaseObject get (long uniqueId)
	{
		return (BaseObject) baseObjects.get (new Long(uniqueId));
	}

	/**
	 * Remove a BaseObject from the registry.
	 *
	 * @param object The object to remove.
	 */
	synchronized public void remove (BaseObject object)
	{
		baseObjects.remove (new Long(object.getUniqueId ()));
	}

	/**
	 * Get an iterator over all objects in this registry.
	 *
	 * @return The object iterator.
	 */
	synchronized public Iterator iterator ()
	{
		return baseObjects.values ().iterator ();
	}

	/**
	 * Remove all base objects from this registry.
	 */
	synchronized public void clear ()
	{
		baseObjects.clear ();
	}
}