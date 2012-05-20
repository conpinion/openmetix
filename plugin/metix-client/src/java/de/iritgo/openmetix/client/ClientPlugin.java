/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client;


import de.iritgo.openmetix.client.command.EditInstrument;
import de.iritgo.openmetix.client.command.EnableAdminFunctions;
import de.iritgo.openmetix.client.command.KeyCheckCommand;
import de.iritgo.openmetix.client.command.PopulatePreferences;
import de.iritgo.openmetix.client.command.SetTabIcon;
import de.iritgo.openmetix.client.command.StatusProgressStep;
import de.iritgo.openmetix.client.gui.ConnectionFailureGUIPane;
import de.iritgo.openmetix.client.gui.DimensionEditor;
import de.iritgo.openmetix.client.gui.GagingSensorEditor;
import de.iritgo.openmetix.client.gui.GagingStationEditor;
import de.iritgo.openmetix.client.gui.GagingStationListEditor;
import de.iritgo.openmetix.client.gui.KeyCheckConfigurator;
import de.iritgo.openmetix.client.gui.KeyCheckResultDisplay;
import de.iritgo.openmetix.client.gui.KeyCheckStart;
import de.iritgo.openmetix.client.gui.MeasurementRecordDialog;
import de.iritgo.openmetix.client.gui.PreferencesGUIPane;
import de.iritgo.openmetix.client.gui.UserLoginFailureGUIPane;
import de.iritgo.openmetix.client.gui.UserLoginGUIPane;
import de.iritgo.openmetix.client.key.KeyCheckRequest;
import de.iritgo.openmetix.client.key.KeyCheckResponse;
import de.iritgo.openmetix.client.manager.MetixClientManager;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.framework.client.gui.ConnectToServerGUIPane;
import de.iritgo.openmetix.framework.user.gui.UserGUIPane;


/**
 * The ClientPlugin contains the basic client gui functionality.
 *
 * @version $Id: ClientPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ClientPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.CLIENT, new MetixClientManager());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.CLIENT, new PopulatePreferences());
		registerCommand (Plugin.CLIENT, new SetTabIcon());
		registerCommand (Plugin.CLIENT, new EnableAdminFunctions());
		registerCommand (Plugin.CLIENT, new EditInstrument());
		registerCommand (Plugin.CLIENT, new StatusProgressStep());
		registerCommand (Plugin.CLIENT, new KeyCheckCommand());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new ConnectToServerGUIPane("ConnectToServer"));
		registerGUIPane (Plugin.CLIENT, new ConnectToServerGUIPane("CheckUser"));
		registerGUIPane (Plugin.CLIENT, new ConnectionFailureGUIPane());
		registerGUIPane (Plugin.CLIENT, new PreferencesGUIPane());
		registerGUIPane (Plugin.CLIENT, new UserLoginGUIPane());
		registerGUIPane (Plugin.CLIENT, new UserLoginFailureGUIPane());
		registerGUIPane (Plugin.CLIENT, new UserGUIPane());
		registerGUIPane (Plugin.CLIENT, new GagingStationListEditor());
		registerGUIPane (Plugin.CLIENT, new GagingStationEditor());
		registerGUIPane (Plugin.CLIENT, new GagingSensorEditor());
		registerGUIPane (Plugin.CLIENT, new MeasurementRecordDialog());
		registerGUIPane (Plugin.CLIENT, new KeyCheckConfigurator());
		registerGUIPane (Plugin.CLIENT, new KeyCheckResultDisplay());
		registerGUIPane (Plugin.CLIENT, new KeyCheckStart());
		registerGUIPane (Plugin.CLIENT, new DimensionEditor());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerActions ()
	{
		registerAction (new KeyCheckRequest());
		registerAction (new KeyCheckResponse());
	}
}