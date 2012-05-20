/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.action;


import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import java.io.IOException;
import java.sql.Timestamp;


/**
 * The SendMeasurementServerAction is executed on the servers and sends
 * the received measurement values to the gaging station manager.
 *
 * @version $Id: SendMeasurementServerAction.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class SendMeasurementServerAction
	extends FrameworkAction
{
	/** The time on which the measurement was taken. */
	private Timestamp timestamp;

	/** The measurement value. */
	private double value;

	/** The station to which the measurement belongs. */
	private long sensorId;

	/** The sensor to which the measurement belongs. */
	private long stationId;

	/**
	 * Create a new SendMeasurementServerAction.
	 */
	public SendMeasurementServerAction ()
	{
		setTypeId ("SMSA");
	}

	/**
	 * Create a new SendMeasurementServerAction.
	 *
	 * @param timestamp The time on which the measurement was taken.
	 * @param value The measurement value.
	 * @param stationId The station to which the measurement belongs.
	 * @param sensorId The sensor to which the measurement belongs.
	 */
	public SendMeasurementServerAction (
		Timestamp timestamp, double value, long stationId, long sensorId)
	{
		this();
		this.timestamp = timestamp;
		this.value = value;
		this.stationId = stationId;
		this.sensorId = sensorId;
	}

	/**
	 * Read the attributes from an input stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (FrameworkInputStream stream)
		throws IOException
	{
		timestamp = new Timestamp(stream.readLong ());
		value = stream.readDouble ();
		stationId = stream.readLong ();
		sensorId = stream.readLong ();
	}

	/**
	 * Write the attributes to an output stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (timestamp.getTime ());
		stream.writeDouble (value);
		stream.writeLong (stationId);
		stream.writeLong (sensorId);
	}

	/**
	 * Perform the action.
	 *
	 * This method simply calls the receiveSensorValue() method of the
	 * gaging station manager.
	 */
	public void perform ()
	{
		GagingStationManager gagingStationManager =
			(GagingStationManager) Engine.instance ().getManagerRegistry ().getManager (
				"GagingStationManager");

		gagingStationManager.receiveSensorValue (
			new Measurement(timestamp, value, stationId, sensorId));
	}
}