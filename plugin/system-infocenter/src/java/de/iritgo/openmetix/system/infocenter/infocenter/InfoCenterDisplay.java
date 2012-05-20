/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.infocenter;


import de.iritgo.openmetix.framework.user.User;


public interface InfoCenterDisplay
{
	public String getId ();

	public void init (String category, int context, User user);

	public void release ();

	public void info (
		User user, int context, String category, String icon, String message, String guiPaneId,
		long uniqueId, int level);
}