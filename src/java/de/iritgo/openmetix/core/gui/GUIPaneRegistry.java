/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.base.BaseObject;
import java.util.HashMap;
import java.util.Map;


/**
 * This registry contains all known gui panes.
 *
 * @version $Id: GUIPaneRegistry.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class GUIPaneRegistry
	extends BaseObject
{
	/** The singleton instance. */
	private static GUIPaneRegistry guiPaneRegistry;

	/** All gui panes. */
	private Map guiPanes;

	/**
	 * Create a new gui pane registry.
	 */
	public GUIPaneRegistry ()
	{
		guiPanes = new HashMap();
	}

	/**
	 * Add a gui pane.
	 *
	 * @param guiPane The gui pane to add.
	 */
	public void add (GUIPane guiPane)
	{
		guiPanes.put (guiPane.getTypeId (), guiPane);
	}

	/**
	 * Remove a gui pane.
	 *
	 * @param guiPaneId The id of the gui pane to remove.
	 */
	public void remove (String guiPaneId)
	{
		guiPanes.remove (guiPaneId);
	}

	/**
	 * Remove a gui pane.
	 *
	 * @param guiPane The gui pane to remove.
	 */
	public void remove (GUIPane guiPane)
	{
		guiPanes.remove (guiPane);
	}

	/**
	 * Retrieve a cloned gui pane.
	 * This method searches for the specified gui pane and creates and
	 * returns a clone.
	 *
	 * @param guiPaneId The id of the gui pane to retrieve.
	 */
	public GUIPane create (String guiPaneId)
	{
		return ((GUIPane) guiPanes.get (guiPaneId)).cloneGUIPane ();
	}

	/**
	 * Retrieve the singleton instance.
	 *
	 * @return The gui pane registry.
	 */
	static public GUIPaneRegistry instance ()
	{
		if (guiPaneRegistry == null)
		{
			guiPaneRegistry = new GUIPaneRegistry();
		}

		return guiPaneRegistry;
	}
}