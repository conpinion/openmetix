/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.server.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.core.plugin.PluginManager;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * ReloadPlugins.
 *
 * @version $Id: ReloadPlugins.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ReloadPlugins
	extends Command
	implements Runnable
{
	public class DisplayItem
	{
		public String displayId;
		public String guiPaneId;
		public IObjectProxy businessProxy;

		public DisplayItem (String displayId, String guiPaneId, IObjectProxy businessProxy)
		{
			this.displayId = displayId;
			this.guiPaneId = guiPaneId;
			this.businessProxy = businessProxy;
		}
	}

	private List visibleWindows;

	public ReloadPlugins ()
	{
		visibleWindows = new LinkedList();
	}

	public void setProperties (Properties properties)
	{
	}

	public void perform ()
	{
		new Thread(this).start ();
	}

	public void run ()
	{
		reloadPlugins ();
	}

	private void reloadPlugins ()
	{
		Engine engine = Engine.instance ();

		ShutdownManager shutdownManager =
			(ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown");

		shutdownManager.shutdown ();

		PluginManager pluginManager = engine.getPluginManager ();

		pluginManager.unloadPlugins ();

		Engine.instance ().getBaseRegistry ().clear ();
		Engine.instance ().getProxyRegistry ().clear ();
		Engine.instance ().getEventRegistry ().clear ();
		Engine.instance ().getProxyEventRegistry ().clear ();

		pluginManager.loadPlugins ();

		pluginManager.initPlugins ();
	}
}