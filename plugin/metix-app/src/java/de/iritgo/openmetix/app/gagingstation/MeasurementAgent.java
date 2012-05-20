/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation;



/**
 * The MeasurementAgent interface is implemented by classes that
 * want to work on measurement values.
 *
 * @version $Id: MeasurementAgent.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public interface MeasurementAgent
{
	/**
	 * Called when a new measurement was received.
	 *
	 * @param measurement The measurement information.
	 */
	public void receiveSensorValue (Measurement measurement);
}