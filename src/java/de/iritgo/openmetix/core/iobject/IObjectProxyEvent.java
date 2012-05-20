/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.event.Event;


/**
 * IObjectProxyEvent.
 *
 * @version $Id: IObjectProxyEvent.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectProxyEvent
	implements Event
{
	/** Unique id. */
	private long uniqueId;

	/** True if we are waiting for a new object. */
	private boolean waitingForNewObject;

	/** The object id. */
	private String iObjectId;

	/** The object. */
	private IObject iObject;

	/**
	 * Create a new IObjectProxyEvent.
	 */
	public IObjectProxyEvent (IObject iObject, long uniqueId, boolean waitingForNewObject)
	{
		this.uniqueId = uniqueId;
		this.waitingForNewObject = waitingForNewObject;
		this.iObject = iObject;
	}

	/**
	 * Create a new IObjectProxyEvent.
	 */
	public IObjectProxyEvent (
		IObject iObject, long uniqueId, String iObjectId, boolean waitingForNewObject)
	{
		this.iObject = iObject;
		this.uniqueId = uniqueId;
		this.waitingForNewObject = waitingForNewObject;
		this.iObjectId = iObjectId;
	}

	/**
	 * Get the unique id.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId ()
	{
		return uniqueId;
	}

	/**
	 * Get the object id.
	 *
	 * @return The object id.
	 */
	public String getObjectId ()
	{
		return iObjectId;
	}

	/**
	 * Get the object.
	 *
	 * @return The object-
	 */
	public IObject getObject ()
	{
		return iObject;
	}

	/**
	 * Check if we are waiting for a new object.
	 *
	 * @return True if we are waiting for a new object.
	 */
	public boolean isWaitingForNewObject ()
	{
		return waitingForNewObject;
	}
}