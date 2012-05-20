/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.IObjectProxyEvent;
import de.iritgo.openmetix.framework.base.DataObject;
import java.io.IOException;


/**
 * ProxyLinkedListRemoveAction.
 *
 * @version $Id: ProxyLinkedListRemoveAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ProxyLinkedListRemoveAction
	extends FrameworkServerAction
{
	private long uniqueId;
	private long parentUniqueId;
	private String iObjectListName;

	public ProxyLinkedListRemoveAction ()
	{
	}

	public ProxyLinkedListRemoveAction (long uniqueId, long parentUniqueId, String iObjectListName)
	{
		this.uniqueId = uniqueId;
		this.parentUniqueId = parentUniqueId;
		this.iObjectListName = iObjectListName;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		uniqueId = stream.readLong ();
		parentUniqueId = stream.readLong ();
		iObjectListName = stream.readUTF ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (uniqueId);
		stream.writeLong (parentUniqueId);
		stream.writeUTF (iObjectListName);
	}

	public void perform ()
	{
		DataObject ownerObject =
			(DataObject) Engine.instance ().getBaseRegistry ().get (parentUniqueId);

		if (ownerObject == null)
		{
			return;
		}

		DataObject dataObject = (DataObject) Engine.instance ().getBaseRegistry ().get (uniqueId);

		IObjectList iObjectList = ownerObject.getIObjectListAttribute (iObjectListName);

		iObjectList.removeIObject (dataObject);

		Engine.instance ().getProxyEventRegistry ().fire (
			ownerObject, new IObjectProxyEvent(ownerObject, parentUniqueId, false));
		Engine.instance ().getEventRegistry ().fire (
			"proxyisuptodate", new IObjectProxyEvent(ownerObject, parentUniqueId, false));
	}
}