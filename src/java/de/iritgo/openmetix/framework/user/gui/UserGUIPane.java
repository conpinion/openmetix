/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.gui;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ILabel;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.user.User;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;


/**
 * UserGUIPane.
 *
 * @version $Id: UserGUIPane.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class UserGUIPane
	extends SwingGUIPane
{
	private JTextField userName;
	private JTextField password;
	private JTextField eMail;

	public UserGUIPane ()
	{
		super("common.userview");
	}

	public IObject getSampleObject ()
	{
		return new User();
	}

	public void initGUI ()
	{
		JPanel allPanel = new JPanel();

		allPanel.setLayout (new GridBagLayout());

		int row = 0;

		ILabel userNameLabel = new ILabel("common.username");

		allPanel.add (
			userNameLabel,
			createConstraints (
				0, row, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets(5, 15, 5, 15)));
		userName = new JTextField();
		allPanel.add (
			userName,
			createConstraints (
				1, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets(5, 15, 5, 15)));

		ILabel emailLabel = new ILabel("common.email");

		allPanel.add (
			emailLabel,
			createConstraints (
				0, row, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets(5, 15, 5, 15)));
		eMail = new JTextField();
		allPanel.add (
			eMail,
			createConstraints (
				1, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets(5, 15, 5, 15)));

		content.add (
			allPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
	}

	public void loadFromObject ()
	{
		User user = (User) iobject;

		userName.setText (user.getName ());
		eMail.setText (user.getEmail ());
	}

	public void storeToObject ()
	{
		User user = (User) iobject;
	}

	public void close ()
	{
		super.close ();
	}

	public void onRegister (ActionEvent event)
	{
	}

	public void onCancel (ActionEvent event)
	{
		display.close ();
	}

	public GUIPane cloneGUIPane ()
	{
		UserGUIPane userGUIPane = new UserGUIPane();

		return userGUIPane;
	}
}