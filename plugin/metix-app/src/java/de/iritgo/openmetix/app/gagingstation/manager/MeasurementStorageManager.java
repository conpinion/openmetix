/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation.manager;


import de.iritgo.openmetix.app.gagingstation.Measurement;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.apache.avalon.framework.configuration.Configuration;
import java.sql.Timestamp;
import java.util.Properties;


/**
 * The measurement storage manager handles the database storage of measurements.
 *
 * @version $Id: MeasurementStorageManager.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class MeasurementStorageManager
	extends BaseObject
	implements Manager
{
	/** Measurement caches (timestamps). */
	private Timestamp[][] cacheAt;

	/** Measurement caches (measurements). */
	private Double[][] cacheValue;

	/** Measurement caches (station ids). */
	private Long[][] cacheStationId;

	/** Measurement caches (sensor ids). */
	private Long[][] cacheSensorId;

	/** Total number of caches. */
	private static final int NUM_CACHES = 2;

	/** Index of the currently used cache. */
	private int activeCache;

	/** Index of the next free cache element. */
	private int cacheIndex;

	/** Cache size. */
	private int batchSize;

	/**
	 * Create a new MeasurementStorageManager.
	 */
	public MeasurementStorageManager ()
	{
		super("MeasurementStorageManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		batchSize = 6 * 60;

		try
		{
			Configuration config = Engine.instance ().getConfiguration ();

			Configuration measurementConfig = config.getChild ("metix-measurement");

			batchSize =
				NumberTools.toInt (
					measurementConfig.getChild ("batch-size").getValue (), batchSize);
		}
		catch (Exception x)
		{
		}

		cacheAt = new Timestamp[NUM_CACHES][batchSize];
		cacheValue = new Double[NUM_CACHES][batchSize];
		cacheStationId = new Long[NUM_CACHES][batchSize];
		cacheSensorId = new Long[NUM_CACHES][batchSize];
	}

	/**
	 * Free all manager resources.
	 */
	public void unload ()
	{
	}

	/**
	 * Receive a new measurement.
	 *
	 * @param measurement The measurement.
	 */
	public synchronized void receiveSensorValue (Measurement measurement)
	{
		if (measurement.isStoreToDatabase ())
		{
			cacheAt[activeCache][cacheIndex] = measurement.getTimestamp ();
			cacheValue[activeCache][cacheIndex] = new Double(measurement.getValue ());
			cacheStationId[activeCache][cacheIndex] = new Long(measurement.getStationId ());
			cacheSensorId[activeCache][cacheIndex] = new Long(measurement.getSensorId ());

			++cacheIndex;

			if (cacheIndex == batchSize)
			{
				flushCache ();
			}
		}
	}

	/**
	 * Store all cached measurements to the database and flip the
	 * caches.
	 */
	private void flushCache ()
	{
		long time = System.currentTimeMillis ();

		Properties insertProps = new Properties();

		insertProps.put ("table", "Measurement");
		insertProps.put ("size", new Integer(batchSize));
		insertProps.put ("column.at", cacheAt[activeCache]);
		insertProps.put ("column.value", cacheValue[activeCache]);
		insertProps.put ("column.stationId", cacheStationId[activeCache]);
		insertProps.put ("column.sensorId", cacheSensorId[activeCache]);
		CommandTools.performSimple ("persist.Insert", insertProps);

		activeCache = (activeCache + 1) % NUM_CACHES;
		cacheIndex = 0;

		time = System.currentTimeMillis () - time;
	}
}