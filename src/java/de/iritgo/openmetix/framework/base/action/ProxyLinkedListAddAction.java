/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.framework.base.DataObject;
import java.io.IOException;


/**
 * ProxyLinkedListAddAction.
 *
 * @version $Id: ProxyLinkedListAddAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ProxyLinkedListAddAction
	extends FrameworkServerAction
{
	private long oldUniqueId;
	private long newUniqueId;
	private long parentUniqueId;

	public ProxyLinkedListAddAction ()
	{
	}

	public ProxyLinkedListAddAction (long oldUniqueId, long newUniqueId, long parentUniqueId)
	{
		this.oldUniqueId = oldUniqueId;
		this.newUniqueId = newUniqueId;
		this.parentUniqueId = parentUniqueId;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		oldUniqueId = stream.readLong ();
		newUniqueId = stream.readLong ();
		parentUniqueId = stream.readLong ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (oldUniqueId);
		stream.writeLong (newUniqueId);
		stream.writeLong (parentUniqueId);
	}

	public void perform ()
	{
		IObject parentObject =
			(DataObject) Engine.instance ().getBaseRegistry ().get (parentUniqueId);

		DataObject dataObject =
			(DataObject) Engine.instance ().getBaseRegistry ().get (oldUniqueId);

		DataObject exists = (DataObject) Engine.instance ().getBaseRegistry ().get (newUniqueId);

		if (exists != null)
		{
			Engine.instance ().getBaseRegistry ().remove (dataObject);

			IObjectProxy oldProxy =
				(IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (oldUniqueId);

			Engine.instance ().getProxyRegistry ().removeProxy (oldProxy);

			return;
		}

		Engine.instance ().getBaseRegistry ().remove (dataObject);

		dataObject.setUniqueId (newUniqueId);
		Engine.instance ().getBaseRegistry ().add (dataObject);

		IObjectProxy oldProxy =
			(IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (oldUniqueId);

		Engine.instance ().getProxyRegistry ().removeProxy (oldProxy);

		oldProxy.setRealObject (dataObject);
		Engine.instance ().getProxyRegistry ().addProxy (oldProxy);

		return;
	}
}