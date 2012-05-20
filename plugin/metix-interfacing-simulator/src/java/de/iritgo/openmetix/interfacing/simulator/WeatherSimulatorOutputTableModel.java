/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.simulator;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.tools.NumberTools;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Swing table model to for displaying a list of WeatherSimulatorOutputs.
 *
 * @version $Id: WeatherSimulatorOutputTableModel.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class WeatherSimulatorOutputTableModel
	extends AbstractTableModel
{
	/** The number column. */
	private static final int COL_NUM = 0;

	/** The minimum value column. */
	private static final int COL_MINIMUM = 1;

	/** The maximum value column. */
	private static final int COL_MAXIMUM = 2;

	/** The standard variation column. */
	private static final int COL_VARIATION = 3;

	/** The list of outputs. */
	private List outputs;

	/** The column title strings. */
	private String[] columnNames =
		new String[]
		{
			"metix.numSymbol",
			"metix.minimum",
			"metix.maximum",
			"metix.variation"
		};

	/** The column types. */
	private Class[] columnClasses =
		new Class[]
		{
			String.class,
			String.class,
			String.class,
			String.class
		};

	/**
	 * Create a new WeatherSimulatorOutputTableModel.
	 */
	public WeatherSimulatorOutputTableModel ()
	{
		outputs = new ArrayList();
	}

	/**
	 * @return The current number of outputs.
	 *
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount ()
	{
		return outputs.size ();
	}

	/**
	 * @return The number of columns in this model.
	 *
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount ()
	{
		return columnNames.length;
	}

	/**
	 * Get the name of a column.
	 *
	 * @param column The column which name should be retrieved.
	 * @return The column title.
	 *
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName (int column)
	{
		return Engine.instance ().getResourceService ().getStringWithoutException (
			columnNames[column]);
	}

	/**
	 * Get the type of a column.
	 *
	 * @param column The column which name should be retrieved.
	 * @return The column type.
	 *
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class getColumnClass (int column)
	{
		return columnClasses[column];
	}

	/**
	 * Retrieve an output attribute.
	 *
	 * @param row The row to retrieve.
	 * @param column The column to retrieve.
	 * @return The output attribute in the specified cell.
	 *
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt (int row, int column)
	{
		WeatherSimulatorOutput output = (WeatherSimulatorOutput) outputs.get (row);

		switch (column)
		{
			case COL_NUM:
				return String.valueOf (row);

			case COL_MINIMUM:
				return String.valueOf (output.getMinimum ());

			case COL_MAXIMUM:
				return String.valueOf (output.getMaximum ());

			case COL_VARIATION:
				return String.valueOf (output.getVariation ());

			default:
				return null;
		}
	}

	/**
	 * Set an output attribute.
	 *
	 * @param val The new attribute value.
	 * @param row The row to set.
	 * @param column The column to set.
	 *
	 * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
	 */
	public void setValueAt (Object val, int row, int column)
	{
		WeatherSimulatorOutput output = (WeatherSimulatorOutput) outputs.get (row);

		switch (column)
		{
			case COL_MINIMUM:
				output.setMinimum (NumberTools.toDouble ((String) val, 0.0));

				break;

			case COL_MAXIMUM:
				output.setMaximum (NumberTools.toDouble ((String) val, 0.0));

				break;

			case COL_VARIATION:
				output.setVariation (NumberTools.toDouble ((String) val, 0.0));

				break;
		}
	}

	/**
	 * Check wether a cell is editable.
	 *
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return True for an editable cell.
	 *
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable (int row, int col)
	{
		return col != COL_NUM;
	}

	/**
	 * Add an output to the model.
	 *
	 * @param output The output to add.
	 */
	public void addOutput (WeatherSimulatorOutput output)
	{
		outputs.add (output);
		fireTableRowsInserted (outputs.size () - 1, outputs.size () - 1);
	}

	/**
	 * Retrieve an output at a specific row.
	 *
	 * @param row The row to retrieve.
	 * @return The output.
	 */
	public WeatherSimulatorOutput getOutput (int row)
	{
		return (WeatherSimulatorOutput) outputs.get (row);
	}

	/**
	 * Delete an output at a specific row.
	 *
	 * @param row The row to delete.
	 */
	public void deleteOutput (int row)
	{
		outputs.remove (row);
		fireTableRowsDeleted (row, row);
	}

	/**
	 * Get an iterator over all outputs.
	 *
	 * @return An output iterator.
	 */
	public Iterator outputIterator ()
	{
		return outputs.iterator ();
	}

	/**
	 * Remove all outputs.
	 */
	public void clear ()
	{
		outputs.clear ();
		fireTableDataChanged ();
	}
}