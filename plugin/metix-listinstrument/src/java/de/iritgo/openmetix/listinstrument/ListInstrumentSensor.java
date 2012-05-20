/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.listinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;


/**
 * Specifies the sensor that should be displayed in a list instrument.
 *
 * @version $Id: ListInstrumentSensor.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ListInstrumentSensor
	extends ConfigurationSensor
{
	/**
	 * Create a new ListInstrumentSensor.
	 */
	public ListInstrumentSensor ()
	{
		super("ListInstrumentSensor");

		putAttribute ("stationName", new String("???"));
		putAttribute ("sensorName", new String("???"));
	}

	/**
	 * Get the name of the station.
	 *
	 * @return The station name.
	 */
	public String getStationName ()
	{
		return getStringAttribute ("stationName");
	}

	/**
	 * Set the name of the station.
	 *
	 * @param stationName The new stationName.
	 */
	public void setStationName (String stationName)
	{
		putAttribute ("stationName", stationName);
	}

	/**
	 * Get the name of the sensor.
	 *
	 * @return The sensor name.
	 */
	public String getSensorName ()
	{
		return getStringAttribute ("sensorName");
	}

	/**
	 * Set the name of the sensor.
	 *
	 * @param sensorName The new sensorName.
	 */
	public void setSensorName (String sensorName)
	{
		putAttribute ("sensorName", sensorName);
	}

	/**
	 * Get a string representation of the sensor.
	 *
	 * @return The string representation.
	 */
	public String toString ()
	{
		return getStationName () + " - " + getSensorName ();
	}
}