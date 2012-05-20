/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.iobject.IObject;
import java.util.Properties;


/**
 * This is the interface that all high level display elements (like dialogs or
 * desktop windows ust implement).
 *
 * @version $Id: IDisplay.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface IDisplay
{
	/**
	 * Get the id of this display.
	 *
	 * @return The display's id.
	 */
	public String getTypeId ();

	/**
	 * Get the gui pane of this display.
	 *
	 * @return The gui pane.
	 */
	public GUIPane getGUIPane ();

	/**
	 * Get the id of the desktop on which this display is displayed.
	 *
	 * @return The desktop id (or null if this display is a dialog).
	 */
	public String getDesktopId ();

	/**
	 * Set the id of the desktop on which this display is displayed.
	 *
	 * @param desktopId The desktop id.
	 */
	public void setDesktopId (String desktopId);

	/**
	 * Set the desktop manager.
	 *
	 * @param desktopManager The desktop manager.
	 */
	public void setDesktopManager (IDesktopManager desktopManager);

	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager ();

	/**
	 * Close this display element.
	 */
	public void close ();

	/**
	 * This method is called by classes implementing window or dialog frames.
	 */
	public void systemClose ();

	/**
	 * Get the data object shown in this display.
	 *
	 * @return The data object.
	 */
	public IObject getDataObject ();

	/**
	 * Set the display title. This title will be displayed on the
	 * display frame's title bar.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title);

	/**
	 * Get the display title.
	 *
	 * @return The display title.
	 */
	public String getTitle ();

	/**
	 * Set a display property.
	 *
	 * @param key The key under which to store the property.
	 * @param value The property value.
	 */
	public void putProperty (String key, Object value);

	/**
	 * Get a display property.
	 *
	 * @param key The key of the property to retrieve.
	 * @return The property value.
	 */
	public Object getProperty (String key);

	/**
	 * Set the display properties.
	 *
	 * @param properties The new properties.
	 */
	public void setProperties (Properties properties);

	/**
	 * Get the display properties.
	 *
	 * @return The display properties.
	 */
	public Properties getProperties ();

	/**
	 * Remove a display property.
	 *
	 * @param key The key of the property to remove.
	 */
	public void removeProperty (String key);

	/**
	 * Displays this display on the screen
	 */
	public void show ();

	/**
	 * Enable/disable the display.
	 *
	 * @param enabled If true the display is enabled.
	 */
	public void setEnabled (boolean enabled);
}