/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gagingstation.Measurement;


/**
 * Behaviour of system sensors.
 *
 * @version $Id: ManualSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class ManualSensorBehaviour
	extends GagingSensorBehaviour
{
	/**
	 * Receive a new measurement.
	 *
	 * @param measurement The measurement.
	 */
	public void receiveSensorValue (Measurement measurement)
	{
		super.receiveSensorValue (measurement);
	}
}