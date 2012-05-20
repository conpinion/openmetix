/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.action;


import de.iritgo.openmetix.comm.chat.chatter.ChatManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkServerAction;
import java.io.IOException;


public class UserLeaveServerAction
	extends FrameworkServerAction
{
	private String userName;
	private int channel;

	public UserLeaveServerAction ()
	{
		super();
	}

	public UserLeaveServerAction (String userName, int channel)
	{
		super();
		this.channel = channel;
		this.userName = userName;
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
		ChatManager chatManager =
			(ChatManager) Engine.instance ().getManagerRegistry ().getManager ("chat.server");

		chatManager.leaveChannel (new Integer(channel), userUniqueId);
	}
}