/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.event.Event;
import de.iritgo.openmetix.core.network.ClientTransceiver;


/**
 * IObjectListEvent.
 *
 * @version $Id: IObjectListEvent.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectListEvent
	implements Event
{
	/** Object was added. */
	public static int ADD = 0;

	/** Object was removed. */
	public static int REMOVE = 1;

	/** Event type. */
	private int type;

	/** The object. */
	private IObject iObject;

	/** Owning object. */
	private IObject ownerIObject;

	/** Attribute name of the object list. */
	private String listAttribute;

	/** The client transceiver. */
	private ClientTransceiver clientTransceiver;

	/**
	 * Create a new IObjectListEvent.
	 *
	 * @param iObject The new prototypeable to add to the system.
	 * @param ownerIObject The ownerIObject of the proxylinkedlist.
	 * @param listAttribute The name of the list attribute.
	 * @param type The type of this event.
	 */
	public IObjectListEvent (IObject iObject, IObject ownerIObject, String listAttribute, int type)
	{
		this(iObject, ownerIObject, listAttribute, null, type);
	}

	/**
	 * Create a new IObjectListEvent.
	 */
	public IObjectListEvent (
		IObject iObject, IObject ownerIObject, String id, ClientTransceiver clientTransceiver,
		int type)
	{
		this.iObject = iObject;
		this.ownerIObject = ownerIObject;
		this.listAttribute = id;
		this.type = type;
		this.clientTransceiver = clientTransceiver;
	}

	/**
	 * Get the object.
	 *
	 * @return The event object.
	 */
	public IObject getObject ()
	{
		return iObject;
	}

	/**
	 * Get the owner object.
	 *
	 * @return The owner object.
	 */
	public IObject getOwnerObject ()
	{
		return ownerIObject;
	}

	/**
	 * Get the event type.
	 *
	 * @return The event type.
	 */
	public int getType ()
	{
		return type;
	}

	/**
	 * Get the list attribute name.
	 *
	 * @return The list attribute name.
	 */
	public String getListAttribute ()
	{
		return listAttribute;
	}

	/**
	 * Get the client transceiver.
	 *
	 * @return The client transceiver.
	 */
	public ClientTransceiver getClientTransceiver ()
	{
		return clientTransceiver;
	}
}