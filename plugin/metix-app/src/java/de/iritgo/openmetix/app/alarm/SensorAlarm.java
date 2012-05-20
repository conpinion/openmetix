/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.alarm;


import de.iritgo.openmetix.core.gui.GUIPane;
import javax.swing.Icon;


/**
 * This class represents an alarm triggered by a sensor.
 *
 * @version $Id: SensorAlarm.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SensorAlarm
{
	/** The gui pane on which the alarm occurred. */
	private GUIPane guiPane;

	/** The original icon of the gui pane. */
	private Icon oldIcon;

	/** The id of the desktop pane on which the alarm occurred. */
	private String desktopId;

	/**
	 * Create a new SensorAlarm.
	 *
	 * @param guiPane The gui pane on which the alarm occurred.
	 * @param oldIcon The original icon of the gui pane.
	 * @param desktopId The id of the desktop pane on which the alarm occurred.
	 */
	public SensorAlarm (GUIPane guiPane, Icon oldIcon, String desktopId)
	{
		this.guiPane = guiPane;
		this.oldIcon = oldIcon;
		this.desktopId = desktopId;
	}

	/**
	 * Get the alarming gui pane.
	 *
	 * @return The alarming gui pane.
	 */
	public GUIPane getGUIPane ()
	{
		return guiPane;
	}

	/**
	 * Get the original icon of the gui pane.
	 *
	 * @return The gui pane's icon.
	 */
	public Icon getOldIcon ()
	{
		return oldIcon;
	}

	/**
	 * Get the alarming desktop id.
	 *
	 * @return The id of the desktop.
	 */
	public String getDesktopId ()
	{
		return desktopId;
	}
}