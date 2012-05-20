/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.listinstrument.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IFormattedTextField;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.listinstrument.ListInstrument;
import de.iritgo.openmetix.listinstrument.ListInstrumentSensor;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;


/**
 * This gui pane is used to configure list instruments.
 *
 * @version $Id: ListInstrumentConfigurator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ListInstrumentConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator, ListSelectionListener
{
	/** No sensor operation */
	private static final int MODE_NONE = 0;

	/** We are currently editing a sensor. */
	private static final int MODE_EDIT = 1;

	/** We are currently adding a sensor. */
	private static final int MODE_ADD = 2;

	/** We are currently performing a sensor operator. */
	private static final int MODE_BUSY = 3;

	/** The instrument title. */
	public ITextField title;

	/** The station an sensor selector. */
	public IStationSensorSelector stationSensorSelector;

	/** The accept sensor config button. */
	public JButton accept;

	/** The delete sensor config button. */
	public JButton delete;

	/** The sensor list. */
	public JList sensorList;

	/** The model for the sensor list. */
	private DefaultListModel sensorModel;

	/** Check to display the station column in the instrument. */
	public JCheckBox showStationColumn;

	/** Check to display the date column in the instrument. */
	public JCheckBox showDateColumn;

	/** True if the minimum alarm should be enabled. */
	public JCheckBox warnMin;

	/** True if the aximum alarm should be enabled. */
	public JCheckBox warnMax;

	/** Minimum alarm value. */
	public IFormattedTextField warnMinValue;

	/** Maximum alarm value. */
	public IFormattedTextField warnMaxValue;

	/** The current sensor config. */
	private ListInstrumentSensor sensorConfig;

	/** The current sensor editing mode. */
	private int sensorConfigMode;

	/**
	 * Create a new ListInstrumentConfigurator.
	 */
	public ListInstrumentConfigurator ()
	{
		super("ListInstrumentConfigurator");
		sensorConfigMode = MODE_NONE;
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
		return new ListInstrumentConfigurator();
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		super.initGUI ();

		try
		{
			UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();

			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/ListInstrumentConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			warnMinValue.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMaxValue.setFormatterFactory (Tools.getDoubleFormatter ());

			sensorModel = new DefaultListModel();
			sensorList.setModel (sensorModel);
			sensorList.addListSelectionListener (this);

			warnMin.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						warnMinValue.setEnabled (warnMin.isSelected ());
					}
				});

			warnMax.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						warnMaxValue.setEnabled (warnMax.isSelected ());
					}
				});

			enableSensorInput (false);
		}
		catch (Exception x)
		{
			Log.logError ("client", "ListInstrumentConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Enable/Disable the sensor components.
	 *
	 * @param enable If true the components are enabled.
	 */
	protected void enableSensorInput (boolean enable)
	{
		stationSensorSelector.setEnabled (enable);
		accept.setEnabled (enable);
		delete.setEnabled (enable);
		warnMin.setEnabled (enable);
		warnMax.setEnabled (enable);
		warnMinValue.setEnabled (enable && warnMin.isSelected ());
		warnMaxValue.setEnabled (enable && warnMax.isSelected ());
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		ListInstrument listInstrument = (ListInstrument) iobject;

		if (! listInstrument.isValid ())
		{
			return;
		}

		sensorModel.removeAllElements ();

		for (Iterator iter = listInstrument.sensorConfigIterator (); iter.hasNext ();)
		{
			ListInstrumentSensor sensorConfig = (ListInstrumentSensor) iter.next ();

			sensorModel.addElement (sensorConfig);
		}

		title.setText (listInstrument.getTitle ());
		showStationColumn.setSelected (listInstrument.showStationColumn ());
		showDateColumn.setSelected (listInstrument.showDateColumn ());
		stationSensorSelector.update ();

		title.selectAll ();

		super.loadFromObject ();
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
		ListInstrument listInstrument = (ListInstrument) iobject;

		listInstrument.setShowStationColumn (showStationColumn.isSelected ());
		listInstrument.setShowDateColumn (showDateColumn.isSelected ());
		listInstrument.setTitle (title.getText ());
		listInstrument.update ();
	}

	/**
	 * Load the sensor gui components from a sensor configuration.
	 *
	 * @param sensorConfig The sensor config to load.
	 */
	public void fillGuiFromSensor (ListInstrumentSensor sensorConfig)
	{
		if (sensorConfig == null)
		{
			return;
		}

		stationSensorSelector.update ();
		stationSensorSelector.setStationIdSensorId (
			sensorConfig.getStationId (), sensorConfig.getSensorId ());

		warnMin.setSelected (sensorConfig.isWarnMin ());
		warnMax.setSelected (sensorConfig.isWarnMax ());
		warnMinValue.setValue (new Double(sensorConfig.getWarnMinValue ()));
		warnMaxValue.setValue (new Double(sensorConfig.getWarnMaxValue ()));
		checkWarningRange (sensorConfig);
	}

	/**
	 * Store the sensor gui components into the sensor configuration.
	 *
	 * @param sensorConfig The sensor config to load.
	 */
	protected void storeToSensor (ListInstrumentSensor sensorConfig)
	{
		ListInstrument listInstrument = (ListInstrument) iobject;

		sensorConfig.setStationName (stationSensorSelector.getSelectedStationName ());
		sensorConfig.setSensorName (stationSensorSelector.getSelectedSensorName ());
		sensorConfig.setStationId (stationSensorSelector.getSelectedStationId ());
		sensorConfig.setSensorId (stationSensorSelector.getSelectedSensorId ());

		sensorConfig.setWarnMin (warnMin.isSelected ());
		sensorConfig.setWarnMax (warnMax.isSelected ());
		sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));
		sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));

		checkWarningRange (sensorConfig);
	}

	/**
	 * A sensor was selected from the list.
	 *
	 * @param event The list selection event.
	 */
	public void valueChanged (ListSelectionEvent event)
	{
		if (event.getValueIsAdjusting ())
		{
			return;
		}

		if (sensorConfigMode == MODE_BUSY)
		{
			sensorConfigMode = MODE_NONE;

			return;
		}

		sensorConfig = (ListInstrumentSensor) sensorList.getSelectedValue ();

		if (sensorConfig != null)
		{
			fillGuiFromSensor (sensorConfig);
			enableSensorInput (true);
			sensorConfigMode = MODE_EDIT;
		}
	}

	/**
	 * Check that the warning minumum value is smaller than the maximum value.
	 * Eventually exchange the values.
	 *
	 * @param checkConfig The sensor config for which to check the warnging range.
	 */
	private void checkWarningRange (ListInstrumentSensor sensorConfig)
	{
		double minValue = NumberTools.toDouble (warnMinValue.getText (), 0.0);
		double maxValue = NumberTools.toDouble (warnMaxValue.getText (), 0.0);

		if (minValue > maxValue)
		{
			sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
			sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));

			warnMinValue.setValue (new Double(sensorConfig.getWarnMaxValue ()));
			warnMaxValue.setValue (new Double(sensorConfig.getWarnMinValue ()));
			sensorConfig.update ();
		}
	}

	/**
	 * Called when a warning range was set.
	 */
	public Action checkWarningRangeAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (sensorModel.size () == 0)
				{
					return;
				}

				GagingSensor sensor = stationSensorSelector.getSelectedSensor ();

				if (sensor == null || sensorConfig == null)
				{
					return;
				}

				checkWarningRange (sensorConfig);
			}
		};

	/**
	 * Accept the changes made to a sensor configuration.
	 */
	public Action sensorAcceptAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				ListInstrument listInstrument = (ListInstrument) iobject;

				if (sensorConfig == null)
				{
					return;
				}

				storeToSensor (sensorConfig);

				if (sensorConfigMode == MODE_EDIT)
				{
					sensorConfig.update ();
					sensorConfigMode = MODE_BUSY;
				}

				if (sensorConfigMode == MODE_ADD)
				{
					listInstrument.addSensorConfig (sensorConfig);
					sensorConfigMode = MODE_NONE;
				}

				storeToObject ();
				enableSensorInput (false);
			}
		};

	/**
	 * Add a new sensor to the instrument.
	 */
	public Action addSensorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				sensorList.clearSelection ();
				sensorConfig = new ListInstrumentSensor();
				fillGuiFromSensor (sensorConfig);
				enableSensorInput (true);
				sensorConfigMode = MODE_ADD;
			}
		};

	/**
	 * Delete a sensor from the instrument.
	 */
	public Action delSensorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				ListInstrument listInstrument = (ListInstrument) iobject;

				enableSensorInput (false);

				int index = sensorList.getSelectedIndex ();

				if (index < 0)
				{
					return;
				}

				ListInstrumentSensor sensor = (ListInstrumentSensor) sensorModel.get (index);

				if (sensor != null)
				{
					sensor.setSensorName ("Deleted");

					listInstrument.removeSensorConfig (sensor);
					sensorConfig = null;
					sensorConfigMode = MODE_NONE;
				}
			}
		};

	/**
	 * Save all modifications to the instrument, close the configurator
	 * and open the instrument display.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				ListInstrument listInstrument = (ListInstrument) iobject;

				boolean wasFresh = listInstrument.isFresh ();

				listInstrument.setFresh (false);
				storeToObject ();

				if (wasFresh)
				{
					CommandTools.performAsync (
						new ShowWindow("ListInstrumentDisplay", listInstrument),
						new Object[]
						{
							"editable",
							"true"
						});
				}

				display.close ();
			}
		};

	/**
	 * Cancel the instrument configuration and close the
	 * configurator.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				cancelInstrument ();
				display.close ();
			}
		};
}