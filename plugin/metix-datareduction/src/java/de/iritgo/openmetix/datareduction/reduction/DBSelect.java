/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.reduction;


import de.iritgo.openmetix.framework.command.CommandTools;
import org.apache.commons.dbutils.ResultSetHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * This action is executed on the server. It receives the historical data query.
 *
 * @version $Id: DBSelect.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DBSelect
	implements ResultSetHandler
{
	private Timestamp startTime;
	private Timestamp stopTime;
	private long stationID;
	private long sensorID;
	private long currentTime;
	private double currentValue;
	private long currentStationID;
	private long currentSensorID;
	private List output;

	public DBSelect ()
	{
		output = new LinkedList();
	}

	public List selectMeasurements (Timestamp start, Timestamp stop, long stationID, long sensorID)
	{
		this.startTime = start;
		this.stopTime = stop;
		this.stationID = stationID;
		this.sensorID = sensorID;

		Properties props = new Properties();
		Object[] params = new Object[4];

		params[0] = startTime;
		params[1] = stopTime;
		params[2] = new Long(stationID);
		params[3] = new Long(sensorID);

		String select;

		select =
			"select * " + " from Measurement where at<=? and at>=? and stationid=? and sensorid=?";

		props = new Properties();
		props.setProperty ("select", select);
		props.put ("handler", this);
		props.put ("params", params);

		CommandTools.performSimple ("persist.Select", props);

		return output;
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
			Timestamp at = rs.getTimestamp ("at");
			double value = rs.getDouble ("value");
			long stationID = rs.getLong ("stationID");
			long sensorID = rs.getLong ("sensorID");

			if (at != null && at.getTime () != 0)
			{
				currentTime = at.getTime ();
				currentValue = value;
				currentStationID = stationID;
				currentSensorID = sensorID;
				output.add (new Double(currentValue));
			}
		}

		return null;
	}
}