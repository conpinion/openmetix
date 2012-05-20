/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.gui;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ILabel;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.network.ConnectObserver;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;


/**
 * ConnectToServerGUIPane.
 *
 * @version $Id: ConnectToServerGUIPane.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ConnectToServerGUIPane
	extends SwingGUIPane
	implements ConnectObserver
{
	private JTextField email;
	private JTextField nickName;
	private int state;
	private JProgressBar progress;

	public ConnectToServerGUIPane (String guiPaneId)
	{
		super(guiPaneId);
	}

	public void initGUI ()
	{
		JPanel allPanel = new JPanel();

		allPanel.setLayout (new GridBagLayout());

		int row = 0;

		ILabel connectTotServer = new ILabel(getTypeId ());

		allPanel.add (
			connectTotServer,
			createConstraints (
				0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets(5, 15, 5, 15)));

		progress = new JProgressBar(0, 100);
		allPanel.add (
			progress,
			createConstraints (
				0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 100, 0, new Insets(5, 15, 5, 15)));

		content.add (
			allPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

		state = 0;

		getDisplay ().putProperty ("weightx", new Double(2.0));
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

	public void close ()
	{
		super.close ();
	}

	public void notice ()
	{
		progress.setValue (state++);

		if (state == 100)
		{
			state = 0;
		}
	}

	public void onCancel (ActionEvent event)
	{
		display.close ();
	}

	public GUIPane cloneGUIPane ()
	{
		ConnectToServerGUIPane pane = new ConnectToServerGUIPane(getTypeId ());

		return pane;
	}
}