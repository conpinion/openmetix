/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.action;


import de.iritgo.openmetix.comm.chat.chatter.ChatManager;
import de.iritgo.openmetix.comm.chat.chatter.UserAllreadyJoindException;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;


public class UserJoinServerAction
	extends NetworkFrameworkServerAction
{
	private String userName;
	private String channel;

	public UserJoinServerAction ()
	{
	}

	public UserJoinServerAction (String userName, String channel)
	{
		this.userName = userName;
		this.channel = channel;
	}

	public String getTypeId ()
	{
		return "server.action.userjoin";
	}

	public String getUserName ()
	{
		return userName;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		channel = stream.readUTF ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (channel);
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		ChatManager chatManager =
			(ChatManager) Engine.instance ().getManagerRegistry ().getManager ("chat.server");

		try
		{
			chatManager.joinChannel (channel, userUniqueId);
		}
		catch (UserAllreadyJoindException x)
		{
		}

		return null;
	}
}