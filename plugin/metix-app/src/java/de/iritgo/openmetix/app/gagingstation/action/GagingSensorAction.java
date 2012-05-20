/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.action;


import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.client.Client;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;


/**
 * The GagingSensorAction is executed on the client and feeds received
 * measurement values into the instruments.
 *
 * @version $Id: GagingSensorAction.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingSensorAction
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
	 * Create a new GagingSensorAction.
	 */
	public GagingSensorAction ()
	{
		setTypeId ("GAC");
	}

	/**
	 * Create a new GagingSensorAction.
	 *
	 * @param timestamp The time on which the measurement was taken.
	 * @param value The measurement value.
	 * @param stationId The station to which the measurement belongs.
	 * @param sensorId The sensor to which the measurement belongs.
	 */
	public GagingSensorAction (Timestamp timestamp, double value, long stationId, long sensorId)
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
		sensorId = stream.readLong ();
		stationId = stream.readLong ();
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
		stream.writeLong (sensorId);
		stream.writeLong (stationId);
	}

	/**
	 * Perform the action.
	 *
	 * This method simply calls the receiveSensorValue() methods of the
	 * instrument displays.
	 */
	public void perform ()
	{
		final IDesktopManager desktopManager =
			Client.instance ().getClientGUI ().getDesktopManager ();

		try
		{
			for (Iterator i = desktopManager.getDisplayIterator (); i.hasNext ();)
			{
				try
				{
					((InstrumentDisplay) ((IDisplay) i.next ()).getGUIPane ()).receiveSensorValue (
						timestamp, value, stationId, sensorId);
				}
				catch (ClassCastException noInstrumentDisplayIgnored)
				{
				}
			}
		}
		catch (Exception igored)
		{
		}
	}
}