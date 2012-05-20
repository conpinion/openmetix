/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.command;


public class InfoCenter
	extends CommonInfoCenterCommand
{
	public InfoCenter ()
	{
		super("infocenter");
	}

	public void perform ()
	{
		de.iritgo.openmetix.system.infocenter.infocenter.InfoCenter.info (
			user, context, category, icon, message, guiPaneId, uniqueId, level);
	}
}