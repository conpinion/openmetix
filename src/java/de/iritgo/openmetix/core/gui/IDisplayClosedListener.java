/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.event.EventListener;


/**
 * Implement this interface if you want to listen to display
 * closing events.
 *
 * @version $Id: IDisplayClosedListener.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface IDisplayClosedListener
	extends EventListener
{
	/**
	 * Called when a display was closed.
	 *
	 * @param event The closing event.
	 */
	public void displayClosedEvent (IDisplayClosedEvent event);
}