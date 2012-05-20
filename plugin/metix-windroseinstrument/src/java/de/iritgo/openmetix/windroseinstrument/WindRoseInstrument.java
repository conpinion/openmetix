/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;
import java.awt.Color;


/**
 * Data object that represents a wind rose instrument.
 *
 * @version $Id: WindRoseInstrument.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class WindRoseInstrument
	extends Instrument
{
	/** The default empty sensor. */
	private WindRoseInstrumentSensor emptySensor = new WindRoseInstrumentSensor();

	/**
	 * Create a new WindRoseInstrumentSensor.
	 */
	public WindRoseInstrument ()
	{
		super("WindRoseInstrument");

		putAttribute ("textColor", new Integer(Color.blue.getRGB ()));
		putAttribute ("font", "Arial-BOLD-48");
		putAttribute ("needleType", new Integer(2));
		putAttribute ("needleColor", new Integer(new Color(204, 0, 51).getRGB ()));
		putAttribute ("roseColor", new Integer(new Color(255, 153, 0).getRGB ()));
		putAttribute ("reverseNeedle", 0);
		putAttribute ("minExtrema", 0);
		putAttribute ("hourExtrema", 0);
		putAttribute ("minExtremaColor", new Integer(new Color(22, 155, 8).getRGB ()));
		putAttribute ("hourExtremaColor", new Integer(new Color(55, 214, 39).getRGB ()));
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
	 * Get the needle type.
	 *
	 * @return The neelde type.
	 */
	public int getNeedleType ()
	{
		return getIntAttribute ("needleType");
	}

	/**
	 * Set the neelde type.
	 *
	 * @param needleType The new needle type.
	 */
	public void setNeedleType (int needleType)
	{
		putAttribute ("needleType", new Integer(needleType));
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

	/**
	 * Get the needle color.
	 *
	 * @return The needle color.
	 */
	public int getNeedleColor ()
	{
		return getIntAttribute ("needleColor");
	}

	/**
	 * Set the needle color.
	 *
	 * @param needleColor The new needle color.
	 */
	public void setNeedleColor (int needleColor)
	{
		putAttribute ("needleColor", needleColor);
	}

	/**
	 * Get the rose color.
	 *
	 * @return The rose color.
	 */
	public int getRoseColor ()
	{
		return getIntAttribute ("roseColor");
	}

	/**
	 * Set the rose color.
	 *
	 * @param roseColor The new rose color.
	 */
	public void setRoseColor (int roseColor)
	{
		putAttribute ("roseColor", roseColor);
	}

	/**
	 * Get the 10-Minutes Extrema value.
	 *
	 * @return The 0 or 1. 0 = false and 1 = true
	 */
	public int getMinExtrema ()
	{
		return getIntAttribute ("minExtrema");
	}

	/**
	 * Set the 10-Minutes Extrema value.  0 = false and 1 = true.
	 *
	 * @param extrema
	 */
	public void setMinExtrema (int extrema)
	{
		putAttribute ("minExtrema", extrema);
	}

	/**
	 * Get the reverse needle value.
	 *
	 * @return Reverse needle value. 0 = false and 1 = true
	 */
	public int getReverseNeedle ()
	{
		return getIntAttribute ("reverseNeedle");
	}

	/**
	 * Set the reverse needle value. 0 = false and 1 = true.
	 *
	 * @param value
	 */
	public void setReverseNeedle (int value)
	{
		putAttribute ("reverseNeedle", value);
	}

	/**
	 * Get the 1-Hour Extrema value.
	 *
	 * @return The 0 or 1. 0 = false and 1 = true
	 */
	public int getHourExtrema ()
	{
		return getIntAttribute ("hourExtrema");
	}

	/**
	 * Set the 1-Hour Extrema value.  0 = false and 1 = true.
	 *
	 * @param extrema
	 */
	public void setHourExtrema (int extrema)
	{
		putAttribute ("hourExtrema", extrema);
	}

	/**
	 * Get the 10-Minutes Extrema color.
	 *
	 * @return 10-Minutes Extrema color
	 */
	public int getMinExtremaColor ()
	{
		return getIntAttribute ("minExtremaColor");
	}

	/**
	 * Set the minExtrema color.
	 *
	 * @param minExtremaColor The new rose color.
	 */
	public void setMinExtremaColor (int minExtremaColor)
	{
		putAttribute ("minExtremaColor", minExtremaColor);
	}

	/**
	 * Get the hourExtrema color.
	 *
	 * @return The hourExtrema color.
	 */
	public int getHourExtremaColor ()
	{
		return getIntAttribute ("hourExtremaColor");
	}

	/**
	 * Set the minExtrema color.
	 *
	 * @param minExtremaColor The new rose color.
	 */
	public void setHourExtremaColor (int hourExtremaColor)
	{
		putAttribute ("hourExtremaColor", hourExtremaColor);
	}
}