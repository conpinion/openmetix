/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gui.GagingSensorBehaviourEditor;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import org.swixml.SwingEngine;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.StringTokenizer;


/**
 * Editor for system output sensors.
 *
 * @version $Id: TwoInputSensorBehaviourEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class TwoInputSensorBehaviourEditor
	extends GagingSensorBehaviourEditor
{
	/** The first station and sensor selector. */
	public IStationSensorSelector stationSensorSelector1;

	/** The second station and sensor selector. */
	public IStationSensorSelector stationSensorSelector2;

	/** Behaviour title. */
	public JLabel title;

	/** Title of the first sensor. */
	public JLabel titleSensor1;

	/** Title of the second sensor. */
	public JLabel titleSensor2;

	/**
	 * Create a new SimplePeriodSensorBehaviourEditor.
	 */
	public TwoInputSensorBehaviourEditor ()
	{
	}

	/**
	 * Create the editor panel.
	 *
	 * @return The editor panel.
	 */
	protected JPanel createEditorPanel ()
	{
		JPanel panel = null;

		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (ClientPlugin.class.getClassLoader ());

			panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/TwoInputSensorBehaviourEditor.xml"));
		}
		catch (Exception x)
		{
			Log.logError (
				"client", "TwoInputSensorBehaviourEditor.createEditorPanel", x.toString ());
		}

		return panel;
	}

	/**
	 * Load the gaging sensor into the gui.
	 *
	 * @param sensor The sensor to load.
	 */
	public void loadSensor (GagingSensor sensor)
	{
		stationSensorSelector1.update ();
		stationSensorSelector2.update ();

		StringTokenizer st = new StringTokenizer(sensor.getInputs (), ",");

		if (st.hasMoreTokens ())
		{
			stationSensorSelector1.setSensorId (NumberTools.toInt (st.nextToken (), 0));
		}

		if (st.hasMoreTokens ())
		{
			stationSensorSelector2.setSensorId (NumberTools.toInt (st.nextToken (), 0));
		}
	}

	/**
	 * Store the gui values into the gaging sensor.
	 *
	 * @param sensor The sensor to store to.
	 */
	public void storeSensor (GagingSensor sensor)
	{
		sensor.setInputs (
			String.valueOf (stationSensorSelector1.getSelectedSensorId ()) + "," +
			String.valueOf (stationSensorSelector2.getSelectedSensorId ()));
	}
}