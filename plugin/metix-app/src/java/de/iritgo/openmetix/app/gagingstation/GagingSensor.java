/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gagingstation;


import de.iritgo.openmetix.app.gagingstation.sensorbehaviour.GagingSensorBehaviour;
import de.iritgo.openmetix.app.gagingstation.sensorbehaviour.GagingSensorBehaviourDescriptor;
import de.iritgo.openmetix.app.gui.ITimeComboBox;
import de.iritgo.openmetix.app.manager.CoreManager;
import de.iritgo.openmetix.app.util.Dimension;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.user.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Data object that represents a gaging sensor.
 *
 * @version $Id: GagingSensor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingSensor
	extends DataObject
	implements MeasurementAgent
{
	/** The null-sensor for error-handling. */
	public static GagingSensor UNKNOWN = new GagingSensor();

	/** The list of all listeners. */
	private List measurementAgents;

	/** Assigned user. */
	private User user;

	/** The sensor behaviour. */
	private GagingSensorBehaviour behaviour;

	/** All sensor from which this sensor receives measurements. */
	private ArrayList inputs;

	/**
	 * Create a new GagingSensor.
	 */
	public GagingSensor ()
	{
		super("GagingSensor");

		putAttribute ("name", "");
		putAttribute ("dimension", "");
		putAttribute ("unit", "");
		putAttribute ("behaviourId", "");
		putAttribute ("systemId", new Long(0));
		putAttribute ("outputId", new Long(0));
		putAttribute ("period", new Double(1));
		putAttribute ("periodType", new Integer(2));
		putAttribute ("inputs", "");

		measurementAgents = new LinkedList();
		inputs = new ArrayList(8);
	}

	/**
	 * Add a measurement agent to this sensor
	 *
	 * @param measurementAgent The measurement agent to add.
	 */
	public synchronized void addMeasurementAgent (MeasurementAgent measurementAgent)
	{
		measurementAgents.add (measurementAgent);
	}

	/**
	 * Remove a measurement agent from this sensor
	 *
	 * @param measurementAgent The measurement agent to remove.
	 */
	public synchronized void removeMeasurementAgent (MeasurementAgent measurementAgent)
	{
		measurementAgents.remove (measurementAgent);
	}

	/**
	 * Get a iterator over all listeners
	 *
	 * @return iterator A iterator over all listeners
	 */
	public Iterator getMeasurementAgentIterator ()
	{
		return measurementAgents.iterator ();
	}

	/**
	 * Get the sensor name.
	 *
	 * @return The sensor name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the sensor name.
	 *
	 * @param name The new sensor name.
	 */
	public void setName (String name)
	{
		putAttribute ("name", name);
	}

	/**
	 * Set the listening user.
	 *
	 * @param user The user.
	 */
	public void setUser (User user)
	{
		this.user = user;
	}

	/**
	 * Get the listening user.
	 *
	 * @return The user.
	 */
	public User getUser ()
	{
		return user;
	}

	/**
	 * Create a string representation of the sensor.
	 *
	 * @return The string representation.
	 */
	public String toString ()
	{
		return getName ();
	}

	/**
	 * Get the sensor's dimension.
	 *
	 * @return The dimension.
	 */
	public String getDimension ()
	{
		return getStringAttribute ("dimension");
	}

	/**
	 * Set the sensor's dimension.
	 *
	 * @param dimension The new dimension.
	 */
	public void setDimension (String dimension)
	{
		putAttribute ("dimension", dimension);
	}

	/**
	 * Get the sensor's unit.
	 *
	 * @return The unit.
	 */
	public String getUnit ()
	{
		return getStringAttribute ("unit");
	}

	/**
	 * Set the sensor's unit.
	 *
	 * @param unit The new unit.
	 */
	public void setUnit (String unit)
	{
		putAttribute ("unit", unit);
	}

	/**
	 * Get the dimension and unit of the sensor.
	 *
	 * @return The sensor's dimension and unit.
	 */
	public Dimension.Unit getDimensionUnit ()
	{
		return Dimension.findDimension (getDimension ()).findUnit (getUnit ());
	}

	/**
	 * Set the dimension and unit.
	 *
	 * @param unit The dimension and unit.
	 */
	public void setDimensionUnit (Dimension.Unit unit)
	{
		setDimension (unit.getDimension ().getDimensionKey ());
		setUnit (unit.getUnitKey ());
	}

	/**
	 * Get the behaviour id.
	 *
	 * @return The behaviour id.
	 */
	public String getBehaviourId ()
	{
		return getStringAttribute ("behaviourId");
	}

	/**
	 * Set the behaviour id.
	 *
	 * @param behaviourId The new behaviour.
	 */
	public void setBehaviourId (String behaviourId)
	{
		putAttribute ("behaviourId", behaviourId);
	}

	/**
	 * Get the gaging system id.
	 *
	 * @return The gaging system id.
	 */
	public long getSystemId ()
	{
		return getLongAttribute ("systemId");
	}

	/**
	 * Set the gaging system id.
	 *
	 * @param systemId The gaging system id.
	 */
	public void setSystemId (long systemId)
	{
		putAttribute ("systemId", new Long(systemId));
	}

	/**
	 * Get the gaging system output id.
	 *
	 * @return The gaging system output id.
	 */
	public long getOutputId ()
	{
		return getLongAttribute ("outputId");
	}

	/**
	 * Set the gaging system output id.
	 *
	 * @param outputId The output id.
	 */
	public void setOutputId (long outputId)
	{
		putAttribute ("outputId", new Long(outputId));
	}

	/**
	 * Get the time period for period sensors.
	 *
	 * @return The time period.
	 */
	public double getPeriod ()
	{
		return getDoubleAttribute ("period");
	}

	/**
	 * Set the time period for period sensors.
	 *
	 * @param period The time period.
	 */
	public void setPeriod (double period)
	{
		putAttribute ("period", new Double(period));
	}

	/**
	 * Get the time period type for period sensors.
	 *
	 * @return The time period type.
	 */
	public int getPeriodType ()
	{
		return getIntAttribute ("periodType");
	}

	/**
	 * Set the time period type for period sensors.
	 *
	 * @param periodType The time period type.
	 */
	public void setPeriodType (int periodType)
	{
		if (periodType == ITimeComboBox.PERIOD_SECOND)
		{
			periodType = ITimeComboBox.PERIOD_MINUTE;
		}

		putAttribute ("periodType", new Integer(periodType));
	}

	/**
	 * Get the sensor inputs.
	 *
	 * @return The sensor inputs.
	 */
	public String getInputs ()
	{
		return getStringAttribute ("inputs");
	}

	/**
	 * Get the number of sensor inputs.
	 *
	 * @return The number of sensor inputs.
	 */
	public int getInputCount ()
	{
		StringTokenizer st = new StringTokenizer(getStringAttribute ("inputs"), ",");

		return st.countTokens ();
	}

	/**
	 * Set the sensor inputs.
	 *
	 * @param inputs The new sensor inputs.
	 */
	public void setInputs (String inputs)
	{
		putAttribute ("inputs", inputs);
	}

	/**
	 * Get the sensor behaviour.
	 *
	 * @return The sensor behaviour.
	 */
	public GagingSensorBehaviour getBehaviour ()
	{
		if (behaviour == null)
		{
			behaviour = GagingSensorBehaviourDescriptor.createBehaviour (getBehaviourId ());
			behaviour.setSensor (this);
			behaviour.init ();
		}

		return behaviour;
	}

	/**
	 * Receive a new measurement and call all agents.
	 *
	 * @param measurement The measurement.
	 */
	public void receiveSensorValue (Measurement measurement)
	{
		GagingSensorBehaviour gsb = getBehaviour ();

		if (gsb != null)
		{
			gsb.receiveSensorValue (measurement);
		}
	}

	/**
	 * Send a new measurement to the measurement agents.
	 *
	 * @param measurement The measurement.
	 */
	public void sendMeasurementToAgents (Measurement measurement)
	{
		for (Iterator i = measurementAgents.iterator (); i.hasNext ();)
		{
			MeasurementAgent l = (MeasurementAgent) i.next ();

			l.receiveSensorValue (measurement);
		}
	}

	/**
	 * Update the input connections.
	 *
	 * This method determines the current set of sensor inputs as specified
	 * by the input-id list attribute 'inputs'. It removes any lost connections
	 * from and adds any new connections to the sending sensor as measurement agents.
	 */
	public void updateInputConnections ()
	{
		CoreManager coreManager =
			(CoreManager) Engine.instance ().getManagerRegistry ().getManager ("CoreManager");

		GagingStationRegistry stationRegistry = coreManager.getGagingStationRegistry ();

		ArrayList newInputs = new ArrayList(8);

		for (StringTokenizer st = new StringTokenizer(getInputs (), ","); st.hasMoreTokens ();)
		{
			long inputSensorId = NumberTools.toLong (st.nextToken (), 0);
			GagingSensor inputSensor = stationRegistry.findSensor (inputSensorId);

			if (inputSensor != null && inputSensor != this)
			{
				newInputs.add (inputSensor);
			}
		}

		boolean inputsChanged = false;

		for (int i = 0; i < inputs.size (); ++i)
		{
			GagingSensor inputSensor = (GagingSensor) inputs.get (i);

			if (! newInputs.contains (inputSensor))
			{
				inputSensor.removeMeasurementAgent (this);
				inputsChanged = true;
			}
		}

		for (int i = 0; i < newInputs.size (); ++i)
		{
			GagingSensor inputSensor = (GagingSensor) newInputs.get (i);

			if (! inputs.contains (inputSensor))
			{
				inputSensor.addMeasurementAgent (this);
				inputsChanged = true;
			}
		}

		inputs = newInputs;

		if (inputsChanged && behaviour != null)
		{
			behaviour.init ();
		}
	}
}