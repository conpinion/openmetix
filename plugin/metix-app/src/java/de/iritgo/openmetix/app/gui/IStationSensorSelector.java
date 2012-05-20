/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.core.iobject.IObjectComboBoxModel;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import org.swixml.SwingEngine;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;


/**
 * This panel combines a station and a sensor combobox.
 *
 * @version $Id: IStationSensorSelector.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class IStationSensorSelector
	extends JPanel
{
	/** Gaging stations. */
	public JComboBox stations;

	/** Gaging sensors. */
	public JComboBox parameters;

	/** Gaging station model. */
	private IObjectComboBoxModel stationsModel;

	/** Gaging sensor model. */
	private IObjectComboBoxModel parametersModel;

	/** The registry of gaging stations. */
	private GagingStationRegistry gagingStationRegistry;

	/** The id of the currently selected station. */
	private long selectedStationId;

	/** The id of the currently selected sensor. */
	private long selectedSensorId;

	/**
	 * Create a new IStationSensorSelector.
	 */
	public IStationSensorSelector ()
	{
		super(new GridBagLayout());

		selectedStationId = -1;
		selectedSensorId = -1;

		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/IStationSensorSelector.xml"));

			add (panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			stationsModel = new IObjectComboBoxModel();
			stations.setModel (stationsModel);

			parametersModel = new IObjectComboBoxModel();
			parameters.setModel (parametersModel);

			stations.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							updateParameterModel ();
						}
					}
				});

			parameters.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							fireActionPerformed (
								new ActionEvent(this, (int) System.currentTimeMillis (), ""));
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "IStationSensorSelector.initGUI", x.toString ());
		}
	}

	/**
	 * Adds an ActionListener to the selector.
	 *
	 * @param l the ActionListener to be added
	 */
	public void addActionListener (ActionListener l)
	{
		listenerList.add (ActionListener.class, l);
	}

	/**
	 * Removes an ActionListener from the button.
	 * If the listener is the currently set Action
	 * for the button, then the Action
	 * is set to null.
	 *
	 * @param l the listener to be removed
	 */
	public void removeActionListener (ActionListener l)
	{
		listenerList.remove (ActionListener.class, l);
	}

	/**
	 * Notifies all listeners that have registered interest for
	* notification on this event type.  The event instance
	* is lazily created using the event
	* parameter.
	*
	* @param event The ActionEvent object
	*/
	protected void fireActionPerformed (ActionEvent event)
	{
		Object[] listeners = listenerList.getListenerList ();
		ActionEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ActionListener.class)
			{
				((ActionListener) listeners[i + 1]).actionPerformed (e);
			}
		}
	}

	/**
	 * Get the id of the selected station.
	 *
	 * @return The id of the selected station.
	 */
	public long getSelectedStationId ()
	{
		GagingStation station = (GagingStation) stationsModel.getSelectedItem ();

		return station == null ? 0 : station.getUniqueId ();
	}

	/**
	 * Get the name of the selected station.
	 *
	 * @return The name of the selected station.
	 */
	public String getSelectedStationName ()
	{
		GagingStation station = (GagingStation) stationsModel.getSelectedItem ();

		return station == null ? "" : station.getName ();
	}

	/**
	 * Get the id of the selected sensor.
	 *
	 * @return The id of the selected sensor.
	 */
	public long getSelectedSensorId ()
	{
		GagingSensor sensor = (GagingSensor) parametersModel.getSelectedItem ();

		return sensor == null ? 0 : sensor.getUniqueId ();
	}

	/**
	 * Get the name of the selected sensor.
	 *
	 * @return The name of the selected sensor.
	 */
	public String getSelectedSensorName ()
	{
		GagingSensor sensor = (GagingSensor) parametersModel.getSelectedItem ();

		return sensor == null ? "" : sensor.getName ();
	}

	/**
	 * Get the selected sensor.
	 *
	 * @return The selected sensor.
	 */
	public GagingSensor getSelectedSensor ()
	{
		return (GagingSensor) parametersModel.getSelectedItem ();
	}

	/**
	 * Set the station and sensorid.
	 */
	public void setStationIdSensorId (long stationId, long sensorId)
	{
		if (stationId == 0 || sensorId == 0)
		{
			return;
		}

		selectedStationId = stationId;
		selectedSensorId = sensorId;

		for (int i = 0; i < stationsModel.getSize (); ++i)
		{
			GagingStation station = (GagingStation) stationsModel.getElementAt (i);

			if (station.getUniqueId () == stationId)
			{
				stations.setSelectedItem (null);
				stations.setSelectedItem (station);

				return;
			}
		}
	}

	/**
	 * Set the sensorid.
	 */
	public void setSensorId (long sensorId)
	{
		try
		{
			GagingStationRegistry gagingStationRegistry =
				(GagingStationRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"GagingStationRegistry", GagingStationRegistry.class);

			GagingStation station = gagingStationRegistry.findStationOfSensor (sensorId);

			if (station != null)
			{
				setStationIdSensorId (station.getUniqueId (), sensorId);
			}
			else
			{
				stations.setSelectedItem (null);
			}
		}
		catch (NoSuchIObjectException x)
		{
		}
	}

	/**
	 * Fill the station with all available stations.
	 */
	protected void updateStationModel ()
	{
		try
		{
			GagingStationRegistry gagingStationRegistry =
				(GagingStationRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"GagingStationRegistry", GagingStationRegistry.class);

			stationsModel.update (gagingStationRegistry.getGagingStations ());
		}
		catch (NoSuchIObjectException x)
		{
		}
	}

	/**
	 * Fill the parameter with the sensors of the selected station.
	 */
	protected void updateParameterModel ()
	{
		GagingStation station = (GagingStation) stationsModel.getSelectedItem ();

		if (station != null)
		{
			parametersModel.update (station.getSensors ());

			for (Iterator i = station.sensorIterator (); i.hasNext ();)
			{
				GagingSensor sensor = (GagingSensor) i.next ();

				if (sensor.getUniqueId () == selectedSensorId)
				{
					parameters.setSelectedItem (sensor);

					break;
				}
			}
		}
		else
		{
			parametersModel.removeAllElements ();
		}
	}

	/**
	 * Update the selector.
	 */
	public void update ()
	{
		updateStationModel ();
	}

	/**
	 * Enable or disable the selector.
	 *
	 * @param enabled If true, the selector will be enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		super.setEnabled (enabled);
		stations.setEnabled (enabled);
		parameters.setEnabled (enabled);
	}

	/**
	 * Create some gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints (
		int x, int y, int width, int height, int fill, int wx, int wy, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		if (insets == null)
		{
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}
}