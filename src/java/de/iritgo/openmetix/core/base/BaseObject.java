/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.base;


import de.iritgo.openmetix.core.logger.Log;


/**
 * Super class of all classes fo the Iritgo framework.
 *
 * BaseObject provides each class with a unique type id
 * which is initialized to the class name by default.
 *
 * In addition each instance is equipped with a world wide unique
 * identifier.
 *
 * @version $Id: BaseObject.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class BaseObject
{
	/** The type id of the object. */
	protected String typeId;

	/** The unique id of the object. */
	protected long uniqueId;

	/**
	 * Create a new BaseObject and initialize the
	 * type id with the class name.
	 */
	public BaseObject ()
	{
		try
		{
			typeId = this.getClass ().getName ().toLowerCase ();
		}
		catch (Exception x)
		{
			Log.log ("system", "BaseObject", "Cannot determine class.", Log.FATAL);
		}
	}

	/**
	 * Create a new BaseObject.
	 *
	 * @param typeId The type id.
	 */
	public BaseObject (String typeId)
	{
		this(typeId, 0);
	}

	/**
	 * Create a new BaseObject.
	 *
	 * @param typeId The type id.
	 * @param uniqueId The unique id.
	 */
	public BaseObject (String typeId, long uniqueId)
	{
		this.typeId = typeId;
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the unique id.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId ()
	{
		return uniqueId;
	}

	/**
	 * Set the unique id.
	 *
	 * @param uniqueId The new unique id.
	 */
	public void setUniqueId (long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the type id.
	 *
	 * @return The type id.
	 */
	public String getTypeId ()
	{
		return typeId;
	}

	/**
	 * Set the type id.
	 *
	 * @param typeId The new type id.
	 */
	public void setTypeId (String typeId)
	{
		this.typeId = typeId;
	}
}