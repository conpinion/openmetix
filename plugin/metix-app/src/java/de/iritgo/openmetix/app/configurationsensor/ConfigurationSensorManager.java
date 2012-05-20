/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.configurationsensor;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.MeasurementAgent;
import de.iritgo.openmetix.app.gagingstation.SensorListener;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.app.gagingstation.manager.SensorListenerManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObjectListEvent;
import de.iritgo.openmetix.core.iobject.IObjectListListener;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.base.IObjectModifiedListener;
import de.iritgo.openmetix.framework.user.User;
import java.util.Iterator;


/**
 * The ConfigurationSensorManager manages the listeners, listening to sensor values.
 * It automatically creates listeners for newly created configuration sensors.
 *
 * @version $Id: ConfigurationSensorManager.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ConfigurationSensorManager
	extends BaseObject
	implements Manager, IObjectModifiedListener, IObjectListListener
{
	/** The gaging station manager. */
	GagingStationManager gagingStationManager;

	/** THe sensor listener manager. */
	SensorListenerManager sensorListenerManager;

	/**
	 * Create a new ConfigurationSensorManager.
	 */
	public ConfigurationSensorManager ()
	{
		super("ConfigurationSensorManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		Engine.instance ().getEventRegistry ().addListener ("objectmodified", this);
		Engine.instance ().getEventRegistry ().addListener ("objectcreated", this);

		gagingStationManager =
			(GagingStationManager) Engine.instance ().getManagerRegistry ().getManager (
				"GagingStationManager");

		sensorListenerManager =
			(SensorListenerManager) Engine.instance ().getManagerRegistry ().getManager (
				"SensorListenerManager");
	}

	/**
	 * Free all manager resources.
	 */
	public void unload ()
	{
	}

	/**
	 * Called when an iobject was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		if (event.getModifiedObject () instanceof ConfigurationSensor)
		{
			addListenerToSensor (
				(ConfigurationSensor) event.getModifiedObject (),
				(User) event.getClientTransceiver ().getConnectedChannel ().getCustomContextObject ());
		}
	}

	/**
	 * Called when an iobject list was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectListEvent (IObjectListEvent event)
	{
		if (event.getObject () instanceof ConfigurationSensor)
		{
			addListenerToSensor (
				(ConfigurationSensor) event.getObject (),
				(User) event.getClientTransceiver ().getConnectedChannel ().getCustomContextObject ());
		}
	}

	/**
	 * Add a listener to a sensor configuration.
	 *
	 * @param configurationSensor The sensor configuration for which to add a listener.
	 * @param user The user for which to add a listener.
	 */
	public void addListenerToSensor (ConfigurationSensor configurationSensor, User user)
	{
		long stationId = configurationSensor.getStationId ();
		long sensorId = configurationSensor.getSensorId ();

		for (Iterator i = gagingStationManager.stationIterator (); i.hasNext ();)
		{
			GagingStation gagingStation = (GagingStation) i.next ();

			if (gagingStation.getUniqueId () == stationId)
			{
				for (Iterator t = gagingStation.sensorIterator (); t.hasNext ();)
				{
					GagingSensor gagingSensor = (GagingSensor) t.next ();

					if (gagingSensor.getUniqueId () == sensorId)
					{
						boolean userListenAlready =
							checkUserListenAlreadyAtThisSensor (gagingSensor, user);

						String sensorListenerId = configurationSensor.getSensorListenerId ();

						if (sensorListenerId.equals ("ignore"))
						{
							return;
						}

						SensorListener sensorListener = null;

						try
						{
							sensorListener =
								(SensorListener) sensorListenerManager.getSensorListener (
									sensorListenerId).getClass ().newInstance ();
						}
						catch (Exception x)
						{
							return;
						}

						sensorListener.init (
							user, gagingSensor, configurationSensor, userListenAlready);
					}
				}
			}
		}
	}

	/**
	 * Check wether a user is already listener to sensor values of the
	 * specified sensor.
	 *
	 * @param gagingSensor The sensor to check.
	 * @param user The user to check.
	 */
	public boolean checkUserListenAlreadyAtThisSensor (GagingSensor gagingSensor, User user)
	{
		boolean userListens = false;

		for (Iterator j = gagingSensor.getMeasurementAgentIterator (); j.hasNext ();)
		{
			MeasurementAgent ma = (MeasurementAgent) j.next ();

			if (ma instanceof SensorListener)
			{
				SensorListener gagingListener = (SensorListener) ma;

				if (gagingListener.getUser () == user)
				{
					userListens = true;

					break;
				}
			}
		}

		return userListens;
	}
}