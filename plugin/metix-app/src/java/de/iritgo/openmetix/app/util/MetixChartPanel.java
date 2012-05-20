/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.util;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


/**
 * @version $Id: MetixChartPanel.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class MetixChartPanel
	extends ChartPanel
{
	public MetixChartPanel (JFreeChart chart)
	{
		super(chart);
	}

	public MetixChartPanel (JFreeChart chart, boolean useBuffer)
	{
		super(chart, useBuffer);
	}

	public MetixChartPanel (
		JFreeChart chart, boolean properties, boolean save, boolean print, boolean zoom,
		boolean tooltips)
	{
		super(chart, properties, save, print, zoom, tooltips);
	}

	public MetixChartPanel (
		JFreeChart chart, int width, int height, int minimumDrawWidth, int minimumDrawHeight,
		int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer, boolean properties,
		boolean save, boolean print, boolean zoom, boolean tooltips)
	{
		super(
			chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth,
			maximumDrawHeight, useBuffer, properties, save, print, zoom, tooltips);
	}

	public void paintComponent (java.awt.Graphics g)
	{
		try
		{
			super.paintComponent (g);
		}
		catch (Exception ignored)
		{
		}
	}
}