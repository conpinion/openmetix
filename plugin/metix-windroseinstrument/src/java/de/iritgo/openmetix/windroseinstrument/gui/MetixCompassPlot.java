/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument.gui;


import de.iritgo.openmetix.core.logger.Log;
import org.jfree.chart.plot.CompassPlot;
import org.jfree.data.ValueDataset;
import java.awt.Font;
import java.awt.Paint;
import java.lang.reflect.Field;


/**
 * A custom compass plot
 *
 * @version $Id: MetixCompassPlot.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class MetixCompassPlot
	extends CompassPlot
{
	/**
	 * Create a new MetixCompassPlot.
	 */
	public MetixCompassPlot ()
	{
	}

	/**
	 * Constructs a new compass plot.
	 *
	 * @param dataset The dataset for the plot.
	 */
	public MetixCompassPlot (ValueDataset dataset)
	{
		super(dataset);
	}

	/**
	 * Set the rose paint.
	 * This method uses reflection since the rose paint field in the base
	 * class has private access.
	 *
	 * @param paint The new paint.
	 */
	public void setRosePaint (Paint paint)
	{
		try
		{
			Field rosePaint = getClass ().getSuperclass ().getDeclaredField ("rosePaint");

			rosePaint.setAccessible (true);
			rosePaint.set (this, paint);
		}
		catch (Exception x)
		{
			Log.logError (
				"client", "MetixCompassPlot.setRosePaint",
				"Unable to set the rosePaint in the super class. " +
				"Eventually this is an incompatibilty with a new JFreeChart version: " + x);
		}
	}

	/**
	 * Sets the series paint.
	 *
	 * @param paint The new paint.
	 */
	public void setSeriesPaint (int pos, Paint paint)
	{
		super.setSeriesPaint (pos, paint);
	}

	/**
	 * Set the compass font.
	     * This method uses reflection since the rose paint field in the base
	     * class has private access.
	 *
	 * @param font The compass font.
	 */
	public void setCompassFont (Font font)
	{
		try
		{
			Field compassFont = getClass ().getSuperclass ().getDeclaredField ("compassFont");

			compassFont.setAccessible (true);
			compassFont.set (this, font);
		}
		catch (Exception x)
		{
			Log.logError (
				"client", "MetixCompassPlot.setRosePaint",
				"Unable to set the rosePaint in the super class. " +
				"Eventually this is an incompatibilty with a new JFreeChart version: " + x);
		}
	}
}