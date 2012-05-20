/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.lambrecht;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverDescriptor;


/**
 * This plugin contains interface components for Lambrecht data loggers.
 *
 * @version $Id: InterfacingLambrechtPlugin.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class InterfacingLambrechtPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new LambrechtOutputEditor());
	}

	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new InterfacingLambrechtManager());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
	}

	/**
	 * Initialize the plugin.
	 *
	 * @param engine The system engine.
	 */
	public void init (Engine engine)
	{
		GagingSystemDriverDescriptor.add (
			new GagingSystemDriverDescriptor(
				"LambrechtSynmet", "metix-interfacing-lambrecht.lambrechtDriver",
				"de.iritgo.openmetix.interfacing.lambrecht.LambrechtSynmetDriver",
				"de.iritgo.openmetix.interfacing.lambrecht.LambrechtSynmetDriverEditor",
				"LambrechtOutputEditor", InterfacingLambrechtPlugin.class.getClassLoader ()));

		super.init (engine);
	}
}