/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;


public class TabChatView
	extends SwingGUIPane
{
	private int channelId;
	private JPanel tabComponent;
	private JTextArea chatArea;
	private JList usersGUI;
	private DefaultListModel users;
	private int tabPos;
	private String tabName;

	public TabChatView ()
	{
		super("common.tabchatview");
	}

	public IObject getSampleObject ()
	{
		return null;
	}

	public GUIPane cloneGUIPane ()
	{
		return new TabChatView();
	}

	public JPanel createTabChatView (int tabCount, String tabName)
	{
		tabPos = tabCount;
		this.tabName = tabName;

		tabComponent = new JPanel();
		tabComponent.setLayout (new GridBagLayout());
		chatArea = new JTextArea();
		users = new DefaultListModel();
		usersGUI = new JList(users);

		JScrollPane scrollPane = new JScrollPane(chatArea);

		tabComponent.add (
			new JScrollPane(usersGUI),
			createConstraints (
				0, 0, 1, 1, GridBagConstraints.BOTH, 20, 100, new Insets(2, 0, 2, 0)));
		tabComponent.add (
			scrollPane,
			createConstraints (
				1, 0, 1, 1, GridBagConstraints.BOTH, 80, 100, new Insets(2, 0, 2, 0)));

		tabComponent.revalidate ();
		tabComponent.repaint ();

		return tabComponent;
	}

	public int getTabPos ()
	{
		return tabPos;
	}

	public String getTabName ()
	{
		return tabName;
	}

	public JPanel getTabPanel ()
	{
		return tabComponent;
	}

	public void chatMessage (String message)
	{
		chatArea.append (message + "\n");
		chatArea.setCaretPosition (chatArea.getText ().length ());
	}

	public void addUser (String user)
	{
		users.addElement (user);
		usersGUI.revalidate ();
		usersGUI.repaint ();
	}

	public void removeUser (String user)
	{
		users.removeElement (user);
		usersGUI.revalidate ();
		usersGUI.repaint ();
	}

	public void onUserInformation (ActionEvent event)
	{
		UserRegistry userRegistry = Client.instance ().getUserRegistry ();
		String userName = (String) users.get (usersGUI.getSelectedIndex ());

		User user = userRegistry.getUser (userName);

		FrameworkProxy userProxy = new FrameworkProxy(user);

		Engine.instance ().getProxyRegistry ().addProxy (userProxy);
		Engine.instance ().getBaseRegistry ().add (user);

		CommandTools.performAsync (new ShowWindow("main.user_" + userName, user));
	}
}