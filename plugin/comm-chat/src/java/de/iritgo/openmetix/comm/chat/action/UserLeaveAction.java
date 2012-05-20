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


public class UserLeaveAction
	extends FrameworkAction
{
	private String userName;
	private int channel;

	public UserLeaveAction ()
	{
		super();
	}

	public UserLeaveAction (String userName, int channel, long userUniqueId)
	{
		this.channel = channel;
		this.userName = userName;
		this.userUniqueId = userUniqueId;
	}

	public String getTypeId ()
	{
		return "action.userleave";
	}

	public String getUserName ()
	{
		return userName;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		channel = stream.readInt ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeUTF (userName);
		stream.writeInt (channel);
	}

	public void perform ()
	{
		ChatClientManager chatManager =
			(ChatClientManager) Engine.instance ().getManagerRegistry ().getManager ("chat.client");

		chatManager.leaveChannel (new Integer(channel), userUniqueId, userName);
	}
}