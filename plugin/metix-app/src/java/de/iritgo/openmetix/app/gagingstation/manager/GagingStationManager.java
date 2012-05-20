/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.manager;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.base.IObjectModifiedListener;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * The GagingStationManager is mainly used to dispatch new measurment values to
 * the gaging sensors.
 *
 * @version $Id: GagingStationManager.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class GagingStationManager
	extends BaseObject
	implements Manager, IObjectModifiedListener
{
	/** The list of all gaging stations. */
	private List gagingStations;

	/**
	 * Create a new GagingStationManager.
	 */
	public GagingStationManager ()
	{
		super("GagingStationManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		gagingStations = new LinkedList();
		Engine.instance ().getEventRegistry ().addListener ("objectmodified", this);
	}

	/**
	 * Free all manager resources.
	 */
	public void unload ()
	{
	}

	/**
	 * Add a gaging station to the manager.
	 *
	 * @param gagingStation The gaging station to add.
	 */
	public void addGagingStation (GagingStation gagingStation)
	{
		gagingStations.add (gagingStation);

		for (Iterator j = gagingStation.sensorIterator (); j.hasNext ();)
		{
			GagingSensor sensor = (GagingSensor) j.next ();

			sensor.updateInputConnections ();
		}
	}

	/**
	 * Remove a gaging station from the manager.
	 *
	 * @param gagingStation The gaging station to remove.
	 */
	public void removeGagingStation (GagingStation gagingStation)
	{
		gagingStations.remove (gagingStation);
	}

	/**
	 * Get an iterator over all gaging staions.
	 *
	 * @return An iterator over all stations.
	 */
	public Iterator stationIterator ()
	{
		return gagingStations.iterator ();
	}

	/**
	 * Dispatch a new measurement to the specified sensor.
	 *
	 * @param timestamp The timestamp of the measurement.
	 * @param value The measurement value.
	 * @param station The receiving station.
	 * @param sensor The receiving sensor.
	 * @param storeToDatabase If true, the measurement is stored in the database.
	 */
	public void receiveSensorValue (
		Timestamp timestamp, double value, GagingStation station, GagingSensor sensor,
		boolean storeToDatabase)
	{
		sensor.receiveSensorValue (
			new Measurement(
				timestamp, value, station.getUniqueId (), sensor.getUniqueId (), storeToDatabase));
	}

	/**
	 * Dispatch a new measurement to the specified sensor.
	 *
	 * @param measurement The measurement to dispatch.
	 */
	public void receiveSensorValue (Measurement measurement)
	{
		for (Iterator i = stationIterator (); i.hasNext ();)
		{
			GagingStation gagingStation = (GagingStation) i.next ();

			if (measurement.stationId != gagingStation.getUniqueId ())
			{
				continue;
			}

			for (Iterator t = gagingStation.sensorIterator (); t.hasNext ();)
			{
				GagingSensor gagingSensor = (GagingSensor) t.next ();

				if (measurement.sensorId != gagingSensor.getUniqueId ())
				{
					continue;
				}

				gagingSensor.receiveSensorValue (measurement);
			}

			break;
		}
	}

	/**
	 * Get a title for the specified station and sensor combination.
	 *
	 * @param stationId The unique id of the station.
	 * @param sensorId The unique id of the sensor.
	 * @return The title.
	 */
	public String getStationSensorTitle (long stationId, long sensorId)
	{
		for (Iterator i = stationIterator (); i.hasNext ();)
		{
			GagingStation station = (GagingStation) i.next ();

			if (station.getUniqueId () == stationId)
			{
				for (Iterator j = station.sensorIterator (); j.hasNext ();)
				{
					GagingSensor sensor = (GagingSensor) j.next ();

					if (sensor.getUniqueId () == sensorId)
					{
						return station.getName () + " - " + sensor.getName ();
					}
				}
			}
		}

		return "???";
	}

	/**
	 * Find the station of a specific sensor.
	 *
	 * @param sensorId The unique id of the sensor to search.
	 * @return The station that contains the sensor.
	 */
	public GagingStation findStationOfSensor (long sensorId)
	{
		for (Iterator iter = gagingStations.iterator (); iter.hasNext ();)
		{
			GagingStation station = (GagingStation) iter.next ();

			for (Iterator j = station.sensorIterator (); j.hasNext ();)
			{
				GagingSensor sensor = (GagingSensor) j.next ();

				if (sensor.getUniqueId () == sensorId)
				{
					return station;
				}
			}
		}

		return null;
	}

	/**
	 * Called when an iobject was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		if (event.getModifiedObject () instanceof GagingSensor)
		{
			GagingSensor sensor = (GagingSensor) event.getModifiedObject ();

			sensor.updateInputConnections ();
		}
		else if (event.getModifiedObject () instanceof GagingStation)
		{
			addGagingStation ((GagingStation) event.getModifiedObject ());
		}
	}
}