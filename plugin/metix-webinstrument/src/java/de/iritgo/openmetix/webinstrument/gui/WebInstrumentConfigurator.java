/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.webinstrument.gui;


import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.app.instrument.InstrumentConfigurator;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ICheckBox;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.webinstrument.WebInstrument;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * This gui pane is used to configure web instruments.
 *
 * @version $Id: WebInstrumentConfigurator.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class WebInstrumentConfigurator
	extends InstrumentConfiguratorGUIPane
	implements InstrumentConfigurator
{
	/** Instrument title. */
	public JTextField title;

	/** Predefined pages. */
	public JComboBox pages;

	/** Web page url. */
	public JTextField url;

	/** Check this to automatically reload the web page. */
	public ICheckBox reload;

	/** The reload interval. */
	public JSlider interval;

	/** The reload interval value. */
	public JLabel intervalValue;

	/**
	 * Elements of the pages combobox.
	 */
	private class PageItem
	{
		/** The page name. */
		public String name;

		/** The page url. */
		public String url;

		/**
		 * Create a new PageItem.
		 *
		 * @param name The page name.
		 * @param url The page url.
		 */
		public PageItem (String name, String url)
		{
			this.name = name;
			this.url = url;
		}

		/**
		 * Create a string representation of the element.
		 *
		 * @return The string representation.
		 */
		public String toString ()
		{
			return name;
		}
	}

	/**
	 * Create a new WebInstrumentConfigurator
	 */
	public WebInstrumentConfigurator ()
	{
		super("WebInstrumentConfigurator");
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new WebInstrument();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new WebInstrumentConfigurator();
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
					getClass ().getResource ("/swixml/WebInstrumentConfigurator.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			pages.addItem (
				new PageItem(
					"10-Day Temperature Outlook for Europe", "http://grads.iges.org/pix/temp4.html"));

			pages.addItem (
				new PageItem(
					"Wetter.com - Niederschlagsradar",
					"http://www.wetter.com/v2/img/wx/radar_DE/l/200410141030.gif"));

			pages.setSelectedIndex (-1);

			pages.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							url.setText (((PageItem) pages.getSelectedItem ()).url);
						}
					}
				});

			reload.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						interval.setEnabled (reload.isSelected ());
						intervalValue.setEnabled (reload.isSelected ());
					}
				});

			intervalValue.setText (String.valueOf (interval.getValue ()));

			interval.addChangeListener (
				new ChangeListener()
				{
					public void stateChanged (ChangeEvent e)
					{
						intervalValue.setText (String.valueOf (interval.getValue ()));
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "WebInstrumentConfigurator.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		WebInstrument webInstrument = (WebInstrument) iobject;

		if (! webInstrument.isValid ())
		{
			return;
		}

		title.setText (webInstrument.getTitle ());
		url.setText (webInstrument.getUrl ());
		reload.setSelected (webInstrument.getReloadInterval () > 0);
		interval.setValue (Math.abs (webInstrument.getReloadInterval ()));

		super.loadFromObject ();
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		WebInstrument webInstrument = (WebInstrument) iobject;

		if (! webInstrument.isValid ())
		{
			return;
		}

		webInstrument.setTitle (title.getText ());
		webInstrument.setUrl (url.getText ());
		webInstrument.setReloadInterval (
			reload.isSelected () ? interval.getValue () : -interval.getValue ());
		webInstrument.update ();
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
				WebInstrument webInstrument = (WebInstrument) iobject;
				boolean wasFresh = webInstrument.isFresh ();

				webInstrument.setFresh (false);
				storeToObject ();

				if (wasFresh)
				{
					CommandTools.performAsync (new ShowWindow("WebInstrumentDisplay", iobject));
				}

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
				cancelInstrument ();
				display.close ();
			}
		};
}