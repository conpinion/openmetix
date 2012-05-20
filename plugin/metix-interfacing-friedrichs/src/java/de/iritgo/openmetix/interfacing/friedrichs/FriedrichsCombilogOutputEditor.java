/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.friedrichs;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.core.tools.StringTools;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutputEditor;
import org.swixml.SwingEngine;
import javax.swing.JPanel;


/**
 * This gui pane is used to edit Friedrich system outputs.
 *
 * @version $Id: FriedrichsCombilogOutputEditor.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class FriedrichsCombilogOutputEditor
	extends GagingOutputEditor
{
	/** The unit number. */
	public ITextField unit;

	/** The channel number. */
	public ITextField channel;

	/**
	 * Create a new FriedrichsCombilogOutputEditor.
	 */
	public FriedrichsCombilogOutputEditor ()
	{
		super("FriedrichsCombilogOutputEditor");
	}

	/**
	 * Create a panel for custom output parameter editing.
	 *
	 * @return The custom parameter panel.
	 */
	protected JPanel createCustomParameterPanel ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (InterfacingFriedrichsPlugin.class.getClassLoader ());

			return (JPanel) swingEngine.render (
				getClass ().getResource ("/swixml/FriedrichsCombilogOutputEditor.xml"));
		}
		catch (Exception x)
		{
			Log.logError ("client", "FriedrichsCombilogOutputEditor.initGUI", x.toString ());
		}

		return null;
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new FriedrichsCombilogOutputEditor();
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	protected void loadFromProperties ()
	{
		if (customConfig.get ("unit") != null)
		{
			unit.setText (customConfig.get ("unit").toString ());
		}

		if (customConfig.get ("channel") != null)
		{
			channel.setText (customConfig.get ("channel").toString ());
		}
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	protected void storeToProperties ()
	{
		if (! StringTools.isTrimEmpty (unit.getText ()))
		{
			int unitValue = NumberTools.toInt (unit.getText (), 0);

			customConfig.put ("unit", String.valueOf (Math.min (unitValue, 255)));
		}

		if (! StringTools.isTrimEmpty (channel.getText ()))
		{
			int channelValue = NumberTools.toInt (channel.getText (), 0);

			customConfig.put ("channel", String.valueOf (Math.min (channelValue, 255)));
		}
	}
}