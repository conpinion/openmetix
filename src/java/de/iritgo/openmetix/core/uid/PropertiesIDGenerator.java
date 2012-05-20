/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.uid;


import de.iritgo.openmetix.core.base.SystemProperties;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;


/**
 * This is an id generator that stores it's state in a property object.
 *
 * @version $Id: PropertiesIDGenerator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class PropertiesIDGenerator
	extends DefaultIDGenerator
{
	/** The properties in which the generator state is stored. */
	protected SystemProperties properties;

	/** The property key under which the generator state is stored. */
	protected String propertyKey;

	/**
	 * Create a new id generator.
	 */
	public PropertiesIDGenerator ()
	{
		super("PropertiesIDGenerator", 1, 1);
	}

	/**
	 * Create a new id generator.
	 *
	 * @param properties The properties in which to store the current state.
	 * @param propertyKey The key under which to store the current state.
	 * @param start The initial id value.
	 * @param step The step increment.
	 */
	public PropertiesIDGenerator (
		SystemProperties properties, String propertyKey, long start, long step)
	{
		super("PropertiesIDGenerator", start, step);
		this.properties = properties;
		this.propertyKey = propertyKey;
	}

	/**
	 * Load the last generator state.
	 */
	public void load ()
	{
		id = properties.getLong (propertyKey, id);

		Log.logDebug (
			"persist", "PropertiesIDGenerator",
			"Successfully loaded the generator state (id=" + id + ")");
	}

	/**
	 * Store the generator state.
	 */
	public void save ()
	{
		properties.put (propertyKey, Long.toString (id));

		Log.logDebug (
			"persist", "PropertiesIDGenerator",
			"Successfully saved the generator state (id=" + id + ")");
	}

	/**
	 * Create a new instance of the id generator.
	 *
	 * @return The fresh instance.
	 */
	public IObject create ()
	{
		return new PropertiesIDGenerator();
	}
}