/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.user.User;
import java.io.IOException;


/**
 * ProxyServerAction
 *
 * @version $Id: ProxyServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ProxyServerAction
	extends NetworkFrameworkServerAction
{
	private long prototypeUniqueId;
	private long timestamp;
	private String prototypeId;

	public ProxyServerAction ()
	{
	}

	public ProxyServerAction (long prototypeUniqueId, long timestamp)
	{
		this.prototypeUniqueId = prototypeUniqueId;
		this.timestamp = timestamp;
		prototypeId = "Unknown";
	}

	public ProxyServerAction (long prototypeUniqueId, String prototypeId, long timestamp)
	{
		this.prototypeUniqueId = prototypeUniqueId;
		this.timestamp = timestamp;
		this.prototypeId = prototypeId;
	}

	public long getPrototypeUniqueId ()
	{
		return prototypeUniqueId;
	}

	public String getTypeId ()
	{
		return "server.action.proxy";
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		prototypeUniqueId = stream.readLong ();
		timestamp = stream.readLong ();
		prototypeId = stream.readUTF ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (prototypeUniqueId);
		stream.writeLong (timestamp);
		stream.writeUTF (prototypeId);
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		IObject prototype = null;

		if (prototypeUniqueId <= 0)
		{
			User user = (User) clientTransceiver.getConnectedChannel ().getCustomContextObject ();

			prototype =
				(IObject) Engine.instance ().getBaseRegistry ().get (
					user.getNewObjectsMapping (new Long(prototypeUniqueId)).longValue ());
		}
		else
		{
			prototype = (IObject) Engine.instance ().getBaseRegistry ().get (prototypeUniqueId);
		}

		if (prototype == null)
		{
			return null;
		}

		clientTransceiver.addReceiver (clientTransceiver.getSender ());

		return (FrameworkAction) new ProxyAction(prototypeUniqueId, 0, prototype);
	}
}