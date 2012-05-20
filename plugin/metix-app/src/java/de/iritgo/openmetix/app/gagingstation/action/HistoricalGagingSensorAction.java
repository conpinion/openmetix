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
import de.iritgo.openmetix.framework.command.CommandTools;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;


/**
 * The HistoricalGagingSensorAction executes a historical measurement query on the server
 * feeds the received historical measurement values into the instrument display.
 *
 * @version $Id: HistoricalGagingSensorAction.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class HistoricalGagingSensorAction
	extends FrameworkAction
	implements ResultSetHandler
{
	/** The time on which the measurement was taken. */
	private Timestamp timestamp;

	/** The measurement value. */
	private double value;

	/** The station to which the measurement belongs. */
	private long sensorId;

	/** The sensor to which the measurement belongs. */
	private long stationId;

	/** The output stream. */
	private FrameworkOutputStream outputStream;

	/**
	 * Create a new HistoricalGagingSensorAction.
	 */
	public HistoricalGagingSensorAction ()
	{
		setTypeId ("HGAC");
	}

	/**
	 * Create a new HistoricalGagingSensorAction.
	 *
	 * @param timestamp The time on which the measurement was taken.
	 * @param value The measurement value.
	 * @param stationId The station to which the measurement belongs.
	 * @param sensorId The sensor to which the measurement belongs.
	 */
	public HistoricalGagingSensorAction (
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
	 * Remember that this method is executed on the client. The historical
	 * measurement values are received in this method and are fed to the
	 * receiving instrument.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (final FrameworkInputStream stream)
		throws IOException
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
						try
						{
							int numOfRows = stream.readInt ();

							int i = 0;

							while (i < numOfRows)
							{
								++i;
								timestamp = new Timestamp(stream.readLong ());
								value = stream.readDouble ();
								sensorId = stream.readLong ();
								stationId = stream.readLong ();

								for (Iterator j = desktopManager.getDisplayIterator ();
									j.hasNext ();)
								{
									try
									{
										((InstrumentDisplay) ((IDisplay) j.next ()).getGUIPane ()).receiveSensorValue (
											timestamp, value, stationId, sensorId);
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
						catch (Exception x)
						{
						}
					}
				});
		}
		catch (Exception x)
		{
			x.printStackTrace ();
		}
	}

	/**
	 * Write the attributes to an output stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		try
		{
			outputStream = stream;

			Properties props = new Properties();

			props.setProperty ("select", "select * from Measurement");
			props.put ("handler", this);

			CommandTools.performSimple ("persist.Select", props);
		}
		catch (Exception x)
		{
			x.printStackTrace ();
		}
	}

	public Object handle (ResultSet rs)
		throws SQLException
	{
		int i = 0;

		try
		{
			rs.last ();

			int numOfRows = rs.getRow ();

			rs.first ();
			outputStream.writeInt (numOfRows);

			Random ran = new Random();

			timestamp = rs.getTimestamp ("at");
			value = rs.getDouble ("value");
			sensorId = rs.getLong ("sensorId");
			stationId = rs.getLong ("stationId");

			outputStream.writeLong (timestamp.getTime ());
			outputStream.writeDouble (value);
			outputStream.writeLong (sensorId);
			outputStream.writeLong (stationId);

			Timestamp timestamp = null;
			double value;
			long sensorId;
			long stationId;

			while (rs.next ())
			{
				i++;

				timestamp = rs.getTimestamp ("at");
				value = rs.getDouble ("value");
				sensorId = rs.getLong ("sensorId");
				stationId = rs.getLong ("stationId");

				outputStream.writeLong (timestamp.getTime ());
				outputStream.writeDouble (value);
				outputStream.writeLong (sensorId);
				outputStream.writeLong (stationId);
			}
		}
		catch (Exception weCanDoNothing)
		{
			weCanDoNothing.printStackTrace ();
		}

		return null;
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
	}
}