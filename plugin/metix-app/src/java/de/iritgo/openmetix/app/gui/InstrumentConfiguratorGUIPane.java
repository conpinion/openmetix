/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.gui;



/**
 * Base class for all instrument configurator gui panes.
 *
 * @version $Id: InstrumentConfiguratorGUIPane.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class InstrumentConfiguratorGUIPane
	extends InstrumentGUIPane
{
	/**
	 * Create a new InstrumentConfiguratorGUIPane.
	 */
	public InstrumentConfiguratorGUIPane ()
	{
		super("InstrumentConfiguratorGUIPane");
	}

	/**
	 * Create a new InstrumentConfiguratorGUIPane.
	 *
	 * @param id The guiPaneId
	 */
	public InstrumentConfiguratorGUIPane (String id)
	{
		super(id);
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		getDisplay ().setEnabled (false);
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		getDisplay ().setEnabled (true);
	}
}