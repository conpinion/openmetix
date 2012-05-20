/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.infocenter;


import de.iritgo.openmetix.framework.user.User;


public class InfoCenterConsoleDisplay
	implements InfoCenterDisplay
{
	public InfoCenterConsoleDisplay ()
	{
	}

	public String getId ()
	{
		return "system.out.infocenterdisplay";
	}

	public void init (String category, int context, User user)
	{
	}

	public void release ()
	{
	}

	public void info (
		User user, int context, String category, String icon, String message, String guiPaneId,
		long uniqueId, int level)
	{
		System.out.println ("[" + level + "][" + category + "] ->" + message + "<-");
	}
}