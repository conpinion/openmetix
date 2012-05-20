/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gagingstation.Measurement;


/**
 * Behaviour of average sensors.
 *
 * @version $Id: AverageSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class AverageSensorBehaviour
	extends SimplePeriodSensorBehaviour
{
	/** Number of seen measurements. */
	int numValues;

	/**
	 * Initialize the behaviour.
	 *
	 * This method is called after all behaviour paramaters have been set.
	 */
	public void init ()
	{
		super.init ();
		numValues = 0;
		value = 0.0;
	}

	/**
	 * Called when a new time period was entered.
	 */
	protected void nextTimePeriod ()
	{
		super.nextTimePeriod ();
		numValues = 0;
		value = 0.0;
	}

	/**
	 * Calculate a new accumulated value.
	 *
	 * @param measurement The received measurement.
	 */
	protected void calculateValue (Measurement measurement)
	{
		if (numValues == 0)
		{
			value = measurement.getValue ();
		}
		else
		{
			value =
				(value + (measurement.getValue () / (double) numValues)) * ((double) numValues / (double) (numValues +
				1));
		}

		++numValues;
	}
}