/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.chatter;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.action.ActionProcessorRegistry;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.util.Iterator;


public class ChatterProcessor
{
	private Long userId;
	private ChatChannel chatChannel;

	public ChatterProcessor (Long userId, ChatChannel chatChannel)
	{
		this.userId = userId;
		this.chatChannel = chatChannel;
	}

	public void doProcessor (Processor processor)
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		User newUser = userRegistry.getUser (userId);
		ActionProcessorRegistry actionProcessorRegistry =
			Engine.instance ().getActionProcessorRegistry ();

		if (chatChannel == null)
		{
			return;
		}

		Iterator i = chatChannel.getMembersIterator ();

		while (i.hasNext ())
		{
			User user = (User) i.next ();

			if (! user.isOnline ())
			{
				continue;
			}

			de.iritgo.openmetix.core.action.AbstractAction action =
				processor.getAction (user, newUser);

			ActionTools.sendToClient (action);
		}
	}
}