/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;


/**
 * Specifies the sensor that should be displayed in a bar instrument.
 *
 * @version $Id: BarInstrumentSensor.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class BarInstrumentSensor
	extends ConfigurationSensor
{
	/**
	 * Create a new BarInstrumentSensor.
	 */
	public BarInstrumentSensor ()
	{
		super("BarInstrumentSensor");
	}
}