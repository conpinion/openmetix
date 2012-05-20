/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.listinstrument.gui;


import de.iritgo.openmetix.app.alarm.SensorAlarmEvent;
import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gui.InstrumentGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IMenuItem;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.listinstrument.ListInstrument;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Iterator;


/**
 * This gui pane is used to display list instruments.
 *
 * @version $Id: ListInstrumentDisplay.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ListInstrumentDisplay
	extends InstrumentGUIPane
	implements InstrumentDisplay
{
	/** The measurement table. */
	public JTable measurements;

	/** The measurement table. */
	public JScrollPane measurementsScrollPane;

	/** Table model of the measurement table. */
	private MeasurementsTableModel measurementsModel;

	/** Our context menu. */
	JPopupMenu contextMenu;

	/**
	 * Create a new ListInstrumentDisplay.
	 */
	public ListInstrumentDisplay ()
	{
		super("ListInstrumentDisplay");
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new ListInstrument();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new ListInstrumentDisplay();
	}

	/**
	 * This cell renderer is used for the value and date columns
	 * to right align the contents.
	 */
	public class RightAlignedRenderer
		extends DefaultTableCellRenderer
	{
		/**
		 * Get the cell renderer component.
		 */
		public Component getTableCellRendererComponent (
			JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column)
		{
			setHorizontalAlignment (JLabel.RIGHT);

			return super.getTableCellRendererComponent (
				table, color, isSelected, hasFocus, row, column);
		}
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/ListInstrumentDisplay.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			measurements.setModel (measurementsModel = new MeasurementsTableModel());
			measurements.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);

			contextMenu = new JPopupMenu();

			IMenuItem editItem =
				new IMenuItem(
					new AbstractAction()
					{
						public void actionPerformed (ActionEvent e)
						{
							CommandTools.performAsync ("EditInstrument");
						}
					});

			editItem.setText ("metix.edit");
			contextMenu.add (editItem);

			MouseAdapter mouseListener =
				new MouseAdapter()
				{
					public void mousePressed (MouseEvent e)
					{
						if (e.isPopupTrigger ())
						{
							contextMenu.show (measurements, e.getX (), e.getY ());
						}
					}

					public void mouseReleased (MouseEvent e)
					{
						if (e.isPopupTrigger ())
						{
							contextMenu.show (measurements, e.getX (), e.getY ());
						}
					}
				};

			measurements.addMouseListener (mouseListener);
			measurementsScrollPane.addMouseListener (mouseListener);

			configureDisplay ();
		}
		catch (Exception x)
		{
			Log.logError ("client", "ListInstrumentDisplay.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		final ListInstrument listInstrument = (ListInstrument) iobject;

		if (! isDisplayValid (listInstrument))
		{
			return;
		}

		configure (
			new Command()
			{
				public void perform ()
				{
					setTitle (listInstrument.getTitle ());

					measurementsModel.removeAllElements ();
					measurementsModel.setShowColumns (
						listInstrument.showStationColumn (), listInstrument.showDateColumn ());

					measurements.getColumnModel ()
								.getColumn (
						measurementsModel.getRealColumn (MeasurementsTableModel.COL_VALUE))
								.setCellRenderer (new RightAlignedRenderer());

					if (listInstrument.showDateColumn ())
					{
						measurements.getColumnModel ()
									.getColumn (
							measurementsModel.getRealColumn (MeasurementsTableModel.COL_DATE))
									.setCellRenderer (new RightAlignedRenderer());
					}

					for (Iterator iter = listInstrument.sensorConfigIterator (); iter.hasNext ();)
					{
						incSensorCount ();

						measurementsModel.addEntry ((ConfigurationSensor) iter.next ());
					}
				}
			});

		if (getDisplay ().getProperty ("metixReload") != null)
		{
			CommandTools.performSimple ("StatusProgressStep");
			getDisplay ().removeProperty ("metixReload");
		}
	}

	/**
	 * This method is called when the gui pane starts waiting
	 * for the attributes of it's iobject.
	 */
	public void waitingForNewObject ()
	{
		setConfigured (false);
	}

	/**
	 * This method receives sensor measurements.
	 *
	 * The measurement values are sent from the server to the client
	 * instrument displays. A display should check if it really displays
	 * measurments for the given sensor. In this case it should
	 * update itself accordingly to the measurement values.
	 *
	 * @param timestamp The timestamp on which the measurement was done.
	 * @param value The measurement value.
	 * @param stationId The id of the gaging station.
	 * @param sensorId The id of the gaging sensor.
	 */
	public void receiveSensorValue (
		Timestamp timestamp, double value, long stationId, long sensorId)
	{
		ListInstrument listInstrument = (ListInstrument) iobject;

		if (! isDisplayValid (listInstrument))
		{
			return;
		}

		if (listInstrument == null)
		{
			return;
		}

		measurementsModel.setSensorValueDate (stationId, sensorId, value, timestamp);

		for (Iterator i = listInstrument.sensorConfigIterator (); i.hasNext ();)
		{
			ConfigurationSensor sensorConfig = (ConfigurationSensor) i.next ();

			if (
				sensorConfig.getStationId () == stationId &&
				sensorConfig.getSensorId () == sensorId &&
				(sensorConfig.isWarnMin () && value <= sensorConfig.getWarnMinValue ()) ||
				(sensorConfig.isWarnMax () && value >= sensorConfig.getWarnMaxValue ()))
			{
				Engine.instance ().getEventRegistry ().fire (
					"sensoralarm", new SensorAlarmEvent(this));
			}
		}
	}

	/**
	 * This method receives historical sensor measurements.
	 *
	 * The measurement values are sent from the server to the client
	 * instrument displays. A display should check if it really displays
	 * measurments for the given sensor. In this case it should
	 * update itself accordingly to the measurement values.
	 *
	 * @param timestamp The timestamp on which the measurement was done.
	 * @param value The measurement value.
	 * @param stationId The id of the gaging station.
	 * @param sensorId The id of the gaging sensor.
	 */
	public void receiveHistoricalSensorValue (
		long instumentUniqueId, Timestamp timestamp, double value, long stationId, long sensorId)
	{
		ListInstrument listInstrument = (ListInstrument) iobject;

		if (instumentUniqueId == listInstrument.getUniqueId ())
		{
			receiveSensorValue (timestamp, value, stationId, sensorId);
		}
	}

	/**
	 * Configure the display.
	 */
	public void configureDisplay ()
	{
	}

	/**
	 * Check wether this display is editable or not.
	 *
	 * @return True if the display is editable.
	 */
	public boolean isEditable ()
	{
		return true;
	}

	/**
	 * Check wether this display is printable or not.
	 *
	 * @return True if the display is printable.
	 */
	public boolean isPrintable ()
	{
		return false;
	}

	/**
	 * Print the display.
	 */
	public void print ()
	{
	}
}