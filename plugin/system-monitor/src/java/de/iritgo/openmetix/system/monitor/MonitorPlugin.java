/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.monitor;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.system.monitor.command.ShowSystemMonitor;
import de.iritgo.openmetix.system.monitor.gui.SystemMonitorGUIPane;
import de.iritgo.openmetix.system.monitor.manager.SystemMonitorManager;


public class MonitorPlugin
	extends FrameworkPlugin
{
	protected void registerCommands ()
	{
		registerCommand (new ShowSystemMonitor());
	}

	protected void registerDataObjects ()
	{
		registerDataObject (new SystemMonitor());
	}

	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new SystemMonitorGUIPane());
	}

	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new SystemMonitorManager());
	}
}