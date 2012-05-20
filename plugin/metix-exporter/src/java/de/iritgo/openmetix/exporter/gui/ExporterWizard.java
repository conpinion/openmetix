/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.gui;


import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.exporter.minmidmax.MinMidMaxExportCommand;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


public class ExporterWizard
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator, ListSelectionListener
{
	public class ComboBoxItem
	{
		public String id;
		public String text;

		public ComboBoxItem (String id)
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

	public class SensorItem
	{
		public String name;
		public long stationId;
		public long sensorId;

		public SensorItem (String name, long stationId, long sensorId)
		{
			this.name = name;
			this.stationId = stationId;
			this.sensorId = sensorId;
		}

		public String toString ()
		{
			return name;
		}
	}

	private SimpleDateFormat fullDateFormat;
	private DefaultComboBoxModel yearsStartModel;
	private DefaultComboBoxModel yearsStopModel;
	private DefaultComboBoxModel yearModel;
	private DefaultComboBoxModel seasonsModel;
	private DefaultComboBoxModel monthsStartModel;
	private DefaultComboBoxModel monthsStopModel;
	private DefaultComboBoxModel monthModel;
	private DefaultComboBoxModel decadeModel;
	private DefaultComboBoxModel daysStartModel;
	private DefaultComboBoxModel daysStopModel;
	private DefaultComboBoxModel dayModel;
	private DefaultComboBoxModel hoursStartModel;
	private DefaultComboBoxModel hoursStopModel;
	private DefaultComboBoxModel hourModel;
	public JComboBox listYearsStart;
	public JComboBox listYearsStop;
	public JComboBox listYear;
	public JComboBox listSeasons;
	public JComboBox listMonthsStart;
	public JComboBox listMonthsStop;
	public JComboBox listMonth;
	public JComboBox listDecade;
	public JComboBox listDaysStart;
	public JComboBox listDaysStop;
	public JComboBox listDay;
	public JComboBox listHoursStart;
	public JComboBox listHoursStop;
	public JComboBox listHour;

	/** The station an sensor selector. */
	public IStationSensorSelector stationSensorSelector;

	/** The absolute filename. */
	public JTextField filename;

	/** The sensor list. */
	public JList sensorList;

	/** The sensor model. */
	public DefaultListModel sensorModel;

	/**
	 * Create a new ReportInstrumentConfigurator
	 */
	public ExporterWizard ()
	{
		super("ExporterWizard");
		fullDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
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
		return new ExporterWizard();
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
					getClass ().getResource ("/swixml/ExporterWizard.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
		}

		yearsStartModel = new DefaultComboBoxModel();
		yearsStopModel = new DefaultComboBoxModel();
		yearModel = new DefaultComboBoxModel();
		seasonsModel = new DefaultComboBoxModel();
		monthsStartModel = new DefaultComboBoxModel();
		monthsStopModel = new DefaultComboBoxModel();
		monthModel = new DefaultComboBoxModel();
		decadeModel = new DefaultComboBoxModel();
		daysStartModel = new DefaultComboBoxModel();
		daysStopModel = new DefaultComboBoxModel();
		dayModel = new DefaultComboBoxModel();
		hoursStartModel = new DefaultComboBoxModel();
		hoursStopModel = new DefaultComboBoxModel();
		hourModel = new DefaultComboBoxModel();
		updateComboBoxModel ();

		listYearsStart.setModel (yearsStartModel);
		listYearsStart.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listYearsStart.setEnabled (true);
							listYearsStop.setEnabled (true);
							listSeasons.setEnabled (false);
							listMonthsStart.setEnabled (false);
							listMonthsStop.setEnabled (false);
							listMonth.setEnabled (false);
							listYear.setEnabled (true);
						}
						else
						{
							listYear.setEnabled (false);
							listSeasons.setEnabled (true);
							listMonthsStart.setEnabled (true);
							listMonthsStop.setEnabled (true);
							listMonth.setEnabled (true);
						}
					}
				}
			});

		listYearsStop.setModel (yearsStopModel);
		listYearsStop.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listYearsStart.setEnabled (true);
							listYearsStop.setEnabled (true);
							listSeasons.setEnabled (false);
							listMonthsStart.setEnabled (false);
							listMonthsStop.setEnabled (false);
							listMonth.setEnabled (false);
							listYear.setEnabled (true);
						}
						else
						{
							listYear.setEnabled (false);
							listSeasons.setEnabled (true);
							listMonthsStart.setEnabled (true);
							listMonthsStop.setEnabled (true);
							listMonth.setEnabled (true);
						}
					}
				}
			});

		listYear.setModel (yearModel);
		listYear.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listYearsStart.setEnabled (true);
							listYearsStop.setEnabled (true);
							listSeasons.setEnabled (false);
							listMonthsStart.setEnabled (false);
							listMonthsStop.setEnabled (false);
							listMonth.setEnabled (false);
						}
						else
						{
							listYearsStart.setEnabled (false);
							listYearsStop.setEnabled (false);
							listSeasons.setEnabled (true);
							listMonthsStart.setEnabled (true);
							listMonthsStop.setEnabled (true);
							listMonth.setEnabled (true);
						}
					}
				}
			});

		listSeasons.setModel (seasonsModel);
		listSeasons.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listMonthsStart.setEnabled (true);
							listMonthsStop.setEnabled (true);
							listMonth.setEnabled (true);
						}
						else
						{
							listMonthsStart.setEnabled (false);
							listMonthsStop.setEnabled (false);
							listMonth.setEnabled (false);
						}
					}
				}
			});

		listSeasons.setEnabled (false);

		listMonthsStart.setModel (monthsStartModel);
		listMonthsStart.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listSeasons.setEnabled (true);
							listMonth.setEnabled (true);
						}
						else
						{
							listSeasons.setEnabled (false);
							listMonth.setEnabled (false);
						}
					}
				}
			});
		listMonthsStart.setEnabled (false);
		listMonthsStop.setModel (monthsStopModel);
		listMonthsStop.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listSeasons.setEnabled (true);
							listMonth.setEnabled (true);
						}
						else
						{
							listSeasons.setEnabled (false);
							listMonth.setEnabled (false);
						}
					}
				}
			});

		listMonthsStop.setEnabled (false);
		listMonth.setModel (monthModel);
		listMonth.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listMonthsStart.setEnabled (true);
							listMonthsStop.setEnabled (true);
							listDaysStart.setEnabled (false);
							listDaysStop.setEnabled (false);
							listDecade.setEnabled (false);
							listDay.setEnabled (false);
						}
						else
						{
							listMonthsStart.setEnabled (false);
							listMonthsStop.setEnabled (false);
							listDaysStart.setEnabled (true);
							listDaysStop.setEnabled (true);
							listDecade.setEnabled (true);
							listDay.setEnabled (true);
						}
					}
				}
			});
		listMonth.setEnabled (false);

		listDecade.setModel (decadeModel);
		listDecade.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listDaysStart.setEnabled (true);
							listDaysStop.setEnabled (true);
							listDay.setEnabled (true);
						}
						else
						{
							listDaysStart.setEnabled (false);
							listDaysStop.setEnabled (false);
							listDay.setEnabled (false);
						}
					}
				}
			});
		listDecade.setEnabled (false);

		listDaysStart.setModel (daysStartModel);
		listDaysStart.setEnabled (false);
		listDaysStart.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listDecade.setEnabled (true);
							listDay.setEnabled (true);
						}
						else
						{
							listDecade.setEnabled (false);
							listDay.setEnabled (false);
						}
					}
				}
			});

		listDaysStop.setModel (daysStopModel);
		listDaysStop.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listDecade.setEnabled (true);
							listDay.setEnabled (true);
						}
						else
						{
							listDecade.setEnabled (false);
							listDay.setEnabled (false);
						}
					}
				}
			});
		listDaysStop.setEnabled (false);
		listDay.setModel (dayModel);
		listDay.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listDaysStart.setEnabled (true);
							listDaysStop.setEnabled (true);
							listDecade.setEnabled (true);
							listHoursStart.setEnabled (false);
							listHoursStop.setEnabled (false);
							listHour.setEnabled (false);
						}
						else
						{
							listDaysStart.setEnabled (false);
							listDaysStop.setEnabled (false);
							listDecade.setEnabled (false);
							listHoursStart.setEnabled (true);
							listHoursStop.setEnabled (true);
							listHour.setEnabled (true);
						}
					}
				}
			});
		listDay.setEnabled (false);

		listHoursStart.setModel (hoursStartModel);
		listHoursStart.setEnabled (false);
		listHoursStart.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listHour.setEnabled (true);
						}
						else
						{
							listHour.setEnabled (false);
						}
					}
				}
			});

		listHoursStop.setModel (hoursStopModel);
		listHoursStop.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listHour.setEnabled (true);
						}
						else
						{
							listHour.setEnabled (false);
						}
					}
				}
			});
		listHoursStop.setEnabled (false);
		listHour.setModel (hourModel);
		listHour.addItemListener (
			new ItemListener()
			{
				public void itemStateChanged (ItemEvent e)
				{
					if (e.getStateChange () == ItemEvent.SELECTED)
					{
						if (((ComboBoxItem) e.getItem ()).id.equals ("exporterwizard.dummy"))
						{
							listHoursStart.setEnabled (true);
							listHoursStop.setEnabled (true);
						}
						else
						{
							listHoursStart.setEnabled (false);
							listHoursStop.setEnabled (false);
						}
					}
				}
			});
		listHour.setEnabled (false);

		sensorModel = new DefaultListModel();
		sensorList.setModel (sensorModel);

		stationSensorSelector.update ();
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
	 * A sensor was selected from the list.
	 *
	 * @param event The list selection event.
	 */
	public void valueChanged (ListSelectionEvent event)
	{
	}

	/**
	 * Update the list of item for the combobox dateYear
	 */
	public void updateComboBoxModel ()
	{
		yearsStartModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		yearsStopModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		yearModel.addElement (new ComboBoxItem("exporterwizard.dummy"));

		for (int i = 1900; i <= 2050; i++)
		{
			yearsStartModel.addElement (new ComboBoxItem("" + i));
			yearsStopModel.addElement (new ComboBoxItem("" + i));
			yearModel.addElement (new ComboBoxItem("" + i));
		}

		seasonsModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		seasonsModel.addElement (new ComboBoxItem("winter"));
		seasonsModel.addElement (new ComboBoxItem("spring"));
		seasonsModel.addElement (new ComboBoxItem("summer"));
		seasonsModel.addElement (new ComboBoxItem("harvest"));

		monthsStartModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		monthsStopModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		monthModel.addElement (new ComboBoxItem("exporterwizard.dummy"));

		monthsStartModel.addElement (new ComboBoxItem("january"));
		monthsStopModel.addElement (new ComboBoxItem("january"));
		monthModel.addElement (new ComboBoxItem("january"));

		monthsStartModel.addElement (new ComboBoxItem("february"));
		monthsStopModel.addElement (new ComboBoxItem("february"));
		monthModel.addElement (new ComboBoxItem("february"));

		monthsStartModel.addElement (new ComboBoxItem("march"));
		monthsStopModel.addElement (new ComboBoxItem("march"));
		monthModel.addElement (new ComboBoxItem("march"));

		monthsStartModel.addElement (new ComboBoxItem("april"));
		monthsStopModel.addElement (new ComboBoxItem("april"));
		monthModel.addElement (new ComboBoxItem("april"));

		monthsStartModel.addElement (new ComboBoxItem("may"));
		monthsStopModel.addElement (new ComboBoxItem("may"));
		monthModel.addElement (new ComboBoxItem("may"));

		monthsStartModel.addElement (new ComboBoxItem("june"));
		monthsStopModel.addElement (new ComboBoxItem("june"));
		monthModel.addElement (new ComboBoxItem("june"));

		monthsStartModel.addElement (new ComboBoxItem("july"));
		monthsStopModel.addElement (new ComboBoxItem("july"));
		monthModel.addElement (new ComboBoxItem("july"));

		monthsStartModel.addElement (new ComboBoxItem("august"));
		monthsStopModel.addElement (new ComboBoxItem("august"));
		monthModel.addElement (new ComboBoxItem("august"));

		monthsStartModel.addElement (new ComboBoxItem("september"));
		monthsStopModel.addElement (new ComboBoxItem("september"));
		monthModel.addElement (new ComboBoxItem("september"));

		monthsStartModel.addElement (new ComboBoxItem("october"));
		monthsStopModel.addElement (new ComboBoxItem("october"));
		monthModel.addElement (new ComboBoxItem("october"));

		monthsStopModel.addElement (new ComboBoxItem("november"));
		monthsStartModel.addElement (new ComboBoxItem("november"));
		monthModel.addElement (new ComboBoxItem("november"));

		monthsStartModel.addElement (new ComboBoxItem("december"));
		monthsStopModel.addElement (new ComboBoxItem("december"));
		monthModel.addElement (new ComboBoxItem("december"));

		decadeModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		decadeModel.addElement (new ComboBoxItem("1dc"));
		decadeModel.addElement (new ComboBoxItem("2dc"));
		decadeModel.addElement (new ComboBoxItem("3dc"));
		decadeModel.addElement (new ComboBoxItem("4dc"));

		daysStartModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		daysStopModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		dayModel.addElement (new ComboBoxItem("exporterwizard.dummy"));

		for (int i = 1; i <= 31; i++)
		{
			daysStartModel.addElement (new ComboBoxItem("" + i));
			daysStopModel.addElement (new ComboBoxItem("" + i));
			dayModel.addElement (new ComboBoxItem("" + i));
		}

		hoursStartModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		hoursStopModel.addElement (new ComboBoxItem("exporterwizard.dummy"));
		hourModel.addElement (new ComboBoxItem("exporterwizard.dummy"));

		for (int i = 0; i <= 24; i++)
		{
			hoursStartModel.addElement (new ComboBoxItem("" + i));
			hoursStopModel.addElement (new ComboBoxItem("" + i));
			hourModel.addElement (new ComboBoxItem("" + i));
		}
	}

	/**
	 * Add a new sensor to the instrument.
	 */
	public Action sensorAcceptAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				sensorModel.addElement (
					new SensorItem(
						stationSensorSelector.getSelectedStationName () + " - " +
						stationSensorSelector.getSelectedSensorName (),
						stationSensorSelector.getSelectedStationId (),
						stationSensorSelector.getSelectedSensorId ()));
				sensorList.repaint ();
			}
		};

	/**
	 * Delete a sensor from the instrument.
	 */
	public Action delSensorAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				int index = sensorList.getSelectedIndex ();

				if (index != -1)
				{
					sensorModel.remove (index);
				}
			}
		};

	/**
	 * Send the action for the eporter.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				LinkedList o = new LinkedList();

				for (int i = 0; i < sensorModel.size (); ++i)
				{
					SensorItem s = (SensorItem) sensorModel.get (i);

					o.add (new Long(s.sensorId));
				}

				Date startDate = null;
				Date stopDate = null;
				int startYear = 0;
				int stopYear = 0;

				try
				{
					int year = 0;
					int monthStart = 1;
					int monthStop = 12;
					int monthDayStart = 1;
					int monthDayStop = 31;
					int month = 0;
					int day = 0;
					int hoursStart = 6;
					int hoursStop = 6;
					int hour = 0;

					if (listYearsStart.isEnabled ())
					{
						year =
							Integer.valueOf (((ComboBoxItem) listYearsStop.getSelectedItem ()).id)
								   .intValue ();
						startYear =
							Integer.valueOf (((ComboBoxItem) listYearsStart.getSelectedItem ()).id)
								   .intValue ();
						stopYear =
							Integer.valueOf (((ComboBoxItem) listYearsStop.getSelectedItem ()).id)
								   .intValue ();
						startDate = fullDateFormat.parse ("01.01." + year + " 6:00");
						stopDate = fullDateFormat.parse ("31.12." + year + " 6:00");
					}

					if (
						listYear.isEnabled () &&
						(! ((ComboBoxItem) listYear.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						year =
							Integer.valueOf (((ComboBoxItem) listYear.getSelectedItem ()).id)
								   .intValue ();
						startDate =
							fullDateFormat.parse (
								monthDayStart + "." + monthStart + "." + year + " 6:00");
						stopDate =
							fullDateFormat.parse (
								monthDayStop + "." + monthStop + "." + year + " 6:00");
					}

					if (
						listSeasons.isEnabled () &&
						(! ((ComboBoxItem) listSeasons.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						if (((ComboBoxItem) listSeasons.getSelectedItem ()).id.equals ("winter"))
						{
							startDate = fullDateFormat.parse ("01.12." + (year - 1) + " 6:00");
							stopDate = fullDateFormat.parse ("01.03." + year + " 6:00");
						}

						if (((ComboBoxItem) listSeasons.getSelectedItem ()).id.equals ("spring"))
						{
							startDate = fullDateFormat.parse ("01.03." + year + " 6:00");
							stopDate = fullDateFormat.parse ("01.06." + year + " 6:00");
						}

						if (((ComboBoxItem) listSeasons.getSelectedItem ()).id.equals ("summer"))
						{
							startDate = fullDateFormat.parse ("01.06." + year + " 6:00");
							stopDate = fullDateFormat.parse ("01.09." + year + " 6:00");
						}

						if (((ComboBoxItem) listSeasons.getSelectedItem ()).id.equals ("harvest"))
						{
							startDate = fullDateFormat.parse ("01.09." + year + " 6:00");
							stopDate = fullDateFormat.parse ("01.12." + year + " 6:00");
						}
					}

					if (
						listMonthsStart.isEnabled () &&
						(! ((ComboBoxItem) listMonthsStart.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						monthStart = listMonthsStart.getSelectedIndex ();
						monthStop = listMonthsStop.getSelectedIndex ();

						startDate =
							fullDateFormat.parse ("01." + monthStart + "." + year + " 6:00");
						stopDate = fullDateFormat.parse ("01." + monthStop + "." + year + " 6:00");
					}

					if (
						listMonth.isEnabled () &&
						(! ((ComboBoxItem) listMonth.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						month = listMonth.getSelectedIndex ();
						startDate = fullDateFormat.parse ("01." + month + "." + year + " 6:00");
						stopDate = fullDateFormat.parse ("31." + month + "." + year + " 6:00");
					}

					if (
						listDecade.isEnabled () &&
						(! ((ComboBoxItem) listDecade.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						if (((ComboBoxItem) listDecade.getSelectedItem ()).id.equals ("1dc"))
						{
							startDate = fullDateFormat.parse ("01." + month + "." + year + " 6:00");
							stopDate = fullDateFormat.parse ("08." + month + "." + year + " 6:00");
						}

						if (((ComboBoxItem) listDecade.getSelectedItem ()).id.equals ("2dc"))
						{
							startDate = fullDateFormat.parse ("08." + month + "." + year + " 6:00");
							stopDate = fullDateFormat.parse ("16." + month + "." + year + " 6:00");
						}

						if (((ComboBoxItem) listDecade.getSelectedItem ()).id.equals ("3dc"))
						{
							startDate = fullDateFormat.parse ("16." + month + "." + year + " 6:00");
							stopDate = fullDateFormat.parse ("24." + month + "." + year + " 6:00");
						}

						if (((ComboBoxItem) listDecade.getSelectedItem ()).id.equals ("4dc"))
						{
							startDate = fullDateFormat.parse ("24." + month + "." + year + " 6:00");
							stopDate = fullDateFormat.parse ("31." + month + "." + year + " 6:00");
						}
					}

					if (
						listDaysStart.isEnabled () &&
						(! ((ComboBoxItem) listDaysStart.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						monthDayStart =
							Integer.valueOf (((ComboBoxItem) listDaysStart.getSelectedItem ()).id)
								   .intValue ();
						monthDayStop =
							Integer.valueOf (((ComboBoxItem) listDaysStop.getSelectedItem ()).id)
								   .intValue ();

						startDate =
							fullDateFormat.parse (
								monthDayStart + "." + month + "." + year + " 6:00");
						stopDate =
							fullDateFormat.parse (
								monthDayStop + "." + month + "." + year + " 6:00");
					}

					if (
						listDay.isEnabled () &&
						(! ((ComboBoxItem) listDay.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						day = Integer.valueOf (((ComboBoxItem) listDay.getSelectedItem ()).id)
									 .intValue ();
						monthDayStart = day;
						monthDayStop = day;
						startDate = fullDateFormat.parse (day + "." + month + "." + year + " 6:00");
						stopDate =
							fullDateFormat.parse ((day + 1) + "." + month + "." + year + " 6:00");
					}

					if (
						listHoursStart.isEnabled () &&
						(! ((ComboBoxItem) listHoursStart.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						hoursStart =
							Integer.valueOf (((ComboBoxItem) listHoursStart.getSelectedItem ()).id)
								   .intValue ();
						hoursStop =
							Integer.valueOf (((ComboBoxItem) listHoursStop.getSelectedItem ()).id)
								   .intValue ();

						startDate =
							fullDateFormat.parse (
								monthDayStart + "." + month + "." + year + " " + hoursStart +
								":00");
						stopDate =
							fullDateFormat.parse (
								monthDayStop + "." + month + "." + year + " " + hoursStart + ":00");
					}

					if (
						listHour.isEnabled () &&
						(! ((ComboBoxItem) listHour.getSelectedItem ()).id.equals (
							"exporterwizard.dummy")))
					{
						hour =
							Integer.valueOf (((ComboBoxItem) listHour.getSelectedItem ()).id)
								   .intValue ();
						startDate =
							fullDateFormat.parse (
								monthDayStart + "." + month + "." + year + " " + hour + ":00");
						stopDate =
							fullDateFormat.parse (
								monthDayStop + "." + month + "." + year + " " + hour + ":60");
					}
				}
				catch (Exception x)
				{
					System.out.println (x);
				}

				if (o.size () == 0)
				{
					JOptionPane.showMessageDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"exportwizard.sensorerror"), "Metix", JOptionPane.OK_OPTION);

					return;
				}

				if (startDate == null)
				{
					JOptionPane.showMessageDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"exportwizard.timeerror"), "Metix", JOptionPane.OK_OPTION);

					return;
				}

				CommandTools.performAsync (new ShowDialog("MinMidMaxResult"));

				MinMidMaxExportCommand mc = new MinMidMaxExportCommand();

				mc.setSensorList (o);

				mc.setStartDate (startDate.getTime ());
				mc.setStopDate (stopDate.getTime ());
				mc.setStartYear ((long) startYear);
				mc.setStopYear ((long) stopYear);

				CommandTools.performAsync (mc);
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