/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Instances of this class describe a specific interface driver
 * behaviour.
 *
 * @version $Id: InterfaceDriverDescriptor.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class InterfaceDriverDescriptor
{
	/** The driver id. */
	private String id;

	/** The driver name. */
	private String name;

	/** The driver editor class. */
	private String driverEditorClass;

	/** The driver editor. */
	private InterfaceDriverEditor driverEditor;

	/** The driver class. */
	private String driverClass;

	/** Standard drivers (map from ids to drivers). */
	private static Map drivers;

	/** Standard drivers (list). */
	private static List driverList;

	/**
	 * Create a new InterfaceDriverDescriptor.
	 *
	 * @param id The driver id.
	 * @param name The driver name.
	 * @param driverEditorClass The driver editor class.
	 * @param driverClass The driver class.
	 */
	public InterfaceDriverDescriptor (
		String id, String name, String driverEditorClass, String driverClass)
	{
		this.id = id;
		this.name = name;
		this.driverEditorClass = driverEditorClass;
		this.driverClass = driverClass;
	}

	/**
	 * Get the driver id.
	 *
	 * @return The driver id.
	 */
	public String getId ()
	{
		return id;
	}

	/**
	 * Get the driver editor.
	 *
	 * @return The driver editor.
	 */
	public InterfaceDriverEditor getDriverEditor (ClassLoader classLoader)
	{
		if (driverEditor == null)
		{
			try
			{
				driverEditor =
					(InterfaceDriverEditor) classLoader.loadClass (driverEditorClass).newInstance ();
			}
			catch (Exception x)
			{
				Log.logError (
					"client", "InterfaceDriverDescriptor.getDriverEditor",
					"Unable to create the driver editor: " + x);
			}
		}

		return driverEditor;
	}

	/**
	 * Get the driver.
	 *
	 * @return The driver.
	 */
	public InterfaceDriver createDriver (ClassLoader classLoader)
	{
		try
		{
			return (InterfaceDriver) classLoader.loadClass (driverClass).newInstance ();
		}
		catch (Exception x)
		{
			Log.logError (
				"server", "InterfaceDriverDescriptor.createDriver",
				"Unable to create the driver: " + x);
		}

		return null;
	}

	/**
	 * Create a string representation.
	 *
	 * @return The string representation.
	 */
	public String toString ()
	{
		return Engine.instance ().getResourceService ().getStringWithoutException (name);
	}

	/**
	 * Get an iterator over all available drivers.
	 *
	 * @return A driver iterator.
	 */
	public static Iterator driverIterator ()
	{
		return driverList.iterator ();
	}

	/**
	 * Add a new driver descriptor.
	 *
	 * @param driverDescriptor The descriptor to add.
	 */
	public static void addDriverDescriptor (InterfaceDriverDescriptor driverDescriptor)
	{
		drivers.put (driverDescriptor.getId (), driverDescriptor);
		driverList.add (driverDescriptor);
	}

	/**
	 * Get a driver by id.
	 *
	 * @param iface The interface for which to create the driver.
	 * @return The drver or null if it wasn't found.
	 */
	public static InterfaceDriver createDriver (Interface iface)
	{
		InterfaceDriverDescriptor descriptor =
			(InterfaceDriverDescriptor) drivers.get (iface.getDriverId ());

		if (descriptor != null)
		{
			InterfaceDriver driver =
				descriptor.createDriver (InterfaceDriverDescriptor.class.getClassLoader ());

			driver.init (iface);

			return driver;
		}

		return null;
	}

	/**
	 * Create the standard drivers.
	 */
	static
	{
		drivers = new HashMap();
		driverList = new LinkedList();

		addDriverDescriptor (
			new InterfaceDriverDescriptor(
				"NullDriver", "metix-interfacing.nullDriver",
				"de.iritgo.openmetix.interfacing.link.NullInterfaceDriverEditor",
				"de.iritgo.openmetix.interfacing.link.NullInterfaceDriver"));

		addDriverDescriptor (
			new InterfaceDriverDescriptor(
				"SerialDriver", "metix-interfacing.serialDriver",
				"de.iritgo.openmetix.interfacing.link.SerialInterfaceDriverEditor",
				"de.iritgo.openmetix.interfacing.link.SerialInterfaceDriver"));
	}
}