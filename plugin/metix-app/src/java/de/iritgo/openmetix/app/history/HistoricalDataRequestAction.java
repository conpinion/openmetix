/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.history;


import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;
import java.util.Date;


/**
 * This action is executed on the server. It receives the historical data query and
 * creates the client action which executes the query in it's write method.
 *
 * @version $Id: HistoricalDataRequestAction.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class HistoricalDataRequestAction
	extends NetworkFrameworkServerAction
{
	/** Id of the receiving sensor. */
	private long sensorId;

	/** Id of the receiving station. */
	private long stationId;

	/** Query start date. */
	private long startDate;

	/** Query end date. */
	private long stopDate;

	/** Id of the receiving instrument. */
	private long instrumentUniqueId;

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public HistoricalDataRequestAction ()
	{
		setTypeId ("HDREQA");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param stationId Id of the receiving station.
	 * @param sensorId Id of the receiving sensor.
	 * @param startDate Start date.
	 * @param stopDate End date.
	 */
	public HistoricalDataRequestAction (
		long instrumentUniqueId, long stationId, long sensorId, Date startDate, Date stopDate)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.sensorId = sensorId;
		this.stationId = stationId;
		this.startDate = startDate.getTime ();
		this.stopDate = stopDate.getTime ();
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
		sensorId = stream.readLong ();
		stationId = stream.readLong ();
		startDate = stream.readLong ();
		stopDate = stream.readLong ();
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
		stream.writeLong (sensorId);
		stream.writeLong (stationId);
		stream.writeLong (startDate);
		stream.writeLong (stopDate);
	}

	/**
	 * Get the action to execute in the client.
	 *
	 * @param clientTransceiver The client transceiver.
	 * @return The client action.
	 */
	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		clientTransceiver.addReceiver (clientTransceiver.getSender ());

		if (stopDate == 0)
		{
			long currentTime = System.currentTimeMillis ();

			startDate = currentTime - startDate;
			stopDate = currentTime;
		}

		return new HistoricalDataResponseAction(
			instrumentUniqueId, stationId, sensorId, startDate, stopDate);
	}
}