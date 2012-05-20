/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.system.infocenter.command.InfoCenter;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.action.InfoCenterAction;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command.AddInfoToGUIDisplay;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command.CreateDiskWriterDisplay;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command.CreateGUINetworkDisplay;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command.RemoveGUINetworkDisplay;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui.NetworkDisplayGUIPane;
import de.iritgo.openmetix.system.infocenter.manager.InfoCenterClientManager;
import de.iritgo.openmetix.system.infocenter.manager.InfoCenterManager;


public class InfoCenterPlugin
	extends FrameworkPlugin
{
	protected void registerDataObjects ()
	{
	}

	protected void registerActions ()
	{
		registerAction (new InfoCenterAction());
	}

	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new NetworkDisplayGUIPane());
	}

	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new InfoCenterManager());
		registerManager (Plugin.CLIENT, new InfoCenterClientManager());
	}

	protected void registerCommands ()
	{
		registerCommand (new InfoCenter());
		registerCommand (new CreateGUINetworkDisplay());
		registerCommand (new CreateDiskWriterDisplay());
		registerCommand (new AddInfoToGUIDisplay());
		registerCommand (new RemoveGUINetworkDisplay());
	}

	protected void registerConsoleCommands ()
	{
	}
}