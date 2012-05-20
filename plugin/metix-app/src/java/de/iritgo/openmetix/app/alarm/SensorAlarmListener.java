/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.alarm;


import de.iritgo.openmetix.core.event.EventListener;


/**
 * Implement this interface if you want to listen to alarm events.
 */
public interface SensorAlarmListener
	extends EventListener
{
	/**
	 * An alarm occurred.
	 *
	 * @param event The alarm event.
	 */
	public void sensorAlarm (SensorAlarmEvent event);
}