/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import javax.swing.JPanel;


/**
 * This is the base class for all sensor behaviour editors.
 *
 * @version $Id: GagingSensorBehaviourEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public abstract class GagingSensorBehaviourEditor
{
	/** The editor panel. */
	protected JPanel panel;

	/**
	 * Create a new GagingSensorBehaviourEditor.
	 */
	public GagingSensorBehaviourEditor ()
	{
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
	 * Load the gaging sensor into the gui.
	 *
	 * @param sensor The sensor to load.
	 */
	public abstract void loadSensor (GagingSensor sensor);

	/**
	 * Store the gui values into the gaging sensor.
	 *
	 * @param sensor The sensor to store to.
	 */
	public abstract void storeSensor (GagingSensor sensor);
}