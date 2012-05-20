/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectComboBoxModel;
import de.iritgo.openmetix.core.iobject.IObjectTableModel;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;


/**
 * This gui pane is used to edit gaging stations.
 *
 * @version $Id: GagingStationEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class GagingStationEditor
	extends SwingGUIPane
	implements TableModelListener
{
	/** The name of the gaging station. */
	public ITextField name;

	/** The gaging station's location. */
	public ITextField location;

	/** Earth location: longitude. */
	public ITextField longitude;

	/** Earth location: latitude. */
	public ITextField latitude;

	/** This table contains all sensors of the station. */
	public JTable sensorTable;

	/** Table model for the sensor table. */
	private IObjectTableModel sensorTableModel;

	/**
	 * Create a new GagingStationEditor.
	 */
	public GagingStationEditor ()
	{
		super("GagingStationEditor");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (ClientPlugin.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/GagingStationEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			sensorTableModel =
				new IObjectTableModel()
					{
						private String[] columnNames =
							new String[]
							{
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.name"),
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.unit")
							};
						private Class[] columnClasses = new Class[]
							{
								String.class,
								String.class
							};

						public int getColumnCount ()
						{
							return columnNames.length;
						}

						public String getColumnName (int col)
						{
							return columnNames[col];
						}

						public Class getColumnClass (int col)
						{
							return columnClasses[col];
						}

						public int getRowCount ()
						{
							GagingStation station = (GagingStation) iobject;

							if (station == null)
							{
								return 0;
							}

							return station.getSensorCount ();
						}

						public Object getValueAt (int row, int col)
						{
							GagingStation station = (GagingStation) iobject;
							GagingSensor sensor = (GagingSensor) station.getSensor (row);

							switch (col)
							{
								case 0:
									return sensor.getName ();

								case 1:
									return sensor.getDimensionUnit ().toString ();

								default:
									return null;
							}
						}
					};
			sensorTableModel.addTableModelListener (this);
			sensorTable.setModel (sensorTableModel);

			sensorTable.addMouseListener (
				new MouseAdapter()
				{
					public void mouseClicked (MouseEvent e)
					{
						if (e.getClickCount () == 2)
						{
							editSensorAction.actionPerformed (null);
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "GagingStationEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		GagingStation station = (GagingStation) iobject;

		name.setText (station.getName ());
		location.setText (station.getLocation ());
		longitude.setText (station.getLongitude ());
		latitude.setText (station.getLatitude ());

		int row = 0;

		for (Iterator i = station.sensorIterator (); i.hasNext ();)
		{
			sensorTableModel.setValueAt (i.next (), row++, 0);
		}

		sensorTable.revalidate ();

		name.selectAll ();

		try
		{
			InterfaceRegistry interfaceRegistry =
				(InterfaceRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"InterfaceRegistry", InterfaceRegistry.class);

			IObjectComboBoxModel model = new IObjectComboBoxModel();

			model.update (interfaceRegistry.getGagingSystems ());
		}
		catch (NoSuchIObjectException x)
		{
		}
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		GagingStation station = (GagingStation) iobject;

		station.setName (name.getText ());
		station.setLocation (location.getText ());
		station.setLongitude (longitude.getText ());
		station.setLatitude (latitude.getText ());
		station.update ();
	}

	/**
	 * Called when the table model has changed.
	 *
	 * @param event The table model event.
	 */
	public void tableChanged (TableModelEvent event)
	{
		sensorTable.revalidate ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new GagingStation();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new GagingStationEditor();
	}

	/**
	 * Create a new sensor.
	 */
	public Action addSensorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();

				GagingStation station = (GagingStation) iobject;
				GagingSensor sensor = new GagingSensor();

				SessionContext sessionContext = new SessionContext("gagingstation");

				sessionContext.add ("gagingstation", station);

				station.addSensor (sensor);
				CommandTools.performAsync (
					new ShowDialog("GagingSensorEditor", sensor, sessionContext));
			}
		};

	/**
	 * Edit an existing sensor.
	 */
	public Action editSensorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();

				int selectedRow = sensorTable.getSelectedRow ();

				if (selectedRow != -1)
				{
					CommandTools.performAsync (
						new ShowDialog(
							"GagingSensorEditor", ((GagingStation) iobject).getSensor (selectedRow)));
				}
			}
		};

	/**
	 * Delete an existing sensor.
	 */
	public Action deleteSensorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				GagingStation station = (GagingStation) iobject;
				int selectedRow = sensorTable.getSelectedRow ();

				if (selectedRow == -1)
				{
					return;
				}

				if (
					JOptionPane.showConfirmDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.removegagingsensor"), "Metix", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					station.removeSensor (station.getSensor (selectedRow));
				}
			}
		};

	/**
	 * Save the data object and close the display.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();
				display.close ();
			}
		};

	/**
	 * Close the display.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (sessionContext != null)
				{
					GagingStationRegistry list =
						(GagingStationRegistry) sessionContext.get ("gagingstationregistry");

					list.removeGagingStation ((GagingStation) iobject);
				}

				display.close ();
			}
		};
}