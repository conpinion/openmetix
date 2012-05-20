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
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class Update
	extends Command
{
	/**
	 * Create a new <code>Update</code> command.
	 */
	public Update ()
	{
		super("persist.Update");
	}

	public void perform ()
	{
		if (properties.get ("handler") == null)
		{
			Log.logError ("persist", "Update", "Missing result set handler");

			return;
		}

		PreparedStatement stmt = null;
		Connection connection = null;

		ResultSetHandler resultSetHandler = (ResultSetHandler) properties.get ("handler");

		if (properties.get ("update") == null)
		{
			Log.logError ("persist", "Update", "Missing update statement");

			return;
		}

		String update = (String) properties.getProperty ("update");

		Object[] params = (Object[]) properties.get ("params");

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		try
		{
			connection = dataSource.getConnection ();

			stmt = connection.prepareStatement (update);
			stmt.execute ();
			stmt.close ();
		}
		catch (Exception x)
		{
			Log.logError ("persist", "Update", "Error while executing sql |" + update + "|: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}
}