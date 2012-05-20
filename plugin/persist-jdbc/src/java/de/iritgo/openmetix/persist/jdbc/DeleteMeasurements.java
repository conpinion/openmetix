/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.logger.Log;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * This command deletes the specified measurements from the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the user to delete.</td></tr>
 * </table>
 *
 * @version $Id: DeleteMeasurements.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class DeleteMeasurements
	extends Command
{
	/**
	 * Create a new <code>DeleteUser</code> command.
	 */
	public DeleteMeasurements ()
	{
		super("persist.DeleteMeasurements");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("stationID") == null)
		{
			Log.logError (
				"persist", "DeleteMeasurements", "Missing data for the measurements to delete");

			return;
		}

		Timestamp startTime = (Timestamp) properties.get ("startTime");
		Timestamp stopTime = (Timestamp) properties.get ("stopTime");
		long stationID = ((Long) properties.get ("stationID")).longValue ();
		long sensorID = ((Long) properties.get ("sensorID")).longValue ();

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = dataSource.getConnection ();

			stmt =
				connection.prepareStatement (
					"delete from Measurement where at<=? and at>=? and stationId=? and sensorId=?");
			stmt.setTimestamp (1, startTime);
			stmt.setTimestamp (2, stopTime);
			stmt.setLong (3, stationID);
			stmt.setLong (4, sensorID);

			stmt.execute ();
			stmt.close ();

			Log.logVerbose ("persist", "DeleteMeasurements", "DELETE Measurements ");
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "DeleteMeasurements", "Error while deleting measurements" + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}
}