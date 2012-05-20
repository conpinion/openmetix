/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystemdriver;


import de.iritgo.openmetix.core.Engine;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Instances of this class describe a specific gaging system driver.
 *
 * @version $Id: GagingSystemDriverDescriptor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class GagingSystemDriverDescriptor
{
	/** The driver id. */
	private String id;

	/** The driver name. */
	private String name;

	/** The driver class. */
	private String className;

	/** Class loader to use to load the driver. */
	private ClassLoader classLoader;

	/** The driver editor class name. */
	private String editorClassName;

	/** The gaging system output editor gui pane id. */
	private String outputEditorId;

	/** Driver map. */
	private static Map drivers;

	/** Driver list. */
	private static List driverList;

	/**
	 * Create a new GagingSystemDriverDescriptor.
	 *
	 * @param id The driver id.
	 * @param name The driver name.
	 * @param className The driver class.
	 * @param editorClassName The driver editor class name.
	 * @param outputEditorId The output editor gui pane id
	 * @param classLoader Class loader to use to load the driver.
	 */
	public GagingSystemDriverDescriptor (
		String id, String name, String className, String editorClassName, String outputEditorId,
		ClassLoader classLoader)
	{
		this.id = id;
		this.name = name;
		this.className = className;
		this.classLoader = classLoader;
		this.outputEditorId = outputEditorId;
		this.editorClassName = editorClassName;
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
	 * Get the driver class name.
	 *
	 * @return The driver class name.
	 */
	public String getClassName ()
	{
		return className;
	}

	/**
	 * Get the class loader.
	 *
	 * @return The class loader.
	 */
	public ClassLoader getClassLoader ()
	{
		return classLoader;
	}

	/**
	 * Get the editor class name.
	 *
	 * @return The editor class name.
	 */
	public String getEditorClassName ()
	{
		return editorClassName;
	}

	/**
	 * Get the gaging system output editor gui pane id.
	 *
	 * @return the output editor gui pane id.
	 */
	public String getOutputEditorId ()
	{
		return outputEditorId;
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
	 * Add a new driver descriptor.
	 *
	 * @param driverDescriptor The descriptor to add.
	 */
	public static void add (GagingSystemDriverDescriptor driverDescriptor)
	{
		drivers.put (driverDescriptor.getId (), driverDescriptor);
		driverList.add (driverDescriptor);
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
	 * Retrieve a driver descriptor.
	 *
	 * @param id The driver id.
	 */
	public static GagingSystemDriverDescriptor get (String id)
	{
		return (GagingSystemDriverDescriptor) drivers.get (id);
	}

	/**
	 * Singleton initialization.
	 */
	static
	{
		drivers = new HashMap();
		driverList = new LinkedList();
	}
}