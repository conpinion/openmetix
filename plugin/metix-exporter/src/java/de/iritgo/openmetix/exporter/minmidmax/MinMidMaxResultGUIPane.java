/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.minmidmax;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITitledPanel;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class MinMidMaxResultGUIPane
	extends InstrumentConfiguratorGUIPane
{
	/** The sensor list. */
	public JTable minMidMaxResults;

	/** Title around the results. */
	public ITitledPanel title;

	/** Result table model. */
	public ResultTableModel resultModel;

	/** The sensor model. */
	public DefaultListModel sensorModel;

	/** Query result. */
	private List resultList;

	/** Query start date. */
	public long startDate;

	/** Query end date. */
	public long endDate;

	/** Query start date display. */
	public JLabel startDateDisplay;

	/** Query end date display. */
	public JLabel endDateDisplay;

	/** Number formatter. */
	protected static DecimalFormat formatter = new DecimalFormat("#.##");

	/**
	 * Create a new MinMidMaxResultGUIPane
	 */
	public MinMidMaxResultGUIPane ()
	{
		super("MinMidMaxResult");
		resultList = new LinkedList();
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
		return new MinMidMaxResultGUIPane();
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
					getClass ().getResource ("/swixml/MinMidMaxResultGUIPane.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
		}

		minMidMaxResults.setModel (resultModel = new ResultTableModel());
		minMidMaxResults.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);

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
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
	}

	/**
	 * Close the result window
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
			}
		};

	/**
	 * Close the result window
	 */
	public Action saveAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();

				chooser.setDialogType (JFileChooser.SAVE_DIALOG);

				int returnVal = chooser.showSaveDialog (null);
				File file = null;

				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					file = chooser.getSelectedFile ();

					try
					{
						DateFormat df =
							DateFormat.getDateTimeInstance (DateFormat.SHORT, DateFormat.SHORT);
						String start = df.format (new Date(startDate));
						String end = df.format (new Date(endDate));

						FileWriter fileWriter = new FileWriter(file);

						for (Iterator i = resultList.iterator (); i.hasNext ();)
						{
							GagingStationRegistry gagingStationRegistry =
								(GagingStationRegistry) AppContext.instance ().getUser ()
																  .getNamedIritgoObject (
									"GagingStationRegistry", GagingStationRegistry.class);

							double sensorId = ((Double) i.next ()).longValue ();
							GagingStation station =
								gagingStationRegistry.findStationOfSensor ((int) sensorId);
							GagingSensor gsensor =
								gagingStationRegistry.findSensor ((int) sensorId);
							String output =
								station.getName () + ";" + gsensor.getName () + ";" +
								station.getUniqueId () + ";" + gsensor.getUniqueId () + ";" +
								start + ";" + end + ";" +
								formatter.format (((Double) i.next ()).doubleValue ()) + ";" +
								formatter.format (((Double) i.next ()).doubleValue ()) + ";" +
								formatter.format (((Double) i.next ()).doubleValue ()) + "\n";

							fileWriter.write (output);
						}

						fileWriter.close ();
					}
					catch (Exception x)
					{
						System.out.println (x);

						JOptionPane.showMessageDialog (
							content,
							Engine.instance ().getResourceService ().getStringWithoutException (
								"exportwizard.saveerror"), "Metix", JOptionPane.OK_OPTION);

						return;
					}

					JOptionPane.showMessageDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"exportwizard.saved"), "Metix", JOptionPane.OK_OPTION);
				}

				display.close ();
			}
		};

	/**
	 * Display the query results.
	 *
	 * @param outputs The query result.
	 * @param startDate Query start date.
	 * @param endDate Query end date.
	 */
	public void viewSensors (List outputs, long startDate, long endDate)
	{
		try
		{
			title.setTitle ("minmidmax.done");

			this.startDate = startDate;
			this.endDate = endDate;

			DateFormat df = DateFormat.getDateTimeInstance (DateFormat.LONG, DateFormat.LONG);

			startDateDisplay.setText (df.format (new Date(startDate)));
			endDateDisplay.setText (df.format (new Date(endDate)));

			GagingStationRegistry gagingStationRegistry =
				(GagingStationRegistry) AppContext.instance ().getUser ().getNamedIritgoObject (
					"GagingStationRegistry", GagingStationRegistry.class);

			for (Iterator i = outputs.iterator (); i.hasNext ();)
			{
				double sensorId = ((Double) i.next ()).doubleValue ();

				resultList.add (new Double(sensorId));

				GagingStation station = gagingStationRegistry.findStationOfSensor ((int) sensorId);

				double min = ((Double) i.next ()).doubleValue ();

				resultList.add (new Double(min));

				double mid = ((Double) i.next ()).doubleValue ();

				resultList.add (new Double(mid));

				double max = ((Double) i.next ()).doubleValue ();

				resultList.add (new Double(max));

				resultModel.addEntry (
					min, mid, max, station.getName (),
					gagingStationRegistry.findSensor ((int) sensorId).getName ());
			}

			content.repaint ();
		}
		catch (Exception x)
		{
		}
	}
}