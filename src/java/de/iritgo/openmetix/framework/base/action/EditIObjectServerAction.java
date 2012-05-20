/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;


/**
 * EditIObjectServerAction.
 *
 * @version $Id: EditIObjectServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class EditIObjectServerAction
	extends FrameworkServerAction
{
	private IObject iObject;
	private long iObjectUniqueId;
	private String iObjectId;

	public EditIObjectServerAction ()
	{
	}

	public EditIObjectServerAction (IObject iObject)
	{
		this.iObject = iObject;
		iObjectUniqueId = iObject.getUniqueId ();
		iObjectId = iObject.getTypeId ();
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		IObject iObjectSample = null;

		try
		{
			iObjectUniqueId = stream.readLong ();
			iObjectId = stream.readUTF ();

			iObject = Engine.instance ().getIObjectFactory ().newInstance (iObjectId);

			iObject.readObject (stream);
		}
		catch (NoSuchIObjectException x)
		{
			Log.logError (
				"system", "EditIObjectServerAction.readObject",
				"No such protoptye registered " + x.getMessage ());
		}
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (iObject.getUniqueId ());
		stream.writeUTF (iObjectId);
		iObject.writeObject (stream);
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		if (iObjectUniqueId <= 0)
		{
			User user = (User) clientTransceiver.getConnectedChannel ().getCustomContextObject ();

			iObject.setUniqueId (
				((DataObject) Engine.instance ().getBaseRegistry ().get (
					user.getNewObjectsMapping (new Long(iObjectUniqueId)).longValue ())).getUniqueId ());
		}
		else
		{
			iObject.setUniqueId (iObjectUniqueId);
		}

		IObject updatediObject = assignCachedObjectToRealiObject ();

		Engine.instance ().getEventRegistry ().fire (
			"objectmodified", new IObjectModifiedEvent(updatediObject, clientTransceiver));

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.isOnline ())
			{
				clientTransceiver.addReceiver (user.getNetworkChannel ());
			}
		}

		EditIObjectAction editiObjectAction =
			new EditIObjectAction(EditIObjectAction.OK, iObject.getUniqueId ());

		editiObjectAction.setTransceiver (clientTransceiver);
		editiObjectAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (editiObjectAction);
	}

	public IObject assignCachedObjectToRealiObject ()
	{
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			iObject.writeObject (new DataOutputStream(buffer));

			IObject updatediObject =
				(IObject) Engine.instance ().getBaseRegistry ().get (iObject.getUniqueId ());

			updatediObject.readObject (
				new DataInputStream(new ByteArrayInputStream(buffer.toByteArray ())));

			return updatediObject;
		}
		catch (IOException x)
		{
		}
		catch (ClassNotFoundException x)
		{
		}

		return null;
	}
}