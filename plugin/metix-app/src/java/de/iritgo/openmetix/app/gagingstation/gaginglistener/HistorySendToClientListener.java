/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.gaginglistener;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.app.gagingstation.SensorListener;
import de.iritgo.openmetix.app.gagingstation.action.HistoricalGagingSensorAction;
import de.iritgo.openmetix.framework.user.User;


/**
 * This listener is used to recieve measurements that are send
 * from a historical measurement query.
 *
 * @version $Id: HistorySendToClientListener.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class HistorySendToClientListener
	extends SensorListener
{
	/** True if the history was already sent. */
	public boolean historyAlreadySend;

	/**
	 * Create a new HistorySendToClientListener.
	 */
	public HistorySendToClientListener ()
	{
		super("HistorySendToClientListener");
	}

	/**
	 * Initialize the listener.
	 *
	 * @param user The listening user.
	 * @param gagingSensor The sensor to listen to.
	 * @param configurationSensor The configuration of the sensor to listen to.
	 * @param listenAlready True if we are allready listening to this sensor.
	 */
	public void init (
		User user, GagingSensor gagingSensor, ConfigurationSensor configurationSensor,
		boolean listenAlready)
	{
		super.init (user, gagingSensor, configurationSensor, listenAlready);

		gagingSensor.addMeasurementAgent (this);
	}

	/**
	 * Called when a new measurement was received.
	 * Subclasses should provide their own implementation of this method.
	 *
	 * @param measurement The received measurement.
	 */
	public void receiveSensorValue (Measurement sensorTuple)
	{
		if (! user.isOnline ())
		{
			historyAlreadySend = false;

			return;
		}

		if (historyAlreadySend)
		{
			return;
		}

		historyAlreadySend = true;

		cleanAndSetUserToTransceiver ();

		HistoricalGagingSensorAction action =
			new HistoricalGagingSensorAction(
				sensorTuple.timestamp, sensorTuple.value, sensorTuple.stationId,
				sensorTuple.sensorId);

		sendAction (action);
	}
}