/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.app.instrument.Instrument;
import de.iritgo.openmetix.app.userprofile.ActiveDisplay;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import java.util.Iterator;


/**
 * Base class for all instrument gui panes.
 *
 * @version $Id: InstrumentGUIPane.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class InstrumentGUIPane
	extends SwingGUIPane
{
	/** Goes true if the instrument is configured. */
	protected boolean configured;

	/** The number of all sensors in all instruments. */
	protected static int sensorTotalCount;

	/** The number of all sensors in this instrument. */
	protected int sensorCount;

	/**
	 * Create a new InstrumentGUIPane.
	 */
	public InstrumentGUIPane ()
	{
		super("InstrumentGUIPane");
	}

	/**
	 * Create a new InstrumentGUIPane.
	 *
	 * @param id The guiPaneId
	 */
	public InstrumentGUIPane (String id)
	{
		super(id);
	}

	/**
	 * Close the BarInstrumentConfigurator from swing button.
	 */
	public void systemClose ()
	{
		super.systemClose ();
		cancelInstrument ();
	}

	/**
	 * Called when the user has canceled an instrument creation.
	 * Removes the instrument from the system.
	 */
	protected void cancelInstrument ()
	{
		if (sessionContext != null)
		{
			if (sessionContext.contains ("edit"))
			{
				return;
			}
		}

		UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();

		Instrument instrument = (Instrument) iobject;

		for (Iterator i = userProfile.activeDisplayIterator (); i.hasNext ();)
		{
			ActiveDisplay ad = (ActiveDisplay) i.next ();

			if (ad.getDisplayUniqueId () == instrument.getUniqueId ())
			{
				return;
			}
		}

		userProfile.removeInstrument (instrument);
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new InstrumentGUIPane();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return null;
	}

	/**
	 * Check wether the instrument is valid or not. An instrument is
	 * valid if all attributes (instrument and sensors) are transfered from
	 * the server to the client.
	 *
	 * @param instrument The instrument to check.
	 * @return True for a valid instrument.
	 */
	public boolean isDisplayValid (Instrument instrument)
	{
		int sensorsValid = 0;

		for (Iterator iter = instrument.sensorConfigIterator (); iter.hasNext ();)
		{
			ConfigurationSensor sensorConfig = (ConfigurationSensor) iter.next ();

			try
			{
				if (isConfigSensorValid (sensorConfig))
				{
					sensorsValid++;
				}
			}
			catch (Exception x)
			{
			}
		}

		return instrument.isValid () && (sensorsValid > 0) &&
		(sensorsValid == instrument.getSensorConfigCount ());
	}

	/**
	 * Check if the sensor and the sensor config valid
	 *
	 * @param config The sensor configuration.
	 * @return True if the configuration is valid.
	 */
	public boolean isConfigSensorValid (final ConfigurationSensor config)
	{
		if ((! config.isValid ()) || (! config.getSensor ().isValid ()))
		{
			Engine.instance ().getProxyEventRegistry ().addEventListener (config, this);

			Engine.instance ().getProxyEventRegistry ().addEventListener (
				GagingStationRegistry.getRegistry (), this);

			GagingStationRegistry.getRegistry ().registerListener (this);

			return false;
		}

		return true;
	}

	/**
	 * Perform the spcified command to configure the instrument.
	 * If this method is called a second time, nothing happens.
	 *
	 * @param command The command to execute.
	 */
	protected void configure (Command command)
	{
		if (! configured)
		{
			command.perform ();
			configured = true;
		}
	}

	/**
	 * Check wether the gui is configured.
	 *
	 * @return True for a configured gui.
	 */
	public boolean isConfigured ()
	{
		return configured;
	}

	/**
	 * Set the configured state.
	 *
	 * @param configured The new configured state.
	 */
	public void setConfigured (boolean configured)
	{
		this.configured = configured;
	}

	/**
	 * Increment the sensor counter.
	 */
	protected void incSensorCount ()
	{
		synchronized (InstrumentGUIPane.class)
		{
			++sensorTotalCount;
			++sensorCount;
		}
	}

	/**
	 * Decrement the sensor counter.
	 */
	protected void decSensorCount ()
	{
		synchronized (InstrumentGUIPane.class)
		{
			sensorTotalCount -= sensorCount;
			sensorCount = 0;
		}
	}

	/**
	 * Get the number of all sensors in all instruments.
	 *
	 * @return The sensor count.
	 */
	public static int getSensorTotalCount ()
	{
		return sensorTotalCount;
	}

	/**
	 * Get the number of all sensors in this instrument.
	 *
	 * @return The sensor count.
	 */
	public int getSensorCount ()
	{
		return sensorCount;
	}
}