/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gui.GagingSensorBehaviourEditor;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Instances of this class describe a specific gaging sensor
 * behaviour.
 *
 * @version $Id: GagingSensorBehaviourDescriptor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class GagingSensorBehaviourDescriptor
{
	/** The behaviour id. */
	private String id;

	/** The behaviour name. */
	private String name;

	/** The behaviour editor class. */
	private String behaviourEditorClass;

	/** The behaviour editor. */
	private GagingSensorBehaviourEditor behaviourEditor;

	/** The behaviour class. */
	private String behaviourClass;

	/** Standard behaviours (map from ids to behaviours). */
	private static Map behaviours;

	/** Standard behaviours (list). */
	private static List behaviourList;

	/**
	 * Create a new SensorTypeItem.
	 *
	 * @param id The behaviour id.
	 * @param name The behaviour name.
	 * @param behaviourEditorClass The behaviour editor class.
	 * @param behaviourClass The behaviour class.
	 */
	public GagingSensorBehaviourDescriptor (
		String id, String name, String behaviourEditorClass, String behaviourClass)
	{
		this.id = id;
		this.name = name;
		this.behaviourEditorClass = behaviourEditorClass;
		this.behaviourClass = behaviourClass;
	}

	/**
	 * Get the behaviour id.
	 *
	 * @return The behaviour id.
	 */
	public String getId ()
	{
		return id;
	}

	/**
	 * Get the behaviour editor.
	 *
	 * @return The behaviour editor.
	 */
	public GagingSensorBehaviourEditor getBehaviourEditor (ClassLoader classLoader)
	{
		if (behaviourEditor == null)
		{
			try
			{
				behaviourEditor =
					(GagingSensorBehaviourEditor) classLoader.loadClass (behaviourEditorClass)
															 .newInstance ();
			}
			catch (Exception x)
			{
				Log.logError (
					"client", "GagingSensorBehaviourDescriptor.getBehaviourEditor",
					"Unable to create the behaviour editor: " + x);
			}
		}

		return behaviourEditor;
	}

	/**
	 * Get the behaviour.
	 *
	 * @return The behaviour.
	 */
	public GagingSensorBehaviour createBehaviour (ClassLoader classLoader)
	{
		try
		{
			return (GagingSensorBehaviour) classLoader.loadClass (behaviourClass).newInstance ();
		}
		catch (Exception x)
		{
			Log.logError (
				"client", "GagingSensorBehaviourDescriptor.getBehaviour",
				"Unable to create the behaviour: " + x);
		}

		return null;
	}

	/**
	 * Create a string representation.
	 *
	 * @return The item string.
	 */
	public String toString ()
	{
		return Engine.instance ().getResourceService ().getStringWithoutException (name);
	}

	/**
	 * Get an iterator over all available behaviours.
	 *
	 * @return A behaviour iterator.
	 */
	public static Iterator behaviourIterator ()
	{
		return behaviourList.iterator ();
	}

	/**
	 * Add a new behaviour descriptor.
	 *
	 * @param behaviourDescriptor The descriptor to add.
	 */
	public static void addBehaviourDescriptor (GagingSensorBehaviourDescriptor behaviourDescriptor)
	{
		behaviours.put (behaviourDescriptor.getId (), behaviourDescriptor);
		behaviourList.add (behaviourDescriptor);
	}

	/**
	 * Get a behavour by id.
	 *
	 * @param id The id of the behaviour to retrieve.
	 * @return The behaviour or null if it wasn't found.
	 */
	public static GagingSensorBehaviour createBehaviour (String id)
	{
		GagingSensorBehaviourDescriptor descriptor =
			(GagingSensorBehaviourDescriptor) behaviours.get (id);

		if (descriptor != null)
		{
			return descriptor.createBehaviour (
				GagingSensorBehaviourDescriptor.class.getClassLoader ());
		}

		return null;
	}

	/**
	 * Create the standard descriptors.
	 */
	static
	{
		behaviours = new HashMap();
		behaviourList = new LinkedList();

		addBehaviourDescriptor (
			new GagingSensorBehaviourDescriptor(
				"SystemSensorBehaviour", "metix.systemSensor",
				"de.iritgo.openmetix.client.gui.SystemSensorBehaviourEditor",
				"de.iritgo.openmetix.app.gagingstation.sensorbehaviour.SystemSensorBehaviour"));

		addBehaviourDescriptor (
			new GagingSensorBehaviourDescriptor(
				"MinimumSensorBehaviour", "metix.minimumSensor",
				"de.iritgo.openmetix.client.gui.SimplePeriodSensorBehaviourEditor",
				"de.iritgo.openmetix.app.gagingstation.sensorbehaviour.MinimumSensorBehaviour"));

		addBehaviourDescriptor (
			new GagingSensorBehaviourDescriptor(
				"MaximumSensorBehaviour", "metix.maximumSensor",
				"de.iritgo.openmetix.client.gui.SimplePeriodSensorBehaviourEditor",
				"de.iritgo.openmetix.app.gagingstation.sensorbehaviour.MaximumSensorBehaviour"));

		addBehaviourDescriptor (
			new GagingSensorBehaviourDescriptor(
				"AverageSensorBehaviour", "metix.averageSensor",
				"de.iritgo.openmetix.client.gui.SimplePeriodSensorBehaviourEditor",
				"de.iritgo.openmetix.app.gagingstation.sensorbehaviour.AverageSensorBehaviour"));

		addBehaviourDescriptor (
			new GagingSensorBehaviourDescriptor(
				"DewpointSensorBehaviour", "metix.dewpointSensor",
				"de.iritgo.openmetix.client.gui.DewpointSensorBehaviourEditor",
				"de.iritgo.openmetix.app.gagingstation.sensorbehaviour.DewpointSensorBehaviour"));

		addBehaviourDescriptor (
			new GagingSensorBehaviourDescriptor(
				"ManualSensorBehaviour", "metix.manualSensor",
				"de.iritgo.openmetix.client.gui.ManualSensorBehaviourEditor",
				"de.iritgo.openmetix.app.gagingstation.sensorbehaviour.ManualSensorBehaviour"));
	}
}