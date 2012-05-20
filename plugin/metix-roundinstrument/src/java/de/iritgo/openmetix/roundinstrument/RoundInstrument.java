/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;
import java.awt.Color;


/**
 * Data object that represents a meter instrument.
 *
 * @version $Id: RoundInstrument.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class RoundInstrument
	extends Instrument
{
	/** The default empty sensor. */
	private RoundInstrumentSensor emptySensor = new RoundInstrumentSensor();

	/** A 360 degree meter. */
	public static final int MODE_FULL = 0;

	/** A 180 degree facing up meter. */
	public static final int MODE_HALF = 1;

	/** A 180 degree facing left meter. */
	public static final int MODE_HALFLEFT = 2;

	/** A 180 degree facing right meter. */
	public static final int MODE_HALFRIGHT = 3;

	/** Division is number of divisions. */
	public static final int DIVISION_NUM = 0;

	/** Division is a range. */
	public static final int DIVISION_RANGE = 1;

	/**
	 * Create a new RoundInstrument.
	 */
	public RoundInstrument ()
	{
		super("RoundInstrument");

		putAttribute ("scalingLow", new Double(0.0));
		putAttribute ("scalingHigh", new Double(100.0));
		putAttribute ("showMinMarker", new Integer(0));
		putAttribute ("showMaxMarker", new Integer(0));
		putAttribute ("showDigital", new Integer(1));
		putAttribute ("displayMode", new Integer(MODE_FULL));
		putAttribute ("font", "Arial-PLAIN-14");
		putAttribute ("needleColor", new Integer(new Color(51, 51, 255).getRGB ()));
		putAttribute ("division", new Double(10.0));
		putAttribute ("divisionType", new Integer(DIVISION_NUM));
	}

	/**
	 * Check wether the instrument should display digital values.
	 *
	 * @return True for a digital display.
	 */
	public boolean showDigital ()
	{
		return getIntAttribute ("showDigital") == 1;
	}

	/**
	 * Determine wether the instrument should display digital values.
	 *
	 * @param showDigital True for a digital display.
	 */
	public void setShowDigital (boolean showDigital)
	{
		putAttribute ("showDigital", showDigital ? 1 : 0);
	}

	/**
	 * Check wether the minimum marker should be drwan.
	 *
	 * @return True if the minimum marker is drawn.
	 */
	public boolean showMinMarker ()
	{
		return getIntAttribute ("showMinMarker") == 1;
	}

	/**
	 * Determine wether the minimum marker should be drwan.
	 *
	 * @param showMinMarker True if the minimum should be drawn.
	 */
	public void setShowMinMarker (boolean showMinMarker)
	{
		putAttribute ("showMinMarker", showMinMarker ? 1 : 0);
	}

	/**
	 * Check wether the maximum marker should be drwan.
	 *
	 * @return True if the maximum marker is drawn.
	 */
	public boolean showMaxMarker ()
	{
		return getIntAttribute ("showMaxMarker") == 1;
	}

	/**
	 * Determine wether the maximum marker should be drwan.
	 *
	 * @param showMaxMarker True if the maximum should be drawn.
	 */
	public void setShowMaxMarker (boolean showMaxMarker)
	{
		putAttribute ("showMaxMarker", showMaxMarker ? 1 : 0);
	}

	/**
	 * Get the display mode.
	 *
	 * @return The display mode.
	 */
	public int getDisplayMode ()
	{
		return getIntAttribute ("displayMode");
	}

	/**
	 * Set the display mode.
	 *
	 * @param mode The new display mode.
	 */
	public void setDisplayMode (int mode)
	{
		putAttribute ("displayMode", mode);
	}

	/**
	 * Get the minimum axis value.
	 *
	 * @return The minimum axis value.
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
	 * Get the axis division.
	 *
	 * @return The axis division.
	 */
	public double getDivision ()
	{
		return getDoubleAttribute ("division");
	}

	/**
	 * Set the axis division.
	 *
	 * @param division The new axis division.
	 */
	public void setDivision (double division)
	{
		putAttribute ("division", division);
	}

	/**
	 * Get the division type.
	 *
	 * @return The division type.
	 */
	public int getDivisionType ()
	{
		return getIntAttribute ("divisionType");
	}

	/**
	 * Set the division type.
	 *
	 * @param divisionType The new division type.
	 */
	public void setDivisionType (int divisionType)
	{
		putAttribute ("divisionType", divisionType);
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