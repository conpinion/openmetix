/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.interfacing.InterfacingPlugin;
import org.swixml.SwingEngine;
import javax.swing.JPanel;


/**
 * Editor for dummy drivers.
 *
 * @version $Id: NullInterfaceDriverEditor.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class NullInterfaceDriverEditor
	extends InterfaceDriverEditor
{
	/**
	 * Create a new NullInterfaceDriverEditor.
	 */
	public NullInterfaceDriverEditor ()
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

			swingEngine.setClassLoader (InterfacingPlugin.class.getClassLoader ());

			panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/NullInterfaceDriverEditor.xml"));
		}
		catch (Exception x)
		{
			Log.logError ("client", "NullInterfaceDriverEditor.createEditorPanel", x.toString ());
		}

		return panel;
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	public void loadFromProperties ()
	{
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	public void storeToProperties ()
	{
	}
}