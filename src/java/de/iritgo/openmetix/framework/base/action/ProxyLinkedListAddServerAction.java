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
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
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
 * ProxyLinkedListAddServerAction.
 *
 * @version $Id: ProxyLinkedListAddServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ProxyLinkedListAddServerAction
	extends FrameworkServerAction
{
	private IObject prototype;
	private long prototypeUniqueId;
	private long ownerUniqueId;
	private String prototypeId;
	private String proxyLinkedListId;

	public ProxyLinkedListAddServerAction ()
	{
	}

	public ProxyLinkedListAddServerAction (
		long ownerUniqueId, String proxyLinkedListId, IObject prototype)
	{
		this.prototype = prototype;
		this.ownerUniqueId = ownerUniqueId;
		this.proxyLinkedListId = proxyLinkedListId;
		prototypeId = prototype.getTypeId ();
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		try
		{
			ownerUniqueId = stream.readLong ();
			prototypeUniqueId = stream.readLong ();
			prototypeId = stream.readUTF ();
			proxyLinkedListId = stream.readUTF ();
			prototype = Engine.instance ().getIObjectFactory ().newInstance (prototypeId);
			prototype.readObject (stream);
		}
		catch (NoSuchIObjectException x)
		{
			Log.log (
				"network", "ProxyLinkedListAddServerAction.readObject",
				"DataObject not registred: " + prototypeId, Log.WARN);
		}
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (ownerUniqueId);
		stream.writeLong (prototype.getUniqueId ());
		stream.writeUTF (prototypeId);
		stream.writeUTF (proxyLinkedListId);
		prototype.writeObject (stream);
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		DataObject owner = null;

		if (ownerUniqueId <= 0)
		{
			User user = (User) clientTransceiver.getConnectedChannel ().getCustomContextObject ();

			owner =
				(DataObject) Engine.instance ().getBaseRegistry ().get (
					user.getNewObjectsMapping (new Long(ownerUniqueId)).longValue ());
		}
		else
		{
			owner = (DataObject) Engine.instance ().getBaseRegistry ().get (ownerUniqueId);
		}

		long newUniqueId = Engine.instance ().getPersistentIDGenerator ().createId ();
		long oldUniqueId = prototype.getUniqueId ();

		if (owner == null)
		{
			Log.logError ("system", "ProxyLinkedListAddServerAction.perform", "Owner is null");

			return;
		}

		IObjectList proxyLinkedList = (IObjectList) owner.getAttribute (proxyLinkedListId);

		if (proxyLinkedList == null)
		{
			Log.logError (
				"system", "ProxyLinkedListAddServerAction.perform", "Proxy linked list is null");

			return;
		}

		prototype.setUniqueId (newUniqueId);

		proxyLinkedList.add (prototype);

		((User) clientTransceiver.getConnectedChannel ().getCustomContextObject ()).putNewObjectsMapping (
			new Long(oldUniqueId), new Long(newUniqueId));

		ProxyLinkedListAddAction proxyLinkedListAction =
			new ProxyLinkedListAddAction(oldUniqueId, newUniqueId, owner.getUniqueId ());

		clientTransceiver.addReceiver (clientTransceiver.getSender ());
		proxyLinkedListAction.setTransceiver (clientTransceiver);
		proxyLinkedListAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (proxyLinkedListAction);

		clientTransceiver =
			new ClientTransceiver(
				clientTransceiver.getSender (), clientTransceiver.getConnectedChannel ());

		Engine.instance ().getEventRegistry ().fire (
			"objectcreated",
			new IObjectListEvent(
				prototype, owner, proxyLinkedListId, clientTransceiver, IObjectListEvent.ADD));

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.isOnline ())
			{
				clientTransceiver.addReceiver (user.getNetworkChannel ());
			}
		}

		EditIObjectAction editPrototypeAction =
			new EditIObjectAction(EditIObjectAction.OK, owner.getUniqueId ());

		editPrototypeAction.setTransceiver (clientTransceiver);
		editPrototypeAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (editPrototypeAction);
	}
}