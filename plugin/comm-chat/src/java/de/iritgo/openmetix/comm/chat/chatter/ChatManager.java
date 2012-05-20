/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.chatter;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownObserver;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ChatManager
	extends BaseObject
	implements Manager, ShutdownObserver
{
	protected Map chatChannels = new HashMap();
	private UserRegistry userRegistry;

	public ChatManager (String id, UserRegistry userRegistry)
	{
		super(id);
		this.userRegistry = userRegistry;
	}

	public void joinChannel (String channelName, long chatter)
		throws UserAllreadyJoindException
	{
		Integer hashCode = new Integer(channelName.hashCode ());
		ChatChannel chatChannel = (ChatChannel) chatChannels.get (hashCode);

		if (chatChannel == null)
		{
			chatChannel = new ChatChannel(channelName, userRegistry);
			chatChannels.put (hashCode, chatChannel);
			Log.log (
				"system", "ChatManager.joinChannel", "Channel added: " + channelName, Log.INFO);
		}

		if (! chatChannel.existsChatterInChannel (new Long(chatter)))
		{
			chatChannel.addChatter (new Long(chatter));
		}
		else
		{
			throw new UserAllreadyJoindException();
		}
	}

	public void leaveChannel (String channelName, long chatter)
	{
		Integer channelId = new Integer(channelName.hashCode ());

		leaveChannel (channelId, chatter);
	}

	public void leaveChannel (Integer channelId, long chatter)
	{
		ChatChannel chatChannel = (ChatChannel) chatChannels.get (channelId);

		if (chatChannel != null)
		{
			chatChannel.removeChatter (new Long(chatter));

			if (chatChannel.getNumChatters () <= 0)
			{
				chatChannels.remove (channelId);
			}
		}
	}

	public void messageChannel (String message, int channelId, long chatter)
	{
	}

	protected ChatChannel getChatChannel (int channelId)
	{
		return ((ChatChannel) chatChannels.get (new Integer(channelId)));
	}

	protected ChatChannel getChatChannel (String channelName)
	{
		return getChatChannel (channelName.hashCode ());
	}

	protected Iterator getChatChannelIterator ()
	{
		return chatChannels.values ().iterator ();
	}

	public void onShutdown ()
	{
		chatChannels.clear ();
	}

	public void onUserLogoff (User user)
	{
		for (Iterator i = chatChannels.values ().iterator (); i.hasNext ();)
		{
			((ChatChannel) i.next ()).removeChatter (user);
		}
	}

	public void init ()
	{
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).addObserver (
			this);
	}

	public void unload ()
	{
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).removeObserver (
			this);
	}
}