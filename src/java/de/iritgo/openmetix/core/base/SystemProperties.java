/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.base;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * This is an enhanced version of Java's Property class.
 *
 * It provides convenient methods for an easier property value access.
 *
 * @version $Id: SystemProperties.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class SystemProperties
	extends Properties
{
	/**
	 * Create a new SystemProperties
	 */
	public SystemProperties ()
	{
	}

	/**
	 * Load the properties from the property file.
	 *
	 * @param fileName The name of the property file.
	 */
	public void load (String fileName)
	{
		Engine engine = Engine.instance ();

		try
		{
			super.load (
				new FileInputStream(engine.getSystemDir () + engine.getFileSeparator () + fileName));

			Log.logInfo (
				"system", "SystemProperties.load",
				"System properties loaded from file '" + fileName + "'");
		}
		catch (IOException x)
		{
			Log.logWarn (
				"system", "SystemProperties.load",
				"Unable to load system properties file '" + fileName + "'");
		}
	}

	/**
	 * Store the properties to the property file.
	 *
	 * @param fileName The name of the property file.
	 */
	public void store (String fileName)
	{
		Engine engine = Engine.instance ();

		try
		{
			super.store (
				new FileOutputStream(
					engine.getSystemDir () + engine.getFileSeparator () + fileName), fileName);

			Log.logInfo (
				"system", "SystemProperties.store",
				"System properties stored to file '" + fileName + "'");
		}
		catch (IOException x)
		{
			Log.logError (
				"system", "SystemProperties.store",
				"Unable to store system properties file '" + fileName + "'");
		}
	}

	/**
	 * Store a string value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, String value)
	{
		setProperty (key, value);
	}

	/**
	 * Retrieve a string value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public String getString (String key, String defaultValue)
	{
		return getProperty (key, defaultValue);
	}

	/**
	 * Store an integer value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, int value)
	{
		setProperty (key, String.valueOf (value));
	}

	/**
	 * Retrieve a integer value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public int getInt (String key, int defaultValue)
	{
		return NumberTools.toInt (getProperty (key), defaultValue);
	}

	/**
	 * Store a long integer value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, long value)
	{
		setProperty (key, String.valueOf (value));
	}

	/**
	 * Retrieve a long integer value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public long getLong (String key, long defaultValue)
	{
		return NumberTools.toLong (getProperty (key), defaultValue);
	}
}