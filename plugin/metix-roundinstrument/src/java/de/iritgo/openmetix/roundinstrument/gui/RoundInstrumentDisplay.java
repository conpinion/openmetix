/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.roundinstrument.gui;


import de.iritgo.openmetix.app.alarm.SensorAlarmEvent;
import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gui.InstrumentGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.roundinstrument.RoundInstrument;
import de.iritgo.openmetix.roundinstrument.plot.NewMeterPlot;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.data.DefaultValueDataset;
import org.jfree.data.Range;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.sql.Timestamp;


/**
 * This gui pane is used to display round instruments.
 *
 * @version $Id: RoundInstrumentDisplay.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class RoundInstrumentDisplay
	extends InstrumentGUIPane
	implements InstrumentDisplay
{
	/** The meter plot. */
	private NewMeterPlot plot;

	/** The meter chart. */
	private JFreeChart chart;

	/** Chart data. */
	private DefaultValueDataset dataset;

	/** Meter chart panel. */
	private ChartPanel chartPanel;

	/**
	 * Create a new RoundInstrumentDisplay.
	 */
	public RoundInstrumentDisplay ()
	{
		super("roundinstrument.roundinstrument");
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
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new RoundInstrumentDisplay();
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		int row = 0;

		dataset = new DefaultValueDataset(new Double(0.0));
		plot = new NewMeterPlot(dataset);
		chart = new JFreeChart(null, plot);

		chart.setAntiAlias (false);
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

		chartPanel = Tools.createInstrumentChartPanel (chart, this);

		content.add (
			chartPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

		configureDisplay ();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		final RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (! isDisplayValid (roundInstrument))
		{
			return;
		}

		configure (
			new Command()
			{
				public void perform ()
				{
					configureChart (chart, roundInstrument, false);
					setTitle (roundInstrument.getTitle ());
					incSensorCount ();
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
	 * Configure the instrument chart.
	 *
	 * @param chart The instrument chart.
	 * @param roundInstrument The instrument.
	 * @param exampeMinMax Should example min and max values be printed?
	 */
	public static void configureChart (
		JFreeChart chart, RoundInstrument roundInstrument, boolean exampeMinMax)
	{
		double max = roundInstrument.getScalingLow ();
		double min = roundInstrument.getScalingHigh ();
		double diff = min - max;

		if (exampeMinMax)
		{
			min = roundInstrument.getScalingLow () + 2 * diff / 5;
			max = roundInstrument.getScalingLow () + 4 * diff / 5;
		}

		NewMeterPlot plot = (NewMeterPlot) chart.getPlot ();

		plot.setRange (
			new Range(roundInstrument.getScalingLow (), roundInstrument.getScalingHigh ()));

		plot.setDrawDigital (roundInstrument.showDigital ());
		plot.setWarningRange (null);
		plot.setFitToWindow (roundInstrument.isFitToWindow ());
		plot.setUnits (
			roundInstrument.getSensorConfig ().getSensor ().getDimensionUnit ().getName ());
		plot.setValueFont (Font.decode (roundInstrument.getFont ()));
		plot.setTickLabelFont (Font.decode (roundInstrument.getFont ()));
		plot.setNeedlePaint (new Color(roundInstrument.getNeedleColor ()));
		plot.setDivision (roundInstrument.getDivision ());
		plot.setDivisionType (roundInstrument.getDivisionType ());

		if (roundInstrument.showMinMarker ())
		{
			plot.setNormalRange (new Range(min, min));
		}
		else
		{
			plot.setNormalRange (null);
		}

		if (roundInstrument.showMaxMarker ())
		{
			plot.setCriticalRange (new Range(max, max));
		}
		else
		{
			plot.setCriticalRange (null);
		}

		switch (roundInstrument.getDisplayMode ())
		{
			case RoundInstrument.MODE_FULL:
			{
				plot.setMeterAngle (NewMeterPlot.DEFAULT_METER_ANGLE);
				plot.setMeterTipAngle (0);
				plot.setDialShape (DialShape.CIRCLE);
				plot.setDigitalPos ("Down");

				break;
			}

			case RoundInstrument.MODE_HALF:
			{
				plot.setMeterAngle (180);
				plot.setMeterTipAngle (0);
				plot.setDialShape (DialShape.PIE);
				plot.setDigitalPos ("Up");

				break;
			}

			case RoundInstrument.MODE_HALFLEFT:
			{
				plot.setMeterAngle (180);
				plot.setMeterTipAngle (-90);
				plot.setDialShape (DialShape.PIE);
				plot.setDigitalPos ("Left");

				break;
			}

			case RoundInstrument.MODE_HALFRIGHT:
			{
				plot.setMeterAngle (180);
				plot.setMeterTipAngle (90);
				plot.setDialShape (DialShape.PIE);
				plot.setDigitalPos ("Right");

				break;
			}
		}
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
		RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (roundInstrument == null)
		{
			return;
		}

		if (! isDisplayValid (roundInstrument))
		{
			return;
		}

		ConfigurationSensor sensorConfig = ((RoundInstrument) iobject).getSensorConfig ();

		try
		{
			if (sensorConfig.getSensorId () == sensorId)
			{
				dataset.setValue (new Double(value));

				if (roundInstrument.showMinMarker ())
				{
					double min = plot.getNormalRange ().getLowerBound ();

					if (value < min)
					{
						plot.setNormalRange (new Range(value, value));
					}
				}

				if (roundInstrument.showMaxMarker ())
				{
					double max = plot.getCriticalRange ().getLowerBound ();

					if (value > max)
					{
						plot.setCriticalRange (new Range(value, value));
					}
				}

				if (
					(sensorConfig.isWarnMin () && value <= sensorConfig.getWarnMinValue ()) ||
					(sensorConfig.isWarnMax () && value >= sensorConfig.getWarnMaxValue ()))
				{
					Engine.instance ().getEventRegistry ().fire (
						"sensoralarm", new SensorAlarmEvent(this));
				}
			}
		}
		catch (Exception x)
		{
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
		RoundInstrument roundInstrument = (RoundInstrument) iobject;

		if (instumentUniqueId == roundInstrument.getUniqueId ())
		{
			receiveSensorValue (timestamp, value, stationId, sensorId);
		}
	}

	/**
	 * Configure the display.
	 */
	public void configureDisplay ()
	{
		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
		Preferences preferences = userProfile.getPreferences ();

		boolean antiAlised = preferences.getDrawAntiAliased ();

		chart.setAntiAlias (antiAlised);

		Log.logInfo (
			"client", "RoundInstrumentDisplay.configureDisplay", "Antialised: " + antiAlised);
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
		return true;
	}

	/**
	 * Print the display.
	 */
	public void print ()
	{
		chartPanel.createChartPrintJob ();
	}
}