/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.util;


import java.util.Date;


/**
 * Use the TimeConverter to convert times between different units.
 *
 * @version $Id: TimeConverter.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class TimeConverter
{
	/** First date of the series of average values. */
	Date firstDate = null;

	/** The last date of the series of average values. */
	Date lastDate = null;

	/** Average value.*/
	double averageValue = 0.0;

	/** Count of values passed for average value. */
	int countValues = 0;

	/** The ratio in milliseconds of the timeconverter. */
	long ratio;

	/**
	 * Create a new TimeConverter.
	 *
	 * @param ratio The convert ratio in milliseconds. Use 0 for no conversion.
	 */
	public TimeConverter (long ratio)
	{
		this.ratio = ratio;
	}

	/**
	 * Add a new value with date to the converter.
	 *
	 * @param value The value to be added.
	 * @param date The date of the value.
	 * @return True, if a new value is available.
	 */
	public boolean putValue (double value, Date date)
	{
		if (countValues == 0)
		{
			firstDate = date;
			averageValue = value;
			countValues = 1;
		}
		else
		{
			averageValue = (averageValue * countValues + value) / ++countValues;
		}

		lastDate = date;

		long diff = lastDate.getTime () - firstDate.getTime ();

		return diff >= ratio;
	}

	/**
	 * Get the average value.
	 *
	 * @return The average value.
	 */
	public double getAverageValue ()
	{
		return averageValue;
	}

	/**
	 * Get the average timestamp.
	 *
	 * @return The average timestamp.
	 */
	public Date getAverageDate ()
	{
		return new Date((lastDate.getTime () + firstDate.getTime ()) / 2);
	}

	/**
	 * Reset the converter.
	 */
	public void reset ()
	{
		countValues = 0;
	}
}