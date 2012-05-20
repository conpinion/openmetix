/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.instrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * Base class for all instrument data objects.
 *
 * @version $Id: Instrument.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public abstract class Instrument
	extends DataObject
{
	/**
	 * Create a new Instrument.
	 *
	 * @param typeId The instrument's type id.
	 */
	public Instrument (String typeId)
	{
		super(typeId);

		putAttribute ("fresh", new Integer(1));
		putAttribute ("title", "Instrument");
		putAttribute ("fitToWindow", new Integer(0));

		putAttribute (
			"sensorConfigs",
			new IObjectList(
				"sensorConfigs", new FrameworkProxy(new ConfigurationSensor("dummy")), this));
	}

	/**
	 * Get the instrument title.
	 *
	 * @return The instrument title.
	 */
	public String getTitle ()
	{
		return getStringAttribute ("title");
	}

	/**
	 * Set the instrument title.
	 *
	 * @param title The new instrument title.
	 */
	public void setTitle (String title)
	{
		putAttribute ("title", title);
	}

	/**
	 * Check wether the instrument is newly created.
	 *
	 * @return True for a new instrument.
	 */
	public boolean isFresh ()
	{
		return getIntAttribute ("fresh") != 0;
	}

	/**
	 * Set the freshness.
	 *
	 * @param fresh True for a new instrument.
	 */
	public void setFresh (boolean fresh)
	{
		putAttribute ("fresh", fresh ? 1 : 0);
	}

	/**
	 * Check wether the display should be fit to the window bounds.
	 *
	 * @return True if the diplay should be expanded.
	 */
	public boolean isFitToWindow ()
	{
		return getIntAttribute ("fitToWindow") != 0;
	}

	/**
	 * Determine wether the display should be fit to the window bounds.
	 *
	 * @param fitToWindow True if the diplay should be expanded.
	 */
	public void setFitToWindow (boolean fitToWindow)
	{
		putAttribute ("fitToWindow", fitToWindow ? 1 : 0);
	}

	/**
	 * Add a sensor configuration.
	 *
	 * @param sensorConfig The sensor configuration to add.
	 */
	public void addSensorConfig (ConfigurationSensor sensorConfig)
	{
		getIObjectListAttribute ("sensorConfigs").add (sensorConfig);
	}

	/**
	 * Get the first sensor configuration.
	 *
	 * @return The first sensor configuration.
	 */
	public ConfigurationSensor getSensorConfig ()
	{
		if (getSensorConfigCount () == 0)
		{
			return null;
		}

		return (ConfigurationSensor) getIObjectListAttribute ("sensorConfigs").get (0);
	}

	/**
	 * Get the number of sensor configurations.
	 *
	 * @return The number of sensor configurations.
	 */
	public int getSensorConfigCount ()
	{
		return getIObjectListAttribute ("sensorConfigs").size ();
	}

	/**
	 * Get an iterator over all sensor configurations.
	 *
	 * @return A sensor configuration iterator.
	 */
	public Iterator sensorConfigIterator ()
	{
		return getIObjectListAttribute ("sensorConfigs").iterator ();
	}

	/**
	 * Remove a sensor configuration.
	 *
	 * @param sensorConfig The sensor configuration to remove.
	 */
	public void removeSensorConfig (ConfigurationSensor sensorConfig)
	{
		getIObjectListAttribute ("sensorConfigs").remove (sensorConfig);
	}

	/**
	 * Retrieve a sensor configuration by index.
	 *
	 * @param index The index of the sensor configuration to retrieve.
	 * @return The sensor configuration.
	 */
	public ConfigurationSensor getSensorConfig (int index)
	{
		return (ConfigurationSensor) getIObjectListAttribute ("sensorConfigs").get (index);
	}

	/**
	 * Get a temporary, empty sensor.
	 *
	 * @return The temporary sensor.
	 */
	public abstract ConfigurationSensor getTmpSensor ();
}