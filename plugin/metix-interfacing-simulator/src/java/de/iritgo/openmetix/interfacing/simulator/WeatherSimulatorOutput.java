/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.simulator;



/**
 * An instance of class WeatherSimulatorOutput describes an output
 * configuration of the WeatherSimulator.
 *
 * @version $Id: WeatherSimulatorOutput.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class WeatherSimulatorOutput
{
	/** Minimum value. */
	private double minimum;

	/** Maximum value. */
	private double maximum;

	/** Variation. */
	private double variation;

	/**
	 * Create a new WeatherSimulatorOutput.
	 */
	public WeatherSimulatorOutput ()
	{
		this(0.0, 0.0, 0.0);
	}

	/**
	 * Create a new WeatherSimulatorOutput.
	 *
	 * @param minimum The minimum value.
	 * @param maximum The maximum value.
	 * @param variation The simulated standard variation.
	 */
	public WeatherSimulatorOutput (double minimum, double maximum, double variation)
	{
		this.minimum = minimum;
		this.maximum = maximum;
		this.variation = variation;
	}

	/**
	 * Get the minumum value.
	 *
	 * @return the minumum value.
	 */
	public double getMinimum ()
	{
		return minimum;
	}

	/**
	 * Set the minimum value.
	 *
	 * @param minimum The new minimum value.
	 */
	public void setMinimum (double minimum)
	{
		this.minimum = minimum;
	}

	/**
	 * Get the maximum value.
	 *
	 * @return The maximum value.
	 */
	public double getMaximum ()
	{
		return maximum;
	}

	/**
	 * Set the maximum value.
	 *
	 * @param maximum The new maximum value.
	 */
	public void setMaximum (double maximum)
	{
		this.maximum = maximum;
	}

	/**
	 * Get the simulated standard variation.
	 *
	 * @return The simulated standard variation.
	 */
	public double getVariation ()
	{
		return variation;
	}

	/**
	 * Set the simulated standard variation.
	 *
	 * @param variation The new simulated standard variation.
	 */
	public void setVariation (double variation)
	{
		this.variation = variation;
	}
}