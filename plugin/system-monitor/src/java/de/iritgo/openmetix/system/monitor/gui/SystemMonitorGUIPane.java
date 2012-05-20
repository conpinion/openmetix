/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.monitor.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.system.monitor.SystemMonitor;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


public class SystemMonitorGUIPane
	extends SwingGUIPane
{
	public JLabel registeredUsers;
	public JLabel onlineUsers;
	public JLabel workingThreads;
	public JLabel freeThreads;
	public JLabel freeRam;
	public JLabel clientFreeRam;
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
			}
		};

	public Action update =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				Engine.instance ().getProxyRegistry ().getProxy (iobject.getUniqueId ()).reset ();
			}
		};

	public SystemMonitorGUIPane ()
	{
		super("SystemMonitor");
	}

	public void initGUI ()
	{
		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/SystemMonitor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 2, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "SystemMonitor.initGUI", x.toString ());
		}
	}

	public IObject getSampleObject ()
	{
		return new SystemMonitor();
	}

	public GUIPane cloneGUIPane ()
	{
		return new SystemMonitorGUIPane();
	}

	public void loadFromObject ()
	{
		SystemMonitor systemMonitor = (SystemMonitor) iobject;

		registeredUsers.setText ("" + systemMonitor.getRegisteredUsers ());
		onlineUsers.setText ("" + systemMonitor.getOnlineUsers ());
		workingThreads.setText ("" + systemMonitor.getWorkingThreads ());
		freeThreads.setText ("" + systemMonitor.getFreeThreads ());
		freeRam.setText ("" + systemMonitor.getFreeRam ());
		clientFreeRam.setText ("" + Runtime.getRuntime ().freeMemory ());
	}
}