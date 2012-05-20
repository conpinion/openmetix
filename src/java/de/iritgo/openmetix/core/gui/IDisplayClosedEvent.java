/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;



/**
 * An IDisplayClosedEvent is fired when a display was closed.
 *
 * @version $Id: IDisplayClosedEvent.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IDisplayClosedEvent
	extends IDisplayEvent
{
	/**
	 * Create a new display event.
	 *
	 * @param IDisplay The display.
	 */
	public IDisplayClosedEvent (IDisplay display)
	{
		super(display);
	}
}