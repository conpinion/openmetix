/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;
import java.awt.Color;


/**
 * Data object that represents a bar instrument.
 *
 * @version $Id: BarInstrument.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class BarInstrument
	extends Instrument
{
	/** The default empty sensor. */
	private BarInstrumentSensor emptySensor = new BarInstrumentSensor();

	/**
	 * Create a new BarInstrument.
	 */
	public BarInstrument ()
	{
		super("BarInstrument");

		putAttribute ("scalingLow", new Double(-15.0));
		putAttribute ("scalingHigh", new Double(100.0));
		putAttribute ("axisLogarithmic", new Integer(0));
		putAttribute ("showMaxMarker", new Integer(0));
		putAttribute ("showMinMarker", new Integer(0));
		putAttribute ("showAxisLabel", new Integer(1));
		putAttribute ("showScaleLabel", new Integer(1));
		putAttribute ("barColor", new Integer(new Color(0, 102, 153).getRGB ()));
		putAttribute ("font", "Arial-PLAIN-14");
	}

	/**
	 * Get the minimum axis value.
	 *
	 * @return The the minimum axis value.
	 */
	public double getScalingLow ()
	{
		return getDoubleAttribute ("scalingLow");
	}

	/**
	 * Set the minimum axis value.
	 *
	 * @param scalingLow The new minimum axis value.
	 */
	public void setScalingLow (double scalingLow)
	{
		putAttribute ("scalingLow", scalingLow);
	}

	/**
	 * Get the maximum axis value.
	 *
	 * @return The maximum axis value.
	 */
	public double getScalingHigh ()
	{
		return getDoubleAttribute ("scalingHigh");
	}

	/**
	 * Set the maximum axis value.
	 *
	 * @param scalingHigh The new maximum axis value.
	 */
	public void setScalingHigh (double scalingHigh)
	{
		putAttribute ("scalingHigh", scalingHigh);
	}

	/**
	 * Get the logarithmic axis flag.
	 *
	 * @return The logarithmic axis flag.
	 */
	public boolean isAxisLogarithmic ()
	{
		return getIntAttribute ("axisLogarithmic") == 1;
	}

	/**
	 * Set the logarithmic axis flag.
	 *
	 * @param axisLogarithmic The new logarithmic axis flag.
	 */
	public void setAxisLogarithmic (boolean axisLogarithmic)
	{
		putAttribute ("axisLogarithmic", axisLogarithmic ? 1 : 0);
	}

	/**
	 * Check wether a maximum marker should be drawn
	 *
	 * @return True if a maximum marker should be drawn.
	 */
	public boolean showMaxMarker ()
	{
		return getIntAttribute ("showMaxMarker") == 1;
	}

	/**
	 * Decide wether maximum marker should be drawn or not.
	 *
	 * @param showMaxMarker True if a maximum marker should be drawn.
	 */
	public void setShowMaxMarker (boolean showMaxMarker)
	{
		putAttribute ("showMaxMarker", showMaxMarker ? 1 : 0);
	}

	/**
	 * Check wether a minimum marker should be drawn
	 *
	 * @return True if a minimum marker should be drawn.
	 */
	public boolean showMinMarker ()
	{
		return getIntAttribute ("showMinMarker") == 1;
	}

	/**
	 * Decide wether minimum marker should be drawn or not.
	 *
	 * @param showMinMarker True if a minimum marker should be drawn.
	 */
	public void setShowMinMarker (boolean showMinMarker)
	{
		putAttribute ("showMinMarker", showMinMarker ? 1 : 0);
	}

	/**
	 * Check wether the axis label should be drawn
	 *
	 * @return True if the axis label should be drawn.
	 */
	public boolean showAxisLabel ()
	{
		return getIntAttribute ("showAxisLabel") == 1;
	}

	/**
	 * Decide wether the axis label should be drawn or not.
	 *
	 * @param showAxisLabel True if the axis label should be drawn.
	 */
	public void setShowAxisLabel (boolean showAxisLabel)
	{
		putAttribute ("showAxisLabel", showAxisLabel ? 1 : 0);
	}

	/**
	 * Check wether the scale label should be drawn
	 *
	 * @return True if the scale label should be drawn.
	 */
	public boolean showScaleLabel ()
	{
		return getIntAttribute ("showScaleLabel") == 1;
	}

	/**
	 * Decide wether the scale label should be drawn or not.
	 *
	 * @param showScaleLabel True if the scale label should be drawn.
	 */
	public void setShowScaleLabel (boolean showScaleLabel)
	{
		putAttribute ("showScaleLabel", showScaleLabel ? 1 : 0);
	}

	/**
	 * Get the bar color.
	 *
	 * @return The bar color.
	 */
	public int getBarColor ()
	{
		return getIntAttribute ("barColor");
	}

	/**
	 * Set the bar color.
	 *
	 * @param barColor The new bar color.
	 */
	public void setBarColor (int barColor)
	{
		putAttribute ("barColor", barColor);
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