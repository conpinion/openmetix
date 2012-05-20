/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.IObjectListEvent;
import de.iritgo.openmetix.core.iobject.IObjectListListener;
import de.iritgo.openmetix.core.iobject.IObjectRequestEvent;
import de.iritgo.openmetix.core.iobject.IObjectRequestListener;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.core.uid.DefaultIDGenerator;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.base.IObjectModifiedListener;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The <code>JDBCManager</code> persists data objects to relational database
 * through the use of the JDBC api.
 *
 * @version $Id: JDBCManager.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class JDBCManager
	extends BaseObject
	implements Manager, IObjectListListener, IObjectModifiedListener, IObjectRequestListener
{
	/** Configured data sources. */
	private Map dataSources;

	/** The default data source. */
	private DataSource defaultDataSource;

	/**
	 * Create a new <code>JDBCManager</code>
	 */
	public JDBCManager ()
	{
		super("persist.JDBCManager");
	}

	/**
	 * Initialize the <code>JDBCManager</code>.
	 *
	 * This method creates the data sources specified in the server
	 * configuration.
	 */
	public void init ()
	{
		dataSources = new HashMap();

		try
		{
			Configuration config = Engine.instance ().getConfiguration ();

			Configuration[] dsConfigs = config.getChild ("datasources").getChildren ("datasource");

			for (int i = 0; i < dsConfigs.length; ++i)
			{
				BasicDataSource ds = new BasicDataSource();

				ds.setDriverClassName (dsConfigs[i].getAttribute ("driverClass"));
				ds.setUsername (dsConfigs[i].getAttribute ("user"));
				ds.setPassword (dsConfigs[i].getAttribute ("password"));
				ds.setUrl (dsConfigs[i].getAttribute ("url"));

				dataSources.put (dsConfigs[i].getAttribute ("id"), ds);

				if (defaultDataSource == null)
				{
					defaultDataSource = ds;
				}

				Log.logInfo (
					"persist", "JDBCManager",
					"Created datasource '" + dsConfigs[i].getAttribute ("id") + "'" + " for URL '" +
					dsConfigs[i].getAttribute ("url") + "'");
			}

			JDBCIDGenerator persistentIdGenerator = new JDBCIDGenerator(2, 2, 1000);

			persistentIdGenerator.load ();
			Engine.instance ().installPersistentIDGenerator (persistentIdGenerator);

			DefaultIDGenerator transientIdGenerator = new DefaultIDGenerator((long) 1, (long) 2);

			Engine.instance ().installTransientIDGenerator (transientIdGenerator);
		}
		catch (Exception x)
		{
			Log.logError ("persist", "JDBCManager", "Error while creating the datasources: " + x);
		}

		Engine.instance ().getEventRegistry ().addListener ("objectcreated", this);
		Engine.instance ().getEventRegistry ().addListener ("objectmodified", this);
		Engine.instance ().getEventRegistry ().addListener ("objectrequested", this);
		Engine.instance ().getEventRegistry ().addListener ("objectremoved", this);
	}

	/**
	 * Called when the plugin is to be unloaded.
	 *
	 * This method closes all active data sources.
	 */
	public void unload ()
	{
		for (Iterator i = dataSources.entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry entry = (Map.Entry) i.next ();

			try
			{
				((BasicDataSource) entry.getValue ()).close ();

				Log.logInfo (
					"persist", "JDBCManager", "Closed datasource '" + entry.getKey () + "'");
			}
			catch (Exception x)
			{
				Log.logError (
					"persist", "JDBCManager",
					"Error during closing the datasource '" + entry.getKey () + "': " + x);
			}
		}
	}

	/**
	 * Get the default data source.
	 *
	 * @return The default data source.
	 */
	public DataSource getDefaultDataSource ()
	{
		return defaultDataSource;
	}

	/**
	 * Called when a data object was added to or removed from an
	 * object list.
	 *
	 * @param event The object list event.
	 */
	public void iObjectListEvent (IObjectListEvent event)
	{
		if (event.getType () == IObjectListEvent.ADD)
		{
			insert (
				(DataObject) event.getObject (), (DataObject) event.getOwnerObject (),
				event.getListAttribute ());
		}

		if (event.getType () == IObjectListEvent.REMOVE)
		{
			delete (
				(DataObject) event.getObject (), (DataObject) event.getOwnerObject (),
				event.getListAttribute ());
		}
	}

	/**
	 * Called when an iritgo object was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		update ((DataObject) event.getModifiedObject ());
	}

	/**
	 * Called when an iritgo object is requested.
	 *
	 * @param event The reuqest event.
	 */
	public void iObjectRequestEvent (IObjectRequestEvent event)
	{
	}

	/**
	 * Insert a new data object into the database.
	 *
	 * @param object The data object to create.
	 * @param owner The data object owning this one.
	 * @param listAttribute The name of the list attribute to which the new
	 *   object belongs.
	 */
	private void insert (DataObject object, DataObject owner, String listAttribute)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = defaultDataSource.getConnection ();

			StringBuffer sqlFields = new StringBuffer("id");
			StringBuffer sqlValues = new StringBuffer("?");

			for (Iterator i = object.getAttributes ().entrySet ().iterator (); i.hasNext ();)
			{
				Map.Entry attribute = (Map.Entry) i.next ();

				if (attribute.getValue () instanceof IObjectList)
				{
					continue;
				}

				sqlFields.append (", " + (String) attribute.getKey ());
				sqlValues.append (", ?");
			}

			String sql =
				"insert into " + object.getTypeId () + " (" + sqlFields.toString () + ") values (" +
				sqlValues.toString () + ")";

			stmt = connection.prepareStatement (sql);
			putAttributesToStatement (object, stmt);
			stmt.execute ();

			Log.logVerbose (
				"persist", "JDBCManager",
				"CREATED " + object.getTypeId () + ":" + object.getUniqueId () + " |" + sql + "|");

			stmt.close ();

			if (owner != null)
			{
				sql = "insert into IritgoObjectList (type, id, attribute, elemType, elemId) values (?, ?, ?, ?, ?)";

				stmt = connection.prepareStatement (sql);
				stmt.setString (1, owner.getTypeId ());
				stmt.setLong (2, owner.getUniqueId ());
				stmt.setString (3, listAttribute);
				stmt.setString (4, object.getTypeId ());
				stmt.setLong (5, object.getUniqueId ());
				stmt.execute ();

				Log.logVerbose (
					"persist", "JDBCManager",
					"CREATED REFRENCE " + owner.getTypeId () + ":" + owner.getUniqueId () + " => " +
					object.getTypeId () + ":" + object.getUniqueId () + " |" + sql + "|");
			}
		}
		catch (Exception x)
		{
			Log.logError (
				"persist", "JDBCManager", "Error while creating new database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}

	/**
	 * Delete a data object from the database.
	 *
	 * @param object The data object to create.
	 * @param owner The data object owning this one.
	 * @param listAttribute The name of the list attribute to which the new
	 *   object belongs.
	 */
	private void delete (DataObject object, DataObject owner, String listAttribute)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = defaultDataSource.getConnection ();

			StringBuffer sqlFields = new StringBuffer("id");
			StringBuffer sqlValues = new StringBuffer("?");

			String sql =
				"delete from IritgoObjectList where type='" + owner.getTypeId () + "'" +
				" AND id=" + owner.getUniqueId () + " AND attribute='" + listAttribute + "'" +
				" AND elemType='" + object.getTypeId () + "'" + " AND elemId=" +
				object.getUniqueId ();

			stmt = connection.prepareStatement (sql);
			stmt.execute ();

			Log.logVerbose (
				"persist", "JDBCManager",
				"Removed " + object.getTypeId () + ":" + object.getUniqueId () + " |" + sql + "|");

			stmt.close ();
		}
		catch (Exception x)
		{
			Log.logError ("persist", "JDBCManager", "Error while removed a database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}

	/**
	 * Store data object changes to the database.
	 *
	 * @param object The data object to update.
	 */
	private void update (DataObject object)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = defaultDataSource.getConnection ();

			StringBuffer sqlAssigns = new StringBuffer("id=?");

			for (Iterator i = object.getAttributes ().entrySet ().iterator (); i.hasNext ();)
			{
				Map.Entry attribute = (Map.Entry) i.next ();

				if (attribute.getValue () instanceof IObjectList)
				{
					continue;
				}

				sqlAssigns.append (", " + (String) attribute.getKey () + "=?");
			}

			String sql =
				"update " + object.getTypeId () + " set " + sqlAssigns.toString () + " where id=" +
				object.getUniqueId ();

			stmt = connection.prepareStatement (sql);
			putAttributesToStatement (object, stmt);
			stmt.execute ();

			Log.logVerbose (
				"persist", "JDBCManager",
				"UPDATE " + object.getTypeId () + ":" + object.getUniqueId () + " |" + sql + "|");
		}
		catch (Exception x)
		{
			Log.logError (
				"persist", "JDBCManager", "Error while creating new database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}

	/**
	 * Transfer all attribute values of a data object to the specified
	 * prepared statement.
	 *
	 * @param object The data object to transfer.
	 * @param stmt The prepared statement.
	 * @throws SQLException If an attribute could not be set.
	 */
	private void putAttributesToStatement (DataObject object, PreparedStatement stmt)
		throws SQLException
	{
		stmt.setLong (1, object.getUniqueId ());

		int pos = 2;

		for (Iterator i = object.getAttributes ().entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry attribute = (Map.Entry) i.next ();

			if (attribute.getValue () instanceof IObjectList)
			{
				continue;
			}

			stmt.setObject (pos++, attribute.getValue ());
		}
	}
}