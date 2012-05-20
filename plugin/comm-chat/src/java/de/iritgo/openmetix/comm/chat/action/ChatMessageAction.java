/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.action;


import de.iritgo.openmetix.comm.chat.chatter.ChatClientManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


public class ChatMessageAction
	extends FrameworkAction
{
	private String userName;
	private String chatMessage;
	private int channelId;

	public ChatMessageAction ()
	{
	}

	public ChatMessageAction (
		String chatMessage, int channelId, long userUniqueId, String userName)
	{
		super(userUniqueId);

		this.userName = userName;
		this.chatMessage = chatMessage;
		this.channelId = channelId;
	}

	public String getTypeId ()
	{
		return "action.chatmessage";
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

	public void perform ()
	{
		ChatClientManager chatManager =
			(ChatClientManager) Engine.instance ().getManagerRegistry ().getManager ("chat.client");

		chatManager.messageChannel (chatMessage, channelId, userUniqueId, userName);
	}
}