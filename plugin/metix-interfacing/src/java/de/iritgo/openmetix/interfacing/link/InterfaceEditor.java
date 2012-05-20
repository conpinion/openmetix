/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.ITitledPanel;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.interfacing.InterfacingPlugin;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;


/**
 * This gui pane is used to edit interfaces.
 *
 * @version $Id: InterfaceEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class InterfaceEditor
	extends SwingGUIPane
{
	/** The name of the gaging system. */
	public ITextField name;

	/** Interface driver. */
	public JComboBox driver;

	/** This title panel contains the driver editor. */
	public ITitledPanel driverPanel;

	/** The driver editor. */
	private InterfaceDriverEditor driverEditor;

	/**
	 * Create a new InterfaceEditor.
	 */
	public InterfaceEditor ()
	{
		super("InterfaceEditor");
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
					getClass ().getResource ("/swixml/InterfaceEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			for (Iterator i = InterfaceDriverDescriptor.driverIterator (); i.hasNext ();)
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
							changeDriver ((InterfaceDriverDescriptor) driver.getSelectedItem ());
						}
					}
				});

			driverEditor = new NullInterfaceDriverEditor();
		}
		catch (Exception x)
		{
			Log.logError ("client", "InterfaceEditor.initGUI", x.toString ());
			x.printStackTrace ();
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		Interface iface = (Interface) iobject;

		name.setText (iface.getName ());

		for (int i = 0; i < driver.getItemCount (); ++i)
		{
			if (
				((InterfaceDriverDescriptor) driver.getItemAt (i)).getId ().equals (
					iface.getDriverId ()))
			{
				driver.setSelectedIndex (i);

				break;
			}
		}

		driverEditor.loadInterface (iface);
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		Interface iface = (Interface) iobject;

		iface.setName (name.getText ());
		iface.setDriverId (((InterfaceDriverDescriptor) driver.getSelectedItem ()).getId ());

		driverEditor.storeInterface (iface);

		iface.update ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new Interface();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new InterfaceEditor();
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

					list.removeInterface ((Interface) iobject);
				}

				display.close ();
			}
		};

	/**
	 * Called when a new driver was selected.
	 *
	 * @param driverDescriptor The new driver.
	 */
	public void changeDriver (InterfaceDriverDescriptor driverDescriptor)
	{
		driverEditor = driverDescriptor.getDriverEditor (InterfacingPlugin.class.getClassLoader ());
		driverPanel.removeAll ();
		driverPanel.add (driverEditor.getPanel ());
		((JDialog) driverPanel.getTopLevelAncestor ()).pack ();
		driverEditor.loadInterface ((Interface) iobject);
	}
}