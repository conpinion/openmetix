/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.webinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;


/**
 * Data object that represents a web digital instrument.
 *
 * @version $Id: WebInstrument.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class WebInstrument
	extends Instrument
{
	/** The default empty sensor. */
	private WebInstrumentSensor emptySensor = new WebInstrumentSensor();

	/**
	 * Create a new WebInstrumentSensor.
	 */
	public WebInstrument ()
	{
		super("WebInstrument");

		putAttribute ("url", "");
		putAttribute ("reloadInterval", new Integer(-10));
	}

	/**
	 * Get the url.
	 *
	 * @return The url.
	 */
	public String getUrl ()
	{
		return getStringAttribute ("url");
	}

	/**
	 * Set the url.
	 *
	 * @param url The url.
	 */
	public void setUrl (String url)
	{
		putAttribute ("url", url);
	}

	/**
	 * Get the reload interval (0 means no reloading).
	 *
	 * @return The reload interval.
	 */
	public int getReloadInterval ()
	{
		return getIntAttribute ("reloadInterval");
	}

	/**
	 * Set the reload interval (0 means no reloading).
	 *
	 * @param reloadInterval The new reload interval..
	 */
	public void setReloadInterval (int reloadInterval)
	{
		putAttribute ("reloadInterval", reloadInterval);
	}

	/**
	 * Get the default empty sensor.
	 *
	 * @return The empty sensor.
	 */
	public ConfigurationSensor getTmpSensor ()
	{
		return null;
	}
}