/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectTableModel;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
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
 * This gui pane displays a list of all gaging stations and lets the user
 * add, edit, and delete gagingstations.
 *
 * @version $Id: GagingStationListEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class GagingStationListEditor
	extends SwingGUIPane
	implements TableModelListener
{
	/** This table lists all gaging stations. */
	public JTable gagingStationTable;

	/** A model for the gaging station table. */
	private IObjectTableModel gagingStationTableModel;

	/**
	 * Create a new GagingStationListEditor.
	 */
	public GagingStationListEditor ()
	{
		super("GagingStationListEditor");
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
					getClass ().getResource ("/swixml/GagingStationListEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			gagingStationTableModel =
				new IObjectTableModel()
					{
						private String[] columnNames =
							new String[]
							{
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.name"),
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.longitude"),
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.latitude")
							};
						private Class[] columnClasses =
							new Class[]
							{
								String.class,
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
							GagingStationRegistry registry = (GagingStationRegistry) iobject;

							if (registry == null)
							{
								return 0;
							}

							return registry.getGagingStationSize ();
						}

						public Object getValueAt (int row, int col)
						{
							GagingStationRegistry registry = (GagingStationRegistry) iobject;
							GagingStation station = (GagingStation) registry.getGagingStation (row);

							switch (col)
							{
								case 0:
									return station.getName ();

								case 1:
									return station.getLongitude ();

								case 2:
									return station.getLatitude ();

								default:
									return null;
							}
						}
					};
			gagingStationTableModel.addTableModelListener (this);
			gagingStationTable.setModel (gagingStationTableModel);

			gagingStationTable.addMouseListener (
				new MouseAdapter()
				{
					public void mouseClicked (MouseEvent e)
					{
						if (e.getClickCount () == 2)
						{
							editGagingStationAction.actionPerformed (null);
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "GagingStationListEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Load the data object into the gui.
	 */
	public void loadFromObject ()
	{
		GagingStationRegistry registry = (GagingStationRegistry) iobject;
		int row = 0;

		for (Iterator i = registry.gagingStationIterator (); i.hasNext ();)
		{
			gagingStationTableModel.setValueAt (i.next (), row++, 0);
		}

		gagingStationTable.revalidate ();
	}

	/**
	 * Store the gui values to the data object.
	 */
	public void storeToObject ()
	{
	}

	/**
	 * Called when the table model has changed.
	 */
	public void tableChanged (TableModelEvent x)
	{
		gagingStationTable.revalidate ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new GagingStationRegistry();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new GagingStationListEditor();
	}

	/**
	 * Create a new gaging station.
	 */
	public Action addGagingStationAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				GagingStationRegistry registry = (GagingStationRegistry) iobject;
				GagingStation station = new GagingStation();

				SessionContext sessionContext = new SessionContext("gagingstationregistry");

				sessionContext.add ("gagingstationregistry", registry);

				registry.addGagingStation (station);
				CommandTools.performAsync (
					new ShowDialog("GagingStationEditor", station, sessionContext));
			}
		};

	/**
	 * Edit an existing gaging station.
	 */
	public Action editGagingStationAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				int selectedRow = gagingStationTable.getSelectedRow ();

				if (selectedRow != -1)
				{
					CommandTools.performAsync (
						new ShowDialog(
							"GagingStationEditor",
							((GagingStationRegistry) iobject).getGagingStation (selectedRow)));
				}
			}
		};

	/**
	 * Delete an existing gaging station.
	 */
	public Action deleteGagingStationAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				GagingStationRegistry registry = (GagingStationRegistry) iobject;
				int selectedRow = gagingStationTable.getSelectedRow ();

				if (selectedRow == -1)
				{
					return;
				}

				if (
					JOptionPane.showConfirmDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.removegagingstation"), "Metix", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					registry.removeGagingStation (
						((GagingStationRegistry) iobject).getGagingStation (selectedRow));
				}
			}
		};

	/**
	 * Close the display
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
			}
		};
}