/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.thies;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverDescriptor;


/**
 * This plugin contains interface components for Thies data loggers.
 *
 * @version $Id: InterfacingThiesPlugin.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class InterfacingThiesPlugin
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
	}

	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new InterfacingThiesManager());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
	}

	/**
	 * Initiaize the plugin.
	 *
	 * @param engine The system engine.
	 */
	public void init (Engine engine)
	{
		GagingSystemDriverDescriptor.add (
			new GagingSystemDriverDescriptor(
				"Thies", "metix-interfacing-thies.thiesDriver",
				"de.iritgo.openmetix.interfacing.thies.ThiesDriver",
				"de.iritgo.openmetix.interfacing.thies.ThiesDriverEditor", "GagingOutputEditor",
				InterfacingThiesPlugin.class.getClassLoader ()));

		super.init (engine);
	}
}