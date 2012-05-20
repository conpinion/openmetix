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
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


/**
 * Get the name and version of the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: GetDatabaseVersion.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class GetDatabaseVersion
	extends Command
{
	/** The chached version. */
	String version;

	/**
	 * Create a new <code>GetDatabaseVersion</code> command.
	 */
	public GetDatabaseVersion ()
	{
		super("persist.GetDatabaseVersion");
	}

	/**
	 * Perform the command.
	 *
	 * @return The database name and version.
	 */
	public Object performWithResult ()
	{
		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		Connection connection = null;

		try
		{
			if (version == null)
			{
				connection = dataSource.getConnection ();

				DatabaseMetaData meta = connection.getMetaData ();

				version = meta.getDatabaseProductName () + " " + meta.getDatabaseProductVersion ();
			}

			return version;
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "Insert", "Unable to get database meta data: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (connection);
		}

		return null;
	}
}