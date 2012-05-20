/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation;


import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * Data object that represents a gaging station.
 *
 * @version $Id: GagingStation.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingStation
	extends DataObject
{
	/**
	 * Create a new GagingStation.
	 */
	public GagingStation ()
	{
		super("GagingStation");

		putAttribute ("name", "");
		putAttribute ("location", "");
		putAttribute ("longitude", "");
		putAttribute ("latitude", "");
		putAttribute (
			"sensors", new IObjectList("sensors", new FrameworkProxy(new GagingSensor()), this));
	}

	/**
	 * Get the station's name.
	 *
	 * @return The station's name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the station's name.
	 *
	 * @param name The new name.
	 */
	public void setName (String name)
	{
		putAttribute ("name", name);
	}

	/**
	 * Get the location of this station.
	 *
	 * @return The location.
	 */
	public String getLocation ()
	{
		return getStringAttribute ("location");
	}

	/**
	 * Set the location of this station.
	 *
	 * @param location The new location.
	 */
	public void setLocation (String location)
	{
		putAttribute ("location", location);
	}

	/**
	 * Get the geographical longitude.
	 *
	 * @return The geographical longitude.
	 */
	public String getLongitude ()
	{
		return getStringAttribute ("longitude");
	}

	/**
	 * Set the geographical longitude.
	 *
	 * @param longitude The geographical longitude.
	 */
	public void setLongitude (String longitude)
	{
		putAttribute ("longitude", longitude);
	}

	/**
	 * Get the geographical latitude.
	 *
	 * @return The geographical latitude.
	 */
	public String getLatitude ()
	{
		return getStringAttribute ("latitude");
	}

	/**
	 * Set the geographical latitude.
	 *
	 * @param longitude The geographical latitude.
	 */
	public void setLatitude (String latitude)
	{
		putAttribute ("latitude", latitude);
	}

	/**
	 * Create a string representation of the station.
	 *
	 * @return The string representation.
	 */
	public String toString ()
	{
		return getName ();
	}

	/**
	 * Add a sensor to the station.
	 *
	 * @param gagingSensor The sensor to add.
	 */
	public void addSensor (GagingSensor gagingSensor)
	{
		getIObjectListAttribute ("sensors").add (gagingSensor);
	}

	/**
	 * Remove a sensor from the station.
	 *
	 * @param gagingSensor The sensor to remove.
	 */
	public void removeSensor (GagingSensor gagingSensor)
	{
		getIObjectListAttribute ("sensors").remove (gagingSensor);
	}

	/**
	 * Get an iterator over all sensors in this station.
	 *
	 * @return A sensor iterator.
	 */
	public Iterator sensorIterator ()
	{
		return getIObjectListAttribute ("sensors").iterator ();
	}

	/**
	 * Get the number of sensors in this station.
	 *
	 * @return The number of sensors.
	 */
	public int getSensorCount ()
	{
		return getIObjectListAttribute ("sensors").size ();
	}

	/**
	 * Get a sensor at a specific index.
	 *
	 * @param index The index of the sensor to retrieve.
	 * @return The sensor.
	 */
	public GagingSensor getSensor (int index)
	{
		return (GagingSensor) getIObjectListAttribute ("sensors").get (index);
	}

	/**
	 * Get all sensors in this station.
	 *
	 * @return The list of all sensors.
	 */
	public IObjectList getSensors ()
	{
		return getIObjectListAttribute ("sensors");
	}

	/**
	 * Find a sensor with a specific id.
	 *
	 * @param sensorId The id of the sensor to retrieve.
	 * @return The sensor or null if the sensor wasn't found.
	 */
	public GagingSensor findSensor (long sensorId)
	{
		for (Iterator iter = sensorIterator (); iter.hasNext ();)
		{
			GagingSensor sensor = (GagingSensor) iter.next ();

			if (sensor.getUniqueId () == sensorId)
			{
				return sensor;
			}
		}

		return null;
	}
}