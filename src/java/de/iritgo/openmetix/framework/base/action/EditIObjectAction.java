/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * EditIObjectAction.
 *
 * @version $Id: EditIObjectAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class EditIObjectAction
	extends FrameworkAction
{
	public static int OK = 0;
	public static int ERROR = -1;
	private List iObjects;
	private int state;
	private long iObjectId;

	public EditIObjectAction ()
	{
		iObjects = new LinkedList();
	}

	public EditIObjectAction (int state, long iObjectId)
	{
		this();

		this.state = state;
		iObjects.add (new Long(iObjectId));
	}

	public EditIObjectAction (int state)
	{
		this();

		this.state = state;
	}

	public void addIObject (IObject iObject)
	{
		iObjects.add (new Long(iObject.getUniqueId ()));
	}

	public void addIObject (long uniqueId)
	{
		iObjects.add (new Long(uniqueId));
	}

	public void addIObject (Long uniqueId)
	{
		iObjects.add (uniqueId);
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		state = stream.readInt ();

		int size = stream.readInt ();

		for (int i = 0; i < size; ++i)
		{
			iObjects.add (new Long(stream.readLong ()));
		}
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeInt (state);
		stream.writeInt (iObjects.size ());

		for (Iterator i = iObjects.iterator (); i.hasNext ();)
		{
			stream.writeLong (((Long) i.next ()).longValue ());
		}
	}

	public void perform ()
	{
		for (Iterator i = iObjects.iterator (); i.hasNext ();)
		{
			IObjectProxy proxy =
				(IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (
					((Long) i.next ()).longValue ());

			if (proxy != null)
			{
				proxy.reset ();
			}
			else
			{
			}
		}
	}
}