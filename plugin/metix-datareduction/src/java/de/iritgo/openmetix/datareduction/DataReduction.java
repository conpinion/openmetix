/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction;


import de.iritgo.openmetix.framework.base.DataObject;


/**
 * @version $Id: DataReduction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class DataReduction
	extends DataObject
{
	public DataReduction ()
	{
		super("DataReduction");

		putAttribute ("startTime", "");
		putAttribute ("interval", "");
		putAttribute ("timeID", "");
		putAttribute ("stationID", "0");
		putAttribute ("sensorID", "0");
	}

	/**
	 * Create a new DataReduction.
	 *
	 * @param uniqueId The datareduction's unique id.
	 */
	public DataReduction (long uniqueId)
	{
		this();
		setUniqueId (uniqueId);
	}

	/**
	 * Get the starttime.
	 *
	 * @return The starttime.
	 */
	public long getStartTime ()
	{
		return getLongAttribute ("startTime");
	}

	/**
	 * Get the interval between starttime and the stoptime.
	 *
	 * @return The interval.
	 */
	public int getInterval ()
	{
		return getIntAttribute ("interval");
	}

	/**
	 * Identifies whether to choose days, months or years.
	 *
	 * @return The time-ID.
	 */
	public int getTimeID ()
	{
		return getIntAttribute ("timeID");
	}

	/**
	 * Get the station-ID. If id equals 0, all station-IDs will be selected
	 *
	 * @return The station-ID
	 */
	public int getStationID ()
	{
		return getIntAttribute ("stationID");
	}

	/**
	 * Get the sensor-ID. If id equals 0, all sensor-IDs will be selected
	 *
	 * @return The sensor-ID.
	 */
	public int getSensorID ()
	{
		return getIntAttribute ("sensorID");
	}

	/**
	 * Set the starttime for the data reduction.
	 *
	 * @param time
	 */
	public void setStartTime (long time)
	{
		putAttribute ("startTime", time);
	}

	/**
	 * Set the interval for the data reduction
	 *
	 * @param interval
	 */
	public void setInterval (int interval)
	{
		putAttribute ("interval", interval);
	}

	/**
	 * Set the time-ID for the data reduction.
	 *
	 * @param id
	 */
	public void setTimeID (int id)
	{
		putAttribute ("timeID", id);
	}

	/**
	 * Set the station-ID for the data reduction.
	 *
	 * @param id
	 */
	public void setStationID (int id)
	{
		putAttribute ("stationID", id);
	}

	/**
	 * Set the sensor-ID for the data reduction.
	 *
	 * @param id
	 */
	public void setSensorID (int id)
	{
		putAttribute ("sensorID", id);
	}
}