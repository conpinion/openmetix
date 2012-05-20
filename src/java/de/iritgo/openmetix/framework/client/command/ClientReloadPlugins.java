/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.plugin.PluginManager;
import de.iritgo.openmetix.framework.base.InitIritgoException;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;
import de.iritgo.openmetix.framework.client.Client;


/**
 * ClientReloadPlugins.
 *
 * @version $Id: ClientReloadPlugins.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ClientReloadPlugins
	extends Command
	implements Runnable
{
	public ClientReloadPlugins ()
	{
	}

	public void perform ()
	{
		new Thread(this).start ();
	}

	public void run ()
	{
		IDesktopManager desktopManager = Client.instance ().getClientGUI ().getDesktopManager ();

		desktopManager.saveVisibleDisplays ();
		desktopManager.closeAllDisplays ();

		ShutdownManager shutdownManager =
			(ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown");

		shutdownManager.shutdown ();

		try
		{
			Client.instance ().stopGUI ();
		}
		catch (InitIritgoException x)
		{
			Log.logFatal ("ClientReloadPlugin", "preform/run", "Can not close the GUI!");
		}

		reloadPlugins ();

		try
		{
			Client.instance ().initGUI ();
		}
		catch (InitIritgoException x)
		{
			Log.logFatal ("ClientReloadPlugin", "preform/run", "Can not init and start the GUI!");
		}

		desktopManager.showSavedDisplays ();
	}

	private void reloadPlugins ()
	{
		Engine engine = Engine.instance ();
		PluginManager pluginManager = engine.getPluginManager ();

		pluginManager.unloadPlugins ();

		Engine.instance ().getBaseRegistry ().clear ();
		Engine.instance ().getProxyRegistry ().clear ();

		pluginManager.loadPlugins ();

		pluginManager.initPlugins ();
	}
}