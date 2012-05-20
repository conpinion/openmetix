/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.simulator;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverDescriptor;


/**
 * This plugin contains components for gaging system simulators.
 *
 * @version $Id: InterfacingSimulatorPlugin.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class InterfacingSimulatorPlugin
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
		registerManager (Plugin.SERVER, new InterfacingSimulatorManager());
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
				"WeatherSimulator", "metix-interfacing-simulator.weatherSimulator",
				"de.iritgo.openmetix.interfacing.simulator.WeatherSimulatorDriver",
				"de.iritgo.openmetix.interfacing.simulator.WeatherSimulatorDriverEditor",
				"GagingOutputEditor", InterfacingSimulatorPlugin.class.getClassLoader ()));

		GagingSystemDriverDescriptor.add (
			new GagingSystemDriverDescriptor(
				"HistoryRepeater", "metix-interfacing-simulator.historyRepeater",
				"de.iritgo.openmetix.interfacing.simulator.HistoryRepeaterDriver",
				"de.iritgo.openmetix.interfacing.simulator.HistoryRepeaterDriverEditor",
				"GagingOutputEditor", InterfacingSimulatorPlugin.class.getClassLoader ()));

		super.init (engine);
	}
}