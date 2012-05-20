/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.history;


import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;


/**
 * This action executes the historical data query in the write method (executed on the
 * server) which streams the query result to the client. The action's perform method
 * (executed on the client) fills the specifed instrument with the historical values.
 *
 * @version $Id: HistoricalDataResponseAction.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class HistoricalDataResponseAction
	extends FrameworkAction
	implements ResultSetHandler
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

	/** Output stream. */
	private FrameworkOutputStream outputStream;

	/** Maximum number of transfered values. */
	private final int NUM_VALUES = 180;

	/** Transfered measurements. */
	private LinkedList measurements;

	/**
	 * Create a new HistoricalDataAction.
	 */
	public HistoricalDataResponseAction ()
	{
		setTypeId ("HDRESA");
	}

	/**
	 * Create a new HistoricalDataAction.
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param stationId Id of the receiving station.
	 * @param sensorId Id of the receiving sensor.
	 * @param startDate Start date.
	 * @param stopDate End date.
	 */
	public HistoricalDataResponseAction (
		long instrumentUniqueId, long stationId, long sensorId, long startDate, long stopDate)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.stationId = stationId;
		this.sensorId = sensorId;
		this.startDate = startDate;
		this.stopDate = stopDate;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * This method reads all measurements from the stream and stores them into
	 * the measurement list.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (final FrameworkInputStream stream)
		throws IOException
	{
		measurements = new LinkedList();

		instrumentUniqueId = stream.readLong ();
		stationId = stream.readLong ();
		sensorId = stream.readLong ();

		for (;;)
		{
			long timestamp = stream.readLong ();

			if (timestamp == 0)
			{
				break;
			}

			double value = stream.readDouble ();

			measurements.add (
				new Measurement(
					instrumentUniqueId, new Timestamp(timestamp), value, stationId, sensorId));
		}
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * This method executes the query and transfers the query result to the
	 * client.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		Properties props = new Properties();
		String dbVersion =
			(String) CommandTools.performSimple ("persist.GetDatabaseVersion", props);

		outputStream = stream;

		outputStream.writeLong (instrumentUniqueId);
		outputStream.writeLong (stationId);
		outputStream.writeLong (sensorId);

		long interval = (stopDate - startDate) / NUM_VALUES;

		if (dbVersion.indexOf ("MySQL") != -1)
		{
			String select =
				"select avg(value) as valueAvg, max(at) as atMax, floor((unix_timestamp(at) - unix_timestamp(?)) / ?) as atRange" +
				" from Measurement where sensorid=? and at>=? and at<?" + " group by atRange";

			Object[] params = new Object[5];

			params[0] = new Timestamp(startDate);
			params[1] = new Long(interval / 1000);
			params[2] = new Long(sensorId);
			params[3] = new Timestamp(startDate);
			params[4] = new Timestamp(stopDate);

			props = new Properties();
			props.setProperty ("select", select);
			props.put ("handler", this);
			props.put ("params", params);
			CommandTools.performSimple ("persist.Select", props);
		}
		else if (dbVersion.indexOf ("HSQL") != -1)
		{
			String select =
				"select avg(value) as valueAvg, max(at) as atMax" +
				" from Measurement where sensorid=? and at>=? and at<?" +
				" group by floor(datediff('ms', at, ?) / ?)";

			Object[] params = new Object[5];

			params[0] = new Long(sensorId);
			params[1] = new Timestamp(startDate);
			params[2] = new Timestamp(stopDate);
			params[3] = new Timestamp(startDate);
			params[4] = new Long(interval);

			props = new Properties();
			props.setProperty ("select", select);
			props.put ("handler", this);
			props.put ("params", params);
			CommandTools.performSimple ("persist.Select", props);
		}
		else
		{
			long intervalStart = startDate;

			Object[] params = new Object[4];

			params[0] = new Long(stationId);
			params[1] = new Long(sensorId);

			props = new Properties();

			for (int i = 0; i < NUM_VALUES; ++i)
			{
				String select =
					"select avg(value) as valueAvg, max(at) as atMax from Measurement where " +
					"stationId=? and sensorId=? and at>=? and at<?";

				params[2] = new Timestamp(intervalStart);
				params[3] = new Timestamp(intervalStart + interval);

				props.setProperty ("select", select);
				props.put ("handler", this);
				props.put ("params", params);
				CommandTools.performSimple ("persist.Select", props);

				intervalStart += interval;

				try
				{
					Thread.sleep (1);
				}
				catch (Exception x)
				{
				}
			}
		}

		try
		{
			outputStream.writeLong (0);
		}
		catch (IOException x)
		{
		}
	}

	/**
	 * Handler method of the ResultSetHandler interface.
	 *
	 * This method actually iterates over the result set and transfers
	 * the measurement values.
	 *
	 * @param rs The query result set.
	 */
	public Object handle (ResultSet rs)
		throws SQLException
	{
		while (rs.next ())
		{
			Timestamp at = rs.getTimestamp ("atMax");
			double value = rs.getDouble ("valueAvg");

			if (at != null && at.getTime () != 0)
			{
				try
				{
					outputStream.writeLong (at.getTime ());
					outputStream.writeDouble (value);
				}
				catch (IOException x)
				{
				}
			}
		}

		return null;
	}

	/**
	 * Perform the action.
	 *
	 * This method transfers all measurements stored in the measurement list
	 * into the instrument.
	 */
	public void perform ()
	{
		final IDesktopManager desktopManager =
			Client.instance ().getClientGUI ().getDesktopManager ();

		try
		{
			SwingUtilities.invokeAndWait (
				new Runnable()
				{
					public void run ()
					{
						for (Iterator i = measurements.iterator (); i.hasNext ();)
						{
							Measurement m = (Measurement) i.next ();

							for (Iterator j = desktopManager.getDisplayIterator (); j.hasNext ();)
							{
								try
								{
									((InstrumentDisplay) ((IDisplay) j.next ()).getGUIPane ()).receiveHistoricalSensorValue (
										m.getInstrumentUniqueId (), m.getTimestamp (), m.getValue (),
										m.getStationId (), m.getSensorId ());
								}
								catch (ClassCastException noInstrumentDisplayIgnored)
								{
								}
								catch (Exception x)
								{
								}
							}
						}
					}
				});
		}
		catch (Exception x)
		{
		}

		try
		{
			Thread.sleep (1);
		}
		catch (Exception x)
		{
		}
	}
}