/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystem;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ICheckBox;
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
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriver;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverDescriptor;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverEditor;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.NullDriver;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.NullDriverEditor;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import de.iritgo.openmetix.interfacing.link.Interface;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Properties;


/**
 * This gui pane is used to edit gaging systems.
 *
 * @version $Id: GagingSystemEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingSystemEditor
	extends SwingGUIPane
	implements TableModelListener
{
	/** The name of the gaging system. */
	public ITextField name;

	/** This combobox contains all available system drivers. */
	public JComboBox driver;

	/** If checked the system will be active. */
	public ICheckBox active;

	/** If checked the system stores measurements to the database. */
	public ICheckBox storeToDatabase;

	/** This table contains all system outputs. */
	public JTable outputsTable;

	/** Table model for the outputs table. */
	private IObjectTableModel outputsTableModel;

	/** Interfaces. */
	public JComboBox interfaces;

	/** Interfaces model. */
	private IObjectComboBoxModel interfacesModel;

	/** Panel containing the system outputs. */
	public JPanel outputsPanel;

	/** Panel containing the driver params. */
	public JPanel driverPanel;

	/** Tabbed pane containing the system settings. */
	public JTabbedPane tabbedSettings;

	/** The driver editor. */
	private GagingSystemDriverEditor driverEditor;

	/** The gaging system driver. */
	private GagingSystemDriver systemDriver;

	/**
	 * Create a new GagingSystemEditor.
	 */
	public GagingSystemEditor ()
	{
		super("GagingSystemEditor");
		systemDriver = new NullDriver();
		driverEditor = new NullDriverEditor();
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

			swingEngine.setClassLoader (AppPlugin.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/GagingSystemEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			for (Iterator i = GagingSystemDriverDescriptor.driverIterator (); i.hasNext ();)
			{
				driver.addItem (i.next ());
			}

			driver.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							changeDriver ((GagingSystemDriverDescriptor) driver.getSelectedItem ());
						}
					}
				});

			outputsTable.addMouseListener (
				new MouseAdapter()
				{
					public void mouseClicked (MouseEvent e)
					{
						if (e.getClickCount () == 2)
						{
							editOutputAction.actionPerformed (null);
						}
					}
				});

			interfacesModel = new IObjectComboBoxModel();
			interfaces.setModel (interfacesModel);

			tabbedSettings.setTitleAt (
				0,
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-interfacing.outputs"));
			tabbedSettings.setTitleAt (
				1,
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix-interfacing.driverparameters"));
		}
		catch (Exception x)
		{
			Log.logError ("client", "GagingSystemEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		GagingSystem system = (GagingSystem) iobject;

		name.setText (system.getName ());
		active.setSelected (system.getActive ());
		storeToDatabase.setSelected (system.isStoreToDatabase ());

		for (int i = 0; i < driver.getItemCount (); ++i)
		{
			if (
				((GagingSystemDriverDescriptor) driver.getItemAt (i)).getId ().equals (
					system.getDriverId ()))
			{
				driver.setSelectedIndex (i);

				break;
			}
		}

		int row = 0;

		for (Iterator i = system.getOutputIterator (); i.hasNext ();)
		{
			outputsTableModel.setValueAt (i.next (), row++, 0);
		}

		outputsTable.revalidate ();

		try
		{
			InterfaceRegistry ifaceRegistry =
				(InterfaceRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"InterfaceRegistry", InterfaceRegistry.class);

			interfacesModel.update (ifaceRegistry.getInterfaces ());

			for (int i = 0; i < interfacesModel.getSize (); ++i)
			{
				Interface iface = (Interface) interfacesModel.getElementAt (i);

				if (iface.getUniqueId () == system.getInterfaceId ())
				{
					interfaces.setSelectedIndex (i);

					break;
				}
			}
		}
		catch (NoSuchIObjectException x)
		{
		}

		driverEditor.loadSystem (system);

		name.selectAll ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		GagingSystem system = (GagingSystem) iobject;

		system.setName (name.getText ());
		system.setActive (active.isSelected ());
		system.setStoreToDatabase (storeToDatabase.isSelected ());
		system.setDriverId (((GagingSystemDriverDescriptor) driver.getSelectedItem ()).getId ());
		system.setInterfaceId (((Interface) interfaces.getSelectedItem ()).getUniqueId ());

		driverEditor.storeSystem (system);

		system.update ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new GagingSystem();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new GagingSystemEditor();
	}

	/**
	 * Called when the table model has changed.
	 *
	 * @param event The table model event.
	 */
	public void tableChanged (TableModelEvent event)
	{
		outputsTable.revalidate ();
	}

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
					InterfaceRegistry list =
						(InterfaceRegistry) sessionContext.get ("interfaceregistry");

					list.removeGagingSystem ((GagingSystem) iobject);
				}

				display.close ();
			}
		};

	/**
	 * Create a new system output.
	 */
	public Action addOutputAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();

				GagingSystem system = (GagingSystem) iobject;
				GagingOutput output = new GagingOutput();

				Properties props = new Properties();

				props.put ("index", new Integer(outputsTableModel.getRowCount ()));

				SessionContext sessionContext = new SessionContext("gagingsystem");

				sessionContext.add ("gagingsystem", system);
				system.addOutput (output);

				String guiPaneId =
					((GagingSystemDriverDescriptor) driver.getSelectedItem ()).getOutputEditorId ();

				CommandTools.performAsync (
					new ShowDialog(guiPaneId, output, sessionContext), props);
			}
		};

	/**
	 * Edit an existing output.
	 */
	public Action editOutputAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();

				int selectedRow = outputsTable.getSelectedRow ();

				if (selectedRow != -1)
				{
					String guiPaneId =
						((GagingSystemDriverDescriptor) driver.getSelectedItem ()).getOutputEditorId ();

					Properties props = new Properties();

					props.put ("index", new Integer(selectedRow));

					CommandTools.performAsync (
						new ShowDialog(guiPaneId, ((GagingSystem) iobject).getOutput (selectedRow)),
						props);
				}
			}
		};

	/**
	 * Delete an existing output.
	 */
	public Action deleteOutputAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				GagingSystem system = (GagingSystem) iobject;
				int selectedRow = outputsTable.getSelectedRow ();

				if (selectedRow == -1)
				{
					return;
				}

				if (
					JOptionPane.showConfirmDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-interfacing.removegagingoutput"), "Metix",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					system.removeOutput (system.getOutput (selectedRow));
				}
			}
		};

	/**
	 * Called when a new driver was selected.
	 *
	 * @param descriptor The descriptor of the new driver.
	 */
	public void changeDriver (GagingSystemDriverDescriptor descriptor)
	{
		try
		{
			systemDriver =
				(GagingSystemDriver) descriptor.getClassLoader ()
											   .loadClass (descriptor.getClassName ()).newInstance ();

			driverEditor =
				(GagingSystemDriverEditor) descriptor.getClassLoader ()
													 .loadClass (descriptor.getEditorClassName ())
													 .newInstance ();
			driverPanel.removeAll ();
			driverPanel.add (BorderLayout.NORTH, driverEditor.getPanel ());
			((JDialog) driverPanel.getTopLevelAncestor ()).pack ();
			driverPanel.repaint ();
			driverEditor.loadSystem ((GagingSystem) iobject);

			outputsTableModel =
				new IObjectTableModel()
					{
						private String[] columnNames =
							new String[]
							{
								"#",
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.name"),
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.unit")
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
							return columnNames.length +
							systemDriver.getCustomOutputParameterCount ();
						}

						public String getColumnName (int col)
						{
							if (col < columnNames.length)
							{
								return columnNames[col];
							}
							else
							{
								return systemDriver.getCustomOutputParameterName (
									col - columnNames.length);
							}
						}

						public Class getColumnClass (int col)
						{
							if (col < columnNames.length)
							{
								return columnClasses[col];
							}
							else
							{
								return systemDriver.getCustomOutputParameterClass (
									col - columnNames.length);
							}
						}

						public int getRowCount ()
						{
							GagingSystem system = (GagingSystem) iobject;

							if (system == null)
							{
								return 0;
							}

							return system.getOutputCount ();
						}

						public Object getValueAt (int row, int col)
						{
							GagingSystem system = (GagingSystem) iobject;
							GagingOutput output = (GagingOutput) system.getOutput (row);

							switch (col)
							{
								case 0:
									return String.valueOf (row);

								case 1:
									return output.getName ();

								case 2:
									return output.getDimensionUnit ().toString ();

								default:
									return systemDriver.getCustomOutputParameterValue (
										system, output, row, col - columnNames.length);
							}
						}
					};
			outputsTableModel.addTableModelListener (this);
			outputsTable.setModel (outputsTableModel);

			FontMetrics fm = content.getGraphics ().getFontMetrics ();
			Rectangle2D numBounds = fm.getStringBounds ("00000", content.getGraphics ());

			outputsTable.getColumnModel ().getColumn (0).setMaxWidth ((int) numBounds.getWidth ());
		}
		catch (ClassNotFoundException x)
		{
			Log.logError (
				"client", "GagingSystemEditor",
				"Unable to create editor for driver '" + descriptor.getId () + "' (" + x + ")");
		}
		catch (InstantiationException x)
		{
			Log.logError (
				"client", "GagingSystemEditor",
				"Unable to create editor for driver '" + descriptor.getId () + "' (" + x + ")");
		}
		catch (IllegalAccessException x)
		{
			Log.logError (
				"client", "GagingSystemEditor",
				"Unable to create editor for driver '" + descriptor.getId () + "' (" + x + ")");
		}
	}
}