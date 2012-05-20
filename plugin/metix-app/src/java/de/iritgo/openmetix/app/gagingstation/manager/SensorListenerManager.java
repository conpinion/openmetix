/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.manager;


import de.iritgo.openmetix.app.gagingstation.SensorListener;
import de.iritgo.openmetix.app.gagingstation.gaginglistener.HistorySendToClientListener;
import de.iritgo.openmetix.app.gagingstation.gaginglistener.SendToClientListener;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The SensorListenerManager manages a list of all sensor listeners.
 *
 * @version $Id: SensorListenerManager.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SensorListenerManager
	extends BaseObject
	implements Manager
{
	/** All managed sensor listeners. */
	private Map sensorListeners;

	/**
	 * Create a new SensorListenerManager.
	 */
	public SensorListenerManager ()
	{
		super("SensorListenerManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		sensorListeners = new HashMap();

		registerSensorListener (new SendToClientListener());
		registerSensorListener (new HistorySendToClientListener());
	}

	/**
	 * Free all manager resources.
	 */
	public void unload ()
	{
	}

	/**
	 * Add a listener to the manager.
	 *
	 * @param sensorListener The listener to add.
	 */
	public void registerSensorListener (SensorListener sensorListener)
	{
		sensorListeners.put (sensorListener.getTypeId (), sensorListener);
	}

	/**
	 * Remove a listener from the manager.
	 *
	 * @param sensorListener The listener to remove.
	 */
	public void removeSensorListener (SensorListener sensorListener)
	{
		sensorListeners.remove (sensorListener.getTypeId ());
	}

	/**
	 * Get a specific sensor listener.
	 *
	 * @param key The key of the listener to retrieve.
	 * @return The sensor listener.
	 */
	public SensorListener getSensorListener (String key)
	{
		return (SensorListener) sensorListeners.get (key);
	}

	/**
	 * Get an iterator over all registered listeners.
	 *
	 * @return An iterator over all listeners.
	 */
	public Iterator listenerIterator ()
	{
		return sensorListeners.values ().iterator ();
	}
}