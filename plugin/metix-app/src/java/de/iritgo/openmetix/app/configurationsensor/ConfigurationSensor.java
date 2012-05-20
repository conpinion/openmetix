/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.configurationsensor;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.app.util.Dimension;
import de.iritgo.openmetix.framework.base.DataObject;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Date;


/**
 * ConfigurationSensors represent a sensor configuration of an instrument.
 *
 * @version $Id: ConfigurationSensor.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ConfigurationSensor
	extends DataObject
{
	/** Number formatter. */
	protected static DecimalFormat formatter = new DecimalFormat("#.##");

	/**
	 * Create a new ConfigurationSensor.
	 *
	 * @param typeId The type id.
	 */
	public ConfigurationSensor (String typeId)
	{
		super(typeId);

		putAttribute ("sensorId", new Long(0));
		putAttribute ("stationId", new Long(0));
		putAttribute ("warnMin", new Integer(0));
		putAttribute ("warnMax", new Integer(0));
		putAttribute ("warnMinValue", new Double(0));
		putAttribute ("warnMaxValue", new Double(0));
		putAttribute ("sensorListenerId", "SendToClientListener");
		putAttribute ("startDate", new Date());
		putAttribute ("stopDate", new Date());
		putAttribute ("color", new Integer(Color.red.getRGB ()));
	}

	/**
	 * Set the sensor id.
	 *
	 * @param sensorId The new sensor id.
	 */
	public void setSensorId (long sensorId)
	{
		putAttribute ("sensorId", new Long(sensorId));
	}

	/**
	 * Get the sensor id.
	 *
	 * @return The sensor id.
	 */
	public long getSensorId ()
	{
		return getLongAttribute ("sensorId");
	}

	/**
	 * Set the sensor listener id.
	 *
	 * @param sensorListenerId The sensor listener id.
	 */
	public void setSensorListenerId (String sensorListenerId)
	{
		putAttribute ("sensorListenerId", sensorListenerId);
	}

	/**
	 * Get the sensor listener id.
	 *
	 * @return The sensor listener id.
	 */
	public String getSensorListenerId ()
	{
		return getStringAttribute ("sensorListenerId");
	}

	/**
	 * Set the start date.
	 *
	 * @param startDate The start date.
	 */
	public void setStartDate (Date startDate)
	{
		putAttribute ("startDate", startDate);
	}

	/**
	 * Get the start date.
	 *
	 * @return The start date.
	 */
	public Date getStartDate ()
	{
		return getDateAttribute ("startDate");
	}

	/**
	 * Set the end date.
	 *
	 * @param stopDate The end date.
	 */
	public void setStopDate (Date stopDate)
	{
		putAttribute ("stopDate", stopDate);
	}

	/**
	 * Get the end date.
	 *
	 * @return The end date.
	 */
	public Date getStopDate ()
	{
		return getDateAttribute ("stopDate");
	}

	/**
	 * Set the station id.
	 *
	 * @param stationId The station id.
	 */
	public void setStationId (long stationId)
	{
		putAttribute ("stationId", new Long(stationId));
	}

	/**
	 * Get the station id.
	 *
	 * @return the station id.
	 */
	public long getStationId ()
	{
		return getLongAttribute ("stationId");
	}

	/**
	 * Get the minimum warning flag.
	 *
	 * @return The minimum warning flag.
	 */
	public boolean isWarnMin ()
	{
		return getIntAttribute ("warnMin") != 0;
	}

	/**
	 * Set the minimum warning flag.
	 *
	 * @param warnMin The new minimum warning flag.
	 */
	public void setWarnMin (boolean warnMin)
	{
		putAttribute ("warnMin", warnMin ? 1 : 0);
	}

	/**
	 * Get the maximum warning flag.
	 *
	 * @return The maximum warning flag.
	 */
	public boolean isWarnMax ()
	{
		return getIntAttribute ("warnMax") != 0;
	}

	/**
	 * Set the maximum warning flag.
	 *
	 * @param warnMax The new maximum warning flag.
	 */
	public void setWarnMax (boolean warnMax)
	{
		putAttribute ("warnMax", warnMax ? 1 : 0);
	}

	/**
	 * Get the minimum warning value.
	 *
	 * @return The minimum warning value.
	 */
	public double getWarnMinValue ()
	{
		return getDoubleAttribute ("warnMinValue");
	}

	/**
	 * Set the minimum warning value.
	 *
	 * @param warnMinValue The new minimum warning value.
	 */
	public void setWarnMinValue (double warnMinValue)
	{
		putAttribute ("warnMinValue", warnMinValue);
	}

	/**
	 * Get the maximum warning value.
	 *
	 * @return The maximum warning value.
	 */
	public double getWarnMaxValue ()
	{
		return getDoubleAttribute ("warnMaxValue");
	}

	/**
	 * Set the maximum warning value.
	 *
	 * @param warnMaxValue The new maximum warning value.
	 */
	public void setWarnMaxValue (double warnMaxValue)
	{
		putAttribute ("warnMaxValue", warnMaxValue);
	}

	/**
	 * Get the sensor color.
	 *
	 * @return The sensor color.
	 */
	public int getColor ()
	{
		return getIntAttribute ("color");
	}

	/**
	 * Set the sensor color.
	 *
	 * @param color The new sensor color.
	 */
	public void setColor (int color)
	{
		putAttribute ("color", color);
	}

	/**
	 * Get the sensor.
	 *
	 * @return The connected sensor, or the UNKNOWN sensor if something
	 *   goes wrong (bad ids or not already loaded).
	 */
	public GagingSensor getSensor ()
	{
		GagingStationRegistry registry = GagingStationRegistry.getRegistry ();
		GagingSensor sensor = null;

		if (registry != null)
		{
			GagingStation station = registry.findStation (getStationId ());

			if (station != null)
			{
				sensor = station.findSensor (getSensorId ());

				if (sensor != null)
				{
					return sensor;
				}
			}
		}

		if (getStationId () == 0)
		{
			GagingStation gagingStation = registry.getGagingStation (0);

			if (gagingStation != null)
			{
				setStationId (gagingStation.getUniqueId ());

				if (getSensorId () == 0)
				{
					GagingSensor gagingSensor = gagingStation.getSensor (0);

					if (gagingSensor != null)
					{
						setSensorId (gagingSensor.getUniqueId ());
					}
				}
			}
		}

		return GagingSensor.UNKNOWN;
	}

	/**
	 * Get a formatet axis label with dimension of the used sensor and its unit.
	 *
	 * @return An axis label.
	 */
	public String getFormattedAxisLabel ()
	{
		GagingSensor sensor = getSensor ();
		Dimension.Unit unit = sensor.getDimensionUnit ();

		return unit.toString ();
	}

	/**
	 * Format the given value with this sensorconfig.
	 * A typical output is: <<value>> <<sensorUnit>>
	 *
	 * @return The formated value.
	 */
	public String formatValue (String value)
	{
		GagingSensor sensor = getSensor ();
		Dimension.Unit unit = sensor.getDimensionUnit ();

		return value + " " + unit.getName ();
	}

	/**
	 * Format the given value with this sensorconfig.
	 * A typical output is: <<value>> <<sensorUnit>>
	 *
	 * @return The formated value.
	 */
	public String formatValue (double value)
	{
		GagingSensor sensor = getSensor ();
		Dimension.Unit unit = sensor.getDimensionUnit ();

		return formatter.format (value) + " " + unit.getName ();
	}
}