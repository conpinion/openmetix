/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.client.command.CloseDisplay;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;


public class ShowChatter
	extends Command
{
	private static boolean isChatterVisible = false;

	public ShowChatter ()
	{
		super("show.chatter");
	}

	public static void setChatterIsVisible ()
	{
		isChatterVisible = true;
	}

	public static void setChatterIsNotVisible ()
	{
		isChatterVisible = false;
	}

	public void perform ()
	{
		if (isChatterVisible)
		{
			isChatterVisible = false;
			CommandTools.performAsync (new CloseDisplay("common.chatview"));
		}
		else
		{
			CommandTools.performAsync (new ShowWindow("common.chatview"));
			isChatterVisible = true;
		}
	}

	public boolean canPerform ()
	{
		return true;
	}
}