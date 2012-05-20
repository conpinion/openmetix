/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystemdriver;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutput;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.link.InterfaceDriver;
import java.sql.Timestamp;
import java.util.Iterator;


/**
 * Base class for all gaging system drivers.
 *
 * @version $Id: GagingSystemDriver.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public abstract class GagingSystemDriver
	extends BaseObject
	implements Runnable
{
	/** This flag controls the running/terminating state of the driver thread. */
	protected boolean running;

	/** The gaging station manager. */
	protected GagingStationManager gagingStationManager;

	/** Our gaging system. */
	protected GagingSystem system;

	/** The communication interface driver. */
	protected InterfaceDriver iface;

	/**
	 * Create a new driver instance.
	 */
	public GagingSystemDriver ()
	{
	}

	/**
	 * Create a new driver instance.
	 *
	 * @param typeId The id of this driver.
	 */
	public GagingSystemDriver (String typeId)
	{
		super(typeId);

		gagingStationManager =
			(GagingStationManager) Engine.instance ().getManagerRegistry ().getManager (
				"GagingStationManager");

		running = true;
	}

	/**
	 * Terminate the drivers processing loop.
	 */
	public void terminate ()
	{
		running = false;
	}

	/**
	 * Set the gaging system.
	 *
	 * @param system The gaging system.
	 */
	public void setGagingSystem (GagingSystem system)
	{
		this.system = system;
	}

	/**
	 * Set the interface driver.
	 *
	 * @param iface The interface driver.
	 */
	public void setInterfaceDriver (InterfaceDriver iface)
	{
		this.iface = iface;
	}

	/**
	 * Send a measurement value.
	 *
	 * @param value The measurement value.
	 * @param outputIndex The index of the output that receives the measurement.
	 */
	protected void sendMeasurement (double value, int outputIndex)
	{
		if (outputIndex < 0 || outputIndex >= system.getOutputCount ())
		{
			return;
		}

		GagingOutput output = system.getOutput (outputIndex);

		for (Iterator i = gagingStationManager.stationIterator (); i.hasNext ();)
		{
			GagingStation station = (GagingStation) i.next ();

			for (Iterator j = station.sensorIterator (); j.hasNext ();)
			{
				GagingSensor sensor = (GagingSensor) j.next ();

				if (
					sensor.getSystemId () == system.getUniqueId () &&
					sensor.getOutputId () == output.getUniqueId ())
				{
					gagingStationManager.receiveSensorValue (
						new Timestamp(System.currentTimeMillis ()), value, station, sensor,
						system.isStoreToDatabase ());
				}
			}
		}
	}

	/**
	 * Get the number of configured outputs.
	 *
	 * @return The number of outputs.
	 */
	public int getOutputCount ()
	{
		return system.getOutputCount ();
	}

	/**
	 * Get the number of custom output parameters.
	 *
	 * @return The number of output parameters.
	 */
	public int getCustomOutputParameterCount ()
	{
		return 0;
	}

	/**
	 * Get the name of a custom output parameter.
	 *
	 * @param index The index of the parameter to retrieve.
	 * @return The parameter name.
	 */
	public String getCustomOutputParameterName (int index)
	{
		return "";
	}

	/**
	 * Get the class of a custom output parameter.
	 *
	 * @param index The index of the parameter to retrieve.
	 * @return The parameter class.
	 */
	public Class getCustomOutputParameterClass (int index)
	{
		return Object.class;
	}

	/**
	 * Get the value of a custom output parameter.
	 *
	 * @param system The gaging system.
	 * @param output The gaging system output.
	 * @param index The index of the output to retrieve.
	 * @param paramIndex The index of the parameter to retrieve.
	 * @return The parameter value.
	 */
	public Object getCustomOutputParameterValue (
		GagingSystem system, GagingOutput output, int index, int paramIndex)
	{
		return null;
	}
}