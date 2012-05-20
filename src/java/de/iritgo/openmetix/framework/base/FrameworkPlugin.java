/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.appcontext.ServerAppContext;
import de.iritgo.openmetix.framework.console.ConsoleCommand;
import de.iritgo.openmetix.framework.console.ConsoleCommandRegistry;
import de.iritgo.openmetix.framework.console.ConsoleManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * FrameworkPlugin.
 *
 * @version $Id: FrameworkPlugin.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public abstract class FrameworkPlugin
	extends Plugin
{
	/** The console command registry. */
	private ConsoleCommandRegistry consoleCommandRegistry;

	/** All console commands. */
	private List consoleCommands;

	/** All data objects. */
	private List dataObjects;

	/**
	 * Create a new FrameworkPlugin.
	 */
	public FrameworkPlugin ()
	{
		super();

		consoleCommands = new LinkedList();
		dataObjects = new LinkedList();

		consoleCommandRegistry =
			((ConsoleManager) engine.getManagerRegistry ().getManager ("console")).getConsoleCommandRegistry ();
	}

	/**
	 * Initialize the plugin.
	 *
	 * @param engine The engine.
	 */
	public void init (Engine engine)
	{
		if (AppContext.instance ().getClient ())
		{
			setMode (Plugin.CLIENT);
		}

		if (ServerAppContext.serverInstance ().getServer ())
		{
			setMode (Plugin.SERVER);
		}

		registerDataObjects ();

		super.init (engine);

		registerConsoleCommands ();
	}

	/**
	 * Unload the plugin.
	 *
	 * @param engine The engine.
	 */
	public void unloadPlugin (Engine engine)
	{
		super.unloadPlugin (engine);

		for (Iterator i = consoleCommands.iterator (); i.hasNext ();)
		{
			consoleCommandRegistry.remove ((ConsoleCommand) i.next ());
		}

		for (Iterator i = dataObjects.iterator (); i.hasNext ();)
		{
			iObjectFactory.remove ((IObject) i.next ());
		}
	}

	/**
	 * Register a console command.
	 */
	protected void registerConsoleCommand (ConsoleCommand consoleCommand)
	{
		consoleCommandRegistry.add (consoleCommand);
		consoleCommands.add (consoleCommand);
	}

	/**
	 * Register a console command.
	 */
	protected void registerConsoleCommand (int mode, ConsoleCommand consoleCommand)
	{
		if (getMode () == mode)
		{
			registerConsoleCommand (consoleCommand);
		}
	}

	/**
	 * Register a data object.
	 *
	 * @param dataObject The data object to register.
	 */
	protected void registerDataObject (DataObject dataObject)
	{
		iObjectFactory.register (dataObject);
		dataObjects.add (dataObject);
	}

	/**
	 * Register a data object.
	 *
	 * @param mode Specifies wether this is a client or server manager.
	 * @param dataObject The data object to register.
	 */
	protected void registerDataObject (int mode, DataObject dataObject)
	{
		if (getMode () == mode)
		{
			registerDataObject (dataObject);
		}
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
	}

	/**
	 * Register all console commands in this method.
	 */
	protected void registerConsoleCommands ()
	{
	}
}