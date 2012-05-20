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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Configuration dialog for the history repeater driver.
 *
 * @version $Id: HistoryRepeaterDriverEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class HistoryRepeaterDriverEditor
	extends GagingSystemDriverEditor
{
	/** The sampling interval. */
	public JSlider interval;

	/** The sampling interval display value. */
	public JLabel intervalValue;

	/**
	 * Create a new configuration dialog.
	 */
	public HistoryRepeaterDriverEditor ()
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
					getClass ().getResource ("/swixml/HistoryRepeaterDriverDialog.xml"));

			interval.addChangeListener (
				new ChangeListener()
				{
					public void stateChanged (ChangeEvent e)
					{
						intervalValue.setText (String.valueOf (interval.getValue ()));
					}
				});

			return panel;
		}
		catch (Exception x)
		{
			Log.logError ("client", "HistoryRepeaterDriverDialog", x.toString ());

			return new JPanel();
		}
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	public void loadFromProperties ()
	{
		int val = NumberTools.toInt ((String) configProperties.get ("interval"), 10);

		interval.setValue (val);
		intervalValue.setText (String.valueOf (val));
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	public void storeToProperties ()
	{
		configProperties.put ("interval", String.valueOf (interval.getValue ()));
	}
}