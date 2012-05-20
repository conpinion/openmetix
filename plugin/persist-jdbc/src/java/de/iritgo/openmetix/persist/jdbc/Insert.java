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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.sql.DataSource;


/**
 * This command implements a direct sql INSERT into the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>table</td><td>String</td><td>Name of the table to insert to.</td></tr>
 *   <tr><td>column.*</td><td>String</td><td>Column value.</td></tr>
 *   <tr><td>[size]</td><td>String</td><td>If set and > 0 this will be a batch insert.</td></tr>
 * </table>
 *
 * @version $Id: Insert.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class Insert
	extends Command
{
	/**
	 * Create a new <code>Insert</code> command.
	 */
	public Insert ()
	{
		super("persist.Insert");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("table") == null)
		{
			Log.logError ("persist", "Insert", "Missing table name");

			return;
		}

		int size = 0;

		if (properties.get ("size") != null)
		{
			size = ((Integer) properties.get ("size")).intValue ();
		}

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		Connection connection = null;
		PreparedStatement stmt = null;

		ArrayList columns = new ArrayList(8);
		ArrayList columnValues = new ArrayList(8);

		for (Iterator i = properties.entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry entry = (Map.Entry) i.next ();

			if (((String) entry.getKey ()).indexOf ("column.") == 0)
			{
				columns.add (((String) entry.getKey ()).substring (7));
				columnValues.add (entry.getValue ());
			}
		}

		int numColumns = columns.size ();

		StringBuffer sqlColumns = new StringBuffer("(id");

		for (int i = 0; i < numColumns; ++i)
		{
			sqlColumns.append (", " + (String) columns.get (i));
		}

		sqlColumns.append (")");

		StringBuffer sqlValues = new StringBuffer("(?");

		for (int i = 0; i < numColumns; ++i)
		{
			sqlValues.append (", ?");
		}

		sqlValues.append (")");

		String sql =
			"insert into " + properties.getProperty ("table") + " " + sqlColumns.toString () +
			" values " + sqlValues.toString ();

		try
		{
			connection = dataSource.getConnection ();

			stmt = connection.prepareStatement (sql);

			if (size <= 0)
			{
				stmt.setLong (1, Engine.instance ().getPersistentIDGenerator ().createId ());

				for (int col = 0; col < numColumns; ++col)
				{
					stmt.setObject (col + 2, columnValues.get (col));
				}

				stmt.execute ();
			}
			else
			{
				for (int row = 0; row < size; ++row)
				{
					stmt.setLong (1, Engine.instance ().getPersistentIDGenerator ().createId ());

					for (int col = 0; col < numColumns; ++col)
					{
						stmt.setObject (col + 2, ((Object[]) columnValues.get (col))[row]);
					}

					stmt.execute ();
				}
			}

			stmt.close ();

			Log.logVerbose ("persist", "Insert", "INSERT |" + sql + "|");
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "Insert", "Error while executing sql |" + sql + "|: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}
}