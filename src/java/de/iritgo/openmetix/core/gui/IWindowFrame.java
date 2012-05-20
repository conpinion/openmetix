/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import javax.swing.Icon;
import java.awt.Rectangle;


/**
 * Interface to be implmented by window frames.
 *
 * @version $Id: IWindowFrame.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface IWindowFrame
{
	/**
	 * Set the window bounds.
	 *
	 * @param bounds The new bounds.
	 */
	public void setBounds (Rectangle bounds);

	/**
	 * Get the window bounds.
	 *
	 * @param bounds The new bounds.
	 */
	public Rectangle getBounds ();

	/**
	 * Set the window title.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title);

	/**
	 * Get the window title.
	 *
	 * @return The title.
	 */
	public String getTitle ();

	/**
	 * Set the dialog icon.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon (Icon icon);

	/**
	 * Get the dialog's icon.
	 *
	 * @return The icon.
	 */
	public Icon getIcon ();

	/**
	 * Close the window frame.
	 */
	public void close ();

	/**
	 * Close the window frame.
	 */
	public void systemClose ();

	/**
	 * Show the window frame.
	 */
	public void showWindow ();

	/**
	 * Set the maxmized state of the window frame.
	 */
	public void setMaximized (boolean maximized);

	/**
	 * Enable/disable the window frame.
	 *
	 * @param enabled If true the window frame is enabled.
	 */
	public void setEnabled (boolean enabled);
}