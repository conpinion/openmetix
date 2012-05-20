/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseRegistry;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectFactory;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.base.DataObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;


/**
 * This command loads *ALL* objects of a specific type.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Description</th></tr>
 *   <tr><td>type</td><td>The type id of the objects to load.</td></tr>
 * </table>
 *
 * @version $Id: LoadAllObjects.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class LoadAllObjects
	extends Command
{
	/**
	 * Create a new <code>LoadAllObjects</code> command.
	 */
	public LoadAllObjects ()
	{
		super("persist.LoadAllObjects");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.getProperty ("type") == null)
		{
			Log.logError (
				"persist", "LoadObjects", "The type of the objects to load wasn't specified");

			return;
		}

		final String type = ((String) properties.getProperty ("type"));

		final IObjectFactory factory = Engine.instance ().getIObjectFactory ();

		IObject sample = null;

		try
		{
			sample = factory.newInstance (type);
		}
		catch (NoSuchIObjectException ignored)
		{
			Log.logError (
				"persist", "LoadObjects", "Attemting to load objects of unknown type '" + type +
				"'");

			return;
		}

		if (! DataObject.class.isInstance (sample))
		{
			Log.logError (
				"persist", "LoadObjects", "Attemting to load objects that are not persitable");

			return;
		}

		final BaseRegistry registry = Engine.instance ().getBaseRegistry ();

		JDBCManager jdbcManager = (JDBCManager) Engine.getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		try
		{
			QueryRunner query = new QueryRunner(dataSource);

			ResultSetHandler resultSetHandler =
				properties.get ("resultSetHandle") != null
				? (ResultSetHandler) properties.get ("resultSetHandler")
				: new ResultSetHandler()
					{
						public Object handle (ResultSet rs)
							throws SQLException
						{
							ResultSetMetaData meta = rs.getMetaData ();

							int numObjects = 0;

							while (rs.next ())
							{
								try
								{
									DataObject object = (DataObject) factory.newInstance (type);

									object.setUniqueId (rs.getLong ("id"));

									for (
										Iterator i =
											object.getAttributes ().entrySet ().iterator ();
										i.hasNext ();)
									{
										Map.Entry attribute = (Map.Entry) i.next ();

										if (attribute.getValue () instanceof IObjectList)
										{
										}
										else
										{
											object.putAttribute (
												(String) attribute.getKey (),
												rs.getObject ((String) attribute.getKey ()));
										}
									}

									registry.add (object);
									++numObjects;
								}
								catch (NoSuchIObjectException ignored)
								{
								}
							}

							return new Integer(numObjects);
						}
					};

			Object numObjects = query.query ("select * from " + type, resultSetHandler);

			Log.logVerbose (
				"persist", "LoadObjects",
				"Successfully loaded " + numObjects + " objects of type '" + type + "'");
		}
		catch (Exception x)
		{
			Log.logError (
				"persist", "LoadObjects", "Error while loading objects of type '" + type + "': " +
				x);
		}
	}
}