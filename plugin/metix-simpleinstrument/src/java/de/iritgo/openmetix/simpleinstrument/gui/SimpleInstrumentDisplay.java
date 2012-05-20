/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.simpleinstrument.gui;


import de.iritgo.openmetix.app.alarm.SensorAlarmEvent;
import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gui.InstrumentGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IMenuItem;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.simpleinstrument.SimpleInstrument;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;


/**
 * This gui pane is used to display simple instruments.
 *
 * @version $Id: SimpleInstrumentDisplay.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SimpleInstrumentDisplay
	extends InstrumentGUIPane
	implements InstrumentDisplay
{
	/** The current value. */
	public JLabel valueText;

	/** Our context menu. */
	JPopupMenu contextMenu;

	/**
	 * Create a new SimpleInstrumentDisplay.
	 */
	public SimpleInstrumentDisplay ()
	{
		super("SimpleInstrumentDisplay");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		try
		{
			UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();
			final Preferences preferences = userProfile.getPreferences ();

			if (preferences.getDrawAntiAliased ())
			{
				valueText =
					new JLabel("???")
						{
							public void paint (Graphics g)
							{
								Graphics2D g2 = (Graphics2D) g;

								g2.setRenderingHint (
									RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_ON);
								super.paint (g2);
							}
						};
			}
			else
			{
				valueText = new JLabel("0.0");
			}

			valueText.setHorizontalAlignment (JLabel.CENTER);

			content.setLayout (new BorderLayout());
			content.add (valueText, BorderLayout.CENTER);

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
							contextMenu.show (content, e.getX (), e.getY ());
						}
					}

					public void mouseReleased (MouseEvent e)
					{
						if (e.isPopupTrigger ())
						{
							contextMenu.show (content, e.getX (), e.getY ());
						}
					}
				};

			content.addMouseListener (mouseListener);

			configureDisplay ();
		}
		catch (Exception x)
		{
			Log.logError ("client", "SimpleInstrumentDisplay.initGUI", x.toString ());
		}
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
		return new SimpleInstrumentDisplay();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		final SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;

		if (! isDisplayValid (simpleInstrument))
		{
			return;
		}

		configure (
			new Command()
			{
				public void perform ()
				{
					valueText.setFont (Font.decode (simpleInstrument.getFont ()));
					valueText.setForeground (new Color(simpleInstrument.getTextColor ()));

					if (! simpleInstrument.isTransparent ())
					{
						content.setBackground (new Color(simpleInstrument.getBackgroundColor ()));
					}

					setTitle (simpleInstrument.getTitle ());
				}
			});

		if (getDisplay ().getProperty ("metixReload") != null)
		{
			CommandTools.performSimple ("StatusProgressStep");
			getDisplay ().removeProperty ("metixReload");
		}

		incSensorCount ();
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
		SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;

		if (! isDisplayValid (simpleInstrument))
		{
			return;
		}

		if (simpleInstrument.getSensorConfig ().getSensorId () == sensorId)
		{
			ConfigurationSensor sensorConfig = ((SimpleInstrument) iobject).getSensorConfig ();

			valueText.setText (sensorConfig.formatValue (value));

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
		SimpleInstrument simpleInstrument = (SimpleInstrument) iobject;

		if (instumentUniqueId == simpleInstrument.getUniqueId ())
		{
			receiveSensorValue (timestamp, value, stationId, sensorId);
		}
	}

	/**
	 * Configure the display new.
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