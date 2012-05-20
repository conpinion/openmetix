/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;


/**
 * Specifies the sensor that should be displayed in a round instrument.
 *
 * @version $Id: RoundInstrumentSensor.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class RoundInstrumentSensor
	extends ConfigurationSensor
{
	/**
	 * Create a new RoundInstrumentSensor.
	 */
	public RoundInstrumentSensor ()
	{
		super("RoundInstrumentSensor");
	}
}