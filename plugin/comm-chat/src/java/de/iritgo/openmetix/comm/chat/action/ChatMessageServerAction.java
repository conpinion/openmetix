/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.action;


import de.iritgo.openmetix.comm.chat.chatter.ChatManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;


public class ChatMessageServerAction
	extends NetworkFrameworkServerAction
{
	private String chatMessage;
	private int channelId;
	private String userName;

	public ChatMessageServerAction ()
	{
	}

	public ChatMessageServerAction (String chatMessage, int channelId, String userName)
	{
		super();
		this.chatMessage = chatMessage;
		this.channelId = channelId;
		this.userName = userName;
	}

	public String getTypeId ()
	{
		return "server.action.chatmessage";
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		chatMessage = stream.readUTF ();
		channelId = stream.readInt ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (chatMessage);
		stream.writeInt (channelId);
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		ChatManager chatManager =
			(ChatManager) Engine.instance ().getManagerRegistry ().getManager ("chat.server");

		chatManager.messageChannel (chatMessage, channelId, userUniqueId);

		return null;
	}
}