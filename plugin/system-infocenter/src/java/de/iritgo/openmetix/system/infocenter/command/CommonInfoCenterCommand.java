/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.user.User;
import java.util.Properties;


public class CommonInfoCenterCommand
	extends Command
{
	protected int context;
	protected String category;
	protected String icon;
	protected String message;
	protected String guiPaneId;
	protected long uniqueId;
	protected int level;
	protected User user;

	public CommonInfoCenterCommand (String id)
	{
		super(id);
	}

	public void setProperties (Properties properties)
	{
		super.setProperties (properties);
		context = Integer.parseInt (properties.getProperty ("context", "0"));
		category = properties.getProperty ("category", "system");
		icon = properties.getProperty ("icon", "none");
		message = properties.getProperty ("message", "no message");
		guiPaneId = properties.getProperty ("guiPaneId", "none");
		uniqueId = Long.parseLong (properties.getProperty ("uniqueId", "0"));
		level = Integer.parseInt (properties.getProperty ("level", "0"));
		user = (User) properties.get ("user");
	}
}