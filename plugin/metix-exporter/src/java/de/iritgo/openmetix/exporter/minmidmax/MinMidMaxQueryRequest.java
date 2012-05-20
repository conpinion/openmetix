/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.minmidmax;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * This action is executed on the server. It receives the historical data query.
 *
 * @version $Id: MinMidMaxQueryRequest.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class MinMidMaxQueryRequest
	extends NetworkFrameworkServerAction
{
	/** List of the receiving sensor. */
	private List sensors;

	/** Query start date. */
	private long startDate;

	/** Query end date. */
	private long stopDate;

	/** Query start year. */
	private long startYear;

	/** Query end date. */
	private long stopYear;

	/** Id of the receiving instrument. */
	private long instrumentUniqueId;

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public MinMidMaxQueryRequest ()
	{
		setTypeId ("MMMQRQ");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param sensorId Id of the receiving sensor.
	 * @param startDate Start date.
	 * @param stopDate End date.
	 */
	public MinMidMaxQueryRequest (
		long instrumentUniqueId, List sensors, long startDate, long stopDate, long startYear,
		long stopYear)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.sensors = sensors;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.startYear = startYear;
		this.stopYear = stopYear;
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
		startDate = stream.readLong ();
		stopDate = stream.readLong ();
		startYear = stream.readLong ();
		stopYear = stream.readLong ();

		int size = stream.readInt ();

		if (sensors == null)
		{
			sensors = new LinkedList();
		}

		for (int i = 0; i < size; ++i)
		{
			sensors.add (new Long(stream.readLong ()));
		}
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
		stream.writeLong (startDate);
		stream.writeLong (stopDate);
		stream.writeLong (startYear);
		stream.writeLong (stopYear);
		stream.writeInt (sensors.size ());

		for (int i = 0; i < sensors.size (); ++i)
		{
			stream.writeLong (((Long) sensors.get (i)).longValue ());
		}
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		final MinMidMaxQueryRequest mmmqr = this;

		Engine.instance ().getThreadService ().add (
			new DBRequest(
				instrumentUniqueId, clientTransceiver.getSender (), sensors, startDate, stopDate,
				startYear, stopYear));
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