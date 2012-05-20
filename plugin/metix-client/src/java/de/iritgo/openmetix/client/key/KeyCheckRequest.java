/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.key;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;


/**
 * This action is executed on the server. It receives the historical data query.
 *
 * @version $Id: KeyCheckRequest.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class KeyCheckRequest
	extends NetworkFrameworkServerAction
{
	private String licensingName = new String();
	private String key = new String();
	private boolean result;
	private int number;
	private String perform = new String();
	private boolean checkStart = false;

	/** Id of the receiving instrument. */
	private long instrumentUniqueId;

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public KeyCheckRequest ()
	{
		setTypeId ("KeyCheckRequest");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param sensorId Id of the receiving sensor.
	 * @param startDate Start date.
	 * @param stopDate End date.
	 */
	public KeyCheckRequest (
		long instrumentUniqueId, String performName, String licensingName, String key,
		boolean checkStart)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.perform = performName;
		this.licensingName = licensingName;
		this.key = key;
		this.checkStart = checkStart;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (FrameworkInputStream stream)
		throws IOException
	{
		instrumentUniqueId = stream.readLong ();
		licensingName = stream.readUTF ();
		key = stream.readUTF ();
		perform = stream.readUTF ();
		checkStart = stream.readBoolean ();
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (instrumentUniqueId);
		stream.writeUTF (licensingName);
		stream.writeUTF (key);
		stream.writeUTF (perform);
		stream.writeBoolean (checkStart);
	}

	public void setLicensingName (String name)
	{
		this.licensingName = name;
	}

	public void setKey (String key)
	{
		this.key = key;
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		final KeyCheckRequest kcr = this;

		if (perform.equals ("update"))
		{
			Engine.instance ().getThreadService ().add (
				new KeyCheckDBUpdate(
					instrumentUniqueId, clientTransceiver.getSender (), licensingName, key,
					checkStart));
		}

		if (perform.equals ("request"))
		{
			Engine.instance ().getThreadService ().add (
				new KeyCheckDBRequest(
					instrumentUniqueId, clientTransceiver.getSender (), checkStart));
		}
	}

	/**
	 * Get the action to execute in the client.
	 *
	 * @param clientTransceiver The client transceiver.
	 * @return The client action.
	 */
	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		return null;
	}
}