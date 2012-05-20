/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.event.Event;


/**
 * IDisplayEvents are fired when displays are opened or closed.
 *
 * @version $Id: IDisplayEvent.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IDisplayEvent
	implements Event
{
	private IDisplay display;

	/**
	 * Create a new display event.
	 *
	 * @param IDisplay The display.
	 */
	public IDisplayEvent (IDisplay display)
	{
		this.display = display;
	}

	/**
	 * Get the display which was the cause of this event.
	 *
	 * @return The event display.
	 */
	public IDisplay getIDisplay ()
	{
		return display;
	}
}