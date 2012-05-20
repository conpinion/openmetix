/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.windroseinstrument.gui;


import de.iritgo.openmetix.app.gui.IFontChooser;
import de.iritgo.openmetix.app.gui.ISensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.windroseinstrument.WindRoseInstrument;
import de.iritgo.openmetix.windroseinstrument.WindRoseInstrumentSensor;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;


/**
 * This gui pane is used to configure wind rose instruments.
 *
 * @version $Id: WindRoseInstrumentConfigurator.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class WindRoseInstrumentConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	/** Instrument title. */
	public ITextField title;

	/** Instrument text color display. */
	public JTextField textColorDisplay;

	/** Instrument needle color display. */
	public JTextField needleColorDisplay;

	/** Instrument rose color display. */
	public JTextField roseColorDisplay;

	/** Instrument minExtrema color display. */
	public JTextField minExtremaColorDisplay;

	/** Instrument hourExtrema color display. */
	public JTextField hourExtremaColorDisplay;

	/** Instrument text font dialog activator. */
	public JTextField fontDisplay;

	/** Needle type combo box. */
	public JComboBox needleType;

	/** Needle against the storm force value. */
	public JCheckBox stormForce;

	/** 10-Minutes Extrema. */
	public JCheckBox minExtrema;

	/** 1-Hour Extrema. */
	public JCheckBox hourExtrema;

	/** Reverse the needle direction. */
	public JCheckBox reverseNeedle;

	/** Is stormforce checkbox selected. */
	private boolean stormForceSelected;

	/** Is 10-Minutes Extrema checkbox selected. */
	private boolean minExtremaSelected;

	/** Is 1-Hour Extrema checkbox selected. */
	private boolean hourExtremaSelected;

	/** Is reverse needle checkbox selected. */
	private boolean reverseNeedleSelected;

	/** Default date format. */
	private SimpleDateFormat fullDateFormat;

	/**Sensor selector. */
	public ISensorSelector sensorSelector;

	/**
	 * Elements of the needle type combobox.
	 */
	private class NeedleTypeItem
	{
		/** Type name. */
		public String name;

		/** Type id. */
		public int id;

		/**
		 * Create a new combobox item.
		 *
		 * @param name The needle type name.
		 * @param id The needle type id.
		 */
		public NeedleTypeItem (String name, int id)
		{
			this.name = name;
			this.id = id;
		}

		/**
		 * Convert this item to a string.
		 *
		 * @return The string representation.
		 */
		public String toString ()
		{
			return name;
		}
	}

	/**
	 * Create a new WindRoseInstrumentConfigurator
	 */
	public WindRoseInstrumentConfigurator ()
	{
		super("WindRoseInstrumentConfigurator");
		fullDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new WindRoseInstrument();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new WindRoseInstrumentConfigurator();
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
					getClass ().getResource ("/swixml/WindRoseInstrumentConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			ResourceService resources = Engine.instance ().getResourceService ();

			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needleArrow"), 0));
			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needleLong"), 2));
			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needlePin"), 3));
			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needlePlum"), 4));
			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needlePointer"), 5));
			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needleShip"), 6));
			needleType.addItem (
				new NeedleTypeItem(resources.getStringWithoutException ("metix.needleWind"), 7));

			sensorSelector.setNeedlePrametersEnabled (false);

			reverseNeedle.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (reverseNeedleSelected == false)
						{
							reverseNeedleSelected = true;
						}
						else
						{
							reverseNeedleSelected = false;
						}
					}
				});

			stormForce.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (stormForceSelected == false)
						{
							needleType.setSelectedIndex (1);
							needleType.setEnabled (false);
							stormForceSelected = true;
							sensorSelector.setNeedlePrametersEnabled (true);
						}
						else
						{
							needleType.setSelectedIndex (1);
							needleType.setEnabled (true);
							stormForceSelected = false;
							sensorSelector.setNeedlePrametersEnabled (false);
						}
					}
				});

			minExtrema.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (minExtremaSelected == false)
						{
							minExtremaSelected = true;
						}
						else
						{
							minExtremaSelected = false;
						}
					}
				});

			hourExtrema.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (hourExtremaSelected == false)
						{
							hourExtremaSelected = true;
						}
						else
						{
							hourExtremaSelected = false;
						}
					}
				});

			getDisplay ().putProperty ("weightx", new Double(2.0));
		}
		catch (Exception x)
		{
			Log.logError ("client", "WindRoseInstrumentConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;

		sensorSelector.update ();

		if (! windRoseInstrument.isValid ())
		{
			return;
		}

		if (windRoseInstrument.getSensorConfig () == null)
		{
			windRoseInstrument.addSensorConfig (new WindRoseInstrumentSensor());

			windRoseInstrument.addSensorConfig (new WindRoseInstrumentSensor());

			return;
		}

		title.setText (windRoseInstrument.getTitle ());
		textColorDisplay.setBackground (new Color(windRoseInstrument.getTextColor ()));

		Font font = Font.decode (windRoseInstrument.getFont ());

		fontDisplay.setText (font.getName () + " " + String.valueOf (font.getSize ()));

		for (int i = 0; i < needleType.getItemCount (); ++i)
		{
			if (
				((NeedleTypeItem) needleType.getItemAt (i)).id == windRoseInstrument.getNeedleType ())
			{
				needleType.setSelectedIndex (i);

				break;
			}
		}

		if (windRoseInstrument.getNeedleType () == 8)
		{
			needleType.setSelectedIndex (1);
			needleType.setEnabled (false);
			stormForce.setSelected (true);
			stormForceSelected = true;
		}

		needleColorDisplay.setBackground (new Color(windRoseInstrument.getNeedleColor ()));
		roseColorDisplay.setBackground (new Color(windRoseInstrument.getRoseColor ()));
		minExtremaColorDisplay.setBackground (new Color(windRoseInstrument.getMinExtremaColor ()));
		hourExtremaColorDisplay.setBackground (
			new Color(windRoseInstrument.getHourExtremaColor ()));

		if (windRoseInstrument.getMinExtrema () == 1)
		{
			minExtrema.setSelected (true);
			minExtremaSelected = true;
		}

		if (windRoseInstrument.getHourExtrema () == 1)
		{
			hourExtrema.setSelected (true);
			hourExtremaSelected = true;
		}

		if (windRoseInstrument.getReverseNeedle () == 1)
		{
			reverseNeedle.setSelected (true);
			reverseNeedleSelected = true;
		}

		sensorSelector.setStationIdSensorId (
			windRoseInstrument.getSensorConfig ().getStationId (),
			windRoseInstrument.getSensorConfig ().getSensorId (),
			windRoseInstrument.getSensorConfig (1).getSensorId ());

		title.selectAll ();

		super.loadFromObject ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;

		if (! windRoseInstrument.isValid ())
		{
			return;
		}

		windRoseInstrument.setTitle (title.getText ());

		if (stormForceSelected == false)
		{
			windRoseInstrument.setNeedleType (((NeedleTypeItem) needleType.getSelectedItem ()).id);
		}
		else
		{
			windRoseInstrument.setNeedleType (8);
		}

		if (minExtremaSelected == true)
		{
			windRoseInstrument.setMinExtrema (1);
		}
		else
		{
			windRoseInstrument.setMinExtrema (0);
		}

		if (hourExtremaSelected == true)
		{
			windRoseInstrument.setHourExtrema (1);
		}
		else
		{
			windRoseInstrument.setHourExtrema (0);
		}

		if (reverseNeedleSelected == true)
		{
			windRoseInstrument.setReverseNeedle (1);
		}
		else
		{
			windRoseInstrument.setReverseNeedle (0);
		}

		windRoseInstrument.update ();

		windRoseInstrument.getSensorConfig ().setStationId (sensorSelector.getSelectedStationId ());
		windRoseInstrument.getSensorConfig ().setSensorId (sensorSelector.getSelectedSensorId ());

		windRoseInstrument.getSensorConfig (1).setStationId (
			sensorSelector.getSelectedStationId ());
		windRoseInstrument.getSensorConfig (1).setSensorId (
			sensorSelector.getSelectedNeedleSensorId ());
		windRoseInstrument.getSensorConfig ().update ();

		windRoseInstrument.update ();
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
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(windRoseInstrument.getTextColor ()));

				if (newColor != null)
				{
					windRoseInstrument.setTextColor (newColor.getRGB ());
					textColorDisplay.setBackground (newColor);
				}
			}
		};

	/**
	 * Show a color chooser and set the needle color from the
	 * chooser result.
	 */
	public Action needleColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(windRoseInstrument.getNeedleColor ()));

				if (newColor != null)
				{
					windRoseInstrument.setNeedleColor (newColor.getRGB ());
					needleColorDisplay.setBackground (newColor);
				}
			}
		};

	/**
	 * Show a color chooser and set the needle color from the
	 * chooser result.
	 */
	public Action roseColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(windRoseInstrument.getRoseColor ()));

				if (newColor != null)
				{
					windRoseInstrument.setRoseColor (newColor.getRGB ());
					roseColorDisplay.setBackground (newColor);
				}
			}
		};

	/**
	 * Show a color chooser and set the minExtrema color from the
	 * chooser result.
	 */
	public Action minExtremaColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(windRoseInstrument.getMinExtremaColor ()));

				if (newColor != null)
				{
					windRoseInstrument.setMinExtremaColor (newColor.getRGB ());
					minExtremaColorDisplay.setBackground (newColor);
				}
			}
		};

	/**
	 * Show a color chooser and set the hourExtrema color from the
	 * chooser result.
	 */
	public Action hourExtremaColorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				Color newColor =
					JColorChooser.showDialog (
						content, "Color", new Color(windRoseInstrument.getHourExtremaColor ()));

				if (newColor != null)
				{
					windRoseInstrument.setHourExtremaColor (newColor.getRGB ());
					hourExtremaColorDisplay.setBackground (newColor);
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
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				Font newFont =
					IFontChooser.showDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.chooseFont"), Font.decode (windRoseInstrument.getFont ()));

				if (newFont != null)
				{
					windRoseInstrument.setFont (IFontChooser.encode (newFont));
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
				WindRoseInstrument windRoseInstrument = (WindRoseInstrument) iobject;
				boolean wasFresh = windRoseInstrument.isFresh ();

				windRoseInstrument.setFresh (false);
				storeToObject ();
				windRoseInstrument.getSensorConfig ().update ();
				windRoseInstrument.getSensorConfig (1).update ();

				if (wasFresh)
				{
					CommandTools.performAsync (
						new ShowWindow("WindRoseInstrumentDisplay", iobject));
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