/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.alarm;


import de.iritgo.openmetix.core.event.Event;
import de.iritgo.openmetix.core.gui.GUIPane;


/**
 * A SensorAlarmEvent is fired if a sensor has signalled an alarm.
 *
 * @version $Id: SensorAlarmEvent.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SensorAlarmEvent
	implements Event
{
	/** The alarming gui pane. */
	private GUIPane guiPane;

	/**
	 * Create a new SensorAlarmEvent.
	 *
	 * @param guiPane The alarming gui pane.
	 */
	public SensorAlarmEvent (GUIPane guiPane)
	{
		this.guiPane = guiPane;
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
}