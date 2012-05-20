/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.listinstrument.gui;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.listinstrument.ListInstrumentSensor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;


/**
 * This table model displays measurement values..
 *
 * @version $Id: MeasurementsTableModel.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class MeasurementsTableModel
	extends javax.swing.table.AbstractTableModel
{
	/** The list elements. */
	Vector sensors = new Vector();

	/** The column titles. */
	String[] columnNames =
	{
		"listinstrument.station",
		"listinstrument.sensor",
		"listinstrument.date",
		"listinstrument.value"
	};

	/** The column count. */
	int columnCount = columnNames.length;

	/** Column index mapping. */
	int[] indexMap = 
	{
		0,
		1,
		2,
		3
	};

	/** The station column. */
	public static final int COL_STATION = 0;

	/** The sensor column. */
	public static final int COL_SENSOR = 1;

	/** The date column. */
	public static final int COL_DATE = 2;

	/** The value column. */
	public static final int COL_VALUE = 3;

	/** If true the station column is displayed. */
	boolean showStationColumn = true;

	/** If true the date column is displayed. */
	boolean showDateColumn = true;

	/** Timestamp formatter. */
	private DateFormat dateFormat;

	/**
	 * List element.
	 */
	class SensorItem
	{
		/** The sensor. */
		public ListInstrumentSensor sensor;

		/** Value time stamp. */
		public Date timestamp;

		/** Current value. */
		public double value;

		/**
		 * Create a new SensorItem.
		 *
		 * @param sensor The sensor configuration.
		 */
		public SensorItem (ListInstrumentSensor sensor)
		{
			this.sensor = sensor;
			timestamp = new Date();
			value = 0.0;
		}
	}

	/**
	 * Create a new MeasurementsTableModel.
	 */
	public MeasurementsTableModel ()
	{
		dateFormat = DateFormat.getDateTimeInstance (DateFormat.MEDIUM, DateFormat.MEDIUM);
	}

	/**
	 * Add a sensor to the model.
	 *
	 * @param sensor The sensor to add.
	 */
	public void addEntry (ConfigurationSensor sensor)
	{
		int row = getRowCount ();

		sensors.add (new SensorItem((ListInstrumentSensor) sensor));
		fireTableRowsInserted (row, row);
	}

	/**
	 * Retrieve a column name.
	 *
	 * @param column The index of the column which name should be retrieved.
	 * @return The column name.
	 */
	public String getColumnName (int column)
	{
		return Engine.instance ().getResourceService ().getStringWithoutException (
			columnNames[getMappedColumn (column)]);
	}

	/**
	 * Get the row count.
	 *
	 * @return The current number of rows.
	 */
	public int getRowCount ()
	{
		return sensors.size ();
	}

	/**
	 * Get the columns count.
	 *
	 * @return The number of columns.
	 */
	public int getColumnCount ()
	{
		return columnCount;
	}

	/**
	 * Retrieve a cell value.
	 *
	 * @param row The row to retrieve.
	 * @param col The column to retrieve.
	 * @return The value of the specified cell.
	 */
	public Object getValueAt (int row, int col)
	{
		SensorItem item = (SensorItem) sensors.get (row);

		switch (getMappedColumn (col))
		{
			case COL_STATION:
				return item.sensor.getStationName ();

			case COL_SENSOR:
				return item.sensor.getSensorName ();

			case COL_DATE:
				return dateFormat.format (item.timestamp);

			case COL_VALUE:
				return item.sensor.formatValue (item.value);
		}

		return "not available";
	}

	/**
	 * Update the value and date of a sensor.
	 *
	 * @param stationId The id of the station to update.
	 * @param sensorId The id of the sensor to update.
	 * @param value The new value.
	 * @param timestamp The new date.
	 */
	public void setSensorValueDate (
		long stationId, long sensorId, double value, Timestamp timestamp)
	{
		for (int i = 0; i < sensors.size (); ++i)
		{
			SensorItem item = (SensorItem) sensors.get (i);

			if (item.sensor.getSensorId () == sensorId)
			{
				item.timestamp.setTime (timestamp.getTime ());
				item.value = value;
				fireTableRowsUpdated (i, i);
			}
		}
	}

	/**
	 * All cells are not editable.
	 *
	 * @param row The row to check for editability.
	 * @param col The column to check for editability.
	 * @return False.
	 */
	public boolean isCellEditable (int row, int col)
	{
		return false;
	}

	/**
	 * Get the sensor configuration at a specific index.
	 *
	 * @param index The index of the sensor to retrieve.
	 * @return The sensor configuration at a specific index.
	 */
	public ConfigurationSensor getSensorConfig (int index)
	{
		return (ConfigurationSensor) sensors.get (index);
	}

	/**
	 * Remove all sensors
	 */
	public void removeAllElements ()
	{
		int count = sensors.size ();

		sensors.removeAllElements ();
		fireTableRowsDeleted (0, count);
	}

	/**
	 * Specifify which columns should be displayed.
	 *
	 * @param showStationColumn If true the station column is displayed.
	 * @param showDateColumn If true the date column is displayed.
	 */
	public void setShowColumns (boolean showStationColumn, boolean showDateColumn)
	{
		this.showStationColumn = showStationColumn;
		this.showDateColumn = showDateColumn;
		columnCount = 0;

		if (showStationColumn)
		{
			indexMap[columnCount++] = COL_STATION;
		}

		indexMap[columnCount++] = COL_SENSOR;

		if (showDateColumn)
		{
			indexMap[columnCount++] = COL_DATE;
		}

		indexMap[columnCount++] = COL_VALUE;

		fireTableStructureChanged ();
	}

	/**
	 * Retrieve the mapped column of the given column.
	 *
	 * @param col The column index.
	 * @return The mapped column index.
	 */
	public int getMappedColumn (int col)
	{
		return indexMap[col];
	}

	/**
	 * Retrieve the real column of the given column.
	 *
	 * @param col The column index.
	 * @return The real column index.
	 */
	public int getRealColumn (int col)
	{
		for (int i = 0; i < indexMap.length; ++i)
		{
			if (indexMap[i] == col)
			{
				return i;
			}
		}

		return 0;
	}
}