/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectTableModel;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
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
 * This gui pane displays a list of interfaces. It lets the user add, edit
 * and delete interfaces.
 *
 * @version $Id: InterfaceListEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class InterfaceListEditor
	extends SwingGUIPane
	implements TableModelListener
{
	/** This table contains all interfaces. */
	public JTable interfaceTable;

	/** Table model for the interface table. */
	private IObjectTableModel interfaceTableModel;

	/**
	 * Create a new InterfaceListEditor.
	 */
	public InterfaceListEditor ()
	{
		super("InterfaceListEditor");
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
					getClass ().getResource ("/swixml/InterfaceListEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			interfaceTableModel =
				new IObjectTableModel()
					{
						private String[] columnNames =
							new String[]
							{
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.name")
							};
						private Class[] columnClasses = new Class[]
							{
								String.class,
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
							InterfaceRegistry registry = (InterfaceRegistry) iobject;

							if (registry == null)
							{
								return 0;
							}

							return registry.getInterfaceCount ();
						}

						public Object getValueAt (int row, int col)
						{
							InterfaceRegistry registry = (InterfaceRegistry) iobject;
							Interface iface = (Interface) registry.getInterface (row);

							switch (col)
							{
								case 0:
									return iface.getName ();

								default:
									return null;
							}
						}
					};

			interfaceTableModel.addTableModelListener (this);
			interfaceTable.setModel (interfaceTableModel);

			interfaceTable.addMouseListener (
				new MouseAdapter()
				{
					public void mouseClicked (MouseEvent e)
					{
						if (e.getClickCount () == 2)
						{
							editInterfaceAction.actionPerformed (null);
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "InterfaceListEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		InterfaceRegistry registry = (InterfaceRegistry) iobject;
		int row = 0;

		for (Iterator i = registry.getInterfaceIterator (); i.hasNext ();)
		{
			interfaceTableModel.setValueAt (i.next (), row++, 0);
		}

		interfaceTable.revalidate ();
	}

	/**
	 * Called when the system table model has changed.
	 *
	 * @param event The table model event.
	 */
	public void tableChanged (TableModelEvent event)
	{
		interfaceTable.revalidate ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new InterfaceRegistry();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new InterfaceListEditor();
	}

	/**
	 * Create a new interface.
	 */
	public Action addInterfaceAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				InterfaceRegistry registry = (InterfaceRegistry) iobject;
				Interface iface = new Interface();

				SessionContext sessionContext = new SessionContext("interfaceregistry");

				sessionContext.add ("interfaceregistry", registry);

				registry.addInterface (iface);
				CommandTools.performAsync (
					new ShowDialog("InterfaceEditor", iface, sessionContext));
			}
		};

	/**
	 * Edit an existing interface.
	 */
	public Action editInterfaceAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				int selectedRow = interfaceTable.getSelectedRow ();

				if (selectedRow != -1)
				{
					CommandTools.performAsync (
						new ShowDialog(
							"InterfaceEditor",
							((InterfaceRegistry) iobject).getInterface (selectedRow)));
				}
			}
		};

	/**
	 * Delete an existing interface.
	 */
	public Action deleteInterfaceAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				InterfaceRegistry registry = (InterfaceRegistry) iobject;
				int selectedRow = interfaceTable.getSelectedRow ();

				if (selectedRow == -1)
				{
					return;
				}

				if (
					JOptionPane.showConfirmDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-interfacing.removeinterface"), "Metix", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					registry.removeInterface (
						((InterfaceRegistry) iobject).getInterface (selectedRow));
				}
			}
		};

	/**
	 * Close the display.
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