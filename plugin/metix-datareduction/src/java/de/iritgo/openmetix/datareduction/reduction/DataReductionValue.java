/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.reduction;


import de.iritgo.openmetix.framework.command.CommandTools;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * @version $Id: DataReductionValue.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DataReductionValue
{
	/** Time before start a data reduction */
	private int timeNumber;

	/** Reference to the combobox list in class DataReductionConfigurator */
	private int timeIndex;

	/** Starttime for data reduction */
	private Timestamp startTime;

	/** Stoptime for data reduction */
	private Timestamp stopTime;
	private Timestamp firstStartTime;
	private Timestamp firstStopTime;

	/** Time between starttime and stoptime in minutes */
	private int duration = 1;
	private List measurements;
	private boolean deleteAllParameters;

	/** Selected station. */
	private long stationID;

	/** Selected sensor. */
	private long sensorID;

	public DataReductionValue (int timeNumber, int timeIndex, boolean deleteAllParameters)
	{
		this.timeNumber = timeNumber;
		this.timeIndex = timeIndex;
		this.deleteAllParameters = deleteAllParameters;
	}

	public void setStartTime ()
	{
		GregorianCalendar cal = new GregorianCalendar();

		switch (timeIndex)
		{
			case 0:
				cal.add (Calendar.MINUTE, -timeNumber);

				break;

			case 1:
				cal.add (Calendar.HOUR_OF_DAY, -timeNumber);

				break;

			case 2:
				cal.add (Calendar.MONTH, -timeNumber);

				break;
		}

		startTime = new Timestamp(cal.getTimeInMillis ());
		cal.add (Calendar.MINUTE, -duration);
		stopTime = new Timestamp(cal.getTimeInMillis ());

		firstStartTime = startTime;
		firstStopTime = stopTime;
	}

	public void setFirstTime ()
	{
		startTime = firstStartTime;
		stopTime = firstStopTime;
	}

	public void setNewStartTime ()
	{
		startTime = stopTime;

		GregorianCalendar cal = new GregorianCalendar();

		cal.setTimeInMillis (startTime.getTime ());
		cal.add (Calendar.MINUTE, -duration);
		stopTime = new Timestamp(cal.getTimeInMillis ());
	}

	public void setSationSensor (long stationID, long sensorID)
	{
		this.stationID = stationID;
		this.sensorID = sensorID;
	}

	public List getMeasurementList ()
	{
		DBSelect dbSelect = new DBSelect();

		measurements = dbSelect.selectMeasurements (startTime, stopTime, stationID, sensorID);

		return measurements;
	}

	public double getAverageOfMeasurements ()
	{
		double average = 0.0;
		double sum = 0.0;

		for (Iterator e = measurements.iterator (); e.hasNext ();)
		{
			Double temp = (Double) e.next ();

			sum += temp.doubleValue ();
		}

		if (measurements.size () != 0)
		{
			average = sum / measurements.size ();
		}
		else
		{
			average = -1111.11;
		}

		return average;
	}

	public void deleteMeasurements ()
	{
		Properties props = new Properties();

		props.put ("startTime", startTime);
		props.put ("stopTime", stopTime);
		props.put ("stationID", new Long(stationID));
		props.put ("sensorID", new Long(sensorID));
		CommandTools.performSimple ("persist.DeleteMeasurements", props);
	}

	public void insertMeasurement (double value)
	{
		Properties props = new Properties();

		props.put ("table", "Measurement");
		props.put ("size", new Integer(0));
		props.put ("column.at", startTime);
		props.put ("column.value", new Double(value));
		props.put ("column.stationId", new Long(stationID));
		props.put ("column.sensorId", new Long(sensorID));
		CommandTools.performSimple ("persist.Insert", props);
		setNewStartTime ();
	}
}