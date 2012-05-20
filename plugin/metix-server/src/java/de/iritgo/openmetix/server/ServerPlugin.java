/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.server;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.server.manager.ServerManager;


/**
 * Server only functionality is contained in this plugin.
 *
 * @version $Id: ServerPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ServerPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new ServerManager());
	}
}