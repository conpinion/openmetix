/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.IObjectListEvent;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;


/**
 * ProxyLinkedListRemoveServerAction.
 *
 * @version $Id: ProxyLinkedListRemoveServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ProxyLinkedListRemoveServerAction
	extends FrameworkServerAction
{
	private long ownerUniqueId;
	private long iObjectUniqueId;
	private String iObjectTypeId;
	private String iObjectListName;

	public ProxyLinkedListRemoveServerAction ()
	{
	}

	public ProxyLinkedListRemoveServerAction (
		long ownerUniqueId, String iObjectListName, IObject prototype)
	{
		this.ownerUniqueId = ownerUniqueId;
		this.iObjectListName = iObjectListName;
		iObjectTypeId = prototype.getTypeId ();
		iObjectUniqueId = prototype.getUniqueId ();
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		ownerUniqueId = stream.readLong ();
		iObjectUniqueId = stream.readLong ();
		iObjectTypeId = stream.readUTF ();
		iObjectListName = stream.readUTF ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (ownerUniqueId);
		stream.writeLong (iObjectUniqueId);
		stream.writeUTF (iObjectTypeId);
		stream.writeUTF (iObjectListName);
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		DataObject owner = null;
		User user = (User) clientTransceiver.getConnectedChannel ().getCustomContextObject ();

		if (ownerUniqueId <= 0)
		{
			owner =
				(DataObject) Engine.instance ().getBaseRegistry ().get (
					user.getNewObjectsMapping (new Long(ownerUniqueId)).longValue ());
		}
		else
		{
			owner = (DataObject) Engine.instance ().getBaseRegistry ().get (ownerUniqueId);
		}

		DataObject iObject = null;

		if (iObjectUniqueId <= 0)
		{
			iObject =
				(DataObject) Engine.instance ().getBaseRegistry ().get (
					user.getNewObjectsMapping (new Long(iObjectUniqueId)).longValue ());
		}
		else
		{
			iObject = (DataObject) Engine.instance ().getBaseRegistry ().get (iObjectUniqueId);
		}

		if ((owner == null) || (iObject == null))
		{
			Log.logError (
				"system", "ProxyLinkedListRemoveServerAction.perform", "Owner or object is null");

			return;
		}

		IObjectList iObjectList = (IObjectList) owner.getAttribute (iObjectListName);

		iObjectList.removeIObject (iObject);

		if (iObjectList == null)
		{
			Log.logError (
				"system", "ProxyLinkedListRemoveServerAction.perform", "Object list is null");

			return;
		}

		ProxyLinkedListRemoveAction proxyLinkedListRemoveAction =
			new ProxyLinkedListRemoveAction(iObject.getUniqueId (), ownerUniqueId, iObjectListName);

		proxyLinkedListRemoveAction.setUniqueId (getUniqueId ());

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User onlineUser = (User) i.next ();

			if (onlineUser.isOnline ())
			{
				clientTransceiver.addReceiver (onlineUser.getNetworkChannel ());
			}
		}

		proxyLinkedListRemoveAction.setTransceiver (clientTransceiver);
		ActionTools.sendToClient (proxyLinkedListRemoveAction);

		Engine.instance ().getEventRegistry ().fire (
			"objectremoved",
			new IObjectListEvent(
				iObject, owner, iObjectListName, clientTransceiver, IObjectListEvent.REMOVE));
	}
}