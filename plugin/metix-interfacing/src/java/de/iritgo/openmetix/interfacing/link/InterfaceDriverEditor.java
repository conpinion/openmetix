/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.logger.Log;
import javax.swing.JPanel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * This is the base class for all interface driver editors.
 *
 * @version $Id: InterfaceDriverEditor.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public abstract class InterfaceDriverEditor
{
	/** The editor panel. */
	protected JPanel panel;

	/** Configuration properties. */
	protected Properties configProperties;

	/**
	 * Create a new InterfaceDriverEditor.
	 */
	public InterfaceDriverEditor ()
	{
		configProperties = new Properties();
		panel = createEditorPanel ();
	}

	/**
	 * Get the editor panel.
	 */
	public JPanel getPanel ()
	{
		return panel;
	}

	/**
	 * Create the editor panel.
	 *
	 * @return The editor panel.
	 */
	protected abstract JPanel createEditorPanel ();

	/**
	 * Load the interface into the gui.
	 *
	 * @param iface The interface to load.
	 */
	public void loadInterface (Interface iface)
	{
		try
		{
			configProperties.load (new ByteArrayInputStream(iface.getDriverParams ().getBytes ()));
			loadFromProperties ();
		}
		catch (IOException x)
		{
			Log.logError ("client", "InterfaceDriverEditor", x.toString ());
		}
	}

	/**
	 * Store the gui values into the interface.
	 *
	 * @param iface The interface to store to.
	 */
	public void storeInterface (Interface iface)
	{
		try
		{
			configProperties.clear ();
			storeToProperties ();

			ByteArrayOutputStream propOut = new ByteArrayOutputStream();

			configProperties.store (propOut, null);
			propOut.close ();
			iface.setDriverParams (propOut.toString ());
		}
		catch (IOException x)
		{
			Log.logError ("client", "DriverConfigurationDialog", x.toString ());
		}
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	public abstract void loadFromProperties ();

	/**
	 * Store the gui values to the configuration properties.
	 */
	public abstract void storeToProperties ();
}