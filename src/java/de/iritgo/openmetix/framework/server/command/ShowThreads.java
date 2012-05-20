/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.server.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.thread.ThreadService;
import de.iritgo.openmetix.core.thread.ThreadSlot;
import java.util.Iterator;


/**
 * ShowThreads.
 *
 * @version $Id: ShowThreads.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ShowThreads
	extends Command
{
	public ShowThreads ()
	{
	}

	public void perform ()
	{
		ThreadService threadService = Engine.instance ().getThreadService ();

		for (Iterator i = threadService.getThreadController ().threadSlotIterator (); i.hasNext ();)
		{
			ThreadSlot slot = (ThreadSlot) i.next ();

			System.out.println (slot.getName ());
		}
	}
}