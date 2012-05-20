/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystemdriver;


import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import javax.swing.JPanel;
import java.util.Properties;


/**
 * This is the base class for all system driver editors.
 *
 * @version $Id: GagingSystemDriverEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public abstract class GagingSystemDriverEditor
{
	/** The editor panel. */
	protected JPanel panel;

	/** Configuration properties. */
	protected Properties configProperties;

	/**
	 * Create a new GagingSystemDriverEditor.
	 */
	public GagingSystemDriverEditor ()
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
	 * @param system The gaging system to load.
	 */
	public void loadSystem (GagingSystem system)
	{
		configProperties = system.getDriverProperties ();
		loadFromProperties ();
	}

	/**
	 * Store the gui values into the gaging system.
	 *
	 * @param system The gaging system to store to.
	 */
	public void storeSystem (GagingSystem system)
	{
		configProperties.clear ();
		storeToProperties ();
		system.setDriverProperties (configProperties);
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