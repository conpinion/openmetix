/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.IObjectProxyEvent;
import de.iritgo.openmetix.core.iobject.IObjectProxyListener;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * This registry contains all known gaging stations.
 *
 * @version $Id: GagingStationRegistry.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingStationRegistry
	extends DataObject
{
	/**
	 * Helper method for easy access to the registry.
	 *
	 * @return The gaging station registry.
	 */
	public static GagingStationRegistry getRegistry ()
	{
		try
		{
			return (GagingStationRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
				"GagingStationRegistry", GagingStationRegistry.class);
		}
		catch (NoSuchIObjectException x)
		{
			return null;
		}
	}

	/**
	 * Create a new GagingStationRegistry.
	 */
	public GagingStationRegistry ()
	{
		super("GagingStationRegistry");

		putAttribute (
			"gagingStations",
			new IObjectList("gagingStations", new FrameworkProxy(new GagingStation()), this));
	}

	/**
	 * Add a new gagingstation to the registry.
	 *
	 * @param gagingStation The station to add.
	 */
	public void addGagingStation (GagingStation gagingStation)
	{
		getIObjectListAttribute ("gagingStations").add (gagingStation);
	}

	/**
	 * Remove a gagingstation from the registry.
	 *
	 * @param gagingStation The station to remove.
	 */
	public boolean removeGagingStation (GagingStation gagingStation)
	{
		return getIObjectListAttribute ("gagingStations").remove (gagingStation);
	}

	/**
	 * Get the gagingstation at a specific index.
	 *
	 * @param index The index of the station to retrieve.
	 * @return The gaging station.
	 */
	public GagingStation getGagingStation (int index)
	{
		return (GagingStation) getIObjectListAttribute ("gagingStations").get (index);
	}

	/**
	 * Get an iterator over all gaging stations.
	 *
	 * @return A station iterator.
	 */
	public Iterator gagingStationIterator ()
	{
		return getIObjectListAttribute ("gagingStations").iterator ();
	}

	/**
	 * Get the number of all stations in the registry.
	 *
	 * @return The number of stations.
	 */
	public int getGagingStationSize ()
	{
		return getIObjectListAttribute ("gagingStations").size ();
	}

	/**
	 * Get the list of all gaging station.
	 *
	 * @return All gaging stations.
	 */
	public IObjectList getGagingStations ()
	{
		return getIObjectListAttribute ("gagingStations");
	}

	/**
	 * Find a station with a specific id.
	 *
	 * @param stationId The id of the station to retrieve.
	 * @return The station or null if the station wasn't found.
	 */
	public GagingStation findStation (long stationId)
	{
		for (Iterator iter = gagingStationIterator (); iter.hasNext ();)
		{
			GagingStation station = (GagingStation) iter.next ();

			if (station.getUniqueId () == stationId)
			{
				return station;
			}
		}

		return null;
	}

	/**
	 * Find a sensor with a specific id.
	 *
	 * @param sensorId The id of the sensor to retrieve.
	 * @return The sensor or null if the sensor wasn't found.
	 */
	public GagingSensor findSensor (long sensorId)
	{
		for (Iterator iter = gagingStationIterator (); iter.hasNext ();)
		{
			GagingStation station = (GagingStation) iter.next ();

			for (Iterator j = station.sensorIterator (); j.hasNext ();)
			{
				GagingSensor sensor = (GagingSensor) j.next ();

				if (sensor.getUniqueId () == sensorId)
				{
					return sensor;
				}
			}
		}

		return null;
	}

	/**
	 * Find the station of a specific sensor.
	 *
	 * @param sensorId The unique id of the sensor to search.
	 * @return The station that contains the sensor.
	 */
	public GagingStation findStationOfSensor (long sensorId)
	{
		for (Iterator iter = gagingStationIterator (); iter.hasNext ();)
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
	 * Register a proxy listener for all sensors in all stations.
	 *
	 * @param listener The listener to add.
	 */
	public void registerListener (final IObjectProxyListener listener)
	{
		for (Iterator iter = gagingStationIterator (); iter.hasNext ();)
		{
			final GagingStation station = (GagingStation) iter.next ();

			Engine.instance ().getProxyEventRegistry ().addEventListener (
				station,
				new IObjectProxyListener()
				{
					public void proxyEvent (IObjectProxyEvent event)
					{
						for (Iterator iter2 = station.sensorIterator (); iter2.hasNext ();)
						{
							GagingSensor sensor = (GagingSensor) iter2.next ();

							Engine.instance ().getProxyEventRegistry ().addEventListener (
								sensor, listener);
						}
					}
				});
		}
	}
}