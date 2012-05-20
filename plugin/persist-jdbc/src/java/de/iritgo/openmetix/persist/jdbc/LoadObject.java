/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.iobject.IObject;
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
 * This command loads a single object of a specific type and unique id.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>type</td><td>String</td><td>The type id of the object to load.</td></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the object to load.</td></tr>
 * </table>
 *
 * @version $Id: LoadObject.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class LoadObject
	extends Command
{
	/**
	 * Create a new <code>LoadObject</code> command.
	 */
	public LoadObject ()
	{
		super("persist.LoadObject");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("id") == null)
		{
			Log.logError ("persist", "LoadObject", "Missing unique id for the object to load");

			return;
		}

		long uniqueId = ((Long) properties.get ("id")).longValue ();

		if (Engine.instance ().getBaseRegistry ().get (uniqueId) != null)
		{
			Log.logVerbose (
				"persist", "LoadObject", "Object with id " + uniqueId +
				" already loaded, skipping");
		}

		final String typeId = (String) properties.get ("type");

		if (typeId == null)
		{
			Log.logError (
				"persist", "LoadObject", "The type of the object to load wasn't specified");

			return;
		}

		IObject object = null;

		try
		{
			object = Engine.instance ().getIObjectFactory ().newInstance (typeId);
		}
		catch (NoSuchIObjectException ignored)
		{
			Log.logError (
				"persist", "LoadObject", "Attemting to load object of unknown type '" + typeId +
				"'");

			return;
		}

		if (! DataObject.class.isInstance (object))
		{
			Log.logError (
				"persist", "LoadObject", "Attemting to load object that is not persitable");

			return;
		}

		object =
			load (
				((JDBCManager) Engine.getManager ("persist.JDBCManager")).getDefaultDataSource (),
				typeId, uniqueId);

		if (object != null)
		{
			Engine.instance ().getBaseRegistry ().add ((BaseObject) object);
		}

		return;
	}

	/**
	 * Load an object.
	 *
	 * @param dataSource The data source to load from.
	 * @param typeId The type of the object to load.
	 * @param uniqueId The unique id of the object to load.
	 * @return The loaded object (already registered with the base registry).
	 */
	private DataObject load (final DataSource dataSource, final String typeId, long uniqueId)
	{
		DataObject object = null;

		try
		{
			QueryRunner query = new QueryRunner(dataSource);

			object =
				(DataObject) query.query (
					"select * from " + typeId + " where id=" + uniqueId,
					new ResultSetHandler()
					{
						public Object handle (ResultSet rs)
							throws SQLException
						{
							ResultSetMetaData meta = rs.getMetaData ();

							if (rs.next ())
							{
								try
								{
									DataObject object =
										(DataObject) Engine.instance ().getIObjectFactory ()
														   .newInstance (typeId);

									object.setUniqueId (rs.getLong ("id"));

									for (
										Iterator i =
											object.getAttributes ().entrySet ().iterator ();
										i.hasNext ();)
									{
										Map.Entry attribute = (Map.Entry) i.next ();

										if (attribute.getValue () instanceof IObjectList)
										{
											loadList (
												dataSource, object,
												object.getIObjectListAttribute (
													(String) attribute.getKey ()));
										}
										else
										{
											object.putAttribute (
												(String) attribute.getKey (),
												rs.getObject ((String) attribute.getKey ()));
										}
									}

									return object;
								}
								catch (NoSuchIObjectException ignored)
								{
									Log.logError (
										"persist", "LoadObject", "NoSuchIObjectException");
								}
							}
							else
							{
							}

							return null;
						}
					});

			if (object != null)
			{
				Log.logVerbose (
					"persist", "LoadObject", "Successfully loaded object " + typeId + ":" +
					uniqueId);
			}
			else
			{
				Log.logError (
					"persist", "LoadObject", "Unable to find object " + typeId + ":" + uniqueId);
			}
		}
		catch (SQLException x)
		{
			Log.logError (
				"persist", "LoadObject",
				"Error while loading the object " + typeId + ":" + uniqueId + ": " + x);
		}

		return object;
	}

	/**
	 * Load a list of iobjects.
	 *
	 * @param dataSource The data source to load from.
	 * @param owner The owner of the list.
	 * @param list the object list to load.
	 */
	public void loadList (final DataSource dataSource, DataObject owner, final IObjectList list)
	{
		try
		{
			QueryRunner query = new QueryRunner(dataSource);

			query.query (
				"select elemType, elemId from IritgoObjectList where id=? and attribute=?",
				new Object[]
				{
					new Long(owner.getUniqueId ()),
					list.getAttributeName ()
				},
				new ResultSetHandler()
				{
					public Object handle (ResultSet rs)
						throws SQLException
					{
						while (rs.next ())
						{
							DataObject element =
								load (dataSource, rs.getString ("elemType"), rs.getLong ("elemId"));

							if (element != null)
							{
								list.add (element);
							}
						}

						return null;
					}
				});
		}
		catch (SQLException x)
		{
			Log.logError (
				"persist", "LoadObject",
				"Error while loading the object list " + list.getOwner ().getTypeId () + "." +
				list.getAttributeName () + ": " + x);
		}
	}
}