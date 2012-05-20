/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.gui;


import de.iritgo.openmetix.app.gui.IDataReductionSensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IButton;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.datareduction.command.DataReductionCommand;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * @version $Id: DataReductionConfigurator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class DataReductionConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	public ITextField timeStart = new ITextField();
	private DefaultComboBoxModel timeStartModel;
	public JComboBox listTimeStart;
	public IButton okButton;
	private int time;
	private int timeID;
	public IDataReductionSensorSelector dataReductionSensorSelector;

	public class ComboBoxHelp
	{
		public String id;
		public String text;

		public ComboBoxHelp (String id)
		{
			this.id = id;
			text = Engine.instance ().getResourceService ().getStringWithoutException (id);

			if (text.startsWith ("!"))
			{
				text = text.substring (1, text.length () - 1);
			}
		}

		public String toString ()
		{
			return text;
		}
	}

	public DataReductionConfigurator ()
	{
		super("DataReductionConfigurator");
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return null;
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new DataReductionConfigurator();
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		super.initGUI ();

		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/DataReductionConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			timeStartModel = new DefaultComboBoxModel();
			timeStartModel.addElement (new ComboBoxHelp("minute"));
			timeStartModel.addElement (new ComboBoxHelp("day"));
			timeStartModel.addElement (new ComboBoxHelp("month"));

			listTimeStart.setModel (timeStartModel);

			dataReductionSensorSelector.update ();

			timeStart.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent e)
					{
						if (checkTime ())
						{
							okButton.setEnabled (true);
						}
						else
						{
							okButton.setEnabled (false);
						}
					}
				});

			timeStart.setText ("0");
		}
		catch (Exception x)
		{
			Log.logError ("client", "DataReductionConfigurator.initGUI", x.toString ());
		}

		super.loadFromObject ();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		super.loadFromObject ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
	}

	private boolean checkTime ()
	{
		try
		{
			char[] chars = timeStart.getText ().toCharArray ();
			StringBuffer after = new StringBuffer(timeStart.getText ().length ());

			for (int i = 0; i < chars.length; i++)
			{
				if (! Character.isWhitespace (chars[i]))
				{
					after.append (chars[i]);
				}
			}

			double timevalue = Double.parseDouble (after.toString ());

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Save all modifications to the instrument, close the configurator
	 * and open the instrument display.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync (new ShowDialog("DataReductionDisplay"));

				DataReductionCommand drc = new DataReductionCommand();
				String timeNumber = timeStart.getText ();

				drc.setTimeNumber (timeNumber);

				int timeIndex = listTimeStart.getSelectedIndex ();

				drc.setTimeIndex (timeIndex);

				long stationID = dataReductionSensorSelector.getSelectedStationId ();

				drc.setStationID (stationID);

				long sensorID = dataReductionSensorSelector.getSelectedSensorId ();

				drc.setSensorID (sensorID);
				drc.setDeleteAllParameters (dataReductionSensorSelector.getDeleteAllParameters ());
				CommandTools.performAsync (drc);

				display.close ();
			}
		};

	/**
	 * Cancel the instrument configuration and close the
	 * configurator.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
			}
		};
}