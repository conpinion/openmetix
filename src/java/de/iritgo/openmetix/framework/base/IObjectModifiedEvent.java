/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import de.iritgo.openmetix.core.event.Event;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.network.ClientTransceiver;


/**
 * IObjectModifiedEvent.
 *
 * @version $Id: IObjectModifiedEvent.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class IObjectModifiedEvent
	implements Event
{
	private IObject modifiedObject;
	private ClientTransceiver clientTransceiver;

	public IObjectModifiedEvent (IObject modifiedObject, ClientTransceiver clientTransceiver)
	{
		this.modifiedObject = modifiedObject;
		this.clientTransceiver = clientTransceiver;
	}

	public IObjectModifiedEvent (IObject modifiedObject)
	{
		this(modifiedObject, null);
	}

	public IObject getModifiedObject ()
	{
		return modifiedObject;
	}

	public ClientTransceiver getClientTransceiver ()
	{
		return clientTransceiver;
	}
}