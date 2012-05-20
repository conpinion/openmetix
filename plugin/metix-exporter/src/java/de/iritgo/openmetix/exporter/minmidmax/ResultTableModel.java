/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.minmidmax;


import de.iritgo.openmetix.core.Engine;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Vector;


/**
 * This table model displays measurement values..
 *
 * @version $Id: ResultTableModel.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ResultTableModel
	extends javax.swing.table.AbstractTableModel
{
	/** The list elements. */
	Vector sensors = new Vector();

	/** The column titles. */
	String[] columnNames =
	{
		"minmidmax.station",
		"minmidmax.sensor",
		"minmidmax.minValue",
		"minmidmax.midValue",
		"minmidmax.maxValue"
	};

	/** The column count. */
	int columnCount = columnNames.length;

	/** Column index mapping. */
	int[] indexMap = 
	{
		0,
		1,
		2,
		3,
		4
	};

	/** The station column. */
	public static final int COL_STATION = 0;
	public static final int COL_SENSOR = 1;

	/** The sensor column. */
	public static final int COL_MIN = 2;

	/** The date column. */
	public static final int COL_MID = 3;

	/** The value column. */
	public static final int COL_MAX = 4;

	/** Timestamp formatter. */
	private DateFormat dateFormat;

	/** Number formatter. */
	protected static DecimalFormat formatter = new DecimalFormat("#.##");

	/**
	 * List element.
	 */
	class SensorItem
	{
		/** Current station */
		public String stationName;

		/** Current sensor */
		public String sensorName;

		/** Current min. */
		public double min;

		/** Current mid. */
		public double mid;

		/** Current max. */
		public double max;

		/**
		 * Create a new SensorItem.
		 */
		public SensorItem (
			double min, double mid, double max, String stationName, String sensorName)
		{
			this.min = min;
			this.mid = mid;
			this.max = max;
			this.stationName = stationName;
			this.sensorName = sensorName;
		}
	}

	/**
	 * Create a new ResultTableModel.
	 */
	public ResultTableModel ()
	{
		dateFormat = DateFormat.getDateTimeInstance (DateFormat.MEDIUM, DateFormat.MEDIUM);
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
				return item.stationName;

			case COL_SENSOR:
				return item.sensorName;

			case COL_MIN:
				return formatter.format (item.min);

			case COL_MID:
				return formatter.format (item.mid);

			case COL_MAX:
				return formatter.format (item.max);
		}

		return "not available";
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
	 * Remove all sensors
	 */
	public void removeAllElements ()
	{
		int count = sensors.size ();

		sensors.removeAllElements ();
		fireTableRowsDeleted (0, count);
	}

	/**
	 * Add a sensor to the model.
	 *
	 * @param sensor The sensor to add.
	 */
	public void addEntry (
		double min, double mid, double max, String stationName, String sensorName)
	{
		int row = getRowCount ();

		sensors.add (new SensorItem(min, mid, max, stationName, sensorName));
		fireTableRowsInserted (row, row);
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