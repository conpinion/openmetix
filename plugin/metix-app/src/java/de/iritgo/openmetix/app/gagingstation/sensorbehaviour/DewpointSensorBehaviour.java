/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gagingstation.Measurement;


/**
 * Behaviour of dewpoint sensors.
 *
 * @version $Id: DewpointSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class DewpointSensorBehaviour
	extends CalculatingSensorBehaviour
{
	/**
	 * Calculate a new value.
	 *
	 * @param measurements The received measurements.
	 */
	protected double calculateValue (Measurement[] measurements)
	{
		double T = measurements[0].getValue ();

		double r = measurements[1].getValue ();

		double DT = 0.0;

		double SVP = 0.0;

		double c1 = 6.1078;

		double c2 = 0.0;

		if (T < 0)
		{
			c2 = 17.84362;
		}
		else
		{
			c2 = 17.08085;
		}

		double c3 = 0.0;

		if (T < 0)
		{
			c3 = 245.425;
		}
		else
		{
			c3 = 234.175;
		}

		SVP = c1 * Math.exp ((c2 * T) / (c3 + T));

		DT = c3 * ((Math.log (0.01 * r * (SVP / c1))) / (c2 - Math.log (0.01 * r * (SVP / c1))));

		return DT;
	}
}