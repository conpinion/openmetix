/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.linechart.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gui.IFontChooser;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.ITimeComboBox;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IFormattedTextField;
import de.iritgo.openmetix.core.gui.swing.IRadioButton;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.linechart.LineChart;
import de.iritgo.openmetix.linechart.LineChartSensor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;


/**
 * This gui pane is used to configure line charts.
 *
 * @version $Id: LineChartConfigurator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class LineChartConfigurator
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

	/** Default date format. */
	private SimpleDateFormat fullDateFormat;

	/** The instrument title. */
	public ITextField title;

	/** The mode of the domain axis. */
	public ButtonGroup domainAxisMode;

	/** The intervall of the domain axis. */
	public JRadioButton domainAxisInterval;

	/** The domain start date. */
	public IFormattedTextField startDate;

	/** The domain start time. */
	public IFormattedTextField startTime;

	/** The domain end date. */
	public IFormattedTextField stopDate;

	/** The domain end time. */
	public IFormattedTextField stopTime;

	/** The 'current' intervall mode. */
	public IRadioButton domainAxisCurrent;

	/** The domain intervall of the 'current' mode. */
	public IFormattedTextField currentDomainCount;

	/** The domain intervall type of the 'current' mode. */
	public ITimeComboBox currentDomainUnits;

	/** The station and sensor selector. */
	public IStationSensorSelector stationSensorSelector;

	/** Click to use the axis settings from the sensor. */
	public JButton updateScale;

	/** Automatic range mode. */
	public JRadioButton axisRangeModeAuto;

	/** Manual range mode. */
	public JRadioButton axisRangeModeManuel;

	/** Use the axis settings from another sensor. */
	public IRadioButton axisRangeModeTakeFrom;

	/** Minimum axis value. */
	public IFormattedTextField axisRangeStart;

	/** Maximum axis value. */
	public IFormattedTextField axisRangeStop;

	/** The other sensor from wich to use the axis settings. */
	public JComboBox axisTakeFrom;

	/** Check to draw the domain grid lines. */
	public JCheckBox showDomainRasterLines;

	/** Check to draw the domain axis labels. */
	public JCheckBox showDomainLabels;

	/** Check to draw the domain axis tick labels. */
	public JCheckBox showDomainTickLabels;

	/** Check to draw the range grid lines. */
	public JCheckBox showRangeRasterLines;

	/** Check to draw the range axis labels. */
	public JCheckBox showRangeLabels;

	/** Check to draw the range axis tick labels. */
	public JCheckBox showRangeTickLabels;

	/** Check to draw a legend. */
	public JCheckBox showLegend;

	/** Click to accept the changes made to a sensor configuration. */
	public JButton accept;

	/** Click to delete a sensor configuration. */
	public JButton delete;

	/** A list of all configured sensors. */
	public JList sensorList;

	/** List model of the sensor list. */
	private DefaultListModel sensorModel;

	/** The preview chart. */
	private JFreeChart chart;

	/** The preview chart panel. */
	public JPanel preview;

	/** True if the minimum alarm should be enabled. */
	public JCheckBox warnMin;

	/** True if the aximum alarm should be enabled. */
	public JCheckBox warnMax;

	/** Minimum alarm value. */
	public IFormattedTextField warnMinValue;

	/** Maximum alarm value. */
	public IFormattedTextField warnMaxValue;

	/** Combobox model of the 'take axis settings' combobox. */
	private DefaultComboBoxModel axisTakeFromModel;

	/** The current sensor config. */
	private LineChartSensor sensorConfig;

	/** The current sensor editing mode. */
	private int sensorConfigMode = MODE_NONE;

	/** The number of the next free sensor */
	private int sensorNr = -1;

	/** Grid color display. */
	public JTextField gridColorDisplay;

	/** Font display. */
	public JTextField fontDisplay;

	/** Diagram line color display. */
	public JTextField lineColorDisplay;

	/** Daigram line color selector. */
	public JButton lineColorChoose;

	/** Default line colors. */
	private Color[] defaultLineColors =
	{
		Color.RED,
		Color.BLUE,
		Color.GREEN.darker (),
		Color.YELLOW.darker (),
		Color.ORANGE,
		Color.MAGENTA,
		Color.CYAN.darker (),
		Color.PINK,
		Color.DARK_GRAY
	};

	/**
	 * Create a new LineChartConfigurator
	 */
	public LineChartConfigurator ()
	{
		super("LineChartConfigurator");

		fullDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}

	/**
	 * Return a sample of the data object that is displayed in thisgui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new LineChart();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new LineChartConfigurator();
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
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/LineChartConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			startDate.setFormatterFactory (Tools.getDateFormatter ());
			stopDate.setFormatterFactory (Tools.getDateFormatter ());
			startTime.setFormatterFactory (Tools.getTimeFormatter ());
			stopTime.setFormatterFactory (Tools.getTimeFormatter ());
			currentDomainCount.setFormatterFactory (Tools.getDoubleFormatter ());
			axisRangeStart.setFormatterFactory (Tools.getDoubleFormatter ());
			axisRangeStop.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMinValue.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMaxValue.setFormatterFactory (Tools.getDoubleFormatter ());

			axisTakeFromModel = new DefaultComboBoxModel();
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

			Calendar calendar = new GregorianCalendar();
			TimeSeries series = new TimeSeries("Sensorwert", Millisecond.class);
			TimeSeriesCollection dataset = new TimeSeriesCollection(series);

			for (int i = 0; i < 40; ++i)
			{
				double value = 40 * Math.exp (-i);

				series.add (new Millisecond(calendar.getTime ()), value);
				calendar.add (Calendar.SECOND, -30);
			}

			chart =
				ChartFactory.createTimeSeriesChart (
					null, "Zeit", "Werte", dataset, true, true, false);
			chart.setAntiAlias (false);
			chart.setBackgroundPaint (content.getBackground ());

			ChartPanel cp = Tools.createPreviewChartPanel (chart);

			preview.add (
				cp, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			stationSensorSelector.addActionListener (parameterAction);
			enableSensorInput (false);

			getDisplay ().putProperty ("weightx", new Double(0.7));
			getDisplay ().putProperty ("weighty", new Double(0.9));
		}
		catch (Exception x)
		{
			Log.logError ("client", "LineChartConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Fill the instrument gui components with the instrument attributes.
	 *
	 * @param instrument The instrument from which to load the attributes.
	 */
	private void fillGuiFromInstrument (LineChart instrument)
	{
		title.setText (instrument.getTitle ());
		gridColorDisplay.setBackground (new Color(instrument.getGridColor ()));

		Font font = Font.decode (instrument.getFont ());

		fontDisplay.setText (font.getName () + " " + String.valueOf (font.getSize ()));

		showDomainRasterLines.setSelected (instrument.showDomainRasterLines ());
		showDomainLabels.setSelected (instrument.showDomainLabels ());
		showDomainTickLabels.setSelected (instrument.showDomainTickLabels ());

		showRangeRasterLines.setSelected (instrument.showRangeRasterLines ());
		showRangeLabels.setSelected (instrument.showRangeLabels ());
		showRangeTickLabels.setSelected (instrument.showRangeTickLabels ());

		showLegend.setSelected (instrument.showLegend ());

		domainAxisInterval.setSelected (instrument.getDomainAxisMode () == LineChart.MODE_INTERVAL);
		domainAxisCurrent.setSelected (instrument.getDomainAxisMode () == LineChart.MODE_LASTTIME);

		startDate.setValue (new Date(instrument.getDomainStartDate ()));
		startTime.setValue (new Date(instrument.getDomainStartDate ()));

		stopDate.setValue (new Date(instrument.getDomainStopDate ()));
		stopTime.setValue (new Date(instrument.getDomainStopDate ()));

		currentDomainCount.setValue (new Double(instrument.getCurrentDomainCount ()));
		currentDomainUnits.setSelectedIndex (instrument.getCurrentDomainUnits ());

		sensorModel.removeAllElements ();
		sensorNr = -1;

		for (Iterator iter = instrument.sensorConfigIterator (); iter.hasNext ();)
		{
			LineChartSensor sensorConfig = (LineChartSensor) iter.next ();

			sensorNr++;

			sensorModel.addElement (sensorConfig);
		}
	}

	/**
	 * Fill the sensor gui components with sensor configuration attributes.
	 *
	 * @param config The sensor configuration from which to load the attributes.
	 */
	private void fillGuiFromSensor (LineChartSensor config)
	{
		if (config == null)
		{
			return;
		}

		axisRangeModeAuto.setSelected (config.getAxisRangeMode () == LineChartSensor.MODE_AUTO);
		axisRangeModeManuel.setSelected (config.getAxisRangeMode () == LineChartSensor.MODE_MANUAL);
		axisRangeModeTakeFrom.setSelected (
			config.getAxisRangeMode () == LineChartSensor.MODE_TAKEFROM);

		axisRangeStart.setValue (new Double(config.getAxisRangeStart ()));
		axisRangeStop.setValue (new Double(config.getAxisRangeStop ()));
		axisTakeFrom.setModel (axisTakeFromModel = new DefaultComboBoxModel());

		stationSensorSelector.setStationIdSensorId (config.getStationId (), config.getSensorId ());

		warnMin.setSelected (config.isWarnMin ());
		warnMax.setSelected (config.isWarnMax ());
		warnMinValue.setValue (new Double(config.getWarnMinValue ()));
		warnMaxValue.setValue (new Double(config.getWarnMaxValue ()));
		checkWarningRange (config);

		lineColorDisplay.setBackground (new Color(config.getColor ()));
	}

	/**
	 * Enable/disable the sensor configuration gui components.
	 *
	 * @param enable If true the gui components are enabled.
	 */
	private void enableSensorInput (boolean enable)
	{
		boolean moreAxisAvailable = axisTakeFromModel.getSize () > 0;

		stationSensorSelector.setEnabled (enable);
		updateScale.setEnabled (enable);

		axisRangeModeAuto.setEnabled (enable);
		axisRangeModeManuel.setEnabled (enable);
		axisRangeModeTakeFrom.setEnabled (enable && moreAxisAvailable);

		axisRangeStart.setEnabled (enable);
		axisRangeStop.setEnabled (enable);
		axisTakeFrom.setEnabled (enable && moreAxisAvailable);
		accept.setEnabled (enable);
		delete.setEnabled (enable);

		if (! moreAxisAvailable && axisRangeModeTakeFrom.isSelected ())
		{
			axisRangeModeAuto.setSelected (true);
		}

		warnMin.setEnabled (enable);
		warnMax.setEnabled (enable);
		warnMinValue.setEnabled (enable && warnMin.isSelected ());
		warnMaxValue.setEnabled (enable && warnMax.isSelected ());

		lineColorChoose.setEnabled (enable);
		lineColorDisplay.setBackground (
			enable ? new Color(sensorConfig.getColor ())
				   : lineColorDisplay.getParent ().getBackground ());
	}

	/**
	 * LoadFormObject, load the Data form Object.
	 */
	public void loadFromObject ()
	{
		LineChart lineChart = (LineChart) iobject;

		stationSensorSelector.update ();

		if (! lineChart.isValid ())
		{
			return;
		}

		try
		{
			fillGuiFromInstrument (lineChart);
		}
		catch (Exception x)
		{
			Log.logError ("server", "LineChartConfigurator", x.toString ());
		}

		title.selectAll ();

		super.loadFromObject ();
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
	}

	/**
	 * Return the current selected mode of the domain axis.
	 *
	 * @return The domain axis mode.
	 */
	private int getSelectedDomainAxisMode ()
	{
		if (domainAxisInterval.isSelected ())
		{
			return LineChart.MODE_INTERVAL;
		}

		return LineChart.MODE_LASTTIME;
	}

	/**
	 * Return the current selected mode of the range axis.
	 *
	 * @return The range axis mode.
	 */
	private int getSelectedRangeMode ()
	{
		if (axisRangeModeManuel.isSelected ())
		{
			return LineChartSensor.MODE_MANUAL;
		}

		if (axisRangeModeTakeFrom.isSelected ())
		{
			return LineChartSensor.MODE_TAKEFROM;
		}

		return LineChartSensor.MODE_AUTO;
	}

	/**
	 * Store the sensor gui components into the sensor configuration.
	 *
	 * @param sensorConfig The sensor config to load.
	 */
	private void storeToSensorConfig (LineChartSensor sensorConfig)
	{
		sensorConfig.setTitle (
			stationSensorSelector.getSelectedStationName () + " - " +
			stationSensorSelector.getSelectedSensorName ());
		sensorConfig.setAxisRangeMode (getSelectedRangeMode ());
		sensorConfig.setAxisRangeStart (Double.parseDouble (axisRangeStart.getText ()));
		sensorConfig.setAxisRangeStop (Double.parseDouble (axisRangeStop.getText ()));
		sensorConfig.setStationId (stationSensorSelector.getSelectedStationId ());
		sensorConfig.setSensorId (stationSensorSelector.getSelectedSensorId ());

		sensorConfig.setWarnMin (warnMin.isSelected ());
		sensorConfig.setWarnMax (warnMax.isSelected ());
		sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));
		sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
		checkWarningRange (sensorConfig);

		int axisNr = 0;

		if (getSelectedRangeMode () == LineChartSensor.MODE_TAKEFROM)
		{
			LineChartSensor conf = (LineChartSensor) axisTakeFromModel.getSelectedItem ();

			axisNr = conf.getSensorNr ();
		}

		sensorConfig.setAxisTakeFrom (axisNr);
	}

	/**
	 * Check that the warning minumum value is smaller than the maximum value.
	 * Eventually exchange the values.
	 *
	 * @param checkConfig The sensor config for which to check the warnging range.
	 */
	private void checkWarningRange (LineChartSensor checkConfig)
	{
		LineChart lineChart = (LineChart) iobject;
		double minValue;
		double maxValue;

		minValue = NumberTools.toDouble (warnMinValue.getText (), 0.0);
		maxValue = NumberTools.toDouble (warnMaxValue.getText (), 0.0);

		if (minValue > maxValue)
		{
			sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
			sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));

			warnMinValue.setValue (new Double(checkConfig.getWarnMaxValue ()));
			warnMaxValue.setValue (new Double(checkConfig.getWarnMinValue ()));
			checkConfig.update ();
		}
	}

	/**
	 * Validate and correct the settings of the domain axis.
	 */
	private void checkAndSetDomainAxisInterval ()
	{
		LineChart lineChart = (LineChart) iobject;

		for (Iterator i = lineChart.sensorConfigIterator (); i.hasNext ();)
		{
			LineChartSensor sensorConfig = (LineChartSensor) i.next ();

			if (domainAxisInterval.isSelected ())
			{
				try
				{
					sensorConfig.setStartDate (
						fullDateFormat.parse (startDate.getText () + " " + startTime.getText ()));
					sensorConfig.setStopDate (
						fullDateFormat.parse (stopDate.getText () + " " + stopTime.getText ()));
					sensorConfig.setSensorListenerId ("ignore");
				}
				catch (java.text.ParseException x)
				{
				}
			}
			else
			{
				long from =
					ITimeComboBox.convert (currentDomainUnits.getSelectedIndex ()) * (long) Double.valueOf (
						currentDomainCount.getText ()).doubleValue ();

				sensorConfig.setStartDate (new Date((from)));
				sensorConfig.setStopDate (new Date(0));
				sensorConfig.setSensorListenerId ("SendToClientListener");
			}

			sensorConfig.update ();
		}
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	private void storeToInstrument ()
	{
		LineChart lineChart = (LineChart) iobject;

		Calendar domainStart = new GregorianCalendar();
		Calendar domainStop = new GregorianCalendar();

		try
		{
			domainStart.setTime (
				fullDateFormat.parse (startDate.getText () + " " + startTime.getText ()));
			domainStop.setTime (
				fullDateFormat.parse (stopDate.getText () + " " + stopTime.getText ()));
		}
		catch (java.text.ParseException x)
		{
			Log.logError ("client", "LineChartConfigurator.storeToInstrument", x.toString ());
		}

		lineChart.setShowDomainRasterLines (showDomainRasterLines.isSelected ());
		lineChart.setShowDomainLabels (showDomainLabels.isSelected ());
		lineChart.setShowDomainTickLabels (showDomainTickLabels.isSelected ());

		lineChart.setShowRangeRasterLines (showRangeRasterLines.isSelected ());
		lineChart.setShowRangeLabels (showRangeLabels.isSelected ());
		lineChart.setShowRangeTickLabels (showRangeTickLabels.isSelected ());

		lineChart.setShowLegend (showLegend.isSelected ());

		lineChart.setTitle (title.getText ());
		lineChart.setDomainAxisMode (getSelectedDomainAxisMode ());
		lineChart.setDomainStartDate (domainStart.getTimeInMillis ());
		lineChart.setDomainStopDate (domainStop.getTimeInMillis ());
		lineChart.setCurrentDomainCount (
			Double.valueOf (currentDomainCount.getText ()).doubleValue ());
		lineChart.setCurrentDomainUnits (currentDomainUnits.getSelectedIndex ());
	}

	/**
	 * Update the preview.
	 */
	private void updatePreview ()
	{
		LineChart lineChart = (LineChart) iobject;

		LineChartDisplay.configureChart (chart, lineChart, null);
		chart.setBackgroundPaint (content.getBackground ());

		Axis axis = ((XYPlot) chart.getPlot ()).getRangeAxis ();

		axis.setLabel (lineChart.showRangeLabels () ? "Sensorwert" : "");
		axis.setTickLabelsVisible (lineChart.showRangeTickLabels ());
	}

	/**
	 * Fill the 'axis take from' model and select the specifed sensor.
	 *
	 * @param sensor The sensor to check.
	 * @param selectedSensorNr The number of the selected sensor or -1 if none is
	 *   selected.
	 */
	private void fillAxisTakeFromModel (GagingSensor sensor, int selectedSensorNr)
	{
		LineChartSensor selectedItem = null;

		axisTakeFrom.removeAllItems ();

		for (int i = 0; i < sensorModel.getSize (); ++i)
		{
			LineChartSensor config = (LineChartSensor) sensorModel.get (i);

			if (
				config.getSensor ().getDimension ().equals (sensor.getDimension ()) &&
				config.getAxisRangeMode () != LineChartSensor.MODE_TAKEFROM &&
				config != sensorConfig)
			{
				axisTakeFrom.addItem (config);

				if (selectedSensorNr == config.getSensorNr ())
				{
					selectedItem = config;
				}
			}
		}

		axisTakeFrom.setSelectedItem (selectedItem);
	}

	/**
	 * Store all object data and update the preview.
	 */
	public Action storeAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToInstrument ();

				updatePreview ();
			}
		};

	/**
	 * Update the axis min and max values from the sensor.
	 */
	public Action updateAxisAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				GagingSensor sensor = stationSensorSelector.getSelectedSensor ();

				if (sensor == null)
				{
					return;
				}

				axisRangeStart.setValue (
					new Double(sensor.getDimensionUnit ().getTypicalMinValue ()));
				axisRangeStop.setValue (
					new Double(sensor.getDimensionUnit ().getTypicalMaxValue ()));
				axisRangeModeManuel.setSelected (true);
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
				LineChart lineChart = (LineChart) iobject;

				enableSensorInput (false);

				if (sensorConfig == null)
				{
					return;
				}

				storeToSensorConfig (sensorConfig);

				if (sensorConfigMode == MODE_EDIT)
				{
					storeToInstrument ();
					lineChart.update ();
					sensorConfig.update ();
					sensorConfigMode = MODE_BUSY;
				}

				if (sensorConfigMode == MODE_ADD)
				{
					storeToInstrument ();
					sensorConfig.setSensorNr (++sensorNr);
					lineChart.update ();
					lineChart.addSensorConfig (sensorConfig);
					sensorConfigMode = MODE_NONE;
				}

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
				enableSensorInput (false);
				sensorList.clearSelection ();

				sensorConfig = new LineChartSensor();
				sensorConfig.setColor (
					defaultLineColors[(sensorNr + 1) % defaultLineColors.length].getRGB ());
				fillGuiFromSensor (sensorConfig);
				storeToSensorConfig (sensorConfig);
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
				LineChart lineChart = (LineChart) iobject;

				enableSensorInput (false);

				int index = sensorList.getSelectedIndex ();

				if (index < 0)
				{
					return;
				}

				LineChartSensor sensor = (LineChartSensor) sensorModel.get (index);

				if (sensor != null)
				{
					lineChart.removeSensorConfig (sensor);
					sensorConfig = null;
				}
			}
		};

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

		sensorConfig = (LineChartSensor) sensorList.getSelectedValue ();

		GagingSensor sensor = stationSensorSelector.getSelectedSensor ();

		if (sensorConfig != null)
		{
			fillGuiFromSensor (sensorConfig);
			fillAxisTakeFromModel (sensor, sensorConfig.getAxisTakeFrom ());
			enableSensorInput (true);
			sensorConfigMode = MODE_EDIT;
		}
	}

	/**
	 * Called when a new sensor was selected from the station/sensor combobox.
	 */
	public Action parameterAction =
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

				fillAxisTakeFromModel (sensor, sensorConfig.getAxisTakeFrom ());
			}
		};

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
	 * Save all modifications to the instrument, close the configurator
	 * and open the instrument display.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				LineChart lineChart = (LineChart) iobject;

				boolean wasFresh = lineChart.isFresh ();

				lineChart.setFresh (false);
				storeToInstrument ();
				lineChart.update ();
				checkAndSetDomainAxisInterval ();

				if (wasFresh)
				{
					CommandTools.performAsync (new ShowWindow("LineChartDisplay", lineChart));
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

	/**
	 * Show a color chooser and set the grid color from the
	 * chooser result.
	 */
	public Action gridColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				LineChart lineChart = (LineChart) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(lineChart.getGridColor ()));

				if (newColor != null)
				{
					lineChart.setGridColor (newColor.getRGB ());
					gridColorDisplay.setBackground (newColor);
					storeToInstrument ();
					updatePreview ();
				}
			}
		};

	/**
	 * Show a font chooser and set the instrument font from the
	 * chooser result.
	 */
	public Action fontAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				LineChart lineChart = (LineChart) iobject;
				Font newFont =
					IFontChooser.showDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.chooseFont"), Font.decode (lineChart.getFont ()));

				if (newFont != null)
				{
					lineChart.setFont (IFontChooser.encode (newFont));
					fontDisplay.setText (
						newFont.getName () + " " + String.valueOf (newFont.getSize ()));
					storeToInstrument ();
					updatePreview ();
				}
			}
		};

	/**
	 * Show a color chooser and set the grid color from the
	 * chooser result.
	 */
	public Action lineColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(sensorConfig.getColor ()));

				if (newColor != null)
				{
					sensorConfig.setColor (newColor.getRGB ());
					lineColorDisplay.setBackground (newColor);
				}
			}
		};
}