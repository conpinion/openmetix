/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.simulator;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverEditor;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;


/**
 * Configuration dialog for the weather simulation driver.
 *
 * @version $Id: WeatherSimulatorDriverEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class WeatherSimulatorDriverEditor
	extends GagingSystemDriverEditor
{
	/** The sampling interval. */
	public JSlider interval;

	/** The sampling interval display value. */
	public JLabel intervalValue;

	/** This table contains all configured simulator outputs. */
	public JTable outputs;

	/** The table model of the outputs table. */
	private WeatherSimulatorOutputTableModel outputsModel;

	/** Click to delete an output. */
	public JButton deleteOutput;

	/**
	 * Create a new configuration dialog.
	 */
	public WeatherSimulatorDriverEditor ()
	{
	}

	/**
	 * Create the editor panel.
	 *
	 * @return The editor panel.
	 */
	protected JPanel createEditorPanel ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (InterfacingSimulatorPlugin.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/WeatherSimulatorDriverDialog.xml"));

			interval.addChangeListener (
				new ChangeListener()
				{
					public void stateChanged (ChangeEvent e)
					{
						intervalValue.setText (String.valueOf (interval.getValue ()));
					}
				});

			outputs.getSelectionModel ().addListSelectionListener (
				new ListSelectionListener()
				{
					public void valueChanged (ListSelectionEvent e)
					{
						if (e.getValueIsAdjusting ())
						{
							return;
						}

						ListSelectionModel lsm = (ListSelectionModel) e.getSource ();

						deleteOutput.setEnabled (! lsm.isSelectionEmpty ());
					}
				});

			outputsModel = new WeatherSimulatorOutputTableModel();
			outputs.setModel (outputsModel);
			outputs.setDefaultEditor (String.class, new DefaultCellEditor(new JTextField()));
			outputs.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);

			return panel;
		}
		catch (Exception x)
		{
			Log.logError ("client", "WeatherSimulatorDriverDialog", x.toString ());

			return new JPanel();
		}
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	public void loadFromProperties ()
	{
		int val = NumberTools.toInt ((String) configProperties.get ("interval"), 1);

		interval.setValue (val);
		intervalValue.setText (String.valueOf (val));

		outputsModel.clear ();

		for (int index = 0;; ++index)
		{
			String key = "out" + String.valueOf (index);

			if (! configProperties.containsKey (key + "min"))
			{
				break;
			}

			WeatherSimulatorOutput output =
				new WeatherSimulatorOutput(
					NumberTools.toDouble (configProperties.getProperty (key + "min"), 0.0),
					NumberTools.toDouble (configProperties.getProperty (key + "max"), 0.0),
					NumberTools.toDouble (configProperties.getProperty (key + "var"), 0.0));

			outputsModel.addOutput (output);
		}
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	public void storeToProperties ()
	{
		configProperties.put ("interval", String.valueOf (interval.getValue ()));

		int index = 0;

		for (Iterator i = outputsModel.outputIterator (); i.hasNext ();)
		{
			WeatherSimulatorOutput output = (WeatherSimulatorOutput) i.next ();
			String key = "out" + String.valueOf (index);

			configProperties.put (key + "min", String.valueOf (output.getMinimum ()));
			configProperties.put (key + "max", String.valueOf (output.getMaximum ()));
			configProperties.put (key + "var", String.valueOf (output.getVariation ()));
			++index;
		}
	}

	/**
	 * Create a new output.
	 */
	public Action addOutputAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				outputsModel.addOutput (new WeatherSimulatorOutput());
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
				outputsModel.deleteOutput (outputs.getSelectedRow ());
			}
		};
}