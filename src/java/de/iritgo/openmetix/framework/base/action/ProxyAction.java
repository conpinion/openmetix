/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * ProxyAction.
 *
 * @version $Id: ProxyAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ProxyAction
	extends FrameworkAction
{
	private long prototypeUniqueId;
	private long timestamp;
	private IObject prototype;

	public ProxyAction ()
	{
	}

	public ProxyAction (long prototypeUniqueId, long timestamp, IObject prototype)
	{
		this.prototypeUniqueId = prototypeUniqueId;
		this.timestamp = timestamp;
		this.prototype = prototype;
	}

	public String getTypeId ()
	{
		return "action.proxy";
	}

	public long getPrototypeUniqueId ()
	{
		return prototypeUniqueId;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		prototypeUniqueId = stream.readLong ();
		timestamp = stream.readLong ();

		if (prototypeUniqueId <= 0)
		{
			User user = AppContext.instance ().getUser ();
			Long newUniqueId = user.getNewObjectsMapping (new Long(prototypeUniqueId));

			if (newUniqueId != null)
			{
				prototypeUniqueId = newUniqueId.longValue ();
			}
		}

		try
		{
			IObject proto = (IObject) Engine.instance ().getBaseRegistry ().get (prototypeUniqueId);

			if (proto == null)
			{
				Log.logError ("system", "ProxyAction.readObject", "Unable to find the IObject");
			}
			else
			{
				prototype = (IObject) proto.getClass ().newInstance ();

				if (prototype == null)
				{
					Log.logError (
						"system", "ProxyAction.readObject",
						"Unable to create a new IObject instance");
				}
				else
				{
					prototype.readObject (stream);
				}
			}
		}
		catch (InstantiationException x)
		{
		}
		catch (IllegalAccessException x)
		{
		}
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (prototypeUniqueId);
		stream.writeLong (timestamp);
		prototype.writeObject (stream);
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		assignCachedObjectToProxyPrototype ();
	}

	public void assignCachedObjectToProxyPrototype ()
	{
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			prototype.writeObject (new DataOutputStream(buffer));

			IObjectProxy proxy =
				(IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (prototypeUniqueId);

			proxy.update (new DataInputStream(new ByteArrayInputStream(buffer.toByteArray ())));
		}
		catch (IOException x)
		{
			x.printStackTrace ();
		}
		catch (ClassNotFoundException x)
		{
			x.printStackTrace ();
		}
	}
}