/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gui.IFontChooser;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.barinstrument.BarInstrument;
import de.iritgo.openmetix.barinstrument.BarInstrumentSensor;
import de.iritgo.openmetix.barinstrument.plot.BarPlot;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IFormattedTextField;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultValueDataset;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * This gui pane is used to configure bar instruments.
 *
 * @version $Id: BarInstrumentConfigurator.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class BarInstrumentConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	/** Instrument title. */
	public ITextField title;

	/** Minimum axis value. */
	public IFormattedTextField scalingLow;

	/** Maximum axis value. */
	public IFormattedTextField scalingHigh;

	/** True for a logarithmic axis. */
	public JCheckBox axisLogarithmic;

	/** True if the maximum marker should be drawn. */
	public JCheckBox showMaxMarker;

	/** True if the minimum marker should be drawn. */
	public JCheckBox showMinMarker;

	/** True if the axis label should be drawn. */
	public JCheckBox showAxisLabel;

	/** True if the scale label should be drawn. */
	public JCheckBox showScaleLabel;

	/** The station an sensor selector. */
	public IStationSensorSelector stationSensorSelector;

	/** Chart preview. */
	public JPanel preview;

	/** Bar chart. */
	private JFreeChart chart;

	/** Chart values. */
	private DefaultValueDataset dataset;

	/** True if the minimum alarm should be enabled. */
	public JCheckBox warnMin;

	/** True if the aximum alarm should be enabled. */
	public JCheckBox warnMax;

	/** Minimum alarm value. */
	public IFormattedTextField warnMinValue;

	/** Maximum alarm value. */
	public IFormattedTextField warnMaxValue;

	/** True if the instrument should be resized to fit the window. */
	public JCheckBox fitToWindow;

	/** True if the instrument is configured. */
	public boolean isConfigured = false;

	/** True if station/sensor-combobox events should be ignored. */
	public boolean blockedComboBox = false;

	/** Instrument bar color dialog activator. */
	public JTextField barColorDisplay;

	/** Instrument text font dialog activator. */
	public JTextField fontDisplay;

	/**
	 * Create a new BarInstrumentConfigurator.
	 */
	public BarInstrumentConfigurator ()
	{
		super("BarInstrumentConfigurator");
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new BarInstrumentConfigurator();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new BarInstrument();
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		super.initGUI ();

		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
		Preferences preferences = userProfile.getPreferences ();

		try
		{
			Log.logInfo (
				"client", "BarInstrumentConfigurator.initGUI",
				"loading BarInstrumentConfigurator.xml");

			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/BarInstrumentConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			scalingLow.setFormatterFactory (Tools.getDoubleFormatter ());
			scalingHigh.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMinValue.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMaxValue.setFormatterFactory (Tools.getDoubleFormatter ());

			dataset = new DefaultValueDataset(new Double(43.0));

			BarPlot plot = new BarPlot(dataset);

			chart = new JFreeChart(null, null, plot, false);
			chart.setAntiAlias (preferences.getDrawAntiAliased ());
			chart.setBackgroundPaint (content.getBackground ());
			plot.setShowMaxMarker (false);
			plot.setShowMinMarker (false);
			plot.setFitToWindow (false);

			ChartPanel cp = Tools.createPreviewChartPanel (chart);

			preview.add (
				cp, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

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

			stationSensorSelector.addActionListener (
				new ActionListener()
				{
					public void actionPerformed (ActionEvent e)
					{
						storeToSensorConfig ();
						updatePreview ();
					}
				});

			getDisplay ().putProperty ("weightx", new Double(0.6));
		}
		catch (Exception x)
		{
			Log.logError ("client", "BarInstrumentConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		blockedComboBox = true;

		BarInstrument barInstrument = (BarInstrument) iobject;

		stationSensorSelector.update ();

		if (isConfigured)
		{
			return;
		}

		if (! barInstrument.isValid ())
		{
			return;
		}

		if (barInstrument.getSensorConfig () == null)
		{
			barInstrument.addSensorConfig (new BarInstrumentSensor());

			return;
		}

		if (! isDisplayValid (barInstrument))
		{
			return;
		}

		isConfigured = true;

		title.setText (barInstrument.getTitle ());
		barColorDisplay.setBackground (new Color(barInstrument.getBarColor ()));

		Font font = Font.decode (barInstrument.getFont ());

		fontDisplay.setText (font.getName () + " " + String.valueOf (font.getSize ()));
		scalingLow.setValue (new Double(barInstrument.getScalingLow ()));
		scalingHigh.setValue (new Double(barInstrument.getScalingHigh ()));

		axisLogarithmic.setSelected (barInstrument.isAxisLogarithmic ());
		showMaxMarker.setSelected (barInstrument.showMaxMarker ());
		showMinMarker.setSelected (barInstrument.showMinMarker ());
		showAxisLabel.setSelected (barInstrument.showAxisLabel ());
		showScaleLabel.setSelected (barInstrument.showScaleLabel ());

		warnMin.setSelected (barInstrument.getSensorConfig ().isWarnMin ());
		warnMax.setSelected (barInstrument.getSensorConfig ().isWarnMax ());
		warnMinValue.setValue (new Double(barInstrument.getSensorConfig ().getWarnMinValue ()));
		warnMaxValue.setValue (new Double(barInstrument.getSensorConfig ().getWarnMaxValue ()));

		fitToWindow.setSelected (barInstrument.isFitToWindow ());

		blockedComboBox = false;

		checkAxisRange ();

		stationSensorSelector.setStationIdSensorId (
			barInstrument.getSensorConfig ().getStationId (),
			barInstrument.getSensorConfig ().getSensorId ());

		updatePreview ();

		title.selectAll ();

		super.loadFromObject ();
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
		BarInstrument barInstrument = (BarInstrument) iobject;

		if (! isDisplayValid (barInstrument))
		{
			return;
		}

		barInstrument.setTitle (title.getText ());
		barInstrument.setScalingLow (Double.valueOf (scalingLow.getText ()).doubleValue ());
		barInstrument.setScalingHigh (Double.valueOf (scalingHigh.getText ()).doubleValue ());
		barInstrument.setAxisLogarithmic (axisLogarithmic.isSelected ());
		barInstrument.setShowMaxMarker (showMaxMarker.isSelected ());
		barInstrument.setShowMinMarker (showMinMarker.isSelected ());
		barInstrument.setFitToWindow (fitToWindow.isSelected ());
		barInstrument.setShowAxisLabel (showAxisLabel.isSelected ());
		barInstrument.setShowScaleLabel (showScaleLabel.isSelected ());

		checkAxisRange ();
		storeToSensorConfig ();
	}

	/**
	 * Store the sensor gui components into the sensor configuration.
	 */
	public void storeToSensorConfig ()
	{
		BarInstrument barInstrument = (BarInstrument) iobject;

		if (! isDisplayValid (barInstrument) || blockedComboBox)
		{
			return;
		}

		BarInstrumentSensor sensorConfig =
			(BarInstrumentSensor) ((BarInstrument) iobject).getSensorConfig ();

		sensorConfig.setStationId (stationSensorSelector.getSelectedStationId ());
		sensorConfig.setSensorId (stationSensorSelector.getSelectedSensorId ());
		sensorConfig.setWarnMin (warnMin.isSelected ());
		sensorConfig.setWarnMax (warnMax.isSelected ());
		sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));
		sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
		checkWarningRange ();
	}

	/**
	 * Check and correct the axis range.
	 */
	protected void checkAxisRange ()
	{
		BarInstrument barInstrument = (BarInstrument) iobject;
		double minValue;
		double maxValue;

		minValue = barInstrument.getScalingLow ();
		maxValue = barInstrument.getScalingHigh ();

		if (minValue > maxValue)
		{
			barInstrument.setScalingLow (maxValue);
			barInstrument.setScalingHigh (minValue);

			scalingLow.setValue (new Double(barInstrument.getScalingHigh ()));
			scalingHigh.setValue (new Double(barInstrument.getScalingLow ()));
			barInstrument.update ();
		}
	}

	/**
	 * Check that the warning minumum value is smaller than the maximum value.
	 * Eventually exchange the values.
	 */
	private void checkWarningRange ()
	{
		BarInstrument barInstrument = (BarInstrument) iobject;
		BarInstrumentSensor sensorConfig =
			(BarInstrumentSensor) ((BarInstrument) iobject).getSensorConfig ();

		double minValue;
		double maxValue;

		minValue = NumberTools.toDouble (warnMinValue.getText (), 0.0);
		maxValue = NumberTools.toDouble (warnMaxValue.getText (), 0.0);

		if (minValue > maxValue)
		{
			sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
			sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));

			warnMinValue.setValue (new Double(barInstrument.getSensorConfig ().getWarnMaxValue ()));
			warnMaxValue.setValue (new Double(barInstrument.getSensorConfig ().getWarnMinValue ()));
			barInstrument.update ();
		}
	}

	/**
	 * Update the chart preview.
	 */
	public void updatePreview ()
	{
		BarInstrument barInstrument = (BarInstrument) iobject;

		if (! isDisplayValid (barInstrument) || blockedComboBox)
		{
			return;
		}

		BarInstrumentDisplay.configureChart (chart, barInstrument, true);

		double diff = barInstrument.getScalingHigh () - barInstrument.getScalingLow ();

		dataset.setValue (new Double(barInstrument.getScalingLow () + diff / 2));
	}

	/**
	 * This method is called when the gui pane starts waiting
	 * for the attributes of it's iobject.
	 */
	public void waitingForNewObject ()
	{
		isConfigured = false;
	}

	/**
	 * Update the axis max and min values from the sensor.
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

				scalingLow.setValue (new Double(sensor.getDimensionUnit ().getTypicalMinValue ()));
				scalingHigh.setValue (new Double(sensor.getDimensionUnit ().getTypicalMaxValue ()));
			}
		};

	/**
	 * Store the instrument and update the preview.
	 */
	public Action storeAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();
				updatePreview ();
			}
		};

	/**
	 * Show a color chooser and set the instrument bar color from the
	 * chooser result.
	 */
	public Action barColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				BarInstrument barInstrument = (BarInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(barInstrument.getBarColor ()));

				if (newColor != null)
				{
					barInstrument.setBarColor (newColor.getRGB ());
					barColorDisplay.setBackground (new Color(barInstrument.getBarColor ()));
					storeToObject ();
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
				BarInstrument barInstrument = (BarInstrument) iobject;
				Font newFont =
					IFontChooser.showDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.chooseFont"), Font.decode (barInstrument.getFont ()));

				if (newFont != null)
				{
					barInstrument.setFont (IFontChooser.encode (newFont));
					fontDisplay.setText (
						newFont.getName () + " " + String.valueOf (newFont.getSize ()));
					storeToObject ();
					updatePreview ();
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
				BarInstrument barInstrument = (BarInstrument) iobject;
				boolean wasFresh = barInstrument.isFresh ();

				barInstrument.setFresh (false);
				storeToSensorConfig ();
				storeToObject ();
				barInstrument.getSensorConfig ().update ();
				barInstrument.update ();

				if (wasFresh)
				{
					CommandTools.performAsync (
						new ShowWindow("BarInstrumentDisplay", barInstrument));
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