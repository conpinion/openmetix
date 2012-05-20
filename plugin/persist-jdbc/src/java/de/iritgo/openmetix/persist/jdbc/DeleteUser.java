/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * This command deletes the specified user to the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the user to delete.</td></tr>
 * </table>
 *
 * @version $Id: DeleteUser.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class DeleteUser
	extends Command
{
	/**
	 * Create a new <code>DeleteUser</code> command.
	 */
	public DeleteUser ()
	{
		super("persist.DeleteUser");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("id") == null)
		{
			Log.logError ("persist", "DeleteUser", "Missing unique id for the user to delete");

			return;
		}

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		long userId = ((Long) properties.get ("id")).longValue ();
		User user = userRegistry.getUser (userId);

		if (user == null)
		{
			Log.logError ("persist", "DeleteUser", "Unable to find user with id " + userId);

			return;
		}

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = dataSource.getConnection ();

			stmt = connection.prepareStatement ("delete from IritgoUser where id=?");
			stmt.setLong (1, userId);
			stmt.execute ();
			stmt.close ();

			stmt = connection.prepareStatement ("delete from IritgoNamedObjects where userId=?");
			stmt.setLong (1, userId);
			stmt.execute ();
			stmt.close ();

			Log.logVerbose ("persist", "DeleteUser", "DELETE USER " + userId);
		}
		catch (SQLException x)
		{
			Log.logError (
				"persist", "DeleteUser", "Error while storing user with id " + userId + ": " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}
}