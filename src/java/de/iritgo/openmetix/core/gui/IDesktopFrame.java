/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import java.awt.Dimension;


/**
 * IDesktopFrame
 *
 * @version $Id: IDesktopFrame.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface IDesktopFrame
{
	/**
	 * Initialize the desktop frame.
	 */
	public void init ();

	/**
	 * Show the desktop frame.
	 */
	public void show ();

	/**
	 * Close the desktop frame.
	 */
	public void close ();

	/**
	 * Show or hide the desktop frame.
	 *
	 * @param visible If true, the desktop is visible.
	 */
	public void setVisible (boolean visible);

	/**
	 * Check wether the desktop frame is visible or not.
	 *
	 * @return true If the desktop frame is visible.
	 */
	public boolean isVisible ();

	/**
	 * Set the fullscreen state.
	 *
	 * @param fullScreen If true, the desktop frame is switched to fullscreen mode.
	 */
	public void setFullScreen (boolean fullScreen);

	/**
	 * Check wether the fullscreen mode is enabled or not.
	 *
	 * @return true If the desktop frame is in fullscreen mode.
	 */
	public boolean isFullScreen ();

	/**
	 * Check wether the dektop frame supports the fullscreen mode or not.
	 *
	 * @return true If the dektop frame supports the fullscreen mode.
	 */
	public boolean canFullScreen ();

	/**
	 * Return the screen size of the desktop frame.
	 *
	 * @return The screen size.
	 */
	public Dimension getScreenSize ();

	/**
	 * Set the desktop frame's title.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title);

	/**
	 * Enable disable the desktop frame.
	 *
	 * @param enabled If true the desktop is enabled.
	 */
	public void setEnabled (boolean enabled);
}