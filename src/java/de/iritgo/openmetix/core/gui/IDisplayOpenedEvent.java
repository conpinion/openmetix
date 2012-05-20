/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;



/**
 * This event is fired when a display was opened on the screen.
 *
 * @version $Id: IDisplayOpenedEvent.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IDisplayOpenedEvent
	extends IDisplayEvent
{
	/**
	 * Create a new display event.
	 *
	 * @param IDisplay The display.
	 */
	public IDisplayOpenedEvent (IDisplay display)
	{
		super(display);
	}
}