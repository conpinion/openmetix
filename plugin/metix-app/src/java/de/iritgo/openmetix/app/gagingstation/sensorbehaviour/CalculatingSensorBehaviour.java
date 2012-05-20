/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.sensorbehaviour;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.core.tools.NumberTools;
import java.sql.Timestamp;
import java.util.StringTokenizer;


/**
 * Behaviour of calculating sensors.
 *
 * @version $Id: CalculatingSensorBehaviour.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class CalculatingSensorBehaviour
	extends GagingSensorBehaviour
{
	/** Tracks which inputs are already received. */
	protected boolean[] inputReceived;

	/** Input sensor to index mapping. */
	protected long[] inputSensorIds;

	/** Stored measurements. */
	protected Measurement[] measurements;

	/** Number of inputs. */
	protected int numInputs;

	/**
	 * Set the gaging sensor.
	 *
	 * @param sensor The new sensor.
	 */
	public void setSensor (GagingSensor sensor)
	{
		super.setSensor (sensor);

		numInputs = sensor.getInputCount ();

		inputReceived = new boolean[numInputs];
		inputSensorIds = new long[numInputs];
		measurements = new Measurement[numInputs];

		StringTokenizer st = new StringTokenizer(sensor.getInputs (), ",");

		for (int i = 0; i < numInputs; ++i)
		{
			inputSensorIds[i] = NumberTools.toLong (st.nextToken (), 0);
		}
	}

	/**
	 * Receive a new measurement.
	 *
	 * @param measurement The measurement.
	 */
	public void receiveSensorValue (Measurement measurement)
	{
		for (int i = 0; i < numInputs; ++i)
		{
			if (measurement.getSensorId () == inputSensorIds[i])
			{
				inputReceived[i] = true;
				measurements[i] = measurement;
			}
		}

		boolean receivedAll = true;

		for (int i = 0; i < numInputs; ++i)
		{
			receivedAll = receivedAll && inputReceived[i];
		}

		if (receivedAll)
		{
			boolean storeToDatabase = false;

			for (int i = 0; i < numInputs; ++i)
			{
				storeToDatabase = storeToDatabase || measurements[i].isStoreToDatabase ();
			}

			double value = calculateValue (measurements);

			super.receiveSensorValue (
				new Measurement(
					new Timestamp(System.currentTimeMillis ()), value, station.getUniqueId (),
					sensor.getUniqueId (), storeToDatabase));

			for (int i = 0; i < numInputs; ++i)
			{
				inputReceived[i] = false;
				measurements[i] = null;
			}
		}
	}

	/**
	 * Calculate a new value.
	 *
	 * @param measurements The received measurements.
	 */
	protected double calculateValue (Measurement[] measurements)
	{
		return 0.0;
	}
}