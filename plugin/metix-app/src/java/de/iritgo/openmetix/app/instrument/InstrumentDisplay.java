/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.instrument;


import java.sql.Timestamp;


/**
 * Base interface for all instrument displays.
 *
 * @version $Id: InstrumentDisplay.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public interface InstrumentDisplay
{
	/**
	 * This method receives sensor measurements.
	 *
	 * The measurement values are sent from the server to the client
	 * instrument displays. A display should check if it really displays
	 * measurments for the given sensor. In this case it should
	 * update itself accordingly to the measurement values.
	 *
	 * @param timestamp The timestamp on which the measurement was done.
	 * @param value The measurement value.
	 * @param stationId The id of the gaging station.
	 * @param sensorId The id of the gaging sensor.
	 */
	public void receiveSensorValue (
		Timestamp timestamp, double value, long stationId, long sensorId);

	/**
	 * This method receives historical sensor measurements.
	 *
	 * The measurement values are sent from the server to the client
	 * instrument displays. A display should check if it really displays
	 * measurments for the given sensor. In this case it should
	 * update itself accordingly to the measurement values.
	 *
	 * @param timestamp The timestamp on which the measurement was done.
	 * @param value The measurement value.
	 * @param stationId The id of the gaging station.
	 * @param sensorId The id of the gaging sensor.
	 */
	public void receiveHistoricalSensorValue (
		long instrumentUniqueId, Timestamp timestamp, double value, long stationId, long sensorId);

	/**
	 * Configure the display new.
	 */
	public void configureDisplay ();

	/**
	 * Check wether this display is editable or not.
	 *
	 * @return True if the display is editable.
	 */
	public boolean isEditable ();

	/**
	 * Check wether this display is printable or not.
	 *
	 * @return True if the display is printable.
	 */
	public boolean isPrintable ();

	/**
	 * Print the display.
	 */
	public void print ();
}