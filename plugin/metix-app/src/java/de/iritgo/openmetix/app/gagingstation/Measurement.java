/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation;


import java.sql.Timestamp;


/**
 * A Measurement contains one measurement value of a specific station, sensor
 * and timestamp combination.
 *
 * @version $Id: Measurement.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class Measurement
{
	/** The time on which the measurement was taken. */
	public Timestamp timestamp;

	/** The measurement value. */
	public double value;

	/** The station to which the measurement belongs. */
	public long stationId;

	/** The sensor to which the measurement belongs. */
	public long sensorId;

	/** The instrument to which the measurement should be sent (may be null). */
	public long instrumentUniqueId;

	/** If true, the measurement should be stored into the database. */
	public boolean storeToDatabase;

	/**
	 * Create a new Measurement.
	 *
	 * @param timestamp The time on which the measurement was taken.
	 * @param value The measurement value.
	 * @param stationId The station to which the measurement belongs.
	 * @param sensorId The sensor to which the measurement belongs.
	 * @param storeToDatabase If true, the measurement should be stored into the database.
	 */
	public Measurement (
		Timestamp timestamp, double value, long stationId, long sensorId, boolean storeToDatabase)
	{
		this.timestamp = timestamp;
		this.value = value;
		this.stationId = stationId;
		this.sensorId = sensorId;
		this.storeToDatabase = storeToDatabase;
	}

	/**
	 * Create a new Measurement.
	 *
	 * @param timestamp The time on which the measurement was taken.
	 * @param value The measurement value.
	 * @param stationId The station to which the measurement belongs.
	 * @param sensorId The sensor to which the measurement belongs.
	 */
	public Measurement (Timestamp timestamp, double value, long stationId, long sensorId)
	{
		this(timestamp, value, stationId, sensorId, false);
	}

	/**
	 * Create a new Measurement.
	 *
	 * @param instrumentUniqueId The instrument to which the measurement should be sent.
	 * @param timestamp The time on which the measurement was taken.
	 * @param value The measurement value.
	 * @param stationId The station to which the measurement belongs.
	 * @param sensorId The sensor to which the measurement belongs.
	 */
	public Measurement (
		long instrumentUniqueId, Timestamp timestamp, double value, long stationId, long sensorId)
	{
		this.instrumentUniqueId = instrumentUniqueId;
		this.timestamp = timestamp;
		this.value = value;
		this.stationId = stationId;
		this.sensorId = sensorId;
	}

	/**
	 * Create a string representation of this measurement.
	 *
	 * @return The measurement as a string.
	 */
	public String toString ()
	{
		return timestamp.toString () + " " + stationId + " " + sensorId + " " + value;
	}

	/**
	 * Get the measurement time.
	 *
	 * @return The measurement time.
	 */
	public Timestamp getTimestamp ()
	{
		return timestamp;
	}

	/**
	 * Get the measurement value.
	 *
	 * @return The measurement value.
	 */
	public double getValue ()
	{
		return value;
	}

	/**
	 * Set the measurement value.
	 *
	 * @param value The new measurement value.
	 */
	public void setValue (double value)
	{
		this.value = value;
	}

	/**
	 * Get the measurement gaging station.
	 *
	 * @return The measurement gaging station.
	 */
	public long getStationId ()
	{
		return stationId;
	}

	/**
	 * Get the measurement gaging sensor id.
	 *
	 * @return The measurement gaging sensor id.
	 */
	public long getSensorId ()
	{
		return sensorId;
	}

	/**
	 * Set the measurement gaging sensor id.
	 *
	 * @param sensorId The new measurement gaging sensor id.
	 */
	public void setSensorId (long sensorId)
	{
		this.sensorId = sensorId;
	}

	/**
	 * Get the measurement instrument.
	 *
	 * @return The measurement instrument.
	 */
	public long getInstrumentUniqueId ()
	{
		return instrumentUniqueId;
	}

	/**
	 * Check wether the measurement should be stored to the database.
	 *
	 * @return True if the measurement should be stored.
	 */
	public boolean isStoreToDatabase ()
	{
		return storeToDatabase;
	}
}