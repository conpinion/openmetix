/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.userprofile;


import de.iritgo.openmetix.framework.base.DataObject;
import java.awt.Rectangle;


/**
 * Configuration for a displayed instrument.
 *
 * @version $Id: ActiveDisplay.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class ActiveDisplay
	extends DataObject
{
	/**
	 * Create a new ActiveDisplay.
	 */
	public ActiveDisplay ()
	{
		super("ActiveDisplay");

		putAttribute ("displayGUIPaneId", "unknown");
		putAttribute ("displayTypeId", "unknown");
		putAttribute ("displayUniqueId", new Long(0));
		putAttribute ("desktopId", "unknown");
		putAttribute ("displayX", new Integer(0));
		putAttribute ("displayY", new Integer(0));
		putAttribute ("displayWidth", new Integer(0));
		putAttribute ("displayHeight", new Integer(0));
		putAttribute ("displayMaximized", new Integer(0));
		putAttribute ("deleted", new Integer(0));
	}

	/**
	 * Set the deleted flag.
	 */
	public void setDeletedFlag ()
	{
		putAttribute ("deleted", new Integer(1));
	}

	/**
	 * Check if this is a deleted display.
	 *
	 * @return True for a deleted display.
	 */
	public boolean isDeleted ()
	{
		return getIntAttribute ("deleted") == 1;
	}

	/**
	 * Set the gui pane id of this display.
	 *
	 * @param guiPaneId The id of the gui pane.
	 */
	public void setDisplayGUIPaneId (String guiPaneId)
	{
		putAttribute ("displayGUIPaneId", guiPaneId);
	}

	/**
	 * Get the gui pane id of this display.
	 *
	 * @return The id of the gui pane.
	 */
	public String getDisplayGUIPaneId ()
	{
		return getStringAttribute ("displayGUIPaneId");
	}

	/**
	 * Set the type id of this display.
	 *
	 * @param typeId The type id of the display.
	 */
	public void setDisplayTypeId (String typeId)
	{
		putAttribute ("displayTypeId", typeId);
	}

	/**
	 * Get the type id of this display.
	 *
	 * @return The type id of the display.
	 */
	public String getDisplayTypeId ()
	{
		return getStringAttribute ("displayTypeId");
	}

	/**
	 * Set the unique id of this display.
	 *
	 * @param uniqueId The unique id of the display.
	 */
	public void setDisplayUniqueId (long uniqueId)
	{
		putAttribute ("displayUniqueId", uniqueId);
	}

	/**
	 * Get the unique id of this display.
	 *
	 * @return The unique id of the display.
	 */
	public long getDisplayUniqueId ()
	{
		return getLongAttribute ("displayUniqueId");
	}

	/**
	 * Set the id of the desktop on which the display is shown.
	 *
	 * @param desktopId The id of the desktop.
	 */
	public void setDesktopId (String desktopId)
	{
		putAttribute ("desktopId", desktopId);
	}

	/**
	 * Get the id of the desktop on which the display is shown.
	 *
	 * @return The id of the desktop.
	 */
	public String getDesktopId ()
	{
		return getStringAttribute ("desktopId");
	}

	/**
	 * Set the display bounds.
	 *
	 * @param rect The new display bounds.
	 */
	public void setBounds (Rectangle rect)
	{
		putAttribute ("displayX", (int) rect.getX ());
		putAttribute ("displayY", (int) rect.getY ());
		putAttribute ("displayWidth", (int) rect.getWidth ());
		putAttribute ("displayHeight", (int) rect.getHeight ());
	}

	/**
	 * Get the display bounds.
	 *
	 * @return The new display bounds.
	 */
	public Rectangle getBounds ()
	{
		return new Rectangle(
			getIntAttribute ("displayX"), getIntAttribute ("displayY"),
			getIntAttribute ("displayWidth"), getIntAttribute ("displayHeight"));
	}

	/**
	 * Set the maximized flag.
	 *
	 * @param maxmized The new maximized flag.
	 */
	public void setMaximized (boolean maxmized)
	{
		putAttribute ("displayMaximized", maxmized ? 1 : 0);
	}

	/**
	 * Get the maximized flag.
	 *
	 * @return The maximized flag.
	 */
	public boolean isMaximized ()
	{
		return getIntAttribute ("displayMaximized") == 1;
	}
}