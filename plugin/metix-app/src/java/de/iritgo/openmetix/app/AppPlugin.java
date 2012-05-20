/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app;


import de.iritgo.openmetix.app.alarm.AlarmManager;
import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensorManager;
import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.app.gagingstation.action.GagingSensorAction;
import de.iritgo.openmetix.app.gagingstation.action.HistoricalGagingSensorAction;
import de.iritgo.openmetix.app.gagingstation.action.SendMeasurementServerAction;
import de.iritgo.openmetix.app.gagingstation.command.AddGagingStationRegistryToUser;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.app.gagingstation.manager.MeasurementStorageManager;
import de.iritgo.openmetix.app.gagingstation.manager.SensorListenerManager;
import de.iritgo.openmetix.app.history.HistoricalDataRequestAction;
import de.iritgo.openmetix.app.history.HistoricalDataResponseAction;
import de.iritgo.openmetix.app.manager.CoreManager;
import de.iritgo.openmetix.app.userprofile.ActiveDisplay;
import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;


/**
 * Core application functionality.
 *
 * @version $Id: AppPlugin.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class AppPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new GagingStationManager());
		registerManager (Plugin.SERVER, new SensorListenerManager());
		registerManager (Plugin.SERVER, new MeasurementStorageManager());
		registerManager (Plugin.SERVER, new CoreManager());
		registerManager (Plugin.SERVER, new ConfigurationSensorManager());
		registerManager (Plugin.CLIENT, new AlarmManager());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.SERVER, new AddGagingStationRegistryToUser());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new UserProfile());
		registerDataObject (new Preferences());
		registerDataObject (new GagingStation());
		registerDataObject (new GagingSensor());
		registerDataObject (new GagingStationRegistry());
		registerDataObject (new ActiveDisplay());
	}

	/**
	 * Register all actions in this method.
	 */
	protected void registerActions ()
	{
		registerAction (new GagingSensorAction());
		registerAction (new HistoricalGagingSensorAction());
		registerAction (new SendMeasurementServerAction());
		registerAction (new HistoricalDataResponseAction());
		registerAction (new HistoricalDataRequestAction());
	}
}