/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.reduction;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;


/**
 * @version $Id: DataReductionRequest.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DataReductionRequest
	extends NetworkFrameworkServerAction
{
	private long instrumentUniqueId;
	private String timeNumber = new String();
	private int timeIndex;
	private long stationID;
	private long sensorID;
	private boolean deleteAllParameters;

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public DataReductionRequest ()
	{
		setTypeId ("DataReductionRequest");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param sensorId Id of the receiving sensor.
	 * @param startDate Start date.
	 * @param stopDate End date.
	 */
	public DataReductionRequest (
		long instrumentUniqueId, String timeNumber, int timeIndex, long stationID, long sensorID,
		boolean deleteAllParameters)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.timeNumber = timeNumber;
		this.timeIndex = timeIndex;
		this.stationID = stationID;
		this.sensorID = sensorID;
		this.deleteAllParameters = deleteAllParameters;
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
		timeNumber = stream.readUTF ();
		timeIndex = stream.readInt ();
		stationID = stream.readLong ();
		sensorID = stream.readLong ();
		deleteAllParameters = stream.readBoolean ();
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
		stream.writeUTF (timeNumber);
		stream.writeInt (timeIndex);
		stream.writeLong (stationID);
		stream.writeLong (sensorID);
		stream.writeBoolean (deleteAllParameters);
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		final DataReductionRequest drr = this;

		Engine.instance ().getThreadService ().add (
			new DataReduction(
				instrumentUniqueId, clientTransceiver.getSender (), timeNumber, timeIndex, stationID,
				sensorID, deleteAllParameters));
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