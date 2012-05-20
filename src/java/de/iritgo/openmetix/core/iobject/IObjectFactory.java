/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import java.util.Map;
import java.util.TreeMap;


/**
 * <code>IObjectFactory</code> is a prototype factory that can
 * create new <code>IObject</code>s by specifying their type id,
 * e.g.
 *
 * @version $Id: IObjectFactory.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectFactory
	extends BaseObject
{
	/** A mapping from type names to prototype objects. */
	private Map iObjectPrototypes = null;

	/**
	 * Create a new <code>IObjectFactory</code>.
	 */
	public IObjectFactory ()
	{
		iObjectPrototypes = new TreeMap();
	}

	/**
	 * Register a <code>IObject</code>.
	 *
	 * @param object The prototype object to add.
	 */
	public void register (IObject object)
	{
		iObjectPrototypes.put (object.getTypeId (), object);
	}

	/**
	 * Remove a <code>IObject</code> from the factory.
	 *
	 * @param object The prototype object to remove.
	 */
	public void remove (IObject object)
	{
		iObjectPrototypes.remove (object.getTypeId ());
	}

	/**
	 * Check wether the factory can create objects of a specific type.
	 *
	 * @param typeId The type to check.
	 * @return True if the factory can generate the specified type.
	 */
	public boolean contains (String typeId)
	{
		return iObjectPrototypes.containsKey (typeId);
	}

	/**
	 * Create a new object instance by specyfing the type id.
	 *
	 * @param typeId The type id of the object to create.
	 */
	public IObject newInstance (String typeId)
		throws NoSuchIObjectException
	{
		IObject prototype = (IObject) iObjectPrototypes.get (typeId);

		if (prototype == null)
		{
			throw new NoSuchIObjectException(typeId);
		}

		try
		{
			return (IObject) prototype.getClass ().newInstance ();
		}
		catch (Exception x)
		{
			Log.log (
				"system", "PrototypeFactory.createInstanceOf", "Cannot determine classname.",
				Log.FATAL);
		}

		return null;
	}
}