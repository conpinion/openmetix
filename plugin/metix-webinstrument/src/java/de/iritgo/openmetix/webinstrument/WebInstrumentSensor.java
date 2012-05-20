/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.webinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;


/**
 * Specifies the sensor that should be displayed in a web instrument.
 *
 * @version $Id: WebInstrumentSensor.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class WebInstrumentSensor
	extends ConfigurationSensor
{
	/**
	 * Create a new WebInstrumentSensor.
	 */
	public WebInstrumentSensor ()
	{
		super("WebInstrumentSensor");
	}
}