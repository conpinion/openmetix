/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.reinhardt;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.core.tools.StringTools;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutputEditor;
import org.swixml.SwingEngine;
import javax.swing.JPanel;


/**
 * This gui pane is used to edit Lambrecht system outputs.
 *
 * @version $Id: ReinhardtOutputEditor.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ReinhardtOutputEditor
	extends GagingOutputEditor
{
	/** The output code. */
	public ITextField code;

	/**
	 * Create a new LambrechtOutputEditor.
	 */
	public ReinhardtOutputEditor ()
	{
		super("ReinhardtOutputEditor");
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

			swingEngine.setClassLoader (InterfacingReinhardtPlugin.class.getClassLoader ());

			return (JPanel) swingEngine.render (
				getClass ().getResource ("/swixml/ReinhardtOutputEditor.xml"));
		}
		catch (Exception x)
		{
			Log.logError ("client", "ReinhardtOutputEditor.initGUI", x.toString ());
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
		return new ReinhardtOutputEditor();
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	protected void loadFromProperties ()
	{
		if (customConfig.get ("code") != null)
		{
			code.setText (customConfig.get ("code").toString ());
		}
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	protected void storeToProperties ()
	{
		if (! StringTools.isTrimEmpty (code.getText ()))
		{
			int codeValue = NumberTools.toInt (code.getText (), 0);

			customConfig.put ("code", String.valueOf (Math.min (codeValue, 255)));
		}
	}
}