/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.monitor.manager;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.core.thread.Threadable;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.system.monitor.SystemMonitor;


public class SystemMonitorManager
	extends BaseObject
	implements Manager
{
	private final SystemMonitor systemMonitor = new SystemMonitor();

	public SystemMonitorManager ()
	{
		super("system-monitor");
	}

	class SystemMonitorWorker
		extends Threadable
	{
		public SystemMonitorWorker ()
		{
			super("SystemMonitorWorker");
		}

		public void run ()
		{
			systemMonitor.setRegisteredUsers (Server.instance ().getUserRegistry ().getUserSize ());
			systemMonitor.setWorkingThreads (
				Engine.instance ().getThreadService ().getWorkingSlots ());
			systemMonitor.setFreeThreads (Engine.instance ().getThreadService ().getFreeSlots ());
			systemMonitor.setFreeRam (Runtime.getRuntime ().freeMemory ());

			try
			{
				Thread.sleep (1000);
			}
			catch (InterruptedException x)
			{
			}
		}
	}

	public void init ()
	{
		systemMonitor.setUniqueId (SystemMonitor.SYSTEM_MONITOR_ID);
		Engine.instance ().getBaseRegistry ().add (systemMonitor);

		Engine.instance ().getThreadService ().add (new SystemMonitorWorker());
	}

	public void unload ()
	{
	}
}