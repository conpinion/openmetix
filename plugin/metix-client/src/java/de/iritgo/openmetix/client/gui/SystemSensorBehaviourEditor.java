/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gui.GagingSensorBehaviourEditor;
import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectComboBoxModel;
import de.iritgo.openmetix.core.iobject.IObjectProxyEvent;
import de.iritgo.openmetix.core.iobject.IObjectProxyListener;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutput;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import org.swixml.SwingEngine;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;


/**
 * Editor for system output sensors.
 *
 * @version $Id: SystemSensorBehaviourEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class SystemSensorBehaviourEditor
	extends GagingSensorBehaviourEditor
	implements IObjectProxyListener
{
	/** Gaging system. */
	public JComboBox systems;

	/** Gaging system output. */
	public JComboBox outputs;

	/** Gaging system model. */
	private IObjectComboBoxModel systemsModel;

	/** Gaging system output model. */
	private IObjectComboBoxModel outputsModel;

	/** The interface registry. */
	private InterfaceRegistry interfaceRegistry;

	/** The currently edited sensor. */
	private GagingSensor sensor;

	/** Used to check that all systems and outputs are loaded. */
	private boolean valid;

	/**
	 * Create a new SystemSensorBehaviourEditor.
	 */
	public SystemSensorBehaviourEditor ()
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
					getClass ().getResource ("/swixml/SystemSensorBehaviourEditor.xml"));

			systemsModel = new IObjectComboBoxModel();
			systems.setModel (systemsModel);

			outputsModel = new IObjectComboBoxModel();
			outputs.setModel (outputsModel);

			interfaceRegistry =
				(InterfaceRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"InterfaceRegistry", InterfaceRegistry.class);

			systems.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							updateOutputsComboBox ();
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "SystemSensorBehaviourEditor.createEditorPanel", x.toString ());
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
		this.sensor = sensor;
		updateSystemsComboBox ();
	}

	/**
	 * Store the gui values into the gaging sensor.
	 *
	 * @param sensor The sensor to store to.
	 */
	public void storeSensor (GagingSensor sensor)
	{
		if (systems.getSelectedItem () != null && outputs.getSelectedItem () != null)
		{
			sensor.setSystemId (((IObject) systems.getSelectedItem ()).getUniqueId ());
			sensor.setOutputId (((IObject) outputs.getSelectedItem ()).getUniqueId ());
		}
	}

	/**
	 * Fill the gaging system combobox with the contents of the interface registry.
	 */
	protected void updateSystemsComboBox ()
	{
		systemsModel.update (interfaceRegistry.getGagingSystems ());

		for (Iterator i = interfaceRegistry.getGagingSystemIterator (); i.hasNext ();)
		{
			GagingSystem system = (GagingSystem) i.next ();

			if (system.getUniqueId () == sensor.getSystemId ())
			{
				systems.setSelectedItem (system);

				break;
			}
		}
	}

	/**
	 * Fill the gaging system output combobox with outputs of the selected system.
	 */
	protected void updateOutputsComboBox ()
	{
		GagingSystem system = (GagingSystem) systems.getSelectedItem ();

		if (system != null)
		{
			outputsModel.update (system.getOutputs ());

			for (Iterator i = system.getOutputIterator (); i.hasNext ();)
			{
				GagingOutput output = (GagingOutput) i.next ();

				if (output.getUniqueId () == sensor.getOutputId ())
				{
					outputs.setSelectedItem (output);

					break;
				}
			}
		}
		else
		{
			outputsModel.removeAllElements ();
		}
	}

	/**
	 * This method is called if a proxy event occurred.
	 *
	 * @param event The proxy event.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
	}
}