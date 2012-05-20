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
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * This command loads a single user with a specific unique id.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the user load.</td></tr>
 * </table>
 *
 * @version $Id: LoadUser.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class LoadUser
	extends Command
{
	/**
	 * Create a new <code>LoadUser</code> command.
	 */
	public LoadUser ()
	{
		super("persist.LoadUser");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("id") == null)
		{
			Log.logError ("persist", "LoadUser", "Missing unique id for the user to load");

			return;
		}

		long uniqueId = ((Long) properties.get ("id")).longValue ();

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		try
		{
			QueryRunner query = new QueryRunner(dataSource);
			final User user =
				(User) query.query (
					"select * from IritgoUser where id=?", new Long(uniqueId),
					new ResultSetHandler()
					{
						public Object handle (ResultSet rs)
							throws SQLException
						{
							if (rs.next ())
							{
								User user =
									new User(
										rs.getString ("name"), rs.getString ("email"),
										rs.getInt ("id"), rs.getString ("password"), 0);

								Server.instance ().getUserRegistry ().addUser (user);
								Engine.instance ().getBaseRegistry ().add (user);

								return user;
							}
							else
							{
								return null;
							}
						}
					});

			if (user == null)
			{
				Log.logError ("persist", "LoadUser", "Unable to find user with id " + uniqueId);

				return;
			}

			query.query (
				"select * from IritgoNamedObjects where userId=?", new Long(uniqueId),
				new ResultSetHandler()
				{
					public Object handle (ResultSet rs)
						throws SQLException
					{
						while (rs.next ())
						{
							user.putNamedIritgoObject (rs.getString ("name"), rs.getLong ("id"));
						}

						return null;
					}
				});

			Log.logVerbose (
				"persist", "LoadUser",
				"Successfully loaded user " + user.getName () + ":" + user.getUniqueId ());
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "LoadUser", "Error while loading the users: " + x);
		}
	}
}