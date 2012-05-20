/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.app.gui.ITimeComboBox;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Base class for sensor behaviours that calculate a sensor value
 * from another sensor and a single time period.
 *
 * @version $Id: SimplePeriodSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class SimplePeriodSensorBehaviour
	extends GagingSensorBehaviour
{
	/** Current accumulated value. */
	protected double value;

	/** Timestamp at which the next period starts. */
	protected long startOfNextPeriod;

	/** Period lenght in milliseconds. */
	protected long periodLength;

	/**
	 * Initialize the behaviour.
	 *
	 * This method is called after all behaviour paramaters have been set.
	 */
	public void init ()
	{
		super.init ();

		Calendar cal = new GregorianCalendar();

		DateFormat df =
			DateFormat.getDateTimeInstance (DateFormat.FULL, DateFormat.FULL, Locale.GERMAN);

		switch (sensor.getPeriodType ())
		{
			case ITimeComboBox.PERIOD_YEAR:
				cal.set (Calendar.DAY_OF_YEAR, 1);

			case ITimeComboBox.PERIOD_MONTH:
				cal.set (Calendar.DAY_OF_MONTH, 1);

			case ITimeComboBox.PERIOD_WEEK:
				cal.set (Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			case ITimeComboBox.PERIOD_DAY:
				cal.set (Calendar.HOUR_OF_DAY, 0);

			case ITimeComboBox.PERIOD_HOUR:
				cal.set (Calendar.MINUTE, 0);

			case ITimeComboBox.PERIOD_SECOND:
			case ITimeComboBox.PERIOD_MINUTE:
				cal.set (Calendar.SECOND, 0);
		}

		periodLength = ITimeComboBox.convert (sensor.getPeriodType ());
		startOfNextPeriod = cal.getTime ().getTime () + periodLength;
	}

	/**
	 * Receive a new measurement.
	 *
	 * @param measurement The measurement.
	 */
	public void receiveSensorValue (Measurement measurement)
	{
		calculateValue (measurement);

		if (measurement.getTimestamp ().getTime () >= startOfNextPeriod)
		{
			super.receiveSensorValue (
				new Measurement(
					new Timestamp(startOfNextPeriod), value, station.getUniqueId (),
					sensor.getUniqueId (), measurement.isStoreToDatabase ()));

			nextTimePeriod ();
		}
	}

	/**
	 * Called when a new time period was entered.
	 */
	protected void nextTimePeriod ()
	{
		startOfNextPeriod += periodLength;

		DateFormat df =
			DateFormat.getDateTimeInstance (DateFormat.FULL, DateFormat.FULL, Locale.GERMAN);
		Date date = new Date(startOfNextPeriod);
	}

	/**
	 * Calculate a new accumulated value.
	 *
	 * @param measurement The received measurement.
	 */
	protected void calculateValue (Measurement measurement)
	{
	}
}