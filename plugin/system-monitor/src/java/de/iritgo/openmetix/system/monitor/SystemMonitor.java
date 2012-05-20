/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.monitor;


import de.iritgo.openmetix.framework.base.DataObject;


public class SystemMonitor
	extends DataObject
{
	public static final long SYSTEM_MONITOR_ID = 11111;

	public SystemMonitor ()
	{
		super("SystemMonitor");
		putAttribute ("registeredUsers", 0);
		putAttribute ("onlineUsers", 0);
		putAttribute ("workingThreads", 0);
		putAttribute ("freeThreads", 0);
		putAttribute ("freeRam", new Long(0));
	}

	public void setRegisteredUsers (int registeredUsers)
	{
		putAttribute ("registeredUsers", registeredUsers);
	}

	public int getRegisteredUsers ()
	{
		return getIntAttribute ("registeredUsers");
	}

	public void setOnlineUsers (int onlineUsers)
	{
		putAttribute ("onlineUsers", onlineUsers);
	}

	public int getOnlineUsers ()
	{
		return getIntAttribute ("onlineUsers");
	}

	public void setWorkingThreads (int workingThreads)
	{
		putAttribute ("workingThreads", workingThreads);
	}

	public int getWorkingThreads ()
	{
		return getIntAttribute ("workingThreads");
	}

	public void setFreeThreads (int freeThreads)
	{
		putAttribute ("freeThreads", freeThreads);
	}

	public int getFreeThreads ()
	{
		return getIntAttribute ("freeThreads");
	}

	public void setFreeRam (long freeRam)
	{
		putAttribute ("freeRam", freeRam);
	}

	public long getFreeRam ()
	{
		return getLongAttribute ("freeRam");
	}
}