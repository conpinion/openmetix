/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.simpleinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;
import java.awt.Color;


/**
 * Data object that represents a simple digital instrument.
 *
 * @version $Id: SimpleInstrument.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class SimpleInstrument
	extends Instrument
{
	/** The default empty sensor. */
	private SimpleInstrumentSensor emptySensor = new SimpleInstrumentSensor();

	/**
	 * Create a new SimpleInstrumentSensor.
	 */
	public SimpleInstrument ()
	{
		super("SimpleInstrument");

		putAttribute ("textColor", new Integer(Color.blue.getRGB ()));
		putAttribute ("backgroundColor", new Integer(Color.white.getRGB ()));
		putAttribute ("transparent", new Integer(1));
		putAttribute ("font", "Arial-BOLD-48");
	}

	/**
	 * Get the instrument color.
	 *
	 * @return The instrument color.
	 */
	public int getTextColor ()
	{
		return getIntAttribute ("textColor");
	}

	/**
	 * Set the instrument color.
	 *
	 * @param textColor The new instrument color.
	 */
	public void setTextColor (int textColor)
	{
		putAttribute ("textColor", textColor);
	}

	/**
	 * Get the instrument background color.
	 *
	 * @return The background color.
	 */
	public int getBackgroundColor ()
	{
		return getIntAttribute ("backgroundColor");
	}

	/**
	 * Set the instrument background color.
	 *
	 * @param backgroundColor The new background color.
	 */
	public void setBackgroundColor (int backgroundColor)
	{
		putAttribute ("backgroundColor", backgroundColor);
	}

	/**
	 * Check wether the instrument background is transparent.
	 *
	 * @return True for a transparent background.
	 */
	public boolean isTransparent ()
	{
		return getIntAttribute ("transparent") != 0;
	}

	/**
	 * Set the background transparency.
	 *
	 * @param transparent True for a transparent background.
	 */
	public void setTransparent (boolean transparent)
	{
		putAttribute ("transparent", transparent ? 1 : 0);
	}

	/**
	 * Get the instrument font.
	 *
	 * @return The instrument font.
	 */
	public String getFont ()
	{
		return getStringAttribute ("font");
	}

	/**
	 * Set the instrument font.
	 *
	 * @param font The new instrument font.
	 */
	public void setFont (String font)
	{
		putAttribute ("font", font);
	}

	/**
	 * Get the default empty sensor.
	 *
	 * @return The empty sensor.
	 */
	public ConfigurationSensor getTmpSensor ()
	{
		return emptySensor;
	}
}