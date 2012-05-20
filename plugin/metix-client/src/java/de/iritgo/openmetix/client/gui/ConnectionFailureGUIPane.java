/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.CallbackActionListener;
import de.iritgo.openmetix.core.gui.swing.IButton;
import de.iritgo.openmetix.core.gui.swing.ILabel;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;


/**
 * GUIPane
 *
 * @version $Id: ConnectionFailureGUIPane.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class ConnectionFailureGUIPane
	extends SwingGUIPane
{
	/**
	 * Standard constructor
	 */
	public ConnectionFailureGUIPane (String id)
	{
		super("common.connectionfailure");
	}

	/**
	 * Standard constructor
	 */
	public ConnectionFailureGUIPane ()
	{
		super("common.connectionfailure");
	}

	/**
	 * Init GUI
	 */
	public void initGUI ()
	{
		JPanel allPanel = new JPanel();

		allPanel.setLayout (new GridBagLayout());

		int row = 0;

		ILabel connectTotServer = new ILabel("common.connectionfailure");

		allPanel.add (
			connectTotServer,
			createConstraints (
				0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets(5, 15, 5, 15)));

		IButton okButton = new IButton("common.ok");

		okButton.addActionListener (new CallbackActionListener(this, "onOkButton"));
		allPanel.add (
			okButton,
			createConstraints (
				0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets(5, 15, 5, 15)));

		content.add (
			allPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
	}

	/**
	 * LoadFormObject, load the Data form Object.
	 */
	public void loadFromObject ()
	{
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
	}

	public IObject getSampleObject ()
	{
		return null;
	}

	/**
	 * Close it.
	 */
	public void close ()
	{
		super.close ();
	}

	/**
	 * Cancel
	 */
	public void onOkButton (ActionEvent event)
	{
		display.close ();
		CommandTools.performAsync (new ShowDialog("UserLoginGUIPane"));
	}

	/**
	 * Return a new instance.
	 */
	public GUIPane cloneGUIPane ()
	{
		ConnectionFailureGUIPane connectionFailureGUIPane =
			new ConnectionFailureGUIPane(getTypeId ());

		return connectionFailureGUIPane;
	}
}