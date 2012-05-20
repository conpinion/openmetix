/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.linechart;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gui.ITimeComboBox;
import de.iritgo.openmetix.app.instrument.Instrument;
import de.iritgo.openmetix.core.Engine;
import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Data object that represents a line chart.
 *
 * @version $Id: LineChart.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class LineChart
	extends Instrument
{
	/** The default empty sensor. */
	LineChartSensor emptySensor = new LineChartSensor();

	/** Axis should run in intervalmode with startdate and enddate */
	static public final int MODE_INTERVAL = 0;

	/** Axis should run in lasttimemode with n last timeunits */
	static public final int MODE_LASTTIME = 1;

	/**
	 * Create a new LineChart.
	 */
	public LineChart ()
	{
		super("LineChart");

		Calendar calendar = new GregorianCalendar();

		calendar.add (Calendar.HOUR_OF_DAY, -1);

		putAttribute ("showDomainRasterLines", new Integer(1));
		putAttribute ("showDomainLabels", new Integer(1));
		putAttribute ("showDomainTickLabels", new Integer(1));
		putAttribute ("showRangeRasterLines", new Integer(1));
		putAttribute ("showRangeLabels", new Integer(1));
		putAttribute ("showRangeTickLabels", new Integer(1));
		putAttribute ("showLegend", new Integer(1));
		putAttribute ("domainAxisMode", new Integer(MODE_LASTTIME));
		putAttribute ("domainStartDate", new Long(calendar.getTimeInMillis ()));
		calendar.add (Calendar.HOUR_OF_DAY, +2);
		putAttribute ("domainStopDate", new Long(calendar.getTimeInMillis ()));
		putAttribute ("currentDomainCount", new Double(5.0));
		putAttribute ("currentDomainUnits", new Integer(ITimeComboBox.PERIOD_MINUTE));
		putAttribute ("gridColor", new Integer(Color.gray.getRGB ()));
		putAttribute ("font", "Arial-PLAIN-10");
	}

	/**
	 * Do we show the domain axis grid lines?
	 *
	 * @return True if the domain axis grid lines should be drawn.
	 */
	public boolean showDomainRasterLines ()
	{
		return getIntAttribute ("showDomainRasterLines") == 1;
	}

	/**
	 * Determine wether to draw the domain axis grid lines or not.
	 *
	 * @param showDomainRasterLines True if the domain axis grid lines should be drawn.
	 */
	public void setShowDomainRasterLines (boolean showDomainRasterLines)
	{
		putAttribute ("showDomainRasterLines", showDomainRasterLines ? 1 : 0);
	}

	/**
	 * Do we show the domain axis labels?
	 *
	 * @return True if the domain axis labels should be drawn.
	 */
	public boolean showDomainLabels ()
	{
		return getIntAttribute ("showDomainLabels") == 1;
	}

	/**
	 * Determine wether to draw the domain axis labels or not.
	 *
	 * @param showDomainLabels True if the domain axis labels should be drawn.
	 */
	public void setShowDomainLabels (boolean showDomainLabels)
	{
		putAttribute ("showDomainLabels", showDomainLabels ? 1 : 0);
	}

	/**
	 * Do we show the domain axis tick labels?
	 *
	 * @return True if the domain axis tick labels should be drawn.
	 */
	public boolean showDomainTickLabels ()
	{
		return getIntAttribute ("showDomainTickLabels") == 1;
	}

	/**
	 * Determine wether to draw the domain axis tick labels or not.
	 *
	 * @param showDomainTickLabels True if the domain axis rick labels should be drawn.
	 */
	public void setShowDomainTickLabels (boolean showDomainTickLabels)
	{
		putAttribute ("showDomainTickLabels", showDomainTickLabels ? 1 : 0);
	}

	/**
	 * Do we show the range axis grid lines?
	 *
	 * @return True if the range axis grid lines should be drawn.
	 */
	public boolean showRangeRasterLines ()
	{
		return getIntAttribute ("showRangeRasterLines") == 1;
	}

	/**
	 * Determine wether to draw the range axis grid lines or not.
	 *
	 * @param showRangeRasterLines True if the range axis grid lines should be drawn.
	 */
	public void setShowRangeRasterLines (boolean showRangeRasterLines)
	{
		putAttribute ("showRangeRasterLines", showRangeRasterLines ? 1 : 0);
	}

	/**
	 * Do we show the range axis labels?
	 *
	 * @return True if the range axis labels should be drawn.
	 */
	public boolean showRangeLabels ()
	{
		return getIntAttribute ("showRangeLabels") == 1;
	}

	/**
	 * Determine wether to draw the range axis labels or not.
	 *
	 * @param showRangeLabels True if the range axis labels should be drawn.
	 */
	public void setShowRangeLabels (boolean showRangeLabels)
	{
		putAttribute ("showRangeLabels", showRangeLabels ? 1 : 0);
	}

	/**
	 * Do we show the range axis tick labels?
	 *
	 * @return True if the range axis tick labels should be drawn.
	 */
	public boolean showRangeTickLabels ()
	{
		return getIntAttribute ("showRangeTickLabels") == 1;
	}

	/**
	 * Determine wether to draw the range axis tick labels or not.
	 *
	 * @param showRangeTickLabels True if the range axis rick labels should be drawn.
	 */
	public void setShowRangeTickLabels (boolean showRangeTickLabels)
	{
		putAttribute ("showRangeTickLabels", showRangeTickLabels ? 1 : 0);
	}

	/**
	 * Do we show the legend?
	 *
	 * @return True if the legend should be drawn.
	 */
	public boolean showLegend ()
	{
		return getIntAttribute ("showLegend") == 1;
	}

	/**
	 * Determine wether to draw the legend or not.
	 *
	 * @param showLegend True if the legend should be drawn.
	 */
	public void setShowLegend (boolean showLegend)
	{
		putAttribute ("showLegend", showLegend ? 1 : 0);
	}

	/**
	 * Get the domain axis mode.
	 *
	 * @return The domain axis mode.
	 */
	public int getDomainAxisMode ()
	{
		return getIntAttribute ("domainAxisMode");
	}

	/**
	 * Set the domain axis mode.
	 *
	 * @param domainAxisMode The new domain axis mode.
	 */
	public void setDomainAxisMode (int domainAxisMode)
	{
		putAttribute ("domainAxisMode", new Integer(domainAxisMode));
	}

	/**
	 * Get the start date in milliseconds for the intervall mode.
	 *
	 * @return The start date in milliseconds.
	 */
	public long getDomainStartDate ()
	{
		return getLongAttribute ("domainStartDate");
	}

	/**
	 * Set the start date in milliseconds for the intervall mode.
	 *
	 * @param domainStartDate The new start date in milliseconds.
	 */
	public void setDomainStartDate (long domainStartDate)
	{
		putAttribute ("domainStartDate", new Long(domainStartDate));
	}

	/**
	 * Get the end date in milliseconds for the intervall mode.
	 *
	 * @return The end date in milliseconds.
	 */
	public long getDomainStopDate ()
	{
		return getLongAttribute ("domainStopDate");
	}

	/**
	 * Set the end date in milliseconds for the intervall mode.
	 *
	 * @param domainStopDate The new end date in milliseconds.
	 */
	public void setDomainStopDate (long domainStopDate)
	{
		putAttribute ("domainStopDate", new Long(domainStopDate));
	}

	/**
	 * Get the number of the last units of the last time mode.
	 *
	 * @return The number of the last domain units.
	 */
	public double getCurrentDomainCount ()
	{
		return getDoubleAttribute ("currentDomainCount");
	}

	/**
	 * Set the number of the last units of the last time mode.
	 *
	 * @param currentDomainCount The new number of the last domain units.
	 */
	public void setCurrentDomainCount (double currentDomainCount)
	{
		putAttribute ("currentDomainCount", new Double(currentDomainCount));
	}

	/**
	 * Get the domain unit for the last time mode.
	 *
	 * @return The domain unit.
	 */
	public int getCurrentDomainUnits ()
	{
		return getIntAttribute ("currentDomainUnits");
	}

	/**
	 * Set the domain unit for the last time mode.
	 *
	 * @param currentDomainUnits The new domain unit.
	 */
	public void setCurrentDomainUnits (int currentDomainUnits)
	{
		putAttribute ("currentDomainUnits", new Integer(currentDomainUnits));
	}

	/**
	 * Get the grid color.
	 *
	 * @return The grid color.
	 */
	public int getGridColor ()
	{
		return getIntAttribute ("gridColor");
	}

	/**
	 * Set the grid color.
	 *
	 * @param gridColor The new grid color.
	 */
	public void setGridColor (int gridColor)
	{
		putAttribute ("gridColor", gridColor);
	}

	/**
	 * Get the chart font.
	 *
	 * @return The chart font.
	 */
	public String getFont ()
	{
		return getStringAttribute ("font");
	}

	/**
	 * Set the chart font.
	 *
	 * @param font The new chart font.
	 */
	public void setFont (String font)
	{
		putAttribute ("font", font);
	}

	/**
	 * Get a formatted axis label string.
	 *
	 * @return The axis label.
	 */
	public String getFormattedAxisLabel ()
	{
		return Engine.instance ().getResourceService ().getStringWithoutException (
			"linechart.timescale");
	}

	/**
	 * Get history count in milliseconds (end date - start date or last x time units).
	 *
	 * @return The history count.
	 */
	public long getHistoryCount ()
	{
		if (getDomainAxisMode () == MODE_INTERVAL)
		{
			return getDomainStopDate () - getDomainStartDate ();
		}

		long timeMult = ITimeComboBox.convert (getCurrentDomainUnits ());

		return (long) getCurrentDomainCount () * timeMult;
	}

	/**
	 * Check if a given time stamp is valid (for the interval mode it must lie between
	 * the start and the end date).
	 *
	 * @return True fot a valid time stamp.
	 */
	public boolean isValidTimeStamp (long millis)
	{
		if (getDomainAxisMode () != LineChart.MODE_INTERVAL)
		{
			return true;
		}

		return millis < getDomainStopDate () && millis > getDomainStartDate ();
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