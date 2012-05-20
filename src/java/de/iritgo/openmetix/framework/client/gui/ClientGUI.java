/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.gui;


import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.framework.base.InitIritgoException;


/**
 * This interface must be implemented by a client class, that actually creates
 * and manages the user interface of the client.
 *
 * @version $Id: ClientGUI.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public interface ClientGUI
{
	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager. */
	public IDesktopManager getDesktopManager ();

	/**
	 * Initialize the client gui.
	 */
	public void init ()
		throws InitIritgoException;

	/**
	 * Start the client application.
	 */
	public void startApplication ();

	/**
	 * Stop the client application.
	 */
	public void stopApplication ();

	/**
	 * Start the client gui.
	 */
	public void startGUI ();

	/**
	 * Stop the client gui.
	 */
	public void stopGUI ();

	/**
	 * Called when the network connection was lost.
	 */
	public void lostNetworkConnection ();
}