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
import java.util.Iterator;
import java.util.Map;


/**
 * This command stores the specified user to the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the user to store.</td></tr>
 * </table>
 *
 * @version $Id: StoreUser.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class StoreUser
	extends Command
{
	/**
	 * Create a new <code>StoreUser</code> command.
	 */
	public StoreUser ()
	{
		super("persist.StoreUser");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("id") == null)
		{
			Log.logError ("persist", "StoreUser", "Missing unique id for the user to store");

			return;
		}

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		long userId = ((Long) properties.get ("id")).longValue ();
		User user = userRegistry.getUser (userId);

		if (user == null)
		{
			Log.logError ("persist", "StoreUser", "Unable to find user with id " + userId);

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

			String userSql =
				"insert into IritgoUser (id, name, password, email) " + " values (?, ?, ?, ?)";

			stmt = connection.prepareStatement (userSql);
			stmt.setLong (1, userId);
			stmt.setString (2, user.getName ());
			stmt.setString (3, user.getPassword ());
			stmt.setString (4, user.getEmail ());
			stmt.execute ();

			stmt =
				connection.prepareStatement (
					"insert into IritgoNamedObjects (userId, name, id) values (?, ?, ?)");

			for (Iterator i = user.getNamedObjectIterator (); i.hasNext ();)
			{
				Map.Entry namedObject = (Map.Entry) i.next ();

				stmt.setLong (1, userId);
				stmt.setString (2, (String) namedObject.getKey ());
				stmt.setLong (3, ((Long) namedObject.getValue ()).longValue ());
				stmt.execute ();
			}

			Log.logVerbose ("persist", "StoreUser", "INSERT USER " + userId + " |" + userSql + "|");
		}
		catch (SQLException x)
		{
			Log.logError (
				"persist", "StoreUser", "Error while storing user with id " + userId + ": " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}
}