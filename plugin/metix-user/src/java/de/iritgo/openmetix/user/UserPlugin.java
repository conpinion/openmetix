/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.user.command.AddMetixUserRegistryToUser;
import de.iritgo.openmetix.user.command.AddMetixUserToUser;
import de.iritgo.openmetix.user.command.AddUserProfileToUser;
import de.iritgo.openmetix.user.gui.EditUserGUIPane;
import de.iritgo.openmetix.user.gui.UserListGUIPane;
import de.iritgo.openmetix.user.manager.MetixUserClientManager;
import de.iritgo.openmetix.user.manager.MetixUserManager;


/**
 * The user plugin contains all functionality that belongs to the user
 * management of the metix application.
 *
 * @version $Id: UserPlugin.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class UserPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new MetixUserManager());
		registerManager (Plugin.CLIENT, new MetixUserClientManager());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.SERVER, new AddMetixUserRegistryToUser());
		registerCommand (Plugin.SERVER, new AddUserProfileToUser());
		registerCommand (Plugin.SERVER, new AddMetixUserToUser());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new MetixUserRegistry());
		registerDataObject (new MetixUser());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new UserListGUIPane());
		registerGUIPane (Plugin.CLIENT, new EditUserGUIPane());
	}
}