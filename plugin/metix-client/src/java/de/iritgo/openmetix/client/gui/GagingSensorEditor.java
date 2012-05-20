/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.sensorbehaviour.GagingSensorBehaviourDescriptor;
import de.iritgo.openmetix.app.gui.GagingSensorBehaviourEditor;
import de.iritgo.openmetix.app.util.Dimension;
import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
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
 * This gui pane is used to edit gaging sensors.
 *
 * @version $Id: GagingSensorEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class GagingSensorEditor
	extends SwingGUIPane
{
	/** Name of the sensor. */
	public ITextField name;

	/** Sensor value dimension. */
	public JComboBox dimension;

	/** Gaging sensor type. */
	public JComboBox sensorType;

	/** The behaviour editor. */
	private GagingSensorBehaviourEditor behaviourEditor;

	/** This title panel contains the behaviour editor. */
	public JPanel behaviourPanel;

	/**
	 * Create a new GagingSensorEditor.
	 */
	public GagingSensorEditor ()
	{
		super("GagingSensorEditor");
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
					getClass ().getResource ("/swixml/GagingSensorEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			dimension.setModel (Dimension.createComboBoxModel ());

			sensorType.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							changeSensorType (
								(GagingSensorBehaviourDescriptor) sensorType.getSelectedItem ());
						}
					}
				});

			for (Iterator i = GagingSensorBehaviourDescriptor.behaviourIterator (); i.hasNext ();)
			{
				sensorType.addItem (i.next ());
			}
		}
		catch (Exception x)
		{
			Log.logError ("client", "GagingSensorEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		GagingSensor sensor = (GagingSensor) iobject;

		if (sensor == null)
		{
			return;
		}

		name.setText (sensor.getName ());
		dimension.setSelectedItem (sensor.getDimensionUnit ());

		for (int i = 0; i < sensorType.getItemCount (); ++i)
		{
			if (
				((GagingSensorBehaviourDescriptor) sensorType.getItemAt (i)).getId ().equals (
					sensor.getBehaviourId ()))
			{
				sensorType.setSelectedIndex (i);

				break;
			}
		}

		behaviourEditor.loadSensor (sensor);

		name.selectAll ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		GagingSensor sensor = (GagingSensor) iobject;

		sensor.setName (name.getText ());
		sensor.setDimensionUnit ((Dimension.Unit) dimension.getSelectedItem ());
		sensor.setBehaviourId (
			((GagingSensorBehaviourDescriptor) sensorType.getSelectedItem ()).getId ());

		if (! sensor.getBehaviourId ().equals ("SystemSensorBehaviour"))
		{
			sensor.setSystemId (0);
			sensor.setOutputId (0);
		}

		behaviourEditor.storeSensor (sensor);

		sensor.update ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new GagingSensor();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new GagingSensorEditor();
	}

	/**
	 * Called when a new sensor type was selected.
	 *
	 * @param sensorType The new sensor type.
	 */
	public void changeSensorType (GagingSensorBehaviourDescriptor behaviourDescriptor)
	{
		behaviourEditor =
			behaviourDescriptor.getBehaviourEditor (ClientPlugin.class.getClassLoader ());
		behaviourPanel.removeAll ();
		behaviourPanel.add (behaviourEditor.getPanel ());
		((JDialog) behaviourPanel.getTopLevelAncestor ()).pack ();
		behaviourEditor.loadSensor ((GagingSensor) iobject);
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
					GagingStation list = (GagingStation) sessionContext.get ("gagingstation");

					list.removeSensor ((GagingSensor) iobject);
				}

				display.close ();
			}
		};
}