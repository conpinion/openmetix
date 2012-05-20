/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.linechart;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;


/**
 * LineChartSensor
 *
 * @version $Id: LineChartSensor.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class LineChartSensor
	extends ConfigurationSensor
{
	/** Modes of the axis. */
	public final static int MODE_AUTO = 0;
	public final static int MODE_MANUAL = 1;
	public final static int MODE_TAKEFROM = 2;

	/**
	 * Constructor
	 */
	public LineChartSensor ()
	{
		super("LineChartSensor");

		putAttribute ("title", new String("Sensor"));
		putAttribute ("sensorNr", new Integer(-1));
		putAttribute ("axisRangeMode", new Integer(MODE_AUTO));
		putAttribute ("axisRangeStart", new Double(-10.0));
		putAttribute ("axisRangeStop", new Double(10.0));
		putAttribute ("axisTakeFrom", new Integer(0));
	}

	/**
	 * Get the title of the sensorconfig.
	 */
	public String getTitle ()
	{
		return getStringAttribute ("title");
	}

	/**
	 * Set the title of the sensorconfig.
	 */
	public void setTitle (String title)
	{
		putAttribute ("title", title);
	}

	/**
	 * Get the axismode.
	 */
	public int getAxisRangeMode ()
	{
		return getIntAttribute ("axisRangeMode");
	}

	/**
	 * Set the axismode.
	 */
	public void setAxisRangeMode (int mode)
	{
		putAttribute ("axisRangeMode", mode);
	}

	/**
	 * Get start of axis.
	 */
	public double getAxisRangeStart ()
	{
		return getDoubleAttribute ("axisRangeStart");
	}

	/**
	 * Set start of axis.
	 */
	public void setAxisRangeStart (double start)
	{
		putAttribute ("axisRangeStart", start);
	}

	/**
	 * Get stop of axis.
	 */
	public double getAxisRangeStop ()
	{
		return getDoubleAttribute ("axisRangeStop");
	}

	/**
	 * Set stop of axis.
	 */
	public void setAxisRangeStop (double stop)
	{
		putAttribute ("axisRangeStop", stop);
	}

	/**
	 * Get the sensornr for the MODE_TAKEFROM.
	 */
	public int getAxisTakeFrom ()
	{
		return getIntAttribute ("axisTakeFrom");
	}

	/**
	 * Set the sensornr for the MODE_TAKEFROM.
	 */
	public void setAxisTakeFrom (int nr)
	{
		putAttribute ("axisTakeFrom", nr);
	}

	/**
	 * Get the sensornr.
	 */
	public int getSensorNr ()
	{
		return getIntAttribute ("sensorNr");
	}

	/**
	 * Set the sensornr.
	 */
	public void setSensorNr (int nr)
	{
		putAttribute ("sensorNr", nr);
	}

	/**
	 * Override, display title.
	 */
	public String toString ()
	{
		return getTitle ();
	}
}