/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.monitor.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseRegistry;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.system.monitor.SystemMonitor;


public class ShowSystemMonitor
	extends Command
{
	public ShowSystemMonitor ()
	{
		super("ShowSystemMonitor");
	}

	public void perform ()
	{
		BaseRegistry baseRegistry = Engine.instance ().getBaseRegistry ();
		SystemMonitor systemMonitor =
			(SystemMonitor) baseRegistry.get (SystemMonitor.SYSTEM_MONITOR_ID);

		if (systemMonitor == null)
		{
			systemMonitor = new SystemMonitor();
			systemMonitor.setUniqueId (SystemMonitor.SYSTEM_MONITOR_ID);
			baseRegistry.add (systemMonitor);
			Engine.instance ().getProxyRegistry ().addProxy (new FrameworkProxy(systemMonitor));
		}

		Engine.instance ().getProxyRegistry ().getProxy (systemMonitor.getUniqueId ()).reset ();

		CommandTools.performAsync (new ShowWindow("SystemMonitor", systemMonitor));
	}
}