/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.logger.Log;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;


/**
 * This command executes an sql SELECT statement.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>handler</td><td>ResultSetHandler</td><td>The result set handler.</td></tr>
 *   <tr><td>select</td><td>String</td><td>The select statement to execute.</td></tr>
 *   <tr><td>params</td><td>Object[]</td><td>The select statement to execute.</td></tr>
 * </table>
 *
 * @version $Id: Select.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class Select
	extends Command
{
	/**
	 * Create a new <code>LoadAllObjects</code> command.
	 */
	public Select ()
	{
		super("persist.Select");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("handler") == null)
		{
			Log.logError ("persist", "Select", "Missing result set handler");

			return;
		}

		ResultSetHandler resultSetHandler = (ResultSetHandler) properties.get ("handler");

		if (properties.get ("select") == null)
		{
			Log.logError ("persist", "Select", "Missing select statement");

			return;
		}

		String select = (String) properties.getProperty ("select");

		Object[] params = (Object[]) properties.get ("params");

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		try
		{
			QueryRunner query = new QueryRunner(dataSource);

			Object res;

			if (params != null)
			{
				res = query.query (select, params, resultSetHandler);
			}
			else
			{
				res = query.query (select, resultSetHandler);
			}

			Log.logVerbose ("persist", "Select", "SELECT |" + select + "|");
		}
		catch (Exception x)
		{
			Log.logError ("persist", "Select", "Error while executing sql |" + select + "|: " + x);
		}
	}
}