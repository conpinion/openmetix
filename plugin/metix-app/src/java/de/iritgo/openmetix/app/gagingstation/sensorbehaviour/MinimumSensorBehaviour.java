/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gagingstation.Measurement;


/**
 * Behaviour of minimum sensors.
 *
 * @version $Id: MinimumSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class MinimumSensorBehaviour
	extends SimplePeriodSensorBehaviour
{
	/**
	 * Initialize the behaviour.
	 *
	 * This method is called after all behaviour paramaters have been set.
	 */
	public void init ()
	{
		super.init ();
		value = Double.MAX_VALUE;
	}

	/**
	 * Called when a new time period was entered.
	 */
	protected void nextTimePeriod ()
	{
		super.nextTimePeriod ();
		value = Double.MAX_VALUE;
	}

	/**
	 * Calculate a new accumulated value.
	 *
	 * @param measurement The received measurement.
	 */
	protected void calculateValue (Measurement measurement)
	{
		value = Math.min (value, measurement.getValue ());
	}
}