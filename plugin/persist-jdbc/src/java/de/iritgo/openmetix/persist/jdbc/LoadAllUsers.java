/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.UserRegistry;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * This command loads *ALL* users currently stored in the database.
 * Later this command should be replaced by a more intelligent method,
 * that loads a user on demand when he has logged into the server.
 *
 * @version $Id: LoadAllUsers.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class LoadAllUsers
	extends Command
{
	/**
	 * Create a new <code>LoadAllUsers</code> command.
	 */
	public LoadAllUsers ()
	{
		super("persist.LoadAllUsers");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		final UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		try
		{
			QueryRunner query = new QueryRunner(dataSource);
			List userIds = (List) query.query ("select id from IritgoUser", new ArrayListHandler());

			for (Iterator i = userIds.iterator (); i.hasNext ();)
			{
				Long userId = (Long) ((Object[]) i.next ())[0];

				Properties props = new Properties();

				props.put ("id", userId);
				CommandTools.performSimple ("persist.LoadUser", props);
			}

			Log.logVerbose (
				"persist", "LoadAllUsers", "Successfully loaded " + userIds.size () + " users");
		}
		catch (Exception x)
		{
			Log.logError ("persist", "LoadAllUsers", "Error while loading the users: " + x);
		}
	}
}