/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.core.action.AbstractAction;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.user.User;


/**
 * A SensorListener listens to incoming measurments.
 *
 * @version $Id: SensorListener.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public abstract class SensorListener
	extends BaseObject
	implements MeasurementAgent
{
	/** The client transceiver. */
	protected ClientTransceiver clientTransceiver;

	/** The listening user. */
	protected User user;

	/** The configuration of the sensor to listen to. */
	protected ConfigurationSensor configurationSensor;

	/** The sensor to listen to. */
	protected GagingSensor gagingSensor;

	/** True if we are allready listening to this sensor. */
	protected boolean listenAlready;

	/**
	 * Create a new SensorListener.
	 *
	 * @parm typeId The type id of the new listener.
	 */
	public SensorListener (String typeId)
	{
		super(typeId);
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
		this.user = user;
		this.gagingSensor = gagingSensor;
		this.configurationSensor = configurationSensor;
		this.listenAlready = listenAlready;
		this.clientTransceiver = new ClientTransceiver(user.getNetworkChannel ());
		this.clientTransceiver.addReceiver (this.clientTransceiver.getSender ());
	}

	/**
	 * Set the listening user.
	 *
	 * @param user The listening user.
	 */
	public void setUser (User user)
	{
		this.user = user;
	}

	/**
	 * Get the listening user.
	 *
	 * @return The listening user.
	 */
	public User getUser ()
	{
		return user;
	}

	/**
	 * Helper method that sends an action to the server.
	 *
	 * @param action The action to perform.
	 */
	protected void sendAction (AbstractAction action)
	{
		action.setTransceiver (clientTransceiver);
		ActionTools.sendToClient (action);
	}

	/**
	 * Remove all previous receivers and add the current user to
	 * the client transceiver.
	 */
	protected void cleanAndSetUserToTransceiver ()
	{
		clientTransceiver.removeAllReceivers ();
		clientTransceiver.setSender (user.getNetworkChannel ());
		clientTransceiver.addReceiver (user.getNetworkChannel ());
	}

	/**
	 * Called when a new measurement was received.
	 * Subclasses should provide their own implementation of this method.
	 *
	 * @param measurement The received measurement.
	 */
	public abstract void receiveSensorValue (Measurement measurement);
}