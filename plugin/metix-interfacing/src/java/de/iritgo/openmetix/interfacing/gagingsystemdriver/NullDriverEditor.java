/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystemdriver;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.interfacing.InterfacingPlugin;
import org.swixml.SwingEngine;
import javax.swing.JPanel;


/**
 * Configuration dialog for 'do nothing' driver.
 *
 * @version $Id: NullDriverEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class NullDriverEditor
	extends GagingSystemDriverEditor
{
	/**
	 * Create a new configuration dialog.
	 */
	public NullDriverEditor ()
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

			swingEngine.setClassLoader (InterfacingPlugin.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/NullDriverDialog.xml"));

			return panel;
		}
		catch (Exception x)
		{
			Log.logError ("client", "NullDriverDialog", x.toString ());

			return new JPanel();
		}
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