/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.gui;


import de.iritgo.openmetix.comm.chat.chatter.ChatClientManager;
import de.iritgo.openmetix.comm.chat.command.ShowChatter;
import de.iritgo.openmetix.comm.chat.command.UserChatCommand;
import de.iritgo.openmetix.comm.chat.command.UserJoinCommand;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.CallbackActionListener;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ChatGUIPane
	extends SwingGUIPane
	implements ChatGUI
{
	private Map channelTabs;
	private JTabbedPane channels;
	private JTextField messageField;
	private int tabCount;
	private DateFormat dateFormat;
	private Date currentTime;

	public ChatGUIPane ()
	{
		super("common.chatview");
		channelTabs = new HashMap();
		dateFormat = DateFormat.getTimeInstance (DateFormat.MEDIUM);
		currentTime = new Date();
	}

	public void initGUI ()
	{
		JPanel allPanel = new JPanel();

		allPanel.setLayout (new GridBagLayout());
		channels = new JTabbedPane();

		allPanel.add (
			channels, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		messageField = new JTextField();
		messageField.addActionListener (new CallbackActionListener(this, "onChatMessage"));
		allPanel.add (
			messageField, createConstraints (0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, null));

		UserJoinCommand userJoinCommand =
			new UserJoinCommand("System", AppContext.instance ().getUser ().getName ());

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (userJoinCommand);

		content.add (
			allPanel, createConstraints (0, 1, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		tabCount = 0;
		ShowChatter.setChatterIsVisible ();
	}

	public void loadFromObject ()
	{
	}

	public void storeToObject ()
	{
	}

	public IObject getSampleObject ()
	{
		return null;
	}

	public void joinChannel (String channelId, String user)
	{
		TabChatView tabChatView =
			(TabChatView) channelTabs.get (new Integer(channelId.hashCode ()));

		if (tabChatView != null)
		{
			MessageFormat mf =
				new MessageFormat(
					Engine.instance ().getResourceService ().getStringWithoutException (
						"chat.userjoind"));

			tabChatView.chatMessage (
				mf.format (new Object[]
					{
						user
					}, new StringBuffer(), null).toString ());

			tabChatView.addUser (user);

			return;
		}

		tabChatView = new TabChatView();

		channelTabs.put (new Integer(channelId.hashCode ()), tabChatView);
		channels.addTab (channelId, tabChatView.createTabChatView (tabCount, channelId));
		channels.revalidate ();
		++tabCount;
	}

	public void leaveChannel (Integer channelId, String user)
	{
		TabChatView tabChatView = (TabChatView) channelTabs.get (channelId);

		if (tabChatView == null)
		{
			return;
		}

		MessageFormat mf =
			new MessageFormat(
				Engine.instance ().getResourceService ().getStringWithoutException (
					"chat.userleave"));

		tabChatView.chatMessage (
			mf.format (new Object[]
				{
					user
				}, new StringBuffer(), null).toString ());

		if (user.equals ("") || AppContext.instance ().getUser ().getName ().equals (user))
		{
			channels.removeTabAt (channels.indexOfTab (tabChatView.getTabName ()));
			channelTabs.remove (channelId);
			--tabCount;

			return;
		}

		tabChatView.removeUser (user);
	}

	public void addMessage (String message, int channelId, String user)
	{
		TabChatView tabChatView = (TabChatView) channelTabs.get (new Integer(channelId));

		currentTime.setTime (System.currentTimeMillis ());
		tabChatView.chatMessage (
			"[" + dateFormat.format (currentTime) + "] " + user + ": " + message);
	}

	public void onChatMessage (ActionEvent event)
	{
		String message = messageField.getText ();

		messageField.setText ("");

		int index = channels.getSelectedIndex ();

		String channel = "System";

		if (index >= 0)
		{
			channel = channels.getTitleAt (index);
		}

		UserChatCommand userChatCommand =
			new UserChatCommand(
				message, channel.hashCode (), AppContext.instance ().getUser ().getName ());

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (userChatCommand);
	}

	public void close ()
	{
		ChatClientManager chatManager =
			(ChatClientManager) Engine.instance ().getManagerRegistry ().getManager ("chat.client");

		chatManager.closeAllChannels ();
		super.close ();
		ShowChatter.setChatterIsNotVisible ();
	}

	public void systemClose ()
	{
		close ();
	}

	public Integer getCurrentChannel ()
	{
		return new Integer(channels.getTitleAt (channels.getSelectedIndex ()).hashCode ());
	}

	public GUIPane cloneGUIPane ()
	{
		ChatGUIPane registerNewUserGUIPane = new ChatGUIPane();

		return registerNewUserGUIPane;
	}
}