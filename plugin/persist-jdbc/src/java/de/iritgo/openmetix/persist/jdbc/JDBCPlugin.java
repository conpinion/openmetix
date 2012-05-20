/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.jdbc;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;


/**
 * The persist-jdbc plugin implements persistent methods to be used with
 * a relational database accessed through JDBC.
 *
 * @version $Id: JDBCPlugin.java,v 1.1 2005/04/24 18:10:41 grappendorf Exp $
 */
public class JDBCPlugin
	extends FrameworkPlugin
{
	protected void registerDataObjects ()
	{
	}

	protected void registerActions ()
	{
	}

	protected void registerGUIPanes ()
	{
	}

	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new JDBCManager());
	}

	protected void registerCommands ()
	{
		registerCommand (new LoadUser());
		registerCommand (new LoadAllUsers());
		registerCommand (new LoadObject());
		registerCommand (new LoadAllObjects());
		registerCommand (new StoreUser());
		registerCommand (new StoreObject());
		registerCommand (new DeleteUser());
		registerCommand (new DeleteMeasurements());
		registerCommand (new Insert());
		registerCommand (new Select());
		registerCommand (new Update());
		registerCommand (new DeleteMeasurements());
		registerCommand (new GetDatabaseVersion());
	}

	protected void registerConsoleCommands ()
	{
	}
}