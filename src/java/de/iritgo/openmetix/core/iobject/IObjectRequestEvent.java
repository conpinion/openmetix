/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.event.Event;


/**
 * IObjectRequestEvent.
 *
 * @version $Id: IObjectRequestEvent.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectRequestEvent
	implements Event
{
	/** The unique id of the requested object. */
	private long uniqueId;

	/**
	 * Create a new  iobject request event.
	 *
	 * @praram uniqueId Unique id of the requested object.
	 */
	public IObjectRequestEvent (long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the unique id of the requested object.
	 *
	 * @return The unique id of the requested object.
	 */
	public long getUniqueId ()
	{
		return uniqueId;
	}
}