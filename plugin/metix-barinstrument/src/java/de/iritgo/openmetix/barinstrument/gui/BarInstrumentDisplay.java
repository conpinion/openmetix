/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.barinstrument.gui;


import de.iritgo.openmetix.app.alarm.SensorAlarmEvent;
import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gui.InstrumentGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.barinstrument.BarInstrument;
import de.iritgo.openmetix.barinstrument.plot.BarPlot;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.DefaultValueDataset;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.sql.Timestamp;


/**
 * This gui pane is used to display bar instruments.
 *
 * @version $Id: BarInstrumentDisplay.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class BarInstrumentDisplay
	extends InstrumentGUIPane
	implements InstrumentDisplay
{
	/** The bar chart. */
	private JFreeChart chart;

	/** Bar chart data. */
	private DefaultValueDataset dataset;

	/** Bar chart panel. */
	private ChartPanel chartPanel;

	/**
	 * Create a new BarInstrumentDisplay.
	 */
	public BarInstrumentDisplay ()
	{
		super("BarInstrumentDisplay");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		dataset = new DefaultValueDataset(new Double(0.0));

		BarPlot plot = new BarPlot(dataset);

		chart = new JFreeChart(null, null, plot, false);
		chart.setAntiAlias (false);
		chart.setBackgroundPaint (content.getBackground ());
		plot.setShowMaxMarker (false);
		plot.setShowMinMarker (false);
		plot.setFitToWindow (false);

		chartPanel = Tools.createInstrumentChartPanel (chart, this);

		content.add (
			chartPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

		configureDisplay ();
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
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new BarInstrumentDisplay();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		final BarInstrument barInstrument = (BarInstrument) iobject;

		if (! isDisplayValid (barInstrument))
		{
			return;
		}

		configure (
			new Command()
			{
				public void perform ()
				{
					configureChart (chart, barInstrument, false);
					chart.setBackgroundPaint (content.getBackground ());
					setTitle (barInstrument.getTitle ());
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
	 * Configure the chart.
	 *
	 * @param chart The chart to configure.
	 * @param barInstrument The bar instrument.
	 * @param showSampleMarker If true a sample marker is displayed.
	 */
	public static void configureChart (
		JFreeChart chart, BarInstrument barInstrument, boolean showSampleMarker)
	{
		ConfigurationSensor sensorConfig = barInstrument.getSensorConfig ();

		double diff = barInstrument.getScalingHigh () - barInstrument.getScalingLow ();
		BarPlot plot = (BarPlot) chart.getPlot ();

		plot.setFitToWindow (barInstrument.isFitToWindow ());
		plot.setRange (barInstrument.getScalingLow (), barInstrument.getScalingHigh ());
		plot.setShowMaxMarker (barInstrument.showMaxMarker ());
		plot.setShowMinMarker (barInstrument.showMinMarker ());
		plot.setBarPaint (new Color(barInstrument.getBarColor ()));

		if (showSampleMarker)
		{
			plot.setMaxMarkerValue (barInstrument.getScalingLow () + 3 * diff / 4);
			plot.setMinMarkerValue (barInstrument.getScalingLow () + 1 * diff / 4);
		}
		else
		{
			plot.setMaxMarkerValue (barInstrument.getScalingLow ());
			plot.setMinMarkerValue (barInstrument.getScalingHigh ());
		}

		String label =
			barInstrument.showAxisLabel ()
			? barInstrument.getSensorConfig ().getFormattedAxisLabel () : "";

		ValueAxis axis = null;

		if (barInstrument.isAxisLogarithmic ())
		{
			axis = new LogarithmicAxis(label);
			((LogarithmicAxis) axis).setLog10TickLabelsFlag (false);
			((LogarithmicAxis) axis).setExpTickLabelsFlag (false);
			((LogarithmicAxis) axis).setStrictValuesFlag (false);
		}
		else
		{
			axis = new NumberAxis(label);
		}

		Font font = Font.decode (barInstrument.getFont ());

		axis.setTickLabelFont (font);
		axis.setLabelFont (font);
		axis.setTickMarksVisible (barInstrument.showScaleLabel ());
		axis.setTickLabelsVisible (barInstrument.showScaleLabel ());

		plot.setRangeAxis (axis);
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
		BarInstrument barInstrument = (BarInstrument) iobject;

		if (! isDisplayValid (barInstrument))
		{
			return;
		}

		ConfigurationSensor sensorConfig = ((BarInstrument) iobject).getSensorConfig ();

		if (sensorConfig.getSensorId () == sensorId)
		{
			BarPlot plot = (BarPlot) chart.getPlot ();

			if (value > plot.getMaxMarkerValue ())
			{
				plot.setMaxMarkerValue (value);
			}

			if (value < plot.getMinMarkerValue ())
			{
				plot.setMinMarkerValue (value);
			}

			dataset.setValue (new Double(value));

			if (
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
		BarInstrument barInstrument = (BarInstrument) iobject;

		if (instumentUniqueId == barInstrument.getUniqueId ())
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
			"client", "BarInstrumentDisplay.configureDisplay", "Antialised: " + antiAlised);
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