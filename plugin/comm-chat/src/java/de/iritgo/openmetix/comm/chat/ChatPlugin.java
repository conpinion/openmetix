/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat;


import de.iritgo.openmetix.comm.chat.action.ChatCloseAction;
import de.iritgo.openmetix.comm.chat.action.ChatMessageAction;
import de.iritgo.openmetix.comm.chat.action.ChatMessageServerAction;
import de.iritgo.openmetix.comm.chat.action.UserJoinAction;
import de.iritgo.openmetix.comm.chat.action.UserJoinServerAction;
import de.iritgo.openmetix.comm.chat.action.UserLeaveAction;
import de.iritgo.openmetix.comm.chat.action.UserLeaveServerAction;
import de.iritgo.openmetix.comm.chat.chatter.ChatClientManager;
import de.iritgo.openmetix.comm.chat.chatter.ChatServerManager;
import de.iritgo.openmetix.comm.chat.command.ShowChatter;
import de.iritgo.openmetix.comm.chat.command.UserJoinCommand;
import de.iritgo.openmetix.comm.chat.command.UserLeaveCommand;
import de.iritgo.openmetix.comm.chat.gui.ChatGUIPane;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.framework.console.ConsoleCommand;


public class ChatPlugin
	extends FrameworkPlugin
{
	protected void registerDataObjects ()
	{
	}

	protected void registerActions ()
	{
		registerAction (new UserJoinAction());
		registerAction (new UserJoinServerAction());
		registerAction (new UserLeaveAction());
		registerAction (new UserLeaveServerAction());
		registerAction (new ChatMessageAction());
		registerAction (new ChatMessageServerAction());
		registerAction (new ChatCloseAction());
	}

	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new ChatGUIPane());
	}

	protected void registerManagers ()
	{
		registerManager (Plugin.CLIENT, new ChatClientManager());
		registerManager (Plugin.SERVER, new ChatServerManager());
	}

	protected void registerCommands ()
	{
		registerCommand (new ShowChatter());
	}

	protected void registerConsoleCommands ()
	{
		registerConsoleCommand (
			Plugin.CLIENT,
			new ConsoleCommand("join", new UserJoinCommand(), "chat.help.joinchannel", 1));
		registerConsoleCommand (
			Plugin.CLIENT,
			new ConsoleCommand("leave", new UserLeaveCommand(), "chat.help.leavechannel", 0));
	}
}