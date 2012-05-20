/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.event.EventListener;


/**
 * Implement this interface if you want to listen to display
 * opening events.
 *
 * @version $Id: IDisplayOpenedListener.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface IDisplayOpenedListener
	extends EventListener
{
	/**
	 * Called when a display was opened.
	 *
	 * @param event The opening event.
	 */
	public void displayOpenedEvent (IDisplayOpenedEvent event);
}