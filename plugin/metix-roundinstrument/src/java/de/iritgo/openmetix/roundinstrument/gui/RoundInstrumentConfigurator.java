/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gui.IFontChooser;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.app.util.Tools;
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
import de.iritgo.openmetix.roundinstrument.RoundInstrument;
import de.iritgo.openmetix.roundinstrument.RoundInstrumentSensor;
import de.iritgo.openmetix.roundinstrument.plot.NewMeterPlot;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.data.DefaultValueDataset;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * This gui pane is used to configure round instruments.
 *
 * @version $Id: RoundInstrumentConfigurator.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class RoundInstrumentConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	/** Instrument title. */
	public ITextField title;

	/** The minimum axis value. */
	public IFormattedTextField scalingLow;

	/** The maximum axis value. */
	public IFormattedTextField scalingHigh;

	/** The station an sensor selector. */
	public IStationSensorSelector stationSensorSelector;

	/** Select the 360 degree mode. */
	public JRadioButton modeFullRound;

	/** Select the 180 degree facing up mode. */
	public JRadioButton modeHalfRound;

	/** Select the 180 degree facing left mode. */
	public JRadioButton modeHalfRoundLeft;

	/** Select the 180 degree facing right mode. */
	public JRadioButton modeHalfRoundRight;

	/** Check to show a maxmimum marker. */
	public JCheckBox showMaxMarker;

	/** Check to show a minmimum marker. */
	public JCheckBox showMinMarker;

	/** Check to display a digital measurement value. */
	public JCheckBox showDigital;

	/** The instrument preview. */
	public JPanel preview;

	/** The preview chart. */
	private JFreeChart chart;

	/** Used to disable any updates during configuration loading. */
	private boolean blockListener;

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

	/** True if the configuration was loaded. */
	public boolean isConfigured = false;

	/** Used to block station/sensor selections during loading. */
	public boolean blockedComboBox = false;

	/** Instrument text font dialog activator. */
	public JTextField fontDisplay;

	/** The round plot. */
	private NewMeterPlot plot;

	/** Instrument needle color dialog activator. */
	public JTextField needleColorDisplay;

	/** Division size. */
	public IFormattedTextField division;

	/** If selected, division is the number of divisions. */
	public JRadioButton divisionIsNum;

	/** If selected, division is a range. */
	public JRadioButton divisionIsRange;

	/**
	 * Create a new RoundInstrumentConfigurator
	 */
	public RoundInstrumentConfigurator ()
	{
		super("RoundInstrumentConfigurator");
		blockListener = true;
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new RoundInstrument();
	}

	/**
	 * Clone the RoundInstrumentConfigurator.
	 *
	 * @return The clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new RoundInstrumentConfigurator();
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
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/RoundInstrumentConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			scalingLow.setFormatterFactory (Tools.getDoubleFormatter ());
			scalingHigh.setFormatterFactory (Tools.getDoubleFormatter ());
			division.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMinValue.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMaxValue.setFormatterFactory (Tools.getDoubleFormatter ());

			DefaultValueDataset dataset = new DefaultValueDataset(new Double(43.0));

			plot = new NewMeterPlot(dataset);

			chart = new JFreeChart(null, null, plot, false);
			chart.setAntiAlias (preferences.getDrawAntiAliased ());
			chart.setBackgroundPaint (content.getBackground ());
			plot.setCriticalRange (null);
			plot.setNormalRange (null);
			plot.setWarningRange (null);
			plot.setFitToWindow (false);
			plot.setValueFont (Font.decode ("Arial-PLAIN-14"));
			plot.setTickLabelFont (Font.decode ("Arial-PLAIN-14"));
			plot.setMeterAngle (NewMeterPlot.DEFAULT_METER_ANGLE);
			plot.setMeterTipAngle (0);
			plot.setDialShape (DialShape.CIRCLE);
			plot.setDigitalPos ("Down");
			plot.setDivision (10);
			plot.setDivisionType (RoundInstrument.DIVISION_NUM);

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

			getDisplay ().putProperty ("weightx", new Double(0.75));
		}
		catch (Exception x)
		{
			Log.logError ("client", "RoundInstrumentConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		blockListener = true;
		blockedComboBox = true;

		stationSensorSelector.update ();

		RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (isConfigured)
		{
			return;
		}

		if (! roundInstrument.isValid ())
		{
			return;
		}

		if (roundInstrument.getSensorConfig () == null)
		{
			roundInstrument.addSensorConfig (new RoundInstrumentSensor());

			return;
		}

		if (! isDisplayValid (roundInstrument))
		{
			return;
		}

		isConfigured = true;

		title.setText (roundInstrument.getTitle ());

		Font font = Font.decode (roundInstrument.getFont ());

		fontDisplay.setText (font.getName () + " " + String.valueOf (font.getSize ()));
		needleColorDisplay.setBackground (new Color(roundInstrument.getNeedleColor ()));

		scalingLow.setValue (new Double(roundInstrument.getScalingLow ()));
		scalingHigh.setValue (new Double(roundInstrument.getScalingHigh ()));
		division.setValue (
			new Double(roundInstrument.getDivision () > 0.0 ? roundInstrument.getDivision () : 1.0));
		divisionIsNum.setSelected (
			roundInstrument.getDivisionType () == RoundInstrument.DIVISION_NUM);
		divisionIsRange.setSelected (
			roundInstrument.getDivisionType () == RoundInstrument.DIVISION_RANGE);

		int mode = roundInstrument.getDisplayMode ();

		modeFullRound.setSelected (mode == RoundInstrument.MODE_FULL);
		modeHalfRound.setSelected (mode == RoundInstrument.MODE_HALF);
		modeHalfRoundLeft.setSelected (mode == RoundInstrument.MODE_HALFLEFT);
		modeHalfRoundRight.setSelected (mode == RoundInstrument.MODE_HALFRIGHT);

		showMaxMarker.setSelected (roundInstrument.showMaxMarker ());
		showMinMarker.setSelected (roundInstrument.showMinMarker ());
		showDigital.setSelected (roundInstrument.showDigital ());

		warnMin.setSelected (roundInstrument.getSensorConfig ().isWarnMin ());
		warnMax.setSelected (roundInstrument.getSensorConfig ().isWarnMax ());
		warnMinValue.setValue (new Double(roundInstrument.getSensorConfig ().getWarnMinValue ()));
		warnMaxValue.setValue (new Double(roundInstrument.getSensorConfig ().getWarnMaxValue ()));

		fitToWindow.setSelected (roundInstrument.isFitToWindow ());

		blockedComboBox = false;

		stationSensorSelector.setStationIdSensorId (
			roundInstrument.getSensorConfig ().getStationId (),
			roundInstrument.getSensorConfig ().getSensorId ());

		checkScalingRange ();
		checkWarningRange ();

		try
		{
			updatePreview ();
		}
		catch (Exception x)
		{
			System.out.println (x);
		}

		blockListener = false;

		title.selectAll ();

		super.loadFromObject ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (! isDisplayValid (roundInstrument) || blockedComboBox)
		{
			return;
		}

		roundInstrument.setTitle (title.getText ());
		roundInstrument.setScalingLow (Double.valueOf (scalingLow.getText ()).doubleValue ());
		roundInstrument.setScalingHigh (Double.valueOf (scalingHigh.getText ()).doubleValue ());
		roundInstrument.setDivision (Double.valueOf (division.getText ()).doubleValue ());
		roundInstrument.setDivisionType (getSelectedDivisionType ());
		roundInstrument.setDisplayMode (getSelectedDisplayMode ());
		roundInstrument.setShowMaxMarker (showMaxMarker.isSelected ());
		roundInstrument.setShowMinMarker (showMinMarker.isSelected ());
		roundInstrument.setShowDigital (showDigital.isSelected ());
		roundInstrument.setFitToWindow (fitToWindow.isSelected ());

		checkScalingRange ();
		storeToSensorConfig ();
	}

	/**
	 * Store the sensor config.
	 */
	public void storeToSensorConfig ()
	{
		RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (! isDisplayValid (roundInstrument) || blockedComboBox)
		{
			return;
		}

		RoundInstrumentSensor sensorConfig =
			(RoundInstrumentSensor) ((RoundInstrument) iobject).getSensorConfig ();

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
	private void checkScalingRange ()
	{
		RoundInstrument roundInstrument = (RoundInstrument) iobject;
		double minValue;
		double maxValue;

		minValue = roundInstrument.getScalingLow ();
		maxValue = roundInstrument.getScalingHigh ();

		if (minValue > maxValue)
		{
			roundInstrument.setScalingLow (maxValue);
			roundInstrument.setScalingHigh (minValue);

			scalingLow.setValue (new Double(roundInstrument.getScalingHigh ()));
			scalingHigh.setValue (new Double(roundInstrument.getScalingLow ()));
			roundInstrument.update ();
		}
	}

	/**
	 * Check that the warning minumum value is smaller than the maximum value.
	 * Eventually exchange the values.
	 */
	private void checkWarningRange ()
	{
		RoundInstrument roundInstrument = (RoundInstrument) iobject;
		RoundInstrumentSensor sensorConfig =
			(RoundInstrumentSensor) ((RoundInstrument) iobject).getSensorConfig ();

		double minValue;
		double maxValue;

		minValue = NumberTools.toDouble (warnMinValue.getText (), 0.0);
		maxValue = NumberTools.toDouble (warnMaxValue.getText (), 0.0);

		if (minValue > maxValue)
		{
			sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
			sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));

			warnMinValue.setValue (
				new Double(roundInstrument.getSensorConfig ().getWarnMaxValue ()));
			warnMaxValue.setValue (
				new Double(roundInstrument.getSensorConfig ().getWarnMinValue ()));
			roundInstrument.update ();
		}
	}

	/**
	 * Return the selected display mode.
	 *
	 * @return The seleced display mode.
	 */
	private int getSelectedDisplayMode ()
	{
		if (modeHalfRound.isSelected ())
		{
			return RoundInstrument.MODE_HALF;
		}

		if (modeHalfRoundLeft.isSelected ())
		{
			return RoundInstrument.MODE_HALFLEFT;
		}

		if (modeHalfRoundRight.isSelected ())
		{
			return RoundInstrument.MODE_HALFRIGHT;
		}

		return RoundInstrument.MODE_FULL;
	}

	/**
	 * Return the selected division type.
	 *
	 * @return The seleced division type.
	 */
	private int getSelectedDivisionType ()
	{
		if (divisionIsNum.isSelected ())
		{
			return RoundInstrument.DIVISION_NUM;
		}

		return RoundInstrument.DIVISION_RANGE;
	}

	/**
	 * Update the instrument preview.
	 */
	public void updatePreview ()
	{
		RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (! isDisplayValid (roundInstrument) || blockedComboBox)
		{
			return;
		}

		RoundInstrumentDisplay.configureChart (chart, roundInstrument, true);

		plot.setValueFont (Font.decode (roundInstrument.getFont ()));
		plot.setTickLabelFont (Font.decode (roundInstrument.getFont ()));
		plot.setNeedlePaint (new Color(roundInstrument.getNeedleColor ()));
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
	 * Show a font chooser and set the instrument font from the
	 * chooser result.
	 */
	public Action fontAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				RoundInstrument roundInstrument = (RoundInstrument) iobject;
				Font newFont =
					IFontChooser.showDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.chooseFont"), Font.decode (roundInstrument.getFont ()));

				if (newFont != null)
				{
					roundInstrument.setFont (IFontChooser.encode (newFont));
					fontDisplay.setText (
						newFont.getName () + " " + String.valueOf (newFont.getSize ()));
					storeToObject ();
					updatePreview ();
				}
			}
		};

	/**
	 * Show a color chooser and set the instrument needle color from the
	 * chooser result.
	 */
	public Action needleColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				RoundInstrument roundInstrument = (RoundInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(roundInstrument.getNeedleColor ()));

				if (newColor != null)
				{
					roundInstrument.setNeedleColor (newColor.getRGB ());
					needleColorDisplay.setBackground (new Color(roundInstrument.getNeedleColor ()));
					storeToObject ();
					updatePreview ();
				}
			}
		};

	/**
	 * Store the instrument attributes and update the preview.
	 */
	public Action storeAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (blockListener)
				{
					return;
				}

				storeToObject ();
				updatePreview ();
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
				RoundInstrument roundInstrument = (RoundInstrument) iobject;
				boolean wasFresh = roundInstrument.isFresh ();

				roundInstrument.setFresh (false);
				storeToSensorConfig ();
				storeToObject ();
				roundInstrument.getSensorConfig ().update ();
				roundInstrument.update ();

				if (wasFresh)
				{
					CommandTools.performAsync (
						new ShowWindow("roundinstrument.roundinstrument", roundInstrument));
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