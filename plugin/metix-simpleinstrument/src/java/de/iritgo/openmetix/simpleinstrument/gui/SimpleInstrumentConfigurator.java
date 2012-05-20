/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.simpleinstrument.gui;


import de.iritgo.openmetix.app.gui.IFontChooser;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IFormattedTextField;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.simpleinstrument.SimpleInstrument;
import de.iritgo.openmetix.simpleinstrument.SimpleInstrumentSensor;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * This gui pane is used to configure simple instruments.
 *
 * @version $Id: SimpleInstrumentConfigurator.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SimpleInstrumentConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	/** Instrument title. */
	public ITextField title;

	/** Instrument text color display. */
	public JTextField textColorDisplay;

	/** Instrument background color display. */
	public JTextField backgroundColorDisplay;

	/** Instrument background color button. */
	public JButton backgroundColorChoose;

	/** True if the background color should be transparent. */
	public JCheckBox transparent;

	/** Instrument text font display. */
	public JTextField fontDisplay;

	/** The station an sensor selector. */
	public IStationSensorSelector stationSensorSelector;

	/** True if the minimum alarm should be enabled. */
	public JCheckBox warnMin;

	/** True if the aximum alarm should be enabled. */
	public JCheckBox warnMax;

	/** Minimum alarm value. */
	public IFormattedTextField warnMinValue;

	/** Maximum alarm value. */
	public IFormattedTextField warnMaxValue;

	/**
	 * Create a new SimpleInstrumentConfigurator
	 */
	public SimpleInstrumentConfigurator ()
	{
		super("SimpleInstrumentConfigurator");
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new SimpleInstrument();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new SimpleInstrumentConfigurator();
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
					getClass ().getResource ("/swixml/SimpleInstrumentConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			warnMinValue.setFormatterFactory (Tools.getDoubleFormatter ());
			warnMaxValue.setFormatterFactory (Tools.getDoubleFormatter ());

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

			transparent.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						backgroundColorChoose.setEnabled (! transparent.isSelected ());
					}
				});

			getDisplay ().putProperty ("weightx", new Double(2.0));
		}
		catch (Exception x)
		{
			Log.logError ("client", "SimpleInstrumentConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;

		stationSensorSelector.update ();

		if (! simpleInstrument.isValid ())
		{
			return;
		}

		if (simpleInstrument.getSensorConfig () == null)
		{
			simpleInstrument.addSensorConfig (new SimpleInstrumentSensor());

			return;
		}

		title.setText (simpleInstrument.getTitle ());
		textColorDisplay.setBackground (new Color(simpleInstrument.getTextColor ()));
		backgroundColorDisplay.setBackground (new Color(simpleInstrument.getBackgroundColor ()));
		transparent.setSelected (simpleInstrument.isTransparent ());

		Font font = Font.decode (simpleInstrument.getFont ());

		fontDisplay.setText (font.getName () + " " + String.valueOf (font.getSize ()));

		stationSensorSelector.setStationIdSensorId (
			simpleInstrument.getSensorConfig ().getStationId (),
			simpleInstrument.getSensorConfig ().getSensorId ());

		warnMin.setSelected (simpleInstrument.getSensorConfig ().isWarnMin ());
		warnMax.setSelected (simpleInstrument.getSensorConfig ().isWarnMax ());
		warnMinValue.setValue (new Double(simpleInstrument.getSensorConfig ().getWarnMinValue ()));
		warnMaxValue.setValue (new Double(simpleInstrument.getSensorConfig ().getWarnMaxValue ()));

		title.selectAll ();

		super.loadFromObject ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;

		if (! simpleInstrument.isValid ())
		{
			return;
		}

		simpleInstrument.setTitle (title.getText ());
		simpleInstrument.setTransparent (transparent.isSelected ());
		simpleInstrument.update ();

		simpleInstrument.getSensorConfig ().setStationId (
			stationSensorSelector.getSelectedStationId ());
		simpleInstrument.getSensorConfig ().setSensorId (
			stationSensorSelector.getSelectedSensorId ());

		simpleInstrument.getSensorConfig ().setWarnMin (warnMin.isSelected ());
		simpleInstrument.getSensorConfig ().setWarnMax (warnMax.isSelected ());
		simpleInstrument.getSensorConfig ().setWarnMinValue (
			NumberTools.toDouble (warnMinValue.getText (), 0.0));
		simpleInstrument.getSensorConfig ().setWarnMaxValue (
			NumberTools.toDouble (warnMaxValue.getText (), 0.0));

		checkWarningRange ();
	}

	/**
	 * Check that the warning minumum value is smaller than the maximum value.
	 * Eventually exchange the values.
	 */
	private void checkWarningRange ()
	{
		SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;
		SimpleInstrumentSensor sensorConfig =
			(SimpleInstrumentSensor) simpleInstrument.getSensorConfig ();

		double minValue;
		double maxValue;

		minValue = NumberTools.toDouble (warnMinValue.getText (), 0.0);
		maxValue = NumberTools.toDouble (warnMaxValue.getText (), 0.0);

		if (minValue > maxValue)
		{
			sensorConfig.setWarnMinValue (NumberTools.toDouble (warnMaxValue.getText (), 0.0));
			sensorConfig.setWarnMaxValue (NumberTools.toDouble (warnMinValue.getText (), 0.0));

			warnMinValue.setValue (
				new Double(simpleInstrument.getSensorConfig ().getWarnMaxValue ()));
			warnMaxValue.setValue (
				new Double(simpleInstrument.getSensorConfig ().getWarnMinValue ()));
			simpleInstrument.update ();
		}
	}

	/**
	 * Show a color chooser and set the instrument color from the
	 * chooser result.
	 */
	public Action textColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(simpleInstrument.getTextColor ()));

				if (newColor != null)
				{
					simpleInstrument.setTextColor (newColor.getRGB ());
					textColorDisplay.setBackground (newColor);
				}
			}
		};

	/**
	 * Show a color chooser and set the instrument background color from the
	 * chooser result.
	 */
	public Action backgroundColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(simpleInstrument.getBackgroundColor ()));

				if (newColor != null)
				{
					simpleInstrument.setBackgroundColor (newColor.getRGB ());
					backgroundColorDisplay.setBackground (newColor);
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
				SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;
				Font newFont =
					IFontChooser.showDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.chooseFont"), Font.decode (simpleInstrument.getFont ()));

				if (newFont != null)
				{
					simpleInstrument.setFont (IFontChooser.encode (newFont));
					fontDisplay.setText (
						newFont.getName () + " " + String.valueOf (newFont.getSize ()));
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
				SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;
				boolean wasFresh = simpleInstrument.isFresh ();

				simpleInstrument.setFresh (false);
				storeToObject ();
				simpleInstrument.getSensorConfig ().update ();

				if (wasFresh)
				{
					CommandTools.performAsync (new ShowWindow("SimpleInstrumentDisplay", iobject));
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