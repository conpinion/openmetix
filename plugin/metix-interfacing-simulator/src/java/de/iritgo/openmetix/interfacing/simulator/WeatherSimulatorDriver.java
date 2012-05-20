/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.simulator;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriver;
import java.util.ArrayList;
import java.util.Random;


/**
 * This is a gaging system driver that creates random weather data.
 *
 * @version $Id: WeatherSimulatorDriver.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class WeatherSimulatorDriver
	extends GagingSystemDriver
{
	/** 1. output generates temperatures. */
	private static final int OUTPUT_TEMPERATURE = 0;

	/** 2. output generates wind directions. */
	private static final int OUTPUT_WINDFORCE = 1;

	/** 3. output generates wind directions. */
	private static final int OUTPUT_WINDDIRECTION = 2;

	/** 4. output generates humidity values. */
	private static final int OUTPUT_HUMIDITY = 3;

	/** 5. output generates pressure values. */
	private static final int OUTPUT_PRESSURE = 4;

	/** 6. output generates percipitation values. */
	private static final int OUTPUT_PERCIPITATION = 5;

	/** Random number generator. */
	Random random;

	/**
	 * Create a new instance of the lambrecht synmet driver.
	 */
	public WeatherSimulatorDriver ()
	{
		super("WeatherSimulatorDriver");
	}

	/**
	 * The main driver method reads synmet messages from a serial
	 * port and feeds the sensor values into the measurement processing system.
	 */
	public void run ()
	{
		try
		{
			random = new Random();

			ArrayList outputs = new ArrayList(10);

			for (int index = 0;; ++index)
			{
				String key = "out" + String.valueOf (index);

				if (! system.getDriverProperties ().containsKey (key + "min"))
				{
					break;
				}

				WeatherSimulatorOutput output =
					new WeatherSimulatorOutput(
						NumberTools.toDouble (
							system.getDriverProperties ().getProperty (key + "min"), 0.0),
						NumberTools.toDouble (
							system.getDriverProperties ().getProperty (key + "max"), 0.0),
						NumberTools.toDouble (
							system.getDriverProperties ().getProperty (key + "var"), 0.0));

				outputs.add (output);
			}

			double[] values = new double[outputs.size ()];

			for (int i = 0; i < outputs.size (); ++i)
			{
				WeatherSimulatorOutput output = (WeatherSimulatorOutput) outputs.get (i);

				values[i] =
					random.nextDouble () * (output.getMaximum () - output.getMinimum ()) +
					output.getMinimum ();
			}

			while (running)
			{
				try
				{
					Thread.sleep (
						1000 * NumberTools.toInt (
							(String) system.getDriverProperties ().get ("interval"), 1));

					for (int i = 0; i < outputs.size (); ++i)
					{
						WeatherSimulatorOutput output = (WeatherSimulatorOutput) outputs.get (i);

						values[i] =
							randomize (
								values[i], output.getMinimum (), output.getMaximum (),
								output.getVariation ());

						sendMeasurement (values[i], i);
					}
				}
				catch (InterruptedException x)
				{
				}
			}
		}
		catch (Exception x)
		{
			Log.logError ("server", "WeatherSimulatorDriver", x.toString ());
		}
	}

	/**
	 * Helper method to randomize a measurement value.
	 *
	 * @param value The current value.
	 * @param min Value range minimum value.
	 * @param max Value range maximum value.
	 * @param variation Variation offset.
	 */
	public double randomize (double value, double min, double max, double variation)
	{
		double delta = random.nextGaussian () * variation;

		if (value + delta > max)
		{
			delta = -delta;
		}
		else if (value + delta < min)
		{
			delta = -delta;
		}

		value += delta;
		value = Math.max (min, value);
		value = Math.min (max, value);

		return value;
	}
}